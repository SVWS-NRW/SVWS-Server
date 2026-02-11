package de.svws_nrw.repo.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Mehrleistungen von Lehrern.
 */
public final class LehrerMehrleistungRepositoryImpl extends RepositoryImpl<DTOLehrerMehrleistung> implements LehrerMehrleistungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerMehrleistungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerMehrleistung.class, (o, id) -> o.ID = id);
	}

	@Override
	public Map<Long, List<DTOLehrerMehrleistung>> getMapByAbschnitt(final Collection<Long> idsAbschnitte) {
		if ((idsAbschnitte == null) || (idsAbschnitte.isEmpty()))
			return Collections.emptyMap();
		final var list = conn.queryList(DTOLehrerMehrleistung.QUERY_LIST_BY_ABSCHNITT_ID, DTOLehrerMehrleistung.class, idsAbschnitte);
		final var map = list.stream().collect(Collectors.groupingBy(f -> f.Abschnitt_ID));
		for (final long idAbschnitt : idsAbschnitte) {
			map.computeIfAbsent(idAbschnitt, id -> new ArrayList<>());
		}
		return map;
	}

}
