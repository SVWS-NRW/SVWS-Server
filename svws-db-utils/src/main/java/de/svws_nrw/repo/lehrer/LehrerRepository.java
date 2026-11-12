package de.svws_nrw.repo.lehrer;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Tabelle der SVWS-Datenbank. Die Referenzprüfung
 * für das Löschen von Lehrern erstreckt sich über die Tabellen, in denen Lehrer-IDs verwendet werden.
 */
public interface LehrerRepository extends ReferencedBulkDeletionRepository<DTOLehrer> {

	/**
	 * Gibt eine Liste aller Statistik-relevanten Lehrer zurück.
	 *
	 * @return die Liste mit den Statistik-relevanten Lehrer-DB-DTOs
	 */
	List<DTOLehrer> getAllStatistikRelevant();

	/**
	 * @param idLehrer {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idLehrer);

	/**
	 * Gibt die Menge der IDs zurück, für die ein Lehrer in der Datenbank existiert. Es wird nur
	 * eine einzige Abfrage auf die IDs ausgeführt, die Lehrer-Entitäten werden nicht geladen.
	 *
	 * @param ids   die zu prüfenden IDs
	 *
	 * @return die Menge der IDs, zu denen ein Lehrer gefunden wurde
	 */
	Set<Long> existsByIds(Collection<Long> ids);

	/**
	 * Gibt die IDs aller Lehrer zurück.
	 *
	 * @return die aufsteigend sortierten IDs aller Lehrer
	 */
	List<Long> getAllIds();

	/**
	 * Prüft, ob bereits ein Lehrer mit dem übergebenen Kürzel existiert.
	 *
	 * @param kuerzel   das zu prüfende Kürzel
	 *
	 * @return {@code true}, wenn das Kürzel bereits vergeben ist, sonst {@code false}
	 */
	boolean existsByKuerzel(String kuerzel);

	/**
	 * Prüft, ob ein anderer Lehrer als der mit der übergebenen ID das übergebene Kürzel verwendet.
	 *
	 * @param kuerzel    das zu prüfende Kürzel
	 * @param idLehrer   die ID des Lehrers, der bei der Prüfung ausgeschlossen wird
	 *
	 * @return {@code true}, wenn das Kürzel von einem anderen Lehrer verwendet wird, sonst {@code false}
	 */
	boolean existsByKuerzelExcludingId(String kuerzel, long idLehrer);

}
