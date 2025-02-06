INSERT INTO `TOPICS` (`TITLE`, `DESCRIPTION`) VALUES 
('Java', 'Java is a high-level, class-based, object-oriented programming language.'),
('Spring', 'Spring is a powerful, feature-rich framework for building Java applications.');


INSERT INTO `USERS` (`USERNAME`, `EMAIL`, `PASSWORD`) VALUES 
('user1', 'user1@test.com', '$2a$10$Gf/3z.NzV3o33277IXdbDu37we2Gya0AUjLSaKqielBfCPYAAS9T.'),
('user2', 'user2@test.com', '$2a$10$nESwX00s/1nGDGKe9Ocan.rEAKAAgDu5/2qoFyaKtLgxK/RJwUnXu');

INSERT INTO `SUBSCRIPTIONS` (`TOPICS_ID`, `USER_ID`) VALUES
(1, 1);
