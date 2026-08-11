package de.svws_nrw.repo.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.repo.Repository;

public interface SchuelerBisherigeSchuleRepository extends Repository<DTOSchuelerAbgaenge> {

	/**
	 * Gibt alle bisherigen Schulen eines Schülers zurück.
	 *
	 * @param idSchueler die ID des Schülers
	 * @return Liste der zugehörigen {@link DTOSchuelerAbgaenge}-Einträge, leer wenn keine vorhanden
	 */
	List<DTOSchuelerAbgaenge> getAllByIdSchueler(Long idSchueler);

}
