package de.svws_nrw.repo.schulwechsel.erfolgreiche_bewerbung;

import java.util.Set;

import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.repo.Repository;

/**
 * Repository-Interface für {@link DTOSchulwechselErfolgreicheBewerbung}-Einträge.
 */
public interface SchulwechselErfolgreicheBewerbungRepository extends Repository<DTOSchulwechselErfolgreicheBewerbung> {

	/**
	 * Prüft ob eine ErfolgreicheBewerbungResponse mit der angegebenen schulkindId bereits existiert.
	 *
	 * @param idSchulkindSchulbewerbung die UUID des Schulkinds bei schulbewerbung.de
	 * @return true wenn ein Eintrag existiert, sonst false
	 */
	boolean existsByIdSchulkindSchulbewerbung(String idSchulkindSchulbewerbung);

	/**
	 * Gibt alle bekannten schulkindIds aus der Datenbank zurück.
	 *
	 * @return Set aller bekannten schulkindIds
	 */
	Set<String> findAllIdSchulkindSchulbewerbung();

}
