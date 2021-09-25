package com.macay.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.macay.dao.ClassessDao;
import com.macay.dao.StudentDao;
import com.macay.entity.Classess;
import com.macay.entity.GenderEnum;
import com.macay.entity.Pager;
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
    public void testInsertStudent() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(37);
        student.setName("test30");
        student.setEmail("123@qq.com");
        student.setAge(30);
        student.setPartyMember(true);
        List<Integer> list = Arrays.asList(new Integer[]{1, 2, 3, 4});
        Classess classess = new Classess();
        classess.setId(2);
        classess.setName("sannianyuban");
        student.setCla(classess);
        student.setHobbies(list);
        student.setGender(GenderEnum.MALE);
        int i = mapper.insertStudent(student);
        System.out.println(i);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testGetStudentById() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = mapper.getStudnetById(37);
        System.out.println(student);
        sqlSession.commit();
        sqlSession.close();
    }
}
