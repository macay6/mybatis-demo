package com.macay.Impl;

import com.macay.dao.StudentDao;
import com.macay.entity.Student;
import com.macay.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @ClassName: StudentDaoImpl
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/3 4:45 下午
 */
public class StudentDaoImpl implements StudentDao {
    @Override
    public Student selectStudentById(Integer id) {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        String sqlId = "com.macay.dao.StudentDao"+"."+"selectStudentById";
        Student students = sqlSession.selectOne(sqlId);
        sqlSession.close();
        return students;
    }

    @Override
    public List<Student> selectStudents() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        String sqlId = "com.macay.dao.StudentDao"+"."+"selectStudents";
        List<Student> students = sqlSession.selectList(sqlId);
        sqlSession.close();
        return students;
    }

    @Override
    public void insertStudent(Student student) {
    }
}
