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
        ClassessDao mapper = sqlSession.getMapper(ClassessDao.class);
        Classess classess = mapper.selectClassWithStudentById(302);
        System.out.println(classess);
        sqlSession.close();
    }

    @Test
    public void testselectAllClassWithStudent() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ClassessDao mapper = sqlSession.getMapper(ClassessDao.class);
        List<Classess> classess = mapper.selectAllClassWithStudent();
        System.out.println("classess size is :" + classess.size());
        for (Classess classs : classess) {
            System.out.println(classs);
        }
        sqlSession.close();
    }

    @Test
    public void selectClassWithStudentByStep() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ClassessDao mapper = sqlSession.getMapper(ClassessDao.class);
        Classess classess = mapper.selectClassWithStudentByStep(302);
        System.out.println(classess);
        sqlSession.close();
    }


    @Test
    public void selectStudentLikeName() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        String str = "m";
        // String name = "%" + str + "%";
        String name = new StringBuffer("%").append(str).append("%").toString();
        List<Student> students = mapper.selectStudentLikeName(name);
        System.out.println(students);
        sqlSession.close();
    }

    @Test
    public void selectStudentLikeName2() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> students = mapper.selectStudentLikeName2("m");
        System.out.println(students);
        sqlSession.close();
    }

    @Test
    public void selectStudentLikeName3() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> students = mapper.selectStudentLikeName3("m");
        System.out.println(students);
        sqlSession.close();
    }
}
