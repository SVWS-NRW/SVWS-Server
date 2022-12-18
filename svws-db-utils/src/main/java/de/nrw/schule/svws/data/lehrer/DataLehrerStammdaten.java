package de.nrw.schule.svws.data.lehrer;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.nrw.schule.svws.core.data.lehrer.LehrerStammdaten;
import de.nrw.schule.svws.core.types.Geschlecht;
import de.nrw.schule.svws.core.types.PersonalTyp;
import de.nrw.schule.svws.core.types.schule.Nationalitaeten;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.data.JSONMapper;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOOrtsteil;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrer;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrerFoto;
import de.nrw.schule.svws.db.utils.OperationError;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerStammdaten}.
 */
public class DataLehrerStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerStammdaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerStammdaten(DBEntityManager conn) {
		super(conn);
	}

	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrer} in einen Core-DTO {@link LehrerStammdaten}.  
	 */
	private Function<DTOLehrer, LehrerStammdaten> dtoMapper = (DTOLehrer lehrer) -> {
		LehrerStammdaten daten = new LehrerStammdaten();
		daten.id = lehrer.ID;
		daten.kuerzel = lehrer.Kuerzel;
		daten.personalTyp = lehrer.PersonTyp.kuerzel;
		daten.anrede = lehrer.Anrede == null ? "" : lehrer.Anrede;
		daten.titel = lehrer.Titel == null ? "" : lehrer.Titel;
		daten.amtsbezeichnung = lehrer.Amtsbezeichnung == null ? "" : lehrer.Amtsbezeichnung;
		daten.nachname = lehrer.Nachname == null ? "" : lehrer.Nachname;
		daten.vorname = lehrer.Vorname == null ? "" : lehrer.Vorname;
		daten.geschlecht = lehrer.Geschlecht.id;
		daten.geburtsdatum = lehrer.Geburtsdatum;
		daten.staatsangehoerigkeitID = lehrer.staatsangehoerigkeit == null ? null : lehrer.staatsangehoerigkeit.daten.iso3;
		daten.strassenname = lehrer.Strassenname; 
		daten.hausnummer = lehrer.HausNr; 
		daten.hausnummerZusatz = lehrer.HausNrZusatz; 
		daten.wohnortID = lehrer.Ort_ID;
		daten.ortsteilID = lehrer.Ortsteil_ID;
		daten.telefon = lehrer.telefon;
		daten.telefonMobil = lehrer.telefonMobil;
		daten.emailDienstlich = lehrer.eMailDienstlich;
		daten.emailPrivat = lehrer.eMailPrivat;
		daten.foto = "";
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
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
    	if (lehrer == null)
    		return OperationError.NOT_FOUND.getResponse();
		LehrerStammdaten daten = dtoMapper.apply(lehrer);
		DTOLehrerFoto lehrerFoto = conn.queryByKey(DTOLehrerFoto.class, id);
		if (lehrerFoto != null)
			daten.foto = lehrerFoto.FotoBase64;
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
    		try {
    			conn.transactionBegin();
	    		DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
		    	if (lehrer == null)
		    		throw OperationError.NOT_FOUND.exception();
		    	for (Entry<String, Object> entry : map.entrySet()) {
		    		String key = entry.getKey();
		    		Object value = entry.getValue();
		    		switch (key) {
		    			// Basisdaten
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
								throw OperationError.BAD_REQUEST.exception();
						}
		    			case "foto" -> {
		    		    	String strData = JSONMapper.convertToString(value, true, true);
    	        			DTOLehrerFoto lehrerFoto = conn.queryByKey(DTOLehrerFoto.class, id);
    	        			if (lehrerFoto == null)
    	        				lehrerFoto = new DTOLehrerFoto(id);
    	        			String oldFoto = lehrerFoto.FotoBase64;
    	        	    	if (((strData == null) && (oldFoto == null)) || ((strData != null) && (strData.equals(oldFoto))))
    	        	    		return Response.status(Status.OK).build();
    	        	    	lehrerFoto.FotoBase64 = strData;
    	        	    	conn.transactionPersist(lehrerFoto);
		    			}
		    			case "kuerzel" -> lehrer.Kuerzel = JSONMapper.convertToString(value, false, false);
		    			case "personalTyp" -> {
			        		PersonalTyp p = PersonalTyp.fromBezeichnung(JSONMapper.convertToString(value, false, false));
			        		if (p == null)
			        			throw OperationError.CONFLICT.exception();
			            	lehrer.PersonTyp = p;
		    			}
		    			case "anrede" -> lehrer.Kuerzel = JSONMapper.convertToString(value, true, true);
		    			case "titel" -> lehrer.Titel = JSONMapper.convertToString(value, true, true);
		    			case "amtsbezeichnung" -> lehrer.Amtsbezeichnung = JSONMapper.convertToString(value, true, true);
		    			case "nachname" -> lehrer.Nachname = JSONMapper.convertToString(value, false, false);
		    			case "vorname" -> lehrer.Vorname = JSONMapper.convertToString(value, false, false);
		    			case "geschlecht" -> {
		    				Geschlecht geschlecht = Geschlecht.fromValue(JSONMapper.convertToInteger(value, false));
		    				if (geschlecht == null)
		    					throw OperationError.CONFLICT.exception();
		    				lehrer.Geschlecht = geschlecht;
		    			}
		    			case "geburtsdatum" -> lehrer.Geburtsdatum = JSONMapper.convertToString(value, false, false);
		    			case "staatsangehoerigkeitID" -> {
		    		    	String staatsangehoerigkeitID = JSONMapper.convertToString(value, true, true);
		    		    	if ((staatsangehoerigkeitID == null) || ("".equals(staatsangehoerigkeitID))) {
	    						lehrer.staatsangehoerigkeit = null;
	    					} else {
	    						Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeitID);
		    			    	if (nat == null)
		    			    		throw OperationError.NOT_FOUND.exception();
		    			    	lehrer.staatsangehoerigkeit = nat;
	    					}
		    			}
		    			
		    			// Wohnort und Kontaktdaten
		    			case "strassenname" -> lehrer.Strassenname = JSONMapper.convertToString(value, true, true);
		    			case "hausnummer" -> lehrer.HausNr = JSONMapper.convertToString(value, true, true);
		    			case "hausnummerZusatz" -> lehrer.HausNrZusatz = JSONMapper.convertToString(value, true, true);
		    			case "wohnortID" -> {
		    				setWohnort(conn, lehrer, JSONMapper.convertToLong(value, true), map.get("ortsteilID") == null ? lehrer.Ortsteil_ID : ((Long)map.get("ortsteilID")));
		    			}
		    			case "ortsteilID" -> {
		    				setWohnort(conn, lehrer, map.get("wohnortID") == null ? lehrer.Ort_ID : ((Long)map.get("wohnortID")), JSONMapper.convertToLong(value, true));
		    			}
		    			case "telefon" -> lehrer.telefon = JSONMapper.convertToString(value, true, true);
		    			case "telefonMobil" -> lehrer.telefonMobil = JSONMapper.convertToString(value, true, true);
		    			case "emailDienstlich" -> lehrer.eMailDienstlich = JSONMapper.convertToString(value, true, true);
		    			case "emailPrivat" -> lehrer.eMailPrivat = JSONMapper.convertToString(value, true, true);
		    			
		    			default -> throw OperationError.BAD_REQUEST.exception();
		    		}
		    	}
		    	conn.transactionPersist(lehrer);
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
     * Setzt den Wohnort bei den Lehrerdaten und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
     * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
     * 
     * @param conn         die aktuelle Datenbankverbindung
     * @param lehrer       das Lehrer-DTO der Datenbank 
     * @param wohnortID    die zu setzende Wohnort-ID
     * @param ortsteilID   die zu setzende Ortsteil-ID
     * 
     * @throws WebApplicationException   eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
     */
    private static void setWohnort(DBEntityManager conn, DTOLehrer lehrer, Long wohnortID, Long ortsteilID) throws WebApplicationException {
    	if ((wohnortID != null) && (wohnortID < 0))
    		throw OperationError.CONFLICT.exception();
    	if ((ortsteilID != null) && (ortsteilID < 0))
    		throw OperationError.CONFLICT.exception();
		lehrer.Ort_ID = wohnortID;
    	// Prüfe, ob die Ortsteil ID in Bezug auf die WohnortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		Long ortsteilIDNeu = ortsteilID;
		if (ortsteilIDNeu != null) {
			DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, ortsteilIDNeu);
	    	if ((ortsteil == null) || ((ortsteil.Ort_ID != null) && (!ortsteil.Ort_ID.equals(wohnortID)))) {
	    		ortsteilIDNeu = null;
	    	}
		}
		lehrer.Ortsteil_ID = ortsteilIDNeu;    	
    }
	
}
