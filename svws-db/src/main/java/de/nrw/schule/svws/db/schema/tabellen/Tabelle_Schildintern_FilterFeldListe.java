package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_FilterFeldListe.
 */
public class Tabelle_Schildintern_FilterFeldListe extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: ID für den Eintrag welche Felder iim Attributsfilter zur Verfügung stehen.");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Bezeichnung im Attributsfilter");

	/** Die Definition der Tabellenspalte DBFeld */
	public SchemaTabelleSpalte col_DBFeld = add("DBFeld", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Datenbankfeld im Attributsfilter");

	/** Die Definition der Tabellenspalte Typ */
	public SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Typ des Feldes im Attributsfilter");

	/** Die Definition der Tabellenspalte Werte */
	public SchemaTabelleSpalte col_Werte = add("Werte", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: mögliche Werte des Feldes im Attributsfilter");

	/** Die Definition der Tabellenspalte StdWert */
	public SchemaTabelleSpalte col_StdWert = add("StdWert", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Schildintern Tabelle: Standardwert im Attributsfilter");

	/** Die Definition der Tabellenspalte Operator */
	public SchemaTabelleSpalte col_Operator = add("Operator", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Schildintern Tabelle: Operator  im Attributsfilter (größer-kleiner)");

	/** Die Definition der Tabellenspalte Zusatzbedingung */
	public SchemaTabelleSpalte col_Zusatzbedingung = add("Zusatzbedingung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Zusatzbedingung im Attributsfilter");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_FilterFeldListe.
	 */
	public Tabelle_Schildintern_FilterFeldListe() {
		super("Schildintern_FilterFeldListe", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternFilterFeldListe");
		setJavaComment("Wird im \"Attributsfilter\" verwendet (für die Ja/Nein-Auswahl)");
	}

}
