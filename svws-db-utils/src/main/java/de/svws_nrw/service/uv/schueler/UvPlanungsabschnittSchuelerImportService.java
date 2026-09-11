package de.svws_nrw.service.uv.schueler;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schueler.Versetzungsvermerk;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle;
import de.svws_nrw.core.data.uv.UvSchuelerImportOptions;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import de.svws_nrw.service.uv.klassen.UvKlasseCreateRequest;
import de.svws_nrw.service.uv.klassen.UvKlasseService;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Import von Schülern in einen UV-Planungsabschnitt.
 */
public final class UvPlanungsabschnittSchuelerImportService {

	private final SchuljahresabschnittService schuljahresabschnittService;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittRepository lernabschnittRepository;
	private final KlassenRepository klassenRepository;
	private final JahrgangRepository jahrgaengeRepository;
	private final UvPlanungsabschnittService uvPlanungsabschnittService;
	private final UvPlanungsabschnittSchuelerService uvPlanungsabschnittSchuelerService;
	private final UvKlasseService uvKlasseService;
	private final UvSchuelergruppeService uvSchuelergruppeService;
	private final UvSchuelergruppeSchuelerService uvSchuelergruppeSchuelerService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuljahresabschnittService        der Service für Schuljahresabschnitte
	 * @param schuelerRepository                 das Repository für Schüler
	 * @param lernabschnittRepository            das Repository für Lernabschnitte
	 * @param klassenRepository                  das Repository für Schild-Klassen
	 * @param jahrgaengeRepository               das Repository für Jahrgänge
	 * @param uvPlanungsabschnittService         der Service für Planungsabschnitte
	 * @param uvPlanungsabschnittSchuelerService der Service für UV-Planungsabschnitt-Schüler
	 * @param uvKlasseService                    der Service für UV-Klassen
	 * @param uvSchuelergruppeService            der Service für UV-Schülergruppen
	 * @param uvSchuelergruppeSchuelerService    der Service für UV-Schülergruppen-Schüler
	 */
	public UvPlanungsabschnittSchuelerImportService(
			final SchuljahresabschnittService schuljahresabschnittService,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository lernabschnittRepository,
			final KlassenRepository klassenRepository,
			final JahrgangRepository jahrgaengeRepository,
			final UvPlanungsabschnittService uvPlanungsabschnittService,
			final UvPlanungsabschnittSchuelerService uvPlanungsabschnittSchuelerService,
			final UvKlasseService uvKlasseService,
			final UvSchuelergruppeService uvSchuelergruppeService,
			final UvSchuelergruppeSchuelerService uvSchuelergruppeSchuelerService) {
		this.schuljahresabschnittService = schuljahresabschnittService;
		this.schuelerRepository = schuelerRepository;
		this.lernabschnittRepository = lernabschnittRepository;
		this.klassenRepository = klassenRepository;
		this.jahrgaengeRepository = jahrgaengeRepository;
		this.uvPlanungsabschnittService = uvPlanungsabschnittService;
		this.uvPlanungsabschnittSchuelerService = uvPlanungsabschnittSchuelerService;
		this.uvKlasseService = uvKlasseService;
		this.uvSchuelergruppeService = uvSchuelergruppeService;
		this.uvSchuelergruppeSchuelerService = uvSchuelergruppeSchuelerService;
	}

	private record ImportKontext(
			Schuljahresabschnitt quellAbschnitt,
			Map<Long, DTOKlassen> quellklassenById,
			Map<Long, DTOSchuelerLernabschnittsdaten> lernabschnitteBySchueler,
			Set<Long> aktiveSchuelerIds,
			Set<Long> bereitsZugeordneteSchuelerIds,
			Map<Long, Long> folgejahrgaengeById,
			Map<Long, DTOKlassen> zielklassenById) {
	}

	private record KlassenZuordnungen(
			Map<Long, Long> quellklasseZuUvKlasse,
			Map<Long, Long> quellklasseZuSchuelergruppe,
			Map<Long, Long> individuelleFolgeklasseZuUvKlasse,
			Map<Long, Long> individuelleFolgeklasseZuSchuelergruppe,
			List<UvKlasse> neueKlassen,
			List<UvSchuelergruppe> neueSchuelergruppen) {
	}

	/**
	 * Ergebnis für das Anlegen fehlender Planungsabschnitt-Schüler.
	 *
	 * @param neueSchueler    die neu angelegten Schüler-Zuordnungen
	 * @param alleSchuelerIds alle Schüler-IDs, die dem Planungsabschnitt zugeordnet sind
	 */
	public record SchuelerImportResult(List<UvPlanungsabschnittSchueler> neueSchueler, Set<Long> alleSchuelerIds) {
	}

	/**
	 * Importiert Schüler anhand der übergebenen Importoptionen in den Ziel-Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Ziel-Planungsabschnitts
	 * @param options               die Importoptionen
	 *
	 * @return das importierte Datenbundle
	 */
	public UvPlanungsabschnittsdatenBundle importSchueler(final long idPlanungsabschnitt, final UvSchuelerImportOptions options) {
		return transactional(() -> doImportSchueler(idPlanungsabschnitt, options));
	}

