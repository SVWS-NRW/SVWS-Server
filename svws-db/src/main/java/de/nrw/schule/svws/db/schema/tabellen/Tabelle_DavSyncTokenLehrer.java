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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle DavSyncTokenLehrer.
 */
public class Tabelle_DavSyncTokenLehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Lehrers");

	/** Die Definition der Tabellenspalte SyncToken */
	public SchemaTabelleSpalte col_SyncToken = add("SyncToken", SchemaDatentypen.DATETIME, false)
        .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an für einen Card-DAV-Eintrag relevanten Lehrerdaten.");


	/** Die Definition des Fremdschlüssels DavSyncTokenLehrer_FK */
	public SchemaTabelleFremdschluessel fk_DavSyncTokenLehrer_FK = addForeignKey(
			"DavSyncTokenLehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_ID, Schema.tab_K_Lehrer.col_ID)
		);

    /** Trigger t_UPDATE_DavSyncTokenLehrer_Lehrer */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenLehrer_Lehrer = addTrigger(
            "t_UPDATE_DavSyncTokenLehrer_Lehrer",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON K_Lehrer FOR EACH ROW
            BEGIN
                DECLARE changed BOOLEAN;
                DECLARE token DATETIME;
                SET changed := 0;
                IF OLD.Nachname <> NEW.Nachname OR OLD.Vorname <> NEW.Vorname OR OLD.Strassenname <> NEW.Strassenname 
                        OR OLD.HausNr <> NEW.HausNr OR OLD.HausNrZusatz <> NEW.HausNrZusatz
                        OR OLD.Ort_ID <> NEW.Ort_ID OR OLD.Ortsteil_ID <> NEW.Ortsteil_ID
                        OR OLD.Tel <> NEW.Tel OR OLD.Handy <> NEW.Handy
                        OR OLD.Email <> NEW.Email OR OLD.EmailDienstlich <> NEW.EmailDienstlich
                        OR OLD.Geschlecht <> NEW.Geschlecht THEN
                    SET changed := 1;
                END IF;
                IF changed = TRUE THEN
                    SET token := (SELECT SyncToken FROM DavSyncTokenLehrer WHERE ID = NEW.ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenLehrer(ID, SyncToken) VALUES (NEW.ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenLehrer SET SyncToken = CURTIME(3) WHERE ID = NEW.ID;
                    END IF;
                END IF;
            END
            """,
            Schema.tab_K_Lehrer, Schema.tab_DavSyncTokenLehrer);

    /** Trigger t_UPDATE_DavSyncTokenLehrer_K_Ort */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_DavSyncTokenLehrer_K_Ort = addTrigger(
            "t_UPDATE_DavSyncTokenLehrer_K_Ort",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON K_Ort FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (SELECT ID AS Lehrer_ID FROM K_Lehrer WHERE Ort_ID = NEW.ID OR Ort_ID = OLD.ID) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenLehrer WHERE ID = sid.Lehrer_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenLehrer(ID, SyncToken) VALUES (sid.Lehrer_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenLehrer SET SyncToken = CURTIME(3) WHERE ID = sid.Lehrer_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_K_Ort, Schema.tab_K_Lehrer, Schema.tab_DavSyncTokenLehrer);

    /** Trigger t_INSERT_DavSyncTokenLehrer_K_Ort */
    public SchemaTabelleTrigger trigger_MariaDB_INSERT_DavSyncTokenLehrer_K_Ort = addTrigger(
            "t_INSERT_DavSyncTokenLehrer_K_Ort",
            DBDriver.MARIA_DB,
            """
            AFTER INSERT ON K_Ort FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (SELECT ID AS Lehrer_ID FROM K_Lehrer WHERE Ort_ID = NEW.ID) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenLehrer WHERE ID = sid.Lehrer_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenLehrer(ID, SyncToken) VALUES (sid.Lehrer_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenLehrer SET SyncToken = CURTIME(3) WHERE ID = sid.Lehrer_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_K_Ort, Schema.tab_K_Lehrer, Schema.tab_DavSyncTokenLehrer);

    /** Trigger t_DELETE_DavSyncTokenLehrer_K_Ort */
    public SchemaTabelleTrigger trigger_MariaDB_DELETE_DavSyncTokenLehrer_K_Ort = addTrigger(
            "t_DELETE_DavSyncTokenLehrer_K_Ort",
            DBDriver.MARIA_DB,
            """
            AFTER DELETE ON K_Ort FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                FOR sid IN (SELECT ID AS Lehrer_ID FROM K_Lehrer WHERE Ort_ID = OLD.ID) DO
                    SET token := (SELECT SyncToken FROM DavSyncTokenLehrer WHERE ID = sid.Lehrer_ID);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenLehrer(ID, SyncToken) VALUES (sid.Lehrer_ID, CURTIME(3));
                    ELSE
                        UPDATE DavSyncTokenLehrer SET SyncToken = CURTIME(3) WHERE ID = sid.Lehrer_ID;
                    END IF;
                END FOR;
            END
            """,
            Schema.tab_K_Ort, Schema.tab_K_Lehrer, Schema.tab_DavSyncTokenLehrer);

    // TODO weitere Trigger für MariaDB 

    // TODO Trigger für SQLite


	/**
	 * Erstellt die Schema-Defintion für die Tabelle DavSyncTokenLehrer.
	 */
	public Tabelle_DavSyncTokenLehrer() {
		super("DavSyncTokenLehrer", SchemaRevisionen.REV_8);
		setMigrate(false);
		setImportExport(false);
		setPKAutoIncrement();
		setJavaSubPackage("svws.dav");
		setJavaClassName("DTODavSyncTokenLehrer");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an den für Card-DAV relevanten Datenbanktabellen "
		        + "für einen Lehrer Änderungen vorgenommen wurden. "
		        + "Diese Zeitstempel dienen als Sync-Token für das Protokoll.");
	}

}
