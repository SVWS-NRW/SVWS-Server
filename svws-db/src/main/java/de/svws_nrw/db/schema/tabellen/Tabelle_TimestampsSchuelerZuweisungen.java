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
public class Tabelle_TimestampsSchuelerZuweisungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public final SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("LernabschnittsID der Zuweisung (E G Kurse GE und PS SK)");

	/** Die Definition der Tabellenspalte Fach_ID */
	public final SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("FachID der Zuweisung");

	/** Die Definition der Tabellenspalte tsKursart */
	public final SchemaTabelleSpalte col_tsKursart = add("tsKursart", SchemaDatentypen.DATETIME, false)
			.setDatenlaenge(3)
			.setNotNull()
			.setJavaComment("Der Zeitstempel (UTC) der letzten Änderung an der Kursart-Zuweisung.");


	/** Die Definition des Fremdschlüssels TimestampsSchuelerZuweisungen_FK */
	public final SchemaTabelleFremdschluessel fk_TimestampsSchuelerZuweisungen_FK = addForeignKey(
			"TimestampsSchuelerZuweisungen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abschnitt_ID, Schema.tab_SchuelerZuweisungen.col_Abschnitt_ID),
			new Pair<>(col_Fach_ID, Schema.tab_SchuelerZuweisungen.col_Fach_ID));


	/** Trigger t_INSERT_TimestampsSchuelerZuweisungen */
	public final SchemaTabelleTrigger trigger_MariaDB_INSERT_TimestampsSchuelerZuweisungen = addTrigger(
			"t_INSERT_TimestampsSchuelerZuweisungen",
			DBDriver.MARIA_DB,
			"""
			AFTER INSERT ON SchuelerZuweisungen FOR EACH ROW
			INSERT INTO TimestampsSchuelerZuweisungen(Abschnitt_ID, Fach_ID, tsKursart) VALUES (NEW.Abschnitt_ID, NEW.Fach_ID, UTC_TIMESTAMP(3));
			""", Schema.tab_SchuelerZuweisungen, Schema.tab_TimestampsSchuelerZuweisungen);

	/** Trigger t_UPDATE_TimestampsSchuelerZuweisungen */
	public final SchemaTabelleTrigger trigger_MariaDB_UPDATE_TimestampsSchuelerZuweisungen = addTrigger(
			"t_UPDATE_TimestampsSchuelerZuweisungen",
			DBDriver.MARIA_DB,
			"""
			AFTER UPDATE ON SchuelerZuweisungen FOR EACH ROW
			BEGIN
			    IF NOT (OLD.Kursart <=> NEW.Kursart) THEN
			        UPDATE TimestampsSchuelerZuweisungen SET tsKursart = UTC_TIMESTAMP(3) WHERE Abschnitt_ID = NEW.Abschnitt_ID AND Fach_ID = NEW.Fach_ID;
			    END IF;
			END
			""",
			Schema.tab_SchuelerZuweisungen, Schema.tab_TimestampsSchuelerZuweisungen);


	/**
	 * Erstellt die Schema-Definition für die Tabelle TimestampsSchuelerZuweisungen.
	 */
	public Tabelle_TimestampsSchuelerZuweisungen() {
		super("TimestampsSchuelerZuweisungen", SchemaRevisionen.REV_77);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("svws.timestamps");
		setJavaClassName("DTOTimestampsSchuelerZuweisungen");
		setJavaComment("Diese Tabelle beinhaltet die Zeitstempel, wann an ausgewählten Spalten der Datenbanktabelle für Kursart-Zuweisungen Änderungen"
				+ " vorgenommen wurden.");
	}

}
