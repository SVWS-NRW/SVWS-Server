package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle AllgemeineMerkmaleKatalog.
 */
public class Tabelle_AllgemeineMerkmaleKatalog extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des allgemeinen Merkmals bei Schulen und/oder Schülern");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Das Kürzel des allgemeinen Merkmals bei Schulen und/oder Schülern");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die texttuelle Beschreibung des allgemeinen Merkmals bei Schulen und/oder Schülern");

	/** Die Definition der Tabellenspalte beiSchule */
	public SchemaTabelleSpalte col_beiSchule = add("beiSchule", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Gibt an, das das Merkmal bei der Schule gesetzt werden kann");

	/** Die Definition der Tabellenspalte beiSchueler */
	public SchemaTabelleSpalte col_beiSchueler = add("beiSchueler", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Gibt an, das das Merkmal bei einem Schüler gesetzt werden kann");

	/** Die Definition der Tabellenspalte KuerzelASD */
	public SchemaTabelleSpalte col_KuerzelASD = add("KuerzelASD", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("ggf. ein Kürzel, welches im Rahmen der amtlichen Schulstatistik verwendet wird");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle AllgemeineMerkmaleKatalog.
	 */
	public Tabelle_AllgemeineMerkmaleKatalog() {
		super("AllgemeineMerkmaleKatalog", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAllgemeineMerkmaleKatalog");
		setJavaComment("Eine Tabelle mit den allgemeinen Merkmalen bei Schulen und Schülern");
	}

}
