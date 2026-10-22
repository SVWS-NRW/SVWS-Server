package de.svws_nrw.repo.lehrer.lernplattform;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattformPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Zuordnungen von Lehrern zu Lernplattformen
 * (inklusive Einwilligungen) in der SVWS-Datenbank. Die ID einer Zuordnung ist zusammengesetzt aus
 * der ID des Lehrers und der ID der Lernplattform.
 */
public interface LehrerLernplattformRepository extends RepositoryBase<DTOLehrerLernplattform, DTOLehrerLernplattformPK> {

	/**
	 * Bestimmt alle Lernplattform-Zuordnungen eines Lehrers.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Zuordnungen des Lehrers, leer wenn keine vorhanden sind
	 */
	List<DTOLehrerLernplattform> getListByIdLehrer(long idLehrer);

	/**
	 * Bestimmt alle Lehrer-Zuordnungen einer Lernplattform.
	 *
	 * @param idLernplattform   die ID der Lernplattform
	 *
	 * @return die Zuordnungen der Lernplattform, leer wenn keine vorhanden sind
	 */
	List<DTOLehrerLernplattform> getListByIdLernplattform(long idLernplattform);

}
