package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_KAoA_Anschlussoption.
 */
public class Tabelle_Schildintern_KAoA_Anschlussoption extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Eindeutige ID für den Datensatz");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Der Datensatz ist gültig ab dem Datum");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Der Datensatz ist gültig bis zu dem Datum");

	/** Die Definition der Tabellenspalte AO_Kuerzel */
	public SchemaTabelleSpalte col_AO_Kuerzel = add("AO_Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Kürzel der Anschlussoption");

	/** Die Definition der Tabellenspalte AO_Beschreibung */
	public SchemaTabelleSpalte col_AO_Beschreibung = add("AO_Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schildintern Tabelle: Beschreibung (Klartext) der Anschlussoption");

	/** Die Definition der Tabellenspalte AO_Stufen */
	public SchemaTabelleSpalte col_AO_Stufen = add("AO_Stufen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Schildintern Tabelle: Jahrgangsstufen in denen der Eintrag gemacht werden darf");

	/** Die Definition der Tabellenspalte Zusatzmerkmal_Anzeige */
	public SchemaTabelleSpalte col_Zusatzmerkmal_Anzeige = add("Zusatzmerkmal_Anzeige", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden");


	/** Die Definition des Non-Unique-Index Schildintern_KAoA_Anschlussoption_IDX1 */
	public SchemaTabelleIndex index_Schildintern_KAoA_Anschlussoption_IDX1 = addIndex("Schildintern_KAoA_Anschlussoption_IDX1", 
			col_AO_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_KAoA_Anschlussoption.
	 */
	public Tabelle_Schildintern_KAoA_Anschlussoption() {
		super("Schildintern_KAoA_Anschlussoption", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOKAoAAnschlussoption");
		setJavaComment("KAOA-Defaultdaten werden vom MSB geliefert");
	}

}
