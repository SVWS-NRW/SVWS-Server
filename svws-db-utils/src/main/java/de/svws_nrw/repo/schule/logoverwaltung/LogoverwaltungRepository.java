package de.svws_nrw.repo.schule.logoverwaltung;

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
}
