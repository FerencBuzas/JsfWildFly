-- Create database, tables and some test data. --

DROP DATABASE IF EXISTS uly;
CREATE DATABASE uly;

CREATE USER IF NOT EXISTS feri IDENTIFIED BY 'feri';
CREATE USER IF NOT EXISTS bea IDENTIFIED BY 'bea';
CREATE USER IF NOT EXISTS marci IDENTIFIED BY 'marci';

USE uly;

-- GRANT ALL PRIVILEGES ON uly.* TO 'feri'@'localhost';
GRANT ALL PRIVILEGES ON uly TO 'feri';
GRANT ALL PRIVILEGES ON uly TO 'bea';
GRANT ALL PRIVILEGES ON uly TO 'marci';

DROP TABLE IF EXISTS myuser, request, event;

-- -------------------------------------------------
CREATE TABLE myuser
  (id         int(5) NOT NULL PRIMARY KEY,
   name       varchar(40) NOT NULL,
   login_name varchar(20) NOT NULL,
   password   varchar(20),
   role       char(1)
  );

INSERT INTO myuser VALUES (1, 'Buzas Feri',  'feri',  'feri',  'A');
INSERT INTO myuser VALUES (2, 'Buzas Bea',   'bea',   'bea',   'U');
INSERT INTO myuser VALUES (3, 'Buzas Marci', 'marci', 'marci', 'U');

-- -------------------------------------------------
CREATE TABLE request
  (id       int(5) NOT NULL PRIMARY KEY,
   user     int(5) NOT NULL, -- REFERENCES myuser,
   sum      int(8),
   state    char(1)   -- 'R'eq, 'A'cc, 'X' (rejected)
  );

INSERT INTO request VALUES (1, 1,  400000,  'A');
INSERT INTO request VALUES (2, 1,  400000,  'R');
INSERT INTO request VALUES (3, 2,  200000,  'A');
INSERT INTO request VALUES (4, 2,  200000,  'R');
INSERT INTO request VALUES (5, 3,  100000,  'X');
INSERT INTO request VALUES (6, 3,  100000,  'R');

-- -------------------------------------------------
CREATE TABLE event
  (id        int(5) NOT NULL PRIMARY KEY,
   time      datetime NOT NULL,    
   user      int(5) NOT NULL, -- REFERENCES myuser,
   eventName varchar(20) NOT NULL,
   success   char(1)   -- 'Y', 'N'
  );

INSERT INTO event VALUES (1, now(), 1, 'accept', 'Y'); 
INSERT INTO event VALUES (2, now(), 1, 'request', 'Y'); 
INSERT INTO event VALUES (3, now(), 2, 'accept', 'Y'); 
INSERT INTO event VALUES (4, now(), 2, 'request', 'Y'); 
INSERT INTO event VALUES (5, now(), 3, 'reject', 'Y'); 
INSERT INTO event VALUES (6, now(), 3, 'request', 'Y'); 
