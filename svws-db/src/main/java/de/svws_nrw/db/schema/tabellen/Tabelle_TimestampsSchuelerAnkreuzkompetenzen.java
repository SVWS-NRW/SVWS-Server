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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle TimestampsSchuelerAnkreuzkompetenzen.
 */
public class Tabelle_TimestampsSchuelerAnkreuzkompetenzen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Schüler-Ankreuzkompetenz");

	/** Die Definition der Tabellenspalte tsStufe */
	public SchemaTabelleSpalte col_tsStufe = add("tsStufe", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Stufe, welche der Ankreuzkompetenz zugeordnet ist.");


	/** Die Definition des Fremdschlüssels TimestampsSchuelerAnkreuzkompetenzen_FK */
	public SchemaTabelleFremdschluessel fk_TimestampsSchuelerAnkreuzkompetenzen_FK = addForeignKey(
			"TimestampsSchuelerAnkreuzkompetenzen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_ID, Schema.tab_SchuelerAnkreuzfloskeln.col_ID));


	/** Trigger t_INSERT_TimestampsSchuelerAnkreuzkompetenzen */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_TimestampsSchuelerAnkreuzkompetenzen = addTrigger(
			"t_INSERT_TimestampsSchuelerAnkreuzkompetenzen",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON SchuelerAnkreuzfloskeln FOR EACH ROW
			INSERT INTO TimestampsSchuelerAnkreuzkompetenzen(ID, tsStufe) VALUES (NEW.ID, CURTIME(3));
			""", Schema.tab_SchuelerAnkreuzfloskeln, Schema.tab_TimestampsSchuelerAnkreuzkompetenzen);

	/** Trigger t_UPDATE_TimestampsSchuelerAnkreuzkompetenzen */
	public SchemaTabelleTrigger trigger_MariaDB_UPDATE_TimestampsSchuelerAnkreuzkompetenzen = addTrigger(
			"t_UPDATE_TimestampsSchuelerAnkreuzkompetenzen",
			DBDriver.MARIA_DB,
			"""
			AFTER UPDATE ON SchuelerAnkreuzfloskeln FOR EACH ROW
			BEGIN
			    IF (OLD.Stufe1 IS NULL AND NEW.Stufe1 IS NOT NULL) OR (OLD.Stufe1 <> NEW.Stufe1) OR
			       (OLD.Stufe2 IS NULL AND NEW.Stufe2 IS NOT NULL) OR (OLD.Stufe2 <> NEW.Stufe2) OR
			       (OLD.Stufe3 IS NULL AND NEW.Stufe3 IS NOT NULL) OR (OLD.Stufe3 <> NEW.Stufe3) OR
			       (OLD.Stufe4 IS NULL AND NEW.Stufe4 IS NOT NULL) OR (OLD.Stufe4 <> NEW.Stufe4) OR
			       (OLD.Stufe5 IS NULL AND NEW.Stufe5 IS NOT NULL) OR (OLD.Stufe5 <> NEW.Stufe5) THEN
			        UPDATE TimestampsSchuelerAnkreuzkompetenzen SET tsStufe = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			END
			""",
			Schema.tab_SchuelerAnkreuzfloskeln, Schema.tab_TimestampsSchuelerAnkreuzkompetenzen);


	/**
	 * Erstellt die Schema-Definition für die Tabelle TimestampsSchuelerAnkreuzkompetenzen.
	 */
	public Tabelle_TimestampsSchuelerAnkreuzkompetenzen() {
		super("TimestampsSchuelerAnkreuzkompetenzen", SchemaRevisionen.REV_25);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.timestamps");
		setJavaClassName("DTOTimestampsSchuelerAnkreuzkompetenzen");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an ausgewählten Spalten der Datenbanktabelle für Ankreuzkompetenzen bei Schülern"
				+ " Änderungen vorgenommen wurden.");
	}

}
