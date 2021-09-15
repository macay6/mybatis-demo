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

    Student selectStudentById2(Integer id);

    Student selectStudentByStep(Integer id);

    List<Student> selectStudentByCid(Integer id);

    List<Student> selectStudentLikeName(String name);

    List<Student> selectStudentLikeName2(String name);

    List<Student> selectStudentLikeName3(String name);

}
