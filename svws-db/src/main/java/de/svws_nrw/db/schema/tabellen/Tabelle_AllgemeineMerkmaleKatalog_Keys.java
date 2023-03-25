package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle AllgemeineMerkmaleKatalog_Keys.
 */
public class Tabelle_AllgemeineMerkmaleKatalog_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Das ASD-Kürzel des allgemeinen Merkmals bei Schulen und/oder Schülern");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle AllgemeineMerkmaleKatalog_Keys.
	 */
	public Tabelle_AllgemeineMerkmaleKatalog_Keys() {
		super("AllgemeineMerkmaleKatalog_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAllgemeineMerkmaleKatalogKeys");
		setJavaComment("Gültige Schlüsselwerte (ASD-Kürzel) für Fremdschlüssel zu den allgemeinen Merkmalen bei Schulen und Schülern");
	}

}
