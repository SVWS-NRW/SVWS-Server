package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungErgebnis;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungErgebnisTermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungDaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausurRich;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.klausuren.GostKlausurplanManager;
import de.svws_nrw.core.utils.gost.klausuren.KlausurterminblockungAlgorithmus;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenKursklausurRepository;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenTerminRepository;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für Kursklausur-Blockungen.
 */
public final class GostKlausurenKursklausurBlockungService {

	private final GostKlausurenVorgabeService vorgabeService;
	private final GostKlausurenTerminRepository terminRepository;
	private final GostKlausurenKursklausurRepository kursklausurRepository;
	private final GostKlausurenSchuelerklausurService schuelerklausurService;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final KurseRepository kurseRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param vorgabeService der Service für Klausurvorgaben
	 * @param terminRepository das Repository für Klausurtermine
	 * @param kursklausurRepository das Repository für Kursklausuren
	 * @param schuelerklausurService der Service für Schülerklausuren
	 * @param schuljahresabschnitteRepository das Repository für Schuljahresabschnitte
	 * @param kurseRepository das Repository für Kurse
	 */
	public GostKlausurenKursklausurBlockungService(final GostKlausurenVorgabeService vorgabeService,
			final GostKlausurenTerminRepository terminRepository,
			final GostKlausurenKursklausurRepository kursklausurRepository,
			final GostKlausurenSchuelerklausurService schuelerklausurService,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final KurseRepository kurseRepository) {
		this.vorgabeService = vorgabeService;
		this.terminRepository = terminRepository;
		this.kursklausurRepository = kursklausurRepository;
		this.schuelerklausurService = schuelerklausurService;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.kurseRepository = kurseRepository;
	}

