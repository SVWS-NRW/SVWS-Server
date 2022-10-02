package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Nationalitaeten.
 */
public class Tabelle_Nationalitaeten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID der Nationalität");

	/** Die Definition der Tabellenspalte Iso3Code */
	public SchemaTabelleSpalte col_Iso3Code = add("Iso3Code", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der dreistellige Länder-Code nach ISO 3166");

	/** Die Definition der Tabellenspalte Iso2Code */
	public SchemaTabelleSpalte col_Iso2Code = add("Iso2Code", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Der zweistellige Länder-Code nach ISO 3166");

	/** Die Definition der Tabellenspalte Iso3Numerisch */
	public SchemaTabelleSpalte col_Iso3Numerisch = add("Iso3Numerisch", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Der dreistellige numerische Länder-Code nach ISO 3166");

	/** Die Definition der Tabellenspalte DEStatisCode */
	public SchemaTabelleSpalte col_DEStatisCode = add("DEStatisCode", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der dreistellige Länder-Code des statistischen Bundesamtes (DESTATIS)");

	/** Die Definition der Tabellenspalte BezeichnungSuche */
	public SchemaTabelleSpalte col_BezeichnungSuche = add("BezeichnungSuche", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Eine für die Suche geeignete Bezeichnung");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Eine kurze Bezeichnung");

	/** Die Definition der Tabellenspalte BezeichnungLang */
	public SchemaTabelleSpalte col_BezeichnungLang = add("BezeichnungLang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Eine lange Bezeichnung");

	/** Die Definition der Tabellenspalte Staatsangehoerigkeit */
	public SchemaTabelleSpalte col_Staatsangehoerigkeit = add("Staatsangehoerigkeit", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die Bezeichnung der Staatsangehörigkeit");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Nationalitaeten.
	 */
	public Tabelle_Nationalitaeten() {
		super("Nationalitaeten", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schule");
		setJavaClassName("DTONationalitaeten");
		setJavaComment("Katalog der Nationalitäten mit ISO- und DESTATIS-Codes");
	}

}
