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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Planungsabschnitte_Lehrer.
 */
public class Tabelle_UV_Planungsabschnitte_Lehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public final SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des UV-Lehrers als Fremdschlüssel auf die Tabelle UV_Lehrer");


	/** Die Definition des Fremdschlüssels auf UV_Planungsabschnitte */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteLehrer_UVPlanungsabschnitte_FK = addForeignKey(
			"UVPlanungsabschnitteLehrer_UVPlanungsabschnitte_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Planungsabschnitte.col_ID)
	);

	/** Die Definition des Fremdschlüssels auf UV_Lehrer_Deprecated_Revision_52 */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteLehrer_UVLehrer_FK_Deprecated_Revision_52 = addForeignKey(
			"UVPlanungsabschnitteLehrer_UVLehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_UV_Lehrer.col_ID)
	).setVeraltet(SchemaRevisionen.REV_65);

	/** Die Definition des Fremdschlüssels auf UV_Lehrer */
	public final SchemaTabelleFremdschluessel fk_UVPlanungsabschnitteLehrer_UVLehrer_FK = addForeignKey(
			"UVPlanungsabschnitteLehrer_UVLehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Lehrer_ID, Schema.tab_UV_Lehrer.col_ID)
	).setRevision(SchemaRevisionen.REV_66);

	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Planungsabschnitte_Lehrer.
	 */
	public Tabelle_UV_Planungsabschnitte_Lehrer() {
		super("UV_Planungsabschnitte_Lehrer", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvPlanungsabschnittLehrer");
		setJavaComment("Tabelle für die konkreten Zuordnungen von Lehrern zu UV-Planungsabschnitten");
	}

}
