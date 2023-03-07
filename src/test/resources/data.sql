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
