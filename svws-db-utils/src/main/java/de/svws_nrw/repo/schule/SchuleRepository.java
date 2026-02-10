package de.svws_nrw.repo.schule;

import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die EigeneSchule-Tabelle der SVWS-Datenbank
 */
public interface SchuleRepository extends Repository<DTOEigeneSchule> {

	/**
	 * Gibt den aktuellen Schuljahresabschnitt der Schule zurück.
	 *
	 * @return der aktuelle Schuljahresabschnitt der Schule
	 */
	long getSchuljahresabschnitt();

}
