package com.tallate.tinycart.dao;

import com.tallate.tinycart.bean.Order;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao {

    void save(@Param("order") Order order);

    Order query(@Param("orderNo") String orderNo);

    List<Order> list(@Param("userName") String userName);

}
