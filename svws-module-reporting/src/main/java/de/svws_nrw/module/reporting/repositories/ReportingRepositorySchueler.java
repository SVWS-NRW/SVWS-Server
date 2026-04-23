package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.adt.map.ListMap4DLongKeys;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerZuweisung;
import de.svws_nrw.module.reporting.types.schueler.telefon.ReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.utils.ReportingListBuilder;

/**
 * Domänen-Repository für Schülerdaten (Stammdaten, Lernabschnitte, Leistungsdaten und Reporting-Objekte).
 * Die Schülerdaten werden bei Bedarf aus der Datenbank nachgeladen und im Cache gehalten.
 */
public class ReportingRepositorySchueler {

	private final ReportingRepository reportingRepository;
	private final DBEntityManager conn;
	private final Logger logger;
	private final ReportingSortierungService sortierungService;

	private final Map<Long, SchuelerStammdaten> mapSchuelerStammdaten = new HashMap<>();
	private final Map<Long, List<ErzieherStammdaten>> mapErzieherStammdaten = new HashMap<>();
	private final Map<Long, List<Sprachbelegung>> mapSchuelerSprachbelegungen = new HashMap<>();
	private final Map<Long, SchuelerSchulbesuchsdaten> mapSchuelerSchulbesuchsdaten = new HashMap<>();
	private final ListMap3DLongKeys<SchuelerLeistungsdaten> mapAlleLeistungsdaten = new ListMap3DLongKeys<>();
	private final ListMap4DLongKeys<SchuelerLernabschnittsdaten> mapAlleLernabschnittsdaten = new ListMap4DLongKeys<>();

	private final Map<Long, ReportingSchueler> mapSchueler = new HashMap<>();
	private final Map<Long, List<ReportingSchuelerTelefonkontakt>> mapSchuelerTelefonkontakte = new HashMap<>();
	private final Map<Long, List<ReportingSchuelerZuweisung>> mapSchuelerZuweisungen = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingSchuelerRepository.
	 *
	 * @param reportingRepository Das zentrale Repository des Reporting-Moduls mit Zugriff auf die domänenspezifischen Repositories.
	 * @param conn                Die Datenbankverbindung.
	 * @param logger              Der Logger.
	 * @param sortierungService   Der Service für die Sortierung.
	 */
	public ReportingRepositorySchueler(final ReportingRepository reportingRepository, final DBEntityManager conn, final Logger logger,
			final ReportingSortierungService sortierungService) {
		this.reportingRepository = reportingRepository;
		this.conn = conn;
		this.logger = logger;
		this.sortierungService = sortierungService;
	}

	/**
	 * Gibt das ReportingSchueler-Objekt zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank nachgeladen.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Das ReportingSchueler-Objekt oder null, falls der Schüler nicht existiert.
	 */
	public ReportingSchueler schueler(final long idSchueler) {
		if (idSchueler < 0) {
			return null;
		}

		if (!mapSchuelerStammdaten.containsKey(idSchueler)) {
			try {
				final SchuelerStammdaten fehlendeSchulerstammdaten = new DataSchuelerStammdaten(this.conn).getById(idSchueler);
				mapSchuelerStammdaten.put(fehlendeSchulerstammdaten.id, fehlendeSchulerstammdaten);
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.logException(
						"FEHLER: Fehler bei der Ermittlung der fehlenden Schülerstammdaten eines Schülers aus der Datenbank im ReportingRepository.",
						e, this.logger, LogLevel.ERROR, 0);
				return null;
			}
		}

		if (mapSchuelerStammdaten.containsKey(idSchueler)) {
			return mapSchueler.computeIfAbsent(idSchueler, key -> new ProxyReportingSchueler(this.reportingRepository, mapSchuelerStammdaten.get(key)));
		} else {
			return null;
		}
	}

	/**
	 * Gibt eine sortierte Liste von ReportingSchueler-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsSchueler Liste der Schüler-IDs.
	 *
	 * @return Sortierte Liste von ReportingSchueler-Objekten.
	 */
	public List<ReportingSchueler> schueler(final List<Long> idsSchueler) {
		return schueler(idsSchueler, true);
	}

