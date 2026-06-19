package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die GOST-Jahrgangs-Tabelle für die einzelnen Abiturjahrgänge der SVWS-Datenbank
 */
public interface GostJahrgangsdatenRepository extends RepositoryBase<DTOGostJahrgangsdaten, Integer> {

	/**
	 * Bestimmt die Jahrgangsdaten anhand der Abiturjahrgänge.
	 * Sind für Datenbank-IDs keine Jahrgangs für die Abiturjahrgänge vorhanden,
	 * so werden nur die gefunden Entitäten zurückgegeben.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge
	 *
	 * @return die Liste mit den gefundenen Jahrgangsdaten
	 */
	List<DTOGostJahrgangsdaten> findListByIds(Collection<Integer> abiturjahrgaenge);


	/**
	 * Erstellt eine Map mit den zu den übergebenen IDs gehörenden Datenbank-Entitäten und ordnet
	 * diese ihrer jeweiligen ID zu.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge
	 *
	 * @return die Map mit der Zuordnung der Datenbank-Entitäten zu ihren IDs
	 */
	Map<Integer, DTOGostJahrgangsdaten> findMapByIds(Collection<Integer> abiturjahrgaenge);

}
