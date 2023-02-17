--
-- Table structure for table `Amendment`
--

DROP TABLE IF EXISTS `Amendment`;
CREATE TABLE `Amendment` (
  `amendId` int NOT NULL AUTO_INCREMENT,
  `index` int NOT NULL,
  `description` varchar(255) NOT NULL,
  `title` varchar(45) NOT NULL,
  `ruleId` int NOT NULL,
  `active` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`amendId`),
  UNIQUE KEY `amendId_UNIQUE` (`amendId`),
  UNIQUE KEY `index_UNIQUE` (`index`),
  KEY `ruleId_idx` (`ruleId`),
  CONSTRAINT `ruleId` FOREIGN KEY (`ruleId`) REFERENCES `Rule` (`ruleId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Amendment`
--

LOCK TABLES `Amendment` WRITE;
INSERT INTO `Amendment` VALUES (2,99,'dghyd','JH',2,1),(3,88,'rendh','SD',2,1),(4,77,'sdghdg','FS',3,1),(5,66,'ghjkgj','ER',3,1),(6,55,'fjff','TR',4,1),(7,44,'ikiki','YT',4,1);
UNLOCK TABLES;

--
-- Table structure for table `Credential`
--

DROP TABLE IF EXISTS `Credential`;
CREATE TABLE `Credential` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `userId` int NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `userIdCred_idx` (`userId`),
  CONSTRAINT `userIdCred` FOREIGN KEY (`userId`) REFERENCES `User` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Credential`
--

LOCK TABLES `Credential` WRITE;
INSERT INTO `Credential` VALUES ('Bobby','qwerty',3),('Smitty','12345',2);
UNLOCK TABLES;

--
-- Table structure for table `Game`
--

DROP TABLE IF EXISTS `Game`;
CREATE TABLE `Game` (
  `gameId` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `createDate` date NOT NULL,
  `currentPlayer` int DEFAULT NULL,
  `userId` int NOT NULL,
  PRIMARY KEY (`gameId`),
  UNIQUE KEY `gameId_UNIQUE` (`gameId`),
  KEY `playerId_idx` (`currentPlayer`),
  KEY `userId_idx` (`userId`),
  CONSTRAINT `playerId` FOREIGN KEY (`currentPlayer`) REFERENCES `Player` (`playerId`),
  CONSTRAINT `userId` FOREIGN KEY (`userId`) REFERENCES `User` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Game`
--

LOCK TABLES `Game` WRITE;
INSERT INTO `Game` VALUES (2,'Rob\'s First Game','2023-02-14',3,2),(3,'Second Game ','2023-02-14',4,3),(4,'The Final Game','2023-02-14',5,4),(5,'Oh No','2023-02-14',6,2);
UNLOCK TABLES;

--
-- Table structure for table `History`
--

DROP TABLE IF EXISTS `History`;
CREATE TABLE `History` (
  `turnId` int NOT NULL AUTO_INCREMENT,
  `action` varchar(127) NOT NULL,
  `playerId` int NOT NULL,
  `gameId` int NOT NULL,
  `RuleId` int DEFAULT NULL,
  `AmendId` int DEFAULT NULL,
  PRIMARY KEY (`turnId`),
  UNIQUE KEY `turnId_UNIQUE` (`turnId`),
  KEY `playerIdHistory_idx` (`playerId`),
  KEY `gameIdHistory_idx` (`gameId`),
  KEY `ruleIdHistory_idx` (`RuleId`),
  KEY `amendIdHistory_idx` (`AmendId`),
  CONSTRAINT `amendIdHistory` FOREIGN KEY (`AmendId`) REFERENCES `Amendment` (`amendId`),
  CONSTRAINT `gameIdHistory` FOREIGN KEY (`gameId`) REFERENCES `Game` (`gameId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `playerIdHistory` FOREIGN KEY (`playerId`) REFERENCES `Player` (`playerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ruleIdHistory` FOREIGN KEY (`RuleId`) REFERENCES `Rule` (`ruleId`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `History`
--

LOCK TABLES `History` WRITE;
INSERT INTO `History` VALUES (9,'Create',3,2,2,NULL),(10,'Amend',3,2,2,5),(11,'Repeal',3,2,NULL,4),(12,'Transmute',3,2,3,2);
UNLOCK TABLES;

--
-- Table structure for table `Player`
--

DROP TABLE IF EXISTS `Player`;
CREATE TABLE `Player` (
  `playerId` int NOT NULL AUTO_INCREMENT,
  `gameId` int NOT NULL,
  `name` varchar(45) NOT NULL,
  `score` int DEFAULT '0',
  `turnOrder` int DEFAULT NULL,
  PRIMARY KEY (`playerId`),
  UNIQUE KEY `playerId_UNIQUE` (`playerId`),
  KEY `gameId_idx` (`gameId`),
  CONSTRAINT `gameId` FOREIGN KEY (`gameId`) REFERENCES `Game` (`gameId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Player`
--

LOCK TABLES `Player` WRITE;
INSERT INTO `Player` VALUES (3,2,'Bob',0,1),(4,3,'Boby',0,1),(5,4,'Table',0,1),(6,5,'Smith',0,1),(7,2,'Joe',0,2),(8,3,'Sasha',0,2),(9,3,'Laurm',0,3),(10,4,'Ipsum',0,2);
UNLOCK TABLES;

--
-- Table structure for table `Rule`
--

DROP TABLE IF EXISTS `Rule`;
CREATE TABLE `Rule` (
  `ruleId` int NOT NULL AUTO_INCREMENT,
  `index` int NOT NULL,
  `description` varchar(255) NOT NULL,
  `title` varchar(45) NOT NULL,
  `gameId` int NOT NULL,
  `active` tinyint NOT NULL DEFAULT '1',
  `mutable` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`ruleId`),
  UNIQUE KEY `ruleId_UNIQUE` (`ruleId`),
  UNIQUE KEY `index_UNIQUE` (`index`),
  KEY `gameIdRule_idx` (`gameId`),
  CONSTRAINT `gameIdRule` FOREIGN KEY (`gameId`) REFERENCES `Game` (`gameId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Rule`
--

LOCK TABLES `Rule` WRITE;
INSERT INTO `Rule` VALUES (2,333,'adfadf','AS',2,1,1),(3,222,'adfad','DF',3,1,1),(4,111,'adfadf','GV',4,1,1),(5,3123,'adfadf','ER',5,1,1),(6,321,'adfadf','RG',2,1,1),(7,123,'adfadf','TH',3,1,1),(8,13413,'adfadf','WE',3,1,1),(9,134,'adfadf','QWERT',4,1,1),(10,12,'adfadf','table',5,1,1);
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `userId` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `userId_UNIQUE` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
INSERT INTO `User` VALUES (2,'Rob'),(3,'Smith'),(4,'Bobby'),(5,'Tables'),(6,'Smitty');
UNLOCK TABLES;
