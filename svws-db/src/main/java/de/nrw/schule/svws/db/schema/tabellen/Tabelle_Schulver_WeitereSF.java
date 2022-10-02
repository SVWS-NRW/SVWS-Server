package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulver_WeitereSF.
 */
public class Tabelle_Schulver_WeitereSF extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SNR */
	public SchemaTabelleSpalte col_SNR = add("SNR", SchemaDatentypen.VARCHAR, true).setDatenlaenge(6)
		.setNotNull()
		.setJavaComment("Schulver Tabelle IT.NRW: Schulnummer der Schule");

	/** Die Definition der Tabellenspalte SGL */
	public SchemaTabelleSpalte col_SGL = add("SGL", SchemaDatentypen.VARCHAR, true).setDatenlaenge(3)
		.setDefault("   ")
		.setNotNull()
		.setJavaComment("Schulver Tabelle IT.NRW: Gliederung Statistikkürzel");

	/** Die Definition der Tabellenspalte FSP */
	public SchemaTabelleSpalte col_FSP = add("FSP", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
		.setDefault("  ")
		.setNotNull()
		.setJavaComment("Schulver Tabelle IT.NRW: Förderschwerpunkt ASD-Kürzel");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schulver Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulver_WeitereSF.
	 */
	public Tabelle_Schulver_WeitereSF() {
		super("Schulver_WeitereSF", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schulver");
		setJavaClassName("DTOSchulverWeitereSF");
		setJavaComment("IT.NRW Liste der weiteren Schulformen (wird für die Schulstatistik verwendet)");
	}

}
