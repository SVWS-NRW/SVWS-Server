package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulabschluesse_Berufsbildend.
 */
public class Tabelle_Schulabschluesse_Berufsbildend extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Art des berufsbildendenbildenden Schulabschlusses");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Kürzel der Art des berufsbildendenbildenden Schulabschlusses");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Bezeichnung der Art des berufsbildendenbildenden Schulabschlusses");

	/** Die Definition der Tabellenspalte Kuerzel_Statistik */
	public SchemaTabelleSpalte col_Kuerzel_Statistik = add("Kuerzel_Statistik", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setNotNull()
		.setJavaComment("Kürzel der Art des berufsbildendenbildenden Schulabschlusses für die amtliche Schulstatistik");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt an, ab welchem Schuljahr die Abschlussart gültig ist");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt an, bis zu welchem Schuljahr die Abschlussart gültig ist");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulabschluesse_Berufsbildend.
	 */
	public Tabelle_Schulabschluesse_Berufsbildend() {
		super("Schulabschluesse_Berufsbildend", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOSchulabschluesse_Berufsbildend");
		setJavaComment("Tabelle der brufsbildenden Schulabschlüsse");
	}

}
