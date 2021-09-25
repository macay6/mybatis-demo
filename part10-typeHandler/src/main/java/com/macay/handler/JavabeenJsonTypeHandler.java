package com.macay.handler;

import com.alibaba.fastjson.JSON;
import com.macay.entity.Classess;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName: JavabeenJsonTypeHandler
 * @Description:
 * @Author: Macay
 * @Date: 2021/9/25 9:58 下午
 */
@MappedTypes(Classess.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JavabeenJsonTypeHandler extends BaseTypeHandler<Classess> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Classess classess, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(classess));
    }

    @Override
    public Classess getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSON.parseObject(resultSet.getString(s), Classess.class);
    }

    @Override
    public Classess getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public Classess getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
