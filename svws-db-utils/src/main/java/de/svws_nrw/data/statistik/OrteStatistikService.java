package de.svws_nrw.data.statistik;

import java.util.List;

import de.svws_nrw.asd.data.statistik.OrteStatistikGesamt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.repo.kataloge.OrteRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Orten für die Statistik
 */
public final class OrteStatistikService {

	/** Das Repository für den Zugriff auf die Orts-Daten */
	private final OrteRepository orteRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param orteRepository   das Repository für die Orte
	 */
	public OrteStatistikService(final OrteRepository orteRepository) {
		this.orteRepository = orteRepository;
	}


	private static OrteStatistikGesamt map(final DTOOrt dto) {
		final var daten = new OrteStatistikGesamt();
		daten.id = dto.ID;
		daten.plz = dto.PLZ;
		daten.ortsname = dto.Bezeichnung;
		return daten;
	}


	/**
	 * Gibt die Liste aller Orte zurück.
	 *
	 * @return die Liste aller Orte
	 */
	public @NotNull List<OrteStatistikGesamt> getList() {
		return orteRepository.getAll().stream().map(f -> map(f)).toList();
	}

}
