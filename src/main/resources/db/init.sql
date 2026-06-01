-- =============================================
-- 线上宠物领养平台 数据库初始化脚本
-- =============================================

-- 0. 设置字符集，防止中文乱码和默认值无效
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS pet_adoption
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE pet_adoption;

-- 2. 清除已有表（按依赖反序）
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS adoption;
DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS user;

-- 3. 用户表
CREATE TABLE user (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    account   VARCHAR(50)  NOT NULL UNIQUE COMMENT '登录账号',
    password  VARCHAR(100) NOT NULL COMMENT '加密密码',
    nickname  VARCHAR(50)  DEFAULT NULL,
    phone     VARCHAR(20)  DEFAULT NULL,
    role      INT          DEFAULT 0 COMMENT '0普通用户 1管理员',
    status    INT          DEFAULT 0 COMMENT '0正常 1禁用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 4. 宠物表
CREATE TABLE pet (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    species     VARCHAR(20)  DEFAULT NULL,
    breed       VARCHAR(50)  DEFAULT NULL,
    age         INT          DEFAULT NULL,
    image       VARCHAR(255) DEFAULT NULL,
    description TEXT         DEFAULT NULL,
    health      VARCHAR(100) DEFAULT NULL,
    status      VARCHAR(20)  DEFAULT '待领养' COMMENT '待领养/已领养/下架',
    add_time    DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物信息表';

-- 5. 领养申请表
CREATE TABLE adoption (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    pet_id      INT          NOT NULL,
    user_id     INT          NOT NULL,
    reason      TEXT         DEFAULT NULL,
    address     VARCHAR(200) DEFAULT NULL,
    status      VARCHAR(20)  DEFAULT '待审核' COMMENT '待审核/通过/驳回/已撤回',
    apply_time  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    audit_time  DATETIME     DEFAULT NULL,
    FOREIGN KEY (pet_id)  REFERENCES pet(id)  ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领养申请表';

-- 6. 留言评论表
CREATE TABLE comment (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    pet_id      INT      NOT NULL,
    user_id     INT      NOT NULL,
    content     TEXT     NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id)  REFERENCES pet(id)  ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='留言评论表';

-- 7. 插入初始数据（密码使用固定的 MD5 值，123456 -> e10adc3949ba59abbe56e057f20f883e）
INSERT INTO user (account, password, nickname, role, status)
VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 1, 0);

INSERT INTO user (account, password, nickname, phone, role, status)
VALUES ('testuser', 'e10adc3949ba59abbe56e057f20f883e', '爱宠人士', '13800138000', 0, 0);

INSERT INTO pet (name, species, breed, age, image, description, health, status)
VALUES
('小白', '猫', '英短', 2, '/images/pet1.jpg', '性格温顺，已驱虫', '健康', '待领养'),
('旺财', '狗', '金毛', 3, '/images/pet2.jpg', '活泼好动，会基本指令', '良好', '待领养'),
('小黄', '猫', '橘猫', 1, '/images/pet3.jpg', '小橘猫一只，爱吃爱睡', '健康', '待领养');

-- 8. 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;