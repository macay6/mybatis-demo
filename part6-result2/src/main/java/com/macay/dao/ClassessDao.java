package com.macay.dao;

import com.macay.entity.Classess;

/**
 * @ClassName: ClassessDao
 * @Description:
 * @Author: Macay
 * @Date: 2021/5/5 11:41 上午
 */
public interface ClassessDao {
    Classess selectById(Integer id);
}
