package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.klausuren.GostKlausurplanManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenVorgaben;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenVorgabeRepository;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für Vorlagenoperationen auf GOSt-Klausurvorgaben.
 */
public final class GostKlausurenVorgabeVorlagenService {

	private final GostKlausurenVorgabeRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Klausurvorgaben
	 */
	public GostKlausurenVorgabeVorlagenService(final GostKlausurenVorgabeRepository repository) {
		this.repository = repository;
	}

	/**
	 * Kopiert die Klausurvorgaben aus der Vorlage in einen Abiturjahrgang.
	 *
	 * @param abiturjahr das Abiturjahr
	 * @param halbjahr das Halbjahr der gymnasialen Oberstufe oder null für alle Halbjahre
	 * @param quartal das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return die neu angelegten Klausurvorgaben
	 */
	public List<GostKlausurvorgabe> copyVorlagenToJahrgang(final int abiturjahr, final GostHalbjahr halbjahr, final int quartal) {
		return transactional(() -> {
			GostKlausurenValidationUtils.checkQuartal(quartal);
			final List<GostKlausurvorgabe> vorlagen = GostKlausurenVorgabeService.toApiList(repository.getListByAbiturjahr(-1));
			final List<GostKlausurvorgabe> zielvorgaben = GostKlausurenVorgabeService.toApiList(repository.getListByAbiturjahr(abiturjahr));
			final List<GostKlausurvorgabe> kopien = GostKlausurplanManager.vorgabeKopiereFehlendeVorlagen(abiturjahr, halbjahr, quartal, vorlagen, zielvorgaben);

			final List<DTOGostKlausurenVorgaben> vorgabenNeu = new ArrayList<>();
			for (final GostKlausurvorgabe kopie : kopien) {
				final DTOGostKlausurenVorgaben dto = new DTOGostKlausurenVorgaben(0, kopie.abiturjahrgang, GostHalbjahr.fromIDorException(kopie.halbjahr),
						kopie.quartal, kopie.idFach, GostKursart.fromKuerzelOrException(kopie.kursart), kopie.dauer, kopie.auswahlzeit,
						kopie.istGklMoeglich, kopie.istMdlPruefung, kopie.istAudioNotwendig, kopie.istVideoNotwendig);
				dto.Bemerkungen = kopie.bemerkungVorgabe;
				vorgabenNeu.add(dto);
			}
			repository.create(vorgabenNeu);
			repository.flush();
			return GostKlausurenVorgabeService.toApiList(vorgabenNeu);
		});
	}

}
