package de.svws_nrw.service.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerMinderleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.LehrerMinderleistungMapper;
import de.svws_nrw.repo.lehrer.minderleistung.LehrerMinderleistungRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response;

import static java.util.Optional.ofNullable;


public final class LehrerMinderleistungService {

	private static final String LEHRERABSCHNITT_NOT_FOUND = "Lehrer-Abschnitt kann nicht per ID aufgelöst werden";
	private static final String SCHULJAHRESABSCHNITT_NOT_FOUND = "Schuljahres-Abschnitt kann nicht per Lehrer-AbschnittsID aufgelöst werden";
	private static final String MINDERLEISTUNG_NOT_RESOLVABLE = "Der Minderleistungsgrund kann nicht aufgelöst werden";

	private final LehrerMinderleistungRepository lehrerMinderleistungRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository;

	private final LehrerMinderleistungMapper minderleistungMapper;

	private final CoreTypeDataManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten> minderLeistungsartenCoreTypeManager;

	/**
	 * Constructor
	 *
	 * @param lehrerMinderleistungRepository {@link LehrerMinderleistungRepository}
	 * @param schuljahresabschnitteRepository {@link SchuljahresabschnitteRepository}
	 * @param lehrerAbschnittsdatenRepository {@link LehrerPersonalabschnittsdatenRepository}
	 * @param minderleistungMapper {@link LehrerMinderleistungMapper}
	 * @param minderLeistungsartenCoreTypeManager {@link CoreTypeDataManager}
	 */
	public LehrerMinderleistungService(
			final LehrerMinderleistungRepository lehrerMinderleistungRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository,
			final LehrerMinderleistungMapper minderleistungMapper,
			final CoreTypeDataManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten> minderLeistungsartenCoreTypeManager) {
		this.lehrerMinderleistungRepository = lehrerMinderleistungRepository;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.lehrerAbschnittsdatenRepository = lehrerAbschnittsdatenRepository;
		this.minderleistungMapper = minderleistungMapper;
		this.minderLeistungsartenCoreTypeManager = minderLeistungsartenCoreTypeManager;
	}

