-- Login tests data

INSERT INTO `User` VALUES (1, 'Foo Bar Jr.');
INSERT INTO `Credential` VALUES ('TestUser', '$argon2id$v=19$m=16384,t=2,p=1$UUtqVlF6Wk1adkxmaXdKQQ$/6PJsEAGnyNRx1/b9/q7OWBp6SvKXIajQVRuswKQIFA', 1);


-- Rules and Amendments Tests

INSERT INTO `User` VALUES (2, 'Master Tester');
INSERT INTO `Game` VALUES (1, 'MyGame', '2015-12-17', NULL, 2);
INSERT INTO `Game` VALUES (2, 'MyGame2', '2015-12-18', NULL, 2);
INSERT INTO `Rule` VALUES (1, 1, 'Look at the wall', 'MyRule1', 1, 1, 0);
INSERT INTO `Rule` VALUES (2, 2, 'Play Undertale', 'MyRule2', 1, 1, 0);
INSERT INTO `Rule` VALUES (3, 3, 'Jump off a bridge', 'MyRule2', 1, 1, 0);
INSERT INTO `Amendment` VALUES (1, 1, 'Or look at the ground', 'MyAmendment1', 1, 0);
INSERT INTO `Amendment` VALUES (2, 2, 'Take a photo while doing it', 'MyAmendment2', 3, 0);
INSERT INTO `Amendment` VALUES (3, 3, 'Smile real wide', 'MyAmendment3', 3, 0);
