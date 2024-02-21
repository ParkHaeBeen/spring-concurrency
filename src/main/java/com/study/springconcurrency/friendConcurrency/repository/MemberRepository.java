package com.study.springconcurrency.friendConcurrency.repository;


import com.study.springconcurrency.friendConcurrency.domain.Member;
import com.study.springconcurrency.friendConcurrency.domain.MemberStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository <Member, Long> {

}
