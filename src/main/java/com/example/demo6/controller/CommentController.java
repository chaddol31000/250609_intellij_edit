package com.example.demo6.controller;

import com.example.demo6.dto.*;
import com.example.demo6.entity.Comment;
import com.example.demo6.service.*;
import io.swagger.v3.oas.annotations.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.List;

// @Validated 가 있으면 스프링 검증 없으면 자바 검증?
@Validated
@RestController
public class CommentController {
  // 댓글 CRUD : Create, Delete 담당. Read 는 PostController 에서 Update 는 없다
  @Autowired
  private CommentService service;

  // write 나 delete 의 경우 갱신을 새로 해야하기에 List Comment 를 불러와야함

  // @RequestBody
  // @ResponseBody + HTTP 상태 코드 → ResponseEntity
  // 아래 처럼 적으면 @ModelAttribute 생략이 가능함 (백은 @ModelAttribute 가 기본임)
  // 회의를 통해 @ModelAttribute 로 갈 지 @RequestBody 로 갈 지 정해야함
  // 아래는 코드는 @ModelAttirbute 로 적음
  // 로그인 했다는 가정을 해야하기에 @Secured 또는 @PreAuthorize("isAuthenticated()") 를 사용해야함
  // 인증 (authentication) : 신원을 확인(로그인) → 401
    // https 는 상호를 인증한다 → 브라우저가 서버의 공인 인증서를 확인
  // 인가 (authhorization) : 인증 후 권한을 확인 → 403

  @Operation(summary = "댓글 작성", description = "댓글 작성 후 글의 모든 댓글 리턴")
  @Secured("ROLE_USER")
  @PostMapping("/api/comments/new")
  public ResponseEntity<List<Comment>> write(@ModelAttribute @Valid CommentDto.Create dto, BindingResult br, Principal principal) {
    // PostRead ← Comments, CommentWrite, CommentWrite 에서 댓글을 작성하려면 Comments 갱신
    List<Comment> comments = service.write(dto, principal.getName());
    return ResponseEntity.ok(comments);

  }

  @Operation(summary = "댓글 삭제", description = "자신이 작성한 댓글 삭제 후 글의 모든 댓글 리턴")
  @Secured("ROLE_USER")
  @DeleteMapping("/api/comments")
  public ResponseEntity<List<Comment>> delete(@ModelAttribute @Valid CommentDto.Delete dto, BindingResult br, Principal principal) {
    List<Comment> comments = service.delete(dto, principal.getName());
    return ResponseEntity.ok(comments);
  }
}
