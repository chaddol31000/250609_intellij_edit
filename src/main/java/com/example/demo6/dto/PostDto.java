package com.example.demo6.dto;

import com.example.demo6.entity.*;
import lombok.*;

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
  public static class Create {

  }

  // 기타 DTO 들 ...
}
