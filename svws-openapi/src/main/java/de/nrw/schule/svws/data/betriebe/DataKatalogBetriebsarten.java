package de.nrw.schule.svws.data.betriebe;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.kataloge.KatalogEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOKatalogAdressart;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KatalogEintrag}.
 */
public class DataKatalogBetriebsarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KatalogEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogBetriebsarten(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKatalogAdressart} in einen Core-DTO {@link KatalogEintrag}.  
	 */
	private Function<DTOKatalogAdressart, KatalogEintrag> dtoMapper = (DTOKatalogAdressart k) -> {
		KatalogEintrag eintrag = new KatalogEintrag();
		eintrag.id = k.ID;
		eintrag.kuerzel = k.ID.toString();
		eintrag.text = k.Bezeichnung;
		eintrag.istSichtbar = true;
		eintrag.istAenderbar = true;
		return eintrag;
	};


	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link KatalogEintrag}.  
	 */
	private Comparator<KatalogEintrag> dataComparator = (a, b) -> {
		Collator collator = Collator.getInstance(Locale.GERMAN);
		return collator.compare(a.text, b.text);
	};


	@Override
	public Response getAll() {
		List<DTOKatalogAdressart> katalog = conn.queryAll(DTOKatalogAdressart.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<KatalogEintrag> daten = katalog.stream().map(dtoMapper).sorted(dataComparator).collect(Collectors.toList());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse("Die ID der gesuchten Beshäftigungart darf nicht null sein.");
		DTOKatalogAdressart art = conn.queryByKey(DTOKatalogAdressart.class, id);
		if (art == null)
			return OperationError.NOT_FOUND.getResponse("Die Beschäftigungsart mit der ID " + id + " existiert nicht.");
		KatalogEintrag eintrag = dtoMapper.apply(art);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(eintrag).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		if (id == null)
            return OperationError.NOT_FOUND.getResponse("Die Id der zu ändernden Beshäftigungart darf nicht null sein.");
        Map<String, Object> map = JSONMapper.toMap(is);
        if (map.size() > 0){
            try{
                conn.transactionBegin();
                DTOKatalogAdressart art = conn.queryByKey(DTOKatalogAdressart.class, id);
                if (art == null)
                    return OperationError.NOT_FOUND.getResponse("Die Beschäftigungsart mit der ID"+id+" existiert nicht.");
                for (Entry<String, Object> entry : map.entrySet()){
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    switch (key){
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
								throw OperationError.BAD_REQUEST.exception();
						}
                        case "text" -> art.Bezeichnung = JSONMapper.convertToString(value, true, true);
						case "istSichtbar" -> art.Sichtbar = JSONMapper.convertToBoolean(value, true);
						case "istAenderbar" -> art.Aenderbar = JSONMapper.convertToBoolean(value, true);
                       	default -> throw OperationError.BAD_REQUEST.exception();
                    }
                }
                conn.transactionPersist(art);
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
