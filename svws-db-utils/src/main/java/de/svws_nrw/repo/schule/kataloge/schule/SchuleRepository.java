package de.svws_nrw.repo.schule.kataloge.schule;

import java.util.Map;

import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.repo.Repository;

public interface SchuleRepository extends Repository<DTOSchuleNRW> {

	/**
	 * @param schulnummer {@link String}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsBySchulnummer(String schulnummer);

	/**
	 * Gibt alle Schulen als Map zurück, wobei die Schulnummer als Schlüssel dient.
	 *
	 * @return Map von Schulnummer auf {@link DTOSchuleNRW}
	 */
	Map<String, DTOSchuleNRW> getSchulenBySchulnummer();

}