	/**
	 * Legt für die übergebenen Schüler-IDs fehlende Planungsabschnitt-Schüler an.
	 *
	 * @param idPlanungsabschnitt    die ID des Planungsabschnitts
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param schuelerIds            die Schüler-IDs, die vorhanden sein sollen
	 * @param fallbackJahrgangId     der Fallback-Jahrgang
	 *
	 * @return das Import-Ergebnis
	 */
	public SchuelerImportResult createMissingPlanungsabschnittSchueler(final long idPlanungsabschnitt,
			final long idSchuljahresabschnitt, final Set<Long> schuelerIds, final Long fallbackJahrgangId) {
		return transactional(() -> doCreateMissingPlanungsabschnittSchueler(idPlanungsabschnitt, idSchuljahresabschnitt, schuelerIds, fallbackJahrgangId));
	}

	/* Führt den eigentlichen Schülerimport aus; wird von importSchueler innerhalb der Transaktion aufgerufen. */
	private UvPlanungsabschnittsdatenBundle doImportSchueler(final long idPlanungsabschnitt, final UvSchuelerImportOptions optionen) {
		final ImportKontext kontext = ladeImportKontext(idPlanungsabschnitt, optionen);
		final UvPlanungsabschnittsdatenBundle result = new UvPlanungsabschnittsdatenBundle();
		result.planungsabschnitt = uvPlanungsabschnittService.get(idPlanungsabschnitt);
		if (!optionen.klassenzuweisungenUebernehmen) {
			result.planungsabschnittschueler = importierePlanungsabschnittSchueler(idPlanungsabschnitt, kontext, optionen, Map.of(), Map.of(), false);
			return result;
		}

		final KlassenZuordnungen klassenZuordnungen = ermittleKlassenZuordnungen(idPlanungsabschnitt, kontext, optionen);
		result.klassen.addAll(klassenZuordnungen.neueKlassen());
		result.schuelergruppen.addAll(klassenZuordnungen.neueSchuelergruppen());
		result.planungsabschnittschueler =
				importierePlanungsabschnittSchueler(idPlanungsabschnitt, kontext, optionen, klassenZuordnungen.quellklasseZuUvKlasse(),
						klassenZuordnungen.individuelleFolgeklasseZuUvKlasse(), true);
		result.schuelergruppenschueler =
				importiereSchuelergruppen(idPlanungsabschnitt, kontext, optionen, klassenZuordnungen.quellklasseZuSchuelergruppe(),
						klassenZuordnungen.individuelleFolgeklasseZuSchuelergruppe(), result.planungsabschnittschueler);
		return result;
	}

	/* Legt fehlende Planungsabschnitt-Schüler an; wird von createMissingPlanungsabschnittSchueler innerhalb der Transaktion aufgerufen. */
	private SchuelerImportResult doCreateMissingPlanungsabschnittSchueler(final long idPlanungsabschnitt,
			final long idSchuljahresabschnitt, final Set<Long> schuelerIds, final Long fallbackJahrgangId) {
		final Set<Long> bereitsZugeordneteSchuelerIds = getBereitsZugeordneteSchuelerIds(idPlanungsabschnitt);
		final Set<Long> fehlendeSchuelerIds = getFehlendeSchuelerIds(schuelerIds, bereitsZugeordneteSchuelerIds);
		if (fehlendeSchuelerIds.isEmpty()) {
			return new SchuelerImportResult(List.of(), bereitsZugeordneteSchuelerIds);
		}

		final Map<Long, Long> jahrgangBySchueler = getJahrgangBySchueler(idSchuljahresabschnitt, fehlendeSchuelerIds);
		final List<UvPlanungsabschnittSchuelerCreateRequest> requests =
				createMissingSchuelerRequests(idPlanungsabschnitt, fehlendeSchuelerIds, jahrgangBySchueler, fallbackJahrgangId);
		final List<UvPlanungsabschnittSchueler> neueSchueler = requests.isEmpty() ? List.of() : uvPlanungsabschnittSchuelerService.createMultiple(requests);
		bereitsZugeordneteSchuelerIds.addAll(neueSchueler.stream().map(schueler -> schueler.idSchueler).collect(Collectors.toSet()));
		return new SchuelerImportResult(neueSchueler, bereitsZugeordneteSchuelerIds);
	}

