package de.svws_nrw.repo.schule.kataloge.ortsteil;

import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;
import de.svws_nrw.repo.Repository;

public interface OrtsteilRepository extends Repository<DTOOrtsteil>, ReferencedBulkDeletionRepository<DTOOrtsteil> {

	/**
	 * Gibt zurück, ob der Name des Ortsteils für den gegebenen Ort noch nicht vergeben ist.
	 * @param ortsteil der Name des Ortsteil.
	 * @param idOrt die id des Orts.
	 * @return true falls der Name des Ortsteils für den gegebenen Ort noch nicht existiert, ansonsten false
	 */
	boolean ortsteilnameIsUniqueForIdOrtCreate(String ortsteil, Long idOrt);

	/**
	 * Gibt zurück, ob der Name des Ortsteils für den angegebenen Ort noch nicht vergeben ist,
	 * wobei der Ortsteil mit der angegebenen ID ausgeschlossen wird.
	 * @param ortsteil der Name des Ortsteils.
	 * @param idOrt die id des Orts.
	 * @param idOrtsteil die ID des zu aktualisierenden Ortsteil.
	 * @return true falls kein anderer Ortsteil den Namen für den Ort verwendet, ansonsten false
	 */
	boolean ortsteilnameIsUniqueForIdOrtPatch(String ortsteil, Long idOrt, long idOrtsteil);


}
