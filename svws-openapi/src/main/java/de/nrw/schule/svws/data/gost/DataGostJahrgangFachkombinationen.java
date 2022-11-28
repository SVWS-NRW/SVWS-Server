package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.function.Function;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.gost.GostJahrgangFachkombination;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.utils.OperationError;
import de.nrw.schule.svws.db.utils.gost.FaecherGost;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostJahrgangFachkombination}.
 */
public class DataGostJahrgangFachkombinationen extends DataManager<Long> {

	/** der Abiturjahrgang */
	protected int abijahrgang;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostJahrgangFachkombination}.
	 * 
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahrgang   der Abiturjahrgang 
	 */
	public DataGostJahrgangFachkombinationen(DBEntityManager conn, int abijahrgang) {
		super(conn);
		this.abijahrgang = abijahrgang;
	}
	
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostJahrgangFachkombinationen} in einen Core-DTO {@link GostJahrgangFachkombination}.  
	 */
	public static Function<DTOGostJahrgangFachkombinationen, GostJahrgangFachkombination> dtoMapper = (DTOGostJahrgangFachkombinationen kombi) -> {
		GostJahrgangFachkombination daten = new GostJahrgangFachkombination();
		daten.id = kombi.ID;
		daten.abiturjahr = kombi.Abi_Jahrgang;
		daten.fachID1 = kombi.Fach1_ID;
		daten.kursart1 = kombi.Kursart1;
		daten.fachID2 = kombi.Fach2_ID;
		daten.kursart2 = kombi.Kursart2;
		daten.gueltigInHalbjahr[0] = kombi.EF1;
		daten.gueltigInHalbjahr[1] = kombi.EF2;
		daten.gueltigInHalbjahr[2] = kombi.Q11;
		daten.gueltigInHalbjahr[3] = kombi.Q12;
		daten.gueltigInHalbjahr[4] = kombi.Q21;
		daten.gueltigInHalbjahr[5] = kombi.Q22;
		daten.typ = kombi.Typ.getValue();
		daten.hinweistext = kombi.Hinweistext;
		return daten;
	};


	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Lese die Fächerkombinationen für den Abiturjahrgang ein
		List<DTOGostJahrgangFachkombinationen> kombis = conn
				.queryNamed("DTOGostJahrgangFachkombinationen.abi_jahrgang", abijahrgang, DTOGostJahrgangFachkombinationen.class);
		if (kombis == null)
			return OperationError.NOT_FOUND.getResponse();
		Vector<GostJahrgangFachkombination> daten = new Vector<>();
		for (DTOGostJahrgangFachkombinationen kombi : kombis)
			daten.add(dtoMapper.apply(kombi));
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


	/**
	 * Löscht eine Regel zu einer Fachkombination der Gymnasialen Oberstufe
	 * 
	 * @param id   die ID der Regel
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(Long id) {
		try {
			conn.transactionBegin();
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme die Fachkombination
			DTOGostJahrgangFachkombinationen kombi = conn.queryByKey(DTOGostJahrgangFachkombinationen.class, id);
			if (kombi == null)
	    		throw OperationError.NOT_FOUND.exception();
			// Erzeuge den Core-DTO, der zurückgegeben wird
			GostJahrgangFachkombination daten = dtoMapper.apply(kombi);
			// Entferne die Fachkombination
			conn.transactionRemove(kombi);
			conn.transactionCommit();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}


	/**
	 * Fügt eine neue Regel zu einer Fachkombination vom angegebenen Typ hinzu
	 *  
	 * @param typ   der typ der Regel
	 * 
	 * @return die neu erstellte Regel
	 */
	public Response add(int typ) {
		try {
			conn.transactionBegin();			
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Prüfe ob der Typ der Regel korrekt ist
			GostLaufbahnplanungFachkombinationTyp kombityp = GostLaufbahnplanungFachkombinationTyp.fromValue(typ);
			// Bestimme die ID für die neue Regel
			DTODBAutoInkremente dbID = conn.queryByKey(DTODBAutoInkremente.class, Schema.tab_Gost_Jahrgang_Fachkombinationen.name());
			long id = dbID == null ? 1 : dbID.MaxID + 1;
			// Bestimme die Fächer der gymnasialen Oberstufe, um zwei Default-Fächer zu bestimmen
			GostFaecherManager fachmanager = FaecherGost.getFaecherListeGost(conn, abijahrgang);
			Vector<GostFach> faecher = fachmanager.toVector(); 
			if (faecher.size() < 2)
				throw OperationError.NOT_FOUND.exception("Nicht genügend Fächer für den Abiturjahrgang definiert.");
			DTOGostJahrgangFachkombinationen kombi = new DTOGostJahrgangFachkombinationen(id, abijahrgang, faecher.get(0).id, faecher.get(1).id, true, true, true, true, true, true, kombityp, "");
    		conn.transactionPersist(kombi);
    		GostJahrgangFachkombination daten = dtoMapper.apply(kombi);
			conn.transactionCommit();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof IllegalArgumentException iae)
				throw OperationError.BAD_REQUEST.exception(iae);
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}

}
