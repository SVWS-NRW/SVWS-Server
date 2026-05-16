package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
	 * Die Methode delegiert an {@link #schueler(List, boolean)}, damit auch die Map der Schülerstammdaten konsistent gefüllt wird.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Das ReportingSchueler-Objekt oder null, falls der Schüler nicht existiert.
	 */
	public ReportingSchueler schueler(final long idSchueler) {
		final ReportingSchueler reportingSchueler = mapSchueler.get(idSchueler);
		if (reportingSchueler != null) {
			return reportingSchueler;
		}
		final List<ReportingSchueler> result = schueler(List.of(idSchueler), false);
		return result.isEmpty() ? null : result.getFirst();
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

		return ReportingRepositoryUtils.erstelleReportingListe(idsSchueler, mapSchuelerStammdaten, mapSchueler,
				fehlendeIds -> new DataSchuelerStammdaten(this.reportingContext.conn()).getListByIds(fehlendeIds),
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
	 * Liefert die Erzieherstammdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Liste der Erzieherstammdaten; leere Liste, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public List<ErzieherStammdaten> erzieherStammdaten(final long idSchueler) {
		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(
				mapSchuelerStammdaten.keySet(),
				mapErzieherStammdaten,
				this::ladeErzieherStammdaten,
				"Erzieherstammdaten",
				this.reportingContext.logger());
		return mapErzieherStammdaten.getOrDefault(idSchueler, List.of());
	}

	private Map<Long, List<ErzieherStammdaten>> ladeErzieherStammdaten(final List<Long> idsSchueler) {
		return new DataErzieherStammdaten(this.reportingContext.conn()).getListBySchuelerIds(idsSchueler).stream()
				.collect(Collectors.groupingBy(e -> e.idSchueler));
	}


	// ##### Sprachbelegungen #####

	/**
	 * Liefert die Sprachbelegungen zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Liste der Sprachbelegungen; leere Liste, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public List<Sprachbelegung> sprachbelegungen(final long idSchueler) {
		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(
				mapSchuelerStammdaten.keySet(),
				mapSchuelerSprachbelegungen,
				this::ladeSprachbelegungen,
				"Sprachbelegungen",
				this.reportingContext.logger());
		return mapSchuelerSprachbelegungen.getOrDefault(idSchueler, List.of());
	}

	private Map<Long, List<Sprachbelegung>> ladeSprachbelegungen(final List<Long> idsSchueler) {
		return DataSchuelerSprachbelegung.getMapBySchuelerIDs(this.reportingContext.conn(), idsSchueler);
	}


	// ##### Schulbesuchsdaten #####

	/**
	 * Liefert die Schulbesuchsdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die Schulbesuchsdaten oder {@code null}, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public SchuelerSchulbesuchsdaten schulbesuchsdaten(final long idSchueler) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				mapSchuelerStammdaten.keySet(),
				mapSchuelerSchulbesuchsdaten,
				this::ladeSchulbesuchsdaten,
				"Schulbesuchsdaten",
				this.reportingContext.logger());
		return mapSchuelerSchulbesuchsdaten.get(idSchueler);
	}

	private Map<Long, SchuelerSchulbesuchsdaten> ladeSchulbesuchsdaten(final List<Long> idsSchueler) {
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
	 * Liefert die Lernabschnittsdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt. Schüler-IDs, für die keine Lernabschnitte zurückgegeben wurden, werden
	 * negativ markiert, damit ein erneuter Bulk-Load vermieden wird.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Liste der Lernabschnittsdaten; leere Liste, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public List<SchuelerLernabschnittsdaten> lernabschnitte(final long idSchueler) {
		if (mapLernabschnittsdaten.containsKey1(idSchueler)) {
			return mapLernabschnittsdaten.get1(idSchueler);
		}

		final List<Long> fehlendeIds = mapSchuelerStammdaten.keySet().stream()
				.filter(id -> !mapLernabschnittsdaten.containsKey1(id))
				.toList();
		final List<Long> zuLadendeIds = fehlendeIds.contains(idSchueler) ? fehlendeIds : List.of(idSchueler);

		try {
			final List<SchuelerLernabschnittsdaten> geladen = ladeLernabschnitte(zuLadendeIds);
			for (final SchuelerLernabschnittsdaten la : geladen) {
				mapLernabschnittsdaten.add(la.schuelerID, la.schuljahresabschnitt, la.wechselNr, la.id, la);
			}

			final Set<Long> idsMitTreffer = geladen.stream().map(la -> la.schuelerID).collect(Collectors.toSet());
			for (final Long id : zuLadendeIds) {
				if (!idsMitTreffer.contains(id)) {
					mapLernabschnittsdaten.addEmpty(id, -1, -1, -1);
				}
			}
		} catch (final ApiOperationException e) {
			ReportingExceptionUtils.logException(
					"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Lernabschnitte des Schülers %d.".formatted(idSchueler),
					e, this.reportingContext.logger(), LogLevel.INFO, 0);
		}
		return mapLernabschnittsdaten.get1(idSchueler);
	}

	private List<SchuelerLernabschnittsdaten> ladeLernabschnitte(final List<Long> idsSchueler) throws ApiOperationException {
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
	 * @param idSchueler Die ID des Schülers, zu dem die Leistungsdaten gehören.
	 * @param idLernabschnitt Die ID des Lernabschnitts, dessen Leistungsdaten geladen werden sollen.
	 */
	public void leistungsdatenZuLernabschnitt(final long idSchueler, final long idLernabschnitt) {
		final List<SchuelerLeistungsdaten> listLeistungsdaten = new ArrayList<>();
		try {
			if (new DataSchuelerLeistungsdaten(this.reportingContext.conn()).getByLernabschnitt(idLernabschnitt, listLeistungsdaten)) {
				listLeistungsdaten.forEach(l -> mapLeistungsdaten.add(idSchueler, idLernabschnitt, l.id, l));
			}
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Fehler bei der Ermittlung der Leistungsdaten zum Lernabschnitt %d aus der Datenbank im ReportingContext."
							.formatted(idLernabschnitt),
					e, this.reportingContext.logger(), LogLevel.ERROR, 0);
		}
	}


	// ##### Telefonkontakte #####

	/**
	 * Liefert die Telefonkontakte zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Sortierte Liste der Telefonkontakte; leere Liste, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public List<ReportingSchuelerTelefonkontakt> telefonkontakte(final long idSchueler) {
		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(
				mapSchuelerStammdaten.keySet(),
				mapSchuelerTelefonkontakte,
				this::ladeTelefonkontakte,
				"Telefonkontakte",
				this.reportingContext.logger());
		return mapSchuelerTelefonkontakte.getOrDefault(idSchueler, List.of());
	}

	private Map<Long, List<ReportingSchuelerTelefonkontakt>> ladeTelefonkontakte(final List<Long> idsSchueler) {
		final List<SchuelerTelefon> schuelerTelefone = new DataSchuelerTelefon(this.reportingContext.conn()).getListFromSchuelerIDs(idsSchueler);
		return schuelerTelefone.stream()
				.collect(Collectors.groupingBy(
						dto -> dto.idSchueler,
						Collectors.collectingAndThen(
								Collectors.mapping(
										t -> (ReportingSchuelerTelefonkontakt) new ProxyReportingSchuelerTelefonkontakt(this.reportingContext, t),
										Collectors.toList()),
								list -> {
									list.sort(Comparator.comparing(ReportingSchuelerTelefonkontakt::sortierung));
									return list;
								})));
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
