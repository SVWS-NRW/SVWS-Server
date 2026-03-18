package de.svws_nrw.service.statistik;

import java.util.List;

import de.svws_nrw.asd.data.statistik.ReligionStatistikGesamt;
import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.repo.kataloge.ReligionRepository;
import de.svws_nrw.repo.schule.SchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Religionen für die Statistik
 */
public final class ReligionStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final SchuleRepository schuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/** Das Repository für den Zugriff auf die Daten zu den Religionen */
	private final ReligionRepository religionRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuleRepository               das Repository für den Zugriff auf die Schuldaten der Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param religionRepository   das Repository für die Religionseinträge
	 */
	public ReligionStatistikService(final SchuleRepository schuleRepository, final SchuljahresabschnittService schuljahresabschnitteService,
			final ReligionRepository religionRepository) {
		this.schuleRepository = schuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
		this.religionRepository = religionRepository;
	}


	private static ReligionStatistikGesamt map(final DTOKonfession dto, final int schuljahr) {
		final var daten = new ReligionStatistikGesamt();
		daten.id = dto.ID;
		final var wert = Religion.data().getWertBySchluessel(dto.StatistikKrz);
		final var eintrag = (wert == null) ? null : wert.daten(schuljahr);
		daten.idKatalog = (eintrag == null) ? Religion.OH.daten(schuljahr).id : eintrag.id;
		return daten;
	}


	/**
	 * Gibt die Liste zu allen statistik-Relevanten Daten der Religionen zurück.
	 *
	 * @return die Liste zu allen statistik-Relevanten Daten der Religionen
	 */
	public @NotNull List<ReligionStatistikGesamt> getList() {
		final var idSchuljahresabschnitt = schuleRepository.getSchuljahresabschnitt();
		final var schuljahresabschnitt = schuljahresabschnitteService.getById(idSchuljahresabschnitt);
		return religionRepository.getAll().stream().map(f -> map(f, schuljahresabschnitt.schuljahr)).toList();
	}

}
