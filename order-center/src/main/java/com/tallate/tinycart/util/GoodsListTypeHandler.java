package com.tallate.tinycart.util;

import com.alibaba.fastjson.JSON;
import com.tallate.tinycart.bean.Goods;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class GoodsListTypeHandler implements TypeHandler<List<Goods>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Goods> goods, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(goods));
    }

    @Override
    public List<Goods> getResult(ResultSet resultSet, String s) throws SQLException {
        return JSON.parseArray(resultSet.getString(s), Goods.class);
    }

    @Override
    public List<Goods> getResult(ResultSet resultSet, int i) throws SQLException {
        return JSON.parseArray(resultSet.getString(i), Goods.class);
    }

    @Override
    public List<Goods> getResult(CallableStatement callableStatement, int i) throws SQLException {
        return JSON.parseArray(callableStatement.getString(i), Goods.class);
    }
}
