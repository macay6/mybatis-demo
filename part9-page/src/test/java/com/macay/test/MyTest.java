package com.macay.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.macay.dao.ClassessDao;
import com.macay.dao.StudentDao;
import com.macay.entity.Classess;
import com.macay.entity.Pager;
import com.macay.entity.Student;
import com.macay.utils.SqlSessionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @ClassName: MyTest
 * @Description:测试mybatis执行sql语句
 * @Author: Macay
 * @Date: 2021/5/2 5:06 下午
 */
public class MyTest {

    @Test
    public void testGetStudentByPage() {
        Pager<Student> studnentByPage = getStudnentByPage(2, 5);
        System.out.println(studnentByPage);
    }

    private Pager<Student> getStudnentByPage(int page, int size) {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Map<String, Object> params = new HashMap<>();
        params.put("startIndex", (page-1)*size);
        params.put("pageSize", size);
        List<Student> students = mapper.getStudentByPage(params);
        Pager<Student> studentPager = new Pager<>();
        studentPager.setPage(page);
        studentPager.setSize(size);
        studentPager.setRows(students);
        studentPager.setTotal(mapper.getCount());
        return studentPager;
    }

    @Test
    public void getStudentByPageHelper() {
        // 集成分页助手插件，测试分页
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        // 设置分页相关参数 当前页+每页显示的条数
        PageHelper.startPage(2, 5);
        List<Student> students = mapper.getStudentByPageHelper();
        // System.out.println(students);
        //获取与分页相关的参数
        PageInfo<Student> pageInfo = new PageInfo<>(students);
        System.out.println("当前页："+pageInfo.getPageNum());
        System.out.println("每页显示条数："+pageInfo.getPageSize());
        System.out.println("总条数："+pageInfo.getTotal());
        System.out.println("总页数："+pageInfo.getPages());
        System.out.println("上一页："+pageInfo.getPrePage());
        System.out.println("下一页："+pageInfo.getNextPage());
        System.out.println("是否是第一页："+pageInfo.isIsFirstPage());
        System.out.println("是否是最后一页："+pageInfo.isIsLastPage());

    }

}
