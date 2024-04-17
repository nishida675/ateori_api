CREATE TABLE user_table
(
   user_id INT NOT NULL AUTO_INCREMENT,
   user_name VARCHAR(10) NOT NULL UNIQUE,
   pass VARCHAR(50) NOT NULL,
   count INT DEFAULT 0 NOT NULL,
   PRIMARY KEY(user_id)
);