package de.svws_nrw.repo.schueler.leistungsdaten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
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
	public List<DTOSchuelerLeistungsdaten> findListByKurs(final long idKurs) {
		return conn.queryList("SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Kurs_ID = ?1", DTOSchuelerLeistungsdaten.class, idKurs);
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

	@Override
	public Map<Long, List<DTOSchuelerLeistungsdaten>> getMapListByLernabschnittsIds(final Collection<Long> idsAbschnitte) {
		final Map<Long, List<DTOSchuelerLeistungsdaten>> map = new HashMap<>();
		if (idsAbschnitte.isEmpty()) {
			return map;
		}
		for (final long id : idsAbschnitte) {
			map.put(id, new ArrayList<>());
		}
		final List<DTOSchuelerLeistungsdaten> list = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, idsAbschnitte);
		for (final DTOSchuelerLeistungsdaten daten : list) {
			map.getOrDefault(daten.Abschnitt_ID, new ArrayList<>()).add(daten);
		}
		return map;
	}

	@Override
	public HashMap2D<Long, Long, DTOSchuelerLeistungsdaten> getMapByLernabschnittsIds(final Collection<Long> idsAbschnitte) {
		final HashMap2D<Long, Long, DTOSchuelerLeistungsdaten> map = new HashMap2D<>();
		if (idsAbschnitte.isEmpty()) {
			return map;
		}
		final List<DTOSchuelerLeistungsdaten> list = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, idsAbschnitte);
		for (final DTOSchuelerLeistungsdaten daten : list) {
			map.put(daten.Abschnitt_ID, daten.Fach_ID, daten);
		}
		return map;
	};

}
