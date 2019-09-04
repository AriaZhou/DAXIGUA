-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: dxg
-- ------------------------------------------------------
-- Server version	8.0.17

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
-- Table structure for table `pgroup`
--

DROP TABLE IF EXISTS `pgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pgroup` (
  `id` varchar(45) NOT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pgroup`
--

LOCK TABLES `pgroup` WRITE;
/*!40000 ALTER TABLE `pgroup` DISABLE KEYS */;
INSERT INTO `pgroup` VALUES ('190666','2019-09-04 00:00:00','2019-09-12 00:00:00'),('190808','2019-08-09 00:00:00','2019-08-09 00:00:00'),('190903','2019-08-09 00:00:00','2019-09-09 00:00:00');
/*!40000 ALTER TABLE `pgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `porder`
--

DROP TABLE IF EXISTS `porder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `porder` (
  `id` varchar(45) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `productid` varchar(45) DEFAULT NULL,
  `ocount` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT '0',
  `time` datetime DEFAULT CURRENT_TIMESTAMP,
  `price` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_idx` (`username`),
  KEY `product_idx` (`productid`),
  CONSTRAINT `product` FOREIGN KEY (`productid`) REFERENCES `product` (`id`),
  CONSTRAINT `user` FOREIGN KEY (`username`) REFERENCES `user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `porder`
--

LOCK TABLES `porder` WRITE;
/*!40000 ALTER TABLE `porder` DISABLE KEYS */;
INSERT INTO `porder` VALUES ('','897694163','326537547',3,0,NULL,'135'),('1231567418650258','123','326537547',2,0,'2019-09-02 18:04:10','90'),('1231567419182258','123','326537547',2,0,'2019-09-02 18:13:02','90'),('1231567488970155','123','324325451',6,0,'2019-09-03 13:36:10','138'),('1231567589888037','123','324325451',2,0,'2019-09-04 17:38:08','46');
/*!40000 ALTER TABLE `porder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` varchar(45) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `uploadtime` datetime DEFAULT CURRENT_TIMESTAMP,
  `pname` varchar(45) DEFAULT NULL,
  `price` varchar(45) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `pcount` int(11) DEFAULT NULL,
  `enabled` int(11) NOT NULL DEFAULT '1',
  `groupid` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `groupid_idx` (`groupid`),
  CONSTRAINT `groupid` FOREIGN KEY (`groupid`) REFERENCES `pgroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('1909031567491662635','897694163',NULL,'test1','270','',99,0,'190903'),('324325451','123','2019-08-09 00:00:00','KKK','23',NULL,97,1,'190808'),('326537547','123','2019-08-08 00:00:00','hhh','45',NULL,97,1,'190903');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `username` varchar(45) NOT NULL,
  `uname` varchar(45) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  `address` text,
  `phone` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `role` varchar(45) NOT NULL DEFAULT 'ROLE_USER',
  `enabled` varchar(45) NOT NULL DEFAULT '1',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('123','sssd','{noop}111','Rm. 5660, 文德文舍學苑F., No.510, Zhongzheng Rd., Xinzhuang Dist.','930557865','circle.z.yaun@gmail.com','ROLE_USER','1'),('897694163','gua','{noop}123','safrwgterhyre','18128037813','yuan.chou@foxmail.com','ROLE_ADMIN','1'),('897694164','usr','{noop}123','rshdrtjdgfj','18128037813','yuan.chou@foxmail.com','ROLE_USER','1');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-04 17:41:46
