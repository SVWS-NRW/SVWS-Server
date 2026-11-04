package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholungFachwahlen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholungFachwahlenPK;
import de.svws_nrw.repo.RepositoryBaseImpl;


/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Fachwahlen, die Schüler für den Abiturjahrgang ihrer Wiederholung planen.
 */
public final class GostSchuelerWiederholungFachwahlenRepositoryImpl
		extends RepositoryBaseImpl<DTOGostSchuelerWiederholungFachwahlen, DTOGostSchuelerWiederholungFachwahlenPK>
		implements GostSchuelerWiederholungFachwahlenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	protected GostSchuelerWiederholungFachwahlenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostSchuelerWiederholungFachwahlen.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOGostSchuelerWiederholungFachwahlenPK id) {
		return new Object[] { id.Schueler_ID, id.Fach_ID };
	}

	@Override
	public Map<Long, List<DTOGostSchuelerWiederholungFachwahlen>> getMapBySchuelerID(final Collection<Long> idsSchueler) {
		final List<Long> tmp = idsSchueler.stream().filter(Objects::nonNull).distinct().toList();
		if (tmp.isEmpty()) {
			return new HashMap<>();
		}
		return conn.queryList(DTOGostSchuelerWiederholungFachwahlen.QUERY_LIST_BY_SCHUELER_ID, DTOGostSchuelerWiederholungFachwahlen.class, tmp)
				.stream().collect(Collectors.groupingBy(fw -> fw.Schueler_ID));
	}

	@Override
	public void deleteMultipleBySchuelerID(final Collection<Long> idsSchueler) {
		final List<Long> tmp = idsSchueler.stream().filter(Objects::nonNull).distinct().toList();
		if (tmp.isEmpty()) {
			return;
		}
		conn.transactionExecuteDelete("DELETE FROM DTOGostSchuelerWiederholungFachwahlen e WHERE e.Schueler_ID IN (%s)"
				.formatted(tmp.stream().map(String::valueOf).collect(Collectors.joining(","))));
	}

}
