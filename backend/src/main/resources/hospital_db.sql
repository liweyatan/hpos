/*
 Navicat Premium Dump SQL

 Source Server         : hospital_db
 Source Server Type    : MySQL
 Source Server Version : 90200 (9.2.0)
 Source Host           : localhost:3306
 Source Schema         : hospital_db

 Target Server Type    : MySQL
 Target Server Version : 90200 (9.2.0)
 File Encoding         : 65001

 Date: 31/12/2025 23:22:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '科室ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '科室名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '科室描述',
  `director` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '科室负责人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '科室位置',
  `active` int NULL DEFAULT 1 COMMENT '是否启用（1:启用，0:禁用）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_department_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '科室信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '内科', '负责消化、呼吸、心血管等系统疾病的诊疗', '张主任', '010-88881111', '门诊楼3层', 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `department` VALUES (2, '外科', '负责手术及外伤处理', '李主任', '010-88882222', '门诊楼4层', 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `department` VALUES (3, '全科', '常见病、多发病的初步诊疗和健康管理', '王主任', '010-88883333', '门诊楼1层', 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `department` VALUES (4, '儿科', '儿童疾病诊疗与健康管理', '赵主任', '010-88884444', '儿科楼2层', 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `department` VALUES (5, '妇产科', '女性疾病诊疗与生育健康', '陈主任', '010-88885555', '妇产科楼3层', 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `department` VALUES (6, '眼科', '眼部疾病诊疗与视力保健', '刘主任', '010-88886666', '门诊楼5层', 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '医生ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '医生姓名',
  `department_id` bigint NOT NULL COMMENT '所属科室ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职称',
  `specialty` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专长',
  `max_patients` int NULL DEFAULT 20 COMMENT '单日最大接诊数',
  `current_patients` int NULL DEFAULT 0 COMMENT '当前已挂号数',
  `available` tinyint(1) NULL DEFAULT 1 COMMENT '是否可预约',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `department_id`(`department_id` ASC) USING BTREE,
  CONSTRAINT `doctor_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '医生信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of doctor
-- ----------------------------
INSERT INTO `doctor` VALUES (1, '张医生', 1, '主任医师', '心血管疾病', 20, 0, 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `doctor` VALUES (2, '李医生', 2, '副主任医师', '普外科', 20, 0, 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `doctor` VALUES (3, '王医生', 3, '主治医师', '全科诊疗', 20, 0, 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `doctor` VALUES (4, '赵医生', 4, '副主任医师', '儿科常见疾病', 20, 0, 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `doctor` VALUES (5, '陈医生', 5, '主任医师', '妇产科疾病', 20, 0, 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');
INSERT INTO `doctor` VALUES (6, '刘医生', 6, '主治医师', '眼科疾病', 20, 0, 1, '2025-12-20 18:23:54', '2025-12-20 18:23:54');

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '患者ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '患者姓名',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `birth_date` date NULL DEFAULT NULL COMMENT '出生日期',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_patient_id_card`(`id_card` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '患者信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of patient
-- ----------------------------
INSERT INTO `patient` VALUES (1, '张三', '110101199001011234', '13800138000', NULL, NULL, '2025-12-20 18:25:51', '2025-12-20 18:25:51');
INSERT INTO `patient` VALUES (4, 'kobe1', '', '13944445556', NULL, NULL, '2025-12-31 18:52:11', '2025-12-31 18:52:11');
INSERT INTO `patient` VALUES (5, '管理员', '123445567777', '13944445555', NULL, NULL, '2025-12-31 22:19:19', '2025-12-31 22:19:19');
INSERT INTO `patient` VALUES (10, '科比', NULL, '13800138888', NULL, NULL, '2025-12-31 23:07:33', '2025-12-31 23:07:33');

-- ----------------------------
-- Table structure for registration_order
-- ----------------------------
DROP TABLE IF EXISTS `registration_order`;
CREATE TABLE `registration_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `patient_id` bigint NOT NULL COMMENT '患者ID',
  `doctor_id` bigint NOT NULL COMMENT '医生ID',
  `register_time` datetime NOT NULL COMMENT '预约就诊时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PENDING' COMMENT '订单状态（PENDING:待处理, CONFIRMED:已确认, CANCELLED:已取消, COMPLETED:已完成）',
  `symptoms` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '症状描述',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注信息',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `patient_id`(`patient_id` ASC) USING BTREE,
  INDEX `doctor_id`(`doctor_id` ASC) USING BTREE,
  INDEX `idx_register_time`(`register_time` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  CONSTRAINT `registration_order_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `registration_order_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '挂号订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of registration_order
-- ----------------------------
INSERT INTO `registration_order` VALUES (1, 1, 1, '2025-12-22 09:00:00', 'pending', '头痛', NULL, '2025-12-20 18:25:51', '2025-12-20 18:25:51');
INSERT INTO `registration_order` VALUES (2, 1, 1, '2025-12-25 09:00:00', 'pending', '头痛', NULL, '2025-12-20 18:30:21', '2025-12-20 18:30:21');
INSERT INTO `registration_order` VALUES (3, 1, 1, '2025-12-21 09:00:00', 'pending', '感冒发烧', NULL, '2025-12-20 18:51:22', '2025-12-20 18:51:22');
INSERT INTO `registration_order` VALUES (4, 1, 1, '2025-12-21 14:30:00', 'pending', NULL, NULL, '2025-12-20 18:58:10', '2025-12-20 18:58:10');
INSERT INTO `registration_order` VALUES (5, 1, 1, '2025-12-30 10:00:00', 'PENDING', '测试症状', NULL, '2025-12-20 21:27:11', '2025-12-20 21:27:11');
INSERT INTO `registration_order` VALUES (6, 1, 1, '2025-12-21 10:30:00', 'PENDING', '', NULL, '2025-12-20 23:56:12', '2025-12-20 23:56:12');
INSERT INTO `registration_order` VALUES (7, 1, 4, '2025-12-21 08:30:00', 'PENDING', '测试数据库连接01', NULL, '2025-12-20 23:58:56', '2025-12-20 23:58:56');
INSERT INTO `registration_order` VALUES (8, 1, 4, '2025-12-21 14:00:00', 'PENDING', '测试11111', NULL, '2025-12-20 23:59:41', '2025-12-20 23:59:41');
INSERT INTO `registration_order` VALUES (9, 1, 3, '2025-12-21 10:00:00', 'PENDING', '', NULL, '2025-12-21 00:00:16', '2025-12-21 00:00:16');
INSERT INTO `registration_order` VALUES (10, 1, 4, '2025-12-21 08:00:00', 'CANCELLED', '', NULL, '2025-12-21 00:00:47', '2025-12-28 15:02:18');
INSERT INTO `registration_order` VALUES (11, 1, 3, '2025-12-28 10:00:00', 'PENDING', '', NULL, '2025-12-22 18:12:49', '2025-12-22 18:12:49');
INSERT INTO `registration_order` VALUES (12, 1, 3, '2025-12-23 14:00:00', 'PENDING', '', NULL, '2025-12-22 18:13:04', '2025-12-22 18:13:04');
INSERT INTO `registration_order` VALUES (13, 1, 1, '2025-12-23 10:30:00', 'PENDING', '', NULL, '2025-12-22 19:43:30', '2025-12-22 19:43:30');
INSERT INTO `registration_order` VALUES (14, 1, 2, '2025-12-25 14:30:00', 'CONFIRMED', '腹痛，需要外科检查', '优先安排', '2025-12-22 20:47:47', '2025-12-22 20:47:47');
INSERT INTO `registration_order` VALUES (15, 1, 5, '2025-12-26 10:00:00', 'PENDING', '妇科检查', '常规体检', '2025-12-22 20:48:12', '2025-12-22 20:48:12');
INSERT INTO `registration_order` VALUES (16, 1, 6, '2025-12-27 09:00:00', 'CANCELLED', '视力模糊，需要眼科检查', '患者临时有事取消', '2025-12-22 20:48:25', '2025-12-22 20:48:25');
INSERT INTO `registration_order` VALUES (17, 1, 4, '2025-12-29 10:30:00', 'PENDING', '儿童感冒', '需要儿科医生检查', '2025-12-22 20:51:11', '2025-12-22 20:51:11');
INSERT INTO `registration_order` VALUES (18, 1, 4, '2025-12-26 14:00:00', 'PENDING', '', NULL, '2025-12-22 22:40:16', '2025-12-22 22:40:16');
INSERT INTO `registration_order` VALUES (19, 1, 3, '2025-12-30 10:30:00', 'PENDING', '', NULL, '2025-12-29 17:11:22', '2025-12-29 17:11:22');
INSERT INTO `registration_order` VALUES (20, 1, 3, '2025-12-30 10:00:00', 'PENDING', '', NULL, '2025-12-30 00:37:11', '2025-12-30 00:37:11');
INSERT INTO `registration_order` VALUES (21, 1, 3, '2026-01-01 08:30:00', 'CANCELLED', '', NULL, '2025-12-31 18:39:02', '2025-12-31 18:39:32');
INSERT INTO `registration_order` VALUES (22, 1, 4, '2026-01-01 08:30:00', 'PENDING', '', NULL, '2025-12-31 18:39:55', '2025-12-31 18:39:55');
INSERT INTO `registration_order` VALUES (25, 4, 6, '2026-01-01 08:30:00', 'PENDING', '', NULL, '2025-12-31 18:52:11', '2025-12-31 18:52:11');
INSERT INTO `registration_order` VALUES (26, 4, 3, '2026-01-06 16:30:00', 'CANCELLED', '', NULL, '2025-12-31 18:52:54', '2025-12-31 23:10:19');
INSERT INTO `registration_order` VALUES (27, 1, 2, '2026-01-01 08:30:00', 'PENDING', '', NULL, '2025-12-31 21:55:38', '2025-12-31 21:55:38');
INSERT INTO `registration_order` VALUES (28, 1, 3, '2026-01-01 10:00:00', 'PENDING', '', NULL, '2025-12-31 21:56:01', '2025-12-31 21:56:01');
INSERT INTO `registration_order` VALUES (29, 1, 3, '2026-01-06 10:30:00', 'PENDING', '', NULL, '2025-12-31 21:57:44', '2025-12-31 21:57:44');
INSERT INTO `registration_order` VALUES (30, 1, 2, '2026-01-01 10:30:00', 'PENDING', '', NULL, '2025-12-31 22:06:13', '2025-12-31 22:06:13');
INSERT INTO `registration_order` VALUES (31, 1, 4, '2026-01-01 14:30:00', 'PENDING', '', NULL, '2025-12-31 22:07:11', '2025-12-31 22:07:11');
INSERT INTO `registration_order` VALUES (32, 1, 6, '2026-01-06 10:30:00', 'PENDING', '', NULL, '2025-12-31 22:10:36', '2025-12-31 22:10:36');
INSERT INTO `registration_order` VALUES (33, 5, 6, '2026-01-04 08:30:00', 'PENDING', '', '', '2025-12-31 22:19:19', '2025-12-31 22:19:19');
INSERT INTO `registration_order` VALUES (34, 10, 2, '2026-01-01 16:30:00', 'PENDING', '', '', '2025-12-31 23:07:33', '2025-12-31 23:07:33');
INSERT INTO `registration_order` VALUES (35, 10, 2, '2026-01-06 16:30:00', 'PENDING', '', '', '2025-12-31 23:08:11', '2025-12-31 23:08:11');
INSERT INTO `registration_order` VALUES (36, 10, 6, '2026-01-05 10:30:00', 'PENDING', '', '', '2025-12-31 23:09:27', '2025-12-31 23:09:27');
INSERT INTO `registration_order` VALUES (37, 4, 3, '2026-01-06 10:00:00', 'PENDING', '', '', '2025-12-31 23:10:12', '2025-12-31 23:10:12');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户角色',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `enabled` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'kobe', '123456', 'ADMIN', 'kobe@hospital.com', '13800138888', '科比', 1, '2025-12-29 17:07:53', '2025-12-31 22:58:41');
INSERT INTO `user` VALUES (2, 'kobe1', '123456', 'PATIENT', NULL, '13944445556', '用户kobe1', 1, '2025-12-31 18:40:49', '2025-12-31 18:40:49');

-- ----------------------------
-- Table structure for doctor_schedule
-- ----------------------------
DROP TABLE IF EXISTS `doctor_schedule`;
CREATE TABLE `doctor_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `doctor_id` bigint NOT NULL COMMENT '医生ID',
  `day_of_week` tinyint NOT NULL COMMENT '星期几(1=周一, 7=周日)',
  `start_time` time NOT NULL COMMENT '开始时间',
  `end_time` time NOT NULL COMMENT '结束时间',
  `slot_duration` int NOT NULL DEFAULT 30 COMMENT '时间段长度(分钟)',
  `max_per_hour` int NOT NULL DEFAULT 5 COMMENT '每小时最大预约数',
  `active` tinyint(1) NOT NULL DEFAULT 1,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_doctor_day` (`doctor_id`, `day_of_week`),
  CONSTRAINT `fk_schedule_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='医生排班表';

-- 每个医生默认排班：周一~周五 上午8:00-12:00 下午14:00-17:00 每小时限5人
INSERT INTO `doctor_schedule` (`doctor_id`, `day_of_week`, `start_time`, `end_time`, `slot_duration`, `max_per_hour`) VALUES
(1,1,'08:00:00','12:00:00',30,5),(1,1,'14:00:00','17:00:00',30,5),
(1,2,'08:00:00','12:00:00',30,5),(1,2,'14:00:00','17:00:00',30,5),
(1,3,'08:00:00','12:00:00',30,5),(1,3,'14:00:00','17:00:00',30,5),
(1,4,'08:00:00','12:00:00',30,5),(1,4,'14:00:00','17:00:00',30,5),
(1,5,'08:00:00','12:00:00',30,5),(1,5,'14:00:00','17:00:00',30,5),
(2,1,'08:00:00','12:00:00',30,5),(2,1,'14:00:00','17:00:00',30,5),
(2,2,'08:00:00','12:00:00',30,5),(2,2,'14:00:00','17:00:00',30,5),
(2,3,'08:00:00','12:00:00',30,5),(2,3,'14:00:00','17:00:00',30,5),
(2,4,'08:00:00','12:00:00',30,5),(2,4,'14:00:00','17:00:00',30,5),
(2,5,'08:00:00','12:00:00',30,5),(2,5,'14:00:00','17:00:00',30,5),
(3,1,'08:00:00','12:00:00',30,5),(3,1,'14:00:00','17:00:00',30,5),
(3,2,'08:00:00','12:00:00',30,5),(3,2,'14:00:00','17:00:00',30,5),
(3,3,'08:00:00','12:00:00',30,5),(3,3,'14:00:00','17:00:00',30,5),
(3,4,'08:00:00','12:00:00',30,5),(3,4,'14:00:00','17:00:00',30,5),
(3,5,'08:00:00','12:00:00',30,5),(3,5,'14:00:00','17:00:00',30,5),
(4,1,'08:00:00','12:00:00',30,5),(4,1,'14:00:00','17:00:00',30,5),
(4,2,'08:00:00','12:00:00',30,5),(4,2,'14:00:00','17:00:00',30,5),
(4,3,'08:00:00','12:00:00',30,5),(4,3,'14:00:00','17:00:00',30,5),
(4,4,'08:00:00','12:00:00',30,5),(4,4,'14:00:00','17:00:00',30,5),
(4,5,'08:00:00','12:00:00',30,5),(4,5,'14:00:00','17:00:00',30,5),
(5,1,'08:00:00','12:00:00',30,5),(5,1,'14:00:00','17:00:00',30,5),
(5,2,'08:00:00','12:00:00',30,5),(5,2,'14:00:00','17:00:00',30,5),
(5,3,'08:00:00','12:00:00',30,5),(5,3,'14:00:00','17:00:00',30,5),
(5,4,'08:00:00','12:00:00',30,5),(5,4,'14:00:00','17:00:00',30,5),
(5,5,'08:00:00','12:00:00',30,5),(5,5,'14:00:00','17:00:00',30,5),
(6,1,'08:00:00','12:00:00',30,5),(6,1,'14:00:00','17:00:00',30,5),
(6,2,'08:00:00','12:00:00',30,5),(6,2,'14:00:00','17:00:00',30,5),
(6,3,'08:00:00','12:00:00',30,5),(6,3,'14:00:00','17:00:00',30,5),
(6,4,'08:00:00','12:00:00',30,5),(6,4,'14:00:00','17:00:00',30,5),
(6,5,'08:00:00','12:00:00',30,5),(6,5,'14:00:00','17:00:00',30,5);

SET FOREIGN_KEY_CHECKS = 1;
