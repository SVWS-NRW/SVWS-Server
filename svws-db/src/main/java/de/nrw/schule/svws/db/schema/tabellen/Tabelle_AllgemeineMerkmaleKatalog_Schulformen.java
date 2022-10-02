package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle AllgemeineMerkmaleKatalog_Schulformen.
 */
public class Tabelle_AllgemeineMerkmaleKatalog_Schulformen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Merkmal_ID */
	public SchemaTabelleSpalte col_Merkmal_ID = add("Merkmal_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("die ID des allgemeinen Merkmals bei Schulen und/oder Schülern");

	/** Die Definition der Tabellenspalte Schulform_Kuerzel */
	public SchemaTabelleSpalte col_Schulform_Kuerzel = add("Schulform_Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("das Kürzel des allgemeinen Merkmals bei Schulen und/oder Schülern");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle AllgemeineMerkmaleKatalog_Schulformen.
	 */
	public Tabelle_AllgemeineMerkmaleKatalog_Schulformen() {
		super("AllgemeineMerkmaleKatalog_Schulformen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAllgemeineMerkmaleKatalogSchulformen");
		setJavaComment("Tabelle mit der Zuordnungen der erlaubten Schulformen bei den allgemeinen Merkmalen bei Schulen und Schülern");
	}

}
