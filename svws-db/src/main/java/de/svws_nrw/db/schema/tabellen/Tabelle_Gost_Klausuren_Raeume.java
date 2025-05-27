package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Raeume.
 */
public class Tabelle_Gost_Klausuren_Raeume extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Klausurraums (generiert)");

	/** Die Definition der Tabellenspalte Termin_ID */
	public SchemaTabelleSpalte col_Termin_ID = add("Termin_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID des Termins");

	/** Die Definition der Tabellenspalte Stundenplan_Raum_ID */
	public SchemaTabelleSpalte col_Stundenplan_Raum_ID = add("Stundenplan_Raum_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("ID des Raums aus der Tabelle Stundenplan_Raeume");

	/** Die Definition der Tabellenspalte Stundenplan_Raum_Kuerzel */
	public SchemaTabelleSpalte col_Stundenplan_Raum_Kuerzel = add("Stundenplan_Raum_Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
			.setJavaComment("Das Kürzel des Stundenplan_Raums, falls keine Stundenplan_Raum_ID gesetzt ist")
			.setRevision(SchemaRevisionen.REV_40);


	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
			.setJavaComment("Text für Bemerkungen zum Klausurraum");

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Raeume_Stundenplan_Raume_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Raeume_Stundenplan_Raume_ID_FK = addForeignKey(
			"Gost_Klausuren_Raeume_Stundenplan_Raum_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Stundenplan_Raum_ID, Schema.tab_Stundenplan_Raeume.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Raeume_Termin_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Raeume_Termin_ID_FK = addForeignKey(
			"Gost_Klausuren_Raeume_Termin_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Termin_ID, Schema.tab_Gost_Klausuren_Termine.col_ID)
	);

	/** Die Definition des Unique-Index Gost_Klausuren_Raume_UC1 */
	public SchemaTabelleUniqueIndex unique_Gost_Klausuren_Raume_UC1 = addUniqueIndex("Gost_Klausuren_Raume_UC1",
			col_Termin_ID, col_Stundenplan_Raum_ID
	);

	/** Die Definition des Unique-Index Gost_Klausuren_Raume_UC2 */
	public SchemaTabelleUniqueIndex unique_Gost_Klausuren_Raume_UC2 = addUniqueIndex("Gost_Klausuren_Raume_UC2",
			col_Termin_ID, col_Stundenplan_Raum_Kuerzel
	).setRevision(SchemaRevisionen.REV_40);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Raume_IDX_Termin_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_Raume_IDX_Termin_ID = addIndex("Gost_Klausuren_Raume_IDX_Termin_ID",
			col_Termin_ID
	);

	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Raeume.
	 */
	public Tabelle_Gost_Klausuren_Raeume() {
		super("Gost_Klausuren_Raeume", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenRaeume");
		setJavaComment("Tabelle für die Definition von Räumen für Klausuren");
	}

}
