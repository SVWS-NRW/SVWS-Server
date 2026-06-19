package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrerPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Zugriff auf die GOST-Beratungslehrer-Tabelle für die einzelnen Abiturjahrgänge der SVWS-Datenbank
 */
public final class GostJahrgangBeratungslehrerRepositoryImpl extends RepositoryBaseImpl<DTOGostJahrgangBeratungslehrer, DTOGostJahrgangBeratungslehrerPK>
		implements GostJahrgangBeratungslehrerRepository {

	protected GostJahrgangBeratungslehrerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostJahrgangBeratungslehrer.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOGostJahrgangBeratungslehrerPK id) {
		return new Object[] { id.Abi_Jahrgang, id.Lehrer_ID };
	}


	@Override
	public List<DTOGostJahrgangBeratungslehrer> getListByAbiturjahrgaenge(final Collection<Integer> abiturjahrgaenge) {
		final List<Integer> tmp = abiturjahrgaenge.stream().filter(jg -> (jg != null) && (jg != -1)).distinct().toList();
		if (tmp.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOGostJahrgangBeratungslehrer.QUERY_LIST_BY_ABI_JAHRGANG, DTOGostJahrgangBeratungslehrer.class, tmp);
	}

	@Override
	public HashMap2D<Integer, Long, DTOGostJahrgangBeratungslehrer> getMap2DByAbiturjahrgaengeAndLehrerID(final Collection<Integer> abiturjahrgaenge) {
		final List<DTOGostJahrgangBeratungslehrer> beratungslehrer = this.getListByAbiturjahrgaenge(abiturjahrgaenge);
		final HashMap2D<Integer, Long, DTOGostJahrgangBeratungslehrer> result = new HashMap2D<>();
		for (final DTOGostJahrgangBeratungslehrer f : beratungslehrer) {
			result.put(f.Abi_Jahrgang, f.Lehrer_ID, f);
		}
		return result;
	}

}
