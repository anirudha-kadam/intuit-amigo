//package com.intuit.craftdemoapps.api.intuitamigo.dao.impl;
//
//import static org.junit.Assert.*;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoRule;
//import org.redisson.api.RedissonClient;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import com.intuit.craftdemoapps.api.intuitamigo.dao.IntuitAmigoRepository;
//import com.intuit.craftdemoapps.api.intuitamigo.fixtures.UserFixture;
//import com.intuit.craftdemoapps.api.intuitamigo.model.User;
//
//public class IntuitAmigoRedisRespositoryImplTest {
//	
//	@Mock
//	RedissonClient redissonClient;
//	
//	@Mock
//	PasswordEncoder passwordEncoder;
//	
//	ExecutorService executorService;
//	
//	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
//	
//	IntuitAmigoRepository repository;
//	
//	@Before
//	public void setup() {
//		executorService = Executors.newFixedThreadPool(100,
//				new ThreadFactoryBuilder().setNameFormat("intuit-amigo-repo-thread-").build());
//		repository = new IntuitAmigoRedisRespositoryImpl(redissonClient, passwordEncoder, executorService);
//	}
//
//	@Test
//	public void testIntuitAmigoRedisRespositoryImpl() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCreateUser() {
//		User createUserReq = UserFixture.getUser();
//		
//		//when(redissonClient.getMap(anyString())).thenReturn(value)
//		User createdUser = repository.createUser(createUserReq);
//		
//        assertNotNull(createdUser);
//        assertNotNull(createdUser.getId());
//         
//        verify(redissonClient, times(1)).getMap("user:"+createdUser.getId());
//        verify(redissonClient, times(1)).getMap("username:userid");
//        //verify(networkMock, times(1)).save("temp.txt");
//	}
//
//	@Test
//	public void testGetUser() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetUserAsync() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testIsUserPresent() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCreatePost() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdatePost() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeletePost() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testFollow() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUnfollow() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetTimeline() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFeed() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPost() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPostAsync() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFollowers() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFollowing() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetNumOfFollowers() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetNumOfFollowing() {
//		fail("Not yet implemented");
//	}
//
//}
