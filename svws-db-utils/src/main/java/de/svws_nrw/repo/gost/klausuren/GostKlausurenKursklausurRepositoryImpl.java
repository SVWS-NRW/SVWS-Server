package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenKursklausuren;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf GOSt-Kursklausuren.
 */
public final class GostKlausurenKursklausurRepositoryImpl extends RepositoryImpl<DTOGostKlausurenKursklausuren>
		implements GostKlausurenKursklausurRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenKursklausurRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostKlausurenKursklausuren.class, dto -> dto.ID, (dto, id) -> dto.ID = id);
	}

	@Override
	public List<DTOGostKlausurenKursklausuren> getListByVorgabeIds(final Collection<Long> vorgabeIds) {
		if ((vorgabeIds == null) || vorgabeIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenKursklausuren.QUERY_LIST_BY_VORGABE_ID, DTOGostKlausurenKursklausuren.class, vorgabeIds);
	}

	@Override
	public List<DTOGostKlausurenKursklausuren> getListByTerminIds(final Collection<Long> terminIds) {
		if ((terminIds == null) || terminIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenKursklausuren.QUERY_LIST_BY_TERMIN_ID, DTOGostKlausurenKursklausuren.class, terminIds);
	}

}
