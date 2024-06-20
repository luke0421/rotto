-- MySQL dump 10.13  Distrib 8.0.34, for macos13 (arm64)
--
-- Host: k10e105.p.ssafy.io    Database: rotto
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account_history_tb`
--

DROP TABLE IF EXISTS `account_history_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_history_tb` (
  `account_history_code` int NOT NULL AUTO_INCREMENT COMMENT 'auto_increment',
  `account_code` int NOT NULL,
  `amount` int NOT NULL,
  `account_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deposit_or_withdrawal` tinyint NOT NULL COMMENT '1 = 입금, 2 = 출금',
  PRIMARY KEY (`account_history_code`),
  KEY `account_history_tb_account_code_IDX` (`account_code`) USING BTREE,
  KEY `account_history_tb_deposit_or_withdrawal_IDX` (`deposit_or_withdrawal`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=221 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_tb`
--

DROP TABLE IF EXISTS `account_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_tb` (
  `account_code` int NOT NULL AUTO_INCREMENT COMMENT 'auto_increment',
  `user_code` int NOT NULL,
  `balance` int NOT NULL,
  `bank_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `account_num` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `account_type` tinyint NOT NULL DEFAULT '0' COMMENT '0 : 공모계좌, 1 : 계좌',
  PRIMARY KEY (`account_code`),
  KEY `account_tb_user_code_IDX` (`user_code`) USING BTREE,
  KEY `account_tb_account_type_IDX` (`account_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `alert_tb`
--

DROP TABLE IF EXISTS `alert_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_tb` (
  `alert_code` int NOT NULL AUTO_INCREMENT,
  `user_code` int NOT NULL,
  `title` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `alert_type` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_read` tinyint NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`alert_code`),
  KEY `alert_tb_user_code_IDX` (`user_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `apply_history_tb`
--

DROP TABLE IF EXISTS `apply_history_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply_history_tb` (
  `apply_history_code` int NOT NULL AUTO_INCREMENT COMMENT 'auto_increment',
  `user_code` int NOT NULL,
  `subscription_code` int NOT NULL,
  `apply_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_delete` tinyint NOT NULL DEFAULT '0',
  `apply_count` int NOT NULL COMMENT '몇개를 신청하는지',
  PRIMARY KEY (`apply_history_code`),
  KEY `apply_history_tb_subscription_code_IDX` (`subscription_code`) USING BTREE,
  KEY `apply_history_tb_user_code_and_subscription_code_IDX` (`user_code`,`subscription_code`) USING BTREE,
  KEY `apply_history_tb_user_code_IDX` (`user_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bean_tb`
--

DROP TABLE IF EXISTS `bean_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bean_tb` (
  `bean_code` int NOT NULL AUTO_INCREMENT,
  `bean_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `bean_img_path` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bean_description` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`bean_code`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expense_detail_tb`
--

DROP TABLE IF EXISTS `expense_detail_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expense_detail_tb` (
  `expense_detail_code` int NOT NULL AUTO_INCREMENT,
  `subscription_code` int NOT NULL,
  `expenditure_content` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '수확비용, 열매선별비용, 기타운영비',
  `expenses` int NOT NULL COMMENT '수확비용 = 농장 규모 * 1000, 열매선별비용 = 농장 규모 * 150, 기타운영비 = 농장 규모 * 400',
  PRIMARY KEY (`expense_detail_code`),
  KEY `expense_detail_tb_subscription_code_IDX` (`subscription_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1365 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `faq_tb`
--

DROP TABLE IF EXISTS `faq_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faq_tb` (
  `faq_code` int NOT NULL AUTO_INCREMENT COMMENT 'auto_increment',
  `title` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`faq_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `farm_tb`
--

DROP TABLE IF EXISTS `farm_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farm_tb` (
  `farm_code` int NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
  `farm_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `farm_ceo_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `farm_logo_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '1.jpg',
  `farm_address` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '에티오피아, 아디스 아바바시 오로미아주 아다마 123-1',
  `farm_scale` int NOT NULL COMMENT '3000(평)',
  `farm_introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `farm_started_time` timestamp NOT NULL,
  `award_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '이력서 쓰듯이 수상내역 텍스트 나열',
  `farm_bean_name` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `farm_bean_grade` tinyint NOT NULL COMMENT '1~3,  1: 나인티플러스, 2: 수퍼 프리미엄 스페셜티, 3: 프리미엄 스페셜티',
  `farm_bank_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '싸피은행',
  `farm_bank_account_num` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`farm_code`),
  KEY `farm_tb_farm_name_IDX` (`farm_name`) USING BTREE,
  KEY `farm_tb_farm_bean_name_IDX` (`farm_bean_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `farm_top10_tb`
--

DROP TABLE IF EXISTS `farm_top10_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farm_top10_tb` (
  `farm_top10_code` int NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
  `farm_code` int NOT NULL COMMENT 'AUTO_INCREMENT',
  PRIMARY KEY (`farm_top10_code`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interest_farm_tb`
--

DROP TABLE IF EXISTS `interest_farm_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interest_farm_tb` (
  `interest_farm_code` int NOT NULL AUTO_INCREMENT COMMENT 'auto_increment',
  `user_code` int NOT NULL,
  `farm_code` int NOT NULL,
  PRIMARY KEY (`interest_farm_code`),
  KEY `interest_farm_tb_user_code_IDX` (`user_code`) USING BTREE,
  KEY `interest_farm_tb_farm_code_IDX` (`farm_code`) USING BTREE,
  KEY `interest_farm_tb_farm_code_and_user_code_IDX` (`farm_code`,`user_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `news_tb`
--

DROP TABLE IF EXISTS `news_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news_tb` (
  `news_code` int NOT NULL AUTO_INCREMENT COMMENT 'auto_increment',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `author` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `img_link` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `news_detail_link` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `post_time` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'May 15, 2024' COMMENT '기사작성시간',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'DB에 생성된 시간',
  PRIMARY KEY (`news_code`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notice_tb`
--

DROP TABLE IF EXISTS `notice_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice_tb` (
  `notice_code` int NOT NULL AUTO_INCREMENT,
  `title` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `req_board_tb`
--

DROP TABLE IF EXISTS `req_board_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `req_board_tb` (
  `req_board_code` int NOT NULL AUTO_INCREMENT COMMENT 'auto_increment',
  `user_code` int NOT NULL,
  `title` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`req_board_code`),
  KEY `req_board_tb_user_code_IDX` (`user_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subscription_tb`
--

DROP TABLE IF EXISTS `subscription_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscription_tb` (
  `subscription_code` int NOT NULL AUTO_INCREMENT COMMENT 'auto_increment',
  `farm_code` int NOT NULL COMMENT 'AUTO_INCREMENT',
  `confirm_price` int DEFAULT NULL,
  `started_time` timestamp NOT NULL,
  `ended_time` timestamp NOT NULL,
  `limit_num` int DEFAULT NULL,
  `return_rate` decimal(10,2) DEFAULT NULL,
  `total_token_count` int NOT NULL COMMENT '청약의 총 발행 토큰 수',
  `partner_farm_rate` int NOT NULL COMMENT '농장주 수익률(토큰 환전시 수수료)',
  `total_sales` int DEFAULT '0' COMMENT '매출(원두 총 판매액)',
  `total_proceed` int DEFAULT '0' COMMENT '청약의 총 수익금: 매출 - (농장수익 + 운영비 + 우리 수수료)',
  `fee` int DEFAULT '0' COMMENT '환급시 발생 수수료(관리자꺼)',
  PRIMARY KEY (`subscription_code`),
  KEY `subscription_tb_farm_code_IDX` (`farm_code`) USING BTREE,
  KEY `subscription_tb_confirm_price_IDX` (`confirm_price`) USING BTREE,
  KEY `subscription_tb_started_time_IDX` (`started_time`) USING BTREE,
  KEY `subscription_tb_ended_time_IDX` (`ended_time`) USING BTREE,
  KEY `subscription_tb_started_time_and_ended_time_IDX` (`started_time`,`ended_time`) USING BTREE,
  KEY `subscription_tb_return_rate_IDX` (`return_rate`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=473 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `terms_tb`
--

DROP TABLE IF EXISTS `terms_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `terms_tb` (
  `terms_code` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`terms_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trade_history_tb`
--

DROP TABLE IF EXISTS `trade_history_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trade_history_tb` (
  `trade_history_code` int NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
  `user_code` int NOT NULL,
  `subscription_code` int NOT NULL,
  `trade_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `trade_num` int NOT NULL,
  `refund` tinyint NOT NULL DEFAULT '0' COMMENT '0: 조각 구매, 1: 조각 환급',
  `bc_address` varchar(48) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '거래지갑주소',
  `token_price` double DEFAULT NULL,
  PRIMARY KEY (`trade_history_code`),
  KEY `trade_history_tb_subscription_code_IDX` (`subscription_code`) USING BTREE,
  KEY `trade_history_tb_user_code_and_refund_IDX` (`user_code`,`refund`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_tb`
--

DROP TABLE IF EXISTS `user_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_tb` (
  `user_code` int NOT NULL AUTO_INCREMENT COMMENT 'auto_increment',
  `name` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '박하늘별님구름햇님보다사랑스러우리(실제로있음)',
  `sex` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'M = 남자, F = 여자',
  `phone_num` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '01012341234',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '123456',
  `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `jumin_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '910101',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0',
  `join_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `delete_time` timestamp NULL DEFAULT NULL,
  `device_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'admin = 1이면 관리자',
  `user_key` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '금융망 계정생성후 유저키 저장용',
  `bc_address` varchar(48) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_code`),
  UNIQUE KEY `user_tb_unique` (`phone_num`),
  KEY `user_tb_phone_num_IDX` (`phone_num`) USING BTREE,
  KEY `user_tb_email_IDX` (`email`) USING BTREE,
  KEY `user_tb_bc_address_IDX` (`bc_address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-20  4:42:18
