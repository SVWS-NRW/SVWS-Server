package de.svws_nrw.repo.lehrer.einwilligung;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutzPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Einwilligungen von Lehrern.
 */
public final class LehrerEinwilligungRepositoryImpl extends RepositoryBaseImpl<DTOLehrerDatenschutz, DTOLehrerDatenschutzPK>
		implements LehrerEinwilligungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerEinwilligungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerDatenschutz.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOLehrerDatenschutzPK id) {
		return new Object[] { id.LehrerID, id.DatenschutzID };
	}

	@Override
	public List<DTOLehrerDatenschutz> getListByIdLehrer(final long idLehrer) {
		return conn.queryList(DTOLehrerDatenschutz.QUERY_BY_LEHRERID, DTOLehrerDatenschutz.class, idLehrer);
	}

	@Override
	public List<DTOLehrerDatenschutz> getListByIdEinwilligungsart(final long idEinwilligungsart) {
		return conn.queryList(DTOLehrerDatenschutz.QUERY_BY_DATENSCHUTZID, DTOLehrerDatenschutz.class, idEinwilligungsart);
	}

}
