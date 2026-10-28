package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import java.util.List;

import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Jahrgangszuordnungen für den Katalog der Ankreuzkompetenzen der SVWS-Datenbank
 */
public interface AnkreuzkompetenzJahrgangRepository extends Repository<DTOAnkreuzkompetenzJahrgang> {

	/**
	 * Ermittelt alle Jahrgangszuordnungen der Ankreuzkompetenz mit der übergebenen ID.
	 *
	 * @param idAnkreuzkompetenz die ID der Ankreuzkompetenz
	 *
	 * @return die Liste der Jahrgangszuordnungen, leer wenn keine vorhanden sind
	 */
	List<DTOAnkreuzkompetenzJahrgang> findListByIdAnkreuzkompetenz(long idAnkreuzkompetenz);

	// Hier sollten ggf. weitere spezifische Funktionen ergänzt werden

}
