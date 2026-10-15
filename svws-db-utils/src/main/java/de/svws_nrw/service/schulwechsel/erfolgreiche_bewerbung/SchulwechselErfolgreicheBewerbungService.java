package de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.SchulwechselDokument;
import de.svws_nrw.core.data.schule.SchulwechselErfolgreicheBewerbung;
import de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungMapper;
import de.svws_nrw.repo.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungRepository;
import de.svws_nrw.service.schulwechsel.client.ErfolgreicheBewerbungResponse;
import de.svws_nrw.service.schulwechsel.client.ErfolgreicheBewerbungenClient;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentCreateRequest;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentService;
import jakarta.ws.rs.core.Response;

/**
 * Service für die Verwaltung von {@link SchulwechselErfolgreicheBewerbung}-Einträgen.
 */
public class SchulwechselErfolgreicheBewerbungService {

	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final SchulwechselErfolgreicheBewerbungRepository repository;
	private final SchulwechselErfolgreicheBewerbungMapper mapper;
	private final SchulwechselDokumentService dokumentService;
	private final ErfolgreicheBewerbungenClient erfolgreicheBewerbungenClient;

	/**
	 * Konstruktor.
	 *
	 * @param repository                    {@link SchulwechselErfolgreicheBewerbungRepository}
	 * @param mapper                        {@link SchulwechselErfolgreicheBewerbungMapper}
	 * @param dokumentService               {@link SchulwechselDokumentService}
	 * @param erfolgreicheBewerbungenClient {@link ErfolgreicheBewerbungenClient}
	 */
	public SchulwechselErfolgreicheBewerbungService(
			final SchulwechselErfolgreicheBewerbungRepository repository,
			final SchulwechselErfolgreicheBewerbungMapper mapper,
			final SchulwechselDokumentService dokumentService,
			final ErfolgreicheBewerbungenClient erfolgreicheBewerbungenClient) {
		this.repository = repository;
		this.mapper = mapper;
		this.dokumentService = dokumentService;
		this.erfolgreicheBewerbungenClient = erfolgreicheBewerbungenClient;
	}

	/**
	 * Gibt alle SchulwechselErfolgreicheBewerbung-Einträge zurück.
	 *
	 * @return eine Liste aller SchulwechselErfolgreicheBewerbung-Einträge
	 */
	public List<SchulwechselErfolgreicheBewerbung> getAll() {
		return this.repository
				.getAll()
				.stream()
				.map(this.mapper::toApi)
				.toList();
	}

	/**
	 * Gibt einen SchulwechselErfolgreicheBewerbung-Eintrag anhand seiner ID zurück.
	 *
	 * @param id die ID des Eintrags
	 * @return der Eintrag als API-Modell
	 * @throws ApiOperationException wenn kein Eintrag mit der ID existiert
	 */
	public SchulwechselErfolgreicheBewerbung getById(final long id) {
		return this.mapper.toApi(this.repository.getById(id));
	}

