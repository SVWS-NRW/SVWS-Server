package de.svws_nrw.service.lehrer.lernplattform;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.lehrer.LehrerLernplattform;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattformPK;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.lernplattform.LehrerLernplattformMapper;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.lernplattform.LehrerLernplattformRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformCredentialRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dieser Service bündelt die Fachlogik für die Zuordnungen von Lehrern zu Lernplattformen
 * (inklusive der Einwilligungen).
 */
public final class LehrerLernplattformService {

	private final LehrerLernplattformRepository repository;
	private final LehrerRepository lehrerRepository;
	private final LernplattformRepository lernplattformRepository;
	private final LernplattformCredentialRepository credentialRepository;
	private final LehrerLernplattformMapper mapper;

	/**
	 * Erstellt einen neuen {@link LehrerLernplattformService}.
	 *
	 * @param repository                das Repository für die Zuordnungen von Lehrern zu Lernplattformen
	 * @param lehrerRepository        das Repository für die Lehrer
	 * @param lernplattformRepository   das Repository für den Lernplattformen-Katalog
	 * @param credentialRepository      das Repository für die Zugangsdaten zu Lernplattformen
	 * @param mapper                    der Mapper zwischen Datenbank-DTO und Core-DTO
	 */
	public LehrerLernplattformService(final LehrerLernplattformRepository repository, final LehrerRepository lehrerRepository,
			final LernplattformRepository lernplattformRepository, final LernplattformCredentialRepository credentialRepository,
			final LehrerLernplattformMapper mapper) {
		this.repository = repository;
		this.lehrerRepository = lehrerRepository;
		this.lernplattformRepository = lernplattformRepository;
		this.credentialRepository = credentialRepository;
		this.mapper = mapper;
	}

	/**
	 * Bestimmt alle Lernplattform-Zuordnungen eines Lehrers.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Zuordnungen des Lehrers, leer wenn keine vorhanden sind
	 */
	public List<LehrerLernplattform> getListByIdLehrer(final long idLehrer) {
		return toApi(repository.getListByIdLehrer(idLehrer));
	}

	/**
	 * Aktualisiert die Einwilligungen einer Zuordnung teilweise (PATCH). Nur die im Request gesetzten Felder werden
	 * übernommen. Die Operation läuft in einer Transaktion.
	 *
	 * @param idLehrer        die ID des Lehrers
	 * @param idLernplattform   die ID der Lernplattform
	 * @param request           die zu ändernden Felder
	 *
	 * @return die aktualisierte Zuordnung
	 *
	 * @throws ApiOperationException wenn keine Zuordnung für die Kombination aus Lehrer und Lernplattform existiert
	 */
	public LehrerLernplattform patch(final long idLehrer, final long idLernplattform, final LehrerLernplattformPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final DTOLehrerLernplattform entity = getEntityOrThrow(idLehrer, idLernplattform);
			mapper.patch(request, entity);
			return toApi(List.of(entity)).getFirst();
		});
	}

	/**
	 * Legt für einen Lehrer zu jeder Lernplattform des Katalogs eine Zuordnung ohne Einwilligungen an. Bereits vorhandene
	 * Zuordnungen bleiben unverändert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die neu angelegten Zuordnungen, leer wenn der Lehrer bereits zu allen Lernplattformen zugeordnet ist
	 *
	 * @throws ApiOperationException wenn der Lehrer nicht existiert
	 */
	public List<LehrerLernplattform> addForLehrer(final long idLehrer) {
		return TransactionSupport.transactional(() -> {
			assertLehrerExists(idLehrer);
			final List<Long> idsFehlend = filterMissing(lernplattformRepository.getAllIds(), getIdsLernplattformenOfLehrer(idLehrer));
			return createAll(idsFehlend.stream().map(idLernplattform -> mapper.toDomain(idLehrer, idLernplattform)).toList());
		});
	}

	/**
	 * Legt für eine Lernplattform zu jedem Lehrer eine Zuordnung ohne Einwilligungen an. Bereits vorhandene
	 * Zuordnungen bleiben unverändert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idLernplattform   die ID der Lernplattform
	 *
	 * @return die neu angelegten Zuordnungen, leer wenn alle Lehrer bereits der Lernplattform zugeordnet sind
	 *
	 * @throws ApiOperationException wenn die Lernplattform nicht existiert
	 */
	public List<LehrerLernplattform> addForLernplattform(final long idLernplattform) {
		return TransactionSupport.transactional(() -> {
			assertLernplattformExists(idLernplattform);
			final List<Long> idsFehlend = filterMissing(lehrerRepository.getAllIds(), getIdsLehrerOfLernplattform(idLernplattform));
			return createAll(idsFehlend.stream().map(idLehrer -> mapper.toDomain(idLehrer, idLernplattform)).toList());
		});
	}

	private DTOLehrerLernplattform getEntityOrThrow(final long idLehrer, final long idLernplattform) {
		return repository.findById(new DTOLehrerLernplattformPK(idLehrer, idLernplattform))
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND,
						"Eine Lernplattform mit LehrerID %d und der LernplattformID %d wurde nicht gefunden.".formatted(idLehrer, idLernplattform)));
	}

	private void assertLehrerExists(final long idLehrer) {
		if (!lehrerRepository.existsById(idLehrer)) {
			throw new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d wurde nicht gefunden.".formatted(idLehrer));
		}
	}

	private void assertLernplattformExists(final long idLernplattform) {
		if (!lernplattformRepository.existsById(idLernplattform)) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Lernplattform mit der ID %d wurde nicht gefunden.".formatted(idLernplattform));
		}
	}

	private Set<Long> getIdsLernplattformenOfLehrer(final long idLehrer) {
		return repository.getListByIdLehrer(idLehrer).stream().map(e -> e.LernplattformID).collect(Collectors.toSet());
	}

	private Set<Long> getIdsLehrerOfLernplattform(final long idLernplattform) {
		return repository.getListByIdLernplattform(idLernplattform).stream().map(e -> e.LehrerID).collect(Collectors.toSet());
	}

	private static List<Long> filterMissing(final Collection<Long> idsAll, final Set<Long> idsExisting) {
		return idsAll.stream().filter(id -> !idsExisting.contains(id)).toList();
	}

	private List<LehrerLernplattform> createAll(final List<DTOLehrerLernplattform> entities) {
		if (entities.isEmpty()) {
			return List.of();
		}
		repository.create(entities);
		return entities.stream().map(e -> mapper.toApi(e, null)).toList();
	}

	private List<LehrerLernplattform> toApi(final List<DTOLehrerLernplattform> entities) {
		final Map<Long, DTOCredentialsLernplattformen> credentials = getCredentialsOf(entities);
		return entities.stream()
				.map(e -> mapper.toApi(e, (e.CredentialID == null) ? null : credentials.get(e.CredentialID)))
				.toList();
	}

	private Map<Long, DTOCredentialsLernplattformen> getCredentialsOf(final List<DTOLehrerLernplattform> entities) {
		final Set<Long> idsCredentials = entities.stream().map(e -> e.CredentialID).filter(Objects::nonNull).collect(Collectors.toSet());
		return credentialRepository.findMapByIds(idsCredentials);
	}

}
