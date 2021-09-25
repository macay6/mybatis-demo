package com.macay.handler;

import com.macay.entity.GenderEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName: MenuIntTypeHandler
 * @Description:
 * @Author: Macay
 * @Date: 2021/9/25 10:52 下午
 */
public class MenuIntTypeHandler extends BaseTypeHandler<GenderEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, GenderEnum genderEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, genderEnum.getCode());
    }

    @Override
    public GenderEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int anInt = resultSet.getInt(s);
        if (anInt == 1) {
            return GenderEnum.getGenderEnum(1);
        } else if (anInt == 2) {
            return GenderEnum.getGenderEnum(2);
        }
        return null;
    }

    @Override
    public GenderEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public GenderEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
