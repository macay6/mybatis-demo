package com.macay.dao;

import com.macay.entity.Student;
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

    // dao接口的方法形参是一个简单类型的
    Student selectByEmail(String email);

    /*
     多个简单类型的参数
     使用@Param命名参数， 注解是mybatis提供的
     位置：在形参定义的前面
     属性：value 自定义的参数名称
    */
    List<Student> selectByNameOrAge(@Param("myName") String name, @Param("myAge") Integer age);

    /*
     * 一个java对象作为参数( 对象由属性， 每个属性有set，get方法)
     */
    List<Student> selectByObject(Student student);

    /*
     * 使用位置，获取参数
     */
    List<Student> selectByPosition(String name,Integer age);

    /*
     * 使用一个map作为参数
     */
    List<Student> selectByMap(Map<String, Object> data);

    List<Student> selectStudentOrderByColumn(@Param("name") String name, @Param("column") String column,
                                             @Param("tableName") String tableName);

    List<Student> selectStudents();

    void insertStudent(Student student);

}
