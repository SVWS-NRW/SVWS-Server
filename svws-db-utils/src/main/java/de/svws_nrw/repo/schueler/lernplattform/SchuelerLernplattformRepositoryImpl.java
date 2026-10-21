package de.svws_nrw.repo.schueler.lernplattform;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattformPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zuordnungen von Schülern zu Lernplattformen.
 */
public final class SchuelerLernplattformRepositoryImpl extends RepositoryBaseImpl<DTOSchuelerLernplattform, DTOSchuelerLernplattformPK>
		implements SchuelerLernplattformRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerLernplattformRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerLernplattform.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOSchuelerLernplattformPK id) {
		return new Object[] { id.SchuelerID, id.LernplattformID };
	}

	@Override
	public List<DTOSchuelerLernplattform> getListByIdSchueler(final long idSchueler) {
		return conn.queryList(DTOSchuelerLernplattform.QUERY_BY_SCHUELERID, DTOSchuelerLernplattform.class, idSchueler);
	}

	@Override
	public List<DTOSchuelerLernplattform> getListByIdLernplattform(final long idLernplattform) {
		return conn.queryList(DTOSchuelerLernplattform.QUERY_BY_LERNPLATTFORMID, DTOSchuelerLernplattform.class, idLernplattform);
	}

}
