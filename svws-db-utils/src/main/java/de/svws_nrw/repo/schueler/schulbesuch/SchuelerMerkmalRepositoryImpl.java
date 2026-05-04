package de.svws_nrw.repo.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.repo.RepositoryImpl;

public final class SchuelerMerkmalRepositoryImpl extends RepositoryImpl<DTOSchuelerMerkmale> implements SchuelerMerkmalRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerMerkmalRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerMerkmale.class, s -> s.id, (s, id) -> s.id = id);
	}

	@Override
	public List<DTOSchuelerMerkmale> getAllByIdSchueler(final Long idSchueler) {
		return this.conn.queryList(DTOSchuelerMerkmale.QUERY_BY_IDSCHUELER, DTOSchuelerMerkmale.class, idSchueler);
	}

}
