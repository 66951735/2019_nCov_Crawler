SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_area_stat
-- ----------------------------
DROP TABLE IF EXISTS `tb_area_stat`;
CREATE TABLE `tb_area_stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province_name` varchar(24) NOT NULL,
  `city_name` varchar(24) DEFAULT '',
  `confirmed_count` int(11) NOT NULL,
  `suspected_count` int(11) NOT NULL,
  `cured_count` int(11) NOT NULL,
  `dead_count` int(11) NOT NULL,
  `comment` varchar(100) DEFAULT '',
  `status` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `statistics_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for tb_statistics
-- ----------------------------
DROP TABLE IF EXISTS `tb_statistics`;
CREATE TABLE `tb_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` datetime NOT NULL,
  `confirmed_count` int(11) NOT NULL,
  `suspected_count` int(11) NOT NULL,
  `cured_count` int(11) NOT NULL,
  `dead_count` int(11) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
