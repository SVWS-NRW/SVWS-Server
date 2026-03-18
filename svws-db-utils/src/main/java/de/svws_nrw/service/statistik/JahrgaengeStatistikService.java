package de.svws_nrw.service.statistik;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.asd.data.statistik.JahrgaengeStatistikGesamt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.jahrgaenge.JahrgaengeRepository;
import de.svws_nrw.repo.schule.SchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Jahrgängen für die Statistik
 */
public final class JahrgaengeStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final SchuleRepository schuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/** Das Repository für den Zugriff auf die Jahrgangs-Daten */
	private final JahrgaengeRepository jahrgaengeRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuleRepository               das Repository für den Zugriff auf die Schuldaten der Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param jahrgaengeRepository   das Repository für die Jahrgänge
	 */
	public JahrgaengeStatistikService(final SchuleRepository schuleRepository, final SchuljahresabschnittService schuljahresabschnitteService,
			final JahrgaengeRepository jahrgaengeRepository) {
		this.schuleRepository = schuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
		this.jahrgaengeRepository = jahrgaengeRepository;
	}


	private static JahrgaengeStatistikGesamt map(final DTOJahrgang dto, final int schuljahr) {
		final var daten = new JahrgaengeStatistikGesamt();
		daten.id = dto.ID;
		daten.kuerzel = dto.InternKrz;
		final var wert = (dto.ASDJahrgang == null) ? null : Jahrgaenge.data().getWertBySchluessel(dto.ASDJahrgang);
		final var eintrag = (wert == null) ? null : wert.daten(schuljahr);
		daten.idKatalog = (eintrag == null) ? null : eintrag.id;
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		return daten;
	}


	/**
	 * Gibt die Liste aller Jahrgänge zurück.
	 *
	 * @return die Liste aller Jahrgänge
	 */
	public @NotNull List<JahrgaengeStatistikGesamt> getList() {
		final var idSchuljahresabschnitt = schuleRepository.getSchuljahresabschnitt();
		final var schuljahresabschnitt = schuljahresabschnitteService.getById(idSchuljahresabschnitt);
		return jahrgaengeRepository.getAll().stream().map(f -> map(f, schuljahresabschnitt.schuljahr)).toList();
	}

}
