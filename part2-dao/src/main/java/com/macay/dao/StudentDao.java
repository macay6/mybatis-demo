package com.macay.dao;

import com.macay.entity.Student;

import java.util.List;

/**
 * @ClassName: StudentDao
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/2 1:54 下午
 */
public interface StudentDao {

    Student selectStudentById(Integer id);

    List<Student> selectStudents();

    void insertStudent(Student student);

}
