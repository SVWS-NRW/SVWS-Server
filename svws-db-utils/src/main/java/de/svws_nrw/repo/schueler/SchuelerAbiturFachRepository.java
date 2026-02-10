package de.svws_nrw.repo.schueler;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Fächer-Tabelle für die Schüler-Abiturdaten der SVWS-Datenbank
 */
public interface SchuelerAbiturFachRepository extends Repository<DTOSchuelerAbiturFach> {

	/**
	 * Bestimmt die Schüler-Abiturfach-Datenbank-Objekte für die übergebenen Schüler-IDs
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Liste mit den DB-DTOs
	 */
	List<DTOSchuelerAbiturFach> getListBySchuelerIds(Collection<Long> idsSchueler);

	/**
	 * Bestimmt die Schüler-Abiturfach-Datenbank-Objekte für die übergebenen Schüler-IDs mit der Einschränkung
	 * auf die Prüfungsfächer
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Liste mit den DB-DTOs
	 */
	List<DTOSchuelerAbiturFach> getListBySchuelerIdsNurPruefungsfaecher(Collection<Long> idsSchueler);

}
