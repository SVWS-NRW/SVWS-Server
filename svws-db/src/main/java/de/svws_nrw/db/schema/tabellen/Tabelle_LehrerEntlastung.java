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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerEntlastung.
 */
public class Tabelle_LehrerEntlastung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID für den Eintrag für die Entlastungsstunden (Mehr-Minderleistung)");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Lehrer-ID für die Entlastungsstunden (Mehr-Minderleistung), in LehrerAbchnittsdaten enthalten");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Lehrerabschnittsdaten");

	/** Die Definition der Tabellenspalte EntlastungsgrundKrz */
	public SchemaTabelleSpalte col_EntlastungsgrundKrz = add("EntlastungsgrundKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kürzel für die Entlastungsstunden (Minderleistung)");

	/** Die Definition der Tabellenspalte EntlastungStd */
	public SchemaTabelleSpalte col_EntlastungStd = add("EntlastungStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Anzahl für die Entlastungsstunden (Minderleistung)");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr für die Entlastungsstunden (Minderleistung)");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnitt für die Entlastungsstunden (Minderleistung)");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels LehrerEntlastung_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_LehrerEntlastung_Abschnitt_FK = addForeignKey(
			"LehrerEntlastung_Abschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abschnitt_ID, Schema.tab_LehrerAbschnittsdaten.col_ID)
		);

	/** Die Definition des Fremdschlüssels LehrerEntlastung_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerEntlastung_Lehrer_FK = addForeignKey(
			"LehrerEntlastung_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setVeraltet(SchemaRevisionen.REV_1);


	/** Die Definition des Unique-Index LehrerEntlastung_UC1 */
	public SchemaTabelleUniqueIndex unique_LehrerEntlastung_UC1 = addUniqueIndex("LehrerEntlastung_UC1", 
			col_Abschnitt_ID, 
			col_EntlastungsgrundKrz
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerEntlastung.
	 */
	public Tabelle_LehrerEntlastung() {
		super("LehrerEntlastung", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerEntlastungsstunde");
		setJavaComment("Entlastungsstundenstunden für Lehrkräfte gültige ASD-Schlüssel");
	}

}
