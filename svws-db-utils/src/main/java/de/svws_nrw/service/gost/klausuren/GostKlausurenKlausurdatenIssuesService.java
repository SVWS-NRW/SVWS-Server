package de.svws_nrw.service.gost.klausuren;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.klausuren.GostKlausurplanManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.service.gost.GostKursBelegungService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Service für Klausurdaten-Issues.
 */
public final class GostKlausurenKlausurdatenIssuesService {

	private static final List<Integer> QUARTALE = List.of(1, 2);

	private final GostKlausurenKlausurdatenService klausurdatenService;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final KurseRepository kurseRepository;
	private final GostKursBelegungService kursBelegungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param klausurdatenService der Service für Klausurdaten
	 * @param schuljahresabschnitteRepository das Repository für Schuljahresabschnitte
	 * @param kurseRepository das Repository für Kurse
	 * @param kursBelegungService der Service für GOSt-Belegungen zu Kursen
	 */
	public GostKlausurenKlausurdatenIssuesService(final GostKlausurenKlausurdatenService klausurdatenService,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final KurseRepository kurseRepository,
			final GostKursBelegungService kursBelegungService) {
		this.klausurdatenService = klausurdatenService;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.kurseRepository = kurseRepository;
		this.kursBelegungService = kursBelegungService;
	}

