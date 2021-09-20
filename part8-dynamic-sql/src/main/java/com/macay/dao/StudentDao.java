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
    List<Student> selectStudentIfInWhere(Student student);

    int updateStudentByIdSelective(Student student);

    int insertStudent(Student student);

    Student selectById(int id);

    Student selectByIdOrName(Student student);

    Student selectByIdOrName2(Student student);

    List<Student> selectByIdList(List<Integer> idList);

    List<Student> selectByIdList2(List<Student> students);

    List<Student> selectByIdArray(Integer[] idArray);

    int insertStudentBatch(List<Student> students);

    int updateStudentByMap(Map<String, Object> map);
}
