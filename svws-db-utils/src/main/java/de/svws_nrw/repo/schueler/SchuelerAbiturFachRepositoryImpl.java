package de.svws_nrw.repo.schueler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Fachinformationen der Abiturdaten von Schülern.
 */
public final class SchuelerAbiturFachRepositoryImpl extends RepositoryImpl<DTOSchuelerAbiturFach> implements SchuelerAbiturFachRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerAbiturFachRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerAbiturFach.class, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOSchuelerAbiturFach> getListBySchuelerIds(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty()))
			return Collections.emptyList();
		return conn.queryList(DTOSchuelerAbiturFach.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerAbiturFach.class, idsSchueler);
	}

	@Override
	public List<DTOSchuelerAbiturFach> getListBySchuelerIdsNurPruefungsfaecher(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty()))
			return Collections.emptyList();
		return conn.queryList("SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Schueler_ID IN ?1 AND e.AbiturFach IS NOT NULL",
				DTOSchuelerAbiturFach.class, idsSchueler);
	}

}
