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
import de.nrw.schule.svws.core.types.fach.ZulaessigesFach;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
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
	public static Function<DTOGostBlockungKurs, GostBlockungKurs> dtoMapper = (DTOGostBlockungKurs kurs) -> {
		GostBlockungKurs daten = new GostBlockungKurs();
		daten.id = kurs.ID;
		daten.fach_id = kurs.Fach_ID;
		daten.kursart = kurs.Kursart.id;
		daten.nummer = kurs.Kursnummer;
		daten.istKoopKurs = kurs.IstKoopKurs;
		daten.suffix = kurs.BezeichnungSuffix == null ? "" : kurs.BezeichnungSuffix;
		daten.anzahlSchienen = kurs.Schienenanzahl;
		daten.wochenstunden = kurs.Wochenstunden;
		return daten;
	};
	

	@Override
	public Response get(Long id) {
		GostUtils.pruefeSchuleMitGOSt(conn);
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
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme den Kurs der Blockung
			DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, id);
			if (kurs == null)
				return OperationError.NOT_FOUND.getResponse();
	        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
	        DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, kurs.Blockung_ID);
	        DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
	        if (vorlage == null)
	        	throw OperationError.BAD_REQUEST.exception("Der Kurs kann nicht angepasst werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");			
	    	for (Entry<String, Object> entry : map.entrySet()) {
	    		String key = entry.getKey();
	    		Object value = entry.getValue();
	    		switch (key) {
					case "id" -> {
						Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
	    			case "fach_id" -> {
						Long patch_fach_id = JSONMapper.convertToLong(value, true);
						if ((patch_fach_id == null) || (patch_fach_id.longValue() != kurs.Fach_ID.longValue()))
							throw OperationError.BAD_REQUEST.exception();
	    			}
	    			case "kursart" -> {
	    				Integer patch_kursart = JSONMapper.convertToInteger(value, true);
						if ((patch_kursart == null) || (patch_kursart.intValue() != kurs.Kursart.id))
							throw OperationError.BAD_REQUEST.exception();
	    			}
	    			case "nummer" -> {
	    				Integer patch_nummer = JSONMapper.convertToInteger(value, true);
						if ((patch_nummer == null) || (patch_nummer.intValue() != kurs.Kursnummer.intValue()))
							throw OperationError.BAD_REQUEST.exception();
	    			}
	    			case "istKoopKurs" -> kurs.IstKoopKurs = JSONMapper.convertToBoolean(value, false);
	    			case "suffix" -> kurs.BezeichnungSuffix = JSONMapper.convertToString(value, false, true);
	    			case "anzahlSchienen" -> {
	    			    int schienenAnzahl = JSONMapper.convertToInteger(value, false);
	    			    if (schienenAnzahl > kurs.Schienenanzahl) {
	    			        // TODO lege den Kurs in zusätzlichen Schienen bei dem Vorlagen-Ergebnis an
	    			    } else if (schienenAnzahl < kurs.Schienenanzahl) {
	    			        // TODO entferne den Kurs in den Schienen im Vorlage-Ergebnis, wo er zu viel enthalten ist
	    			    }
	    			}
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
     * Fügt einen weiteren Kurses zu einer Blockung der Gymnasialen Oberstufe hinzu
	 * 
     * @param idBlockung   die ID der Blockung
     * @param idFach       die ID des Faches
     * @param idKursart    die ID der Kursart
	 * 
	 * @return Eine Response mit der ID des neuen Kurses der Blockung 
	 */
	public Response addKurs(long idBlockung, long idFach, int idKursart) {
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
	        	throw OperationError.BAD_REQUEST.exception("Der Kurs kann nicht hinzugefügt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
			// Bestimme das Fach und prüfe, ob es ein Fach der gymnasialen Oberstufe ist
			DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
			if (fach == null)
				throw OperationError.NOT_FOUND.exception();
			if ((fach.StatistikFach == ZulaessigesFach.VF) || (!fach.IstOberstufenFach))
				throw OperationError.CONFLICT.exception();
			// Bestimme die Kursart
			GostKursart kursart = GostKursart.fromID(idKursart);
			if (kursart == GostKursart.GK)
				kursart = (fach.StatistikFach == ZulaessigesFach.VX) 
					? GostKursart.VTF
					: (fach.StatistikFach == ZulaessigesFach.PX) ? GostKursart.PJK : GostKursart.GK;
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			DTODBAutoInkremente dbKurseID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Kurse");
			long idKurs = dbKurseID == null ? 1 : dbKurseID.MaxID + 1;
			// Ermittle, ob bereits Kurse mit für das Fach und die Kursart existieren
	    	String jpql = "SELECT e FROM DTOGostBlockungKurs e WHERE e.Blockung_ID = ?1 and e.Fach_ID = ?2 and e.Kursart = ?3";
	    	List<DTOGostBlockungKurs> kurse = conn.queryList(jpql, DTOGostBlockungKurs.class, idBlockung, idFach, kursart);
	    	int kursnummer = 1;
	    	if ((kurse != null) && (kurse.size() > 0)) { // Bestimme die erste freie Kursnummer
	    		Set<Integer> kursIDs = kurse.stream().map(e -> e.Kursnummer).collect(Collectors.toSet());
	    		while (kursIDs.contains(kursnummer))
	    			kursnummer++;
	    	}
	    	DTOGostBlockungKurs kurs = null;
	    	if (kursart == GostKursart.LK) {
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, 5);	    		
	    	} else if (kursart == GostKursart.GK) {
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, fach.IstMoeglichAlsNeueFremdspracheInSekII ? 4 : 3);
	    	} else if (kursart == GostKursart.PJK) {
	    		// TODO Wochenstunden anhand der Tabelle GostFaecher bestimmen (PJK)
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, 3);
	    	} else if (kursart == GostKursart.VTF) {
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, 2);	    		
	    	} else if (kursart == GostKursart.ZK) {
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, 3);	    		
	    	}
			conn.transactionPersist(kurs);
			// Füge den Kurs in die erste Schiene des Zwischenergebnisses ein
			List<DTOGostBlockungSchiene> schienen = conn.queryList("SELECT e FROM DTOGostBlockungSchiene e WHERE e.Blockung_ID = ?1 AND e.Nummer = ?2", DTOGostBlockungSchiene.class, blockung.ID, 1);
			if (schienen.size() != 1)
				throw OperationError.INTERNAL_SERVER_ERROR.exception();
			DTOGostBlockungZwischenergebnisKursSchiene ks = new DTOGostBlockungZwischenergebnisKursSchiene(vorlage.ID, idKurs, schienen.get(0).ID);
			conn.transactionPersist(ks);
			conn.transactionCommit();
			GostBlockungKurs daten = dtoMapper.apply(kurs);
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
     * Entfernt einen Kurs des angegebenen Faches und Kursart bei einer Blockung der 
     * Gymnasialen Oberstufe.
	 * 
     * @param idBlockung   die ID der Blockung
     * @param idFach       die ID des Faches
     * @param idKursart    die ID der Kursart
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt. 
	 */
	public Response deleteKurs(long idBlockung, long idFach, int idKursart) {
		try {
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
	        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
	        DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
	        DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
	        if (vorlage == null)
	        	throw OperationError.BAD_REQUEST.exception("Der Kurs kann nicht entfernt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
			// Bestimme das Fach und prüfe, ob es ein Fach der gymnasialen Oberstufe ist
			DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
			if (fach == null)
				throw OperationError.NOT_FOUND.exception();
			if ((fach.StatistikFach == ZulaessigesFach.VF) || (!fach.IstOberstufenFach))
				throw OperationError.CONFLICT.exception();
			// Bestimme die Kursart
			GostKursart kursart = GostKursart.fromID(idKursart);
			if (kursart == GostKursart.GK)
				kursart = (fach.StatistikFach == ZulaessigesFach.VX) 
					? GostKursart.VTF
					: (fach.StatistikFach == ZulaessigesFach.PX) ? GostKursart.PJK : GostKursart.GK;
			// Bestimme die Kurse der Blockung, welche das Kriterium erfüllen und löschen Kurs mit der höchsten Kursnummer
	    	String jpql = "SELECT e FROM DTOGostBlockungKurs e WHERE e.Blockung_ID = ?1 and e.Fach_ID = ?2 and e.Kursart = ?3";
	    	List<DTOGostBlockungKurs> kurse = conn.queryList(jpql, DTOGostBlockungKurs.class, idBlockung, idFach, kursart);
	    	if ((kurse == null) || (kurse.size() == 0))
	    		throw OperationError.NOT_FOUND.exception();
	    	DTOGostBlockungKurs kurs = kurse.stream().max((a,b) -> Integer.compare(a.Kursnummer, b.Kursnummer)).get();
			GostBlockungKurs daten = dtoMapper.apply(kurs);
			if (!conn.transactionRemove(kurs))
				throw OperationError.INTERNAL_SERVER_ERROR.exception();
			conn.transactionCommit();
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
			GostUtils.pruefeSchuleMitGOSt(conn);
			DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, id);
			if (kurs == null)
				return OperationError.NOT_FOUND.getResponse();
	        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
	        DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, kurs.Blockung_ID);
	        DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
	        if (vorlage == null)
	        	throw OperationError.BAD_REQUEST.exception("Der Kurs kann nicht entfernt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
			// Entferne den Kurs
			GostBlockungKurs daten = dtoMapper.apply(kurs);
			conn.transactionRemove(kurs); // TODO BACHRAN muss hier nicht bei false eine Exception geworfen werden? 
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
