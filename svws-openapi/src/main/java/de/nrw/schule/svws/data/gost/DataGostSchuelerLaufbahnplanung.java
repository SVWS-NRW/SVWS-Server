package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.Abiturdaten;
import de.nrw.schule.svws.core.data.gost.GostSchuelerFachwahl;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostSchueler;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;
import de.nrw.schule.svws.db.utils.data.Schule;
import de.nrw.schule.svws.db.utils.gost.FaecherGost;
import de.nrw.schule.svws.db.utils.gost.GostSchuelerLaufbahn;
import de.nrw.schule.svws.module.pdf.gost.PDFGostWahlbogen;
import jakarta.persistence.TypedQuery;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Abiturdaten}.
 */
public class DataGostSchuelerLaufbahnplanung extends DataManager<Long> {
	
	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Abiturdaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostSchuelerLaufbahnplanung(DBEntityManager conn) {
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

	@Override
	public Response get(Long schueler_id) {
		if (schueler_id == null)
	    	return OperationError.NOT_FOUND.getResponse();
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
    		return OperationError.NOT_FOUND.getResponse();
    	Schulform schulform = schule.Schulform;
    	if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
    		return OperationError.NOT_FOUND.getResponse();
    	Abiturdaten daten = GostSchuelerLaufbahn.get(conn, schueler_id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long schueler_id, InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt die Fachwahl für die gymnasiale Oberstufe zu einem Fach von dem angegebenen Schüler.
	 * 
	 * @param schueler_id   die ID des Schülers
	 * @param fach_id       die ID des Faches
	 * 
	 * @return Die HTTP-Response der Get-Operation
	 */
	public Response getFachwahl(Long schueler_id, Long fach_id) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
    		return OperationError.NOT_FOUND.getResponse();
    	Schulform schulform = schule.Schulform;
    	if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
    		return OperationError.NOT_FOUND.getResponse();
    	DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
    	if (schueler == null)
    		return OperationError.NOT_FOUND.getResponse();
    	DTOFach fach = conn.queryByKey(DTOFach.class, fach_id);
    	if ((fach == null) || (fach.IstOberstufenFach == null) || !fach.IstOberstufenFach)
    		return OperationError.NOT_FOUND.getResponse();
    	DTOGostSchuelerFachbelegungen fachbelegung = conn.queryByKey(DTOGostSchuelerFachbelegungen.class, schueler.ID, fach.ID);
    	if (fachbelegung == null)
    		return OperationError.NOT_FOUND.getResponse();
    	GostSchuelerFachwahl fachwahl = new GostSchuelerFachwahl();
    	fachwahl.EF1 = fachbelegung.EF1_Kursart;
    	fachwahl.EF2 = fachbelegung.EF2_Kursart;
    	fachwahl.Q11 = fachbelegung.Q11_Kursart;
    	fachwahl.Q12 = fachbelegung.Q12_Kursart;
    	fachwahl.Q21 = fachbelegung.Q21_Kursart;
    	fachwahl.Q22 = fachbelegung.Q22_Kursart;
    	fachwahl.abiturFach = fachbelegung.AbiturFach;
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(fachwahl).build();
	}
	
	

	/**
	 * Führt den Fachwahl-Patch für das angegebene Halbjahr aus, sofern dieser gültig ist und in dem 
	 * angegebenen Halbjahr erlaubt ist. Ein Patch ist nicht erlaubt, wenn dieser in das aktuelle
	 * Halbjahr oder ein Halbjahr davor fällt, da hier bereits eine Kursblockung stattgefunden hat
	 * und Anpassungen über die Kurswahlen bzw. die Leistungsdaten erfolgen sollten.
	 *  
	 * @param fwDB          der Wert für die Fachwahl aus der DB
	 * @param halbjahr      das Halbjahr, auf welches sich der Patch bezieht
	 * @param aktHalbjahr   das Halbjahr, in welchem sich der Schüler befindet
	 * @param fach          das Fach, für welches die Fachwahl angepasst werden soll
	 * @param value         der Wert für die Fachwahl
	 * 
	 * @return der zu übertragende Wert
	 * 
	 * @throws WebApplicationException (CONFLICT) falls die Fachwahl ungültig ist
	 */
	private static String patchFachwahlHalbjahr(String fwDB, GostHalbjahr halbjahr, GostHalbjahr aktHalbjahr, DTOFach fach, Object value) throws WebApplicationException {
		String fw = JSONMapper.convertToString(value, true, false);
		if (((fw == null) && (fwDB == null)) || ((fw != null) && (fw.equals(fwDB))))
			return fwDB;
		// prüfe, ob eine Änderung bei diesem Schüler überhaupt erlaubt ist oder in das aktuelle Halbjahr des Schülers oder früher fällt...
		if ((aktHalbjahr != null) && (aktHalbjahr.compareTo(halbjahr) >= 0))
			throw OperationError.CONFLICT.exception();
		boolean valid = (fw == null)
				|| (fw.equals("M")) || (fw.equals("S")) 
				|| (((fw.equals("LK")) || (fw.equals("ZK"))) && (!halbjahr.istEinfuehrungsphase()))
				|| ((fw.equals("AT")) && ("SP".equals(fach.StatistikFach.daten.kuerzelASD)));
		if (!valid)
			throw OperationError.CONFLICT.exception();
		return fw;
	}
	
	
	/**
	 * Passt die Fachwahl für die gymnasiale Oberstufe zu einem Fach von dem angegebenen Schüler an. 
	 * 
	 * @param schueler_id   die ID des Schülers
	 * @param fach_id       die ID des Faches
	 * @param is            der {@link InputStream} mit dem JSON-Patch für die Fachwahl
	 * 
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public Response patchFachwahl(Long schueler_id, Long fach_id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
    		try {
    			conn.transactionBegin();
    	    	Schule schule = Schule.queryCached(conn);
    	    	if (schule == null)
    	    		throw OperationError.NOT_FOUND.exception();
    	    	Schulform schulform = schule.getSchulform();
    	    	if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
    	    		throw OperationError.NOT_FOUND.exception();
    	    	DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
    	    	if (schueler == null)
    	    		throw OperationError.NOT_FOUND.exception();
    	    	// Ermittle den aktuellen Schuljahresaschnitt und den dazugehörigen Schüler-Lernabschnitt 
    	    	DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schueler.Schuljahresabschnitts_ID);
    	    	if (abschnitt == null)
    	    		throw OperationError.NOT_FOUND.exception();
    	    	TypedQuery<DTOSchuelerLernabschnittsdaten> queryAktAbschnitt = conn.query("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = :schueler_id AND e.Schuljahresabschnitts_ID = :abschnitt_id", DTOSchuelerLernabschnittsdaten.class);
    	    	DTOSchuelerLernabschnittsdaten lernabschnitt = queryAktAbschnitt
    	    			.setParameter("schueler_id", schueler.ID)
    	    			.setParameter("abschnitt_id", schueler.Schuljahresabschnitts_ID)
    	    			.getResultList().stream().findFirst().orElse(null);
    	    	if (lernabschnitt == null)
    	    		throw OperationError.NOT_FOUND.exception();
    			GostHalbjahr aktHalbjahr = schule.istImQuartalsmodus()  
    					? GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, (abschnitt.Abschnitt + 1) / 2)
    					: GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, abschnitt.Abschnitt);
    	    	// Bestimme das Fach und die Fachbelegungen in der DB. Liegen keine vor, so erstelle eine neue Fachnbelegung in der DB,um den Patch zu speichern 
    	    	DTOFach fach = conn.queryByKey(DTOFach.class, fach_id);
    	    	if ((fach == null) || (fach.IstOberstufenFach == null) || !fach.IstOberstufenFach)
    	    		throw OperationError.NOT_FOUND.exception();
    	    	DTOGostSchuelerFachbelegungen fachbelegung = conn.queryByKey(DTOGostSchuelerFachbelegungen.class, schueler.ID, fach.ID);
    	    	if (fachbelegung == null)
    	    		fachbelegung = new DTOGostSchuelerFachbelegungen(schueler.ID, fach.ID);
		    	for (Entry<String, Object> entry : map.entrySet()) {
		    		String key = entry.getKey();
		    		Object value = entry.getValue();
		    		switch (key) {
		    			case "EF1" -> fachbelegung.EF1_Kursart = patchFachwahlHalbjahr(fachbelegung.EF1_Kursart, GostHalbjahr.EF1, aktHalbjahr, fach, value);
		    			case "EF2" -> fachbelegung.EF2_Kursart = patchFachwahlHalbjahr(fachbelegung.EF2_Kursart, GostHalbjahr.EF2, aktHalbjahr, fach, value);
		    			case "Q11" -> fachbelegung.Q11_Kursart = patchFachwahlHalbjahr(fachbelegung.Q11_Kursart, GostHalbjahr.Q11, aktHalbjahr, fach, value);
		    			case "Q12" -> fachbelegung.Q12_Kursart = patchFachwahlHalbjahr(fachbelegung.Q12_Kursart, GostHalbjahr.Q12, aktHalbjahr, fach, value);
		    			case "Q21" -> fachbelegung.Q21_Kursart = patchFachwahlHalbjahr(fachbelegung.Q21_Kursart, GostHalbjahr.Q21, aktHalbjahr, fach, value);
		    			case "Q22" -> fachbelegung.Q22_Kursart = patchFachwahlHalbjahr(fachbelegung.Q22_Kursart, GostHalbjahr.Q22, aktHalbjahr, fach, value);
		    			case "abiturFach" -> {
		    				Integer af = JSONMapper.convertToInteger(value, true);
		    		    	if ((af != null) && ((af < 1) || (af > 4)))
		    		    		throw OperationError.CONFLICT.exception();
		    		    	fachbelegung.AbiturFach = af;			    				
		    			}
		    			default -> throw OperationError.BAD_REQUEST.exception();
		    		}
		    	}
		    	conn.transactionPersist(fachbelegung);
		    	conn.transactionCommit();
    		} catch (Exception e) {
    			if (e instanceof WebApplicationException webAppException)
    				return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    		} finally {
    			// Perform a rollback if necessary
    			conn.transactionRollback();
    		}
    	}
    	return Response.status(Status.OK).build();
	}
	
	
	/**
	 * Erstellt das PDF-Dokument für den Wahlbogen zu der Laufbahn 
	 * eines Schülers der gymnasialen Oberstufe.
	 * 
	 * @param schueler_id   die ID des Schülers 
	 * 
	 * @return die HTTP-Response mit dem PDF-Dokument
	 */
	public Response getPDFWahlbogen(Long schueler_id) {
		// Lese die Laufbahndaten aus der DB
		if (schueler_id == null)
	    	return OperationError.NOT_FOUND.getResponse();
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
    		return OperationError.NOT_FOUND.getResponse();
    	Schulform schulform = schule.Schulform;
    	if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
    		return OperationError.NOT_FOUND.getResponse();
    	DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
		if (schueler == null)
    		return OperationError.NOT_FOUND.getResponse();
		DTOGostSchueler gostSchueler = conn.queryByKey(DTOGostSchueler.class, schueler_id);
		if (gostSchueler == null)
    		return OperationError.NOT_FOUND.getResponse();
		DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schueler.Schuljahresabschnitts_ID);
		if (abschnitt == null)
    		return OperationError.NOT_FOUND.getResponse();
    	TypedQuery<DTOSchuelerLernabschnittsdaten> queryAktAbschnitt = conn.query("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = :schueler_id AND e.Schuljahresabschnitts_ID = :abschnitt_id", DTOSchuelerLernabschnittsdaten.class);
    	DTOSchuelerLernabschnittsdaten aktAbschnitt = queryAktAbschnitt
    			.setParameter("schueler_id", schueler_id)
    			.setParameter("abschnitt_id", abschnitt.ID)
    			.getResultList().stream().findFirst().orElse(null);
    	if (aktAbschnitt == null)
    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
    	DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, aktAbschnitt.Klassen_ID);
		if (klasse == null)
    		return OperationError.NOT_FOUND.getResponse();
    	Abiturdaten daten = GostSchuelerLaufbahn.get(conn, schueler_id);
		if (daten == null)
    		return OperationError.NOT_FOUND.getResponse();
    	DTOGostJahrgangsdaten jahrgangsDaten = conn.queryByKey(DTOGostJahrgangsdaten.class, daten.abiturjahr);
		if (jahrgangsDaten == null)
    		return OperationError.NOT_FOUND.getResponse();
    	// TODO Bei Schulen mit Quartalen fehlt die Bestimmung des Halbjahres anstatt abschnitt.Abschnitt...
    	GostHalbjahr halbjahr = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(daten.abiturjahr, abschnitt.Jahr, abschnitt.Abschnitt);
    	GostHalbjahr planungsHalbjahr = GostHalbjahr.getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(daten.abiturjahr, abschnitt.Jahr, abschnitt.Abschnitt);
    	if (planungsHalbjahr == null)
    		planungsHalbjahr = (halbjahr == null) ? GostHalbjahr.EF1 : GostHalbjahr.Q22;
    	GostFaecherManager gostFaecher = FaecherGost.getFaecherListeGost(conn, daten.abiturjahr);
    	if (gostFaecher.isEmpty())
    		return OperationError.NOT_FOUND.getResponse();
    	PDFGostWahlbogen wahlbogen = new PDFGostWahlbogen(
    		schueler.Vorname + " " + schueler.Nachname,
    		schueler.Geschlecht,
    		klasse.Klasse,
    		new String[] { schule.Bezeichnung1, schule.Bezeichnung2, schule.Bezeichnung3 }, 
    		daten,
    		gostFaecher,
    		planungsHalbjahr,
    		jahrgangsDaten.TextBeratungsbogen,
    		gostSchueler.DatumBeratung
    	);
		byte[] data = wahlbogen.toByteArray();
		if (data == null)
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		return Response.status(Status.OK).type("application/pdf")
			.header("Content-Disposition", "attachment; filename=Laufbahnplanung_" + schueler.Nachname.replace(' ', '_') + "_" + schueler.Vorname.replace(' ', '_') + ".pdf")  // TODO ergänze Informationen zum Dateinamen, z.B. Schülername oder ID
			.entity(data).build();
	}

}
