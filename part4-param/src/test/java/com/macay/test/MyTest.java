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
    public void testSelectStudentsByUtils() {
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
    public void testBasicParam() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student students = mapper.selectByEmail("123@qq.com");
        System.out.println(students);
        sqlSession.close();
    }

    @Test
    public void testSelectByNameOrAge() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> students = mapper.selectByNameOrAge("macay", 20);
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }

    @Test
    public void testselectByObject() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student stu = new Student();
        stu.setName("macay");
        stu.setAge(20);
        List<Student> students = mapper.selectByObject(stu);
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }

    @Test
    public void testSelectByPosition() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> students = mapper.selectByPosition("macay", 20);
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }

    @Test
    public void testselectByMap() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Map<String, Object> data = new HashMap<>();
        data.put("myName", "macay");
        data.put("myAge", 20);
        List<Student> students = mapper.selectByMap(data);
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }

    @Test
    public void selectStudentOrderByColumn() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> students = mapper.selectStudentOrderByColumn("macay","age","student");
        students.forEach(student -> System.out.println(student));
        sqlSession.close();
    }
}
