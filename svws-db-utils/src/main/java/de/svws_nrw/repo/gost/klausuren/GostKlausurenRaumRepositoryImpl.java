package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenRaeume;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf GOSt-Klausurräume.
 */
public final class GostKlausurenRaumRepositoryImpl extends RepositoryImpl<DTOGostKlausurenRaeume>
		implements GostKlausurenRaumRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenRaumRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostKlausurenRaeume.class, dto -> dto.ID, (dto, id) -> dto.ID = id);
	}

	@Override
	public List<DTOGostKlausurenRaeume> getListByTerminIds(final Collection<Long> terminIds) {
		if ((terminIds == null) || terminIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenRaeume.QUERY_LIST_BY_TERMIN_ID, DTOGostKlausurenRaeume.class, terminIds);
	}

}
