INSERT INTO locator.user (id,email, firstname, lastname, password,token) VALUES (1,'a@wp.pl','ABC','CBA',123,'432f-34fw-w3q');
INSERT INTO locator.location (id,latitude,longitude,accuracy,time) VALUES (1,'25.363','31.044','2.0','2016-12-18T10:45:35.230');
INSERT INTO locator.user_location (User_id, location_id) VALUES (1,1);
INSERT INTO locator.user (id,email, firstname, lastname, password,token) VALUES (2,'b@wp.pl','ABC','CBA',123,'2f-34fw-w3q');
INSERT INTO locator.location (id,latitude,longitude,accuracy,time) VALUES (2,'54.34','65.12','3.0','2016-12-15T10:45:35.230');
INSERT INTO locator.user_location (User_id, location_id) VALUES (2,2);
INSERT INTO locator.user_user (User_id,friends_id) VALUES(1,2);
INSERT INTO locator.user (id,email, firstname, lastname, password,token) VALUES (3,'bczyzowski@wp.pl','bczyz','czyz',123,'54-34fw-w3q');