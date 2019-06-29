package com.ning.spring.orm.util;

import com.ning.spring.orm.framework.BaseCrudDaoSupport;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author JAY
 * @Date 2019/6/29 10:53
 * @Description 泛型操作类.
 **/
public class GenericUtil {

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or <code>Object.class</code> if
     *         cannot be determined
     */
    public static Class getSuperClassGenricType(Class<? extends BaseCrudDaoSupport> clazz, int index) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)){
            return Object.class;
        }
        //获取参数化类型的集合
        Type[] params = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
}
