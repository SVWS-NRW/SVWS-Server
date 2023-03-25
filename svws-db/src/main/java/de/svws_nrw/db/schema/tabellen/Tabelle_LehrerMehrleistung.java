package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerMehrleistung.
 */
public class Tabelle_LehrerMehrleistung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID für den Eintrag für die Mehrarbeitsstunden eines Lehrers");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Lehrer-ID die zu den Mehrarbeitsstunden gehört, in LehrerAbchnittsdaten enthalten");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Lehrerabschnittsdaten");

	/** Die Definition der Tabellenspalte MehrleistungsgrundKrz */
	public SchemaTabelleSpalte col_MehrleistungsgrundKrz = add("MehrleistungsgrundKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Mehrarbeitsstunden Kürzel");

	/** Die Definition der Tabellenspalte MehrleistungStd */
	public SchemaTabelleSpalte col_MehrleistungStd = add("MehrleistungStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Anzahl Mehrarbeitsstunden");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr der Mehrarbeitsstunden");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnitt der Mehrarbeitsstunden");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels LehrerMehrleistung_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_LehrerMehrleistung_Abschnitt_FK = addForeignKey(
			"LehrerMehrleistung_Abschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abschnitt_ID, Schema.tab_LehrerAbschnittsdaten.col_ID)
		);

	/** Die Definition des Fremdschlüssels LehrerMehrleistung_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerMehrleistung_Lehrer_FK = addForeignKey(
			"LehrerMehrleistung_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setVeraltet(SchemaRevisionen.REV_1);


	/** Die Definition des Unique-Index LehrerMehrleistung_UC1 */
	public SchemaTabelleUniqueIndex unique_LehrerMehrleistung_UC1 = addUniqueIndex("LehrerMehrleistung_UC1", 
			col_Abschnitt_ID, 
			col_MehrleistungsgrundKrz
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerMehrleistung.
	 */
	public Tabelle_LehrerMehrleistung() {
		super("LehrerMehrleistung", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerMehrleistung");
		setJavaComment("gültige Schlüssel zur Lehrkraft für die Mehrleistungsstunden");
	}

}
