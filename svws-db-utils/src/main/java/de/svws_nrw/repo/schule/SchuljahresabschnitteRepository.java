package de.svws_nrw.repo.schule;

import java.util.Optional;

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

	/**
	 * Ermittelt einen Schuljahresabschnitt anhand von Schuljahr und Abschnitt.
	 *
	 * @param schuljahr das Schuljahr
	 * @param abschnitt der Abschnitt
	 *
	 * @return der Schuljahresabschnitt, falls vorhanden
	 */
	Optional<DTOSchuljahresabschnitte> findBySchuljahrAndAbschnitt(int schuljahr, int abschnitt);

}
