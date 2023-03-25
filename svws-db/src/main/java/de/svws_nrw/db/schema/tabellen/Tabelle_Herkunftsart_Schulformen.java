package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Herkunftsart_Schulformen.
 */
public class Tabelle_Herkunftsart_Schulformen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Herkunftsart_ID */
	public SchemaTabelleSpalte col_Herkunftsart_ID = add("Herkunftsart_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("die ID der Herkunftsart");

	/** Die Definition der Tabellenspalte Schulform_Kuerzel */
	public SchemaTabelleSpalte col_Schulform_Kuerzel = add("Schulform_Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("das Kürzel der Schulform");

	/** Die Definition der Tabellenspalte KurzBezeichnung */
	public SchemaTabelleSpalte col_KurzBezeichnung = add("KurzBezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Eine kurze Bezeichnung der Herkunftsart");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die Bezeichnung der Herkunftsart");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Herkunftsart_Schulformen.
	 */
	public Tabelle_Herkunftsart_Schulformen() {
		super("Herkunftsart_Schulformen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOHerkunftsartSchulformen");
		setJavaComment("Tabelle mit der Zuordnungen der erlaubten Schulformen bei den Herkunftsarten und den jeweiligen Beschreibungen der Arten ");
	}

}
