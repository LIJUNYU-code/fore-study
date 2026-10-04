-- ============================================================
-- 小型电商 + 秒杀系统 初始测试数据
-- ============================================================
USE fore_shop;

-- 用户数据 (密码均为 123456 的BCrypt加密结果)
INSERT INTO `t_user` (`username`, `password`, `nickname`, `phone`, `status`) VALUES
('admin',  '$2a$10$qwusrzQQVx1HspO0israwuFxO4ErUc.cbKXP8vvEN84N.Q.QqLZX.', '管理员', '13800000000', 1),
('zhangsan', '$2a$10$qwusrzQQVx1HspO0israwuFxO4ErUc.cbKXP8vvEN84N.Q.QqLZX.', '张三', '13800000001', 1),
('lisi',   '$2a$10$qwusrzQQVx1HspO0israwuFxO4ErUc.cbKXP8vvEN84N.Q.QqLZX.', '李四', '13800000002', 1),
('wangwu', '$2a$10$qwusrzQQVx1HspO0israwuFxO4ErUc.cbKXP8vvEN84N.Q.QqLZX.', '王五', '13800000003', 1);

-- 分类数据
INSERT INTO `t_category` (`name`, `parent_id`, `sort`, `status`) VALUES
('数码电子', 0, 1, 1),
('手机通讯', 1, 1, 1),
('电脑办公', 1, 2, 1),
('服装鞋帽', 0, 2, 1),
('男装',   4, 1, 1),
('女装',   4, 2, 1),
('食品饮料', 0, 3, 1),
('零食',   7, 1, 1),
('饮料',   7, 2, 1);

-- 商品数据
INSERT INTO `t_product` (`name`, `category_id`, `price`, `original_price`, `stock`, `sales`, `description`, `image`, `status`) VALUES
('iPhone 15 Pro Max', 2, 9999.00, 10999.00, 100, 56, 'Apple iPhone 15 Pro Max 256GB 原色钛金属', '/images/iphone15.jpg', 1),
('华为 Mate 60 Pro',  2, 6999.00, 7999.00,  200, 89, '华为 Mate 60 Pro 12GB+256GB 雅丹黑', '/images/mate60.jpg', 1),
('小米14 Ultra',      2, 5999.00, 6499.00,  150, 34, '小米14 Ultra 16GB+512GB 黑色', '/images/mi14.jpg', 1),
('MacBook Pro 14',    3, 14999.00, 15999.00, 50, 22, 'Apple MacBook Pro 14英寸 M3 Pro芯片', '/images/macbook.jpg', 1),
('ThinkPad X1 Carbon',3, 8999.00, 9999.00,  80, 15, '联想 ThinkPad X1 Carbon Gen11 i7', '/images/thinkpad.jpg', 1),
('优衣库羽绒服',      5, 799.00,  1299.00,  500, 200, '优衣库轻型羽绒服 男款 黑色', '/images/uniqlo.jpg', 1),
('耐克Air Max',       6, 899.00,  1199.00,  300, 150, 'Nike Air Max 270 气垫运动鞋', '/images/nike.jpg', 1),
('三只松鼠坚果礼盒',  8, 89.90,   129.90,   1000, 500, '三只松鼠坚果大礼包 1458g', '/images/squirrel.jpg', 1),
('可口可乐330ml*24',  9, 49.90,   59.90,    2000, 800, '可口可乐 经典口味 330ml*24罐装', '/images/cola.jpg', 1),
('卫龙辣条大礼包',    8, 29.90,   39.90,    3000, 1200, '卫龙辣条大礼包 678g', '/images/weilong.jpg', 1);

-- 秒杀活动数据 (时间请根据实际测试调整)
INSERT INTO `t_seckill_activity` (`product_id`, `seckill_price`, `total_stock`, `available_stock`, `start_time`, `end_time`, `status`, `limit_per_user`) VALUES
(2,  4999.00, 10, 10, '2026-09-18 10:00:00', '2026-09-18 12:00:00', 0, 1),
(8,  39.90,   50, 50, '2026-09-18 10:00:00', '2026-09-18 12:00:00', 0, 2),
(10, 9.90,    100, 100, '2026-09-18 10:00:00', '2026-09-18 12:00:00', 0, 3);