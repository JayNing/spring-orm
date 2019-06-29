package com.ning.spring.orm;

import com.ning.spring.orm.demo.dao.MemberDao;
import com.ning.spring.orm.demo.dao.OrderDao;
import com.ning.spring.orm.demo.entity.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

/**
 * @Author JAY
 * @Date 2019/6/29 10:37
 * @Description TODO
 **/
@ContextConfiguration(locations = {"classpath:application-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class OrmTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Test
    public void testList(){
        try {
            HashMap<String, Object> map = new HashMap<>();
            System.out.println(orderDao.select(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testInsert(){
        try {
          Member member = new Member();
          member.setAge(20);
          member.setName("张三");
          memberDao.insert(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
