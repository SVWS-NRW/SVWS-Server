package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Lerngruppen.
 */
public class Tabelle_UV_Lerngruppen extends SchemaTabelle {

	/** Eindeutige ID der Lerngruppe im Planungsabschnitt (automatisch generiert) */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Eindeutige ID der Lerngruppe im Planungsabschnitt (automatisch generiert)");

	/** Fremdschlüssel auf die Klasse (Tabelle UV_Klassen) */
	public final SchemaTabelleSpalte col_Klasse_ID = add("Klasse_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Fremdschlüssel auf die Klasse (Tabelle UV_Klassen)");

	/** Fremdschlüssel auf das Fach (Tabelle UV_Faecher) */
	public final SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Fremdschlüssel auf das Fach (Tabelle UV_Faecher)");

	/** Fremdschlüssel auf den Kurs (Tabelle UV_Kurse) */
	public final SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Fremdschlüssel auf den Kurs (Tabelle UV_Kurse)");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

	/** Die Definition der Tabellenspalte Wochenstunden */
	public final SchemaTabelleSpalte col_Wochenstunden = add("Wochenstunden", SchemaDatentypen.FLOAT, false)
			.setNotNull()
			.setJavaComment("Die Anzahl der vorgesehenen Wochenstunden für die Lerngruppe");

	/** Die Definition der Tabellenspalte WochenstundenUnterrichtet */
	public final SchemaTabelleSpalte col_WochenstundenUnterrichtet = add("WochenstundenUnterrichtet", SchemaDatentypen.FLOAT, false)
			.setNotNull()
			.setJavaComment("Die Anzahl der Wochenstunden, die die Lerngruppe tatsächlich unterrichtet wurde");



	/** Die Definition der Tabellenspalte KoopSchulNr */
	public final SchemaTabelleSpalte col_KoopSchulNr = add("KoopSchulNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
			.setJavaComment("Schulnummer von Koopschule, null falls kein Koop");

	/** Die Definition der Tabellenspalte KoopAnzahlExterne */
	public final SchemaTabelleSpalte col_KoopAnzahlExterne = add("KoopAnzahlExterne", SchemaDatentypen.INT, false)
			.setNotNull()
			.setJavaComment("Die Anzahl der externen Schüler von Koop-Schulen");



	/** Die Definition des Fremdschlüssels auf UV_Planungsabschnitte */
	public final SchemaTabelleFremdschluessel fk_UVLerngruppen_UVPlanungsabschnitte_FK = addForeignKey(
			"UVLerngruppen_UVPlanungsabschnitte_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Planungsabschnitte.col_ID)
	);

	/** Fremdschlüssel auf die Tabelle UV_Klassen */
	public final SchemaTabelleFremdschluessel fk_UVLerngruppen_UVKlassen_FK = addForeignKey(
			"UVLerngruppen_UVKlassen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Klasse_ID, Schema.tab_UV_Klassen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Klassen.col_Planungsabschnitt_ID)
	);

	/** Fremdschlüssel auf die Tabelle UV_Faecher DEPRECATED wegen Bug bei Erstellung des FKs (Spaltenreihenfolge #4a702e6e) */
	public final SchemaTabelleFremdschluessel fk_UVLerngruppen_UVFaecher_FK_Deprecated_Revision_49 = addForeignKey(
			"UVLerngruppen_UVFaecher_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Fach_ID, Schema.tab_UV_Faecher.col_ID)
	).setVeraltet(SchemaRevisionen.REV_49);

	/** Fremdschlüssel auf die Tabelle UV_Faecher */
	public final SchemaTabelleFremdschluessel fk_UVLerngruppen_UVStundentafelnFaecher_FK_Deprecated_Revision_53 = addForeignKey(
			"UVLerngruppen_UVFaecher_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Fach_ID, Schema.tab_UV_Stundentafeln_Faecher.col_ID)
	).setRevision(SchemaRevisionen.REV_50).setVeraltet(SchemaRevisionen.REV_65);

	/** Fremdschlüssel auf die Tabelle UV_Faecher */
	public final SchemaTabelleFremdschluessel fk_UVLerngruppen_UVFaecher_FK = addForeignKey(
			"UVLerngruppen_UVFaecher_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Fach_ID, Schema.tab_UV_Faecher.col_ID)
	).setRevision(SchemaRevisionen.REV_66);

	/** Fremdschlüssel auf die Tabelle UV_Kurse */
	public final SchemaTabelleFremdschluessel fk_UVLerngruppen_UVKurse_FK = addForeignKey(
			"UVLerngruppen_UVKurse_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Kurs_ID, Schema.tab_UV_Kurse.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Kurse.col_Planungsabschnitt_ID)
	);

	/** Unique-Index für die Kombination ID und Planungsabschnitt_ID, benötigt durch 2-teiligen FK */
	public final SchemaTabelleUniqueIndex unique_UV_Lerngruppen_UC1 = addUniqueIndex("UV_Lerngruppen_UC1",
			col_ID, col_Planungsabschnitt_ID
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Lerngruppen.
	 */
	public Tabelle_UV_Lerngruppen() {
		super("UV_Lerngruppen", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvLerngruppe");
		setJavaComment("Tabelle für die Lerngruppen eines Planungsabschnitts der Unterrichtsverteilung (UV)");
	}

}
