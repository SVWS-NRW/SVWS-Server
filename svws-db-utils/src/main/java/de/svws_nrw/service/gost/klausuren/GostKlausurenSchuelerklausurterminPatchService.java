package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für höherwertige Schülerklausurtermin-Patches inklusive Raumdaten.
 */
public final class GostKlausurenSchuelerklausurterminPatchService {

	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenRaumzuweisungService raumzuweisungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuelerklausurterminService der Service für Schülerklausurtermine
	 * @param raumzuweisungService der Service für Raumzuweisungen
	 */
	public GostKlausurenSchuelerklausurterminPatchService(final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenRaumzuweisungService raumzuweisungService) {
		this.schuelerklausurterminService = schuelerklausurterminService;
		this.raumzuweisungService = raumzuweisungService;
	}

	/**
	 * Patcht einen Schülerklausurtermin.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die geänderten Raumdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData patch(final GostKlausurenSchuelerklausurterminPatchRequest patchRequest)
			throws ApiOperationException {
		return patchMultiple(List.of(patchRequest));
	}

	/**
	 * Patcht mehrere Schülerklausurtermine.
	 *
	 * @param patchRequests die Patch-Daten
	 *
	 * @return die geänderten Raumdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData patchMultiple(final Collection<GostKlausurenSchuelerklausurterminPatchRequest> patchRequests)
			throws ApiOperationException {
		return transactional(() -> patchMultipleInTransaction(patchRequests));
	}

	private GostKlausurenPatchResponseData patchMultipleInTransaction(final Collection<GostKlausurenSchuelerklausurterminPatchRequest> patchRequests) {
		final List<GostKlausurenSchuelerklausurterminPatchRequest> patches = List.copyOf(patchRequests);
		final List<Long> patchIds = patches.stream()
				.map(patchRequest -> patchRequest.id)
				.distinct()
				.toList();
		final Map<Long, GostSchuelerklausurtermin> beforeById = schuelerklausurterminService.getListByIds(patchIds).stream()
				.collect(Collectors.toMap(schuelerklausurtermin -> schuelerklausurtermin.id, Function.identity()));
		if (beforeById.size() != patchIds.size()) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}
		final List<GostSchuelerklausurtermin> beforeList = patches.stream()
				.map(patchRequest -> beforeById.get(patchRequest.id))
				.toList();
		final List<GostSchuelerklausurtermin> afterList = schuelerklausurterminService.patchMultiple(patches);
		final List<Long> schuelerklausurterminIdsMitGeloeschterRaumzuweisung = new ArrayList<>();
		final List<GostSchuelerklausurtermin> schuelerklausurtermineMitGeaenderterStartzeit = new ArrayList<>();
		for (int i = 0; i < patches.size(); i++) {
			final GostSchuelerklausurtermin before = beforeList.get(i);
			final GostSchuelerklausurtermin after = afterList.get(i);
			if (!Objects.equals(before.idTermin, after.idTermin)) {
				schuelerklausurterminIdsMitGeloeschterRaumzuweisung.add(after.id);
			}
			if (!Objects.equals(before.startzeit, after.startzeit)) {
				schuelerklausurtermineMitGeaenderterStartzeit.add(after);
			}
		}
		final GostKlausurenPatchResponseData result = new GostKlausurenPatchResponseData();
		if (!schuelerklausurterminIdsMitGeloeschterRaumzuweisung.isEmpty()) {
			result.addAll(raumzuweisungService.loescheRaumzuweisungenFuerSchuelerklausurtermine(schuelerklausurterminIdsMitGeloeschterRaumzuweisung));
		}
		if (!schuelerklausurtermineMitGeaenderterStartzeit.isEmpty()) {
			result.addAll(raumzuweisungService.updateRaeumeZuSchuelerklausurterminen(schuelerklausurtermineMitGeaenderterStartzeit));
		}
		return result;
	}

}
