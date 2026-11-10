package de.svws_nrw.service.lehrer.stammdaten;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.stammdaten.LehrerStammdatenMapper;
import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.service.lehrer.einwilligung.LehrerEinwilligungService;
import de.svws_nrw.service.lehrer.foto.LehrerFoto;
import de.svws_nrw.service.lehrer.foto.LehrerFotoService;
import de.svws_nrw.service.lehrer.lernplattform.LehrerLernplattformService;
import jakarta.ws.rs.core.Response;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Service für den Zugriff auf die Stammdaten von Lehrkräften und deren Änderung.
 *
 * <p>Der Service übernimmt insbesondere:</p>
 * <ul>
 *     <li>das Laden der Stammdaten einzelner, mehrerer oder aller Lehrkräfte (jeweils mit oder ohne Foto),</li>
 *     <li>das Anlegen einer Lehrkraft inklusive der Initialisierung der Lernplattformen und Einwilligungen,</li>
 *     <li>das partielle Aktualisieren der Stammdaten einer Lehrkraft,</li>
 *     <li>die Validierung von Kürzel, Personal-Typ, Geschlecht, Staatsangehörigkeit sowie Wohnort und Ortsteil.</li>
 * </ul>
 */
public class LehrerStammdatenService {

	private static final String LEHRER_NOT_FOUND = "Keine Lehrkraft mit der ID %d gefunden.";
	private static final String KUERZEL_EXISTS = "Das Kuerzel %s ist bereits vorhanden.";
	private static final String PERSONAL_TYP_NOT_FOUND = "Ein PersonalTyp mit dem Kuerzel %s wurde nicht gefunden.";
	private static final String GESCHLECHT_NOT_FOUND = "Kein Geschlecht mit dem Wert %s vorhanden.";
	private static final String NATIONALITAET_NOT_FOUND = "keine Nationalität zur ID %s vorhanden.";
	private static final String WOHNORT_INVALID = "WohnortID %d ungültig.";
	private static final String ORTSTEIL_INVALID = "OrtsteilID %d ungültig.";

	private final LehrerStammdatenRepositories repositories;
	private final LehrerFotoService fotoService;
	private final LehrerLernplattformService lernplattformService;
	private final LehrerEinwilligungService einwilligungService;
	private final LehrerStammdatenMapper mapper;
	private final SchulleitungMapper schulleitungMapper;

	/**
	 * Erstellt einen neuen Service für Lehrer-Stammdaten.
	 *
	 * @param repositories           die benötigten Repositories
	 * @param fotoService            der Service für die Lehrer-Fotos
	 * @param lernplattformService   der Service für die Lernplattform-Zuordnungen der Lehrer
	 * @param einwilligungService    der Service für die Einwilligungen der Lehrer
	 * @param mapper                 der Mapper für die Abbildung der Stammdaten
	 * @param schulleitungMapper     der Mapper für die Abbildung der Leitungsfunktionen
	 */
	public LehrerStammdatenService(
			final LehrerStammdatenRepositories repositories,
			final LehrerFotoService fotoService,
			final LehrerLernplattformService lernplattformService,
			final LehrerEinwilligungService einwilligungService,
			final LehrerStammdatenMapper mapper,
			final SchulleitungMapper schulleitungMapper
	) {
		this.repositories = repositories;
		this.fotoService = fotoService;
		this.lernplattformService = lernplattformService;
		this.einwilligungService = einwilligungService;
		this.mapper = mapper;
		this.schulleitungMapper = schulleitungMapper;
	}

	/**
	 * Lädt die Stammdaten einer Lehrkraft anhand ihrer ID.
	 *
	 * @param id   die ID der Lehrkraft
	 *
	 * @return die Stammdaten der Lehrkraft inklusive Foto
	 */
	public LehrerStammdaten getById(final long id) {
		return toApiMitFotos(List.of(loadLehrer(id))).getFirst();
	}

	/**
	 * Lädt die Stammdaten der Lehrkräfte zu den übergebenen IDs inklusive Foto.
	 *
	 * @param ids   die IDs der Lehrkräfte
	 *
	 * @return die Stammdaten der Lehrkräfte, bei {@code null} oder einer leeren ID-Liste eine leere Liste
	 */
	public List<LehrerStammdaten> getList(final List<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptyList();
		}

