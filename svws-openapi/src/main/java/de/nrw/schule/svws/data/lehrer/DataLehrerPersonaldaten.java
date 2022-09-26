package de.nrw.schule.svws.data.lehrer;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.lehrer.LehrerPersonaldaten;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrer;
import de.nrw.schule.svws.db.utils.OperationError;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerPersonaldaten}.
 */
public class DataLehrerPersonaldaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerPersonaldaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonaldaten(DBEntityManager conn) {
		super(conn);
	}

	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrer} in einen Core-DTO {@link LehrerPersonaldaten}.  
	 */
	private Function<DTOLehrer, LehrerPersonaldaten> dtoMapper = (DTOLehrer lehrer) -> {
		LehrerPersonaldaten daten = new LehrerPersonaldaten();
		daten.id = lehrer.ID;
		daten.identNrTeil1 = lehrer.identNrTeil1;
		daten.identNrTeil2SerNr = lehrer.identNrTeil2SerNr;
		daten.personalaktennummer = lehrer.PANr;
		daten.lbvPersonalnummer = lehrer.personalNrLBV;
		daten.lbvVerguetungsschluessel = lehrer.verguetungsSchluessel;
		daten.zugangsdatum = lehrer.DatumZugang;
		daten.zugangsgrund = lehrer.GrundZugang;
		daten.abgangsdatum = lehrer.DatumAbgang;
		daten.abgangsgrund = lehrer.GrundAbgang;
		daten.pflichtstundensoll = lehrer.PflichtstdSoll;
		daten.rechtsverhaeltnis = lehrer.Rechtsverhaeltnis;
		daten.beschaeftigungsart = lehrer.Beschaeftigungsart;
		daten.einsatzstatus = lehrer.Einsatzstatus;
		daten.stammschulnummer = lehrer.StammschulNr;
		daten.masernImpfnachweis = lehrer.MasernImpfnachweis;
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
		LehrerPersonaldaten daten = dtoMapper.apply(lehrer);
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
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id != id))
								throw OperationError.BAD_REQUEST.exception();
						}

		    			case "identNrTeil1" -> lehrer.identNrTeil1 = JSONMapper.convertToString(value, true, true);
		    			case "identNrTeil2SerNr" -> lehrer.identNrTeil2SerNr = JSONMapper.convertToString(value, true, true);
		    			case "personalaktennummer" -> lehrer.PANr = JSONMapper.convertToString(value, true, true);
		    			case "lbvPersonalnummer" -> lehrer.personalNrLBV = JSONMapper.convertToString(value, true, true);
		    			case "lbvVerguetungsschluessel" -> lehrer.verguetungsSchluessel = JSONMapper.convertToString(value, true, true);

		    			case "zugangsdatum" -> lehrer.DatumZugang = JSONMapper.convertToString(value, true, true);
		    			case "zugangsgrund" -> lehrer.GrundZugang = JSONMapper.convertToString(value, true, true);   // TODO Katalog prüfen ...
		    			case "abgangsdatum" -> lehrer.DatumAbgang = JSONMapper.convertToString(value, true, true);
		    			case "abgangsgrund" -> lehrer.GrundAbgang = JSONMapper.convertToString(value, true, true);   // TODO Katalog prüfen ...

		    			case "pflichtstundensoll" -> lehrer.PflichtstdSoll = JSONMapper.convertToDouble(value, true);
		    			case "rechtsverhaeltnis" -> lehrer.Rechtsverhaeltnis = JSONMapper.convertToString(value, true, true);   // TODO Katalog prüfen ...
		    			case "beschaeftigungsart" -> lehrer.Beschaeftigungsart = JSONMapper.convertToString(value, true, true);   // TODO Katalog prüfen ...
		    			case "einsatzstatus" -> lehrer.Einsatzstatus = JSONMapper.convertToString(value, true, true);   // TODO Katalog prüfen ...

		    			case "stammschulnummer" -> lehrer.StammschulNr = JSONMapper.convertToString(value, true, false);
		    			case "hatMasernimpfnachweis" -> lehrer.MasernImpfnachweis = JSONMapper.convertToBoolean(value, false);

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
	
}
