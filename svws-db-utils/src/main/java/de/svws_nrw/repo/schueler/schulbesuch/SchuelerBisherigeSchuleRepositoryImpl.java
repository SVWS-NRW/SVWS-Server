package de.svws_nrw.repo.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.repo.RepositoryImpl;

public final class SchuelerBisherigeSchuleRepositoryImpl extends RepositoryImpl<DTOSchuelerAbgaenge> implements SchuelerBisherigeSchuleRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerBisherigeSchuleRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerAbgaenge.class, s -> s.id, (s, id) -> s.id = id);
	}

	@Override
	public List<DTOSchuelerAbgaenge> getAllByIdSchueler(final Long idSchueler) {
		return this.conn.queryList(DTOSchuelerAbgaenge.QUERY_BY_IDSCHUELER, DTOSchuelerAbgaenge.class, idSchueler);
	}
}
