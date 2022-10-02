package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulgliederungen_Schulformen.
 */
public class Tabelle_Schulgliederungen_Schulformen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schulgliederung_ID */
	public SchemaTabelleSpalte col_Schulgliederung_ID = add("Schulgliederung_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("die ID der Schulgliederung");

	/** Die Definition der Tabellenspalte Schulform_Kuerzel */
	public SchemaTabelleSpalte col_Schulform_Kuerzel = add("Schulform_Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("das Kürzel der Schulform");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulgliederungen_Schulformen.
	 */
	public Tabelle_Schulgliederungen_Schulformen() {
		super("Schulgliederungen_Schulformen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAlleSchulgliederungenSchulformen");
		setJavaComment("Tabelle mit der Zuordnungen der erlaubten Schulformen bei Schulgliederungen");
	}

}
