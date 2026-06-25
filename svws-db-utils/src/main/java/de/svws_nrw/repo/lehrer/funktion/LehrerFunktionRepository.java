package de.svws_nrw.repo.lehrer.funktion;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFunktion;
import de.svws_nrw.repo.Repository;

public interface LehrerFunktionRepository extends Repository<DTOLehrerFunktion> {

	/**
	 * Gibt alle Lehrerfunktionen für einen Lehrerabschnitt zurück.
	 *
	 * @param idAbschnitt die ID der Lehrerabschnittsdaten
	 * @return Liste der zugehörigen Lehrerfunktionen
	 */
	List<DTOLehrerFunktion> findAllByIdAbschnitt(long idAbschnitt);

	/**
	 * Prüft, ob bereits eine Lehrerfunktion mit der gegebenen Abschnitt- und Funktions-ID existiert.
	 *
	 * @param idAbschnitt die ID der Lehrerabschnittsdaten
	 * @param idFunktion  die ID der Funktion
	 * @return {@code true} wenn ein Eintrag existiert, sonst {@code false}
	 */
	boolean existsByIdAbschnittAndIdFunktion(long idAbschnitt, long idFunktion);

	/**
	 * Prüft, ob bereits eine Lehrerfunktion mit der gegebenen Abschnitt- und Funktions-ID existiert,
	 * wobei ein bestimmter Eintrag (über seine ID) bei der Prüfung ignoriert wird.
	 *
	 * <p>Typischer Anwendungsfall: Validierung bei einem Update – der aktuell zu ändernde Datensatz
	 * soll nicht als "Duplikat" gegen sich selbst zählen.</p>
	 *
	 * @param idAbschnitt die ID der Lehrerabschnittsdaten
	 * @param idFunktion  die ID der Funktion
	 * @param excludeId   die ID des Eintrags, der bei der Existenzprüfung ignoriert wird
	 * @return {@code true}, wenn ein anderer Eintrag mit {@code idAbschnitt} und {@code idFunktion} existiert,
	 *         dessen ID ungleich {@code excludeId} ist, sonst {@code false}
	 */
	boolean existsByIdAbschnittAndIdFunktionExcludingId(long idAbschnitt, long idFunktion, long excludeId);

}
