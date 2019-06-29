package com.ning.spring.orm.demo.dao;

import com.ning.spring.orm.demo.entity.Order;
import com.ning.spring.orm.framework.BaseCrudDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Author JAY
 * @Date 2019/6/29 10:11
 * @Description TODO
 **/
@Repository
public class OrderDao extends BaseCrudDaoSupport<Order, Long> {

    @Override
    @Resource(name="dataSource")
    public void setDataSource(DataSource dataSource){
        super.setDataSource(dataSource);
    }

    @Override
    protected String getPKColumn() {
        return "id";
    }


}
