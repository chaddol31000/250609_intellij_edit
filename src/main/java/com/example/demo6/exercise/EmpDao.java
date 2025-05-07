package com.example.demo6.exercise;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface EmpDao {
    // emp 에서 job 을 출력하는 쿼리와 메소드
    @Select("select distinct job from emp order by job asc")
    List<String> findJob1();

    // 부서번호를 입력 받아 그 부서의 사원 출력
    @Select("select * from emp where deptno=#{deptno}")
    List<Map<String, Object>> findEmpByDeptno1(int deptno);

    // comm 을 수령하는 사원들 출력
    @Select("select * from emp where comm is not null")
    List<Map<String,Object>> findEmpByCommIsNotNull1();


    List<String> findJob2();

    // 일정 급여 이하를 출력
    // select * from emp where sal<=?
    List<Map<String, Object>> findEmpBySalLessThan2(int sal);

    List<Map<String,Object>> findEmpByDname(String dname);

    // 사번으로 검색 (하나만 불러오기)
    // Optional 을 사용하면 값이 있을 수도 있고 없을 수도 있다는 걸 알려준다
    // 값이 없을 수도 있으니 체크해라 라는 뜻으로?
    Optional<Map<String,Object>> findByEmpno(int empno);

    // emp2 에서 직위(position) 을 출력
    List<Map<String,Object>> findByPosition(String position);

    // emp2 에서 emp_type 으로 검색해 사원을 출력
    List<Map<String, Object>> findByEmpType(String emp_type);

    // emp2 에서 사번을 입력 받아 사원을 출력
    Optional<Map<String, Object>> findEmp2ByEmpno(int empno);
}
