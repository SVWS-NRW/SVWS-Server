package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.NoteConverterFromInteger;
import de.svws_nrw.db.converter.current.statkue.SchulgliederungKuerzelConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerLernabschnittsdaten.
 */
public class Tabelle_SchuelerLernabschnittsdaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eine eindeutige ID für den Lernabschnitt des Schülers");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Schülers – verweist auf den Schülers");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte WechselNr */
	public SchemaTabelleSpalte col_WechselNr = add("WechselNr", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Wird für Wiederholungen im Laufenden Abschnitt genutzt NULL=aktueller Abschnitt 1=vor dem ersten Wechsel 2=vor dem zweiten Wechsel usw");

	/** Die Definition der Tabellenspalte Schulbesuchsjahre */
	public SchemaTabelleSpalte col_Schulbesuchsjahre = add("Schulbesuchsjahre", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Schulbesuchsjahre für den Lernabschnitt");

	/** Die Definition der Tabellenspalte Hochrechnung */
	public SchemaTabelleSpalte col_Hochrechnung = add("Hochrechnung", SchemaDatentypen.INT, false)
		.setJavaComment("Lernabschnitt ist Hochrechnung (nur noch BK)");

	/** Die Definition der Tabellenspalte SemesterWertung */
	public SchemaTabelleSpalte col_SemesterWertung = add("SemesterWertung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gewerteter Abschnitt (Ja/Nein)");

	/** Die Definition der Tabellenspalte PruefOrdnung */
	public SchemaTabelleSpalte col_PruefOrdnung = add("PruefOrdnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Prüfungsordnung des Lernabschnitts");

	/** Die Definition der Tabellenspalte Klassen_ID */
	public SchemaTabelleSpalte col_Klassen_ID = add("Klassen_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Klassen_ID des Schülers für den Lernabschnitt");

	/** Die Definition der Tabellenspalte Tutor_ID */
	public SchemaTabelleSpalte col_Tutor_ID = add("Tutor_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die Lehrer-ID des Tutors, der dem Schüler zugeordnet ist, oder null falls keine Zuordnung vorgenommen wurde");

	/** Die Definition der Tabellenspalte Verspaetet */
	public SchemaTabelleSpalte col_Verspaetet = add("Verspaetet", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("ID des Nachprüfungsfaches");

	/** Die Definition der Tabellenspalte NPV_Fach_ID */
	public SchemaTabelleSpalte col_NPV_Fach_ID = add("NPV_Fach_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("TODO: Note der Nachprüfung");

	/** Die Definition der Tabellenspalte NPV_NoteKrz */
	public SchemaTabelleSpalte col_NPV_NoteKrz = add("NPV_NoteKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("TODO: Datum der Nachprüfung");

	/** Die Definition der Tabellenspalte NPV_Datum */
	public SchemaTabelleSpalte col_NPV_Datum = add("NPV_Datum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("TODO: BK: ID des Nachprüfungsfaches für den allgemein-bildenen Abschluss");

	/** Die Definition der Tabellenspalte NPAA_Fach_ID */
	public SchemaTabelleSpalte col_NPAA_Fach_ID = add("NPAA_Fach_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("TODO: BK: Note des Nachprüfungsfaches für den allgemein-bildenen Abschluss");

	/** Die Definition der Tabellenspalte NPAA_NoteKrz */
	public SchemaTabelleSpalte col_NPAA_NoteKrz = add("NPAA_NoteKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("TODO: BK: Datum der Nachprüfung für den allgemein-bildenen Abschluss");

	/** Die Definition der Tabellenspalte NPAA_Datum */
	public SchemaTabelleSpalte col_NPAA_Datum = add("NPAA_Datum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("TODO: BK: dito für berufs-qualifizierende Nachprüfung");

	/** Die Definition der Tabellenspalte NPBQ_Fach_ID */
	public SchemaTabelleSpalte col_NPBQ_Fach_ID = add("NPBQ_Fach_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("TODO: BK: dito für berufs-qualifizierende Nachprüfung");

	/** Die Definition der Tabellenspalte NPBQ_NoteKrz */
	public SchemaTabelleSpalte col_NPBQ_NoteKrz = add("NPBQ_NoteKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("TODO: BK: dito für berufs-qualifizierende Nachprüfung");

	/** Die Definition der Tabellenspalte NPBQ_Datum */
	public SchemaTabelleSpalte col_NPBQ_Datum = add("NPBQ_Datum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("TODO: ID des Nachprüfungsfaches");

	/** Die Definition der Tabellenspalte VersetzungKrz */
	public SchemaTabelleSpalte col_VersetzungKrz = add("VersetzungKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Kürzel des Versetungsvermerk");

	/** Die Definition der Tabellenspalte AbschlussArt */
	public SchemaTabelleSpalte col_AbschlussArt = add("AbschlussArt", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Art des Abschlusses");

	/** Die Definition der Tabellenspalte AbschlIstPrognose */
	public SchemaTabelleSpalte col_AbschlIstPrognose = add("AbschlIstPrognose", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob Abschluss Prognose ist (GE, PS und SK)");

	/** Die Definition der Tabellenspalte Konferenzdatum */
	public SchemaTabelleSpalte col_Konferenzdatum = add("Konferenzdatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Konferenzdatum");

	/** Die Definition der Tabellenspalte ZeugnisDatum */
	public SchemaTabelleSpalte col_ZeugnisDatum = add("ZeugnisDatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Zeugnisdatum");

	/** Die Definition der Tabellenspalte Klassenlehrer */
	public SchemaTabelleSpalte col_Klassenlehrer = add("Klassenlehrer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Klassenlehrer Kürzel (hier muss ID rein)");

	/** Die Definition der Tabellenspalte ASDSchulgliederung */
	public SchemaTabelleSpalte col_ASDSchulgliederung = add("ASDSchulgliederung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaName("Schulgliederung")
		.setConverter(SchulgliederungKuerzelConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("ASD-Kürzel SGL");

	/** Die Definition der Tabellenspalte ASDJahrgang */
	public SchemaTabelleSpalte col_ASDJahrgang = add("ASDJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("ASD-Jahrgang kann alles über ID geregelt werden");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Jahrgangs der zum Report zugeordnet wird");

	/** Die Definition der Tabellenspalte Fachklasse_ID */
	public SchemaTabelleSpalte col_Fachklasse_ID = add("Fachklasse_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID der Fachklasse (BK)");

	/** Die Definition der Tabellenspalte Schwerpunkt_ID */
	public SchemaTabelleSpalte col_Schwerpunkt_ID = add("Schwerpunkt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Schwerpunkts aus dem Katalog");

	/** Die Definition der Tabellenspalte ZeugnisBem */
	public SchemaTabelleSpalte col_ZeugnisBem = add("ZeugnisBem", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Zeugnisbemerkung");

	/** Die Definition der Tabellenspalte Schwerbehinderung */
	public SchemaTabelleSpalte col_Schwerbehinderung = add("Schwerbehinderung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Schwerbehinderung (Ja/Nein)");

	/** Die Definition der Tabellenspalte Foerderschwerpunkt_ID */
	public SchemaTabelleSpalte col_Foerderschwerpunkt_ID = add("Foerderschwerpunkt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID Hauptförderschwerpunkt");

	/** Die Definition der Tabellenspalte OrgFormKrz */
	public SchemaTabelleSpalte col_OrgFormKrz = add("OrgFormKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Kürzel der Organisationsform");

	/** Die Definition der Tabellenspalte RefPaed */
	public SchemaTabelleSpalte col_RefPaed = add("RefPaed", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("TODO DEPRECATED: Reformpädagogik");

	/** Die Definition der Tabellenspalte Klassenart */
	public SchemaTabelleSpalte col_Klassenart = add("Klassenart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Klassenart");

	/** Die Definition der Tabellenspalte SumFehlStd */
	public SchemaTabelleSpalte col_SumFehlStd = add("SumFehlStd", SchemaDatentypen.INT, false)
		.setJavaComment("Summer der Fehlstunden");

	/** Die Definition der Tabellenspalte SumFehlStdU */
	public SchemaTabelleSpalte col_SumFehlStdU = add("SumFehlStdU", SchemaDatentypen.INT, false)
		.setJavaComment("Summer der Fehlstunden unentschuldigt");

	/** Die Definition der Tabellenspalte Wiederholung */
	public SchemaTabelleSpalte col_Wiederholung = add("Wiederholung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Lernabschnitt wurde wiederholt (Ja/Nein)");

	/** Die Definition der Tabellenspalte Gesamtnote_GS */
	public SchemaTabelleSpalte col_Gesamtnote_GS = add("Gesamtnote_GS", SchemaDatentypen.INT, false)
		.setConverter(NoteConverterFromInteger.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Lernbereichnote Gesellschaftswissenschaft oder Arbeitlehre HA10");

	/** Die Definition der Tabellenspalte Gesamtnote_NW */
	public SchemaTabelleSpalte col_Gesamtnote_NW = add("Gesamtnote_NW", SchemaDatentypen.INT, false)
		.setConverter(NoteConverterFromInteger.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Lernbereichnote Naturwissenschaft HA10");

	/** Die Definition der Tabellenspalte Folgeklasse_ID */
	public SchemaTabelleSpalte col_Folgeklasse_ID = add("Folgeklasse_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID der Folgeklasse für den Lernabschnitt, sofern dieser vom Standard der Klassentabelle abweicht");

	/** Die Definition der Tabellenspalte Foerderschwerpunkt2_ID */
	public SchemaTabelleSpalte col_Foerderschwerpunkt2_ID = add("Foerderschwerpunkt2_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Weiterer Förderschwerpunkt ID");

	/** Die Definition der Tabellenspalte Abschluss */
	public SchemaTabelleSpalte col_Abschluss = add("Abschluss", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("allgemeinbildender Abschluss");

	/** Die Definition der Tabellenspalte Abschluss_B */
	public SchemaTabelleSpalte col_Abschluss_B = add("Abschluss_B", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("berufsbezogener Abschnluss (BK)");

	/** Die Definition der Tabellenspalte DSNote */
	public SchemaTabelleSpalte col_DSNote = add("DSNote", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setJavaComment("Durchschnittsnote im betreffenden Abschnitt Ist allerdings in der Programmoberfläche nicht verfügbar Der Inhalt wird durch die Prüfungsalgorithmen gefüllt es ist eine Ausgabe in Reports möglich");

	/** Die Definition der Tabellenspalte AV_Leist */
	public SchemaTabelleSpalte col_AV_Leist = add("AV_Leist", SchemaDatentypen.INT, false)
		.setJavaComment("DEPRECATED: Kopfnote");

	/** Die Definition der Tabellenspalte AV_Zuv */
	public SchemaTabelleSpalte col_AV_Zuv = add("AV_Zuv", SchemaDatentypen.INT, false)
		.setJavaComment("DEPRECATED: Kopfnote");

	/** Die Definition der Tabellenspalte AV_Selbst */
	public SchemaTabelleSpalte col_AV_Selbst = add("AV_Selbst", SchemaDatentypen.INT, false)
		.setJavaComment("DEPRECATED: Kopfnote");

	/** Die Definition der Tabellenspalte SV_Verant */
	public SchemaTabelleSpalte col_SV_Verant = add("SV_Verant", SchemaDatentypen.INT, false)
		.setJavaComment("DEPRECATED: Kopfnote");

	/** Die Definition der Tabellenspalte SV_Konfl */
	public SchemaTabelleSpalte col_SV_Konfl = add("SV_Konfl", SchemaDatentypen.INT, false)
		.setJavaComment("DEPRECATED: Kopfnote");

	/** Die Definition der Tabellenspalte SV_Koop */
	public SchemaTabelleSpalte col_SV_Koop = add("SV_Koop", SchemaDatentypen.INT, false)
		.setJavaComment("DEPRECATED: Kopfnote");

	/** Die Definition der Tabellenspalte StvKlassenlehrer_ID */
	public SchemaTabelleSpalte col_StvKlassenlehrer_ID = add("StvKlassenlehrer_ID", SchemaDatentypen.BIGINT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Stellvertretender Klassenlehrer Kürzel sollte ID sein");

	/** Die Definition der Tabellenspalte MoeglNPFaecher */
	public SchemaTabelleSpalte col_MoeglNPFaecher = add("MoeglNPFaecher", SchemaDatentypen.TEXT, false)
		.setJavaComment("Auflistung der mögllichen Nachprüfungsfächer kommagetrennt");

	/** Die Definition der Tabellenspalte Zertifikate */
	public SchemaTabelleSpalte col_Zertifikate = add("Zertifikate", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("DEPRECATED: Hier war mal geplant, Texte für Zertifikate einzugeben");

	/** Die Definition der Tabellenspalte DatumFHR */
	public SchemaTabelleSpalte col_DatumFHR = add("DatumFHR", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Datum FHR");

	/** Die Definition der Tabellenspalte PruefAlgoErgebnis */
	public SchemaTabelleSpalte col_PruefAlgoErgebnis = add("PruefAlgoErgebnis", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für die Ergebnisse der Abschlussberechnungen");

	/** Die Definition der Tabellenspalte Zeugnisart */
	public SchemaTabelleSpalte col_Zeugnisart = add("Zeugnisart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Angabe der Zeugnisart");

	/** Die Definition der Tabellenspalte DatumVon */
	public SchemaTabelleSpalte col_DatumVon = add("DatumVon", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Beginn Lernabschnitt");

	/** Die Definition der Tabellenspalte DatumBis */
	public SchemaTabelleSpalte col_DatumBis = add("DatumBis", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Ende Lernabschnitt");

	/** Die Definition der Tabellenspalte FehlstundenGrenzwert */
	public SchemaTabelleSpalte col_FehlstundenGrenzwert = add("FehlstundenGrenzwert", SchemaDatentypen.INT, false)
		.setJavaComment("Grenzwert für Fehlstunden (BK Warnbriefe zur Entlassung)");

	/** Die Definition der Tabellenspalte Sonderpaedagoge_ID */
	public SchemaTabelleSpalte col_Sonderpaedagoge_ID = add("Sonderpaedagoge_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Hier kann die ID einer Lehrkraft eingetragen werden die dann die Schüler als Förderpädagoge betreut und im Notenmodul hat");

	/** Die Definition der Tabellenspalte FachPraktAnteilAusr */
	public SchemaTabelleSpalte col_FachPraktAnteilAusr = add("FachPraktAnteilAusr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setNotNull()
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Enthält die Angabe, ob die Fachpraktischen Anteile in Anlage B08 B09 B10 ausreichend sind für Versetzung (BK)");

	/** Die Definition der Tabellenspalte BilingualerZweig */
	public SchemaTabelleSpalte col_BilingualerZweig = add("BilingualerZweig", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Sprache des Bilingualen Zweigs");

	/** Die Definition der Tabellenspalte AOSF */
	public SchemaTabelleSpalte col_AOSF = add("AOSF", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob der Schüler ein AOSF hat");

	/** Die Definition der Tabellenspalte Autist */
	public SchemaTabelleSpalte col_Autist = add("Autist", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob Autismuss vorliegt (Ja/Nein)");

	/** Die Definition der Tabellenspalte ZieldifferentesLernen */
	public SchemaTabelleSpalte col_ZieldifferentesLernen = add("ZieldifferentesLernen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob der Schüler zieldifferent unterrichtet wird");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Schuljahr des Lernabschnitts");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Abschnitt des Lernabschnitts");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Klasse */
	public SchemaTabelleSpalte col_Klasse = add("Klasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Klassen-Bezeichnung für den Lernabschnitts");

	/** Die Definition der Tabellenspalte Folgeklasse */
	public SchemaTabelleSpalte col_Folgeklasse = add("Folgeklasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Bezeichnung der Folgeklasse für den Lernabschnitt des Schülers");


	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Schuljahreabschnitt_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Schuljahreabschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Fach_Nachpruefung_1_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Fach_Nachpruefung_1_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Fach_Nachpruefung_1_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_NPV_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Fach_Nachpruefung_2_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Fach_Nachpruefung_2_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Fach_Nachpruefung_2_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_NPAA_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Fach_Nachpruefung_3_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Fach_Nachpruefung_3_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Fach_Nachpruefung_3_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_NPBQ_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Fachklasse_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Fachklasse_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Fachklasse_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Fachklasse_ID, Schema.tab_EigeneSchule_Fachklassen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Foerderschwerpunkt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Foerderschwerpunkt_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Foerderschwerpunkt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Foerderschwerpunkt_ID, Schema.tab_K_Foerderschwerpunkt.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Foerderschwerpunkt2_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Foerderschwerpunkt2_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Foerderschwerpunkt2_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Foerderschwerpunkt2_ID, Schema.tab_K_Foerderschwerpunkt.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Jahrgang_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Jahrgang_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Jahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Klassen_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Klassen_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Klassen_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Klassen_ID, Schema.tab_Klassen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_FolgeKlasse_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_FolgeKlasse_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_FolgeKlasse_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Folgeklasse_ID, Schema.tab_Klassen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Schueler_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Schwerpunkt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Schwerpunkt_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Schwerpunkt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Schwerpunkt_ID, Schema.tab_K_Schwerpunkt.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Sonderpaedagoge_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Sonderpaedagoge_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Sonderpaedagoge_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Sonderpaedagoge_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerLernabschnittsdaten_Tutor_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLernabschnittsdaten_Tutor_FK = addForeignKey(
			"SchuelerLernabschnittsdaten_Tutor_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Tutor_ID, Schema.tab_K_Lehrer.col_ID)
		);
 
	/** Die Definition des Unique-Index SchuelerLernabschnittsdaten_UC1 */
	public SchemaTabelleUniqueIndex unique_SchuelerLernabschnittsdaten_UC1 = addUniqueIndex("SchuelerLernabschnittsdaten_UC1", 
			col_Schueler_ID, 
			col_Schuljahresabschnitts_ID, 
			col_WechselNr
		)
		.setRevision(SchemaRevisionen.REV_1);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerLernabschnittsdaten.
	 */
	public Tabelle_SchuelerLernabschnittsdaten() {
		super("SchuelerLernabschnittsdaten", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerLernabschnittsdaten");
		setJavaComment("Lernabschnitte zum Schüler");
	}

}
