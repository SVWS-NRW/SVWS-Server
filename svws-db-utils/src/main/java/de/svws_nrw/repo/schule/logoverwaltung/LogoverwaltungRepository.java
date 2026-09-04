package de.svws_nrw.repo.schule.logoverwaltung;

import java.util.Optional;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import de.svws_nrw.repo.Repository;

public interface LogoverwaltungRepository extends Repository<DTOLogo> {

	/**
	 * Prüft, ob ein Logo mit der angegebenen Kennung existiert.
	 *
	 * @param kennung die Kennung des Logos
	 * @return true, wenn ein Logo mit der angegebenen Kennung existiert, sonst false
	 */
	boolean existsByKennung(ReportingBildDefinition kennung);

	/**
	 * Ermittelt das Logo zur angegebenen Kennung und liefert es in einem Optional zurück, sofern es existiert.
	 *
	 * @param kennung die Kennung des Logos
	 * @return ein Optional mit dem Logo zu der angegebenen Kennung, sonst Optional.empty()
	 */
	Optional<DTOLogo> findByKennung(ReportingBildDefinition kennung);
}
