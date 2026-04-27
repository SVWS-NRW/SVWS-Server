package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schule.DataEinwilligungsarten;
import de.svws_nrw.data.schule.DataLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingLehrer;
import de.svws_nrw.module.reporting.types.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.utils.ReportingListBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * Domänen-Repository für Lehrkräfte (Stammdaten und Reporting-Objekte).
 * Die Lehrerstammdaten werden bei der Initialisierung aus der Datenbank geladen und
 * können bei Bedarf für einzelne fehlende Lehrkräfte nachgeladen werden.
 */
public class ReportingRepositoryLehrer {

	private final ReportingRepository reportingRepository;

	private Map<Long, LehrerStammdaten> mapLehrerStammdaten;
	private final Map<Long, ReportingLehrer> mapLehrer = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingLehrerRepository und initialisiert die Lehrerstammdaten.
	 *
	 * @param reportingRepository Das zentrale Repository des Reporting-Moduls mit Zugriff auf die domänenspezifischen Repositories.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public ReportingRepositoryLehrer(final ReportingRepository reportingRepository) throws ApiOperationException {
		this.reportingRepository = reportingRepository;

		initLehrerStammdaten();
	}

	private void initLehrerStammdaten() throws ApiOperationException {
		try {
			this.reportingRepository.logger().logLn(LogLevel.DEBUG, 8, "Ermittle die Lehrerstammdaten.");
			this.mapLehrerStammdaten = new DataLehrerStammdaten(this.reportingRepository.conn(), new DataLernplattformen(this.reportingRepository.conn()), new DataEinwilligungsarten(this.reportingRepository.conn())).getAll().stream()
					.collect(Collectors.toMap(l -> l.id, l -> l));
		} catch (final Exception e) {
			this.mapLehrerStammdaten = new HashMap<>();
			this.reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die Lehrerstammdaten konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Lehrerstammdaten konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Gibt das ReportingLehrer-Objekt zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank nachgeladen.
	 *
	 * @param idLehrer Die ID des Lehrers.
	 *
	 * @return Das ReportingLehrer-Objekt oder null, falls die Lehrkraft nicht existiert.
	 */
	public ReportingLehrer lehrer(final long idLehrer) {
		if (idLehrer < 0) {
			return null;
		}

		if (!mapLehrerStammdaten.containsKey(idLehrer)) {
			try {
				final LehrerStammdaten fehlendeLehrerstammdaten = new DataLehrerStammdaten(this.reportingRepository.conn(), new DataLernplattformen(this.reportingRepository.conn()),
						new DataEinwilligungsarten(this.reportingRepository.conn())).getById(idLehrer);
				mapLehrerStammdaten.put(fehlendeLehrerstammdaten.id, fehlendeLehrerstammdaten);
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.logException(
						"FEHLER: Fehler bei der Ermittlung der fehlenden Lehrerstammdaten einer Lehrkraft aus der Datenbank im ReportingRepository.", e,
						this.reportingRepository.logger(), LogLevel.ERROR, 0);
				return null;
			}
		}

		if (mapLehrerStammdaten.containsKey(idLehrer)) {
			return mapLehrer.computeIfAbsent(idLehrer, key -> new ProxyReportingLehrer(this.reportingRepository, mapLehrerStammdaten.get(key)));
		} else {
			return null;
		}
	}

	/**
	 * Gibt eine sortierte Liste von ReportingLehrer-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsLehrer Liste der Lehrer-IDs.
	 *
	 * @return Sortierte Liste von ReportingLehrer-Objekten.
	 */
	public List<ReportingLehrer> lehrer(final List<Long> idsLehrer) {
		return lehrer(idsLehrer, true);
	}

	/**
	 * Gibt eine Liste von ReportingLehrer-Objekten zu den übergebenen IDs zurück, optional sortiert.
	 *
	 * @param idsLehrer     Liste der Lehrer-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 *
	 * @return Liste von ReportingLehrer-Objekten.
	 */
	public List<ReportingLehrer> lehrer(final List<Long> idsLehrer, final boolean sortiereListe) {
		final Optional<Comparator<ReportingLehrer>> optionalComparator = sortiereListe
				? ComparatorFactory.buildOptionalComparator(this.reportingRepository.sortierungService(), this.reportingRepository.logger(), ReportingLehrer.class.getSimpleName(),
						SortierungRegistryReportingLehrer.sortierungRegistry())
				: Optional.empty();

		return ReportingListBuilder.erstelleReportingListe(idsLehrer, mapLehrerStammdaten, mapLehrer,
				fehlendeIds -> {
					try {
						return new DataLehrerStammdaten(this.reportingRepository.conn(), new DataLernplattformen(this.reportingRepository.conn()), new DataEinwilligungsarten(this.reportingRepository.conn())).getListByIDs(fehlendeIds);
					} catch (final ApiOperationException e) {
						ReportingExceptionUtils.logException(
								"FEHLER: Fehler bei der Ermittlung der fehlenden Lehrerstammdaten einer Lehrerliste aus der Datenbank im "
										+ "ReportingRepository.",
								e, this.reportingRepository.logger(), LogLevel.ERROR, 0);
						return new ArrayList<>();
					}
				},
				key -> new ProxyReportingLehrer(this.reportingRepository, mapLehrerStammdaten.get(key)),
				stammdaten -> stammdaten.id,
				optionalComparator,
				"Lehrer", this.reportingRepository.logger());
	}

	/**
	 * Gibt die Map der Lehrerstammdaten zurück, indiziert nach der ID des Lehrers.
	 *
	 * @return Map der Lehrerstammdaten
	 */
	public Map<Long, LehrerStammdaten> stammdaten() {
		return mapLehrerStammdaten;
	}

	/**
	 * Gibt die Map der bereits erzeugten ReportingLehrer-Objekte zurück, indiziert nach Lehrer-ID.
	 *
	 * @return Map der ReportingLehrer-Objekte
	 */
	public Map<Long, ReportingLehrer> mapLehrer() {
		return mapLehrer;
	}
}
