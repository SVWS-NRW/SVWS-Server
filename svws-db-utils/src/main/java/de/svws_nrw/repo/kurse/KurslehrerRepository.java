package de.svws_nrw.repo.kurse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrerPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Kurs-Zusatzkräfte-Tabelle der SVWS-Datenbank
 */
public interface KurslehrerRepository extends RepositoryBase<DTOKursLehrer, DTOKursLehrerPK> {

	/**
	 * Bestimmt die Zuordnung der Kurslehrer-DTOs der Zusatzkräfte zu den Kursen mit den übergebenen IDs.
	 *
	 * @param idsKurse   die IDs der Kurse
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<DTOKursLehrer>> getMapZusatzkraefte(Collection<Long> idsKurse);

}