	/* Lädt alle für einen Schülerimport benötigten Quell- und Zielinformationen; wird von doImportSchueler verwendet. */
	private ImportKontext ladeImportKontext(final long idPlanungsabschnitt, final UvSchuelerImportOptions optionen) {
		final Schuljahresabschnitt quellAbschnitt = schuljahresabschnittService.getById(optionen.idSchuljahresabschnitt);
		final Map<Long, DTOKlassen> quellklassenById = klassenRepository.getMapBySchuljahresabschnitt(quellAbschnitt.id);
		final Set<Long> aktiveSchuelerIds = getAktiveSchuelerIds(quellAbschnitt);
		final Map<Long, DTOSchuelerLernabschnittsdaten> lernabschnitteBySchueler =
				lernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(aktiveSchuelerIds, optionen.idSchuljahresabschnitt);
		final Set<Long> bereitsZugeordneteSchuelerIds = getBereitsZugeordneteSchuelerIds(idPlanungsabschnitt);
		final Map<Long, Long> folgejahrgaengeById = optionen.folgejahrgang ? getFolgejahrgaengeById(lernabschnitteBySchueler.values()) : Map.of();
		final Map<Long, DTOKlassen> zielklassenById = (optionen.folgejahrgang && (quellAbschnitt.idFolgeAbschnitt != null))
				? klassenRepository.getMapBySchuljahresabschnitt(quellAbschnitt.idFolgeAbschnitt)
				: Map.of();
		return new ImportKontext(quellAbschnitt, quellklassenById, lernabschnitteBySchueler, aktiveSchuelerIds,
				bereitsZugeordneteSchuelerIds, folgejahrgaengeById, zielklassenById);
	}

