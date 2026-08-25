import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingReportvorlageKonfigurationStundenplanung } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationStundenplanung';
import { ReportingReportvorlageParameter } from '../../../core/data/reporting/ReportingReportvorlageParameter';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { ReportingReportvorlageDatenContext } from '../../../core/types/reporting/ReportingReportvorlageDatenContext';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { ReportingReportvorlageKonfigurationKurse } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationKurse';
import { ArrayList } from '../../../java/util/ArrayList';
import { ReportingReportvorlageUtils } from '../../../core/utils/reporting/ReportingReportvorlageUtils';
import { ReportingReportvorlageKonfigurationLehrer } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationLehrer';
import { JavaString } from '../../../java/lang/JavaString';
import { ReportingReportvorlageKonfigurationGost } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationGost';
import { ReportingParameter } from '../../../core/data/reporting/ReportingParameter';
import { ReportingReportvorlageKonfigurationSchueler } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationSchueler';
import { BenutzerKompetenz } from '../../../core/types/benutzer/BenutzerKompetenz';
import { ReportingReportvorlageParameterGruppe } from '../../../core/data/reporting/ReportingReportvorlageParameterGruppe';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';
import { ReportingReportvorlageKonfigurationKlassen } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationKlassen';
import { HashSet } from '../../../java/util/HashSet';

