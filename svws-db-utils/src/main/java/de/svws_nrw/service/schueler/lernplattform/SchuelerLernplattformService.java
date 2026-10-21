package de.svws_nrw.service.schueler.lernplattform;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schueler.SchuelerLernplattform;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattformPK;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.lernplattform.SchuelerLernplattformMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.lernplattform.SchuelerLernplattformRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformCredentialRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dieser Service bündelt die Fachlogik für die Zuordnungen von Schülern zu Lernplattformen
 * (inklusive der Einwilligungen).
 */
public final class SchuelerLernplattformService {

	private final SchuelerLernplattformRepository repository;
	private final SchuelerRepository schuelerRepository;
	private final LernplattformRepository lernplattformRepository;
	private final LernplattformCredentialRepository credentialRepository;
	private final SchuelerLernplattformMapper mapper;

	/**
	 * Erstellt einen neuen {@link SchuelerLernplattformService}.
	 *
	 * @param repository                das Repository für die Zuordnungen von Schülern zu Lernplattformen
	 * @param schuelerRepository        das Repository für die Schüler
	 * @param lernplattformRepository   das Repository für den Lernplattformen-Katalog
	 * @param credentialRepository      das Repository für die Zugangsdaten zu Lernplattformen
	 * @param mapper                    der Mapper zwischen Datenbank-DTO und Core-DTO
	 */
	public SchuelerLernplattformService(final SchuelerLernplattformRepository repository, final SchuelerRepository schuelerRepository,
			final LernplattformRepository lernplattformRepository, final LernplattformCredentialRepository credentialRepository,
			final SchuelerLernplattformMapper mapper) {
		this.repository = repository;
		this.schuelerRepository = schuelerRepository;
		this.lernplattformRepository = lernplattformRepository;
		this.credentialRepository = credentialRepository;
		this.mapper = mapper;
	}

	/**
	 * Bestimmt alle Lernplattform-Zuordnungen eines Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Zuordnungen des Schülers, leer wenn keine vorhanden sind
	 */
	public List<SchuelerLernplattform> getListByIdSchueler(final long idSchueler) {
		return toApi(repository.getListByIdSchueler(idSchueler));
	}

	/**
	 * Aktualisiert die Einwilligungen einer Zuordnung teilweise (PATCH). Nur die im Request gesetzten Felder werden
	 * übernommen. Die Operation läuft in einer Transaktion.
	 *
	 * @param idSchueler        die ID des Schülers
	 * @param idLernplattform   die ID der Lernplattform
	 * @param request           die zu ändernden Felder
	 *
	 * @return die aktualisierte Zuordnung
	 *
	 * @throws ApiOperationException wenn keine Zuordnung für die Kombination aus Schüler und Lernplattform existiert
	 */
	public SchuelerLernplattform patch(final long idSchueler, final long idLernplattform, final SchuelerLernplattformPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final DTOSchuelerLernplattform entity = getEntityOrThrow(idSchueler, idLernplattform);
			mapper.patch(request, entity);
			return toApi(List.of(entity)).getFirst();
		});
	}

	/**
	 * Legt für einen Schüler zu jeder Lernplattform des Katalogs eine Zuordnung ohne Einwilligungen an. Bereits vorhandene
	 * Zuordnungen bleiben unverändert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die neu angelegten Zuordnungen, leer wenn der Schüler bereits zu allen Lernplattformen zugeordnet ist
	 *
	 * @throws ApiOperationException wenn der Schüler nicht existiert
	 */
	public List<SchuelerLernplattform> addForSchueler(final long idSchueler) {
		return TransactionSupport.transactional(() -> {
			assertSchuelerExists(idSchueler);
			final List<Long> idsFehlend = filterMissing(lernplattformRepository.getAllIds(), getIdsLernplattformenOfSchueler(idSchueler));
			return createAll(idsFehlend.stream().map(idLernplattform -> mapper.toDomain(idSchueler, idLernplattform)).toList());
		});
	}

	/**
	 * Legt für eine Lernplattform zu jedem Schüler eine Zuordnung ohne Einwilligungen an. Bereits vorhandene
	 * Zuordnungen bleiben unverändert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idLernplattform   die ID der Lernplattform
	 *
	 * @return die neu angelegten Zuordnungen, leer wenn alle Schüler bereits der Lernplattform zugeordnet sind
	 *
	 * @throws ApiOperationException wenn die Lernplattform nicht existiert
	 */
	public List<SchuelerLernplattform> addForLernplattform(final long idLernplattform) {
		return TransactionSupport.transactional(() -> {
			assertLernplattformExists(idLernplattform);
			final List<Long> idsFehlend = filterMissing(schuelerRepository.getAllIds(), getIdsSchuelerOfLernplattform(idLernplattform));
			return createAll(idsFehlend.stream().map(idSchueler -> mapper.toDomain(idSchueler, idLernplattform)).toList());
		});
	}

	private DTOSchuelerLernplattform getEntityOrThrow(final long idSchueler, final long idLernplattform) {
		return repository.findById(new DTOSchuelerLernplattformPK(idSchueler, idLernplattform))
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND,
						"Eine Lernplattform mit SchuelerID %d und der LernplattformID %d wurde nicht gefunden.".formatted(idSchueler, idLernplattform)));
	}

	private void assertSchuelerExists(final long idSchueler) {
		if (!schuelerRepository.existsById(idSchueler)) {
			throw new ApiOperationException(Status.NOT_FOUND, "Ein Schüler mit der ID %d wurde nicht gefunden.".formatted(idSchueler));
		}
	}

	private void assertLernplattformExists(final long idLernplattform) {
		if (!lernplattformRepository.existsById(idLernplattform)) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Lernplattform mit der ID %d wurde nicht gefunden.".formatted(idLernplattform));
		}
	}

	private Set<Long> getIdsLernplattformenOfSchueler(final long idSchueler) {
		return repository.getListByIdSchueler(idSchueler).stream().map(e -> e.LernplattformID).collect(Collectors.toSet());
	}

	private Set<Long> getIdsSchuelerOfLernplattform(final long idLernplattform) {
		return repository.getListByIdLernplattform(idLernplattform).stream().map(e -> e.SchuelerID).collect(Collectors.toSet());
	}

	private static List<Long> filterMissing(final Collection<Long> idsAlle, final Set<Long> idsVorhanden) {
		return idsAlle.stream().filter(id -> !idsVorhanden.contains(id)).toList();
	}

	private List<SchuelerLernplattform> createAll(final List<DTOSchuelerLernplattform> entities) {
		if (entities.isEmpty()) {
			return List.of();
		}
		repository.create(entities);
		return entities.stream().map(e -> mapper.toApi(e, null)).toList();
	}

	private List<SchuelerLernplattform> toApi(final List<DTOSchuelerLernplattform> entities) {
		final Map<Long, DTOCredentialsLernplattformen> credentials = getCredentialsOf(entities);
		return entities.stream()
				.map(e -> mapper.toApi(e, (e.CredentialID == null) ? null : credentials.get(e.CredentialID)))
				.toList();
	}

	private Map<Long, DTOCredentialsLernplattformen> getCredentialsOf(final List<DTOSchuelerLernplattform> entities) {
		final Set<Long> idsCredentials = entities.stream().map(e -> e.CredentialID).filter(Objects::nonNull).collect(Collectors.toSet());
		return credentialRepository.findMapByIds(idsCredentials);
	}

}
