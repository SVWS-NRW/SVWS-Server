package de.svws_nrw.service.statistik;

import java.util.List;

import de.svws_nrw.asd.data.statistik.FachStatistikGesamt;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Fächern für die Statistik
 */
public final class FachStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/** Das Repository für den Zugriff auf die Daten zu den Fächern */
	private final FachRepository fachRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param eigeneSchuleRepository               das Repository für den Zugriff auf die Schuldaten der Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param fachRepository                 das Repository für die Fächer
	 */
	public FachStatistikService(final EigeneSchuleRepository eigeneSchuleRepository, final SchuljahresabschnittService schuljahresabschnitteService,
			final FachRepository fachRepository) {
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
		this.fachRepository = fachRepository;
	}


	private static FachStatistikGesamt map(final DTOFach dto) {
		final var daten = new FachStatistikGesamt();
		daten.id = dto.ID;
		daten.kuerzel = dto.Kuerzel;
		daten.kuerzelStatistik = dto.StatistikKuerzel; // TODO auf ID statt kuerzel, dafür wird später schuljahr benötigt
		daten.bilingualeSprache = dto.Unterrichtssprache;
		return daten;
	}


	/**
	 * Gibt die Liste zu allen statistik-Relevanten Daten der Fächer zurück.
	 *
	 * @return die Liste zu allen statistik-Relevanten Daten der Fächer
	 */
	public @NotNull List<FachStatistikGesamt> getList() {
		return fachRepository.getAll().stream().map(FachStatistikService::map).toList();
	}

}
