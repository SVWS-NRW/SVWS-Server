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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerEinzelleistungen.
 */
public class Tabelle_SchuelerEinzelleistungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Teilleistung");

	/** Die Definition der Tabellenspalte Datum */
	public SchemaTabelleSpalte col_Datum = add("Datum", SchemaDatentypen.DATE, false)
		.setConverter("DatumConverter")
		.setJavaComment("Datum der Teilleistung");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("LehrerID der Teilleistung");

	/** Die Definition der Tabellenspalte Art_ID */
	public SchemaTabelleSpalte col_Art_ID = add("Art_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Art der Teilleistung");

	/** Die Definition der Tabellenspalte Bemerkung */
	public SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bemerkung zur Teilleistung");

	/** Die Definition der Tabellenspalte Leistung_ID */
	public SchemaTabelleSpalte col_Leistung_ID = add("Leistung_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("LeistungsdatenID der Teilleistung");

	/** Die Definition der Tabellenspalte NotenKrz */
	public SchemaTabelleSpalte col_NotenKrz = add("NotenKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Notenkürzel der Teilleistung");


	/** Die Definition des Fremdschlüssels SchuelerEinzelleistungen_Art_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerEinzelleistungen_Art_FK = addForeignKey(
			"SchuelerEinzelleistungen_Art_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Art_ID, Schema.tab_K_Einzelleistungen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerEinzelleistungen_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerEinzelleistungen_Lehrer_FK = addForeignKey(
			"SchuelerEinzelleistungen_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerEinzelleistungen_Leistungsdaten_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerEinzelleistungen_Leistungsdaten_FK = addForeignKey(
			"SchuelerEinzelleistungen_Leistungsdaten_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Leistung_ID, Schema.tab_SchuelerLeistungsdaten.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerEinzelleistungen.
	 */
	public Tabelle_SchuelerEinzelleistungen() {
		super("SchuelerEinzelleistungen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerTeilleistung");
		setJavaComment("Teilleistungen zu Fach und Schüler");
	}

}
