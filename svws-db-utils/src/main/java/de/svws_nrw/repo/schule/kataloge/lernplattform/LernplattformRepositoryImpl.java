package de.svws_nrw.repo.schule.kataloge.lernplattform;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Lernplattformen-Katalog.
 */
public final class LernplattformRepositoryImpl extends RepositoryImpl<DTOLernplattformen> implements LernplattformRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LernplattformRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLernplattformen.class, l -> l.ID, (l, id) -> l.ID = id);
	}

	@Override
	public List<Long> getAllIds() {
		return conn.queryList("SELECT e.ID FROM DTOLernplattformen e ORDER BY e.ID", Long.class);
	}

	@Override
	public boolean existsById(final Long idLernplattform) {
		return conn.existsBy(DTOLernplattformen.QUERY_BY_ID, DTOLernplattformen.class, idLernplattform);
	}

}
