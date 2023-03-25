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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Schuelerklausuren_Raeume_Stunden.
 */
public class Tabelle_Gost_Klausuren_Schuelerklausuren_Raeume_Stunden extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schuelerklausur_ID */
	public SchemaTabelleSpalte col_Schuelerklausur_ID = add("Schuelerklausur_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Schuelerklausur");

	/** Die Definition der Tabellenspalte KlausurRaumStunde_ID */
	public SchemaTabelleSpalte col_KlausurRaumStunde_ID = add("KlausurRaumStunde_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klausurraumstunde");

	
	/** Die Definition des Fremdschlüssels Schuelerklausur_ID_Schuelerklausur_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Raeume_Stunden_Schuelerklausur_ID_FK = addForeignKey(
			"Gost_Klausuren_SKlausuren_Raeume_Stunden_SK_ID_FK", // Fremdschlüsselname Gost_Klausuren_Schuelerklausuren_Raeume_Stunden_Schuelerklausur_ID_FK zu lang
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schuelerklausur_ID, Schema.tab_Gost_Klausuren_Schuelerklausuren.col_ID)
		);
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_Schuelerklausuren_Raeume_Stunden_KlausurRaumStunde_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Raeume_Stunden_KlausurRaumStunde_ID_FK = addForeignKey(
			"Gost_Klausuren_SKlausuren_Raeume_Stunden_KRS_ID_FK", // Fremdschlüsselname Gost_Klausuren_Schuelerklausuren_Raeume_Stunden_KlausurRaumStunde_ID_FK zu lang 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_KlausurRaumStunde_ID, Schema.tab_Gost_Klausuren_Raeume_Stunden.col_ID)
		);
	

	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Schuelerklausuren_Raeume_Stunden.
	 */
	public Tabelle_Gost_Klausuren_Schuelerklausuren_Raeume_Stunden() {
		super("Gost_Klausuren_Schuelerklausuren_Raeume_Stunden", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenSchuelerklausurenRaeumeStunden");
		setJavaComment("Tabelle für die Definition von Schülerklausur-Raumstunden");
	}

}
