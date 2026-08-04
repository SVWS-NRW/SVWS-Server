package de.svws_nrw.repo.wiedervorlage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.svws_nrw.db.dto.current.schule.DTOWiedervorlage;
import de.svws_nrw.repo.Repository;

/**
 * Interface für den Datenbankzugriff auf {@link DTOWiedervorlage}-Entitäten.
 * Deklariert domänenspezifische Query-Methoden zusätzlich zu den geerbten CRUD-Operationen.
 */
public interface WiedervorlageRepository extends Repository<DTOWiedervorlage> {

	/**
	 * Gibt die Wiedervorlage mit der angegebenen ID zurück, sofern der Benutzer
	 * auf sie zugreifen darf. Zugriff besteht, wenn die Wiedervorlage dem Benutzer
	 * direkt zugeordnet ist oder einer Benutzergruppe, der der Benutzer angehört.
	 *
	 * @param id         die ID der gesuchten Wiedervorlage
	 * @param idBenutzer die ID des zugreifenden Benutzers
	 *
	 * @return die Wiedervorlage, oder {@link Optional#empty()} wenn nicht vorhanden oder kein Zugriff
	 */
	Optional<DTOWiedervorlage> findByIdAndBenutzerId(long id, long idBenutzer);

	/**
	 * Liefert alle Wiedervorlage-Einträge, die direkt einem bestimmten Benutzer gehören.
	 *
	 * @param idBenutzer die ID des Benutzers
	 *
	 * @return Liste der zugehörigen {@link DTOWiedervorlage}-Entitäten, nie {@code null}
	 */
	List<DTOWiedervorlage> findAllByBenutzerId(long idBenutzer);

	/**
	 * Liefert alle Wiedervorlage-Einträge, die direkt einem bestimmten Benutzer gehören.
	 *
	 * @param ids die zu fetchenden Ids
	 * @param idBenutzer die ID des Benutzers
	 *
	 * @return Liste der zugehörigen {@link DTOWiedervorlage}-Entitäten, nie {@code null}
	 */
	List<DTOWiedervorlage> findAllByIdsAndBenutzerId(Set<Long> ids, long idBenutzer);

	/**
	 * Löscht Wiedervorlagen anhand ihrer Ids
	 * @param ids Ids
	 */
	void deleteByIds(Set<Long> ids);

	/**
	 * Gibt die Anzahl offener Wiedervorlagen des heutigen Datums zurück
	 * @param idBenutzer Die BenutzerId
	 *
	 * @return Anzahl offener Wiedervorlagen als Int
	 */
	long getAnzahlOffeneWiedervorlagen(long idBenutzer);

	/**
	 * Löscht alle erledigten Wiedervorlagen, deren Wiedervorlage-Zeitpunkt vor dem
	 * übergebenen delete Datum liegt und die nicht explizit von der automatischen Löschung
	 * ausgenommen sind.
	 *
	 * @param deleteDate Date, ab dem Wiedervorlagen gelöscht werden
	 *
	 */
	void deleteAbgelaufeneWiedervorlagen(LocalDate deleteDate);
}
