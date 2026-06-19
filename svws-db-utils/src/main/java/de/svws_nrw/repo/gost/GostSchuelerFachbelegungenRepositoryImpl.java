package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungenPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Zugriff auf die Schüler-Fachbelegungen in der gymnasialen Oberstufe in der SVWS-Datenbank
 */
public final class GostSchuelerFachbelegungenRepositoryImpl extends RepositoryBaseImpl<DTOGostSchuelerFachbelegungen, DTOGostSchuelerFachbelegungenPK>
		implements GostSchuelerFachbelegungenRepository {

	protected GostSchuelerFachbelegungenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostSchuelerFachbelegungen.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOGostSchuelerFachbelegungenPK id) {
		return new Object[] { id.Schueler_ID, id.Fach_ID };
	}

	@Override
	public HashMap2D<Long, Long, DTOGostSchuelerFachbelegungen> getMap2DBySchuelerIDAndFachID(final Collection<Long> idsSchueler) {
		final HashMap2D<Long, Long, DTOGostSchuelerFachbelegungen> result = new HashMap2D<>();
		final List<Long> tmp = idsSchueler.stream().filter(Objects::nonNull).distinct().toList();
		if (tmp.isEmpty()) {
			return result;
		}
		final List<DTOGostSchuelerFachbelegungen> fachbelegungen = conn.queryList(DTOGostSchuelerFachbelegungen.QUERY_LIST_BY_SCHUELER_ID, DTOGostSchuelerFachbelegungen.class, tmp);
		for (final DTOGostSchuelerFachbelegungen fb : fachbelegungen) {
			result.put(fb.Schueler_ID, fb.Fach_ID, fb);
		}
		return result;
	}

	@Override
	public Map<Long, List<DTOGostSchuelerFachbelegungen>> getMapBySchuelerID(final Collection<Long> idsSchueler) {
		final Map<Long, List<DTOGostSchuelerFachbelegungen>> result = new HashMap<>();
		final List<Long> tmp = idsSchueler.stream().filter(Objects::nonNull).distinct().toList();
		if (tmp.isEmpty()) {
			return result;
		}
		return conn.queryList(DTOGostSchuelerFachbelegungen.QUERY_LIST_BY_SCHUELER_ID, DTOGostSchuelerFachbelegungen.class, tmp)
				.stream().collect(Collectors.groupingBy(fb -> fb.Schueler_ID));
	}

	@Override
	public void deleteMultipleBySchuelerID(final Collection<Long> idsSchueler) {
		if (idsSchueler.isEmpty()) {
			return;
		}
		conn.transactionExecuteDelete("DELETE FROM DTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID IN (%s)"
				.formatted(idsSchueler.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(","))));
	}

}
