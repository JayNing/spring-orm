package com.ning.demo.spring.orm;

import com.ning.demo.spring.orm.entity.Member;
import com.ning.demo.spring.orm.entity.Order;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * @Author JAY
 * @Date 2019/6/27 22:01
 * @Description TODO
 **/
public class JdbcTest {
    public static void main(String[] args) {
        Member condition = new Member();
        condition.setName("Jay");
        condition.setAge(19);
        Order order = new Order();
//        List<?> result = query(new Member());
        List<?> result = query(order);
        System.out.println(Arrays.toString(result.toArray()));
    }

    private static List<?> query(Object condition) {
        List<Object> result = new ArrayList<>();
        try {
            //1、加载驱动类
            Class.forName("com.mysql.jdbc.Driver");
            //2、获取连接
            Connection connection = DriverManager.
                    getConnection("jdbc:mysql://127.0.0.1:3306/orm_demo?characterEncoding=UTF-8&rewriteBatchedStatements=true",
                            "root", "123456");

            //解析表名
            Class<?> aClass = condition.getClass();
            String tableName = "";
            Table table = aClass.getAnnotation(Table.class);
            tableName = table.name();

            String sql = "select * from " + tableName;

            //属性和表列名映射map,存储属性名-列名映射
            Map<String, String> fieldMapping = new HashMap<>();
            //存储列名-属性名映射
            Map<String, String> columnMapping = new HashMap<>();

            //拼接sql查询条件
            StringBuilder sb = new StringBuilder(" where 1 = 1 ");
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)){
                    Column column = field.getAnnotation(Column.class);
                    fieldMapping.put(field.getName(),column.name());
                    columnMapping.put(column.name(),field.getName());
                }else {
                    fieldMapping.put(field.getName(),field.getName());
                    columnMapping.put(field.getName(),field.getName());
                }
            }

            for (Field field : fields) {
                //判断属性值是否为空
                field.setAccessible(true);
                Object value = field.get(condition);
                if (value != null){
                    if (String.class == field.getType()){
                        sb = sb.append(" and ").append(fieldMapping.get(field.getName()))
                                .append(" = '").append(value).append("'");
                    }else if (Integer.class == field.getType()){
                        sb = sb.append(" and ").append(fieldMapping.get(field.getName()))
                                .append(" = ").append(value);
                    }
                }
            }

            sql = sql + sb.toString();

            System.out.println("执行sql ： " + sql);

            PreparedStatement ps = connection.prepareStatement(sql);
            int columnCount = ps.getMetaData().getColumnCount();
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Object instance = aClass.newInstance();
                for (int i = 1; i <= columnCount; i++){
                    //从rs中取得当前这个游标下的列名
                    String columnName = ps.getMetaData().getColumnName(i);
                    String fieldName = columnMapping.get(columnName);
                    Field declaredField = aClass.getDeclaredField(fieldName);
                    declaredField.setAccessible(true);
                    declaredField.set(instance, rs.getObject(i));
                }
                result.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
