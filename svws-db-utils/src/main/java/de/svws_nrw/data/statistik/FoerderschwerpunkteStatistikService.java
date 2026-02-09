package de.svws_nrw.data.statistik;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.asd.data.statistik.FoerderschwerpunktStatistikGesamt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.repo.kataloge.FoerderschwerpunkteRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Förderschwerpunkten für die Statistik
 */
public final class FoerderschwerpunkteStatistikService {

	/** Das Repository für den Zugriff auf die Förderschwerpunkt-Daten */
	private final FoerderschwerpunkteRepository foerderschwerpunkteRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param foerderschwerpunkteRepository   das Repository für die Förderschwerpunkte
	 */
	public FoerderschwerpunkteStatistikService(final FoerderschwerpunkteRepository foerderschwerpunkteRepository) {
		this.foerderschwerpunkteRepository = foerderschwerpunkteRepository;
	}


	private static FoerderschwerpunktStatistikGesamt map(final DTOFoerderschwerpunkt dto) {
		final var daten = new FoerderschwerpunktStatistikGesamt();
		daten.id = dto.ID;
		daten.kuerzelStatistik = Objects.requireNonNullElse(dto.StatistikKrz, "");
		return daten;
	}


	/**
	 * Gibt die Liste aller Förderschwerpunkte zurück.
	 *
	 * @return die Liste aller Förderschwerpunkte
	 */
	public @NotNull List<FoerderschwerpunktStatistikGesamt> getList() {
		return foerderschwerpunkteRepository.getAll().stream().map(f -> map(f)).toList();
	}

}
