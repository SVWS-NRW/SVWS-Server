package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.adt.map.ListMap4DLongKeys;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.data.schueler.SchuelerTelefon;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.erzieher.DataErzieherStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerLeistungsdaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerSprachbelegung;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerTelefon;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOSchuelerZuweisung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerZuweisung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerZuweisung;
import de.svws_nrw.module.reporting.types.schueler.telefon.ProxyReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.types.schueler.telefon.ReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.utils.ReportingListBuilder;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchServiceFactory;

/**
 * Domänen-Repository für Schülerdaten (Stammdaten, Lernabschnitte, Leistungsdaten und Reporting-Objekte).
 * Die Schülerdaten werden bei Bedarf aus der Datenbank nachgeladen und im Cache gehalten.
 */
public class ReportingRepositorySchueler {

	private final ReportingContext reportingContext;

	private final Map<Long, SchuelerStammdaten> mapSchuelerStammdaten = new HashMap<>();
	private final Map<Long, List<ErzieherStammdaten>> mapErzieherStammdaten = new HashMap<>();
	private final Map<Long, List<Sprachbelegung>> mapSchuelerSprachbelegungen = new HashMap<>();
	private final Map<Long, SchuelerSchulbesuchsdaten> mapSchuelerSchulbesuchsdaten = new HashMap<>();
	private final ListMap3DLongKeys<SchuelerLeistungsdaten> mapLeistungsdaten = new ListMap3DLongKeys<>();
	private final ListMap4DLongKeys<SchuelerLernabschnittsdaten> mapLernabschnittsdaten = new ListMap4DLongKeys<>();

	private final Map<Long, ReportingSchueler> mapSchueler = new HashMap<>();
	private final Map<Long, List<ReportingSchuelerTelefonkontakt>> mapSchuelerTelefonkontakte = new HashMap<>();
	private final Map<Long, List<ReportingSchuelerZuweisung>> mapSchuelerZuweisungen = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingSchuelerRepository.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 */
	public ReportingRepositorySchueler(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}


