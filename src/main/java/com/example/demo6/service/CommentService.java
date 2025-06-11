package com.example.demo6.service;

import com.example.demo6.dao.CommentDao;
import com.example.demo6.dto.*;
import com.example.demo6.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CommentService {
  @Autowired
  private CommentDao commentDao;

  public List<Comment> write(CommentDto.Create dto, String loginId) {
    commentDao.save(dto.getPno(), dto.getContent(), loginId);
    return commentDao.findByPno(dto.getPno());
  }

  public List<Comment> delete(CommentDto.Delete dto, String loginId) {
    commentDao.deleteByCnoAndWriter(dto.getCno(), loginId);
    return commentDao.findByPno(dto.getPno());

  }
}
