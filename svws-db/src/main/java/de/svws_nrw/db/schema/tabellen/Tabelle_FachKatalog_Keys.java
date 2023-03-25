package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle FachKatalog_Keys.
 */
public class Tabelle_FachKatalog_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Das ASD-Kürzel des Faches");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle FachKatalog_Keys.
	 */
	public Tabelle_FachKatalog_Keys() {
		super("FachKatalog_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.faecher");
		setJavaClassName("DTOFaecherKatalogKeys");
		setJavaComment("Gültige Schlüsselwerte (ASD-Kürzel) für Fremdschlüssel zu den zulässigen Fächern");
	}

}
