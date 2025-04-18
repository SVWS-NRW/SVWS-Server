package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle OrganisationsformenKatalog_Keys.
 */
public class Tabelle_OrganisationsformenKatalog_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
			.setNotNull()
			.setJavaComment("Das Kürzel der Organisationsform");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle OrganisationsformenKatalog_Keys.
	 */
	public Tabelle_OrganisationsformenKatalog_Keys() {
		super("OrganisationsformenKatalog_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOOrganisationsformenKatalogKeys");
		setJavaComment("Gültige Schlüsselwerte (ASD-Kürzel) für Fremdschlüssel zu den zulässigen Organisationsformen");
	}

}