	/**
	 * Gibt eine Liste von ReportingSchueler-Objekten zu den übergebenen IDs zurück, optional sortiert.
	 *
	 * @param idsSchueler   Liste der Schüler-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 *
	 * @return Liste von ReportingSchueler-Objekten.
	 */
	public List<ReportingSchueler> schueler(final List<Long> idsSchueler, final boolean sortiereListe) {
		final Optional<Comparator<ReportingSchueler>> optionalComparator = sortiereListe
				? ComparatorFactory.buildOptionalComparator(this.sortierungService, this.logger, ReportingSchueler.class.getSimpleName(),
						SortierungRegistryReportingSchueler.sortierungRegistry())
				: Optional.empty();

		return ReportingListBuilder.erstelleReportingListe(idsSchueler, mapSchuelerStammdaten, mapSchueler,
				fehlendeIds -> {
					try {
						return new DataSchuelerStammdaten(this.conn).getListByIds(fehlendeIds);
					} catch (final ApiOperationException e) {
						ReportingExceptionUtils.logException(
								"FEHLER: Fehler bei der Ermittlung der fehlenden Schülerstammdaten einer Schülerliste aus der Datenbank im "
										+ "ReportingRepository.",
								e, this.logger, LogLevel.ERROR, 0);
						return new ArrayList<>();
					}
				},
				key -> new ProxyReportingSchueler(this.reportingRepository, mapSchuelerStammdaten.get(key)),
				stammdaten -> stammdaten.id,
				optionalComparator,
				"Schüler", this.logger);
	}

	/**
	 * Gibt die Map der Schülerstammdaten zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Schülerstammdaten.
	 */
	public Map<Long, SchuelerStammdaten> mapSchuelerStammdaten() {
		return mapSchuelerStammdaten;
	}

	/**
	 * Gibt die Map der Erzieherstammdaten zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Erzieherstammdaten.
	 */
	public Map<Long, List<ErzieherStammdaten>> mapErzieherStammdaten() {
		return mapErzieherStammdaten;
	}

	/**
	 * Gibt die Map der Sprachbelegungen der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Sprachbelegungen.
	 */
	public Map<Long, List<Sprachbelegung>> mapSchuelerSprachbelegungen() {
		return mapSchuelerSprachbelegungen;
	}

	/**
	 * Gibt die Map der Schulbesuchsdaten der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Schulbesuchsdaten.
	 */
	public Map<Long, SchuelerSchulbesuchsdaten> mapSchuelerSchulbesuchsdaten() {
		return mapSchuelerSchulbesuchsdaten;
	}

	/**
	 * Gibt die dreidimensionale Map aller Leistungsdaten der Schüler zurück.
	 *
	 * @return Map der Leistungsdaten.
	 */
	public ListMap3DLongKeys<SchuelerLeistungsdaten> mapAlleLeistungsdaten() {
		return mapAlleLeistungsdaten;
	}

	/**
	 * Gibt die vierdimensionale Map aller Lernabschnittsdaten der Schüler zurück.
	 *
	 * @return Map der Lernabschnittsdaten.
	 */
	public ListMap4DLongKeys<SchuelerLernabschnittsdaten> mapAlleLernabschnittsdaten() {
		return mapAlleLernabschnittsdaten;
	}

	/**
	 * Gibt die Map der bereits erzeugten ReportingSchueler-Objekte zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der ReportingSchueler-Objekte.
	 */
	public Map<Long, ReportingSchueler> mapSchueler() {
		return mapSchueler;
	}

	/**
	 * Gibt die Map der Telefonkontakte der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Telefonkontakte.
	 */
	public Map<Long, List<ReportingSchuelerTelefonkontakt>> mapSchuelerTelefonkontakte() {
		return mapSchuelerTelefonkontakte;
	}

	/**
	 * Gibt die Map der Zuweisungen der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Zuweisungen.
	 */
	public Map<Long, List<ReportingSchuelerZuweisung>> mapSchuelerZuweisungen() {
		return mapSchuelerZuweisungen;
	}
}
