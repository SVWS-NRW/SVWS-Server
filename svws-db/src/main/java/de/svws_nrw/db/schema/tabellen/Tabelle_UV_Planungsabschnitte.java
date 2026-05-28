package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Planungsabschnitte.
 */
public class Tabelle_UV_Planungsabschnitte extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Planungsabschnitts (generiert)");

	/** Die Definition der Tabellenspalte Schuljahr */
	public final SchemaTabelleSpalte col_Schuljahr = add("Schuljahr", SchemaDatentypen.INT, false)
			.setNotNull()
			.setJavaComment("Schuljahr des Planungsabschnitts (z.B. 2012 für 2012/13)");

	/** Die Definition der Tabellenspalte Aktiv */
	public final SchemaTabelleSpalte col_Aktiv = add("Aktiv", SchemaDatentypen.BOOLEAN, false)
			.setDefault("0")
			.setNotNull()
			.setJavaComment("Gibt an, ob der Planungsabschnitt aktiv ist. An einem Stichtag darf nur ein Planungsabschnitt aktiv sein");

	/** Die Definition der Tabellenspalte GueltigAb_Deprecated_Revision_49 */
	public final SchemaTabelleSpalte col_GueltigAb = add("GueltigAb", SchemaDatentypen.DATE, false)
			.setDefault("1899-01-01")
			.setNotNull()
			.setConverter(DatumConverter.class)
			.setJavaComment("Datum, ab dem der Planungsabschnitt gültig ist")
			.setVeraltet(SchemaRevisionen.REV_49);

	/** Die Definition der Tabellenspalte GueltigVon */
	public final SchemaTabelleSpalte col_GueltigVon = add("GueltigVon", SchemaDatentypen.DATE, false)
			.setDefault("1899-01-01")
			.setNotNull()
			.setConverter(DatumConverter.class)
			.setJavaComment("Datum, ab dem der Planungsabschnitt gültig ist")
			.setRevision(SchemaRevisionen.REV_50);


	/** Die Definition der Tabellenspalte GueltigBis */
	public final SchemaTabelleSpalte col_GueltigBis = add("GueltigBis", SchemaDatentypen.DATE, false)
			.setConverter(DatumConverter.class)
			.setJavaComment("Datum, bis zu dem der Planungsabschnitt gültig ist. Wenn null gesetzt ist, gilt der Planungsabschnitt unbegrenzt weiter");

	/** Die Definition der Tabellenspalte Beschreibung */
	public final SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
			.setJavaComment("Optionale Beschreibung oder Kommentar zum Planungsabschnitt");


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Planungsabschnitte.
	 */
	public Tabelle_UV_Planungsabschnitte() {
		super("UV_Planungsabschnitte", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvPlanungsabschnitt");
		setJavaComment("Tabelle für die Planungsabschnitte der UV");
	}

}
