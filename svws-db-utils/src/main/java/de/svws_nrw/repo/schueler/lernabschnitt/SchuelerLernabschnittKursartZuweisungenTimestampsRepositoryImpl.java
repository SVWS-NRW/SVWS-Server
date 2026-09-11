package de.svws_nrw.repo.schueler.lernabschnitt;

import java.util.Collection;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerZuweisungen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerZuweisungenPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitstempel der neuen Schüler-Kursart-Zuweisungen für Lernabschnitte
 */
public final class SchuelerLernabschnittKursartZuweisungenTimestampsRepositoryImpl
		extends RepositoryBaseImpl<DTOTimestampsSchuelerZuweisungen, DTOTimestampsSchuelerZuweisungenPK>
		implements SchuelerLernabschnittKursartZuweisungenTimestampsRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerLernabschnittKursartZuweisungenTimestampsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTimestampsSchuelerZuweisungen.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOTimestampsSchuelerZuweisungenPK id) {
		return new Object[] { id.Abschnitt_ID, id.Fach_ID };
	}

	@Override
	public HashMap2D<Long, Long, DTOTimestampsSchuelerZuweisungen> getMapByLernabschnitte(final Collection<Long> idsLernabschnitte) {
		final HashMap2D<Long, Long, DTOTimestampsSchuelerZuweisungen> result = new HashMap2D<>();
		if ((idsLernabschnitte == null) || (idsLernabschnitte.isEmpty())) {
			return result;
		}

		final var listZuordnungen = conn.queryList(DTOTimestampsSchuelerZuweisungen.QUERY_LIST_BY_ABSCHNITT_ID, DTOTimestampsSchuelerZuweisungen.class, idsLernabschnitte);
		for (final var z : listZuordnungen) {
			result.put(z.Abschnitt_ID, z.Fach_ID, z);
		}
		return result;
	}

}
