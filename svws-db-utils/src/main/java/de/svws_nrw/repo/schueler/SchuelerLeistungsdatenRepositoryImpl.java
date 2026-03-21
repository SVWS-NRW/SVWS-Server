package de.svws_nrw.repo.schueler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Schüler-Leistungsdaten
 */
public final class SchuelerLeistungsdatenRepositoryImpl extends RepositoryImpl<DTOSchuelerLeistungsdaten>
		implements SchuelerLeistungsdatenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerLeistungsdatenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerLeistungsdaten.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	@Override
	public List<DTOSchuelerLeistungsdaten> findListByLernabschnitt(final Collection<Long> idsLernabschnitte) {
		if (idsLernabschnitte.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, idsLernabschnitte);
	}

	@Override
	public List<DTOSchuelerLeistungsdaten> findListByLernabschnittAndFachlehrer(final Collection<Long> idsLernabschnitte, final Collection<Long> idsFachlehrer) {
		if (idsLernabschnitte.isEmpty() || idsFachlehrer.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryList("SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN ?1 AND e.Fachlehrer_ID IN ?2",
				DTOSchuelerLeistungsdaten.class, idsLernabschnitte, idsFachlehrer);
	}

	@Override
	public List<DTOSchuelerLeistungsdaten> findListByLernabschnittAndFach(final Collection<Long> idsLernabschnitte, final Collection<Long> idsFaecher) {
		if (idsLernabschnitte.isEmpty() || idsFaecher.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryList("SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN ?1 AND e.Fach_ID IN ?2",
				DTOSchuelerLeistungsdaten.class, idsLernabschnitte, idsFaecher);
	}

}
