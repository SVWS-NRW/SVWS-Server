package de.svws_nrw.data.statistik;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.asd.data.statistik.JahrgaengeStatistikGesamt;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.jahrgaenge.JahrgaengeRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Jahrgängen für die Statistik
 */
public final class JahrgaengeStatistikService {

	/** Das Repository für den Zugriff auf die Jahrgangs-Daten */
	private final JahrgaengeRepository jahrgaengeRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param jahrgaengeRepository   das Repository für die Jahrgänge
	 */
	public JahrgaengeStatistikService(final JahrgaengeRepository jahrgaengeRepository) {
		this.jahrgaengeRepository = jahrgaengeRepository;
	}


	private static JahrgaengeStatistikGesamt map(final DTOJahrgang dto) {
		final var daten = new JahrgaengeStatistikGesamt();
		daten.id = dto.ID;
		daten.kuerzel = dto.InternKrz;
		daten.kuerzelStatistik = dto.ASDJahrgang;
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		return daten;
	}


	/**
	 * Gibt die Liste aller Jahrgänge zurück.
	 *
	 * @return die Liste aller Jahrgänge
	 */
	public @NotNull List<JahrgaengeStatistikGesamt> getList() {
		return jahrgaengeRepository.getAll().stream().map(f -> map(f)).toList();
	}

}
