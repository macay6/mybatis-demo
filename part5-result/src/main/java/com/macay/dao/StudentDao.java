package com.macay.dao;

import com.macay.entity.Student;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: StudentDao
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/2 1:54 下午
 */
public interface StudentDao {

    Student selectStudentById(Integer id);

    List<Student> selectStudents();

    Map<String, Object> selectStudentById2(Integer id);

    List<Map<String, Object>> selectStudentByAge(Integer id);

    @MapKey("id")
    Map<Integer, Student> selectStudentByAge2(Integer id);


}
