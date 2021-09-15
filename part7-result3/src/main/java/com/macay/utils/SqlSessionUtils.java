package com.macay.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: SqlSessionUtils
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/3 12:40 下午
 */
public class SqlSessionUtils {
    private static SqlSessionFactory sessionFactory = null;

    // 初始化sqlsessionFactory
    static {
        String config = "mybatis.xml";
        try {
            InputStream resourceAsStream = Resources.getResourceAsStream(config);
            sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 创建方法，获取SqlSession对象
    public static SqlSession getSqlSession() {
        SqlSession sqlSession = null;
        if (null != sessionFactory) {
            sqlSession = sessionFactory.openSession();
        }
        return sqlSession;
    }
}
