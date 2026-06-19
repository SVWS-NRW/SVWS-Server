package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Zugriff auf die GOST-Jahrgangs-Tabelle für die einzelnen Abiturjahrgänge der SVWS-Datenbank
 */
public final class GostJahrgangsdatenRepositoryImpl extends RepositoryBaseImpl<DTOGostJahrgangsdaten, Integer> implements GostJahrgangsdatenRepository {

	protected GostJahrgangsdatenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostJahrgangsdaten.class);
	}

	@Override
	protected Object[] mapIdToParameter(final Integer abijahrgang) {
		return new Object[] { abijahrgang };
	}

	@Override
	public List<DTOGostJahrgangsdaten> findListByIds(final Collection<Integer> abiturjahrgaenge) {
		if ((abiturjahrgaenge == null) || (abiturjahrgaenge.isEmpty())) {
			return Collections.emptyList();
		}
		return conn.queryByKeyList(entityClass, abiturjahrgaenge);
	}


	@Override
	public Map<Integer, DTOGostJahrgangsdaten> findMapByIds(final Collection<Integer> abiturjahrgaenge) {
		return this.findListByIds(abiturjahrgaenge).stream().filter(Objects::nonNull).collect(Collectors.toMap(e -> e.Abi_Jahrgang, e -> e));
	}

}
