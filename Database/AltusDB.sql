-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: altus
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `brand_models`
--

DROP TABLE IF EXISTS `brand_models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand_models` (
  `brand_model_id` int NOT NULL AUTO_INCREMENT,
  `service_id` int NOT NULL,
  `brand_name` varchar(20) NOT NULL,
  `model` varchar(20) NOT NULL,
  `min_year` smallint NOT NULL,
  PRIMARY KEY (`brand_model_id`),
  KEY `service_id` (`service_id`),
  CONSTRAINT `brand_models_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `vehicle_service` (`service_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brand_models`
--

LOCK TABLES `brand_models` WRITE;
/*!40000 ALTER TABLE `brand_models` DISABLE KEYS */;
INSERT INTO `brand_models` VALUES (1,1,'Suzuki','Old Swift',2010),(2,1,'Suzuki','New Swift',2024);
/*!40000 ALTER TABLE `brand_models` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drivers`
--

DROP TABLE IF EXISTS `drivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drivers` (
  `driver_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `licence_number` varchar(15) DEFAULT NULL,
  `is_document_verified` tinyint(1) DEFAULT '0',
  `licence_photo` varchar(255) DEFAULT NULL,
  `is_available` tinyint(1) DEFAULT NULL,
  `verification_status` varchar(20) NOT NULL DEFAULT '0',
  `comment` varchar(254) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` enum('System','Admin') DEFAULT 'System',
  `updated_by` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`driver_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `licence_number` (`licence_number`),
  CONSTRAINT `drivers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drivers`
--

LOCK TABLES `drivers` WRITE;
/*!40000 ALTER TABLE `drivers` DISABLE KEYS */;
INSERT INTO `drivers` VALUES (1,3,NULL,0,NULL,NULL,'0',NULL,'2025-03-11 18:10:19','2025-03-11 18:10:19','System',NULL),(2,4,NULL,0,NULL,NULL,'0',NULL,'2025-03-11 18:10:33','2025-03-11 18:10:33','System',NULL),(3,8,NULL,0,NULL,NULL,'0',NULL,'2025-03-12 15:40:10','2025-03-12 15:40:10','System',NULL);
/*!40000 ALTER TABLE `drivers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(10) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Admin'),(2,'Customer'),(3,'Driver');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone_no` varchar(10) NOT NULL,
  `email_id` varchar(254) NOT NULL,
  `password` varchar(20) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `display_id` varchar(10) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` enum('System','Admin') DEFAULT 'System',
  `updated_by` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `phone_no` (`phone_no`),
  UNIQUE KEY `email_id` (`email_id`),
  UNIQUE KEY `display_id` (`display_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,1,'Krushit','Babariya','7777777777','ksb@admin.in','sadmin@123',1,'SUPERADMIN','2025-03-11 18:09:18','2025-03-11 18:09:18','System',NULL),(2,2,'Arpan','Shah','8523697415','ajs@narola.email','Arpan@113',0,'US0002R84','2025-03-11 18:10:07','2025-03-11 18:10:07','System',NULL),(3,3,'Vinay','Adatiya','4563217896','vad@narola.email','Vinay@123',0,'DR0003V413','2025-03-11 18:10:19','2025-03-12 10:28:50','System',NULL),(4,3,'Charan','Panday','0654123987','charan@gmail.com','Charan@123',0,'DR0004V886','2025-03-11 18:10:33','2025-03-11 18:10:33','System',NULL),(5,2,'Karan','Panday','4445556667','karan@yahoo.mail','Karan@123',0,'US0005R185','2025-03-11 18:16:45','2025-03-11 18:16:45','System',NULL),(6,2,'Rashmika','Mandana','8885552221','rashu@email.com','Rashu@123',0,'US0006R26','2025-03-11 18:22:19','2025-03-11 18:22:19','System',NULL),(7,2,'Aline','Patel','9899995499','alina@gmail.com','Alina@123',0,'US0007R208','2025-03-12 15:34:55','2025-03-12 15:34:55','System',NULL),(8,3,'David','Singh','8888895499','david@gmail.com','David@123',0,'DR0008V436','2025-03-12 15:40:10','2025-03-12 15:40:10','System',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_service`
--

DROP TABLE IF EXISTS `vehicle_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_service` (
  `service_id` int NOT NULL AUTO_INCREMENT,
  `service_name` varchar(50) NOT NULL,
  `base_fare` decimal(10,2) NOT NULL,
  `per_km_rate` decimal(10,2) NOT NULL,
  `vehicle_type` enum('2W','3W','4W') NOT NULL,
  `max_passengers` int NOT NULL,
  `commission_percentage` decimal(3,1) NOT NULL,
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `service_name` (`service_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_service`
--

LOCK TABLES `vehicle_service` WRITE;
/*!40000 ALTER TABLE `vehicle_service` DISABLE KEYS */;
INSERT INTO `vehicle_service` VALUES (1,'Altus Go',100.00,20.50,'4W',4,8.0);
/*!40000 ALTER TABLE `vehicle_service` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-12 16:40:30
