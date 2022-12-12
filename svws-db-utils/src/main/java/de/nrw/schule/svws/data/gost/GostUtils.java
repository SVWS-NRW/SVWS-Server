package de.nrw.schule.svws.data.gost;

import java.util.List;

import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.schule.Schulform;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKurs;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;

/**
 * Dies Klassen stellt Hilfmethoden für den Datenbankzugriff
 * zur Verfügung, welche in den Data-Klassen an mehreren Stellen 
 * verwendet werden.  
 */
public class GostUtils {
	
	/**
	 * Prüft, ob es die Schule eine Schulform mit einer Gymnasiale Oberstufe (GOSt) hat.
	 *
	 * @param conn   die aktuelle Datenbankverbindung 
	 * 
	 * @return das Datenbank-DTO der Schule, falls eine Schule mit Gymnasialer Oberstufe vorliegt
	 * 
	 * @throws WebApplicationException    falls keine Schule definiert ist oder die Schulform keine Gymnasiale Oberstufe hat 
	 */
	public static DTOEigeneSchule pruefeSchuleMitGOSt(final DBEntityManager conn) throws WebApplicationException {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception();
		Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			throw OperationError.NOT_FOUND.exception();
		return schule;
	}


	/**
	 * Prüft, ob in dem angebenen Schuljahresabschnitt für das angebene Halbjahr der gymnasialen Oberstufe
	 * bereits Kurse der gymnasialen Oberstufe vorhanden sidn oder nicht. 
	 * 
	 * @param conn       die aktuelle Datenbankverbindung
	 * @param halbjahr   das Halbjahr
	 * @param abschnitt  der Schuljahresabschnitt
	 * 
	 * @return true, wenn bereits Kurse vorhanden sind und ansonsten false
	 */
	public static boolean pruefeHatOberstufenKurseInAbschnitt(final DBEntityManager conn, 
			final GostHalbjahr halbjahr, final DTOSchuljahresabschnitte abschnitt) {
		List<DTOKurs> kurse = conn.queryList("SELECT e FROM DTOKurs e WHERE e.ASDJahrgang = ?1 AND e.Schuljahresabschnitts_ID = ?2", 
				DTOKurs.class, halbjahr.jahrgang, abschnitt.ID);
		for (DTOKurs kurs : kurse) {
			GostKursart kursart = GostKursart.fromKuerzel(kurs.KursartAllg);
			if (kursart != null)
				return true;
		}
		return false;
	}

}
