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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge.
 */
public class Tabelle_UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

	/** Die Definition der Tabellenspalte Zeitraster_ID */
	public final SchemaTabelleSpalte col_Zeitraster_ID = add("Zeitraster_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Zeitrasters des Planungsabschnitts");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public final SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des zugeordneten Jahrgangs");



	/** Die Definition des Fremdschlüssels auf UV_Planungsabschnitte_Zeitraster */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteZeitrJahrg_UVPlanungsabschnitteZeitr_FK = addForeignKey(
			"UVPlanungsabschnitteZeitrJahrg_UVPlanungsabschnitteZeitr_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Planungsabschnitte_Zeitraster.col_Planungsabschnitt_ID),
			new Pair<>(col_Zeitraster_ID, Schema.tab_UV_Planungsabschnitte_Zeitraster.col_Zeitraster_ID)
	);

	/** Fremdschlüssel auf die Tabelle EigeneSchule_Jahrgaenge */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteZeitrJahrg_EigeneSchuleJahrgaenge_FK = addForeignKey(
			"UVPlanungsabschnitteZeitrJahrg_EigeneSchuleJahrgaenge_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Jahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID)
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge.
	 */
	public Tabelle_UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge() {
		super("UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang");
		setJavaComment("Tabelle für die Zuordnung von Jahrgängen zu Zeitrastern eines Planungsabschnitts der Unterrichtsverteilung (UV)");
	}

}
