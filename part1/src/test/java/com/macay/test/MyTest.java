package com.macay.test;

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
     * 测试常规方法
     * @throws IOException
     */
    @Test
    public void testSelectById() throws IOException {
        //调用mybatis某个对象的方法，执行mapper文件中的sql语句
        //mybatis核心类： SqlSessionFactory

        //1.定义mybatis主配置文件的位置, 从类路径开始的相对路径
        String config="mybatis.xml";
        //2.读取主配置文件。使用mybatis框架中的Resources类
        InputStream inputStream = Resources.getResourceAsStream(config);
        //3.创建SqlSessionFactory对象， 使用SqlSessionFactoryBuidler类
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);

        //4.获取SqlSession对象。
        SqlSession session = factory.openSession();

        //5.指定要执行的sql语句的 id
        //  sql的id = namespace+"."+ select|update|insert|delete标签的id属性值
        String sqlId = "com.macay.dao.StudentDao"+"."+"selectStudentById";

        // 6.通过SqlSession的方法，执行sql语句
        Student student = session.selectOne(sqlId);
        System.out.println("使用mybatis查询一个学生："+student);

        // 7.关闭SqlSession对象
        session.close();
    }

    /**
     * 工具类批量查询
     */
    @Test
    public void testSelectStudentsByUtils() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        String sqlId = "com.macay.dao.StudentDao"+"."+"selectStudents";
        List<Student> students = sqlSession.selectList(sqlId);
        for (Student student : students) {
            System.out.println(student);
        }
        sqlSession.close();
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        String sqlId = "com.macay.dao.StudentDao"+"."+"insertStudent";
        Student student = new Student();
        student.setId(4);
        student.setName("libai");
        student.setEmail("libai@qq.com");
        student.setAge(20);
        int row = sqlSession.insert(sqlId, student);
        // 需要手动提交事务
        sqlSession.commit();
        System.out.println("插入的数量是:" + row);
        sqlSession.close();

    }
}
