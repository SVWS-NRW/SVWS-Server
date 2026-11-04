package de.svws_nrw.service.schule.teilstandort;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Teilstandort;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.teilstandort.TeilstandortMapper;
import de.svws_nrw.repo.schule.teilstandort.TeilstandortRepository;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für die Teilstandorte
 */
public final class TeilstandortService {

	private final TeilstandortRepository repository;
	private final TeilstandortMapper mapper;

	private static final String ADR_MERKMAL_EXISTIERT_BEREITS = "Ein Teilstandort mit dem AdrMerkmal %s existiert bereits.";
	private static final String TEILSTANDORT_NICHT_GEFUNDEN = "Ein Teilstandort mit dem AdrMerkmal %s existiert nicht.";
	private static final String TEILSTANDORT_REFERENZIERT =
			"Der Teilstandort mit dem AdrMerkmal %s kann nicht gelöscht werden, da er in einer Klasse referenziert wird.";
	private static final String ADR_MERKMALE_FEHLEN = "Für das Löschen müssen Adressmerkmale angegeben werden. Null ist nicht zulässig.";

	/**
	 * Erstellt einen neuen TeilstandortService
	 *
	 * @param teilstandortRepository   das Repository für die Teilstandorte
	 * @param mapper                   der Mapper für die Teilstandorte
	 */
	public TeilstandortService(
			final TeilstandortRepository teilstandortRepository,
			final TeilstandortMapper mapper) {
		this.repository = teilstandortRepository;
		this.mapper = mapper;
	}

	/**
	 * Gibt alle Teilstandorte zurück
	 *
	 * @return Liste aller Einträge
	 */
	public List<Teilstandort> getAll() {
		final List<DTOTeilstandorte> teilstandorte = this.repository.getAll();
		final Set<String> referencedIds = this.repository.getReferencedIds(mapToIds(teilstandorte));
		return teilstandorte
				.stream()
				.map(this.mapper::toApi)
				.map(t -> setReferenceFlag(t, referencedIds))
				.toList();
	}

	private static Set<String> mapToIds(final List<DTOTeilstandorte> teilstandorte) {
		return teilstandorte
				.stream()
				.map(t -> t.AdrMerkmal)
				.collect(Collectors.toSet());
	}

	private static Teilstandort setReferenceFlag(final Teilstandort teilstandort, final Set<String> idsOfReferencedTeilstandorte) {
		teilstandort.referenziertInAnderenTabellen = idsOfReferencedTeilstandorte.contains(teilstandort.adrMerkmal);
		return teilstandort;
	}

	/**
	 * Legt einen neuen Teilstandort an. Validiert vor dem Anlegen, dass das Adressmerkmal noch nicht vergeben ist.
	 *
	 * @param dto   der {@link TeilstandortCreateRequest} mit den Daten des Teilstandortes
	 *
	 * @return der neu angelegte {@link Teilstandort}
	 *
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST}, wenn das Adressmerkmal bereits vergeben ist
	 */
	public Teilstandort create(final TeilstandortCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			if (repository.existsById(dto.adrMerkmal)) {
				throw new ApiOperationException(Response.Status.BAD_REQUEST, ADR_MERKMAL_EXISTIERT_BEREITS.formatted(dto.adrMerkmal));
			}
			final var created = repository.create(mapper.toDomain(dto));
			return mapper.toApi(created);
		});
	}

	/**
	 * Aktualisiert einen bestehenden Teilstandort partiell anhand der im {@link TeilstandortPatchRequest}
	 * gesetzten Felder. Felder mit {@code undefined}-Wert bleiben unverändert.
	 *
	 * @param id  die ID des zu aktualisierenden Teilstandortes
	 * @param dto der {@link TeilstandortPatchRequest} mit den zu ändernden Feldern
	 * @return der aktualisierte {@link Teilstandort}
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND} wenn kein Teilstandort zur {@code id} existiert.
	 */
	public Teilstandort patch(final String id, final TeilstandortPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, TEILSTANDORT_NICHT_GEFUNDEN.formatted(id)));
			mapper.patch(dto, entity);
			return this.mapper.toApi(entity);
		});
	}

	/**
	 * Löscht die Teilstandorte mit den angegebenen Adressmerkmalen. Referenzierte und nicht gefundene
	 * Teilstandorte werden nicht gelöscht, sondern mit einer Meldung im Log der Antwort markiert.
	 * Mehrfach übergebene Adressmerkmale werden nur einmal verarbeitet. Die Antworten werden in der Reihenfolge
	 * des ersten Auftretens der Adressmerkmale zurückgegeben.
	 *
	 * @param idsToDelete   die Adressmerkmale der zu löschenden Teilstandorte
	 *
	 * @return die Liste der {@link SimpleOperationResponse}-Einträge, je einer pro eindeutigem Adressmerkmal
	 *
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST}, wenn die Liste {@code null} ist
	 */
	public List<SimpleOperationResponse> deleteMultiple(final List<String> idsToDelete) {
		if (idsToDelete == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ADR_MERKMALE_FEHLEN);
		}
		if (idsToDelete.isEmpty()) {
			return Collections.emptyList();
		}
		final List<String> uniqueIds = idsToDelete.stream().distinct().toList();

		return TransactionSupport.transactional(() -> {
			final Set<String> referencedIds = this.repository.getReferencedIds(uniqueIds);
			final List<SimpleOperationResponse> responses = uniqueIds.stream()
					.map(id -> deleteTeilstandort(id, referencedIds))
					.toList();
			return responses;
		});

	}

	private SimpleOperationResponse deleteTeilstandort(final String adrMerkmal, final Set<String> referencedIds) {
		final SimpleOperationResponse operationResponse = new SimpleOperationResponse();
		operationResponse.success = false;

		if (referencedIds.contains(adrMerkmal)) {
			operationResponse.log.add(TEILSTANDORT_REFERENZIERT.formatted(adrMerkmal));
			return operationResponse;
		}

		final DTOTeilstandorte entity = repository.findById(adrMerkmal).orElse(null);
		if (entity == null) {
			operationResponse.log.add(TEILSTANDORT_NICHT_GEFUNDEN.formatted(adrMerkmal));
			return operationResponse;
		}

		repository.delete(entity);
		operationResponse.success = true;
		return operationResponse;
	}
}
