package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungKurs}.
 */
public class DataGostBlockungKurs extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungKurs}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungKurs(DBEntityManager conn) {
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
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostBlockungKurs} in einen Core-DTO {@link GostBlockungKurs}.  
	 */
	private Function<DTOGostBlockungKurs, GostBlockungKurs> dtoMapper = (DTOGostBlockungKurs kurs) -> {
		GostBlockungKurs daten = new GostBlockungKurs();
		daten.id = kurs.ID;
		daten.fach_id = kurs.Fach_ID;
		daten.kursart = kurs.Kursart.id;
		daten.nummer = kurs.Kursnummer;
		daten.istKoopKurs = kurs.IstKoopKurs;
		daten.suffix = kurs.BezeichnungSuffix == null ? "" : kurs.BezeichnungSuffix;
		daten.wochenstunden = kurs.Wochenstunden;
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
		// Bestimme den Kurs der Blockung
		DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, id);
		if (kurs == null)
			return OperationError.NOT_FOUND.getResponse();
		GostBlockungKurs daten = dtoMapper.apply(kurs);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() <= 0) 
	    	return Response.status(Status.OK).build();
		try {
			conn.transactionBegin();
			// Bestimme den Kurs der Blockung
			DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, id);
			if (kurs == null)
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
	    			case "fach_id" -> {
						Long patch_fach_id = JSONMapper.convertToLong(value, true);
						if ((patch_fach_id == null) || (patch_fach_id != kurs.Fach_ID))
							throw OperationError.BAD_REQUEST.exception();
	    			}
	    			case "kursart" -> {
	    				Integer patch_kursart = JSONMapper.convertToInteger(value, true);
						if ((patch_kursart == null) || (patch_kursart != kurs.Kursart.id))
							throw OperationError.BAD_REQUEST.exception();
	    			}
	    			case "nummer" -> {
	    				Integer patch_nummer = JSONMapper.convertToInteger(value, true);
						if ((patch_nummer == null) || (patch_nummer != kurs.Kursnummer))
							throw OperationError.BAD_REQUEST.exception();
	    			}
	    			case "istKoopKurs" -> kurs.IstKoopKurs = JSONMapper.convertToBoolean(value, false);
	    			case "suffix" -> kurs.BezeichnungSuffix = JSONMapper.convertToString(value, false, true);
	    			case "wochenstunden" -> {
	    				kurs.Wochenstunden = JSONMapper.convertToInteger(value, false);
	    				if ((kurs.Wochenstunden < 1) || (kurs.Wochenstunden > 40)) 
	    					throw OperationError.BAD_REQUEST.exception();
	    			}
	    			default -> throw OperationError.BAD_REQUEST.exception();
	    		}
	    	}
	    	conn.transactionPersist(kurs);
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
	 * Löscht einen Kurs einer Blockung der Gymnasialen Oberstufe
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(Long id) {
		try {
			// Bestimme den Kurs der Blockung
			conn.transactionBegin();
			DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				return OperationError.NOT_FOUND.getResponse();
			Schulform schulform = schule.Schulform;
			if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
				return OperationError.NOT_FOUND.getResponse();
			DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, id);
			if (kurs == null)
				return OperationError.NOT_FOUND.getResponse();
			// Entferne den Kurs
			GostBlockungKurs daten = dtoMapper.apply(kurs);
			conn.transactionRemove(kurs);
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
