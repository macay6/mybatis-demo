package com.macay.test;

import com.macay.dao.NewStudentDao;
import com.macay.dao.StudentDao;
import com.macay.entity.NewStudent;
import com.macay.entity.Student;
import com.macay.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Map;

/**
 * @ClassName: MyTest2
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/4 10:36 下午
 */
public class MyTest2 {
    @Test
    public void testSelectById() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        NewStudentDao mapper = sqlSession.getMapper(NewStudentDao.class);
        NewStudent newStudent = mapper.selectById(1);
        System.out.println(newStudent);
        sqlSession.close();
    }

    @Test
    public void testSelectById2() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        NewStudentDao mapper = sqlSession.getMapper(NewStudentDao.class);
        NewStudent newStudent = mapper.selectById2(1);
        System.out.println(newStudent);
        sqlSession.close();
    }
}
