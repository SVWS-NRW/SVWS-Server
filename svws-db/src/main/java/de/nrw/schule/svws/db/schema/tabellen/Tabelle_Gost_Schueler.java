package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.DatumConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Schueler.
 */
public class Tabelle_Gost_Schueler extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Die ID des Schülers in der Schülertabelle");

	/** Die Definition der Tabellenspalte DatumBeratung */
	public SchemaTabelleSpalte col_DatumBeratung = add("DatumBeratung", SchemaDatentypen.DATETIME, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Das Datum der letzten Beratung des Schülers");

	/** Die Definition der Tabellenspalte DatumRuecklauf */
	public SchemaTabelleSpalte col_DatumRuecklauf = add("DatumRuecklauf", SchemaDatentypen.DATETIME, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Das Datum an dem der letzte Beratungsbogen des Schülersmit seiner Fächerwahl in der Schule eingereicht wurde");

	/** Die Definition der Tabellenspalte HatSportattest */
	public SchemaTabelleSpalte col_HatSportattest = add("HatSportattest", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Gibt an, ob ein Sportattest bei dem Schüler vorliegt oder nicht und die Wahl eines Ersatzfaches zulässig ist: 1 - true, 0 - false");

	/** Die Definition der Tabellenspalte Kommentar */
	public SchemaTabelleSpalte col_Kommentar = add("Kommentar", SchemaDatentypen.TEXT, false)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Kommentar des Beratungslehrers zur der Wahl des Schülers");

	/** Die Definition der Tabellenspalte Beratungslehrer_ID */
	public SchemaTabelleSpalte col_Beratungslehrer_ID = add("Beratungslehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: ID des Beratungslehrers, der die letzte Beratung vorgenommen hat");

	/** Die Definition der Tabellenspalte PruefPhase */
	public SchemaTabelleSpalte col_PruefPhase = add("PruefPhase", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Gibt an welche Halbjahre bei der Belegprüfung geprüft werden sollen (E - nur EF.1, G - Gesamtprüfung bis einschließlich Q2.2)");

	/** Die Definition der Tabellenspalte BesondereLernleistung_Art */
	public SchemaTabelleSpalte col_BesondereLernleistung_Art = add("BesondereLernleistung_Art", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Die Art einer besonderen Lernleistung");

	/** Die Definition der Tabellenspalte BesondereLernleistung_Punkte */
	public SchemaTabelleSpalte col_BesondereLernleistung_Punkte = add("BesondereLernleistung_Punkte", SchemaDatentypen.INT, false)
		.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Die Notenpunkte der besonderen Lernleistung ");


	/** Die Definition des Fremdschlüssels Gost_Schueler_Schueler_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Schueler_Schueler_ID_FK = addForeignKey(
			"Gost_Schueler_Schueler_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Schueler.
	 */
	public Tabelle_Gost_Schueler() {
		super("Gost_Schueler", SchemaRevisionen.REV_4);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostSchueler");
		setJavaComment("Gymnasiale Oberstufe: Zusatzinformationen für Schüler der gymnasialen Oberstufe ");
	}

}
