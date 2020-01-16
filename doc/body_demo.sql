/*
Navicat MySQL Data Transfer

Source Server         : wy
Source Server Version : 50727
Source Host           : zccxywy.cn:3306
Source Database       : body_demo

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2020-01-16 10:30:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_image
-- ----------------------------
DROP TABLE IF EXISTS `user_image`;
CREATE TABLE `user_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ut` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户标识',
  `old` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原图本地地址',
  `analysis` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '解析图本地地址',
  `body_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '全身图本地地址',
  `head_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头部本地地址',
  `chest_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '胸部本地地址',
  `leg_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '腿部本地地址',
  `pelvis_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '盆骨本地地址',
  `spine_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '脊柱本地地址',
  `old_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `out_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `body_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `head_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chest_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `leg_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pelvis_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `spine_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `headHeel_L` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头部侧倾-左',
  `headHeel_R` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头部侧倾-右',
  `highLowShoulder_L` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '高低肩-左',
  `highLowShoulder_R` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '高低肩-右',
  `pelvicTilt_L` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '盆骨侧倾-左',
  `pelvicTilt_R` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '盆骨侧倾-右',
  `scoliosis_L` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '脊柱侧弯-左',
  `scoliosis_R` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '脊柱侧弯-右',
  `o_leftleg_r` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'O型腿-左腿',
  `o_rightleg_r` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'O型腿-右腿',
  `x_leftleg_r` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'X型腿-左腿',
  `x_rightleg_r` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'X型腿-右腿',
  `l_shoulder_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '左肩切图',
  `r_shoulder_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '右肩切图',
  `l_pelvis_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '左盆骨切图',
  `r_pelvis_image_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '右盆骨切图',
  `headHeel_L_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头部侧倾-左',
  `headHeel_R_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头部侧倾-右',
  `highLowShoulder_L_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '高低肩-左',
  `highLowShoulder_R_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '高低肩-右',
  `pelvicTilt_L_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '盆骨侧倾-左',
  `pelvicTilt_R_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '盆骨侧倾-右',
  `scoliosis_L_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '脊柱侧弯-左',
  `scoliosis_R_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '脊柱侧弯-右',
  `o_leftleg_r_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'O型腿-左腿',
  `o_rightleg_r_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'O型腿-右腿',
  `x_leftleg_r_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'X型腿-左腿',
  `x_rightleg_r_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'X型腿-右腿',
  `head_image_side_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `leg_image_side_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hum_image_side_oss` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `head_forward_r` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '废弃',
  `knee_hyperextension_r` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '废弃',
  `humpback_r` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '盆骨前倾',
  `head_forward_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头部前倾',
  `knee_hyperextension_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用1',
  `humpback_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用2',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of user_image
-- ----------------------------

-- ----------------------------
-- Table structure for value
-- ----------------------------
DROP TABLE IF EXISTS `value`;
CREATE TABLE `value` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'value',
  `head` int(11) DEFAULT NULL COMMENT '头部侧倾',
  `shoulder` int(11) DEFAULT NULL COMMENT '高低肩',
  `spine` int(11) DEFAULT NULL COMMENT '脊柱侧弯',
  `hip` int(11) DEFAULT NULL COMMENT '盆骨侧倾',
  `left_leg` int(11) DEFAULT NULL,
  `right_leg` int(11) DEFAULT NULL,
  `head_forward` int(11) DEFAULT NULL COMMENT '头部前倾',
  `knee_hyperextension` int(11) DEFAULT NULL COMMENT '膝过伸',
  `hunchback` int(11) DEFAULT NULL COMMENT '圆肩驼背',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of value
-- ----------------------------
INSERT INTO `value` VALUES ('1', '12', '11', '6', '8', '18', '18', '50', '50', null);
