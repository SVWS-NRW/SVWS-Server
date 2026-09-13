package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.utils.ApiOperationException;
import org.openapitools.jackson.nullable.JsonNullable;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für höherwertige Termin-Patches inklusive Terminverschiebungen und Raumdaten.
 */
public final class GostKlausurenTerminPatchService {

	private final GostKlausurenTerminService terminService;
	private final GostKlausurenKursklausurService kursklausurService;
	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenRaumzuweisungService raumzuweisungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param terminService der Service für Klausurtermine
	 * @param kursklausurService der Service für Kursklausuren
	 * @param schuelerklausurterminService der Service für Schülerklausurtermine
	 * @param raumzuweisungService der Service für Raumzuweisungen
	 */
	public GostKlausurenTerminPatchService(final GostKlausurenTerminService terminService,
			final GostKlausurenKursklausurService kursklausurService,
			final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenRaumzuweisungService raumzuweisungService) {
		this.terminService = terminService;
		this.kursklausurService = kursklausurService;
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
			final List<GostSchuelerklausurtermin> nachschreiber = schuelerklausurterminService.removeTerminFromNachschreiberByTerminId(before.id);
			result.schuelerklausurterminePatched.addAll(nachschreiber);
			final List<Long> nachschreiberIds = nachschreiber.stream().map(nachschreiberTermin -> nachschreiberTermin.id).toList();
			result.addAll(raumzuweisungService.loescheRaumzuweisungenFuerSchuelerklausurtermine(nachschreiberIds));
		}
		final GostKlausurtermin after = terminService.patch(patchRequest);
		result.terminPatched = after;
		if (!Objects.equals(before.datum, after.datum) || !Objects.equals(before.startzeit, after.startzeit)) {
			result.kursklausurenPatched.addAll(loescheIndividuelleKursklausurStartzeiten(after));
			result.addAll(raumzuweisungService.updateRaeumeZuKlausurtermin(after));
		}
		return result;
	}

	private List<GostKursklausur> loescheIndividuelleKursklausurStartzeiten(final GostKlausurtermin termin) {
		final List<GostKlausurenKursklausurPatchRequest> patches = new ArrayList<>();
		for (final GostKursklausur kursklausur : kursklausurService.getListByTerminIds(List.of(termin.id))) {
			if (kursklausur.startzeit == null) {
				continue;
			}
			final GostKlausurenKursklausurPatchRequest patch = new GostKlausurenKursklausurPatchRequest();
			patch.id = kursklausur.id;
			patch.startzeit = JsonNullable.of(null);
			patches.add(patch);
		}
		return kursklausurService.patchMultiple(patches);
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
