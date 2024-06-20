package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EnmTeilleistungen.
 */
public class Tabelle_EnmTeilleistungen extends SchemaTabelle {

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


	/** Die Definition des Fremdschlüssels EnmTeilleistungen_FK */
	public SchemaTabelleFremdschluessel fk_EnmTeilleistungen_FK = addForeignKey(
			"EnmTeilleistungen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_ID, Schema.tab_SchuelerEinzelleistungen.col_ID));


	/** Trigger t_INSERT_EnmTeilleistungen */
	public SchemaTabelleTrigger trigger_MariaDB_INSERT_EnmTeilleistungen = addTrigger(
			"t_INSERT_EnmTeilleistungen",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON SchuelerEinzelleistungen FOR EACH ROW
			INSERT INTO EnmTeilleistungen(ID, tsDatum, tsLehrer_ID, tsArt_ID, tsBemerkung, tsNotenKrz) VALUES (NEW.ID, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3));
			""", Schema.tab_SchuelerEinzelleistungen, Schema.tab_EnmTeilleistungen);

	/** Trigger t_UPDATE_EnmTeilleistungen */
	public SchemaTabelleTrigger trigger_MariaDB_UPDATE_EnmTeilleistungen = addTrigger(
			"t_UPDATE_EnmTeilleistungen",
			DBDriver.MARIA_DB,
			"""
			AFTER UPDATE ON SchuelerEinzelleistungen FOR EACH ROW
			BEGIN
			    IF (OLD.Datum IS NULL AND NEW.Datum IS NOT NULL) OR (OLD.Datum <> NEW.Datum) THEN
			        UPDATE EnmTeilleistungen SET tsDatum = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.Lehrer_ID IS NULL AND NEW.Lehrer_ID IS NOT NULL) OR (OLD.Lehrer_ID <> NEW.Lehrer_ID) THEN
			        UPDATE EnmTeilleistungen SET tsLehrer_ID = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.Art_ID IS NULL AND NEW.Art_ID IS NOT NULL) OR (OLD.Art_ID <> NEW.Art_ID) THEN
			        UPDATE EnmTeilleistungen SET tsArt_ID = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.Bemerkung IS NULL AND NEW.Bemerkung IS NOT NULL) OR (OLD.Bemerkung <> NEW.Bemerkung) THEN
			        UPDATE EnmTeilleistungen SET tsBemerkung = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			    IF (OLD.NotenKrz IS NULL AND NEW.NotenKrz IS NOT NULL) OR (OLD.NotenKrz <> NEW.NotenKrz) THEN
			        UPDATE EnmTeilleistungen SET tsNotenKrz = CURTIME(3) WHERE ID = NEW.ID;
			    END IF;
			END
			""",
			Schema.tab_SchuelerEinzelleistungen, Schema.tab_EnmTeilleistungen);

	// TODO Trigger für SQLite


	/**
	 * Erstellt die Schema-Definition für die Tabelle EnmTeilleistungen.
	 */
	public Tabelle_EnmTeilleistungen() {
		super("EnmTeilleistungen", SchemaRevisionen.REV_14);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.enm");
		setJavaClassName("DTOEnmTeilleistungen");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an den für das ENM relevanten Spalten "
				+ "der Datenbanktabelle für Teilleistungen Änderungen vorgenommen wurden.");
	}

}
