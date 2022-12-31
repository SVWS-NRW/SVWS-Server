package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Klausuren.
 */
public class Tabelle_Gost_Klausuren_Klausuren extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klausurvorgaben (generiert)");

	/** Die Definition der Tabellenspalte Vorgabe_ID */
	public SchemaTabelleSpalte col_Vorgabe_ID = add("Vorgabe_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Klausurvorgaben");

	/** Die Definition der Tabellenspalte Kurs_ID */
	public SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Kurs_ID der Klausur");
	
	/** Die Definition der Tabellenspalte Termin */
	public SchemaTabelleSpalte col_Termin_ID = add("Termin_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Klausurtermins");
	
	/** Die Definition der Tabellenspalte Termin */
	public SchemaTabelleSpalte col_Raum_ID = add("Raum_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Klausurraums");
	
	/** Die Definition der Tabellenspalte Startzeit */
	public SchemaTabelleSpalte col_Startzeit = add("Startzeit", SchemaDatentypen.TIME, false)
		.setJavaComment("Startzeit der Klausur, wenn abweichend von Startzeit der Klausur-Schiene");	
	
	
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_Klausuren_Vorgabe_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Klausuren_Vorgabe_ID_FK = addForeignKey(
			"Gost_Klausuren_Klausuren_Vorgabe_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Vorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
		);
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_Klausuren_Kurs_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Klausuren_Kurs_ID_FK = addForeignKey(
			"Gost_Klausuren_Klausuren_Kurs_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Kurs_ID, Schema.tab_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Klausuren_Termin_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Klausuren_Termin_ID_FK = addForeignKey(
			"Gost_Klausuren_Klausuren_Termin_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Termin_ID, Schema.tab_Gost_Klausuren_Termine.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Klausuren_Raum_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Klausuren_Raum_ID_FK = addForeignKey(
			"Gost_Klausuren_Klausuren_Raum_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Termin_ID, Schema.tab_Gost_Klausuren_Raeume.col_ID)
		);
	
	
	/** Die Definition des Unique-Index Gost_Klausuren_Klausuren_UC1 */
	public SchemaTabelleUniqueIndex unique_Gost_Klausuren_Klausuren_UC1 = addUniqueIndex("Gost_Klausuren_Klausuren_UC1", 
			col_Vorgabe_ID, col_Kurs_ID
		);



	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Klausuren.
	 */
	public Tabelle_Gost_Klausuren_Klausuren() {
		super("Gost_Klausuren_Klausuren", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenKlausuren");
		setJavaComment("Tabelle für die konkreten Klausurentitäten");
	}

}
