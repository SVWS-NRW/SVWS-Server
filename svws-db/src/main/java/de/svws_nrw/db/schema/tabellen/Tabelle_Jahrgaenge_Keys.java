package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Jahrgaenge_Keys.
 */
public class Tabelle_Jahrgaenge_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
			.setNotNull()
			.setJavaComment("Das zweistellige Kürzel des Jahrgangs");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Jahrgaenge_Keys.
	 */
	public Tabelle_Jahrgaenge_Keys() {
		super("Jahrgaenge_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAlleJahrgaengeKeys");
		setJavaComment("Tabelle der Schlüsselwerte für Jahrgänge, die bei anderen Tabellen als Foreign-Keys verwendet werden können");
	}

}
