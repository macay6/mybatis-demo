package com.macay.entity;

import com.sun.tools.internal.jxc.gen.config.Classes;

/**
 * @ClassName: Student
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/2 1:49 下午
 */
public class Student {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Classess cla;

    public Classess getCla() {
        return cla;
    }

    public void setCla(Classess cla) {
        this.cla = cla;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", cla=" + cla +
                '}';
    }
}
