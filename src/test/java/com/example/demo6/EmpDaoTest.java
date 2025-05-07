package com.example.demo6;

import com.example.demo6.exercise.EmpDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmpDaoTest {
    @Autowired
    private EmpDao empDao;

    @Test
    public void findJob1Test() {
        empDao.findJob1().forEach(a->System.out.println(a));
        assertEquals(5, empDao.findJob1().size());
    }

    @Test
    public void findEmpByDeptno1Test() {
        empDao.findEmpByDeptno1(10).forEach(a->System.out.println(a));
        assertEquals(4, empDao.findEmpByDeptno1(10).size());
    }

    @Test
    public void findEmpByCommIsNotNull1Test() {
        assertEquals(4, empDao.findEmpByCommIsNotNull1().size());
    }

    @Test
    public void findJob2Test() {
        assertEquals(5,empDao.findJob2().size());
    }

    @Test
    public void findEmpBySalLessThan2Test() {
        empDao.findEmpBySalLessThan2(5000).forEach(a->System.out.println(a.get("job")));
        // Entity 로 리턴을 받았다면 a.getJob(); 으로 찍으면 됨
        // 장점 : 오티는 절대 발생하지 않음
        // 단점 : 클래스를 만들어야 한다

        // Map 으로 리턴을 받았다면 a.get("JOB");
        // 장점 : 클래스를 만들 필요가 없음 (간편함)
        // 단점 : 오타나면 니 책임
        assertEquals(15, empDao.findEmpBySalLessThan2(5000).size());
    }

    @Test
    public void findEmpByDnameTest() {
        assertEquals(6, empDao.findEmpByDname("SALES").size());
    }

    @Test
    public void findEmpnoTest() {
        // 값이 있을 때
        assertEquals(true, empDao.findByEmpno(7369).isPresent());
        // 값이 없을 때 (꼭 null 체크해주기)
        assertEquals(true, empDao.findByEmpno(9000).isEmpty());
        // 결과가 false 가 되게 하려면?
        assertEquals(false, empDao.findByEmpno(8000).isPresent());
    }

    @Test
    public void findByPositionTest() {
        assertEquals(6, empDao.findByPosition("사원").size());

    }

    @Test
    public void findByEmpTypeTest() {
        assertEquals(4, empDao.findByEmpType("정규직").size());
    }

    @Test
    public void findEmp2ByEmpnoTest() {
        System.out.println(empDao.findEmp2ByEmpno(19900101));
        assertEquals(true, empDao.findEmp2ByEmpno(19900101).isPresent());
        assertEquals(true, empDao.findEmp2ByEmpno(19990102).isEmpty());
    }
}
