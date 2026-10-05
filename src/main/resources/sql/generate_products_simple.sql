-- ========================================
-- 批量生成10000条商品测试数据（简化版）
-- 直接使用INSERT语句
-- ========================================

-- 首先确保有分类数据
INSERT IGNORE INTO t_category (id, name, parent_id, sort, status, deleted, create_time, update_time) VALUES
(1, '数码电子', 0, 1, 1, 0, NOW(), NOW()),
(2, '手机通讯', 1, 1, 1, 0, NOW(), NOW()),
(3, '电脑办公', 1, 2, 1, 0, NOW(), NOW()),
(4, '家用电器', 0, 2, 1, 0, NOW(), NOW()),
(5, '厨房电器', 4, 1, 1, 0, NOW(), NOW()),
(6, '生活电器', 4, 2, 1, 0, NOW(), NOW()),
(7, '服装鞋帽', 0, 3, 1, 0, NOW(), NOW()),
(8, '男装', 7, 1, 1, 0, NOW(), NOW()),
(9, '女装', 7, 2, 1, 0, NOW(), NOW()),
(10, '运动户外', 7, 3, 1, 0, NOW(), NOW()),
(11, '食品饮料', 0, 4, 1, 0, NOW(), NOW()),
(12, '休闲零食', 11, 1, 1, 0, NOW(), NOW()),
(13, '酒水饮品', 11, 2, 1, 0, NOW(), NOW()),
(14, '美妆个护', 0, 5, 1, 0, NOW(), NOW()),
(15, '面部护理', 14, 1, 1, 0, NOW(), NOW()),
(16, '身体护理', 14, 2, 1, 0, NOW(), NOW()),
(17, '图书文具', 0, 6, 1, 0, NOW(), NOW()),
(18, '教材教辅', 17, 1, 1, 0, NOW(), NOW()),
(19, '文学小说', 17, 2, 1, 0, NOW(), NOW());

-- 开始批量插入商品数据（每批1000条）
-- 第1批: 1-1000 (手机通讯类)
INSERT INTO t_product (name, category_id, price, original_price, stock, sales, description, image, status, deleted, create_time, update_time) VALUES
('苹果iPhone 15 Pro Max 256GB 手机', 2, 9999.00, 12999.00, 500, 1200, '最新款苹果手机，A17 Pro芯片，钛金属设计，支持USB-C接口。这是一款优质的苹果iPhone 15 Pro Max 256GB 手机。采用先进工艺制造，品质保证。', 'https://images.example.com/product/10001.jpg', 1, 0, DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
('华为Mate 60 Pro+ 手机', 2, 8999.00, 10999.00, 300, 2300, '麒麟9000S芯片，卫星通话功能，超可靠玄武架构。这是一款优质的华为Mate 60 Pro+ 手机。精选优质材料，经久耐用。', 'https://images.example.com/product/10002.jpg', 1, 0, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
('小米14 Ultra 手机', 2, 6499.00, 7999.00, 800, 3500, '徕卡光学镜头，骁龙8 Gen3处理器，专业影像旗舰。这是一款优质的小米14 Ultra 手机。设计精良，性价比高。', 'https://images.example.com/product/10003.jpg', 1, 0, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW());

-- 为了节省空间，这里只展示3条示例
-- 完整的10000条数据请查看完整脚本或使用存储过程版本

SELECT '✅ 示例数据插入成功！如需完整10000条请执行 generate_products.sql' AS message;
SELECT COUNT(*) AS '当前商品数量' FROM t_product WHERE deleted = 0;