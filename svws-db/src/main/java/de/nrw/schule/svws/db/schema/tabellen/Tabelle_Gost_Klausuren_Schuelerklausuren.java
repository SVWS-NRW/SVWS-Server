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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Schuelerklausuren.
 */
public class Tabelle_Gost_Klausuren_Schuelerklausuren extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klausurvorgaben (generiert)");

	/** Die Definition der Tabellenspalte Kursklausur_ID */
	public SchemaTabelleSpalte col_Kursklausur_ID = add("Kursklausur_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Kursklausur");
	
	/** Die Definition der Tabellenspalte Termin_ID */
	public SchemaTabelleSpalte col_Termin_ID = add("Termin_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Klausurtermins");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Schülers");
	
//	/** Die Definition der Tabellenspalte Raum_ID */
//	public SchemaTabelleSpalte col_Raum_ID = add("Raum_ID", SchemaDatentypen.BIGINT, false)
//		.setJavaComment("ID des Klausurraums");
	
	/** Die Definition der Tabellenspalte Startzeit */
	public SchemaTabelleSpalte col_Startzeit = add("Startzeit", SchemaDatentypen.TIME, false)
		.setJavaComment("Startzeit der Klausur, wenn abweichend von Startzeit der Klausur-Schiene");	
	
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_Schuelerklausuren_Kursklausur_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Kursklausur_ID_FK = addForeignKey(
			"Gost_Klausuren_Schuelerklausuren_Kursklausur_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Kursklausur_ID, Schema.tab_Gost_Klausuren_Kursklausuren.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Schuelerklausuren_Schueler_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Schueler_ID_FK = addForeignKey(
			"Gost_Klausuren_Schuelerklausuren_Schueler_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_Schuelerklausuren_Termin_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Termin_ID_FK = addForeignKey(
			"Gost_Klausuren_Schuelerklausuren_Termin_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Termin_ID, Schema.tab_Gost_Klausuren_Termine.col_ID)
		);

//	/** Die Definition des Fremdschlüssels Gost_Klausuren_Schuelerklausuren_Raum_ID_FK */
//	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Raum_ID_FK = addForeignKey(
//			"Gost_Klausuren_Schuelerklausuren_Raum_ID_FK", 
//			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
//			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
//			new Pair<>(col_Raum_ID, Schema.tab_Gost_Klausuren_Raeume.col_ID)
//		);
	
	
	/** Die Definition des Unique-Index Gost_Klausuren_Schuelerklausuren_UC1 */
	public SchemaTabelleUniqueIndex unique_Gost_Klausuren_Klausuren_UC1 = addUniqueIndex("Gost_Klausuren_Schuelerklausuren_UC1", 
			col_Kursklausur_ID, col_Schueler_ID
		);



	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Schuelerklausuren.
	 */
	public Tabelle_Gost_Klausuren_Schuelerklausuren() {
		super("Gost_Klausuren_Schuelerklausuren", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenSchuelerklausuren");
		setJavaComment("Tabelle für die konkreten Schuelerklausurenentitäten");
	}

}
