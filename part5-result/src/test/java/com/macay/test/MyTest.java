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

    /**
     * 动态代理开发
     */
    @Test
    public void testSelectStudentsById() {
        // 1、获得sqlSession对象
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        // 2、获取Mapper代理对象
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        System.out.println("mapper is :" + mapper.getClass().getName());
        // 3、执行Mapper代理对象的查询方法
        Student student = mapper.selectStudentById(2);
        System.out.println(student);
        // 4、关闭session
        sqlSession.close();
    }

    @Test
    public void testSelectStudents() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> students = mapper.selectStudents();
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }

    @Test
    public void testSelectStudentsById2() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Map<String, Object> map = mapper.selectStudentById2(1);
        System.out.println(map);
        sqlSession.close();
    }


    @Test
    public void testselectStudentByAge() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Map<String, Object>> list = mapper.selectStudentByAge(20);
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
        sqlSession.close();
    }

    @Test
    public void testselectStudentByAge2() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Map<Integer, Student> map = mapper.selectStudentByAge2(20);
        System.out.println(map);
        sqlSession.close();
    }

}