	/* Ermittelt die bereits im Planungsabschnitt vorhandenen Schüler; wird beim Import und beim Ergänzen fehlender Schüler verwendet. */
	private Set<Long> getBereitsZugeordneteSchuelerIds(final long idPlanungsabschnitt) {
		return uvPlanungsabschnittSchuelerService.getListByPlanungsabschnitt(idPlanungsabschnitt).stream()
				.map(dto -> dto.idSchueler)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/* Filtert aus einer Schülerauswahl die noch nicht zugeordneten Schüler; wird beim Ergänzen fehlender Schüler verwendet. */
	private Set<Long> getFehlendeSchuelerIds(final Set<Long> schuelerIds, final Set<Long> bereitsZugeordneteSchuelerIds) {
		if ((schuelerIds == null) || schuelerIds.isEmpty()) {
			return Set.of();
		}
		return schuelerIds.stream()
				.filter(id -> !bereitsZugeordneteSchuelerIds.contains(id))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/* Liest die Jahrgangszuordnung der angegebenen Schüler im Quellabschnitt; wird beim Ergänzen fehlender Schüler verwendet. */
	private Map<Long, Long> getJahrgangBySchueler(final long idSchuljahresabschnitt, final Set<Long> schuelerIds) {
		return lernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(schuelerIds, idSchuljahresabschnitt)
				.values().stream()
				.filter(lernabschnitt -> lernabschnitt.Jahrgang_ID != null)
				.collect(Collectors.toMap(lernabschnitt -> lernabschnitt.Schueler_ID, lernabschnitt -> lernabschnitt.Jahrgang_ID, (a, b) -> a));
	}

	/* Erzeugt Anfragen für alle fehlenden Schüler mit ermitteltem oder vorgegebenem Jahrgang; wird beim Ergänzen fehlender Schüler verwendet. */
	private List<UvPlanungsabschnittSchuelerCreateRequest> createMissingSchuelerRequests(final long idPlanungsabschnitt,
			final Set<Long> fehlendeSchuelerIds, final Map<Long, Long> jahrgangBySchueler, final Long fallbackJahrgangId) {
		return fehlendeSchuelerIds.stream()
				.map(schuelerId -> createMissingSchuelerRequest(idPlanungsabschnitt, schuelerId, jahrgangBySchueler, fallbackJahrgangId))
				.filter(Objects::nonNull)
				.toList();
	}

	/* Erzeugt die Anlegeanfrage eines einzelnen fehlenden Schülers; wird von createMissingSchuelerRequests verwendet. */
	private UvPlanungsabschnittSchuelerCreateRequest createMissingSchuelerRequest(final long idPlanungsabschnitt,
			final long schuelerId, final Map<Long, Long> jahrgangBySchueler, final Long fallbackJahrgangId) {
		final long idJahrgang = jahrgangBySchueler.getOrDefault(schuelerId, (fallbackJahrgangId != null) ? fallbackJahrgangId : -1L);
		if (idJahrgang < 0) {
			return null;
		}
		final UvPlanungsabschnittSchuelerCreateRequest request = new UvPlanungsabschnittSchuelerCreateRequest();
		request.idPlanungsabschnitt = idPlanungsabschnitt;
		request.idSchueler = schuelerId;
		request.idJahrgang = idJahrgang;
		return request;
	}

	/* Ermittelt die aktiven Schüler des Quellabschnitts; wird beim Aufbau des Importkontexts verwendet. */
	private Set<Long> getAktiveSchuelerIds(final Schuljahresabschnitt quellAbschnitt) {
		final long idStatusAktiv = SchuelerStatus.AKTIV.daten(quellAbschnitt.schuljahr).id;
		return schuelerRepository.getMapByStatusAndSchuljahresabschnitt(quellAbschnitt.id, List.of(idStatusAktiv)).keySet();
	}

	/* Bildet verwendete Jahrgänge auf ihren Folgejahrgang ab; wird für Importe in den Folgejahrgang beim Aufbau des Kontexts verwendet. */
	private Map<Long, Long> getFolgejahrgaengeById(final Collection<DTOSchuelerLernabschnittsdaten> lernabschnitte) {
		final Set<Long> jahrgangIds = lernabschnitte.stream()
				.map(lernabschnitt -> lernabschnitt.Jahrgang_ID)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		if (jahrgangIds.isEmpty()) {
			return Map.of();
		}
		return jahrgaengeRepository.findMapByIds(jahrgangIds).values().stream()
				.filter(jahrgang -> jahrgang.Folgejahrgang_ID != null)
				.collect(Collectors.toMap(jahrgang -> jahrgang.ID, jahrgang -> jahrgang.Folgejahrgang_ID));
	}

	/* Ermittelt und erzeugt bei Bedarf die Klassen- und Schülergruppenzuordnungen; wird vom Schülerimport mit Klassenzuweisungen verwendet. */
	private KlassenZuordnungen ermittleKlassenZuordnungen(final long idPlanungsabschnitt, final ImportKontext kontext, final UvSchuelerImportOptions optionen) {
		final long idZielAbschnitt = getZielAbschnitt(kontext.quellAbschnitt(), optionen);
		final Map<String, UvKlasse> uvKlassenByKuerzel = uvKlasseService.getListByPlanungsabschnitt(idPlanungsabschnitt).stream()
				.collect(Collectors.toMap(klasse -> klasse.kuerzel, Function.identity(), (a, b) -> a));
		final KlassenZuordnungen neueKlassen = optionen.createMissingKlassen
				? createFehlendeKlassen(idPlanungsabschnitt, idZielAbschnitt, kontext, optionen, uvKlassenByKuerzel)
				: new KlassenZuordnungen(Map.of(), Map.of(), Map.of(), Map.of(), List.of(), List.of());
		final Map<String, UvKlasse> alleUvKlassenByKuerzel = new LinkedHashMap<>(uvKlassenByKuerzel);
		neueKlassen.neueKlassen().forEach(klasse -> alleUvKlassenByKuerzel.put(klasse.kuerzel, klasse));
		return new KlassenZuordnungen(
				mapQuellklassenZuUvWert(kontext, optionen, alleUvKlassenByKuerzel, klasse -> klasse.id),
				mapQuellklassenZuUvWert(kontext, optionen, alleUvKlassenByKuerzel, klasse -> klasse.idSchuelergruppe),
				mapIndividuelleFolgeklassenZuUvWert(kontext, optionen, alleUvKlassenByKuerzel, klasse -> klasse.id),
				mapIndividuelleFolgeklassenZuUvWert(kontext, optionen, alleUvKlassenByKuerzel, klasse -> klasse.idSchuelergruppe),
				neueKlassen.neueKlassen(),
				neueKlassen.neueSchuelergruppen());
	}

	/* Bestimmt Quell- oder Folgeabschnitt als Klassen-Zielabschnitt; wird bei der Ermittlung der Klassenzuordnungen verwendet. */
	private long getZielAbschnitt(final Schuljahresabschnitt quellAbschnitt, final UvSchuelerImportOptions optionen) {
		if (!optionen.folgejahrgang) {
			return quellAbschnitt.id;
		}
		if (quellAbschnitt.idFolgeAbschnitt == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für den gewählten Schuljahresabschnitt ist kein Folgeabschnitt definiert.");
		}
		return quellAbschnitt.idFolgeAbschnitt;
	}

	/* Legt noch nicht vorhandene UV-Klassen und ihre Schülergruppen an; wird bei aktivierter Klassenanlage verwendet. */
	private KlassenZuordnungen createFehlendeKlassen(final long idPlanungsabschnitt, final long idZielAbschnitt,
			final ImportKontext kontext, final UvSchuelerImportOptions optionen, final Map<String, UvKlasse> uvKlassenByKuerzel) {
		final List<DTOKlassen> fehlendeZielklassen = ermittleFehlendeZielklassen(kontext, optionen, idZielAbschnitt, uvKlassenByKuerzel);
		if (fehlendeZielklassen.isEmpty()) {
			return new KlassenZuordnungen(Map.of(), Map.of(), Map.of(), Map.of(), List.of(), List.of());
		}

		final List<UvSchuelergruppe> neueSchuelergruppen = createSchuelergruppen(idPlanungsabschnitt, fehlendeZielklassen);
		final Map<String, UvSchuelergruppe> schuelergruppenByKuerzel = neueSchuelergruppen.stream()
				.collect(Collectors.toMap(gruppe -> gruppe.bezeichnung.substring("Klasse ".length()), Function.identity(), (a, b) -> a));
		final List<UvKlasse> neueKlassen = createKlassen(idPlanungsabschnitt, idZielAbschnitt, fehlendeZielklassen, schuelergruppenByKuerzel);

		return new KlassenZuordnungen(
				Map.of(),
				Map.of(),
				Map.of(),
				Map.of(),
				neueKlassen,
				neueSchuelergruppen);
	}

	/* Bestimmt die für den Import relevanten, im UV noch fehlenden Klassen; wird vor dem Anlegen fehlender Klassen verwendet. */
	private List<DTOKlassen> ermittleFehlendeZielklassen(final ImportKontext kontext, final UvSchuelerImportOptions optionen, final long idZielAbschnitt,
			final Map<String, UvKlasse> uvKlassenByKuerzel) {
		final Set<Long> importierbareQuellklassenIds = getImportierbareQuellklassenIds(kontext);
		final List<DTOKlassen> regulaereZielklassen = importierbareQuellklassenIds.stream()
				.map(kontext.quellklassenById()::get)
				.filter(Objects::nonNull)
				.filter(quellklasse -> getZielklassenkuerzel(quellklasse, optionen) != null)
				.map(quellklasse -> optionen.folgejahrgang
						? createVirtuelleFolgeklasse(quellklasse, idZielAbschnitt, kontext.folgejahrgaengeById())
						: quellklasse)
				.filter(klasse -> !uvKlassenByKuerzel.containsKey(klasse.Klasse))
				.toList();
		final Set<Long> individuelleFolgeklassenIds = getIndividuelleFolgeklassenIds(kontext, optionen);
		final List<DTOKlassen> individuelleFolgeklassen = kontext.zielklassenById().values().stream()
				.filter(klasse -> individuelleFolgeklassenIds.contains(klasse.ID))
				.filter(klasse -> (klasse.Klasse != null) && !klasse.Klasse.isBlank())
				.filter(klasse -> !uvKlassenByKuerzel.containsKey(klasse.Klasse))
				.toList();
		return java.util.stream.Stream.concat(regulaereZielklassen.stream(), individuelleFolgeklassen.stream())
				.collect(Collectors.toMap(klasse -> klasse.Klasse, Function.identity(), (a, b) -> a, LinkedHashMap::new))
				.values().stream().toList();
	}

	/* Ermittelt die Quellklassen, die für noch nicht importierte aktive Schüler benötigt werden; wird beim Anlegen virtueller Folgeklassen verwendet. */
	private Set<Long> getImportierbareQuellklassenIds(final ImportKontext kontext) {
		return kontext.lernabschnitteBySchueler().values().stream()
				.filter(lernabschnitt -> kontext.aktiveSchuelerIds().contains(lernabschnitt.Schueler_ID))
				.filter(lernabschnitt -> !kontext.bereitsZugeordneteSchuelerIds().contains(lernabschnitt.Schueler_ID))
				.map(lernabschnitt -> lernabschnitt.Klassen_ID)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	/* Ermittelt die von importierbaren Schülern referenzierten individuellen Schild-Folgeklassen; wird beim Anlegen und Zuordnen von UV-Klassen verwendet. */
	private Set<Long> getIndividuelleFolgeklassenIds(final ImportKontext kontext, final UvSchuelerImportOptions optionen) {
		if (!optionen.folgejahrgang || !optionen.versetzungsvermerkeBeruecksichtigen) {
			return Set.of();
		}
		return kontext.lernabschnitteBySchueler().values().stream()
				.filter(lernabschnitt -> kontext.aktiveSchuelerIds().contains(lernabschnitt.Schueler_ID))
				.filter(lernabschnitt -> !kontext.bereitsZugeordneteSchuelerIds().contains(lernabschnitt.Schueler_ID))
				.map(lernabschnitt -> lernabschnitt.Folgeklasse_ID)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	/* Leitet die Daten einer anzulegenden UV-Folgeklasse aus ihrer Quellklasse ab. */
	private DTOKlassen createVirtuelleFolgeklasse(final DTOKlassen quellklasse, final long idZielAbschnitt,
			final Map<Long, Long> folgejahrgaengeById) {
		final DTOKlassen virtuelleFolgeklasse = new DTOKlassen(quellklasse.ID, idZielAbschnitt, quellklasse.FKlasse);
		virtuelleFolgeklasse.Bezeichnung = quellklasse.FKlasse;
		virtuelleFolgeklasse.Jahrgang_ID = folgejahrgaengeById.get(quellklasse.Jahrgang_ID);
		virtuelleFolgeklasse.OrgFormKrz = quellklasse.OrgFormKrz;
		virtuelleFolgeklasse.ASDSchulformNr = quellklasse.ASDSchulformNr;
		virtuelleFolgeklasse.Fachklasse_ID = quellklasse.Fachklasse_ID;
		return virtuelleFolgeklasse;
	}


	/* Legt zu fehlenden Zielklassen die zugehörigen Standard-Schülergruppen an; wird beim Anlegen fehlender Klassen verwendet. */
	private List<UvSchuelergruppe> createSchuelergruppen(final long idPlanungsabschnitt, final List<DTOKlassen> fehlendeZielklassen) {
		return uvSchuelergruppeService.createMultiple(fehlendeZielklassen.stream()
				.map(klasse -> createSchuelergruppeRequest(idPlanungsabschnitt, klasse))
				.toList());
	}

	/* Erstellt die Anlegeanfrage für die Schülergruppe einer Klasse; wird von createSchuelergruppen verwendet. */
	private UvSchuelergruppeCreateRequest createSchuelergruppeRequest(final long idPlanungsabschnitt, final DTOKlassen klasse) {
		final UvSchuelergruppeCreateRequest request = new UvSchuelergruppeCreateRequest();
		request.idPlanungsabschnitt = idPlanungsabschnitt;
		request.bezeichnung = "Klasse " + klasse.Klasse;
		if (klasse.Jahrgang_ID != null) {
			request.idsJahrgaengeErlaubt.add(klasse.Jahrgang_ID);
		}
		return request;
	}

	/* Legt UV-Klassen für die fehlenden Zielklassen an; wird nach dem Anlegen der Schülergruppen verwendet. */
	private List<UvKlasse> createKlassen(final long idPlanungsabschnitt, final long idZielAbschnitt, final List<DTOKlassen> fehlendeZielklassen,
			final Map<String, UvSchuelergruppe> schuelergruppenByKuerzel) {
		return uvKlasseService.createMultiple(fehlendeZielklassen.stream()
				.map(klasse -> createKlasseRequest(idPlanungsabschnitt, idZielAbschnitt, klasse, schuelergruppenByKuerzel.get(klasse.Klasse)))
				.toList());
	}

	/* Überträgt die relevanten Schild-Klassendaten in eine UV-Klassen-Anfrage; wird von createKlassen verwendet. */
	private UvKlasseCreateRequest createKlasseRequest(final long idPlanungsabschnitt, final long idZielAbschnitt,
			final DTOKlassen klasse, final UvSchuelergruppe schuelergruppe) {
		final UvKlasseCreateRequest request = new UvKlasseCreateRequest();
		request.idPlanungsabschnitt = idPlanungsabschnitt;
		request.idSchuljahresabschnitt = idZielAbschnitt;
		request.bezeichnung = klasse.Bezeichnung;
		request.kuerzel = klasse.Klasse;
		request.parallelitaet = getParallelitaet(klasse.Klasse);
		request.idSchuelergruppe = schuelergruppe.id;
		request.orgFormKrz = klasse.OrgFormKrz;
		request.idFachklasse = klasse.Fachklasse_ID;
		request.asdSchulformNr = klasse.ASDSchulformNr;
		return request;
	}

	/* Leitet die Parallelität aus dem letzten Buchstaben des Klassenkürzels ab; wird beim Erzeugen einer UV-Klassen-Anfrage verwendet. */
	private String getParallelitaet(final String kuerzel) {
		if ((kuerzel == null) || (kuerzel.length() <= 1)) {
			return "";
		}
		final char lastChar = kuerzel.charAt(kuerzel.length() - 1);
		return Character.isLetter(lastChar) ? String.valueOf(lastChar) : "";
	}

	/* Leitet die Zuordnungen der Quellklassen aus dem jeweils maßgeblichen UV-Klassenkürzel ab; wird für Klassen und Schülergruppen verwendet. */
	private Map<Long, Long> mapQuellklassenZuUvWert(final ImportKontext kontext, final UvSchuelerImportOptions optionen,
			final Map<String, UvKlasse> uvKlassenByKuerzel, final ToLongFunction<UvKlasse> wert) {
		return getImportierbareQuellklassenIds(kontext).stream()
				.map(kontext.quellklassenById()::get)
				.filter(Objects::nonNull)
				.filter(klasse -> getZielklassenkuerzel(klasse, optionen) != null)
				.filter(klasse -> uvKlassenByKuerzel.containsKey(getZielklassenkuerzel(klasse, optionen)))
				.collect(Collectors.toMap(klasse -> klasse.ID,
						klasse -> wert.applyAsLong(uvKlassenByKuerzel.get(getZielklassenkuerzel(klasse, optionen)))));
	}

	/* Bestimmt das für die UV-Zuordnung maßgebliche Klassen- oder Folgeklassenkürzel einer Quellklasse. */
	private String getZielklassenkuerzel(final DTOKlassen quellklasse, final UvSchuelerImportOptions optionen) {
		final String kuerzel = optionen.folgejahrgang ? quellklasse.FKlasse : quellklasse.Klasse;
		return ((kuerzel == null) || kuerzel.isBlank()) ? null : kuerzel;
	}

	/* Leitet die Zuordnungen individueller Schild-Folgeklassen zu UV-Werten ab; wird für Klassen und Schülergruppen verwendet. */
	private Map<Long, Long> mapIndividuelleFolgeklassenZuUvWert(final ImportKontext kontext, final UvSchuelerImportOptions optionen,
			final Map<String, UvKlasse> uvKlassenByKuerzel, final ToLongFunction<UvKlasse> wert) {
		final Set<Long> individuelleFolgeklassenIds = getIndividuelleFolgeklassenIds(kontext, optionen);
		return kontext.zielklassenById().entrySet().stream()
				.filter(entry -> individuelleFolgeklassenIds.contains(entry.getKey()))
				.filter(entry -> (entry.getValue().Klasse != null) && uvKlassenByKuerzel.containsKey(entry.getValue().Klasse))
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> wert.applyAsLong(uvKlassenByKuerzel.get(entry.getValue().Klasse))));
	}

	/* Erzeugt die fehlenden Planungsabschnitt-Schüler anhand des Importkontexts; wird vom eigentlichen Schülerimport verwendet. */
	private List<UvPlanungsabschnittSchueler> importierePlanungsabschnittSchueler(final long idPlanungsabschnitt,
			final ImportKontext kontext, final UvSchuelerImportOptions optionen, final Map<Long, Long> quellklasseZuUvKlasse,
			final Map<Long, Long> individuelleFolgeklasseZuUvKlasse, final boolean mitKlassenzuweisungen) {
		final Set<Long> bereitsImportiert = new LinkedHashSet<>(kontext.bereitsZugeordneteSchuelerIds());
		final List<UvPlanungsabschnittSchuelerCreateRequest> requests =
				kontext.lernabschnitteBySchueler().values().stream()
						.filter(lernabschnitt -> istImportierbar(lernabschnitt, kontext, bereitsImportiert))
						.map(lernabschnitt -> createSchuelerRequest(
								idPlanungsabschnitt,
								lernabschnitt,
								optionen,
								kontext,
								quellklasseZuUvKlasse,
								individuelleFolgeklasseZuUvKlasse,
								mitKlassenzuweisungen))
						.filter(Objects::nonNull)
						.toList();
		requests.forEach(request -> bereitsImportiert.add(request.idSchueler));
		return requests.isEmpty() ? List.of() : uvPlanungsabschnittSchuelerService.createMultiple(requests);
	}

	/* Prüft Aktivstatus, vorhandene Zuordnung und Jahrgang eines Lernabschnitts; wird beim Filtern importierbarer Schüler verwendet. */
	private boolean istImportierbar(final DTOSchuelerLernabschnittsdaten lernabschnitt, final ImportKontext kontext, final Set<Long> bereitsImportiert) {
		return kontext.aktiveSchuelerIds().contains(lernabschnitt.Schueler_ID)
				&& !bereitsImportiert.contains(lernabschnitt.Schueler_ID)
				&& (lernabschnitt.Jahrgang_ID != null);
	}

	/* Ermittelt Zieljahrgang und Zielklasse eines Schülers und erstellt daraus die Anlegeanfrage; wird beim Schülerimport verwendet. */
	private UvPlanungsabschnittSchuelerCreateRequest createSchuelerRequest(final long idPlanungsabschnitt,
			final DTOSchuelerLernabschnittsdaten lernabschnitt, final UvSchuelerImportOptions optionen, final ImportKontext kontext,
			final Map<Long, Long> quellklasseZuUvKlasse, final Map<Long, Long> individuelleFolgeklasseZuUvKlasse,
			final boolean mitKlassenzuweisungen) {
		final DTOKlassen individuelleFolgeklasse = getIndividuelleFolgeklasse(lernabschnitt, optionen, kontext);
		final Long idJahrgang = getZieljahrgang(lernabschnitt, optionen, kontext, individuelleFolgeklasse);
		if (idJahrgang == null) {
			return null;
		}

		final Long idKlasse = getZielUvKlasse(lernabschnitt, optionen, individuelleFolgeklasse, mitKlassenzuweisungen,
				quellklasseZuUvKlasse, individuelleFolgeklasseZuUvKlasse);
		if (mitKlassenzuweisungen && (lernabschnitt.Klassen_ID != null) && (idKlasse == null)) {
			return null;
		}

		final UvPlanungsabschnittSchuelerCreateRequest request = new UvPlanungsabschnittSchuelerCreateRequest();
		request.idPlanungsabschnitt = idPlanungsabschnitt;
		request.idSchueler = lernabschnitt.Schueler_ID;
		request.idJahrgang = idJahrgang;
		request.idKlasse = idKlasse;
		return request;
	}

	/* Liefert die individuelle Folgeklasse eines Schülers bei aktivierter Vermerksberücksichtigung; wird bei der Zielzuordnung verwendet. */
	private DTOKlassen getIndividuelleFolgeklasse(final DTOSchuelerLernabschnittsdaten lernabschnitt, final UvSchuelerImportOptions optionen,
			final ImportKontext kontext) {
		if (!optionen.folgejahrgang || !optionen.versetzungsvermerkeBeruecksichtigen || (lernabschnitt.Folgeklasse_ID == null)) {
			return null;
		}
		return kontext.zielklassenById().get(lernabschnitt.Folgeklasse_ID);
	}

	/* Bestimmt den Zieljahrgang unter Einbeziehung individueller Folgeklassen und Verbleibsvermerke; wird beim Erzeugen einer Schüleranfrage verwendet. */
	private Long getZieljahrgang(final DTOSchuelerLernabschnittsdaten lernabschnitt, final UvSchuelerImportOptions optionen,
			final ImportKontext kontext, final DTOKlassen individuelleFolgeklasse) {
		if (!optionen.folgejahrgang) {
			return lernabschnitt.Jahrgang_ID;
		}
		if ((individuelleFolgeklasse != null) && (individuelleFolgeklasse.Jahrgang_ID != null)) {
			return individuelleFolgeklasse.Jahrgang_ID;
		}
		if (optionen.versetzungsvermerkeBeruecksichtigen && istVerbleibenderVersetzungsvermerk(lernabschnitt.VersetzungKrz)) {
			return lernabschnitt.Jahrgang_ID;
		}
		return kontext.folgejahrgaengeById().get(lernabschnitt.Jahrgang_ID);
	}

	/* Bestimmt die UV-Zielklasse unter Einbeziehung individueller Folgeklassen und Verbleibsvermerke; wird beim Erzeugen einer Schüleranfrage verwendet. */
	private Long getZielUvKlasse(final DTOSchuelerLernabschnittsdaten lernabschnitt, final UvSchuelerImportOptions optionen,
			final DTOKlassen individuelleFolgeklasse, final boolean mitKlassenzuweisungen, final Map<Long, Long> quellklasseZuUvKlasse,
			final Map<Long, Long> individuelleFolgeklasseZuUvKlasse) {
		if (!mitKlassenzuweisungen || (lernabschnitt.Klassen_ID == null)) {
			return null;
		}
		if (individuelleFolgeklasse != null) {
			return individuelleFolgeklasseZuUvKlasse.get(individuelleFolgeklasse.ID);
		}
		if (optionen.folgejahrgang && optionen.versetzungsvermerkeBeruecksichtigen
				&& istVerbleibenderVersetzungsvermerk(lernabschnitt.VersetzungKrz)) {
			return null;
		}
		return quellklasseZuUvKlasse.get(lernabschnitt.Klassen_ID);
	}

	/* Erkennt Versetzungsvermerke, die einen Verbleib im Jahrgang bedeuten; wird bei Jahrgangs- und Klassenzielzuordnung verwendet. */
	private boolean istVerbleibenderVersetzungsvermerk(final String kuerzel) {
		final Versetzungsvermerk vermerk = Versetzungsvermerk.data().getWertByKuerzel(kuerzel);
		if (vermerk == null) {
			return false;
		}
		return switch (vermerk) {
			case FREIWILLIG_ZURUECK, NICHT_VERSETZT, NICHT_VERSETZT_NACHPRUEFUNG, VERBLEIB_SCHULEINGANGSPHASE, VERBLEIB_STUFE -> true;
			default -> false;
		};
	}

	/* Ordnet importierte Schüler den Klassen-Schülergruppen zu; wird nach dem Schülerimport mit Klassenzuweisungen verwendet. */
	private List<UvSchuelergruppeSchueler> importiereSchuelergruppen(final long idPlanungsabschnitt, final ImportKontext kontext,
			final UvSchuelerImportOptions optionen, final Map<Long, Long> quellklasseZuSchuelergruppe,
			final Map<Long, Long> individuelleFolgeklasseZuSchuelergruppe, final List<UvPlanungsabschnittSchueler> importierteSchueler) {
		if ((quellklasseZuSchuelergruppe.isEmpty() && individuelleFolgeklasseZuSchuelergruppe.isEmpty()) || importierteSchueler.isEmpty()) {
			return List.of();
		}
		final Set<Long> schuelerIds = importierteSchueler.stream().map(schueler -> schueler.idSchueler).collect(Collectors.toSet());
		final List<UvSchuelergruppeSchuelerCreateRequest> requests = kontext.lernabschnitteBySchueler().values().stream()
				.filter(lernabschnitt -> schuelerIds.contains(lernabschnitt.Schueler_ID))
				.filter(lernabschnitt -> lernabschnitt.Klassen_ID != null)
				.map(lernabschnitt -> createSchuelergruppenRequest(idPlanungsabschnitt, lernabschnitt, optionen, kontext,
						quellklasseZuSchuelergruppe, individuelleFolgeklasseZuSchuelergruppe))
				.filter(Objects::nonNull)
				.toList();
		return requests.isEmpty() ? List.of() : uvSchuelergruppeSchuelerService.createMultiple(requests);
	}

	/* Erstellt die Gruppenmitgliedschaft eines importierten Schülers; wird von importiereSchuelergruppen verwendet. */
	private UvSchuelergruppeSchuelerCreateRequest createSchuelergruppenRequest(final long idPlanungsabschnitt,
			final DTOSchuelerLernabschnittsdaten lernabschnitt, final UvSchuelerImportOptions optionen, final ImportKontext kontext,
			final Map<Long, Long> quellklasseZuSchuelergruppe, final Map<Long, Long> individuelleFolgeklasseZuSchuelergruppe) {
		final DTOKlassen individuelleFolgeklasse = getIndividuelleFolgeklasse(lernabschnitt, optionen, kontext);
		final Long idSchuelergruppe = (individuelleFolgeklasse == null)
				? quellklasseZuSchuelergruppe.get(lernabschnitt.Klassen_ID)
				: individuelleFolgeklasseZuSchuelergruppe.get(individuelleFolgeklasse.ID);
		if (idSchuelergruppe == null) {
			return null;
		}
		final UvSchuelergruppeSchuelerCreateRequest request = new UvSchuelergruppeSchuelerCreateRequest();
		request.idPlanungsabschnitt = idPlanungsabschnitt;
		request.idSchuelergruppe = idSchuelergruppe;
		request.idSchueler = lernabschnitt.Schueler_ID;
		return request;
	}

}
