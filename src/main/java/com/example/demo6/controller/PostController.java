package com.example.demo6.controller;

import com.example.demo6.dto.*;
import com.example.demo6.entity.*;
import com.example.demo6.service.*;
import io.swagger.v3.oas.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.*;

@Validated
@RestController
public class PostController {
  @Autowired
  private PostService service;

  @Operation(summary = "페이징", description = "기본페이지 번호 1, 기본 페이지 크기 10으로 페이징")
  @GetMapping("/posts")
  public ResponseEntity<PostDto.Pages> findAll(@RequestParam(defaultValue = "1") int pageno,
                                               @RequestParam(defaultValue = "10") int pagesize) {
    return ResponseEntity.ok(service.findAll(pageno,pagesize));
  }

  // @RequestParam 이나 @ModelAttribute 에서는 사용자가 값을 넘기지 않으면 null 이 되고 널체크로 오류를 발견한다
  // @PathVariable 은 사용자가 넘기는 값이 주소의 일부다
  // ㄴ 만약 ↓ 주소에서 pno 를 넘기지 않았다면 get /posts 가 돼서 주소가 달라지게 된다
  // ㄴ pno 가 111 이라면 get /posts/111, 그런데 값을 안 넘겼다면 get /posts. 즉, 주소가 아예 다르다
  @Operation(summary = "글읽기", description = "글읽기")
  @GetMapping("/posts/post/{pno}")
  public ResponseEntity<Map<String,Object>> findByPno(@PathVariable int pno, Principal principal) {
    // 로그인 했으면 로그인 아이디, 비로그인이면 null 을 대입
    String loginId = principal==null? null:principal.getName();
    return ResponseEntity.ok(service.findByPno(pno, loginId));

    // MemberController 는 컨트롤러에서 if 문 걸어서 200 응답, 또는 409 응답을 만들어낸다
    // 또 다른 방법으로는 내가 원하지 않는 방향으로 진행되면 서비스에서 예외 발생 → ControllerAdvice 로 넘긴다
    // 컨트롤러는 200응답(바람직한 흐름)만 담당, 바람직하지 않은 흐름은 Advice 에서 담당
    // 이렇게 만들면 컨트롤러는 실패할 일이 없음 실패는 모두 서비스가 감당
    // 작업이 원하는 방향으로 가면 컨트롤러.

//    @Operation(summary = "글읽기", description = "글읽기")
//    @GetMapping("/posts/post/{pno}")
//    public ResponseEntity<Post> findByPno(@RequestParam int pno, Principal principal) {
//      // 로그인 했으면 로그인 아이디, 비로그인이면 null 을 대입
//      String loginId = principal==null? null:principal.getName();
//      return ResponseEntity.ok(service.findByPno(pno, loginId));

    // 위 같은 경우는 GetMapping 에 "/posts/post" 라고 링크를 걸어도 코드가 실행됨
    // 왜?? @RequestParam 이니까 pno 가 없어도 코드가 죽지 않고 실행되기 때문

  }
}
