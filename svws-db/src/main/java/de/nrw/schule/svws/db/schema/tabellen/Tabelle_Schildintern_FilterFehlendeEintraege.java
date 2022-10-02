package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_FilterFehlendeEintraege.
 */
public class Tabelle_Schildintern_FilterFehlendeEintraege extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: ID des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Schildintern Tabelle: Beschreibung des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden");

	/** Die Definition der Tabellenspalte Feldname */
	public SchemaTabelleSpalte col_Feldname = add("Feldname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Schildintern Tabelle: Feldname des zu prüfenden Feldes");

	/** Die Definition der Tabellenspalte Tabellen */
	public SchemaTabelleSpalte col_Tabellen = add("Tabellen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Tabellenname des zu prüfenden Feldes");

	/** Die Definition der Tabellenspalte SQLText */
	public SchemaTabelleSpalte col_SQLText = add("SQLText", SchemaDatentypen.TEXT, false)
		.setJavaComment("Schildintern Tabelle: Abfrage die zur Prüfung des Feldes führt.");

	/** Die Definition der Tabellenspalte Schulform */
	public SchemaTabelleSpalte col_Schulform = add("Schulform", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: ggf Schulform für bestimmte Schulformen");

	/** Die Definition der Tabellenspalte Feldtyp */
	public SchemaTabelleSpalte col_Feldtyp = add("Feldtyp", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: Feldty des zu prüfenden Feldes");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_FilterFehlendeEintraege.
	 */
	public Tabelle_Schildintern_FilterFehlendeEintraege() {
		super("Schildintern_FilterFehlendeEintraege", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternFilterFehlendeEintraege");
		setJavaComment("Schild2: Liste mit Feldern, die für den Filter auf fehlende Einträge verwendet werden können");
	}

}
