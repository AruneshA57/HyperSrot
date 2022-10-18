-- MySQL dump 10.13  Distrib 8.0.25, for macos11 (x86_64)
--
-- Host: srot-test.cmqre0y4lsxl.ap-south-1.rds.amazonaws.com    Database: srot_test
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `bank_kyc`
--

DROP TABLE IF EXISTS `bank_kyc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank_kyc` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `timestamp` varchar(255) DEFAULT NULL,
  `validation_id` varchar(255) DEFAULT NULL,
  `consumer_id` bigint DEFAULT NULL,
  `investor_id` bigint DEFAULT NULL,
  `razorpay_debit_transaction_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd5v3bra0e7yxr58w25uy2ftyr` (`consumer_id`),
  KEY `FKmmcuggw4jors23qrutbjvu1nw` (`investor_id`),
  KEY `FKa5sr07jrp35eldtk3iyud9850` (`razorpay_debit_transaction_id`),
  CONSTRAINT `FKa5sr07jrp35eldtk3iyud9850` FOREIGN KEY (`razorpay_debit_transaction_id`) REFERENCES `razorpay_debit_transaction` (`id`),
  CONSTRAINT `FKd5v3bra0e7yxr58w25uy2ftyr` FOREIGN KEY (`consumer_id`) REFERENCES `consumers` (`id`),
  CONSTRAINT `FKmmcuggw4jors23qrutbjvu1nw` FOREIGN KEY (`investor_id`) REFERENCES `investors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_kyc`
--

