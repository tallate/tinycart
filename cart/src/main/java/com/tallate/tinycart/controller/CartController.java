package com.tallate.tinycart.controller;

import com.tallate.tinycart.bean.Goods;
import com.tallate.tinycart.service.CartService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @RequestMapping("/add")
    public void addItem(@RequestParam String userName, @RequestBody Goods goods) {
        cartService.add(userName, goods);
    }

    @RequestMapping("list")
    public List<Goods> listGoods(@RequestParam String userName) {
        return cartService.list(userName);
    }

}
