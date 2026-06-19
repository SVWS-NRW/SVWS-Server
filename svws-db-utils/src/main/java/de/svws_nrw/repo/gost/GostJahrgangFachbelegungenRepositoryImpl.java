package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungenPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Zugriff auf die Vorlagen-Fachbelegungen von Abiturjahrgängen in der gymnasialen Oberstufe in der SVWS-Datenbank
 */
public final class GostJahrgangFachbelegungenRepositoryImpl extends RepositoryBaseImpl<DTOGostJahrgangFachbelegungen, DTOGostJahrgangFachbelegungenPK>
		implements GostJahrgangFachbelegungenRepository {

	protected GostJahrgangFachbelegungenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostJahrgangFachbelegungen.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOGostJahrgangFachbelegungenPK id) {
		return new Object[] { id.Abi_Jahrgang, id.Fach_ID };
	}

	@Override
	public HashMap2D<Integer, Long, DTOGostJahrgangFachbelegungen> getMap2DByAbiturjahrgangAndFachID(final Collection<Integer> abiturjahrgaenge) {
		final HashMap2D<Integer, Long, DTOGostJahrgangFachbelegungen> result = new HashMap2D<>();
		final List<Integer> tmp = abiturjahrgaenge.stream().filter(Objects::nonNull).distinct().toList();
		if (tmp.isEmpty()) {
			return result;
		}
		final List<DTOGostJahrgangFachbelegungen> fachbelegungen = conn.queryList(DTOGostJahrgangFachbelegungen.QUERY_LIST_BY_ABI_JAHRGANG, DTOGostJahrgangFachbelegungen.class, tmp);
		for (final DTOGostJahrgangFachbelegungen fb : fachbelegungen) {
			result.put(fb.Abi_Jahrgang, fb.Fach_ID, fb);
		}
		return result;
	}

	@Override
	public void deleteMultipleByAbiturjahrgang(final Collection<Long> abiturjahrgaenge) {
		conn.transactionExecuteDelete("DELETE FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang IN %d".formatted(abiturjahrgaenge));
	}

}
