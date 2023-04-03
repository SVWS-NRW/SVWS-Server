package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Aufsichten.
 */
public class Tabelle_Gost_Klausuren_Entfall extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Termin_ID */
	public SchemaTabelleSpalte col_Termin_ID = add("Termin_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Klausurtermins");

	/** Die Definition der Tabellenspalte Kurs_ID */
	public SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Kurses");

	/** Die Definition der Tabellenspalte Zeitraster_ID */
	public SchemaTabelleSpalte col_Zeitraster_ID = add("Zeitraster_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Zeitrasters");

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Entfall_Termin_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Entfall_Termin_ID_FK = addForeignKey(
			"Gost_Klausuren_Entfall_Termin_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Termin_ID, Schema.tab_Gost_Klausuren_Termine.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Entfall_Kurs_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Entfall_Kurs_ID_FK = addForeignKey(
			"Gost_Klausuren_Entfall_Kurs_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Kurs_ID, Schema.tab_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Entfall_Zeitraster_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Entfall_Zeitraster_ID_FK = addForeignKey(
			"Gost_Klausuren_Entfall_Zeitraster_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Zeitraster_ID, Schema.tab_Stundenplan_Zeitraster.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Aufsichten.
	 */
	public Tabelle_Gost_Klausuren_Entfall() {
		super("Gost_Klausuren_Entfall", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenEntfall");
		setJavaComment("Tabelle für die Definition von Entfall für Klausuren");
	}

}
