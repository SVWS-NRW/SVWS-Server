package de.svws_nrw.repo.gost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die GOST-Jahrgangs-Tabelle mit den Fachkombinationen der SVWS-Datenbank
 */
public final class GostJahrgangFachkombinationenRepositoryImpl extends RepositoryImpl<DTOGostJahrgangFachkombinationen> implements GostJahrgangFachkombinationenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public GostJahrgangFachkombinationenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostJahrgangFachkombinationen.class, o -> o.ID, (o, id) -> o.ID	 = id);
	}


	@Override
	public List<DTOGostJahrgangFachkombinationen> getListByAbiturjahrgaenge(final Collection<Integer> abiturjahrgaenge) {
		final List<Integer> tmp = abiturjahrgaenge.stream().filter(Objects::nonNull).distinct().toList();
		if (tmp.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryList(DTOGostJahrgangFachkombinationen.QUERY_LIST_BY_ABI_JAHRGANG, DTOGostJahrgangFachkombinationen.class, tmp);
	}

	@Override
	public Map<Integer, List<DTOGostJahrgangFachkombinationen>> getMapByAbiturjahrgaenge(final Collection<Integer> abiturjahrgaenge) {
		return this.getListByAbiturjahrgaenge(abiturjahrgaenge).stream().collect(Collectors.groupingBy(fk -> fk.Abi_Jahrgang));
	}

}
