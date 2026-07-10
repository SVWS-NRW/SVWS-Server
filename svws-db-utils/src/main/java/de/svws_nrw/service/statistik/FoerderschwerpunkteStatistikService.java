package de.svws_nrw.service.statistik;

import java.util.List;

import de.svws_nrw.asd.data.statistik.FoerderschwerpunktStatistikGesamt;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.repo.kataloge.FoerderschwerpunkteRepository;
import de.svws_nrw.repo.schule.SchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Förderschwerpunkten für die Statistik
 */
public final class FoerderschwerpunkteStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final SchuleRepository schuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/** Das Repository für den Zugriff auf die Förderschwerpunkt-Daten */
	private final FoerderschwerpunkteRepository foerderschwerpunkteRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuleRepository               das Repository für den Zugriff auf die Schuldaten der Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param foerderschwerpunkteRepository   das Repository für die Förderschwerpunkte
	 */
	public FoerderschwerpunkteStatistikService(final SchuleRepository schuleRepository, final SchuljahresabschnittService schuljahresabschnitteService,
			final FoerderschwerpunkteRepository foerderschwerpunkteRepository) {
		this.schuleRepository = schuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
		this.foerderschwerpunkteRepository = foerderschwerpunkteRepository;
	}


	private static FoerderschwerpunktStatistikGesamt map(final DTOFoerderschwerpunkt dto, final int schuljahr) {
		final var daten = new FoerderschwerpunktStatistikGesamt();
		daten.id = dto.ID;
		final var eintrag = Foerderschwerpunkt.data().getIDByWertAndSchuljahr(Foerderschwerpunkt.data().getWertBySchluessel(dto.StatistikKrz), schuljahr);
		daten.idKatalog = (eintrag == null) ? Foerderschwerpunkt.KEINER.daten(schuljahr).id : eintrag;
		return daten;
	}


	/**
	 * Gibt die Liste aller Förderschwerpunkte zurück.
	 *
	 * @return die Liste aller Förderschwerpunkte
	 */
	public @NotNull List<FoerderschwerpunktStatistikGesamt> getList() {
		final var idSchuljahresabschnitt = schuleRepository.getIdSchuljahresabschnitt();
		final var schuljahresabschnitt = schuljahresabschnitteService.getById(idSchuljahresabschnitt);
		return foerderschwerpunkteRepository.getAll().stream().map(f -> map(f, schuljahresabschnitt.schuljahr)).toList();
	}

}
