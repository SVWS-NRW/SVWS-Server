package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.klausuren.GostKlausurplanManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenKursklausurRepository;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenSchuelerklausurRepository;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenSchuelerklausurterminRepository;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.service.gost.GostKursBelegungService;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für das Erzeugen von Kursklausuren inklusive abhängiger Schülerklausurdaten.
 */
public final class GostKlausurenKursklausurCreationService {

	private final GostKlausurenVorgabeService vorgabeService;
	private final GostKlausurenKursklausurService kursklausurService;
	private final GostKlausurenKursklausurRepository kursklausurRepository;
	private final GostKlausurenSchuelerklausurRepository schuelerklausurRepository;
	private final GostKlausurenSchuelerklausurterminRepository schuelerklausurterminRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final KurseRepository kurseRepository;
	private final GostKursBelegungService kursBelegungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param vorgabeService der Service für Klausurvorgaben
	 * @param kursklausurService der Service für Kursklausuren
	 * @param kursklausurRepository das Repository für Kursklausuren
	 * @param schuelerklausurRepository das Repository für Schülerklausuren
	 * @param schuelerklausurterminRepository das Repository für Schülerklausurtermine
	 * @param schuljahresabschnitteRepository das Repository für Schuljahresabschnitte
	 * @param kurseRepository das Repository für Kurse
	 * @param kursBelegungService der Service für GOSt-Belegungen zu Kursen
	 */
	public GostKlausurenKursklausurCreationService(final GostKlausurenVorgabeService vorgabeService,
			final GostKlausurenKursklausurService kursklausurService,
			final GostKlausurenKursklausurRepository kursklausurRepository,
			final GostKlausurenSchuelerklausurRepository schuelerklausurRepository,
			final GostKlausurenSchuelerklausurterminRepository schuelerklausurterminRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final KurseRepository kurseRepository,
			final GostKursBelegungService kursBelegungService) {
		this.vorgabeService = vorgabeService;
		this.kursklausurService = kursklausurService;
		this.kursklausurRepository = kursklausurRepository;
		this.schuelerklausurRepository = schuelerklausurRepository;
		this.schuelerklausurterminRepository = schuelerklausurterminRepository;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.kurseRepository = kurseRepository;
		this.kursBelegungService = kursBelegungService;
	}

