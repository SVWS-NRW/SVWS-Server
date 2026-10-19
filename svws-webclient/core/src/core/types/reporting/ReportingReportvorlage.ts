import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingReportvorlageParameter } from '../../../core/data/reporting/ReportingReportvorlageParameter';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { ReportingReportvorlageDatenContext } from '../../../core/types/reporting/ReportingReportvorlageDatenContext';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { ReportingReportvorlageKonfigurationLehrer } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationLehrer';
import { JavaString } from '../../../java/lang/JavaString';
import { ReportingReportvorlageKonfigurationGost } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationGost';
import { ReportingParameter } from '../../../core/data/reporting/ReportingParameter';
import { ReportingReportvorlageKonfigurationSchueler } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationSchueler';
import { BenutzerKompetenz } from '../../../core/types/benutzer/BenutzerKompetenz';
import { ReportingReportvorlageSchulformen } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageSchulformen';
import type { List } from '../../../java/util/List';
import { ReportingReportvorlageKonfigurationKlassen } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationKlassen';
import { ServerMode } from '../../../core/types/ServerMode';
import { HashSet } from '../../../java/util/HashSet';
import { ReportingReportvorlageKonfigurationStundenplanung } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationStundenplanung';
import { ReportingReportvorlageKonfigurationKurse } from '../../../core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationKurse';
import { ReportingReportvorlageUiNutzung } from '../../../core/types/reporting/ReportingReportvorlageUiNutzung';
import { ReportingReportvorlageUtils } from '../../../core/utils/reporting/ReportingReportvorlageUtils';
import { ReportingReportvorlageDefinition } from '../../../core/types/reporting/ReportingReportvorlageDefinition';
import { ReportingReportvorlageParameterGruppe } from '../../../core/data/reporting/ReportingReportvorlageParameterGruppe';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';

