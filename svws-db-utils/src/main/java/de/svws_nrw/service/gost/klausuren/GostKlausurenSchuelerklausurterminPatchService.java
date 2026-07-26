package de.svws_nrw.service.gost.klausuren;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.db.utils.ApiOperationException;

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
		return transactional(() -> patchInTransaction(patchRequest));
	}

	private GostKlausurenPatchResponseData patchInTransaction(final GostKlausurenSchuelerklausurterminPatchRequest patchRequest) {
		final GostSchuelerklausurtermin before = schuelerklausurterminService.get(patchRequest.id);
		final GostSchuelerklausurtermin after = schuelerklausurterminService.patch(patchRequest);
		final GostKlausurenPatchResponseData raumDataChanged = new GostKlausurenPatchResponseData();
		if (!Objects.equals(before.idTermin, after.idTermin)) {
			raumDataChanged.addAll(raumzuweisungService.loescheRaumzuweisungenFuerSchuelerklausurtermine(List.of(after.id)));
		}
		if (!Objects.equals(before.startzeit, after.startzeit)) {
			raumDataChanged.addAll(raumzuweisungService.updateRaeumeZuSchuelerklausurterminen(
					List.of(after)));
		}
		return raumDataChanged;
	}

}
