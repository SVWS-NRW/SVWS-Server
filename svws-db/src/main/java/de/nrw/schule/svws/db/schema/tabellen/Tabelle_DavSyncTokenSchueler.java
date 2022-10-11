package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;
import de.nrw.schule.svws.db.schema.SchemaTabelleTrigger;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle DavSyncTokenSchueler.
 */
public class Tabelle_DavSyncTokenSchueler extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Schülers");

	/** Die Definition der Tabellenspalte SyncToken */
	public SchemaTabelleSpalte col_SyncToken = add("SyncToken", SchemaDatentypen.DATETIME, false)
	    .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an für einen Card-DAV-Eintrag relevanten Schülerdaten.");


	/** Die Definition des Fremdschlüssels DavSyncTokenSchueler_FK */
	public SchemaTabelleFremdschluessel fk_DavSyncTokenSchueler_FK = addForeignKey(
			"DavSyncTokenSchueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_ID, Schema.tab_Schueler.col_ID)
		);

    /** Trigger t_UPDATE_DavSyncTokenSchueler_Schueler */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenSchueler_Schueler = addTrigger(
            "t_UPDATE_DavSyncTokenSchueler_Schueler",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON Schueler FOR EACH ROW
            BEGIN
                DECLARE changed BOOLEAN;
                DECLARE token DATETIME;
                SET changed := 0;
                IF OLD.Name <> NEW.Name OR OLD.Vorname <> NEW.Vorname OR OLD.Status <> NEW.Status THEN
                    SET changed := 1;
                END IF;
                IF changed = TRUE THEN
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = NEW.ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = NEW.ID;
                    END IF;
                END IF;
            END
            """,
            Schema.tab_Schueler, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_UPDATE_DavSyncTokenSchueler_Kurs_Schueler */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenSchueler_Kurs_Schueler = addTrigger(
            "t_UPDATE_DavSyncTokenSchueler_Kurs_Schueler",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON Kurs_Schueler FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                IF OLD.Schueler_ID <> NEW.Schueler_ID THEN
                    SET token := (SELECT DavSyncTokenSchueler.SyncToken FROM DavSyncTokenSchueler WHERE ID = OLD.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (OLD.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = OLD.Schueler_ID;
                    END IF;
                END IF;
                SET token := (SELECT DavSyncTokenSchueler.SyncToken FROM DavSyncTokenSchueler WHERE ID = NEW.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = NEW.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_Kurs_Schueler, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_INSERT_DavSyncTokenSchueler_Kurs_Schueler */
    public SchemaTabelleTrigger trigger_MariaDB_INSERT_DavSyncTokenSchueler_Kurs_Schueler = addTrigger(
            "t_INSERT_DavSyncTokenSchueler_Kurs_Schueler",
            DBDriver.MARIA_DB,
            """
            AFTER INSERT ON Kurs_Schueler FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                SET token := (SELECT DavSyncTokenSchueler.SyncToken FROM DavSyncTokenSchueler WHERE ID = NEW.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = NEW.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_Kurs_Schueler, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_DELETE_DavSyncTokenSchueler_Kurs_Schueler */
    public SchemaTabelleTrigger trigger_MariaDB_DELETE_DavSyncTokenSchueler_Kurs_Schueler = addTrigger(
            "t_DELETE_DavSyncTokenSchueler_Kurs_Schueler",
            DBDriver.MARIA_DB,
            """
            AFTER DELETE ON Kurs_Schueler FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                SET token := (SELECT DavSyncTokenSchueler.SyncToken FROM DavSyncTokenSchueler WHERE ID = OLD.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (OLD.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = OLD.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_Kurs_Schueler, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_UPDATE_DavSyncTokenSchueler_SchuelerErzAdr */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenSchueler_SchuelerErzAdr = addTrigger(
            "t_UPDATE_DavSyncTokenSchueler_SchuelerErzAdr",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON SchuelerErzAdr FOR EACH ROW
            BEGIN
                DECLARE changed BOOLEAN;
                DECLARE token DATETIME;
                SET changed := 0;
                IF OLD.Schueler_ID <> NEW.Schueler_ID THEN
                    SET changed := 1;
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = OLD.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (OLD.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = OLD.Schueler_ID;
                    END IF;
                END IF;
                IF OLD.ErzOrt_ID <> NEW.ErzOrt_ID OR OLD.ErzStrassenname <> NEW.ErzStrassenname 
                        OR OLD.ErzieherArt_ID <> NEW.ErzieherArt_ID
                        OR OLD.ErzHausNr <> NEW.ErzHausNr OR OLD.ErzHausNrZusatz <> NEW.ErzHausNrZusatz
                        OR OLD.ErzEmail <> NEW.ErzEmail OR OLD.ErzEmail2 <> NEW.ErzEmail2
                        OR OLD.Name1 <> NEW.Name1 OR OLD.Name2 <> NEW.Name2
                        OR OLD.Vorname1 <> NEW.Vorname1 OR OLD.Vorname2 <> NEW.Vorname2
                        OR OLD.Erz1ZusatzNachname <> NEW.Erz1ZusatzNachname OR OLD.Erz2ZusatzNachname <> NEW.Erz2ZusatzNachname
                        THEN
                    SET changed := 1;
                END IF;
                IF changed = TRUE THEN
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = NEW.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = NEW.Schueler_ID;
                    END IF;
                END IF;
            END
            """,
            Schema.tab_SchuelerErzAdr, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_INSERT_DavSyncTokenSchueler_SchuelerErzAdr */
    public SchemaTabelleTrigger trigger_MariaDB_INSERT_DavSyncTokenSchueler_SchuelerErzAdr = addTrigger(
            "t_INSERT_DavSyncTokenSchueler_SchuelerErzAdr",
            DBDriver.MARIA_DB,
            """
            AFTER INSERT ON SchuelerErzAdr FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                SET token := (SELECT DavSyncTokenSchueler.SyncToken FROM DavSyncTokenSchueler WHERE ID = NEW.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = NEW.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_SchuelerErzAdr, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_DELETE_DavSyncTokenSchueler_SchuelerErzAdr */
    public SchemaTabelleTrigger trigger_MariaDB_DELETE_DavSyncTokenSchueler_SchuelerErzAdr = addTrigger(
            "t_DELETE_DavSyncTokenSchueler_SchuelerErzAdr",
            DBDriver.MARIA_DB,
            """
            AFTER DELETE ON SchuelerErzAdr FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                SET token := (SELECT DavSyncTokenSchueler.SyncToken FROM DavSyncTokenSchueler WHERE ID = OLD.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (OLD.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = OLD.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_SchuelerErzAdr, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_UPDATE_DavSyncTokenSchueler_SchuelerTelefone */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenSchueler_SchuelerTelefone = addTrigger(
            "t_UPDATE_DavSyncTokenSchueler_SchuelerTelefone",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON SchuelerTelefone FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                IF OLD.Schueler_ID <> NEW.Schueler_ID THEN
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = OLD.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (OLD.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = OLD.Schueler_ID;
                    END IF;
                END IF;
                SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = NEW.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = NEW.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_SchuelerTelefone, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_INSERT_DavSyncTokenSchueler_SchuelerTelefone */
    public SchemaTabelleTrigger trigger_MariaDB_INSERT_DavSyncTokenSchueler_SchuelerTelefone = addTrigger(
            "t_INSERT_DavSyncTokenSchueler_SchuelerTelefone",
            DBDriver.MARIA_DB,
            """
            AFTER INSERT ON SchuelerTelefone FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                SET token := (SELECT DavSyncTokenSchueler.SyncToken FROM DavSyncTokenSchueler WHERE ID = NEW.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = NEW.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_SchuelerTelefone, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_DELETE_DavSyncTokenSchueler_SchuelerTelefone */
    public SchemaTabelleTrigger trigger_MariaDB_DELETE_DavSyncTokenSchueler_SchuelerTelefone = addTrigger(
            "t_DELETE_DavSyncTokenSchueler_SchuelerTelefone",
            DBDriver.MARIA_DB,
            """
            AFTER DELETE ON SchuelerTelefone FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                SET token := (SELECT DavSyncTokenSchueler.SyncToken FROM DavSyncTokenSchueler WHERE ID = OLD.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (OLD.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = OLD.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_SchuelerTelefone, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_UPDATE_DavSyncTokenSchueler_K_Ort */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenSchueler_K_Ort = addTrigger(
            "t_UPDATE_DavSyncTokenSchueler_K_Ort",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON K_Ort FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (
                    SELECT DISTINCT s.Schueler_ID FROM (
                        SELECT ID AS Schueler_ID FROM Schueler WHERE Ort_ID = NEW.ID OR Ort_ID = OLD.ID
                        UNION
                        SELECT Schueler_ID FROM SchuelerErzAdr WHERE ErzOrt_ID = NEW.ID OR ErzOrt_ID = OLD.ID
                    ) s
                ) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = sid.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (sid.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = sid.Schueler_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_K_Ort, Schema.tab_Schueler, Schema.tab_SchuelerErzAdr, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_INSERT_DavSyncTokenSchueler_K_Ort */
    public SchemaTabelleTrigger trigger_MariaDB_INSERT_DavSyncTokenSchueler_K_Ort = addTrigger(
            "t_INSERT_DavSyncTokenSchueler_K_Ort",
            DBDriver.MARIA_DB,
            """
            AFTER INSERT ON K_Ort FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (
                    SELECT DISTINCT s.Schueler_ID FROM (
                        SELECT ID AS Schueler_ID FROM Schueler WHERE Ort_ID = NEW.ID
                        UNION
                        SELECT Schueler_ID FROM SchuelerErzAdr WHERE ErzOrt_ID = NEW.ID
                    ) s
                ) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = sid.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (sid.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = sid.Schueler_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_K_Ort, Schema.tab_Schueler, Schema.tab_SchuelerErzAdr, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_DELETE_DavSyncTokenSchueler_K_Ort */
    public SchemaTabelleTrigger trigger_MariaDB_DELETE_DavSyncTokenSchueler_K_Ort = addTrigger(
            "t_DELETE_DavSyncTokenSchueler_K_Ort",
            DBDriver.MARIA_DB,
            """
            AFTER DELETE ON K_Ort FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (
                    SELECT DISTINCT s.Schueler_ID FROM (
                        SELECT ID AS Schueler_ID FROM Schueler WHERE Ort_ID = OLD.ID
                        UNION
                        SELECT Schueler_ID FROM SchuelerErzAdr WHERE ErzOrt_ID = OLD.ID
                    ) s
                ) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = sid.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (sid.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = sid.Schueler_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_K_Ort, Schema.tab_Schueler, Schema.tab_SchuelerErzAdr, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_UPDATE_DavSyncTokenSchueler_Kurse */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenSchueler_Kurse = addTrigger(
            "t_UPDATE_DavSyncTokenSchueler_Kurse",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON Kurse FOR EACH ROW
            BEGIN
                DECLARE changed BOOLEAN;
                DECLARE token DATETIME;
                SET changed := 0;
                IF OLD.ID <> NEW.ID OR OLD.KurzBez <> NEW.KurzBez OR OLD.Jahrgang_ID <> NEW.Jahrgang_ID 
                        OR OLD.ASDJahrgang <> NEW.ASDJahrgang THEN
                    SET changed := 1;
                END IF;
                IF changed = TRUE THEN
                    FOR sid IN (SELECT DISTINCT Schueler_ID FROM Kurs_Schueler WHERE Kurs_ID = NEW.ID OR Kurs_ID = OLD.ID) DO
                        SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = sid.Schueler_ID);
                        IF token IS NULL THEN
                            INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (sid.Schueler_ID, CURTIME(3));
                        ELSE
                            UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = sid.Schueler_ID;
                        END IF;
                    END FOR;
                END IF;
            END
            """,
            Schema.tab_Kurse, Schema.tab_Kurs_Schueler, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_INSERT_DavSyncTokenSchueler_Kurse */
    public SchemaTabelleTrigger trigger_MariaDB_INSERT_DavSyncTokenSchueler_Kurse = addTrigger(
            "t_INSERT_DavSyncTokenSchueler_Kurse",
            DBDriver.MARIA_DB,
            """
            AFTER INSERT ON Kurse FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (SELECT DISTINCT Schueler_ID FROM Kurs_Schueler WHERE Kurs_ID = NEW.ID) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = sid.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (sid.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = sid.Schueler_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_Kurse, Schema.tab_Kurs_Schueler, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_DELETE_DavSyncTokenSchueler_Kurse */
    public SchemaTabelleTrigger trigger_MariaDB_DELETE_DavSyncTokenSchueler_Kurse = addTrigger(
            "t_DELETE_DavSyncTokenSchueler_Kurse",
            DBDriver.MARIA_DB,
            """
            AFTER DELETE ON Kurse FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (SELECT DISTINCT Schueler_ID FROM Kurs_Schueler WHERE Kurs_ID = OLD.ID) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = sid.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (sid.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = sid.Schueler_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_Kurse, Schema.tab_Kurs_Schueler, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_UPDATE_DavSyncTokenSchueler_Klassen */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenSchueler_Klassen = addTrigger(
            "t_UPDATE_DavSyncTokenSchueler_Klassen",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON Klassen FOR EACH ROW
            BEGIN
                DECLARE changed BOOLEAN;
                DECLARE token DATETIME;
                SET changed := 0;
                IF OLD.ID <> NEW.ID OR OLD.Klasse <> NEW.Klasse OR OLD.ASDKlasse <> NEW.ASDKlasse
                        OR OLD.Jahrgang_ID <> NEW.Jahrgang_ID THEN
                    SET changed := 1;
                END IF;
                IF changed = TRUE THEN
                    FOR sid IN (SELECT DISTINCT Schueler_ID FROM SchuelerLernabschnittsdaten WHERE Klassen_ID = NEW.ID OR Klassen_ID = OLD.ID) DO
                        SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = sid.Schueler_ID);
                        IF token IS NULL THEN
                            INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (sid.Schueler_ID, CURTIME(3));
                        ELSE
                            UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = sid.Schueler_ID;
                        END IF;
                    END FOR;
                END IF;
            END
            """,
            Schema.tab_Klassen, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_INSERT_DavSyncTokenSchueler_Klassen */
    public SchemaTabelleTrigger trigger_MariaDB_INSERT_DavSyncTokenSchueler_Klassen = addTrigger(
            "t_INSERT_DavSyncTokenSchueler_Klassen",
            DBDriver.MARIA_DB,
            """
            AFTER INSERT ON Klassen FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (SELECT DISTINCT Schueler_ID FROM SchuelerLernabschnittsdaten WHERE Klassen_ID = NEW.ID) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = sid.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (sid.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = sid.Schueler_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_Klassen, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_DELETE_DavSyncTokenSchueler_Klassen */
    public SchemaTabelleTrigger trigger_MariaDB_DELETE_DavSyncTokenSchueler_Klassen = addTrigger(
            "t_DELETE_DavSyncTokenSchueler_Klassen",
            DBDriver.MARIA_DB,
            """
            AFTER DELETE ON Klassen FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (SELECT DISTINCT Schueler_ID FROM SchuelerLernabschnittsdaten WHERE Klassen_ID = OLD.ID) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = sid.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (sid.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = sid.Schueler_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_Klassen, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_UPDATE_DavSyncTokenSchueler_SchuelerLernabschnittsdaten */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenSchueler_SchuelerLernabschnittsdaten = addTrigger(
            "t_UPDATE_DavSyncTokenSchueler_SchuelerLernabschnittsdaten",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON SchuelerLernabschnittsdaten FOR EACH ROW
            BEGIN
                DECLARE changed BOOLEAN;
                DECLARE token DATETIME;
                SET changed := 0;
                IF OLD.ID <> NEW.ID OR OLD.Klassen_ID <> NEW.Klassen_ID OR OLD.Schueler_ID <> NEW.Schueler_ID THEN
                    SET changed := 1;
                END IF;
                IF changed = TRUE THEN
                    SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = NEW.Schueler_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.Schueler_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = NEW.Schueler_ID;
                    END IF;
                END IF;
            END
            """,
            Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_INSERT_DavSyncTokenSchueler_SchuelerLernabschnittsdaten */
    public SchemaTabelleTrigger trigger_MariaDB_INSERT_DavSyncTokenSchueler_SchuelerLernabschnittsdaten = addTrigger(
            "t_INSERT_DavSyncTokenSchueler_SchuelerLernabschnittsdaten",
            DBDriver.MARIA_DB,
            """
            AFTER INSERT ON SchuelerLernabschnittsdaten FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = NEW.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = NEW.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_DavSyncTokenSchueler);

    /** Trigger t_DELETE_DavSyncTokenSchueler_SchuelerLernabschnittsdaten */
    public SchemaTabelleTrigger trigger_MariaDB_DELETE_DavSyncTokenSchueler_SchuelerLernabschnittsdaten = addTrigger(
            "t_DELETE_DavSyncTokenSchueler_SchuelerLernabschnittsdaten",
            DBDriver.MARIA_DB,
            """
            AFTER DELETE ON SchuelerLernabschnittsdaten FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                SET token := (SELECT SyncToken FROM DavSyncTokenSchueler WHERE ID = OLD.Schueler_ID);
                IF token IS NULL THEN
                    INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (OLD.Schueler_ID, CURTIME(3));
                ELSE
                    UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME(3) WHERE ID = OLD.Schueler_ID;
                END IF;
            END
            """,
            Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_DavSyncTokenSchueler);

    
    // TODO weitere Trigger für MariaDB 

    // TODO Trigger für SQLite


	/**
	 * Erstellt die Schema-Defintion für die Tabelle DavSyncTokenSchueler.
	 */
	public Tabelle_DavSyncTokenSchueler() {
		super("DavSyncTokenSchueler", SchemaRevisionen.REV_8);
		setMigrate(false);
		setImportExport(false);
		setPKAutoIncrement();
		setJavaSubPackage("svws.dav");
		setJavaClassName("DTODavSyncTokenSchueler");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an den für Card-DAV relevanten Datenbanktabellen "
		        + "für einen Schüler Änderungen vorgenommen wurden. "
		        + "Diese Zeitstempel dienen als Sync-Token für das Protokoll.");
	}

}
