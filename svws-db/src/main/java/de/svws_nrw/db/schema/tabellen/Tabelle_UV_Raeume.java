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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Raeume.
 */
public class Tabelle_UV_Raeume extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des UV-Raums (generiert, planungsspezifisch)");

	/** Die Definition der Tabellenspalte Kuerzel */
	public final SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
			.setNotNull()
			.setJavaComment("Das Kürzel des Raums");

	/** Die Definition der Tabellenspalte Raumgruppe_ID */
	public final SchemaTabelleSpalte col_Raumgruppe_ID = add("Raumgruppe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Raumgruppe_ID, falls der Raum zu einer Gruppe gehört")
			.setRevision(SchemaRevisionen.REV_66);

	/** Die Definition der Tabellenspalte Beschreibung */
	public final SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
			.setJavaComment("Gegebenenfalls eine ausführlichere Beschreibung des Raumes")
			.setRevision(SchemaRevisionen.REV_66);

	/** Die Definition der Tabellenspalte Groesse */
	public final SchemaTabelleSpalte col_Groesse = add("Groesse", SchemaDatentypen.INT, false)
			.setDefault("30")
			.setNotNull()
			.setJavaComment("Die Größe des Raumes, d.h. wie viele Schüler hier max. Platz haben")
			.setRevision(SchemaRevisionen.REV_66);

	/** Die Definition der Tabellenspalte GueltigAb_Deprecated_Revision_49 */
	public final SchemaTabelleSpalte col_GueltigAb = add("GueltigAb", SchemaDatentypen.DATE, false)
			.setDefault("1899-01-01")
			.setNotNull()
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, ab dem der Raum gültig ist")
			.setVeraltet(SchemaRevisionen.REV_49);

	/** Die Definition der Tabellenspalte GueltigVon */
	public final SchemaTabelleSpalte col_GueltigVon = add("GueltigVon", SchemaDatentypen.DATE, false)
			.setDefault("1899-01-01")
			.setNotNull()
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, ab dem der Raum gültig ist")
			.setRevision(SchemaRevisionen.REV_50);

	/** Die Definition der Tabellenspalte GueltigBis */
	public final SchemaTabelleSpalte col_GueltigBis = add("GueltigBis", SchemaDatentypen.DATE, false)
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, bis wann der Raum gültig ist. Ist kein Datum gesetzt, gilt der Raum unbegrenzt weiter.");


	/** Die Definition des Fremdschlüssels auf UvRaumgruppen */
	public final SchemaTabelleFremdschluessel fk_UVRaeume_Raumgruppen_FK = addForeignKey(
			"UVRaeume_Raumgruppen",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Raumgruppe_ID, Schema.tab_UV_Raumgruppen.col_ID)
	).setRevision(SchemaRevisionen.REV_66);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Raeume.
	 */
	public Tabelle_UV_Raeume() {
		super("UV_Raeume", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvRaum");
		setJavaComment("Tabelle für die konkreten Räume der UV");
	}

}
