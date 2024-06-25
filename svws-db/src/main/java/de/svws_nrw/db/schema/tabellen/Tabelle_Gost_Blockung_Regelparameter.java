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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Blockung_Regelparameter.
 */
public class Tabelle_Gost_Blockung_Regelparameter extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Regel_ID */
	public SchemaTabelleSpalte col_Regel_ID = add("Regel_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Regel-Parameters");

	/** Die Definition der Tabellenspalte Nummer */
	public SchemaTabelleSpalte col_Nummer = add("Nummer", SchemaDatentypen.INT, true)
			.setNotNull()
			.setJavaComment("Die Nummer des Parameters der Regel, beginnend bei 1");

	/** Die Definition der Tabellenspalte Parameter */
	public SchemaTabelleSpalte col_Parameter = add("Parameter", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Der Wert des Parameters der Regel (hängt vom Typ der Regel ab)");


	/** Die Definition des Fremdschlüssels Gost_Blockung_Regelparameter_Regel_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Regelparameter_Regel_FK = addForeignKey(
			"Gost_Blockung_Regelparameter_Regel_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Regel_ID, Schema.tab_Gost_Blockung_Regeln.col_ID)
	);

	/** Die Definition des Non-Unique-Index Gost_Blockung_Regelparameter_IDX_Regel_ID */
	public SchemaTabelleIndex index_Gost_Blockung_Regelparameter_IDX_Regel_ID = addIndex("Gost_Blockung_Regelparameter_IDX_Regel_ID",
			col_Regel_ID
	);

	/** Die Definition des Non-Unique-Index Gost_Blockung_Regelparameter_IDX_Regel_ID_Nummer */
	public SchemaTabelleIndex index_Gost_Blockung_Regelparameter_IDX_Regel_ID_Nummer = addIndex("Gost_Blockung_Regelparameter_IDX_Regel_ID_Nummer",
			col_Regel_ID,
			col_Nummer
	);

	/** Die Definition des Non-Unique-Index Gost_Blockung_Regelparameter_IDX_Regel_ID_Nummer_Parameter */
	public SchemaTabelleIndex index_Gost_Blockung_Regelparameter_IDX_Regel_ID_Nummer_Parameter =
			addIndex("Gost_Blockung_Regelparameter_IDX_Regel_ID_Nummer_Parameter",
					col_Regel_ID,
					col_Nummer,
					col_Parameter
			).setRevision(SchemaRevisionen.REV_12);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Blockung_Regelparameter.
	 */
	public Tabelle_Gost_Blockung_Regelparameter() {
		super("Gost_Blockung_Regelparameter", SchemaRevisionen.REV_7);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost.kursblockung");
		setJavaClassName("DTOGostBlockungRegelParameter");
		setJavaComment("Tabelle für die Parameter von Regeln, welche als Bedingungen zu einer Kursblockung der gymnasialen Oberstufe zugeordnet sind");
	}

}
