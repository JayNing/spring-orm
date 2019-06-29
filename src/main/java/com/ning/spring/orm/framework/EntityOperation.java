package com.ning.spring.orm.framework;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author JAY
 * @Date 2019/6/29 11:04
 * @Description 实体对象的反射操作
 **/
public class EntityOperation<T> {

    public Class<T> entityClass = null; // 泛型实体Class对象
    public final String tableName;
    public String allColumn = "*";
    public Field pkField;
    public Map<String, String> fieldToColumn;
    public Map<String, String> columnToField;
    public final RowMapper<T> rowMapper;

    public EntityOperation(Class<T> clazz,String pk) {
        this.entityClass = clazz;
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            this.tableName = table.name();
        } else {
            this.tableName =  entityClass.getSimpleName();
        }
        Field[] fields = this.entityClass.getDeclaredFields();
        this.fieldToColumn = generateFieldToColunmMap(fields);
        this.columnToField = generateColumnToFieldMap(fieldToColumn);
        fillPkFieldAndAllColumn(fields, pk);
        this.rowMapper = createRowMapper();
    }

    private RowMapper<T> createRowMapper() {
        return new RowMapper<T>(){
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                T instance = null;
                try {
                    instance = entityClass.newInstance();
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++){
                        String columnName = metaData.getColumnName(i);
                        String fieldName = columnToField.get(columnName);
                        Field field = entityClass.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        field.set(instance,rs.getObject(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return instance;
            }
        };
    }

    private void fillPkFieldAndAllColumn(Field[] fields, String pk) {
        try {
            if(!StringUtils.isEmpty(pk)){
                pkField = entityClass.getDeclaredField(pk);
                pkField.setAccessible(true);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            if(StringUtils.isEmpty(pk)){
                Id id = field.getAnnotation(Id.class);
                if(id != null){
                    pkField = field;
                    break;
                }
            }
            sb = sb.append(",").append(fieldToColumn.get(field.getName()));
        }
        this.allColumn = sb.substring(1);
    }

    private Map<String,String> generateColumnToFieldMap(Map<String,String> fieldToColumn) {
        Map<String,String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : fieldToColumn.entrySet()) {
            result.put(entry.getValue(),entry.getKey());
        }
        return result;
    }


    private Map<String,String> generateFieldToColunmMap(Field[] fields) {

        Map<String,String> result = new HashMap<>();

        if (null == fields || fields.length == 0){
            return new HashMap<>(1);
        }
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)){
                Column column = field.getAnnotation(Column.class);
                result.put(field.getName(), column.name());
            } else {
                //将属性按照驼峰命名法反解析出 _ 格式的列名，如： userName ， 对应数据库列名为 user_name
                String columnName = getColumnName(field.getName());
                result.put(field.getName(), columnName);
            }
        }
        return result;
    }

    public static String getColumnName(String fieldName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++){
            String str = fieldName.substring(i, i + 1);
            if (str.equals(str.toUpperCase())){
                //说明是大写，需要加_
                sb = sb.append("_" + str.toLowerCase());
            }else {
                sb = sb.append(str);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getColumnName("userName"));
    }

    public Object getFieldValue(String fieldName, Object value) {
        try {
            Field field = this.entityClass.getDeclaredField(fieldName);
            Class<?> type = field.getType();
            if (type == String.class){
                return "'" + value + "'";
            }else if (type == Integer.class){
                return value;
            }else if (type == Long.class){
                return value;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return value;
    }
}