	/**
	 * Ermittelt Klausurdaten-Issues.
	 *
	 * @param abiturjahr der Abiturjahrgang
	 * @param halbjahr das GOSt-Halbjahr
	 *
	 * @return die Halbjahresdaten mit Klausurdaten-Issues
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenHalbjahresdaten getKlausurdatenIssues(final int abiturjahr, final GostHalbjahr halbjahr)
			throws ApiOperationException {
		final GostKlausurenKlausurdaten vorhandeneKlausurdaten = klausurdatenService.getKlausurdaten(abiturjahr, halbjahr.id, true);
		final DTOSchuljahresabschnitte schuljahresabschnitt =
				schuljahresabschnitteRepository.findBySchuljahrAndAbschnitt(halbjahr.getSchuljahrFromAbiturjahr(abiturjahr), halbjahr.halbjahr)
						.orElse(null);
		return ermittleKlausurdatenIssues(abiturjahr, halbjahr, schuljahresabschnitt, vorhandeneKlausurdaten);
	}

	private GostKlausurenHalbjahresdaten ermittleKlausurdatenIssues(final int abiturjahr,
			final GostHalbjahr halbjahr, final DTOSchuljahresabschnitte schuljahresabschnitt,
			final GostKlausurenKlausurdaten vorhandeneKlausurdaten) throws ApiOperationException {
		if (schuljahresabschnitt == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Noch kein Schuljahresabschnitt für den Abiturjahrgang %d und das Halbjahr %s definiert."
							.formatted(abiturjahr, halbjahr.kuerzel));
		}

		final GostKlausurenHalbjahresdaten klausurdatenIssues = new GostKlausurenHalbjahresdaten(abiturjahr, halbjahr.id);
		final List<DTOKurs> kurse = kurseRepository.getListBySchuljahresabschnittAndJahrgang(schuljahresabschnitt.ID, halbjahr.jahrgang);
		final GostKlausurplanManager manager = new GostKlausurplanManager(vorhandeneKlausurdaten);

		for (final DTOKurs kurs : kurse) {
			final GostKursart kursart = GostKursart.fromKuerzelOrException(kurs.KursartAllg);
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte =
					kursBelegungService.getAktiveSchriftlicheGostSchuelerLernabschnittsdatenByKurs(schuljahresabschnitt.Jahr, kurs.ID);
			for (final int quartal : QUARTALE) {
				final GostKlausurvorgabe vorgabe = manager
						.vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(abiturjahr, halbjahr, quartal, kursart, kurs.Fach_ID);

				if (lernabschnitte.isEmpty()) {
					bearbeiteKursOhneSchueler(klausurdatenIssues, manager, vorgabe, kurs);
					continue;
				}
				if (vorgabe == null) {
					erzeugeFehlendeVorgabe(klausurdatenIssues, manager, abiturjahr, halbjahr, quartal, kurs, kursart);
					continue;
				}
				bearbeiteKursMitVorgabe(klausurdatenIssues, manager, vorgabe, kurs, lernabschnitte);
			}
		}
		return klausurdatenIssues;
	}

	private static void bearbeiteKursOhneSchueler(final GostKlausurenHalbjahresdaten klausurdatenIssues, final GostKlausurplanManager manager,
			final GostKlausurvorgabe vorgabe, final DTOKurs kurs) {
		if (vorgabe == null) {
			return;
		}
		final GostKursklausur kursklausur = manager.kursklausurByVorgabeAndKursid(vorgabe, kurs.ID);
		if (kursklausur == null) {
			return;
		}
		klausurdatenIssues.klausurdaten.kursklausuren.add(kursklausur);
		manager.kursklausurfehlendAdd(kursklausur);
		klausurdatenIssues.klausurdaten.schuelerklausuren.addAll(manager.schuelerklausurGetMengeByKursklausur(kursklausur));
	}

	private static void erzeugeFehlendeVorgabe(final GostKlausurenHalbjahresdaten klausurdatenIssues, final GostKlausurplanManager manager,
			final int abiturjahr, final GostHalbjahr halbjahr, final int quartal, final DTOKurs kurs, final GostKursart kursart) {
		if (manager.vorgabefehlendGetByHalbjahrAndQuartalAndKursartallgAndFachid(
				abiturjahr, halbjahr, quartal, kursart, kurs.Fach_ID) != null) {
			return;
		}

		final GostKlausurvorgabe neueVorgabe = new GostKlausurvorgabe();
		neueVorgabe.abiturjahrgang = abiturjahr;
		neueVorgabe.halbjahr = halbjahr.id;
		neueVorgabe.idFach = kurs.Fach_ID;
		neueVorgabe.kursart = kurs.KursartAllg;
		neueVorgabe.quartal = quartal;
		klausurdatenIssues.klausurdaten.vorgaben.add(neueVorgabe);
		manager.vorgabefehlendAdd(neueVorgabe);
	}

	private static void bearbeiteKursMitVorgabe(final GostKlausurenHalbjahresdaten klausurdatenIssues, final GostKlausurplanManager manager,
			final GostKlausurvorgabe vorgabe, final DTOKurs kurs, final List<DTOSchuelerLernabschnittsdaten> lernabschnitte) {

		final GostKursklausur kursklausur = manager.kursklausurByVorgabeAndKursid(vorgabe, kurs.ID);
		if (kursklausur == null) {
			final GostKursklausur neueKursklausur = new GostKursklausur();
			neueKursklausur.idKurs = kurs.ID;
			neueKursklausur.idVorgabe = vorgabe.id;
			klausurdatenIssues.klausurdaten.kursklausuren.add(neueKursklausur);
			return;
		}

		final Map<Long, GostSchuelerklausur> aktiveSchuelerklausurenBySchuelerId =
				manager.schuelerklausurGetMengeByKursklausur(kursklausur)
						.stream().filter(sk -> sk.aktiv).collect(Collectors.toMap(sk -> sk.idSchueler, sk -> sk));

		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			if (manager.schuelerklausurByKursklausurAndSchuelerid(kursklausur, lernabschnitt.Schueler_ID) == null) {
				final GostSchuelerklausur fehlendeSchuelerklausur = new GostSchuelerklausur();
				fehlendeSchuelerklausur.idKursklausur = kursklausur.id;
				fehlendeSchuelerklausur.idSchueler = lernabschnitt.Schueler_ID;
				klausurdatenIssues.klausurdaten.schuelerklausuren.add(fehlendeSchuelerklausur);
			} else {
				aktiveSchuelerklausurenBySchuelerId.remove(lernabschnitt.Schueler_ID);
			}
		}
		klausurdatenIssues.klausurdaten.schuelerklausuren.addAll(aktiveSchuelerklausurenBySchuelerId.values());
	}

}
