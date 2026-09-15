package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostNachschreibterminblockungKonfiguration;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.klausuren.GostKlausurplanManager;
import de.svws_nrw.core.utils.gost.klausuren.KlausurblockungNachschreiberAlgorithmus;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenSchuelerklausurterminRepository;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenTerminRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für Nachschreibtermin-Blockungen.
 */
public final class GostKlausurenNachschreibterminBlockungService {

	private final GostKlausurenVorgabeService vorgabeService;
	private final GostKlausurenTerminRepository terminRepository;
	private final GostKlausurenKursklausurService kursklausurService;
	private final GostKlausurenSchuelerklausurService schuelerklausurService;
	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenSchuelerklausurterminRepository schuelerklausurterminRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param vorgabeService der Service für Klausurvorgaben
	 * @param terminRepository das Repository für Klausurtermine
	 * @param kursklausurService der Service für Kursklausuren
	 * @param schuelerklausurService der Service für Schülerklausuren
	 * @param schuelerklausurterminService der Service für Schülerklausurtermine
	 * @param schuelerklausurterminRepository das Repository für Schülerklausurtermine
	 * @param schuljahresabschnitteRepository das Repository für Schuljahresabschnitte
	 */
	public GostKlausurenNachschreibterminBlockungService(final GostKlausurenVorgabeService vorgabeService,
			final GostKlausurenTerminRepository terminRepository,
			final GostKlausurenKursklausurService kursklausurService,
			final GostKlausurenSchuelerklausurService schuelerklausurService,
			final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenSchuelerklausurterminRepository schuelerklausurterminRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository) {
		this.vorgabeService = vorgabeService;
		this.terminRepository = terminRepository;
		this.kursklausurService = kursklausurService;
		this.schuelerklausurService = schuelerklausurService;
		this.schuelerklausurterminService = schuelerklausurterminService;
		this.schuelerklausurterminRepository = schuelerklausurterminRepository;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
	}

