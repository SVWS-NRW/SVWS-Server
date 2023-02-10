package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.util.List;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.schueler.SchuelerLernabschnittNachpruefung;
import de.nrw.schule.svws.core.data.schueler.SchuelerLernabschnittNachpruefungsdaten;
import de.nrw.schule.svws.core.data.schueler.SchuelerLernabschnittsdaten;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.nrw.schule.svws.db.utils.OperationError;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerLernabschnittsdaten}.
 */
public class DataSchuelerLernabschnittsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerLernabschnittsdaten}.
	 * 
	 * @param conn                   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLernabschnittsdaten(DBEntityManager conn) {
		super(conn);
	}
	
	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Bestimmt die Lernabschnittsdaten anhand der übergebenen Schüler-ID und dem
	 * angegebenen Schuljahresabschnitts 
	 * 
	 * @param schueler_id            die Schüler-ID
	 * @param schuljahresabschnitt   der Schuljahresabschnitt
	 * 
	 * @return die Lernabschnittsdaten
	 */
	public Response get(Long schueler_id, long schuljahresabschnitt) {
		// Prüfe, ob der Schüler mit der ID existiert
		if (schueler_id == null)
			return OperationError.NOT_FOUND.getResponse();
    	DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
    	if (schueler == null)
			return OperationError.NOT_FOUND.getResponse();
    	// Bestimme den aktuellen Lernabschnitt
    	String jpql = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 and e.Schuljahresabschnitts_ID = ?2 and e.WechselNr IS NULL";
    	List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = conn.queryList(jpql, DTOSchuelerLernabschnittsdaten.class, schueler_id, schuljahresabschnitt);
    	if ((lernabschnittsdaten == null) || lernabschnittsdaten.size() <= 0)
			return OperationError.NOT_FOUND.getResponse();
    	if (lernabschnittsdaten.size() > 1)
    		return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    	DTOSchuelerLernabschnittsdaten aktuell = lernabschnittsdaten.get(0);
		return this.get(aktuell.ID);
	}
	
	@Override
	public Response get(Long id) {
		// Prüfe, ob der Lernabschnitt mit der ID existiert
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();		
		DTOSchuelerLernabschnittsdaten aktuell = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, id);
    	if (aktuell == null)
			return OperationError.NOT_FOUND.getResponse();
    	// Ermittle die Fachbemerkungen
    	List<DTOSchuelerPSFachBemerkungen> bemerkungen = conn.queryNamed("DTOSchuelerPSFachBemerkungen.abschnitt_id", aktuell.ID, DTOSchuelerPSFachBemerkungen.class); 
    	if (bemerkungen == null)
    		return OperationError.NOT_FOUND.getResponse();
    	if (bemerkungen.size() > 1)
    		return OperationError.INTERNAL_SERVER_ERROR.getResponse();

    	SchuelerLernabschnittsdaten daten = new SchuelerLernabschnittsdaten();
    	daten.id = aktuell.ID;
    	daten.schuelerID = aktuell.Schueler_ID;
    	daten.schuljahresabschnitt = aktuell.Schuljahresabschnitts_ID;
    	daten.wechselNr = aktuell.WechselNr;
    	daten.datumAnfang = aktuell.DatumVon;
    	daten.datumEnde = aktuell.DatumBis;
    	daten.datumKonferenz = aktuell.Konferenzdatum;
    	daten.datumZeugnis = aktuell.ZeugnisDatum;
    	daten.anzahlSchulbesuchsjahre = aktuell.Schulbesuchsjahre;
    	daten.istGewertet = aktuell.SemesterWertung == null ? true : aktuell.SemesterWertung;
    	daten.istWiederholung = aktuell.Wiederholung == null ? false : aktuell.Wiederholung;
    	daten.pruefungsOrdnung = aktuell.PruefOrdnung;
    	daten.tutorID = aktuell.Tutor_ID;
    	daten.klassenID = aktuell.Klassen_ID;
    	daten.folgeklassenID = aktuell.Folgeklasse_ID;
    	daten.schulgliederung = aktuell.Schulgliederung.daten.kuerzel; 
    	daten.jahrgangID = aktuell.Jahrgang_ID;
    	daten.fachklasseID = aktuell.Fachklasse_ID;
    	daten.schwerpunktID = aktuell.Schwerpunkt_ID;
    	daten.organisationsform = aktuell.OrgFormKrz;
    	daten.Klassenart = aktuell.Klassenart;
    	daten.fehlstundenGesamt = aktuell.SumFehlStd == null ? 0 : aktuell.SumFehlStd;
    	daten.fehlstundenUnentschuldigt = aktuell.SumFehlStdU == null ? 0 : aktuell.SumFehlStdU;
    	daten.fehlstundenGrenzwert = aktuell.FehlstundenGrenzwert;
    	daten.hatSchwerbehinderungsNachweis = aktuell.Schwerbehinderung == null ? false : aktuell.Schwerbehinderung;
    	daten.hatAOSF = aktuell.AOSF == null ? false : aktuell.AOSF;
    	daten.hatAutismus = aktuell.Autist == null ? false : aktuell.Autist;
    	daten.hatZieldifferentenUnterricht = aktuell.ZieldifferentesLernen == null ? false : aktuell.ZieldifferentesLernen;
    	daten.foerderschwerpunkt1ID = aktuell.Foerderschwerpunkt_ID;
    	daten.foerderschwerpunkt2ID = aktuell.Foerderschwerpunkt2_ID;
    	daten.sonderpaedagogeID = aktuell.Sonderpaedagoge_ID;
    	daten.bilingualerZweig = aktuell.BilingualerZweig;
    	daten.istFachpraktischerAnteilAusreichend = aktuell.FachPraktAnteilAusr;
    	daten.versetzungsvermerk = aktuell.VersetzungKrz;
    	daten.noteDurchschnitt = aktuell.DSNote;
    	daten.noteLernbereichGSbzwAL = aktuell.Gesamtnote_GS == null ? null : aktuell.Gesamtnote_GS.getNoteSekI();
    	daten.noteLernbereichNW = aktuell.Gesamtnote_NW == null ? null : aktuell.Gesamtnote_NW.getNoteSekI();
    	daten.abschlussart = aktuell.AbschlussArt;
    	daten.istAbschlussPrognose = aktuell.AbschlIstPrognose;
    	daten.abschluss = aktuell.Abschluss;
    	daten.abschlussBerufsbildend = aktuell.Abschluss_B;
    	daten.textErgebnisPruefungsalgorithmus = aktuell.PruefAlgoErgebnis;
    	daten.zeugnisart = aktuell.Zeugnisart;
    	if (aktuell.MoeglNPFaecher != null) {
    		String[] moeglicheNPFaecher = aktuell.MoeglNPFaecher.split(",");
    		if ((moeglicheNPFaecher.length > 0) && (!"".equals(moeglicheNPFaecher[0].trim()))) {
    			daten.nachpruefungen = new SchuelerLernabschnittNachpruefungsdaten();
    			for (String fach : moeglicheNPFaecher)
    				daten.nachpruefungen.moegliche.add(fach);
    			if (aktuell.NPV_Fach_ID != null) {
    				SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
    				np.grund = "V";
    				np.fachID = aktuell.NPV_Fach_ID;
    				np.datum = aktuell.NPV_Datum;
    				np.note = aktuell.NPV_NoteKrz;
    				daten.nachpruefungen.pruefungen.add(np);
    			}
    			if (aktuell.NPAA_Fach_ID != null) {
    				SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
    				np.grund = "A";
    				np.fachID = aktuell.NPAA_Fach_ID;
    				np.datum = aktuell.NPAA_Datum;
    				np.note = aktuell.NPAA_NoteKrz;
    				daten.nachpruefungen.pruefungen.add(np);
    			}
    			if (aktuell.NPBQ_Fach_ID != null) {
    				SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
    				np.grund = "B";
    				np.fachID = aktuell.NPBQ_Fach_ID;
    				np.datum = aktuell.NPBQ_Datum;
    				np.note = aktuell.NPBQ_NoteKrz;
    				daten.nachpruefungen.pruefungen.add(np);
    			}
    		}
    	}
    	daten.bemerkungen.zeugnisAllgemein = aktuell.ZeugnisBem; 
    	if (bemerkungen.size() > 0) {
    		DTOSchuelerPSFachBemerkungen b = bemerkungen.get(0);
	    	daten.bemerkungen.zeugnisASV = b.ASV;
	    	daten.bemerkungen.zeugnisLELS = b.LELS;
	    	daten.bemerkungen.zeugnisAUE = b.AUE;
	    	daten.bemerkungen.uebergangESF = b.ESF;
	    	daten.bemerkungen.foerderschwerpunkt = b.BemerkungFSP;
	    	daten.bemerkungen.versetzungsentscheidung = b.BemerkungVersetzung;
    	}

    	if (!(new DataSchuelerLeistungsdaten(conn).getByLernabschnitt(aktuell.ID, daten.leistungsdaten)))
    		return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    	
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

}
