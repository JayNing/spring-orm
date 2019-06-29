package com.ning.spring.orm.framework;

import com.ning.spring.orm.util.GenericUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.Table;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @Author JAY
 * @Date 2019/6/29 10:19
 * @Description TODO
 **/
public abstract class BaseCrudDaoSupport<T extends Serializable, PK extends Serializable> implements BaseCrudDao<T, PK>{
    private static final long serialVersionUID = 1L;

    private String tableName = "";

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplateOrm;

    private EntityOperation<T> operation;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateOrm = new JdbcTemplate(dataSource);

    }

    /**
     * 获取主键列名称 建议子类重写
     * @return
     */
    protected abstract String getPKColumn();

    protected BaseCrudDaoSupport() {
        Class<T> paramClass = GenericUtil.getSuperClassGenricType(this.getClass(),0);
        operation = new EntityOperation<>(paramClass,this.getPKColumn());
        tableName = operation.tableName;
    }

    @Override
    public List<T> select(Map<String, Object> queryRule) throws Exception {
        //解析表名
        String sql = "select " + operation.allColumn + " from " + tableName;
        //拼接查询条件
        StringBuilder where = new StringBuilder();
        if (queryRule.size() > 0){
            where = where.append(" where ");
            for (Map.Entry<String, Object> entry : queryRule.entrySet()) {
                String fieldName = entry.getKey();
                if (null != entry.getValue()){
                    String columnName = operation.fieldToColumn.get(fieldName);
                    where = where.append(columnName).append("=").append(operation.getFieldValue(fieldName,entry.getValue()));
                }
            }
        }
        return this.jdbcTemplateOrm.query(sql + where, operation.rowMapper);
    }

    @Override
    public Page<T> selectPage(Map<String, Object> queryRule, int pageNum, int pageSize) throws Exception {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectBySql(String sql, Object... args) throws Exception {
        return null;
    }

    @Override
    public Page<Map<String, Object>> selectBySqlToPage(String sql, Object[] param, int pageNo, int pageSize) throws Exception {
        return null;
    }

    @Override
    public boolean delete(T entity) throws Exception {
        return false;
    }

    @Override
    public int deleteAll(List<T> list) throws Exception {
        return 0;
    }

    @Override
    public PK insertAndReturnId(T entity) throws Exception {
        return null;
    }

    //如果属性为空，则不插入此属性列
    @Override
    public boolean insert(T entity) throws Exception {

        String sql = "insert into " + tableName ;

        StringBuilder insertColumns = new StringBuilder();
        StringBuilder insertValues = new StringBuilder();

        Class<? extends Serializable> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(entity);
            if (null != fieldValue){
                //说明参数有值
                String columnName = operation.fieldToColumn.get(field.getName());
                insertColumns = insertColumns.append(",").append(columnName);
                //判断属性类型，String， Integer
                if (field.getType() == String.class){
                    insertValues = insertValues.append(",").append("'" + fieldValue + "'");
                }else {
                    insertValues = insertValues.append(",").append(fieldValue);
                }
            }
        }
        if (insertColumns.length() == 0 || insertValues.length() == 0){
            return false;
        }
        String executeSql = sql + "(" + insertColumns.substring(1) + ") value (" + insertValues.substring(1) + ")";
        System.out.println("executeSql : " + executeSql);
        this.jdbcTemplateOrm.execute(executeSql);
        return true;
    }

    @Override
    public int insertAll(List<T> list) throws Exception {
        return 0;
    }

    @Override
    public boolean update(T entity) throws Exception {
        return false;
    }
}
