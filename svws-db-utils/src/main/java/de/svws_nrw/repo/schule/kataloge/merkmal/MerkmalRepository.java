package de.svws_nrw.repo.schule.kataloge.merkmal;

import java.util.Optional;

import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.Repository;

public interface MerkmalRepository extends Repository<DTOMerkmale> {


	/**
	 * Prüft, ob ein Merkmal mit dem angegebenen Kürzel bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param kuerzel das zu prüfende Kürzel (Kurztext)
	 * @return {@code true}, wenn ein Merkmal mit diesem Kürzel existiert, {@code false} sonst
	 */
	boolean kuerzelIsAlreadyUsedCreate(String kuerzel);

	/**
	 * Prüft, ob ein Merkmal mit dem angegebenen Kürzel bereits in der Datenbank existiert,
	 * wobei das Merkmal mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param kuerzel das zu prüfende Kürzel (Kurztext)
	 * @param id die ID des Merkmals, das von der Prüfung ausgeschlossen werden soll
	 * @return {@code true}, wenn ein anderes Merkmal mit diesem Kürzel existiert, {@code false} sonst
	 */
	boolean kuerzelIsAlreadyUsedPatch(String kuerzel, long id);

	/**
	 * Prüft, ob ein Merkmal mit der angegebenen Bezeichnung bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung (Langtext)
	 * @return {@code true}, wenn ein Merkmal mit dieser Bezeichnung existiert, {@code false} sonst
	 */
	boolean bezeichnungIsAlreadyUsedCreate(String bezeichnung);

	/**
	 * Prüft, ob ein Merkmal mit der angegebenen Bezeichnung bereits in der Datenbank existiert,
	 * wobei das Merkmal mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung (Langtext)
	 * @param id die ID des Merkmals, das von der Prüfung ausgeschlossen werden soll
	 * @return {@code true}, wenn ein anderes Merkmal mit dieser Bezeichnung existiert, {@code false} sonst
	 */
	boolean bezeichnungIsAlreadyUsedPatch(String bezeichnung, long id);

	/**
	 * Gibt die Merkmal-entität zum Kürzel zurück
	 *
	 * @param kuerzel das Kürzel des Merkmals
	 *
	 * @return {@link DTOMerkmale}-Eintrag
	 * @throws ApiOperationException wenn kein Kürzel übergeben wurde oder dieses keinem Merkmal eindeutig zugeordnert werden kann
	 */
	Optional<DTOMerkmale> getByKuerzel(String kuerzel);
}
