package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefungsdaten;
import de.svws_nrw.asd.types.schueler.Versetzungsvermerk;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Lernabschnitt.
 */
public class ReportingSchuelerLernabschnitt extends ReportingBaseType {

	/** Die Sortierkonfiguration für {@link ReportingSchuelerLernabschnitt} (siehe {@link ReportingSchuelerLernabschnittSortierung}). */
	public static final ReportingSortierung<ReportingSchuelerLernabschnitt> SORTIERUNG = ReportingSchuelerLernabschnittSortierung.SORTIERUNG;

	/** Der erreichte allgemeinbildende Abschluss */
	protected String abschluss;

	/** Die Art des Abschlusses (siehe Katalog) */
	protected Integer abschlussart;

	/** Der erreichte berufsbezogene Abschluss am Berufskolleg */
	protected String abschlussBerufsbildend;

	/** Die Belegungen der Ankreuzkompetenzen des Schülers in diesem Lernabschnitt. */
	protected List<ReportingSchuelerAnkreuzkompetenz> ankreuzkompetenzen;

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

	/** Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend ist für Versetzung */
	protected boolean istFachpraktischerAnteilAusreichend;

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht */
	protected boolean istGewertet;

	/** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht */
	protected boolean istWiederholung;

	/** Die Klasse des Schülers aus diesem Lernabschnitt */
	protected ReportingKlasse klasse;

	/** Die ID der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type) */
	protected Long idKlassenart;

	/** Die Informationen zu den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind. */
	protected SchuelerLernabschnittNachpruefungsdaten nachpruefungen;

	/** Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden */
	protected String noteDurchschnitt;

	/** Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klassen 10 */
	protected Integer noteLernbereichGSbzwAL;

	/** Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10 */
	protected Integer noteLernbereichNW;

	/** Die ID der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type) */
	protected Long idOrganisationsform;

	/** Die Prüfungsordnung, die beim Schüler in diesem Lernabschnitt anzuwenden ist. */
	protected String pruefungsOrdnung;

	/** Der Schüler, zu dem diese Lernabschnittsdaten gehören. */
	protected ReportingSchueler schueler;

	/** Die ID der Schulgliederung bzw. des Bildungsgangs des Schülers. */
	protected Long idSchulgliederung;

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

	/** Die Zuweisungen des Schülers in diesem Lernabschnitt. */
	protected List<ReportingSchuelerZuweisung> zuweisungen;

	/** Die Leistungsdaten des Schülers in diesem Lernabschnitt. */
	private List<ReportingSchuelerLeistungsdaten> leistungsdaten;

