package com.intuit.craftdemoapps.api.intuitamigo.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RDeque;
import org.redisson.api.RFuture;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;
import com.intuit.craftdemoapps.api.intuitamigo.model.User;

@RunWith(MockitoJUnitRunner.class)
public class IntuitAmigoRedisRespositoryImplTest {

    private static final String USERS_PREFIX = "user:";
    private static final String USERNAME_USERID_MAP = "username:userid";
    private static final String POSTS_PREFIX = "post:";
    private static final String FEED_PREFIX = "feed:";
    private static final String TIMELINE_PREFIX = "timeline:";
    private static final String FOLLOWERS_PREFIX = "followers:";
    private static final String FOLLOWING_PREFIX = "followings:";
    public static final String USER_NAME = "Test123";
    public static final String USER_ID = "123";
    
    @Mock
    RAtomicLong userIdSequence;

    @Mock
    RAtomicLong postIdSequence;

    @Mock
    RedissonClient redissonClient;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    User user;

    @Mock
    ExecutorService executorService;

    @InjectMocks
    IntuitAmigoRedisRespositoryImpl repository;

    @Mock
    private RMap<String, String> userMap;

    @Mock
    private RMap<String, String> userNameMap;

    @Mock
    private Post post;

    @Mock
    private RMap<String, String> postMap;

    @Mock
    private RDeque<String> timeLine;

    @Mock
    private RScoredSortedSet<String> feed;

    @Mock
    private RScoredSortedSet<String> followers;

    @Mock
    private RScoredSortedSet<String> followings;

    @Mock
    private RFuture<Boolean> followersFuture;

    @Mock
    private RFuture<Boolean> followingsFuture;

    @Mock
    private RList<String> timeline;

    @Mock
    private RList<String> timeLinePage;

    @Mock
    private Collection<ScoredEntry<String>> entryRange;

    @Before
    public void setUp() {
        initMocks(this);
        //executorService = Executors.newFixedThreadPool(100,
        //	new ThreadFactoryBuilder().setNameFormat("intuit-amigo-repo-thread-").build());
        //repository = new IntuitAmigoRedisRespositoryImpl(redissonClient, passwordEncoder, executorService);
    }

    @Test
    public void testCreateUser() {
        when(redissonClient.getAtomicLong("userIdSequence")).thenReturn(userIdSequence);
        when(redissonClient.getAtomicLong("postIdSequence")).thenReturn(postIdSequence);
        doReturn(userMap).when(redissonClient).getMap(USERS_PREFIX + USER_ID);
        doReturn(userNameMap).when(redissonClient).getMap(USERNAME_USERID_MAP);

        when(userIdSequence.incrementAndGet()).thenReturn(123L);

        Date date = Mockito.mock(Date.class);
        when(date.getTime()).thenReturn(1L);

        when(user.getId()).thenReturn("123");
        when(user.getName()).thenReturn("Test");
        when(user.getUsername()).thenReturn(USER_NAME);
        when(user.getMemberSince()).thenReturn(date);
        when(user.getPassword()).thenReturn("PassWord");

        when(passwordEncoder.encode("PassWord")).thenReturn("PXYZ");

        User createdUser = repository.createUser(this.user);

        assertNotNull(createdUser);
        assertEquals("123", createdUser.getId());
        assertEquals(date, createdUser.getMemberSince());
        verify(userIdSequence, times(1)).incrementAndGet();
        verify(passwordEncoder, times(1)).encode("PassWord");
        verify(redissonClient).getMap(USERS_PREFIX + createdUser.getId());
        verify(redissonClient, times(1)).getMap(USERNAME_USERID_MAP);
    }

    @Test
    public void testGetUserWhenUserIsNotPresentReturnsNull() {
        doReturn(userNameMap).when(redissonClient).getMap(USERNAME_USERID_MAP);
        when(userNameMap.containsKey(USER_NAME)).thenReturn(false);

        assertNull(repository.getUser(USER_NAME));
        verify(redissonClient, times(1)).getMap(USERNAME_USERID_MAP);
        verify(redissonClient, times(0)).getMap(USERS_PREFIX + USER_ID);
    }

    @Test
    public void testGetUserWhenUserIsPresentReturnsUser() {
        doReturn(userNameMap).when(redissonClient).getMap(USERNAME_USERID_MAP);
        when(userNameMap.containsKey(USER_NAME)).thenReturn(true);
        when(userNameMap.get(USER_NAME)).thenReturn(USER_ID);
        when(userMap.get("memberSince")).thenReturn("12");
        doReturn(userMap).when(redissonClient).getMap(USERS_PREFIX + USER_ID);

        User retrievedUser = repository.getUser(USER_NAME);
        assertNotNull(retrievedUser);
        verify(redissonClient).getMap(USERS_PREFIX + USER_ID);
        verify(redissonClient, times(2)).getMap(USERNAME_USERID_MAP);
    }

