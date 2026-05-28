package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Planungsabschnitte_Schueler.
 */
public class Tabelle_UV_Planungsabschnitte_Schueler extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public final SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des Schülers als Fremdschlüssel auf die Tabelle Schueler");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public final SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID des Jahrgangs der dem Schüler zugeordnet wird");

	/** Die Definition der Tabellenspalte Klasse_ID */
	public final SchemaTabelleSpalte col_Klasse_ID_DEPRECATED = add("Klasse_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Klassen_ID des Schülers für den Planungsabschnitt")
			.setVeraltet(SchemaRevisionen.REV_65);

	/** Die Definition der Tabellenspalte Klasse_ID */
	public final SchemaTabelleSpalte col_Klasse_ID = add("Klasse_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Klassen_ID des Schülers für den Planungsabschnitt")
			.setRevision(SchemaRevisionen.REV_66);


	/** Die Definition des Fremdschlüssels auf UV_Planungsabschnitte */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteSchueler_UVPlanungsabschnitte_FK = addForeignKey(
			"UVPlanungsabschnitteSchueler_UVPlanungsabschnitte_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Planungsabschnitte.col_ID)
	);

	/** Die Definition des Fremdschlüssels auf Schueler */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteSchueler_Schueler_FK = addForeignKey(
			"UVPlanungsabschnitteSchueler_Schueler",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	);

	/** Die Definition des Fremdschlüssels UVPlanungsabschnitteSchueler_EigeneSchuleJahrgaenge_FK */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteSchueler_EigeneSchuleJahrgaenge_FK = addForeignKey(
			"UVPlanungsabschnitteSchueler_EigeneSchuleJahrgaenge_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Jahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID)
	);

	/** Die Definition des Fremdschlüssels UVPlanungsabschnitteSchueler_UVKlassen_FK DEPRECATED wegen Bug bei Erstellung des FKs (Spaltenreihenfolge #4a702e6e) */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteSchueler_UVKlassen_FK_Deprecated_Revision_49 = addForeignKey(
			"UVPlanungsabschnitteSchueler_UVKlassen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Klasse_ID, Schema.tab_UV_Klassen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Klassen.col_Planungsabschnitt_ID)
	).setVeraltet(SchemaRevisionen.REV_65);

	/** Die Definition des Fremdschlüssels UVPlanungsabschnitteSchueler_UVKlassen_FK */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteSchueler_UVKlassen_FK = addForeignKey(
			"UVPlanungsabschnitteSchueler_UVKlassen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Klasse_ID, Schema.tab_UV_Klassen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Klassen.col_Planungsabschnitt_ID)
	).setRevision(SchemaRevisionen.REV_66);

	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Planungsabschnitte_Schueler.
	 */
	public Tabelle_UV_Planungsabschnitte_Schueler() {
		super("UV_Planungsabschnitte_Schueler", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvPlanungsabschnittSchueler");
		setJavaComment("Tabelle für die konkreten Zuordnungen von Schülern zu UV-Planungsabschnitten");
	}

}
