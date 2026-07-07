package de.svws_nrw.repo.schule;

import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Schuljahresabschnitts-Tabelle der SVWS-Datenbank
 */
public interface SchuljahresabschnitteRepository extends Repository<DTOSchuljahresabschnitte> {

	/**
	 * @param idSchuljahresabschnitt {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idSchuljahresabschnitt);

}
