package de.svws_nrw.service.statistik;

import java.util.List;

import de.svws_nrw.asd.data.statistik.FoerderschwerpunktStatistikGesamt;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt.FoerderschwerpunktRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Förderschwerpunkten für die Statistik
 */
public final class FoerderschwerpunkteStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/** Das Repository für den Zugriff auf die Förderschwerpunkt-Daten */
	private final FoerderschwerpunktRepository foerderschwerpunktRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param eigeneSchuleRepository               das Repository für den Zugriff auf die Schuldaten der Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param foerderschwerpunktRepository   das Repository für die Förderschwerpunkte
	 */
	public FoerderschwerpunkteStatistikService(final EigeneSchuleRepository eigeneSchuleRepository, final SchuljahresabschnittService schuljahresabschnitteService,
			final FoerderschwerpunktRepository foerderschwerpunktRepository) {
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
		this.foerderschwerpunktRepository = foerderschwerpunktRepository;
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
		final var idSchuljahresabschnitt = eigeneSchuleRepository.getIdSchuljahresabschnitt();
		final var schuljahresabschnitt = schuljahresabschnitteService.getById(idSchuljahresabschnitt);
		return foerderschwerpunktRepository.getAll().stream().map(f -> map(f, schuljahresabschnitt.schuljahr)).toList();
	}

}
