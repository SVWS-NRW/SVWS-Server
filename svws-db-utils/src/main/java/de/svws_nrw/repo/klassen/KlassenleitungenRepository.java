package de.svws_nrw.repo.klassen;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitungPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Klassenleitungen-Tabelle der SVWS-Datenbank
 */
public interface KlassenleitungenRepository extends RepositoryBase<DTOKlassenLeitung, DTOKlassenLeitungPK> {

	/**
	 * Bestimmt die Zuordnung der Lehrer-IDs der Klassenleitungen zu den Klassen mit den übergebenen IDs.
	 *
	 * @param idsKlassen   die IDs der Klassen
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<Long>> getMapKlassenleitungen(Collection<Long> idsKlassen);

	/**
	 * Bestimmt die Klassenleitungen der Klasse mit der übergebenen ID.
	 *
	 * @param idKlasse   die ID der Klasse
	 *
	 * @return die Klassenleitungen der Klasse, bzw. eine leere Liste, wenn die Klasse keine Klassenleitung hat
	 */
	List<DTOKlassenLeitung> findListByIdKlasse(long idKlasse);
}
