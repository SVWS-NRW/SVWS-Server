package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
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
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.repo.schueler.SchuelerAnkreuzkompetenzenRepositoryImpl;
import de.svws_nrw.module.reporting.signing.SchulbescheinigungQrDaten;
import de.svws_nrw.module.reporting.signing.SchulbescheinigungQrFactory;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.types.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerAnkreuzkompetenz;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerZuweisung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerAnkreuzkompetenz;
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
	private final Map<Long, List<DTOSchuelerAnkreuzfloskeln>> mapSchuelerAnkreuzkompetenzen = new HashMap<>();
	private final Map<Long, SchulbescheinigungQrDaten> mapSchulbescheinigungQrDaten = new HashMap<>();
	private final Set<Long> idsLernabschnitteZuLadenLeistungsdaten = new HashSet<>();
	private final Set<Long> idsLernabschnitteZuLadenAnkreuzkompetenzen = new HashSet<>();

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
		final List<ReportingSchueler> result = schueler(List.of(idSchueler), false);
		return result.isEmpty() ? null : result.getFirst();
	}

	/**
	 * Gibt das ReportingSchueler-Objekt zur übergebenen ID für die modulinterne Auflösung von Rückverweisen zurück. Fehlt der Eintrag im Cache,
	 * wird er aus der Datenbank nachgeladen.
	 * Anders als {@link #schueler(long)} wird der Benutzerfilter <b>nicht</b> angewendet: Der Rückverweis eines Objektes auf den Schüler, zu dem
	 * es fachlich gehört, darf nicht davon abhängen, ob dieser Schüler in der Ausgabe erscheint. Für Daten, die an die Vorlagen gehen, ist
	 * weiterhin {@link #schueler(long)} zu verwenden.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Das ReportingSchueler-Objekt oder null, falls der Schüler nicht existiert oder nicht geladen werden konnte.
	 */
	public ReportingSchueler schuelerOhneFilter(final long idSchueler) {
		final List<ReportingSchueler> result = schueler(List.of(idSchueler), false, false);
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
		return schueler(idsSchueler, sortiereListe, true);
	}

	/**
	 * Gibt eine Liste von ReportingSchueler-Objekten zu den übergebenen IDs zurück, optional sortiert und optional gefiltert.
	 *
	 * @param idsSchueler   Liste der Schüler-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 * @param mitFilter     Gibt an, ob der Benutzerfilter auf die Rückgabe angewendet werden soll. Die Caches werden unabhängig davon vollständig gefüllt.
	 *
	 * @return Liste von ReportingSchueler-Objekten.
	 */
	private List<ReportingSchueler> schueler(final List<Long> idsSchueler, final boolean sortiereListe, final boolean mitFilter) {
		final Comparator<ReportingSchueler> comparator = ComparatorFactory.buildComparator(this.reportingContext.sortierungService(),
				this.reportingContext.logger(), ReportingSchueler.class.getSimpleName(),
				ReportingSchueler.SORTIERUNG, sortiereListe);
		final Predicate<ReportingSchueler> filter = mitFilter ? ReportingSchueler.FILTER.bedingung(
				this.reportingContext.filterService().getFilter(ReportingSchueler.class.getSimpleName()), null) : null;

		return ReportingRepositoryUtils.erstelleReportingListe(idsSchueler, mapSchuelerStammdaten, mapSchueler,
				fehlendeIds -> new DataSchuelerStammdaten(this.reportingContext.conn()).getListByIds(fehlendeIds),
				key -> new ProxyReportingSchueler(this.reportingContext, mapSchuelerStammdaten.get(key)),
				stammdaten -> stammdaten.id,
				comparator, filter,
				"Schüler", this.reportingContext.logger());
	}

	/**
	 * Gibt die IDs aller Schüler zurück, deren Stammdaten bereits im Cache dieses Repositories liegen. Die Methode lädt selbst nichts
	 * nach und wendet keinen Filter an; sie dient dazu, ohnehin benötigte Daten für den bereits bekannten Bestand gebündelt
	 * nachladen zu können, statt je Schüler eine eigene Abfrage abzusetzen.
	 *
	 * @return Unveränderliche Liste der IDs. Aufrufer, die die Liste erweitern wollen, legen eine eigene Kopie an.
	 */
	public List<Long> idsGeladenerSchueler() {
		return List.copyOf(mapSchuelerStammdaten.keySet());
	}

	/**
	 * Übernimmt die Stammdaten eines Schülers in den Cache dieses Repositories und ersetzt dabei einen bereits vorhandenen Eintrag.
	 * Die Methode ist für die Selbstregistrierung des {@link ProxyReportingSchueler} bei dessen Konstruktion vorgesehen; sie ersetzt
	 * den früheren direkten Schreibzugriff auf die Cache-Map.
	 *
	 * @param idSchueler Die ID des Schülers.
	 * @param stammdaten Die Stammdaten des Schülers.
	 */
	public void registriereStammdaten(final long idSchueler, final SchuelerStammdaten stammdaten) {
		mapSchuelerStammdaten.put(idSchueler, stammdaten);
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


	// ##### Signierte Schulbescheinigung (QR-Codes) #####

	/**
	 * Liefert die gerenderten QR-Codes der signierten Schulbescheinigung zum übergebenen Schüler. Beim ersten Zugriff
	 * werden für alle bekannten Schüler die XML-Dokumente erzeugt, in einem einzigen Batch signiert und die beiden
	 * QR-Codes (Inhalt und Signatur) als SVG gerendert; das Ergebnis wird im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die QR-Daten der Schulbescheinigung; im Fehlerfall ein Eintrag mit gesetzter Fehlermeldung (niemals {@code null}).
	 */
	public SchulbescheinigungQrDaten schulbescheinigungQrDaten(final long idSchueler) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				mapSchuelerStammdaten.keySet(),
				mapSchulbescheinigungQrDaten,
				idsSchueler -> new SchulbescheinigungQrFactory(this.reportingContext).erzeuge(idsSchueler),
				"Schulbescheinigung-QR-Daten",
				this.reportingContext.logger());
		return mapSchulbescheinigungQrDaten.get(idSchueler);
	}

	// ##### Lernabschnitts- und Leistungsdaten, Ankreuzkompetenzen #####

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

		final List<Long> idsSchuelerFehlend = mapSchuelerStammdaten.keySet().stream()
				.filter(id -> !mapLernabschnittsdaten.containsKey1(id))
				.toList();
		final List<Long> idsSchuelerZuLaden = idsSchuelerFehlend.contains(idSchueler) ? idsSchuelerFehlend : List.of(idSchueler);

		try {
			final List<SchuelerLernabschnittsdaten> schuelerLernabschnittsdaten = ladeLernabschnitte(idsSchuelerZuLaden);
			for (final SchuelerLernabschnittsdaten la : schuelerLernabschnittsdaten) {
				mapLernabschnittsdaten.add(la.schuelerID, la.schuljahresabschnitt, la.wechselNr, la.id, la);
				// Speichere die IDs der geladenen Lernabschnitte für das Nachladen der Leistungsdaten und Ankreuzkompetenzen.
				idsLernabschnitteZuLadenLeistungsdaten.add(la.id);
				idsLernabschnitteZuLadenAnkreuzkompetenzen.add(la.id);
			}

			final Set<Long> idsSchuelerMitLernabschnittsdaten = schuelerLernabschnittsdaten.stream().map(la -> la.schuelerID).collect(Collectors.toSet());
			// Schüler ohne Lernabschnitte werden auch in der Lernabschnitt-Map hinterlegt, damit die Abfragen für diese Schüler nicht immer wieder neu gestartet werden müssen.
			for (final Long id : idsSchuelerZuLaden) {
				if (!idsSchuelerMitLernabschnittsdaten.contains(id)) {
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
	 * Liefert die Leistungsdaten für den übergebenen Lernabschnitt eines Schülers als Liste von Reporting-Objekten.
	 * Beim ersten Zugriff auf einen Schuljahresabschnitt werden in einer einzigen Abfrage alle Leistungsdaten für sämtliche
	 * bereits bekannten Lernabschnitte dieses Schuljahresabschnitts nachgeladen und im Cache abgelegt.
	 *
	 * @param reportingSchuelerLernabschnitt Der Lernabschnitt, dessen Leistungsdaten geliefert werden sollen.
	 *
	 * @return Liste der Leistungsdaten des Lernabschnitts. Leere Liste, falls keine vorhanden sind.
	 */
	public List<ReportingSchuelerLeistungsdaten> leistungsdatenZuLernabschnitt(final ReportingSchuelerLernabschnitt reportingSchuelerLernabschnitt) {
		leistungsdatenZuSchuljahresabschnitt(reportingSchuelerLernabschnitt.idSchuljahresabschnitt());
		return mapLeistungsdaten.get12(reportingSchuelerLernabschnitt.idSchueler(), reportingSchuelerLernabschnitt.id()).stream()
				.map(sld -> (ReportingSchuelerLeistungsdaten) new ProxyReportingSchuelerLeistungsdaten(this.reportingContext, reportingSchuelerLernabschnitt,
						sld))
				.toList();
	}

	/**
	 * Stellt sicher, dass die Leistungsdaten der Schüler für alle bekannten Lernabschnitte des angegebenen Schuljahresabschnitts geladen sind.
	 * Beim ersten Aufruf für einen Schuljahresabschnitt werden in einer einzigen Abfrage alle Leistungsdaten für sämtliche im Cache vorhandenen
	 * Lernabschnitte dieses Schuljahresabschnitts nachgeladen und in der 3D-Map nach Schüler-, Lernabschnitts- und Leistungsdaten-ID im Cache abgelegt.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts, für den die Leistungsdaten geladen werden sollen.
	 */
	private void leistungsdatenZuSchuljahresabschnitt(final long idSchuljahresabschnitt) {
		final List<Long> idsLernabschnitte = getLernabschnitteZuLaden(idsLernabschnitteZuLadenLeistungsdaten, idSchuljahresabschnitt);
		if (idsLernabschnitte.isEmpty()) {
			return;
		}
		try {
			final DataSchuelerLeistungsdaten dataSchuelerLeistungsdaten = new DataSchuelerLeistungsdaten(this.reportingContext.conn());
			final List<SchuelerLeistungsdaten> schuelerLeistungsdaten = dataSchuelerLeistungsdaten.getByLernabschnitten(idsLernabschnitte);
			for (final SchuelerLeistungsdaten sld : schuelerLeistungsdaten) {
				final SchuelerLernabschnittsdaten lernabschnitt = mapLernabschnittsdaten.getSingle4OrNull(sld.lernabschnittID);
				if ((lernabschnitt != null) && !mapLeistungsdaten.containsKey123(lernabschnitt.schuelerID, lernabschnitt.id, sld.id)) {
					mapLeistungsdaten.add(lernabschnitt.schuelerID, lernabschnitt.id, sld.id, sld);
				}
			}
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Fehler bei der Ermittlung der Schüler-Leistungsdaten zum Schuljahresabschnitt %d aus der Datenbank im ReportingContext."
							.formatted(idSchuljahresabschnitt),
					e, this.reportingContext.logger(), LogLevel.ERROR, 0);
		}
	}

	/**
	 * Liefert die Belegungen der Ankreuzkompetenzen für den übergebenen Lernabschnitt eines Schülers als Liste von Reporting-Objekten.
	 * Beim ersten Zugriff auf einen Schuljahresabschnitt werden in einer einzigen Abfrage alle Belegungen für sämtliche
	 * bereits bekannten Lernabschnitte dieses Schuljahresabschnitts nachgeladen und im Cache abgelegt.
	 *
	 * @param lernabschnitt Der Lernabschnitt, dessen Belegungen der Ankreuzkompetenzen geliefert werden sollen.
	 *
	 * @return Liste der Belegungen der Ankreuzkompetenzen des Lernabschnitts. Leere Liste, falls keine vorhanden sind.
	 */
	public List<ReportingSchuelerAnkreuzkompetenz> schuelerLernabschnittAnkreuzkompetenzen(final ReportingSchuelerLernabschnitt lernabschnitt) {
		schuelerAnkreuzkompetenzenZuSchuljahresabschnitt(lernabschnitt.idSchuljahresabschnitt());
		return mapSchuelerAnkreuzkompetenzen.getOrDefault(lernabschnitt.id(), new ArrayList<>()).stream()
				.map(dto -> (ReportingSchuelerAnkreuzkompetenz) new ProxyReportingSchuelerAnkreuzkompetenz(dto, lernabschnitt))
				.sorted(Comparator.comparingInt(sak -> sak.ankreuzkompetenz().sortierung()))
				.toList();
	}

	/**
	 * Stellt sicher, dass die Ankreuzkompetenzen der Schüler für alle bekannten Lernabschnitte des angegebenen Schuljahresabschnitts geladen sind.
	 * Beim ersten Aufruf für einen Schuljahresabschnitt werden in einer einzigen Abfrage alle Belegungen für sämtliche im Cache vorhandenen
	 * Lernabschnitte dieses Schuljahresabschnitts nachgeladen und nach Lernabschnitts-ID indiziert im Cache abgelegt.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts, für den die Belegungen geladen werden sollen.
	 */
	private void schuelerAnkreuzkompetenzenZuSchuljahresabschnitt(final long idSchuljahresabschnitt) {
		final List<Long> idsLernabschnitte = getLernabschnitteZuLaden(idsLernabschnitteZuLadenAnkreuzkompetenzen, idSchuljahresabschnitt);
		if (idsLernabschnitte.isEmpty()) {
			return;
		}
		try {
			final List<DTOSchuelerAnkreuzfloskeln> dtos =
					new SchuelerAnkreuzkompetenzenRepositoryImpl(this.reportingContext.conn()).findListByLernabschnitt(idsLernabschnitte);
			for (final DTOSchuelerAnkreuzfloskeln dto : dtos) {
				mapSchuelerAnkreuzkompetenzen.computeIfAbsent(dto.Abschnitt_ID, k -> new ArrayList<>()).add(dto);
			}
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Fehler bei der Ermittlung der Schüler-Ankreuzkompetenzen zum Schuljahresabschnitt %d aus der Datenbank im ReportingContext."
							.formatted(idSchuljahresabschnitt),
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


	// ##### Hilfsmethoden #####

	/**
	 * Ermittelt die Lernabschnitt-IDs, für die noch Daten (wie Leistungsdaten oder Ankreuzkompetenzen) nachgeladen werden müssen.
	 * Dabei werden nur diejenigen Lernabschnitte berücksichtigt, die zum übergebenen Schuljahresabschnitt gehören.
	 * Die zurückgegebenen IDs werden aus dem übergebenen Set entfernt, sodass sie bei zukünftigen Aufrufen nicht erneut geladen werden.
	 *
	 * @param idsLernabschnitteZuLaden Das Set, welches die IDs der Lernabschnitte enthält, für die noch Daten fehlen.
	 * @param idSchuljahresabschnitt   Die ID des Schuljahresabschnitts, für den Daten nachgeladen werden sollen.
	 *
	 * @return Eine Liste mit Lernabschnitt-IDs, für die im angegebenen Schuljahresabschnitt Daten nachgeladen werden müssen.
	 */
	private List<Long> getLernabschnitteZuLaden(final Set<Long> idsLernabschnitteZuLaden, final long idSchuljahresabschnitt) {
		// Alle Lernabschnitte des Schuljahresabschnitts aus dem Cache holen
		final List<Long> idsLernabschnitteImCache = mapLernabschnittsdaten.get2(idSchuljahresabschnitt).stream()
				.map(la -> la.id)
				.distinct()
				.toList();

		// Herausfiltern, welche dieser Lernabschnitte noch im Set der zu ladenden stehen
		final List<Long> idsLernabschnitteZuLadenImCache = idsLernabschnitteImCache.stream()
				.filter(idsLernabschnitteZuLaden::contains)
				.toList();

		// Die IDs, die wir jetzt laden, aus dem Set entfernen (konsumieren)
		idsLernabschnitteZuLadenImCache.forEach(idsLernabschnitteZuLaden::remove);

		return idsLernabschnitteZuLadenImCache;
	}
}
