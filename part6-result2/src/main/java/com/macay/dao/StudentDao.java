package com.macay.dao;

import com.macay.entity.Student;

/**
 * @ClassName: StudentDao
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/2 1:54 下午
 */
public interface StudentDao {

    Student selectStudentById(Integer id);

    Student selectStudentById2(Integer id);

    Student selectStudentById3(Integer id);

    Student selectStudentByStep(Integer id);

}
