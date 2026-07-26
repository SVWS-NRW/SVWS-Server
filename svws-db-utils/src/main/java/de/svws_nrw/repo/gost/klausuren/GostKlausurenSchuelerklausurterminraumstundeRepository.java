package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermineRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf GOSt-Schülerklausurtermin-Raumstunden-Zuordnungen.
 */
public interface GostKlausurenSchuelerklausurterminraumstundeRepository
		extends RepositoryBase<DTOGostKlausurenSchuelerklausurenTermineRaumstunden, DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK> {

	/**
	 * Ermittelt Zuordnungen zu den angegebenen Schülerklausurterminen.
	 *
	 * @param schuelerklausurterminIds die IDs der Schülerklausurtermine
	 *
	 * @return die Liste der Zuordnungen
	 */
	List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> getListBySchuelerklausurterminIds(Collection<Long> schuelerklausurterminIds);

	/**
	 * Ermittelt Zuordnungen zu den angegebenen Klausurraumstunden.
	 *
	 * @param raumstundeIds die IDs der Klausurraumstunden
	 *
	 * @return die Liste der Zuordnungen
	 */
	List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> getListByRaumstundeIds(Collection<Long> raumstundeIds);

}
