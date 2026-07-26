package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermineRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf GOSt-Schülerklausurtermin-Raumstunden-Zuordnungen.
 */
public final class GostKlausurenSchuelerklausurterminraumstundeRepositoryImpl
		extends RepositoryBaseImpl<DTOGostKlausurenSchuelerklausurenTermineRaumstunden, DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK>
		implements GostKlausurenSchuelerklausurterminraumstundeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenSchuelerklausurterminraumstundeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK id) {
		return new Object[] { id.Schuelerklausurtermin_ID, id.Raumstunde_ID };
	}

	@Override
	public List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> getListBySchuelerklausurterminIds(
			final Collection<Long> schuelerklausurterminIds) {
		if ((schuelerklausurterminIds == null) || schuelerklausurterminIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenSchuelerklausurenTermineRaumstunden.QUERY_LIST_BY_SCHUELERKLAUSURTERMIN_ID,
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class, schuelerklausurterminIds);
	}

	@Override
	public List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> getListByRaumstundeIds(final Collection<Long> raumstundeIds) {
		if ((raumstundeIds == null) || raumstundeIds.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostKlausurenSchuelerklausurenTermineRaumstunden.QUERY_LIST_BY_RAUMSTUNDE_ID,
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class, raumstundeIds);
	}

}
