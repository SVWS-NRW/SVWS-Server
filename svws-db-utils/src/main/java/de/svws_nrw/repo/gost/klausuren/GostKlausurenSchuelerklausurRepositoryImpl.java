package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf GOSt-Schülerklausuren.
 */
public final class GostKlausurenSchuelerklausurRepositoryImpl extends RepositoryImpl<DTOGostKlausurenSchuelerklausuren>
		implements GostKlausurenSchuelerklausurRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenSchuelerklausurRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostKlausurenSchuelerklausuren.class, dto -> dto.ID, (dto, id) -> dto.ID = id);
	}

	@Override
	public List<DTOGostKlausurenSchuelerklausuren> getListByKursklausurIds(final Collection<Long> kursklausurIds) {
		if ((kursklausurIds == null) || kursklausurIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenSchuelerklausuren.QUERY_LIST_BY_KURSKLAUSUR_ID,
				DTOGostKlausurenSchuelerklausuren.class, kursklausurIds);
	}

}
