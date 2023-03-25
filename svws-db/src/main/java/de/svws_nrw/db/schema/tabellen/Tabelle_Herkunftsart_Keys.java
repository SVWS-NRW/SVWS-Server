package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Herkunftsart_Keys.
 */
public class Tabelle_Herkunftsart_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Das Kürzel der Herkunftsart");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Herkunftsart_Keys.
	 */
	public Tabelle_Herkunftsart_Keys() {
		super("Herkunftsart_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schule");
		setJavaClassName("DTOHerkunftsartKeys");
		setJavaComment("Gültige Schlüsselwerte für Fremdschlüssel zu den Herkunftsarten");
	}

}
