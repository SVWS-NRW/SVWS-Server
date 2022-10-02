package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle FachKatalog_Schulformen.
 */
public class Tabelle_FachKatalog_Schulformen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("die ID des Faches");

	/** Die Definition der Tabellenspalte Schulform_Kuerzel */
	public SchemaTabelleSpalte col_Schulform_Kuerzel = add("Schulform_Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("das Kürzel der Schulform");

	/** Die Definition der Tabellenspalte Schulgliederung_Kuerzel */
	public SchemaTabelleSpalte col_Schulgliederung_Kuerzel = add("Schulgliederung_Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("das Kürzel der Schulgliederung bzw. des Bildungsganges. Leerer String, falls alle Gliederungen der Schulform gemeint sind");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle FachKatalog_Schulformen.
	 */
	public Tabelle_FachKatalog_Schulformen() {
		super("FachKatalog_Schulformen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.faecher");
		setJavaClassName("DTOFaecherKatalogSchulformen");
		setJavaComment("Tabelle mit der Zuordnungen der erlaubten Schulformen bei den zulässigen Fächern");
	}

}