	/** Eine Map zum schnellen Aufrufen der Leistungsdaten nach der ID, ID-Fach und ID-Kurs */
	private ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> listMapLeistungsdaten = new ListMap3DLongKeys<>();


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abschluss Der erreichte allgemeinbildende Abschluss
	 * @param abschlussart Die Art des Abschlusses (siehe Katalog)
	 * @param abschlussBerufsbildend Der erreichte berufsbezogene Abschluss am Berufskolleg
	 * @param ankreuzkompetenzen Die Liste der Ankreuzkompetenzen für den Lernabschnitt.
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
	 * @param istFachpraktischerAnteilAusreichend Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend ist für Versetzung
	 * @param istGewertet Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht
	 * @param istWiederholung Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht
	 * @param klasse Die Klasse des Schülers aus diesem Lernabschnitt
	 * @param idKlassenart Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type)
	 * @param leistungsdaten Die Leistungsdaten des Schülers in diesem Lernabschnitt.
	 * @param nachpruefungen Die Informationen zu den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind.
	 * @param noteDurchschnitt Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden
	 * @param noteLernbereichGSbzwAL Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klassen 10
	 * @param noteLernbereichNW Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10
	 * @param idOrganisationsform Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type)
	 * @param pruefungsOrdnung Die Prüfungsordnung, die beim Schüler in diesem Lernabschnitt anzuwenden ist.
	 * @param schueler Der Schüler, zu dem diese Lernabschnittsdaten gehören.
	 * @param idSchulgliederung Die ID der Schulgliederung bzw. des Bildungsgangs des Schülers.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt, zu welchem diese Lernabschnittsdaten gehören.
	 * @param sonderpaedagoge Der Sonderpädagoge, der den Schüler betreut
	 * @param textErgebnisPruefungsalgorithmus Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung
	 * @param tutor Der Lehrer, der den Schüler als Tutor betreut, oder null, falls keiner zugewiesen ist
	 * @param uebergangsempfehlungText  Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I.
	 * @param versetzungsentscheidungText Der Text zur Versetzungsentscheidung auf dem Zeugnis.
	 * @param idVersetzungsvermerk Die ID des Versetzungsvermerks
	 * @param wechselNr Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, 0=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.).
	 * @param zeugnisart Die Art des Zeugnisses.
	 * @param zeugnisASVText Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten.
	 * @param zeugnisAUEText Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement
	 * @param zeugnisBemerkungText Der Text für allgemeine Zeugnisbemerkungen.
	 * @param zeugnisLELSText Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen.
	 * @param zuweisungen Die Liste der Zuweisungen für den Lernabschnitt.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingSchuelerLernabschnitt(final String abschluss, final Integer abschlussart, final String abschlussBerufsbildend,
			final List<ReportingSchuelerAnkreuzkompetenz> ankreuzkompetenzen,
			final String bilingualerZweig, final String datumAnfang, final String datumEnde, final String datumKonferenz,
			final String datumZeugnis, final int fehlstundenGesamt, final Integer fehlstundenGrenzwert, final int fehlstundenUnentschuldigt,
			final FoerderschwerpunktEintrag foerderschwerpunkt1, final FoerderschwerpunktEintrag foerderschwerpunkt2, final String foerderschwerpunktText,
			final ReportingKlasse folgeklasse, final boolean hatAOSF, final boolean hatAutismus, final boolean hatSchwerbehinderungsNachweis,
			final boolean hatZieldifferentenUnterricht, final ReportingJahrgang jahrgang, final long id, final Long idFachklasse,
			final Long idFoerderschwerpunkt1, final Long idFoerderschwerpunkt2, final Long idFolgeklasse, final Long idJahrgang, final Long idKlasse,
			final long idSchueler, final long idSchuljahresabschnitt, final Long idSchwerpunkt, final Long idSonderpaedagoge, final Long idTutor,
			final boolean istAbschlussPrognose, final boolean istFachpraktischerAnteilAusreichend, final boolean istGewertet, final boolean istWiederholung,
			final ReportingKlasse klasse, final Long idKlassenart, final List<ReportingSchuelerLeistungsdaten> leistungsdaten,
			final SchuelerLernabschnittNachpruefungsdaten nachpruefungen, final String noteDurchschnitt, final Integer noteLernbereichGSbzwAL,
			final Integer noteLernbereichNW, final Long idOrganisationsform, final String pruefungsOrdnung, final ReportingSchueler schueler,
			final Long idSchulgliederung, final ReportingSchuljahresabschnitt schuljahresabschnitt, final ReportingLehrer sonderpaedagoge,
			final String textErgebnisPruefungsalgorithmus, final ReportingLehrer tutor, final String uebergangsempfehlungText,
			final String versetzungsentscheidungText, final Long idVersetzungsvermerk, final int wechselNr, final String zeugnisart,
			final String zeugnisASVText, final String zeugnisAUEText, final String zeugnisBemerkungText, final String zeugnisLELSText,
			final List<ReportingSchuelerZuweisung> zuweisungen) {
		this.abschluss = ersetzeNullBlankTrim(abschluss);
		this.abschlussart = abschlussart;
		this.abschlussBerufsbildend = ersetzeNullBlankTrim(abschlussBerufsbildend);
		this.ankreuzkompetenzen =
				(ankreuzkompetenzen != null) ? new ArrayList<>(ankreuzkompetenzen.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.bilingualerZweig = ersetzeNullBlankTrim(bilingualerZweig);
		this.datumAnfang = ersetzeNullBlankTrim(datumAnfang);
		this.datumEnde = ersetzeNullBlankTrim(datumEnde);
		this.datumKonferenz = ersetzeNullBlankTrim(datumKonferenz);
		this.datumZeugnis = ersetzeNullBlankTrim(datumZeugnis);
		this.fehlstundenGesamt = fehlstundenGesamt;
		this.fehlstundenGrenzwert = fehlstundenGrenzwert;
		this.fehlstundenUnentschuldigt = fehlstundenUnentschuldigt;
		this.foerderschwerpunkt1 = foerderschwerpunkt1;
		this.foerderschwerpunkt2 = foerderschwerpunkt2;
		this.foerderschwerpunktText = ersetzeNullBlankTrim(foerderschwerpunktText);
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
		this.idKlassenart = idKlassenart;
		this.leistungsdaten = (leistungsdaten != null) ? new ArrayList<>(leistungsdaten.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.nachpruefungen = nachpruefungen;
		this.noteDurchschnitt = ersetzeNullBlankTrim(noteDurchschnitt);
		this.noteLernbereichGSbzwAL = noteLernbereichGSbzwAL;
		this.noteLernbereichNW = noteLernbereichNW;
		this.idOrganisationsform = idOrganisationsform;
		this.pruefungsOrdnung = ersetzeNullBlankTrim(pruefungsOrdnung);
		this.schueler = schueler;
		this.idSchulgliederung = idSchulgliederung;
		this.schuljahresabschnitt = schuljahresabschnitt;
		this.sonderpaedagoge = sonderpaedagoge;
		this.textErgebnisPruefungsalgorithmus = ersetzeNullBlankTrim(textErgebnisPruefungsalgorithmus);
		this.tutor = tutor;
		this.uebergangsempfehlungText = ersetzeNullBlankTrim(uebergangsempfehlungText);
		this.versetzungsentscheidungText = ersetzeNullBlankTrim(versetzungsentscheidungText);
		this.versetzungsvermerkKuerzel = Versetzungsvermerk.data().getEintragByID(idVersetzungsvermerk).kuerzel;
		this.wechselNr = wechselNr;
		this.zeugnisart = ersetzeNullBlankTrim(zeugnisart);
		this.zeugnisASVText = ersetzeNullBlankTrim(zeugnisASVText);
		this.zeugnisAUEText = ersetzeNullBlankTrim(zeugnisAUEText);
		this.zeugnisBemerkungText = ersetzeNullBlankTrim(zeugnisBemerkungText);
		this.zeugnisLELSText = ersetzeNullBlankTrim(zeugnisLELSText);
		this.zuweisungen = (zuweisungen != null) ? new ArrayList<>(zuweisungen.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
	}



	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return 31 + Long.hashCode(this.id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof final ReportingSchuelerLernabschnitt other)) {
			return false;
		}
		return (this.id == other.id);
	}


	// ##### Berechnete Felder #####

	/**
	 * Bestimmt das voraussichtliche Entlassungsdatum von der Schule auf Basis der Restabschnitte des Jahrgangs und des Schuljahresabschnitts dieses
	 * Lernabschnitts.
	 *
	 * @return Voraussichtliches Entlassungsdatum
	 */
	public String jahrVoraussichtlicheEntlassung() {
		if ((jahrgang() == null) || (schuljahresabschnitt() == null) || (this.jahrgang.anzahlRestabschnitte() == null)) {
			return "";
		}

		return String.valueOf(this.schuljahresabschnitt.schuljahr() + (this.jahrgang.anzahlRestabschnitte() / 2) + (this.schuljahresabschnitt.abschnitt() % 2));
	}


	// ##### Getter #####

	/**
	 * Der erreichte allgemeinbildende Abschluss
	 *
	 * @return Inhalt des Feldes abschluss; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String abschluss() {
		return this.abschluss;
	}

	/**
	 * Die Art des Abschlusses (siehe Katalog)
	 *
	 * @return Inhalt des Feldes abschlussart
	 */
	public Integer abschlussart() {
		return this.abschlussart;
	}

	/**
	 * Der erreichte berufsbezogene Abschluss am Berufskolleg
	 *
	 * @return Inhalt des Feldes abschlussBerufsbildend; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String abschlussBerufsbildend() {
		return this.abschlussBerufsbildend;
	}

	/**
	 * Die Belegungen der Ankreuzkompetenzen des Schülers in diesem Lernabschnitt.
	 *
	 * @return Inhalt des Feldes ankreuzkompetenzen; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingSchuelerAnkreuzkompetenz> ankreuzkompetenzen() {
		return this.ankreuzkompetenzen;
	}

	/**
	 * Gibt die Ankreuzkompetenzen des Schülers zum Arbeits- und Sozialverhalten (ASV) aus diesem Lernabschnitt zurück,
	 * sortiert nach der Sortierung der Ankreuzkompetenz.
	 *
	 * @return Liste der Ankreuzkompetenzen zum ASV.
	 */
	public List<ReportingSchuelerAnkreuzkompetenz> ankreuzkompetenzenASV() {
		if (ankreuzkompetenzen() == null) {
			return new ArrayList<>();
		}
		return ankreuzkompetenzen().stream()
				.filter(a -> (a.ankreuzkompetenz() != null) && a.ankreuzkompetenz().istASV())
				.sorted(Comparator.comparingInt(a -> a.ankreuzkompetenz().sortierung()))
				.toList();
	}

	/**
	 * Gibt eine Map zurück, die zu jedem Fach, das nicht in den Leistungsdaten des Lernabschnitts enthalten ist,
	 * für das aber Ankreuzkompetenzen belegt sind, die zugehörigen Belegungen liefert. Die Map ist nach der Sortierung
	 * des Faches geordnet, die Belegungen pro Fach nach der Sortierung der Ankreuzkompetenz.
	 *
	 * @return Map mit Fach als Schlüssel und Liste der Ankreuzkompetenz-Belegungen als Wert.
	 */
	public Map<ReportingFach, List<ReportingSchuelerAnkreuzkompetenz>> ankreuzkompetenzenFaecherOhneLeistungsdaten() {
		if (ankreuzkompetenzen() == null) {
			return new LinkedHashMap<>();
		}
		final Set<Long> idsFaecherMitLeistungen = (leistungsdaten() == null) ? new HashSet<>()
				: leistungsdaten().stream()
						.filter(l -> l.fach() != null)
						.map(l -> l.fach().id())
						.collect(Collectors.toSet());
		return ankreuzkompetenzen().stream()
				.filter(a -> (a.ankreuzkompetenz() != null)
						&& (a.ankreuzkompetenz().fach() != null)
						&& !idsFaecherMitLeistungen.contains(a.ankreuzkompetenz().fach().id()))
				.sorted(Comparator
						.comparingInt((final ReportingSchuelerAnkreuzkompetenz a) -> a.ankreuzkompetenz().fachSortierung())
						.thenComparingInt(a -> a.ankreuzkompetenz().sortierung()))
				.collect(Collectors.groupingBy(
						a -> a.ankreuzkompetenz().fach(),
						LinkedHashMap::new,
						Collectors.toList()));
	}

	/**
	 * Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird
	 *
	 * @return Inhalt des Feldes bilingualerZweig; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String bilingualerZweig() {
		return this.bilingualerZweig;
	}

	/**
	 * Das Datum, wann der Lernabschnitt beginnt
	 *
	 * @return Inhalt des Feldes datumAnfang; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String datumAnfang() {
		return this.datumAnfang;
	}

	/**
	 * Das Datum, wann der Lernabschnitt endet
	 *
	 * @return Inhalt des Feldes datumEnde; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String datumEnde() {
		return this.datumEnde;
	}

	/**
	 * Das Datum der Konferenz
	 *
	 * @return Inhalt des Feldes datumKonferenz; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String datumKonferenz() {
		return this.datumKonferenz;
	}

	/**
	 * Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung
	 *
	 * @return Inhalt des Feldes datumZeugnis; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String datumZeugnis() {
		return this.datumZeugnis;
	}

	/**
	 * Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt
	 *
	 * @return Inhalt des Feldes fehlstundenGesamt
	 */
	public int fehlstundenGesamt() {
		return this.fehlstundenGesamt;
	}

	/**
	 * Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden
	 *
	 * @return Inhalt des Feldes fehlstundenGrenzwert
	 */
	public Integer fehlstundenGrenzwert() {
		return this.fehlstundenGrenzwert;
	}

	/**
	 * Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt
	 *
	 * @return Inhalt des Feldes fehlstundenUnentschuldigt
	 */
	public int fehlstundenUnentschuldigt() {
		return this.fehlstundenUnentschuldigt;
	}

	/**
	 * Der erste Förderschwerpunkt des Schülers zu diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes foerderschwerpunkt1
	 */
	public FoerderschwerpunktEintrag foerderschwerpunkt1() {
		return this.foerderschwerpunkt1;
	}

	/**
	 * Der zweite Förderschwerpunkt des Schülers zu diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes foerderschwerpunkt2
	 */
	public FoerderschwerpunktEintrag foerderschwerpunkt2() {
		return this.foerderschwerpunkt2;
	}

	/**
	 * Der Text mit Angaben zum Förderschwerpunkt.
	 *
	 * @return Inhalt des Feldes foerderschwerpunktText; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String foerderschwerpunktText() {
		return this.foerderschwerpunktText;
	}

	/**
	 * Die Folgeklasse des Schülers aus diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes folgeklasse
	 */
	public ReportingKlasse folgeklasse() {
		return this.folgeklasse;
	}

	/**
	 * Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt
	 *
	 * @return Inhalt des Feldes hatAOSF
	 */
	public boolean hatAOSF() {
		return this.hatAOSF;
	}

	/**
	 * Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht
	 *
	 * @return Inhalt des Feldes hatAutismus
	 */
	public boolean hatAutismus() {
		return this.hatAutismus;
	}

	/**
	 * Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht
	 *
	 * @return Inhalt des Feldes hatSchwerbehinderungsNachweis
	 */
	public boolean hatSchwerbehinderungsNachweis() {
		return this.hatSchwerbehinderungsNachweis;
	}

	/**
	 * Gibt an, ob zieldifferent unterrichtet wird oder nicht
	 *
	 * @return Inhalt des Feldes hatZieldifferentenUnterricht
	 */
	public boolean hatZieldifferentenUnterricht() {
		return this.hatZieldifferentenUnterricht;
	}

	/**
	 * Der Jahrgang des Schülers in diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes jahrgang; kann {@code null} sein, wenn dem Lernabschnitt kein Jahrgang zugeordnet ist.
	 */
	public ReportingJahrgang jahrgang() {
		return this.jahrgang;
	}

	/**
	 * Die ID des Lernabschnitts in der Datenbank.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return this.id;
	}

	/**
	 * Die ID der Fachklasse des Schülers an einem Berufskolleg
	 *
	 * @return Inhalt des Feldes idFachklasse
	 */
	public Long idFachklasse() {
		return this.idFachklasse;
	}

	/**
	 * Die ID des Hauptförderschwerpunktes des Schülers
	 *
	 * @return Inhalt des Feldes idFoerderschwerpunkt1
	 */
	public Long idFoerderschwerpunkt1() {
		return this.idFoerderschwerpunkt1;
	}

	/**
	 * Die ID des weiteren Förderschwerpunktes des Schülers
	 *
	 * @return Inhalt des Feldes idFoerderschwerpunkt2
	 */
	public Long idFoerderschwerpunkt2() {
		return this.idFoerderschwerpunkt2;
	}

	/**
	 * Die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht.
	 *
	 * @return Inhalt des Feldes idFolgeklasse
	 */
	public Long idFolgeklasse() {
		return this.idFolgeklasse;
	}

	/**
	 * Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist
	 *
	 * @return Inhalt des Feldes idJahrgang
	 */
	public Long idJahrgang() {
		return this.idJahrgang;
	}

	/**
	 * Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.
	 *
	 * @return Inhalt des Feldes idKlasse
	 */
	public Long idKlasse() {
		return this.idKlasse;
	}

	/**
	 * Die ID des Schülers des Lernabschnitts in der Datenbank.
	 *
	 * @return Inhalt des Feldes idSchueler
	 */
	public long idSchueler() {
		return this.idSchueler;
	}

	/**
	 * Die ID des Schuljahresabschnitts des Lernabschnitts in der Datenbank.
	 *
	 * @return Inhalt des Feldes idSchuljahresabschnitt
	 */
	public long idSchuljahresabschnitt() {
		return this.idSchuljahresabschnitt;
	}

	/**
	 * Die ID des Schwerpunktes des Schülers laut dem Schwerpunkt-Katalog
	 *
	 * @return Inhalt des Feldes idSchwerpunkt
	 */
	public Long idSchwerpunkt() {
		return this.idSchwerpunkt;
	}

	/**
	 * Die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat
	 *
	 * @return Inhalt des Feldes idSonderpaedagoge
	 */
	public Long idSonderpaedagoge() {
		return this.idSonderpaedagoge;
	}

	/**
	 * Die ID des Tutors des Schülers in der Datenbank.
	 *
	 * @return Inhalt des Feldes idTutor
	 */
	public Long idTutor() {
		return this.idTutor;
	}

	/**
	 * Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht (siehe Katalog)
	 *
	 * @return Inhalt des Feldes istAbschlussPrognose
	 */
	public boolean istAbschlussPrognose() {
		return this.istAbschlussPrognose;
	}

	/**
	 * Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend ist für Versetzung
	 *
	 * @return Inhalt des Feldes istFachpraktischerAnteilAusreichend
	 */
	public boolean istFachpraktischerAnteilAusreichend() {
		return this.istFachpraktischerAnteilAusreichend;
	}

	/**
	 * Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht
	 *
	 * @return Inhalt des Feldes istGewertet
	 */
	public boolean istGewertet() {
		return this.istGewertet;
	}

	/**
	 * Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht
	 *
	 * @return Inhalt des Feldes istWiederholung
	 */
	public boolean istWiederholung() {
		return this.istWiederholung;
	}

	/**
	 * Die Klasse des Schülers aus diesem Lernabschnitt
	 *
	 * @return Inhalt des Feldes klasse; kann {@code null} sein, wenn dem Lernabschnitt keine Klasse zugeordnet ist.
	 */
	public ReportingKlasse klasse() {
		return this.klasse;
	}

	/**
	 * Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type)
	 *
	 * @return Inhalt des Feldes klassenart
	 */
	public Long idKlassenart() {
		return this.idKlassenart;
	}

	/**
	 * Gibt die Leistungsdaten des Klassenunterrichts zurück. Klassenunterricht liegt vor, wenn kein Kurs eingetragen ist.
	 *
	 * @return Liste der Leistungsdaten des Klassenunterrichts
	 */
	public List<ReportingSchuelerLeistungsdaten> klassenunterricht() {
		if (leistungsdaten() == null) {
			return new ArrayList<>();
		}
		// Unterrichte ohne Kurs haben die ID -1 in der ListMap im dritten Index (Kurs-ID)
		return this.listMapLeistungsdaten.get3(-1);
	}

	/**
	 * Gibt die Leistungsdaten des Kursunterrichts zurück. Kursunterricht liegt vor, wenn ein Kurs eingetragen ist.
	 *
	 * @return Liste der Leistungsdaten des Kursunterrichts
	 */
	public List<ReportingSchuelerLeistungsdaten> kursunterricht() {
		if (leistungsdaten() == null) {
			return new ArrayList<>();
		}

		// Kursunterrichte haben eine Kurs-ID, also hat Key3 einen Wert größer -1.
		final List<ReportingSchuelerLeistungsdaten> leistungsdatenKursunterrichte = new ArrayList<>();
		this.listMapLeistungsdaten.keySet3().stream().toList().stream().filter(idKurs -> (idKurs > -1))
				.forEach(idKurs -> leistungsdatenKursunterrichte.addAll(this.listMapLeistungsdaten.get3(idKurs)));

		return leistungsdatenKursunterrichte;
	}

	/**
	 * Die Leistungsdaten des Schülers in diesem Lernabschnitt.
	 *
	 * @return Inhalt des Feldes leistungsdaten; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingSchuelerLeistungsdaten> leistungsdaten() {
		return this.leistungsdaten;
	}

	/**
	 * Liefert den Eintrag der Schüler-Leistungsdaten, der dem Klassenunterricht zu den gegebenen IDs von Fach und Lehrer entspricht.
	 *
	 * @param idFach Die ID des Fachs, zu dem die Leistungsdaten abgefragt werden sollen.
	 * @param idLehrer Die ID des Lehrers, der dem Unterricht zugeordnet ist.
	 *
	 * @return Der Eintrag aus den Schüler-Leistungsdaten, der dem Klassenunterricht zu den gegebenen IDs von Fach und Lehrer. Gibt es mehrere, so wird die
	 * Kursart PUK oder leer gewählt. Wird kein Eintrag gefunden, so wird null zurückgegeben.
	 */
	public ReportingSchuelerLeistungsdaten leistungsdatenKlassenunterrichtZurIdFachIdLehrer(final long idFach, final long idLehrer) {
		if ((idFach < 0) || (idLehrer < 0) || (leistungsdaten() == null)) {
			return null;
		}

		// Klassenunterrichte haben keine Kurs-ID, also hat Key3 den Wert -1.
		final List<ReportingSchuelerLeistungsdaten> leistungsdatenZumFachUndLehrer =
				this.listMapLeistungsdaten.get23(idFach, -1).stream().filter(l -> (l.fachlehrer() != null) && (l.fachlehrer().id() == idLehrer)).toList();

		// Wenn es keinen Eintrag gibt, gebe null zurück.
		if (leistungsdatenZumFachUndLehrer.isEmpty()) {
			return null;
		}

		// Wenn nur ein Eintrag existiert, wird dieser zurückgegeben
		if (leistungsdatenZumFachUndLehrer.size() == 1) {
			return leistungsdatenZumFachUndLehrer.getFirst();
		}

		// Wenn es mehrere Leistungsdatensätze ohne Kurs-ID, aber mit gleichem Fach und Lehrer gibt, muss noch die Kursart verschieden sein.
		// Wähle dann die Kursart PUK zuerst und wenn das nicht hilft, die leere Kursart. Andernfalls null.
		return leistungsdatenZumFachUndLehrer.stream()
				.filter(l -> "PUK".equalsIgnoreCase(l.kursart) || "".equals(l.kursart))
				.min(Comparator.comparingInt(l -> "PUK".equalsIgnoreCase(l.kursart) ? 0 : 1))
				.orElse(null);
	}

	/**
	 * Gibt die Leistungsdaten eines Schülers basierend auf der angegebenen ID zurück.
	 *
	 * @param id Die ID, die zur Identifikation der Leistungsdaten verwendet wird.
	 *
	 * @return Die Leistungsdaten des Schülers, wenn die ID vorhanden ist. Gibt null zurück, wenn keine
	 *         entsprechenden Leistungsdaten gefunden werden.
	 */
	public ReportingSchuelerLeistungsdaten leistungsdatenZurId(final long id) {
		if ((id >= 0) && (leistungsdaten() != null)) {
			return this.listMapLeistungsdaten.getSingle1OrNull(id);
		}
		return null;
	}

	/**
	 * Liefert eine Liste von Schüler-Leistungsdaten, die zu einer angegebenen Fach-ID und Lehrer-ID gehören und kein Kursunterricht sind.
	 *
	 * @param idFach Die ID des Fachs, zu dem die Leistungsdaten abgefragt werden sollen.
	 *
	 * @return Eine Liste von ReportingSchuelerLeistungsdaten, die zu der angegebenen Fach-ID gehören,
	 * oder eine leere Liste, falls keine Daten gefunden wurden.
	 */
	public List<ReportingSchuelerLeistungsdaten> leistungsdatenZurIdFach(final long idFach) {
		if ((idFach >= 0) && (leistungsdaten() != null)) {
			return this.listMapLeistungsdaten.get2(idFach);
		}
		return new ArrayList<>();
	}

	/**
	 * Methode, um die Leistungsdaten eines Schülers anhand der Kurs-ID abzurufen.
	 *
	 * @param idKurs Die eindeutige ID des Kurses, für den die Leistungsdaten abgerufen werden sollen.
	 *
	 * @return Ein Objekt vom Typ ReportingSchuelerLeistungsdaten, das die Leistungsdaten des Schülers zu dem angegebenen Kurs enthält,
	 *         oder null, wenn keine entsprechenden Daten gefunden werden.
	 */
	public ReportingSchuelerLeistungsdaten leistungsdatenZurIdKurs(final long idKurs) {
		if ((idKurs >= 0) && (leistungsdaten() != null)) {
			return this.listMapLeistungsdaten.getSingle3OrNull(idKurs);
		}
		return null;
	}

	/**
	 * Die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind.
	 *
	 * @return Inhalt des Feldes nachpruefungen
	 */
	public SchuelerLernabschnittNachpruefungsdaten nachpruefungen() {
		return this.nachpruefungen;
	}

	/**
	 * Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden
	 *
	 * @return Inhalt des Feldes noteDurchschnitt; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String noteDurchschnitt() {
		return this.noteDurchschnitt;
	}

	/**
	 * Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klassen 10
	 *
	 * @return Inhalt des Feldes noteLernbereichGSbzwAL
	 */
	public Integer noteLernbereichGSbzwAL() {
		return this.noteLernbereichGSbzwAL;
	}

	/**
	 * Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10
	 *
	 * @return Inhalt des Feldes noteLernbereichNW
	 */
	public Integer noteLernbereichNW() {
		return this.noteLernbereichNW;
	}

	/**
	 * Die ID der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type)
	 *
	 * @return die ID der Organisationsform.
	 */
	public Long idOrganisationsform() {
		return this.idOrganisationsform;
	}

	/**
	 * Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.
	 *
	 * @return Inhalt des Feldes pruefungsOrdnung; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String pruefungsOrdnung() {
		return this.pruefungsOrdnung;
	}

	/**
	 * Der Schüler, zu dem diese Lernabschnittsdaten gehören.
	 *
	 * @return Inhalt des Feldes schueler; kann {@code null} sein, wenn die Daten des Schülers nicht geladen werden konnten.
	 */
	public ReportingSchueler schueler() {
		return this.schueler;
	}

	/**
	 * Die ID der Schulgliederung bzw. des Bildungsgangs des Schülers.
	 *
	 * @return die ID des Feldes schulgliederung.
	 */
	public Long idSchulgliederung() {
		return this.idSchulgliederung;
	}

	/**
	 * Der Schuljahresabschnitt, zu welchem diese Lernabschnittsdaten gehören.
	 *
	 * @return Inhalt des Feldes schuljahresabschnitt; kann {@code null} sein, wenn kein Schuljahresabschnitt zugeordnet ist.
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return this.schuljahresabschnitt;
	}

	/**
	 * Der Sonderpädagoge, der den Schüler betreut
	 *
	 * @return Inhalt des Feldes sonderpaedagoge
	 */
	public ReportingLehrer sonderpaedagoge() {
		return this.sonderpaedagoge;
	}

	/**
	 * Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung
	 *
	 * @return Inhalt des Feldes textErgebnisPruefungsalgorithmus; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String textErgebnisPruefungsalgorithmus() {
		return this.textErgebnisPruefungsalgorithmus;
	}

	/**
	 * Der Tutor, der den Schüler betreut
	 *
	 * @return Inhalt des Feldes tutor
	 */
	public ReportingLehrer tutor() {
		return this.tutor;
	}

	/**
	 * Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I.
	 *
	 * @return Inhalt des Feldes uebergangsempfehlungText; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String uebergangsempfehlungText() {
		return this.uebergangsempfehlungText;
	}

	/**
	 * Der Text zur Versetzungsentscheidung auf dem Zeugnis.
	 *
	 * @return Inhalt des Feldes versetzungsentscheidungText; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String versetzungsentscheidungText() {
		return this.versetzungsentscheidungText;
	}

	/**
	 * Das Kürzel des Versetzungsvermerks
	 *
	 * @return Inhalt des Feldes versetzungsvermerkKuerzel; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String versetzungsvermerkKuerzel() {
		return this.versetzungsvermerkKuerzel;
	}

	/**
	 * Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z. B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.).
	 *
	 * @return Inhalt des Feldes wechselNr
	 */
	public int wechselNr() {
		return this.wechselNr;
	}

	/**
	 * Die Art des Zeugnisses
	 *
	 * @return Inhalt des Feldes zeugnisart; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String zeugnisart() {
		return this.zeugnisart;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten.
	 *
	 * @return Inhalt des Feldes zeugnisASVText; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String zeugnisASVText() {
		return this.zeugnisASVText;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement.
	 *
	 * @return Inhalt des Feldes zeugnisAUEText; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String zeugnisAUEText() {
		return this.zeugnisAUEText;
	}

	/**
	 * Der Text für allgemeine Zeugnisbemerkungen.
	 *
	 * @return Inhalt des Feldes zeugnisBemerkungText; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String zeugnisBemerkungText() {
		return this.zeugnisBemerkungText;
	}

	/**
	 * Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen.
	 *
	 * @return Inhalt des Feldes zeugnisLELSText; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String zeugnisLELSText() {
		return this.zeugnisLELSText;
	}

	/**
	 * Die Zuweisungen des Schülers in diesem Lernabschnitt.
	 *
	 * @return Inhalt des Feldes zuweisungen; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingSchuelerZuweisung> zuweisungen() {
		return this.zuweisungen;
	}


	// ##### Setter #####

	/**
	 * Setzt die Leistungsdaten des Schülers in diesem Lernabschnitt neu und aktualisiert die Liste der Leistungsdaten-Map.
	 *
	 * @param leistungsdaten Die hinzuzufügenden Leistungsdaten.
	 */
	public void setLeistungsdaten(final List<ReportingSchuelerLeistungsdaten> leistungsdaten) {
		this.leistungsdaten = new ArrayList<>();
		this.listMapLeistungsdaten = new ListMap3DLongKeys<>();
		if (leistungsdaten != null) {
			for (final ReportingSchuelerLeistungsdaten leistungsdatenElement : leistungsdaten) {
				if (leistungsdatenElement != null) {
					final long idFach = (leistungsdatenElement.fach() == null) ? -1 : leistungsdatenElement.fach().id();
					final long idKurs = (leistungsdatenElement.kurs() == null) ? -1 : leistungsdatenElement.kurs().id();
					this.leistungsdaten.add(leistungsdatenElement);
					this.listMapLeistungsdaten.add(leistungsdatenElement.id(), idFach, idKurs, leistungsdatenElement);
				}
			}
		}
	}
}
