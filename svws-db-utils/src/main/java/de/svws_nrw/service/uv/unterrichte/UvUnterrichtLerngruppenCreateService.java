package de.svws_nrw.service.uv.unterrichte;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvUnterricht;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterricht;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtRepository;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppeService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für das Erstellen fehlender UV-Unterrichte auf Basis von Lerngruppen.
 */
public final class UvUnterrichtLerngruppenCreateService {

	private final UvLerngruppeService uvLerngruppeService;
	private final UvUnterrichtRepository uvUnterrichtRepository;
	private final UvUnterrichtService uvUnterrichtService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvLerngruppeService    der CRUD-Service für UV-Lerngruppen
	 * @param uvUnterrichtRepository das Repository für UV-Unterrichte
	 * @param uvUnterrichtService    der CRUD-Service für UV-Unterrichte
	 */
	public UvUnterrichtLerngruppenCreateService(final UvLerngruppeService uvLerngruppeService,
			final UvUnterrichtRepository uvUnterrichtRepository,
			final UvUnterrichtService uvUnterrichtService) {
		this.uvLerngruppeService = uvLerngruppeService;
		this.uvUnterrichtRepository = uvUnterrichtRepository;
		this.uvUnterrichtService = uvUnterrichtService;
	}

	/**
	 * Erstellt für die angegebene Lerngruppe fehlende Unterrichte.
	 *
	 * @param idLerngruppe   die ID der Lerngruppe
	 *
	 * @return die neu erstellten Unterrichte
	 */
	public List<UvUnterricht> createByLerngruppe(final long idLerngruppe) {
		return createByLerngruppen(List.of(idLerngruppe));
	}

	/**
	 * Erstellt für die angegebenen Lerngruppen fehlende Unterrichte.
	 *
	 * @param idsLerngruppen   die IDs der Lerngruppen
	 *
	 * @return die neu erstellten Unterrichte
	 */
	public List<UvUnterricht> createByLerngruppen(final Collection<Long> idsLerngruppen) {
		return transactional(() -> doCreateByLerngruppen(normalizeIds(idsLerngruppen)));
	}

	private List<UvUnterricht> doCreateByLerngruppen(final List<Long> idsLerngruppen) {
		if (idsLerngruppen.isEmpty()) {
			return List.of();
		}

		final List<UvLerngruppe> lerngruppen = uvLerngruppeService.getList(idsLerngruppen);
		final Map<Long, Long> vorhandeneUnterrichtCounts = getVorhandeneUnterrichtCounts(idsLerngruppen);
		final List<UvUnterrichtCreateRequest> requests = createRequests(lerngruppen, vorhandeneUnterrichtCounts);
		return requests.isEmpty() ? List.of() : uvUnterrichtService.createMultiple(requests);
	}

	private List<Long> normalizeIds(final Collection<Long> idsLerngruppen) {
		if ((idsLerngruppen == null) || idsLerngruppen.isEmpty()) {
			return List.of();
		}
		return new ArrayList<>(new LinkedHashSet<>(idsLerngruppen));
	}

	private Map<Long, Long> getVorhandeneUnterrichtCounts(final Collection<Long> idsLerngruppen) {
		final Map<Long, Long> result = new LinkedHashMap<>();
		for (final DTOUvUnterricht unterricht : uvUnterrichtRepository.getListByLerngruppenIds(idsLerngruppen)) {
			result.merge(unterricht.Lerngruppe_ID, 1L, Long::sum);
		}
		return result;
	}

	private List<UvUnterrichtCreateRequest> createRequests(final List<UvLerngruppe> lerngruppen, final Map<Long, Long> vorhandeneUnterrichtCounts) {
		final List<UvUnterrichtCreateRequest> result = new ArrayList<>();
		for (final UvLerngruppe lerngruppe : lerngruppen) {
			result.addAll(createRequestsForLerngruppe(lerngruppe, vorhandeneUnterrichtCounts.getOrDefault(lerngruppe.id, 0L)));
		}
		return result;
	}

	private List<UvUnterrichtCreateRequest> createRequestsForLerngruppe(final UvLerngruppe lerngruppe, final long vorhanden) {
		final int soll = validateSollUnterricht(lerngruppe);
		final long anzahlFehlend = soll - vorhanden;
		if (anzahlFehlend <= 0) {
			return List.of();
		}

		final List<UvUnterrichtCreateRequest> result = new ArrayList<>();
		for (int i = 0; i < anzahlFehlend; i++) {
			result.add(createRequest(lerngruppe));
		}
		return result;
	}

	private int validateSollUnterricht(final UvLerngruppe lerngruppe) {
		final double soll = lerngruppe.wochenstundenUnterrichtet;
		if (soll < 0) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Lerngruppe mit der ID %d hat negative WochenstundenUnterrichtet.".formatted(lerngruppe.id));
		}
		return (int) soll;
	}

	private UvUnterrichtCreateRequest createRequest(final UvLerngruppe lerngruppe) {
		final UvUnterrichtCreateRequest request = new UvUnterrichtCreateRequest();
		request.idPlanungsabschnitt = lerngruppe.idPlanungsabschnitt;
		request.idLerngruppe = lerngruppe.id;
		request.idZeitrasterEintrag = null;
		return request;
	}

}
