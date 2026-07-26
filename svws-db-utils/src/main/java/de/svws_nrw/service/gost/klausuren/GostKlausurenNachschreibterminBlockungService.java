package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
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
			final List<GostSchuelerklausurtermin> managerSchuelerklausurtermine = getManagerSchuelerklausurtermine();
			klausurplanManager = createKlausurplanManager(managerSchuelerklausurtermine);
			nachschreiberById = getNachschreiberById();
			verarbeiteZuordnungen(new KlausurblockungNachschreiberAlgorithmus().berechne(config, klausurplanManager));
			persistiereBlockung();
			return createResponse();
		}

		private List<GostSchuelerklausurtermin> getManagerSchuelerklausurtermine() {
			final List<GostSchuelerklausurtermin> result = new ArrayList<>();
			result.addAll(config.schuelerklausurtermine);
			result.addAll(getSchuelerklausurtermineZuTerminIds(config.termine.stream().map(t -> t.id).toList(), true));
			return result;
		}

		private List<GostSchuelerklausurtermin> getSchuelerklausurtermineZuTerminIds(final List<Long> terminIds, final boolean includeAbwesend) {
			if (terminIds.isEmpty()) {
				return new ArrayList<>();
			}
			final List<GostKursklausur> kursklausuren = getKursklausurenZuTerminIds(terminIds);
			final List<GostSchuelerklausur> schuelerklausuren = getSchuelerklausurenZuKursklausuren(kursklausuren);
			final List<Long> kursSchuelerklausurIds = schuelerklausuren.stream().map(sk -> sk.id).toList();
			final Map<Long, List<GostSchuelerklausurtermin>> termineBySchuelerklausur =
					schuelerklausurterminService.getListBySchuelerklausurIds(kursSchuelerklausurIds).stream()
							.collect(Collectors.groupingBy(skt -> skt.idSchuelerklausur));
			final Map<Long, GostSchuelerklausurtermin> result = new HashMap<>();
			schuelerklausurterminService.getListByTerminIds(terminIds).forEach(skt -> result.put(skt.id, skt));
			for (final List<GostSchuelerklausurtermin> termine : termineBySchuelerklausur.values()) {
				final boolean hatNachschreiber = termine.stream().anyMatch(skt -> skt.folgeNr > 0);
				termine.stream().filter(skt -> skt.folgeNr == 0).findFirst()
						.filter(skt -> includeAbwesend || !hatNachschreiber)
						.ifPresent(skt -> result.put(skt.id, skt));
			}
			return new ArrayList<>(result.values());
		}

		private GostKlausurplanManager createKlausurplanManager(final List<GostSchuelerklausurtermin> schuelerklausurtermine) {
			final List<GostSchuelerklausur> schuelerklausuren = getSchuelerklausurenZuSchuelerklausurterminen(schuelerklausurtermine);
			final List<GostKursklausur> kursklausuren = getKursklausurenZuSchuelerklausuren(schuelerklausuren);
			return new GostKlausurplanManager(vorgabeService.getListByIds(kursklausuren.stream().map(k -> k.idVorgabe).toList()),
					kursklausuren, config.termine, schuelerklausuren, schuelerklausurtermine);
		}

		private List<GostKursklausur> getKursklausurenZuTerminIds(final List<Long> terminIds) {
			if (terminIds.isEmpty()) {
				return new ArrayList<>();
			}
			return kursklausurService.getListByTerminIds(terminIds);
		}

		private List<GostSchuelerklausur> getSchuelerklausurenZuKursklausuren(final List<GostKursklausur> kursklausuren) {
			if (kursklausuren.isEmpty()) {
				return new ArrayList<>();
			}
			return schuelerklausurService.getListByKursklausurIds(kursklausuren.stream().map(kk -> kk.id).toList());
		}

		private List<GostSchuelerklausur> getSchuelerklausurenZuSchuelerklausurterminen(final List<GostSchuelerklausurtermin> termine) {
			if (termine.isEmpty()) {
				return new ArrayList<>();
			}
			final List<GostSchuelerklausur> schuelerklausuren =
					schuelerklausurService.getListByIds(termine.stream().map(sk -> sk.idSchuelerklausur).toList());
			if (schuelerklausuren.isEmpty()) {
				throw new ApiOperationException(Status.CONFLICT, "Schülerklausuren zu Schülerklausurterminen nicht gefunden.");
			}
			return schuelerklausuren;
		}

		private List<GostKursklausur> getKursklausurenZuSchuelerklausuren(final List<GostSchuelerklausur> schuelerklausuren) {
			if (schuelerklausuren.isEmpty()) {
				return new ArrayList<>();
			}
			return kursklausurService.getListByIds(schuelerklausuren.stream().map(sk -> sk.idKursklausur).toList());
		}

		private Map<Long, DTOGostKlausurenSchuelerklausurenTermine> getNachschreiberById() {
			return schuelerklausurterminRepository.findListByIds(config.schuelerklausurtermine.stream().map(skt -> skt.id).toList())
					.stream().collect(Collectors.toMap(skt -> skt.ID, skt -> skt));
		}

		private void verarbeiteZuordnungen(final List<Pair<GostSchuelerklausurtermin, Long>> zuordnungen) {
			for (final Pair<GostSchuelerklausurtermin, Long> zuordnung : zuordnungen) {
				verarbeiteZuordnung(zuordnung);
			}
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
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Noch kein Schuljahresabschnitt für dieses Halbjahr definiert."));
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
