package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import de.svws_nrw.core.data.schueler.SchuelerLernabschnittNachpruefungsdaten;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

import java.util.List;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Lernabschnitt.
 */
public class ReportingSchuelerLernabschnitt extends ReportingBaseType {

	/** Der erreichte allgemeinbildende Abschluss */
	protected String abschluss;

	/** Die Art des Abschlusses (siehe Katalog) */
	protected Integer abschlussart;

	/** Der erreichte berufsbezogene Abschluss am Berufskolleg */
	protected String abschlussBerufsbildend;

	/** Die Anzahl der Schulbesuchsjahre */
	protected Integer anzahlSchulbesuchsjahre;

	/** Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird */
	protected String bilingualerZweig;

	/** Das Datum, wann der Lernabschnitt beginnt */
	protected String datumAnfang;

	/** Das Datum, wann der Lernabschnitt endet */
	protected String datumEnde;

	/** Das Datum der Konferenz */
	protected String datumKonferenz;

	/** Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung */
	protected String datumZeugnis;

	/** Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt */
	protected int fehlstundenGesamt;

	/** Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden */
	protected Integer fehlstundenGrenzwert;

	/** Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt */
	protected int fehlstundenUnentschuldigt;

	/** Der erste Förderschwerpunkt des Schülers zu diesem Lernabschnitt */
	protected FoerderschwerpunktEintrag foerderschwerpunkt1;

	/** Der zweite Förderschwerpunkt des Schülers zu diesem Lernabschnitt */
	protected FoerderschwerpunktEintrag foerderschwerpunkt2;

	/** Der Text mit Angaben zum Förderschwerpunkt. */
	protected String foerderschwerpunktText;

	/** Die Folgeklasse des Schülers aus diesem Lernabschnitt */
	protected ReportingKlasse folgeklasse;

	/** Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt */
	protected boolean hatAOSF;

	/** Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht */
	protected boolean hatAutismus;

	/** Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht */
	protected boolean hatSchwerbehinderungsNachweis;

	/** Gibt an, ob zieldifferent unterrichtet wird oder nicht */
	protected boolean hatZieldifferentenUnterricht;

	/** Der Jahrgang des Schülers in diesem Lernabschnitt */
	protected ReportingJahrgang jahrgang;

	/** Die ID des Lernabschnitts in der Datenbank. */
	protected long id;

	/** Die ID der Fachklasse des Schülers an einem Berufskolleg */
	protected Long idFachklasse;

	/** Die ID des Hauptförderschwerpunktes des Schülers */
	protected Long idFoerderschwerpunkt1;

	/** Die ID des weiteren Förderschwerpunktes des Schülers */
	protected Long idFoerderschwerpunkt2;

	/** Die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht. */
	protected Long idFolgeklasse;

	/** Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist */
	protected Long idJahrgang;

	/** Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist. */
	protected Long idKlasse;

	/** Die ID des Schülers des Lernabschnitts in der Datenbank. */
	protected long idSchueler;

	/** Die ID des Schuljahresabschnitts des Lernabschnitts in der Datenbank. */
	protected long idSchuljahresabschnitt;

	/** Die ID des Schwerpunktes des Schülers laut dem Schwerpunkt-Katalog */
	protected Long idSchwerpunkt;

	/** Die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat */
	protected Long idSonderpaedagoge;

	/** Die ID des Tutors des Schülers in der Datenbank. */
	protected Long idTutor;

	/** Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht (siehe Katalog) */
	protected boolean istAbschlussPrognose;

	/** Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend sind für Versetzung */
	protected boolean istFachpraktischerAnteilAusreichend;

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht */
	protected boolean istGewertet;

	/** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht */
	protected boolean istWiederholung;

	/** Die Klasse des Schülers aus diesem Lernabschnitt */
	protected ReportingKlasse klasse;

	/** Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type) */
	protected String klassenart;

	/** Die Leistungsdaten des Schülers in diesem Lernabschnitt. */
	protected List<ReportingSchuelerLeistungsdaten> leistungsdaten;

	/** Die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind. */
	protected SchuelerLernabschnittNachpruefungsdaten nachpruefungen;

	/** Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden */
	protected String noteDurchschnitt;

