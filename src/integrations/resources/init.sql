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
  CONSTRAINT `userId` FOREIGN KEY (`userId`) REFERENCES `User` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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

-- Add the foreign key constraint now that player table has been created
ALTER TABLE `Game`
    ADD FOREIGN KEY (`currentPlayer`)
    REFERENCES `Player` (`playerId`);


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
  KEY `gameIdRule_idx` (`gameId`),
  CONSTRAINT `gameIdRule` FOREIGN KEY (`gameId`) REFERENCES `Game` (`gameId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
  KEY `ruleId_idx` (`ruleId`),
  CONSTRAINT `ruleId` FOREIGN KEY (`ruleId`) REFERENCES `Rule` (`ruleId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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


-- Login tests data

INSERT INTO `User` VALUES (10, 'Agamemnon');
INSERT INTO `User` VALUES (5, 'Cincinnatus'), (1, 'Foo Bar Jr.'), (3, 'Alcibiades'), (100, 'Samwise Gamgee');
INSERT INTO `Credential` VALUES ('TestUser', '$argon2id$v=19$m=16384,t=2,p=1$UUtqVlF6Wk1adkxmaXdKQQ$/6PJsEAGnyNRx1/b9/q7OWBp6SvKXIajQVRuswKQIFA', 1);

-- Rules and Amendments Tests

INSERT INTO `User` VALUES (2, 'Master Tester');

INSERT INTO `Game` VALUES (1, 'MyGame', '2015-12-17', NULL, 2);
INSERT INTO `Rule` VALUES (1, 1, 'Look at the wall', 'MyRule1', 1, 1, 0);
INSERT INTO `Amendment` VALUES (1, 1, 'Or look at the ground', 'MyAmendment1', 1, 1);
INSERT INTO `Rule` VALUES (2, 2, 'Play Undertale', 'MyRule2', 1, 1, 0);
INSERT INTO `Rule` VALUES (3, 3, 'Jump off a bridge', 'MyRule3', 1, 1, 0);
INSERT INTO `Amendment` VALUES (2, 2, 'Take a photo while doing it', 'MyAmendment2', 3, 1);
INSERT INTO `Amendment` VALUES (3, 3, 'Smile real wide', 'MyAmendment3', 3, 1);
INSERT INTO `Rule` VALUES (5, 5, 'Talk politics', 'MyRule4', 1, 1, 0);
INSERT INTO `Amendment` VALUES (5, 5, 'Also talk religion', 'MyInactiveAmendment1', 5, 0);

INSERT INTO `Game` VALUES (2, 'MyGame2', '2015-12-18', NULL, 2);

INSERT INTO `Game` VALUES (3, 'MyGame3', '2015-12-18', NULL, 2);
INSERT INTO `Rule` VALUES (4, 4, 'Run in circles', 'MyInactiveRule1', 3, 0, 0);
INSERT INTO `Amendment` VALUES (4, 4, 'Run slowly', 'MyAmendment4', 4, 1);

-- Games Tests

INSERT INTO `User` VALUES (4, 'Game Master');
INSERT INTO `Game` VALUES (42, 'The Journey', '2015-03-14', NULL, 3);

-- Enacting Rules Tests

-- Do not create a game with value 5.
INSERT INTO `Game` VALUES (6, 'The Fifth Game', '2023-03-20', NULL, 2);

-- Repealing Rules and Amendments Tests

INSERT INTO `Game` VALUES (4, 'MyGame', '2015-12-17', NULL, 2);
INSERT INTO `Rule` VALUES (6, 6, 'Look at the wall The Sequel', 'MyRule1', 4, 1, 0);
INSERT INTO `Rule` VALUES (7, 7, 'Repeal Amendments', 'rule', 4, 1, 0);
INSERT INTO `Amendment` VALUES (6, 6, 'Or look at the ground again', 'MyAmendment1', 6, 1);
INSERT INTO `Amendment` VALUES (7, 7, 'Or look at the ground again part two', 'MyAmendment1', 6, 1);
INSERT INTO `Amendment` VALUES (8, 8, 'Test for repealing amendments', 'repealAmend', 7, 1);

