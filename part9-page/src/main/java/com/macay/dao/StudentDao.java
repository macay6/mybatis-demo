package com.macay.dao;

import com.macay.entity.Student;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: StudentDao
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/2 1:54 下午
 */
public interface StudentDao {

    List<Student> getStudentByPage(Map<String, Object> params);

    List<Student> getStudentByPageHelper();

    long getCount();

}
