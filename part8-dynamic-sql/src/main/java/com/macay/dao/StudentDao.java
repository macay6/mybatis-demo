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
    List<Student> selectStudentIfInWhere(Student student);

    int updateStudentByIdSelective(Student student);

    int insertStudent(Student student);
}
