package com.macay.dao;

import com.macay.entity.NewStudent;

/**
 * @ClassName: NewStudentDao
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/4 10:36 下午
 */
public interface NewStudentDao {

    NewStudent selectById(Integer id);

    NewStudent selectById2(Integer id);
}
