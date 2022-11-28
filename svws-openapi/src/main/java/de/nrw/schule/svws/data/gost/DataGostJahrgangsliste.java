package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.gost.GostJahrgang;
import de.nrw.schule.svws.core.types.jahrgang.Jahrgaenge;
import de.nrw.schule.svws.core.utils.gost.GostAbiturjahrUtils;
import de.nrw.schule.svws.core.utils.jahrgang.JahrgangsUtils;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostJahrgang}.
 */
public class DataGostJahrgangsliste extends DataManager<Integer> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostJahrgang}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostJahrgangsliste(DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		DTOEigeneSchule schule = GostUtils.pruefeSchuleMitGOSt(conn);

    	// Bestimme den aktuellen Schuljahresabschnitt der Schule
		DTOSchuljahresabschnitte aktuellerAbschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (aktuellerAbschnitt == null)
    		return OperationError.NOT_FOUND.getResponse();
		
		// Bestimme die Jahrgaenge der Schule
		List<DTOJahrgang> dtosJahrgaenge = conn.queryAll(DTOJahrgang.class);
		if ((dtosJahrgaenge == null) || (dtosJahrgaenge.size() <= 0))
    		return OperationError.NOT_FOUND.getResponse();
    	
    	// Lese alle Abiturjahrgänge aus der Datenbank ein und ergänze diese im Vektor
    	Vector<GostJahrgang> daten = new Vector<>();
		List<DTOGostJahrgangsdaten> dtos = conn.queryAll(DTOGostJahrgangsdaten.class);
		if (dtos != null) {
			dtos = dtos.stream().sorted((a,b) -> Integer.compare(a.Abi_Jahrgang, b.Abi_Jahrgang)).collect(Collectors.toList());
			for (DTOGostJahrgangsdaten jahrgangsdaten : dtos) {
				if (jahrgangsdaten.Abi_Jahrgang < 0)
					continue;
				GostJahrgang eintrag = new GostJahrgang();
				eintrag.abiturjahr = jahrgangsdaten.Abi_Jahrgang;
				int restjahre = jahrgangsdaten.Abi_Jahrgang - aktuellerAbschnitt.Jahr;
				for (DTOJahrgang jahrgang : dtosJahrgaenge) {
					Integer jahrgangRestjahre = JahrgangsUtils.getRestlicheJahre(schule.Schulform, jahrgang.Gliederung, jahrgang.ASDJahrgang);
					if (jahrgangRestjahre != null && restjahre == jahrgangRestjahre) {
						eintrag.jahrgang = jahrgang.ASDJahrgang;
						if (Jahrgaenge.JG_EF.daten.kuerzel.equals(jahrgang.ASDJahrgang) 
								|| Jahrgaenge.JG_Q1.daten.kuerzel.equals(jahrgang.ASDJahrgang)
								|| Jahrgaenge.JG_Q2.daten.kuerzel.equals(jahrgang.ASDJahrgang))
							break;
					}
				}
				eintrag.bezeichnung = "Abi " + eintrag.abiturjahr + ((eintrag.jahrgang == null) ? "" : " (" + eintrag.jahrgang + ")");
				eintrag.istAbgeschlossen = (restjahre < 1);
				daten.add(eintrag);
			}
		}
		
    	// Füge einen Eintrag ein, der für keinen Abiturjahrgang steht - also für allgemeine Informationen zu Abiturjahrgängen
    	GostJahrgang eintrag = new GostJahrgang();
    	eintrag.abiturjahr = -1;
    	eintrag.jahrgang = null;
    	eintrag.bezeichnung = "Allgemein / Vorlage";
    	eintrag.istAbgeschlossen = false;
    	daten.add(0, eintrag);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(Integer id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Integer id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Erstellt einen neuen Abiturjahrgang für die gymnasiale Oberstufe.
	 * Dabei werden die Daten des Jahrgangs mit der übergebenen ID als Grundlage 
	 * verwendet.
	 * 
	 * @param jahrgang_id   die ID des Jahrgangs 
	 * 
	 * @return die HTTP-Response, im Erfolgsfall mit dem Abiturjahrgang
	 */
	public Response create(long jahrgang_id) {
		// Prüfe die Schuldaten
		DTOEigeneSchule schule = GostUtils.pruefeSchuleMitGOSt(conn);
		DTOSchuljahresabschnitte aktuellerAbschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (aktuellerAbschnitt == null)
    		return OperationError.NOT_FOUND.getResponse();
    	
    	// Ermittle die Jahrgangsdaten und bestimme das Abiturjahr
    	DTOJahrgang jahrgang = conn.queryByKey(DTOJahrgang.class, jahrgang_id);
		if (jahrgang == null)
    		return OperationError.NOT_FOUND.getResponse();
    	Integer abiturjahr = GostAbiturjahrUtils.getGostAbiturjahr(schule.Schulform, jahrgang.Gliederung, aktuellerAbschnitt.Jahr, jahrgang.ASDJahrgang);
		if (abiturjahr == null)
    		return OperationError.NOT_FOUND.getResponse();
		
		// Prüfe, ob der Abiturjahrgang bereits angelegt wurde
		DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr);
		if (jahrgangsdaten != null)
			return OperationError.CONFLICT.getResponse();
		
		// Lade die Vorlage für den neuen Abiturjahrgang
		DTOGostJahrgangsdaten jahrgangsdatenVorlage = DataGostJahrgangsdaten.getVorlage(conn);
		
		// Erstelle die Jahrgangsdaten mit Default-Werten, Beratungslehrer sind zunächst nicht zugeordnet
		jahrgangsdaten = new DTOGostJahrgangsdaten(abiturjahr);
		jahrgangsdaten.ZusatzkursGEErstesHalbjahr = jahrgangsdatenVorlage.ZusatzkursGEErstesHalbjahr;
		jahrgangsdaten.ZusatzkursGEVorhanden = jahrgangsdatenVorlage.ZusatzkursGEVorhanden;
		jahrgangsdaten.ZusatzkursSWErstesHalbjahr = jahrgangsdatenVorlage.ZusatzkursSWErstesHalbjahr;
		jahrgangsdaten.ZusatzkursSWVorhanden = jahrgangsdatenVorlage.ZusatzkursSWVorhanden;
		jahrgangsdaten.TextBeratungsbogen = jahrgangsdatenVorlage.TextBeratungsbogen;
		jahrgangsdaten.TextMailversand = jahrgangsdatenVorlage.TextMailversand;
		if (!conn.persist(jahrgangsdaten))
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		// Kopiere die Fächer der Gymnasialen Oberstufe aus der allgemeinen Vorlage
		List<DTOFach> faecher = conn.queryNamed("DTOFach.istoberstufenfach", true, DTOFach.class);
		if (faecher == null)
			throw new NullPointerException();
		Vector<DTOGostJahrgangFaecher> gostFaecher = new Vector<>();
		for (DTOFach fach : faecher) {
			if ((fach.Sichtbar == null) || (!fach.Sichtbar))
				continue;
			DTOGostJahrgangFaecher gostFach = new DTOGostJahrgangFaecher(abiturjahr, fach.ID, 
					fach.IstMoeglichEF1, fach.IstMoeglichEF2, fach.IstMoeglichQ11, fach.IstMoeglichQ12, 
					fach.IstMoeglichQ21, fach.IstMoeglichQ22,fach.IstMoeglichAbiGK, fach.IstMoeglichAbiLK);
			gostFach.WochenstundenQPhase = fach.WochenstundenQualifikationsphase;
			gostFaecher.add(gostFach);
		}
		if ((gostFaecher.size() > 0) && (!conn.persistAll(gostFaecher)))
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		// Kopiere die Informationen zu nicht möglichen und geforderten Fachkombinationen aus der Vorlage
		List<DTOGostJahrgangFachkombinationen> faecherKombis = 
				conn.queryNamed("DTOGostJahrgangFachkombinationen.abi_jahrgang", null, DTOGostJahrgangFachkombinationen.class);
		if (faecherKombis == null)
			throw new NullPointerException();
		if (faecherKombis.size() > 0) { 
			Vector<DTOGostJahrgangFachkombinationen> gostFaecherKombis = new Vector<>();
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			DTODBAutoInkremente dbNmkID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Jahrgang_Fachkombinationen");
			long idNMK = dbNmkID == null ? 1 : dbNmkID.MaxID + 1;
			for (DTOGostJahrgangFachkombinationen kombi : faecherKombis) {
				DTOGostJahrgangFachkombinationen k = new DTOGostJahrgangFachkombinationen(idNMK++, 
						kombi.Fach1_ID, kombi.Fach2_ID, kombi.EF1, kombi.EF2, kombi.Q11, kombi.Q12, kombi.Q21, kombi.Q22,  
						kombi.Typ, kombi.Hinweistext);
				k.Abi_Jahrgang = kombi.Abi_Jahrgang;
				k.Kursart1 = kombi.Kursart1;
				k.Kursart2 = kombi.Kursart2;
				gostFaecherKombis.add(k);
			}
			if (!conn.persistAll(gostFaecherKombis))
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(abiturjahr).build();
	}
	
}
