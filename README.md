  _____               _   _      __   __   ____     _       ____     _____   
 |_ " _|     ___     | \ |"|     \ \ / /U /"___|U  /"\  uU |  _"\ u |_ " _|  
   | |      |_"_|   <|  \| |>     \ V / \| | u   \/ _ \/  \| |_) |/   | |    
  /| |\      | |    U| |\  |u    U_|"|_u | |/__  / ___ \   |  _ <    /| |\   
 u |_|U    U/| |\u   |_| \_|       |_|    \____|/_/   \_\  |_| \_\  u |_|U   
 _// \\_.-,_|___|_,-.||   \\,-..-,//|(_  _// \\  \\    >>  //   \\_ _// \\_  
(__) (__)\_)-' '-(_/ (_")  (_/  \_) (__)(__)(__)(__)  (__)(__)  (__)__) (__) 

模拟一下加购物车、下单操作，测试RocketMQ事务消息。

# 实现
## 依赖的中间件
MySQL、RocketMQ，按默认方式建就完事了。

## 数据库表
```
create database tinycart;

create table `order` (
  order_no varchar(50) primary key,
  user_name varchar(20) not null default '',
  goods_list text
) default charset = utf8mb4 engine = InnoDB;
```

## 加购物车
加购物车即加入到一个本地缓存：
`com.tallate.tinycart.service.CartService.add`
添加购物车：
```
127.0.0.1:8081/cart/add?userName=Mike
{
	"name": "apple",
	"price": 1.5,
	"count": 3
}
```
查询购物车：
```
127.0.0.1:8081/cart/list?userName=Mike
```

## 下单
下单是发消息通知购物车服务，
`com.tallate.tinycart.service.OrderServiceImpl.save`
下单：
```
127.0.0.1:8080/order/save
{
	"userName": "Mike",
	"goodsList": [
		{
			"name": "apple",
			"price": 1.5,
			"count": 4
		}
	]
}
```
查询订单：
```
127.0.0.1:8080/order/query?orderNo=05be5370-f77e-429c-bcfa-750de84e27ed
```
