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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Lerngruppen_Lehrer.
 */
public class Tabelle_UV_Lerngruppen_Lehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Lerngruppen-Lehrers (generiert, planungsspezifisch)");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

	/** Die Definition der Tabellenspalte Lerngruppe_ID */
	public final SchemaTabelleSpalte col_Lerngruppe_ID = add("Lerngruppe_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID der UV_Lerngruppe");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public final SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID des Lehrers, welcher der Lerngruppe zugeordnet ist");

	/** Die Definition der Tabellenspalte Reihenfolge */
	public final SchemaTabelleSpalte col_Reihenfolge = add("Reihenfolge", SchemaDatentypen.INT, false)
			.setDefault("1")
			.setNotNull()
			.setJavaComment("Eine Reihenfolge für die Lehrer, z.B. zur Unterscheidung der eigentlichen Hauptlehrkraft (z.B. 1) und einer Zusatzkraft (z.B. 2)");

	/** Die Definition der Tabellenspalte Wochenstunden */
	public final SchemaTabelleSpalte col_Wochenstunden = add("Wochenstunden", SchemaDatentypen.FLOAT, false)
			.setNotNull()
			.setJavaComment("Die Anzahl der Wochenstunden, für die der Lehrer in der Lerngruppe eingesetzt wird");

	/** Die Definition der Tabellenspalte WochenstundenAngerechnet */
	public final SchemaTabelleSpalte col_WochenstundenAngerechnet = add("WochenstundenAngerechnet", SchemaDatentypen.FLOAT, false)
			.setNotNull()
			.setJavaComment("Die Anzahl der Wochenstunden, die deputatswirksam sind");



	/** Die Definition des Fremdschlüssels UV_Lerngruppen_Lehrer_Lerngruppe_FK DEPRECATED wegen Bug bei Erstellung des FKs (Spaltenreihenfolge #4a702e6e) */
	public final SchemaTabelleFremdschluessel fk_UV_Lerngruppen_Lehrer_Lerngruppe_FK_Deprecated_Revision_49 = addForeignKey(
			"UV_Lerngruppen_Lehrer_Lerngruppe_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lerngruppe_ID, Schema.tab_UV_Lerngruppen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Lerngruppen.col_Planungsabschnitt_ID)
	).setVeraltet(SchemaRevisionen.REV_49);

	/** Die Definition des Fremdschlüssels UV_Lerngruppen_Lehrer_Lerngruppe_FK */
	public final SchemaTabelleFremdschluessel fk_UV_Lerngruppen_Lehrer_Lerngruppe_FK = addForeignKey(
			"UV_Lerngruppen_Lehrer_Lerngruppe_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lerngruppe_ID, Schema.tab_UV_Lerngruppen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Lerngruppen.col_Planungsabschnitt_ID)
	).setRevision(SchemaRevisionen.REV_50);

	/** Die Definition des Fremdschlüssels UV_LerngruppenLehrer_PlanungsabschnitteLehrer_FK */
	public final SchemaTabelleFremdschluessel fk_UVLerngruppenLehrer_UVPlanungsabschnitteLehrer_FK = addForeignKey(
			"UVLerngruppenLehrer_UVPlanungsabschnitteLehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Planungsabschnitte_Lehrer.col_Planungsabschnitt_ID),
			new Pair<>(col_Lehrer_ID, Schema.tab_UV_Planungsabschnitte_Lehrer.col_Lehrer_ID)
	);

	/** Die Definition des Unique-Index UV_Lerngruppen_Lehrer_UC1 */
	public final SchemaTabelleUniqueIndex unique_UV_Lerngruppen_Lehrer_UC1 = addUniqueIndex("UV_Lerngruppen_Lehrer_UC1",
			col_Lerngruppe_ID,
			col_Lehrer_ID
	);

	/** Die Definition des Unique-Index UV_Lerngruppen_Lehrer_UC2 */
	public final SchemaTabelleUniqueIndex unique_UV_Lerngruppen_Lehrer_UC2 = addUniqueIndex("UV_Lerngruppen_Lehrer_UC2",
			col_Lerngruppe_ID,
			col_Reihenfolge
	).setVeraltet(SchemaRevisionen.REV_65);

	/** Unique-Index für die Kombination ID und Planungsabschnitt_ID, benötigt durch 2-teiligen FK */
	public final SchemaTabelleUniqueIndex unique_UV_Lerngruppen_Lehrer_UC3 = addUniqueIndex("UV_Lerngruppen_Lehrer_UC3",
			col_ID,
			col_Planungsabschnitt_ID
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Lerngruppen_Lehrer.
	 */
	public Tabelle_UV_Lerngruppen_Lehrer() {
		super("UV_Lerngruppen_Lehrer", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvLerngruppenLehrer");
		setJavaComment("Tabelle für die Lehrer, welche einer Lerngruppe der UV zugeordnet sind");
	}

}
