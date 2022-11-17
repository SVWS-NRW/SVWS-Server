package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle ErzieherDatenschutz.
 */
public class Tabelle_ErzieherDatenschutz extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ErzieherID */
	public SchemaTabelleSpalte col_ErzieherID = add("ErzieherID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ErzieherID des Datenschutzeintrags");

	/** Die Definition der Tabellenspalte DatenschutzID */
	public SchemaTabelleSpalte col_DatenschutzID = add("DatenschutzID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("DatenschutzID des Eintrags");

	/** Die Definition der Tabellenspalte Status */
	public SchemaTabelleSpalte col_Status = add("Status", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Status des Datenschutz-Eintrags (true/false)");


	/** Die Definition des Fremdschlüssels ErzieherDatenschutz_Erzieher_FK */
	public SchemaTabelleFremdschluessel fk_ErzieherDatenschutz_Erzieher_FK = addForeignKey(
			"ErzieherDatenschutz_Erzieher_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_ErzieherID, Schema.tab_SchuelerErzAdr.col_ID)
		);

	/** Die Definition des Fremdschlüssels ErzieherDatenschutz_Datenschutz_FK */
	public SchemaTabelleFremdschluessel fk_ErzieherDatenschutz_Datenschutz_FK = addForeignKey(
			"ErzieherDatenschutz_Datenschutz_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_DatenschutzID, Schema.tab_K_Datenschutz.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle ErzieherDatenschutz.
	 */
	public Tabelle_ErzieherDatenschutz() {
		super("ErzieherDatenschutz", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.erzieher");
		setJavaClassName("DTOErzieherDatenschutz");
		setJavaComment("Tabelle zur Speicherung der Einwilligungen zu Datenschutz bei den Erziehern");
	}

}
