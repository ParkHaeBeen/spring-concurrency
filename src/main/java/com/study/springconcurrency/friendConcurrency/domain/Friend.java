package com.study.springconcurrency.friendConcurrency.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Friend   {

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  private FriendStatus status;

  @ManyToOne
  @JoinColumn (name = "member_req_id") // 변경된 부분
  private Member memberRequest;

  @ManyToOne
  @JoinColumn(name = "member_id") // 변경된 부분
  private Member friend;

  public void modifyFriendStatus(final FriendStatus friendStatus){
    this.status=friendStatus;
  }

}
