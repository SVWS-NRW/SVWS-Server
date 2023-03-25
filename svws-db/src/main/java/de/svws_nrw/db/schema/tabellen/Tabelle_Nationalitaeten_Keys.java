package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Nationalitaeten_Keys.
 */
public class Tabelle_Nationalitaeten_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte DEStatisCode */
	public SchemaTabelleSpalte col_DEStatisCode = add("DEStatisCode", SchemaDatentypen.VARCHAR, true).setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der dreistellige Länder-Code des statistischen Bundesamtes (DESTATIS)");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Nationalitaeten_Keys.
	 */
	public Tabelle_Nationalitaeten_Keys() {
		super("Nationalitaeten_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schule");
		setJavaClassName("DTONationalitaetenKeys");
		setJavaComment("Gültige Schlüsselwerte für Fremdschlüssel zu den Nationalitäten");
	}

}
