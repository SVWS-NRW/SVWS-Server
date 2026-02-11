-- SVWS Schema Initialisierung
-- Erstellt die Basis-Tabellen für SVWS

CREATE DATABASE IF NOT EXISTS svwsdb CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
USE svwsdb;

-- Basis Schema Core Type Versionen Tabelle
CREATE TABLE IF NOT EXISTS Schema_Core_Type_Versionen (
    NameTabelle VARCHAR(255) NOT NULL,
    Name VARCHAR(255) NOT NULL,
    Version BIGINT NOT NULL,
    PRIMARY KEY (NameTabelle, Name)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

-- Initial Core Type Version
INSERT INTO Schema_Core_Type_Versionen (NameTabelle, Name, Version)
VALUES ('Schema_Core_Type_Versionen', 'CoreTypes', 1)
ON DUPLICATE KEY UPDATE Version = Version;

-- Schema Status Tabelle
CREATE TABLE IF NOT EXISTS Schema_Status (
    ID BIGINT NOT NULL,
    IsTainted BOOLEAN NOT NULL DEFAULT FALSE,
    Revision BIGINT DEFAULT 0,
    PRIMARY KEY (ID)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

-- Initial Schema Status
INSERT INTO Schema_Status (ID, IsTainted, Revision)
VALUES (1, FALSE, 0)
ON DUPLICATE KEY UPDATE ID = ID;

-- Schema AutoInkremente Tabelle
CREATE TABLE IF NOT EXISTS Schema_AutoInkremente (
    NameTabelle VARCHAR(200) NOT NULL,
    MaxID BIGINT NOT NULL,
    PRIMARY KEY (NameTabelle)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

-- Schema Revision Tabelle
CREATE TABLE IF NOT EXISTS Schema_Revision (
    Revision BIGINT NOT NULL,
    IsTainted BOOLEAN NOT NULL DEFAULT FALSE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

INSERT INTO Schema_Revision (Revision, IsTainted)
VALUES (0, FALSE)
ON DUPLICATE KEY UPDATE Revision = Revision;

-- Grant permissions
GRANT ALL PRIVILEGES ON svwsdb.* TO 'svwsuser'@'%';
FLUSH PRIVILEGES;
