package com.example.demo6.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.time.*;

@Getter
public class Comment {
  private int cno;
  private String content;
  private String writer;
  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private LocalDateTime writeTime;
  private int pno;

}
