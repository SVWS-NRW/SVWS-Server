package de.svws_nrw.repo.klassen;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Klassendaten.
 */
public final class KlassenRepositoryImpl extends RepositoryImpl<DTOKlassen> implements KlassenRepository {

	/** Die Abfrage einer Klasse anhand des Kürzels im Schuljahresabschnitt. */
	static final String QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT =
			"SELECT e FROM DTOKlassen e WHERE e.Klasse = ?1 AND e.Schuljahresabschnitts_ID = ?2";

	/** Die Abfrage einer Klasse anhand des ASD-Kürzels im Schuljahresabschnitt. */
	static final String QUERY_BY_ASD_KUERZEL_AND_SCHULJAHRESABSCHNITT =
			"SELECT e FROM DTOKlassen e WHERE e.ASDKlasse = ?1 AND e.Schuljahresabschnitts_ID = ?2";

	/** Die Abfrage einer anderen Klasse als der angegebenen mit dem Kürzel im Schuljahresabschnitt. */
	static final String QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT_EXCLUDING_ID =
			"SELECT e FROM DTOKlassen e WHERE e.Klasse = ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.ID <> ?3";

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public KlassenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOKlassen.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOKlassen> getListBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt);
	}

	@Override
	public Map<Long, DTOKlassen> getMapBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return this.getListBySchuljahresabschnitt(idSchuljahresabschnitt).stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	@Override
	public boolean existsById(final Long idKlasse) {
		return conn.existsBy(DTOKlassen.QUERY_BY_ID, DTOKlassen.class, idKlasse);
	}

	@Override
	public Optional<DTOKlassen> findByKuerzelAndSchuljahresabschnitt(final String kuerzel, final long idSchuljahresabschnitt) {
		if (kuerzel == null) {
			return Optional.empty();
		}
		return conn.queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, kuerzel, idSchuljahresabschnitt).stream().findFirst();
	}

	@Override
	public Optional<DTOKlassen> findByKuerzelOrASDKuerzelAndSchuljahresabschnitt(final String kuerzel, final String asdKuerzel,
			final long idSchuljahresabschnitt) {
		final Optional<DTOKlassen> klasse = findByKuerzelAndSchuljahresabschnitt(kuerzel, idSchuljahresabschnitt);
		if (klasse.isPresent() || (asdKuerzel == null)) {
			return klasse;
		}
		return conn.queryList(QUERY_BY_ASD_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, asdKuerzel, idSchuljahresabschnitt).stream().findFirst();
	}

	@Override
	public boolean kuerzelIsAlreadyUsedCreate(final String kuerzel, final long idSchuljahresabschnitt) {
		return conn.existsBy(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, kuerzel, idSchuljahresabschnitt);
	}

	@Override
	public boolean kuerzelIsAlreadyUsedPatch(final String kuerzel, final long idSchuljahresabschnitt, final long idKlasse) {
		return conn.existsBy(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT_EXCLUDING_ID, DTOKlassen.class, kuerzel, idSchuljahresabschnitt, idKlasse);
	}

}
