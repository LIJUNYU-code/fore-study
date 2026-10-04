-- ============================================================
-- 小型电商 + 秒杀系统 数据库建表脚本
-- Database: fore_shop
-- ============================================================

CREATE DATABASE IF NOT EXISTS fore_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE fore_shop;

-- -----------------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(128) NOT NULL COMMENT '密码(加密存储)',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- -----------------------------------------------------------
-- 2. 商品分类表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name`        VARCHAR(50)  NOT NULL COMMENT '分类名称',
    `parent_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '父分类ID, 0表示顶级分类',
    `sort`        INT          NOT NULL DEFAULT 0 COMMENT '排序值, 越小越靠前',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- -----------------------------------------------------------
-- 3. 商品表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
    `id`            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `name`          VARCHAR(200)  NOT NULL COMMENT '商品名称',
    `category_id`   BIGINT        NOT NULL COMMENT '分类ID',
    `price`         DECIMAL(10,2) NOT NULL COMMENT '售价',
    `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
    `stock`         INT           NOT NULL DEFAULT 0 COMMENT '库存数量',
    `sales`         INT           NOT NULL DEFAULT 0 COMMENT '已售数量',
    `description`   TEXT          DEFAULT NULL COMMENT '商品描述',
    `image`         VARCHAR(255)  DEFAULT NULL COMMENT '主图URL',
    `status`        TINYINT       NOT NULL DEFAULT 1 COMMENT '状态: 0-下架 1-上架',
    `deleted`       TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    `create_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- -----------------------------------------------------------
-- 4. 购物车表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `t_cart`;
CREATE TABLE `t_cart` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `user_id`     BIGINT   NOT NULL COMMENT '用户ID',
    `product_id`  BIGINT   NOT NULL COMMENT '商品ID',
    `quantity`    INT      NOT NULL DEFAULT 1 COMMENT '商品数量',
    `selected`    TINYINT  NOT NULL DEFAULT 1 COMMENT '是否选中: 0-未选中 1-选中',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- -----------------------------------------------------------
-- 5. 订单表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
    `id`           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no`     VARCHAR(32)   NOT NULL COMMENT '订单编号',
    `user_id`      BIGINT        NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `pay_amount`   DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    `pay_time`     DATETIME      DEFAULT NULL COMMENT '支付时间',
    `status`       TINYINT       NOT NULL DEFAULT 0 COMMENT '订单状态: 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-已退款',
    `remark`       VARCHAR(255)  DEFAULT NULL COMMENT '订单备注',
    `deleted`      TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    `create_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- -----------------------------------------------------------
-- 6. 订单项表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item` (
    `id`            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
    `order_id`      BIGINT        NOT NULL COMMENT '订单ID',
    `product_id`    BIGINT        NOT NULL COMMENT '商品ID',
    `product_name`  VARCHAR(200)  NOT NULL COMMENT '商品名称(快照)',
    `product_image` VARCHAR(255)  DEFAULT NULL COMMENT '商品图片(快照)',
    `price`         DECIMAL(10,2) NOT NULL COMMENT '商品单价(快照)',
    `quantity`      INT           NOT NULL COMMENT '购买数量',
    `create_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- -----------------------------------------------------------
-- 7. 秒杀活动表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `t_seckill_activity`;
CREATE TABLE `t_seckill_activity` (
    `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '秒杀活动ID',
    `product_id`      BIGINT        NOT NULL COMMENT '关联商品ID',
    `seckill_price`   DECIMAL(10,2) NOT NULL COMMENT '秒杀价格',
    `total_stock`     INT           NOT NULL COMMENT '秒杀总库存',
    `available_stock` INT           NOT NULL COMMENT '剩余可用库存',
    `start_time`      DATETIME      NOT NULL COMMENT '活动开始时间',
    `end_time`        DATETIME      NOT NULL COMMENT '活动结束时间',
    `status`          TINYINT       NOT NULL DEFAULT 0 COMMENT '活动状态: 0-未开始 1-进行中 2-已结束',
    `limit_per_user`  INT           NOT NULL DEFAULT 1 COMMENT '每用户限购数量',
    `deleted`         TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    `create_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀活动表';

-- -----------------------------------------------------------
-- 8. 秒杀订单表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `t_seckill_order`;
CREATE TABLE `t_seckill_order` (
    `id`                 BIGINT   NOT NULL AUTO_INCREMENT COMMENT '秒杀订单ID',
    `seckill_activity_id` BIGINT   NOT NULL COMMENT '秒杀活动ID',
    `user_id`            BIGINT   NOT NULL COMMENT '用户ID',
    `order_id`           BIGINT   NOT NULL COMMENT '关联订单ID',
    `create_time`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_user` (`seckill_activity_id`, `user_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀订单表';