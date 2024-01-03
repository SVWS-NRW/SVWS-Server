package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Schuelerklausuren_Termine.
 */
public class Tabelle_Gost_Klausuren_Schuelerklausuren_Termine extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Schülerklausur-Termins (generiert)");

	/** Die Definition der Tabellenspalte Schuelerklausur_ID */
	public SchemaTabelleSpalte col_Schuelerklausur_ID = add("Schuelerklausur_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Schülerklausur");

	/** Die Definition der Tabellenspalte Termin_ID */
	public SchemaTabelleSpalte col_Termin_ID = add("Termin_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Klausurtermins, null falls Termin der Kursklausur");

	/** Die Definition der Tabellenspalte Startzeit */
	public SchemaTabelleSpalte col_Startzeit = add("Startzeit", SchemaDatentypen.TIME, false)
		.setConverter(UhrzeitConverter.class)
		.setJavaComment("Startzeit der Klausur, wenn abweichend von Startzeit des Klausurtermins");


	/** Die Definition des Fremdschlüssels Gost_Klausuren_Schuelerklausuren_Termine_Schuelerklausur_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Termine_Schuelerklausur_ID_FK = addForeignKey(
			"Gost_Klausuren_Schuelerklausuren_Termine_Schuelerklausur_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schuelerklausur_ID, Schema.tab_Gost_Klausuren_Schuelerklausuren.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Schuelerklausuren_Termine_Termine_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Schuelerklausuren_Termine_Termine_ID_FK = addForeignKey(
			"Gost_Klausuren_Schuelerklausuren_Termine_Termine_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Termin_ID, Schema.tab_Gost_Klausuren_Termine.col_ID)
		);

	/** Die Definition des Unique-Index Gost_Klausuren_Schuelerklausuren_Termine_UC1 */
	public SchemaTabelleUniqueIndex unique_Gost_Klausuren_Schuelerklausuren_Termine_UC1 = addUniqueIndex("Gost_Klausuren_Schuelerklausuren_Termine_UC1",
			col_Schuelerklausur_ID, col_Termin_ID
		);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Schuelerklausuren_Termine_IDX_Schuelerklausur_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_Schuelerklausuren_Termine_IDX_Schuelerklausur_ID = addIndex("Gost_Klausuren_Schuelerklausuren_Termine_IDX_Schuelerklausur_ID",
			col_Schuelerklausur_ID
		);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Schuelerklausuren_Termine_IDX_Termin_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_Schuelerklausuren_Termine_IDX_Termin_ID = addIndex("Gost_Klausuren_Schuelerklausuren_Termine_IDX_Termin_ID",
			col_Termin_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Schuelerklausuren.
	 */
	public Tabelle_Gost_Klausuren_Schuelerklausuren_Termine() {
		super("Gost_Klausuren_Schuelerklausuren_Termine", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenSchuelerklausurenTermine");
		setJavaComment("Tabelle für die Terminzuordnung von Schülerklausuren");
	}

}
