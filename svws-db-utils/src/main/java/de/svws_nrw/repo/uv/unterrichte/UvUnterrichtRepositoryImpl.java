package de.svws_nrw.repo.uv.unterrichte;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterricht;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Unterrichte
 * (Tabelle UV_Unterrichte).
 */
public final class UvUnterrichtRepositoryImpl extends RepositoryImpl<DTOUvUnterricht> implements UvUnterrichtRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvUnterrichtRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvUnterricht.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvUnterricht> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvUnterricht.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvUnterricht.class, idPlanungsabschnitt);
	}

	@Override
	public List<DTOUvUnterricht> getListByLerngruppenIds(final Collection<Long> idsLerngruppen) {
		return idsLerngruppen.isEmpty() ? List.of()
				: conn.queryList(DTOUvUnterricht.QUERY_LIST_BY_LERNGRUPPE_ID, DTOUvUnterricht.class, idsLerngruppen);
	}

}
