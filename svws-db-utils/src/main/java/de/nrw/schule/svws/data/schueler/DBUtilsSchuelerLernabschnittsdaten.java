package de.nrw.schule.svws.data.schueler;

import java.util.List;

import de.nrw.schule.svws.data.jahrgaenge.DBUtilsJahrgaenge;
import de.nrw.schule.svws.data.klassen.DBUtilsKlassen;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;

/**
 * Diese Klasse beinhaltet wiederverwendbare Hilfsmethoden
 * zu Schüler-Lernabschnittsdaten in Bezug auf den Datenbank-Zugriff. 
 */
public class DBUtilsSchuelerLernabschnittsdaten {

	/**
	 * Erstellt ein neues {@link DTOSchuelerLernabschnittsdaten}-Objekt mit Default-Werten.
	 * 
	 * @param idNew                    die ID des neuen Schüler-Lernabschnitts
	 * @param idSchueler               die ID des Schülers
	 * @param schuljahresabschnitt     der Schuljahresabschnitt
	 * @param klasse                   das DTO der Klassen, dem der Lernabschnitt zugeordnet wird
	 * @param jahrgang                 das DTO des Jahrgangs, dem der Lernabschnitt zugeordnet wird
	 * 
	 * @return das DB-DTO-Objekt
	 */
	private static DTOSchuelerLernabschnittsdaten createDefault(final long idNew, final long idSchueler, final DTOSchuljahresabschnitte schuljahresabschnitt,
			final DTOKlassen klasse, final DTOJahrgang jahrgang) {
		DTOSchuelerLernabschnittsdaten lernabschnitt = new DTOSchuelerLernabschnittsdaten(idNew, idSchueler, schuljahresabschnitt.ID, false);
		lernabschnitt.WechselNr = null;
		lernabschnitt.Schulbesuchsjahre = null;
		lernabschnitt.Hochrechnung = null;
		lernabschnitt.SemesterWertung = true;
		lernabschnitt.PruefOrdnung = klasse.PruefOrdnung;
		lernabschnitt.Klassen_ID = klasse.ID;
		lernabschnitt.Verspaetet = null;
		lernabschnitt.NPV_Fach_ID = null;
		lernabschnitt.NPV_NoteKrz = null;
		lernabschnitt.NPV_Datum = null;
		lernabschnitt.NPAA_Fach_ID = null;
		lernabschnitt.NPAA_NoteKrz = null;
		lernabschnitt.NPAA_Datum = null;
		lernabschnitt.NPBQ_Fach_ID = null;
		lernabschnitt.NPBQ_NoteKrz = null;
		lernabschnitt.NPBQ_Datum = null;
		lernabschnitt.VersetzungKrz = null;
		lernabschnitt.AbschlussArt = null;
		lernabschnitt.AbschlIstPrognose = null;
		lernabschnitt.Konferenzdatum = null;
		lernabschnitt.ZeugnisDatum = null;
		lernabschnitt.Schulgliederung = jahrgang.Gliederung;
		lernabschnitt.ASDJahrgang = jahrgang.ASDJahrgang;
		lernabschnitt.Jahrgang_ID = jahrgang.ID;
		lernabschnitt.Fachklasse_ID = null;
		lernabschnitt.Schwerpunkt_ID = null;
		lernabschnitt.ZeugnisBem = null;
		lernabschnitt.Schwerbehinderung = null;
		lernabschnitt.Foerderschwerpunkt_ID = null;
		lernabschnitt.OrgFormKrz = klasse.OrgFormKrz;
		lernabschnitt.RefPaed = null;
		lernabschnitt.Klassenart = klasse.Klassenart;
		lernabschnitt.SumFehlStd = 0;
		lernabschnitt.SumFehlStdU = 0;
		lernabschnitt.Wiederholung = false; 
		lernabschnitt.Gesamtnote_GS = null;
		lernabschnitt.Gesamtnote_NW = null;
		lernabschnitt.Folgeklasse_ID = null;
		lernabschnitt.Foerderschwerpunkt2_ID = null;
		lernabschnitt.Abschluss = null;
		lernabschnitt.Abschluss_B = null;
		lernabschnitt.DSNote = null;
		lernabschnitt.AV_Leist = null;
		lernabschnitt.AV_Zuv = null;
		lernabschnitt.AV_Selbst = null;
		lernabschnitt.SV_Verant = null;
		lernabschnitt.SV_Konfl = null;
		lernabschnitt.SV_Koop = null;
		lernabschnitt.MoeglNPFaecher = null;
		lernabschnitt.Zertifikate = null;
		lernabschnitt.DatumFHR = null;
		lernabschnitt.PruefAlgoErgebnis = null;
		lernabschnitt.Zeugnisart = null;
		lernabschnitt.DatumVon = null;  // TODO
		lernabschnitt.DatumBis = null;  // TODO
		lernabschnitt.FehlstundenGrenzwert = null;
		lernabschnitt.Sonderpaedagoge_ID = null;  // TODO aus alten Lernabschnitt übernehmen
		lernabschnitt.FachPraktAnteilAusr = null;
		lernabschnitt.BilingualerZweig = null;   // TODO aus alten Lernabschnitt übernehmen
		lernabschnitt.AOSF = null;   // TODO aus alten Lernabschnitt übernehmen
		lernabschnitt.Autist = null;   // TODO aus alten Lernabschnitt übernehmen
		lernabschnitt.ZieldifferentesLernen = null;   // TODO aus alten Lernabschnitt übernehmen
		return lernabschnitt;
	}


