package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Blockung_Zwischenergebnisse.
 */
public class Tabelle_Gost_Blockung_Zwischenergebnisse extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Zwischenergebnisses einer Blockung (generiert)");

	/** Die Definition der Tabellenspalte Blockung_ID */
	public SchemaTabelleSpalte col_Blockung_ID = add("Blockung_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Blockung");

	/** Die Definition der Tabellenspalte IstAktiv */
	public SchemaTabelleSpalte col_IstAktiv = add("IstAktiv", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob das Zwischenergebnis als aktives Zwischenergebnis einer Blockung markiert wurde oder nicht: 1 - true, 0 - false ");

	/** Die Definition des Fremdschlüssels Gost_Blockung_Zwischenergebnisse_Blockung_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Zwischenergebnisse_Blockung_FK = addForeignKey(
			"Gost_Blockung_Zwischenergebnisse_Blockung_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Blockung_ID, Schema.tab_Gost_Blockung.col_ID)
		);


	/** Die Definition des Non-Unique-Index Gost_Blockung_Zwischenergebnisse_IDX_Blockung_ID */
	public SchemaTabelleIndex index_Gost_Blockung_Zwischenergebnisse_IDX_Blockung_ID = addIndex("Gost_Blockung_Zwischenergebnisse_IDX_Blockung_ID",
		col_Blockung_ID
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Blockung_Zwischenergebnisse.
	 */
	public Tabelle_Gost_Blockung_Zwischenergebnisse() {
		super("Gost_Blockung_Zwischenergebnisse", SchemaRevisionen.REV_7);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.kursblockung");
		setJavaClassName("DTOGostBlockungZwischenergebnis");
		setJavaComment("Tabelle für Zwischenergebnisse, welche einer Kursblockung der gymnasialen Oberstufe zugeordnet sind");
	}

}
