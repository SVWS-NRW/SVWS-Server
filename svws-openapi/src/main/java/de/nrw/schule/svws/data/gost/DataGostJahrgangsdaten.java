package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.GostJahrgangsdaten;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.core.utils.jahrgang.JahrgangsUtils;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostJahrgangsdaten}.
 */
public class DataGostJahrgangsdaten extends DataManager<Integer> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostJahrgangsdaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostJahrgangsdaten(DBEntityManager conn) {
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
	public Response get(Integer abi_jahrgang) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
    		return OperationError.NOT_FOUND.getResponse();
    	Schulform schulform = schule.Schulform;
    	if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
    		return OperationError.NOT_FOUND.getResponse();

    	// Bestimme den aktuellen Schuljahresabschnitt der Schule
		DTOSchuljahresabschnitte aktuellerAbschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (aktuellerAbschnitt == null)
    		return OperationError.NOT_FOUND.getResponse();
		
		// Bestimme die Jahrgaenge der Schule
		List<DTOJahrgang> dtosJahrgaenge = conn.queryAll(DTOJahrgang.class);
		if ((dtosJahrgaenge == null) || (dtosJahrgaenge.size() <= 0))
    		return OperationError.NOT_FOUND.getResponse();
    	
    	// Lese alle Abiturjahrgänge aus der Datenbank ein und ergänze diese im Vektor
		DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, abi_jahrgang);
		if (jahrgangsdaten == null)
    		return OperationError.NOT_FOUND.getResponse();
		
		GostJahrgangsdaten daten = new GostJahrgangsdaten();
		daten.abiturjahr = jahrgangsdaten.Abi_Jahrgang;
		int restjahre = jahrgangsdaten.Abi_Jahrgang - aktuellerAbschnitt.Jahr;
		for (DTOJahrgang jahrgang : dtosJahrgaenge) {
			Integer jahrgangRestjahre = JahrgangsUtils.getRestlicheJahre(schulform, jahrgang.Gliederung, jahrgang.ASDJahrgang);
			if (jahrgangRestjahre != null && restjahre == jahrgangRestjahre) {
				daten.jahrgang = jahrgang.ASDJahrgang;
				break;
			}
		}
		daten.bezeichnung = "Abi " + daten.abiturjahr + ((daten.jahrgang == null) ? "" : " (" + daten.jahrgang + ")");
		daten.istAbgeschlossen = (restjahre < 1);
    	daten.textBeratungsbogen = jahrgangsdaten.TextBeratungsbogen; 
    	daten.textMailversand = jahrgangsdaten.TextMailversand;
		// TODO Verfügbarkeit von Zusatzkurse - siehe DTOGostJahrgangsdaten
    	daten.beginnZusatzkursGE = jahrgangsdaten.ZusatzkursGEErstesHalbjahr;
    	daten.beginnZusatzkursSW = jahrgangsdaten.ZusatzkursSWErstesHalbjahr;
    	List<DTOGostJahrgangBeratungslehrer> dtosBeratungslehrer = conn.queryNamed("DTOGostJahrgangBeratungslehrer.abi_jahrgang", daten.abiturjahr, DTOGostJahrgangBeratungslehrer.class);
    	daten.beratungslehrer.addAll(DataGostBeratungslehrer.getBeratungslehrer(conn, dtosBeratungslehrer));		
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Integer abiturjahr, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() <= 0) 
	    	return Response.status(Status.OK).build();
		try {
			DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr);
	    	if (jahrgangsdaten == null)
	    		throw OperationError.NOT_FOUND.exception();
	    	for (Entry<String, Object> entry : map.entrySet()) {
	    		String key = entry.getKey();
	    		Object value = entry.getValue();
	    		switch (key) {
					case "abiturjahr" -> {
						Integer patch_abiturjahr = JSONMapper.convertToInteger(value, true);
						if ((patch_abiturjahr == null) || (patch_abiturjahr != abiturjahr))
							throw OperationError.BAD_REQUEST.exception();
					}
    				case "jahrgang" -> throw OperationError.BAD_REQUEST.exception();
    				case "bezeichnung" -> throw OperationError.BAD_REQUEST.exception();
    				case "istAbgeschlossen" -> throw OperationError.BAD_REQUEST.exception();
    				case "textBeratungsbogen" -> jahrgangsdaten.TextBeratungsbogen = JSONMapper.convertToString(value, true, true);
    				case "textMailversand" -> jahrgangsdaten.TextMailversand = JSONMapper.convertToString(value, true, true);
    				// TODO Verfügbarkeit von Zusatzkurse - siehe DTOGostJahrgangsdaten
    				// TODO case "beginnZusatzkursGE" -> jahrgangsdaten.ZusatzkursGEErstesHalbjahr = JSONMapper.convertToString(value, false, false); // TODO Kürzel prüfen
    				// TODO case "beginnZusatzkursSW" -> jahrgangsdaten.ZusatzkursSWErstesHalbjahr = JSONMapper.convertToString(value, false, false); // TODO Kürzel prüfen
    				// TODO case "beratungslehrer" -> TODO set Beratungslehrer - zusätzliche API
	    			default -> throw OperationError.BAD_REQUEST.exception();
	    		}
	    	}
	    	conn.transactionPersist(jahrgangsdaten);
	    	conn.transactionCommit();
	    	return Response.status(Status.OK).build();
		} catch (Exception e) {
			if (e instanceof WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
	}

}
