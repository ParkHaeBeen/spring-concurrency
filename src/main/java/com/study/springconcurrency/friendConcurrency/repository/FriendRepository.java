package com.study.springconcurrency.friendConcurrency.repository;


import com.study.springconcurrency.friendConcurrency.domain.Friend;
import com.study.springconcurrency.friendConcurrency.domain.FriendStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<Friend, Long>  {

  Optional<Friend> findByMemberRequestMemberIdAndFriendMemberIdAndStatus(Long memberRequestId,
      Long friend, FriendStatus friendStatus);

  @Query("SELECT friend FROM Friend friend  WHERE friend.memberRequest.memberId = :memberRequestId "
      + "AND friend.friend.memberId = :friendId AND friend.status IN (:statusList)")
  Optional<Friend> findFriendRequest(@Param("memberRequestId") Long memberRequestId,
      @Param("friendId") Long friendId, @Param("statusList") List <FriendStatus> statusList);

  Optional<Friend> findByMemberRequestMemberId(Long id);
  Optional<Friend> findByFriend_MemberId(Long id);

  @Query(value = "SELECT GET_LOCK(:key, 3000)", nativeQuery = true)
  Integer getLock(String key);

  @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
  Integer releaseLock(String key);
}
