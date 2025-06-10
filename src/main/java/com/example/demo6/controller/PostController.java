package com.example.demo6.controller;

import com.example.demo6.dto.*;
import com.example.demo6.entity.*;
import com.example.demo6.service.*;
import io.swagger.v3.oas.annotations.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import java.security.*;
import java.util.*;

// @Validated 는 스프링 검증, 없으면 자바 표준 검증 수행. 스프링 검증이 좀 더 간략함
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
  @GetMapping("/posts/post")
  public ResponseEntity<Map<String,Object>> findByPno(@RequestParam int pno, Principal principal) {
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
  @Operation(summary = "글쓰기")
  @PreAuthorize("isAuthenticated()") // 관리자도 글 작성이 가능함 (로그인한 아이디만 검열함)
  @Secured("ROLE_USER") // 실제 게시판을 만들 때는 Secured 를 쓰는 게 더 낫다 (Secured 는 관리자 계정으로는 작성 불가능)
  @PostMapping("/posts/new")
  public ResponseEntity<Post> write(@ModelAttribute @Valid PostDto.Write dto, BindingResult br, Principal principal) {
    // 작성한 글을 그대로 쏴주기
    // 보통 관습적으로 엔티티를 쏴준다
    // principal 은 로그인 한 값이 null 인 걸 검열하나 지금 secured 가 걸려 있어서 어차피 실패하면 튕김 그래서 검열을 할 필요가 없음
    Post post = service.write(dto, principal.getName());
    return ResponseEntity.ok(post);
  }

  @Secured("ROLE_USER")
  @PutMapping("/posts/post")
  @Operation(summary = "글변경", description = "글번호로 제목과 내용 변경")
  public ResponseEntity<String> update(@ModelAttribute @Valid PostDto.Update dto, BindingResult br, Principal principal) {
    // 글 쓴 사람이 맞나 확인해야 하기에 principal 이 있어야함
    service.update(dto, principal.getName());
    return ResponseEntity.ok("글을 변경했습니다");
  }

  @Secured("ROLE_USER")
  @DeleteMapping("/posts/post")
  @Operation(summary = "삭제")
  public ResponseEntity<String> delete(@RequestParam @NotNull Integer pno, Principal principal) {
    // 만약 pno 를 검열하려면 NotNull 이 필요함 ! 꼭 NotNull 로 검열해주기
    // @Validated 이 없으면 NotNull 로 검열이 불가능
    service.delete(pno, principal.getName());
    return ResponseEntity.ok("글을 삭제했습니다");
  }

  @Secured("ROLE_USER")
  @PutMapping("/posts/good")
  @Operation(summary = "글 추천", description = "이미 추천한 글은 재추천 불가")
  public ResponseEntity<Integer> 추천 (@RequestParam @NotNull Integer pno, Principal principal) {
    int newGoodCnt = service.추천(pno, principal.getName());
    return ResponseEntity.ok(newGoodCnt);
  }

//  @Operation(summary = "글 비추", description = "이미 비추천한 글은 다시 불가")
//  public ResponseEntity<Integer> 비추천
}
