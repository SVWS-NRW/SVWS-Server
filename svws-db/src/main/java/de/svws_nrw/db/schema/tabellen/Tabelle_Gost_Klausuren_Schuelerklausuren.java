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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Schuelerklausuren.
 */
public class Tabelle_Gost_Klausuren_Schuelerklausuren extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klausurvorgaben (generiert)");

	/** Die Definition der Tabellenspalte Kursklausur_ID */
	public SchemaTabelleSpalte col_Kursklausur_ID = add("Kursklausur_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Kursklausur");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Schülers");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Bemerkungen zur Schuelerklausur");


	/** Die Definition des Fremdschlüssels Gost_Klausuren_Schuelerklausuren_Kursklausur_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Kursklausur_ID_FK = addForeignKey(
			"Gost_Klausuren_Schuelerklausuren_Kursklausur_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Kursklausur_ID, Schema.tab_Gost_Klausuren_Kursklausuren.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Schuelerklausuren_Schueler_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Schueler_ID_FK = addForeignKey(
			"Gost_Klausuren_Schuelerklausuren_Schueler_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Unique-Index Gost_Klausuren_Schuelerklausuren_UC1 */
	public SchemaTabelleUniqueIndex unique_Gost_Klausuren_Klausuren_UC1 = addUniqueIndex("Gost_Klausuren_Schuelerklausuren_UC1",
			col_Kursklausur_ID, col_Schueler_ID
		);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Schuelerklausuren_IDX_Kursklausur_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_Schuelerklausuren_IDX_Kursklausur_ID = addIndex("Gost_Klausuren_Schuelerklausuren_IDX_Kursklausur_ID",
			col_Kursklausur_ID
		);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Schuelerklausuren_IDX_Schueler_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_Schuelerklausuren_IDX_Schueler_ID = addIndex("Gost_Klausuren_Schuelerklausuren_IDX_Schueler_ID",
			col_Schueler_ID
		);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Schuelerklausuren_IDX_Schueler_ID_Kursklausur_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_Schuelerklausuren_IDX_Schueler_ID_Kursklausur_ID = addIndex("Gost_Klausuren_Schuelerklausuren_IDX_Schueler_ID_Kursklausur_ID",
			col_Schueler_ID, col_Kursklausur_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Schuelerklausuren.
	 */
	public Tabelle_Gost_Klausuren_Schuelerklausuren() {
		super("Gost_Klausuren_Schuelerklausuren", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenSchuelerklausuren");
		setJavaComment("Tabelle für die konkreten Schuelerklausurenentitäten");
	}

}
