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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EnmLernabschnittsdaten.
 */
public class Tabelle_EnmLernabschnittsdaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Lernabschnittsdaten");

	/** Die Definition der Tabellenspalte tsSumFehlStd */
	public SchemaTabelleSpalte col_tsSumFehlStd = add("tsSumFehlStd", SchemaDatentypen.DATETIME, false)
	    .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an der Summe der Fehlstunden.");

	/** Die Definition der Tabellenspalte tsSumFehlStdU */
	public SchemaTabelleSpalte col_tsSumFehlStdU = add("tsSumFehlStdU", SchemaDatentypen.DATETIME, false)
	    .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an der Summe der unentschuldigten Fehlstunden.");

	/** Die Definition der Tabellenspalte tsZeugnisBem */
	public SchemaTabelleSpalte col_tsZeugnisBem = add("tsZeugnisBem", SchemaDatentypen.DATETIME, false)
	    .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an den Zeugnisbemerkungen.");

	/** Die Definition der Tabellenspalte tsASV */
	public SchemaTabelleSpalte col_tsASV = add("tsASV", SchemaDatentypen.DATETIME, false)
	    .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an den Bemerkungen zum Arbeits- und Sozialverhalten.");
	
	/** Die Definition der Tabellenspalte tsAUE */
	public SchemaTabelleSpalte col_tsAUE = add("tsAUE", SchemaDatentypen.DATETIME, false)
	    .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an den Bemerkungen zum außerunterrichtlichen Engagement.");

	/** Die Definition der Tabellenspalte tsBemerkungVersetzung */
	public SchemaTabelleSpalte col_tsBemerkungVersetzung = add("tsBemerkungVersetzung", SchemaDatentypen.DATETIME, false)
	    .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Änderung an den Bemerkungen zur Versetzung.");


	/** Die Definition des Fremdschlüssels EnmLernabschnittsdaten_FK */
	public SchemaTabelleFremdschluessel fk_EnmLernabschnittsdaten_FK = addForeignKey(
			"EnmLernabschnittsdaten_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_ID, Schema.tab_SchuelerLernabschnittsdaten.col_ID)
		);

	
    /** Trigger t_INSERT_EnmLernabschnittsdaten */
    public SchemaTabelleTrigger trigger_MariaDB_INSERT_EnmLernabschnittsdaten = addTrigger(
    		"t_INSERT_EnmLernabschnittsdaten",
    		DBDriver.MARIA_DB,
    		"""
    		AFTER INSERT ON SchuelerLernabschnittsdaten FOR EACH ROW
    		INSERT INTO EnmLernabschnittsdaten(ID, tsSumFehlStd, tsSumFehlStdU, tsZeugnisBem, tsASV, tsAUE, tsBemerkungVersetzung) VALUES (NEW.ID, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3));
    		""", Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EnmLernabschnittsdaten);


    /** Trigger t_UPDATE_EnmLernabschnittsdaten */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_EnmLernabschnittsdaten = addTrigger(
            "t_UPDATE_EnmLernabschnittsdaten",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON SchuelerLernabschnittsdaten FOR EACH ROW
            BEGIN
                IF (OLD.SumFehlStd IS NULL AND NEW.SumFehlStd IS NOT NULL) OR (OLD.SumFehlStd <> NEW.SumFehlStd) THEN
                    UPDATE EnmLernabschnittsdaten SET tsSumFehlStd = CURTIME(3) WHERE ID = NEW.ID;
                END IF;
                IF (OLD.SumFehlStdU IS NULL AND NEW.SumFehlStdU IS NOT NULL) OR (OLD.SumFehlStdU <> NEW.SumFehlStdU) THEN
                    UPDATE EnmLernabschnittsdaten SET tsSumFehlStdU = CURTIME(3) WHERE ID = NEW.ID;
                END IF;
                IF (OLD.ZeugnisBem IS NULL AND NEW.ZeugnisBem IS NOT NULL) OR (OLD.ZeugnisBem <> NEW.ZeugnisBem) THEN
                    UPDATE EnmLernabschnittsdaten SET tsZeugnisBem = CURTIME(3) WHERE ID = NEW.ID;
                END IF;
            END
            """,
            Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EnmLernabschnittsdaten);

    /** Trigger t_UPDATE_EnmLernabschnittsdaten_Bemerkungen */
    public SchemaTabelleTrigger trigger_MariaDB_UPDATE_EnmLernabschnittsdaten_Bemerkungen = addTrigger(
            "t_UPDATE_EnmLernabschnittsdaten_Bemerkungen",
            DBDriver.MARIA_DB,
            """
            AFTER UPDATE ON SchuelerLD_PSFachBem FOR EACH ROW
            BEGIN
                IF (OLD.ASV IS NULL AND NEW.ASV IS NOT NULL) OR (OLD.ASV <> NEW.ASV) THEN
                    UPDATE EnmLernabschnittsdaten SET tsASV = CURTIME(3) WHERE ID = NEW.Abschnitt_ID;
                END IF;
                IF (OLD.AUE IS NULL AND NEW.AUE IS NOT NULL) OR (OLD.AUE <> NEW.AUE) THEN
                    UPDATE EnmLernabschnittsdaten SET tsAUE = CURTIME(3) WHERE ID = NEW.Abschnitt_ID;
                END IF;
                IF (OLD.BemerkungVersetzung IS NULL AND NEW.BemerkungVersetzung IS NOT NULL) OR (OLD.BemerkungVersetzung <> NEW.BemerkungVersetzung) THEN
                    UPDATE EnmLernabschnittsdaten SET tsBemerkungVersetzung = CURTIME(3) WHERE ID = NEW.Abschnitt_ID;
                END IF;
            END
            """,
            Schema.tab_SchuelerLD_PSFachBem, Schema.tab_EnmLernabschnittsdaten);


    // TODO Trigger für SQLite


	/**
	 * Erstellt die Schema-Definition für die Tabelle EnmLernabschnittsdaten.
	 */
	public Tabelle_EnmLernabschnittsdaten() {
		super("EnmLernabschnittsdaten", SchemaRevisionen.REV_6);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("svws.enm");
		setJavaClassName("DTOEnmLernabschnittsdaten");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an den für das ENM relevanten Spalten "
		        + "der Datenbanktabelle für Lernabschnittsdaten Änderungen vorgenommen wurden.");
	}

}
