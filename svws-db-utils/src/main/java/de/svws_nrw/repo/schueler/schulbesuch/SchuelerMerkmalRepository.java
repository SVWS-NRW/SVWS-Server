package de.svws_nrw.repo.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.repo.Repository;

public interface SchuelerMerkmalRepository extends Repository<DTOSchuelerMerkmale> {

	/**
	 * Gibt alle Schulbesuchsmerkmale eines Schülers zurück.
	 *
	 * @param idSchueler die ID des Schülers
	 *
	 * @return Liste der zugehörigen {@link DTOSchuelerMerkmale}-Einträge, leer wenn keine vorhanden
	 */
	List<DTOSchuelerMerkmale> getAllByIdSchueler(Long idSchueler);
}
