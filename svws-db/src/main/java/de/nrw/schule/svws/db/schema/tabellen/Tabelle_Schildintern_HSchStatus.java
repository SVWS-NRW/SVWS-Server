package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_HSchStatus.
 */
public class Tabelle_Schildintern_HSchStatus extends SchemaTabelle {

	/** Die Definition der Tabellenspalte StatusNr */
	public SchemaTabelleSpalte col_StatusNr = add("StatusNr", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Nummer des Schüler-Status");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schildintern Tabelle: Klartext des Schülerstatus");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Sortierung des Schülerstatus");

	/** Die Definition der Tabellenspalte InSimExp */
	public SchemaTabelleSpalte col_InSimExp = add("InSimExp", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Schildintern Tabelle: DEPRECATED: ");

	/** Die Definition der Tabellenspalte SIMAbschnitt */
	public SchemaTabelleSpalte col_SIMAbschnitt = add("SIMAbschnitt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: DEPRECATED: ");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_HSchStatus.
	 */
	public Tabelle_Schildintern_HSchStatus() {
		super("Schildintern_HSchStatus", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternSchuelerStatus");
		setJavaComment("Liste der möglichen Schüler Status-Werte");
	}

}
