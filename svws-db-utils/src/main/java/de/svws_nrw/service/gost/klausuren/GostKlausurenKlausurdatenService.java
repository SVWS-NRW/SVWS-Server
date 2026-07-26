package de.svws_nrw.service.gost.klausuren;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Service für aggregierte Klausurdaten.
 */
public final class GostKlausurenKlausurdatenService {

	private final GostKlausurenVorgabeService vorgabeService;
	private final GostKlausurenKursklausurService kursklausurService;
	private final GostKlausurenSchuelerklausurService schuelerklausurService;
	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenTerminService terminService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param vorgabeService der Service für Klausurvorgaben
	 * @param kursklausurService der Service für Kursklausuren
	 * @param schuelerklausurService der Service für Schülerklausuren
	 * @param schuelerklausurterminService der Service für Schülerklausurtermine
	 * @param terminService der Service für Klausurtermine
	 */
	public GostKlausurenKlausurdatenService(final GostKlausurenVorgabeService vorgabeService,
			final GostKlausurenKursklausurService kursklausurService,
			final GostKlausurenSchuelerklausurService schuelerklausurService,
			final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenTerminService terminService) {
		this.vorgabeService = vorgabeService;
		this.kursklausurService = kursklausurService;
		this.schuelerklausurService = schuelerklausurService;
		this.schuelerklausurterminService = schuelerklausurterminService;
		this.terminService = terminService;
	}

	/**
	 * Ermittelt die Klausurdaten für einen Abiturjahrgang und ein Halbjahr.
	 *
	 * @param abiturjahr der Abiturjahrgang
	 * @param halbjahr das GOSt-Halbjahr
	 * @param ganzesSchuljahr ob das gesamte Schuljahr geladen werden soll
	 *
	 * @return die Klausurdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenKlausurdaten getKlausurdaten(final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr)
			throws ApiOperationException {
		final GostKlausurenKlausurdaten data = new GostKlausurenKlausurdaten();
		data.vorgaben = vorgabeService.getListByAbiturjahr(abiturjahr, halbjahr, ganzesSchuljahr);
		data.kursklausuren = kursklausurService.getListByVorgabeIds(data.vorgaben.stream().map(v -> v.id).toList());
		data.schuelerklausuren = schuelerklausurService.getListByKursklausurIds(data.kursklausuren.stream().map(k -> k.id).toList());
		data.schuelerklausurtermine = schuelerklausurterminService
				.getListBySchuelerklausurIds(data.schuelerklausuren.stream().map(s -> s.id).toList());
		data.termine = terminService.getListByAbiturjahrAndHalbjahrIncludingTerminIds(abiturjahr, halbjahr, ganzesSchuljahr,
				data.schuelerklausurtermine.stream().filter(skt -> skt.idTermin != null).map(skt -> skt.idTermin).toList());
		return data;
	}

}
