package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenRaumstunden;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf GOSt-Klausurraumstunden.
 */
public final class GostKlausurenRaumstundeRepositoryImpl extends RepositoryImpl<DTOGostKlausurenRaumstunden>
		implements GostKlausurenRaumstundeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenRaumstundeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostKlausurenRaumstunden.class, dto -> dto.ID, (dto, id) -> dto.ID = id);
	}

	@Override
	public List<DTOGostKlausurenRaumstunden> getListByRaumIds(final Collection<Long> raumIds) {
		if ((raumIds == null) || raumIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenRaumstunden.QUERY_LIST_BY_KLAUSURRAUM_ID, DTOGostKlausurenRaumstunden.class, raumIds);
	}

	@Override
	public List<DTOGostKlausurenRaumstunden> getUnreferenced() {
		return conn.queryList(
				"SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID NOT IN (SELECT w.Raumstunde_ID FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden w)",
				DTOGostKlausurenRaumstunden.class);
	}

}
