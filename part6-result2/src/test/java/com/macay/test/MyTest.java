package com.macay.test;

import com.macay.dao.StudentDao;
import com.macay.entity.Student;
import com.macay.utils.SqlSessionUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MyTest
 * @Description:测试mybatis执行sql语句
 * @Author: Macay
 * @Date: 2021/5/2 5:06 下午
 */
public class MyTest {

    @Test
    public void testSelectStudentsById() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = mapper.selectStudentById(1);
        System.out.println(student);
        sqlSession.close();
    }


    @Test
    public void testSelectStudentsById2() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = mapper.selectStudentById2(1);
        System.out.println(student);
        sqlSession.close();
    }

    @Test
    public void testSelectStudentsByStep() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = mapper.selectStudentByStep(1);
        System.out.println(student);
        sqlSession.close();
    }

    @Test
    public void testSelectStudentsById3() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = mapper.selectStudentById3(1);
        System.out.println(student);
        sqlSession.close();
    }
}
