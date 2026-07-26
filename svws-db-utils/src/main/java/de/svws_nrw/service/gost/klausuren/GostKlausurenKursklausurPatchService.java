package de.svws_nrw.service.gost.klausuren;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für höherwertige Kursklausur-Patches inklusive abhängiger Raumdaten.
 */
public final class GostKlausurenKursklausurPatchService {

	private final GostKlausurenKursklausurService kursklausurService;
	private final GostKlausurenTerminService terminService;
	private final GostKlausurenVorgabeService vorgabeService;
	private final GostKlausurenSchuelerklausurService schuelerklausurService;
	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenRaumzuweisungService raumzuweisungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param kursklausurService der Service für Kursklausuren
	 * @param terminService der Service für Klausurtermine
	 * @param vorgabeService der Service für Klausurvorgaben
	 * @param schuelerklausurService der Service für Schülerklausuren
	 * @param schuelerklausurterminService der Service für Schülerklausurtermine
	 * @param raumzuweisungService der Service für Raumzuweisungen
	 */
	public GostKlausurenKursklausurPatchService(final GostKlausurenKursklausurService kursklausurService,
			final GostKlausurenTerminService terminService,
			final GostKlausurenVorgabeService vorgabeService,
			final GostKlausurenSchuelerklausurService schuelerklausurService,
			final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenRaumzuweisungService raumzuweisungService) {
		this.kursklausurService = kursklausurService;
		this.terminService = terminService;
		this.vorgabeService = vorgabeService;
		this.schuelerklausurService = schuelerklausurService;
		this.schuelerklausurterminService = schuelerklausurterminService;
		this.raumzuweisungService = raumzuweisungService;
	}

	/**
	 * Patcht eine Kursklausur.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die geänderten Raumdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData patch(final GostKlausurenKursklausurPatchRequest patchRequest) throws ApiOperationException {
		return transactional(() -> patchInTransaction(patchRequest));
	}

	private GostKlausurenPatchResponseData patchInTransaction(final GostKlausurenKursklausurPatchRequest patchRequest) {
		final GostKursklausur before = kursklausurService.get(patchRequest.id);
		validateTerminQuartal(before, patchRequest);
		final GostKursklausur after = kursklausurService.patch(patchRequest);
		final GostKlausurenPatchResponseData result = handleRaumdatenChanges(before, after);
		result.kursklausurPatched = after;
		return result;
	}

	private void validateTerminQuartal(final GostKursklausur before, final GostKlausurenKursklausurPatchRequest patchRequest) {
		if (!patchRequest.idTermin.isPresent()) {
			return;
		}
		final Long newTerminId = patchRequest.idTermin.get();
		if (newTerminId != null) {
			final int terminQuartal = terminService.get(newTerminId).quartal;
			final int vorgabeQuartal = vorgabeService.get(before.idVorgabe).quartal;
			if ((terminQuartal != 0) && !Objects.equals(terminQuartal, vorgabeQuartal)) {
				throw new ApiOperationException(Status.CONFLICT, "Klausur-Quartal entspricht nicht Termin-Quartal.");
			}
		}
	}

	private GostKlausurenPatchResponseData handleRaumdatenChanges(final GostKursklausur before, final GostKursklausur after) {
		if (!Objects.equals(before.idTermin, after.idTermin)) {
			return raumzuweisungService.loescheRaumzuweisungenFuerSchuelerklausurtermine(getSchuelerklausurtermine(after).stream().map(skt -> skt.id).toList());
		}
		if (!Objects.equals(before.startzeit, after.startzeit)) {
			return raumzuweisungService.updateRaeumeZuSchuelerklausurterminen(getSchuelerklausurtermine(after));
		}
		return new GostKlausurenPatchResponseData();
	}

	private List<GostSchuelerklausurtermin> getSchuelerklausurtermine(final GostKursklausur klausur) {
		return schuelerklausurterminService.getListBySchuelerklausurIds(
				schuelerklausurService.getListByKursklausurIds(List.of(klausur.id)).stream().map(sk -> sk.id).toList());
	}

}
