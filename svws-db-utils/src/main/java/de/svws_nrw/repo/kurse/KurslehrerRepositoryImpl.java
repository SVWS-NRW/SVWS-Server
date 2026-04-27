package de.svws_nrw.repo.kurse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrerPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Zusatzkräfte von Kursen.
 */
public final class KurslehrerRepositoryImpl extends RepositoryBaseImpl<DTOKursLehrer, DTOKursLehrerPK> implements KurslehrerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public KurslehrerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOKursLehrer.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOKursLehrerPK id) {
		return new Object[] { id.Kurs_ID, id.Lehrer_ID };
	}

	@Override
	public Map<Long, List<DTOKursLehrer>> getMapZusatzkraefte(final Collection<Long> idsKurse) {
		if ((idsKurse == null) || (idsKurse.isEmpty())) {
			return Collections.emptyMap();
		}

		final var listZusatzkraefte = conn.queryList(DTOKursLehrer.QUERY_LIST_BY_KURS_ID, DTOKursLehrer.class, idsKurse);

		final Map<Long, List<DTOKursLehrer>> map = HashMap.newHashMap(idsKurse.size());
		for (final Long idKurs : idsKurse) {
			map.put(idKurs, new ArrayList<>());
		}

		for (final var l : listZusatzkraefte) {
			final List<DTOKursLehrer> lehrerListe = map.get(l.Kurs_ID);
			if (lehrerListe != null) {
				lehrerListe.add(l);
			}
		}
		return map;
	}

}
