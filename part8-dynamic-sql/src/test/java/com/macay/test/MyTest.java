package com.macay.test;

import com.macay.dao.ClassessDao;
import com.macay.dao.StudentDao;
import com.macay.entity.Classess;
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
        Student student = new Student();
        student.setName("m");
        student.setEmail(null);
        // student.setEmail("12@qq.com");
        List<Student> students = mapper.selectStudentIfInWhere(student);
        students.forEach(stu -> System.out.println(stu));
        sqlSession.close();
    }


    @Test
    public void updateStudentByIdSelective() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(2);
        student.setName("hhh");
        student.setEmail(null);
        int count = mapper.updateStudentByIdSelective(student);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void insertStudent() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(7);
        student.setName("test4");
        // student.setEmail(null);
        student.setAge(100);
        int count = mapper.insertStudent(student);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }
}
