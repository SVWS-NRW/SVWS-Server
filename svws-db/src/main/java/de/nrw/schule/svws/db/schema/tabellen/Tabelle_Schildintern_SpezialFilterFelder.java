package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_SpezialFilterFelder.
 */
public class Tabelle_Schildintern_SpezialFilterFelder extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: ID der Felder die im Filter II zur Verfügung stehen.");

	/** Die Definition der Tabellenspalte Gruppe */
	public SchemaTabelleSpalte col_Gruppe = add("Gruppe", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schildintern Tabelle: Gruppe in Filter II");

	/** Die Definition der Tabellenspalte KurzBez */
	public SchemaTabelleSpalte col_KurzBez = add("KurzBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Kurzbezeichnung in Filter II");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Bezeichnung in Filter II");

	/** Die Definition der Tabellenspalte Grundschule */
	public SchemaTabelleSpalte col_Grundschule = add("Grundschule", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: Filter II");

	/** Die Definition der Tabellenspalte Tabelle */
	public SchemaTabelleSpalte col_Tabelle = add("Tabelle", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Schildintern Tabelle: Tabelle für Filter II");

	/** Die Definition der Tabellenspalte DBFeld */
	public SchemaTabelleSpalte col_DBFeld = add("DBFeld", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Datenbankfeld für Filter II");

	/** Die Definition der Tabellenspalte Typ */
	public SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: Feldty für Filter II");

	/** Die Definition der Tabellenspalte Control */
	public SchemaTabelleSpalte col_Control = add("Control", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Schildintern Tabelle: Filter II");

	/** Die Definition der Tabellenspalte WerteAnzeige */
	public SchemaTabelleSpalte col_WerteAnzeige = add("WerteAnzeige", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: Filter II");

	/** Die Definition der Tabellenspalte WerteSQL */
	public SchemaTabelleSpalte col_WerteSQL = add("WerteSQL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: Filter II");

	/** Die Definition der Tabellenspalte LookupInfo */
	public SchemaTabelleSpalte col_LookupInfo = add("LookupInfo", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Filter II");

	/** Die Definition der Tabellenspalte OperatorenAnzeige */
	public SchemaTabelleSpalte col_OperatorenAnzeige = add("OperatorenAnzeige", SchemaDatentypen.VARCHAR, false).setDatenlaenge(150)
		.setJavaComment("Schildintern Tabelle: Filter II");

	/** Die Definition der Tabellenspalte OperatorenSQL */
	public SchemaTabelleSpalte col_OperatorenSQL = add("OperatorenSQL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Schildintern Tabelle: Filter II");

	/** Die Definition der Tabellenspalte Zusatzbedingung */
	public SchemaTabelleSpalte col_Zusatzbedingung = add("Zusatzbedingung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Schildintern Tabelle: Filter II");

	/** Die Definition der Tabellenspalte ZusatzTabellen */
	public SchemaTabelleSpalte col_ZusatzTabellen = add("ZusatzTabellen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Filter II");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_SpezialFilterFelder.
	 */
	public Tabelle_Schildintern_SpezialFilterFelder() {
		super("Schildintern_SpezialFilterFelder", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternSpezialFilterFelder");
		setJavaComment("Liefert die Felder, die im Filter II verfügbar sind");
	}

}
