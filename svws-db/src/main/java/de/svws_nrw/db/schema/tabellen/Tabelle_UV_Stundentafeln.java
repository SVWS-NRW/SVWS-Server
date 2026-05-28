package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Stundentafeln.
 */
public class Tabelle_UV_Stundentafeln extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Stundentafel (generiert)");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public final SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID zur Kennzeichnung des Jahrgangs-Datensatzes");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public final SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(64)
			.setNotNull()
			.setJavaComment("Bezeichnung der Stundentafel")
			.setRevision(SchemaRevisionen.REV_50);

	/** Die Definition der Tabellenspalte GueltigAb_Deprecated_Revision_49 */
	public final SchemaTabelleSpalte col_GueltigAb = add("GueltigAb", SchemaDatentypen.DATE, false)
			.setDefault("1899-01-01")
			.setNotNull()
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, ab dem die Stundentafel gültig ist")
			.setVeraltet(SchemaRevisionen.REV_49);

	/** Die Definition der Tabellenspalte GueltigVon */
	public final SchemaTabelleSpalte col_GueltigVon = add("GueltigVon", SchemaDatentypen.DATE, false)
			.setDefault("1899-01-01")
			.setNotNull()
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, ab dem die Stundentafel gültig ist")
			.setRevision(SchemaRevisionen.REV_50);

	/** Die Definition der Tabellenspalte GueltigBis */
	public final SchemaTabelleSpalte col_GueltigBis = add("GueltigBis", SchemaDatentypen.DATE, false)
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, bis wann die Stundentafel gültig ist. Ist kein Datum gesetzt, gilt die Stundentafel unbegrenzt weiter");

	/** Die Definition der Tabellenspalte Beschreibung */
	public final SchemaTabelleSpalte col_Beschreibung_DEPRECATED = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
			.setNotNull()
			.setJavaComment("Beschreibung oder Kommentar zur Stundentafel")
			.setVeraltet(SchemaRevisionen.REV_49);

	/** Die Definition der Tabellenspalte Beschreibung */
	public final SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
			.setJavaComment("Beschreibung oder Kommentar zur Stundentafel")
			.setRevision(SchemaRevisionen.REV_50);

	/** Die Definition des Fremdschlüssels auf EigeneSchule_Jahrgaenge */
	public final SchemaTabelleFremdschluessel fk_UVStundentafeln_EigeneSchuleJahrgaenge_FK = addForeignKey(
			"UVStundentafeln_EigeneSchuleJahrgaenge_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Jahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID)
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Stundentafeln.
	 */
	public Tabelle_UV_Stundentafeln() {
		super("UV_Stundentafeln", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvStundentafel");
		setJavaComment("Tabelle für die konkreten Stundentafeln der UV");
	}

}
