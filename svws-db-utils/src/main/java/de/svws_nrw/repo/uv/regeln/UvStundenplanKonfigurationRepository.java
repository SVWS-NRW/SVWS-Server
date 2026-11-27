package de.svws_nrw.repo.uv.regeln;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanKonfiguration;
import de.svws_nrw.repo.Repository;

/** Repository für die Stundenplankonfigurationen der UV. */
public interface UvStundenplanKonfigurationRepository extends Repository<DTOUvStundenplanKonfiguration> {

	/**
	 * Liefert die Konfigurationen eines Planungsabschnitts.
	 *
	 * @param idPlan die ID des Planungsabschnitts
	 * @return die Konfigurationen
	 */
	List<DTOUvStundenplanKonfiguration> getListByPlanungsabschnitt(long idPlan);
}
