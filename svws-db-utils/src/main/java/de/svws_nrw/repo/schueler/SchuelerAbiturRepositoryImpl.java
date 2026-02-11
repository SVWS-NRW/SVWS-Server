package de.svws_nrw.repo.schueler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Abiturdaten von Schülern.
 */
public final class SchuelerAbiturRepositoryImpl extends RepositoryImpl<DTOSchuelerAbitur> implements SchuelerAbiturRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerAbiturRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerAbitur.class, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOSchuelerAbitur> getListBySchuelerIds(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty()))
			return Collections.emptyList();
		return conn.queryList(DTOSchuelerAbitur.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerAbitur.class, idsSchueler);
	}

}
