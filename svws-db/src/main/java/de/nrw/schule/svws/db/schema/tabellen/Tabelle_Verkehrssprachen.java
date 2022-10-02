package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Verkehrssprachen.
 */
public class Tabelle_Verkehrssprachen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID der Verkehrssprache");

	/** Die Definition der Tabellenspalte Iso3 */
	public SchemaTabelleSpalte col_Iso3 = add("Iso3", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der dreistellige ISO 639-Code der Verkehrssprache");

	/** Die Definition der Tabellenspalte Iso2 */
	public SchemaTabelleSpalte col_Iso2 = add("Iso2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Der zweistellige ISO 639-1 Code der Verkehrssprache");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die Bezeichnung der Verkehrssprache");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Verkehrssprachen.
	 */
	public Tabelle_Verkehrssprachen() {
		super("Verkehrssprachen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schule");
		setJavaClassName("DTOVerkehrssprachen");
		setJavaComment("Die Verkehrssprachen, welche bei dem Migrationshintergrund zur Verfügung stehen");
	}

}
