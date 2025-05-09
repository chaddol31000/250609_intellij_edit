package com.example.demo6;

import com.example.demo6.exercise.EmpDao2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmpDaoTest2 {
  @Autowired
  private EmpDao2 empDao2;

  @Test
  public void findByDeptno() {
    System.out.println(empDao2.findByDeptno(10));
  }
}
