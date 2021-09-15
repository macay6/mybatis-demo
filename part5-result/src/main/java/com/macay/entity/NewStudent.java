package com.macay.entity;

/**
 * @ClassName: NewStudent
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/4 10:32 下午
 */
public class NewStudent {
    private Integer sid;
    private String sName;
    private String email;
    private Integer age;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
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
        return "NewStudent{" +
                "sid=" + sid +
                ", sName='" + sName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
