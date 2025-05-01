package com.example.demo6;

import com.example.demo6.dao.*;
import com.example.demo6.entity.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

// swagger 로는 글을 많이 넣을 수 없음 (너무 오래 걸리고 복잡함)
@SpringBootTest
public class PostDaoTest {
  @Autowired
  private PostDao postDao;

//  @Test
//  public void 글때려박기() {
//    for(int i = 0; i<123; i++) {
//      Post p = Post.builder().title(i+"번째 글").content("내용 없음").writer("spring").build();
//      postDao.save(p);
//    }
//  }

  @Test
  public void 페이징쿼리퀘스트() {
    // pageno 가 1이므로 123~114번까지 출력
    postDao.findAll(1, 10).forEach(post->System.out.println(post.getPno()));

    //pageno 가 13이면 3~1 출력
    postDao.findAll(13,10).forEach(post->System.out.println(post.getPno()));
  }

  @Test
  public void findByPnoWithCommentsTest() {
    System.out.println(postDao.findByPnoWithComments(10));
  }

}
