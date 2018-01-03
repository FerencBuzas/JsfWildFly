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

INSERT INTO Event (id, date, user_id, event_name, success) VALUES (1, now(), 1, 'accept', 1); 
INSERT INTO Event (id, date, user_id, event_name, success) VALUES (2, now(), 1, 'request', 0); 
INSERT INTO Event (id, date, user_id, event_name, success) VALUES (3, now(), 2, 'accept', 1); 
INSERT INTO Event (id, date, user_id, event_name, success) VALUES (4, now(), 2, 'request', 1); 
INSERT INTO Event (id, date, user_id, event_name, success) VALUES (5, now(), 3, 'reject', 1); 
INSERT INTO Event (id, date, user_id, event_name, success) VALUES (6, now(), 3, 'request', 1); 
