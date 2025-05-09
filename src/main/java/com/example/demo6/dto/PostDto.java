package com.example.demo6.dto;

import com.example.demo6.entity.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

// PostDto 는 Dto 들을 담는 클래스다 → 왜? Dto 클래스 개수를 줄여서 PostDto.Pages, PostDto.Create... 이렇게 만들려고
public class PostDto {
  // 페이지 출력 DTO
  @Data
  @AllArgsConstructor
  public static class Pages {
    private int prev;
    private int start;
    private int end;
    private int next;
    private int pageno;
    private List<Post> posts;
  }

  // 글 작성 DTO
  @Data
  public static class Write {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    public Post toEntity(String loginId) {
      return Post.builder().title(title).content(content).writer(loginId).build();
    }
  }

  // 글 변경 DTO
  // 문자열 검증 : NotEmpty
  // 문자열이 아닌 것 : NotNull
  @Data
  public static class Update {
    @NotNull
    private Integer pno;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    // 둘 다 비어있으면 변경할 값이 없기에 튕겨 나가야함
    // 하지만 둘 다 비어있을 수도 있기에 그거에 대한 건 따로 코드를 짜줘야함
  }
}
