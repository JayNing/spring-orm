package com.ning.spring.orm.framework;

import java.util.List;
import java.util.Map;

/**
 * @Author JAY
 * @Date 2019/6/29 10:12
 * @Description TODO
 **/
public interface BaseCrudDao<T, PK>{

    /**
     * 获取列表
     * @param queryRule 查询条件
     * @return
     */
    List<T> select(Map<String, Object> queryRule) throws Exception;

    /**
     * 分页查询
     * @param queryRule
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<T> selectPage(Map<String, Object> queryRule,int pageNum, int pageSize) throws Exception;

    /**
     * 根据SQL获取列表
     * @param sql SQL语句
     * @param args 参数
     * @return
     */
    List<Map<String,Object>> selectBySql(String sql, Object... args) throws Exception;

    /**
     * 根据SQL获取分页
     * @param sql SQL语句
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @return
     */
    Page<Map<String,Object>> selectBySqlToPage(String sql, Object [] param, int pageNo, int pageSize) throws Exception;



    /**
     * 删除一条记录
     * @param entity entity中的ID不能为空，如果ID为空，其他条件不能为空，都为空不予执行
     * @return
     */
    boolean delete(T entity) throws Exception;
    /**
     * 批量删除
     * @param list
     * @return 返回受影响的行数
     * @throws Exception
     */
    int deleteAll(List<T> list) throws Exception;

    /**
     * 插入一条记录并返回插入后的ID
     * @param entity 只要entity不等于null，就执行插入
     * @return
     */
    PK insertAndReturnId(T entity) throws Exception;

    /**
     * 插入一条记录自增ID
     * @param entity
     * @return
     * @throws Exception
     */
    boolean insert(T entity) throws Exception;

    /**
     * 批量插入
     * @param list
     * @return 返回受影响的行数
     * @throws Exception
     */
    int insertAll(List<T> list) throws Exception;

    boolean update(T entity) throws Exception;

}