	/** Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klassen 10 */
	protected Integer noteLernbereichGSbzwAL;

	/** Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10 */
	protected Integer noteLernbereichNW;

	/** Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type) */
	protected String organisationsform;

	/** Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist. */
	protected String pruefungsOrdnung;

	/** Der Schüler, zu dem dieser Lernabschnittsdaten gehören. */
	protected ReportingSchueler schueler;

	/** Das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers. */
	protected String schulgliederung;

	/** Der Schuljahresabschnitt, zu welchem diese Lernabschnittsdaten gehören. */
	protected ReportingSchuljahresabschnitt schuljahresabschnitt;

	/** Der Sonderpädagoge, der den Schüler betreut */
	protected ReportingLehrer sonderpaedagoge;

	/** Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung */
	protected String textErgebnisPruefungsalgorithmus;

	/** Der Tutor, der den Schüler betreut */
	protected ReportingLehrer tutor;

	/** Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I. */
	protected String uebergangsempfehlungText;

	/** Der Text zur Versetzungsentscheidung auf dem Zeugnis. */
	protected String versetzungsentscheidungText;

	/** Das Kürzel des Versetzungsvermerks */
	protected String versetzungsvermerkKuerzel;

	/** Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.). */
	protected int wechselNr;

	/** Die Art des Zeugnisses */
	protected String zeugnisart;

	/** Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten. */
	protected String zeugnisASVText;

	/** Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement. */
	protected String zeugnisAUEText;

	/** Der Text für allgemeine Zeugnisbemerkungen. */
	protected String zeugnisBemerkungText;

