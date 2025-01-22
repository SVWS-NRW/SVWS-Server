package de.svws_nrw.data.schueler;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.data.jahrgaenge.DBUtilsJahrgaenge;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse beinhaltet wiederverwendbare Hilfsmethoden
 * zu Schüler-Lernabschnittsdaten in Bezug auf den Datenbank-Zugriff.
 */
public final class DBUtilsSchuelerLernabschnittsdaten {

	private DBUtilsSchuelerLernabschnittsdaten() {
		throw new IllegalStateException("Instantiation of " + DBUtilsSchuelerLernabschnittsdaten.class.getName() + " not allowed");
	}

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
	private static DTOSchuelerLernabschnittsdaten createDefault(final long idNew, final long idSchueler, final Schuljahresabschnitt schuljahresabschnitt,
			final DTOKlassen klasse, final DTOJahrgang jahrgang) {
		final DTOSchuelerLernabschnittsdaten lernabschnitt = new DTOSchuelerLernabschnittsdaten(idNew, idSchueler, schuljahresabschnitt.id, false, false);
		lernabschnitt.WechselNr = 0;
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
		lernabschnitt.Schulgliederung = jahrgang.GliederungKuerzel;
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
		return conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0",
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
	public static boolean pruefeWiederholung(final DBEntityManager conn, final Schuljahresabschnitt schuljahresabschnitt, final Long idSchueler,
			final Long idJahrgang) {
		if ((schuljahresabschnitt == null) || (idSchueler == null) || (idJahrgang == null))
			return false;
		final List<Long> schuljahresabschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.WechselNr = 0 AND e.Jahrgang_ID = ?2 AND e.Schuljahresabschnitts_ID <> ?3",
				DTOSchuelerLernabschnittsdaten.class, idSchueler, idJahrgang, schuljahresabschnitt.id)
				.stream().map(sla -> sla.Schuljahresabschnitts_ID).toList();
		if (schuljahresabschnitte.isEmpty())
			return false;
		return conn.queryByKeyList(DTOSchuljahresabschnitte.class, schuljahresabschnitte).stream()
				.anyMatch(sja -> Objects.equals(sja.Abschnitt, schuljahresabschnitt.abschnitt) && !Objects.equals(sja.Jahr, schuljahresabschnitt.abschnitt));
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
	 * @param idSLA                  die ID des anzulegenden Lernabschnitts
	 * @param conn                   die Datenbank-Verbindung mit einer aktiven Transaktion
	 * @param idSchueler             die ID des Schülers
	 * @param schuljahresabschnitt   der Schuljahresabschnitt
	 *
	 * @return der Schüler-Lernabschnitt
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Erstellen des Lernabschnitts auftritt
	 */
	public static DTOSchuelerLernabschnittsdaten createByPrevious(final long idSLA, final DBEntityManager conn, final long idSchueler,
			final Schuljahresabschnitt schuljahresabschnitt) throws ApiOperationException {
		// Prüfe, ob der vorige Lernabschnitt existiert
		final DataKlassendaten dataKlassendaten = new DataKlassendaten(conn);
		final DTOSchuelerLernabschnittsdaten slaPrev = get(conn, idSchueler, schuljahresabschnitt.idVorigerAbschnitt);
		if (slaPrev != null) {
			// Bestimme Klasse, Jahrgang und weiteres aus dem vorigen Schuljahresabschnitt
			final boolean schuljahrNeu = (schuljahresabschnitt.abschnitt == 1);
			DTOKlassen klassePrev;  // Bestimme die entsprechende Klasse im vorigen Lernabschnitt
			if (slaPrev.Folgeklasse_ID == null) { // Wenn die Folge-Klasse gesetzt ist, dann muss bei einem neuen Schuljahr dieses Feld genutzt werden...
				klassePrev = dataKlassendaten.getDTO(slaPrev.Klassen_ID);
				if (schuljahrNeu)
					klassePrev = dataKlassendaten.getDTOByKuerzelOrASDKuerzelAndHalbjahresabschnittId(klassePrev.FKlasse, null,
							klassePrev.Schuljahresabschnitts_ID);
			} else {
				klassePrev = dataKlassendaten.getDTO(schuljahrNeu ? slaPrev.Folgeklasse_ID : slaPrev.Klassen_ID);
			}
			final DTOKlassen klasse = dataKlassendaten.getDTOByKuerzelOrASDKuerzelAndHalbjahresabschnittId(klassePrev.Klasse, klassePrev.ASDKlasse,
					schuljahresabschnitt.id);
			final DTOJahrgang jahrgang = DBUtilsJahrgaenge.get(conn, klasse.Jahrgang_ID);
			final DTOSchuelerLernabschnittsdaten sla = createDefault(idSLA, idSchueler, schuljahresabschnitt, klasse, jahrgang);
			if (slaPrev.Schulbesuchsjahre != null)
				sla.Schulbesuchsjahre = schuljahrNeu ? (slaPrev.Schulbesuchsjahre + 1) : slaPrev.Schulbesuchsjahre;
			sla.Hochrechnung = slaPrev.Hochrechnung;
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
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Fehler beim Schreiben des Schüler-Lernabschnitts %d.%d des Schülers %d in die Datenbank".formatted(schuljahresabschnitt.schuljahr,
								schuljahresabschnitt.abschnitt, idSchueler));
			conn.transactionFlush();
			return sla;
		}
		throw new ApiOperationException(Status.NOT_FOUND, ("Fehler beim Erstellen des Schüler-Lernabschnitts %d.%d des Schülers %d. Es wurden keine"
				+ " ausreichenden Daten zu einem vorigen Schüler-Lernabschnitt gefunden.")
				.formatted(schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt, idSchueler));
	}

}
