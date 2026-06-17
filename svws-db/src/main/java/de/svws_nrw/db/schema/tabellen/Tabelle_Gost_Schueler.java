package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Schueler.
 */
public class Tabelle_Gost_Schueler extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public final SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Die ID des Schülers in der Schülertabelle");

	/** Die Definition der Tabellenspalte DatumBeratung */
	public final SchemaTabelleSpalte col_DatumBeratung = add("DatumBeratung", SchemaDatentypen.DATETIME, false)
			.setConverter(DatumConverter.class)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Das Datum der letzten Beratung des Schülers");

	/** Die Definition der Tabellenspalte DatumRuecklauf */
	public final SchemaTabelleSpalte col_DatumRuecklauf = add("DatumRuecklauf", SchemaDatentypen.DATETIME, false)
			.setConverter(DatumConverter.class)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten:"
					+ " Das Datum an dem der letzte Beratungsbogen des Schülersmit seiner Fächerwahl in der Schule eingereicht wurde");

	/** Die Definition der Tabellenspalte HatSportattest */
	public final SchemaTabelleSpalte col_HatSportattest = add("HatSportattest", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setNotNull()
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten:"
					+ " Gibt an, ob ein Sportattest bei dem Schüler vorliegt oder nicht und die Wahl eines Ersatzfaches zulässig ist: 1 - true, 0 - false");

	/** Die Definition der Tabellenspalte Kommentar */
	public final SchemaTabelleSpalte col_Kommentar = add("Kommentar", SchemaDatentypen.TEXT, false)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Kommentar des Beratungslehrers zur der Wahl des Schülers");

	/** Die Definition der Tabellenspalte Beratungslehrer_ID */
	public final SchemaTabelleSpalte col_Beratungslehrer_ID = add("Beratungslehrer_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: ID des Beratungslehrers, der die letzte Beratung vorgenommen hat");

	/** Die Definition der Tabellenspalte PruefPhase */
	public final SchemaTabelleSpalte col_PruefPhase = add("PruefPhase", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten:"
					+ " Gibt an welche Halbjahre bei der Belegprüfung geprüft werden sollen (E - nur EF.1, G - Gesamtprüfung bis einschließlich Q2.2)");

	/** Die Definition der Tabellenspalte BesondereLernleistung_Art */
	public final SchemaTabelleSpalte col_BesondereLernleistung_Art = add("BesondereLernleistung_Art", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Die Art einer besonderen Lernleistung");

	/** Die Definition der Tabellenspalte BesondereLernleistung_Punkte */
	public final SchemaTabelleSpalte col_BesondereLernleistung_Punkte = add("BesondereLernleistung_Punkte", SchemaDatentypen.INT, false)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Die Notenpunkte der besonderen Lernleistung ");

	/** Die Definition der Tabellenspalte GKL_EF_AF1_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_EF_AF1_Klausurvorgabe_ID = add("GKL_EF_AF1_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der EF in Aufgabenfeld 1")
			.setRevision(SchemaRevisionen.REV_68);

	/** Die Definition der Tabellenspalte GKL_EF_AF2_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_EF_AF2_Klausurvorgabe_ID = add("GKL_EF_AF2_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der EF in Aufgabenfeld 2")
			.setRevision(SchemaRevisionen.REV_68);

	/** Die Definition der Tabellenspalte GKL_EF_AF3_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_EF_AF3_Klausurvorgabe_ID = add("GKL_EF_AF3_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der EF in Aufgabenfeld 3")
			.setRevision(SchemaRevisionen.REV_68);

	/** Die Definition der Tabellenspalte GKL_Q_AF1_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_Q_AF1_Klausurvorgabe_ID = add("GKL_Q_AF1_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der Q-Phase in Aufgabenfeld 1")
			.setRevision(SchemaRevisionen.REV_68);

	/** Die Definition der Tabellenspalte GKL_Q_AF2_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_Q_AF2_Klausurvorgabe_ID = add("GKL_Q_AF2_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der Q-Phase in Aufgabenfeld 2")
			.setRevision(SchemaRevisionen.REV_68);

	/** Die Definition der Tabellenspalte GKL_Q_AF3_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_Q_AF3_Klausurvorgabe_ID = add("GKL_Q_AF3_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Schülerdaten: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der Q-Phase in Aufgabenfeld 3")
			.setRevision(SchemaRevisionen.REV_68);


	/** Die Definition des Fremdschlüssels Gost_Schueler_Schueler_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Schueler_ID_FK = addForeignKey(
			"Gost_Schueler_Schueler_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_GKL_EF_AF1_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_GKL_EF_AF1_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_GKL_EF_AF1_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_EF_AF1_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	).setRevision(SchemaRevisionen.REV_68);

	/** Die Definition des Fremdschlüssels Gost_Schueler_GKL_EF_AF2_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_GKL_EF_AF2_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_GKL_EF_AF2_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_EF_AF2_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	).setRevision(SchemaRevisionen.REV_68);

	/** Die Definition des Fremdschlüssels Gost_Schueler_GKL_EF_AF3_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_GKL_EF_AF3_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_GKL_EF_AF3_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_EF_AF3_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	).setRevision(SchemaRevisionen.REV_68);

	/** Die Definition des Fremdschlüssels Gost_Schueler_GKL_Q_AF1_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_GKL_Q_AF1_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_GKL_Q_AF1_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_Q_AF1_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	).setRevision(SchemaRevisionen.REV_68);

	/** Die Definition des Fremdschlüssels Gost_Schueler_GKL_Q_AF2_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_GKL_Q_AF2_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_GKL_Q_AF2_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_Q_AF2_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	).setRevision(SchemaRevisionen.REV_68);

	/** Die Definition des Fremdschlüssels Gost_Schueler_GKL_Q_AF3_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_GKL_Q_AF3_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_GKL_Q_AF3_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_Q_AF3_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	).setRevision(SchemaRevisionen.REV_68);


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
