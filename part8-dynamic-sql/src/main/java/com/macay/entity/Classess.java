package com.macay.entity;

import java.util.List;

/**
 * @ClassName: Class
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/4 11:56 下午
 */
public class Classess {
    private Integer id;
    private String name;
    private List<Student> students;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Classess{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", students=" + students +
                '}';
    }
}
