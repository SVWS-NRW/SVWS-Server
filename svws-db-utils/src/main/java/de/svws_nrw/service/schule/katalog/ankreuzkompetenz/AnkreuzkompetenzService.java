package de.svws_nrw.service.schule.katalog.ankreuzkompetenz;

import java.util.Comparator;
import java.util.List;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Ankreuzkompetenz;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzkompetenz;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzMapper;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für die Verwaltung des Katalogs der Ankreuzkompetenzen.
 * <p>
 * Stellt die Operationen zum Lesen, Anlegen, Ändern und Löschen bereit und führt die fachlichen Validierungen durch.
 */
public class AnkreuzkompetenzService {

	private static final String ASV_MIT_FACH = "Einer ASV-Ankreuzkompetenz kann kein Fach zugeordnet werden.";
	private static final String FACH_ERFORDERLICH = "Für diese Ankreuzkompetenz muss ein Fach ausgewählt werden.";
	private static final String FACH_NICHT_GEFUNDEN = "Kein Fach mit der ID %d gefunden.";
	private static final String KEINE_SCHULGLIEDERUNG_GEFUNDEN = "Keine Schulgliederung mit dem Schlüssel %s gefunden.";
	private static final String SCHULGLIEDERUNG_NICHT_GUELTIG = "Die Schulgliederung ist für diese Schulform nicht gültig.";
	private static final String FLOSKELTEXT_MIT_FACH_VERWENDET = "Der Floskeltext %s wird für die Fach-ID %d bereits verwendet.";
	private static final String FLOSKELTEXT_OHNE_FACH_VERWENDET = "Der Floskeltext %s wird bereits für Ankreuzkompetenzen ohne Fach verwendet.";
	private static final String ANKREUZKOMPETENZ_NICHT_GEFUNDEN = "Keine Ankreuzkompetenz mit der ID %d gefunden.";

	private final AnkreuzkompetenzRepository repository;
	private final FachRepository fachRepository;
	private final AnkreuzkompetenzJahrgangService ankreuzkompetenzJahrgangService;
	private final EigeneSchuleService eigeneSchuleService;
	private final AnkreuzkompetenzMapper mapper;

	/**
	 * Erstellt einen neuen AnkreuzkompetenzService.
	 *
	 * @param repository                      das Repository für den Datenbankzugriff auf die Ankreuzkompetenzen
	 * @param fachRepository                  das Repository für den Datenbankzugriff auf die Fächer
	 * @param ankreuzkompetenzJahrgangService der Service für die Jahrgangszuordnungen der Ankreuzkompetenzen
	 * @param eigeneSchuleService             der Service für die Daten der eigenen Schule
	 * @param mapper                          der Mapper zur Konvertierung zwischen Datenbank- und API-Modell
	 */
	public AnkreuzkompetenzService(
			final AnkreuzkompetenzRepository repository,
			final FachRepository fachRepository,
			final AnkreuzkompetenzJahrgangService ankreuzkompetenzJahrgangService,
			final EigeneSchuleService eigeneSchuleService,
			final AnkreuzkompetenzMapper mapper
	) {
		this.repository = repository;
		this.fachRepository = fachRepository;
		this.ankreuzkompetenzJahrgangService = ankreuzkompetenzJahrgangService;
		this.eigeneSchuleService = eigeneSchuleService;
		this.mapper = mapper;
	}

	/**
	 * Gibt alle Ankreuzkompetenzen aufsteigend nach ID sortiert zurück, jeweils mit ihren Jahrgangszuordnungen
	 * und der Information, ob sie bereits bei Schülern verwendet werden.
	 *
	 * @return die Liste aller Ankreuzkompetenzen
	 */
	public List<Ankreuzkompetenz> getAll() {
		final var entities = repository.getAll().stream()
				.sorted(Comparator.comparingLong(e -> e.id))
				.toList();
		final var referencedIds = repository.getReferencedIds(entities.stream().map(e -> e.id).toList());
		final var zuordnungen = ankreuzkompetenzJahrgangService.getAllByIdAnkreuzkompetenz();

		return entities.stream()
				.map(e -> toApi(e, referencedIds.contains(e.id), zuordnungen.get(e.id)))
				.toList();
	}

