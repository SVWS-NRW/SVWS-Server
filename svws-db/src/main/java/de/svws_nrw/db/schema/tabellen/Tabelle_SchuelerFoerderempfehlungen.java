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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerFoerderempfehlungen.
 */
public class Tabelle_SchuelerFoerderempfehlungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte GU_ID */
	public SchemaTabelleSpalte col_GU_ID = add("GU_ID", SchemaDatentypen.VARCHAR, true).setDatenlaenge(40)
		.setNotNull()
		.setJavaComment("GU_ID der Förderempfehlung (wird genutzt für Import Export)");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Schüler-ID der Förderempfehlung, in Abschnitt_ID enthalten");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID der zugehörigen Schülerlernabschnittsdaten");

	/** Die Definition der Tabellenspalte DatumAngelegt */
	public SchemaTabelleSpalte col_DatumAngelegt = add("DatumAngelegt", SchemaDatentypen.DATE, false)
		.setNotNull()
		.setConverter(DatumConverter.class)
		.setJavaComment("Anlegedatum der Förderempfehlung");

	/** Die Definition der Tabellenspalte Klassen_ID */
	public SchemaTabelleSpalte col_Klassen_ID = add("Klassen_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Klassen-ID der Förderempfehlung");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("LehrerID der Förderempfehlung");

	/** Die Definition der Tabellenspalte DatumAenderungSchild */
	public SchemaTabelleSpalte col_DatumAenderungSchild = add("DatumAenderungSchild", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Änderungsdatum in Schild-NRW der Förderempfehlung");

	/** Die Definition der Tabellenspalte DatumAenderungSchildWeb */
	public SchemaTabelleSpalte col_DatumAenderungSchildWeb = add("DatumAenderungSchildWeb", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Änderungsdatum in SchildWeb der Förderempfehlung");

	/** Die Definition der Tabellenspalte Inhaltl_Prozessbez_Komp */
	public SchemaTabelleSpalte col_Inhaltl_Prozessbez_Komp = add("Inhaltl_Prozessbez_Komp", SchemaDatentypen.TEXT, false)
		.setJavaComment("Inhalt Prozessbezogene Kompetenzen der Förderempfehlung");

	/** Die Definition der Tabellenspalte Methodische_Komp */
	public SchemaTabelleSpalte col_Methodische_Komp = add("Methodische_Komp", SchemaDatentypen.TEXT, false)
		.setJavaComment("Inhalte methodische Kompetenzen der Förderempfehlung");

	/** Die Definition der Tabellenspalte Lern_Arbeitsverhalten */
	public SchemaTabelleSpalte col_Lern_Arbeitsverhalten = add("Lern_Arbeitsverhalten", SchemaDatentypen.TEXT, false)
		.setJavaComment("Inhalt Lern und Arbeitsverhalten der Förderempfehlung");

	/** Die Definition der Tabellenspalte Massn_Inhaltl_Prozessbez_Komp */
	public SchemaTabelleSpalte col_Massn_Inhaltl_Prozessbez_Komp = add("Massn_Inhaltl_Prozessbez_Komp", SchemaDatentypen.TEXT, false)
		.setJavaComment("Inhalt Maßnahmen Prozessbezogenen Kompetenzen der Förderempfehlung");

	/** Die Definition der Tabellenspalte Massn_Methodische_Komp */
	public SchemaTabelleSpalte col_Massn_Methodische_Komp = add("Massn_Methodische_Komp", SchemaDatentypen.TEXT, false)
		.setJavaComment("Inhalt Maßnahmen methodische Kompetenzen der Förderempfehlung");

	/** Die Definition der Tabellenspalte Massn_Lern_Arbeitsverhalten */
	public SchemaTabelleSpalte col_Massn_Lern_Arbeitsverhalten = add("Massn_Lern_Arbeitsverhalten", SchemaDatentypen.TEXT, false)
		.setJavaComment("Inhalt Maßnahmen Lern und Arbeitsverhalten der Förderempfehlung");

	/** Die Definition der Tabellenspalte Verantwortlichkeit_Eltern */
	public SchemaTabelleSpalte col_Verantwortlichkeit_Eltern = add("Verantwortlichkeit_Eltern", SchemaDatentypen.TEXT, false)
		.setJavaComment("Inhalt Verantwortlichkeit der Eltern der Förderempfehlung");

	/** Die Definition der Tabellenspalte Verantwortlichkeit_Schueler */
	public SchemaTabelleSpalte col_Verantwortlichkeit_Schueler = add("Verantwortlichkeit_Schueler", SchemaDatentypen.TEXT, false)
		.setJavaComment("Inhalt Verantwortlichkeit des Schülers der Förderempfehlung");

	/** Die Definition der Tabellenspalte Zeitrahmen_von_Datum */
	public SchemaTabelleSpalte col_Zeitrahmen_von_Datum = add("Zeitrahmen_von_Datum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Zeitrahmen Datum von der Förderempfehlung");

	/** Die Definition der Tabellenspalte Zeitrahmen_bis_Datum */
	public SchemaTabelleSpalte col_Zeitrahmen_bis_Datum = add("Zeitrahmen_bis_Datum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Zeitrahmen Datum bis der Förderempfehlung");

	/** Die Definition der Tabellenspalte Ueberpruefung_Datum */
	public SchemaTabelleSpalte col_Ueberpruefung_Datum = add("Ueberpruefung_Datum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum der Überprüfung der Förderempfehlung");

	/** Die Definition der Tabellenspalte Naechstes_Beratungsgespraech */
	public SchemaTabelleSpalte col_Naechstes_Beratungsgespraech = add("Naechstes_Beratungsgespraech", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum nächstes Beratungsgespräch der Förderempfehlung");

	/** Die Definition der Tabellenspalte EingabeFertig */
	public SchemaTabelleSpalte col_EingabeFertig = add("EingabeFertig", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Eingabe abgeschlossen Ja Nein  der Förderempfehlung");

	/** Die Definition der Tabellenspalte Faecher */
	public SchemaTabelleSpalte col_Faecher = add("Faecher", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Fächer der Förderempfehlung im Kürzel komma getrennt");

	/** Die Definition der Tabellenspalte Abgeschlossen */
	public SchemaTabelleSpalte col_Abgeschlossen = add("Abgeschlossen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Datum Abgeschlossen der Förderempfehlung");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr der Förderempfehlung");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnitt der Förderempfehlung");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Klasse */
	public SchemaTabelleSpalte col_Klasse = add("Klasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Klassen-Bezeichnung der Förderempfehlung");


	/** Die Definition des Fremdschlüssels SchuelerFoerderempfehlungen_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFoerderempfehlungen_Abschnitt_FK = addForeignKey(
			"SchuelerFoerderempfehlungen_Abschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abschnitt_ID, Schema.tab_SchuelerLernabschnittsdaten.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerFoerderempfehlungen_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFoerderempfehlungen_Schueler_FK = addForeignKey(
			"SchuelerFoerderempfehlungen_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		)
		.setVeraltet(SchemaRevisionen.REV_1);

	/** Die Definition des Fremdschlüssels SchuelerFoerderempfehlungen_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFoerderempfehlungen_Lehrer_FK = addForeignKey(
			"SchuelerFoerderempfehlungen_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerFoerderempfehlungen_Klassen_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFoerderempfehlungen_Klassen_FK = addForeignKey(
			"SchuelerFoerderempfehlungen_Klassen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Klassen_ID, Schema.tab_Klassen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerFoerderempfehlungen.
	 */
	public Tabelle_SchuelerFoerderempfehlungen() {
		super("SchuelerFoerderempfehlungen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerFoerderempfehlung");
		setJavaComment("Eingetragene Förderempfehlungen zum Schüler");
	}

}
