package de.svws_nrw.service.schule;

import java.util.List;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.utils.schule.SchuljahresabschnittsUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.SchuljahresabschnittMapper;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response;

/**
 * Ein Service für den Zugriff auf die Schuljahresabschnitte der Schule.
 * <p>
 * Stellt Lese-, Erstellungs- und Änderungsoperationen für Schuljahresabschnitte bereit und führt Validierungen durch.
 */
public final class SchuljahresabschnittService {

	private static final String ABSCHNITT_NICHT_GEFUNDEN = "Der Schuljahresabschnitt mit der ID %d wurde nicht gefunden.";
	private static final String ABSCHNITT_EXISTIERT_BEREITS = "Der Schuljahresabschnitt für das Schuljahr %d und den Abschnitt %d existiert bereits.";
	private static final String VORGAENGER_NICHT_GEFUNDEN = "Der angegebene vorherige Schuljahresabschnitt mit der ID %d existiert nicht.";
	private static final String FOLGEABSCHNITT_NICHT_GEFUNDEN = "Der angegebene folgende Schuljahresabschnitt mit der ID %d existiert nicht.";
	private static final String EIGENER_VORGAENGER = "Ein Schuljahresabschnitt darf nicht sein eigener vorheriger Schuljahresabschnitt sein.";
	private static final String EIGENER_FOLGEABSCHNITT = "Ein Schuljahresabschnitt darf nicht sein eigener folgender Schuljahresabschnitt sein.";

	/** Das Repository für die Schuljahresabschnitte */
	private final SchuljahresabschnitteRepository repository;

	/** Der Mapper zur Konvertierung zwischen Datenbank-DTOs, Requests und API-Objekten */
	private final SchuljahresabschnittMapper mapper;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository für die Schuljahresabschnitte
	 * @param mapper       der Mapper zur Konvertierung zwischen Datenbank-DTOs, Requests und API-Objekten
	 */
	public SchuljahresabschnittService(final SchuljahresabschnitteRepository repository, final SchuljahresabschnittMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}


	/**
	 * Bestimmt den Schuljahresabschnitt anhand der ID.
	 *
	 * @param id   die ID des Schuljahresabschnittes
	 *
	 * @return der Schuljahresabschnitt
	 *
	 * @throws ApiOperationException   wenn kein Schuljahresabschnitt zur ID gefunden wurde
	 */
	public Schuljahresabschnitt getById(final Long id) throws ApiOperationException {
		return repository.findById(id)
				.map(mapper::toApi)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, ABSCHNITT_NICHT_GEFUNDEN.formatted(id)));
	}


	/**
	 * Gibt eine Liste aller Schuljahresabschnitte zurück, aufsteigend sortiert nach Schuljahr und Abschnitt.
	 *
	 * @return die sortierte Liste der Schuljahresabschnitte
	 */
	public List<Schuljahresabschnitt> getList() {
		return repository.getAll().stream()
				.map(mapper::toApi)
				.sorted(SchuljahresabschnittsUtils.comparator)
				.toList();
	}


	/**
	 * Erstellt einen neuen Schuljahresabschnitt. Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param request   die Daten für den neuen Schuljahresabschnitt
	 *
	 * @return der erstellte Schuljahresabschnitt
	 *
	 * @throws ApiOperationException   wenn die Kombination aus Schuljahr und Abschnitt bereits existiert oder
	 *                                 ein angegebener vorheriger oder folgender Schuljahresabschnitt nicht existiert
	 */
	public Schuljahresabschnitt create(final SchuljahresabschnittCreateRequest request) throws ApiOperationException {
		return TransactionSupport.transactional(() -> {
			validateCreate(request);
			final DTOSchuljahresabschnitte created = repository.create(mapper.toDomain(request));
			return mapper.toApi(created);
		});
	}


	/**
	 * Ändert einen bestehenden Schuljahresabschnitt teilweise. Nur die im Request gesetzten Felder werden
	 * geändert. Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param id        die ID des zu ändernden Schuljahresabschnittes
	 * @param request   die zu ändernden Felder
	 *
	 * @return der geänderte Schuljahresabschnitt
	 *
	 * @throws ApiOperationException   wenn der Schuljahresabschnitt nicht gefunden wurde, ein angegebener vorheriger
	 *                                 oder folgender Schuljahresabschnitt nicht existiert oder ein Schuljahresabschnitt
	 *                                 mit sich selbst verknüpft werden soll
	 */
	public Schuljahresabschnitt patch(final long id, final SchuljahresabschnittPatchRequest request) throws ApiOperationException {
		return TransactionSupport.transactional(() -> {
			final DTOSchuljahresabschnitte entity = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, ABSCHNITT_NICHT_GEFUNDEN.formatted(id)));
			validatePatch(id, request);
			mapper.patch(request, entity);
			return mapper.toApi(entity);
		});
	}


	private void validateCreate(final SchuljahresabschnittCreateRequest request) {
		if (repository.findBySchuljahrAndAbschnitt(request.schuljahr, request.abschnitt).isPresent()) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ABSCHNITT_EXISTIERT_BEREITS.formatted(request.schuljahr, request.abschnitt));
		}
		validateVorgaenger(request.idVorigerAbschnitt, null);
		validateFolgeabschnitt(request.idFolgeAbschnitt, null);
	}


	private void validatePatch(final long id, final SchuljahresabschnittPatchRequest request) {
		request.idVorigerAbschnitt.ifPresent(idVoriger -> validateVorgaenger(idVoriger, id));
		request.idFolgeAbschnitt.ifPresent(idFolge -> validateFolgeabschnitt(idFolge, id));
	}


	private void validateVorgaenger(final Long idVoriger, final Long idAbschnitt) {
		if (idVoriger == null) {
			return;
		}
		if (idVoriger.equals(idAbschnitt)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, EIGENER_VORGAENGER);
		}
		if (!repository.existsById(idVoriger)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, VORGAENGER_NICHT_GEFUNDEN.formatted(idVoriger));
		}
	}


	private void validateFolgeabschnitt(final Long idFolge, final Long idAbschnitt) {
		if (idFolge == null) {
			return;
		}
		if (idFolge.equals(idAbschnitt)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, EIGENER_FOLGEABSCHNITT);
		}
		if (!repository.existsById(idFolge)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, FOLGEABSCHNITT_NICHT_GEFUNDEN.formatted(idFolge));
		}
	}

}
