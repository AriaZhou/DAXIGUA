-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: dxg
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `porder`
--

DROP TABLE IF EXISTS `porder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8 ;
CREATE TABLE `porder` (
  `id` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `productid` varchar(45) NOT NULL,
  `ocount` int(11) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '0',
  `time` datetime NOT NULL,
  `price` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_idx` (`username`),
  KEY `product_idx` (`productid`),
  CONSTRAINT `product` FOREIGN KEY (`productid`) REFERENCES `product` (`id`),
  CONSTRAINT `user` FOREIGN KEY (`username`) REFERENCES `user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `porder`
--

LOCK TABLES `porder` WRITE;
/*!40000 ALTER TABLE `porder` DISABLE KEYS */;
INSERT INTO `porder` VALUES ('1231566634677073','123','190801',1,0,'2019-08-24 16:17:00','45'),('1231566634685191','123','190834',2,0,'2019-08-24 16:18:00','134'),('8976941641566635675519','897694164','190801',1,0,'2019-08-24 16:34:00','45');
/*!40000 ALTER TABLE `porder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8 ;
CREATE TABLE `product` (
  `id` varchar(45) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `uploadtime` datetime DEFAULT NULL,
  `pname` varchar(45) NOT NULL,
  `price` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `pcount` int(11) DEFAULT NULL,
  `starttime` datetime NOT NULL,
  `endtime` datetime NOT NULL,
  `enabled` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('190708',NULL,NULL,'多少个','78','都给我买！',99,'2019-08-01 00:00:00','2019-09-30 00:00:00',1),('190801',NULL,NULL,'发表是不是','45','都给我买！',99,'2019-09-30 00:00:00','2019-09-30 00:00:00',1),('190820',NULL,NULL,'魏国强爱人','34','都给我买！',99,'2019-09-30 00:00:00','2019-09-30 00:00:00',1),('190834',NULL,NULL,'违法','67','都给我买！',99,'2019-09-30 00:00:00','2019-09-30 00:00:00',1),('190912','',NULL,'小橘里汁','111','都给我买！',99,'2019-08-01 00:00:00','2019-09-30 00:00:00',1),('192076',NULL,NULL,'三个','93','都给我买！',99,'2019-08-01 00:00:00','2019-09-30 00:00:00',1),('197579',NULL,NULL,'二分干','99','都给我买！',99,'2019-08-01 00:00:00','2019-09-30 00:00:00',1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8 ;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('123','sd','{noop}111','Rm. 5660, 文德文舍學苑F., No.510, Zhongzheng Rd., Xinzhuang Dist.','930557865','circle.z.yaun@gmail.com','ROLE_USER','1'),('897694163','gua','{noop}123','safrwgterhyre','18128037813','yuan.chou@foxmail.com','ROLE_ADMIN','1'),('897694164','usr','{noop}123','rshdrtjdgfj','18128037813','yuan.chou@foxmail.com','ROLE_USER','1');
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

-- Dump completed on 2019-08-24 20:31:36
