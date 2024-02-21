package com.study.springconcurrency.friendConcurrency.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member  {

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long memberId;

  @Column (nullable = false, length = 100)
  private String name;

  @Column(nullable = false)
  private String password;

  @Enumerated (EnumType.STRING)
  private MemberStatus status;

  @Enumerated(EnumType.STRING)
  private MemberRole role;

}