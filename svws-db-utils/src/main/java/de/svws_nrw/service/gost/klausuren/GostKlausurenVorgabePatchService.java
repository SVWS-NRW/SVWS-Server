package de.svws_nrw.service.gost.klausuren;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für das Patchen von GOSt-Klausurvorgaben einschließlich der dadurch erforderlichen Aktualisierung von Raumbelegungen.
 */
public final class GostKlausurenVorgabePatchService {

	private final GostKlausurenVorgabeService vorgabeService;
	private final GostKlausurenRaumzuweisungService raumzuweisungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param vorgabeService der Service für Klausurvorgaben
	 * @param raumzuweisungService der Service für Raumzuweisungen
	 */
	public GostKlausurenVorgabePatchService(final GostKlausurenVorgabeService vorgabeService,
			final GostKlausurenRaumzuweisungService raumzuweisungService) {
		this.vorgabeService = vorgabeService;
		this.raumzuweisungService = raumzuweisungService;
	}

	/**
	 * Patcht mehrere Klausurvorgaben und aktualisiert bei einer Änderung von Dauer oder Auswahlzeit die betroffenen Raumbelegungen.
	 *
	 * @param patches die Patch-Daten
	 *
	 * @return die gepatchten Vorgaben und Raumdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData patchMultiple(final Collection<GostKlausurenVorgabePatchRequest> patches)
			throws ApiOperationException {
		return transactional(() -> patchMultipleInTransaction(patches));
	}

	private GostKlausurenPatchResponseData patchMultipleInTransaction(final Collection<GostKlausurenVorgabePatchRequest> patches) {
		if (patches == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Patchen müssen Daten angegeben werden. Null ist nicht zulässig.");
		}
		for (final GostKlausurenVorgabePatchRequest patch : patches) {
			if ((patch == null) || (patch.id == null)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Jeder Patch muss eine ID der Klausurvorgabe enthalten.");
			}
		}
		final Map<Long, GostKlausurvorgabe> vorgabenVorherById = vorgabeService.getMapByIds(patches.stream().map(patch -> patch.id).toList());
		final GostKlausurenPatchResponseData result = new GostKlausurenPatchResponseData();
		result.vorgabenPatched.addAll(vorgabeService.patchMultiple(patches));
		final Set<Long> vorgabeIdsMitGeaenderterDauer = getVorgabeIdsMitGeaenderterDauer(vorgabenVorherById, result.vorgabenPatched);
		if (!vorgabeIdsMitGeaenderterDauer.isEmpty()) {
			result.addAll(raumzuweisungService.updateRaeumeZuKlausurvorgaben(vorgabeIdsMitGeaenderterDauer));
		}
		return result;
	}

	private static Set<Long> getVorgabeIdsMitGeaenderterDauer(final Map<Long, GostKlausurvorgabe> vorgabenVorherById,
			final List<GostKlausurvorgabe> vorgabenNachher) {
		final Map<Long, GostKlausurvorgabe> vorgabenNachherById = new HashMap<>();
		for (final GostKlausurvorgabe vorgabe : vorgabenNachher) {
			vorgabenNachherById.put(vorgabe.id, vorgabe);
		}
		final Set<Long> result = new HashSet<>();
		for (final Map.Entry<Long, GostKlausurvorgabe> entry : vorgabenNachherById.entrySet()) {
			final GostKlausurvorgabe vorher = vorgabenVorherById.get(entry.getKey());
			final GostKlausurvorgabe nachher = entry.getValue();
			if (!Objects.equals(vorher.dauer, nachher.dauer) || !Objects.equals(vorher.auswahlzeit, nachher.auswahlzeit)) {
				result.add(entry.getKey());
			}
		}
		return result;
	}

}
