package de.svws_nrw.service.klassen;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.klassen.KlassenMapper;
import de.svws_nrw.mapper.klassen.KlassenMappingContext;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.teilstandort.TeilstandortRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für den Zugriff auf Klassen (Lesen, Anlegen, Ändern und Löschen) und für die Default-Sortierung.
 * <p>
 * Der Service bestimmt die Klassen und prüft die Eingaben. Das Laden der Daten, die für das Mapping benötigt werden
 * (Klassenleitungen, Schüler, Schuljahresabschnitte, Nachbarklassen), übernimmt der {@link KlassenMappingContextLoader}.
 * Beim Anlegen und Ändern prüft der Service die Attribute mit Datenbankbezug und setzt sie, alle übrigen Attribute überträgt der
 * {@link KlassenMapper}. Die Klassenleitungen ersetzt der {@link KlassenleitungService}, die Default-Sortierung setzt der {@link KlassenDefaultSorter}.
 */
public final class KlassenService {

	private static final String ID_NICHT_NULL = "Die ID für die Klasse darf nicht null sein.";
	private static final String IDS_NICHT_NULL = "Die IDs für die Klassen dürfen nicht null sein.";
	private static final String KLASSE_NICHT_GEFUNDEN = "Keine Klasse zur ID %d gefunden.";
	private static final String KLASSEN_NICHT_ALLE_GEFUNDEN = "Es wurden nicht alle Klassen zu den IDs gefunden.";
	private static final String ABSCHNITT_UNGUELTIG_BEIM_ANLEGEN =
			"Eine Klasse kann nur mit einem gültigen Schuljahresabschnitt angelegt werden. Die ID %d ist ungültig.";
	private static final String KEINE_KLASSEN_GEFUNDEN = "Es wurden keine Klassen zu den IDs gefunden.";
	private static final String KLASSE_HAT_SCHUELER = "Klasse %s (ID: %d) hat noch %d verknüpfte(n) Schüler.";
	private static final String KUERZEL_EXISTIERT_BEREITS = "Die Klasse %s existiert bereits im Schuljahresabschnitt %d";
	private static final String JAHRGANG_NICHT_GEFUNDEN = "Der Jahrgang mit der ID %d konnte nicht gefunden werden.";
	private static final String KEIN_TEILSTANDORT = "Es ist kein Teilstandort definiert, es muss mindestens ein Teilstandort hinterlegt sein.";
	private static final String TEILSTANDORT_NICHT_GEFUNDEN = "Der Teilstandort %s wurde nicht gefunden.";
	private static final String VORGAENGERKLASSE_NICHT_GEFUNDEN = "Die Vorgängerklasse mit der ID %d wurde nicht gefunden.";
	private static final String FOLGEKLASSE_NICHT_GEFUNDEN = "Die Folgeklasse mit der ID %d wurde nicht gefunden.";
	private static final String VORGAENGERKLASSE_FALSCHER_ABSCHNITT =
			"Die ID für die Vorgängerklasse gehört nicht zu einer Klasse aus dem vorigen Schuljahresabschnitt.";
	private static final String FOLGEKLASSE_FALSCHER_ABSCHNITT =
			"Die ID für die Folgeklasse gehört nicht zu einer Klasse aus dem nachfolgenden Schuljahresabschnitt.";

