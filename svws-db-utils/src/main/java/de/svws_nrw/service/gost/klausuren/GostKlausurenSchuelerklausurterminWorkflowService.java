package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.core.utils.gost.klausuren.GostKlausurplanManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Gemeinsamer Workflow für das Erstellen und Patchen von Schülerklausurterminen, einschließlich Fachprüfung und Raumdaten.
 */
public final class GostKlausurenSchuelerklausurterminWorkflowService {

	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenSchuelerklausurService schuelerklausurService;
	private final GostKlausurenKursklausurService kursklausurService;
	private final GostKlausurenVorgabeService vorgabeService;
	private final GostKlausurenTerminService terminService;
	private final GostKlausurenRaumzuweisungService raumzuweisungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuelerklausurterminService der Service für Schülerklausurtermine
	 * @param schuelerklausurService der Service für Schülerklausuren
	 * @param kursklausurService der Service für Kursklausuren
	 * @param vorgabeService der Service für Vorgaben
	 * @param terminService der Service für Termine
	 * @param raumzuweisungService der Service für Raumzuweisungen
	 */
	public GostKlausurenSchuelerklausurterminWorkflowService(final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenSchuelerklausurService schuelerklausurService, final GostKlausurenKursklausurService kursklausurService,
			final GostKlausurenVorgabeService vorgabeService, final GostKlausurenTerminService terminService,
			final GostKlausurenRaumzuweisungService raumzuweisungService) {
		this.schuelerklausurterminService = schuelerklausurterminService;
		this.schuelerklausurService = schuelerklausurService;
		this.kursklausurService = kursklausurService;
		this.vorgabeService = vorgabeService;
		this.terminService = terminService;
		this.raumzuweisungService = raumzuweisungService;
	}

	/**
	 * Patcht einen Schülerklausurtermin.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die geänderten Raumdaten
	 */
	public GostKlausurenPatchResponseData patch(final GostKlausurenSchuelerklausurterminPatchRequest patchRequest) {
		return patchMultiple(List.of(patchRequest));
	}

	/**
	 * Patcht mehrere Schülerklausurtermine.
	 *
	 * @param patchRequests die Patch-Daten
	 *
	 * @return die geänderten Raumdaten
	 */
	public GostKlausurenPatchResponseData patchMultiple(final Collection<GostKlausurenSchuelerklausurterminPatchRequest> patchRequests) {
		return transactional(() -> patchMultipleInTransaction(patchRequests));
	}

	private GostKlausurenPatchResponseData patchMultipleInTransaction(final Collection<GostKlausurenSchuelerklausurterminPatchRequest> patchRequests) {
		final List<GostKlausurenSchuelerklausurterminPatchRequest> patches = List.copyOf(patchRequests);
		final List<Long> patchIds = patches.stream().map(patch -> patch.id).distinct().toList();
		final Map<Long, GostSchuelerklausurtermin> beforeById = schuelerklausurterminService.getListByIds(patchIds).stream()
				.collect(Collectors.toMap(schuelerklausurtermin -> schuelerklausurtermin.id, Function.identity()));
		if (beforeById.size() != patchIds.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Mindestens ein Schülerklausurtermin ist nicht mehr vorhanden. Aktualisieren Sie die Ansicht und wiederholen Sie die Änderung.");
		}
		final Map<Long, Long> zuweisungen = new HashMap<>();
		for (final GostKlausurenSchuelerklausurterminPatchRequest patch : patches) {
			if (patch.idTermin.isPresent()) {
				zuweisungen.put(patch.id, patch.idTermin.get());
			}
		}
		validateNachschreiberZielzustand(zuweisungen, zuweisungen.keySet().stream().map(beforeById::get).toList(), null);
		final List<Long> idsMitTerminwechsel = new ArrayList<>();
		final List<GostSchuelerklausurtermin> versucheMitStartzeitwechsel = new ArrayList<>();
		for (final GostSchuelerklausurtermin after : schuelerklausurterminService.patchMultiple(patches)) {
			final GostSchuelerklausurtermin before = beforeById.get(after.id);
			if (!Objects.equals(before.idTermin, after.idTermin)) {
				idsMitTerminwechsel.add(after.id);
			}
			if (!Objects.equals(before.startzeit, after.startzeit)) {
				versucheMitStartzeitwechsel.add(after);
			}
		}
		final GostKlausurenPatchResponseData result = new GostKlausurenPatchResponseData();
		if (!idsMitTerminwechsel.isEmpty()) {
			result.addAll(raumzuweisungService.loescheRaumzuweisungenFuerSchuelerklausurtermine(idsMitTerminwechsel));
		}
		if (!versucheMitStartzeitwechsel.isEmpty()) {
			result.addAll(raumzuweisungService.updateRaeumeZuSchuelerklausurterminen(versucheMitStartzeitwechsel));
		}
		return result;
	}

