package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzkompetenz;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

/**
 * Das Interface für ein Repository zum Zugriff auf den Katalog der Ankreuzkompetenzen der SVWS-Datenbank
 */
public interface AnkreuzkompetenzRepository extends ReferencedBulkDeletionRepository<DTOAnkreuzkompetenz> {

	/**
	 * @param idAnkreuzkompetenz {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idAnkreuzkompetenz);

	/**
	 * Prüft, ob der Floskeltext für das angegebene Fach bereits bei einer Ankreuzkompetenz verwendet wird.
	 * Die Prüfung erfolgt case-insensitiv. Ist {@code idFach} {@code null}, so werden die Einträge ohne Fach geprüft.
	 *
	 * @param floskelText der zu prüfende Floskeltext
	 * @param idFach      die ID des Faches oder {@code null} für Einträge ohne Fach
	 *
	 * @return {@code true}, wenn der Floskeltext für das Fach bereits verwendet wird, sonst {@code false}
	 */
	boolean floskelTextIsAlreadyUsedCreate(String floskelText, Long idFach);

	/**
	 * Prüft, ob der Floskeltext für das angegebene Fach bereits bei einer anderen Ankreuzkompetenz verwendet wird,
	 * wobei die Ankreuzkompetenz mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv. Ist {@code idFach} {@code null}, so werden die Einträge ohne Fach geprüft.
	 * <p>
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass sich ein Eintrag selbst als Duplikat erkennt.
	 *
	 * @param floskelText der zu prüfende Floskeltext
	 * @param idFach      die ID des Faches oder {@code null} für Einträge ohne Fach
	 * @param id          die ID der Ankreuzkompetenz, die von der Prüfung ausgeschlossen werden soll
	 *
	 * @return {@code true}, wenn der Floskeltext für das Fach bei einer anderen Ankreuzkompetenz verwendet wird, sonst {@code false}
	 */
	boolean floskelTextIsAlreadyUsedPatch(String floskelText, Long idFach, long id);

}
