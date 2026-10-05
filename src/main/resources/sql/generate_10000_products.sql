-- ========================================
-- 批量生成10000条商品测试数据
-- 数据库: fore_shop
-- 执行方式: mysql -u root -p fore_shop < generate_10000_products.sql
-- ========================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 第1步: 插入分类数据（如果不存在）
INSERT IGNORE INTO t_category (name, parent_id, sort, status, deleted, create_time, update_time) VALUES
('数码电子', 0, 1, 1, 0, NOW(), NOW()),
('手机通讯', 1, 1, 1, 0, NOW(), NOW()),
('电脑办公', 1, 2, 1, 0, NOW(), NOW()),
('家用电器', 0, 2, 1, 0, NOW(), NOW()),
('厨房电器', 4, 1, 1, 0, NOW(), NOW()),
('生活电器', 4, 2, 1, 0, NOW(), NOW()),
('服装鞋帽', 0, 3, 1, 0, NOW(), NOW()),
('男装', 7, 1, 1, 0, NOW(), NOW()),
('女装', 7, 2, 1, 0, NOW(), NOW()),
('运动户外', 7, 3, 1, 0, NOW(), NOW()),
('食品饮料', 0, 4, 1, 0, NOW(), NOW()),
('休闲零食', 11, 1, 1, 0, NOW(), NOW()),
('酒水饮品', 11, 2, 1, 0, NOW(), NOW()),
('美妆个护', 0, 5, 1, 0, NOW(), NOW()),
('面部护理', 14, 1, 1, 0, NOW(), NOW()),
('身体护理', 14, 2, 1, 0, NOW(), NOW()),
('图书文具', 0, 6, 1, 0, NOW(), NOW()),
('教材教辅', 17, 1, 1, 0, NOW(), NOW()),
('文学小说', 17, 2, 1, 0, NOW(), NOW());

SELECT '✅ 分类数据准备完成' AS message;

-- 第2步: 创建临时存储过程生成商品数据
DROP PROCEDURE IF EXISTS batch_insert_products;

DELIMITER $$

