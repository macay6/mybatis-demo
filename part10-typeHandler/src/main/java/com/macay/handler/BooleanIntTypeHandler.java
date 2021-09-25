package com.macay.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName: BooleanIntTypeHandler
 * @Description:
 * @Author: Macay
 * @Date: 2021/9/25 1:01 下午
 */
public class BooleanIntTypeHandler extends BaseTypeHandler<Boolean> {
    @Override
    // 用于定义在Mybatis设置参数时该如何把Java类型的参数转换为对应的数据库类型
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Boolean aBoolean, JdbcType jdbcType) throws SQLException {
        if (aBoolean) {
            preparedStatement.setInt(i, 1);
        } else {
            preparedStatement.setInt(i, 0);
        }
    }

    @Override
    // 用于在Mybatis获取数据结果集时如何把数据库类型转换为对应的Java类型
    public Boolean getNullableResult(ResultSet resultSet, String str) throws SQLException {
        int anInt = resultSet.getInt(str);
        if (anInt == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public Boolean getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
