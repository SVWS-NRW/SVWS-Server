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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Jahrgang_Sprachenfolge.
 */
public class Tabelle_Gost_Jahrgang_Sprachenfolge extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: Schuljahr, in welchem der Jahrgang das Abitur macht");

	/** Die Definition der Tabellenspalte Sprache */
	public SchemaTabelleSpalte col_Sprache = add("Sprache", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: Atomares Sprachkürzel des Sprach-Faches");

	/** Die Definition der Tabellenspalte ReihenfolgeNr */
	public SchemaTabelleSpalte col_ReihenfolgeNr = add("ReihenfolgeNr", SchemaDatentypen.INT, false)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: Reihenfolge Nummer des Sprachenfolgeeintrags");

	/** Die Definition der Tabellenspalte ASDJahrgangVon */
	public SchemaTabelleSpalte col_ASDJahrgangVon = add("ASDJahrgangVon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: ASD-Jahrgang des Beginns des Sprachenfolgeeintrags");


	/** Die Definition des Fremdschlüssels Gost_Jahrgang_Sprachenfolge_Abi_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Jahrgang_Sprachenfolge_Abi_Jahrgang_FK = addForeignKey(
		"Gost_Jahrgang_Sprachenfolge_Abi_Jahrgang_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Jahrgang_Sprachenfolge.
	 */
	public Tabelle_Gost_Jahrgang_Sprachenfolge() {
		super("Gost_Jahrgang_Sprachenfolge", SchemaRevisionen.REV_4);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostJahrgangSprachenfolge");
		setJavaComment("Gymnasiale Oberstufe - Jahrgangsdaten: Informationen zu der jahrgangsspezifischen Vorlage zu den bei den Schülern im Jahrgang vorhandenen Sprachenfolgen");
	}

}
