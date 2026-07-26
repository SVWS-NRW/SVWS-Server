package de.svws_nrw.service.gost.klausuren;

import java.util.Comparator;
import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für das Erzeugen von GOSt-Schülerklausurterminen mit den fachlichen Seiteneffekten des Klausurplans.
 */
public final class GostKlausurenSchuelerklausurterminCreationService {

	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenRaumzuweisungService raumzuweisungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuelerklausurterminService der Basis-Service für Schülerklausurtermine
	 * @param raumzuweisungService der Service für Raumzuweisungen
	 */
	public GostKlausurenSchuelerklausurterminCreationService(final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenRaumzuweisungService raumzuweisungService) {
		this.schuelerklausurterminService = schuelerklausurterminService;
		this.raumzuweisungService = raumzuweisungService;
	}

	/**
	 * Erstellt einen Schülerklausurtermin und entfernt Raumzuweisungen des bisherigen letzten Schülerklausurtermins.
	 *
	 * @param createRequest die Create-Daten
	 *
	 * @return der neue Schülerklausurtermin
	 */
	public GostSchuelerklausurtermin create(final GostKlausurenSchuelerklausurterminCreateRequest createRequest) {
		return transactional(() -> {
			final List<DTOGostKlausurenSchuelerklausurenTermine> vorhandeneTermine =
					schuelerklausurterminService.getListBySchuelerklausurId(createRequest.idSchuelerklausur);
			loescheRaumzuweisungDesLetztenTermins(vorhandeneTermine);
			return schuelerklausurterminService.create(createRequest, GostKlausurenSchuelerklausurterminService.getNaechsteFolgeNr(vorhandeneTermine));
		});
	}

	private void loescheRaumzuweisungDesLetztenTermins(final List<DTOGostKlausurenSchuelerklausurenTermine> vorhandeneTermine) {
		vorhandeneTermine.stream()
				.max(Comparator.comparingInt(dto -> dto.Folge_Nr))
				.ifPresent(dto -> raumzuweisungService.loescheRaumzuweisungenFuerSchuelerklausurtermine(List.of(dto.ID)));
	}

}
