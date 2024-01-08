package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittNachpruefungsdaten;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

import java.util.List;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Lernabschnitt.</p>
 * <p>Sie enthält Daten eines Lernabschnitts eines Schülers, darunter Zugehörigkeiten zu Jahrgang und Klasse, Bemerkungen, Leistungsdaten, Versetzungsdaten und Nachprüfungen.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingSchuelerLernabschnitt {

	/** Der erreichte allgemeinbildende Abschluss */
	private String abschluss;

	/** Die Art des Abschlusses (siehe Katalog) */
	private Integer abschlussart;

	/** Der erreichte berufsbezogene Abschluss am Berufskolleg */
	private String abschlussBerufsbildend;

	/** Die Anzahl der Schulbesuchsjahre */
	private Integer anzahlSchulbesuchsjahre;

	/** Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird */
	private String bilingualerZweig;

	/** Das Datum, wann der Lernabschnitt beginnt */
	private String datumAnfang;

	/** Das Datum, wann der Lernabschnitt endet */
	private String datumEnde;

	/** Das Datum der Konferenz */
	private String datumKonferenz;

	/** Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung */
	private String datumZeugnis;

	/** Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt */
	private int fehlstundenGesamt;

	/** Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden */
	private Integer fehlstundenGrenzwert;

	/** Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt */
	private int fehlstundenUnentschuldigt;

	/** Der erste Förderschwerpunkt des Schülers zu diesem Lernabschnitt */
	private FoerderschwerpunktEintrag foerderschwerpunkt1;

	/** Der zweite Förderschwerpunkt des Schülers zu diesem Lernabschnitt */
	private FoerderschwerpunktEintrag foerderschwerpunkt2;

	/** Der Text mit Angaben zum Förderschwerpunkt. */
	private String foerderschwerpunktText;

	/** Die Folgeklasse des Schülers aus diesem Lernabschnitt */
	private ReportingKlasse folgeklasse;

	/** Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt */
	private boolean hatAOSF;

	/** Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht */
	private boolean hatAutismus;

	/** Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht */
	private boolean hatSchwerbehinderungsNachweis;

	/** Gibt an, ob zieldifferent unterrichtet wird oder nicht */
	private boolean hatZieldifferentenUnterricht;

	/** Der Jahrgang des Schülers in diesem Lernabschnitt */
	private ReportingJahrgang jahrgang;

	/** Die ID des Lernabschnitts in der Datenbank. */
	private long id;

	/** Die ID der Fachklasse des Schülers an einem Berufskolleg */
	private Long idFachklasse;

	/** Die ID des Hauptförderschwerpunktes des Schülers */
	private Long idFoerderschwerpunkt1;

	/** Die ID des weiteren Förderschwerpunktes des Schülers */
	private Long idFoerderschwerpunkt2;

	/** Die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht. */
	private Long idFolgeklasse;

	/** Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist */
	private Long idJahrgang;

	/** Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist. */
	private Long idKlasse;

	/** Die ID des Schülers des Lernabschnitts in der Datenbank. */
	private long idSchueler;

	/** Die ID des Schuljahresabschnitts des Lernabschnitts in der Datenbank. */
	private long idSchuljahresabschnitt;

	/** Die ID des Schwerpunktes des Schülers laut dem Schwerpunkt-Katalog */
	private Long idSchwerpunkt;

	/** Die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat */
	private Long idSonderpaedagoge;

	/** Die ID des Tutors des Schülers in der Datenbank. */
	private Long idTutor;

	/** Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht (siehe Katalog) */
	private boolean istAbschlussPrognose;

	/** Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend sind für Versetzung */
	private boolean istFachpraktischerAnteilAusreichend;

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht */
	private boolean istGewertet;

	/** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht */
	private boolean istWiederholung;

	/** Die Klasse des Schülers aus diesem Lernabschnitt */
	private ReportingKlasse klasse;

	/** Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type) */
	private String klassenart;

	/** Die Leistungsdaten des Schülers in diesem Lernabschnitt. */
	private List<SchuelerLeistungsdaten> leistungsdaten;

	/** Die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind. */
	private SchuelerLernabschnittNachpruefungsdaten nachpruefungen;

	/** Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden */
	private String noteDurchschnitt;

	/** Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klassen 10 */
	private Integer noteLernbereichGSbzwAL;

	/** Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10 */
	private Integer noteLernbereichNW;

	/** Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type) */
	private String organisationsform;

	/** Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist. */
	private String pruefungsOrdnung;

	/** Der Schüler, zu dem dieser Lernabschnittsdaten gehören. */
	private ReportingSchueler schueler;

	/** Das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers. */
	private String schulgliederung;

	/** Der Schuljahresabschnitt, zu welchem diese Lernabschnittsdaten gehören. */
	private ReportingSchuljahresabschnitt schuljahresabschnitt;

	/** Der Sonderpädagoge, der den Schüler betreut */
	private ReportingLehrer sonderpaedagoge;

	/** Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung */
	private String textErgebnisPruefungsalgorithmus;

	/** Der Tutor, der den Schüler betreut */
	private ReportingLehrer tutor;

	/** Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I. */
	private String uebergangsempfehlungText;

	/** Der Text zur Versetzungsentscheidung auf dem Zeugnis. */
	private String versetzungsentscheidungText;

	/** Das Kürzel des Versetzungsvermerks */
	private String versetzungsvermerkKuerzel;

	/** Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.). */
	private int wechselNr;

	/** Die Art des Zeugnisses */
	private String zeugnisart;

	/** Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten. */
	private String zeugnisASVText;

	/** Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement. */
	private String zeugnisAUEText;

	/** Der Text für allgemeine Zeugnisbemerkungen. */
	private String zeugnisBemerkungText;

	/** Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen. */
	private String zeugnisLELSText;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abschluss Der erreichte allgemeinbildende Abschluss
	 * @param abschlussart Die Art des Abschlusses (siehe Katalog)
	 * @param abschlussBerufsbildend Der erreichte berufsbezogene Abschluss am Berufskolleg
	 * @param anzahlSchulbesuchsjahre Die Anzahl der Schulbesuchsjahre
	 * @param bilingualerZweig Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird
	 * @param datumAnfang Das Datum, wann der Lernabschnitt beginnt
	 * @param datumEnde Das Datum, wann der Lernabschnitt endet
	 * @param datumKonferenz Das Datum der Konferenz
	 * @param datumZeugnis Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung
	 * @param fehlstundenGesamt Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt
	 * @param fehlstundenGrenzwert Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden
	 * @param fehlstundenUnentschuldigt Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt
	 * @param foerderschwerpunkt1 Der erste Förderschwerpunkt des Schülers zu diesem Lernabschnitt.
	 * @param foerderschwerpunkt2 Der zweite Förderschwerpunkt des Schülers zu diesem Lernabschnitt.
	 * @param foerderschwerpunktText Der Text mit Angaben zum Förderschwerpunkt.
	 * @param folgeklasse Die Folgeklasse des Schülers aus diesem Lernabschnitt
	 * @param hatAOSF Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt
	 * @param hatAutismus Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht
	 * @param hatSchwerbehinderungsNachweis Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht
	 * @param hatZieldifferentenUnterricht Gibt an, ob zieldifferent unterrichtet wird oder nicht
	 * @param jahrgang Der Jahrgang des Schülers in diesem Lernabschnitt
	 * @param id Die ID des Lernabschnitts in der Datenbank.
	 * @param idFachklasse Die ID der Fachklasse des Schülers an einem Berufskolleg
	 * @param idFoerderschwerpunkt1 Die ID des Hauptförderschwerpunktes des Schülers
	 * @param idFoerderschwerpunkt2 Die ID des weiteren Förderschwerpunktes des Schülers
	 * @param idFolgeklasse Die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht.
	 * @param idJahrgang Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist
	 * @param idKlasse Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.
	 * @param idSchueler Die ID des Schülers des Lernabschnitts in der Datenbank.
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts des Lernabschnitts in der Datenbank.
	 * @param idSchwerpunkt Der Schwerpunkt eines Schülers laut dem Schwerpunkt-Katalog
	 * @param idSonderpaedagoge Die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat
	 * @param idTutor Die ID des Tutors des Schülers in der Datenbank.
	 * @param istAbschlussPrognose Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht (siehe Katalog)
	 * @param istFachpraktischerAnteilAusreichend Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend sind für Versetzung
	 * @param istGewertet Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht
	 * @param istWiederholung Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht
	 * @param klasse Die Klasse des Schülers aus diesem Lernabschnitt
	 * @param klassenart Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type)
	 * @param leistungsdaten Die Leistungsdaten des Schülers in diesem Lernabschnitt.
	 * @param nachpruefungen Die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind.
	 * @param noteDurchschnitt Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden
	 * @param noteLernbereichGSbzwAL Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klassen 10
	 * @param noteLernbereichNW Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10
	 * @param organisationsform Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type)
	 * @param pruefungsOrdnung Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.
	 * @param schueler Der Schüler, zu dem diese Lernabschnittsdaten gehören.
	 * @param schulgliederung Das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt, zu welchem diese Lernabschnittsdaten gehören.
	 * @param sonderpaedagoge Der Sonderpädagoge, der den Schüler betreut
	 * @param textErgebnisPruefungsalgorithmus Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung
	 * @param tutor Der Lehrer, der den Schüler als Tutor betreut, oder null, falls keiner zugewiesen ist
	 * @param uebergangsempfehlungText  Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I.
	 * @param versetzungsentscheidungText Der Text zur Versetzungsentscheidung auf dem Zeugnis.
	 * @param versetzungsvermerkKuerzel Das Kürzel des Versetzungsvermerks
	 * @param wechselNr Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, 0=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.).
	 * @param zeugnisart Die Art des Zeugnisses.
	 * @param zeugnisASVText Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten.
	 * @param zeugnisAUEText Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement
	 * @param zeugnisBemerkungText Der Text für allgemeine Zeugnisbemerkungen.
	 * @param zeugnisLELSText Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen.
	 */
	public ReportingSchuelerLernabschnitt(final String abschluss, final Integer abschlussart, final String abschlussBerufsbildend, final Integer anzahlSchulbesuchsjahre, final String bilingualerZweig, final String datumAnfang, final String datumEnde, final String datumKonferenz, final String datumZeugnis, final int fehlstundenGesamt, final Integer fehlstundenGrenzwert, final int fehlstundenUnentschuldigt, final FoerderschwerpunktEintrag foerderschwerpunkt1, final FoerderschwerpunktEintrag foerderschwerpunkt2, final String foerderschwerpunktText, final ReportingKlasse folgeklasse, final boolean hatAOSF, final boolean hatAutismus, final boolean hatSchwerbehinderungsNachweis, final boolean hatZieldifferentenUnterricht, final ReportingJahrgang jahrgang, final long id, final Long idFachklasse, final Long idFoerderschwerpunkt1, final Long idFoerderschwerpunkt2, final Long idFolgeklasse, final Long idJahrgang, final Long idKlasse, final long idSchueler, final long idSchuljahresabschnitt, final Long idSchwerpunkt, final Long idSonderpaedagoge, final Long idTutor, final boolean istAbschlussPrognose, final boolean istFachpraktischerAnteilAusreichend, final boolean istGewertet, final boolean istWiederholung, final ReportingKlasse klasse, final String klassenart, final List<SchuelerLeistungsdaten> leistungsdaten, final SchuelerLernabschnittNachpruefungsdaten nachpruefungen, final String noteDurchschnitt, final Integer noteLernbereichGSbzwAL, final Integer noteLernbereichNW, final String organisationsform, final String pruefungsOrdnung, final ReportingSchueler schueler, final String schulgliederung, final ReportingSchuljahresabschnitt schuljahresabschnitt, final ReportingLehrer sonderpaedagoge, final String textErgebnisPruefungsalgorithmus, final ReportingLehrer tutor, final String uebergangsempfehlungText, final String versetzungsentscheidungText, final String versetzungsvermerkKuerzel, final int wechselNr, final String zeugnisart, final String zeugnisASVText, final String zeugnisAUEText, final String zeugnisBemerkungText, final String zeugnisLELSText) {
		this.abschluss = abschluss;
		this.abschlussart = abschlussart;
		this.abschlussBerufsbildend = abschlussBerufsbildend;
		this.anzahlSchulbesuchsjahre = anzahlSchulbesuchsjahre;
		this.bilingualerZweig = bilingualerZweig;
		this.datumAnfang = datumAnfang;
		this.datumEnde = datumEnde;
		this.datumKonferenz = datumKonferenz;
		this.datumZeugnis = datumZeugnis;
		this.fehlstundenGesamt = fehlstundenGesamt;
		this.fehlstundenGrenzwert = fehlstundenGrenzwert;
		this.fehlstundenUnentschuldigt = fehlstundenUnentschuldigt;
		this.foerderschwerpunkt1 = foerderschwerpunkt1;
		this.foerderschwerpunkt2 = foerderschwerpunkt2;
		this.foerderschwerpunktText = foerderschwerpunktText;
		this.folgeklasse = folgeklasse;
		this.hatAOSF = hatAOSF;
		this.hatAutismus = hatAutismus;
		this.hatSchwerbehinderungsNachweis = hatSchwerbehinderungsNachweis;
		this.hatZieldifferentenUnterricht = hatZieldifferentenUnterricht;
		this.jahrgang = jahrgang;
		this.id = id;
		this.idFachklasse = idFachklasse;
		this.idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		this.idFoerderschwerpunkt2 = idFoerderschwerpunkt2;
		this.idFolgeklasse = idFolgeklasse;
		this.idJahrgang = idJahrgang;
		this.idKlasse = idKlasse;
		this.idSchueler = idSchueler;
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.idSchwerpunkt = idSchwerpunkt;
		this.idSonderpaedagoge = idSonderpaedagoge;
		this.idTutor = idTutor;
		this.istAbschlussPrognose = istAbschlussPrognose;
		this.istFachpraktischerAnteilAusreichend = istFachpraktischerAnteilAusreichend;
		this.istGewertet = istGewertet;
		this.istWiederholung = istWiederholung;
		this.klasse = klasse;
		this.klassenart = klassenart;
		this.leistungsdaten = leistungsdaten;
		this.nachpruefungen = nachpruefungen;
		this.noteDurchschnitt = noteDurchschnitt;
		this.noteLernbereichGSbzwAL = noteLernbereichGSbzwAL;
		this.noteLernbereichNW = noteLernbereichNW;
		this.organisationsform = organisationsform;
		this.pruefungsOrdnung = pruefungsOrdnung;
		this.schueler = schueler;
		this.schulgliederung = schulgliederung;
		this.schuljahresabschnitt = schuljahresabschnitt;
		this.sonderpaedagoge = sonderpaedagoge;
		this.textErgebnisPruefungsalgorithmus = textErgebnisPruefungsalgorithmus;
		this.tutor = tutor;
		this.uebergangsempfehlungText = uebergangsempfehlungText;
		this.versetzungsentscheidungText = versetzungsentscheidungText;
		this.versetzungsvermerkKuerzel = versetzungsvermerkKuerzel;
		this.wechselNr = wechselNr;
		this.zeugnisart = zeugnisart;
		this.zeugnisASVText = zeugnisASVText;
		this.zeugnisAUEText = zeugnisAUEText;
		this.zeugnisBemerkungText = zeugnisBemerkungText;
		this.zeugnisLELSText = zeugnisLELSText;
	}



	// ##### Getter und Setter #####

	/**
	 * Der erreichte allgemeinbildende Abschluss
	 * @return Inhalt des Feldes abschluss
	 */
	public String abschluss() {
		return abschluss;
	}

	/**
	 * Der erreichte allgemeinbildende Abschluss wird gesetzt.
	 * @param abschluss Neuer Wert für das Feld abschluss
	 */
	public void setAbschluss(final String abschluss) {
		this.abschluss = abschluss;
	}

	/**
	 * Die Art des Abschlusses (siehe Katalog)
	 * @return Inhalt des Feldes abschlussart
	 */
	public Integer abschlussart() {
		return abschlussart;
	}

	/**
	 * Die Art des Abschlusses (siehe Katalog) wird gesetzt.
	 * @param abschlussart Neuer Wert für das Feld abschlussart
	 */
	public void setAbschlussart(final Integer abschlussart) {
		this.abschlussart = abschlussart;
	}

	/**
	 * Der erreichte berufsbezogene Abschluss am Berufskolleg
	 * @return Inhalt des Feldes abschlussBerufsbildend
	 */
	public String abschlussBerufsbildend() {
		return abschlussBerufsbildend;
	}

	/**
	 * Der erreichte berufsbezogene Abschluss am Berufskolleg wird gesetzt.
	 * @param abschlussBerufsbildend Neuer Wert für das Feld abschlussBerufsbildend
	 */
	public void setAbschlussBerufsbildend(final String abschlussBerufsbildend) {
		this.abschlussBerufsbildend = abschlussBerufsbildend;
	}

	/**
	 * Die Anzahl der Schulbesuchsjahre
	 * @return Inhalt des Feldes anzahlSchulbesuchsjahre
	 */
	public Integer anzahlSchulbesuchsjahre() {
		return anzahlSchulbesuchsjahre;
	}

	/**
	 * Die Anzahl der Schulbesuchsjahre wird gesetzt.
	 * @param anzahlSchulbesuchsjahre Neuer Wert für das Feld anzahlSchulbesuchsjahre
	 */
	public void setAnzahlSchulbesuchsjahre(final Integer anzahlSchulbesuchsjahre) {
		this.anzahlSchulbesuchsjahre = anzahlSchulbesuchsjahre;
	}

	/**
	 * Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird
	 * @return Inhalt des Feldes bilingualerZweig
	 */
	public String bilingualerZweig() {
		return bilingualerZweig;
	}

	/**
	 * Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird, wird gesetzt.
	 * @param bilingualerZweig Neuer Wert für das Feld bilingualerZweig
	 */
	public void setBilingualerZweig(final String bilingualerZweig) {
		this.bilingualerZweig = bilingualerZweig;
	}

	/**
	 * Das Datum, wann der Lernabschnitt beginnt
	 * @return Inhalt des Feldes datumAnfang
	 */
	public String datumAnfang() {
		return datumAnfang;
	}

	/**
	 * Das Datum, wann der Lernabschnitt beginnt, wird gesetzt.
	 * @param datumAnfang Neuer Wert für das Feld datumAnfang
	 */
	public void setDatumAnfang(final String datumAnfang) {
		this.datumAnfang = datumAnfang;
	}

	/**
	 * Das Datum, wann der Lernabschnitt endet
	 * @return Inhalt des Feldes datumEnde
	 */
	public String datumEnde() {
		return datumEnde;
	}

	/**
	 * Das Datum, wann der Lernabschnitt endet, wird gesetzt.
	 * @param datumEnde Neuer Wert für das Feld datumEnde
	 */
	public void setDatumEnde(final String datumEnde) {
		this.datumEnde = datumEnde;
	}

	/**
	 * Das Datum der Konferenz
	 * @return Inhalt des Feldes datumKonferenz
	 */
	public String datumKonferenz() {
		return datumKonferenz;
	}

	/**
	 * Das Datum der Konferenz wird gesetzt.
	 * @param datumKonferenz Neuer Wert für das Feld datumKonferenz
	 */
	public void setDatumKonferenz(final String datumKonferenz) {
		this.datumKonferenz = datumKonferenz;
	}

	/**
	 * Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung
	 * @return Inhalt des Feldes datumZeugnis
	 */
	public String datumZeugnis() {
		return datumZeugnis;
	}

	/**
	 * Das Datum des Zeugnisses bzw der Laufbahnbescheinigung wird gesetzt.
	 * @param datumZeugnis Neuer Wert für das Feld datumZeugnis
	 */
	public void setDatumZeugnis(final String datumZeugnis) {
		this.datumZeugnis = datumZeugnis;
	}

	/**
	 * Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt
	 * @return Inhalt des Feldes fehlstundenGesamt
	 */
	public int fehlstundenGesamt() {
		return fehlstundenGesamt;
	}

	/**
	 * Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt wird gesetzt.
	 * @param fehlstundenGesamt Neuer Wert für das Feld fehlstundenGesamt
	 */
	public void setFehlstundenGesamt(final int fehlstundenGesamt) {
		this.fehlstundenGesamt = fehlstundenGesamt;
	}

	/**
	 * Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden
	 * @return Inhalt des Feldes fehlstundenGrenzwert
	 */
	public Integer fehlstundenGrenzwert() {
		return fehlstundenGrenzwert;
	}

	/**
	 * Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden wird gesetzt.
	 * @param fehlstundenGrenzwert Neuer Wert für das Feld fehlstundenGrenzwert
	 */
	public void setFehlstundenGrenzwert(final Integer fehlstundenGrenzwert) {
		this.fehlstundenGrenzwert = fehlstundenGrenzwert;
	}

	/**
	 * Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt
	 * @return Inhalt des Feldes fehlstundenUnentschuldigt
	 */
	public int fehlstundenUnentschuldigt() {
		return fehlstundenUnentschuldigt;
	}

	/**
	 * Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt wird gesetzt.
	 * @param fehlstundenUnentschuldigt Neuer Wert für das Feld fehlstundenUnentschuldigt
	 */
	public void setFehlstundenUnentschuldigt(final int fehlstundenUnentschuldigt) {
		this.fehlstundenUnentschuldigt = fehlstundenUnentschuldigt;
	}

	/**
	 * Der erste Förderschwerpunkt des Schülers zu diesem Lernabschnitt
	 * @return Inhalt des Feldes foerderschwerpunkt1
	 */
	public FoerderschwerpunktEintrag foerderschwerpunkt1() {
		return foerderschwerpunkt1;
	}

	/**
	 * Der erste Förderschwerpunkt des Schülers zu diesem Lernabschnitt wird gesetzt.
	 * @param foerderschwerpunkt1 Neuer Wert für das Feld foerderschwerpunkt1
	 */
	public void setFoerderschwerpunkt1(final FoerderschwerpunktEintrag foerderschwerpunkt1) {
		this.foerderschwerpunkt1 = foerderschwerpunkt1;
	}

	/**
	 * Der zweite Förderschwerpunkt des Schülers zu diesem Lernabschnitt
	 * @return Inhalt des Feldes foerderschwerpunkt2
	 */
	public FoerderschwerpunktEintrag foerderschwerpunkt2() {
		return foerderschwerpunkt2;
	}

	/**
	 * Der zweite Förderschwerpunkt des Schülers zu diesem Lernabschnitt wird gesetzt.
	 * @param foerderschwerpunkt2 Neuer Wert für das Feld foerderschwerpunkt2
	 */
	public void setFoerderschwerpunkt2(final FoerderschwerpunktEintrag foerderschwerpunkt2) {
		this.foerderschwerpunkt2 = foerderschwerpunkt2;
	}

	/**
	 * Der Text mit Angaben zum Förderschwerpunkt.
	 * @return Inhalt des Feldes foerderschwerpunktText
	 */
	public String foerderschwerpunktText() {
		return foerderschwerpunktText;
	}

	/**
	 * Der Text mit Angaben zum Förderschwerpunkt wird gesetzt.
	 * @param foerderschwerpunktText Neuer Wert für das Feld foerderschwerpunktText
	 */
	public void setFoerderschwerpunktText(final String foerderschwerpunktText) {
		this.foerderschwerpunktText = foerderschwerpunktText;
	}

	/**
	 * Die Folgeklasse des Schülers aus diesem Lernabschnitt
	 * @return Inhalt des Feldes folgeklasse
	 */
	public ReportingKlasse folgeklasse() {
		return folgeklasse;
	}

	/**
	 * Die Folgeklasse des Schülers aus diesem Lernabschnitt wird gesetzt.
	 * @param folgeklasse Neuer Wert für das Feld folgeklasse
	 */
	public void setFolgeklasse(final ReportingKlasse folgeklasse) {
		this.folgeklasse = folgeklasse;
	}

	/**
	 * Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt
	 * @return Inhalt des Feldes hatAOSF
	 */
	public boolean hatAOSF() {
		return hatAOSF;
	}

	/**
	 * Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt wird gesetzt.
	 * @param hatAOSF Neuer Wert für das Feld hatAOSF
	 */
	public void setHatAOSF(final boolean hatAOSF) {
		this.hatAOSF = hatAOSF;
	}

	/**
	 * Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht
	 * @return Inhalt des Feldes hatAutismus
	 */
	public boolean hatAutismus() {
		return hatAutismus;
	}

	/**
	 * Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht wird gesetzt.
	 * @param hatAutismus Neuer Wert für das Feld hatAutismus
	 */
	public void setHatAutismus(final boolean hatAutismus) {
		this.hatAutismus = hatAutismus;
	}

	/**
	 * Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht
	 * @return Inhalt des Feldes hatSchwerbehinderungsNachweis
	 */
	public boolean hatSchwerbehinderungsNachweis() {
		return hatSchwerbehinderungsNachweis;
	}

	/**
	 * Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht wird gesetzt.
	 * @param hatSchwerbehinderungsNachweis Neuer Wert für das Feld hatSchwerbehinderungsNachweis
	 */
	public void setHatSchwerbehinderungsNachweis(final boolean hatSchwerbehinderungsNachweis) {
		this.hatSchwerbehinderungsNachweis = hatSchwerbehinderungsNachweis;
	}

	/**
	 * Gibt an, ob zieldifferent unterrichtet wird oder nicht
	 * @return Inhalt des Feldes hatZieldifferentenUnterricht
	 */
	public boolean hatZieldifferentenUnterricht() {
		return hatZieldifferentenUnterricht;
	}

	/**
	 * Gibt an, ob zieldifferent unterrichtet wird oder nicht wird gesetzt.
	 * @param hatZieldifferentenUnterricht Neuer Wert für das Feld hatZieldifferentenUnterricht
	 */
	public void setHatZieldifferentenUnterricht(final boolean hatZieldifferentenUnterricht) {
		this.hatZieldifferentenUnterricht = hatZieldifferentenUnterricht;
	}

	/**
	 * Der Jahrgang des Schülers in diesem Lernabschnitt
	 * @return Inhalt des Feldes jahrgang
	 */
	public ReportingJahrgang jahrgang() {
		return jahrgang;
	}

	/**
	 * Der Jahrgang des Schülers in diesem Lernabschnitt wird gesetzt.
	 * @param jahrgang Neuer Wert für das Feld jahrgang
	 */
	public void setJahrgang(final ReportingJahrgang jahrgang) {
		this.jahrgang = jahrgang;
	}

	/**
	 * Die ID des Lernabschnitts in der Datenbank.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID des Lernabschnitts in der Datenbank wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Die ID der Fachklasse des Schülers an einem Berufskolleg
	 * @return Inhalt des Feldes idFachklasse
	 */
	public Long idFachklasse() {
		return idFachklasse;
	}

	/**
	 * Die ID der Fachklasse des Schülers an einem Berufskolleg wird gesetzt.
	 * @param idFachklasse Neuer Wert für das Feld idFachklasse
	 */
	public void setIdFachklasse(final Long idFachklasse) {
		this.idFachklasse = idFachklasse;
	}

	/**
	 * Die ID des Hauptförderschwerpunktes des Schülers
	 * @return Inhalt des Feldes idFoerderschwerpunkt1
	 */
	public Long idFoerderschwerpunkt1() {
		return idFoerderschwerpunkt1;
	}

	/**
	 * Die ID des Hauptförderschwerpunktes des Schülers wird gesetzt.
	 * @param idFoerderschwerpunkt1 Neuer Wert für das Feld idFoerderschwerpunkt1
	 */
	public void setIdFoerderschwerpunkt1(final Long idFoerderschwerpunkt1) {
		this.idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
	}

	/**
	 * Die ID des weiteren Förderschwerpunktes des Schülers
	 * @return Inhalt des Feldes idFoerderschwerpunkt2
	 */
	public Long idFoerderschwerpunkt2() {
		return idFoerderschwerpunkt2;
	}

	/**
	 * Die ID des weiteren Förderschwerpunktes des Schülers wird gesetzt.
	 * @param idFoerderschwerpunkt2 Neuer Wert für das Feld idFoerderschwerpunkt2
	 */
	public void setIdFoerderschwerpunkt2(final Long idFoerderschwerpunkt2) {
		this.idFoerderschwerpunkt2 = idFoerderschwerpunkt2;
	}

	/**
	 * Die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht.
	 * @return Inhalt des Feldes idFolgeklasse
	 */
	public Long idFolgeklasse() {
		return idFolgeklasse;
	}

	/**
	 * Die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht wird gesetzt.
	 * @param idFolgeklasse Neuer Wert für das Feld idFolgeklasse
	 */
	public void setIdFolgeklasse(final Long idFolgeklasse) {
		this.idFolgeklasse = idFolgeklasse;
	}

	/**
	 * Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist
	 * @return Inhalt des Feldes idJahrgang
	 */
	public Long idJahrgang() {
		return idJahrgang;
	}

	/**
	 * Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist, wird gesetzt.
	 * @param idJahrgang Neuer Wert für das Feld idJahrgang
	 */
	public void setIdJahrgang(final Long idJahrgang) {
		this.idJahrgang = idJahrgang;
	}

	/**
	 * Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.
	 * @return Inhalt des Feldes idKlasse
	 */
	public Long idKlasse() {
		return idKlasse;
	}

	/**
	 * Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist, wird gesetzt.
	 * @param idKlasse Neuer Wert für das Feld idKlasse
	 */
	public void setIdKlasse(final Long idKlasse) {
		this.idKlasse = idKlasse;
	}

	/**
	 * Die ID des Schülers des Lernabschnitts in der Datenbank.
	 * @return Inhalt des Feldes idSchueler
	 */
	public long idSchueler() {
		return idSchueler;
	}

	/**
	 * Die ID des Schülers des Lernabschnitts in der Datenbank wird gesetzt.
	 * @param idSchueler Neuer Wert für das Feld idSchueler
	 */
	public void setIdSchueler(final long idSchueler) {
		this.idSchueler = idSchueler;
	}

	/**
	 * Die ID des Schuljahresabschnitts des Lernabschnitts in der Datenbank.
	 * @return Inhalt des Feldes idSchuljahresabschnitt
	 */
	public long idSchuljahresabschnitt() {
		return idSchuljahresabschnitt;
	}

	/**
	 * Die ID des Schuljahresabschnitts des Lernabschnitts in der Datenbank wird gesetzt.
	 * @param idSchuljahresabschnitt Neuer Wert für das Feld idSchuljahresabschnitt
	 */
	public void setIdSchuljahresabschnitt(final long idSchuljahresabschnitt) {
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
	}

	/**
	 * Die ID des Schwerpunktes des Schülers laut dem Schwerpunkt-Katalog
	 * @return Inhalt des Feldes idSchwerpunkt
	 */
	public Long idSchwerpunkt() {
		return idSchwerpunkt;
	}

	/**
	 * Die ID des Schwerpunktes des Schülers laut dem Schwerpunkt-Katalog wird gesetzt.
	 * @param idSchwerpunkt Neuer Wert für das Feld idSchwerpunkt
	 */
	public void setIdSchwerpunkt(final Long idSchwerpunkt) {
		this.idSchwerpunkt = idSchwerpunkt;
	}

	/**
	 * Die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat
	 * @return Inhalt des Feldes idSonderpaedagoge
	 */
	public Long idSonderpaedagoge() {
		return idSonderpaedagoge;
	}

	/**
	 * Die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat, wird gesetzt.
	 * @param idSonderpaedagoge Neuer Wert für das Feld idSonderpaedagoge
	 */
	public void setIdSonderpaedagoge(final Long idSonderpaedagoge) {
		this.idSonderpaedagoge = idSonderpaedagoge;
	}

	/**
	 * Die ID des Tutors des Schülers in der Datenbank.
	 * @return Inhalt des Feldes idTutor
	 */
	public Long idTutor() {
		return idTutor;
	}

	/**
	 * Die ID des Tutors des Schülers in der Datenbank wird gesetzt.
	 * @param idTutor Neuer Wert für das Feld idTutor
	 */
	public void setIdTutor(final Long idTutor) {
		this.idTutor = idTutor;
	}

	/**
	 * Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht (siehe Katalog)
	 * @return Inhalt des Feldes istAbschlussPrognose
	 */
	public boolean istAbschlussPrognose() {
		return istAbschlussPrognose;
	}

	/**
	 * Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht (siehe Katalog) wird gesetzt.
	 * @param istAbschlussPrognose Neuer Wert für das Feld istAbschlussPrognose
	 */
	public void setIstAbschlussPrognose(final boolean istAbschlussPrognose) {
		this.istAbschlussPrognose = istAbschlussPrognose;
	}

	/**
	 * Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend sind für Versetzung
	 * @return Inhalt des Feldes istFachpraktischerAnteilAusreichend
	 */
	public boolean istFachpraktischerAnteilAusreichend() {
		return istFachpraktischerAnteilAusreichend;
	}

	/**
	 * Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend sind für Versetzung wird gesetzt.
	 * @param istFachpraktischerAnteilAusreichend Neuer Wert für das Feld istFachpraktischerAnteilAusreichend
	 */
	public void setIstFachpraktischerAnteilAusreichend(final boolean istFachpraktischerAnteilAusreichend) {
		this.istFachpraktischerAnteilAusreichend = istFachpraktischerAnteilAusreichend;
	}

	/**
	 * Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht
	 * @return Inhalt des Feldes istGewertet
	 */
	public boolean istGewertet() {
		return istGewertet;
	}

	/**
	 * Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht wird gesetzt.
	 * @param istGewertet Neuer Wert für das Feld istGewertet
	 */
	public void setIstGewertet(final boolean istGewertet) {
		this.istGewertet = istGewertet;
	}

	/**
	 * Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht
	 * @return Inhalt des Feldes istWiederholung
	 */
	public boolean istWiederholung() {
		return istWiederholung;
	}

	/**
	 * Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht wird gesetzt.
	 * @param istWiederholung Neuer Wert für das Feld istWiederholung
	 */
	public void setIstWiederholung(final boolean istWiederholung) {
		this.istWiederholung = istWiederholung;
	}

	/**
	 * Die Klasse des Schülers aus diesem Lernabschnitt
	 * @return Inhalt des Feldes klasse
	 */
	public ReportingKlasse klasse() {
		return klasse;
	}

	/**
	 * Die Klasse des Schülers aus diesem Lernabschnitt wird gesetzt.
	 * @param klasse Neuer Wert für das Feld klasse
	 */
	public void setKlasse(final ReportingKlasse klasse) {
		this.klasse = klasse;
	}

	/**
	 * Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type)
	 * @return Inhalt des Feldes klassenart
	 */
	public String klassenart() {
		return klassenart;
	}

	/**
	 * Das Kürzel der Klassenart in Bezug auf den Schüler (z. B. Regelklasse - siehe Core-Type) wird gesetzt.
	 * @param klassenart Neuer Wert für das Feld klassenart
	 */
	public void setKlassenart(final String klassenart) {
		this.klassenart = klassenart;
	}

	/**
	 * Die Leistungsdaten des Schülers in diesem Lernabschnitt.
	 * @return Inhalt des Feldes leistungsdaten
	 */
	public List<SchuelerLeistungsdaten> leistungsdaten() {
		return leistungsdaten;
	}

	/**
	 * Die Leistungsdaten des Schülers in diesem Lernabschnitt wird gesetzt.
	 * @param leistungsdaten Neuer Wert für das Feld leistungsdaten
	 */
	public void setLeistungsdaten(final List<SchuelerLeistungsdaten> leistungsdaten) {
		this.leistungsdaten = leistungsdaten;
	}

	/**
	 * Die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind.
	 * @return Inhalt des Feldes nachpruefungen
	 */
	public SchuelerLernabschnittNachpruefungsdaten nachpruefungen() {
		return nachpruefungen;
	}

	/**
	 * Die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind, wird gesetzt.
	 * @param nachpruefungen Neuer Wert für das Feld nachpruefungen
	 */
	public void setNachpruefungen(final SchuelerLernabschnittNachpruefungsdaten nachpruefungen) {
		this.nachpruefungen = nachpruefungen;
	}

	/**
	 * Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden
	 * @return Inhalt des Feldes noteDurchschnitt
	 */
	public String noteDurchschnitt() {
		return noteDurchschnitt;
	}

	/**
	 * Die Durchschnittsnote in diesem Lernabschnitt - wird ggf von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden wird gesetzt.
	 * @param noteDurchschnitt Neuer Wert für das Feld noteDurchschnitt
	 */
	public void setNoteDurchschnitt(final String noteDurchschnitt) {
		this.noteDurchschnitt = noteDurchschnitt;
	}

	/**
	 * Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klassen 10
	 * @return Inhalt des Feldes noteLernbereichGSbzwAL
	 */
	public Integer noteLernbereichGSbzwAL() {
		return noteLernbereichGSbzwAL;
	}

	/**
	 * Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klassen 10 wird gesetzt.
	 * @param noteLernbereichGSbzwAL Neuer Wert für das Feld noteLernbereichGSbzwAL
	 */
	public void setNoteLernbereichGSbzwAL(final Integer noteLernbereichGSbzwAL) {
		this.noteLernbereichGSbzwAL = noteLernbereichGSbzwAL;
	}

	/**
	 * Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10
	 * @return Inhalt des Feldes noteLernbereichNW
	 */
	public Integer noteLernbereichNW() {
		return noteLernbereichNW;
	}

	/**
	 * Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10 wird gesetzt.
	 * @param noteLernbereichNW Neuer Wert für das Feld noteLernbereichNW
	 */
	public void setNoteLernbereichNW(final Integer noteLernbereichNW) {
		this.noteLernbereichNW = noteLernbereichNW;
	}

	/**
	 * Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type)
	 * @return Inhalt des Feldes organisationsform
	 */
	public String organisationsform() {
		return organisationsform;
	}

	/**
	 * Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z. B. Ganztag - siehe Core-Type) wird gesetzt.
	 * @param organisationsform Neuer Wert für das Feld organisationsform
	 */
	public void setOrganisationsform(final String organisationsform) {
		this.organisationsform = organisationsform;
	}

	/**
	 * Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.
	 * @return Inhalt des Feldes pruefungsOrdnung
	 */
	public String pruefungsOrdnung() {
		return pruefungsOrdnung;
	}

	/**
	 * Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist, wird gesetzt.
	 * @param pruefungsOrdnung Neuer Wert für das Feld pruefungsOrdnung
	 */
	public void setPruefungsOrdnung(final String pruefungsOrdnung) {
		this.pruefungsOrdnung = pruefungsOrdnung;
	}

	/**
	 * Der Schüler, zu dem dieser Lernabschnittsdaten gehören.
	 * @return Inhalt des Feldes schueler
	 */
	public ReportingSchueler schueler() {
		return schueler;
	}

	/**
	 * Der Schüler, zu dem dieser Lernabschnittsdaten gehören wird gesetzt.
	 * @param schueler Neuer Wert für das Feld schueler
	 */
	public void setSchueler(final ReportingSchueler schueler) {
		this.schueler = schueler;
	}

	/**
	 * Das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers.
	 * @return Inhalt des Feldes schulgliederung
	 */
	public String schulgliederung() {
		return schulgliederung;
	}

	/**
	 * Das Kürzel der Schulgliederung bzw des Bildungsgangs des Schülers wird gesetzt.
	 * @param schulgliederung Neuer Wert für das Feld schulgliederung
	 */
	public void setSchulgliederung(final String schulgliederung) {
		this.schulgliederung = schulgliederung;
	}

	/**
	 * Der Schuljahresabschnitt, zu welchem diese Lernabschnittsdaten gehören.
	 * @return Inhalt des Feldes schuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return schuljahresabschnitt;
	}

	/**
	 * Der Schuljahresabschnitt, zu welchem diese Lernabschnittsdaten gehören wird gesetzt.
	 * @param schuljahresabschnitt Neuer Wert für das Feld schuljahresabschnitt
	 */
	public void setSchuljahresabschnitt(final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		this.schuljahresabschnitt = schuljahresabschnitt;
	}

	/**
	 * Der Sonderpädagoge, der den Schüler betreut
	 * @return Inhalt des Feldes sonderpaedagoge
	 */
	public ReportingLehrer sonderpaedagoge() {
		return sonderpaedagoge;
	}

	/**
	 * Der Sonderpädagoge, der den Schüler betreut, wird gesetzt.
	 * @param sonderpaedagoge Neuer Wert für das Feld sonderpaedagoge
	 */
	public void setSonderpaedagoge(final ReportingLehrer sonderpaedagoge) {
		this.sonderpaedagoge = sonderpaedagoge;
	}

	/**
	 * Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung
	 * @return Inhalt des Feldes textErgebnisPruefungsalgorithmus
	 */
	public String textErgebnisPruefungsalgorithmus() {
		return textErgebnisPruefungsalgorithmus;
	}

	/**
	 * Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung wird gesetzt.
	 * @param textErgebnisPruefungsalgorithmus Neuer Wert für das Feld textErgebnisPruefungsalgorithmus
	 */
	public void setTextErgebnisPruefungsalgorithmus(final String textErgebnisPruefungsalgorithmus) {
		this.textErgebnisPruefungsalgorithmus = textErgebnisPruefungsalgorithmus;
	}

	/**
	 * Der Tutor, der den Schüler betreut
	 * @return Inhalt des Feldes tutor
	 */
	public ReportingLehrer tutor() {
		return tutor;
	}

	/**
	 * Der Tutor, der den Schüler betreut, wird gesetzt.
	 * @param tutor Neuer Wert für das Feld tutor
	 */
	public void setTutor(final ReportingLehrer tutor) {
		this.tutor = tutor;
	}

	/**
	 * Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I.
	 * @return Inhalt des Feldes uebergangsempfehlungText
	 */
	public String uebergangsempfehlungText() {
		return uebergangsempfehlungText;
	}

	/**
	 * Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I wird gesetzt.
	 * @param uebergangsempfehlungText Neuer Wert für das Feld uebergangsempfehlungText
	 */
	public void setUebergangsempfehlungText(final String uebergangsempfehlungText) {
		this.uebergangsempfehlungText = uebergangsempfehlungText;
	}

	/**
	 * Der Text zur Versetzungsentscheidung auf dem Zeugnis.
	 * @return Inhalt des Feldes versetzungsentscheidungText
	 */
	public String versetzungsentscheidungText() {
		return versetzungsentscheidungText;
	}

	/**
	 * Der Text zur Versetzungsentscheidung auf dem Zeugnis wird gesetzt.
	 * @param versetzungsentscheidungText Neuer Wert für das Feld versetzungsentscheidungText
	 */
	public void setVersetzungsentscheidungText(final String versetzungsentscheidungText) {
		this.versetzungsentscheidungText = versetzungsentscheidungText;
	}

	/**
	 * Das Kürzel des Versetzungsvermerks
	 * @return Inhalt des Feldes versetzungsvermerkKuerzel
	 */
	public String versetzungsvermerkKuerzel() {
		return versetzungsvermerkKuerzel;
	}

	/**
	 * Das Kürzel des Versetzungsvermerks wird gesetzt.
	 * @param versetzungsvermerkKuerzel Neuer Wert für das Feld versetzungsvermerkKuerzel
	 */
	public void setVersetzungsvermerkKuerzel(final String versetzungsvermerkKuerzel) {
		this.versetzungsvermerkKuerzel = versetzungsvermerkKuerzel;
	}

	/**
	 * Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z. B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.).
	 * @return Inhalt des Feldes wechselNr
	 */
	public int wechselNr() {
		return wechselNr;
	}

	/**
	 * Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z. B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.) wird gesetzt.
	 * @param wechselNr Neuer Wert für das Feld wechselNr
	 */
	public void setWechselNr(final int wechselNr) {
		this.wechselNr = wechselNr;
	}

	/**
	 * Die Art des Zeugnisses
	 * @return Inhalt des Feldes zeugnisart
	 */
	public String zeugnisart() {
		return zeugnisart;
	}

	/**
	 * Die Art des Zeugnisses wird gesetzt.
	 * @param zeugnisart Neuer Wert für das Feld zeugnisart
	 */
	public void setZeugnisart(final String zeugnisart) {
		this.zeugnisart = zeugnisart;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten.
	 * @return Inhalt des Feldes zeugnisASVText
	 */
	public String zeugnisASVText() {
		return zeugnisASVText;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten wird gesetzt.
	 * @param zeugnisASVText Neuer Wert für das Feld zeugnisASVText
	 */
	public void setZeugnisASVText(final String zeugnisASVText) {
		this.zeugnisASVText = zeugnisASVText;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement.
	 * @return Inhalt des Feldes zeugnisAUEText
	 */
	public String zeugnisAUEText() {
		return zeugnisAUEText;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement wird gesetzt.
	 * @param zeugnisAUEText Neuer Wert für das Feld zeugnisAUEText
	 */
	public void setZeugnisAUEText(final String zeugnisAUEText) {
		this.zeugnisAUEText = zeugnisAUEText;
	}

	/**
	 * Der Text für allgemeine Zeugnisbemerkungen.
	 * @return Inhalt des Feldes zeugnisBemerkungText
	 */
	public String zeugnisBemerkungText() {
		return zeugnisBemerkungText;
	}

	/**
	 * Der Text für allgemeine Zeugnisbemerkungen wird gesetzt.
	 * @param zeugnisBemerkungText Neuer Wert für das Feld zeugnisBemerkungText
	 */
	public void setZeugnisBemerkungText(final String zeugnisBemerkungText) {
		this.zeugnisBemerkungText = zeugnisBemerkungText;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen.
	 * @return Inhalt des Feldes zeugnisLELSText
	 */
	public String zeugnisLELSText() {
		return zeugnisLELSText;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen wird gesetzt.
	 * @param zeugnisLELSText Neuer Wert für das Feld zeugnisLELSText
	 */
	public void setZeugnisLELSText(final String zeugnisLELSText) {
		this.zeugnisLELSText = zeugnisLELSText;
	}
}
