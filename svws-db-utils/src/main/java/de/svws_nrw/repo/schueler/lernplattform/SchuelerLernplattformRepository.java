package de.svws_nrw.repo.schueler.lernplattform;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattformPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Zuordnungen von Schülern zu Lernplattformen
 * (inklusive Einwilligungen) in der SVWS-Datenbank. Die ID einer Zuordnung ist zusammengesetzt aus
 * der ID des Schülers und der ID der Lernplattform.
 */
public interface SchuelerLernplattformRepository extends RepositoryBase<DTOSchuelerLernplattform, DTOSchuelerLernplattformPK> {

	/**
	 * Bestimmt alle Lernplattform-Zuordnungen eines Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Zuordnungen des Schülers, leer wenn keine vorhanden sind
	 */
	List<DTOSchuelerLernplattform> getListByIdSchueler(long idSchueler);

	/**
	 * Bestimmt alle Schüler-Zuordnungen einer Lernplattform.
	 *
	 * @param idLernplattform   die ID der Lernplattform
	 *
	 * @return die Zuordnungen der Lernplattform, leer wenn keine vorhanden sind
	 */
	List<DTOSchuelerLernplattform> getListByIdLernplattform(long idLernplattform);

}
