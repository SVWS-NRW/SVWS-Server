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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Klassen_Lehrer.
 */
public class Tabelle_UV_Klassen_Lehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Klassen-Lehrers (generiert, planungsspezifisch)");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

	/** Die Definition der Tabellenspalte Klasse_ID */
	public final SchemaTabelleSpalte col_Klasse_ID = add("Klasse_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID der UV_Klasse, welcher der Klassenlehrer zugeordnet ist");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public final SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID des Lehrers, welcher der Klasse als Klassenlehrer zugeordnet ist");

	/** Die Definition der Tabellenspalte Reihenfolge */
	public final SchemaTabelleSpalte col_Reihenfolge = add("Reihenfolge", SchemaDatentypen.INT, false)
			.setDefault("1")
			.setNotNull()
			.setJavaComment("Eine Reihenfolge für die Klassenlehrer, z.B. zur Unterscheidung von Klassenleitung (1) und stellvertretender Klassenleitung (2)");


	/** Die Definition des Fremdschlüssels UV_Klassen_Lehrer_Klasse_FK */
	public final SchemaTabelleFremdschluessel fk_UV_Klassen_Lehrer_Klasse_FK = addForeignKey(
			"UV_Klassen_Lehrer_Klasse_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Klasse_ID, Schema.tab_UV_Klassen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Klassen.col_Planungsabschnitt_ID)
	);

	/** Die Definition des Fremdschlüssels UV_Klassen_Lehrer_PlanungsabschnitteLehrer_FK */
	public final SchemaTabelleFremdschluessel fk_UV_Klassen_Lehrer_PlanungsabschnitteLehrer_FK = addForeignKey(
			"UV_Klassen_Lehrer_PlanungsabschnitteLehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Planungsabschnitte_Lehrer.col_Planungsabschnitt_ID),
			new Pair<>(col_Lehrer_ID, Schema.tab_UV_Planungsabschnitte_Lehrer.col_Lehrer_ID)
	);

	/** Die Definition des Unique-Index UV_Klassen_Lehrer_UC1 - ein Lehrer darf einer Klasse nur einmal zugewiesen sein */
	public final SchemaTabelleUniqueIndex unique_UV_Klassen_Lehrer_UC1 = addUniqueIndex("UV_Klassen_Lehrer_UC1",
			col_Klasse_ID,
			col_Lehrer_ID
	);

	/** Unique-Index für die Kombination ID und Planungsabschnitt_ID, benötigt durch 2-teiligen FK */
	public final SchemaTabelleUniqueIndex unique_UV_Klassen_Lehrer_UC2 = addUniqueIndex("UV_Klassen_Lehrer_UC2",
			col_ID,
			col_Planungsabschnitt_ID
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Klassen_Lehrer.
	 */
	public Tabelle_UV_Klassen_Lehrer() {
		super("UV_Klassen_Lehrer", SchemaRevisionen.REV_66);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvKlassenLehrer");
		setJavaComment("Tabelle für die Klassenlehrer, welche einer Klasse der UV zugeordnet sind");
	}

}
