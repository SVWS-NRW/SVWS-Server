package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_KAoA_Kategorie.
 */
public class Tabelle_Schildintern_KAoA_Kategorie extends SchemaTabelle {

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

	/** Die Definition der Tabellenspalte K_Kuerzel */
	public SchemaTabelleSpalte col_K_Kuerzel = add("K_Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Kürzel der Kategorie");

	/** Die Definition der Tabellenspalte K_Beschreibung */
	public SchemaTabelleSpalte col_K_Beschreibung = add("K_Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schildintern Tabelle: Beschreibung (Klartext) der Kategorie");

	/** Die Definition der Tabellenspalte K_Jahrgaenge */
	public SchemaTabelleSpalte col_K_Jahrgaenge = add("K_Jahrgaenge", SchemaDatentypen.VARCHAR, false).setDatenlaenge(25)
		.setJavaComment("Schildintern Tabelle: Jahrgangsstufen in denen der Eintrag gemacht werden darf");


	/** Die Definition des Non-Unique-Index Schildintern_KAoA_Kategorie_IDX1 */
	public SchemaTabelleIndex index_Schildintern_KAoA_Kategorie_IDX1 = addIndex("Schildintern_KAoA_Kategorie_IDX1", 
			col_K_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_KAoA_Kategorie.
	 */
	public Tabelle_Schildintern_KAoA_Kategorie() {
		super("Schildintern_KAoA_Kategorie", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOKAoAKategorie");
		setJavaComment("KAOA-Defaultdaten werden vom MSB geliefert");
	}

}
