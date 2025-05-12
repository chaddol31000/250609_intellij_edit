package com.example.demo6.service;

import com.example.demo6.dao.*;
import com.example.demo6.dto.*;
import com.example.demo6.entity.*;
import com.example.demo6.exception.*;

import com.example.demo6.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class PostService {
  @Autowired
  private PostDao postDao;
  @Autowired
  private CommentDao commentDao;
  @Autowired
  private postMemberGoodDao postMemberGoodDao;
  private static final int BLOCK_SIZE = 5;

  public PostDto.Pages findAll(int pageno, int pagesize) {
    int totalcount = postDao.count();
    List<Post> posts = postDao.findAll(pageno, pagesize);
    return Demo6Util.getPages(pageno, pagesize, BLOCK_SIZE, totalcount, posts);
  }

  public Map<String, Object> findByPno(int pno, String loginId) {
    // Consumer : 입력은 있고, 출력은 없다
    // Supplier : 입력은 없고, 출력은 있다 → 예외를 발생
    Map<String, Object> post = postDao.findByPnoWithComments(pno).orElseThrow(() -> new EntityNotFoundException("글을 찾을 수 없습니다"));
    if (loginId != null && post.get("writer").equals(loginId)) {
      postDao.increaseReadCnt(pno);
    }
    return post;

    // loginId : 비로그인이면 null, 로그인 했으면 로그인 한 아이디가 들어있음
    // findByPno 한 결과 Optional 에서 객체의 값을 꺼내서 post 에 저장해라
    // 만약 없다면 예외를 발생 시켜라 → ControllerAdvice 가서 오류 메시지를 출력
//    Post post = postDao.findByPno(pno).orElseThrow(()->new RuntimeException());
//    List<Comment> comments = commentDao.findByPno(pno);
//    if(!post.getWriter().equals(loginId))
//      postDao.increaseReadCnt(pno);
    // loginId는 null 이 될 수 있기 때문에 오류가 일어날 가능성이 더 높음 그래서 위처럼 작성해야한다
    // if(!loginId.equals(get.Writer())
    //    postDao.increaseReadCnt(pno); ← xxxxxx
//    return Map.of("post",post,"comments",comments);

  }

  public Post write(PostDto.Write dto, String loginId) {
    Post post = dto.toEntity(loginId);
    postDao.save(post);
    return post;
  }


  public void update(PostDto.Update dto, String loginId) {
    // ex) 내용을 바꾸는 경우라서 dto 의 title 이 비어있다면 → update 를 수행하면 title 이 지워질텐데?
    // 그러니까 Dto 의 title, content 에 @NotEmpty 를 걸자
    // 그러면 사용자가 변경할 때 반드시 제목, 내용을 모두 입력해야 하는거야?
    // 읽기 화면에 출력했던 제목과 내용을 다시 받아오면 돼 그러면 변경한 항목은 변경한대로, 변경하지 않은 항목은 기존 내용을 받아오자!!

    // 글을 읽어서 글이 있다면 예외
    Post post = postDao.findByPno(dto.getPno()).orElseThrow(()-> new EntityNotFoundException("글을 찾을 수 없습니다"));
    // 글을 변경하려는 사용자가 글쓴이가 아니라면 예외
    if(!post.getWriter().equals(loginId))
      throw new JobFailException("잘못된 작업입니다");
    postDao.update(dto);
  }


  public void delete(Integer pno, String loginId) {
    // 글을 읽어서 글이 있다면 예외
    Post post = postDao.findByPno(pno).orElseThrow(()-> new EntityNotFoundException("글을 찾을 수 없습니다"));
    if(!post.getWriter().equals(loginId))
      throw new JobFailException("잘못된 작업입니다");
    postDao.delete(pno);
  }

  public int 추천(int pno, String loginId) {
    // 자기가 작성한 글은 추천할 수 없다
    // 비로그인이면 추천할 수 없다 → @PreAuthrize() 로 필터링 돼서 여기까지 안 옴 (신경 쓸 필요 x)
    // 자기가 작성하지 않은 글은 추천할 수 있다

    // 1. 글이 없으면 예외 처리
    // 1-1. 자기가 작성한 글이면 추천을 못하기에 예외 처리
    // 2. 이미 추천한 글이면 기존 추천 수를 리턴 or 이미 추천한 글이면 예외 처리
    // 3. 추천하지 않은 글이면 추천 후 시로운 추천 수를 리턴
    Post post = postDao.findByPno(pno).orElseThrow(() -> new EntityNotFoundException("글을 찾을 수 없습니다"));
    if (post.getWriter().equals(loginId))
      throw new JobFailException("자신의 글은 추천할 수 없습니다");
    boolean 추천했니 = postMemberGoodDao.existsByPnoAndUsername(pno, loginId);
    if (추천했니)
      throw new JobFailException("이미 추천했습니다");
    postMemberGoodDao.save(pno, loginId);
    postDao.increaseGoodCnt(pno);
    return postDao.findGoodCntByPno(pno);
  }
}