   /* @Test
    public void testGetUserAsync() {
        fail("Not yet implemented");
    }*/

    @Test
    public void testCreatePost() {
        when(postIdSequence.incrementAndGet()).thenReturn(123L);
        when(post.getId()).thenReturn("789");
        Date date = Mockito.mock(Date.class);
        when(post.getPostedAt()).thenReturn(date);
        when(post.getText()).thenReturn("New Post");
        when(post.getAuthor()).thenReturn(USER_NAME);
        when(date.getTime()).thenReturn(999L);
        doReturn(postMap).when(redissonClient).getMap(POSTS_PREFIX + USER_ID);
        doReturn(timeLine).when(redissonClient).getDeque(TIMELINE_PREFIX + USER_NAME);
        doReturn(feed).when(redissonClient).getScoredSortedSet(FEED_PREFIX + USER_NAME);
        doReturn(followers).when(redissonClient).getScoredSortedSet(FOLLOWERS_PREFIX + USER_NAME);

        Post createdPost = repository.createPost(USER_NAME, post);
        assertNotNull(createdPost);
        assertEquals("789", createdPost.getId());
        assertEquals(USER_NAME, createdPost.getAuthor());
        assertEquals(date, createdPost.getPostedAt());
        verify(postIdSequence).incrementAndGet();
        verify(redissonClient, times(1)).getMap(POSTS_PREFIX + USER_ID);
        verify(redissonClient, times(1)).getDeque(TIMELINE_PREFIX + USER_NAME);
        verify(redissonClient, times(1)).getScoredSortedSet(FEED_PREFIX + USER_NAME);
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWERS_PREFIX + USER_NAME);
    }


    @Test
    public void testUpdatePost() {
        doReturn(postMap).when(redissonClient).getMap(POSTS_PREFIX + "789");
        when(post.getId()).thenReturn("789");
        Date date = Mockito.mock(Date.class);
        when(post.getPostedAt()).thenReturn(date);
        when(post.getText()).thenReturn("New Post");
        when(post.getAuthor()).thenReturn(USER_NAME);
        when(date.getTime()).thenReturn(999L);
        Post updatedPost = repository.updatePost("789", post);
        assertNotNull(updatedPost);
        assertEquals("789", updatedPost.getId());
        assertEquals(USER_NAME, updatedPost.getAuthor());
        assertEquals(date, updatedPost.getPostedAt());
        verify(redissonClient, times(1)).getMap(POSTS_PREFIX + "789");
    }


    @Test
    public void testDeletePost() {
        doReturn(postMap).when(redissonClient).getMap(POSTS_PREFIX + "789");
        doReturn(timeLine).when(redissonClient).getDeque(TIMELINE_PREFIX + USER_NAME);
        doReturn(feed).when(redissonClient).getScoredSortedSet(FEED_PREFIX + USER_NAME);
        doReturn(followers).when(redissonClient).getScoredSortedSet(FOLLOWERS_PREFIX + USER_NAME);
        when(postMap.get("postedAt")).thenReturn("444");

        repository.deletePost(USER_NAME, "789");
        verify(redissonClient, times(1)).getMap(POSTS_PREFIX + "789");
        verify(redissonClient, times(1)).getDeque(TIMELINE_PREFIX + USER_NAME);
        verify(redissonClient, times(1)).getScoredSortedSet(FEED_PREFIX + USER_NAME);
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWERS_PREFIX + USER_NAME);
    }


    @Test
    public void testFollow() {
        doReturn(followers).when(redissonClient).getScoredSortedSet(FOLLOWERS_PREFIX + "xyz");
        doReturn(followings).when(redissonClient).getScoredSortedSet(FOLLOWING_PREFIX + "abc");
        doReturn(timeLine).when(redissonClient).getDeque(TIMELINE_PREFIX + "xyz");
        doReturn(feed).when(redissonClient).getScoredSortedSet(FEED_PREFIX + "abc");
        when(followers.addAsync(isA(Double.class), isA(String.class))).thenReturn(followersFuture);
        when(followings.addAsync(isA(Double.class), isA(String.class))).thenReturn(followingsFuture);
        when(followersFuture.isDone()).thenReturn(true);
        when(followingsFuture.isDone()).thenReturn(true);
        when(timeLine.parallelStream()).thenReturn(Stream.empty());

        repository.follow("abc", "xyz");
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWERS_PREFIX + "xyz");
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWING_PREFIX + "abc");
        verify(redissonClient, times(1)).getDeque(TIMELINE_PREFIX + "xyz");
        verify(redissonClient, times(1)).getScoredSortedSet(FEED_PREFIX + "abc");
    }

    @Test
    public void testUnfollow() {
        doReturn(followers).when(redissonClient).getScoredSortedSet(FOLLOWERS_PREFIX + "xyz");
        doReturn(followings).when(redissonClient).getScoredSortedSet(FOLLOWING_PREFIX + "abc");
        doReturn(timeLine).when(redissonClient).getDeque(TIMELINE_PREFIX + "xyz");
        doReturn(feed).when(redissonClient).getScoredSortedSet(FEED_PREFIX + "abc");
        when(followers.removeAsync("abc")).thenReturn(followersFuture);
        when(followings.removeAsync("xyz")).thenReturn(followingsFuture);
        when(followersFuture.isDone()).thenReturn(true);
        when(followingsFuture.isDone()).thenReturn(true);
        when(timeLine.parallelStream()).thenReturn(Stream.empty());

        repository.unfollow("abc", "xyz");
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWERS_PREFIX + "xyz");
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWING_PREFIX + "abc");
        verify(redissonClient, times(1)).getDeque(TIMELINE_PREFIX + "xyz");
        verify(redissonClient, times(1)).getScoredSortedSet(FEED_PREFIX + "abc");
    }

    @Test
    public void testGetTimeline() {
        doReturn(timeline).when(redissonClient).getList(TIMELINE_PREFIX + USER_NAME);
        when(timeline.size()).thenReturn(1);
        when(timeline.subList(1, 1)).thenReturn(timeLinePage);
        when(timeLinePage.stream()).thenReturn(Stream.empty());

        Timeline processedTimeline = repository.getTimeline(USER_NAME, 1, 10);
        assertNotNull(processedTimeline);
        assertEquals(1, processedTimeline.getIndex().intValue());
        assertEquals(10, processedTimeline.getLimit().intValue());
        assertEquals(USER_NAME, processedTimeline.getUsername());
        verify(redissonClient, times(1)).getList(TIMELINE_PREFIX + USER_NAME);
    }

    @Test
    public void testGetFeed() {
        doReturn(feed).when(redissonClient).getScoredSortedSet(FEED_PREFIX + USER_NAME);
        when(feed.entryRangeReversed(1, 1)).thenReturn(entryRange);
        when(entryRange.size()).thenReturn(5);
        when(entryRange.stream()).thenReturn(Stream.empty());

        Feed processedFeed = repository.getFeed(USER_NAME, 1, 2);
        assertNotNull(processedFeed);
        assertEquals(1, processedFeed.getIndex().intValue());
        assertEquals(2, processedFeed.getLimit().intValue());
        assertEquals(USER_NAME, processedFeed.getUsername());
        verify(redissonClient, times(1)).getScoredSortedSet(FEED_PREFIX + USER_NAME);
    }

    @Test
    public void testGetPost() {
        doReturn(postMap).when(redissonClient).getMap(POSTS_PREFIX + "789");
        when(postMap.get("id")).thenReturn("789");
        when(postMap.get("text")).thenReturn("new post");
        when(postMap.get("author")).thenReturn("AK");
        when(postMap.get("postedAt")).thenReturn("999");

        Post retrievedPost = repository.getPost("789");
        assertNotNull(retrievedPost);
        assertEquals("789", retrievedPost.getId());
        assertEquals("new post", retrievedPost.getText());
        assertEquals("AK", retrievedPost.getAuthor());
        verify(redissonClient, times(1)).getMap(POSTS_PREFIX + "789");
    }

    @Test
    public void testGetFollowers() {
        doReturn(followers).when(redissonClient).getScoredSortedSet(FOLLOWERS_PREFIX + USER_NAME);
        when(followers.size()).thenReturn(1);

        repository.getFollowers(USER_NAME);
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWERS_PREFIX + USER_NAME);
    }

    @Test
    public void testGetFollowing() {
        doReturn(followings).when(redissonClient).getScoredSortedSet(FOLLOWING_PREFIX + USER_NAME);
        when(followings.size()).thenReturn(1);

        repository.getFollowing(USER_NAME);
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWING_PREFIX + USER_NAME);
    }

    @Test
    public void testGetNumOfFollowers() {
        doReturn(followers).when(redissonClient).getScoredSortedSet(FOLLOWERS_PREFIX + USER_NAME);
        when(followers.size()).thenReturn(10);

        Integer numOfFollowers = repository.getNumOfFollowers(USER_NAME);
        assertEquals(10, numOfFollowers.intValue());
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWERS_PREFIX + USER_NAME);
    }


    @Test
    public void testGetNumOfFollowing() {
        doReturn(followings).when(redissonClient).getScoredSortedSet(FOLLOWING_PREFIX + USER_NAME);
        when(followings.size()).thenReturn(5);

        Integer numOfFollowers = repository.getNumOfFollowing(USER_NAME);
        assertEquals(5, numOfFollowers.intValue());
        verify(redissonClient, times(1)).getScoredSortedSet(FOLLOWING_PREFIX + USER_NAME);
    }


    /*
    @Test
    public void testGetPostAsync() {
        fail("Not yet implemented");
    }*/

}
