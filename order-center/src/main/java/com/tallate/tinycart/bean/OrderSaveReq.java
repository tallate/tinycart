package com.tallate.tinycart.bean;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSaveReq implements Serializable {

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 商品列表
     */
    private List<Goods> goodsList;

}
