package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Raumgruppen.
 */
public class Tabelle_UV_Raumgruppen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der UV-Raumgruppe (generiert, planungsspezifisch)");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public final SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
			.setNotNull()
			.setJavaComment("Die Bezeichnung der Raumgruppe");

	/** Die Definition der Tabellenspalte GueltigVon */
	public final SchemaTabelleSpalte col_GueltigVon = add("GueltigVon", SchemaDatentypen.DATE, false)
			.setDefault("1899-01-01")
			.setNotNull()
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, ab dem die Raumgruppe gültig ist");

	/** Die Definition der Tabellenspalte GueltigBis */
	public final SchemaTabelleSpalte col_GueltigBis = add("GueltigBis", SchemaDatentypen.DATE, false)
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, bis wann die Raumgruppe gültig ist. Ist kein Datum gesetzt, gilt die Raumgruppe unbegrenzt weiter.");

	/** Die Definition der Tabellenspalte Beschreibung */
	public final SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
			.setJavaComment("Optionale Beschreibung oder Kommentar zur Raumgruppe");


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Raumgruppen.
	 */
	public Tabelle_UV_Raumgruppen() {
		super("UV_Raumgruppen", SchemaRevisionen.REV_66);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvRaumgruppe");
		setJavaComment("Tabelle für die konkreten Raumgruppen der UV");
	}

}
