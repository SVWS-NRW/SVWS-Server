package de.svws_nrw.module.reporting.repositories;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * Domänen-Repository für Schuldaten und Schuljahresabschnitte.
 * Die Schulstammdaten und alle Schuljahresabschnitte werden bei der Initialisierung aus der Datenbank geladen.
 */
public class ReportingRepositorySchule {

	private final ReportingRepository reportingRepository;
	private final SchuleStammdaten schulstammdaten;
	private final Long idAktuellerSchuljahresabschnitt;
	private final Long idAuswahlSchuljahresabschnitt;
	private final Map<Long, ReportingSchuljahresabschnitt> mapSchuljahresabschnitte = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingSchuleRepository und initialisiert Schulstammdaten und Schuljahresabschnitte.
	 *
	 * @param reportingRepository           Das zentrale Repository des Reporting-Moduls mit Zugriff auf die domänenspezifischen Repositories.
	 * @param idAuswahlSchuljahresabschnitt Die ID des ausgewählten Schuljahresabschnitts.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public ReportingRepositorySchule(final ReportingRepository reportingRepository, final long idAuswahlSchuljahresabschnitt) throws ApiOperationException {
		this.reportingRepository = reportingRepository;
		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 8, "Ermittle Stammdaten und Abschnitte der Schule.");
		try {
			this.schulstammdaten = DataSchuleStammdaten.getStammdaten(this.reportingRepository.conn());

			final List<Schuljahresabschnitt> datenSchuljahresabschnitte = this.reportingRepository.conn().getUser().schuleGetStammdaten().abschnitte;
			for (final Schuljahresabschnitt datenSchuljahresabschnitt : datenSchuljahresabschnitte) {
				mapSchuljahresabschnitte.putIfAbsent(datenSchuljahresabschnitt.id,
						new ProxyReportingSchuljahresabschnitt(this.reportingRepository, datenSchuljahresabschnitt));
			}

			this.idAktuellerSchuljahresabschnitt = this.schulstammdaten.idSchuljahresabschnitt;
			this.idAuswahlSchuljahresabschnitt = idAuswahlSchuljahresabschnitt;
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Die Stamm- oder Abschnittsdaten der Schule konnten nicht ermittelt werden oder der Schuljahresabschnitt ist ungültig.",
					e, this.reportingRepository.logger(), LogLevel.ERROR, 8);
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Die Stamm- oder Abschnittsdaten der Schule konnten nicht ermittelt werden oder der übergebene Schuljahresabschnitt ist ungültig.");
		}
	}

	/**
	 * Gibt die Stammdaten der Schule zurück.
	 *
	 * @return Die Stammdaten der Schule.
	 */
	public SchuleStammdaten stammdaten() {
		return schulstammdaten;
	}

	/**
	 * Gibt alle Schuljahresabschnitte der Schule als sortierte Liste zurück (nach Schuljahr und Abschnitt).
	 *
	 * @return Alle Schuljahresabschnitte der Schule.
	 */
	public List<ReportingSchuljahresabschnitt> schuljahresabschnitte() {
		return mapSchuljahresabschnitte.values().stream().sorted(
				Comparator.comparing(ReportingSchuljahresabschnitt::schuljahr)
						.thenComparing(ReportingSchuljahresabschnitt::abschnitt))
				.toList();
	}

	/**
	 * Gibt den Schuljahresabschnitt zur übergebenen ID zurück.
	 *
	 * @param id Die ID des angeforderten Schuljahresabschnitts.
	 *
	 * @return Der Schuljahresabschnitt zur ID.
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt(final long id) {
		return mapSchuljahresabschnitte.get(id);
	}

	/**
	 * Gibt den Schuljahresabschnitt zum angegebenen Schuljahr und Abschnitt zurück.
	 *
	 * @param schuljahr Das Schuljahr.
	 * @param abschnitt Der Abschnitt.
	 *
	 * @return Der Schuljahresabschnitt zu den Angaben oder null, falls keiner existiert.
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt(final int schuljahr, final int abschnitt) {
		final List<ReportingSchuljahresabschnitt> result =
				mapSchuljahresabschnitte.values().stream().filter(a -> (a.schuljahr() == schuljahr) && (a.abschnitt() == abschnitt)).toList();
		if (result.isEmpty()) {
			return null;
		}
		return result.getFirst();
	}

	/**
	 * Gibt den aktuellen Schuljahresabschnitt der Schule zurück.
	 *
	 * @return Der aktuelle Schuljahresabschnitt der Schule.
	 */
	public ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt() {
		return this.mapSchuljahresabschnitte.get(idAktuellerSchuljahresabschnitt);
	}

	/**
	 * Gibt den für den Druck ausgewählten Schuljahresabschnitt zurück.
	 *
	 * @return Der Schuljahresabschnitt der Auswahl für den Druck.
	 */
	public ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt() {
		return this.mapSchuljahresabschnitte.get(idAuswahlSchuljahresabschnitt);
	}

	/**
	 * Gibt die Map der Schuljahresabschnitte zurück, indiziert nach ID.
	 *
	 * @return Map der Schuljahresabschnitte.
	 */
	public Map<Long, ReportingSchuljahresabschnitt> mapSchuljahresabschnitte() {
		return mapSchuljahresabschnitte;
	}
}