	/**
	 * Liefert alle Minderleistungsstunden zu übergebenen IDs
	 *
	 * @param ids für das Auflösen benötigte Identifier
	 *
	 * @return Liste von {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getList(final List<Long> ids) {
		return lehrerMinderleistungRepository.findListByIds(ids)
				.stream()
				.map(this::toApi)
				.toList();
	}

	/**
	 * Liefert Minderleistungsstunden anhand einer ID
	 *
	 * @param id zugehörige ID
	 *
	 * @return {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden get(final long id) {
		return lehrerMinderleistungRepository.findById(id)
				.map(this::toApi)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND));
	}

	/**
	 * Liefert Minderleistungsstunden zu Abschnittsdaten eines Lehrers
	 *
	 * @param id Abschnitts ID
	 *
	 * @return Liste von {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getListByLehrerabschnittsdatenId(final long id) {
		return lehrerMinderleistungRepository.getAllByLehrerAbschnittId(id)
				.stream()
				.map(this::toApi)
				.toList();
	}

	/**
	 * Ermittelt die Minderleistungs-Einträge gruppiert nach den IDs der Lehrerabschnittsdaten.
	 *
	 * @param idsLehrerAbschnittsdaten die IDs der Lehrerabschnittsdaten
	 * @return Map von Lehrerabschnittsdaten-ID auf Liste der zugehörigen Minderleistungen
	 */
	public Map<Long, List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> getListByIdLehrerAbschnittsdaten(final Collection<Long> idsLehrerAbschnittsdaten) {
		return lehrerMinderleistungRepository.getListByIdLehrerAbschnittsdaten(idsLehrerAbschnittsdaten).entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> entry.getValue().stream().map(this::toApi).toList()
				));
	}

	/**
	 * Erzeugt eine Minderleistungsstunde
	 *
	 * @param request {@link LehrerMinderleistungCreateRequest}
	 *
	 * @return {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden create(final LehrerMinderleistungCreateRequest request) {
		return TransactionSupport.transactional(() -> {
			final var id = lehrerMinderleistungRepository.getNextID();
			final var entity = createEntity(request, id);

			final var persisted = lehrerMinderleistungRepository.create(entity);
			return minderleistungMapper.toApi(persisted, request.idGrund);
		});

	}

	/**
	 * Erzeugt mehrere Minderleistungsstunden
	 *
	 * @param requests Liste von {@link LehrerMinderleistungCreateRequest}
	 *
	 * @return Liste von {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> createMultiple(final List<LehrerMinderleistungCreateRequest> requests) {
		return TransactionSupport.transactional(() -> {
			final var unsavedEntities = new ArrayList<DTOLehrerEntlastungsstunde>();
			var firstId = lehrerMinderleistungRepository.getNextID();
			for (final var request : requests) {
				final var entity = createEntity(request, firstId);
				unsavedEntities.add(entity);
				firstId++;
			}

			lehrerMinderleistungRepository.create(unsavedEntities);

			return unsavedEntities.stream()
					.map(this::toApi)
					.toList();
		});
	}

	/**
	 * Patched vorhandene Minderleistungsstunde
	 *
	 * @param request {@link LehrerMinderleistungPatchRequest}
	 * @param id die ID des zu patchenden Objektes
	 *
	 * @return {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstunden patch(final LehrerMinderleistungPatchRequest request, final Long id) {
		return TransactionSupport.transactional(() -> {
			final var persisted = lehrerMinderleistungRepository.getById(id);

			request.anzahl.ifPresent(anzahl -> persisted.anzahl = anzahl);
			request.idGrund.ifPresent(idGrund -> persisted.entlastungsgrundKrz = getEntlastungsgrundKrzById(idGrund));

			return toApi(persisted);
		});
	}

	/**
	 * Patched mehrere vorhandene Minderleistungsstunden
	 *
	 * @param patches Liste von {@link LehrerMinderleistungBatchPatchRequest}
	 *
	 * @return Liste {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 */
	public List<LehrerPersonalabschnittsdatenAnrechnungsstunden> patchMultiple(final List<LehrerMinderleistungBatchPatchRequest> patches) {
		return TransactionSupport.transactional(() -> patches.stream()
				.map(p -> patch(p, p.id))
				.toList());
	}

	/**
	 * Löscht vorhandene Minderleistung
	 * @param id zu löschende ID
	 * @return {@link SimpleOperationResponse}
	 */
	public SimpleOperationResponse delete(final long id) {
		return TransactionSupport.transactional(() -> {
			final var toDelete = lehrerMinderleistungRepository.getById(id);
			lehrerMinderleistungRepository.delete(toDelete);

			return createResponseLog(id);
		});
	}

	/**
	 * Löscht vorhandene Minderleistungen
	 * @param ids zu löschende IDs
	 * @return Liste von {@link SimpleOperationResponse}
	 */
	public List<SimpleOperationResponse> deleteMultiple(final List<Long> ids) {
		return TransactionSupport.transactional(() -> {
			final var toDelete = lehrerMinderleistungRepository.findListByIds(ids);

			lehrerMinderleistungRepository.delete(toDelete);

			return toDelete.stream()
					.map(d -> createResponseLog(d.id))
					.toList();
		});

	}

	private static SimpleOperationResponse createResponseLog(final long id) {
		final var log = new SimpleOperationResponse();
		log.id = id;
		log.success = true;
		return log;
	}

	private String getEntlastungsgrundKrzById(final long grundId) {
		return ofNullable(minderLeistungsartenCoreTypeManager.getEintragByID(grundId))
				.map(g -> g.kuerzel)
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST, MINDERLEISTUNG_NOT_RESOLVABLE));
	}

	private Long getEntlastungsgrundByKuerzel(final DTOLehrerEntlastungsstunde domain) {
		final var schuljahrAbschnitt = getSchuljahresAbschnittById(domain.idAbschnittsdaten);

		return ofNullable(minderLeistungsartenCoreTypeManager.getWertByKuerzel(domain.entlastungsgrundKrz))
				.map(d -> d.daten(schuljahrAbschnitt.Jahr))
				.map(g -> g.id)
				.orElse(null);

	}

	private DTOSchuljahresabschnitte getSchuljahresAbschnittById(final long idAbschnitt) {
		final var schuljahresAbschnittId = lehrerAbschnittsdatenRepository.findById(idAbschnitt)
				.map(abschnitt -> abschnitt.Schuljahresabschnitts_ID)
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST, LEHRERABSCHNITT_NOT_FOUND));

		return schuljahresabschnitteRepository.findById(schuljahresAbschnittId)
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST, SCHULJAHRESABSCHNITT_NOT_FOUND));
	}

	private DTOLehrerEntlastungsstunde createEntity(final LehrerMinderleistungCreateRequest request, final long id) {
		final var kuerzel = getEntlastungsgrundKrzById(request.idGrund);
		return minderleistungMapper.toDomain(request, id, kuerzel);
	}

	private LehrerPersonalabschnittsdatenAnrechnungsstunden toApi(final DTOLehrerEntlastungsstunde domain) {
		return minderleistungMapper.toApi(domain, getEntlastungsgrundByKuerzel(domain));
	}
}
