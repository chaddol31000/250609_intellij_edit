package com.example.demo6.exercise;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface EmpDao2 {
  // 부서정보와 그 부서에서 근무하는 사원 정보를 같이 읽어온다
  // Map 으로 빼는 것과 Entity 로 빼는 것 둘 다 해 볼 예정
  DeptWithEmp findByDeptno(int deptno);
}
