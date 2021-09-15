package com.macay.entity;

/**
 * @ClassName: Class
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/4 11:56 下午
 */
public class Classess {
    private Integer id;
    private String name;

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

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
