package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf GOSt-Schülerklausurtermine.
 */
public interface GostKlausurenSchuelerklausurterminRepository extends Repository<DTOGostKlausurenSchuelerklausurenTermine> {

	/**
	 * Ermittelt Schülerklausurtermine zu den angegebenen Schülerklausuren.
	 *
	 * @param schuelerklausurIds die IDs der Schülerklausuren
	 *
	 * @return die Liste der Schülerklausurtermine
	 */
	List<DTOGostKlausurenSchuelerklausurenTermine> getListBySchuelerklausurIds(Collection<Long> schuelerklausurIds);

	/**
	 * Ermittelt Schülerklausurtermine zu den angegebenen tatsächlichen Klausurterminen. Für Nachschreibtermine wird die am
	 * Schülerklausurtermin gesetzte Termin-ID verwendet. Für Haupttermine ({@code Folge_Nr = 0}) wird die Termin-ID der zugehörigen
	 * Kursklausur verwendet, weil am Schülerklausurtermin keine Termin-ID gesetzt ist.
	 *
	 * @param terminIds die IDs der Klausurtermine
	 *
	 * @return die Liste der Schülerklausurtermine
	 */
	List<DTOGostKlausurenSchuelerklausurenTermine> getListByTerminIds(Collection<Long> terminIds);

	/**
	 * Ermittelt Schülerklausurtermine zu den angegebenen gesetzten Termin-IDs. Diese Abfrage berücksichtigt ausschließlich die direkt am
	 * Schülerklausurtermin gesetzte Termin-ID und liefert daher keine Haupttermine, deren Termin-ID an der Kursklausur gesetzt ist.
	 *
	 * @param terminIds die IDs der Klausurtermine
	 *
	 * @return die Liste der Schülerklausurtermine
	 */
	List<DTOGostKlausurenSchuelerklausurenTermine> getListByGesetztenTerminIds(Collection<Long> terminIds);

	/**
	 * Ermittelt Haupttermin-Schülerklausurtermine zu den angegebenen Kursklausuren.
	 *
	 * @param kursklausurIds die IDs der Kursklausuren
	 *
	 * @return die Liste der Haupttermin-Schülerklausurtermine
	 */
	List<DTOGostKlausurenSchuelerklausurenTermine> getListHaupttermineByKursklausurIds(Collection<Long> kursklausurIds);

}