	/**
	 * Erstellt einen neuen SchulwechselErfolgreicheBewerbung-Eintrag.
	 *
	 * @param dto die Daten für den neuen Eintrag
	 * @return der erstellte Eintrag als API-Modell
	 */
	public SchulwechselErfolgreicheBewerbung create(final SchulwechselErfolgreicheBewerbungCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			validateStatus(dto.idStatus);
			final var entity = this.mapper.toDomain(dto);
			final var created = this.repository.create(entity);
			return this.mapper.toApi(created);
		});
	}

	/**
	 * Erstellt mehrere neue SchulwechselErfolgreicheBewerbung-Einträge.
	 *
	 * @param dtos die Daten für die neuen Einträge
	 * @return die erstellten Einträge als API-Modelle, in derselben Reihenfolge wie die Eingabe
	 */
	public List<SchulwechselErfolgreicheBewerbung> createAll(final List<SchulwechselErfolgreicheBewerbungCreateRequest> dtos) {
		dtos.forEach(dto -> validateStatus(dto.idStatus));
		final var entities = dtos.stream()
				.map(this.mapper::toDomain)
				.toList();
		final var created = this.repository.create(entities);
		return created.stream()
				.map(this.mapper::toApi)
				.toList();
	}

	/**
	 * Aktualisiert einen bestehenden SchulwechselErfolgreicheBewerbung-Eintrag teilweise (PATCH).
	 *
	 * @param id  die ID des zu aktualisierenden Eintrags
	 * @param dto die zu aktualisierenden Felder
	 * @return der aktualisierte Eintrag als API-Modell
	 */
	public SchulwechselErfolgreicheBewerbung patch(final long id, final SchulwechselErfolgreicheBewerbungPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			dto.idStatus.ifPresent(this::validateStatus);
			final var entity = repository.getById(id);
			mapper.patch(dto, entity);
			return this.mapper.toApi(entity);
		});
	}

	/**
	 * Löscht die SchulwechselErfolgreicheBewerbung-Einträge mit den angegebenen IDs.
	 * Nicht gefundene IDs werden stillschweigend ignoriert.
	 *
	 * @param idsToDelete Liste der zu löschenden IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() -> {
			final var entitiesToDelete = repository.findListByIds(idsToDelete);
			return repository.delete(entitiesToDelete)
					.stream()
					.map(e -> SimpleOperationResponse.ofSuccess(e.id))
					.sorted(Comparator.comparingLong(response -> response.id))
					.toList();
		});
	}

	/**
	 * Prüft wie viele neue erfolgreiche Bewerbungen vom Remote-Server verfügbar sind.
	 * Legt keine neuen Einträge an.
	 *
	 * @return die Anzahl neuer erfolgreicher Bewerbungen
	 * @throws ApiOperationException mit Status 409 wenn eine schulkindId doppelt im Remote-Ergebnis vorkommt,
	 *                               sowie bei Fehlern in der Kommunikation mit dem Remote-Service oder bei
	 *                               ungültigen Remote-Daten, siehe {@link ErfolgreicheBewerbungenClient#getAllBewerbungen()}
	 */
	public int syncPreview() {
		final List<ErfolgreicheBewerbungResponse> responsesAll = erfolgreicheBewerbungenClient.getAllBewerbungen();
		validateNoDuplicatesRemote(responsesAll);

		final Set<String> existingIds = repository.findAllIdSchulkindSchulbewerbung();
		return (int) responsesAll.stream()
				.filter(b -> !existingIds.contains(b.schulkindId()))
				.count();
	}

	/**
	 * Synchronisiert erfolgreiche Bewerbungen vom Schulbewerbungs-Service.
	 * <p>
	 * Ablauf:
	 * <ol>
	 *   <li>Alle erfolgreichen Bewerbungen werden außerhalb einer Transaktion vom Remote-Service
	 *       abgerufen. Remote-Duplikate und ungültige schulkindIds werden als Fehler gemeldet.</li>
	 *   <li>Bereits bekannte schulkindIds werden übersprungen und im Ergebnis ausgewiesen.</li>
	 *   <li>Für die verbleibenden, noch unbekannten schulkindIds werden die zugehörigen
	 *       XSchule-Dokumente ebenfalls außerhalb einer Transaktion vom Remote-Service abgerufen.</li>
	 *   <li>Dokumente und Bewerbungen werden atomar in einer einzigen Transaktion persistiert.</li>
	 * </ol>
	 *
	 * @return ein {@link SyncResult} mit den neu angelegten Einträgen und den übersprungenen schulkindIds
	 * @throws ApiOperationException mit Status 409 wenn eine schulkindId doppelt im Remote-Ergebnis vorkommt,
	 *                               sowie bei Fehlern in der Kommunikation mit dem Remote-Service oder bei
	 *                               ungültigen Remote-Daten, siehe {@link ErfolgreicheBewerbungenClient#getAllBewerbungen()}
	 */
	public SyncResult sync() {
		final List<ErfolgreicheBewerbungResponse> responsesAll = erfolgreicheBewerbungenClient.getAllBewerbungen();
		validateNoDuplicatesRemote(responsesAll);

		final Set<String> existingIds = repository.findAllIdSchulkindSchulbewerbung();
		final Map<Boolean, List<ErfolgreicheBewerbungResponse>> partitioned = responsesAll.stream()
				.collect(Collectors.partitioningBy(b -> existingIds.contains(b.schulkindId())));
		final List<String> skipped = partitioned.get(true).stream()
				.map(ErfolgreicheBewerbungResponse::schulkindId)
				.toList();

		final List<ErfolgreicheBewerbungResponse> responsesNew = partitioned.get(false);
		if (responsesNew.isEmpty()) {
			return new SyncResult(List.of(), skipped);
		}
		final List<BewerbungWithDocument> bewerbungenNew = fetchDokumente(responsesNew);

		return TransactionSupport.transactional(() -> {
			final String now = OffsetDateTime.now(ZoneOffset.UTC).format(DATETIME_FORMATTER);
			final List<SchulwechselErfolgreicheBewerbung> created = persistBewerbungen(bewerbungenNew, now);
			return new SyncResult(created, skipped);
		});
	}

	/**
	 * Ruft für die übergebenen Bewerbungen jeweils das zugehörige XSchule-Dokument vom Remote-Service ab.
	 *
	 * @param bewerbungen die Bewerbungen, für die Dokumente abgerufen werden sollen
	 * @return Liste von {@link BewerbungWithDocument}-Paaren, in der Reihenfolge der Eingabe
	 */
	private List<BewerbungWithDocument> fetchDokumente(final List<ErfolgreicheBewerbungResponse> bewerbungen) {
		return bewerbungen.stream()
				.map(b -> new BewerbungWithDocument(b, erfolgreicheBewerbungenClient.getXSchule(b.schulkindId())))
				.toList();
	}

	/**
	 * Legt für die übergebenen Bewerbungen zuerst die XSchule-Dokumente und anschließend die
	 * Bewerbungs-Einträge an. Muss innerhalb einer Transaktion aufgerufen werden.
	 * <p>
	 * Die Zuordnung von Dokument zu Bewerbung erfolgt über den Index und setzt voraus, dass
	 * {@link SchulwechselDokumentService#createAll(List)} die Reihenfolge der Eingabe beibehält.
	 *
	 * @param bewerbungen     die noch nicht bekannten Bewerbungen mit ihren Dokumenten
	 * @param now             der Zeitstempel für createdAt und lastModified
	 * @return die erstellten Einträge als API-Modelle, in derselben Reihenfolge wie die Eingabe
	 */
	private List<SchulwechselErfolgreicheBewerbung> persistBewerbungen(final List<BewerbungWithDocument> bewerbungen, final String now) {
		final List<SchulwechselDokumentCreateRequest> dokumentRequests = bewerbungen.stream()
				.map(b -> createRequestDokument(b.bewerbungResponse().schulkindId(), b.xmlDocument(), now))
				.toList();
		final List<SchulwechselDokument> dokumente = dokumentService.createAll(dokumentRequests);

		final List<SchulwechselErfolgreicheBewerbungCreateRequest> bewerbungRequests = IntStream.range(0, bewerbungen.size())
				.mapToObj(i -> createRequestBewerbung(bewerbungen.get(i).bewerbungResponse().schulkindId(), dokumente.get(i).id, now))
				.toList();
		return createAll(bewerbungRequests);
	}

	private void validateStatus(final Integer idStatus) {
		if (StatusSchulwechselErfolgreicheBewerbung.getByIdOrNull(idStatus) == null) {
			throw new ApiOperationException(
					Response.Status.BAD_REQUEST,
					"Ungültiger Status mit der ID %s.".formatted(idStatus)
			);
		}
	}

	private static SchulwechselDokumentCreateRequest createRequestDokument(final String schulkindId, final String xmlDocument, final String now) {
		final var request = new SchulwechselDokumentCreateRequest();
		request.fileName = "bewerbung_" + schulkindId + ".xml";
		request.xmlDocument = xmlDocument;
		request.createdAt = now;
		request.lastModified = now;
		return request;
	}

	private static SchulwechselErfolgreicheBewerbungCreateRequest createRequestBewerbung(final String schulkindId, final long idDocument, final String now) {
		final var request = new SchulwechselErfolgreicheBewerbungCreateRequest();
		request.idSchulkindSchulbewerbung = schulkindId;
		request.idStatus = StatusSchulwechselErfolgreicheBewerbung.NEU.getId();
		request.lastModified = now;
		request.idDocument = idDocument;
		return request;
	}

	private static void validateNoDuplicatesRemote(final List<ErfolgreicheBewerbungResponse> bewerbungen) {
		final Set<String> remoteIds = new HashSet<>();
		for (final var b : bewerbungen) {
			final String schulkindId = b.schulkindId();
			if (!remoteIds.add(schulkindId)) {
				throw new ApiOperationException(
						Response.Status.CONFLICT,
						"Doppelte schulkindId in der Remote-Antwort: %s".formatted(schulkindId)
				);
			}
		}
	}

	private record BewerbungWithDocument(
			ErfolgreicheBewerbungResponse bewerbungResponse,
			String xmlDocument
	) {
	}
}
