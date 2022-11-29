package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungRegel;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungRegelParameter;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
	public static Function<DTOGostBlockungSchiene, GostBlockungSchiene> dtoMapper = (DTOGostBlockungSchiene schiene) -> {
		GostBlockungSchiene daten = new GostBlockungSchiene();
		daten.id = schiene.ID;
		daten.nummer = schiene.Nummer;
		daten.bezeichnung = schiene.Bezeichnung;
		daten.wochenstunden = schiene.Wochenstunden;
		return daten;
	};
	

	@Override
	public Response get(Long id) {
		GostUtils.pruefeSchuleMitGOSt(conn);
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
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme die Schiene der Blockung
			DTOGostBlockungSchiene schiene = conn.queryByKey(DTOGostBlockungSchiene.class, id);
			if (schiene == null)
				return OperationError.NOT_FOUND.getResponse();
	        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
	        DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, schiene.Blockung_ID);
	        DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
	        if (vorlage == null)
	        	throw OperationError.BAD_REQUEST.exception("Die Schiene kann nicht angepasst werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
	    	for (Entry<String, Object> entry : map.entrySet()) {
	    		String key = entry.getKey();
	    		Object value = entry.getValue();
	    		switch (key) {
					case "id" -> {
						Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
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
	 * @param die zu entfernende Schiene (DB-DTO)
	 * 
	 * @return die entfernte Schiene (Core-DTO).
	 */
	private GostBlockungSchiene _delete(DTOGostBlockungSchiene schiene) {
		if (schiene == null)
			throw OperationError.NOT_FOUND.exception();
		
        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
        DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, schiene.Blockung_ID);
        DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
        if (vorlage == null)
        	throw OperationError.BAD_REQUEST.exception("Die Schiene kann nicht entfernt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
        
        // Prüfe, ob die Schiene im aktuelle Vorlage-Ergebnis Kurse hat: Dann ist das entfernen nicht erlaubt...
    	List<DTOGostBlockungZwischenergebnisKursSchiene> kurse = conn.queryList(
			"SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID = ?1 AND e.Schienen_ID = ?2", 
			DTOGostBlockungZwischenergebnisKursSchiene.class, 
			vorlage.ID, schiene.ID
    	);
        if (kurse.size() > 0)
        	throw OperationError.BAD_REQUEST.exception("Die Schiene kann nicht entfernt werden, da der Schiene bereits Kurse zugeordnet sind. Diese müssen zuerst entfernt werden.");
        GostBlockungSchiene daten = dtoMapper.apply(schiene);
        
        // Passt die Schienen-Nummern bei den Regeln an.
		List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", schiene.Blockung_ID, DTOGostBlockungSchiene.class);
		for (DTOGostBlockungSchiene tmp : schienen) {
		    if (daten.id == tmp.ID) {
	            conn.transactionRemove(tmp); // Entferne die Schiene
		    } else if (tmp.Nummer.intValue() > daten.nummer) {
		        tmp.Nummer--; // Reduziere die Nummer dieser Schiene aufgrund der entfernten Schiene 
		        conn.transactionPersist(tmp);
		    }
		}
		
		// Passe alle Regeln einem Parametern Schienenanzahl an.
		// Bestimme alle Regeln der Blockung
		List<DTOGostBlockungRegel> dtoRegeln = conn.queryNamed("DTOGostBlockungRegel.blockung_id", schiene.Blockung_ID, DTOGostBlockungRegel.class);
		Map<Long, DTOGostBlockungRegel> mapDTORegeln = dtoRegeln.stream().collect(Collectors.toMap(r -> r.ID, r -> r));
		if (dtoRegeln.size() > 0) {
			List<Long> regelIDs = dtoRegeln.stream().map(r -> r.ID).toList();
			// Bestimme die RegelParameter dieser Regeln
			List<DTOGostBlockungRegelParameter> dtoRegelParameter = conn
					.queryNamed("DTOGostBlockungRegelParameter.regel_id.multiple", regelIDs, DTOGostBlockungRegelParameter.class);
			Map<Long, List<DTOGostBlockungRegelParameter>> mapParameter = dtoRegelParameter.stream().collect(Collectors.groupingBy(r -> r.Regel_ID));
			// Erstelle die Core-Types und prüfe auf neue Parameter-Werte (verwende hierbei den gleichen Algorithmus, wie im zugehörigen Daten-Manager..
			List<GostBlockungRegel> regeln = DataGostBlockungRegel.getBlockungsregeln(dtoRegeln, dtoRegelParameter);
			// Übertrage die Anpassungen in die Datenbank
			for (GostBlockungRegel regel : regeln) {
				if (regel.parameter.size() == 0)
					continue;
				List<DTOGostBlockungRegelParameter> dtoParams = mapParameter.get(regel.id);
				if ((dtoParams == null) || (dtoParams.size() == 0))
					throw OperationError.INTERNAL_SERVER_ERROR.exception();
				Map<Integer, DTOGostBlockungRegelParameter> mapParam = dtoParams.stream().collect(Collectors.toMap(p -> p.Nummer, p -> p));
				long[] newParams = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(regel, daten.nummer);
				if (newParams == null) { // Lösche die Regel in der DB und gehe zur nächsten über
					conn.transactionRemove(mapDTORegeln.get(regel.id));
					continue;
				}
				// Prüfe, ob die Parameter der Regel verändert wurden und aktualisiere diese ggf. in der DB 
				for (int i = 0; i < newParams.length; i++) {
					if (newParams[i] != regel.parameter.get(i)) {
						DTOGostBlockungRegelParameter dtoParam = mapParam.get(i);
						dtoParam.Parameter = newParams[i];
						conn.transactionPersist(dtoParam);
					}
				}
			}
		}
		return daten;
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
		    if (id == null)
		        return OperationError.CONFLICT.getResponse();
			// Bestimme die Schiene der Blockung
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
            GostBlockungSchiene daten = _delete(conn.queryByKey(DTOGostBlockungSchiene.class, id));
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
     * Fügt eine weitere Schiene zu einer Blockung der Gymnasialen Oberstufe hinzu
	 * 
     * @param idBlockung   die ID der Blockung
	 * 
	 * @return Eine Response mit der ID der neuen Schiene 
	 */
	public Response addSchiene(long idBlockung) {
		try {
			conn.transactionBegin();
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Prüfe, ob die Blockung mit der ID existiert			
			DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
			if (blockung == null)
				throw OperationError.NOT_FOUND.exception();
	        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
	        DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
	        if (vorlage == null)
	        	throw OperationError.BAD_REQUEST.exception("Die Schiene kann nicht hinzugefügt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			DTODBAutoInkremente dbSchienenID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Schienen");
			long idSchiene = dbSchienenID == null ? 1 : dbSchienenID.MaxID + 1;
			// Ermittle, ob bereits Schienen existieren
			List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", idBlockung, DTOGostBlockungSchiene.class);
	    	int schienennummer = 1;
	    	if ((schienen != null) && (schienen.size() > 0)) { // Bestimme die erste freie Schienennummer
	    		Set<Integer> schienenIDs = schienen.stream().map(e -> e.Nummer).collect(Collectors.toSet());
	    		while (schienenIDs.contains(schienennummer))
	    			schienennummer++;
	    	}
	    	DTOGostBlockungSchiene schiene = new DTOGostBlockungSchiene(idSchiene, idBlockung, schienennummer, "Schiene " + schienennummer, 3);
	    	conn.transactionPersist(schiene);
			conn.transactionCommit();
			GostBlockungSchiene daten = dtoMapper.apply(schiene);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof IllegalArgumentException e)
				throw OperationError.NOT_FOUND.exception();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}

	
	/** 
     * Entfernt eine Schiene bei einer Blockung der Gymnasialen Oberstufe.
	 * 
     * @param idBlockung   die ID der Blockung
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt. 
	 */
	public Response deleteSchiene(long idBlockung) {
		try {
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme die Schienen der Blockung und löschen die Schiene mit der höchsten Nummer
	    	List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", idBlockung, DTOGostBlockungSchiene.class);
	    	if ((schienen == null) || (schienen.size() == 0))
	    		throw OperationError.NOT_FOUND.exception();
	    	DTOGostBlockungSchiene schiene = schienen.stream().max((a,b) -> Integer.compare(a.Nummer, b.Nummer)).get();
            GostBlockungSchiene daten = _delete(schiene);
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
