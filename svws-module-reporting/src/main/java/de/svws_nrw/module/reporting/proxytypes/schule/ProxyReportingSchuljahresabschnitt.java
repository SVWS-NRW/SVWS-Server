package de.svws_nrw.module.reporting.proxytypes.schule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.module.reporting.proxytypes.fach.ProxyReportingFach;
import de.svws_nrw.module.reporting.proxytypes.jahrgang.ProxyReportingJahrgang;
import de.svws_nrw.module.reporting.proxytypes.klasse.ProxyReportingKlasse;
import de.svws_nrw.module.reporting.proxytypes.kurs.ProxyReportingKurs;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schuljahresabschnitt und erweitert die Klasse {@link ReportingSchuljahresabschnitt}.
 */
public class ProxyReportingSchuljahresabschnitt extends ReportingSchuljahresabschnitt {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuljahresabschnitt}.
	 *
	 * @param reportingRepository 	Repository für das Reporting.
	 * @param schuljahresabschnitt	Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchuljahresabschnitt(final ReportingRepository reportingRepository, final Schuljahresabschnitt schuljahresabschnitt) {
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
				new HashMap<>());

		this.reportingRepository = reportingRepository;

		super.folgenderAbschnitt =
				(schuljahresabschnitt.idFolgeAbschnitt != null) ? this.reportingRepository.schuljahresabschnitt(schuljahresabschnitt.idFolgeAbschnitt) : null;
		super.vorherigerAbschnitt =
				(schuljahresabschnitt.idVorigerAbschnitt != null) ? this.reportingRepository.schuljahresabschnitt(schuljahresabschnitt.idVorigerAbschnitt)
						: null;

		// Die weiteren Daten werden später durch lazy-loading ergänzt
	}


	/**
	 * Gibt die Map der Fächer dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Fächer, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingFach> mapFaecher() {
		if ((super.mapFaecher == null) || super.mapFaecher.isEmpty()) {
			super.mapFaecher = new HashMap<>();
			this.reportingRepository.mapFaecher().forEach((idFach, fach) -> super.mapFaecher.put(idFach, new ProxyReportingFach(fach, this.schuljahr)));
		}
		return super.mapFaecher;
	}

	/**
	 * Gibt die Map der Jahrgänge dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Jahrgänge, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingJahrgang> mapJahrgaenge() {
		if ((super.mapJahrgaenge == null) || super.mapJahrgaenge.isEmpty()) {
			super.mapJahrgaenge = new HashMap<>();
			// TODO: Wenn die Jahrgänge eine Gültigkeit erhalten, dann ist diese hier auch zu implementieren. Aktuell werden in alle Schuljahresabschnitte alle
			//  Jahrgänge übernommen.
			this.reportingRepository.mapJahrgaenge().forEach((idJahrgang, jahrgang) -> super.mapJahrgaenge.put(idJahrgang,
					new ProxyReportingJahrgang(this.reportingRepository, jahrgang, this)));
		}
		return super.mapJahrgaenge;
	}

	/**
	 * Gibt die Map der Klassen dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Klassen, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingKlasse> mapKlassen() {
		if ((super.mapKlassen == null) || super.mapKlassen.isEmpty()) {
			super.mapKlassen = new HashMap<>();
			List<KlassenDaten> klassendaten = new ArrayList<>();
			try {
				this.reportingRepository.logger().logLn(LogLevel.DEBUG, 8, "Ermittle die Klassendaten.");
				klassendaten = new DataKlassendaten(this.reportingRepository.conn()).getListBySchuljahresabschnittID(this.id(), true);
			} catch (final Exception e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Erstellung der Klassenliste für den Schuljahresabschnitt %s.".formatted(this.textSchuljahresabschnittKurz()), e,
						reportingRepository.logger(), LogLevel.ERROR, 0);
			}
			if (klassendaten.isEmpty())
				return super.mapKlassen;

			for (final KlassenDaten klasse : klassendaten) {
				final ReportingKlasse reportingKlasse = new ProxyReportingKlasse(this.reportingRepository, klasse);
				super.mapKlassen.put(reportingKlasse.id(), reportingKlasse);
				this.reportingRepository.mapKlassen().put(reportingKlasse.id(), reportingKlasse);
			}

		}
		return super.mapKlassen;
	}

	/**
	 * Gibt die Map der Kurse dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Kurse, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingKurs> mapKurse() {
		if ((super.mapKurse == null) || super.mapKurse.isEmpty()) {
			super.mapKurse = new HashMap<>();
			List<KursDaten> kurseDaten = new ArrayList<>();
			try {
				this.reportingRepository.logger().logLn(LogLevel.DEBUG, 8, "Ermittle die Kursdaten.");
				kurseDaten = new DataKurse(this.reportingRepository.conn()).getListBySchuljahresabschnittID(this.id(), true);
			} catch (final Exception e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Erstellung der Liste der Kurse für den Schuljahresabschnitt %s.".formatted(this.textSchuljahresabschnittKurz()),
						e, reportingRepository.logger(), LogLevel.ERROR, 0);
			}
			if (kurseDaten.isEmpty())
				return super.mapKurse;

			for (final KursDaten kurs : kurseDaten) {
				final ReportingKurs reportingKurs = new ProxyReportingKurs(this.reportingRepository, kurs);
				super.mapKurse.put(reportingKurs.id(), reportingKurs);
				this.reportingRepository.mapKurse().put(reportingKurs.id(), reportingKurs);
			}
		}
		return super.mapKurse;
	}
}