	private final KlassenRepository klassenRepository;
	private final JahrgangRepository jahrgangRepository;
	private final TeilstandortRepository teilstandortRepository;
	private final SchuelerLernabschnittRepository lernabschnittRepository;
	private final SchuljahresabschnittService schuljahresabschnittService;
	private final KlassenMappingContextLoader kontextLoader;
	private final KlassenMapper klassenMapper;
	private final EigeneSchuleService eigeneSchuleService;
	private final KlassenleitungService klassenleitungService;
	private final KlassenDefaultSorter defaultSorter;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repositories                  die Repositories, die der Service benötigt
	 * @param schuljahresabschnittService   der Service für die Schuljahresabschnitte
	 * @param eigeneSchuleService           der Service für die Daten der eigenen Schule (Schulform)
	 * @param kontextLoader                 der Loader für die Daten, die für das Mapping der Klassen benötigt werden
	 * @param klassenMapper                 der Mapper für die Konvertierung der Klassen
	 * @param klassenleitungService         der Service, der die Klassenleitungen einer Klasse ersetzt
	 * @param defaultSorter                 der Sorter, der die Default-Sortierung der Klassen setzt
	 */
	KlassenService(
			final KlassenRepositories repositories,
			final SchuljahresabschnittService schuljahresabschnittService,
			final EigeneSchuleService eigeneSchuleService,
			final KlassenMappingContextLoader kontextLoader,
			final KlassenMapper klassenMapper,
			final KlassenleitungService klassenleitungService,
			final KlassenDefaultSorter defaultSorter) {
		this.klassenRepository = repositories.klassenRepository();
		this.jahrgangRepository = repositories.jahrgangRepository();
		this.teilstandortRepository = repositories.teilstandortRepository();
		this.lernabschnittRepository = repositories.lernabschnittRepository();
		this.schuljahresabschnittService = schuljahresabschnittService;
		this.eigeneSchuleService = eigeneSchuleService;
		this.kontextLoader = kontextLoader;
		this.klassenMapper = klassenMapper;
		this.klassenleitungService = klassenleitungService;
		this.defaultSorter = defaultSorter;
	}

	/**
	 * Bestimmt die Daten einer Klasse inklusive der Schüler der Klasse.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Daten der Klasse
	 */
	public KlassenDaten getById(final Long id) {
		return toKlassenDaten(List.of(getDtoById(id)), true).getFirst();
	}

	/**
	 * Bestimmt die Daten aller Klassen eines Schuljahresabschnitts.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param mitSchueler              gibt an, ob die Schüler der Klassen mitgeliefert werden sollen
	 *
	 * @return die Daten der Klassen, bzw. eine leere Liste, wenn der Schuljahresabschnitt keine Klassen enthält
	 */
	public List<KlassenDaten> getListBySchuljahresabschnitt(final long idSchuljahresabschnitt, final boolean mitSchueler) {
		// Der Abschnitt wird auch dann geprüft, wenn er keine Klassen enthält. Der Loader lädt ihn später erneut, was durch den Cache der Verbindung nichts kostet.
		schuljahresabschnittService.getById(idSchuljahresabschnitt);
		return toKlassenDaten(klassenRepository.getListBySchuljahresabschnitt(idSchuljahresabschnitt), mitSchueler);
	}

	/**
	 * Bestimmt die Daten der Klassen zu den übergebenen IDs inklusive der Schüler der Klassen. Die Klassen werden in der
	 * Reihenfolge der IDs zurückgegeben, wobei doppelte IDs nur einmal berücksichtigt werden.
	 *
	 * @param ids   die IDs der Klassen
	 *
	 * @return die Daten der Klassen mit Schülern, bzw. eine leere Liste, wenn keine IDs übergeben wurden
	 */
	public List<KlassenDaten> getListByIdsMitSchueler(final List<Long> ids) {
		return getListByIds(ids, true);
	}

	/**
	 * Bestimmt die Daten der Klassen zu den übergebenen IDs. Die Klassen werden in der Reihenfolge der IDs
	 * zurückgegeben, wobei doppelte IDs nur einmal berücksichtigt werden.
	 *
	 * @param ids           die IDs der Klassen
	 * @param mitSchueler   gibt an, ob die Schüler der Klassen mitgeliefert werden sollen
	 *
	 * @return die Daten der Klassen, bzw. eine leere Liste, wenn keine IDs übergeben wurden
	 */
	public List<KlassenDaten> getListByIds(final List<Long> ids, final boolean mitSchueler) {
		return toKlassenDaten(getDtosByIds(ids), mitSchueler);
	}

	/**
	 * Legt eine neue Klasse an. Nicht angegebene Attribute werden mit den Standardwerten der Schulform vorbelegt.
	 *
	 * @param request   der Request mit den Attributen der neuen Klasse
	 *
	 * @return die Daten der neuen Klasse
	 */
	public KlassenDaten create(final KlassenCreateRequest request) {
		return TransactionSupport.transactional(() -> {
			final var abschnitt = getAbschnittFuerNeueKlasse(request.idSchuljahresabschnitt);
			final var dto = createDto(request, abschnitt, eigeneSchuleService.getSchulform());
			klassenRepository.create(dto);

			return toKlassenDaten(List.of(dto), true).getFirst();
		});
	}

