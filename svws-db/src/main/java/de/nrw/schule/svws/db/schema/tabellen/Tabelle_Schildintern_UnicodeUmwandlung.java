package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_UnicodeUmwandlung.
 */
public class Tabelle_Schildintern_UnicodeUmwandlung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: ID für den Primärschlüssel der Tabelle UnicodeUmwandlung");

	/** Die Definition der Tabellenspalte Unicodezeichen */
	public SchemaTabelleSpalte col_Unicodezeichen = add("Unicodezeichen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schildintern Tabelle: Unicodezeichen das umgewandelt werden muss");

	/** Die Definition der Tabellenspalte Ersatzzeichen */
	public SchemaTabelleSpalte col_Ersatzzeichen = add("Ersatzzeichen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schildintern Tabelle: Erstazzeichen für das Unicodezeichen");

	/** Die Definition der Tabellenspalte DecimalZeichen */
	public SchemaTabelleSpalte col_DecimalZeichen = add("DecimalZeichen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: Unicodezeichen in Dezimaldarstellung");

	/** Die Definition der Tabellenspalte DecimalErsatzzeichen */
	public SchemaTabelleSpalte col_DecimalErsatzzeichen = add("DecimalErsatzzeichen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: Ersatzzeichen in Dezimaldarstellung (bei zwei Zeichen mit + getrennt)");

	/** Die Definition der Tabellenspalte Hexzeichen */
	public SchemaTabelleSpalte col_Hexzeichen = add("Hexzeichen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Hexdarstellung des Unicodezeichen das gewandelt werden muss");

	/** Die Definition der Tabellenspalte HexErsatzzeichen */
	public SchemaTabelleSpalte col_HexErsatzzeichen = add("HexErsatzzeichen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Hexdarstellung des Ersatzzeichens das gewandelt werden muss (bei zwei Zeichen mit + getrennt)");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_UnicodeUmwandlung.
	 */
	public Tabelle_Schildintern_UnicodeUmwandlung() {
		super("Schildintern_UnicodeUmwandlung", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternUnicodeUmwandllung");
		setJavaComment("Liefert eine Tabelle mit der Unicodezeichen für Emailkonforme Namen umgewandelt werden können");
	}

}
