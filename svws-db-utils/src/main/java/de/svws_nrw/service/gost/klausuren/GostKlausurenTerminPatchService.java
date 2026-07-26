package de.svws_nrw.service.gost.klausuren;

import java.util.Objects;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.utils.ApiOperationException;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für höherwertige Termin-Patches inklusive Terminverschiebungen und Raumdaten.
 */
public final class GostKlausurenTerminPatchService {

	private final GostKlausurenTerminService terminService;
	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenRaumzuweisungService raumzuweisungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param terminService der Service für Klausurtermine
	 * @param schuelerklausurterminService der Service für Schülerklausurtermine
	 * @param raumzuweisungService der Service für Raumzuweisungen
	 */
	public GostKlausurenTerminPatchService(final GostKlausurenTerminService terminService,
			final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenRaumzuweisungService raumzuweisungService) {
		this.terminService = terminService;
		this.schuelerklausurterminService = schuelerklausurterminService;
		this.raumzuweisungService = raumzuweisungService;
	}

	/**
	 * Patcht einen Klausurtermin.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die geänderten Raumdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData patch(final GostKlausurenTerminPatchRequest patchRequest) throws ApiOperationException {
		return transactional(() -> patchInTransaction(patchRequest));
	}

	private GostKlausurenPatchResponseData patchInTransaction(final GostKlausurenTerminPatchRequest patchRequest) {
		final GostKlausurtermin before = terminService.get(patchRequest.id);
		final GostKlausurenPatchResponseData raumDataChanged = handleRaumdatenBeforePatch(before, patchRequest);
		final GostKlausurenPatchResponseData result = (raumDataChanged == null) ? new GostKlausurenPatchResponseData() : raumDataChanged;
		if (isNachschreiberZugelassenRemoved(before, patchRequest)) {
			result.schuelerklausurterminePatched.addAll(schuelerklausurterminService.removeTerminFromNachschreiberByTerminId(before.id));
		}
		final GostKlausurtermin after = terminService.patch(patchRequest);
		result.terminPatched = after;
		if (!Objects.equals(before.datum, after.datum) || !Objects.equals(before.startzeit, after.startzeit)) {
			result.addAll(raumzuweisungService.updateRaeumeZuKlausurtermin(after));
		}
		return result;
	}

	private static boolean isNachschreiberZugelassenRemoved(final GostKlausurtermin before, final GostKlausurenTerminPatchRequest patchRequest) {
		if (!patchRequest.nachschreiberZugelassen.isPresent()) {
			return false;
		}
		final boolean newValue = JSONMapper.convertToBoolean(patchRequest.nachschreiberZugelassen.get(), false, "nachschreiberZugelassen");
		return before.nachschreiberZugelassen && !newValue;
	}

	private GostKlausurenPatchResponseData handleRaumdatenBeforePatch(final GostKlausurtermin before,
			final GostKlausurenTerminPatchRequest patchRequest) {
		if (!patchRequest.datum.isPresent()) {
			return null;
		}
		final String newDate = JSONMapper.convertToString(patchRequest.datum.get(), true, false, null, "datum");
		if (!Objects.equals(newDate, before.datum)) {
			final GostKlausurenPatchResponseData changed = raumzuweisungService.handleRaumzuweisungenBeiTerminverschiebung(before);
			return ((newDate != null) && (changed == null)) ? new GostKlausurenPatchResponseData() : changed;
		}
		return null;
	}

}
