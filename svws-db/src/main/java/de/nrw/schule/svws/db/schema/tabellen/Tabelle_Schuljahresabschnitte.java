package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schuljahresabschnitte.
 */
public class Tabelle_Schuljahresabschnitte extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Schuljahresabschnittes");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Schuljahr des Schuljahresabschnitts (z.B. 2012 für 2012/13)");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Abschnitt des Schuljahresabschnitts");

	/** Die Definition der Tabellenspalte VorigerAbschnitt_ID */
	public SchemaTabelleSpalte col_VorigerAbschnitt_ID = add("VorigerAbschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des vorigen Schuljahresabschnitts");

	/** Die Definition der Tabellenspalte FolgeAbschnitt_ID */
	public SchemaTabelleSpalte col_FolgeAbschnitt_ID = add("FolgeAbschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des nachfolgenden Schuljahresabschnitts");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schuljahresabschnitte.
	 */
	public Tabelle_Schuljahresabschnitte() {
		super("Schuljahresabschnitte", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOSchuljahresabschnitte");
		setJavaComment("Tabelle mit den in der DB angelegten Schuljahresabschnitten ");
	}

}
