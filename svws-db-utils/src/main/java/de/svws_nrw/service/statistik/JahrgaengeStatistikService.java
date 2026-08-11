package de.svws_nrw.service.statistik;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.asd.data.statistik.JahrgaengeStatistikGesamt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Jahrgängen für die Statistik
 */
public final class JahrgaengeStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/** Das Repository für den Zugriff auf die Jahrgangs-Daten */
	private final JahrgangRepository jahrgangRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param eigeneSchuleRepository               das Repository für den Zugriff auf die Schuldaten der Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param jahrgangRepository   das Repository für die Jahrgänge
	 */
	public JahrgaengeStatistikService(final EigeneSchuleRepository eigeneSchuleRepository, final SchuljahresabschnittService schuljahresabschnitteService,
			final JahrgangRepository jahrgangRepository) {
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
		this.jahrgangRepository = jahrgangRepository;
	}


	private static JahrgaengeStatistikGesamt map(final DTOJahrgang dto, final int schuljahr) {
		final var daten = new JahrgaengeStatistikGesamt();
		daten.id = dto.ID;
		daten.kuerzel = dto.InternKrz;
		daten.idKatalog = Jahrgaenge.data().getIDByWertAndSchuljahr(Jahrgaenge.data().getWertBySchluessel(dto.ASDJahrgang), schuljahr);
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		return daten;
	}


	/**
	 * Gibt die Liste aller Jahrgänge zurück.
	 *
	 * @return die Liste aller Jahrgänge
	 */
	public @NotNull List<JahrgaengeStatistikGesamt> getList() {
		final var idSchuljahresabschnitt = eigeneSchuleRepository.getIdSchuljahresabschnitt();
		final var schuljahresabschnitt = schuljahresabschnitteService.getById(idSchuljahresabschnitt);
		return jahrgangRepository.getAll().stream().map(f -> map(f, schuljahresabschnitt.schuljahr)).toList();
	}

}
