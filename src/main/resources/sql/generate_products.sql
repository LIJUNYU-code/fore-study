-- ========================================
-- 批量生成10000条商品测试数据
-- 数据库: fore_shop
-- 表: t_product
-- ========================================

-- 首先确保有分类数据（如果没有请先运行下面的分类插入语句）
INSERT IGNORE INTO t_category (name, parent_id, sort, status, create_time, update_time) VALUES
('数码电子', 0, 1, 1, NOW(), NOW()),
('手机通讯', 1, 1, 1, NOW(), NOW()),
('电脑办公', 1, 2, 1, NOW(), NOW()),
('家用电器', 0, 2, 1, NOW(), NOW()),
('厨房电器', 4, 1, 1, NOW(), NOW()),
('生活电器', 4, 2, 1, NOW(), NOW()),
('服装鞋帽', 0, 3, 1, NOW(), NOW()),
('男装', 7, 1, 1, NOW(), NOW()),
('女装', 7, 2, 1, NOW(), NOW()),
('运动户外', 7, 3, 1, NOW(), NOW()),
('食品饮料', 0, 4, 1, NOW(), NOW()),
('休闲零食', 10, 1, 1, NOW(), NOW()),
('酒水饮品', 10, 2, 1, NOW(), NOW()),
('美妆个护', 0, 5, 1, NOW(), NOW()),
('面部护理', 13, 1, 1, NOW(), NOW()),
('身体护理', 13, 2, 1, NOW(), NOW()),
('图书文具', 0, 6, 1, NOW(), NOW()),
('教材教辅', 16, 1, 1, NOW(), NOW()),
('文学小说', 16, 2, 1, NOW(), NOW());

-- ========================================
-- 开始生成10000条商品数据
-- 使用存储过程批量插入
-- ========================================

DELIMITER //

