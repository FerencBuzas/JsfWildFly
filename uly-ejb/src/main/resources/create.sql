-- Create database, tables and some test data. --

DROP DATABASE IF EXISTS uly;

CREATE DATABASE uly;

CREATE USER IF NOT EXISTS feri IDENTIFIED BY 'feri';
CREATE USER IF NOT EXISTS bea IDENTIFIED BY 'bea';
CREATE USER IF NOT EXISTS marci IDENTIFIED BY 'marci';

USE uly;

GRANT ALL ON uly TO feri;
GRANT ALL ON uly TO bea;
GRANT ALL ON uly TO marci;

-- -------------------------------------

CREATE TABLE `Myuser` (
  `id`         bigint(20) NOT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `name`       varchar(255) NOT NULL,
  `password`   varchar(255) NOT NULL,
  `role`       varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

CREATE TABLE `Request` (
  `id`         bigint(20) NOT NULL,
  `state`      varchar(255) DEFAULT NULL,
  `sum`        bigint(20) NOT NULL,
  `user_id`    bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key_req_user` (`user_id`),
  CONSTRAINT `FK_req_user_id` FOREIGN KEY (`user_id`) REFERENCES `Myuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
