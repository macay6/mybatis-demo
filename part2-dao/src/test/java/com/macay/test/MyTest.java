package com.macay.test;

import com.macay.Impl.StudentDaoImpl;
import com.macay.dao.StudentDao;
import com.macay.entity.Student;
import com.macay.utils.SqlSessionUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName: MyTest
 * @Description:测试mybatis执行sql语句
 * @Author: Macay
 * @Date: 2021/5/2 5:06 下午
 */
public class MyTest {

    /**
     * 原始Dao层开发
     */
    @Test
    public void testSelectStudentsByUtils() {
        StudentDao studentDao = new StudentDaoImpl();
        Student student = studentDao.selectStudentById(2);
        System.out.println(student);
    }

    @Test
    public void testInsert() {
        StudentDao studentDao = new StudentDaoImpl();
        List<Student> students = studentDao.selectStudents();
        students.forEach(student -> System.out.println(student));
    }
}
