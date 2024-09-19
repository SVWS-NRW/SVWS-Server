package de.svws_nrw.module.reporting.proxytypes.schule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.faecher.DataFachdaten;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.fach.ProxyReportingFach;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schuljahresabschnitt und erweitert die Klasse {@link ReportingSchuljahresabschnitt}.</p>
 *
 *  <p>In diesem Kontext besitzt die Proxy-Klasse ausschließlich die gleichen Methoden wie die zugehörige Reporting-Super-Klasse.
 *  Während die Super-Klasse aber als reiner Datentyp konzipiert ist, d. h. ohne Anbindung an die Datenbank,
 *  greift die Proxy-Klassen an verschiedenen Stellen auf die Datenbank zu.</p>
 *
 *  <ul>
 *      <li>Die Proxy-Klasse stellt in der Regel einen zusätzlichen Constructor zur Verfügung, um Reporting-Objekte
 *  		aus Stammdatenobjekten (aus dem Package core.data) erstellen zu können. Darin werden Felder, die Reporting-Objekte
 *  		zurückgegeben und nicht im Stammdatenobjekt enthalten sind, mit null initialisiert.</li>
 * 		<li>Die Proxy-Klasse überschreibt einzelne Getter der Super-Klasse (beispielsweise bei Felder, die mit null initialisiert wurden)
 *  		und lädt dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
 *  		halten.</li>
 *  </ul>
 */
public class ProxyReportingSchuljahresabschnitt extends ReportingSchuljahresabschnitt {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis eines Stammdaten-Objektes.
	 * @param reportingRepository 	Repository für die Reporting.
	 * @param schuljahresabschnitt	Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchuljahresabschnitt(final ReportingRepository reportingRepository, final Schuljahresabschnitt schuljahresabschnitt) {
		super(schuljahresabschnitt.abschnitt, schuljahresabschnitt.id, schuljahresabschnitt.schuljahr, new ArrayList<>());

		this.reportingRepository = reportingRepository;

		// Die weiteren Daten werden später durch lazy-loading ergänzt
	}


	/**
	 * Gibt die Liste der faecher dieses Schuljahresabschnitts zurück. Ist diese Liste leer, so wird sie aus den Fächern des Repositories erzeugt.
	 * @return Liste der Fächer, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public List<ReportingFach> faecher() {
		if ((super.faecher == null) || super.faecher.isEmpty()) {
			super.faecher = new ArrayList<>();
			final List<FachDaten> faecherdaten = new ArrayList<>();

			try {
				faecherdaten.addAll(new DataFachdaten(this.reportingRepository.conn())
						.getFaecherdatenFromList(this.reportingRepository.mapFaecher().values().stream().toList()).values().stream().toList());
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Erstellung der Fächerliste für den Schuljahresabschnitt %d.%d.".formatted(this.schuljahr, this.abschnitt), e,
						reportingRepository.logger(), LogLevel.ERROR, 0);
			}
			if (faecherdaten.isEmpty())
				return super.faecher;

			final Map<Long, DTOFach> mapfaecherFuerFaecherdatenGost =
					this.reportingRepository.mapFaecher().values().stream().collect(Collectors.toMap(f -> f.ID, f -> f));
			final Map<Long, GostFach> mapFaecherdatenGost = mapfaecherFuerFaecherdatenGost.values().stream()
					.collect(Collectors.toMap(f -> f.ID, f -> DBUtilsFaecherGost.mapFromDTOFach(this.schuljahr, f, mapfaecherFuerFaecherdatenGost)));
			for (final FachDaten fach : faecherdaten)
				super.faecher.add(new ProxyReportingFach(fach, mapFaecherdatenGost.get(fach.id), this.schuljahr));
		}
		return super.faecher;
	}
}
