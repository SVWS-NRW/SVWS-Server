package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.NoteConverterFromNotenpunkte;
import de.svws_nrw.db.converter.current.NoteConverterFromNotenpunkteString;
import de.svws_nrw.db.converter.current.gost.AbiturBelegungsartConverter;
import de.svws_nrw.db.converter.current.gost.AbiturKursMarkierungConverter;
import de.svws_nrw.db.converter.current.gost.GOStAbiturFachConverter;
import de.svws_nrw.db.converter.current.gost.GOStKursartConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerAbiFaecher.
 */
public class Tabelle_SchuelerAbiFaecher extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eine eindeutige ID für die Abitur-Fach-Informationen des Schülers");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Schülers – verweist auf den Schüler");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Faches – verweist auf das Fach");

	/** Die Definition der Tabellenspalte FachKrz */
	public SchemaTabelleSpalte col_FachKrz = add("FachKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaName("FachKuerzel")
		.setJavaComment("Das Kürzel des Faches");

	/** Die Definition der Tabellenspalte FSortierung */
	public SchemaTabelleSpalte col_FSortierung = add("FSortierung", SchemaDatentypen.INT, false)
		.setJavaName("-")
		.setJavaComment("Eine Zahl, welche die Sortierung der Fächer angibt");

	/** Die Definition der Tabellenspalte Kurs_ID */
	public SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Letzer Kurs Q2, 2.Hj: Die Kurs ID - verweist auf Kurse");

	/** Die Definition der Tabellenspalte KursartAllg */
	public SchemaTabelleSpalte col_KursartAllg = add("KursartAllg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaName("KursartAllgemein")
		.setConverter(GOStKursartConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Die allgemeine Kursart des Faches (z.B. GK, LK)");

	/** Die Definition der Tabellenspalte Fachlehrer_ID */
	public SchemaTabelleSpalte col_Fachlehrer_ID = add("Fachlehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Letzer Kurs Q2, 2.Hj: Die ID des Fachlehrers, der als letztes in diesem Fach unterrichtet hat");

	/** Die Definition der Tabellenspalte AbiFach */
	public SchemaTabelleSpalte col_AbiFach = add("AbiFach", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaName("AbiturFach")
		.setConverter(GOStAbiturFachConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Gibt an, ob das Fach bei der Abiturprüfung gewählt wurde und dann die Nummer des Abiturfaches (1-4)");

	/** Die Definition der Tabellenspalte P11_1 */
	public SchemaTabelleSpalte col_P11_1 = add("P11_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaName("EF_HJ1_Notenpunkte")
		.setConverter(NoteConverterFromNotenpunkteString.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("EF, 1. HJ: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde");

	/** Die Definition der Tabellenspalte S11_1 */
	public SchemaTabelleSpalte col_S11_1 = add("S11_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("EF_HJ1_BelegungArt")
		.setConverter(AbiturBelegungsartConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("EF, 1. HJ: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich)");

	/** Die Definition der Tabellenspalte P11_2 */
	public SchemaTabelleSpalte col_P11_2 = add("P11_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaName("EF_HJ2_Notenpunkte")
		.setConverter(NoteConverterFromNotenpunkteString.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("EF, 2. HJ: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde");

	/** Die Definition der Tabellenspalte S11_2 */
	public SchemaTabelleSpalte col_S11_2 = add("S11_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("EF_HJ2_BelegungArt")
		.setConverter(AbiturBelegungsartConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("EF, 2. HJ: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich)");

	/** Die Definition der Tabellenspalte P_FA */
	public SchemaTabelleSpalte col_P_FA = add("P_FA", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaName("Facharbeit_Notenpunkte")
		.setConverter(NoteConverterFromNotenpunkteString.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("BK: eingebrachte Facharbeit – Notenpunkte");

	/** Die Definition der Tabellenspalte R_FA */
	public SchemaTabelleSpalte col_R_FA = add("R_FA", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Facharbeit_MarkiertFuerAbiturBerechnung")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("BK: eingebrachte Facharbeit – Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt)");

	/** Die Definition der Tabellenspalte W12_1 */
	public SchemaTabelleSpalte col_W12_1 = add("W12_1", SchemaDatentypen.INT, false)
		.setJavaName("Q1_HJ1_Wochenstunden")
		.setJavaComment("Q1, 1. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr");

	/** Die Definition der Tabellenspalte P12_1 */
	public SchemaTabelleSpalte col_P12_1 = add("P12_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaName("Q1_HJ1_Notenpunkte")
		.setConverter(NoteConverterFromNotenpunkteString.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q1, 1. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde");

	/** Die Definition der Tabellenspalte H12_1 */
	public SchemaTabelleSpalte col_H12_1 = add("H12_1", SchemaDatentypen.INT, false)
		.setJavaName("-")
		.setJavaComment("BK: Q1, 1. Hj: Speichert die User_ID von einem Benutzer, welche eine Proberechnung mit fiktiven Daten durchführt");

	/** Die Definition der Tabellenspalte R12_1 */
	public SchemaTabelleSpalte col_R12_1 = add("R12_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Q1_HJ1_MarkiertFuerAbiturBerechnung")
		.setConverter(AbiturKursMarkierungConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q1, 1. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt)");

	/** Die Definition der Tabellenspalte S12_1 */
	public SchemaTabelleSpalte col_S12_1 = add("S12_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Q1_HJ1_BelegungArt")
		.setConverter(AbiturBelegungsartConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q1, 1. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK)");

	/** Die Definition der Tabellenspalte W12_2 */
	public SchemaTabelleSpalte col_W12_2 = add("W12_2", SchemaDatentypen.INT, false)
		.setJavaName("Q1_HJ2_Wochenstunden")
		.setJavaComment("Q1, 2. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr");

	/** Die Definition der Tabellenspalte P12_2 */
	public SchemaTabelleSpalte col_P12_2 = add("P12_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaName("Q1_HJ2_Notenpunkte")
		.setConverter(NoteConverterFromNotenpunkteString.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q1, 2. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde");

	/** Die Definition der Tabellenspalte H12_2 */
	public SchemaTabelleSpalte col_H12_2 = add("H12_2", SchemaDatentypen.INT, false)
		.setJavaName("-")
		.setJavaComment("BK: Q1, 2. Hj: Speichert die User_ID von einem Benutzer, welche eine Proberechnung mit fiktiven Daten durchführt");

	/** Die Definition der Tabellenspalte R12_2 */
	public SchemaTabelleSpalte col_R12_2 = add("R12_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Q1_HJ2_MarkiertFuerAbiturBerechnung")
		.setConverter(AbiturKursMarkierungConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q1, 2. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt)");

	/** Die Definition der Tabellenspalte S12_2 */
	public SchemaTabelleSpalte col_S12_2 = add("S12_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Q1_HJ2_BelegungArt")
		.setConverter(AbiturBelegungsartConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q1, 2. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK)");

	/** Die Definition der Tabellenspalte W13_1 */
	public SchemaTabelleSpalte col_W13_1 = add("W13_1", SchemaDatentypen.INT, false)
		.setJavaName("Q2_HJ1_Wochenstunden")
		.setJavaComment("Q2, 1. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr");

	/** Die Definition der Tabellenspalte P13_1 */
	public SchemaTabelleSpalte col_P13_1 = add("P13_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaName("Q2_HJ1_Notenpunkte")
		.setConverter(NoteConverterFromNotenpunkteString.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q2, 1. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde");

	/** Die Definition der Tabellenspalte H13_1 */
	public SchemaTabelleSpalte col_H13_1 = add("H13_1", SchemaDatentypen.INT, false)
		.setJavaName("-")
		.setJavaComment("BK: Q2, 1. Hj: Speichert die User_ID von einem Benutzer, welche eine Proberechnung mit fiktiven Daten durchführt");

	/** Die Definition der Tabellenspalte R13_1 */
	public SchemaTabelleSpalte col_R13_1 = add("R13_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Q2_HJ1_MarkiertFuerAbiturBerechnung")
		.setConverter(AbiturKursMarkierungConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q2, 1. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt)");

	/** Die Definition der Tabellenspalte S13_1 */
	public SchemaTabelleSpalte col_S13_1 = add("S13_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Q2_HJ1_BelegungArt")
		.setConverter(AbiturBelegungsartConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q2, 1. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK)");

	/** Die Definition der Tabellenspalte W13_2 */
	public SchemaTabelleSpalte col_W13_2 = add("W13_2", SchemaDatentypen.INT, false)
		.setJavaName("Q2_HJ2_Wochenstunden")
		.setJavaComment("Q2,21. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr");

	/** Die Definition der Tabellenspalte P13_2 */
	public SchemaTabelleSpalte col_P13_2 = add("P13_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaName("Q2_HJ2_Notenpunkte")
		.setConverter(NoteConverterFromNotenpunkteString.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q2, 2. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde");

	/** Die Definition der Tabellenspalte H13_2 */
	public SchemaTabelleSpalte col_H13_2 = add("H13_2", SchemaDatentypen.INT, false)
		.setJavaName("-")
		.setJavaComment("BK: Q2, 2. Hj: Speichert die User_ID von einem Benutzer, welche eine Proberechnung mit fiktiven Daten durchführt");

	/** Die Definition der Tabellenspalte R13_2 */
	public SchemaTabelleSpalte col_R13_2 = add("R13_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Q2_HJ2_MarkiertFuerAbiturBerechnung")
		.setConverter(AbiturKursMarkierungConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q2, 2. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt)");

	/** Die Definition der Tabellenspalte S13_2 */
	public SchemaTabelleSpalte col_S13_2 = add("S13_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("Q2_HJ2_BelegungArt")
		.setConverter(AbiturBelegungsartConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Q2, 2. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK)");

	/** Die Definition der Tabellenspalte Zulassung */
	public SchemaTabelleSpalte col_Zulassung = add("Zulassung", SchemaDatentypen.SMALLINT, false)
		.setJavaName("ZulassungPunktsumme")
		.setJavaComment("Die Punkte für das Fach für die Abiturzulassung, NULL falls kein Kurs des Faches eingeht");

	/** Die Definition der Tabellenspalte Durchschnitt */
	public SchemaTabelleSpalte col_Durchschnitt = add("Durchschnitt", SchemaDatentypen.FLOAT, false)
		.setJavaName("ZulassungNotenpunktdurchschnitt")
		.setJavaComment("Der Notendurchschnitt, falls das Fach eines der ersten drei Abiturfächer ist");

	/** Die Definition der Tabellenspalte AbiPruefErgebnis */
	public SchemaTabelleSpalte col_AbiPruefErgebnis = add("AbiPruefErgebnis", SchemaDatentypen.SMALLINT, false)
		.setJavaName("PruefungNotenpunkte")
		.setConverter(NoteConverterFromNotenpunkte.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Die Notenpunkte aus der Abiturprüfung, falls das Fach eines der vier Abiturfächer ist");

	/** Die Definition der Tabellenspalte Zwischenstand */
	public SchemaTabelleSpalte col_Zwischenstand = add("Zwischenstand", SchemaDatentypen.SMALLINT, false)
		.setJavaName("PruefungPunktsummeZwischenstand")
		.setJavaComment("Die Notenpunkte aus der Abiturprüfung multipliziert mit dem entsprechenden Faktor, falls das Fach eines der vier Abiturfächer ist (hier wird z.B. die besondere Lernleistung berücksichtigt");

	/** Die Definition der Tabellenspalte MdlPflichtPruefung */
	public SchemaTabelleSpalte col_MdlPflichtPruefung = add("MdlPflichtPruefung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("PruefungMuendlichAbweichung")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("true, falls eine mündliche Abweichungsprüfung in einem der ersten drei Abiturfächer nötig ist.");

	/** Die Definition der Tabellenspalte MdlBestPruefung */
	public SchemaTabelleSpalte col_MdlBestPruefung = add("MdlBestPruefung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("PruefungMuendlichBestehen")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("true, falls eine mündliche Bestehensprüfung in den ersten drei Abiturfächern notwendig ist, damit das Abitur noch bestanden werden kann.");

	/** Die Definition der Tabellenspalte MdlFreiwPruefung */
	public SchemaTabelleSpalte col_MdlFreiwPruefung = add("MdlFreiwPruefung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("PruefungMuendlichFreiwillig")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("true, falls eine freiwillige Prüfung in einem der ersten drei Abiturfächer stattfindet.");

	/** Die Definition der Tabellenspalte MdlPruefErgebnis */
	public SchemaTabelleSpalte col_MdlPruefErgebnis = add("MdlPruefErgebnis", SchemaDatentypen.SMALLINT, false)
		.setJavaName("PruefungMuendlichNotenpunkte")
		.setConverter(NoteConverterFromNotenpunkte.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("enthält die Notenpunkte aus der mündlichen Abiturprüfung in einem der ersten drei Abiturfächer, falls diese durchgeführt wird, ansonsten NULL");

	/** Die Definition der Tabellenspalte MdlPruefFolge */
	public SchemaTabelleSpalte col_MdlPruefFolge = add("MdlPruefFolge", SchemaDatentypen.SMALLINT, false)
		.setJavaName("PruefungMuendlichReihenfolge")
		.setJavaComment("enthält die Reihenfolge für mündliche Prüfungen in den ersten drei Abiturfächern, falls mehrere angesetzt werden (1, 2, 3)");

	/** Die Definition der Tabellenspalte AbiErgebnis */
	public SchemaTabelleSpalte col_AbiErgebnis = add("AbiErgebnis", SchemaDatentypen.SMALLINT, false)
		.setJavaName("PruefungPunktsummeGesamt")
		.setJavaComment("Die Notenpunkte aus der Abiturprüfung multipliziert mit dem entsprechenden Faktor (hier wird z.B. die besondere Lernleistung) und unter Einbeziehung einer möglichen mündlicher Prüfung");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Die Nummer der Schule, zu der dieser Datensatz gehört (falls mehrere Schulen mit dem gleichen Schema arbeiten)");

	/** Die Definition der Tabellenspalte Fachlehrer */
	public SchemaTabelleSpalte col_Fachlehrer = add("Fachlehrer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Letzer Kurs Q2, 2.Hj: Der Fachlehrer, der als letztes in diesem Fach unterrichtet hat");


	/** Die Definition des Fremdschlüssels SchuelerAbiFaecher_Fach_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAbiFaecher_Fach_FK = addForeignKey(
			"SchuelerAbiFaecher_Fach_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerAbiFaecher_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAbiFaecher_Schueler_FK = addForeignKey(
			"SchuelerAbiFaecher_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerAbiFaecher_Kurs_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAbiFaecher_Kurs_FK = addForeignKey(
			"SchuelerAbiFaecher_Kurs_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Kurs_ID, Schema.tab_Kurse.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerAbiFaecher.
	 */
	public Tabelle_SchuelerAbiFaecher() {
		super("SchuelerAbiFaecher", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler.abitur");
		setJavaClassName("DTOSchuelerAbiturFach");
		setJavaComment("Abiturfächer zum Schüler");
	}

}
