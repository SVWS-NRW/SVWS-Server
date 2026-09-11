package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurslehrer;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;

/**
 * Repository für den Import aus Gost-Kursblockungen.
 */
public interface GostKursblockungImportRepository {

	/**
	 * Liefert die Gost-Blockung zur angegebenen ID.
	 *
	 * @param idBlockung   die ID der Blockung
	 *
	 * @return die Gost-Blockung oder {@code null}
	 */
	DTOGostBlockung getBlockung(long idBlockung);

	/**
	 * Liefert das Gost-Zwischenergebnis zur angegebenen ID.
	 *
	 * @param idErgebnis   die ID des Zwischenergebnisses
	 *
	 * @return das Zwischenergebnis oder {@code null}
	 */
	DTOGostBlockungZwischenergebnis getZwischenergebnis(long idErgebnis);

	/**
	 * Liefert alle Gost-Kurse einer Blockung.
	 *
	 * @param idBlockung   die ID der Blockung
	 *
	 * @return die Liste der Kurse
	 */
	List<DTOGostBlockungKurs> getKurseByBlockungId(long idBlockung);

	/**
	 * Liefert alle Kurs-Lehrer-Zuordnungen zu den angegebenen Gost-Kursen.
	 *
	 * @param kursIds   die IDs der Gost-Kurse
	 *
	 * @return die Liste der Kurs-Lehrer-Zuordnungen
	 */
	List<DTOGostBlockungKurslehrer> getKurslehrerByKursIds(Collection<Long> kursIds);

	/**
	 * Liefert alle Schienen einer Gost-Blockung.
	 *
	 * @param idBlockung   die ID der Blockung
	 *
	 * @return die Liste der Schienen
	 */
	List<DTOGostBlockungSchiene> getSchienenByBlockungId(long idBlockung);

	/**
	 * Liefert die Kurs-Schienen-Zuordnungen eines Gost-Zwischenergebnisses.
	 *
	 * @param idErgebnis   die ID des Zwischenergebnisses
	 *
	 * @return die Liste der Kurs-Schienen-Zuordnungen
	 */
	List<DTOGostBlockungZwischenergebnisKursSchiene> getErgebnisKursSchienenByZwischenergebnisId(long idErgebnis);

	/**
	 * Liefert die Kurs-Schüler-Zuordnungen eines Gost-Zwischenergebnisses.
	 *
	 * @param idErgebnis   die ID des Zwischenergebnisses
	 *
	 * @return die Liste der Kurs-Schüler-Zuordnungen
	 */
	List<DTOGostBlockungZwischenergebnisKursSchueler> getErgebnisKursSchuelerByZwischenergebnisId(long idErgebnis);

}
