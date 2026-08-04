package de.svws_nrw.repo.wiedervorlage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schule.DTOWiedervorlage;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Implementierung des {@link WiedervorlageRepository}-Interfaces.
 * Enthält ausschließlich datenbankspezifische Zugriffsmethoden ohne Geschäftslogik.
 */
public final class WiedervorlageRepositoryImpl extends RepositoryImpl<DTOWiedervorlage> implements WiedervorlageRepository {

	/**
	 * Gibt die Wiedervorlagen zurück, die dem Benutzer direkt oder über eine Gruppe
	 * zugeordnet ist und die angegebene ID besitzt.
	 */
	static final String QUERY_BY_ID_AND_BENUTZER_ID =
			"""
			SELECT DISTINCT w \
			FROM DTOWiedervorlage w \
			  LEFT JOIN DTOBenutzergruppenMitglied m ON m.Gruppe_ID = w.idBenutzergruppe \
			WHERE w.id = ?1 \
			  AND (w.idBenutzer = ?2 OR m.Benutzer_ID = ?2) \
			""";

	/**
	 * Datenbankabfrage für die Anzahl der fälligen, noch nicht erledigten Wiedervorlagen (distinct)
	 * eines Benutzers oder einer Benutzergruppe des Nutzers.
	 */
	static final String QUERY_COUNT_FAELLIG_FOR_BENUTZER =
			"""
			SELECT COUNT(DISTINCT w.id) \
			FROM DTOWiedervorlage w \
			  LEFT JOIN DTOBenutzergruppenMitglied m ON m.Gruppe_ID = w.idBenutzergruppe \
			WHERE (w.idBenutzer = ?1 OR m.Benutzer_ID = ?1) \
			  AND w.tsErledigt IS NULL \
			  AND FUNCTION('DATE', w.tsWiedervorlage) <= CURRENT_DATE \
			""";

	/**
	 * Datenbankabfrage für alle Wiedervorlagen (distinct) eines Benutzers. Berücksichtigt sowohl
	 * direkt zugeordnete Wiedervorlagen als auch solche, die einer Benutzergruppe
	 * zugeordnet sind, der der Benutzer angehört.
	 */
	static final String QUERY_ALL_BY_BENUTZER_ID =
			"""
			SELECT DISTINCT w \
			FROM DTOWiedervorlage w \
			  LEFT JOIN DTOBenutzergruppenMitglied m ON m.Gruppe_ID = w.idBenutzergruppe \
			WHERE w.idBenutzer = ?1 \
			   OR m.Benutzer_ID = ?1 \
			ORDER BY w.tsWiedervorlage ASC \
			""";

	/**
	 * Datenbankabfrage für alle Wiedervorlagen (distinct) eines Benutzers. Berücksichtigt sowohl
	 * direkt zugeordnete Wiedervorlagen als auch solche, die einer Benutzergruppe
	 * zugeordnet sind, der der Benutzer angehört.
	 */
	static final String QUERY_ALL_BY_IDS_AND_BENUTZER_ID =
			"""
			SELECT DISTINCT w \
			FROM DTOWiedervorlage w \
			  LEFT JOIN DTOBenutzergruppenMitglied m ON m.Gruppe_ID = w.idBenutzergruppe \
			WHERE w.id IN ?1
			  AND (w.idBenutzer = ?2 OR m.Benutzer_ID = ?2 ) \
			ORDER BY w.tsWiedervorlage ASC \
			""";

	/**
	 * Löscht Wiedervorlagen anhand ihrer IDs, sofern der Benutzer direkt oder über
	 * eine Benutzergruppe Zugriff hat. Einträge ohne Zugriff werden ignoriert.
	 */
	static final String DELETE_BY_IDS = "DELETE FROM DTOWiedervorlage w WHERE w.id IN ?1";

	/**
	 * Löscht alle Wiedervorlagen, anhand des Fälligkeitsdatums die nicht
	 * explizit von der automatischen Löschung ausgenommen sind.
	 */
	static final String DELETE_ABGELAUFENE_WIEDERVORLAGEN =
			"""
			DELETE FROM DTOWiedervorlage w \
			WHERE w.tsWiedervorlage <= ?1 \
			 AND w.automatischErledigt = true \
			 AND w.idBenutzerErledigt != null \
			""";

	/**
	 * Erstellt eine neue Instanz des WiedervorlageRepositoryImpl.
	 *
	 * @param conn der Datenbankzugriff
	 */
	public WiedervorlageRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOWiedervorlage.class, e -> e.id, (e, id) -> e.id = id);
	}

	@Override
	public Optional<DTOWiedervorlage> findByIdAndBenutzerId(final long id, final long idBenutzer) {
		return conn.queryList(QUERY_BY_ID_AND_BENUTZER_ID, DTOWiedervorlage.class, id, idBenutzer)
				.stream()
				.findFirst();
	}

	@Override
	public List<DTOWiedervorlage> findAllByBenutzerId(final long idBenutzer) {
		return conn.queryList(QUERY_ALL_BY_BENUTZER_ID, DTOWiedervorlage.class, idBenutzer);
	}

	@Override
	public List<DTOWiedervorlage> findAllByIdsAndBenutzerId(final Set<Long> ids, final long idBenutzer) {
		return conn.queryList(QUERY_ALL_BY_IDS_AND_BENUTZER_ID, DTOWiedervorlage.class, ids, idBenutzer);
	}

	@Override
	public void deleteByIds(final Set<Long> ids) {
		conn.executeDelete(DELETE_BY_IDS, ids);
	}

	@Override
	public long getAnzahlOffeneWiedervorlagen(final long idBenutzer) {
		return conn.queryList(QUERY_COUNT_FAELLIG_FOR_BENUTZER, Long.class, idBenutzer)
				.getFirst();
	}

	@Override
	public void deleteAbgelaufeneWiedervorlagen(final LocalDate deleteDate) {
		conn.executeDelete(DELETE_ABGELAUFENE_WIEDERVORLAGEN, deleteDate.toString());
	}
}
