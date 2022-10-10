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
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an für einen Card-DAV-Eintrag relevanten Schülerdaten.");


	/** Die Definition des Fremdschlüssels DavSyncTokenSchueler_FK */
	public SchemaTabelleFremdschluessel fk_DavSyncTokenSchueler_FK = addForeignKey(
			"DavSyncTokenSchueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_ID, Schema.tab_Schueler.col_ID)
		);

    // TODO Trigger für weitere Spalten ergänzen... 
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
                IF OLD.Name <> NEW.Name OR OLD.Vorname <> NEW.Vorname THEN
                    SET changed := 1;
                END IF;
                IF changed = TRUE THEN
                    SET token := (SELECT DavSyncTokenSchueler.SyncToken FROM DavSyncTokenSchueler);
                    IF token IS NULL THEN
                        INSERT INTO DavSyncTokenSchueler(ID, SyncToken) VALUES (NEW.ID, CURTIME());
                    ELSE
                        UPDATE DavSyncTokenSchueler SET SyncToken = CURTIME() WHERE ID = NEW.ID;
                    END IF;
                END IF;
            END
            """,
            Schema.tab_Schueler, Schema.tab_DavSyncTokenSchueler);

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
