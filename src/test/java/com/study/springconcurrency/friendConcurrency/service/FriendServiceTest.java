package com.study.springconcurrency.friendConcurrency.service;

import static org.junit.jupiter.api.Assertions.*;

import com.study.springconcurrency.friendConcurrency.domain.Friend;
import com.study.springconcurrency.friendConcurrency.repository.FriendRepository;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FriendServiceTest {

  @Autowired
  private FriendService friendService;
  
  @Autowired
  private FriendRepository friendRepository;
  
  @Test
  void test () throws InterruptedException {
    int numThreads = 1;

    CountDownLatch latch = new CountDownLatch(numThreads);
    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
    for (int i = 0; i < numThreads; i++) {
      executorService.execute(() -> {
        try {
          Long requestId = 1L;
          Long friendId = 2L;
          friendService.requestFriend(requestId,friendId);
          friendService.requestFriend(friendId,requestId);

        }catch (Exception e){
          System.out.println("error = "+e.getMessage());
        }finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    Optional <Friend> byMemberRequestMemberId = friendRepository.findByMemberRequestMemberId(1L);
    System.out.println("byMemberRequestMemberId = " + byMemberRequestMemberId);
    Optional <Friend> byMemberRequestMemberId1 = friendRepository.findByMemberRequestMemberId(2L);
    System.out.println("byMemberRequestMemberId1 = " + byMemberRequestMemberId1);
  }

  @Test
  void test2 () throws InterruptedException {
    int numThreads = 1;

    CountDownLatch latch = new CountDownLatch(numThreads);
    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
    for (int i = 0; i < numThreads; i++) {
      executorService.execute(() -> {
        try {
          Long requestId = 1L;
          Long friendId = 2L;
          friendService.requestFriendNamedLock(requestId,friendId);
          friendService.requestFriendNamedLock(friendId,requestId);

        }catch (Exception e){
          System.out.println("error = "+e.getMessage());
        }finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    Optional <Friend> byMemberRequestMemberId = friendRepository.findByMemberRequestMemberId(1L);
    System.out.println("byMemberRequestMemberId = " + byMemberRequestMemberId);
    Optional <Friend> byMemberRequestMemberId1 = friendRepository.findByMemberRequestMemberId(2L);
    System.out.println("byMemberRequestMemberId1 = " + byMemberRequestMemberId1);
  }
}