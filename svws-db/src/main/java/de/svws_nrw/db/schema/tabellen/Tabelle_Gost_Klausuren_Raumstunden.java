package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Raumstunden.
 */
public class Tabelle_Gost_Klausuren_Raumstunden extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Raumstunde (generiert)");

	/** Die Definition der Tabellenspalte Klausurraum_ID */
	public SchemaTabelleSpalte col_Klausurraum_ID = add("Klausurraum_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Klausurraums");

	/** Die Definition der Tabellenspalte Zeitraster_ID */
	public SchemaTabelleSpalte col_Zeitraster_ID = add("Zeitraster_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Zeitrasters");

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Raumstunden_Klausurraum_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Raumstunden_Klausurraum_ID_FK = addForeignKey(
			"Gost_Klausuren_Raumstunden_Klausurraum_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Klausurraum_ID, Schema.tab_Gost_Klausuren_Raeume.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Raumstunden_Zeitraster_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Raumstunden_Zeitraster_ID_FK = addForeignKey(
			"Gost_Klausuren_Raumstunden_Zeitraster_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Zeitraster_ID, Schema.tab_Stundenplan_Zeitraster.col_ID)
		);

	/** Die Definition des Unique-Index Gost_Klausuren_Raumstunden_UC1 */
	public SchemaTabelleUniqueIndex unique_Gost_Klausuren_Raumstunden_UC1 = addUniqueIndex("unique_Gost_Klausuren_Raumstunden_UC1",
			col_Klausurraum_ID, col_Zeitraster_ID
		);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Raumstunden_IDX_Klausurraum_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_Raumstunden_IDX_Klausurraum_ID = addIndex("Gost_Klausuren_Raumstunden_IDX_Klausurraum_ID",
		col_Klausurraum_ID
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Raumstunden.
	 */
	public Tabelle_Gost_Klausuren_Raumstunden() {
		super("Gost_Klausuren_Raumstunden", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenRaumstunden");
		setJavaComment("Tabelle für die Definition von Klausurraumstunden");
	}

}
