package de.nrw.schule.svws.data.kataloge;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.schule.ReligionEintrag;
import de.nrw.schule.svws.core.data.schule.ReligionKatalogEintrag;
import de.nrw.schule.svws.core.types.statkue.Religion;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOKonfession;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ReligionEintrag}.
 */
public class DataReligionen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ReligionEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataReligionen(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStatkueNationalitaeten} in einen Core-DTO {@link ReligionEintrag}.  
	 */
	private Function<DTOKonfession, ReligionEintrag> dtoMapper = (DTOKonfession k) -> {
		ReligionEintrag daten = new ReligionEintrag();
		daten.id = k.ID;
		daten.text = k.Bezeichnung;
		daten.textZeugnis = k.ZeugnisBezeichnung;
		daten.kuerzel = k.StatistikKrz;
		daten.sortierung = k.Sortierung;
		daten.istSichtbar = k.Sichtbar;
		daten.istAenderbar = k.Aenderbar;
		return daten;
	};

	@Override
	public Response getAll() {
    	List<DTOKonfession> katalog = conn.queryAll(DTOKonfession.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<ReligionEintrag> daten = katalog.stream().map(dtoMapper).collect(Collectors.toList());        
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(Long id) {
		DTOKonfession reli = conn.queryByKey(DTOKonfession.class, id);
		if (reli == null)
			throw OperationError.NOT_FOUND.exception();
		ReligionEintrag daten = dtoMapper.apply(reli);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		if (id == null)
            return OperationError.NOT_FOUND.getResponse("Die Id der zu ändernden Religion darf nicht null sein.");
        Map<String, Object> map = JSONMapper.toMap(is);
        if (map.size() > 0){
            try{
                conn.transactionBegin();
                DTOKonfession reli = conn.queryByKey(DTOKonfession.class, id);
                if (reli == null)
                    return OperationError.NOT_FOUND.getResponse("Die Beschäftigungsart mit der ID"+id+" existiert nicht.");
                for (Entry<String, Object> entry : map.entrySet()){
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    switch (key){
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id != id))
								throw OperationError.BAD_REQUEST.exception();
						}
						case "kuerzel" -> reli.StatistikKrz = JSONMapper.convertToString(value, true, true);
                        case "text" -> reli.Bezeichnung = JSONMapper.convertToString(value, true, true);
                        case "textZeugnis" -> reli.ZeugnisBezeichnung = JSONMapper.convertToString(value, true, true);
						case "istSichtbar" -> reli.Sichtbar = JSONMapper.convertToBoolean(value, true);
						case "istAenderbar" -> reli.Aenderbar = JSONMapper.convertToBoolean(value, true);
						case "sortierung" -> reli.Sortierung = JSONMapper.convertToInteger(value, true);
                       	default -> throw OperationError.BAD_REQUEST.exception();
                    }
                }
                conn.transactionPersist(reli);
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
	 * Erstellt eine neue Religion
	 * 
	 * @param  is					JSON-Objekt mit den Daten
	 * @return Eine Response mit der neuen Religion 
	 */
	public Response create(InputStream is) {
		DTOKonfession reli = null;
		Map<String, Object> map = JSONMapper.toMap(is);
			if(map.size() > 0) {
				try {
					conn.transactionBegin();
					// Bestimme die ID der neuen Religion
					DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "K_Religion");
					Long ID = lastID == null ? 1 : lastID.MaxID + 1;
					// Religion anlegen
					reli = new DTOKonfession(ID, "");
					for(Entry<String, Object> entry : map.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						switch(key) {
							
							case "id" -> {
								Long create_id = JSONMapper.convertToLong(value, true) ;
								create_id = null;  // TODO JSON-Object liefert von der Client-Seite den Attributswert für id die Integerzahl 0.
								if ( create_id != null )
									throw OperationError.BAD_REQUEST.exception("Religion-ID muss bei der Erstellung null sein.");
							}
							case "kuerzel" -> {
								reli.StatistikKrz = JSONMapper.convertToString(value, true, true);
								if(reli.StatistikKrz != null) {
									ReligionKatalogEintrag rke = Religion.getByKuerzel(reli.StatistikKrz).daten;
									if( rke == null)
										throw OperationError.NOT_FOUND.exception("Eine Religion mit dem  Küruel "+reli.StatistikKrz+" existiert in der amtlichen Statistik nicht.");
								}
							}
	                        case "text" -> reli.Bezeichnung = JSONMapper.convertToString(value, true, true);
	                        case "textZeugnis" -> reli.ZeugnisBezeichnung = JSONMapper.convertToString(value, true, true);
							case "istSichtbar" -> reli.Sichtbar = JSONMapper.convertToBoolean(value, true);
							case "istAenderbar" -> reli.Aenderbar = JSONMapper.convertToBoolean(value, true);
							case "sortierung" -> reli.Sortierung = JSONMapper.convertToInteger(value, true);
	                       	default -> throw OperationError.BAD_REQUEST.exception();
						}
					}
					conn.transactionPersist(reli);
					if( !conn.transactionCommit())
						return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren der Religion");
				}catch (Exception e) {
					if(e instanceof WebApplicationException webApplicationException)
						return webApplicationException.getResponse();
					return OperationError.INTERNAL_SERVER_ERROR.getResponse();
				}finally {
					conn.transactionRollback();
				}
			}
			ReligionEintrag daten = dtoMapper.apply(reli);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}
	
}