	/**
	 * Blockt Nachschreibtermine.
	 *
	 * @param config die Konfiguration
	 *
	 * @return die aktualisierten Klausurdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenKlausurdaten blocken(final GostNachschreibterminblockungKonfiguration config) throws ApiOperationException {
		return transactional(() -> new BlockungRun(config).execute());
	}

	private final class BlockungRun {

		private final GostNachschreibterminblockungKonfiguration config;
		private final Map<Long, DTOGostKlausurenTermine> neueTermineByBlockungsId = new HashMap<>();
		private final Map<DTOGostKlausurenSchuelerklausurenTermine, DTOGostKlausurenTermine> neueTermineByNachschreiber = new IdentityHashMap<>();
		private Map<Long, DTOGostKlausurenSchuelerklausurenTermine> nachschreiberById;
		private GostKlausurplanManager klausurplanManager;

		private BlockungRun(final GostNachschreibterminblockungKonfiguration config) {
			this.config = config;
		}

		private GostKlausurenKlausurdaten execute() {
			// Fachliche Eigenschaften kommen aus der Datenbank, nicht aus den übergebenen Anzeigeobjekten.
			nachschreiberById = getNachschreiberById();
			config.schuelerklausurtermine = nachschreiberById.values().stream().map(GostKlausurenSchuelerklausurterminService::toApi).toList();
			klausurplanManager = createKlausurplanManager(getManagerSchuelerklausurtermine());
			config.termine = config.termine.stream().map(t -> t.id).distinct().map(klausurplanManager::terminGetByIdOrException).toList();
			config.schuelerklausurtermine = config.schuelerklausurtermine.stream()
					.map(skt -> klausurplanManager.schuelerklausurterminGetByIdOrException(skt.id)).toList();
			for (final GostSchuelerklausurtermin skt : config.schuelerklausurtermine) {
				if ((skt.folgeNr <= 0) || (skt.idTermin != null) || !klausurplanManager.istSchuelerklausurterminAktuell(skt)) {
					throw new ApiOperationException(Status.CONFLICT, "Die Auswahl enthält bereits verplante oder nicht mehr aktuelle Nachschreibversuche. Aktualisieren Sie die Auswahl.");
				}
			}
			for (final Pair<GostSchuelerklausurtermin, Long> zuordnung : new KlausurblockungNachschreiberAlgorithmus().berechne(config, klausurplanManager)) {
				verarbeiteZuordnung(zuordnung);
			}
			persistiereBlockung();
			return createResponse();
		}

		private List<GostSchuelerklausurtermin> getManagerSchuelerklausurtermine() {
			final List<GostSchuelerklausurtermin> result = new ArrayList<>(config.schuelerklausurtermine);
			result.addAll(schuelerklausurterminService.getListByTerminIds(config.termine.stream().map(t -> t.id).toList()));
			return schuelerklausurterminService.getListBySchuelerklausurIds(
					result.stream().map(skt -> skt.idSchuelerklausur).distinct().toList());
		}

		private GostKlausurplanManager createKlausurplanManager(final List<GostSchuelerklausurtermin> schuelerklausurtermine) {
			final List<GostSchuelerklausur> schuelerklausuren = schuelerklausurService.getListByIds(
					schuelerklausurtermine.stream().map(skt -> skt.idSchuelerklausur).distinct().toList());
			if (!schuelerklausurtermine.isEmpty() && schuelerklausuren.isEmpty()) {
				throw new ApiOperationException(Status.CONFLICT, "Schülerklausuren zu Schülerklausurterminen nicht gefunden.");
			}
			final List<GostKursklausur> kursklausuren = kursklausurService.getListByIds(
					schuelerklausuren.stream().map(sk -> sk.idKursklausur).distinct().toList());
			final List<Long> terminIds = new ArrayList<>(config.termine.stream().map(t -> t.id).toList());
			terminIds.addAll(kursklausuren.stream().map(k -> k.idTermin).filter(Objects::nonNull).toList());
			terminIds.addAll(schuelerklausurtermine.stream().map(skt -> skt.idTermin).filter(Objects::nonNull).toList());
			final List<Long> eindeutigeTerminIds = terminIds.stream().distinct().toList();
			final List<GostKlausurtermin> termine = terminRepository.findListByIds(eindeutigeTerminIds).stream()
					.map(GostKlausurenTerminService::toApi).toList();
			if (termine.size() != eindeutigeTerminIds.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Mindestens ein Klausurtermin wurde nicht gefunden.");
			}
			return new GostKlausurplanManager(vorgabeService.getListByIds(kursklausuren.stream().map(k -> k.idVorgabe).toList()),
					kursklausuren, termine, schuelerklausuren, schuelerklausurtermine);
		}

		private Map<Long, DTOGostKlausurenSchuelerklausurenTermine> getNachschreiberById() {
			final List<Long> ids = config.schuelerklausurtermine.stream().map(skt -> skt.id).distinct().toList();
			final Map<Long, DTOGostKlausurenSchuelerklausurenTermine> result = schuelerklausurterminRepository.findMapByIds(ids);
			if (result.size() != ids.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Mindestens ein Schülerklausurtermin wurde nicht gefunden.");
			}
			return result;
		}

		private void verarbeiteZuordnung(final Pair<GostSchuelerklausurtermin, Long> zuordnung) {
			final DTOGostKlausurenSchuelerklausurenTermine dtoSkt = DeveloperNotificationException.ifMapGetIsNull(nachschreiberById, zuordnung.a.id);
			if (zuordnung.b >= 0) {
				dtoSkt.Termin_ID = zuordnung.b;
				return;
			}
			final DTOGostKlausurenTermine neuerTermin = getOrCreateTerminZuBlockungsId(zuordnung.b, zuordnung.a);
			neueTermineByNachschreiber.put(dtoSkt, neuerTermin);
		}

		private DTOGostKlausurenTermine getOrCreateTerminZuBlockungsId(final long blockungsId,
				final GostSchuelerklausurtermin schuelerklausurtermin) {
			final GostKlausurvorgabe vorgabe = klausurplanManager.vorgabeBySchuelerklausurtermin(schuelerklausurtermin);
			DTOGostKlausurenTermine termin = neueTermineByBlockungsId.get(blockungsId);
			if (termin == null) {
				termin = createTermin(vorgabe);
				neueTermineByBlockungsId.put(blockungsId, termin);
			}
			if (termin.Quartal != vorgabe.quartal) {
				termin.Quartal = 0;
			}
			return termin;
		}

		private DTOGostKlausurenTermine createTermin(final GostKlausurvorgabe vorgabe) {
			final GostHalbjahr gostHalbjahr = GostHalbjahr.fromIDorException(vorgabe.halbjahr);
			final DTOSchuljahresabschnitte schuljahresabschnitt = schuljahresabschnitteRepository
					.findBySchuljahrAndAbschnitt(gostHalbjahr.getSchuljahrFromAbiturjahr(vorgabe.abiturjahrgang), (vorgabe.halbjahr % 2) + 1)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Für dieses Halbjahr fehlt der Schuljahresabschnitt. Legen Sie diesen zuerst an."));
			return new DTOGostKlausurenTermine(-1L, schuljahresabschnitt.ID, vorgabe.abiturjahrgang,
					gostHalbjahr, vorgabe.quartal, false, true);
		}

		private void persistiereBlockung() {
			persistiereNeueTermine();
			setzeTerminIdsDerNachschreiber();
			schuelerklausurterminRepository.update(nachschreiberById.values());
			schuelerklausurterminRepository.flush();
		}

		private void persistiereNeueTermine() {
			if (neueTermineByBlockungsId.isEmpty()) {
				return;
			}
			terminRepository.create(neueTermineByBlockungsId.values());
			terminRepository.flush();
		}

		private void setzeTerminIdsDerNachschreiber() {
			for (final Map.Entry<DTOGostKlausurenSchuelerklausurenTermine, DTOGostKlausurenTermine> entry : neueTermineByNachschreiber.entrySet()) {
				entry.getKey().Termin_ID = entry.getValue().ID;
			}
		}

		private GostKlausurenKlausurdaten createResponse() {
			final GostKlausurenKlausurdaten blockungsDaten = new GostKlausurenKlausurdaten();
			blockungsDaten.schuelerklausurtermine =
					nachschreiberById.values().stream().map(GostKlausurenSchuelerklausurterminService::toApi).toList();
			blockungsDaten.termine = neueTermineByBlockungsId.values().stream().map(GostKlausurenTerminService::toApi).toList();
			return blockungsDaten;
		}

	}

}