		return toApiMitFotos(repositories.lehrerRepository().findListByIds(ids));
	}

	/**
	 * Lädt die Stammdaten der Lehrkräfte zu den übergebenen IDs ohne Foto.
	 *
	 * @param ids   die IDs der Lehrkräfte
	 *
	 * @return die Stammdaten der Lehrkräfte ohne Foto, bei {@code null} oder einer leeren ID-Liste eine leere Liste
	 */
	public List<LehrerStammdaten> getListOhneFotos(final List<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptyList();
		}

		return toApiOhneFotos(repositories.lehrerRepository().findListByIds(ids));
	}

	/**
	 * Lädt die Stammdaten aller Lehrkräfte inklusive Foto.
	 *
	 * @return die Stammdaten aller Lehrkräfte
	 */
	public List<LehrerStammdaten> getAll() {
		return toApiMitFotos(repositories.lehrerRepository().getAll());
	}

	/**
	 * Lädt die Stammdaten aller Lehrkräfte ohne Foto.
	 *
	 * @return die Stammdaten aller Lehrkräfte ohne Foto
	 */
	public List<LehrerStammdaten> getAllOhneFotos() {
		return toApiOhneFotos(repositories.lehrerRepository().getAll());
	}

	/**
	 * Legt eine neue Lehrkraft an und initialisiert die Lernplattformen sowie die Einwilligungen für die
	 * Einwilligungsarten der Personenart Lehrer.
	 *
	 * @param request   der Request mit den Daten der neuen Lehrkraft
	 *
	 * @return die Stammdaten der neu angelegten Lehrkraft
	 */
	public LehrerStammdaten create(final LehrerStammdatenCreateRequest request) {
		return TransactionSupport.transactional(() -> {
			validateCreate(request);
			final var lehrer = mapper.toDomain(request);
			applyWohnortAndOrtsteil(lehrer, JsonNullable.of(request.wohnortID), JsonNullable.of(request.ortsteilID));
			final var created = repositories.lehrerRepository().create(lehrer);
			lernplattformService.addForLehrer(created.ID);
			einwilligungService.addForLehrer(created.ID);

			return toApiMitFotos(List.of(created)).getFirst();
		});
	}

	/**
	 * Aktualisiert die Stammdaten einer Lehrkraft partiell. Nicht im Request angegebene Werte bleiben unverändert.
	 *
	 * @param id        die ID der zu aktualisierenden Lehrkraft
	 * @param request   der Request mit den zu ändernden Werten
	 *
	 * @return die aktualisierten Stammdaten der Lehrkraft
	 */
	public LehrerStammdaten patch(final long id, final LehrerStammdatenPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final var lehrer = loadLehrer(id);
			validatePatch(id, request);
			mapper.patch(request, lehrer);
			applyWohnortAndOrtsteil(lehrer, request.wohnortID, request.ortsteilID);
			request.foto.ifPresent(foto -> fotoService.upsertOrDelete(id, foto));

			return toApiMitFotos(List.of(lehrer)).getFirst();
		});
	}

	private DTOLehrer loadLehrer(final long id) {
		return repositories.lehrerRepository().findById(id)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, LEHRER_NOT_FOUND.formatted(id)));
	}

	// Wohnort und Ortsteil

	/**
	 * Setzt Wohnort und Ortsteil der Lehrkraft. Ist nur einer der beiden Werte im Request enthalten, wird für den anderen
	 * der Wert der Lehrkraft verwendet. Ein Ortsteil, der nicht zum Wohnort passt, wird auf {@code null} gesetzt.
	 *
	 * @param lehrer       die zu ändernde Lehrkraft
	 * @param wohnortID    die ID des Wohnortes aus dem Request, nicht gesetzt (undefined), wenn der Wohnort unverändert bleibt
	 * @param ortsteilID   die ID des Ortsteils aus dem Request, nicht gesetzt (undefined), wenn der Ortsteil unverändert bleibt
	 */
	private void applyWohnortAndOrtsteil(final DTOLehrer lehrer, final JsonNullable<Long> wohnortID, final JsonNullable<Long> ortsteilID) {
		if (!wohnortID.isPresent() && !ortsteilID.isPresent()) {
			return;
		}
		final var wohnort = new Wohnort(
				wohnortID.isPresent() ? wohnortID.get() : lehrer.Ort_ID,
				ortsteilID.isPresent() ? ortsteilID.get() : lehrer.Ortsteil_ID);
		validateWohnort(wohnort);
		lehrer.Ort_ID = wohnort.idOrt();
		lehrer.Ortsteil_ID = belongsOrtsteilToOrt(wohnort) ? wohnort.idOrtsteil() : null;
	}

	private void validateWohnort(final Wohnort wohnort) {
		final var idOrt = wohnort.idOrt();
		final var idOrtsteil = wohnort.idOrtsteil();
		if ((idOrt != null) && ((idOrt < 0) || !repositories.ortRepository().existsById(idOrt))) {
			throw new ApiOperationException(Response.Status.CONFLICT, WOHNORT_INVALID.formatted(idOrt));
		}
		if ((idOrtsteil != null) && (idOrtsteil < 0)) {
			throw new ApiOperationException(Response.Status.CONFLICT, ORTSTEIL_INVALID.formatted(idOrtsteil));
		}
	}

	private boolean belongsOrtsteilToOrt(final Wohnort wohnort) {
		if ((wohnort.idOrtsteil() == null) || (wohnort.idOrt() == null)) {
			return false;
		}

		return repositories.ortsteilRepository().findById(wohnort.idOrtsteil())
				.map(ortsteil -> wohnort.idOrt().equals(ortsteil.idOrt))
				.orElse(false);
	}

	// Validierung

	private void validateCreate(final LehrerStammdatenCreateRequest request) {
		validateKuerzelExists(request.kuerzel, null);
		validatePersonalTyp(request.personalTyp);
		validateGeschlecht(request.geschlecht);
		validateStaatsangehoerigkeit(request.idStaatsangehoerigkeit);
	}

	private void validatePatch(final long id, final LehrerStammdatenPatchRequest request) {
		request.kuerzel.ifPresent(kuerzel -> validateKuerzelExists(kuerzel, id));
		request.personalTyp.ifPresent(this::validatePersonalTyp);
		request.geschlecht.ifPresent(this::validateGeschlecht);
		request.idStaatsangehoerigkeit.ifPresent(this::validateStaatsangehoerigkeit);
	}

	private void validateKuerzelExists(final String kuerzel, final Long excludingId) {
		final var lehrerRepository = repositories.lehrerRepository();
		final var kuerzelExists = (excludingId == null)
				? lehrerRepository.existsByKuerzel(kuerzel)
				: lehrerRepository.existsByKuerzelExcludingId(kuerzel, excludingId);
		if (kuerzelExists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KUERZEL_EXISTS.formatted(kuerzel));
		}
	}

	private void validatePersonalTyp(final String kuerzel) {
		if (PersonalTyp.fromKuerzel(kuerzel) == null) {
			throw new ApiOperationException(Response.Status.CONFLICT, PERSONAL_TYP_NOT_FOUND.formatted(kuerzel));
		}
	}

	private void validateGeschlecht(final Integer idGeschlecht) {
		if (Geschlecht.fromValue(idGeschlecht) == null) {
			throw new ApiOperationException(Response.Status.CONFLICT, GESCHLECHT_NOT_FOUND.formatted(idGeschlecht));
		}
	}

	private void validateStaatsangehoerigkeit(final Long idStaatsangehoerigkeit) {
		if ((idStaatsangehoerigkeit != null) && (Nationalitaeten.data().getWertByIDOrNull(idStaatsangehoerigkeit) == null)) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, NATIONALITAET_NOT_FOUND.formatted(idStaatsangehoerigkeit));
		}
	}

	// Mapping

	private List<LehrerStammdaten> toApiOhneFotos(final List<DTOLehrer> lehrer) {
		return lehrer.stream()
				.map(l -> toApi(l, null))
				.toList();
	}

	private List<LehrerStammdaten> toApiMitFotos(final List<DTOLehrer> lehrer) {
		if (lehrer.isEmpty()) {
			return Collections.emptyList();
		}
		final var ids = lehrer.stream()
				.map(l -> l.ID)
				.toList();
		final Map<Long, LehrerFoto> fotosByIdLehrer = fotoService.getByLehrerIds(ids).stream()
				.collect(Collectors.toMap(LehrerFoto::idLehrer, Function.identity()));

		return lehrer.stream()
				.map(l -> toApi(l, fotosByIdLehrer.get(l.ID)))
				.toList();
	}

	private LehrerStammdaten toApi(final DTOLehrer lehrer, final LehrerFoto foto) {
		final var daten = mapper.toApi(lehrer);
		daten.foto = (foto == null) ? null : foto.fotoBase64();
		repositories.schulleitungRepository().getAllByIdLehrer(lehrer.ID).stream()
				.map(schulleitungMapper::toApi)
				.forEach(daten.leitungsfunktionen::add);

		return daten;
	}

	/**
	 * Wohnort und Ortsteil einer Lehrkraft, die gemeinsam validiert werden.
	 *
	 * @param idOrt        die ID des Wohnortes
	 * @param idOrtsteil   die ID des Ortsteils
	 */
	private record Wohnort(Long idOrt, Long idOrtsteil) { }

}
