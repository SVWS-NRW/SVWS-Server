package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.types.schule.Schulform;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.utils.OperationError;
import de.nrw.schule.svws.db.utils.data.Schule;
import de.nrw.schule.svws.db.utils.gost.FaecherGost;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostFach}.
 */
public class DataGostFaecher extends DataManager<Long> {

	private Integer abijahr;
	
	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostFach}.
	 * 
	 * @param conn      die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahr   der Abi-Jahrgang, für welchen die Gost-Fächer verwaltet werden sollen,
	 *                  null für die allgemeinen Jahrgangsübergreifenden Gost-Fachinformationen 
	 */
	public DataGostFaecher(DBEntityManager conn, Integer abijahr) {
		super(conn);
		this.abijahr = abijahr;
	}

	/**
	 * Bestimmt die Liste der Fächer der gymnasialen Oberstufe für den 
	 * spezifizierten Abiturjahrgang.
	 * 
	 * @return der Manager für die Liste der Fächer der gymnasialen Oberstufe
	 */
	public GostFaecherManager getListInternal() {
		Schulform schulform = Schule.queryCached(conn).getSchulform();
    	if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
    		return null;
    	return FaecherGost.getFaecherListeGost(conn, abijahr);
	}
	
	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		Collection<GostFach> daten = getListInternal().faecher();
    	if (daten == null)
    		return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(Long id) {
		GostUtils.pruefeSchuleMitGOSt(conn);
    	Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
    	if (faecher == null)
    		return OperationError.NOT_FOUND.getResponse();
		DTOFach fach = faecher.get(id); 
    	if (fach == null)
    		return OperationError.NOT_FOUND.getResponse();
		GostFach daten = null;
		if (abijahr == null) {
	    	daten = FaecherGost.mapFromDTOFach(fach, faecher);
		} else {
	    	// TODO Prüfe, ob der Abiturjahrgang abiturjahr gültig ist oder nicht
	    	DTOGostJahrgangFaecher jf = conn.queryByKey(DTOGostJahrgangFaecher.class, abijahr, id);
	    	if (jf == null)
	    		return OperationError.NOT_FOUND.getResponse();
	    	daten = FaecherGost.mapFromDTOGostJahrgangFaecher(jf, faecher);
		}
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
    		try {
    			conn.transactionBegin();
    			GostUtils.pruefeSchuleMitGOSt(conn);
    	    	Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
    	    	if (faecher == null)
    	    		return OperationError.NOT_FOUND.getResponse();
    			DTOFach fach = faecher.get(id);
		    	if (fach == null)
    	    		return OperationError.NOT_FOUND.getResponse();
    			if (abijahr == null) {
			    	for (Entry<String, Object> entry : map.entrySet()) {
			    		String key = entry.getKey();
			    		Object value = entry.getValue();
			    		switch (key) {
							case "id" -> {
								Long patch_id = JSONMapper.convertToLong(value, true);
								if ((patch_id == null) || (patch_id != id))
									throw OperationError.BAD_REQUEST.exception();
							}
			    			// Änderungen von allgemeinen Fachinformationen sind hier nicht erlaubt, nur GOSt-spezifische
			    			case "kuerzel" -> throw OperationError.BAD_REQUEST.exception();
			    			case "kuerzelAnzeige" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "bezeichnung" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "sortierung" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "istFremdsprache" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "istFremdSpracheNeuEinsetzend" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "biliSprache" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "istMoeglichAbiLK" -> fach.IstMoeglichAbiLK = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichAbiGK" -> fach.IstMoeglichAbiGK = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichEF1" -> fach.IstMoeglichEF1 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichEF2" -> fach.IstMoeglichEF2 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichQ11" -> fach.IstMoeglichQ11 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichQ12" -> fach.IstMoeglichQ12 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichQ21" -> fach.IstMoeglichQ21 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichQ22" -> fach.IstMoeglichQ22 = JSONMapper.convertToBoolean(value, false);
			    			case "wochenstundenEF1" -> throw OperationError.BAD_REQUEST.exception();  // TODO derzeit nicht unterstützt
			    			case "wochenstundenEF2" -> throw OperationError.BAD_REQUEST.exception();  // TODO derzeit nicht unterstützt
			    			case "wochenstundenQualifikationsphase" -> {
			    				// TODO Prüfe, ob die Wochenstunden bei dem Fach gesetzt werden dürfen (z.B. PJK) sonst: throw OperationError.BAD_REQUEST.exception();  
			    				fach.WochenstundenQualifikationsphase = JSONMapper.convertToInteger(value, false);
			    			}
			    			case "mussSchriftlichEF1" -> fach.MussSchriftlichEF1 = JSONMapper.convertToBoolean(value, false);
			    			case "mussSchriftlichEF2" -> fach.MussSchriftlichEF2 = JSONMapper.convertToBoolean(value, false);
			    			case "projektKursLeitfach1ID" -> {
			    				fach.ProjektKursLeitfach1_ID = JSONMapper.convertToLong(value, false);
			    		    	if ((fach.ProjektKursLeitfach1_ID != null) && (fach.ProjektKursLeitfach1_ID < 0))
			    		    		throw OperationError.CONFLICT.exception();    	
		    			    	if (faecher.get(fach.ProjektKursLeitfach1_ID) == null)
		    			    		throw OperationError.NOT_FOUND.exception();
			    			}
			    			case "projektKursLeitfach2ID" -> {
			    				fach.ProjektKursLeitfach2_ID = JSONMapper.convertToLong(value, false);
			    		    	if ((fach.ProjektKursLeitfach2_ID != null) && (fach.ProjektKursLeitfach2_ID < 0))
			    		    		throw OperationError.CONFLICT.exception();    	
		    			    	if (faecher.get(fach.ProjektKursLeitfach2_ID) == null)
		    			    		throw OperationError.NOT_FOUND.exception();
			    			}
			    			case "projektKursLeitfach1Kuerzel" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "projektKursLeitfach2Kuerzel" -> throw OperationError.BAD_REQUEST.exception();  
			    			default -> throw OperationError.BAD_REQUEST.exception();
			    		}
			    	}
			    	conn.transactionPersist(fach);
		    	} else {
	    	    	// TODO Prüfe, ob der Abiturjahrgang abiturjahr gültig ist oder nicht
	    	    	DTOGostJahrgangFaecher jf = conn.queryByKey(DTOGostJahrgangFaecher.class, abijahr, id);
	    	    	if (jf == null)
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
			    			// Änderungen von allgemeinen Fachinformationen sind hier nicht erlaubt, nur GOSt-spezifische
			    			case "kuerzel" -> throw OperationError.BAD_REQUEST.exception();
			    			case "kuerzelAnzeige" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "bezeichnung" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "sortierung" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "istFremdsprache" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "istFremdSpracheNeuEinsetzend" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "biliSprache" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "istMoeglichAbiLK" -> jf.WaehlbarAbiLK = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichAbiGK" -> jf.WaehlbarAbiGK = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichEF1" -> jf.WaehlbarEF1 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichEF2" -> jf.WaehlbarEF2 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichQ11" -> jf.WaehlbarQ11 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichQ12" -> jf.WaehlbarQ12 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichQ21" -> jf.WaehlbarQ21 = JSONMapper.convertToBoolean(value, false);
			    			case "istMoeglichQ22" -> jf.WaehlbarQ22 = JSONMapper.convertToBoolean(value, false);
			    			case "wochenstundenEF1" -> throw OperationError.BAD_REQUEST.exception();  // TODO derzeit nicht unterstützt
			    			case "wochenstundenEF2" -> throw OperationError.BAD_REQUEST.exception();  // TODO derzeit nicht unterstützt
			    			case "wochenstundenQualifikationsphase" -> {
			    				// TODO Prüfe, ob die Wochenstunden bei dem Fach gesetzt werden dürfen (z.B. PJK) sonst: throw OperationError.BAD_REQUEST.exception();  
			    				jf.WochenstundenQPhase = JSONMapper.convertToInteger(value, false);
			    			}
			    			case "mussSchriftlichEF1" -> jf.SchiftlichkeitEF1 = JSONMapper.convertToBoolean(value, false) ? "J" : "N";
			    			case "mussSchriftlichEF2" -> jf.SchiftlichkeitEF2 = JSONMapper.convertToBoolean(value, false) ? "J" : "N";
			    			case "projektKursLeitfach1ID" -> throw OperationError.BAD_REQUEST.exception();
			    			case "projektKursLeitfach2ID" -> throw OperationError.BAD_REQUEST.exception();
			    			case "projektKursLeitfach1Kuerzel" -> throw OperationError.BAD_REQUEST.exception();  
			    			case "projektKursLeitfach2Kuerzel" -> throw OperationError.BAD_REQUEST.exception();  
			    			default -> throw OperationError.BAD_REQUEST.exception();
			    		}
			    	}
			    	conn.transactionPersist(jf);
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
		}
        return Response.status(Status.OK).build();
	}

}
