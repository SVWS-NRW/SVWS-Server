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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Planungsabschnitte_Zeitraster.
 */
public class Tabelle_UV_Planungsabschnitte_Zeitraster extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

	/** Die Definition der Tabellenspalte Zeitraster_ID */
	public final SchemaTabelleSpalte col_Zeitraster_ID = add("Zeitraster_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Zeitrasters des Planungsabschnitts");

	/** Die Definition des Fremdschlüssels auf UV_Planungsabschnitte */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteZeitraster_UVPlanungsabschnitte_FK = addForeignKey(
			"UVPlanungsabschnitteZeitraster_UVPlanungsabschnitte_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Planungsabschnitte.col_ID)
	);

	/** Die Definition des Fremdschlüssels Zeitraster_FK */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteZeitraster_UVZeitraster_FK = addForeignKey(
			"UVPlanungsabschnitteZeitraster_UVZeitraster_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Zeitraster_ID, Schema.tab_UV_Zeitraster.col_ID));

	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Planungsabschnitte_Zeitraster.
	 */
	public Tabelle_UV_Planungsabschnitte_Zeitraster() {
		super("UV_Planungsabschnitte_Zeitraster", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvPlanungsabschnittZeitraster");
		setJavaComment("Tabelle für die Zuordnung von Zeitrastern zu einem Planungsabschnitt der Unterrichtsverteilung (UV)");
	}

}
