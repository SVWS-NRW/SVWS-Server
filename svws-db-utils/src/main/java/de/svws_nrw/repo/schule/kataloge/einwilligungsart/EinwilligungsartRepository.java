package de.svws_nrw.repo.schule.kataloge.einwilligungsart;

import java.util.List;

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf den Einwilligungsarten-Katalog der SVWS-Datenbank
 */
public interface EinwilligungsartRepository extends Repository<DTOKatalogEinwilligungsart> {

	/**
	 * Bestimmt die IDs aller Einwilligungsarten, die für den übergebenen Personentyp gelten.
	 *
	 * @param personTyp   der Personentyp
	 *
	 * @return die aufsteigend sortierten IDs der Einwilligungsarten, leer wenn keine vorhanden sind
	 */
	List<Long> getAllIdsByPersonTyp(PersonTyp personTyp);

	/**
	 * Prüft, ob eine Einwilligungsart mit der übergebenen ID für den übergebenen Personentyp existiert.
	 *
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 * @param personTyp            der Personentyp
	 *
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsByIdAndPersonTyp(long idEinwilligungsart, PersonTyp personTyp);

}
