package com.intuit.craftdemoapps.api.intuitamigo.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RDeque;
import org.redisson.api.RFuture;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.intuit.craftdemoapps.api.intuitamigo.dao.IntuitAmigoRepository;
import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;
import com.intuit.craftdemoapps.api.intuitamigo.model.User;


@Component
public class IntuitAmigoRedisRespositoryImpl implements IntuitAmigoRepository {

	private static final String USERS_PREFIX = "user:";

	private static final String USERNAME_USERID_MAP = "username:userid";

	private static final String FOLLOWERS_PREFIX = "followers:";

	private static final String FOLLOWING_PREFIX = "followings:";

	private static final String POSTS_PREFIX = "post:";

	private static final String FEED_PREFIX = "feed:";

	private static final String TIMELINE_PREFIX = "timeline:";

	private RedissonClient redissonClient;
	
	private PasswordEncoder passwordEncoder;
	
	private ExecutorService executorService;

	private RAtomicLong userIdSequence;
	private RAtomicLong postIdSequence;

	@Autowired
	public IntuitAmigoRedisRespositoryImpl(RedissonClient redissonClient, PasswordEncoder passwordEncoder, ExecutorService executorService) {
		this.redissonClient = redissonClient;
		this.passwordEncoder = passwordEncoder;
		this.userIdSequence = redissonClient.getAtomicLong("userIdSequence");
		this.postIdSequence = redissonClient.getAtomicLong("postIdSequence");
		this.executorService = executorService;
	}

	@Override
	public User createUser(User user) {
		String userId = String.valueOf(userIdSequence.incrementAndGet());
		user.setId(userId);
		user.setMemberSince(new Date());

		RMap<String, String> userMap = redissonClient.getMap(USERS_PREFIX + userId);
		userMap.put("id", user.getId());
		userMap.put("name", user.getName());
		userMap.put("username", user.getUsername());
		userMap.put("memberSince", String.valueOf(user.getMemberSince().getTime()));
		userMap.put("password", passwordEncoder.encode(user.getPassword()));

		RMap<String, String> userNameMap = redissonClient.getMap(USERNAME_USERID_MAP);
		userNameMap.put(user.getUsername(), userId);

		return user;
	}

	@Override
	public User getUser(String username) {
		
		if(!isUserPresent(username)) {
			return null;
		}
		
		RMap<String, String> userNameMap = redissonClient.getMap(USERNAME_USERID_MAP);

		RMap<String, String> userMap = redissonClient.getMap(USERS_PREFIX + userNameMap.get(username));

		User user = new User();
		user.setId(userMap.get("id"));
		user.setName(userMap.get("name"));
		user.setUsername(userMap.get("username"));
		user.setPassword(userMap.get("password"));
		user.setMemberSince(new Date(Long.parseLong(userMap.get("memberSince"))));
		return user;
	}
	
	@Override
	public Future<User> getUserAsync(String username) {
		Callable<User> callableObj = new Callable<User>() {
			@Override
			public User call() throws Exception {
				User user = getUser(username);
				user.setPassword(null);
				user.setFollowers(getNumOfFollowers(username));
				user.setFollowing(getNumOfFollowing(username));
				return user;
			}
		};
		return executorService.submit(callableObj);
	}

	@Override
	public boolean isUserPresent(String username) {
		return redissonClient.getMap(USERNAME_USERID_MAP).containsKey(username);
	}

	@Override
	public Post createPost(String username, Post post) {
		String postId = String.valueOf(postIdSequence.incrementAndGet());

		Date now = new Date();

		post.setId(postId);
		post.setAuthor(username);
		post.setPostedAt(now);

		writePost(post, postId);
		
		publishPost(username, postId, now.getTime());

		return post;
	}

	private RMap<String, String> writePost(Post post, String postId) {
		RMap<String, String> postMap = redissonClient.getMap(POSTS_PREFIX + postId);
		postMap.put("id", post.getId());
		postMap.put("text", post.getText());
		postMap.put("author", post.getAuthor());
		postMap.put("postedAt", String.valueOf(post.getPostedAt().getTime()));
		return postMap;
	}
	
	
	@Override
	public Post updatePost(String postId, Post post) {
		writePost(post, postId);
		return post;
	}

