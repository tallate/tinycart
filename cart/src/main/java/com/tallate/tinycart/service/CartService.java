package com.tallate.tinycart.service;

import com.google.common.collect.Lists;
import com.tallate.tinycart.bean.Goods;
import com.tallate.tinycart.bean.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class CartService {

    private final Map<String, List<Goods>> goodsMap = new HashMap<>();

    private final Set<String> idpSet = new HashSet<>();

    public void add(String userName, Goods goods) {
        // TODO 加分布式锁
        synchronized (goodsMap) {
            List<Goods> goodsList = goodsMap.computeIfAbsent(userName, k -> new ArrayList<>());
            for (Goods g : goodsList) {
                if (Objects.equals(g.getName(), goods.getName())) {
                    g.setCount(g.getCount() + goods.getCount());
                    return;
                }
            }
            goodsList.add(goods);
        }
    }

    private List<String> getGoodsNames(List<Goods> goodsList) {
        return Optional.ofNullable(goodsList).orElse(Lists.newArrayList()).stream()
                .map(Goods::getName).collect(Collectors.toList());
    }

    public void remove(Order order) {
        // TODO 分布式锁
        synchronized (goodsMap) {
            if (idpSet.contains(order.getOrderNo())) {
                log.info("由于幂等校验被忽略, orderNo:{}", order.getOrderNo());
                return;
            }
            List<Goods> goodsList = goodsMap.get(order.getUserName());
            log.info("处理前, goods:{}", getGoodsNames(goodsList));
            List<Goods> orderGoodsList = order.getGoodsList();
            if (CollectionUtils.isEmpty(goodsList)
                    || CollectionUtils.isEmpty(orderGoodsList)) {
                log.info("goods列表为空，跳过处理");
                return;
            }
            Map<String, Goods> orderGoodsMap = orderGoodsList.stream()
                    .collect(Collectors.toMap(Goods::getName, g -> g));
            goodsList.removeIf(next -> orderGoodsMap.containsKey(next.getName()));
            log.info("处理后, goods:{}", getGoodsNames(goodsList));
            idpSet.add(order.getOrderNo());
        }
    }

    public List<Goods> list(String userName) {
        List<Goods> goods = goodsMap.get(userName);
        return goods == null ? Lists.newArrayList() : goods;
    }
}