CREATE PROCEDURE batch_insert_products()
BEGIN
    DECLARE v_counter INT DEFAULT 1;
    DECLARE v_category_id INT;
    DECLARE v_name VARCHAR(200);
    DECLARE v_price DECIMAL(10,2);
    DECLARE v_original_price DECIMAL(10,2);
    DECLARE v_stock INT;
    DECLARE v_sales INT;
    DECLARE v_desc TEXT;
    DECLARE v_image VARCHAR(255);
    DECLARE v_status TINYINT;
    DECLARE v_create_time DATETIME;

    WHILE v_counter <= 10000 DO
        -- 随机选择子分类ID (2-19)
        SET v_category_id = FLOOR(2 + RAND() * 18);

        -- 根据分类ID生成不同的商品名称和价格
        CASE v_category_id
            WHEN 2 THEN -- 手机通讯
                SET v_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 8), '苹果', '华为', '小米', 'OPPO', 'vivo', '三星', '荣耀', '一加'),
                    ELT(1 + FLOOR(RAND() * 5), 'Pro', 'Max', 'Ultra', 'Plus', ''),
                    ' ',
                    2020 + FLOOR(RAND() * 6),
                    '款',
                    IF(RAND() > 0.5, CONCAT(' ', 128 + FLOOR(RAND() * 512), 'GB'), ''),
                    ' 手机'
                );
                SET v_price = ROUND(2000 + RAND() * 8000, 2);

            WHEN 3 THEN -- 电脑办公
                SET v_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 6), '联想', '戴尔', '华硕', '惠普', '苹果', '微软'),
                    ELT(1 + FLOOR(RAND() * 4), '笔记本', '台式机', '一体机', '工作站'),
                    IF(RAND() > 0.5, ' Pro', ''),
                    ' i',
                    5 + FLOOR(RAND() * 9),
                    IF(RAND() > 0.5, CONCAT(' ', 8 + FLOOR(RAND() * 24), 'G'), ''),
                    IF(RAND() > 0.5, CONCAT(' ', 256 + FLOOR(RAND() * 768), 'GB'), '')
                );
                SET v_price = ROUND(3000 + RAND() * 15000, 2);

            WHEN 5 OR 6 THEN -- 家用电器
                SET v_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 8), '美的', '格力', '海尔', '西门子', '方太', '老板', '九阳', '苏泊尔'),
                    IF(RAND() > 0.5, '智能', ''),
                    ELT(1 + FLOOR(RAND() * 6), '电饭煲', '微波炉', '烤箱', '洗碗机', '油烟机', '冰箱')
                );
                SET v_price = ROUND(500 + RAND() * 5000, 2);

            WHEN 8 OR 9 OR 10 THEN -- 服装鞋帽
                SET v_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 8), '优衣库', 'ZARA', '耐克', '阿迪达斯', '李宁', '安踏', '太平鸟', '海澜之家'),
                    ELT(1 + FLOOR(RAND() * 4), '春季', '夏季', '秋季', '冬季'),
                    ELT(1 + FLOOR(RAND() * 6), '新款', '时尚', '经典', '潮流', '舒适', '运动'),
                    ELT(1 + FLOOR(RAND() * 6), 'T恤', '衬衫', '外套', '牛仔裤', '连衣裙', '卫衣')
                );
                SET v_price = ROUND(50 + RAND() * 500, 2);

            WHEN 12 OR 13 THEN -- 食品饮料
                SET v_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 8), '三只松鼠', '良品铺子', '百草味', '旺旺', '康师傅', '统一', '农夫山泉', '可口可乐'),
                    ELT(1 + FLOOR(RAND() * 6), '袋装', '盒装', '瓶装', '罐装', '箱装', '桶装'),
                    ELT(1 + FLOOR(RAND() * 8), '坚果', '饼干', '薯片', '糖果', '饮料', '方便面', '巧克力', '果干'),
                    IF(RAND() > 0.5, CONCAT(' ', 100 + FLOOR(RAND() * 900), 'g'), ''),
                    IF(RAND() > 0.5, CONCAT(' x', 1 + FLOOR(RAND() * 12), '包'), '')
                );
                SET v_price = ROUND(10 + RAND() * 100, 2);

            WHEN 15 OR 16 THEN -- 美妆个护
                SET v_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 8), '欧莱雅', '兰蔻', '雅诗兰黛', '资生堂', 'SK-II', '薇诺娜', '完美日记', '花西子'),
                    ELT(1 + FLOOR(RAND() * 6), '保湿', '美白', '抗皱', '控油', '修护', '清洁'),
                    ELT(1 + FLOOR(RAND() * 6), '精华液', '面霜', '洗面奶', '面膜', '乳液', '爽肤水')
                );
                SET v_price = ROUND(80 + RAND() * 800, 2);

            WHEN 18 OR 19 THEN -- 图书文具
                SET v_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 8), '人民教育出版社', '机械工业出版社', '清华大学出版社', '北京大学出版社', '中信出版社', '磨铁', '读客', '博集天卷'),
                    '《',
                    ELT(1 + FLOOR(RAND() * 10), '高等数学', '线性代数', '概率论', 'Python编程', 'Java从入门到精通', '红楼梦', '西游记', '三体', '活着', '平凡的世界'),
                    '》',
                    ELT(1 + FLOOR(RAND() * 4), '上册', '下册', '修订版', '')
                );
                SET v_price = ROUND(20 + RAND() * 200, 2);

            ELSE -- 其他默认
                SET v_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 10), '高品质', '热销', '新品', '精选', '优质', '超值', '限量', '特惠', '爆款', '推荐'),
                    ELT(1 + FLOOR(RAND() * 10), '时尚', '实用', '便携', '多功能', '智能', '环保', '健康', '安全', '高效', '精美'),
                    ELT(1 + FLOOR(RAND() * 10), '商品A', '商品B', '产品C', '物品D', '好货E', '精品F', '优选G', '特选H', '定制I', '尊享J')
                );
                SET v_price = ROUND(100 + RAND() * 1000, 2);
        END CASE;

        -- 计算原价（比售价高10%-50%）
        SET v_original_price = ROUND(v_price * (1.1 + RAND() * 0.4), 2);

        -- 随机库存、销量、状态
        SET v_stock = FLOOR(RAND() * 1001);
        SET v_sales = FLOOR(RAND() * 5001);
        SET v_status = IF(RAND() > 0.05, 1, 0);  -- 95%上架

        -- 生成描述
        SET v_desc = CONCAT('这是一款优质的', v_name, '。',
            ELT(1 + FLOOR(RAND() * 4),
                '采用先进工艺制造，品质保证。',
                '精选优质材料，经久耐用。',
                '设计精良，性价比高。',
                '深受消费者喜爱，好评如潮。'
            ),
            IF(RAND() > 0.6, '支持七天无理由退换货。', ''),
            IF(RAND() > 0.8, '限时优惠，欲购从速！', '')
        );

        -- 生成图片URL
        SET v_image = CONCAT('https://images.example.com/product/', 10000 + FLOOR(RAND() * 90000), '.jpg');

        -- 生成创建时间（过去一年内的随机时间）
        SET v_create_time = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY);

        -- 插入数据
        INSERT INTO t_product (
            name, category_id, price, original_price,
            stock, sales, description, image,
            status, deleted, create_time, update_time
        ) VALUES (
            v_name, v_category_id, v_price, v_original_price,
            v_stock, v_sales, v_desc, v_image,
            v_status, 0, v_create_time, NOW()
        );

        SET v_counter = v_counter + 1;

        -- 每1000条打印进度
        IF v_counter MOD 1000 = 0 THEN
            SELECT CONCAT('📊 进度: ', v_counter, '/10000 (', ROUND(v_counter * 100 / 10000, 2), '%)') AS progress;
        END IF;
    END WHILE;
END$$

DELIMITER ;

-- 第3步: 执行存储过程
SELECT '🚀 开始生成10000条商品数据...' AS message;
CALL batch_insert_products();

-- 第4步: 清理存储过程
DROP PROCEDURE IF EXISTS batch_insert_products;

-- 第5步: 显示统计结果
SET FOREIGN_KEY_CHECKS = 1;

SELECT '========================================' AS '';
SELECT '✅ 数据生成完成！' AS message;
SELECT '========================================' AS '';

SELECT COUNT(*) AS '总商品数量' FROM t_product WHERE deleted = 0;

SELECT 
    c.name AS '分类名称',
    COUNT(p.id) AS '商品数量',
    ROUND(AVG(p.price), 2) AS '平均价格',
    MIN(p.price) AS '最低价格',
    MAX(p.price) AS '最高价格'
FROM t_product p
LEFT JOIN t_category c ON p.category_id = c.id
WHERE p.deleted = 0
GROUP BY p.category_id, c.name
ORDER BY COUNT(p.id) DESC;

SELECT '' AS '';
SELECT '🎉 恭喜！10000条商品数据已成功生成！' AS success_message;