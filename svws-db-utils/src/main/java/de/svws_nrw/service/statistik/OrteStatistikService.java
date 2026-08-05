package de.svws_nrw.service.statistik;

import java.util.List;

import de.svws_nrw.asd.data.statistik.OrteStatistikGesamt;
import de.svws_nrw.asd.types.schule.Laender;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.repo.kataloge.OrteRepository;
import de.svws_nrw.repo.schule.SchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Orten für die Statistik
 */
public final class OrteStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final SchuleRepository schuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/** Das Repository für den Zugriff auf die Orts-Daten */
	private final OrteRepository orteRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuleRepository               das Repository für die Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param orteRepository                 das Repository für die Orte
	 */
	public OrteStatistikService(final SchuleRepository schuleRepository, final SchuljahresabschnittService schuljahresabschnitteService,
			final OrteRepository orteRepository) {
		this.schuleRepository = schuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
		this.orteRepository = orteRepository;
	}


	private static OrteStatistikGesamt map(final DTOOrt dto, final int schuljahr) {
		final var daten = new OrteStatistikGesamt();
		daten.id = dto.ID;
		daten.plz = dto.PLZ;
		daten.ortsname = dto.Bezeichnung;
		daten.idLand = Laender.data().getIDByWertAndSchuljahr(Laender.data().getWertBySchluessel(dto.Land), schuljahr);
		return daten;
	}


	/**
	 * Gibt die Liste aller Orte zurück.
	 *
	 * @return die Liste aller Orte
	 */
	public @NotNull List<OrteStatistikGesamt> getList() {
		final var idSchuljahresabschnitt = schuleRepository.getIdSchuljahresabschnitt();
		final var schuljahresabschnitt = schuljahresabschnitteService.getById(idSchuljahresabschnitt);
		return orteRepository.getAll().stream().map(f -> map(f, schuljahresabschnitt.schuljahr)).toList();
	}

}
