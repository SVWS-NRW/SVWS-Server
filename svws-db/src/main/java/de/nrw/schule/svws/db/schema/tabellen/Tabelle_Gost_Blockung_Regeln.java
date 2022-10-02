package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Blockung_Regeln.
 */
public class Tabelle_Gost_Blockung_Regeln extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Regeldefinition der Blockung: ID der Regel (generiert)");

	/** Die Definition der Tabellenspalte Blockung_ID */
	public SchemaTabelleSpalte col_Blockung_ID = add("Blockung_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Regeldefinition der Blockung: ID der Blockung");

	/** Die Definition der Tabellenspalte Typ */
	public SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.INT, false)
		.setNotNull()
		.setConverter("GostKursblockungRegelTypConverter")
		.setJavaComment("Kursblockung der Gymnasialen Oberstufe - Regeldefinition der Blockung: Die ID des Typs der Regeldefinition (siehe Core-Type GostKursblockungRegeltyp)");


	/** Die Definition des Fremdschlüssels Gost_Blockung_Regeln_Blockung_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Regeln_Blockung_FK = addForeignKey(
			"Gost_Blockung_Regeln_Blockung_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Blockung_ID, Schema.tab_Gost_Blockung.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Blockung_Regeln.
	 */
	public Tabelle_Gost_Blockung_Regeln() {
		super("Gost_Blockung_Regeln", SchemaRevisionen.REV_7);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.kursblockung");
		setJavaClassName("DTOGostBlockungRegel");
		setJavaComment("Tabelle für die Regeln, welche als Bedingungen zu einer Kursblockung der gymnasialen Oberstufe zugeordnet sind");
	}

}