export class ReportingReportvorlage extends JavaEnum<ReportingReportvorlage> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingReportvorlage> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingReportvorlage> = new Map<string, ReportingReportvorlage>();

	/**
	 * Report-Vorlage: GOSt - Klausurplanung - Klausurtermine-Kurse
	 */
	public static readonly GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN", 0, new ReportingReportvorlageDefinition("GostKlausurplanung-KlausurtermineMitKursen").setUiTitel("Klausurplan der Kurse").setUiBeschreibung("Einen Plan mit den Klausurterminen der Kurse erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_TERMINE).setPfadHtmlTemplate("gost/klausurplanung/GostKlausurplanungKlausurtermineMitKursen.html").setDateiname("GOSt-Klausurplanung-Klausurtermine-Kurse").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKlausurplanungVKlausurtermineMitKursen()));

	/**
	 * Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren
	 */
	public static readonly GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN", 1, new ReportingReportvorlageDefinition("GostKlausurplanung-SchuelerMitKlausuren").setUiTitel("Klausurplan der Schülerinnen und Schüler").setUiBeschreibung("Einen Plan mit den Klausurterminen der Schülerinnen und Schüler erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_SCHUELER).setPfadHtmlTemplate("gost/klausurplanung/GostKlausurplanungSchuelerMitKlausuren.html").setDateiname("GOSt-Klausurplanung-Schueler-Klausuren").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKlausurplanungVSchuelerMitKlausuren()));

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler
	 */
	public static readonly GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN", 2, new ReportingReportvorlageDefinition("GostKursplanung-KursMitKursschuelern").setUiTitel("Kursliste").setUiBeschreibung("Eine Liste mit den Schülerinnen und Schülern der Kurse aus der GOSt-Kursplaung erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE).setPfadHtmlTemplate("gost/kursplanung/GostKursplanungKursMitKursschuelern.html").setDateiname("GOSt-Blockungsergebnis-Kurs-Schueler").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKursplanungVKursMitKursschuelern()));

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte
	 */
	public static readonly GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN", 3, new ReportingReportvorlageDefinition("GostKursplanung-KurseMitStatistikwerten").setUiTitel("Kursstatistik").setUiBeschreibung("Eine Liste mit den Kursen aus der GOSt-Kursplanung und ihren Statistikwerten erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE).setPfadHtmlTemplate("gost/kursplanung/GostKursplanungKurseMitStatistikwerten.html").setDateiname("GOSt-Blockungsergebnis-Kurse-Statistikwerte").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKursplanungVKurseMitStatistikwerten()));

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN", 4, new ReportingReportvorlageDefinition("GostKursplanung-SchuelerMitKursen").setUiTitel("Kurszuordnungen der Schülerinnen und Schüler").setUiBeschreibung("Eien Übersicht mit den einzelnen Kurszuorndungen der Schülerinnen und Schüler aus der GOSt-Kursplanung erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER).setPfadHtmlTemplate("gost/kursplanung/GostKursplanungSchuelerMitKursen.html").setDateiname("GOSt-Blockungsergebnis-Schueler-Kurse").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKursplanungVSchuelerMitKursen()));

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN", 5, new ReportingReportvorlageDefinition("GostKursplanung-SchuelerMitSchienenKursen").setUiTitel("Kurs-Schienen-Zuordnungen der Schülerinnen und Schüler").setUiBeschreibung("Eine Übersicht mit den einzelnen Kurszuordnungen und deeren Schienen für die Schülerinnen und Schüler aus der GOSt-Kursplanung erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER).setPfadHtmlTemplate("gost/kursplanung/GostKursplanungSchuelerMitSchienenKursen.html").setDateiname("GOSt-Blockungsergebnis-Schueler-Schienen-Kurse").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKursplanungVSchuelerMitSchienenKursen()));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken
	 */
	public static readonly GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN", 6, new ReportingReportvorlageDefinition("GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken").setUiTitel("Fachwahlstatistiken").setUiBeschreibung("Eine statische Übersicht der Fachwahlen eines Abiturjahrgangs aus der GOSt-Laufbahnplanung erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG).setPfadHtmlTemplate("gost/laufbahnplanung/GostLaufbahnplanungAbiturjahrgangFachwahlstatistiken.html").setDateiname("GOSt-Laufbahnplanung-Abiturjahrgang-Fachwahlstatistiken").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken()));

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Fotos - Namen
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN", 7, new ReportingReportvorlageDefinition("Klasse-Liste-Schueler-Fotos-Namen").setUiTitel("Fotoübersicht klassenweise").setUiBeschreibung("Eine Übersicht mit den Fotos der Schülerinnen und Schüler der Klassen erzeugen oder versenden.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.KLASSEN)).setDatenContext(ReportingReportvorlageDatenContext.KLASSEN).setPfadHtmlTemplate("klassen/KlasseListeSchuelerFotosNamen.html").setDateiname("Klasse-Liste-Schueler-Fotos-Namen").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerFotosNamen()));

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 8, new ReportingReportvorlageDefinition("Klasse-Liste-Schueler-Kontaktdaten-Erzieher").setUiTitel("Klassenliste mit Kontaktdaten").setUiBeschreibung("Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler der Klassen erzeugen oder versenden.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.KLASSEN)).setDatenContext(ReportingReportvorlageDatenContext.KLASSEN).setPfadHtmlTemplate("klassen/KlasseListeSchuelerKontaktdatenErzieher.html").setDateiname("Klasse-Liste-Schueler-Kontaktdaten-Erzieher").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerKontaktdatenerzieher()));

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN", 9, new ReportingReportvorlageDefinition("Klasse-Liste-Schueler-Leistungsdaten").setUiTitel("Leistungsübersicht klassenweise").setUiBeschreibung("Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der Klassen erzeugen.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.KLASSEN)).setDatenContext(ReportingReportvorlageDatenContext.KLASSEN).setPfadHtmlTemplate("klassen/leistungsdaten/KlasseListeSchuelerLeistungsdaten.html").setDateiname("Klassen-Liste-Schueler-Leistungsdaten").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)).setReportingParameter(ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerLeistungsdaten()));

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten - Detailliert
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT", 10, new ReportingReportvorlageDefinition("Klasse-Liste-Schueler-Leistungsdaten-Detailliert").setUiTitel("Leistungsübersicht klassenweise (detailliert)").setUiBeschreibung("Eine detaillierte Übersicht der Leistungsdaten der Schülerinnen und Schüler der Klassen erzeugen.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.KLASSEN)).setDatenContext(ReportingReportvorlageDatenContext.KLASSEN).setPfadHtmlTemplate("klassen/leistungsdaten/KlasseListeSchuelerLeistungsdatenDetailliert.html").setDateiname("Klassen-Liste-Schueler-Leistungsdaten-Detailliert").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)).setReportingParameter(ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerLeistungsdatenDetailliert()));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 11, new ReportingReportvorlageDefinition("Kurs-Liste-Schueler-Kontaktdaten-Erzieher").setUiTitel("Kursliste mit Kontaktdaten").setUiBeschreibung("Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler der Kurse erzeugen oder versenden.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.KURSE)).setDatenContext(ReportingReportvorlageDatenContext.KURSE).setPfadHtmlTemplate("kurse/KursListeSchuelerKontaktdatenErzieher.html").setDateiname("Kurs-Liste-Schueler-Kontaktdaten-Erzieher").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerKontaktdatenerzieher()));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Fotos - Namen
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_FOTOS_NAMEN: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_FOTOS_NAMEN", 12, new ReportingReportvorlageDefinition("Kurs-Liste-Schueler-Fotos-Namen").setUiTitel("Fotoübersicht kursweise").setUiBeschreibung("Eine Übersicht mit den Fotos der Schülerinnen und Schüler der Kurse erzeugen oder versenden.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.KURSE)).setDatenContext(ReportingReportvorlageDatenContext.KURSE).setPfadHtmlTemplate("kurse/KursListeSchuelerFotosNamen.html").setDateiname("Kurs-Liste-Schueler-Fotos-Namen").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerFotosNamen()));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN", 13, new ReportingReportvorlageDefinition("Kurs-Liste-Schueler-Leistungsdaten").setUiTitel("Leistungsübersicht kursweise").setUiBeschreibung("Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der Kurse erzeugen.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.KURSE)).setDatenContext(ReportingReportvorlageDatenContext.KURSE).setPfadHtmlTemplate("kurse/leistungsdaten/KursListeSchuelerLeistungsdaten.html").setDateiname("Kurs-Liste-Schueler-Leistungsdaten").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)).setReportingParameter(ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerLeistungsdaten()));

	/**
	 * Report-Vorlage: Lehrer - Liste - Kontaktdaten
	 */
	public static readonly LEHRER_V_LISTE_KONTAKTDATEN: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_LISTE_KONTAKTDATEN", 14, new ReportingReportvorlageDefinition("Lehrer-Liste-Kontaktdaten").setUiTitel("Lehrerliste mit Kontaktdaten").setUiBeschreibung("Eine Liste mit den Kontaktdaten der ausgewählten Lehrkräfte erzeugen.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.LEHRER)).setDatenContext(ReportingReportvorlageDatenContext.LEHRER).setPfadHtmlTemplate("lehrer/listen/LehrerListeKontaktdaten.html").setDateiname("Lehrer-Liste-Kontaktdaten").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.LEHRERDATEN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationLehrer.getLehrerVListeKontaktdaten()));

	/**
	 * Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten
	 */
	public static readonly LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN", 15, new ReportingReportvorlageDefinition("Lehrer-Liste-Schueler-Leistungsdaten").setUiTitel("Leistungsdaten der Lerngruppen").setUiBeschreibung("Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der ausgewählten Lehrkräfte nach Lerngruppen erzeugen").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.LEHRER)).setDatenContext(ReportingReportvorlageDatenContext.LEHRER).setPfadHtmlTemplate("lehrer/leistungsdaten/LehrerListeSchuelerLeistungsdaten.html").setDateiname("Lehrer-Liste-Schueler-Leistungsdaten").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)).setReportingParameter(ReportingReportvorlageKonfigurationLehrer.getLehrerVListeSchuelerLeistungsdaten()));

	/**
	 * Report-Vorlage: Lehrer - Zugangsdaten für das externe Notenmodul
	 */
	public static readonly LEHRER_V_NOTENMODUL_ANSCHREIBEN_ZUGANGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_NOTENMODUL_ANSCHREIBEN_ZUGANGSDATEN", 16, new ReportingReportvorlageDefinition("Lehrer-Notenmodul-Anschreiben-Zugangsdaten").setUiTitel("Zugangsdaten für den WebNotenManager").setUiBeschreibung("Ein Anschreiben mit dem Initialkennwort für den WebNotenManager je Lehrkraft erzeugen.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.LEHRER)).setDatenContext(ReportingReportvorlageDatenContext.LEHRER).setPfadHtmlTemplate("lehrer/notenmodul/LehrerNotenmodulAnschreibenZugangsdaten.html").setDateiname("Lehrer-Notenmodul-Anschreiben-Zugangsdaten").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.NOTENMODUL_ADMINISTRATION)).setReportingParameter(ReportingReportvorlageKonfigurationLehrer.getLehrerVNotenmodulAnschreibenZugangsdaten()));

	/**
	 * Report-Vorlage: Lehrer - Stammdaten - Liste
	 */
	public static readonly LEHRER_V_STAMMDATENLISTE: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_STAMMDATENLISTE", 17, new ReportingReportvorlageDefinition("Lehrer-Stammdatenliste").setUiTitel("Stammdatenliste der Lehrkräfte").setUiBeschreibung("Stammdatenliste der Lehrkräfte erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.LEHRER).setPfadHtmlTemplate("lehrer/stammdaten/LehrerStammdatenliste.html").setDateiname("Lehrer-Stammdatenliste").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.LEHRERDATEN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationLehrer.getLehrerVStammdatenliste()));

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A4
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4", 18, new ReportingReportvorlageDefinition("Schueler-GostAbiturApoAnlage12-A4").setUiTitel("APO-GOSt - Anlage 12 - Abiturzeugnis (DIN-A4)").setUiBeschreibung("Erzeugt das Abiturzeugnis des Schülerinnen und Schüler gemäß APO-GOSt Anlage 12").setDatenContext(ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR).setPfadHtmlTemplate("schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A4.html").setDateiname("APO-GOSt-Anlage12").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostAbiturApoAnlage12A4()));

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A3
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3", 19, new ReportingReportvorlageDefinition("Schueler-GostAbiturApoAnlage12-A3").setUiTitel("APO-GOSt - Anlage 12 - Abiturzeugnis (DIN-A3)").setUiBeschreibung("Erzeugt das Abiturzeugnis des Schülerinnen und Schüler gemäß APO-GOSt Anlage 12").setDatenContext(ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR).setPfadHtmlTemplate("schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A3.html").setDateiname("APO-GOSt-Anlage12").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostAbiturApoAnlage12A3()));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT", 20, new ReportingReportvorlageDefinition("Schueler-GostLaufbahnplanungErgebnisuebersicht").setUiTitel("Ergebnisübersicht der GOSt-Laufbahnplanung").setUiBeschreibung("Ergebnisübersicht der GOSt-Laufbahnplanung nach Schülerinnen und Schüler für Beratungslehrkräfte erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG).setPfadHtmlTemplate("schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungErgebnisuebersicht.html").setDateiname("GOSt-Laufbahnplanung-Pruefungsergebnisse").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostLaufbahnplanungErgebnisuebersicht()));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN", 21, new ReportingReportvorlageDefinition("Schueler-GostLaufbahnplanungWahlbogen").setUiTitel("GOST-Laufbahnwahlbogen").setUiBeschreibung("Die GOST-Laufbahnwahlbögen für Schülerinnen und Schüler erzeugen oder versenden.").setDatenContext(ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG).setPfadHtmlTemplate("schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungWahlbogen.html").setDateiname("GOSt-Laufbahnplanung-Wahlboegen").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)).setSchulformen(ReportingReportvorlageSchulformen.GOST).setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostLaufbahnplanungWahlbogen()));

	/**
	 * Report-Vorlage: Schüler - Grundschulzeugnis - Klasse 4
	 */
	public static readonly SCHUELER_V_GRUNDSCHULZEUGNIS_KLASSE_4: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GRUNDSCHULZEUGNIS_KLASSE_4", 22, new ReportingReportvorlageDefinition("Schueler-GrundschulzeugnisKlasse4").setUiTitel("Grundschulzeugnis Klasse 4").setUiBeschreibung("Ein Zeugnis der Grundschule für die Klasse 4 erstellen.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.SCHUELER, ReportingReportvorlageUiNutzung.KLASSEN)).setDatenContext(ReportingReportvorlageDatenContext.SCHUELER).setPfadHtmlTemplate("schueler/aogs/SchuelerGrundschulzeugnisKlasse4.html").setDateiname("Grundschulzeugnis-Klasse4").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)).setSchulformen(ReportingReportvorlageSchulformen.GRUNDSCHULE).setErforderlicherServerMode(ServerMode.ALPHA).setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGrundschulzeugnisKlasse4()));

	/**
	 * Report-Vorlage: Schüler - Schulbescheinigung
	 */
	public static readonly SCHUELER_V_SCHULBESCHEINIGUNG: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_SCHULBESCHEINIGUNG", 23, new ReportingReportvorlageDefinition("Schueler-Schulbescheinigung").setUiTitel("Schulbescheinigung").setUiBeschreibung("Eine Schulbescheinigung für Schülerinnen und Schüler oder deren Erziehungsberechtigte erzeugen.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.SCHUELER)).setDatenContext(ReportingReportvorlageDatenContext.SCHUELER).setPfadHtmlTemplate("schueler/anschreiben/SchuelerSchulbescheinigung.html").setDateiname("Schueler-Schulbescheinigung").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVSchulbescheinigung()));

	/**
	 * Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher
	 */
	public static readonly SCHUELER_V_LISTE_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_LISTE_KONTAKTDATENERZIEHER", 24, new ReportingReportvorlageDefinition("Schueler-Liste-Kontaktdaten-Erzieher").setUiTitel("Schülerliste mit Kontaktdaten").setUiBeschreibung("Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler erzeugen oder versenden.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.SCHUELER)).setDatenContext(ReportingReportvorlageDatenContext.SCHUELER).setPfadHtmlTemplate("schueler/listen/SchuelerListeKontaktdatenErzieher.html").setDateiname("Schueler-Liste-Kontaktdaten-Erzieher").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVListeKontaktdatenerzieher()));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_FACH_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_FACH_STUNDENPLAN", 25, new ReportingReportvorlageDefinition("Stundenplanung-FachStundenplan").setUiTitel("Fach-Stundenplan").setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Fächer erzeugen oder versenden.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.FAECHER)).setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH).setPfadHtmlTemplate("stundenplanung/StundenplanungFachStundenplan.html").setDateiname("Fach-Stundenplan").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVFachStundenplan()));

	/**
	 * Report-Vorlage: Stundenplanung - Klasse - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN", 26, new ReportingReportvorlageDefinition("Stundenplanung-KlassenStundenplan").setUiTitel("Klassen-Stundenplan").setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Klassen erzeugen oder versenden.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.KLASSEN)).setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN).setPfadHtmlTemplate("stundenplanung/StundenplanungKlassenStundenplan.html").setDateiname("Klassen-Stundenplan").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVKlassenStundenplan()));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN", 27, new ReportingReportvorlageDefinition("Stundenplanung-LehrerStundenplan").setUiTitel("Lehrer-Stundenplan").setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Lehrkräfte erzeugen oder versenden.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.LEHRER)).setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER).setPfadHtmlTemplate("stundenplanung/StundenplanungLehrerStundenplan.html").setDateiname("Lehrer-Stundenplan").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVLehrerStundenplan()));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT", 28, new ReportingReportvorlageDefinition("Stundenplanung-LehrerStundenplanKombiniert").setUiTitel("Lehrer-Stundenplan kombiniert").setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Lehrkräfte in einer kombinierten Ansicht erzeugen").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.LEHRER)).setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER).setPfadHtmlTemplate("stundenplanung/StundenplanungLehrerStundenplanKombiniert.html").setDateiname("Lehrer-Stundenplan-Kombiniert").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVLehrerStundenplanKombiniert()));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_RAUM_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_RAUM_STUNDENPLAN", 29, new ReportingReportvorlageDefinition("Stundenplanung-RaumStundenplan").setUiTitel("Raum-Stundenplan").setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Räume erzeugen.").setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM).setPfadHtmlTemplate("stundenplanung/StundenplanungRaumStundenplan.html").setDateiname("Raum-Stundenplan").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVRaumStundenplan()));

	/**
	 * Report-Vorlage: Stundenplanung - Schüler - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN", 30, new ReportingReportvorlageDefinition("Stundenplanung-SchuelerStundenplan").setUiTitel("Schüler-Stundenplan").setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Schülerinnen und Schüler erzeugen oder versenden.").setUiNutzungen(ArrayList.of(ReportingReportvorlageUiNutzung.SCHUELER)).setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER).setPfadHtmlTemplate("stundenplanung/StundenplanungSchuelerStundenplan.html").setDateiname("Schueler-Stundenplan").setBenutzerKompetenzen(ArrayList.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)).setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVSchuelerStundenplan()));

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
	 * Die Bereiche im Client, in denen die Report-Vorlage angeboten wird.
	 */
	private readonly uiNutzungen: List<ReportingReportvorlageUiNutzung>;

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
	 * Der mindestens erforderliche ServerMode, damit die Report-Vorlage genutzt werden darf. STABLE lässt sie in allen Modi zu.
	 */
	private readonly erforderlicherServerMode: ServerMode;

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
	 * Übernimmt die Angaben aus der Definition. Fehlt eine Pflichtangabe, schlägt schon das Laden des Enums fehl.
	 *
	 * @param definition   die Definition mit den Angaben zur Report-Vorlage
	 */
	private constructor(name: string, ordinal: number, definition: ReportingReportvorlageDefinition) {
		super(name, ordinal);
		ReportingReportvorlage.all_values_by_ordinal.push(this);
		ReportingReportvorlage.all_values_by_name.set(name, this);
		this.bezeichnung = definition.getBezeichnung();
		this.uiTitel = definition.getUiTitel();
		this.uiBeschreibung = definition.getUiBeschreibung();
		this.uiNutzungen = definition.getUiNutzungen();
		this.datenContext = definition.getDatenContext();
		this.pfadHtmlTemplate = definition.getPfadHtmlTemplate();
		this.dateiname = definition.getDateiname();
		this.benutzerKompetenzen = definition.getBenutzerKompetenzen();
		this.schulformen = definition.getSchulformen();
		this.erforderlicherServerMode = definition.getErforderlicherServerMode();
		this.reportingParameter = definition.getReportingParameter();
		this.reportingParameter.reportvorlage = this.bezeichnung;
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
	 * Gibt die Bereiche im Client zurück, in denen diese Report-Vorlage angeboten wird.
	 *
	 * @return die Bereiche im Client, ggf. leer
	 */
	public getUiNutzungen(): List<ReportingReportvorlageUiNutzung> {
		return this.uiNutzungen;
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
	 * Gibt den mindestens erforderlichen ServerMode zurück, damit diese Report-Vorlage genutzt werden darf.
	 *
	 * @return Der erforderliche ServerMode. STABLE lässt die Vorlage in allen Modi zu.
	 */
	public getErforderlicherServerMode(): ServerMode {
		return this.erforderlicherServerMode;
	}

	/**
	 * Gibt an, ob diese Report-Vorlage in dem übergebenen ServerMode genutzt werden darf. Der Client blendet die Vorlage sonst aus, der Server lehnt sie ab.
	 *
	 * @param serverMode Der ServerMode, in dem der Server betrieben wird.
	 *
	 * @return true, wenn der ServerMode für diese Vorlage ausreicht; sonst false.
	 */
	public giltFuerServerMode(serverMode: ServerMode): boolean {
		return this.erforderlicherServerMode.checkServerMode(serverMode);
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
	 * Ermittelt alle Report-Vorlagen, die der Client in dem übergebenen Bereich anbietet. Die Liste ist nach dem Titel sortiert, weil Anwender eine Card
	 * über ihren Titel suchen.
	 *
	 * @param uiNutzung   der Bereich im Client
	 *
	 * @return die Report-Vorlagen mit dieser Nutzung, ggf. leer
	 */
	public static getByUiNutzung(uiNutzung: ReportingReportvorlageUiNutzung): List<ReportingReportvorlage> {
		const ergebnis: List<ReportingReportvorlage> = new ArrayList<ReportingReportvorlage>();
		for (const rv of ReportingReportvorlage.values()) {
			if (rv.uiNutzungen.contains(uiNutzung)) {
				ergebnis.add(rv);
			}
		}
		ergebnis.sort({ compare: (a: ReportingReportvorlage, b: ReportingReportvorlage) => JavaString.compareToIgnoreCase(a.uiTitel, b.uiTitel) });
		return ergebnis;
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
		return tmp ?? null;
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