	private void publishPost(String username, String postId, Long postedAt) {

		// add to timeline
		RDeque<String> timeline = redissonClient.getDeque(TIMELINE_PREFIX + username);
		timeline.addFirst(postId);

		// add to self feed
		RScoredSortedSet<String> feed = redissonClient.getScoredSortedSet(FEED_PREFIX + username);
		feed.add(postedAt, postId);

		// add to all follower's feed
		RScoredSortedSet<String> followers = redissonClient.getScoredSortedSet(FOLLOWERS_PREFIX + username);
		
		followers.forEach((follower) -> {
			RScoredSortedSet<String> followerFeed = redissonClient.getScoredSortedSet(FEED_PREFIX + follower);
			followerFeed.addAsync(postedAt, postId);
		});
	}

	@Override
	public void deletePost(String username, String postId) {

		RMap<String, String> postMap = redissonClient.getMap(POSTS_PREFIX + postId);
		unpublishPost(username, postId, Long.valueOf(postMap.get("postedAt")));
		postMap.delete();
	}

	private void unpublishPost(String username, String postId, Long postedAt) {

		// remove from timeline
		RDeque<String> timeline = redissonClient.getDeque(TIMELINE_PREFIX + username);
		timeline.removeAsync(postId);

		// remove from self feed
		RScoredSortedSet<String> feed = redissonClient.getScoredSortedSet(FEED_PREFIX + username);
		feed.remove(postId);

		// get followers
		RScoredSortedSet<String> followers = redissonClient.getScoredSortedSet(FOLLOWERS_PREFIX + username);

		//remove from follower's feed
		followers.forEach((follower) -> {
			RScoredSortedSet<String> followerFeed = redissonClient.getScoredSortedSet(FEED_PREFIX + follower);
			followerFeed.removeAsync(postId);
		});
	}

	@Override
	public void follow(String follower, String following) {

		Long followingSince = new Date().getTime();

		RScoredSortedSet<String> followers = redissonClient.getScoredSortedSet(FOLLOWERS_PREFIX + following);
		RFuture<Boolean> followersFuture = followers.addAsync(followingSince, follower);
		RScoredSortedSet<String> followings = redissonClient.getScoredSortedSet(FOLLOWING_PREFIX + follower);
		RFuture<Boolean> followingsFuture = followings.addAsync(followingSince, following);

		while (!(followersFuture.isDone() && followingsFuture.isDone())) {
			continue;
		}

		// get following's timeline
		RDeque<String> timeline = redissonClient.getDeque(TIMELINE_PREFIX + following);

		// get follower's feed
		RScoredSortedSet<String> feed = redissonClient.getScoredSortedSet(FEED_PREFIX + follower);

		// add posts to follower's feed
		timeline.parallelStream().forEach(e -> {
			Post post = getPost(e);
			feed.addAsync(post.getPostedAt().getTime(), e);
		});
	}

	@Override
	public void unfollow(String follower, String following) {

		RScoredSortedSet<String> followers = redissonClient.getScoredSortedSet(FOLLOWERS_PREFIX + following);
		RFuture<Boolean> followersFuture = followers.removeAsync(follower);
		RScoredSortedSet<String> followings = redissonClient.getScoredSortedSet(FOLLOWING_PREFIX + follower);
		RFuture<Boolean> followingsFuture = followings.removeAsync(following);

		while (!(followersFuture.isDone() && followingsFuture.isDone())) {
			continue;
		}

		// get following's timeline
		RDeque<String> timeline = redissonClient.getDeque(TIMELINE_PREFIX + following);

		// get follower's feed
		RScoredSortedSet<String> feed = redissonClient.getScoredSortedSet(FEED_PREFIX + follower);

		// remove posts from follower's feed
		timeline.parallelStream().forEach(e -> {
			feed.removeAsync(e);
		});
	}

