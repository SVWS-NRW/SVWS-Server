package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Jahrgaenge_Bezeichnungen.
 */
public class Tabelle_Jahrgaenge_Bezeichnungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID des Jahrgangs");

	/** Die Definition der Tabellenspalte Schulform_Kuerzel */
	public SchemaTabelleSpalte col_Schulform_Kuerzel = add("Schulform_Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("das Kürzel der Schulform, für welche die Bezeichnung gültig ist");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("die Bezeichnung des Jahrgangs");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Jahrgaenge_Bezeichnungen.
	 */
	public Tabelle_Jahrgaenge_Bezeichnungen() {
		super("Jahrgaenge_Bezeichnungen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAlleJahrgaengeBezeichnungen");
		setJavaComment("Tabelle der Bezeichnungen von Jahrgaenge bei den einzelnen Schulformen");
	}

}
