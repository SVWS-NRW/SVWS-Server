package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf GOSt-Schülerklausurtermine.
 */
public final class GostKlausurenSchuelerklausurterminRepositoryImpl extends RepositoryImpl<DTOGostKlausurenSchuelerklausurenTermine>
		implements GostKlausurenSchuelerklausurterminRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenSchuelerklausurterminRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostKlausurenSchuelerklausurenTermine.class, dto -> dto.ID, (dto, id) -> dto.ID = id);
	}

	@Override
	public List<DTOGostKlausurenSchuelerklausurenTermine> getListBySchuelerklausurIds(final Collection<Long> schuelerklausurIds) {
		if ((schuelerklausurIds == null) || schuelerklausurIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenSchuelerklausurenTermine.QUERY_LIST_BY_SCHUELERKLAUSUR_ID,
				DTOGostKlausurenSchuelerklausurenTermine.class, schuelerklausurIds);
	}

	@Override
	public List<DTOGostKlausurenSchuelerklausurenTermine> getListByTerminIds(final Collection<Long> terminIds) {
		if ((terminIds == null) || terminIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenSchuelerklausurenTermine.QUERY_LIST_BY_TERMIN_ID,
				DTOGostKlausurenSchuelerklausurenTermine.class, terminIds);
	}

}