	@Override
	public Timeline getTimeline(String username, Integer index, Integer limit) {
		
		RList<String> timeline = redissonClient.getList(TIMELINE_PREFIX + username);
		RList<String> timelinePage = timeline.subList(index, Math.min(timeline.size(), limit));
		List<Future<Post>> postFutures = new ArrayList<>(timelinePage.size());
		List<Post> posts = new ArrayList<>(timelinePage.size());
		
		timelinePage.stream()
		.forEach(pid -> {
			postFutures.add(getPostAsync(pid));
		});
		
		for(Future<Post> future : postFutures) {
			while(!future.isDone()) {
				continue;
			}
			try {
				posts.add(future.get());
			} catch (InterruptedException | ExecutionException e) {				
				e.printStackTrace();
			}
		}
		
		Timeline processedTimeline = new Timeline();
		processedTimeline.setIndex(index);
		processedTimeline.setLimit(limit);
		processedTimeline.setPosts(posts);
		processedTimeline.setUsername(username);
		processedTimeline.setRefreshedAt(new Date());
		return processedTimeline;
	}

	@Override
	public Feed getFeed(String username, Integer index, Integer limit) {

		RScoredSortedSet<String> feed = redissonClient.getScoredSortedSet(FEED_PREFIX + username);
		Collection<ScoredEntry<String>> entryRange = feed.entryRangeReversed(index, limit - 1);
		List<Post> posts = new ArrayList<>(entryRange.size());
		
		entryRange.stream()
		.forEach(postEntry -> {
			posts.add(getPost(postEntry.getValue()));
		});
		
		Feed processedFeed = new Feed();
		processedFeed.setIndex(index);
		processedFeed.setLimit(limit);
		processedFeed.setPosts(posts);
		processedFeed.setUsername(username);
		processedFeed.setRefreshedAt(new Date());
		return processedFeed;
	}

	@Override
	public Post getPost(String postId) {

		Post post = new Post();

		RMap<String, String> postMap = redissonClient.getMap(POSTS_PREFIX + postId);
		post.setId(postMap.get("id"));
		post.setText(postMap.get("text"));
		post.setAuthor(postMap.get("author"));
		post.setPostedAt(new Date(Long.valueOf(postMap.get("postedAt"))));

		return post;

	}
	
	@Override
	public Future<Post> getPostAsync(String postId) {
		
		Callable<Post> callableObj = new Callable<Post>() {
			@Override
			public Post call() throws Exception {
				return getPost(postId);
			}
		};
		return executorService.submit(callableObj);
	}
	

	@Override
	public List<User> getFollowers(String username) {
		
		RScoredSortedSet<String> followers = redissonClient.getScoredSortedSet(FOLLOWERS_PREFIX + username);
		return getUsersAsync(followers);
	}

	@Override
	public List<User> getFollowing(String username) {
		RScoredSortedSet<String> following = redissonClient.getScoredSortedSet(FOLLOWING_PREFIX + username);
		return getUsersAsync(following);
	}

	private List<User> getUsersAsync(RScoredSortedSet<String> usernames) {
		List<User> users = new ArrayList<>(usernames.size());
		List<Future<User>> userFutures = new ArrayList<>(usernames.size());

		usernames.forEach(follow -> {
			userFutures.add(getUserAsync(follow));
		});
		
		for(Future<User> future : userFutures) {
			while(!future.isDone()) {
				continue;
			}
			try {
				users.add(future.get());
			} catch (InterruptedException | ExecutionException e) {				
				e.printStackTrace();
			}
		}
		return users;
	}

	@Override
	public Integer getNumOfFollowers(String username) {
		RScoredSortedSet<String> followers = redissonClient.getScoredSortedSet(FOLLOWERS_PREFIX + username);
		return followers.size();		
	}

	@Override
	public Integer getNumOfFollowing(String username) {
		RScoredSortedSet<String> followings = redissonClient.getScoredSortedSet(FOLLOWING_PREFIX + username);
		return followings.size();
	}

}
