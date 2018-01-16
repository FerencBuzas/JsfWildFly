-- Create database, tables and some test data. --

DROP DATABASE IF EXISTS uly;

CREATE DATABASE uly;

USE uly;

-- -------------------------------------

-- used only for JAAS
CREATE TABLE `Roles` (
  `PrincipalID` varchar(255) NOT NULL,
  `Role`        varchar(255) NOT NULL,
  `RoleGroup`   varchar(255) NOT NULL,
  PRIMARY KEY (`PrincipalID`, `Role`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- both for JAAS and the application
CREATE TABLE `Myuser` (
  `id`         bigint(20) NOT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `name`       varchar(255) NOT NULL,
  `password`   varchar(255) NOT NULL,
  `role`        varchar(255) NOT NULL,      -- TODO: redundant
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- for the app only
CREATE TABLE `Event` (
  `id`         bigint(20) NOT NULL,
  `date`       datetime DEFAULT NULL,
  `event_name` varchar(255) DEFAULT NULL,
  `success`    bit(1) NOT NULL,
  `user_id`    bigint(20) DEFAULT NULL,
  `info`       varchar(255) DEFAULT NULL,
  `type`       varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key_ev_user_id` (`user_id`),
  CONSTRAINT `FK_ev_user_id` FOREIGN KEY (`user_id`) REFERENCES `Myuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- for the app only
CREATE TABLE `Request` (
  `id`         bigint(20) NOT NULL,
  `state`      varchar(255) DEFAULT NULL,
  `sum`        bigint(20) NOT NULL,
  `user_id`    bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key_req_user` (`user_id`),
  CONSTRAINT `FK_req_user_id` FOREIGN KEY (`user_id`) REFERENCES `Myuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
