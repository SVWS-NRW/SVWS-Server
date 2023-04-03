package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerDatenschutz.
 */
public class Tabelle_LehrerDatenschutz extends SchemaTabelle {

	/** Die Definition der Tabellenspalte LehrerID */
	public SchemaTabelleSpalte col_LehrerID = add("LehrerID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("LehrerID des Datenschutzeintrags");

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

	/** Die Definition der Tabellenspalte Abgefragt */
	public SchemaTabelleSpalte col_Abgefragt = add("Abgefragt", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Status der Abfrage Datenschutz-Eintrags (true/false)");


	/** Die Definition des Fremdschlüssels LehrerDatenschutz_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerDatenschutz_Lehrer_FK = addForeignKey(
			"LehrerDatenschutz_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_LehrerID, Schema.tab_K_Lehrer.col_ID)
		);

	/** Die Definition des Fremdschlüssels LehrerDatenschutz_Datenschutz_FK */
	public SchemaTabelleFremdschluessel fk_LehrerDatenschutz_Datenschutz_FK = addForeignKey(
			"LehrerDatenschutz_Datenschutz_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_DatenschutzID, Schema.tab_K_Datenschutz.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerDatenschutz.
	 */
	public Tabelle_LehrerDatenschutz() {
		super("LehrerDatenschutz", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerDatenschutz");
		setJavaComment("Einwilligungen Datenschutz für die Lehrer");
	}

}
