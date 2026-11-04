package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholungFachwahlen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholungFachwahlenPK;
import de.svws_nrw.repo.RepositoryBase;


/**
 * Das Interface für ein Repository zum Zugriff auf die Fachwahlen, die Schüler für den Abiturjahrgang ihrer Wiederholung planen.
 */
public interface GostSchuelerWiederholungFachwahlenRepository extends RepositoryBase<DTOGostSchuelerWiederholungFachwahlen, DTOGostSchuelerWiederholungFachwahlenPK> {

	/**
	 * Gibt die Fachwahlen der angegebenen Schüler gruppiert nach der Schüler-ID zurück.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Map mit den Listen der Fachwahlen zu den Schüler-IDs
	 */
	Map<Long, List<DTOGostSchuelerWiederholungFachwahlen>> getMapBySchuelerID(Collection<Long> idsSchueler);

	/**
	 * Entfernt alle Fachwahlen der angegebenen Schüler.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 */
	void deleteMultipleBySchuelerID(Collection<Long> idsSchueler);

}
