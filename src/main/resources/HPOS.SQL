-- ============================================
-- 医院挂号系统 - SQL文件
-- 版本：2.0
-- 适用：Spring Boot 后端 + Vue 前端
-- ============================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `hospital_system`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `hospital_system`;

-- 2. 关闭外键约束（导入时）
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 3. 核心表结构（6张表）
-- ============================================

-- 3.1 科室表
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '科室ID',
  `dept_name` varchar(50) NOT NULL COMMENT '科室名称',
  `introduction` varchar(200) DEFAULT NULL COMMENT '简介',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：1正常 0停用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='科室表';

-- 3.2 医生表
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '医生ID',
  `real_name` varchar(20) NOT NULL COMMENT '姓名',
  `title` varchar(20) DEFAULT NULL COMMENT '职称',
  `specialty` varchar(100) DEFAULT NULL COMMENT '擅长',
  `dept_id` int NOT NULL COMMENT '科室ID',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `status` tinyint DEFAULT 1 COMMENT '状态：1正常 0停诊',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB COMMENT='医生表';

-- 3.3 号源表
DROP TABLE IF EXISTS `registration_source`;
CREATE TABLE `registration_source` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '号源ID',
  `doctor_id` int NOT NULL COMMENT '医生ID',
  `work_date` date NOT NULL COMMENT '日期',
  `period` tinyint NOT NULL COMMENT '时段：1上午 2下午',
  `total_count` int DEFAULT 20 COMMENT '总号数',
  `available_count` int DEFAULT 20 COMMENT '剩余号数',
  `fee` decimal(8,2) DEFAULT 0.00 COMMENT '费用',
  `status` tinyint DEFAULT 1 COMMENT '状态：1可约 0停约',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_doctor_date_period` (`doctor_id`, `work_date`, `period`)
) ENGINE=InnoDB COMMENT='号源表';

-- 3.4 患者表
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '患者ID',
  `real_name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` tinyint DEFAULT 1 COMMENT '性别：1男 2女',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB COMMENT='患者表';

-- 3.5 挂号订单表
DROP TABLE IF EXISTS `registration_order`;
CREATE TABLE `registration_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(20) NOT NULL COMMENT '订单号',
  `patient_id` int NOT NULL COMMENT '患者ID',
  `source_id` int NOT NULL COMMENT '号源ID',
  `doctor_id` int NOT NULL COMMENT '医生ID',
  `dept_id` int NOT NULL COMMENT '科室ID',
  `work_date` date NOT NULL COMMENT '就诊日期',
  `period` tinyint NOT NULL COMMENT '时段',
  `fee` decimal(8,2) NOT NULL COMMENT '费用',
  `status` tinyint DEFAULT 0 COMMENT '状态：0待支付 1已支付 2已取消 3已就诊',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_patient_id` (`patient_id`),
  KEY `idx_source_id` (`source_id`)
) ENGINE=InnoDB COMMENT='挂号订单表';

-- 3.6 用户表（登录用）
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB COMMENT='用户表';

-- 4. 添加外键约束
ALTER TABLE `doctor` ADD CONSTRAINT `fk_doctor_dept` FOREIGN KEY (`dept_id`) REFERENCES `department` (`id`);
ALTER TABLE `registration_source` ADD CONSTRAINT `fk_source_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`);
ALTER TABLE `registration_order` ADD CONSTRAINT `fk_order_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`);
ALTER TABLE `registration_order` ADD CONSTRAINT `fk_order_source` FOREIGN KEY (`source_id`) REFERENCES `registration_source` (`id`);

-- 5. 开启外键约束
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 6. 插入测试数据
-- ============================================

-- 6.1 插入3个科室
INSERT INTO `department` (dept_name, introduction, sort_order) VALUES
    ('内科', '治疗内科常见疾病，如感冒、高血压、糖尿病等', 1),
    ('外科', '开展各类外科手术，包括微创手术', 2),
    ('儿科', '儿童疾病诊治，专业儿科医生', 3);

-- 6.2 插入5个医生
INSERT INTO `doctor` (real_name, title, specialty, dept_id, avatar) VALUES
    ('张明', '主任医师', '心血管疾病、高血压', 1, 'doctor1.jpg'),
    ('李华', '副主任医师', '消化内科、胃肠疾病', 1, 'doctor2.jpg'),
    ('王强', '主任医师', '骨科手术、创伤修复', 2, 'doctor3.jpg'),
    ('刘芳', '副主任医师', '小儿呼吸、儿科常见病', 3, 'doctor4.jpg'),
    ('陈伟', '主治医师', '小儿保健、营养指导', 3, 'doctor5.jpg');

-- 6.3 插入3个患者
INSERT INTO `patient` (real_name, gender, phone, id_card, birthday) VALUES
    ('张三', 1, '13800138001', '110101199001011234', '1990-01-01'),
    ('李四', 2, '13800138002', '110101199002021235', '1990-02-02'),
    ('王五', 1, '13800138003', '110101199003031236', '1990-03-03');

-- 6.4 插入测试用户（密码统一为 123456 的 MD5 值）
INSERT INTO `sys_user` (username, password, phone) VALUES
    ('zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '13800138001'),
    ('lisi', 'e10adc3949ba59abbe56e057f20f883e', '13800138002'),
    ('admin', 'e10adc3949ba59abbe56e057f20f883e', '13800138000');

-- 6.5 插入未来3天的号源
INSERT INTO `registration_source` (doctor_id, work_date, period, total_count, available_count, fee) VALUES
    -- 张明医生（内科）
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 20, 20, 15.00),
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 2, 20, 20, 15.00),
    (1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1, 20, 20, 15.00),
    (1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 2, 20, 20, 15.00),
    -- 李华医生（内科）
    (2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 20, 20, 12.00),
    (2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 2, 20, 20, 12.00),
    -- 王强医生（外科）
    (3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1, 20, 20, 20.00),
    (3, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 2, 20, 20, 20.00),
    -- 刘芳医生（儿科）
    (4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 20, 20, 10.00),
    (4, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 2, 20, 20, 10.00),
    -- 陈伟医生（儿科）
    (5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1, 20, 20, 8.00),
    (5, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 2, 20, 20, 8.00);

-- 6.6 插入1个测试订单
INSERT INTO `registration_order` (order_no, patient_id, source_id, doctor_id, dept_id, work_date, period, fee, status) VALUES
    ('REG2026000001', 1, 1, 1, 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 15.00, 1);

-- ============================================
-- 7. 创建视图
-- ============================================

-- 7.1 号源详情视图
CREATE OR REPLACE VIEW v_doctor_schedule AS
SELECT
    rs.id AS source_id,
    rs.work_date,
    rs.period,
    CASE rs.period
        WHEN 1 THEN '上午'
        WHEN 2 THEN '下午'
        ELSE '其他'
    END AS period_text,
    rs.total_count,
    rs.available_count,
    rs.fee,
    rs.status,
    d.id AS doctor_id,
    d.real_name AS doctor_name,
    d.title AS doctor_title,
    d.specialty,
    d.avatar,
    dept.id AS dept_id,
    dept.dept_name
FROM registration_source rs
JOIN doctor d ON rs.doctor_id = d.id
JOIN department dept ON d.dept_id = dept.id
WHERE rs.status = 1
  AND rs.work_date >= CURDATE()
  AND rs.available_count > 0
ORDER BY rs.work_date, rs.period, d.real_name;

-- 7.2 医生排班视图
CREATE OR REPLACE VIEW v_doctor_work_schedule AS
SELECT
    d.id AS doctor_id,
    d.real_name AS doctor_name,
    d.title,
    dept.dept_name,
    rs.work_date,
    rs.period,
    rs.available_count
FROM doctor d
JOIN department dept ON d.dept_id = dept.id
LEFT JOIN registration_source rs ON d.id = rs.doctor_id AND rs.work_date >= CURDATE()
WHERE d.status = 1
ORDER BY dept.sort_order, d.real_name, rs.work_date;

-- ============================================
-- 8. 创建索引
-- ============================================

CREATE INDEX idx_source_date ON registration_source(work_date, status);
CREATE INDEX idx_source_doctor ON registration_source(doctor_id, work_date, period);
CREATE INDEX idx_order_created ON registration_order(create_time DESC);
CREATE INDEX idx_order_patient ON registration_order(patient_id, work_date DESC);

-- ============================================
-- 9. 查看数据
-- ============================================

SELECT '科室表' AS table_name, COUNT(*) AS count FROM department
UNION ALL
SELECT '医生表', COUNT(*) FROM doctor
UNION ALL
SELECT '患者表', COUNT(*) FROM patient
UNION ALL
SELECT '号源表', COUNT(*) FROM registration_source
UNION ALL
SELECT '订单表', COUNT(*) FROM registration_order
UNION ALL
SELECT '用户表', COUNT(*) FROM sys_user;
