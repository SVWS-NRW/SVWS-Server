package de.svws_nrw.repo.lehrer;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Abschnittsdaten-Tabelle der SVWS-Datenbank
 */
public interface LehrerAbschnittsdatenRepository extends Repository<DTOLehrerAbschnittsdaten> {

	/**
	 * Bestimmt die Lehrer-Abschnittsdaten-Datenbank-Objekte für einen Schuljahresabschnitt für die übergebenen Lehrer-IDs
	 *
	 * @param idsLehrer                die IDs der Lehrer für welche die Abschnittsdaten bestimmt werden sollen
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes für welchen die Abschnittsdaten bestimmt werden sollen
	 *
	 * @return die Liste mit den Abschnittsdaten-DB-DTOs
	 */
	List<DTOLehrerAbschnittsdaten> getListByLehrerIdsAndSchuljahresabschnitt(Collection<Long> idsLehrer, long idSchuljahresabschnitt);

}
