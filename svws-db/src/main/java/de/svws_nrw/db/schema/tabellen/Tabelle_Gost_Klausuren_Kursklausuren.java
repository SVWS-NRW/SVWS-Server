package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Kursklausuren.
 */
public class Tabelle_Gost_Klausuren_Kursklausuren extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Kursklausur (generiert)");

	/** Die Definition der Tabellenspalte Vorgabe_ID */
	public SchemaTabelleSpalte col_Vorgabe_ID = add("Vorgabe_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Klausurvorgaben");

	/** Die Definition der Tabellenspalte Kurs_ID */
	public SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Kurs_ID der Klausur");

	/** Die Definition der Tabellenspalte Termin_ID */
	public SchemaTabelleSpalte col_Termin_ID = add("Termin_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Klausurtermins");

//	/** Die Definition der Tabellenspalte Raum_ID */
//	public SchemaTabelleSpalte col_Raum_ID = add("Raum_ID", SchemaDatentypen.BIGINT, false)
//		.setJavaComment("ID des Klausurraums");

	/** Die Definition der Tabellenspalte Startzeit */
	public SchemaTabelleSpalte col_Startzeit = add("Startzeit", SchemaDatentypen.TIME, false)
		.setConverter(UhrzeitConverter.class)
		.setJavaComment("Startzeit der Klausur, wenn abweichend von Startzeit der Klausur-Schiene");



	/** Die Definition des Fremdschlüssels Gost_Klausuren_Kursklausuren_Vorgabe_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Kursklausuren_Vorgabe_ID_FK = addForeignKey(
			"Gost_Klausuren_Kursklausuren_Vorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Vorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Kursklausuren_Kurs_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Kursklausuren_Kurs_ID_FK = addForeignKey(
			"Gost_Klausuren_Kursklausuren_Kurs_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Kurs_ID, Schema.tab_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Kursklausuren_Termin_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Kursklausuren_Termin_ID_FK = addForeignKey(
			"Gost_Klausuren_Kursklausuren_Termin_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Termin_ID, Schema.tab_Gost_Klausuren_Termine.col_ID)
		);

//	/** Die Definition des Fremdschlüssels Gost_Klausuren_Kursklausuren_Raum_ID_FK */
//	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Kursklausuren_Raum_ID_FK = addForeignKey(
//			"Gost_Klausuren_Kursklausuren_Raum_ID_FK",
//			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
//			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
//			new Pair<>(col_Raum_ID, Schema.tab_Gost_Klausuren_Raeume.col_ID)
//		);


	/** Die Definition des Unique-Index Gost_Klausuren_Kursklausuren_UC1 */
	public SchemaTabelleUniqueIndex unique_Gost_Klausuren_Kursklausuren_UC1 = addUniqueIndex("Gost_Klausuren_Kursklausuren_UC1",
			col_Vorgabe_ID, col_Kurs_ID
		);



	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Kursklausuren.
	 */
	public Tabelle_Gost_Klausuren_Kursklausuren() {
		super("Gost_Klausuren_Kursklausuren", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenKursklausuren");
		setJavaComment("Tabelle für die konkreten Kursklausurentitäten");
	}

}
