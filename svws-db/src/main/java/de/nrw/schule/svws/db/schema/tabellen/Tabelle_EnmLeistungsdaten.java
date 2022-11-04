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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EnmLeistungsdaten.
 */
public class Tabelle_EnmLeistungsdaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Leistungsdaten");

	/** Die Definition der Tabellenspalte tsNote */
	public SchemaTabelleSpalte col_tsNotenKrz = add("tsNotenKrz", SchemaDatentypen.DATETIME, false)
	    .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an der Note.");


	/** Die Definition des Fremdschlüssels EnmLeistungsdaten_FK */
	public SchemaTabelleFremdschluessel fk_EnmLeistungsdaten_FK = addForeignKey(
			"EnmLeistungsdaten_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_ID, Schema.tab_SchuelerLeistungsdaten.col_ID)
		);

    /** Trigger t_UPDATE_EnmLeistungsdaten */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_EnmLeistungsdaten = addTrigger(
            "t_UPDATE_EnmLeistungsdaten",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON SchuelerLeistungsdaten FOR EACH ROW
            BEGIN
                DECLARE token DATETIME;
                IF OLD.NotenKrz <> NEW.NotenKrz THEN
                    SET token := (SELECT tsNotenKrz FROM EnmLeistungsdaten WHERE ID = NEW.ID);
                    IF token IS NULL THEN
                        INSERT INTO EnmLeistungsdaten(ID, tsNotenKrz) VALUES (NEW.ID, CURTIME(3));
                    ELSE
                        UPDATE EnmLeistungsdaten SET tsNotenKrz = CURTIME(3) WHERE ID = NEW.ID;
                    END IF;
                END IF;
            END
            """,
            Schema.tab_SchuelerLeistungsdaten, Schema.tab_EnmLeistungsdaten);

    // TODO Trigger für SQLite


	/**
	 * Erstellt die Schema-Definition für die Tabelle EnmLeistungsdaten.
	 */
	public Tabelle_EnmLeistungsdaten() {
		super("EnmLeistungsdaten", SchemaRevisionen.REV_6);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("svws.enm");
		setJavaClassName("DTOEnmLeistungsdaten");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an den für das ENM relevanten Spalten "
		        + "der Datenbanktabelle für Leistungsdaten Änderungen vorgenommen wurden.");
	}

}
