package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleTrigger;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle TimestampsSchuelerLernabschnittsdaten.
 */
public class Tabelle_TimestampsSchuelerLernabschnittsdaten extends SchemaTabelle {

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


	/** Die Definition des Fremdschlüssels TimestampsSchuelerLernabschnittsdaten_FK */
	public SchemaTabelleFremdschluessel fk_TimestampsSchuelerLernabschnittsdaten_FK = addForeignKey(
			"TimestampsSchuelerLernabschnittsdaten_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_ID, Schema.tab_SchuelerLernabschnittsdaten.col_ID));


	/** Trigger t_INSERT_TimestampsSchuelerLernabschnittsdaten */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_TimestampsSchuelerLernabschnittsdaten = addTrigger(
			"t_INSERT_TimestampsSchuelerLernabschnittsdaten",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON SchuelerLernabschnittsdaten FOR EACH ROW
			INSERT INTO TimestampsSchuelerLernabschnittsdaten(ID, tsSumFehlStd, tsSumFehlStdU, tsZeugnisBem, tsASV, tsAUE, tsBemerkungVersetzung) VALUES (NEW.ID, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3));
			""", Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_TimestampsSchuelerLernabschnittsdaten);


	/** Trigger t_UPDATE_TimestampsSchuelerLernabschnittsdaten */
	public SchemaTabelleTrigger trigger_MariaDB_UPDATE_TimestampsSchuelerLernabschnittsdaten = addTrigger(
			"t_UPDATE_TimestampsSchuelerLernabschnittsdaten",
			DBDriver.MARIA_DB,
			"""
            AFTER UPDATE ON SchuelerLernabschnittsdaten FOR EACH ROW
            BEGIN
                IF (OLD.SumFehlStd IS NULL AND NEW.SumFehlStd IS NOT NULL) OR (OLD.SumFehlStd <> NEW.SumFehlStd) THEN
                    UPDATE TimestampsSchuelerLernabschnittsdaten SET tsSumFehlStd = CURTIME(3) WHERE ID = NEW.ID;
                END IF;
                IF (OLD.SumFehlStdU IS NULL AND NEW.SumFehlStdU IS NOT NULL) OR (OLD.SumFehlStdU <> NEW.SumFehlStdU) THEN
                    UPDATE TimestampsSchuelerLernabschnittsdaten SET tsSumFehlStdU = CURTIME(3) WHERE ID = NEW.ID;
                END IF;
                IF (OLD.ZeugnisBem IS NULL AND NEW.ZeugnisBem IS NOT NULL) OR (OLD.ZeugnisBem <> NEW.ZeugnisBem) THEN
                    UPDATE TimestampsSchuelerLernabschnittsdaten SET tsZeugnisBem = CURTIME(3) WHERE ID = NEW.ID;
                END IF;
            END
            """,
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_TimestampsSchuelerLernabschnittsdaten);

	/** Trigger t_UPDATE_TimestampsSchuelerLernabschnittsdaten_Bemerkungen */
	public SchemaTabelleTrigger trigger_MariaDB_UPDATE_TimestampsSchuelerLernabschnittsdaten_Bemerkungen = addTrigger(
			"t_UPDATE_TimestampsSchuelerLernabschnittsdaten_Bemerkungen",
			DBDriver.MARIA_DB,
			"""
            AFTER UPDATE ON SchuelerLD_PSFachBem FOR EACH ROW
            BEGIN
                IF (OLD.ASV IS NULL AND NEW.ASV IS NOT NULL) OR (OLD.ASV <> NEW.ASV) THEN
                    UPDATE TimestampsSchuelerLernabschnittsdaten SET tsASV = CURTIME(3) WHERE ID = NEW.Abschnitt_ID;
                END IF;
                IF (OLD.AUE IS NULL AND NEW.AUE IS NOT NULL) OR (OLD.AUE <> NEW.AUE) THEN
                    UPDATE TimestampsSchuelerLernabschnittsdaten SET tsAUE = CURTIME(3) WHERE ID = NEW.Abschnitt_ID;
                END IF;
                IF (OLD.BemerkungVersetzung IS NULL AND NEW.BemerkungVersetzung IS NOT NULL) OR (OLD.BemerkungVersetzung <> NEW.BemerkungVersetzung) THEN
                    UPDATE TimestampsSchuelerLernabschnittsdaten SET tsBemerkungVersetzung = CURTIME(3) WHERE ID = NEW.Abschnitt_ID;
                END IF;
            END
            """,
			Schema.tab_SchuelerLD_PSFachBem, Schema.tab_TimestampsSchuelerLernabschnittsdaten);


	/**
	 * Erstellt die Schema-Definition für die Tabelle TimestampsSchuelerLernabschnittsdaten.
	 */
	public Tabelle_TimestampsSchuelerLernabschnittsdaten() {
		super("TimestampsSchuelerLernabschnittsdaten", SchemaRevisionen.REV_25);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.timestamps");
		setJavaClassName("DTOTimestampsSchuelerLernabschnittsdaten");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an ausgewählten Spalten der Datenbanktabelle für Lernabschnittsdaten Änderungen"
				+ " vorgenommen wurden.");
	}

}
