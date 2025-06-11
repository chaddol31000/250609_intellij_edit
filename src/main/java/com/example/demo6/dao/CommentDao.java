package com.example.demo6.dao;

import com.example.demo6.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface CommentDao {
  @Select ("select * from comments where pno=#{pno}")
  List<Comment> findByPno(int pno);

  @Insert("insert into comments(cno, content,write_time, writer, pno) values(comments_seq.nextval,#{content},sysdate,#{writer},#{pno})")
  int save (int pno, String content, String writer);

  @Delete("delete from comments where cno=#{cno} and writer=#{loginId}")
  int deleteByCnoAndWriter(int cno, String loginId);
}

