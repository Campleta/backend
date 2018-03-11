LOCK TABLES `campleta_test`.`roles` WRITE;
INSERT INTO `campleta_test`.`roles` (NAME) VALUES ('Guest');
INSERT INTO `campleta_test`.`roles` (NAME) VALUES ('User');
INSERT INTO `campleta_test`.`roles` (NAME) VALUES ('Admin');
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`campsites` WRITE;
INSERT INTO `campleta_test`.`campsites` (ID, NAME) VALUES (1, 'Marina di Venezia');
INSERT INTO `campleta_test`.`campsites` (ID, NAME) VALUES (2, "Campleta Gili");
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`users` WRITE;
INSERT INTO `campleta_test`.`users` (ID, EMAIL, FIRSTNAME, LASTNAME, PASSPORT, PASSWORD) VALUES (2,'test@campleta.com','Test','Lastname','12345678','b0f3dc043a9c5c05f67651a8c9108b4c2b98e7246b2eea14cb204295');
INSERT INTO `campleta_test`.`users` (ID) VALUES (3);
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`users_roles` WRITE;
INSERT INTO `campleta_test`.`users_roles` (USERS_ID, ROLES_NAME) VALUES(2, "User");
INSERT INTO `campleta_test`.`users_roles` (USERS_ID, ROLES_NAME) VALUES(2, "Admin");
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`areatypes` WRITE;
INSERT INTO `campleta_test`.`areatypes` (NAME) VALUES("Tent");
INSERT INTO `campleta_test`.`areatypes` (NAME) VALUES("Caravan");
INSERT INTO `campleta_test`.`areatypes` (NAME) VALUES("House");
INSERT INTO `campleta_test`.`areatypes` (NAME) VALUES("Big House");
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`areas` WRITE;
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(1, "A1", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(2, "A2", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(3, "A3", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(4, "A4", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(5, "A5", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(6, "B1", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(7, "B2", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(8, "B3", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(9, "B4", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(10, "B5", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(11, "C1", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(12, "C2", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(13, "C3", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(14, "C4", 1);
INSERT INTO `campleta_test`.`areas` (ID, NAME, CAMPSITE_ID) VALUES(15, "C5", 1);
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`areas_areatypes` WRITE;
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(1, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(1, 2);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(2, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(2, 2);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(3, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(3, 2);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(4, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(4, 2);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(5, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(5, 2);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(6, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(7, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(8, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(9, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(10, 1);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(11, 3);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(12, 3);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(13, 3);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(14, 3);
INSERT INTO `campleta_test`.`areas_areatypes` (AREA_ID, AREATYPE_ID) VALUES(15, 3);
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`campsites_areatypes` WRITE;
INSERT INTO `campleta_test`.`campsites_areatypes` (AREATYPE_ID, CAMPSITE_ID) VALUES(1, 1);
INSERT INTO `campleta_test`.`campsites_areatypes` (AREATYPE_ID, CAMPSITE_ID) VALUES(2, 1);
INSERT INTO `campleta_test`.`campsites_areatypes` (AREATYPE_ID, CAMPSITE_ID) VALUES(3, 1);
INSERT INTO `campleta_test`.`campsites_areatypes` (AREATYPE_ID, CAMPSITE_ID) VALUES(4, 1);
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`campsites_users` WRITE;
INSERT INTO `campleta_test`.`campsites_users` (CAMPSITE_ID, USER_ID) VALUES(1, 2);
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`reservations` WRITE;
INSERT INTO `campleta_test`.`reservations` (ID, STARTDATE, ENDDATE, CAMPSITE_ID, AREATYPE_ID, AREA_ID) VALUES (1, "2018-08-07 10:00:00", "2018-08-24 09:59:00", 1, 1, 1);
INSERT INTO `campleta_test`.`reservations` (ID, STARTDATE, ENDDATE, CAMPSITE_ID, AREATYPE_ID, AREA_ID) VALUES (2, "2018-08-16 10:00:00", "2018-08-26 09:59:00", 1, 1, 2);
INSERT INTO `campleta_test`.`reservations` (ID, STARTDATE, ENDDATE, CAMPSITE_ID, AREATYPE_ID, AREA_ID) VALUES (3, "2018-08-16 10:00:00", "2018-08-26 09:59:00", 1, 1, 3);
INSERT INTO `campleta_test`.`reservations` (ID, STARTDATE, ENDDATE, CAMPSITE_ID, AREATYPE_ID, AREA_ID) VALUES (4, "2018-09-01 10:00:00", "2018-09-14 09:59:00", 1, 1, 1);
INSERT INTO `campleta_test`.`reservations` (ID, STARTDATE, ENDDATE, CAMPSITE_ID, AREATYPE_ID, AREA_ID) VALUES (5, "2018-08-26 10:00:00", "2018-09-06 09:59:00", 1, 2, 4);
INSERT INTO `campleta_test`.`reservations` (ID, STARTDATE, ENDDATE, CAMPSITE_ID, AREATYPE_ID) VALUES (6, "2018-08-26 10:00:00", "2018-09-06 09:59:00", 1, 2);
INSERT INTO `campleta_test`.`reservations` (ID, STARTDATE, ENDDATE, CAMPSITE_ID, AREATYPE_ID) VALUES (7, "2018-08-26 10:00:00", "2018-09-06 09:59:00", 1, 2);
INSERT INTO `campleta_test`.`reservations` (ID, STARTDATE, ENDDATE, CAMPSITE_ID, AREATYPE_ID, AREA_ID) VALUES (5, "2018-08-26 10:00:00", "2018-09-06 09:59:00", 1, 2, 1);
INSERT INTO `campleta_test`.`reservations` (ID, STARTDATE, ENDDATE, CAMPSITE_ID, AREATYPE_ID, AREA_ID) VALUES (5, "2018-08-26 10:00:00", "2018-09-06 09:59:00", 1, 2, 1);
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`stay` WRITE;
INSERT INTO `campleta_test`.`stay` (ID, STARTDATE, ENDDATE, RESERVATION_ID) VALUES (1, "2018-08-26 10:00:00", "2018-09-06 09:59:00", 5);
INSERT INTO `campleta_test`.`stay` (ID, STARTDATE, ENDDATE, RESERVATION_ID) VALUES (2, "2018-08-30 10:00:00", "2018-09-02 09:59:00", 5);
UNLOCK TABLES;

LOCK TABLES `campleta_test`.`stay_users` WRITE;
INSERT INTO `campleta_test`.`stay_users` (STAY_ID, GUESTS_ID) VALUES (1, 2);
INSERT INTO `campleta_test`.`stay_users` (STAY_ID, GUESTS_ID) VALUES (2, 3);
UNLOCK TABLES;