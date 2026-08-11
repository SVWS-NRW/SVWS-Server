package de.svws_nrw.repo.schule.kataloge.schule;

import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.repo.Repository;

public interface SchuleRepository extends Repository<DTOSchuleNRW> {

	/**
	 * @param schulnummer {@link String}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsBySchulnummer(String schulnummer);

}
