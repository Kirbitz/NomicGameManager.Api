-- Rules and Amendments Tests

INSERT INTO `User` VALUES (2, 'Master Tester');
INSERT INTO `Game` VALUES (1, 'MyGame', '2015-12-17', NULL, 2);
INSERT INTO `Game` VALUES (2, 'MyGame2', '2015-12-18', NULL, 2);
INSERT INTO `Rule` VALUES (1, 1, 'Look at the wall', 'MyRule1', 1, 1, 0);
INSERT INTO `Rule` VALUES (2, 2, 'Play Undertale', 'MyRule2', 1, 1, 0);
INSERT INTO `Amendment` VALUES (1, 1, 'Or look at the ground', 'MyAmendment1', 1, 0);
