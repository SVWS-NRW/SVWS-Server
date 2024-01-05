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

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schienen.
 */
public class Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Zwischenergebnis_ID */
	public SchemaTabelleSpalte col_Zwischenergebnis_ID = add("Zwischenergebnis_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Kurs-Schienen-Zuordnung eines Zwischenergebnisses: ID des Zwischenergebnisses");

	/** Die Definition der Tabellenspalte Blockung_Kurs_ID */
	public SchemaTabelleSpalte col_Blockung_Kurs_ID = add("Blockung_Kurs_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Kurs-Schienen-Zuordnung eines Zwischenergebnisses: ID des Kurses");

	/** Die Definition der Tabellenspalte Schienen_ID */
	public SchemaTabelleSpalte col_Schienen_ID = add("Schienen_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Kurs-Schienen-Zuordnung eines Zwischenergebnisses: ID der Schiene aus der Blockung");


	/** Die Definition des Fremdschlüssels Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_ErgID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_ErgID_FK = addForeignKey(
			"Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_ErgID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Zwischenergebnis_ID, Schema.tab_Gost_Blockung_Zwischenergebnisse.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_Kurs_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_Kurs_FK = addForeignKey(
			"Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_Kurs_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Blockung_Kurs_ID, Schema.tab_Gost_Blockung_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_Schiene_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_Schiene_FK = addForeignKey(
			"Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_Schiene_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schienen_ID, Schema.tab_Gost_Blockung_Schienen.col_ID)
		);

	/** Die Definition des Non-Unique-Index Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_IDX1 */
	public SchemaTabelleIndex index_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_IDX1 = addIndex("Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_IDX1",
		col_Zwischenergebnis_ID
	);

	/** Die Definition des Non-Unique-Index Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_IDX2 */
	public SchemaTabelleIndex index_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_IDX2 = addIndex("Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_IDX2",
		col_Zwischenergebnis_ID,
		col_Blockung_Kurs_ID
	);

	/** Die Definition des Non-Unique-Index Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_IDX3 */
	public SchemaTabelleIndex index_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_IDX3 = addIndex("Gost_Blockung_Zwischenergebnisse_Kurs_Schienen_IDX3",
		col_Zwischenergebnis_ID,
		col_Schienen_ID
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schienen.
	 */
	public Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen() {
		super("Gost_Blockung_Zwischenergebnisse_Kurs_Schienen", SchemaRevisionen.REV_7);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost.kursblockung");
		setJavaClassName("DTOGostBlockungZwischenergebnisKursSchiene");
		setJavaComment("Tabelle für die Zuordnung von Kursen zu Schienen bei Zwischenergebnissen, welche einer Kursblockung der gymnasialen Oberstufe zugeordnet sind");
	}

}
