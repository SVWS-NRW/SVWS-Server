package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecherPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Zugriff auf die GOST-Fächer-Tabelle für die einzelnen Abiturjahrgänge der SVWS-Datenbank
 */
public final class GostJahrgangFaecherRepositoryImpl extends RepositoryBaseImpl<DTOGostJahrgangFaecher, DTOGostJahrgangFaecherPK>
		implements GostJahrgangFaecherRepository {

	protected GostJahrgangFaecherRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostJahrgangFaecher.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOGostJahrgangFaecherPK id) {
		return new Object[] { id.Abi_Jahrgang, id.Fach_ID };
	}


	@Override
	public HashMap2D<Integer, Long, DTOGostJahrgangFaecher> getMap2DByAbiturjahrgangAndFachID(final Collection<Integer> abiturjahrgaenge) {
		final HashMap2D<Integer, Long, DTOGostJahrgangFaecher> result = new HashMap2D<>();
		final List<Integer> tmp = abiturjahrgaenge.stream().filter(jg -> (jg != null) && (jg != -1)).distinct().toList();
		if (tmp.isEmpty()) {
			return result;
		}
		final List<DTOGostJahrgangFaecher> jahrgangfaecher = conn.queryList(DTOGostJahrgangFaecher.QUERY_LIST_BY_ABI_JAHRGANG, DTOGostJahrgangFaecher.class, tmp);
		for (final DTOGostJahrgangFaecher f : jahrgangfaecher) {
			result.put(f.Abi_Jahrgang, f.Fach_ID, f);
		}
		return result;
	}

}
