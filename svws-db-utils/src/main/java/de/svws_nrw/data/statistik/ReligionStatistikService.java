package de.svws_nrw.data.statistik;

import java.util.List;

import de.svws_nrw.asd.data.statistik.ReligionStatistikGesamt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.repo.kataloge.ReligionRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Religionen für die Statistik
 */
public final class ReligionStatistikService {

	/** Das Repository für den Zugriff auf die Daten zu den Religionen */
	private final ReligionRepository religionRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param religionRepository   das Repository für die Religionseinträge
	 */
	public ReligionStatistikService(final ReligionRepository religionRepository) {
		this.religionRepository = religionRepository;
	}


	private static ReligionStatistikGesamt map(final DTOKonfession dto) {
		final var daten = new ReligionStatistikGesamt();
		daten.id = dto.ID;
		daten.kuerzel = dto.StatistikKrz;
		return daten;
	}


	/**
	 * Gibt die Liste zu allen statistik-Relevanten Daten der Religionen zurück.
	 *
	 * @return die Liste zu allen statistik-Relevanten Daten der Religionen
	 */
	public @NotNull List<ReligionStatistikGesamt> getList() {
		return religionRepository.getAll().stream().map(f -> map(f)).toList();
	}

}