	/**
	 * Erstellt einen Schülerklausurtermin und entfernt Raumzuweisungen des bisherigen letzten Schülerklausurtermins.
	 *
	 * @param createRequest die Create-Daten
	 *
	 * @return die Änderung des Schülerklausurtermins und der bereinigten Raumdaten
	 */
	public GostKlausurenPatchResponseData create(final GostKlausurenSchuelerklausurterminCreateRequest createRequest) {
		return transactional(() -> {
			final List<DTOGostKlausurenSchuelerklausurenTermine> vorhandeneTermine =
					schuelerklausurterminService.getListBySchuelerklausurId(createRequest.idSchuelerklausur);
			final int folgeNr = GostKlausurenSchuelerklausurterminService.getNaechsteFolgeNr(vorhandeneTermine);
			final GostSchuelerklausurtermin neuerVersuch = new GostSchuelerklausurtermin();
			neuerVersuch.idSchuelerklausur = createRequest.idSchuelerklausur;
			neuerVersuch.folgeNr = folgeNr;
			neuerVersuch.idTermin = createRequest.idTermin.orElse(null);
			validateNachschreiberZielzustand(Map.of(), List.of(neuerVersuch), neuerVersuch);
			final GostKlausurenPatchResponseData result = loescheRaumzuweisungDesLetztenTermins(vorhandeneTermine);
			result.schuelerklausurterminePatched.add(schuelerklausurterminService.create(createRequest, folgeNr));
			return result;
		});
	}

	private GostKlausurenPatchResponseData loescheRaumzuweisungDesLetztenTermins(
			final List<DTOGostKlausurenSchuelerklausurenTermine> vorhandeneTermine) {
		return vorhandeneTermine.stream()
				.max(Comparator.comparingInt(dto -> dto.Folge_Nr))
				.map(dto -> raumzuweisungService.loescheRaumzuweisungenFuerSchuelerklausurtermine(List.of(dto.ID)))
				.orElseGet(GostKlausurenPatchResponseData::new);
	}

	/* Prüft den gemeinsamen Endzustand auf frischen API-Objekten, bevor Create oder Patch Daten verändern.
	 * Der Datenservice erzeugt diese unabhängig von den persistenten DTOs; neuerVersuch wird lokal in create angelegt. */
	private void validateNachschreiberZielzustand(final Map<Long, Long> zuweisungen,
			final Collection<GostSchuelerklausurtermin> geaenderteVersuche, final GostSchuelerklausurtermin neuerVersuch) {
		final Set<Long> zielIds = new HashSet<>(zuweisungen.values());
		zielIds.remove(null);
		if ((neuerVersuch != null) && (neuerVersuch.idTermin != null)) {
			zielIds.add(neuerVersuch.idTermin);
		}
		if (zielIds.isEmpty()) {
			return;
		}
		final Set<Long> schuelerklausurIds = geaenderteVersuche.stream().map(skt -> skt.idSchuelerklausur).collect(Collectors.toSet());
		schuelerklausurIds.addAll(schuelerklausurterminService.getListByTerminIds(zielIds).stream().map(skt -> skt.idSchuelerklausur).toList());
		final Map<Long, GostSchuelerklausurtermin> versuche = new HashMap<>();
		final List<GostSchuelerklausurtermin> zuPruefen = new ArrayList<>();
		for (final GostSchuelerklausurtermin skt : schuelerklausurterminService.getListBySchuelerklausurIds(schuelerklausurIds)) {
			if (zuweisungen.containsKey(skt.id)) {
				if (skt.folgeNr == 0) {
					throw new ApiOperationException(Status.CONFLICT, "Haupttermine werden über die Kursklausur zugewiesen.");
				}
				skt.idTermin = zuweisungen.get(skt.id);
				zuPruefen.add(skt);
			}
			versuche.put(skt.id, skt);
		}
		if (neuerVersuch != null) {
			if (neuerVersuch.folgeNr == 0) {
				throw new ApiOperationException(Status.CONFLICT, "Eine Nachschreiberzuweisung benötigt einen Nachschreibversuch.");
			}
			neuerVersuch.id = 0;
			while (versuche.containsKey(neuerVersuch.id)) {
				neuerVersuch.id++;
			}
			versuche.put(neuerVersuch.id, neuerVersuch);
			zuPruefen.add(neuerVersuch);
		}
		final List<GostSchuelerklausur> schuelerklausuren = schuelerklausurService.getListByIds(schuelerklausurIds);
		final List<GostKursklausur> kursklausuren = kursklausurService.getListByIds(
				schuelerklausuren.stream().map(sk -> sk.idKursklausur).distinct().toList());
		final Set<Long> terminIds = new HashSet<>(zielIds);
		terminIds.addAll(kursklausuren.stream().map(kk -> kk.idTermin).filter(Objects::nonNull).toList());
		terminIds.addAll(versuche.values().stream().map(skt -> skt.idTermin).filter(Objects::nonNull).toList());
		final List<GostKlausurtermin> termine = terminService.getListByIds(terminIds);
		if (termine.size() != terminIds.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Mindestens ein ausgewählter Klausurtermin ist nicht mehr vorhanden. Aktualisieren Sie die Ansicht und wiederholen Sie die Zuordnung.");
		}
		final GostKlausurplanManager manager = new GostKlausurplanManager(
				vorgabeService.getListByIds(kursklausuren.stream().map(kk -> kk.idVorgabe).distinct().toList()),
				kursklausuren, termine, schuelerklausuren, versuche.values());
		for (final GostSchuelerklausurtermin skt : zuPruefen) {
			if (skt.idTermin == null) {
				continue;
			}
			final GostKlausurtermin termin = manager.terminGetByIdOrException(skt.idTermin);
			if (!manager.schuelerklausurterminPasstInNachschreibtermin(termin, skt)) {
				throw new ApiOperationException(Status.CONFLICT, "Die Nachschreiberzuweisung ist nicht möglich. Prüfen Sie das Terminquartal, die Zulassung für Nachschreiber und weitere Klausuren des Schülers am Zieltermin.");
			}
		}
	}
}
