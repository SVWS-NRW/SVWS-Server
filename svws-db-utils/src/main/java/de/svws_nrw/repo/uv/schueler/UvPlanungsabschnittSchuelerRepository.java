package de.svws_nrw.repo.uv.schueler;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchueler;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchuelerPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Planungsabschnitt-Schüler-Zuordnungen
 * (UV_PlanungsabschnittSchueler) der SVWS-Datenbank.
 */
public interface UvPlanungsabschnittSchuelerRepository extends RepositoryBase<DTOUvPlanungsabschnittSchueler, DTOUvPlanungsabschnittSchuelerPK> {

	/**
	 * Ermittelt alle Planungsabschnitt-Schüler-Zuordnungen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	List<DTOUvPlanungsabschnittSchueler> getListByPlanungsabschnitt(long idPlanungsabschnitt);

	/**
	 * Ermittelt die Schüler-Datensätze zu den übergebenen IDs.
	 *
	 * @param schuelerIds   die Schüler-IDs
	 *
	 * @return die Liste der Schüler-Datensätze
	 */
	List<DTOSchueler> getSchuelerByIds(Collection<Long> schuelerIds);

}
