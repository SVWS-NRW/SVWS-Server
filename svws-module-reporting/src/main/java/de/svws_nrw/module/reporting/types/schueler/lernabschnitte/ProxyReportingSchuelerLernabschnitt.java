package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lernabschnitt und erweitert die Klasse {@link ReportingSchuelerLernabschnitt}.
 */
public class ProxyReportingSchuelerLernabschnitt extends ReportingSchuelerLernabschnitt {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerLernabschnitt}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param schuelerLernabschnittsdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchuelerLernabschnitt(final ReportingContext reportingContext, final SchuelerLernabschnittsdaten schuelerLernabschnittsdaten) {
		super(ersetzeNullBlankTrim(schuelerLernabschnittsdaten.abschluss),
				schuelerLernabschnittsdaten.abschlussart,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.abschlussBerufsbildend),
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bilingualerZweig),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.datumAnfang),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.datumEnde),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.datumKonferenz),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.datumZeugnis),
				schuelerLernabschnittsdaten.fehlstundenGesamt,
				schuelerLernabschnittsdaten.fehlstundenGrenzwert,
				schuelerLernabschnittsdaten.fehlstundenUnentschuldigt,
				null,
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.foerderschwerpunkt),
				null,
				schuelerLernabschnittsdaten.hatAOSF,
				schuelerLernabschnittsdaten.hatAutismus,
				schuelerLernabschnittsdaten.hatSchwerbehinderungsNachweis,
				schuelerLernabschnittsdaten.hatZieldifferentenUnterricht,
				null,
				schuelerLernabschnittsdaten.id,
				schuelerLernabschnittsdaten.fachklasseID,
				schuelerLernabschnittsdaten.foerderschwerpunkt1ID,
				schuelerLernabschnittsdaten.foerderschwerpunkt2ID,
				schuelerLernabschnittsdaten.folgeklassenID,
				schuelerLernabschnittsdaten.jahrgangID,
				schuelerLernabschnittsdaten.klassenID,
				schuelerLernabschnittsdaten.schuelerID,
				schuelerLernabschnittsdaten.schuljahresabschnitt,
				schuelerLernabschnittsdaten.schwerpunktID,
				schuelerLernabschnittsdaten.sonderpaedagogeID,
				schuelerLernabschnittsdaten.tutorID,
				schuelerLernabschnittsdaten.istAbschlussPrognose,
				schuelerLernabschnittsdaten.istFachpraktischerAnteilAusreichend,
				schuelerLernabschnittsdaten.istGewertet,
				schuelerLernabschnittsdaten.istWiederholung,
				null,
				schuelerLernabschnittsdaten.idKlassenart,
				new ArrayList<>(),
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.noteDurchschnitt),
				schuelerLernabschnittsdaten.noteLernbereichGSbzwAL,
				schuelerLernabschnittsdaten.noteLernbereichNW,
				schuelerLernabschnittsdaten.idOrganisationsform,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.pruefungsOrdnung),
				null,
				schuelerLernabschnittsdaten.idSchulgliederung,
				null,
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.textErgebnisPruefungsalgorithmus),
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.uebergangESF),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.versetzungsentscheidung),
				schuelerLernabschnittsdaten.idVersetzungsvermerk,
				schuelerLernabschnittsdaten.wechselNr,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.zeugnisart),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.zeugnisASV),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.zeugnisAUE),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.zeugnisAllgemein),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.zeugnisLELS),
				null);

		this.reportingContext = reportingContext;

		super.foerderschwerpunkt1 = this.reportingContext.repositoryKataloge().foerderschwerpunkt(schuelerLernabschnittsdaten.foerderschwerpunkt1ID);
		super.foerderschwerpunkt2 = this.reportingContext.repositoryKataloge().foerderschwerpunkt(schuelerLernabschnittsdaten.foerderschwerpunkt2ID);

		super.schuljahresabschnitt = this.reportingContext.repositorySchule().schuljahresabschnitt(super.idSchuljahresabschnitt());

		// Der Rückverweis auf den Schüler wird ungefiltert aufgelöst - der Lernabschnitt gehört fachlich zu diesem Schüler,
		// unabhängig davon, ob der Schüler selbst in der Ausgabe erscheint.
		super.schueler = this.reportingContext.repositorySchueler().schuelerOhneFilter(schuelerLernabschnittsdaten.schuelerID);

		schuelerLernabschnittsdaten.leistungsdaten.forEach(
				l -> this.reportingContext.repositorySchueler().leistungsdaten().add(schuelerLernabschnittsdaten.schuelerID, id(), l.id, l));
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return this.reportingContext;
	}


	/**
	 * Stellt die Belegungen der Ankreuzkompetenzen zur Verfügung, die in diesem Lernabschnitt dem Schüler zugeordnet sind.
	 * Beim ersten Zugriff wird ein Bulk-Load über alle Lernabschnitte des zugehörigen Schuljahresabschnitts ausgelöst.
	 *
	 * @return Die Belegungen der Ankreuzkompetenzen des Lernabschnitts.
	 */
	@Override
	public List<ReportingSchuelerAnkreuzkompetenz> ankreuzkompetenzen() {
		if ((super.ankreuzkompetenzen == null) || super.ankreuzkompetenzen.isEmpty()) {
			super.ankreuzkompetenzen = this.reportingContext.repositorySchueler().schuelerLernabschnittAnkreuzkompetenzen(this);
		}
		return super.ankreuzkompetenzen;
	}

	/**
	 * Stellt die Daten der Folgeklasse des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 *
	 * @return Daten der Folgeklasse
	 */
	@Override
	public ReportingKlasse folgeklasse() {
		if ((super.folgeklasse() == null) && (super.idFolgeklasse() != null) && (super.idFolgeklasse() >= 0)) {
			super.folgeklasse = this.reportingContext.repositoryLerngruppen().klasse(super.idFolgeklasse());
		}
		return super.folgeklasse();
	}

	/**
	 * Stellt die Daten des Jahrgangs des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 *
	 * @return Daten des Jahrgangs
	 */
	@Override
	public ReportingJahrgang jahrgang() {
		if ((super.jahrgang == null) && (super.idJahrgang != null) && (super.idJahrgang >= 0) && (super.schuljahresabschnitt != null)) {
			super.jahrgang = super.schuljahresabschnitt.jahrgang(super.idJahrgang);
		}
		return super.jahrgang();
	}

	/**
	 * Stellt die Daten der Klasse des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 *
	 * @return Daten der Klasse
	 */
	@Override
	public ReportingKlasse klasse() {
		if ((super.klasse() == null) && (super.idKlasse() != null) && (super.idKlasse() >= 0) && (super.schuljahresabschnitt != null)) {
			super.klasse = super.schuljahresabschnitt.klasse(super.idKlasse);
		}
		return super.klasse();
	}

	/**
	 * Stellt die Leistungsdaten zur Verfügung, die in diesem Lernabschnitt dem Schüler zugeordnet sind.
	 * Beim ersten Zugriff wird ein Bulk-Load über alle Lernabschnitte des zugehörigen Schuljahresabschnitts ausgelöst.
	 *
	 * @return Die Leistungsdaten des Lernabschnitts.
	 */
	@Override
	public List<ReportingSchuelerLeistungsdaten> leistungsdaten() {
		if (super.leistungsdaten().isEmpty()) {
			final ReportingSortierungService sortierungService = this.reportingContext.sortierungService();
			final Logger logger = this.reportingContext.logger();

			final Comparator<ReportingSchuelerLeistungsdaten> comparator =
					ComparatorFactory.buildComparator(sortierungService, logger, ReportingSchuelerLeistungsdaten.class.getSimpleName(),
							ReportingSchuelerLeistungsdaten.SORTIERUNG, true);

			final Predicate<ReportingSchuelerLeistungsdaten> filter = ReportingSchuelerLeistungsdaten.FILTER.bedingung(
					this.reportingContext.filterService().getFilter(ReportingSchuelerLeistungsdaten.class.getSimpleName()), null);

			final var leistungsdaten = this.reportingContext.repositorySchueler().leistungsdatenZuLernabschnitt(this);
			super.setLeistungsdaten(leistungsdaten.stream().filter(filter).sorted(comparator).toList());
		}
		return super.leistungsdaten();
	}

	// TODO Klasse für die Nachprüfungen für die Reporting erzeugen und dann die Daten im überschriebenen Getter hier dynamisch nachladen.

	/**
	 * Stellt die Daten des Sonderpädagogen zur Verfügung, der in diesem Lernabschnitt dem Schüler zugeordnet ist.
	 *
	 * @return Daten des Sonderpädagogen
	 */
	@Override
	public ReportingLehrer sonderpaedagoge() {
		if ((super.sonderpaedagoge() == null) && (super.idSonderpaedagoge() != null) && (super.idSonderpaedagoge() >= 0)) {
			super.sonderpaedagoge = this.reportingContext.repositoryLehrer().lehrer(super.idSonderpaedagoge());
		}
		return super.sonderpaedagoge();
	}

	/**
	 * Stellt die Daten des Tutors zur Verfügung, der in diesem Lernabschnitt dem Schüler zugeordnet ist.
	 *
	 * @return Daten des Tutors
	 */
	@Override
	public ReportingLehrer tutor() {
		if ((super.tutor() == null) && (super.idTutor() >= 0)) {
			super.tutor = this.reportingContext.repositoryLehrer().lehrer(super.idTutor());
		}
		return super.tutor();
	}

	/**
	 * Die Zuweisungen des Schülers in diesem Lernabschnitt.
	 *
	 * @return DIe Liste der Zuweisungen.
	 */
	@Override
	public List<ReportingSchuelerZuweisung> zuweisungen() {
		if ((super.zuweisungen == null) || super.zuweisungen.isEmpty()) {
			super.zuweisungen = this.reportingContext.repositorySchueler().zuweisungen(id(), this);
		}
		return super.zuweisungen;
	}
}
