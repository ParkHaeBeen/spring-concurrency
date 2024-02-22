package com.study.springconcurrency.friendConcurrency.service;


import static com.study.springconcurrency.friendConcurrency.domain.FriendStatus.FRIEND_ACCEPTED;
import static com.study.springconcurrency.friendConcurrency.domain.FriendStatus.FRIEND_CHECKING;
import static com.study.springconcurrency.friendConcurrency.domain.FriendStatus.FRIEND_DELETE;
import static com.study.springconcurrency.friendConcurrency.domain.FriendStatus.FRIEND_REFUSED;

import com.study.springconcurrency.friendConcurrency.aop.DistributedLock;
import com.study.springconcurrency.friendConcurrency.domain.Friend;
import com.study.springconcurrency.friendConcurrency.domain.Member;
import com.study.springconcurrency.friendConcurrency.repository.FriendRepository;
import com.study.springconcurrency.friendConcurrency.repository.MemberRepository;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {

  private final FriendRepository friendRepository;
  private final MemberRepository memberRepository;

  @DistributedLock (key = "RequestFriend")
  public boolean requestFriend(final Long requestId, final Long friendId) {
    final Member requestMember = memberRepository.findById(requestId).get();
    final Member friend = memberRepository.findById(friendId).get();
    log.info("requestMember = {}",requestMember);
    log.info("friend = {}",friend);
    //이미 친구인지, 친구 요청을 받는 쪽에서 이미 요청을 했는지 확인(친구 요청 유효성 검사)
    validateFriendRequest(requestMember, friend);

    //처음 친구 요청이면 insert하고 친구 삭제,거절후 또 요청한거면 update 실행
    final Optional<Friend> friendRequest = friendRepository.findFriendRequest(
        requestMember.getMemberId(),
        friend.getMemberId(), Arrays.asList(FRIEND_REFUSED, FRIEND_DELETE));
    log.info("request = {}",friendRequest);
    if (friendRequest.isPresent()) {
      final Friend friendPreRequest = friendRequest.get();
      friendPreRequest.modifyFriendStatus(FRIEND_CHECKING);
    } else {
      final Friend friendFirstRequest = Friend.builder()
          .memberRequest(requestMember)
          .friend(friend)
          .status(FRIEND_CHECKING)
          .build();
      friendRepository.save(friendFirstRequest);
    }

    return true;
  }

  private void validateFriendRequest(final Member requestMember, final Member friend) {
    //요청 A, 요청받은 B
    //A -> B 가 이미 친구 인지
    final boolean friendExist = friendRepository.findByMemberRequestMemberIdAndFriendMemberIdAndStatus(
        requestMember.getMemberId(), friend.getMemberId(), FRIEND_ACCEPTED).isPresent();
    System.out.println("friendExist = " + friendExist);
    if (friendExist) {
      throw new RuntimeException();
    }

    //B -> A 가 이미 요청한 기록이 있는지
    System.out.println("requestMember = " + requestMember.getMemberId()+" "+friend.getMemberId());
    final boolean friendRequestExist = friendRepository.findByMemberRequestMemberIdAndFriendMemberIdAndStatus(
        friend.getMemberId(),
        requestMember.getMemberId(), FRIEND_CHECKING).isPresent();
    System.out.println("friendRequestExist = " + friendRequestExist);
    if (friendRequestExist) {
      throw new RuntimeException();
    }
  }

  @Transactional
  public void getLock(Long requestId, Long friendId){
    String key = Math.max(requestId,friendId)+" "+Math.min(requestId,friendId);
    long startTime = 0L;
    try {
      startTime = System.currentTimeMillis();

      Integer acquiredLock = friendRepository.getLock(key);
      if (acquiredLock != 1) {
        throw new RuntimeException("Lock 획득에 실패했습니다. [id: %d]".formatted(key));
      }
      log.info("lock= {}",acquiredLock);
      requestFriendNamedLock(requestId,friendId);
    } finally {
      friendRepository.releaseLock(key);
      log.info("락 놓음");
      long endTime = System.currentTimeMillis();
      long elapsedTime = endTime - startTime;
      log.info("lock 획득과 놓기 named= {}",elapsedTime);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Boolean requestFriendNamedLock(Long requestId, Long friendId){
    final Member requestMember = memberRepository.findById(requestId).get();
    final Member friend = memberRepository.findById(friendId).get();
    log.info("requestMember = {}",requestMember);
    log.info("friend = {}",friend);
    //이미 친구인지, 친구 요청을 받는 쪽에서 이미 요청을 했는지 확인(친구 요청 유효성 검사)
    validateFriendRequest(requestMember, friend);

    //처음 친구 요청이면 insert하고 친구 삭제,거절후 또 요청한거면 update 실행
    final Optional<Friend> friendRequest = friendRepository.findFriendRequest(
        requestMember.getMemberId(),
        friend.getMemberId(), Arrays.asList(FRIEND_REFUSED, FRIEND_DELETE));
    log.info("request = {}",friendRequest);
    if (friendRequest.isPresent()) {
      final Friend friendPreRequest = friendRequest.get();
      friendPreRequest.modifyFriendStatus(FRIEND_CHECKING);
      friendRepository.saveAndFlush(friendPreRequest);

    } else {
      final Friend friendFirstRequest = Friend.builder()
          .memberRequest(requestMember)
          .friend(friend)
          .status(FRIEND_CHECKING)
          .build();
      friendRepository.saveAndFlush(friendFirstRequest);
    }

    return true;
  }

}