LOCK TABLES `bank_kyc` WRITE;
/*!40000 ALTER TABLE `bank_kyc` DISABLE KEYS */;
/*!40000 ALTER TABLE `bank_kyc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consumers`
--

DROP TABLE IF EXISTS `consumers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consumers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `aadhar_kyc_status` varchar(255) DEFAULT NULL,
  `aadhar_number` varchar(255) DEFAULT NULL,
  `bank_account_name` varchar(255) DEFAULT NULL,
  `bank_account_number` varchar(255) DEFAULT NULL,
  `bank_ifsc` varchar(255) DEFAULT NULL,
  `bank_name` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `account_not_expired` bit(1) DEFAULT NULL,
  `account_not_locked` bit(1) DEFAULT NULL,
  `credential_not_expired` bit(1) DEFAULT NULL,
  `email_confirmed` bit(1) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `joining_date` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  `pan_kyc_status` varchar(255) DEFAULT NULL,
  `pan_number` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `authorities` varchar(255) DEFAULT NULL,
  `tfa` bit(1) DEFAULT NULL,
  `point_of_contact` varchar(255) DEFAULT NULL,
  `referral_code` varchar(255) DEFAULT NULL,
  `bankkyc_id` bigint DEFAULT NULL,
  `razorpay_contact_id` bigint DEFAULT NULL,
  `razorpay_fund_account_id` bigint DEFAULT NULL,
  `wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1570sdjvxb2wtftljv7vj5h9` (`bankkyc_id`),
  KEY `FK4wlhlisheigktypmk4yoxds65` (`razorpay_contact_id`),
  KEY `FKibwmuk9sgjnkyg3lfds6gkcms` (`razorpay_fund_account_id`),
  KEY `FK6m76hgf6l4s9fq0o5962bak54` (`wallet_id`),
  CONSTRAINT `FK1570sdjvxb2wtftljv7vj5h9` FOREIGN KEY (`bankkyc_id`) REFERENCES `bank_kyc` (`id`),
  CONSTRAINT `FK4wlhlisheigktypmk4yoxds65` FOREIGN KEY (`razorpay_contact_id`) REFERENCES `razorpay_contact` (`id`),
  CONSTRAINT `FK6m76hgf6l4s9fq0o5962bak54` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`),
  CONSTRAINT `FKibwmuk9sgjnkyg3lfds6gkcms` FOREIGN KEY (`razorpay_fund_account_id`) REFERENCES `razorpay_fund_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consumers`
--

LOCK TABLES `consumers` WRITE;
/*!40000 ALTER TABLE `consumers` DISABLE KEYS */;
/*!40000 ALTER TABLE `consumers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contracts`
--

DROP TABLE IF EXISTS `contracts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contracts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `allocation_date` date DEFAULT NULL,
  `amount_invested` bigint DEFAULT NULL,
  `investment_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq67cl65kdmphqactd5q7hjb9e` (`investment_id`),
  CONSTRAINT `FKq67cl65kdmphqactd5q7hjb9e` FOREIGN KEY (`investment_id`) REFERENCES `investments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contracts`
--

LOCK TABLES `contracts` WRITE;
/*!40000 ALTER TABLE `contracts` DISABLE KEYS */;
/*!40000 ALTER TABLE `contracts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS `coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `coupon_code` varchar(255) DEFAULT NULL,
  `discount_amount` int DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sre2vcap4vs6qucaksomk3c7s` (`coupon_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon`
--

LOCK TABLES `coupon` WRITE;
/*!40000 ALTER TABLE `coupon` DISABLE KEYS */;
INSERT INTO `coupon` VALUES (1,'DIWALI2021',1000,15,'2021-11-07'),(2,'NEWYEAR2021',2000,15,'2021-12-25'),(3,'EARLY_VR_2022',100,365,'2022-02-27');
/*!40000 ALTER TABLE `coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `debit_investment_transaction`
--

DROP TABLE IF EXISTS `debit_investment_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `debit_investment_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `particulars` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `timestamp` date DEFAULT NULL,
  `wallet_id` bigint DEFAULT NULL,
  `investment_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6sve5xv6kuqckmvijwkby5n0u` (`wallet_id`),
  KEY `FK4f0pyd3cf8yp7a84haxdv811y` (`investment_id`),
  CONSTRAINT `FK4f0pyd3cf8yp7a84haxdv811y` FOREIGN KEY (`investment_id`) REFERENCES `investments` (`id`),
  CONSTRAINT `FK6sve5xv6kuqckmvijwkby5n0u` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `debit_investment_transaction`
--

LOCK TABLES `debit_investment_transaction` WRITE;
/*!40000 ALTER TABLE `debit_investment_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `debit_investment_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_codes`
--

DROP TABLE IF EXISTS `email_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_codes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_codes`
--

LOCK TABLES `email_codes` WRITE;
/*!40000 ALTER TABLE `email_codes` DISABLE KEYS */;
INSERT INTO `email_codes` VALUES (1,'JJuz7CjgrRWpXb1iWIjWvV5fMC8zJTqDAwmqpnQbXV5LjL0teLAdRuFWnTia5M1C','2022-02-27 21:19:00.134330','PENDING',3);
/*!40000 ALTER TABLE `email_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_confirmation_codes`
--

DROP TABLE IF EXISTS `email_confirmation_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_confirmation_codes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_confirmation_codes`
--

LOCK TABLES `email_confirmation_codes` WRITE;
/*!40000 ALTER TABLE `email_confirmation_codes` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_confirmation_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `investments`
--

DROP TABLE IF EXISTS `investments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `investment_date` date DEFAULT NULL,
  `investment_tenure` int DEFAULT NULL,
  `power_generated` bigint DEFAULT NULL,
  `rate_per_unit` int DEFAULT NULL,
  `yield` bigint DEFAULT NULL,
  `contract_id` bigint DEFAULT NULL,
  `coupon_id` bigint DEFAULT NULL,
  `investor_id` bigint DEFAULT NULL,
  `listing_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjxcik47tk2n5654qid3fsbby7` (`contract_id`),
  KEY `FKmrexv3m9n60af8ysg59q4q4s2` (`listing_id`),
  KEY `FKsk4lrv6rlwwtbuk5qnljqb2nc` (`coupon_id`),
  KEY `FK9uo7o410kt34s9xc29rth2hci` (`investor_id`),
  CONSTRAINT `FKjxcik47tk2n5654qid3fsbby7` FOREIGN KEY (`contract_id`) REFERENCES `contracts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `investments`
--

LOCK TABLES `investments` WRITE;
/*!40000 ALTER TABLE `investments` DISABLE KEYS */;
/*!40000 ALTER TABLE `investments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `investor_coupon`
--

DROP TABLE IF EXISTS `investor_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investor_coupon` (
  `coupon_id` bigint NOT NULL,
  `investor_id` bigint NOT NULL,
  `used` bit(1) NOT NULL,
  PRIMARY KEY (`coupon_id`,`investor_id`),
  KEY `FK78cocjqggxgqbrctpcisn5fqy` (`investor_id`),
  CONSTRAINT `FK78cocjqggxgqbrctpcisn5fqy` FOREIGN KEY (`investor_id`) REFERENCES `investors` (`id`),
  CONSTRAINT `FKbjyivtud3ymwdvktoym5n9r64` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `investor_coupon`
--

LOCK TABLES `investor_coupon` WRITE;
/*!40000 ALTER TABLE `investor_coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `investor_coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `investor_questionnaire`
--

DROP TABLE IF EXISTS `investor_questionnaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investor_questionnaire` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `features_suggestion` text,
  `investment_intention` varchar(255) DEFAULT NULL,
  `investment_plan` varchar(255) DEFAULT NULL,
  `monthly_income` varchar(255) DEFAULT NULL,
  `monthly_savings` varchar(255) DEFAULT NULL,
  `payment_frequency` varchar(255) DEFAULT NULL,
  `questions` text,
  `savings_preference` varchar(255) DEFAULT NULL,
  `webinar` bit(1) DEFAULT NULL,
  `investor_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgdx5pjjqeigvc9v0htwnhpels` (`investor_id`),
  CONSTRAINT `FKgdx5pjjqeigvc9v0htwnhpels` FOREIGN KEY (`investor_id`) REFERENCES `investors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `investor_questionnaire`
--

LOCK TABLES `investor_questionnaire` WRITE;
/*!40000 ALTER TABLE `investor_questionnaire` DISABLE KEYS */;
/*!40000 ALTER TABLE `investor_questionnaire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `investor_rate`
--

DROP TABLE IF EXISTS `investor_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investor_rate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `investor_rate` bigint DEFAULT NULL,
  `tenure` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `investor_rate`
--

LOCK TABLES `investor_rate` WRITE;
/*!40000 ALTER TABLE `investor_rate` DISABLE KEYS */;
INSERT INTO `investor_rate` VALUES (1,_binary '',560,60),(2,_binary '',485,30);
/*!40000 ALTER TABLE `investor_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `investor_returns`
--

DROP TABLE IF EXISTS `investor_returns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investor_returns` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `cost` bigint DEFAULT NULL,
  `power_depreciation` bigint DEFAULT NULL,
  `power_depreciation_interval` bigint DEFAULT NULL,
  `tds` bigint DEFAULT NULL,
  `units` bigint DEFAULT NULL,
  `value_depreciation` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `investor_returns`
--

LOCK TABLES `investor_returns` WRITE;
/*!40000 ALTER TABLE `investor_returns` DISABLE KEYS */;
INSERT INTO `investor_returns` VALUES (1,_binary '',5250000,1,12,2,4,1);
/*!40000 ALTER TABLE `investor_returns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `investors`
--

DROP TABLE IF EXISTS `investors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `aadhar_kyc_status` varchar(255) DEFAULT NULL,
  `aadhar_number` varchar(255) DEFAULT NULL,
  `bank_account_name` varchar(255) DEFAULT NULL,
  `bank_account_number` varchar(255) DEFAULT NULL,
  `bank_ifsc` varchar(255) DEFAULT NULL,
  `bank_name` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `account_not_expired` bit(1) DEFAULT NULL,
  `account_not_locked` bit(1) DEFAULT NULL,
  `credential_not_expired` bit(1) DEFAULT NULL,
  `email_confirmed` bit(1) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `joining_date` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  `pan_kyc_status` varchar(255) DEFAULT NULL,
  `pan_number` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `authorities` varchar(255) DEFAULT NULL,
  `tfa` bit(1) DEFAULT NULL,
  `joining_code` varchar(255) DEFAULT NULL,
  `nominee` varchar(255) DEFAULT NULL,
  `referral_code` varchar(255) DEFAULT NULL,
  `bankkyc_id` bigint DEFAULT NULL,
  `razorpay_contact_id` bigint DEFAULT NULL,
  `razorpay_fund_account_id` bigint DEFAULT NULL,
  `wallet_id` bigint DEFAULT NULL,
  `questionaire_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1nd6lwwiu6i0ljlwfvn00p6ls` (`referral_code`),
  KEY `FKa236nhp2xp3c7be2upmc1fxs6` (`bankkyc_id`),
  KEY `FKkerjxdpfh5jq71di9jt6gl2ag` (`razorpay_contact_id`),
  KEY `FKfb2wq36vyj8w4n3bekii0d7ek` (`razorpay_fund_account_id`),
  KEY `FKpkmabxpv4f35ka6qoy8e0n7ae` (`wallet_id`),
  KEY `FKlqsx9gd0u48pj97ic0l8eyv4o` (`questionaire_id`),
  CONSTRAINT `FKa236nhp2xp3c7be2upmc1fxs6` FOREIGN KEY (`bankkyc_id`) REFERENCES `bank_kyc` (`id`),
  CONSTRAINT `FKfb2wq36vyj8w4n3bekii0d7ek` FOREIGN KEY (`razorpay_fund_account_id`) REFERENCES `razorpay_fund_account` (`id`),
  CONSTRAINT `FKkerjxdpfh5jq71di9jt6gl2ag` FOREIGN KEY (`razorpay_contact_id`) REFERENCES `razorpay_contact` (`id`),
  CONSTRAINT `FKlqsx9gd0u48pj97ic0l8eyv4o` FOREIGN KEY (`questionaire_id`) REFERENCES `investor_questionnaire` (`id`),
  CONSTRAINT `FKpkmabxpv4f35ka6qoy8e0n7ae` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `investors`
--

LOCK TABLES `investors` WRITE;
/*!40000 ALTER TABLE `investors` DISABLE KEYS */;
INSERT INTO `investors` VALUES (1,'COMPLETE',NULL,'Bhavna Rana','11111111','IFSC111','BANK1','1991-11-30','xxxx@gmail.com',_binary '',_binary '',_binary '',_binary '\0',_binary '','2022-02-27','Bhavna Rana',NULL,'COMPLETE',NULL,'$2a$10$r0WEQcLWNM97x/oBgbHWZOI8vOiZzGairoESwYHwDZQetDiOWTKUC','9999999999','INVESTOR',NULL,'',NULL,NULL,NULL,NULL,NULL,1,NULL),(2,'COMPLETE',NULL,'Twinkle Agarwal','22222222','IFSC222','BANK2',NULL,'xxxx@gmail.com',_binary '\0',_binary '\0',_binary '\0',_binary '\0',_binary '\0','2021-11-08','Twinkle Agarwal',NULL,'COMPLETE',NULL,NULL,'9999999999',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'PENDING',NULL,NULL,NULL,NULL,NULL,'1988-10-18','rana.vikas007@gmail.com',_binary '',_binary '',_binary '',_binary '\0',_binary '','2022-02-27','Vikas Rana',NULL,'PENDING',NULL,'$2a$10$Bzno/uYXxKvc7jBFK/7Bw.rswlBrsFwzLv4dLzu/XFiEWQLpWwts.','919972638091','INVESTOR',_binary '\0',NULL,NULL,'VR2202272',NULL,1,NULL,2,NULL);
/*!40000 ALTER TABLE `investors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `invoice_bill` varchar(255) DEFAULT NULL,
  `tenure` int DEFAULT NULL,
  `investor_id` bigint DEFAULT NULL,
  `listing_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6i7mq9rvrjdb29r9uurcwk1y9` (`investor_id`),
  KEY `FKsjpj820r39udmx7wn0r27m8wj` (`listing_id`),
  CONSTRAINT `FK6i7mq9rvrjdb29r9uurcwk1y9` FOREIGN KEY (`investor_id`) REFERENCES `investors` (`id`),
  CONSTRAINT `FKsjpj820r39udmx7wn0r27m8wj` FOREIGN KEY (`listing_id`) REFERENCES `listings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listings`
--

DROP TABLE IF EXISTS `listings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_date` date DEFAULT NULL,
  `fund_limit` bigint DEFAULT NULL,
  `fund_raised` bigint DEFAULT NULL,
  `listing_status` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `investor_rate30_id` bigint DEFAULT NULL,
  `investor_rate60_id` bigint DEFAULT NULL,
  `investor_returns_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9ouv9ldsq8mbm3dn3cua4pm68` (`investor_rate30_id`),
  KEY `FKdsv52cej474yjxy86av5xom7p` (`investor_rate60_id`),
  KEY `FKermvrlrsqjnsjod5stoil16h4` (`investor_returns_id`),
  CONSTRAINT `FK9ouv9ldsq8mbm3dn3cua4pm68` FOREIGN KEY (`investor_rate30_id`) REFERENCES `investor_rate` (`id`),
  CONSTRAINT `FKdsv52cej474yjxy86av5xom7p` FOREIGN KEY (`investor_rate60_id`) REFERENCES `investor_rate` (`id`),
  CONSTRAINT `FKermvrlrsqjnsjod5stoil16h4` FOREIGN KEY (`investor_returns_id`) REFERENCES `investor_returns` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listings`
--

LOCK TABLES `listings` WRITE;
/*!40000 ALTER TABLE `listings` DISABLE KEYS */;
INSERT INTO `listings` VALUES (1,'2021-12-30',1575000000,300000,'FUNDING','2021-12-05',2,1,1);
/*!40000 ALTER TABLE `listings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` int DEFAULT NULL,
  `amount_paid` int DEFAULT NULL,
  `attempts` int DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `order_timestamp` date DEFAULT NULL,
  `payment_timestamp` date DEFAULT NULL,
  `razorpay_order_id` varchar(255) DEFAULT NULL,
  `razorpay_payment_id` varchar(255) DEFAULT NULL,
  `receipt_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8af1p36hch7wt2bbqe3o0gqf1` (`wallet_id`),
  CONSTRAINT `FK8af1p36hch7wt2bbqe3o0gqf1` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plants`
--

DROP TABLE IF EXISTS `plants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plants` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `daily_unit_benchmark` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `pin_code` varchar(255) DEFAULT NULL,
  `plant_capacity` bigint DEFAULT NULL,
  `plant_cost` bigint DEFAULT NULL,
  `plant_status` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `street_address` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `listing_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpavovoaa2t36ck50yw7qgpeyh` (`listing_id`),
  CONSTRAINT `FKpavovoaa2t36ck50yw7qgpeyh` FOREIGN KEY (`listing_id`) REFERENCES `listings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plants`
--

LOCK TABLES `plants` WRITE;
/*!40000 ALTER TABLE `plants` DISABLE KEYS */;
INSERT INTO `plants` VALUES (1,NULL,NULL,NULL,NULL,100000,1000000000,NULL,NULL,NULL,'RESIDENTIAL',1),(2,NULL,NULL,NULL,NULL,200000,2000000000,NULL,NULL,NULL,'HOSPITAL',1);
/*!40000 ALTER TABLE `plants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `razorpay_contact`
--

DROP TABLE IF EXISTS `razorpay_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `razorpay_contact` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `contact_id` varchar(255) DEFAULT NULL,
  `timestamp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `razorpay_contact`
--

LOCK TABLES `razorpay_contact` WRITE;
/*!40000 ALTER TABLE `razorpay_contact` DISABLE KEYS */;
INSERT INTO `razorpay_contact` VALUES (1,_binary '','cont_InbH3Rx7mmRW2a','1643031891');
/*!40000 ALTER TABLE `razorpay_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `razorpay_credit_transaction`
--

DROP TABLE IF EXISTS `razorpay_credit_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `razorpay_credit_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `particulars` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `timestamp` date DEFAULT NULL,
  `amount_paid` bigint DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `num_of_attempts` int DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `order_timestamp` date DEFAULT NULL,
  `payment_id` varchar(255) DEFAULT NULL,
  `wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK814lnm8i0vymdjguwt8ewjhx9` (`wallet_id`),
  CONSTRAINT `FK814lnm8i0vymdjguwt8ewjhx9` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `razorpay_credit_transaction`
--

LOCK TABLES `razorpay_credit_transaction` WRITE;
/*!40000 ALTER TABLE `razorpay_credit_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `razorpay_credit_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `razorpay_debit_transaction`
--

DROP TABLE IF EXISTS `razorpay_debit_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `razorpay_debit_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `particulars` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `timestamp` date DEFAULT NULL,
  `failure_reason` varchar(255) DEFAULT NULL,
  `fees` bigint DEFAULT NULL,
  `idempotency_key` varchar(255) DEFAULT NULL,
  `mode` varchar(255) DEFAULT NULL,
  `narration` varchar(255) DEFAULT NULL,
  `payout_id` varchar(255) DEFAULT NULL,
  `request_timestamp` date DEFAULT NULL,
  `tax` bigint DEFAULT NULL,
  `utr` varchar(255) DEFAULT NULL,
  `wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5g135jbtn3w8at0cffkbcfqpo` (`wallet_id`),
  CONSTRAINT `FK5g135jbtn3w8at0cffkbcfqpo` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `razorpay_debit_transaction`
--

LOCK TABLES `razorpay_debit_transaction` WRITE;
/*!40000 ALTER TABLE `razorpay_debit_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `razorpay_debit_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `razorpay_fund_account`
--

DROP TABLE IF EXISTS `razorpay_fund_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `razorpay_fund_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `fund_id` varchar(255) DEFAULT NULL,
  `timestamp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `razorpay_fund_account`
--

LOCK TABLES `razorpay_fund_account` WRITE;
/*!40000 ALTER TABLE `razorpay_fund_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `razorpay_fund_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `referral`
--

DROP TABLE IF EXISTS `referral`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `referral` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `referral_bonus` bigint DEFAULT NULL,
  `referee_id` bigint DEFAULT NULL,
  `referrer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrg73edglrwesj67jaq9pcjn7h` (`referee_id`),
  KEY `FKdxv4pxtm2jtsxdhwmntfgj5no` (`referrer_id`),
  CONSTRAINT `FKdxv4pxtm2jtsxdhwmntfgj5no` FOREIGN KEY (`referrer_id`) REFERENCES `investors` (`id`),
  CONSTRAINT `FKrg73edglrwesj67jaq9pcjn7h` FOREIGN KEY (`referee_id`) REFERENCES `investors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `referral`
--

LOCK TABLES `referral` WRITE;
/*!40000 ALTER TABLE `referral` DISABLE KEYS */;
/*!40000 ALTER TABLE `referral` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `referral_credit_transaction`
--

DROP TABLE IF EXISTS `referral_credit_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `referral_credit_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `particulars` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `timestamp` date DEFAULT NULL,
  `wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd98v83s3giavg8gfkbgeipn3g` (`wallet_id`),
  CONSTRAINT `FKd98v83s3giavg8gfkbgeipn3g` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `referral_credit_transaction`
--

LOCK TABLES `referral_credit_transaction` WRITE;
/*!40000 ALTER TABLE `referral_credit_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `referral_credit_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallet`
--

DROP TABLE IF EXISTS `wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `joining_bonus` bigint DEFAULT NULL,
  `questionnaire_bonus` bigint DEFAULT NULL,
  `referrals_bonus` bigint DEFAULT NULL,
  `wallet_balance` bigint DEFAULT NULL,
  `investor_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjp4ib4r4dbajljruv2705xowv` (`investor_id`),
  CONSTRAINT `FKjp4ib4r4dbajljruv2705xowv` FOREIGN KEY (`investor_id`) REFERENCES `investors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wallet`
--

LOCK TABLES `wallet` WRITE;
/*!40000 ALTER TABLE `wallet` DISABLE KEYS */;
INSERT INTO `wallet` VALUES (1,0,0,0,0,1),(2,0,0,0,0,3);
/*!40000 ALTER TABLE `wallet` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-27 21:40:01
