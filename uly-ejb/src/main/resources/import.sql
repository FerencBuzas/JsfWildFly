-- Create database, tables and some test data. --

USE uly;

TRUNCATE TABLE Event;
TRUNCATE TABLE Request;
TRUNCATE TABLE Myuser;

INSERT INTO Myuser (id, name, login_name, password, role) VALUES (1, 'Buzas Feri',  'feri',  'feri', 'Admin');
INSERT INTO Myuser (id, name, login_name, password, role) VALUES (2, 'Buzas Bea',   'bea',   'bea',  'User');
INSERT INTO Myuser (id, name, login_name, password, role) VALUES (3, 'Buzas Marci', 'marci', 'marci', 'User');

INSERT INTO Request (id, user_id, sum, state) VALUES (1, 1,  400000,  'Accepted');
INSERT INTO Request (id, user_id, sum, state) VALUES (2, 1,  400000,  'Requested');
INSERT INTO Request (id, user_id, sum, state) VALUES (3, 2,  200000,  'Accepted');
INSERT INTO Request (id, user_id, sum, state) VALUES (4, 2,  200000,  'Requested');
INSERT INTO Request (id, user_id, sum, state) VALUES (5, 3,  100000,  'Rejected');
INSERT INTO Request (id, user_id, sum, state) VALUES (6, 3,  100000,  'Requested');

INSERT INTO Event (id, date, user_id, type, info, success) VALUES (1, now(), 1, 'Accept', '', 1); 
INSERT INTO Event (id, date, user_id, type, info, success) VALUES (2, now(), 2, 'Request', '', 0); 
INSERT INTO Event (id, date, user_id, type, info, success) VALUES (3, now(), 3, 'Modify', 'test1->test2', 0); 
