package de.svws_nrw.repo.lehrer.lernplattform;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattformPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zuordnungen von Lehrern zu Lernplattformen.
 */
public final class LehrerLernplattformRepositoryImpl extends RepositoryBaseImpl<DTOLehrerLernplattform, DTOLehrerLernplattformPK>
		implements LehrerLernplattformRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerLernplattformRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerLernplattform.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOLehrerLernplattformPK id) {
		return new Object[] { id.LehrerID, id.LernplattformID };
	}

	@Override
	public List<DTOLehrerLernplattform> getListByIdLehrer(final long idLehrer) {
		return conn.queryList(DTOLehrerLernplattform.QUERY_BY_LEHRERID, DTOLehrerLernplattform.class, idLehrer);
	}

	@Override
	public List<DTOLehrerLernplattform> getListByIdLernplattform(final long idLernplattform) {
		return conn.queryList(DTOLehrerLernplattform.QUERY_BY_LERNPLATTFORMID, DTOLehrerLernplattform.class, idLernplattform);
	}

}
