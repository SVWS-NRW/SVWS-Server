package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_SchuelerklausurenTermine_Raumstunden.
 */
public class Tabelle_Gost_Klausuren_SchuelerklausurenTermine_Raumstunden extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schuelerklausurtermin_ID */
	public SchemaTabelleSpalte col_Schuelerklausurtermin_ID = add("Schuelerklausurtermin_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Schuelerklausurtermins");

	/** Die Definition der Tabellenspalte Raumstunde_ID */
	public SchemaTabelleSpalte col_Raumstunde_ID = add("Raumstunde_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klausurraumstunde");


	/** Die Definition des Fremdschlüssels Gost_Klausuren_SchuelerklausurenTermine_Raumstunden_Schuelerklausurtermin_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_SchuelerklausurenTermine_Raumstunden_Schuelerklausur_ID_FK = addForeignKey(
			"Gost_Klausuren_SKT_Raumstunden_SK_ID_FK", // Fremdschlüsselname Gost_Klausuren_SchuelerklausurenTermine_Raumstunden_Schuelerklausur_ID_FK zu lang
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schuelerklausurtermin_ID, Schema.tab_Gost_Klausuren_Schuelerklausuren_Termine.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_SchuelerklausurenTermine_Raumstunden_KlausurRaumStunde_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_SchuelerklausurenTermine_Raumstunden_KlausurRaumStunde_ID_FK = addForeignKey(
			"Gost_Klausuren_SKT_Raumstunden_KRS_ID_FK", // Fremdschlüsselname Gost_Klausuren_SchuelerklausurenTermine_Raumstunden_KlausurRaumStunde_ID_FK zu lang
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Raumstunde_ID, Schema.tab_Gost_Klausuren_Raumstunden.col_ID)
		);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_SchuelerklausurenTermine_Raumstunden_IDX_Schuelerklausur_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_SKT_Raumstunden_IDX_Schuelerklausurtermin_ID = addIndex("Gost_Klausuren_SKT_Raumstunden_IDX_Schuelerklausurtermin_ID",
			col_Schuelerklausurtermin_ID
		);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_SchuelerklausurenTermine_Raumstunden_IDX_KlausurRaumStunde_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_SKT_Raumstunden_IDX_KlausurRaumStunde_ID = addIndex("Gost_Klausuren_SKT_Raumstunden_IDX_KlausurRaumStunde_ID",
			col_Raumstunde_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Schuelerklausuren_Raeume_Stunden.
	 */
	public Tabelle_Gost_Klausuren_SchuelerklausurenTermine_Raumstunden() {
		super("Gost_Klausuren_SchuelerklausurenTermine_Raumstunden", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenSchuelerklausurenTermineRaumstunden");
		setJavaComment("Tabelle für die Definition von Schülerklausurtermin-Raumstunden");
	}

}
