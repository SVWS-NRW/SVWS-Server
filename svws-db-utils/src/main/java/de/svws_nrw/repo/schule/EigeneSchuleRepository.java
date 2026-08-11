package de.svws_nrw.repo.schule;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.repo.Repository;
import jakarta.annotation.Nonnull;

/**
 * Das Interface für ein Repository zum Zugriff auf die EigeneSchule-Tabelle der SVWS-Datenbank
 */
public interface EigeneSchuleRepository extends Repository<DTOEigeneSchule> {

	/**
	 * Gibt den aktuellen Schuljahresabschnitt der Schule zurück.
	 *
	 * @return der aktuelle Schuljahresabschnitt der Schule
	 */
	long getIdSchuljahresabschnitt();

	/**
	 * Gibt die Schulnummer der Schule zurück.
	 *
	 * @return die Schulnummer der Schule
	 */
	int getSchulnummer();

	/**
	 * Gibt die Schulform der Schule zurück
	 *
	 * @return die Schulform der Schule
	 */
	@Nonnull
	Schulform getSchulform();

}
