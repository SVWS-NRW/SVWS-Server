package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungKurs}.
 */
public class DataGostBlockungSchiene extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungKurs}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungSchiene(DBEntityManager conn) {
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
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostBlockungSchiene} in einen Core-DTO {@link GostBlockungSchiene}.  
	 */
	private Function<DTOGostBlockungSchiene, GostBlockungSchiene> dtoMapper = (DTOGostBlockungSchiene schiene) -> {
		GostBlockungSchiene daten = new GostBlockungSchiene();
		daten.id = schiene.ID;
		daten.bezeichnung = schiene.Bezeichnung;
		daten.wochenstunden = schiene.Wochenstunden;
		return daten;
	};
	

	@Override
	public Response get(Long id) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			return OperationError.NOT_FOUND.getResponse();
		Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			return OperationError.NOT_FOUND.getResponse();
		// Bestimme die Schiene der Blockung
		DTOGostBlockungSchiene schiene = conn.queryByKey(DTOGostBlockungSchiene.class, id);
		if (schiene == null)
			return OperationError.NOT_FOUND.getResponse();
		GostBlockungSchiene daten = dtoMapper.apply(schiene);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() <= 0) 
	    	return Response.status(Status.OK).build();
		try {
			conn.transactionBegin();
			// Bestimme die Schiene der Blockung
			DTOGostBlockungSchiene schiene = conn.queryByKey(DTOGostBlockungSchiene.class, id);
			if (schiene == null)
				return OperationError.NOT_FOUND.getResponse();
	    	for (Entry<String, Object> entry : map.entrySet()) {
	    		String key = entry.getKey();
	    		Object value = entry.getValue();
	    		switch (key) {
					case "id" -> {
						Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id != id))
							throw OperationError.BAD_REQUEST.exception();
					}
	    			case "bezeichnung" -> schiene.Bezeichnung = JSONMapper.convertToString(value, false, false);
	    			case "wochenstunden" -> {
	    				schiene.Wochenstunden = JSONMapper.convertToInteger(value, false);
	    				if ((schiene.Wochenstunden < 1) || (schiene.Wochenstunden > 40)) 
	    					throw OperationError.BAD_REQUEST.exception();
	    			}
	    			case "nummer" -> {
						Integer patch_nummer = JSONMapper.convertToInteger(value, true);
	    				if ((patch_nummer == null) || (patch_nummer != schiene.Nummer))
	    					throw OperationError.BAD_REQUEST.exception();
	    			}
	    			default -> throw OperationError.BAD_REQUEST.exception();
	    		}
	    	}
	    	conn.transactionPersist(schiene);
	    	conn.transactionCommit();
		} catch (Exception e) {
			if (e instanceof WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
    	return Response.status(Status.OK).build();
	}

	
	
	/**
	 * Löscht eine Schiene einer Blockung der Gymnasialen Oberstufe
	 * 
	 * @param id   die ID der Schiene
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(Long id) {
		try {
			// Bestimme die Schiene der Blockung
			conn.transactionBegin();
			DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				return OperationError.NOT_FOUND.getResponse();
			Schulform schulform = schule.Schulform;
			if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
				return OperationError.NOT_FOUND.getResponse();
			DTOGostBlockungSchiene schiene = conn.queryByKey(DTOGostBlockungSchiene.class, id);
			if (schiene == null)
				return OperationError.NOT_FOUND.getResponse();
			// Entferne die Schiene
			GostBlockungSchiene daten = dtoMapper.apply(schiene);
			conn.transactionRemove(schiene);
			conn.transactionCommit();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}

}