	/**
	 * Bestimmt den Schüler-Lernabschnitt mit dem angegebenen Schuljahresabschnitt und gibt diesen
	 * zurück. Ist keiner vorhanden, so wird null zurückgegeben.
	 * 
	 * @param conn                     die Datenbank-Verbindung
	 * @param idSchueler               die ID des Schülers
	 * @param idSchuljahresabschnitt   der ID des Schuljahresabschnitts 
	 * 
	 * @return der Schüler-Lernabschnitt oder null
	 */
	public static DTOSchuelerLernabschnittsdaten get(final DBEntityManager conn, final Long idSchueler, final Long idSchuljahresabschnitt) {
		if ((idSchueler == null) || (idSchuljahresabschnitt == null))
			return null;
		return conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr IS NULL",
			DTOSchuelerLernabschnittsdaten.class, idSchueler, idSchuljahresabschnitt).stream().findFirst().orElse(null);
	}


	/**
	 * Prüft, ob bei dem übergebenen Schüler ein Lernabschnitt mit dem übergebene Jahrgang in einem 
	 * anderen Schuljahr und gleichen Abschnitt in diesem Jahr existiert. (Wiederholter Abschnitt)   
	 * 
	 * @param conn                   die Datenbank-Verbindung
	 * @param schuljahresabschnitt   der Schuljahresabschnitt 
	 * @param idSchueler             die ID des Schülers
	 * @param idJahrgang             die ID des Jahrgangs
	 * 
	 * @return true, falls ein solcher Lernabschnitt existiert und ansonsten false
	 */
	public static boolean pruefeWiederholung(final DBEntityManager conn, final DTOSchuljahresabschnitte schuljahresabschnitt, final Long idSchueler, final Long idJahrgang) {
		if ((schuljahresabschnitt == null) || (idSchueler == null) || (idJahrgang == null))
			return false;
		List<Long> schuljahresabschnitte = conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.WechselNr IS NULL AND e.Jahrgang_ID = ?2 AND e.Schuljahresabschnitts_ID <> ?3", DTOSchuelerLernabschnittsdaten.class, idSchueler, idJahrgang, schuljahresabschnitt.ID)
				.stream().map(sla -> sla.Schuljahresabschnitts_ID).toList();
		if (schuljahresabschnitte.size() == 0)
			return false;
		return conn.queryNamed("DTOSchuljahresabschnitte.id.multiple", schuljahresabschnitte, DTOSchuljahresabschnitte.class).stream()
				.anyMatch(sja -> sja.Abschnitt == schuljahresabschnitt.Abschnitt && sja.Jahr != schuljahresabschnitt.Abschnitt);
	}


	/**
	 * Bestimmt den Schüler-Lernabschnitt mit dem angegebenen Schuljahresabschnitt. Ist dieser
	 * vorhanden, so wird er zurückgegeben.
	 * Ist er nicht vorhanden, so wird versucht einen neuen Schüler-Lernabschnitt mit dem 
	 * angegebenen Schuljahresabschnitt basierend auf dem Schüler-Lernabschnitt des vorigen 
	 * Schuljahresabschnittes zu erstellen. 
	 * Ist kein voriger Schuljahresabschnitt definiert oder kein entsprechender Schüler-Lernabschnitt
	 * vorhanden (z.B. bei Neuaufnahmen), so wird basierend auf den Daten des Schülers 
	 * - sofern möglich - ein Lernabschnitt angelegt. 
	 * 
	 * @param conn                   die Datenbank-Verbindung
	 * @param idSchueler             die ID des Schülers
	 * @param schuljahresabschnitt   der Schuljahresabschnitt 
	 * 
	 * @return der Schüler-Lernabschnitt
	 * 
	 * @throws WebApplicationException   falls ein Fehler beim Erstellen des Lernabschnitts auftritt
	 */
	public static DTOSchuelerLernabschnittsdaten transactionGetOrCreateByPrevious(final DBEntityManager conn, final long idSchueler, 
			final DTOSchuljahresabschnitte schuljahresabschnitt) throws WebApplicationException {
		try {
			conn.transactionBegin();
			// Prüfe, ob der Lernabschnitt bereits existiert
			DTOSchuelerLernabschnittsdaten sla = get(conn, idSchueler, schuljahresabschnitt.ID);
			if (sla != null)
				return sla;
			// Bestimme die ID, mit welche der Schüler-Lernabschnitt eingefügt wird
			DTODBAutoInkremente dbID = conn.queryByKey(DTODBAutoInkremente.class, Schema.tab_SchuelerLernabschnittsdaten.name());
			long idSLA = (dbID == null) ? 1 : dbID.MaxID + 1;
			// Prüfe, ob der vorige Lernabschnitt existiert
			DTOSchuelerLernabschnittsdaten slaPrev = get(conn, idSchueler, schuljahresabschnitt.VorigerAbschnitt_ID);
			if (slaPrev != null) {
				// Bestimme Klasse, Jahrgang und weiteres aus dem vorigen Schuljahresabschnitt
				boolean schuljahrNeu = (schuljahresabschnitt.Abschnitt == 1);
				DTOKlassen klassePrev;  // Bestimme die entsprechende Klasse im vorigen Lernabschnitt
				if (slaPrev.Folgeklasse_ID == null) { // Wenn die Folge-Klasse gesetzt ist, dann muss bei einem neuen Schuljahr dieses Feld genutzt werden... 
					klassePrev = DBUtilsKlassen.get(conn, slaPrev.Klassen_ID);
					if (schuljahrNeu)
						klassePrev = DBUtilsKlassen.getFolgeKlasse(conn, klassePrev);
				} else {
					klassePrev = DBUtilsKlassen.get(conn, schuljahrNeu ? slaPrev.Folgeklasse_ID : slaPrev.Klassen_ID); 
				}
				DTOKlassen klasse = DBUtilsKlassen.getKlasseInAbschnitt(conn, klassePrev, schuljahresabschnitt.ID);
				DTOJahrgang jahrgang = DBUtilsJahrgaenge.get(conn, klasse.Jahrgang_ID); 
				sla = createDefault(idSLA, idSchueler, schuljahresabschnitt, klasse, jahrgang);
				sla.Schulbesuchsjahre = (slaPrev.Schulbesuchsjahre == null) ? null : (schuljahrNeu ? slaPrev.Schulbesuchsjahre + 1 : slaPrev.Schulbesuchsjahre);
				sla.BilingualerZweig = slaPrev.BilingualerZweig;
				sla.Schwerbehinderung = slaPrev.Schwerbehinderung;
				sla.Foerderschwerpunkt_ID = slaPrev.Foerderschwerpunkt_ID;
				sla.Foerderschwerpunkt2_ID = slaPrev.Foerderschwerpunkt2_ID;
				sla.Sonderpaedagoge_ID = slaPrev.Sonderpaedagoge_ID;
				sla.AOSF = slaPrev.AOSF;
				sla.Autist = slaPrev.Autist;
				sla.ZieldifferentesLernen = slaPrev.ZieldifferentesLernen;
				sla.Folgeklasse_ID = null;
				sla.Wiederholung = pruefeWiederholung(conn, schuljahresabschnitt, idSchueler, sla.Jahrgang_ID);
				if (!conn.transactionPersist(sla))
		        	throw OperationError.INTERNAL_SERVER_ERROR.exception("Fehler beim Schreiben des Schüler-Lernabschnitts " + schuljahresabschnitt.Jahr + "." + schuljahresabschnitt.Abschnitt + " des Schülers " + idSchueler + " in die Datenbank");
		        if (!conn.transactionCommit())
		        	throw OperationError.INTERNAL_SERVER_ERROR.exception("Fehler beim Schreiben des Schüler-Lernabschnitts " + schuljahresabschnitt.Jahr + "." + schuljahresabschnitt.Abschnitt + " des Schülers " + idSchueler + " in die Datenbank");
		        return sla;
			}
        	throw OperationError.NOT_FOUND.exception("Fehler beim Erstellen des Schüler-Lernabschnitts " + schuljahresabschnitt.Jahr + "." + schuljahresabschnitt.Abschnitt + " des Schülers " + idSchueler + ". Es wurden keine ausreichenden Daten zu einem vorigen Schüler-Lernabschnitt gefunden.");
		} catch (Exception e) {
			if (!conn.transactionRollback())
	        	throw OperationError.INTERNAL_SERVER_ERROR.exception(e);
			throw e;
		}
	}

}
