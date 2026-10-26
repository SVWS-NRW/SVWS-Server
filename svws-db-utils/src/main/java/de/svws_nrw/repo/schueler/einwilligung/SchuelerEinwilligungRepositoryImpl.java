package de.svws_nrw.repo.schueler.einwilligung;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutzPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Einwilligungen von Schülern.
 */
public final class SchuelerEinwilligungRepositoryImpl extends RepositoryBaseImpl<DTOSchuelerDatenschutz, DTOSchuelerDatenschutzPK>
		implements SchuelerEinwilligungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerEinwilligungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerDatenschutz.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOSchuelerDatenschutzPK id) {
		return new Object[] { id.Schueler_ID, id.Datenschutz_ID };
	}

	@Override
	public List<DTOSchuelerDatenschutz> getListByIdSchueler(final long idSchueler) {
		return conn.queryList(DTOSchuelerDatenschutz.QUERY_BY_SCHUELER_ID, DTOSchuelerDatenschutz.class, idSchueler);
	}

	@Override
	public List<DTOSchuelerDatenschutz> getListByIdEinwilligungsart(final long idEinwilligungsart) {
		return conn.queryList(DTOSchuelerDatenschutz.QUERY_BY_DATENSCHUTZ_ID, DTOSchuelerDatenschutz.class, idEinwilligungsart);
	}

}
