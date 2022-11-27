package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.GostJahrgangFachkombinationen;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostJahrgangFachkombinationen}.
 */
public class DataGostJahrgangFachkombinationen extends DataManager<Long> {

	/** der Abiturjahrgang */
	protected Integer abijahrgang;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostJahrgangFachkombinationen}.
	 * 
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahrgang   der Abiturjahrgang 
	 */
	public DataGostJahrgangFachkombinationen(DBEntityManager conn, Integer abijahrgang) {
		super(conn);
		this.abijahrgang = abijahrgang;
	}
	
	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Lese die Fächerkombinationen für den Abiturjahrgang ein
		List<DTOGostJahrgangFachkombinationen> kombis = (abijahrgang == null)
				? conn.queryList("SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Abi_Jahrgang IS NULL", DTOGostJahrgangFachkombinationen.class)
				: conn.queryNamed("DTOGostJahrgangFachkombinationen.abi_jahrgang", abijahrgang, DTOGostJahrgangFachkombinationen.class);
		if (kombis == null)
			return OperationError.NOT_FOUND.getResponse();
		Vector<GostJahrgangFachkombinationen> daten = new Vector<>();
		for (DTOGostJahrgangFachkombinationen kombi : kombis) {
			GostJahrgangFachkombinationen result = new GostJahrgangFachkombinationen();
			result.id = kombi.ID;
			result.abiturjahr = kombi.Abi_Jahrgang;
			result.fachID1 = kombi.Fach1_ID;
			result.kursart1 = kombi.Kursart1;
			result.fachID2 = kombi.Fach2_ID;
			result.kursart2 = kombi.Kursart2;
			result.gueltigInHalbjahr[0] = kombi.EF1;
			result.gueltigInHalbjahr[1] = kombi.EF2;
			result.gueltigInHalbjahr[2] = kombi.Q11;
			result.gueltigInHalbjahr[3] = kombi.Q12;
			result.gueltigInHalbjahr[4] = kombi.Q21;
			result.gueltigInHalbjahr[5] = kombi.Q22;
			result.typ = kombi.Typ.getValue();
			result.hinweistext = kombi.Hinweistext;
			daten.add(result);
		}
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Long id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
    		try {
    			conn.transactionBegin();
    			GostUtils.pruefeSchuleMitGOSt(conn);
				DTOGostJahrgangFachkombinationen kombi = conn.queryByKey(DTOGostJahrgangFachkombinationen.class, id);
				if (kombi == null)
					throw OperationError.NOT_FOUND.exception();
		    	for (Entry<String, Object> entry : map.entrySet()) {
		    		String key = entry.getKey();
		    		Object value = entry.getValue();
		    		switch (key) {
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
								throw OperationError.BAD_REQUEST.exception();
						}
		    			case "abiturjahr" -> throw OperationError.BAD_REQUEST.exception();
		    			case "fachID1" -> {
		    				kombi.Fach1_ID = JSONMapper.convertToLong(value, false);
		    				DTOFach fach = conn.queryByKey(DTOFach.class, kombi.Fach1_ID);
		    				if (fach == null)
		    					throw OperationError.NOT_FOUND.exception();
		    				if (!fach.IstOberstufenFach)
		    		    		throw OperationError.CONFLICT.exception();    	
		    			}
		    			case "fachID2" -> {
		    				kombi.Fach2_ID = JSONMapper.convertToLong(value, false);
		    				DTOFach fach = conn.queryByKey(DTOFach.class, kombi.Fach2_ID);
		    				if (fach == null)
		    					throw OperationError.NOT_FOUND.exception();
		    				if (!fach.IstOberstufenFach)
		    		    		throw OperationError.CONFLICT.exception();    	
		    			}
		    			case "kursart1" -> {
		    				kombi.Kursart1 = JSONMapper.convertToString(value, true, false);
		    				if (kombi.Kursart1 == null) {
		    					GostKursart kursart = GostKursart.fromKuerzel(kombi.Kursart1);
		    					if (kursart == null)
			    					throw OperationError.NOT_FOUND.exception();
		    				}
		    			}
		    			case "kursart2" -> {
		    				kombi.Kursart2 = JSONMapper.convertToString(value, true, false);
		    				if (kombi.Kursart2 == null) {
		    					GostKursart kursart = GostKursart.fromKuerzel(kombi.Kursart2);
		    					if (kursart == null)
			    					throw OperationError.NOT_FOUND.exception();
		    				}
		    			}
		    			case "gueltigInHalbjahr" -> {
		    				Boolean[] data = JSONMapper.convertToBooleanArray(value, false, false, 6);
		    				kombi.EF1 = data[0];
		    				kombi.EF2 = data[1];
		    				kombi.Q11 = data[2];
		    				kombi.Q12 = data[3];
		    				kombi.Q21 = data[4];
		    				kombi.Q22 = data[5];
		    			}
		    			case "typ" -> throw OperationError.BAD_REQUEST.exception();
		    			case "hinweistext" -> {
		    				kombi.Hinweistext = JSONMapper.convertToString(value, false, true);
		    			}
		    			default -> throw OperationError.BAD_REQUEST.exception();
		    		}
		    	}
		    	conn.transactionPersist(kombi);
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