	/**
	 * Blockt Kursklausuren.
	 *
	 * @param blockungDaten die Blockungsdaten
	 *
	 * @return die aktualisierten Klausurdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenKlausurdaten blocken(final GostKlausurterminblockungDaten blockungDaten) throws ApiOperationException {
		return transactional(() -> new BlockungRun(blockungDaten).execute());
	}

	private static GostKursklausurRich createRichKursklausur(final GostKursklausur kursklausur, final GostKlausurvorgabe vorgabe,
			final DTOKurs kurs, final List<GostSchuelerklausur> schuelerklausuren) {
		if (kurs == null) {
			throw new ApiOperationException(Status.CONFLICT, "Kurs zur Kursklausur nicht gefunden.");
		}
		final GostKursklausurRich richKursklausur = new GostKursklausurRich();
		richKursklausur.abiturjahrgang = vorgabe.abiturjahrgang;
		richKursklausur.bemerkung = kursklausur.bemerkung;
		richKursklausur.halbjahr = vorgabe.halbjahr;
		richKursklausur.id = kursklausur.id;
		richKursklausur.idFach = vorgabe.idFach;
		richKursklausur.idKurs = kursklausur.idKurs;
		richKursklausur.idLehrer = kurs.Lehrer_ID;
		richKursklausur.idTermin = kursklausur.idTermin;
		richKursklausur.idVorgabe = vorgabe.id;
		richKursklausur.kursart = vorgabe.kursart;
		richKursklausur.kursKurzbezeichnung = kurs.KurzBez;
		richKursklausur.kursSchiene = parseKursSchienen(kurs.Schienen);
		richKursklausur.quartal = vorgabe.quartal;
		richKursklausur.schuelerIds = schuelerklausuren.stream().map(s -> s.idSchueler).toList();
		richKursklausur.startzeit = kursklausur.startzeit;
		return richKursklausur;
	}

	private static int[] parseKursSchienen(final String schienen) {
		try {
			return Stream.of(schienen.split(",")).mapToInt(Integer::parseInt).toArray();
		} catch (@SuppressWarnings("unused") final NullPointerException | NumberFormatException e) {
			return new int[0];
		}
	}

	private final class BlockungRun {

		private final GostKlausurterminblockungDaten blockungDaten;
		private final List<DTOGostKlausurenTermine> neueTermine = new ArrayList<>();
		private final List<DTOGostKlausurenKursklausuren> aktualisierteKursklausuren = new ArrayList<>();
		private final Map<DTOGostKlausurenTermine, List<DTOGostKlausurenKursklausuren>> kursklausurenByTermin = new IdentityHashMap<>();
		private final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitteByVorgabe = new HashMap<>();

		private BlockungRun(final GostKlausurterminblockungDaten blockungDaten) {
			this.blockungDaten = blockungDaten;
		}

		private GostKlausurenKlausurdaten execute() {
			blockungDaten.kursklausurenRich = createRichKursklausuren(blockungDaten.kursklausuren);
			final GostKlausurterminblockungErgebnis ergebnis = new KlausurterminblockungAlgorithmus().apply(blockungDaten);
			for (final GostKlausurterminblockungErgebnisTermin ergebnisTermin : ergebnis.termine) {
				verarbeiteErgebnisTermin(ergebnisTermin);
			}
			persistiereBlockung();
			return createResponse();
		}

		private List<GostKursklausurRich> createRichKursklausuren(final List<GostKursklausur> kursklausuren) {
			final List<GostKursklausurRich> kursklausurenRich = new ArrayList<>();
			if (kursklausuren.isEmpty()) {
				return kursklausurenRich;
			}

			final List<GostKlausurvorgabe> vorgaben = vorgabeService.getListByIds(kursklausuren.stream().map(k -> k.idVorgabe).toList());
			if (vorgaben.isEmpty()) {
				return new ArrayList<>();
			}

			final GostKlausurplanManager manager = new GostKlausurplanManager(vorgaben);
			final Map<Long, List<GostSchuelerklausur>> schuelerklausurenByKursklausur =
					schuelerklausurService.getListByKursklausurIds(kursklausuren.stream().map(k -> k.id).toList()).stream()
							.filter(sk -> sk.aktiv).collect(Collectors.groupingBy(s -> s.idKursklausur));
			if (schuelerklausurenByKursklausur.isEmpty()) {
				return new ArrayList<>();
			}

			final Map<Long, DTOKurs> kurseById = kurseRepository.findListByIds(kursklausuren.stream().map(k -> k.idKurs).distinct().toList())
					.stream().collect(Collectors.toMap(k -> k.ID, k -> k));
			for (final GostKursklausur kursklausur : kursklausuren) {
				final GostKlausurvorgabe vorgabe = manager.vorgabeGetByIdOrException(kursklausur.idVorgabe);
				final List<GostSchuelerklausur> schuelerklausuren = schuelerklausurenByKursklausur.get(kursklausur.id);
				if ((schuelerklausuren != null) && !schuelerklausuren.isEmpty()) {
					kursklausurenRich.add(createRichKursklausur(kursklausur, vorgabe, kurseById.get(kursklausur.idKurs), schuelerklausuren));
				}
			}
			return kursklausurenRich;
		}

		private void verarbeiteErgebnisTermin(final GostKlausurterminblockungErgebnisTermin ergebnisTermin) {
			final List<DTOGostKlausurenKursklausuren> kursklausuren = kursklausurRepository.findListByIds(ergebnisTermin.idsKursklausuren);
			final GostKlausurplanManager manager = new GostKlausurplanManager(
					vorgabeService.getListByIds(kursklausuren.stream().map(k -> k.Vorgabe_ID).toList()));
			DTOGostKlausurenTermine termin = null;
			for (final DTOGostKlausurenKursklausuren kursklausur : kursklausuren) {
				final GostKlausurvorgabe vorgabe = manager.vorgabeGetByIdOrException(kursklausur.Vorgabe_ID);
				if (termin == null) {
					termin = createTermin(vorgabe);
				}
				validateTerminPasstZuVorgabe(termin, vorgabe);
			}
			kursklausurenByTermin.put(termin, kursklausuren);
		}

		private DTOGostKlausurenTermine createTermin(final GostKlausurvorgabe vorgabe) {
			final GostHalbjahr gostHalbjahr = GostHalbjahr.fromIDorException(vorgabe.halbjahr);
			final DTOSchuljahresabschnitte schuljahresabschnitt = getSchuljahresabschnitt(vorgabe, gostHalbjahr);
			final DTOGostKlausurenTermine termin = new DTOGostKlausurenTermine(-1L, schuljahresabschnitt.ID, vorgabe.abiturjahrgang,
					gostHalbjahr, vorgabe.quartal, true, false);
			neueTermine.add(termin);
			return termin;
		}

		private DTOSchuljahresabschnitte getSchuljahresabschnitt(final GostKlausurvorgabe vorgabe, final GostHalbjahr gostHalbjahr) {
			return schuljahresabschnitteByVorgabe.computeIfAbsent(vorgabe.id, id -> schuljahresabschnitteRepository
					.findBySchuljahrAndAbschnitt(gostHalbjahr.getSchuljahrFromAbiturjahr(vorgabe.abiturjahrgang), (vorgabe.halbjahr % 2) + 1)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Noch kein Schuljahresabschnitt für dieses Halbjahr definiert.")));
		}

		private void validateTerminPasstZuVorgabe(final DTOGostKlausurenTermine termin, final GostKlausurvorgabe vorgabe) {
			if ((termin.Abi_Jahrgang != vorgabe.abiturjahrgang) || (termin.Halbjahr != GostHalbjahr.fromIDorException(vorgabe.halbjahr))
					|| (termin.Quartal != vorgabe.quartal)) {
				throw new ApiOperationException(Status.CONFLICT, "Kursklausuren mit unterschiedlichen Jahrgängen, Halbjahren oder Quartalen an einem Termin.");
			}
		}

		private void persistiereBlockung() {
			persistiereNeueTermine();
			setzeTerminIdsDerKursklausuren();
			kursklausurRepository.update(aktualisierteKursklausuren);
			kursklausurRepository.flush();
		}

		private void persistiereNeueTermine() {
			if (neueTermine.isEmpty()) {
				return;
			}
			terminRepository.create(neueTermine);
			terminRepository.flush();
		}

		private void setzeTerminIdsDerKursklausuren() {
			for (final Map.Entry<DTOGostKlausurenTermine, List<DTOGostKlausurenKursklausuren>> entry : kursklausurenByTermin.entrySet()) {
				for (final DTOGostKlausurenKursklausuren kursklausur : entry.getValue()) {
					kursklausur.Termin_ID = entry.getKey().ID;
					aktualisierteKursklausuren.add(kursklausur);
				}
			}
		}

		private GostKlausurenKlausurdaten createResponse() {
			final GostKlausurenKlausurdaten response = new GostKlausurenKlausurdaten();
			response.termine = neueTermine.stream().map(GostKlausurenTerminService::toApi).toList();
			response.kursklausuren = aktualisierteKursklausuren.stream().map(GostKlausurenKursklausurService::toApi).toList();
			return response;
		}

	}

}
