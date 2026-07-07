package de.svws_nrw.repo.schule.kataloge.schulen;

import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.repo.Repository;

public interface SchulenRepository extends Repository<DTOSchuleNRW> {

	/**
	 * @param schulnummer {@link String}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsBySchulnummer(String schulnummer);

}
