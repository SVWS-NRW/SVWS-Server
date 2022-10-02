package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_KAoA_Berufsfeld.
 */
public class Tabelle_Schildintern_KAoA_Berufsfeld extends SchemaTabelle {

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

	/** Die Definition der Tabellenspalte BF_Kuerzel */
	public SchemaTabelleSpalte col_BF_Kuerzel = add("BF_Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Kürzel des Berufsfeldes");

	/** Die Definition der Tabellenspalte BF_Beschreibung */
	public SchemaTabelleSpalte col_BF_Beschreibung = add("BF_Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schildintern Tabelle: Beschreibung (Klartext) des Berufsfeldes");


	/** Die Definition des Non-Unique-Index Schildintern_KAoA_Berufsfeld_IDX1 */
	public SchemaTabelleIndex index_Schildintern_KAoA_Berufsfeld_IDX1 = addIndex("Schildintern_KAoA_Berufsfeld_IDX1", 
			col_BF_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_KAoA_Berufsfeld.
	 */
	public Tabelle_Schildintern_KAoA_Berufsfeld() {
		super("Schildintern_KAoA_Berufsfeld", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOKAoABerufsfeld");
		setJavaComment("KAOA-Defaultdaten werden vom MSB geliefert");
	}

}
