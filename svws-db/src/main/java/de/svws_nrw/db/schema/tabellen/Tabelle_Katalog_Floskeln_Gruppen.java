package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Katalog_Floskel_Gruppen.
 */
public class Tabelle_Katalog_Floskeln_Gruppen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Floskelgruppe (generiert)");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setNotNull()
			.setJavaComment("Das Kürzel der Floskelgruppe");

	/** Die Definition der Tabellenspalte Hauptgruppe_ID */
	public SchemaTabelleSpalte col_Hauptgruppe_ID = add("Hauptgruppe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die übergeordnete Hauptgruppe, welche spezifiziert zu welchem Bemerkungskatalog die Floskeln der Floskelgruppe gehören. (siehe Core-Type)");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
			.setNotNull()
			.setJavaComment("Die Bezeichnung der Floskelgruppe");

	/** Die Definition der Tabellenspalte Farbe */
	public SchemaTabelleSpalte col_Farbe = add("Farbe", SchemaDatentypen.INT, false)
			.setJavaComment("Die Farbe für die Floskelgruppe");

	/** Die Definition des Unique-Index KatalogFloskelGruppen_UC1 */
	public SchemaTabelleUniqueIndex unique_KatalogFloskelGruppen_UC1 = addUniqueIndex("KatalogFloskelGruppen_UC1", col_Kuerzel);
	/**
	 * Erstellt die Schema-Definition für die Tabelle Floskelgruppen.
	 */
	public Tabelle_Katalog_Floskeln_Gruppen() {
		super("Katalog_Floskeln_Gruppen", SchemaRevisionen.REV_52);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("katalog");
		setJavaClassName("DTOFloskelgruppen");
		setJavaComment("Liste der Floskelgruppen. Diese Liste kann erweitert werden um Textbausteinen zu verwalten und die Anzeige in den Editoren zu steuern");
	}

}
