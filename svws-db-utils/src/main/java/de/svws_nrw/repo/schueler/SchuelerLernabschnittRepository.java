package de.svws_nrw.repo.schueler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

}
