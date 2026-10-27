package de.svws_nrw.repo.kurse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToLongFunction;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchuelerPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Kurs-Schüler-Zuordnung.
 */
public final class KursSchuelerRepositoryImpl extends RepositoryBaseImpl<DTOKursSchueler, DTOKursSchuelerPK> implements KursSchuelerRepository {

	/** Die Datenbankabfrage für die Einträge im aktuellen Lernabschnitt zu einer Liste von Kursen */
	static final String QUERY_LIST_BY_KURS_ID_AKTUELL =
			"SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0";

	/** Die Datenbankabfrage für die Einträge im aktuellen Lernabschnitt zu einer Liste von Schülern */
	static final String QUERY_LIST_BY_SCHUELER_ID_AKTUELL =
			"SELECT e FROM DTOKursSchueler e WHERE e.Schueler_ID IN ?1 AND e.LernabschnittWechselNr = 0";

	/** Die Datenbankabfrage für die Einträge im aktuellen Lernabschnitt zu einer Liste von Schülern, eingeschränkt auf die Kurse eines Schuljahresabschnitts */
	static final String QUERY_LIST_BY_SCHUELER_ID_AND_SCHULJAHRESABSCHNITT_AKTUELL =
			"SELECT ks FROM DTOKurs k, DTOKursSchueler ks WHERE k.ID = ks.Kurs_ID AND k.Schuljahresabschnitts_ID = ?1 AND ks.Schueler_ID IN ?2 AND ks.LernabschnittWechselNr = 0";


	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public KursSchuelerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOKursSchueler.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOKursSchuelerPK id) {
		return new Object[] { id.Kurs_ID, id.Schueler_ID, id.LernabschnittWechselNr, id.Leistung_ID };
	}

	@Override
	public Map<Long, List<DTOKursSchueler>> getMapByKurse(final Collection<Long> idsKurse) {
		if ((idsKurse == null) || (idsKurse.isEmpty())) {
			return Collections.emptyMap();
		}
		final List<DTOKursSchueler> eintraege = conn.queryList(QUERY_LIST_BY_KURS_ID_AKTUELL, DTOKursSchueler.class, idsKurse);
		return groupBy(idsKurse, eintraege, e -> e.Kurs_ID);
	}

	@Override
	public Map<Long, List<DTOKursSchueler>> getMapBySchueler(final Collection<Long> idsSchueler, final Long idSchuljahresabschnitt) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyMap();
		}
		final List<DTOKursSchueler> eintraege = (idSchuljahresabschnitt == null)
				? conn.queryList(QUERY_LIST_BY_SCHUELER_ID_AKTUELL, DTOKursSchueler.class, idsSchueler)
				: conn.queryList(QUERY_LIST_BY_SCHUELER_ID_AND_SCHULJAHRESABSCHNITT_AKTUELL, DTOKursSchueler.class, idSchuljahresabschnitt, idsSchueler);
		return groupBy(idsSchueler, eintraege, e -> e.Schueler_ID);
	}

	/**
	 * Gruppiert die Einträge anhand des übergebenen Schlüssels. Jedem übergebenen Schlüssel wird mindestens eine leere Liste zugeordnet.
	 *
	 * @param keys         die Schlüssel, welche in der Map enthalten sein sollen
	 * @param eintraege    die zu gruppierenden Einträge
	 * @param getKey       die Funktion zur Bestimmung des Schlüssels eines Eintrags
	 *
	 * @return die Map mit den gruppierten Einträgen
	 */
	private static Map<Long, List<DTOKursSchueler>> groupBy(final Collection<Long> keys, final List<DTOKursSchueler> eintraege,
			final ToLongFunction<DTOKursSchueler> getKey) {
		final Map<Long, List<DTOKursSchueler>> map = HashMap.newHashMap(keys.size());
		for (final Long key : keys) {
			map.put(key, new ArrayList<>());
		}
		for (final DTOKursSchueler eintrag : eintraege) {
			final List<DTOKursSchueler> liste = map.get(getKey.applyAsLong(eintrag));
			if (liste != null) {
				liste.add(eintrag);
			}
		}
		return map;
	}

}
