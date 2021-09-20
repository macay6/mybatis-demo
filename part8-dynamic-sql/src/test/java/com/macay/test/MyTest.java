package com.macay.test;

import com.macay.dao.ClassessDao;
import com.macay.dao.StudentDao;
import com.macay.entity.Classess;
import com.macay.entity.Student;
import com.macay.utils.SqlSessionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
        student.setId(12);
        student.setName("test4");
        // student.setEmail(null);
        student.setAge(100);
        int count = mapper.insertStudent(student);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void insertStudent2() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(15);
        student.setName("test4");
        student.setEmail("123456@qq.com");
        student.setAge(100);
        int i = mapper.insertStudent(student);
        System.out.println(mapper.selectById(15));
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void insertStudent3() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(16);
        student.setName("test4");
        student.setEmail(null);
        student.setAge(100);
        int i = mapper.insertStudent(student);
        System.out.println(mapper.selectById(16));
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void selectByIdOrName1() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(16);
        student.setName("macay");
        System.out.println(mapper.selectByIdOrName(student));
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void selectByIdOrName2() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setName("macay");
        System.out.println(mapper.selectByIdOrName(student));
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void selectByIdOrName3() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        System.out.println(mapper.selectByIdOrName(student));
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void selectByIdOrName4() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setName("macay");
        System.out.println(mapper.selectByIdOrName2(student));
        sqlSession.commit();
        sqlSession.close();
    }


    @Test
    public void selectByIdList() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
        List<Student> students = mapper.selectByIdList(list);
        System.out.println(students);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void selectByIdList2() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> list = new ArrayList<>();
        Student student = new Student();
        student.setId(1);
        Student student1 = new Student();
        student1.setId(2);
        list.add(student);
        list.add(student1);
        List<Student> students = mapper.selectByIdList2(list);
        System.out.println(students);
        sqlSession.commit();
        sqlSession.close();
    }


    @Test
    public void selectByIdArray() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Integer> list = new ArrayList<>();
        Integer[] ids = new Integer[3];
        list.add(1);
        list.add(2);
        list.add(3);
        Integer[] integers = list.toArray(ids);
        List<Student> students = mapper.selectByIdArray(integers);
        System.out.println(students);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void insertStudentBatch() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        List<Student> list = new ArrayList<>();
        Student student = new Student();
        student.setId(22);
        student.setName("test20");
        student.setAge(20);
        student.setEmail("test20@qq.com");
        Student student1 = new Student();
        student1.setId(23);
        student1.setName("test21");
        student1.setAge(21);
        student1.setEmail("test21@qq.com");
        list.add(student);
        list.add(student1);
        if (CollectionUtils.isNotEmpty(list)) {
            int count = mapper.insertStudentBatch(list);
            System.out.println(count);
        }
        sqlSession.commit();
        sqlSession.close();
    }


    @Test
    public void updateStudentByMap() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Map<String, Object> map = new HashMap<>();
        map.put("id", 3);
        map.put("name", "test3");
        map.put("email", "test3@qq.com");
        if (MapUtils.isNotEmpty(map)) {
            int count = mapper.updateStudentByMap(map);
            System.out.println(count);
        }
        System.out.println(mapper.selectById(3));
        sqlSession.commit();
        sqlSession.close();
    }


}
