package de.svws_nrw.repo.lehrer.minderleistung;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Minderleistungen von Lehrern.
 */
public final class LehrerMinderleistungRepositoryImpl extends RepositoryImpl<DTOLehrerEntlastungsstunde> implements LehrerMinderleistungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerMinderleistungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerEntlastungsstunde.class, o -> o.id, (o, id) -> o.id = id);
	}

	@Override
	public Map<Long, List<DTOLehrerEntlastungsstunde>> getMapByAbschnittIds(final Collection<Long> idsAbschnitte) {
		if ((idsAbschnitte == null) || (idsAbschnitte.isEmpty())) {
			return Collections.emptyMap();
		}

		final var list = conn.queryList(DTOLehrerEntlastungsstunde.QUERY_LIST_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idsAbschnitte);
		final var map = list.stream().collect(Collectors.groupingBy(f -> f.idAbschnittsdaten));
		for (final long idAbschnitt : idsAbschnitte) {
			map.computeIfAbsent(idAbschnitt, id -> new ArrayList<>());
		}
		return map;
	}

	@Override
	public List<DTOLehrerEntlastungsstunde> getAllByLehrerAbschnittId(final long idAbschnitt) {
		return conn.queryList(DTOLehrerEntlastungsstunde.QUERY_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idAbschnitt);
	}

	@Override
	public Map<Long, List<DTOLehrerEntlastungsstunde>> getListByIdLehrerAbschnittsdaten(final Collection<Long> idsLehrerPersonalabschnittsdaten) {
		final var result = conn.queryList(DTOLehrerEntlastungsstunde.QUERY_LIST_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idsLehrerPersonalabschnittsdaten);
		return result.stream().collect(Collectors.groupingBy(entity -> entity.idAbschnittsdaten));
	}

}