export class ReportingReportvorlage extends JavaEnum<ReportingReportvorlage> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingReportvorlage> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingReportvorlage> = new Map<string, ReportingReportvorlage>();

	/**
	 * Report-Vorlage: GOSt - Klausurplanung - Klausurtermine-Kurse
	 */
	public static readonly GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN", 0, "GostKlausurplanung-KlausurtermineMitKursen", "Klausurplan der Kurse", "Einen Plan mit den Klausurterminen der Kurse erzeugen.", ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_TERMINE, "gost/klausurplanung/GostKlausurplanungKlausurtermineMitKursen.html", "GOSt-Klausurplanung-Klausurtermine-Kurse", ArrayList.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationGost.getGostKlausurplanungVKlausurtermineMitKursen());

	/**
	 * Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren
	 */
	public static readonly GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN", 1, "GostKlausurplanung-SchuelerMitKlausuren", "Klausurplan der Schülerinnen und Schüler", "Einen Plan mit den Klausurterminen der Schülerinnen und Schüler erzeugen.", ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_SCHUELER, "gost/klausurplanung/GostKlausurplanungSchuelerMitKlausuren.html", "GOSt-Klausurplanung-Schueler-Klausuren", ArrayList.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationGost.getGostKlausurplanungVSchuelerMitKlausuren());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler
	 */
	public static readonly GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN", 2, "GostKursplanung-KursMitKursschuelern", "Kursliste", "Eine Liste mit den Schülerinnen und Schülern der Kurse aus der GOSt-Kursplaung erzeugen.", ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE, "gost/kursplanung/GostKursplanungKursMitKursschuelern.html", "GOSt-Blockungsergebnis-Kurs-Schueler", ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationGost.getGostKursplanungVKursMitKursschuelern());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte
	 */
	public static readonly GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN", 3, "GostKursplanung-KurseMitStatistikwerten", "Kursstatistik", "Eine Liste mit den Kursen aus der GOSt-Kursplanung und ihren Statistikwerten erzeugen.", ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE, "gost/kursplanung/GostKursplanungKurseMitStatistikwerten.html", "GOSt-Blockungsergebnis-Kurse-Statistikwerte", ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationGost.getGostKursplanungVKurseMitStatistikwerten());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN", 4, "GostKursplanung-SchuelerMitKursen", "Kurszuordnungen der Schülerinnen und Schüler", "Eien Übersicht mit den einzelnen Kurszuorndungen der Schülerinnen und Schüler aus der GOSt-Kursplanung erzeugen.", ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER, "gost/kursplanung/GostKursplanungSchuelerMitKursen.html", "GOSt-Blockungsergebnis-Schueler-Kurse", ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationGost.getGostKursplanungVSchuelerMitKursen());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN", 5, "GostKursplanung-SchuelerMitSchienenKursen", "Kurs-Schienen-Zuordnungen der Schülerinnen und Schüler", "Eine Übersicht mit den einzelnen Kurszuordnungen und deeren Schienen für die Schülerinnen und Schüler aus der GOSt-Kursplanung erzeugen.", ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER, "gost/kursplanung/GostKursplanungSchuelerMitSchienenKursen.html", "GOSt-Blockungsergebnis-Schueler-Schienen-Kurse", ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationGost.getGostKursplanungVSchuelerMitSchienenKursen());

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken
	 */
	public static readonly GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN", 6, "GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken", "Fachwahlstatistiken", "Eine statische Übersicht der Fachwahlen eines Abiturjahrgangs aus der GOSt-Laufbahnplanung erzeugen.", ReportingReportvorlageDatenContext.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG, "gost/laufbahnplanung/GostLaufbahnplanungAbiturjahrgangFachwahlstatistiken.html", "GOSt-Laufbahnplanung-Abiturjahrgang-Fachwahlstatistiken", ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationGost.getGostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken());

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Fotos - Namen
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN", 7, "Klasse-Liste-Schueler-Fotos-Namen", "Fotoübersicht klassenweise", "Eine Übersicht mit den Fotos der Schülerinnen und Schüler der Klassen erzeugen oder versenden.", ReportingReportvorlageDatenContext.KLASSEN, "klassen/KlasseListeSchuelerFotosNamen.html", "Klasse-Liste-Schueler-Fotos-Namen", ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerFotosNamen());

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 8, "Klasse-Liste-Schueler-Kontaktdaten-Erzieher", "Klassenliste mit Kontaktdaten", "Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler der Klassen erzeugen oder versenden.", ReportingReportvorlageDatenContext.KLASSEN, "klassen/KlasseListeSchuelerKontaktdatenErzieher.html", "Klasse-Liste-Schueler-Kontaktdaten-Erzieher", ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerKontaktdatenerzieher());

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN", 9, "Klasse-Liste-Schueler-Leistungsdaten", "Leistungsübersicht klassenweise", "Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der Klassen erzeugen.", ReportingReportvorlageDatenContext.KLASSEN, "klassen/leistungsdaten/KlasseListeSchuelerLeistungsdaten.html", "Klassen-Liste-Schueler-Leistungsdaten", ArrayList.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN), ArrayList.of(), ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerLeistungsdaten());

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten - Detailliert
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT", 10, "Klasse-Liste-Schueler-Leistungsdaten-Detailliert", "Leistungsübersicht klassenweise (detailliert)", "Eine detaillierte Übersicht der Leistungsdaten der Schülerinnen und Schüler der Klassen erzeugen.", ReportingReportvorlageDatenContext.KLASSEN, "klassen/leistungsdaten/KlasseListeSchuelerLeistungsdatenDetailliert.html", "Klassen-Liste-Schueler-Leistungsdaten-Detailliert", ArrayList.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN), ArrayList.of(), ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerLeistungsdatenDetailliert());

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 11, "Kurs-Liste-Schueler-Kontaktdaten-Erzieher", "Kursliste mit Kontaktdaten", "Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler der Kurse erzeugen oder versenden.", ReportingReportvorlageDatenContext.KURSE, "kurse/KursListeSchuelerKontaktdatenErzieher.html", "Kurs-Liste-Schueler-Kontaktdaten-Erzieher", ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerKontaktdatenerzieher());

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Fotos - Namen
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_FOTOS_NAMEN: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_FOTOS_NAMEN", 12, "Kurs-Liste-Schueler-Fotos-Namen", "Fotoübersicht kursweise", "Eine Übersicht mit den Fotos der Schülerinnen und Schüler der Kurse erzeugen oder versenden.", ReportingReportvorlageDatenContext.KURSE, "kurse/KursListeSchuelerFotosNamen.html", "Kurs-Liste-Schueler-Fotos-Namen", ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerFotosNamen());

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN", 13, "Kurs-Liste-Schueler-Leistungsdaten", "Leistungsübersicht kursweise", "Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der Kurse erzeugen.", ReportingReportvorlageDatenContext.KURSE, "kurse/leistungsdaten/KursListeSchuelerLeistungsdaten.html", "Kurs-Liste-Schueler-Leistungsdaten", ArrayList.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN), ArrayList.of(), ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerLeistungsdaten());

	/**
	 * Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten
	 */
	public static readonly LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN", 14, "Lehrer-Liste-Schueler-Leistungsdaten", "Leistungsdaten der Lerngruppen", "Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der ausgewählten Lehrkräfte nach Lerngruppen erzeugen", ReportingReportvorlageDatenContext.LEHRER, "lehrer/leistungsdaten/LehrerListeSchuelerLeistungsdaten.html", "Lehrer-Liste-Schueler-Leistungsdaten", ArrayList.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN), ArrayList.of(), ReportingReportvorlageKonfigurationLehrer.getLehrerVListeSchuelerLeistungsdaten());

	/**
	 * Report-Vorlage: Lehrer - Stammdaten - Liste
	 */
	public static readonly LEHRER_V_STAMMDATENLISTE: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_STAMMDATENLISTE", 15, "Lehrer-Stammdatenliste", "Stammdatenliste der Lehrkräfte", "Stammdatenliste der Lehrkräfte erzeugen.", ReportingReportvorlageDatenContext.LEHRER, "lehrer/stammdaten/LehrerStammdatenliste.html", "Lehrer-Stammdatenliste", ArrayList.of(BenutzerKompetenz.LEHRERDATEN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationLehrer.getLehrerVStammdatenliste());

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A4
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4", 16, "Schueler-GostAbiturApoAnlage12-A4", "APO-GOSt - Anlage 12 - Abiturzeugnis (DIN-A4)", "Erzeugt das Abiturzeugnis des Schülerinnen und Schüler gemäß APO-GOSt Anlage 12", ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR, "schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A4.html", "APO-GOSt-Anlage12", ArrayList.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostAbiturApoAnlage12A4());

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A3
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3", 17, "Schueler-GostAbiturApoAnlage12-A3", "APO-GOSt - Anlage 12 - Abiturzeugnis (DIN-A3)", "Erzeugt das Abiturzeugnis des Schülerinnen und Schüler gemäß APO-GOSt Anlage 12", ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR, "schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A3.html", "APO-GOSt-Anlage12", ArrayList.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostAbiturApoAnlage12A3());

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT", 18, "Schueler-GostLaufbahnplanungErgebnisuebersicht", "Ergebnisübersicht der GOSt-Laufbahnplanung", "Ergebnisübersicht der GOSt-Laufbahnplanung nach Schülerinnen und Schüler für Beratungslehrkräfte erzeugen.", ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG, "schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungErgebnisuebersicht.html", "GOSt-Laufbahnplanung-Pruefungsergebnisse", ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostLaufbahnplanungErgebnisuebersicht());

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN", 19, "Schueler-GostLaufbahnplanungWahlbogen", "GOST-Laufbahnwahlbogen", "Die GOST-Laufbahnwahlbögen für Schülerinnen und Schüler erzeugen oder versenden.", ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG, "schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungWahlbogen.html", "GOSt-Laufbahnplanung-Wahlboegen", ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN), ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF), ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostLaufbahnplanungWahlbogen());

	/**
	 * Report-Vorlage: Schüler - Schulbescheinigung
	 */
	public static readonly SCHUELER_V_SCHULBESCHEINIGUNG: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_SCHULBESCHEINIGUNG", 20, "Schueler-Schulbescheinigung", "Schulbescheinigung", "Eine Schulbescheinigung für Schülerinnen und Schüler oder deren Erziehungsberechtigte erzeugen.", ReportingReportvorlageDatenContext.SCHUELER, "schueler/anschreiben/SchuelerSchulbescheinigung.html", "Schueler-Schulbescheinigung", ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationSchueler.getSchuelerVSchulbescheinigung());

	/**
	 * Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher
	 */
	public static readonly SCHUELER_V_LISTE_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_LISTE_KONTAKTDATENERZIEHER", 21, "Schueler-Liste-Kontaktdaten-Erzieher", "Schülerliste mit Kontaktdaten", "Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler erzeugen oder versenden.", ReportingReportvorlageDatenContext.SCHUELER, "schueler/listen/SchuelerListeKontaktdatenErzieher.html", "Schueler-Liste-Kontaktdaten-Erzieher", ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationSchueler.getSchuelerVListeKontaktdatenerzieher());

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_FACH_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_FACH_STUNDENPLAN", 22, "Stundenplanung-FachStundenplan", "Fach-Stundenplan", "Den ausgewählten Stundenplan für die ausgewählten Fächer erzeugen oder versenden.", ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH, "stundenplanung/StundenplanungFachStundenplan.html", "Fach-Stundenplan", ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVFachStundenplan());

	/**
	 * Report-Vorlage: Stundenplanung - Klasse - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN", 23, "Stundenplanung-KlassenStundenplan", "Klassen-Stundenplan", "Den ausgewählten Stundenplan für die ausgewählten Klassen erzeugen oder versenden.", ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN, "stundenplanung/StundenplanungKlassenStundenplan.html", "Klassen-Stundenplan", ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVKlassenStundenplan());

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN", 24, "Stundenplanung-LehrerStundenplan", "Lehrer-Stundenplan", "Den ausgewählten Stundenplan für die ausgewählten Lehrkräfte erzeugen oder versenden.", ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER, "stundenplanung/StundenplanungLehrerStundenplan.html", "Lehrer-Stundenplan", ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVLehrerStundenplan());

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT", 25, "Stundenplanung-LehrerStundenplanKombiniert", "Lehrer-Stundenplan kombiniert", "Den ausgewählten Stundenplan für die ausgewählten Lehrkräfte in einer kombinierten Ansicht erzeugen", ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER, "stundenplanung/StundenplanungLehrerStundenplanKombiniert.html", "Lehrer-Stundenplan-Kombiniert", ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVLehrerStundenplanKombiniert());

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_RAUM_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_RAUM_STUNDENPLAN", 26, "Stundenplanung-RaumStundenplan", "Raum-Stundenplan", "Den ausgewählten Stundenplan für die ausgewählten Räume erzeugen.", ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM, "stundenplanung/StundenplanungRaumStundenplan.html", "Raum-Stundenplan", ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVRaumStundenplan());

	/**
	 * Report-Vorlage: Stundenplanung - Schüler - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN", 27, "Stundenplanung-SchuelerStundenplan", "Schüler-Stundenplan", "Den ausgewählten Stundenplan für die ausgewählten Schülerinnen und Schüler erzeugen oder versenden.", ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER, "stundenplanung/StundenplanungSchuelerStundenplan.html", "Schueler-Stundenplan", ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN), ArrayList.of(), ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVSchuelerStundenplan());

	/**
	 * Die Bezeichnung der Report-Vorlage
	 */
	private readonly bezeichnung: string;

	/**
	 * Der Titel, der in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Titel.
	 */
	private readonly uiTitel: string;

	/**
	 * Die Beschreibung, die in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Beschreibung.
	 */
	private readonly uiBeschreibung: string;

	/**
	 * Der Daten-Context, der für die HTML-Template-Datei verwendet wird.
	 */
	private readonly datenContext: ReportingReportvorlageDatenContext;

	/**
	 * Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad.
	 */
	private readonly pfadHtmlTemplate: string;

	/**
	 * Der statische Dateiname ohne Dateiendung, der bei der Ausgabe als ZIP-Datei verwendet wird.
	 */
	private readonly dateiname: string;

	/**
	 * Die Liste mit Benutzerkompetenzen (als OR-Verknüpfung) gemäß {@link BenutzerKompetenz}, die zur Nutzung des Templates erforderlich sind.
	 */
	private readonly benutzerKompetenzen: List<BenutzerKompetenz>;

	/**
	 * Die Schulformen, an denen die Report-Vorlage genutzt werden darf. Eine leere Liste gilt für alle Schulformen.
	 */
	private readonly schulformen: List<Schulform>;

	/**
	 * Reporting-Parameter inkl. der gültigen Vorlage-Parametergruppen für diese Report-Vorlage.
	 */
	private readonly reportingParameter: ReportingParameter;

	/**
	 * Interne Map für direkten Zugriff auf Parameter: Reportvorlage > Parametername > Parameter
	 */
	private static readonly MAP_PARAMETER: JavaMap<string, JavaMap<string, ReportingReportvorlageParameter>> = new HashMap<string, JavaMap<string, ReportingReportvorlageParameter>>();

	/**
	 * Interne Map für direkten Zugriff auf Parametergruppen: Reportvorlage > Parametergruppe > Parameterliste
	 */
	private static readonly MAP_PARAMETERGRUPPEN: JavaMap<string, JavaMap<string, ReportingReportvorlageParameterGruppe>> = new HashMap<string, JavaMap<string, ReportingReportvorlageParameterGruppe>>();

	/**
	 * Interner Index, der Namensbestandteile der Enum mit den passenden Reportvorlagen verwaltet.
	 */
	private static readonly MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN: JavaMap<string, List<ReportingReportvorlage>> = new HashMap<string, List<ReportingReportvorlage>>();

	/**
	 *  Der Präfix des Config-Schlüssels, unter dem die benutzerspezifischen Einstellungen einer Report-Vorlage in der Client-Konfiguration gespeichert werden.
	 *  Vervollständigt wird der Schlüssel je Report-Vorlage um deren {@link #bezeichnung} (siehe {@link #getConfigKeyBenutzerVorlage()}).
	 */
	private static readonly CONFIG_KEY_PREFIX_BENUTZER_VORLAGE: string = "reporting.einstellungen.benutzer.vorlage.";

	/**
	 * Gibt an, ob die internen Indexstrukturen bereits initialisiert wurden.
	 */
	private static mapsInitialisiert: boolean = false;

	/**
	 * Konstruktor für eine Reporting-Reportvorlage.
	 *
	 * @param bezeichnung         Die Bezeichnung der Reportvorlage.
	 * @param uiTitel             Der Titel, der in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Titel.
	 * @param uiBeschreibung      Die Beschreibung, die in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Beschreibung.
	 * @param datenContext        Der Hauptdaten-Context, der für die HTML-Template-Datei verwendet wird.
	 * @param pfadHtmlTemplate    Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad.
	 * @param dateiname           Der statische Dateiname ohne Dateiendung.
	 * @param benutzerKompetenzen Die Liste mit Benutzerkompetenzen.
	 * @param schulformen         Die Schulformen, an denen die Vorlage genutzt werden darf. Eine leere Liste gilt für alle Schulformen.
	 * @param reportingParameter  Eine Liste mit den Vorlage-Parametern, basierend auf der jeweiligen Definition.
	 */
	private constructor(name: string, ordinal: number, bezeichnung: string, uiTitel: string, uiBeschreibung: string, datenContext: ReportingReportvorlageDatenContext, pfadHtmlTemplate: string, dateiname: string, benutzerKompetenzen: List<BenutzerKompetenz>, schulformen: List<Schulform>, reportingParameter: ReportingParameter) {
		super(name, ordinal);
		ReportingReportvorlage.all_values_by_ordinal.push(this);
		ReportingReportvorlage.all_values_by_name.set(name, this);
		this.bezeichnung = bezeichnung;
		this.uiTitel = uiTitel;
		this.uiBeschreibung = uiBeschreibung;
		reportingParameter.reportvorlage = bezeichnung;
		this.datenContext = datenContext;
		this.pfadHtmlTemplate = pfadHtmlTemplate;
		this.dateiname = dateiname;
		this.benutzerKompetenzen = benutzerKompetenzen;
		this.schulformen = schulformen;
		this.reportingParameter = reportingParameter;
	}

	/**
	 * Gibt die Bezeichnung dieser Report-Vorlage zurück
	 *
	 * @return Die Bezeichnung dieser Report-Vorlage
	 */
	public getBezeichnung(): string {
		return (this.bezeichnung !== null) ? this.bezeichnung : "";
	}

	/**
	 * Gibt den Schlüssel zurück, unter dem die benutzerspezifischen Einstellungen dieser Report-Vorlage in der Client-Konfiguration gespeichert werden
	 * (Schema {@code reporting.einstellungen.benutzer.vorlage.<bezeichnung>}). Die Client-Anwendung, unter der dieser Schlüssel abgelegt wird,
	 * ist stets {@link de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationBenutzerweit#CONFIG_APP_NAME}.
	 *
	 * @return der Config-Schlüssel für die benutzerspezifischen Einstellungen dieser Report-Vorlage
	 */
	public getConfigKeyBenutzerVorlage(): string {
		return ReportingReportvorlage.CONFIG_KEY_PREFIX_BENUTZER_VORLAGE + this.bezeichnung;
	}

	/**
	 * Gibt den Titel für die Benutzeroberfläche zurück.
	 *
	 * @return Der Titel für die Benutzeroberfläche. Falls kein Titel gesetzt wurde, wird ein leerer String zurückgegeben.
	 */
	public getUiTitel(): string {
		return (this.uiTitel !== null) ? this.uiTitel : "";
	}

	/**
	 * Gibt die Beschreibung für die Benutzeroberfläche zurück.
	 *
	 * @return Die Beschreibung für die  Benutzeroberfläche. Falls keine Beschreibung gesetzt wurde, wird ein leerer String zurückgegeben.
	 */
	public getUiBeschreibung(): string {
		return (this.uiBeschreibung !== null) ? this.uiBeschreibung : "";
	}

	/**
	 * Liefert den Daten-Context der aktuellen HTML-Template-Definition.
	 *
	 * @return Der Daten-Context
	 */
	public getReportingReportvorlageDatenContext(): ReportingReportvorlageDatenContext {
		return this.datenContext;
	}

	/**
	 * Gibt den statischen Root-Pfad zurück.
	 *
	 * @return der Root-Pfad für alle Reporting-Templates
	 */
	public static getRootPfad(): string {
		return "de/svws_nrw/module/reporting/";
	}

	/**
	 * Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad.
	 *
	 * @return Der Dateipfad zur HTML-Template-Datei
	 */
	public getPfadHtmlTemplate(): string {
		return this.pfadHtmlTemplate;
	}

	/**
	 * Pfad zur HTML-Template-Datei, inklusive des Root-Pfads.
	 *
	 * @return Der Root-Dateipfad zur HTML-Template-Datei
	 */
	public getRootPfadHtmlTemplate(): string {
		return ReportingReportvorlage.getRootPfad() + this.pfadHtmlTemplate;
	}

	/**
	 * Gibt den statischen Dateinamen ohne Dateiendung zurück.
	 *
	 * @return Der statische Dateiname
	 */
	public getDateiname(): string {
		return this.dateiname;
	}

	/**
	 * Gibt den relativen Pfad zur Dateinamensvorlage zurück, indem im Pfad der HTML-Vorlage die Endung ".html" durch ".name.tpl" ersetzt wird.
	 *
	 * @return Der Pfad zur Dateinamensvorlage.
	 */
	public getPfadDateinamensvorlage(): string {
		return JavaString.replace(this.pfadHtmlTemplate, ".html", ".name.tpl");
	}

	/**
	 * Gibt die Benutzer-Kompetenzen für diese Template-Definition zurück
	 *
	 * @return Die Liste der Benutzerkompetenzen
	 */
	public getBenutzerKompetenzen(): List<BenutzerKompetenz> {
		return this.benutzerKompetenzen;
	}

	/**
	 * Gibt die Schulformen zurück, an denen diese Report-Vorlage genutzt werden darf.
	 * Ist die Liste leer, so gilt die Vorlage für alle Schulformen.
	 *
	 * @return Die Liste der Schulformen
	 */
	public getSchulformen(): List<Schulform> {
		return this.schulformen;
	}

	/**
	 * Gibt an, ob diese Report-Vorlage an der übergebenen Schulform genutzt werden darf. Eine leere Liste an der Vorlage lässt jede Schulform zu.
	 * Nennt die Vorlage Schulformen, muss die übergebene darunter sein; eine nicht übergebene Schulform gilt dann als unzulässig.
	 *
	 * @param schulform Die zu prüfende Schulform. {@code null} ist zulässig.
	 *
	 * @return true, wenn die Vorlage an dieser Schulform genutzt werden darf; sonst false.
	 */
	public giltFuerSchulform(schulform: Schulform | null): boolean {
		return this.schulformen.isEmpty() || ((schulform !== null) && this.schulformen.contains(schulform));
	}

	/**
	 * Gibt eine Kopie der ReportingParamater für diese Report-Vorlage zurück. So wird verhindert, dass die Werte der ReportingParameter in der ENUM
	 * verändert werden können, was im Client Server weit greifen würde.
	 *
	 * @return Die Kopie der ReportingParameter für diese Report-Vorlage.
	 */
	public getReportingParameter(): ReportingParameter {
		return ReportingReportvorlageUtils.cloneReportingParameter(this.reportingParameter);
	}

	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand der übergebenen Bezeichnung.
	 *
	 * @param bezeichnung Die Bezeichnung der Report-Vorlage
	 *
	 * @return Die Report-Vorlage
	 */
	public static getByBezeichnung(bezeichnung: string): ReportingReportvorlage | null {
		if (JavaString.isEmpty(bezeichnung)) {
			return null;
		}
		for (const rv of ReportingReportvorlage.values()) {
			if (JavaObject.equalsTranspiler(rv.bezeichnung, (bezeichnung))) {
				return rv;
			}
		}
		return null;
	}

	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand des übergebenen Namens. Der Vergleich ignoriert Groß- und Kleinschreibung.
	 *
	 * @param name Der Name der Report-Vorlage
	 *
	 * @return Die Report-Vorlage
	 */
	public static getByName(name: string): ReportingReportvorlage | null {
		if (JavaString.isEmpty(name)) {
			return null;
		}
		for (const rv of ReportingReportvorlage.values()) {
			if (JavaString.equalsIgnoreCase(rv.name(), name)) {
				return rv;
			}
		}
		return null;
	}

	/**
	 * Ermittelt alle Report-Vorlagen, deren Enum-Name alle übergebenen Namensbestandteile enthält. Dabei werden im Enum-Namen nur die durch '_' getrennten Teile
	 * berücksichtigt.
	 * Der Vergleich erfolgt ohne Beachtung der Groß-/Kleinschreibung für Namensbestandteile mit einer Länge größer oder gleich 3. Suchbegriffe, die diese
	 * Bedingungen nicht erfüllen, werden ignoriert.
	 *
	 * @param namensbestandteile  die Liste der Namensbestandteile, die in den Namensbestandteilen enthalten sein müssen
	 *
	 * @return eine Liste aller passenden Report-Vorlagen
	 */
	public static getByNamensbestandteilen(namensbestandteile: List<string>): List<ReportingReportvorlage> {
		ReportingReportvorlage.mapsInitialisieren();
		const namensbestandteileNormalisiert: List<string> | null = new ArrayList<string>();
		for (const bestandteil of namensbestandteile) {
			if ((bestandteil !== null) && !JavaString.isBlank(bestandteil) && (bestandteil.trim().length >= 3)) {
				namensbestandteileNormalisiert.add(bestandteil.trim().toLowerCase());
			}
		}
		let gueltigeVorlagen: JavaSet<ReportingReportvorlage> | null = null;
		for (const suchbegriff of namensbestandteileNormalisiert) {
			const treffer: List<ReportingReportvorlage> | null = ReportingReportvorlage.MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.get(suchbegriff);
			if ((treffer === null) || treffer.isEmpty()) {
				return new ArrayList();
			}
			if (gueltigeVorlagen === null) {
				gueltigeVorlagen = new HashSet(treffer);
			} else {
				gueltigeVorlagen.retainAll(treffer);
				if (gueltigeVorlagen.isEmpty()) {
					return new ArrayList();
				}
			}
		}
		return (gueltigeVorlagen === null) ? new ArrayList() : new ArrayList(gueltigeVorlagen);
	}

	/**
	 * Liefert alle Default-Vorlageparameter der Reportvorlage über deren Bezeichnung als Liste ohne Gruppenzuordnung.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @return Liste der Default-Vorlageparameter der Reportvorlage (ggf. leer)
	 */
	public getDefaultVorlageparameterByVorlage(): List<ReportingReportvorlageParameter> {
		ReportingReportvorlage.mapsInitialisieren();
		const key1: string | null = ReportingReportvorlageUtils.normalizeKeyInput(this.bezeichnung);
		const mapParam: JavaMap<string, ReportingReportvorlageParameter> | null = ReportingReportvorlage.MAP_PARAMETER.get(key1);
		return (mapParam === null) ? new ArrayList() : new ArrayList(mapParam.values());
	}

	/**
	 * Liefert alle Default-Vorlageparameter einer Parametergruppe aus der Reportvorlage.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param parametergruppeName der Name der Vorlageparametergruppe
	 *
	 * @return Liste der Default-Vorlageparameter der Gruppe (ggf. leer)
	 */
	public getDefaultVorlageparameterByGruppe(parametergruppeName: string): List<ReportingReportvorlageParameter> {
		ReportingReportvorlage.mapsInitialisieren();
		const key1: string | null = ReportingReportvorlageUtils.normalizeKeyInput(this.bezeichnung);
		const key2: string | null = ReportingReportvorlageUtils.normalizeKeyInput(parametergruppeName);
		const mapGruppen: JavaMap<string, ReportingReportvorlageParameterGruppe> | null = ReportingReportvorlage.MAP_PARAMETERGRUPPEN.get(key1);
		if (mapGruppen !== null) {
			const gruppe: ReportingReportvorlageParameterGruppe | null = mapGruppen.get(key2);
			if ((gruppe !== null) && (gruppe.reportvorlageParameter !== null)) {
				return gruppe.reportvorlageParameter;
			}
		}
		return new ArrayList<ReportingReportvorlageParameter>();
	}

	/**
	 * Liefert genau den gewünschten Default-Vorlageparameter der Reportvorlage.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param parameterName der Name des Vorlageparameters
	 *
	 * @return der Default-Vorlageparameter oder null, falls nicht vorhanden.
	 */
	public getDefaultVorlageparameter(parameterName: string): ReportingReportvorlageParameter | null {
		ReportingReportvorlage.mapsInitialisieren();
		const key1: string | null = ReportingReportvorlageUtils.normalizeKeyInput(this.bezeichnung);
		const key3: string | null = ReportingReportvorlageUtils.normalizeKeyInput(parameterName);
		const mapParam: JavaMap<string, ReportingReportvorlageParameter> | null = ReportingReportvorlage.MAP_PARAMETER.get(key1);
		return (mapParam !== null) ? mapParam.get(key3) : null;
	}

	/**
	 * Setzt den Wert eines Vorlageparameters innerhalb der ReportingParameter, die übergeben werden. Die Default-Werte der ENUM werden dadurch nicht angepasst.
	 *
	 * @param reportingParameter   das zu verändernde ReportingParameter-Objekt.
	 * @param vorlageparameterName der Name des zu suchenden Vorlageparameters
	 * @param wert                 der zu setzende Wert als String
	 */
	public setReportingParameterVorlageparameter(reportingParameter: ReportingParameter, vorlageparameterName: string, wert: string): void {
		for (const gruppe of reportingParameter.reportvorlageParameterGruppen) {
			if (gruppe.reportvorlageParameter === null) {
				continue;
			}
			for (const param of gruppe.reportvorlageParameter) {
				if ((param.name !== null) && JavaObject.equalsTranspiler(param.name, (vorlageparameterName))) {
					param.wert = wert;
					return;
				}
			}
		}
	}

	/**
	 * Liefert die Default-Vorlageparametergruppe der Reportvorlage über deren Namen.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param gruppenName der Name der Vorlageparametergruppe
	 *
	 * @return die Default-Vorlageparametergruppe oder null, falls nicht vorhanden.
	 */
	public getDefaultVorlageparametergruppeByName(gruppenName: string): ReportingReportvorlageParameterGruppe | null {
		ReportingReportvorlage.mapsInitialisieren();
		const key1: string | null = ReportingReportvorlageUtils.normalizeKeyInput(this.bezeichnung);
		const key2: string | null = ReportingReportvorlageUtils.normalizeKeyInput(gruppenName);
		const mapGruppen: JavaMap<string, ReportingReportvorlageParameterGruppe> | null = ReportingReportvorlage.MAP_PARAMETERGRUPPEN.get(key1);
		return (mapGruppen !== null) ? mapGruppen.get(key2) : null;
	}

	/**
	 *Initialisiert einmalig interne ListMaps/Indizes für schnelleren Zugriff.
	 */
	private static mapsInitialisieren(): void {
		if (ReportingReportvorlage.mapsInitialisiert) {
			return;
		}
		for (const reportvorlage of ReportingReportvorlage.values()) {
			ReportingReportvorlage.initialisiereParameterMaps(reportvorlage);
			ReportingReportvorlage.initialisiereNamensbestandteileIndex(reportvorlage);
		}
		ReportingReportvorlage.mapsInitialisiert = true;
	}

	/**
	 * Initialisiert die Maps für die Parameter und Parametergruppen einer Reportvorlage.
	 *
	 * @param reportvorlage die Reportvorlage
	 */
	private static initialisiereParameterMaps(reportvorlage: ReportingReportvorlage): void {
		const key1: string | null = ReportingReportvorlageUtils.normalizeKeyInput(reportvorlage.bezeichnung);
		let mapParam: JavaMap<string, ReportingReportvorlageParameter> | null = ReportingReportvorlage.MAP_PARAMETER.get(key1);
		if (mapParam === null) {
			mapParam = new HashMap();
			ReportingReportvorlage.MAP_PARAMETER.put(key1, mapParam);
		}
		let mapGruppen: JavaMap<string, ReportingReportvorlageParameterGruppe> | null = ReportingReportvorlage.MAP_PARAMETERGRUPPEN.get(key1);
		if (mapGruppen === null) {
			mapGruppen = new HashMap();
			ReportingReportvorlage.MAP_PARAMETERGRUPPEN.put(key1, mapGruppen);
		}
		for (const gruppe of reportvorlage.reportingParameter.reportvorlageParameterGruppen) {
			if ((gruppe === null) || (gruppe.name === null) || (gruppe.reportvorlageParameter === null)) {
				continue;
			}
			const key2: string | null = ReportingReportvorlageUtils.normalizeKeyInput(gruppe.name);
			mapGruppen.put(key2, gruppe);
			for (const parameter of gruppe.reportvorlageParameter) {
				if ((parameter === null) || (parameter.name === null)) {
					continue;
				}
				const key3: string | null = ReportingReportvorlageUtils.normalizeKeyInput(parameter.name);
				mapParam.put(key3, parameter);
			}
		}
	}

	/**
	 * Initialisiert den Suchindex für die Bestandteile des ENUM-Namens einer Reportvorlage.
	 *
	 * @param reportvorlage die Reportvorlage
	 */
	private static initialisiereNamensbestandteileIndex(reportvorlage: ReportingReportvorlage): void {
		const enumNameBestandteile: Array<string | null> | null = reportvorlage.name().toLowerCase().split("_");
		for (const bestandteil of enumNameBestandteile) {
			if ((bestandteil === null) || JavaString.isBlank(bestandteil)) {
				continue;
			}
			let vorlagen: List<ReportingReportvorlage> | null = ReportingReportvorlage.MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.get(bestandteil);
			if (vorlagen === null) {
				vorlagen = new ArrayList();
				ReportingReportvorlage.MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.put(bestandteil, vorlagen);
			}
			vorlagen.add(reportvorlage);
		}
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingReportvorlage> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingReportvorlage | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingReportvorlage';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingReportvorlage', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlage>('de.svws_nrw.core.types.reporting.ReportingReportvorlage');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingReportvorlage(obj: unknown): ReportingReportvorlage {
	return obj as ReportingReportvorlage;
}
