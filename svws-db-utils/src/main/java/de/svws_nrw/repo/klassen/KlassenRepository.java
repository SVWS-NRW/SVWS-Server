package de.svws_nrw.repo.klassen;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Klassen-Tabelle der SVWS-Datenbank
 */
public interface KlassenRepository extends Repository<DTOKlassen> {

	/**
	 * Gibt eine Liste aller Klassen für den angegebenen Schuljahresabschnitt zurück.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Liste der Klassen
	 */
	List<DTOKlassen> getListBySchuljahresabschnitt(long idSchuljahresabschnitt);

	/**
	 * Gibt eine Map aller Klassen für den angegebenen Schuljahresabschnitt zurück.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Map der Klassen
	 */
	Map<Long, DTOKlassen> getMapBySchuljahresabschnitt(long idSchuljahresabschnitt);

	/**
	 * @param idKlasse {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idKlasse);

	/**
	 * Bestimmt die Klasse zu dem angegebenen Kürzel im angegebenen Schuljahresabschnitt.
	 *
	 * @param kuerzel                  das Kürzel der Klasse
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return ein Optional mit der Klasse, bzw. ein leeres Optional, wenn das Kürzel null ist oder keine Klasse gefunden wurde
	 */
	Optional<DTOKlassen> findByKuerzelAndSchuljahresabschnitt(String kuerzel, long idSchuljahresabschnitt);

	/**
	 * Bestimmt die Klasse zu dem angegebenen Kürzel im angegebenen Schuljahresabschnitt. Ist das Kürzel null oder
	 * wird keine Klasse gefunden, so wird die Klasse anhand des ASD-Kürzels im selben Schuljahresabschnitt bestimmt.
	 * Gibt es mehrere Treffer, so wird der erste Treffer zurückgegeben.
	 *
	 * @param kuerzel                  das Kürzel der Klasse, kann null sein
	 * @param asdKuerzel               das ASD-Kürzel der Klasse, kann null sein
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return ein Optional mit der Klasse, bzw. ein leeres Optional, wenn keine Klasse gefunden wurde
	 */
	Optional<DTOKlassen> findByKuerzelOrASDKuerzelAndSchuljahresabschnitt(String kuerzel, String asdKuerzel, long idSchuljahresabschnitt);

	/**
	 * Prüft, ob in dem angegebenen Schuljahresabschnitt bereits eine Klasse mit dem Kürzel existiert.
	 * Diese Methode ist für das Anlegen einer Klasse gedacht.
	 *
	 * @param kuerzel                  das zu prüfende Kürzel
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return {@code true}, wenn bereits eine Klasse mit diesem Kürzel im Schuljahresabschnitt existiert, sonst {@code false}
	 */
	boolean kuerzelIsAlreadyUsedCreate(String kuerzel, long idSchuljahresabschnitt);

	/**
	 * Prüft, ob in dem angegebenen Schuljahresabschnitt bereits eine andere Klasse mit dem Kürzel existiert,
	 * wobei die Klasse mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Diese Methode ist für das Ändern einer Klasse gedacht, damit sich die Klasse nicht selbst als Duplikat erkennt.
	 *
	 * @param kuerzel                  das zu prüfende Kürzel
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param idKlasse                 die ID der Klasse, die von der Prüfung ausgeschlossen werden soll
	 *
	 * @return {@code true}, wenn eine andere Klasse mit diesem Kürzel im Schuljahresabschnitt existiert, sonst {@code false}
	 */
	boolean kuerzelIsAlreadyUsedPatch(String kuerzel, long idSchuljahresabschnitt, long idKlasse);

}
