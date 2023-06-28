package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.NoteConverterFromKuerzel;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerLeistungsdaten.
 */
public class Tabelle_SchuelerLeistungsdaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eine eindeutige ID für die Leistungsdaten des Schülers");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des zugehörigen Lernabschnittes – verweist auf den Lernabschnitt des Schülers");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des zugehörigen Faches – verweist auf das Fach");

	/** Die Definition der Tabellenspalte Hochrechnung */
	public SchemaTabelleSpalte col_Hochrechnung = add("Hochrechnung", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt an ob der Datensatz aus einem vorherigen Abschnitt geholt wurde die Minuszahl gibt die Schritte zurück an");

	/** Die Definition der Tabellenspalte Fachlehrer_ID */
	public SchemaTabelleSpalte col_Fachlehrer_ID = add("Fachlehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die ID des zugehörigen Fach-Lehrers");

	/** Die Definition der Tabellenspalte Kursart */
	public SchemaTabelleSpalte col_Kursart = add("Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Die Kursart");

	/** Die Definition der Tabellenspalte KursartAllg */
	public SchemaTabelleSpalte col_KursartAllg = add("KursartAllg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Die allgemeine Kursart des Faches (z.B. GK, LK)");

	/** Die Definition der Tabellenspalte Kurs_ID */
	public SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die eindeutige ID des zugehörigen Kurses, sofern kein Klassenunterricht vorliegt – verweist auf den Kurs");

	/** Die Definition der Tabellenspalte NotenKrz */
	public SchemaTabelleSpalte col_NotenKrz = add("NotenKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setConverter(NoteConverterFromKuerzel.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Das Notenkürzel der erteilten Note");

	/** Die Definition der Tabellenspalte NotenKrzQuartal */
	public SchemaTabelleSpalte col_NotenKrzQuartal = add("NotenKrzQuartal", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setConverter(NoteConverterFromKuerzel.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Das Notenkürzel der Quartals-Note");

	/** Die Definition der Tabellenspalte Warnung */
	public SchemaTabelleSpalte col_Warnung = add("Warnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("gibt an, ob die Leistung gemahnt wurde bzw. gemahnt werden soll – sie Mahndatum");

	/** Die Definition der Tabellenspalte Warndatum */
	public SchemaTabelleSpalte col_Warndatum = add("Warndatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("gibt das Datum an, wann die Leistung gemahnt wurde");

	/** Die Definition der Tabellenspalte AbiFach */
	public SchemaTabelleSpalte col_AbiFach = add("AbiFach", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("gibt an, ob das Fach als Abiturfach belegt wurde (NULL, 1, 2, 3, 4)");

	/** Die Definition der Tabellenspalte Wochenstunden */
	public SchemaTabelleSpalte col_Wochenstunden = add("Wochenstunden", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("gibt die Wochenstunden");

	/** Die Definition der Tabellenspalte AbiZeugnis */
	public SchemaTabelleSpalte col_AbiZeugnis = add("AbiZeugnis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED: Relikt aus Winschild nicht mehr benötigt");

	/** Die Definition der Tabellenspalte Prognose */
	public SchemaTabelleSpalte col_Prognose = add("Prognose", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("DEPRECATED: Relikt aus Winschild nicht mehr benötigt");

	/** Die Definition der Tabellenspalte FehlStd */
	public SchemaTabelleSpalte col_FehlStd = add("FehlStd", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Fachbezogene Fehlstunden");

	/** Die Definition der Tabellenspalte uFehlStd */
	public SchemaTabelleSpalte col_uFehlStd = add("uFehlStd", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Fachbezogene Fehlstunden unentschuldigt");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierungnummer des Leistungsdatensatzes");

	/** Die Definition der Tabellenspalte Lernentw */
	public SchemaTabelleSpalte col_Lernentw = add("Lernentw", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Fachbezogene Lernentwicklung Bemerkung");

	/** Die Definition der Tabellenspalte Gekoppelt */
	public SchemaTabelleSpalte col_Gekoppelt = add("Gekoppelt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("DEPRECATED: Relikt aus Winschild nicht mehr benötigt");

	/** Die Definition der Tabellenspalte VorherAbgeschl */
	public SchemaTabelleSpalte col_VorherAbgeschl = add("VorherAbgeschl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob das Fach Epochal war oder ist");

	/** Die Definition der Tabellenspalte AbschlussJahrgang */
	public SchemaTabelleSpalte col_AbschlussJahrgang = add("AbschlussJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Hier wird beim Holen von abgeschlossenen Fächern eingetragen, wann das Fach zuletzt unterrichtet wurde");

	/** Die Definition der Tabellenspalte HochrechnungStatus */
	public SchemaTabelleSpalte col_HochrechnungStatus = add("HochrechnungStatus", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("DEPRECATED: Hier wurde früher gespeichert, ob es sich um eine temporäre oder dauerhafte Hochrechnung handelt. Eine Hochrechnung ist nur noch am BK möglich und immer temporär.");

	/** Die Definition der Tabellenspalte SchulNr */
	public SchemaTabelleSpalte col_SchulNr = add("SchulNr", SchemaDatentypen.INT, false)
		.setJavaComment("Schulnummer bei externem Unterricht");

	/** Die Definition der Tabellenspalte Zusatzkraft_ID */
	public SchemaTabelleSpalte col_Zusatzkraft_ID = add("Zusatzkraft_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die Lehrer-ID der Zusatzkraft");

	/** Die Definition der Tabellenspalte WochenstdZusatzkraft */
	public SchemaTabelleSpalte col_WochenstdZusatzkraft = add("WochenstdZusatzkraft", SchemaDatentypen.INT, false)
		.setJavaComment("Wochenstunden Zusatzkraft");

	/** Die Definition der Tabellenspalte Prf10Fach */
	public SchemaTabelleSpalte col_Prf10Fach = add("Prf10Fach", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Ist Fach für ZP10 / ZK10");

	/** Die Definition der Tabellenspalte AufZeugnis */
	public SchemaTabelleSpalte col_AufZeugnis = add("AufZeugnis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Fach kommt aufs Zeugnnis Ja Nein");

	/** Die Definition der Tabellenspalte Gewichtung */
	public SchemaTabelleSpalte col_Gewichtung = add("Gewichtung", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setJavaComment("Gewichtung allgemeinbidend BK");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte NoteAbschlussBA */
	public SchemaTabelleSpalte col_NoteAbschlussBA = add("NoteAbschlussBA", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Beim BK wird hier die Note Berufsabschluss eingetragen. Ist vermutl. nicht mehr notwendig, wenn alle Zeugnisse sich aus dem BKAbschluss-Fächern bedienen");

	/** Die Definition der Tabellenspalte Umfang */
	public SchemaTabelleSpalte col_Umfang = add("Umfang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Facheigenschaft für Lernstandsberichte (V voller Umfang) (R reduzierter Umfang)");

	/** Die Definition der Tabellenspalte Fachlehrer */
	public SchemaTabelleSpalte col_Fachlehrer = add("Fachlehrer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Das Kürzel des zugehörigen Fach-Lehrers");

	/** Die Definition der Tabellenspalte Zusatzkraft */
	public SchemaTabelleSpalte col_Zusatzkraft = add("Zusatzkraft", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Das Kürzel der Zusatzkraft / des Lehrers");


	/** Die Definition des Fremdschlüssels SchuelerLeistungsdaten_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLeistungsdaten_Abschnitt_FK = addForeignKey(
			"SchuelerLeistungsdaten_Abschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abschnitt_ID, Schema.tab_SchuelerLernabschnittsdaten.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerLeistungsdaten_Fach_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLeistungsdaten_Fach_FK = addForeignKey(
			"SchuelerLeistungsdaten_Fach_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerLeistungsdaten_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLeistungsdaten_Lehrer_FK = addForeignKey(
			"SchuelerLeistungsdaten_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Fachlehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLeistungsdaten_Lehrer_Zusatzkraft_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLeistungsdaten_Lehrer_Zusatzkraft_FK = addForeignKey(
			"SchuelerLeistungsdaten_Lehrer_Zusatzkraft_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Zusatzkraft_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLeistungsdaten_Kurs_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLeistungsdaten_Kurs_FK = addForeignKey(
			"SchuelerLeistungsdaten_Kurs_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Kurs_ID, Schema.tab_Kurse.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/** Die Definition des Non-Unique-Index SchuelerLeistungsdaten_IDX1 */
	public SchemaTabelleIndex index_SchuelerLeistungsdaten_IDX1 = addIndex("SchuelerLeistungsdaten_IDX1",
			col_Kurs_ID
		);

	/** Die Definition des Non-Unique-Index SchuelerLeistungsdaten_IDX2 */
	public SchemaTabelleIndex index_SchuelerLeistungsdaten_IDX2 = addIndex("SchuelerLeistungsdaten_IDX2",
			col_Fach_ID
		);

	/** Die Definition des Non-Unique-Index SchuelerLeistungsdaten_IDX3 */
	public SchemaTabelleIndex index_SchuelerLeistungsdaten_IDX3 = addIndex("SchuelerLeistungsdaten_IDX3",
			col_Fachlehrer_ID
		);


	/** Die Definition des Unique-Index SchuelerLeistungsdaten_UC1 */
	public SchemaTabelleUniqueIndex unique_SchuelerLeistungsdaten_UC1 = addUniqueIndex("SchuelerLeistungsdaten_UC1",
			col_Abschnitt_ID,
			col_Fach_ID,
			col_Kurs_ID,
			col_Kursart,
			col_Fachlehrer_ID
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerLeistungsdaten.
	 */
	public Tabelle_SchuelerLeistungsdaten() {
		super("SchuelerLeistungsdaten", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerLeistungsdaten");
		setJavaComment("Leistungsdaten (Fächer) zu Lernabschnitten und Schülern");
	}

}
