package com.study.springconcurrency.friendConcurrency.controller;

import com.study.springconcurrency.friendConcurrency.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/friends")
@RequiredArgsConstructor
public class FriendController {
  private final FriendService friendService;

  @PostMapping ("/{memberId}")
  public ResponseEntity <Boolean> requestFriend(@RequestBody Long requestId
      ,@PathVariable final Long memberId){
    return ResponseEntity.ok(friendService.requestFriend(requestId,memberId));
  }

}
