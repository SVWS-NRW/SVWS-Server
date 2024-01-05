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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Blockung_Kurslehrer.
 */
public class Tabelle_Gost_Blockung_Kurslehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Blockung_Kurs_ID */
	public SchemaTabelleSpalte col_Blockung_Kurs_ID = add("Blockung_Kurs_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Kurses");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Lehrers, welcher dem Kurs zugeordnet ist");

	/** Die Definition der Tabellenspalte Reihenfolge */
	public SchemaTabelleSpalte col_Reihenfolge = add("Reihenfolge", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setNotNull()
		.setJavaComment("Eine Reihenfolge für die Lehrer, z.B. zur Unterscheidung des eigentlichen Kurslehrer (z.B. 1) und einer Zusatzkraft (z.B. 2)");

	/** Die Definition der Tabellenspalte Wochenstunden */
	public SchemaTabelleSpalte col_Wochenstunden = add("Wochenstunden", SchemaDatentypen.INT, false)
		.setDefault("3")
		.setNotNull()
		.setJavaComment("Die Anzahl der Wochenstunden für die der Lehrer in dem Kurs eingesetzt wird");


	/** Die Definition des Fremdschlüssels Gost_Blockung_Kurslehrer_Kurs_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Kurslehrer_Kurs_FK = addForeignKey(
			"Gost_Blockung_Kurslehrer_Kurs_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Blockung_Kurs_ID, Schema.tab_Gost_Blockung_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Blockung_Kurslehrer_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Kurslehrer_Lehrer_FK = addForeignKey(
			"Gost_Blockung_Kurslehrer_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);

	/** Die Definition des Non-Unique-Index Gost_Blockung_Kurslehrer_IDX_Blockung_Kurs_ID */
	public SchemaTabelleIndex index_Gost_Blockung_Kurslehrer_IDX_Blockung_Kurs_ID = addIndex("Gost_Blockung_Kurslehrer_IDX_Blockung_Kurs_ID",
		col_Blockung_Kurs_ID
	);

	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Blockung_Kurslehrer.
	 */
	public Tabelle_Gost_Blockung_Kurslehrer() {
		super("Gost_Blockung_Kurslehrer", SchemaRevisionen.REV_7);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost.kursblockung");
		setJavaClassName("DTOGostBlockungKurslehrer");
		setJavaComment("Tabelle für die Lehrer, welche einem Kurs einer Kursblockung der gymnasialen Oberstufe zugeordnet sind");
	}

}