	/**
	 * Ändert die Attribute einer Klasse. Nur die im Request gesetzten Attribute werden geändert.
	 *
	 * @param id        die ID der Klasse
	 * @param request   der Request mit den zu ändernden Attributen
	 *
	 * @return die Daten der geänderten Klasse
	 */
	public KlassenDaten patch(final Long id, final KlassenPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final var dto = getDtoById(id);
			final var abschnitt = schuljahresabschnittService.getById(dto.Schuljahresabschnitts_ID);
			applyPatch(dto, abschnitt, eigeneSchuleService.getSchulform(), request);
			klassenRepository.update(dto);

			return toKlassenDaten(List.of(dto), true).getFirst();
		});
	}

	/**
	 * Löscht die Klassen zu den angegebenen IDs. Klassen, denen noch nicht gelöschte Schüler zugeordnet sind, werden nicht gelöscht.
	 * Für jede ID wird das Ergebnis der Löschoperation zurückgegeben.
	 *
	 * @param ids   die IDs der zu löschenden Klassen
	 *
	 * @return die Ergebnisse der Löschoperationen in der Reihenfolge der IDs, bzw. eine leere Liste, wenn keine IDs übergeben wurden
	 */
	public List<SimpleOperationResponse> delete(final List<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, IDS_NICHT_NULL);
		}
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}
		return TransactionSupport.transactional(() -> deleteKlassen(ids));
	}

	/**
	 * Setzt für die Klassen eines Schuljahresabschnitts die Default-Sortierung anhand der Sortierung der Jahrgänge und der Parallelität.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public void setDefaultSortierung(final long idSchuljahresabschnitt) {
		TransactionSupport.transactional(() -> defaultSorter.apply(idSchuljahresabschnitt));
	}

	/**
	 * Löscht die Klassen zu den IDs. Klassen mit Schülern und nicht gefundene Klassen werden nicht gelöscht und mit einer Fehlermeldung
	 * zurückgegeben. Werden gar keine Klassen gefunden, so wird eine Exception geworfen.
	 *
	 * @param ids   die IDs der zu löschenden Klassen, nicht leer
	 *
	 * @return die Ergebnisse der Löschoperationen in der Reihenfolge der IDs, doppelte IDs nur einmal
	 */
	private List<SimpleOperationResponse> deleteKlassen(final List<Long> ids) {
		final var idsDistinct = ids.stream()
				.filter(Objects::nonNull)
				.distinct()
				.toList();
		final var klassen = klassenRepository.findListByIds(idsDistinct).stream()
				.collect(Collectors.toMap(klasse -> klasse.ID, Function.identity()));
		if (klassen.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, KEINE_KLASSEN_GEFUNDEN);
		}
		final var anzahlSchuelerJeKlasse = lernabschnittRepository.getMapAnzahlSchuelerAktuell(klassen.keySet());

		return idsDistinct.stream()
				.map(id -> deleteKlasse(id, klassen, anzahlSchuelerJeKlasse))
				.toList();
	}

	private SimpleOperationResponse deleteKlasse(final long id, final Map<Long, DTOKlassen> klassen, final Map<Long, Long> anzahlSchuelerJeKlasse) {
		final var klasse = klassen.get(id);
		if (klasse == null) {
			return SimpleOperationResponse.ofError(id, KLASSE_NICHT_GEFUNDEN.formatted(id));
		}
		final var anzahlSchueler = anzahlSchuelerJeKlasse.getOrDefault(id, 0L);
		if (anzahlSchueler > 0) {
			return SimpleOperationResponse.ofError(id, KLASSE_HAT_SCHUELER.formatted(klasse.Klasse, id, anzahlSchueler));
		}
		return deleteDto(klasse);
	}

	private SimpleOperationResponse deleteDto(final DTOKlassen klasse) {
		try {
			klassenRepository.delete(klasse);
		} catch (final RepositoryException e) {
			return SimpleOperationResponse.ofError(klasse.ID, e.getMessage());
		}
		return SimpleOperationResponse.ofSuccess(klasse.ID);
	}

	// Anlegen und Ändern

	/**
	 * Erstellt die neue Klasse mit den Standardwerten und den Attributen aus dem Request. Die Klasse ist noch nicht persistiert.
	 *
	 * @param request     der Request
	 * @param abschnitt   der Schuljahresabschnitt der neuen Klasse
	 * @param schulform   die Schulform der Schule
	 *
	 * @return die neue Klasse
	 */
	private DTOKlassen createDto(final KlassenCreateRequest request, final Schuljahresabschnitt abschnitt, final Schulform schulform) {
		validateCreateRequest(request, abschnitt);

		final var jahrgang = loadJahrgang(request.idJahrgang);
		final var adrMerkmal = getAdrMerkmalDefault();

		final var dto = klassenMapper.toDomain(request, abschnitt, schulform, jahrgang, adrMerkmal);
		dto.VKlasse = loadKuerzelVorgaengerklasse(request.idVorgaengerklasse, abschnitt);
		dto.FKlasse = loadKuerzelFolgeklasse(request.idFolgeklasse, abschnitt);

		return dto;
	}

	private void validateCreateRequest(final KlassenCreateRequest request, final Schuljahresabschnitt abschnitt) {
		validateKuerzelCreate(request.kuerzel, abschnitt.id);
		if (request.teilstandort != null) {
			validateTeilstandort(request.teilstandort);
		}
	}

	/**
	 * Wendet die im Patch gesetzten Attribute auf die Klasse an.
	 *
	 * @param dto         die Klasse
	 * @param abschnitt   der Schuljahresabschnitt der Klasse
	 * @param schulform   die Schulform der Schule
	 * @param request     der Patch
	 */
	private void applyPatch(final DTOKlassen dto, final Schuljahresabschnitt abschnitt, final Schulform schulform, final KlassenPatchRequest request) {
		validatePatchRequest(dto, request);

		klassenMapper.patch(request, dto, abschnitt, schulform);

		request.idJahrgang.ifPresent(idJahrgang -> klassenMapper.applyJahrgang(dto, loadJahrgang(idJahrgang)));
		request.idVorgaengerklasse.ifPresent(id -> dto.VKlasse = loadKuerzelVorgaengerklasse(id, abschnitt));
		request.idFolgeklasse.ifPresent(id -> dto.FKlasse = loadKuerzelFolgeklasse(id, abschnitt));
		request.klassenLeitungen.ifPresent(idsLehrer -> klassenleitungService.replace(dto.ID, idsLehrer));
	}

	private void validateKuerzelCreate(final String kuerzel, final long idAbschnitt) {
		if (klassenRepository.kuerzelIsAlreadyUsedCreate(kuerzel, idAbschnitt)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KUERZEL_EXISTIERT_BEREITS.formatted(kuerzel, idAbschnitt));
		}
	}

	private void validatePatchRequest(final DTOKlassen dto, final KlassenPatchRequest request) {
		request.kuerzel.ifPresent(kuerzel -> validateKuerzelPatch(dto, kuerzel));
		request.teilstandort.ifPresent(this::validateTeilstandort);
	}

	private void validateKuerzelPatch(final DTOKlassen dto, final String kuerzel) {
		if (klassenRepository.kuerzelIsAlreadyUsedPatch(kuerzel, dto.Schuljahresabschnitts_ID, dto.ID)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KUERZEL_EXISTIERT_BEREITS.formatted(kuerzel, dto.Schuljahresabschnitts_ID));
		}
	}

	private DTOJahrgang loadJahrgang(final Long idJahrgang) {
		if (idJahrgang == null) {
			return null;
		}
		return jahrgangRepository.findById(idJahrgang)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, JAHRGANG_NICHT_GEFUNDEN.formatted(idJahrgang)));
	}

	private String getAdrMerkmalDefault() {
		return teilstandortRepository.findFirst()
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, KEIN_TEILSTANDORT))
				.AdrMerkmal;
	}

	private void validateTeilstandort(final String adrMerkmal) {
		if (!teilstandortRepository.existsById(adrMerkmal)) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, TEILSTANDORT_NICHT_GEFUNDEN.formatted(adrMerkmal));
		}
	}

	private String loadKuerzelVorgaengerklasse(final Long idVorgaengerklasse, final Schuljahresabschnitt abschnitt) {
		return loadKuerzelNachbarklasse(idVorgaengerklasse, abschnitt.idVorigerAbschnitt, VORGAENGERKLASSE_NICHT_GEFUNDEN, VORGAENGERKLASSE_FALSCHER_ABSCHNITT);
	}

	private String loadKuerzelFolgeklasse(final Long idFolgeklasse, final Schuljahresabschnitt abschnitt) {
		return loadKuerzelNachbarklasse(idFolgeklasse, abschnitt.idFolgeAbschnitt, FOLGEKLASSE_NICHT_GEFUNDEN, FOLGEKLASSE_FALSCHER_ABSCHNITT);
	}

	/**
	 * Bestimmt das Kürzel der Vorgänger- bzw. Folgeklasse. Die Klasse muss existieren und im erwarteten Schuljahresabschnitt liegen.
	 *
	 * @param idKlasse                   die ID der Vorgänger- bzw. Folgeklasse oder null
	 * @param idErwarteterAbschnitt      die ID des Schuljahresabschnitts, in dem die Klasse liegen muss
	 * @param meldungNichtGefunden       die Meldung, wenn die Klasse nicht existiert
	 * @param meldungFalscherAbschnitt   die Meldung, wenn die Klasse im falschen Schuljahresabschnitt liegt
	 *
	 * @return das Kürzel der Klasse oder null, wenn keine ID übergeben wurde
	 */
	private String loadKuerzelNachbarklasse(final Long idKlasse, final Long idErwarteterAbschnitt, final String meldungNichtGefunden,
			final String meldungFalscherAbschnitt) {
		if (idKlasse == null) {
			return null;
		}
		final var klasse = klassenRepository.findById(idKlasse)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, meldungNichtGefunden.formatted(idKlasse)));
		if (!Objects.equals(klasse.Schuljahresabschnitts_ID, idErwarteterAbschnitt)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, meldungFalscherAbschnitt);
		}
		return klasse.Klasse;
	}

	/**
	 * Bestimmt den Schuljahresabschnitt, in dem eine Klasse angelegt werden soll.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return der Schuljahresabschnitt
	 */
	private Schuljahresabschnitt getAbschnittFuerNeueKlasse(final Long idSchuljahresabschnitt) {
		return schuljahresabschnittService.findById(idSchuljahresabschnitt)
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST, ABSCHNITT_UNGUELTIG_BEIM_ANLEGEN.formatted(idSchuljahresabschnitt)));
	}

	/**
	 * Bestimmt die Klasse zu der ID.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Klasse
	 */
	private DTOKlassen getDtoById(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ID_NICHT_NULL);
		}
		return klassenRepository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, KLASSE_NICHT_GEFUNDEN.formatted(id)));
	}

	/**
	 * Bestimmt die Klassen zu den übergebenen IDs. Die Klassen werden in der Reihenfolge der IDs zurückgegeben,
	 * wobei doppelte IDs nur einmal berücksichtigt werden.
	 *
	 * @param ids   die IDs der Klassen
	 *
	 * @return die Klassen, bzw. eine leere Liste, wenn keine IDs übergeben wurden
	 */
	private List<DTOKlassen> getDtosByIds(final List<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, IDS_NICHT_NULL);
		}

		final var idsDistinct = new LinkedHashSet<>(ids);
		if (idsDistinct.isEmpty()) {
			return Collections.emptyList();
		}

		final Map<Long, DTOKlassen> klassen = klassenRepository.findMapByIds(idsDistinct);
		if (!klassen.keySet().containsAll(idsDistinct)) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, KLASSEN_NICHT_ALLE_GEFUNDEN);
		}

		return idsDistinct.stream()
				.map(klassen::get)
				.toList();
	}

	/**
	 * Konvertiert die Klassen in die Core-DTOs, wobei die benötigten Daten für alle Klassen gemeinsam geladen werden.
	 *
	 * @param klassen       die Klassen
	 * @param mitSchueler   gibt an, ob die Schüler der Klassen mitgeliefert werden sollen
	 *
	 * @return die Core-DTOs in der Reihenfolge der übergebenen Klassen
	 */
	private List<KlassenDaten> toKlassenDaten(final List<DTOKlassen> klassen, final boolean mitSchueler) {
		final Map<Long, KlassenMappingContext> kontexte = kontextLoader.load(klassen, mitSchueler);
		return klassen.stream()
				.map(klasse -> klassenMapper.toApi(klasse, kontexte.get(klasse.ID)))
				.toList();
	}

}
