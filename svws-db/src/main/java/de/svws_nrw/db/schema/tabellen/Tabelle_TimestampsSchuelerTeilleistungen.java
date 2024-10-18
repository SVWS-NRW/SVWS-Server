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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle TimestampsSchuelerTeilleistungen.
 */
public class Tabelle_TimestampsSchuelerTeilleistungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Teilleistungen");

	/** Die Definition der Tabellenspalte tsDatum */
	public SchemaTabelleSpalte col_tsDatum = add("tsDatum", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an dem Datum der Teilleistung, wann diese erbracht wurde.");

	/** Die Definition der Tabellenspalte tsLehrer_ID */
	public SchemaTabelleSpalte col_tsLehrer_ID = add("tsLehrer_ID", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Lehrer-ID.");

	/** Die Definition der Tabellenspalte tsArt_ID */
	public SchemaTabelleSpalte col_tsArt_ID = add("tsArt_ID", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung der Teilleistungsart.");

	/** Die Definition der Tabellenspalte tsBemerkung */
	public SchemaTabelleSpalte col_tsBemerkung = add("tsBemerkung", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Bemerkung.");

	/** Die Definition der Tabellenspalte tsNotenKrz */
	public SchemaTabelleSpalte col_tsNotenKrz = add("tsNotenKrz", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel der letzten Änderung an der Note.");


	/** Die Definition des Fremdschlüssels TimestampsSchuelerTeilleistungen_FK */
	public SchemaTabelleFremdschluessel fk_TimestampsSchuelerTeilleistungen_FK = addForeignKey(
			"TimestampsSchuelerTeilleistungen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_ID, Schema.tab_SchuelerEinzelleistungen.col_ID));


	/** Trigger t_INSERT_TimestampsSchuelerTeilleistungen */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_TimestampsSchuelerTeilleistungen = addTrigger(
			"t_INSERT_TimestampsSchuelerTeilleistungen",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON SchuelerEinzelleistungen FOR EACH ROW
			INSERT INTO TimestampsSchuelerTeilleistungen(ID, tsDatum, tsLehrer_ID, tsArt_ID, tsBemerkung, tsNotenKrz) VALUES (NEW.ID, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3));
			""", Schema.tab_SchuelerEinzelleistungen, Schema.tab_TimestampsSchuelerTeilleistungen);

	/** Trigger t_UPDATE_TimestampsSchuelerTeilleistungen */
	public SchemaTabelleTrigger trigger_MariaDB_UPDATE_TimestampsSchuelerTeilleistungen = addTrigger(
			"t_UPDATE_TimestampsSchuelerTeilleistungen",
			DBDriver.MARIA_DB,
			"""
			AFTER UPDATE ON SchuelerEinzelleistungen FOR EACH ROW
			BEGIN
			    IF (OLD.Datum IS NULL AND NEW.Datum IS NOT NULL) OR (OLD.Datum <> NEW.Datum) THEN
			        UPDATE TimestampsSchuelerTeilleistungen SET tsDatum = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.Lehrer_ID IS NULL AND NEW.Lehrer_ID IS NOT NULL) OR (OLD.Lehrer_ID <> NEW.Lehrer_ID) THEN
			        UPDATE TimestampsSchuelerTeilleistungen SET tsLehrer_ID = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.Art_ID IS NULL AND NEW.Art_ID IS NOT NULL) OR (OLD.Art_ID <> NEW.Art_ID) THEN
			        UPDATE TimestampsSchuelerTeilleistungen SET tsArt_ID = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.Bemerkung IS NULL AND NEW.Bemerkung IS NOT NULL) OR (OLD.Bemerkung <> NEW.Bemerkung) THEN
			        UPDATE TimestampsSchuelerTeilleistungen SET tsBemerkung = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.NotenKrz IS NULL AND NEW.NotenKrz IS NOT NULL) OR (OLD.NotenKrz <> NEW.NotenKrz) THEN
			        UPDATE TimestampsSchuelerTeilleistungen SET tsNotenKrz = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			END
			""",
			Schema.tab_SchuelerEinzelleistungen, Schema.tab_TimestampsSchuelerTeilleistungen);


	/**
	 * Erstellt die Schema-Definition für die Tabelle TimestampsSchuelerTeilleistungen.
	 */
	public Tabelle_TimestampsSchuelerTeilleistungen() {
		super("TimestampsSchuelerTeilleistungen", SchemaRevisionen.REV_25);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.timestamps");
		setJavaClassName("DTOTimestampsSchuelerTeilleistungen");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an ausgewählten Spalten der Datenbanktabelle für Teilleistungen Änderungen"
				+ " vorgenommen wurden.");
	}

}
