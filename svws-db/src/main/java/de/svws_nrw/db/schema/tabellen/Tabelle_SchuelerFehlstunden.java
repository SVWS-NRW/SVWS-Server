package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerFehlstunden.
 */
public class Tabelle_SchuelerFehlstunden extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Fehlstundeneintrags");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("AbschnittsID des zugehörigen Lernabschnitts");

	/** Die Definition der Tabellenspalte Datum */
	public SchemaTabelleSpalte col_Datum = add("Datum", SchemaDatentypen.DATE, false)
		.setNotNull()
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum der Fehlzeit");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("FachID der Fehlzeit");

	/** Die Definition der Tabellenspalte FehlStd */
	public SchemaTabelleSpalte col_FehlStd = add("FehlStd", SchemaDatentypen.FLOAT, false)
		.setNotNull()
		.setJavaComment("Anzahl der Fehlstunden");

	/** Die Definition der Tabellenspalte VonStd */
	public SchemaTabelleSpalte col_VonStd = add("VonStd", SchemaDatentypen.INT, false)
		.setJavaComment("Beginn Stunde Fehlzeit");

	/** Die Definition der Tabellenspalte BisStd */
	public SchemaTabelleSpalte col_BisStd = add("BisStd", SchemaDatentypen.INT, false)
		.setJavaComment("Ende Stunde Fehlzeit");

	/** Die Definition der Tabellenspalte Entschuldigt */
	public SchemaTabelleSpalte col_Entschuldigt = add("Entschuldigt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Entschuldigt Ja Nein");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("LehrerID der Fehlzeit");


	/** Die Definition des Fremdschlüssels SchuelerFehlstunden_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFehlstunden_Abschnitt_FK = addForeignKey(
			"SchuelerFehlstunden_Abschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abschnitt_ID, Schema.tab_SchuelerLernabschnittsdaten.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerFehlstunden_Fach_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFehlstunden_Fach_FK = addForeignKey(
			"SchuelerFehlstunden_Fach_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerFehlstunden_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFehlstunden_Lehrer_FK = addForeignKey(
			"SchuelerFehlstunden_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerFehlstunden.
	 */
	public Tabelle_SchuelerFehlstunden() {
		super("SchuelerFehlstunden", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerFehlstunden");
		setJavaComment("Tagebezogene Fehlzeiten (wenn diese erfasst werden) zum SchülerLernabschnitt");
	}

}
