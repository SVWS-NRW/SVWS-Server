package de.svws_nrw.service.schule.katalog.fachklasse;

import java.util.Comparator;
import java.util.List;

import de.svws_nrw.asd.types.schule.DQRNiveau;
import de.svws_nrw.asd.types.schule.Fachklasse;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.FachklasseEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.fachklasse.FachklasseMapper;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import jakarta.ws.rs.core.Response;

/**
 * Service Klasse für die Verwaltung von Fachklassen
 */
public final class FachklasseService {

	private static final String KUERZEL_WIRD_BEREITS_VERWENDET = "Das Kürzel %s wird bereits verwendet.";

	private final FachklasseRepository repo;
	private final FachklasseMapper mapper;
	private final EigeneSchuleService eigeneSchuleService;


	/**
	 * @param repo {@link FachklasseRepository}
	 * @param mapper {@link FachklasseMapper}
	 * @param eigeneSchuleService {@link EigeneSchuleService}
	 */
	public FachklasseService(final FachklasseRepository repo, final FachklasseMapper mapper, final EigeneSchuleService eigeneSchuleService) {
		this.repo = repo;
		this.mapper = mapper;
		this.eigeneSchuleService = eigeneSchuleService;
	}

	/**
	 * Gibt alle Fachklassen des schulinternen Katalogs zurück.
	 *
	 * @return Liste aller {@link FachklasseEintrag}-DTOs, sortiert nach Datenbankreihenfolge
	 */
	public List<FachklasseEintrag> getAll() {
		final var schuljahr = eigeneSchuleService.getSchuljahr();

		final var entities = repo.getAll();
		final var ids = entities.stream()
				.map(e -> e.id)
				.toList();
		final var referencedIds = repo.getReferencedIds(ids);

		return entities.stream()
				.map(e -> {
					final var eintrag = mapper.toApi(e, schuljahr);
					eintrag.referenziertInAnderenTabellen = referencedIds.contains(e.id);
					return eintrag;
				})
				.toList();
	}

	/**
	 * Legt eine neue Fachklasse im schulinternen Katalog an.
	 * Validiert vor dem Anlegen die Eindeutigkeit von Bezeichnung und Kürzel
	 * sowie die Existenz der referenzierten {@code idFachklasse} im CoreType-Katalog.
	 *
	 * @param dto der {@link FachklasseEintragCreateRequest} mit den Pflichtfeldern
	 * @return der {@link FachklasseEintrag} der neu angelegten Fachklasse
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn Bezeichnung oder Kürzel
	 *                               bereits vergeben sind oder die {@code idFachklasse} unbekannt ist
	 */
	public FachklasseEintrag create(final FachklasseEintragCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(dto);
			final var fachklasse = mapper.toDomain(dto);
			final var created = repo.create(fachklasse);
			final var schuljahr = eigeneSchuleService.getSchuljahr();
			return mapper.toApi(created, schuljahr);
		});
	}

	/**
	 * Aktualisiert eine bestehende Fachklasse partiell anhand der im {@link FachklasseEintragPatchRequest}
	 * gesetzten Felder. Felder mit {@code undefined}-Wert bleiben unverändert.
	 * Ist {@code idFachklasse} gesetzt, werden die CoreType-abhängigen Felder neu aufgelöst.
	 *
	 * @param id  die ID der zu aktualisierenden Fachklasse
	 * @param dto der {@link FachklasseEintragPatchRequest} mit den zu ändernden Feldern
	 * @return der aktualisierte {@link FachklasseEintrag}
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND} wenn keine Fachklasse zur {@code id} existiert,
	 *                               mit {@code 400 BAD_REQUEST} wenn Bezeichnung oder Kürzel bereits vergeben sind
	 *                               oder die {@code idFachklasse} unbekannt ist
	 */
	public FachklasseEintrag patch(final long id, final FachklasseEintragPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = repo.getById(id);
			validatePatch(dto, id);
			mapper.patch(dto, entity);
			final var schuljahr = eigeneSchuleService.getSchuljahr();
			return this.mapper.toApi(entity, schuljahr);
		});
	}

	/**
	 * Löscht die Fachklassen mit den angegebenen IDs.
	 * Referenzierte Fachklassen werden nicht gelöscht, sondern mit einer Fehlermeldung markiert.
	 * Nicht gefundene IDs werden als Fehler zurückgegeben.
	 *
	 * @param idsToDelete Liste der zu löschenden Fachklassen-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() -> {
			final var referencedIds = repo.getReferencedIds(idsToDelete);
			final var entitiesToDelete = repo.findListByIds(idsToDelete);

			final var foundIds = entitiesToDelete.stream().map(e -> e.id).toList();

			final var unreferenced = entitiesToDelete.stream()
					.filter(e -> !referencedIds.contains(e.id))
					.toList();

			final var deletedIds = repo.delete(unreferenced).stream().map(e -> e.id).toList();

			return idsToDelete.stream()
					.map(id -> {
						if (!foundIds.contains(id)) {
							return SimpleOperationResponse.ofError(id, "Ort mit ID %d wurde nicht gefunden.".formatted(id));
						}
						if (referencedIds.contains(id)) {
							return SimpleOperationResponse.ofError(id, "Ort mit ID %d ist referenziert und kann nicht gelöscht werden.".formatted(id));
						}
						if (!deletedIds.contains(id)) {
							return SimpleOperationResponse.ofError(id, "Ort mit ID %d konnte nicht gelöscht werden.".formatted(id));
						}
						return SimpleOperationResponse.ofSuccess(id);
					})
					.sorted(Comparator.comparingLong(r -> r.id))
					.toList();
		});
	}

	private void validateCreate(final FachklasseEintragCreateRequest dto) {
		validateUniqueKuerzel(dto.kuerzel, null);
		validateIdFachklasse(dto.idFachklasse);
		validateIdDqrNiveau(dto.idDqrNiveau);
	}

	private void validatePatch(final FachklasseEintragPatchRequest dto, final long id) {
		dto.kuerzel.ifPresent(kuerzel -> validateUniqueKuerzel(kuerzel, id));
		dto.idFachklasse.ifPresent(this::validateIdFachklasse);
		dto.idDqrNiveau.ifPresent(this::validateIdDqrNiveau);
	}

	private void validateIdFachklasse(final Long idFachklasse) {
		if (Fachklasse.data().getEintragByID(idFachklasse) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Keine Fachklasse für die id %d gefunden".formatted(idFachklasse));
		}
	}

	private void validateIdDqrNiveau(final Integer idDqrNiveau) {
		if (idDqrNiveau == null) {
			return;
		}
		if (DQRNiveau.data().getEintragByID(Long.valueOf(idDqrNiveau)) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein DQR-Niveau für die id %d gefunden".formatted(idDqrNiveau));
		}
	}

	private void validateUniqueKuerzel(final String kuerzel, final Long idToExclude) {
		final var exists = (idToExclude == null)
				? repo.kuerzelIsAlreadyUsedCreate(kuerzel)
				: repo.kuerzelIsAlreadyUsedPatch(kuerzel, idToExclude);

		if (exists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KUERZEL_WIRD_BEREITS_VERWENDET.formatted(kuerzel));
		}
	}

}
