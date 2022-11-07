package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungRegel;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungRegelParameter;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungRegel}.
 */
public class DataGostBlockungRegel extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungRegel}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungRegel(DBEntityManager conn) {
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
	public Response get(Long id) {
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Bestimme die Regel der Blockung
		DTOGostBlockungRegel regel = conn.queryByKey(DTOGostBlockungRegel.class, id);
		if (regel == null)
			return OperationError.NOT_FOUND.getResponse();
		GostBlockungRegel daten = new GostBlockungRegel();
		daten.id = regel.ID;
		daten.typ = regel.Typ.typ;
		List<DTOGostBlockungRegelParameter> p = conn.queryNamed("DTOGostBlockungRegelParameter.regel_id", daten.id, DTOGostBlockungRegelParameter.class);
		if ((p != null) && (p.size() > 0))
			daten.parameter.addAll(p.stream().sorted((a, b) -> Integer.compare(a.Nummer, b.Nummer)).map(r -> r.Parameter).collect(Collectors.toList()));
		
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(Long id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() <= 0) 
	    	return Response.status(Status.OK).build();
		try {
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme die Regel der Blockung
			DTOGostBlockungRegel regel = conn.queryByKey(DTOGostBlockungRegel.class, id);
			if (regel == null)
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
	    			case "typ" -> {
						Integer patch_typ = JSONMapper.convertToInteger(value, true);
						if ((patch_typ == null) || (patch_typ != regel.Typ.typ))
							throw OperationError.BAD_REQUEST.exception();
	    			}
	    			case "parameter" -> {
	    				if (!(value instanceof List))
	    					throw OperationError.BAD_REQUEST.exception();
	    				@SuppressWarnings("unchecked")
						List<? extends Number> params = (List<? extends Number>)value;
	    				// Überprüfe zunächst die Anzahl der Parameter
	    				int pcount = regel.Typ.getParamCount();
	    				if (pcount != params.size())
	    					throw OperationError.BAD_REQUEST.exception();
	    				// Aktualisiere die Parameter
	    				for (int i = 0; i < pcount; i++) {
	    					// Bestimme Typ und Wert
	    					GostKursblockungRegelParameterTyp ptype = regel.Typ.getParamType(i);
	    					long pvalue = params.get(i).longValue();
	    					// Überprüfe den Parameter-Wert, ob dieser einen gültigen Wert für die Blockung enthält
	    					switch (ptype) {
								case KURSART -> {
									GostKursart kursart = GostKursart.fromIDorNull((int)pvalue);
									if (kursart == null)
										throw OperationError.BAD_REQUEST.exception();
								}
								case KURS_ID -> {
									DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, pvalue);
									if ((kurs == null) || (kurs.Blockung_ID != regel.Blockung_ID))
										throw OperationError.BAD_REQUEST.exception();
								}
								case SCHIENEN_NR -> {
									List<DTOGostBlockungSchiene> dtos = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", regel.Blockung_ID, DTOGostBlockungSchiene.class);
									if ((dtos == null) || (dtos.size() <= 0))
										throw OperationError.BAD_REQUEST.exception();
									Set<Integer> schienen = dtos.stream().map(s -> s.Nummer).collect(Collectors.toSet());
									if (!schienen.contains((int)pvalue))
										throw OperationError.BAD_REQUEST.exception();
								}
								case SCHUELER_ID -> {
									DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, pvalue);
									if (schueler == null)
										throw OperationError.BAD_REQUEST.exception();
								}
								default -> throw OperationError.BAD_REQUEST.exception();
	    					}
	    					// Aktualisiere den Parameter-Wert in der Datenbank, sofern er sich geändert hat
	    					DTOGostBlockungRegelParameter param = conn.queryByKey(DTOGostBlockungRegelParameter.class, id, i);
							if (param.Parameter != pvalue) {
								param.Parameter = pvalue;
								conn.transactionPersist(param);
							}
	    				}
	    			}
	    			default -> throw OperationError.BAD_REQUEST.exception();
	    		}
	    	}
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
	 * Löscht eine Regel einer Blockung der Gymnasialen Oberstufe
	 * 
	 * @param id   die ID der Regel
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(Long id) {
		try {
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme zunächst die Regel-Parameter (diese werden beim Entfernen der Regel automatisch mit entfernt.
			GostBlockungRegel daten = new GostBlockungRegel();
			daten.id = id;
	    	List<DTOGostBlockungRegelParameter> params = conn.queryNamed("DTOGostBlockungRegelParameter.regel_id", id, DTOGostBlockungRegelParameter.class);
	    	if ((params == null) || (params.size() <= 0))
	    		throw OperationError.NOT_FOUND.exception();
	    	params.sort((a,b) -> Integer.compare(a.Nummer, b.Nummer));
			for (DTOGostBlockungRegelParameter param : params)
				daten.parameter.add(param.Parameter);
			// Bestimme die Regel
			DTOGostBlockungRegel regel = conn.queryByKey(DTOGostBlockungRegel.class, id);
			if (regel == null)
	    		throw OperationError.NOT_FOUND.exception();
			daten.typ = regel.Typ.typ;
			// Entferne die Regel
			conn.transactionRemove(regel);
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
