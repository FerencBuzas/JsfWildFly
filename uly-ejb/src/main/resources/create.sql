-- Create database tables. --

CREATE USER IF NOT EXISTS 'feri'@'localhost' IDENTIFIED BY 'feri';
GRANT ALL PRIVILEGES ON *.* TO 'feri'@'localhost' WITH GRANT OPTION;

DROP DATABASE IF EXISTS uly;

CREATE DATABASE uly;

USE uly;

-- -------------------------------------

CREATE TABLE `Myuser` (
  `login_name` varchar(255) NOT NULL,
  `full_name`  varchar(255) NOT NULL,
  `password`   varchar(255) NOT NULL,
  
  PRIMARY KEY (`login_name`)
);

CREATE TABLE `Myrole` (
  `princ_id` varchar(255) NOT NULL,
  `role_name`  varchar(255) NOT NULL,
  
  PRIMARY KEY (`princ_id`, `role_name`),
  CONSTRAINT `fkPrincId` FOREIGN KEY (`princ_id`) REFERENCES `Myuser` (`login_name`)
);

CREATE TABLE `Event` (
  `id`         bigint(20) NOT NULL,
  `date`       datetime DEFAULT CURRENT_TIMESTAMP,
  `user_id`    varchar(255) NOT NULL,
  `type`       varchar(255) DEFAULT NULL,
  `info`       varchar(255) DEFAULT NULL,
  `success`    bit(1) NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`),
  KEY `key_ev_user_id` (`user_id`),
  CONSTRAINT `FK_ev_user_id` FOREIGN KEY (`user_id`) REFERENCES `Myuser` (`login_name`)
);

CREATE TABLE `Request` (
  `id`         bigint(20) NOT NULL,
  `state`      varchar(255) DEFAULT NULL,
  `sum`        bigint(20) NOT NULL,
  `user_id`    varchar(255) NOT NULL,
  
  PRIMARY KEY (`id`),
  KEY `key_req_user` (`user_id`),
  CONSTRAINT `FK_req_user_id` FOREIGN KEY (`user_id`) REFERENCES `Myuser` (`login_name`) 
);
