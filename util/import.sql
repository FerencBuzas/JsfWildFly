-- Create some test data. --
-- Call this after create.sql.

USE jsfwf;

-- Scrambled passwords of the users
--   'feri',   '1k697MUfDmAxLMue4/AKSl9isIIaG+rjOaw+erlGR4c='
--   'admin2', 'HBQrLQGqNOmja95IBkWlf9aeFBVdrPq1o/kle3f9yNg='
--   'admin3', 'T8K1ZzogGtmx/APcs0bhuq1ENR2qBQPVU0tN/cxDMuA='
--   'bea',    'tuFVeh7TkA176OKLtfE32RgS8x5ZqQzt2sAna8Uy0Y4='
--   'marci',  'PxYSa1DXlVrZic1NSTSYfh+pvLAoePIhA5j3EsAtxDU='
--   'guest',  'hJg8YPfarcHLhphiH4AsDZ+aPDwpXIEHSPsEgRXBhuw='

INSERT INTO `Myuser` (login_name, full_name, password) VALUES ('feri',   'Buzas Feri',  'feri');
INSERT INTO `Myuser` (login_name, full_name, password) VALUES ('bea',    'Buzas Bea',   'bea');
INSERT INTO `Myuser` (login_name, full_name, password) VALUES ('admin2', 'Admin 2',     'admin2');
INSERT INTO `Myuser` (login_name, full_name, password) VALUES ('admin3', 'Admin 3',     'admin3');
INSERT INTO `Myuser` (login_name, full_name, password) VALUES ('marci',  'Buzas Marci', 'marci');
INSERT INTO `Myuser` (login_name, full_name, password) VALUES ('guest',  'Guest user',  'guest');

INSERT INTO `Myrole` (princ_id, role_name) VALUES ('feri',   'Admin');
INSERT INTO `Myrole` (princ_id, role_name) VALUES ('feri',   'User');
INSERT INTO `Myrole` (princ_id, role_name) VALUES ('bea',    'User');
INSERT INTO `Myrole` (princ_id, role_name) VALUES ('admin2', 'Admin');
INSERT INTO `Myrole` (princ_id, role_name) VALUES ('admin2', 'User');
INSERT INTO `Myrole` (princ_id, role_name) VALUES ('admin3', 'Admin');
INSERT INTO `Myrole` (princ_id, role_name) VALUES ('marci',  'User');
INSERT INTO `Myrole` (princ_id, role_name) VALUES ('guest', 'User');

-- Some test data for development
INSERT INTO Request (id, user_id, sum, state) VALUES (1, 'bea',   100010,  'Accepted');
INSERT INTO Request (id, user_id, sum, state) VALUES (2, 'bea',   200010,  'Requested');
INSERT INTO Request (id, user_id, sum, state) VALUES (3, 'marci', 200020,  'Accepted');
INSERT INTO Request (id, user_id, sum, state) VALUES (4, 'marci', 300020,  'Requested');
INSERT INTO Request (id, user_id, sum, state) VALUES (5, 'guest',  50030,  'Rejected');
INSERT INTO Request (id, user_id, sum, state) VALUES (6, 'guest',  50031,  'Requested');

INSERT INTO `Event` (user_id, type, info, success) VALUES ('bea',   'Accept', '', 1); 
INSERT INTO `Event` (user_id, type, info, success) VALUES ('marci', 'Request', '', 0 ); 
INSERT INTO `Event` (user_id, type, info, success) VALUES ('guest', 'Modify', 'test1->test2', 0);
