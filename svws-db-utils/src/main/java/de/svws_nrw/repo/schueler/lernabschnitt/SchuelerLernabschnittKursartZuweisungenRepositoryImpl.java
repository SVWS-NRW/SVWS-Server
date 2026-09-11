package de.svws_nrw.repo.schueler.lernabschnitt;

import java.util.Collection;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOSchuelerZuweisung;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOSchuelerZuweisungPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die neuen Schüler-Kursart-Zuweisungen für Lernabschnitte
 */
public final class SchuelerLernabschnittKursartZuweisungenRepositoryImpl extends RepositoryBaseImpl<DTOSchuelerZuweisung, DTOSchuelerZuweisungPK>
		implements SchuelerLernabschnittKursartZuweisungenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerLernabschnittKursartZuweisungenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerZuweisung.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOSchuelerZuweisungPK id) {
		return new Object[] { id.Abschnitt_ID, id.Fach_ID };
	}

	@Override
	public HashMap2D<Long, Long, DTOSchuelerZuweisung> getMapByLernabschnitte(final Collection<Long> idsLernabschnitte) {
		final HashMap2D<Long, Long, DTOSchuelerZuweisung> result = new HashMap2D<>();
		if ((idsLernabschnitte == null) || (idsLernabschnitte.isEmpty())) {
			return result;
		}

		final var listZuordnungen = conn.queryList(DTOSchuelerZuweisung.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerZuweisung.class, idsLernabschnitte);
		for (final var z : listZuordnungen) {
			result.put(z.Abschnitt_ID, z.Fach_ID, z);
		}
		return result;
	}

}
