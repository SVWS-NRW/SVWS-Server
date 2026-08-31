package de.svws_nrw.service.schule.katalog.ankreuzkompetenz;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangMapper;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzJahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

public class AnkreuzkompetenzJahrgangService {

	private final AnkreuzkompetenzJahrgangRepository repository;
	private final AnkreuzkompetenzRepository ankreuzkompetenzRepository;
	private final JahrgangRepository jahrgangRepository;

	private final AnkreuzkompetenzJahrgangMapper mapper;

	/**
	 * constructor
	 *
	 * @param repository {@link AnkreuzkompetenzJahrgangRepository}
	 * @param ankreuzkompetenzRepository {@link AnkreuzkompetenzRepository}
	 * @param jahrgangRepository {@link JahrgangRepository}
	 * @param mapper {@link AnkreuzkompetenzJahrgangMapper}
	 */
	public AnkreuzkompetenzJahrgangService(
			final AnkreuzkompetenzJahrgangRepository repository,
			final AnkreuzkompetenzRepository ankreuzkompetenzRepository,
			final JahrgangRepository jahrgangRepository,
			final AnkreuzkompetenzJahrgangMapper mapper) {
		this.repository = repository;
		this.ankreuzkompetenzRepository = ankreuzkompetenzRepository;
		this.jahrgangRepository = jahrgangRepository;
		this.mapper = mapper;
	}

	/**
	 * Gibt eine Map aller AnkreuzkompetenzJahrgangszuordnungen nach ID aus dem Schulkatalog zurück.
	 *
	 * @return Map aller AnkreuzkompetenzJahrgangszuordnungen nach ID aus dem Schulkatalog.
	 */
	public Map<Long, List<AnkreuzkompetenzJahrgangszuordnung>> getAllByIdAnkreuzkompetenz() {
		return repository.getAll().stream()
				.map(mapper::toApi)
				.collect(Collectors.groupingBy(a -> a.idAnkreuzkompetenz));
	}

	/**
	 * Erstellt mehrere neue {@link AnkreuzkompetenzJahrgangszuordnung}.
	 *
	 * @param dtos die Daten für die neuen Einträge
	 *
	 * @return die erstellten AnkreuzkompetenzJahrgangszuordnung als API-Modelle
	 */
	public List<AnkreuzkompetenzJahrgangszuordnung> createMultiple(final List<AnkreuzkompetenzJahrgangCreateRequest> dtos) {
		return TransactionSupport.transactional(() -> {
			final var entities = dtos.stream()
					.map(dto -> {
						validateCreate(dto);
						return mapper.toDomain(dto);
					})
					.toList();
			final var created = this.repository.create(entities);
			return created.stream()
					.map(mapper::toApi)
					.toList();
		});
	}

	private void validateCreate(final AnkreuzkompetenzJahrgangCreateRequest dto) {
		if (!ankreuzkompetenzRepository.existsById(dto.idAnkreuzkompetenz)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Keine Ankreuzkompetenz für die ID %d gefunden".formatted(dto.idAnkreuzkompetenz));
		}
		if (!jahrgangRepository.existsById(dto.idJahrgang)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Jahrgang für die ID %d gefunden".formatted(dto.idJahrgang));
		}
	}

	/**
	 * Löscht die AnkreuzkompetenzJahrgangszuordnungen mit den angegebenen IDs.
	 * Nicht gefundene IDs werden stillschweigend ignoriert.
	 * Jeder Eintrag in der Rückgabeliste enthält die ID und ob die Löschung erfolgreich war.
	 *
	 * @param idsToDelete Liste der zu löschenden AnkreuzkompetenzJahrgangszuordnung-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(
						idsToDelete,
						repository,
						e -> e.id,
						"AnkreuzkompetenzJahrgangzuordnung"
				)
		);
	}

}