	/**
	 * Legt eine neue Ankreuzkompetenz an. Die Regeln werden anhand der Werte des Requests geprüft.
	 *
	 * @param dto der {@link AnkreuzkompetenzCreateRequest} mit den Daten der neuen Ankreuzkompetenz
	 *
	 * @return die neu angelegte Ankreuzkompetenz
	 *
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST}, wenn ASV und Fach sich widersprechen oder der Floskeltext für das Fach bereits
	 *                               verwendet wird, mit {@code 404 NOT_FOUND}, wenn das Fach oder die Schulgliederung unbekannt ist,
	 *                               mit {@code 409 CONFLICT}, wenn die Schulgliederung für die Schulform nicht gültig ist
	 */
	public Ankreuzkompetenz create(final AnkreuzkompetenzCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			validateCreate(dto);
			final var created = repository.create(mapper.toDomain(dto));
			return toApi(created, false, null);
		});
	}

	/**
	 * Ändert eine Ankreuzkompetenz teilweise anhand der im {@link AnkreuzkompetenzPatchRequest} gesetzten Felder.
	 * Die Regeln werden am Endzustand der Ankreuzkompetenz geprüft.
	 *
	 * @param id  die ID der zu ändernden Ankreuzkompetenz
	 * @param dto der {@link AnkreuzkompetenzPatchRequest} mit den zu ändernden Feldern
	 *
	 * @return die geänderte Ankreuzkompetenz
	 *
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND}, wenn die Ankreuzkompetenz, das Fach oder die Schulgliederung unbekannt ist,
	 *                               mit {@code 400 BAD_REQUEST}, wenn ASV und Fach sich widersprechen oder der Floskeltext für das Fach bereits
	 *                               verwendet wird, mit {@code 409 CONFLICT}, wenn die Schulgliederung für die Schulform nicht gültig ist
	 */
	public Ankreuzkompetenz patch(final long id, final AnkreuzkompetenzPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, ANKREUZKOMPETENZ_NICHT_GEFUNDEN.formatted(id)));
			validatePatch(entity, dto);
			mapper.patch(dto, entity);
			final var referenziert = !repository.getReferencedIds(List.of(id)).isEmpty();
			return toApi(entity, referenziert, ankreuzkompetenzJahrgangService.getByIdAnkreuzkompetenz(id));
		});
	}

	/**
	 * Löscht die Ankreuzkompetenzen mit den angegebenen IDs. Ankreuzkompetenzen, die bereits bei Schülern verwendet werden,
	 * werden nicht gelöscht, sondern mit einer Fehlermeldung markiert. Nicht gefundene IDs werden als Fehler zurückgegeben.
	 *
	 * @param idsToDelete die IDs der zu löschenden Ankreuzkompetenzen
	 *
	 * @return die Liste der Ergebnisse je ID, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.deleteWithReferenceCheck(idsToDelete, repository, e -> e.id, "Ankreuzkompetenz"));
	}

	private Ankreuzkompetenz toApi(
			final DTOAnkreuzkompetenz entity,
			final boolean referenziert,
			final List<AnkreuzkompetenzJahrgangszuordnung> zuordnungen
	) {
		final var ankreuzkompetenz = mapper.toApi(entity);
		ankreuzkompetenz.referenziertInAnderenTabellen = referenziert;
		if (zuordnungen != null) {
			ankreuzkompetenz.jahrgaengezuordnung.addAll(zuordnungen);
		}
		return ankreuzkompetenz;
	}

	private void validateCreate(final AnkreuzkompetenzCreateRequest dto) {
		validateAsvUndFach(dto.istASV, dto.idFach);
		validateFachExistiert(dto.idFach);
		validateSchulgliederung(dto.schulgliederung);
		validateFloskelTextEindeutig(dto.floskelText, dto.idFach, null);
	}

	private void validatePatch(final DTOAnkreuzkompetenz entity, final AnkreuzkompetenzPatchRequest dto) {
		final var istASV = dto.istASV.isPresent() ? Boolean.TRUE.equals(dto.istASV.get()) : (entity.istASV == 1);
		final var idFach = dto.idFach.isPresent() ? dto.idFach.get() : entity.idFach;
		final var floskelText = dto.floskelText.isPresent() ? dto.floskelText.get() : entity.floskelText;

		if (dto.istASV.isPresent() || dto.idFach.isPresent()) {
			validateAsvUndFach(istASV, idFach);
		}
		if (dto.idFach.isPresent()) {
			validateFachExistiert(idFach);
		}
		if (dto.schulgliederung.isPresent()) {
			validateSchulgliederung(dto.schulgliederung.get());
		}
		if (dto.floskelText.isPresent() || dto.idFach.isPresent()) {
			validateFloskelTextEindeutig(floskelText, idFach, entity.id);
		}
	}

	private static void validateAsvUndFach(final Boolean istASV, final Long idFach) {
		final var asv = Boolean.TRUE.equals(istASV);
		if (asv && (idFach != null)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ASV_MIT_FACH);
		}
		if (!asv && (idFach == null)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, FACH_ERFORDERLICH);
		}
	}

	private void validateFachExistiert(final Long idFach) {
		if ((idFach != null) && fachRepository.findById(idFach).isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, FACH_NICHT_GEFUNDEN.formatted(idFach));
		}
	}

	private void validateSchulgliederung(final String schluesselSchulgliederung) {
		if (schluesselSchulgliederung == null) {
			return;
		}
		final var schulgliederung = Schulgliederung.data().getWertBySchluessel(schluesselSchulgliederung);
		if (schulgliederung == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, KEINE_SCHULGLIEDERUNG_GEFUNDEN.formatted(schluesselSchulgliederung));
		}
		if (!schulgliederung.hatSchulform(eigeneSchuleService.getSchuljahr(), eigeneSchuleService.getSchulform())) {
			throw new ApiOperationException(Response.Status.CONFLICT, SCHULGLIEDERUNG_NICHT_GUELTIG);
		}
	}

	private void validateFloskelTextEindeutig(final String floskelText, final Long idFach, final Long idToExclude) {
		final var bereitsVergeben = (idToExclude == null)
				? repository.floskelTextIsAlreadyUsedCreate(floskelText, idFach)
				: repository.floskelTextIsAlreadyUsedPatch(floskelText, idFach, idToExclude);
		if (bereitsVergeben) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, (idFach == null)
					? FLOSKELTEXT_OHNE_FACH_VERWENDET.formatted(floskelText)
					: FLOSKELTEXT_MIT_FACH_VERWENDET.formatted(floskelText, idFach));
		}
	}

}
