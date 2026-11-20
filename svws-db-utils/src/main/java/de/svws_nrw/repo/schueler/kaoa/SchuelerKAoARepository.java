package de.svws_nrw.repo.schueler.kaoa;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die KAoA-Daten der Schüler in der SVWS-Datenbank.
 */
public interface SchuelerKAoARepository extends Repository<DTOSchuelerKAoADaten> {

	/**
	 * Bestimmt die KAoA-Daten, die zu den übergebenen Lernabschnitten gehören.
	 *
	 * @param idsLernabschnitte   die IDs der Lernabschnitte
	 *
	 * @return die KAoA-Daten oder eine leere Liste, wenn keine IDs übergeben oder keine Daten gefunden wurden
	 */
	List<DTOSchuelerKAoADaten> findListByLernabschnitte(Collection<Long> idsLernabschnitte);

}
