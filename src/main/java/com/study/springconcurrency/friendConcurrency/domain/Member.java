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

  private String profileImageFileName;

  @Column (nullable = false, length = 100)
  private String name;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated (EnumType.STRING)
  private MemberStatus status;

  @Enumerated(EnumType.STRING)
  private MemberRole role;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MemberProvider provider;

  public Member modifyStatus(final MemberStatus status) {
    this.status = status;
    return this;
  }

  public Member modifyPassword(final String password) {
    this.password = password;
    return this;
  }

  public Member modifyProfileImageFileName(final String fileName) {
    this.profileImageFileName = fileName;
    return this;
  }
}