package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerLeitungsfunktion_Keys.
 */
public class Tabelle_LehrerLeitungsfunktion_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Leitungsfunktion");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerLeitungsfunktion_Keys.
	 */
	public Tabelle_LehrerLeitungsfunktion_Keys() {
		super("LehrerLeitungsfunktion_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerLeitungsfunktionKeys");
		setJavaComment("Gültige Schlüsselwerte für Fremdschlüssel zu den definierten Leitungsfunktionen von Lehrern");
	}

}
