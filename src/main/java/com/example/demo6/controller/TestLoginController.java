package com.example.demo6.controller;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

@Controller
public class TestLoginController {
  @GetMapping("/login")
  public void login() {
  }
}