	// ##### Schüler (Reporting-Objekte und Stammdaten) #####

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
				final SchuelerStammdaten fehlendeSchulerstammdaten = new DataSchuelerStammdaten(this.reportingContext.conn()).getById(idSchueler);
				mapSchuelerStammdaten.put(fehlendeSchulerstammdaten.id, fehlendeSchulerstammdaten);
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.logException(
						"FEHLER: Fehler bei der Ermittlung der fehlenden Schülerstammdaten eines Schülers aus der Datenbank im ReportingContext.",
						e, this.reportingContext.logger(), LogLevel.ERROR, 0);
				return null;
			}
		}

		if (mapSchuelerStammdaten.containsKey(idSchueler)) {
			return mapSchueler.computeIfAbsent(idSchueler, key -> new ProxyReportingSchueler(this.reportingContext, mapSchuelerStammdaten.get(key)));
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
				? ComparatorFactory.buildOptionalComparator(this.reportingContext.sortierungService(), this.reportingContext.logger(),
						ReportingSchueler.class.getSimpleName(),
						SortierungRegistryReportingSchueler.sortierungRegistry())
				: Optional.empty();

		return ReportingListBuilder.erstelleReportingListe(idsSchueler, mapSchuelerStammdaten, mapSchueler,
				fehlendeIds -> {
					try {
						return new DataSchuelerStammdaten(this.reportingContext.conn()).getListByIds(fehlendeIds);
					} catch (final ApiOperationException e) {
						ReportingExceptionUtils.logException(
								"FEHLER: Fehler bei der Ermittlung der fehlenden Schülerstammdaten einer Schülerliste aus der Datenbank im "
										+ "ReportingContext.",
								e, this.reportingContext.logger(), LogLevel.ERROR, 0);
						return new ArrayList<>();
					}
				},
				key -> new ProxyReportingSchueler(this.reportingContext, mapSchuelerStammdaten.get(key)),
				stammdaten -> stammdaten.id,
				optionalComparator,
				"Schüler", this.reportingContext.logger());
	}

	/**
	 * Gibt die Map der bereits erzeugten ReportingSchueler-Objekte zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der ReportingSchueler-Objekte.
	 */
	public Map<Long, ReportingSchueler> schueler() {
		return mapSchueler;
	}

	/**
	 * Gibt die Map der Schülerstammdaten zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Schülerstammdaten.
	 */
	public Map<Long, SchuelerStammdaten> stammdaten() {
		return mapSchuelerStammdaten;
	}


	// ##### Erzieherstammdaten #####

	/**
	 * Gibt die Map der Erzieherstammdaten zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Erzieherstammdaten.
	 */
	public Map<Long, List<ErzieherStammdaten>> erzieherStammdaten() {
		return mapErzieherStammdaten;
	}

	/**
	 * Lädt die Erzieherstammdaten zu den übergebenen Schüler-IDs aus der Datenbank und gruppiert sie nach Schüler-ID.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Erzieherstammdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und Liste der zugehörigen Erzieherstammdaten als Wert.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public Map<Long, List<ErzieherStammdaten>> erzieherStammdaten(final List<Long> idsSchueler) throws ApiOperationException {
		return new DataErzieherStammdaten(this.reportingContext.conn()).getListBySchuelerIds(idsSchueler).stream()
				.collect(Collectors.groupingBy(e -> e.idSchueler));
	}


	// ##### Sprachbelegungen #####

	/**
	 * Gibt die Map der Sprachbelegungen der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Sprachbelegungen.
	 */
	public Map<Long, List<Sprachbelegung>> sprachbelegungen() {
		return mapSchuelerSprachbelegungen;
	}

	/**
	 * Lädt die Sprachbelegungen zu den übergebenen Schüler-IDs aus der Datenbank.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Sprachbelegungen geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und Liste der zugehörigen Sprachbelegungen als Wert.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public Map<Long, List<Sprachbelegung>> sprachbelegungen(final List<Long> idsSchueler) throws ApiOperationException {
		return DataSchuelerSprachbelegung.getMapBySchuelerIDs(this.reportingContext.conn(), idsSchueler);
	}


	// ##### Schulbesuchsdaten #####

	/**
	 * Gibt die Map der Schulbesuchsdaten der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Schulbesuchsdaten.
	 */
	public Map<Long, SchuelerSchulbesuchsdaten> schulbesuchsdaten() {
		return mapSchuelerSchulbesuchsdaten;
	}

	/**
	 * Lädt die Schulbesuchsdaten zu den übergebenen Schüler-IDs aus der Datenbank und liefert sie als Map indiziert nach Schüler-ID.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Schulbesuchsdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Schulbesuchsdaten als Wert.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public Map<Long, SchuelerSchulbesuchsdaten> schulbesuchsdaten(final List<Long> idsSchueler) throws ApiOperationException {
		return SchulbesuchServiceFactory
				.getNewInstance()
				.getSchulbesuchService()
				.getByIds(idsSchueler)
				.stream()
				.collect(Collectors.toMap(sb -> sb.id, sb -> sb));
	}

	// ##### Lernabschnitts- und Leistungsdaten #####

	/**
	 * Gibt die vierdimensionale Map aller Lernabschnittsdaten der Schüler zurück.
	 *
	 * @return Map der Lernabschnittsdaten.
	 */
	public ListMap4DLongKeys<SchuelerLernabschnittsdaten> lernabschnittsdaten() {
		return mapLernabschnittsdaten;
	}

	/**
	 * Lädt die Lernabschnittsdaten zu den übergebenen Schüler-IDs aus der Datenbank.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Lernabschnittsdaten geladen werden sollen.
	 *
	 * @return Liste der Lernabschnittsdaten der Schüler.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public List<SchuelerLernabschnittsdaten> lernabschnittsdaten(final List<Long> idsSchueler) throws ApiOperationException {
		return new DataSchuelerLernabschnittsdaten(this.reportingContext.conn()).getListFromSchuelerIDs(idsSchueler, false, false);
	}

	/**
	 * Gibt die dreidimensionale Map aller Leistungsdaten der Schüler zurück.
	 *
	 * @return Map der Leistungsdaten.
	 */
	public ListMap3DLongKeys<SchuelerLeistungsdaten> leistungsdaten() {
		return mapLeistungsdaten;
	}

	/**
	 * Lädt die Leistungsdaten zum übergebenen Lernabschnitt aus der Datenbank und trägt sie in die zentrale Map der Leistungsdaten ein.
	 *
	 * @param idSchueler      Die ID des Schülers, zu dem die Leistungsdaten gehören.
	 * @param idLernabschnitt Die ID des Lernabschnitts, dessen Leistungsdaten geladen werden sollen.
	 *
	 * @return true, falls die Leistungsdaten geladen und eingetragen wurden, sonst false.
	 */
	public boolean leistungsdatenZuLernabschnitt(final long idSchueler, final long idLernabschnitt) {
		final List<SchuelerLeistungsdaten> listLeistungsdaten = new ArrayList<>();
		try {
			if (new DataSchuelerLeistungsdaten(this.reportingContext.conn()).getByLernabschnitt(idLernabschnitt, listLeistungsdaten)) {
				listLeistungsdaten.forEach(l -> mapLeistungsdaten.add(idSchueler, idLernabschnitt, l.id, l));
				return true;
			}
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Fehler bei der Ermittlung der Leistungsdaten zum Lernabschnitt %d aus der Datenbank im ReportingContext."
							.formatted(idLernabschnitt),
					e, this.reportingContext.logger(), LogLevel.ERROR, 0);
		}
		return false;
	}


	// ##### Telefonkontakte #####

	/**
	 * Gibt die Map der Telefonkontakte der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Telefonkontakte.
	 */
	public Map<Long, List<ReportingSchuelerTelefonkontakt>> telefonkontakte() {
		return mapSchuelerTelefonkontakte;
	}

	/**
	 * Lädt die Telefonkontakte zu den übergebenen Schüler-IDs aus der Datenbank, erzeugt die zugehörigen Reporting-Objekte und
	 * gruppiert sie sortiert nach Schüler-ID.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Telefonkontakte geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und sortierter Liste der zugehörigen Telefonkontakte als Wert.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public Map<Long, List<ReportingSchuelerTelefonkontakt>> telefonkontakte(final List<Long> idsSchueler) throws ApiOperationException {
		final List<SchuelerTelefon> schuelerTelefone = new DataSchuelerTelefon(this.reportingContext.conn()).getListFromSchuelerIDs(idsSchueler);
		return schuelerTelefone.stream()
				.collect(Collectors.groupingBy(
						dto -> dto.idSchueler,
						Collectors.collectingAndThen(
								Collectors.mapping(
										t -> (ReportingSchuelerTelefonkontakt) new ProxyReportingSchuelerTelefonkontakt(this.reportingContext, t),
										Collectors.toList()),
								list -> list.stream()
										.sorted(Comparator.comparing(ReportingSchuelerTelefonkontakt::sortierung))
										.toList())));
	}


	// ##### Schüler-Zuweisungen #####

	/**
	 * Gibt die Map der Zuweisungen der Schüler zurück, indiziert nach Lernabschnitts-ID.
	 *
	 * @return Map der Zuweisungen.
	 */
	public Map<Long, List<ReportingSchuelerZuweisung>> zuweisungen() {
		return mapSchuelerZuweisungen;
	}

	/**
	 * Lädt die Schüler-Zuweisungen zum übergebenen Lernabschnitt aus der Datenbank und cachet sie. Bei erneutem Aufruf wird der
	 * Cache zurückgegeben.
	 *
	 * @param idLernabschnitt Die ID des Lernabschnitts, zu dem die Zuweisungen geladen werden sollen.
	 * @param lernabschnitt   Der Lernabschnitt, der zur Erstellung der Reporting-Zuweisungen benötigt wird.
	 *
	 * @return Liste der Reporting-Zuweisungen. Leere Liste, falls keine Daten ermittelt werden konnten.
	 */
	public List<ReportingSchuelerZuweisung> zuweisungen(final long idLernabschnitt, final ReportingSchuelerLernabschnitt lernabschnitt) {
		if (mapSchuelerZuweisungen.containsKey(idLernabschnitt)) {
			return mapSchuelerZuweisungen.get(idLernabschnitt);
		}
		final List<ReportingSchuelerZuweisung> reportingZuweisungen = new ArrayList<>();
		try {
			final List<DTOSchuelerZuweisung> dtos =
					this.reportingContext.conn().queryList(DTOSchuelerZuweisung.QUERY_BY_ABSCHNITT_ID, DTOSchuelerZuweisung.class, idLernabschnitt);
			if (dtos != null) {
				for (final DTOSchuelerZuweisung dto : dtos) {
					reportingZuweisungen.add(new ProxyReportingSchuelerZuweisung(this.reportingContext, dto, lernabschnitt));
				}
			}
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"INFO: Fehler bei der Ermittlung der Zuweisungen für Lernabschnitt %d aus der Datenbank. Gebe leere Liste zurück."
							.formatted(idLernabschnitt),
					e, this.reportingContext.logger(), LogLevel.INFO, 0);
		}
		mapSchuelerZuweisungen.put(idLernabschnitt, reportingZuweisungen);
		return reportingZuweisungen;
	}
}
