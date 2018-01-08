-- Create database, tables and some test data. --

USE uly;

TRUNCATE TABLE Event;
TRUNCATE TABLE Request;
TRUNCATE TABLE Myuser;

-- The required 3-3 users
INSERT INTO Myuser (id, name, login_name, password, role) VALUES (1, 'Buzas Feri',  'feri',   'feri',  'Admin');
INSERT INTO Myuser (id, name, login_name, password, role) VALUES (2, 'Admin 2',     'admin2', 'bea',   'Admin');
INSERT INTO Myuser (id, name, login_name, password, role) VALUES (3, 'Admin 3',     'admin3', 'bea',   'Admin');
INSERT INTO Myuser (id, name, login_name, password, role) VALUES (4, 'Buzas Bea',   'bea',    'bea',   'User');
INSERT INTO Myuser (id, name, login_name, password, role) VALUES (5, 'Buzas Marci', 'marci',  'marci', 'User');
INSERT INTO Myuser (id, name, login_name, password, role) VALUES (6, 'Guest user',  'guest',  'guest', 'User');

-- Some test data for development
INSERT INTO Request (id, user_id, sum, state) VALUES (1, 4,  400000,  'Accepted');
INSERT INTO Request (id, user_id, sum, state) VALUES (2, 4,  400000,  'Requested');
INSERT INTO Request (id, user_id, sum, state) VALUES (3, 5,  200000,  'Accepted');
INSERT INTO Request (id, user_id, sum, state) VALUES (4, 5,  200000,  'Requested');
INSERT INTO Request (id, user_id, sum, state) VALUES (5, 6,  100000,  'Rejected');
INSERT INTO Request (id, user_id, sum, state) VALUES (6, 6,  100000,  'Requested');

INSERT INTO Event (id, date, user_id, type, info, success) VALUES (1, now(), 4, 'Accept', '', 1); 
INSERT INTO Event (id, date, user_id, type, info, success) VALUES (2, now(), 5, 'Request', '', 0); 
INSERT INTO Event (id, date, user_id, type, info, success) VALUES (3, now(), 6, 'Modify', 'test1->test2', 0); 
