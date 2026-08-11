package de.svws_nrw.repo.schueler.lernabschnitt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Schüler-Lernabschnittsdaten-Tabelle der SVWS-Datenbank
 */
public interface SchuelerLernabschnittRepository extends Repository<DTOSchuelerLernabschnittsdaten> {

	/**
	 * Bestimmt die Zuordnung der Schüler-IDs zu den Klassen mit den übergebenen IDs.
	 *
	 * @param idsKlassen   die IDs der Klassen
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<Long>> getMapKlassenSchueler(Collection<Long> idsKlassen);


	/**
	 * Gibt eine Map der Lernabschnittsdaten für den Lernabschnitt mit der Wechsel-Nummer 0 aus dem Schuljahresabschnitt zugeordnet
	 * zu der der angegebenen ID für die Menge der angegebenen Schüler-IDs zurück. Sollte eine Schüler-ID nicht gefunden werden,
	 * so ist diese auch nicht in der Map enthalten.
	 *
	 * @param idsSchueler              die IDs der Schüler
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Map mit den Lernabschnitten der Schüler
	 */
	Map<Long, DTOSchuelerLernabschnittsdaten> getMapBySchuelerIDsAndSchuljahreabschnitt(Collection<Long> idsSchueler, long idSchuljahresabschnitt);

	/**
	 * Gibt eine Map der Lernabschnittsdaten für den Lernabschnitt mit der Wechsel-Nummer 0 aus dem Schuljahresabschnitt zugeordnet
	 * zu der der ID der Lernabschnittsdaten für die Menge der angegebenen Schüler-IDs zurück. Sollte eine Schüler-ID nicht gefunden werden,
	 * so ist für diese auch kein Lernabschnitt in der Map enthalten.
	 *
	 * @param idsSchueler              die IDs der Schüler
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Map mit den Lernabschnitten der Schüler
	 */
	Map<Long, DTOSchuelerLernabschnittsdaten> getMapByLernabschnittIDAndSchuljahreabschnitt(Collection<Long> idsSchueler, long idSchuljahresabschnitt);

	/**
	 * Gibt für die übergebene Menge an Schüler-IDs eine Map der Lernabschnittsdaten für die Lernabschnitte mit der Wechsel-Nummer 0 aus allen
	 * Schuljahresabschnitten zugeordnet zu der der ID der Lernabschnittsdaten für die Menge der angegebenen Schüler-IDs zurück.
	 * Sollte eine Schüler-ID nicht gefunden werden, so ist für diese auch kein Lernabschnitt in der Map enthalten.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Map mit den Lernabschnitten der Schüler
	 */
	Map<Long, DTOSchuelerLernabschnittsdaten> getMapByLernabschnittID(Collection<Long> idsSchueler);

	/**
	 * Gibt für die übergebene Menge an Schüler-IDs eine Map zurück, die jeder Schüler-ID die Liste aller IDs der zugehörigen
	 * Lernabschnittsdaten zuordnet. Sollte eine Schüler-ID nicht gefunden werden, so ist diese auch nicht in der Map enthalten.
	 *
	 * @param   idsSchueler die IDs der Schüler
	 *
	 * @return die Map mit den Lernabschnitts-IDs pro Schüler
	 */
	Map<Long, List<Long>> getMapAllLernabschnittIDsBySchuelerIDs(Collection<Long> idsSchueler);

	/**
	 * Gibt für die Schüler-ID die Lernabschnittsdaten zurück, welche die Wechsel-Nummer 0 haben und deren Schuljahresabschnitt-IDs
	 * dem Schüler aktuell zugeordnet ist.
	 *
	 * @param idSchueler   die IDs der Schüler
	 *
	 * @return die Lernabschnittsdaten
	 */
	Optional<DTOSchuelerLernabschnittsdaten> findAktuellBySchuelerID(Long idSchueler);

	/**
	 * Gibt für die übergebene Menge an Schüler-IDs eine Map zurück, die jeder Schüler-ID die Lernabschnittsdaten
	 * zuordnet, welche die Wechsel-Nummer 0 haben und deren Schuljahresabschnitt-IDs dem Schüler zugeordnet ist.
	 * Sollte eine Schüler-ID nicht gefunden werden oder kein aktueller Lernabschnitt dazu, so ist die Schüler-ID
	 * auch nicht in der zurückgegebenen Map vertreten.
	 *
	 * @param idsSchueler                die IDs der Schüler
	 *
	 * @return die Map mit der Zuordnung
	 */
	Map<Long, DTOSchuelerLernabschnittsdaten> getMapAktuelleBySchuelerIDs(Collection<Long> idsSchueler);

	/**
	 * Gibt für die übergebene Menge an Schüler-IDs eine Map zurück, die jeder Schüler-ID die Liste aller
	 * gewerteten Lernabschnittsdaten der Gymnasialen Oberstufe zuordnet.
	 * Sollte eine Schüler-ID nicht gefunden werden, so wird für diesen eine leere Liste in der Map zurückgegeben.
	 *
	 * @param   idsSchueler die IDs der Schüler
	 *
	 * @return die Map mit den Lernabschnitts-IDs pro Schüler
	 */
	Map<Long, List<DTOSchuelerLernabschnittsdaten>> getMapGostLernabschnitteBySchuelerIDs(Collection<Long> idsSchueler);

	/**
	 * Gibt die gewerteten Lernabschnitte des Schülers in dem angegeben Jahrgang zurück.
	 *
	 * @param idSchueler    die ID des Schülers
	 * @param asdJahrgang   der Statistik-Jahrgang
	 *
	 * @return die Liste der Lernabschnitte
	 */
	List<DTOSchuelerLernabschnittsdaten> getGewerteteAbschnittInASDJahrgang(long idSchueler, String asdJahrgang);
}
