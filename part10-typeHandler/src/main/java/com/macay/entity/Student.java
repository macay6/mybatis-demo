package com.macay.entity;


import java.util.Date;
import java.util.List;

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
    private Boolean partyMember;
    private Classess cla;
    private List<Integer> hobbies;
    private GenderEnum gender;
    private Date regDate;


    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public List<Integer> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Integer> hobbies) {
        this.hobbies = hobbies;
    }

    public Boolean getPartyMember() {
        return partyMember;
    }

    public void setPartyMember(Boolean partyMember) {
        this.partyMember = partyMember;
    }

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
                ", partyMember=" + partyMember +
                ", cla=" + cla +
                ", hobbies=" + hobbies +
                ", gender=" + gender +
                ", regDate=" + regDate +
                '}';
    }
}
