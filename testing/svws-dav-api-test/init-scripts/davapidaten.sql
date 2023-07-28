use gymabi;
UPDATE Credentials SET PasswordHash='$2a$10$jecQsUFy4xaUBUQIpphKPeHrKHbZIg4N2BLbRgKfmDtu1APi4gRVG' WHERE ID IN (1, 4, 5, 6);-- hash für password = 'password'
INSERT INTO BenutzerKompetenzen VALUES (5, 201);
INSERT INTO BenutzerKompetenzen VALUES (5, 202);
INSERT INTO BenutzerKompetenzen VALUES (5, 301);
INSERT INTO BenutzerKompetenzen VALUES (5, 302);
INSERT INTO BenutzerKompetenzen VALUES (5, 303);
INSERT INTO BenutzerKompetenzen VALUES (6, 201);
INSERT INTO BenutzerKompetenzen VALUES (6, 202);
INSERT INTO BenutzerKompetenzen VALUES (6, 301);
INSERT INTO BenutzerKompetenzen VALUES (6, 302);
INSERT INTO BenutzerKompetenzen VALUES (6, 303);
INSERT INTO DavRessourceCollections(benutzer_Id , typ, Anzeigename, Beschreibung, SyncToken) VALUES (5, 1, "Gemeinsamer Kalender", "zum Testen", NOW());
INSERT INTO DavRessourceCollectionsACL (Benutzer_ID, ressourcecollection_id, berechtigungen) VALUES (5, (SELECT ID FROM DavRessourceCollections WHERE Anzeigename LIKE 'Gemeinsamer Kalender'), 'rw');
INSERT INTO DavRessourceCollectionsACL (Benutzer_ID, ressourcecollection_id, berechtigungen) VALUES (6, (SELECT ID FROM DavRessourceCollections WHERE Anzeigename LIKE 'Gemeinsamer Kalender'), 'r-');