package de.svws_nrw.module.reporting.types.schule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.module.reporting.types.ankreuzkompetenz.ProxyReportingAnkreuzkompetenz;
import de.svws_nrw.module.reporting.types.ankreuzkompetenz.ReportingAnkreuzkompetenz;
import de.svws_nrw.module.reporting.types.fach.ProxyReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ProxyReportingJahrgang;
import de.svws_nrw.module.reporting.types.lerngruppen.ProxyReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ProxyReportingKurs;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schuljahresabschnitt und erweitert die Klasse {@link ReportingSchuljahresabschnitt}.
 */
public class ProxyReportingSchuljahresabschnitt extends ReportingSchuljahresabschnitt {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuljahresabschnitt}.
	 *
	 * @param reportingContext 	Repository für das Reporting.
	 * @param schuljahresabschnitt	Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchuljahresabschnitt(final ReportingContext reportingContext, final Schuljahresabschnitt schuljahresabschnitt) {
		super(schuljahresabschnitt.id,
				schuljahresabschnitt.schuljahr,
				schuljahresabschnitt.abschnitt,
				schuljahresabschnitt.idFolgeAbschnitt,
				schuljahresabschnitt.idVorigerAbschnitt,
				null,
				null,
				new HashMap<>(),
				new HashMap<>(),
				new HashMap<>(),
				new HashMap<>(),
				new HashMap<>());

		this.reportingContext = reportingContext;
	}


	/**
	 * Gibt den folgenden Schuljahresabschnitt zurück (lazy-loading).
	 *
	 * @return Der folgende Schuljahresabschnitt oder null.
	 */
	@Override
	public ReportingSchuljahresabschnitt folgenderAbschnitt() {
		if ((super.folgenderAbschnitt == null) && (super.idFolgenderAbschnitt != null)) {
			super.folgenderAbschnitt = this.reportingContext.repositorySchule().schuljahresabschnitt(super.idFolgenderAbschnitt);
		}
		return super.folgenderAbschnitt;
	}

	/**
	 * Gibt den vorherigen Schuljahresabschnitt zurück (lazy-loading).
	 *
	 * @return Der vorherige Schuljahresabschnitt oder null.
	 */
	@Override
	public ReportingSchuljahresabschnitt vorherigerAbschnitt() {
		if ((super.vorherigerAbschnitt == null) && (super.idVorherigerAbschnitt != null)) {
			super.vorherigerAbschnitt = this.reportingContext.repositorySchule().schuljahresabschnitt(super.idVorherigerAbschnitt);
		}
		return super.vorherigerAbschnitt;
	}


	/**
	 * Gibt die Map der Fächer dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Fächer, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingFach> faecher() {
		if ((super.faecher == null) || super.faecher.isEmpty()) {
			super.faecher = new HashMap<>();
			this.reportingContext.repositoryKataloge().faecher()
					.forEach((idFach, fach) -> super.faecher.put(idFach, new ProxyReportingFach(fach, this.schuljahr)));
		}
		return super.faecher;
	}

	/**
	 * Gibt die Map der Jahrgänge dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Jahrgänge, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingJahrgang> jahrgaenge() {
		if ((super.jahrgaenge == null) || super.jahrgaenge.isEmpty()) {
			super.jahrgaenge = new HashMap<>();
			this.reportingContext.repositoryKataloge().jahrgaenge().forEach((idJahrgang, jahrgang) -> super.jahrgaenge.put(idJahrgang,
					new ProxyReportingJahrgang(this.reportingContext, jahrgang, this)));
		}
		return super.jahrgaenge;
	}

	/**
	 * Gibt die Map der Klassen dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Klassen, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingKlasse> klassen() {
		if ((super.klassen == null) || super.klassen.isEmpty()) {
			super.klassen = new HashMap<>();
			final List<KlassenDaten> klassendaten = this.reportingContext.repositoryLerngruppen().klassenBySchuljahresabschnitt(this.id());
			if (klassendaten.isEmpty()) {
				return super.klassen;
			}

			for (final KlassenDaten klasse : klassendaten) {
				final ReportingKlasse reportingKlasse = new ProxyReportingKlasse(this.reportingContext, klasse);
				super.klassen.put(reportingKlasse.id(), reportingKlasse);
				this.reportingContext.repositoryLerngruppen().klassen().put(reportingKlasse.id(), reportingKlasse);
			}

		}
		return super.klassen;
	}

	/**
	 * Gibt die Map der Kurse dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Kurse, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingKurs> kurse() {
		if ((super.kurse == null) || super.kurse.isEmpty()) {
			super.kurse = new HashMap<>();
			final List<KursDaten> kurseDaten = this.reportingContext.repositoryLerngruppen().kurseBySchuljahresabschnitt(this.id());
			if (kurseDaten.isEmpty()) {
				return super.kurse;
			}

			for (final KursDaten kurs : kurseDaten) {
				final ReportingKurs reportingKurs = new ProxyReportingKurs(this.reportingContext, kurs);
				super.kurse.put(reportingKurs.id(), reportingKurs);
				this.reportingContext.repositoryLerngruppen().kurse().put(reportingKurs.id(), reportingKurs);
			}
		}
		return super.kurse;
	}

	/**
	 * Gibt die Map der Ankreuzkompetenzen dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Ankreuzkompetenzen, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingAnkreuzkompetenz> ankreuzkompetenzen() {
		if ((super.ankreuzkompetenzen == null) || super.ankreuzkompetenzen.isEmpty()) {
			super.ankreuzkompetenzen = new HashMap<>();
			this.reportingContext.repositoryKataloge().ankreuzkompetenzen()
					.forEach((idAnkreuzkompetenz, ankreuzkompetenz) -> {
						if ((ankreuzkompetenz.abschnitt == 0) || (ankreuzkompetenz.abschnitt == super.abschnitt)) {
							super.ankreuzkompetenzen.put(idAnkreuzkompetenz, new ProxyReportingAnkreuzkompetenz(ankreuzkompetenz, this));
						}
					});
		}
		return super.ankreuzkompetenzen;
	}
}
