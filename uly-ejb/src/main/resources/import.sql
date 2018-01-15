-- Create database, tables and some test data. --

USE uly;

-- TRUNCATE TABLE Event;
-- TRUNCATE TABLE Request;
-- TRUNCATE TABLE Myuser;
-- TRUNCATE TABLE Role;

-- The required 3-3 users
-- INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
--   (1, 'Buzas Feri', 'feri', '1k697MUfDmAxLMue4/AKSl9isIIaG+rjOaw+erlGR4c=', 'Admin');
-- INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
--   (2, 'Admin 2', 'admin2', 'HBQrLQGqNOmja95IBkWlf9aeFBVdrPq1o/kle3f9yNg=', 'Admin');
-- INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
--   (3, 'Admin 3', 'admin3', 'T8K1ZzogGtmx/APcs0bhuq1ENR2qBQPVU0tN/cxDMuA=', 'Admin');
-- INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
--   (4, 'Buzas Bea', 'bea', 'tuFVeh7TkA176OKLtfE32RgS8x5ZqQzt2sAna8Uy0Y4=', 'User');
-- INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
--   (5, 'Buzas Marci', 'marci', 'PxYSa1DXlVrZic1NSTSYfh+pvLAoePIhA5j3EsAtxDU=', 'User');
-- INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
--   (6, 'Guest user', 'guest', 'hJg8YPfarcHLhphiH4AsDZ+aPDwpXIEHSPsEgRXBhuw=', 'User');

INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
  (1, 'Buzas Feri', 'feri', 'feri', 'Admin');
INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
  (2, 'Admin 2', 'admin2', 'admin2', 'Admin');
INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
  (3, 'Admin 3', 'admin3', 'admin3', 'Admin');
INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
  (4, 'Buzas Bea', 'bea', 'bea', 'User');
INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
  (5, 'Buzas Marci', 'marci', 'marci', 'User');
INSERT INTO `Myuser` (id, name, login_name, password, role) VALUES
  (6, 'Guest user', 'guest', 'guest', 'User');

INSERT INTO `Roles` (PrincipalID, Role, RoleGroup) VALUES ('feri',   'Admin', 'Admin');
INSERT INTO `Roles` (PrincipalID, Role, RoleGroup) VALUES ('admin1', 'Admin', 'Admin');
INSERT INTO `Roles` (PrincipalID, Role, RoleGroup) VALUES ('admin2', 'Admin', 'Admin');
INSERT INTO `Roles` (PrincipalID, Role, RoleGroup) VALUES ('bea',    'User', 'User');
INSERT INTO `Roles` (PrincipalID, Role, RoleGroup) VALUES ('marci',  'User', 'User');
INSERT INTO `Roles` (PrincipalID, Role, RoleGroup) VALUES ('guest', 'User', 'User');

-- Some test data for development
INSERT INTO Request (id, user_id, sum, state) VALUES (1, 4,  400000,  'Accepted');
INSERT INTO Request (id, user_id, sum, state) VALUES (2, 4,  400000,  'Requested');
INSERT INTO Request (id, user_id, sum, state) VALUES (3, 5,  200000,  'Accepted');
INSERT INTO Request (id, user_id, sum, state) VALUES (4, 5,  200000,  'Requested');
INSERT INTO Request (id, user_id, sum, state) VALUES (5, 6,  100000,  'Rejected');
INSERT INTO Request (id, user_id, sum, state) VALUES (6, 6,  100000,  'Requested');

INSERT INTO `Event` (id, date, user_id, type, info, success) VALUES (1, now(), 4, 'Accept', '', 1); 
INSERT INTO `Event` (id, date, user_id, type, info, success) VALUES (2, now(), 5, 'Request', '', 0); 
INSERT INTO `Event` (id, date, user_id, type, info, success) VALUES (3, now(), 6, 'Modify', 'test1->test2', 0);

