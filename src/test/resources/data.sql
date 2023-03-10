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
INSERT INTO `User` VALUES (3, 'Game Master');
INSERT INTO `Game` VALUES (42, 'The Journey', '2015-03-14', NULL, 3);

-- Repealing Rules Tests
INSERT INTO `Game` VALUES (4, 'MyGame', '2015-12-17', NULL, 2);
INSERT INTO `Rule` VALUES (6, 6, 'Look at the wall The Sequel', 'MyRule1', 4, 1, 0);
INSERT INTO `Amendment` VALUES (6, 6, 'Or look at the ground again', 'MyAmendment1', 6, 1);
INSERT INTO `Amendment` VALUES (7, 7, 'Or look at the ground again part two', 'MyAmendment1', 6, 1);