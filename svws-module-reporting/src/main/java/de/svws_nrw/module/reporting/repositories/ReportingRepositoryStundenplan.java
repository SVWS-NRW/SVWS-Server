package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.data.stundenplan.DataStundenplanPausenaufsichten;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterricht;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterrichtsverteilung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.stundenplanung.ProxyReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import jakarta.ws.rs.core.Response.Status;

/**
 * Domänen-Repository für Stundenpläne (Definitionen, Manager und Reporting-Objekte).
 * Die Stundenplandefinitionen werden bei der Initialisierung geladen; Manager und Reporting-Objekte werden bei Bedarf erzeugt und gecacht.
 */
public class ReportingRepositoryStundenplan {

	private final ReportingRepository reportingRepository;
	private final DBEntityManager conn;
	private final Logger logger;

	private List<StundenplanListeEintrag> stundenplandefinitionen;
	private final Map<Long, StundenplanManager> mapStundenplanManager = new HashMap<>();
	private final Map<Long, ReportingStundenplanungStundenplan> mapStundenplaene = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingStundenplanRepository und initialisiert die Stundenplandefinition.
	 *
	 * @param reportingRepository Das zentrale Repository des Reporting-Moduls mit Zugriff auf die domänenspezifischen Repositories.
	 * @param conn                Die Datenbankverbindung.
	 * @param logger              Der Logger.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public ReportingRepositoryStundenplan(final ReportingRepository reportingRepository, final DBEntityManager conn, final Logger logger)
			throws ApiOperationException {
		this.reportingRepository = reportingRepository;
		this.conn = conn;
		this.logger = logger;

		initStundenplanDefinitionen();
	}

	private void initStundenplanDefinitionen() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle alle Stundenplan-Definitionen der Schule.");
			this.stundenplandefinitionen = new ArrayList<>(DataStundenplanListe.getStundenplaeneAktiv(this.conn, null));
			if (!this.stundenplandefinitionen.isEmpty()) {
				this.stundenplandefinitionen.sort(Comparator.comparing((StundenplanListeEintrag sle) -> sle.gueltigAb).reversed());
			}
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "Die Daten der Stundenpläne konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Daten der Stundenpläne konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Gibt den Stundenplan zurück, der am übergebenen Datum gültig ist.
	 *
	 * @param datum Das Datum im Format yyyy-mm-dd.
	 *
	 * @return Der Stundenplan zum Datum oder null, falls keiner existiert.
	 */
	public ReportingStundenplanungStundenplan stundenplan(final String datum) {
		if ((datum == null) || (datum.length() != 10) || (stundenplandefinitionen == null) || (stundenplandefinitionen.isEmpty())) {
			return null;
		}

		final StundenplanListeEintrag stundenplandefinitionZuDatum = stundenplandefinitionen.stream()
				.filter(d -> ((d.gueltigAb != null) && (d.gueltigBis != null) && (datum.compareTo(d.gueltigAb) >= 0) && (datum.compareTo(d.gueltigBis) <= 0)))
				.findFirst().orElse(null);

		if (stundenplandefinitionZuDatum != null) {
			return stundenplan(stundenplandefinitionZuDatum.id);
		} else {
			return null;
		}
	}

	/**
	 * Gibt den Stundenplan zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank erzeugt.
	 *
	 * @param idStundenplan Die ID des Stundenplans.
	 *
	 * @return Der Stundenplan zur ID oder null, falls keiner existiert.
	 */
	public ReportingStundenplanungStundenplan stundenplan(final long idStundenplan) {
		if (stundenplandefinitionen.stream().noneMatch(d -> d.id == idStundenplan)) {
			return null;
		}

		if (mapStundenplaene.containsKey(idStundenplan)) {
			return mapStundenplaene.get(idStundenplan);
		}

		if (mapStundenplanManager.containsKey(idStundenplan)) {
			mapStundenplaene.computeIfAbsent(idStundenplan,
					key -> new ProxyReportingStundenplanungStundenplan(this.reportingRepository, mapStundenplanManager.get(key)));
			return mapStundenplaene.get(idStundenplan);
		}

		try {
			final StundenplanManager manager = stundenplanManager(idStundenplan);
			if (manager != null) {
				mapStundenplanManager.put(idStundenplan, manager);
				mapStundenplaene.put(idStundenplan, new ProxyReportingStundenplanungStundenplan(this.reportingRepository, manager));
				return mapStundenplaene.get(idStundenplan);
			}
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			// Stundenplan konnte nicht aus der Datenbank ermittelt werden.
		}
		return null;
	}

	/**
	 * Gibt den StundenplanManager zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank erzeugt.
	 *
	 * @param idStundenplan Die ID des Stundenplans.
	 *
	 * @return Der StundenplanManager zur ID oder null, falls der Stundenplan nicht existiert.
	 */
	public StundenplanManager stundenplanManager(final long idStundenplan) {
		mapStundenplanManager.computeIfAbsent(idStundenplan, key -> {
			try {
				final Stundenplan stundenplan = new DataStundenplan(conn).getById(key);
				if (stundenplan == null) {
					return null;
				}
				final List<StundenplanUnterricht> unterrichte = DataStundenplanUnterricht.getUnterrichte(this.conn, key);
				final List<StundenplanPausenaufsicht> aufsichten = DataStundenplanPausenaufsichten.getAufsichten(this.conn, key);
				final StundenplanUnterrichtsverteilung unterrichtsverteilung = DataStundenplanUnterrichtsverteilung.getUnterrichtsverteilung(this.conn, key);
				return new StundenplanManager(stundenplan, unterrichte, aufsichten, unterrichtsverteilung);
			} catch (final ApiOperationException e) {
				return null;
			}
		});

		return this.mapStundenplanManager.get(idStundenplan);
	}

	/**
	 * Gibt die Liste aller aktiven Stundenplandefinitionen zurück, absteigend nach Gültigkeitsbeginn sortiert.
	 *
	 * @return Liste aller Stundenplandefinitionen.
	 */
	public List<StundenplanListeEintrag> stundenplandefinitionen() {
		return stundenplandefinitionen;
	}

	/**
	 * Gibt die Map der bereits erzeugten StundenplanManager zurück, indiziert nach Stundenplan-ID.
	 *
	 * @return Map der StundenplanManager.
	 */
	public Map<Long, StundenplanManager> mapStundenplanManager() {
		return mapStundenplanManager;
	}

	/**
	 * Gibt die Map der bereits erzeugten Reporting-Stundenpläne zurück, indiziert nach Stundenplan-ID.
	 *
	 * @return Map der Reporting-Stundenpläne.
	 */
	public Map<Long, ReportingStundenplanungStundenplan> mapStundenplaene() {
		return mapStundenplaene;
	}
}