	/** Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen. */
	protected String zeugnisLELSText;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
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
	public ReportingSchuelerLernabschnitt(final String abschluss, final Integer abschlussart, final String abschlussBerufsbildend,
			final Integer anzahlSchulbesuchsjahre, final String bilingualerZweig, final String datumAnfang, final String datumEnde, final String datumKonferenz,
			final String datumZeugnis, final int fehlstundenGesamt, final Integer fehlstundenGrenzwert, final int fehlstundenUnentschuldigt,
			final FoerderschwerpunktEintrag foerderschwerpunkt1, final FoerderschwerpunktEintrag foerderschwerpunkt2, final String foerderschwerpunktText,
			final ReportingKlasse folgeklasse, final boolean hatAOSF, final boolean hatAutismus, final boolean hatSchwerbehinderungsNachweis,
			final boolean hatZieldifferentenUnterricht, final ReportingJahrgang jahrgang, final long id, final Long idFachklasse,
			final Long idFoerderschwerpunkt1, final Long idFoerderschwerpunkt2, final Long idFolgeklasse, final Long idJahrgang, final Long idKlasse,
			final long idSchueler, final long idSchuljahresabschnitt, final Long idSchwerpunkt, final Long idSonderpaedagoge, final Long idTutor,
			final boolean istAbschlussPrognose, final boolean istFachpraktischerAnteilAusreichend, final boolean istGewertet, final boolean istWiederholung,
			final ReportingKlasse klasse, final String klassenart, final List<ReportingSchuelerLeistungsdaten> leistungsdaten,
			final SchuelerLernabschnittNachpruefungsdaten nachpruefungen, final String noteDurchschnitt, final Integer noteLernbereichGSbzwAL,
			final Integer noteLernbereichNW, final String organisationsform, final String pruefungsOrdnung, final ReportingSchueler schueler,
			final String schulgliederung, final ReportingSchuljahresabschnitt schuljahresabschnitt, final ReportingLehrer sonderpaedagoge,
			final String textErgebnisPruefungsalgorithmus, final ReportingLehrer tutor, final String uebergangsempfehlungText,
			final String versetzungsentscheidungText, final String versetzungsvermerkKuerzel, final int wechselNr, final String zeugnisart,
			final String zeugnisASVText, final String zeugnisAUEText, final String zeugnisBemerkungText, final String zeugnisLELSText) {
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



	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingSchuelerLernabschnitt other))
			return false;
		return (id == other.id);
	}


	// ##### Getter #####

	/**
	 * Der erreichte allgemeinbildende Abschluss
	 *
	 * @return Inhalt des Feldes abschluss
	 */
	public String abschluss() {
		return abschluss;
	}

	/**
	 * Die Art des Abschlusses (siehe Katalog)
	 *
	 * @return Inhalt des Feldes abschlussart
	 */
	public Integer abschlussart() {
		return abschlussart;
	}

	/**
	 * Der erreichte berufsbezogene Abschluss am Berufskolleg
	 *
	 * @return Inhalt des Feldes abschlussBerufsbildend
	 */
	public String abschlussBerufsbildend() {
		return abschlussBerufsbildend;
	}

	/**
	 * Die Anzahl der Schulbesuchsjahre
	 *
	 * @return Inhalt des Feldes anzahlSchulbesuchsjahre
	 */
	public Integer anzahlSchulbesuchsjahre() {
		return anzahlSchulbesuchsjahre;
	}

	/**
	 * Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird
	 *
	 * @return Inhalt des Feldes bilingualerZweig
	 */
	public String bilingualerZweig() {
		return bilingualerZweig;
	}

	/**
	 * Das Datum, wann der Lernabschnitt beginnt
	 *
	 * @return Inhalt des Feldes datumAnfang
	 */
	public String datumAnfang() {
		return datumAnfang;
	}

	/**
	 * Das Datum, wann der Lernabschnitt endet
	 *
	 * @return Inhalt des Feldes datumEnde
	 */
	public String datumEnde() {
		return datumEnde;
	}

	/**
	 * Das Datum der Konferenz
	 *
	 * @return Inhalt des Feldes datumKonferenz
	 */
	public String datumKonferenz() {
		return datumKonferenz;
	}

	/**
	 * Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung
	 *
	 * @return Inhalt des Feldes datumZeugnis
	 */
	public String datumZeugnis() {
		return datumZeugnis;
	}

	/**
	 * Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt
	 *
	 * @return Inhalt des Feldes fehlstundenGesamt
	 */
	public int fehlstundenGesamt() {
		return fehlstundenGesamt;
	}

	/**
	 * Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden
	 *
	 * @return Inhalt des Feldes fehlstundenGrenzwert
	 */
	public Integer fehlstundenGrenzwert() {
		return fehlstundenGrenzwert;
	}

	/**
	 * Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt
	 *
	 * @return Inhalt des Feldes fehlstundenUnentschuldigt
	 */
	public int fehlstundenUnentschuldigt() {
		return fehlstundenUnentschuldigt;
	}

	/**
	 * Der erste Förderschwerpunkt des Schülers zu diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes foerderschwerpunkt1
	 */
	public FoerderschwerpunktEintrag foerderschwerpunkt1() {
		return foerderschwerpunkt1;
	}

	/**
	 * Der zweite Förderschwerpunkt des Schülers zu diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes foerderschwerpunkt2
	 */
	public FoerderschwerpunktEintrag foerderschwerpunkt2() {
		return foerderschwerpunkt2;
	}

	/**
	 * Der Text mit Angaben zum Förderschwerpunkt.
	 *
	 * @return Inhalt des Feldes foerderschwerpunktText
	 */
	public String foerderschwerpunktText() {
		return foerderschwerpunktText;
	}

	/**
	 * Die Folgeklasse des Schülers aus diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes folgeklasse
	 */
	public ReportingKlasse folgeklasse() {
		return folgeklasse;
	}

	/**
	 * Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt
	 *
	 * @return Inhalt des Feldes hatAOSF
	 */
	public boolean hatAOSF() {
		return hatAOSF;
	}

	/**
	 * Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht
	 *
	 * @return Inhalt des Feldes hatAutismus
	 */
	public boolean hatAutismus() {
		return hatAutismus;
	}

	/**
	 * Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht
	 *
	 * @return Inhalt des Feldes hatSchwerbehinderungsNachweis
	 */
	public boolean hatSchwerbehinderungsNachweis() {
		return hatSchwerbehinderungsNachweis;
	}

	/**
	 * Gibt an, ob zieldifferent unterrichtet wird oder nicht
	 *
	 * @return Inhalt des Feldes hatZieldifferentenUnterricht
	 */
	public boolean hatZieldifferentenUnterricht() {
		return hatZieldifferentenUnterricht;
	}

	/**
	 * Der Jahrgang des Schülers in diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes jahrgang
	 */
	public ReportingJahrgang jahrgang() {
		return jahrgang;
	}

	/**
	 * Die ID des Lernabschnitts in der Datenbank.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID der Fachklasse des Schülers an einem Berufskolleg
	 *
	 * @return Inhalt des Feldes idFachklasse
	 */
	public Long idFachklasse() {
		return idFachklasse;
	}

	/**
	 * Die ID des Hauptförderschwerpunktes des Schülers
	 *
	 * @return Inhalt des Feldes idFoerderschwerpunkt1
	 */
	public Long idFoerderschwerpunkt1() {
		return idFoerderschwerpunkt1;
	}

	/**
	 * Die ID des weiteren Förderschwerpunktes des Schülers
	 *
	 * @return Inhalt des Feldes idFoerderschwerpunkt2
	 */
	public Long idFoerderschwerpunkt2() {
		return idFoerderschwerpunkt2;
	}

	/**
	 * Die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht.
	 *
	 * @return Inhalt des Feldes idFolgeklasse
	 */
	public Long idFolgeklasse() {
		return idFolgeklasse;
	}

	/**
	 * Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist
	 *
	 * @return Inhalt des Feldes idJahrgang
	 */
	public Long idJahrgang() {
		return idJahrgang;
	}

	/**
	 * Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.
	 *
	 * @return Inhalt des Feldes idKlasse
	 */
	public Long idKlasse() {
		return idKlasse;
	}

	/**
	 * Die ID des Schülers des Lernabschnitts in der Datenbank.
	 *
	 * @return Inhalt des Feldes idSchueler
	 */
	public long idSchueler() {
		return idSchueler;
	}

	/**
	 * Die ID des Schuljahresabschnitts des Lernabschnitts in der Datenbank.
	 *
	 * @return Inhalt des Feldes idSchuljahresabschnitt
	 */
	public long idSchuljahresabschnitt() {
		return idSchuljahresabschnitt;
	}

	/**
	 * Die ID des Schwerpunktes des Schülers laut dem Schwerpunkt-Katalog
	 *
	 * @return Inhalt des Feldes idSchwerpunkt
	 */
	public Long idSchwerpunkt() {
		return idSchwerpunkt;
	}

	/**
	 * Die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat
	 *
	 * @return Inhalt des Feldes idSonderpaedagoge
	 */
	public Long idSonderpaedagoge() {
		return idSonderpaedagoge;
	}

	/**
	 * Die ID des Tutors des Schülers in der Datenbank.
	 *
	 * @return Inhalt des Feldes idTutor
	 */
	public Long idTutor() {
		return idTutor;
	}

	/**
	 * Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht (siehe Katalog)
	 *
	 * @return Inhalt des Feldes istAbschlussPrognose
	 */
	public boolean istAbschlussPrognose() {
		return istAbschlussPrognose;
	}

	/**
	 * Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend sind für Versetzung
	 *
	 * @return Inhalt des Feldes istFachpraktischerAnteilAusreichend
	 */
	public boolean istFachpraktischerAnteilAusreichend() {
		return istFachpraktischerAnteilAusreichend;
	}

	/**
	 * Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht
	 *
	 * @return Inhalt des Feldes istGewertet
	 */
	public boolean istGewertet() {
		return istGewertet;
	}

	/**
	 * Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht
	 *
	 * @return Inhalt des Feldes istWiederholung
	 */
	public boolean istWiederholung() {
		return istWiederholung;
	}

	/**
	 * Die Klasse des Schülers aus diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes klasse
	 */
	public ReportingKlasse klasse() {
		return klasse;
	}

	/**
	 * Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type)
	 *
	 * @return Inhalt des Feldes klassenart
	 */
	public String klassenart() {
		return klassenart;
	}

	/**
	 * Die Leistungsdaten des Schülers in diesem Lernabschnitt.
	 *
	 * @return Inhalt des Feldes leistungsdaten
	 */
	public List<ReportingSchuelerLeistungsdaten> leistungsdaten() {
		return leistungsdaten;
	}

	/**
	 * Die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind.
	 *
	 * @return Inhalt des Feldes nachpruefungen
	 */
	public SchuelerLernabschnittNachpruefungsdaten nachpruefungen() {
		return nachpruefungen;
	}

	/**
	 * Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden
	 *
	 * @return Inhalt des Feldes noteDurchschnitt
	 */
	public String noteDurchschnitt() {
		return noteDurchschnitt;
	}

	/**
	 * Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klassen 10
	 *
	 * @return Inhalt des Feldes noteLernbereichGSbzwAL
	 */
	public Integer noteLernbereichGSbzwAL() {
		return noteLernbereichGSbzwAL;
	}

	/**
	 * Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10
	 *
	 * @return Inhalt des Feldes noteLernbereichNW
	 */
	public Integer noteLernbereichNW() {
		return noteLernbereichNW;
	}

	/**
	 * Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type)
	 *
	 * @return Inhalt des Feldes organisationsform
	 */
	public String organisationsform() {
		return organisationsform;
	}

	/**
	 * Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.
	 *
	 * @return Inhalt des Feldes pruefungsOrdnung
	 */
	public String pruefungsOrdnung() {
		return pruefungsOrdnung;
	}

	/**
	 * Der Schüler, zu dem dieser Lernabschnittsdaten gehören.
	 *
	 * @return Inhalt des Feldes schueler
	 */
	public ReportingSchueler schueler() {
		return schueler;
	}

	/**
	 * Das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers.
	 *
	 * @return Inhalt des Feldes schulgliederung
	 */
	public String schulgliederung() {
		return schulgliederung;
	}

	/**
	 * Der Schuljahresabschnitt, zu welchem diese Lernabschnittsdaten gehören.
	 *
	 * @return Inhalt des Feldes schuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return schuljahresabschnitt;
	}

	/**
	 * Der Sonderpädagoge, der den Schüler betreut
	 *
	 * @return Inhalt des Feldes sonderpaedagoge
	 */
	public ReportingLehrer sonderpaedagoge() {
		return sonderpaedagoge;
	}

	/**
	 * Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung
	 *
	 * @return Inhalt des Feldes textErgebnisPruefungsalgorithmus
	 */
	public String textErgebnisPruefungsalgorithmus() {
		return textErgebnisPruefungsalgorithmus;
	}

	/**
	 * Der Tutor, der den Schüler betreut
	 *
	 * @return Inhalt des Feldes tutor
	 */
	public ReportingLehrer tutor() {
		return tutor;
	}

	/**
	 * Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I.
	 *
	 * @return Inhalt des Feldes uebergangsempfehlungText
	 */
	public String uebergangsempfehlungText() {
		return uebergangsempfehlungText;
	}

	/**
	 * Der Text zur Versetzungsentscheidung auf dem Zeugnis.
	 *
	 * @return Inhalt des Feldes versetzungsentscheidungText
	 */
	public String versetzungsentscheidungText() {
		return versetzungsentscheidungText;
	}

	/**
	 * Das Kürzel des Versetzungsvermerks
	 *
	 * @return Inhalt des Feldes versetzungsvermerkKuerzel
	 */
	public String versetzungsvermerkKuerzel() {
		return versetzungsvermerkKuerzel;
	}

	/**
	 * Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z. B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.).
	 *
	 * @return Inhalt des Feldes wechselNr
	 */
	public int wechselNr() {
		return wechselNr;
	}

	/**
	 * Die Art des Zeugnisses
	 *
	 * @return Inhalt des Feldes zeugnisart
	 */
	public String zeugnisart() {
		return zeugnisart;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten.
	 *
	 * @return Inhalt des Feldes zeugnisASVText
	 */
	public String zeugnisASVText() {
		return zeugnisASVText;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement.
	 *
	 * @return Inhalt des Feldes zeugnisAUEText
	 */
	public String zeugnisAUEText() {
		return zeugnisAUEText;
	}

	/**
	 * Der Text für allgemeine Zeugnisbemerkungen.
	 *
	 * @return Inhalt des Feldes zeugnisBemerkungText
	 */
	public String zeugnisBemerkungText() {
		return zeugnisBemerkungText;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen.
	 *
	 * @return Inhalt des Feldes zeugnisLELSText
	 */
	public String zeugnisLELSText() {
		return zeugnisLELSText;
	}

}
