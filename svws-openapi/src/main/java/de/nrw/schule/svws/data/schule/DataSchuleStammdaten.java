package de.nrw.schule.svws.data.schule;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.schule.SchuleStammdaten;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.utils.OperationError;
import de.nrw.schule.svws.db.utils.data.Schule;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuleStammdaten}.
 */
public class DataSchuleStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuleStammdaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuleStammdaten(DBEntityManager conn) {
		super(conn);
	}

	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOEigeneSchule} in einen Core-DTO {@link SchuleStammdaten}.  
	 */
	private Function<DTOEigeneSchule, SchuleStammdaten> dtoMapper = (DTOEigeneSchule schule) -> {
    	SchuleStammdaten daten = new SchuleStammdaten();
		daten.schulNr = schule.SchulNr;
		daten.schulform = schule.Schulform.daten.kuerzel;
		daten.bezeichnung1 = schule.Bezeichnung1;
		daten.bezeichnung2 = schule.Bezeichnung2;
		daten.bezeichnung3 = schule.Bezeichnung3;
		daten.strassenname = schule.Strassenname;
		daten.hausnummer = schule.HausNr;
		daten.hausnummerZusatz = schule.HausNrZusatz;
		daten.plz = schule.PLZ;
		daten.ort = schule.Ort;
		daten.telefon = schule.Telefon;
		daten.fax = schule.Fax;
		daten.email = schule.Email;
		daten.webAdresse = schule.WebAdresse;
		daten.idSchuljahresabschnitt = schule.Schuljahresabschnitts_ID;
		daten.anzJGS_Jahr = schule.AnzJGS_Jahr;
		daten.schuleAbschnitte.anzahlAbschnitte = schule.AnzahlAbschnitte;
		daten.schuleAbschnitte.abschnittBez = schule.AbschnittBez;
		daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt1);
		if (daten.schuleAbschnitte.anzahlAbschnitte >= 2)
			daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt2);
		if (daten.schuleAbschnitte.anzahlAbschnitte >= 3)
			daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt3);
		if (daten.schuleAbschnitte.anzahlAbschnitte >= 4)
			daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt4);
		daten.dauerUnterrichtseinheit = schule.DauerUnterrichtseinheit == null ? 45 : schule.DauerUnterrichtseinheit;
		return daten;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Response get(Long id) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
    		return OperationError.NOT_FOUND.getResponse();
    	SchuleStammdaten daten = dtoMapper.apply(schule);
    	daten.abschnitte.addAll((new DataSchuljahresabschnitte(conn)).getAbschnitte());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}
	
	/**
	 * Bestimmt die Schulnummer der Schule
	 * 
	 * @return die Schulnummer oder null im Fehlerfall
	 */
	public Integer getSchulnummer() {
    	DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if (schule == null)
    		return null;
    	return schule.SchulNr;
	}
	
	
	/**
	 * Bestimmt die Schulnummer der Schule
	 * 
	 * @return Die HTTP-Response (NOT_FOUND im Fehlerfall)
	 */
	public Response getSchulnummerResponse() {
    	DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if (schule == null)
    		return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(schule.SchulNr).build();
	}
	

	@Override
	public Response patch(Long id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
    		try {
    			conn.transactionBegin();
    			DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		    	if (schule == null)
		    		return OperationError.NOT_FOUND.getResponse();
		    	for (Entry<String, Object> entry : map.entrySet()) {
		    		String key = entry.getKey();
		    		Object value = entry.getValue();
		    		switch (key) {
		    			case "schulNr" -> throw OperationError.BAD_REQUEST.exception(); 
		    			case "schulform" -> throw OperationError.BAD_REQUEST.exception();
		    			case "bezeichnung1" -> schule.Bezeichnung1 = JSONMapper.convertToString(value, true, true);
		    			case "bezeichnung2" -> schule.Bezeichnung2 = JSONMapper.convertToString(value, true, true);
		    			case "bezeichnung3" -> schule.Bezeichnung3 = JSONMapper.convertToString(value, true, true);
		    			case "strassenname" -> schule.Strassenname = JSONMapper.convertToString(value, true, true);
		    			case "hausnummer" -> schule.HausNr = JSONMapper.convertToString(value, true, true);
		    			case "hausnummerZusatz" -> schule.HausNrZusatz = JSONMapper.convertToString(value, true, true);

		    			case "plz" -> schule.PLZ = JSONMapper.convertToString(value, true, true); // TODO Schema anpassen: Stakue-Ortskatalog nutzen -> Orts-ID
		    			case "ort" -> schule.Ort = JSONMapper.convertToString(value, true, true); // TODO Schema anpassen: Stakue-Ortskatalog nutzen -> Orts-ID

		    			case "telefon" -> schule.Telefon = JSONMapper.convertToString(value, true, true);
		    			case "fax" -> schule.Fax = JSONMapper.convertToString(value, true, true);
		    			case "email" -> schule.Email = JSONMapper.convertToString(value, true, true);
		    			case "webAdresse" -> schule.WebAdresse = JSONMapper.convertToString(value, true, true);
		    			
		    			case "idSchuljahresabschnitt" -> schule.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false); // TODO ID des Schuljahresabschnittes überprüfen
		    			case "anzJGS_Jahr" -> schule.AnzJGS_Jahr = JSONMapper.convertToInteger(value, false); // TODO Abschnitt überprüfen

		    			case "schuleAbschnitte" -> {
		    				@SuppressWarnings("unchecked") // TODO check conversion
							Map<String, Object> mapAbschnitte = (Map<String, Object>)value;
		    				if (mapAbschnitte.containsKey("anzahlAbschnitte")) {
		    					Integer anzahlAbschnitte = JSONMapper.convertToInteger(mapAbschnitte.get("anzahlAbschnitte"), false);
		    					if ((anzahlAbschnitte < 1) || (anzahlAbschnitte > 4))
		    						throw OperationError.CONFLICT.exception();
		    					schule.AnzahlAbschnitte = anzahlAbschnitte; 
		    				}
		    				if (mapAbschnitte.containsKey("abschnittBez"))
		    					schule.AbschnittBez = JSONMapper.convertToString(mapAbschnitte.get("abschnittBez"), true, true);
		    				if (mapAbschnitte.containsKey("bezAbschnitte")) {
		    					List<?> bezAbschnitte = (List<?>)mapAbschnitte.get("bezAbschnitte");
		    					if (bezAbschnitte.size() != schule.AnzahlAbschnitte)
		    						throw OperationError.CONFLICT.exception();
		    					for (int i = 0; i < bezAbschnitte.size(); i++) {
		    						Object objBezeichnung = bezAbschnitte.get(i);
		    						if (!(objBezeichnung instanceof String))
		    							throw OperationError.BAD_REQUEST.exception();
		    						switch (i) {
		    							case 0 -> schule.BezAbschnitt1 = (String)objBezeichnung;
		    							case 1 -> schule.BezAbschnitt2 = (String)objBezeichnung;
		    							case 2 -> schule.BezAbschnitt3 = (String)objBezeichnung;
		    							case 3 -> schule.BezAbschnitt4 = (String)objBezeichnung;
		    						}
		    					}
		    				}
		    			}

		    			case "dauerUnterrichtseinheit" -> schule.DauerUnterrichtseinheit = JSONMapper.convertToInteger(value, false); // TODO Dauer in Minuten prüfen, evtl. einschränken
		    			case "abschnitte" -> throw OperationError.BAD_REQUEST.exception();
		    			
		    			default -> throw OperationError.BAD_REQUEST.exception();
		    		}
		    	}
		    	conn.transactionPersist(schule);
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
	 * Ermittelt das Schullogo.
	 * 
	 * @return Die HTTP-Response der Get-Operation
	 */
	public Response getSchullogo() {
    	Schule schule = Schule.query(conn);
    	if ((schule == null) || (schule.dto == null))
    		return OperationError.NOT_FOUND.getResponse();
    	String daten = "\"" + schule.dto.SchulLogoBase64 + "\"";
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}
	
	
	/**
	 * Ersetzt das Schullogo. 
	 * 
	 * @param is            der {@link InputStream} mit dem JSON-Patch für das Logo
	 * 
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public Response putSchullogo(InputStream is) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if (schule == null)
    		throw OperationError.NOT_FOUND.exception();
    	schule.SchulLogoBase64 = JSONMapper.toString(is);
    	conn.persist(schule);
		return Response.ok().build();
	}
	
	
}