	/**
	 * Erzeugt Kursklausuren für den angegebenen Jahrgang, das Halbjahr und Quartal.
	 *
	 * @param abiturjahr der Abiturjahrgang
	 * @param halbjahr das GOSt-Halbjahr
	 * @param quartal das Quartal
	 *
	 * @return die erzeugten Klausurdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenKlausurdaten createKlausuren(final int abiturjahr, final int halbjahr, final int quartal) throws ApiOperationException {
		return transactional(() -> new CreationRun(abiturjahr, halbjahr, quartal).execute());
	}

	private static String createKurseOhneVorgabeDescription(final List<DTOKurs> kurseOhneVorgabe) {
		if (kurseOhneVorgabe.isEmpty()) {
			return null;
		}
		final String kursText = (kurseOhneVorgabe.size() == 1) ? "den Kurs" : "die Kurse";
		final String kursListe = kurseOhneVorgabe.stream().map(k -> k.KurzBez).collect(Collectors.joining(", "));
		final String wurdeText = (kurseOhneVorgabe.size() > 1) ? "n" : "";
		return String.format("Für %s %s wurde%s keine Klausur erzeugt, da die entsprechende Klausurvorgabe fehlt.", kursText, kursListe, wurdeText);
	}

	private static List<DTOGostKlausurenSchuelerklausuren> createSchuelerklausurenZuKursklausur(
			final DTOGostKlausurenKursklausuren kursklausur, final List<DTOSchuelerLernabschnittsdaten> lernabschnitte) {
		final List<DTOGostKlausurenSchuelerklausuren> result = new ArrayList<>();
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			result.add(new DTOGostKlausurenSchuelerklausuren(-1L, kursklausur.ID, lernabschnitt.Schueler_ID, true));
		}
		return result;
	}

	private static List<DTOGostKlausurenSchuelerklausurenTermine> createSchuelerklausurenTermineZuSchuelerklausuren(
			final List<DTOGostKlausurenSchuelerklausuren> schuelerklausuren) {
		final List<DTOGostKlausurenSchuelerklausurenTermine> result = new ArrayList<>();
		for (final DTOGostKlausurenSchuelerklausuren schuelerklausur : schuelerklausuren) {
			result.add(new DTOGostKlausurenSchuelerklausurenTermine(-1L, schuelerklausur.ID, 0));
		}
		return result;
	}

	private final class CreationRun {

		private final int abiturjahr;
		private final int halbjahr;
		private final int quartal;
		private final GostHalbjahr gostHalbjahr;
		private final List<DTOGostKlausurenKursklausuren> kursklausuren = new ArrayList<>();
		private final Map<DTOGostKlausurenKursklausuren, List<DTOSchuelerLernabschnittsdaten>> lernabschnitteByKursklausur = new IdentityHashMap<>();
		private final List<DTOGostKlausurenSchuelerklausuren> schuelerklausuren = new ArrayList<>();
		private final List<DTOGostKlausurenSchuelerklausurenTermine> schuelerklausurtermine = new ArrayList<>();
		private final List<DTOKurs> kurseOhneVorgabe = new ArrayList<>();
		private List<GostKlausurvorgabe> vorgaben;
		private GostKlausurplanManager klausurplanManager;
		private Map<Long, Map<Long, GostKursklausur>> kursklausurenByKursAndVorgabe;
		private DTOSchuljahresabschnitte schuljahresabschnitt;
		private List<DTOKurs> kurse;

		private CreationRun(final int abiturjahr, final int halbjahr, final int quartal) {
			this.abiturjahr = abiturjahr;
			this.halbjahr = halbjahr;
			this.quartal = quartal;
			this.gostHalbjahr = GostHalbjahr.fromID(halbjahr);
		}

		private GostKlausurenKlausurdaten execute() {
			loadCreationContext();
			collectKursklausuren();
			kursklausurRepository.create(kursklausuren);
			createSchuelerklausurenZuKursklausuren();
			schuelerklausurRepository.create(schuelerklausuren);
			schuelerklausurtermine.addAll(createSchuelerklausurenTermineZuSchuelerklausuren(schuelerklausuren));
			schuelerklausurterminRepository.create(schuelerklausurtermine);
			flushRepositories();
			return createResponse();
		}

		private void loadCreationContext() {
			vorgaben = vorgabeService.getListByAbiturjahr(abiturjahr, halbjahr, false);
			if (vorgaben.isEmpty()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Keine Klausurvorgaben für dieses Halbjahr definiert.");
			}
			klausurplanManager = new GostKlausurplanManager(vorgaben);
			kursklausurenByKursAndVorgabe = getExistingKursklausurenByKursAndVorgabe();
			schuljahresabschnitt = schuljahresabschnitteRepository
					.findBySchuljahrAndAbschnitt(gostHalbjahr.getSchuljahrFromAbiturjahr(abiturjahr), (gostHalbjahr.id % 2) + 1)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Noch kein Schuljahresabschnitt für dieses Halbjahr definiert."));
			kurse = kurseRepository.getListBySchuljahresabschnittAndJahrgang(schuljahresabschnitt.ID, gostHalbjahr.jahrgang);
		}

		private Map<Long, Map<Long, GostKursklausur>> getExistingKursklausurenByKursAndVorgabe() {
			final List<GostKursklausur> existing = kursklausurService.getListByVorgabeIds(vorgaben.stream().map(v -> v.id).toList());
			return existing.stream().collect(Collectors.groupingBy(k -> k.idKurs, Collectors.toMap(k -> k.idVorgabe, Function.identity())));
		}

		private void collectKursklausuren() {
			for (final DTOKurs kurs : kurse) {
				collectKursklausurenForKurs(kurs);
			}
		}

		private void collectKursklausurenForKurs(final DTOKurs kurs) {
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte =
					kursBelegungService.getAktiveSchriftlicheGostSchuelerLernabschnittsdatenByKurs(schuljahresabschnitt.Jahr, kurs.ID);
			final List<GostKlausurvorgabe> kursVorgaben = klausurplanManager.vorgabeGetMengeByHalbjahrAndQuartalAndKursartallgAndFachid(abiturjahr,
					gostHalbjahr, quartal, GostKursart.fromKuerzelOrException(kurs.KursartAllg), kurs.Fach_ID);
			if (kursVorgaben.isEmpty() && !lernabschnitte.isEmpty()) {
				kurseOhneVorgabe.add(kurs);
			}
			for (final GostKlausurvorgabe vorgabe : kursVorgaben) {
				addKursklausurIfMissing(kurs, vorgabe, lernabschnitte);
			}
		}

		private void addKursklausurIfMissing(final DTOKurs kurs, final GostKlausurvorgabe vorgabe,
				final List<DTOSchuelerLernabschnittsdaten> lernabschnitte) {
			if (hasExistingKursklausur(kurs.ID, vorgabe.id) || lernabschnitte.isEmpty()) {
				return;
			}
			final DTOGostKlausurenKursklausuren kursklausur = new DTOGostKlausurenKursklausuren(-1L, vorgabe.id, kurs.ID);
			kursklausuren.add(kursklausur);
			lernabschnitteByKursklausur.put(kursklausur, lernabschnitte);
		}

		private boolean hasExistingKursklausur(final long kursId, final long vorgabeId) {
			return kursklausurenByKursAndVorgabe.containsKey(kursId) && kursklausurenByKursAndVorgabe.get(kursId).containsKey(vorgabeId);
		}

		private void createSchuelerklausurenZuKursklausuren() {
			for (final DTOGostKlausurenKursklausuren kursklausur : kursklausuren) {
				schuelerklausuren.addAll(createSchuelerklausurenZuKursklausur(kursklausur, lernabschnitteByKursklausur.get(kursklausur)));
			}
		}

		private void flushRepositories() {
			kursklausurRepository.flush();
			schuelerklausurRepository.flush();
			schuelerklausurterminRepository.flush();
		}

		private GostKlausurenKlausurdaten createResponse() {
			final GostKlausurenKlausurdaten result = new GostKlausurenKlausurdaten();
			result.kursklausuren = kursklausuren.stream().map(GostKlausurenKursklausurService::toApi).toList();
			result.schuelerklausuren = schuelerklausuren.stream().map(GostKlausurenSchuelerklausurService::toApi).toList();
			result.schuelerklausurtermine = schuelerklausurtermine.stream().map(GostKlausurenSchuelerklausurterminService::toApi).toList();
			result.description = createKurseOhneVorgabeDescription(kurseOhneVorgabe);
			return result;
		}

	}

}
