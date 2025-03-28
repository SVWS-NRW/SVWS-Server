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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle TimestampsSchuelerLeistungsdaten.
 */
public class Tabelle_TimestampsSchuelerLeistungsdaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Leistungsdaten");

	/** Die Definition der Tabellenspalte tsNotenKrz */
	public SchemaTabelleSpalte col_tsNotenKrz = add("tsNotenKrz", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Note.");

	/** Die Definition der Tabellenspalte tsNotenKrzQuartal */
	public SchemaTabelleSpalte col_tsNotenKrzQuartal = add("tsNotenKrzQuartal", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Note.");

	/** Die Definition der Tabellenspalte tsFehlStd */
	public SchemaTabelleSpalte col_tsFehlStd = add("tsFehlStd", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an den Fehlstunden.");

	/** Die Definition der Tabellenspalte tsuFehlStd */
	public SchemaTabelleSpalte col_tsuFehlStd = add("tsuFehlStd", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an den unentschuldigten Fehlstunden.");

	/** Die Definition der Tabellenspalte tsuFehlStd */
	public SchemaTabelleSpalte col_tsLernentw = add("tsLernentw", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an den fachbezogenen Bemerkungen.");

	/** Die Definition der Tabellenspalte tsWarnung */
	public SchemaTabelleSpalte col_tsWarnung = add("tsWarnung", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung, ob gemahnt wird.");


	/** Die Definition des Fremdschlüssels TimestampsSchuelerLeistungsdaten_FK */
	public SchemaTabelleFremdschluessel fk_TimestampsSchuelerLeistungsdaten_FK = addForeignKey(
			"TimestampsSchuelerLeistungsdaten_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_ID, Schema.tab_SchuelerLeistungsdaten.col_ID));


	/** Trigger t_INSERT_TimestampsSchuelerLeistungsdaten */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_TimestampsSchuelerLeistungsdaten = addTrigger(
			"t_INSERT_TimestampsSchuelerLeistungsdaten",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON SchuelerLeistungsdaten FOR EACH ROW
			INSERT INTO TimestampsSchuelerLeistungsdaten(ID, tsNotenKrz, tsNotenKrzQuartal, tsFehlStd, tsuFehlStd, tsLernentw, tsWarnung) VALUES (NEW.ID, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3));
			""", Schema.tab_SchuelerLeistungsdaten, Schema.tab_TimestampsSchuelerLeistungsdaten);

	/** Trigger t_UPDATE_TimestampsSchuelerLeistungsdaten */
	public SchemaTabelleTrigger trigger_MariaDB_UPDATE_TimestampsSchuelerLeistungsdaten = addTrigger(
			"t_UPDATE_TimestampsSchuelerLeistungsdaten",
			DBDriver.MARIA_DB,
			"""
			AFTER UPDATE ON SchuelerLeistungsdaten FOR EACH ROW
			BEGIN
			    IF (OLD.NotenKrz IS NULL AND NEW.NotenKrz IS NOT NULL) OR (OLD.NotenKrz <> NEW.NotenKrz) THEN
			        UPDATE TimestampsSchuelerLeistungsdaten SET tsNotenKrz = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.NotenKrzQuartal IS NULL AND NEW.NotenKrzQuartal IS NOT NULL) OR (OLD.NotenKrzQuartal <> NEW.NotenKrzQuartal) THEN
			        UPDATE TimestampsSchuelerLeistungsdaten SET tsNotenKrzQuartal = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.FehlStd IS NULL AND NEW.FehlStd IS NOT NULL) OR (OLD.FehlStd <> NEW.FehlStd) THEN
			        UPDATE TimestampsSchuelerLeistungsdaten SET tsFehlStd = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.uFehlStd IS NULL AND NEW.uFehlStd IS NOT NULL) OR (OLD.uFehlStd <> NEW.uFehlStd) THEN
			        UPDATE TimestampsSchuelerLeistungsdaten SET tsuFehlStd = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.Lernentw IS NULL AND NEW.Lernentw IS NOT NULL) OR (OLD.Lernentw <> NEW.Lernentw) THEN
			        UPDATE TimestampsSchuelerLeistungsdaten SET tsLernentw = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.Warnung IS NULL AND NEW.Warnung IS NOT NULL) OR (OLD.Warnung <> NEW.Warnung) THEN
			        UPDATE TimestampsSchuelerLeistungsdaten SET tsWarnung = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			END
			""",
			Schema.tab_SchuelerLeistungsdaten, Schema.tab_TimestampsSchuelerLeistungsdaten);

	// TODO Trigger für SQLite


	/**
	 * Erstellt die Schema-Definition für die Tabelle TimestampsSchuelerLeistungsdaten.
	 */
	public Tabelle_TimestampsSchuelerLeistungsdaten() {
		super("TimestampsSchuelerLeistungsdaten", SchemaRevisionen.REV_25);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.timestamps");
		setJavaClassName("DTOTimestampsSchuelerLeistungsdaten");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an ausgewählten Spalten der Datenbanktabelle für Leistungsdaten Änderungen"
				+ " vorgenommen wurden.");
	}

}