CREATE PROCEDURE generate_product_data()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE category_id INT;
    DECLARE product_name VARCHAR(200);
    DECLARE base_price DECIMAL(10,2);
    DECLARE original_price DECIMAL(10,2);
    DECLARE stock INT;
    DECLARE sales INT;
    DECLARE desc_text TEXT;
    
    -- 定义商品名称前缀数组
    DECLARE name_prefixes TEXT;
    
    WHILE i <= 10000 DO
        -- 随机选择分类ID (2-19，排除顶级分类)
        SET category_id = FLOOR(2 + RAND() * 18);
        
        -- 根据分类生成不同的商品名称
        CASE
            WHEN category_id IN (2) THEN
                SET product_name = CONCAT(
                    ELT(FLOOR(1 + RAND() * 8), '苹果', '华为', '小米', 'OPPO', 'vivo', '三星', '荣耀', '一加'),
                    ELT(FLOOR(1 + RAND() * 5), 'Pro', 'Max', 'Ultra', 'Plus', ''),
                    ELT(FLOOR(1 + RAND() * 5), ' ', ' ', '', '', ''),
                    FLOOR(2020 + RAND() * 6), '款',
                    CASE WHEN RAND() > 0.5 THEN CONCAT(' ', FLOOR(128 + RAND() * 512), 'GB') ELSE '' END,
                    ' 手机'
                );
                SET base_price = ROUND((2000 + RAND() * 8000), 2);
                
            WHEN category_id IN (3) THEN
                SET product_name = CONCAT(
                    ELT(FLOOR(1 + RAND() * 6), '联想', '戴尔', '华硕', '惠普', '苹果', '微软'),
                    ELT(FLOOR(1 + RAND() * 4), '笔记本', '台式机', '一体机', '工作站'),
                    ELT(FLOOR(1 + RAND() * 5), ' Pro', ' Max', ' Air', '', ''),
                    CASE WHEN RAND() > 0.5 THEN CONCAT(' i', FLOOR(5 + RAND() * 9)) ELSE '' END,
                    CASE WHEN RAND() > 0.5 THEN CONCAT(' ', FLOOR(8 + RAND() * 24), 'G') ELSE '' END,
                    CASE WHEN RAND() > 0.5 THEN CONCAT(' ', FLOOR(256 + RAND() * 768), 'GB') ELSE '' END
                );
                SET base_price = ROUND((3000 + RAND() * 15000), 2);
                
            WHEN category_id IN (5, 6) THEN
                SET product_name = CONCAT(
                    ELT(FLOOR(1 + RAND() * 8), '美的', '格力', '海尔', '西门子', '方太', '老板', '九阳', '苏泊尔'),
                    ELT(FLOOR(1 + RAND() * 6), '智能', '全自动', '变频', '静音', '节能', ''),
                    ELT(FLOOR(1 + RAND() * 6), '电饭煲', '微波炉', '烤箱', '洗碗机', '油烟机', '冰箱')
                );
                SET base_price = ROUND((500 + RAND() * 5000), 2);
                
            WHEN category_id IN (8, 9, 10) THEN
                SET product_name = CONCAT(
                    ELT(FLOOR(1 + RAND() * 8), '优衣库', 'ZARA', '耐克', '阿迪达斯', '李宁', '安踏', '太平鸟', '海澜之家'),
                    ELT(FLOOR(1 + RAND() * 4), '春季', '夏季', '秋季', '冬季'),
                    ELT(FLOOR(1 + RAND() * 6), '新款', '时尚', '经典', '潮流', '舒适', '运动'),
                    ELT(FLOOR(1 + RAND() * 6), 'T恤', '衬衫', '外套', '牛仔裤', '连衣裙', '卫衣')
                );
                SET base_price = ROUND((50 + RAND() * 500), 2);
                
            WHEN category_id IN (11, 12) THEN
                SET product_name = CONCAT(
                    ELT(FLOOR(1 + RAND() * 8), '三只松鼠', '良品铺子', '百草味', '旺旺', '康师傅', '统一', '农夫山泉', '可口可乐'),
                    ELT(FLOOR(1 + RAND() * 6), '袋装', '盒装', '瓶装', '罐装', '箱装', '桶装'),
                    ELT(FLOOR(1 + RAND() * 8), '坚果', '饼干', '薯片', '糖果', '饮料', '方便面', '巧克力', '果干'),
                    CASE WHEN RAND() > 0.5 THEN CONCAT(' ', FLOOR(100 + RAND() * 900), 'g') ELSE '' END,
                    CASE WHEN RAND() > 0.5 THEN CONCAT(' x', FLOOR(1 + RAND() * 12), '包') ELSE '' END
                );
                SET base_price = ROUND((10 + RAND() * 100), 2);
                
            WHEN category_id IN (14, 15) THEN
                SET product_name = CONCAT(
                    ELT(FLOOR(1 + RAND() * 8), '欧莱雅', '兰蔻', '雅诗兰黛', '资生堂', 'SK-II', '薇诺娜', '完美日记', '花西子'),
                    ELT(FLOOR(1 + RAND() * 6), '保湿', '美白', '抗皱', '控油', '修护', '清洁'),
                    ELT(FLOOR(1 + RAND() * 6), '精华液', '面霜', '洗面奶', '面膜', '乳液', '爽肤水')
                );
                SET base_price = ROUND((80 + RAND() * 800), 2);
                
            WHEN category_id IN (17, 18) THEN
                SET product_name = CONCAT(
                    ELT(FLOOR(1 + RAND() * 8), '人民教育出版社', '机械工业出版社', '清华大学出版社', '北京大学出版社', '中信出版社', '磨铁', '读客', '博集天卷'),
                    ELT(FLOOR(1 + RAND() * 6), '《', '', '', '', '', ''),
                    ELT(FLOOR(1 + RAND() * 10), '高等数学', '线性代数', '概率论', 'Python编程', 'Java从入门到精通', '红楼梦', '西游记', '三体', '活着', '平凡的世界'),
                    ELT(FLOOR(1 + RAND() * 4), '》上册', '》下册', '》第', '》修订版', '')
                );
                SET base_price = ROUND((20 + RAND() * 200), 2);
                
            ELSE
                SET product_name = CONCAT(
                    ELT(FLOOR(1 + RAND() * 10), '高品质', '热销', '新品', '精选', '优质', '超值', '限量', '特惠', '爆款', '推荐'),
                    ELT(FLOOR(1 + RAND() * 10), '时尚', '实用', '便携', '多功能', '智能', '环保', '健康', '安全', '高效', '精美'),
                    ELT(FLOOR(1 + RAND() * 15), '商品A', '商品B', '产品C', '物品D', '好货E', '精品F', '优选G', '特选H', '定制I', '尊享J', '豪华K', '标准L', '升级M', '旗舰N', '至尊O')
                );
                SET base_price = ROUND((100 + RAND() * 1000), 2);
        END CASE;
        
        -- 计算原价（比售价高10%-50%）
        SET original_price = ROUND(base_price * (1.1 + RAND() * 0.4), 2);
        
        -- 随机库存 (0-1000)
        SET stock = FLOOR(RAND() * 1001);
        
        -- 随机销量 (0-5000)
        SET sales = FLOOR(RAND() * 5001);
        
        -- 生成描述文本
        SET desc_text = CONCAT(
            '这是一款优质的', product_name, '。',
            CASE 
                WHEN RAND() > 0.7 THEN '采用先进工艺制造，品质保证。'
                WHEN RAND() > 0.5 THEN '精选优质材料，经久耐用。'
                WHEN RAND() > 0.3 THEN '设计精良，性价比高。'
                ELSE '深受消费者喜爱，好评如潮。'
            END,
            CASE 
                WHEN RAND() > 0.6 THEN '支持七天无理由退换货。'
                ELSE ''
            END,
            CASE 
                WHEN RAND() > 0.8 THEN '限时优惠，欲购从速！'
                ELSE ''
            END
        );
        
        -- 插入数据
        INSERT INTO t_product (
            name, category_id, price, original_price, 
            stock, sales, description, image, 
            status, deleted, create_time, update_time
        ) VALUES (
            product_name,
            category_id,
            base_price,
            original_price,
            stock,
            sales,
            desc_text,
            CONCAT('https://images.example.com/product/', FLOOR(10000 + RAND() * 90000), '.jpg'),
            IF(RAND() > 0.05, 1, 0),  -- 95%的商品上架
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY),
            NOW()
        );
        
        SET i = i + 1;
        
        -- 每1000条提交一次事务，避免锁表时间过长
        IF i MOD 1000 = 0 THEN
            COMMIT;
        END IF;
    END WHILE;
END //

DELIMITER ;

-- 执行存储过程生成数据
CALL generate_product_data();

-- 删除存储过程
DROP PROCEDURE IF EXISTS generate_product_data;

-- 显示结果
SELECT COUNT(*) AS '总商品数量' FROM t_product WHERE deleted = 0;
SELECT 
    c.name AS '分类名称',
    COUNT(p.id) AS '商品数量',
    AVG(p.price) AS '平均价格',
    MIN(p.price) AS '最低价格',
    MAX(p.price) AS '最高价格'
FROM t_product p
LEFT JOIN t_category c ON p.category_id = c.id
WHERE p.deleted = 0
GROUP BY p.category_id, c.name
ORDER BY COUNT(p.id) DESC;

-- 完成
SELECT '✅ 成功生成10000条商品数据！' AS message;