package de.svws_nrw.service.gost.klausuren;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Service für schülerbezogene Klausurdaten.
 */
public final class GostKlausurenSchuelerKlausurdatenService {

	private final GostKlausurenSchuelerklausurService schuelerklausurService;
	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenKursklausurService kursklausurService;
	private final GostKlausurenVorgabeService vorgabeService;
	private final GostKlausurenTerminService terminService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuelerklausurService der Service für Schülerklausuren
	 * @param schuelerklausurterminService der Service für Schülerklausurtermine
	 * @param kursklausurService der Service für Kursklausuren
	 * @param vorgabeService der Service für Klausurvorgaben
	 * @param terminService der Service für Klausurtermine
	 */
	public GostKlausurenSchuelerKlausurdatenService(final GostKlausurenSchuelerklausurService schuelerklausurService,
			final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenKursklausurService kursklausurService,
			final GostKlausurenVorgabeService vorgabeService,
			final GostKlausurenTerminService terminService) {
		this.schuelerklausurService = schuelerklausurService;
		this.schuelerklausurterminService = schuelerklausurterminService;
		this.kursklausurService = kursklausurService;
		this.vorgabeService = vorgabeService;
		this.terminService = terminService;
	}

	/**
	 * Ermittelt die Klausurdaten eines Schülers.
	 *
	 * @param idSchueler die ID des Schülers
	 * @param abiturjahr der Abiturjahrgang
	 * @param halbjahr das GOSt-Halbjahr
	 *
	 * @return die Klausurdaten des Schülers
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenKlausurdaten getKlausurdatenBySchuelerId(final long idSchueler, final int abiturjahr, final int halbjahr)
			throws ApiOperationException {
		final GostKlausurenKlausurdaten klausurdaten = new GostKlausurenKlausurdaten();
		final GostHalbjahr gostHalbjahr = GostHalbjahr.fromIDorException(halbjahr);
		final List<GostKlausurvorgabe> vorgaben = vorgabeService.getListByAbiturjahr(abiturjahr, gostHalbjahr.id, false);
		final List<GostKursklausur> kursklausuren = kursklausurService.getListByVorgabeIds(vorgaben.stream().map(v -> v.id).toList());
		klausurdaten.schuelerklausuren = schuelerklausurService.getListByKursklausurIds(kursklausuren.stream().map(k -> k.id).toList())
				.stream().filter(sk -> sk.idSchueler == idSchueler).toList();

		if (!klausurdaten.schuelerklausuren.isEmpty()) {
			klausurdaten.schuelerklausurtermine = schuelerklausurterminService.getListBySchuelerklausurIds(
					klausurdaten.schuelerklausuren.stream().map(sk -> sk.id).toList());
			final Set<Long> kursklausurIds = new HashSet<>(klausurdaten.schuelerklausuren.stream().map(sk -> sk.idKursklausur).toList());
			klausurdaten.kursklausuren = kursklausuren.stream().filter(k -> kursklausurIds.contains(k.id)).toList();
			final Set<Long> vorgabeIds = new HashSet<>(klausurdaten.kursklausuren.stream().map(k -> k.idVorgabe).toList());
			klausurdaten.vorgaben = vorgaben.stream().filter(v -> vorgabeIds.contains(v.id)).toList();
			final Set<Long> terminIds = new HashSet<>();
			terminIds.addAll(klausurdaten.schuelerklausurtermine.stream().filter(skt -> skt.idTermin != null).map(skt -> skt.idTermin).toList());
			terminIds.addAll(klausurdaten.kursklausuren.stream().filter(kk -> kk.idTermin != null).map(kk -> kk.idTermin).toList());
			klausurdaten.termine = terminService.getListByIds(terminIds);
		}
		return klausurdaten;
	}

}
