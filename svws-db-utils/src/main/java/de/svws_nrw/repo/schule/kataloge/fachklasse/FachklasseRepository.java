package de.svws_nrw.repo.schule.kataloge.fachklasse;

import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

public interface FachklasseRepository extends ReferencedBulkDeletionRepository<DTOFachklassen> {

	/**
	 * Prüft, ob eine Fachklasse mit dem angegebenen Kürzel bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param kuerzel das zu prüfende Kürzel (Kurztext)
	 * @return {@code true}, wenn eine Fachklasse mit diesem Kürzel existiert, sonst {@code false}
	 */
	boolean kuerzelIsAlreadyUsedCreate(String kuerzel);

	/**
	 * Prüft, ob eine Fachklasse mit dem angegebenen Kürzel bereits in der Datenbank existiert,
	 * wobei die Fachklasse mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * <p>
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param kuerzel das zu prüfende Kürzel
	 * @param id die ID der Fachklasse, das von der Prüfung ausgeschlossen werden soll
	 * @return {@code true}, wenn ein andere Fachklasse mit diesem Kürzel existiert, sonst {@code false}
	 */
	boolean kuerzelIsAlreadyUsedPatch(String kuerzel, long id);

}
