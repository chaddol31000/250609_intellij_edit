package com.example.demo6.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
;

@Mapper
public interface postMemberGoodDao {

  @Select("select count(*) from posts_members_good where pno=#{pno} and username=#{loginId} and rownum=1")
  boolean existsByPnoAndUsername (int pno, String loginId);

  @Insert("insert into posts_members_good values(#{pno}, #{loginId})")
  int save(int pno, String loginId);


}
