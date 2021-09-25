package com.macay.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ListStringTypeHandler
 * @Description:
 * @Author: Macay
 * @Date: 2021/9/25 9:13 下午
 */
public class ListStringTypeHandler extends BaseTypeHandler<List<Integer>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Integer> list,
                                    JdbcType jdbcType) throws SQLException {
        StringBuffer sb = new StringBuffer();
        list.forEach(item -> {
            sb.append(String.valueOf(item)).append(",");
        });
        preparedStatement.setString(i, sb.substring(0, sb.length() -1));

    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if(null == resultSet.getString(s) || 0 == resultSet.getString(s).trim().length()){
            return null;
        }
        String string = resultSet.getString(s);
        String[] split = string.split(",");
        List<Integer> list = new ArrayList<>();
        for (String str : split) {
            list.add(Integer.valueOf(str));
        }
        return list;
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
