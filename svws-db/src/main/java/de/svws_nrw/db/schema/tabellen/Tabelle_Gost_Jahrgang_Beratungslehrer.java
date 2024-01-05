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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Jahrgang_Beratungslehrer.
 */
public class Tabelle_Gost_Jahrgang_Beratungslehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Schuljahr, in welchem der Jahrgang das Abitur macht ");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: ID des Beratungslehrers in der Lehrertabelle");


	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Beratungslehrer_Abi_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Beratungslehrer_Abi_Jahrgang_FK = addForeignKey(
			"Gost_Jahrgang_Beratungslehrer_Abi_Jahrgang_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
		);

	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Beratungslehrer_Lehrer_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Beratungslehrer_Lehrer_ID_FK = addForeignKey(
			"Gost_Jahrgang_Beratungslehrer_Lehrer_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);


	/** Die Definition des Non-Unique-Index Gost_Jahrgang_Beratungslehrer_IDX_Abi_Jahrgang */
	public SchemaTabelleIndex index_Gost_Jahrgang_Beratungslehrer_IDX_Abi_Jahrgang = addIndex("Gost_Jahrgang_Beratungslehrer_IDX_Abi_Jahrgang",
		col_Abi_Jahrgang
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Jahrgang_Beratungslehrer.
	 */
	public Tabelle_Gost_Jahrgang_Beratungslehrer() {
		super("Gost_Jahrgang_Beratungslehrer", SchemaRevisionen.REV_4);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostJahrgangBeratungslehrer");
		setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Die Beratungslehrer in den einzelnen Jahrgängen");
	}

}
