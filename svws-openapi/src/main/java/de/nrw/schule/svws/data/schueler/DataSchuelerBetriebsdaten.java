package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.schueler.SchuelerBetriebsdaten;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOAnsprechpartnerAllgemeineAdresse;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOKatalogAllgemeineAdresse;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrer;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerAllgemeineAdresse;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerBetriebsdaten}.
 */
public class DataSchuelerBetriebsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerBetriebsdaten}.
	 * 
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerBetriebsdaten(DBEntityManager conn) {
		super(conn);
	}	


	/**
	 * Lambda-Ausdruck zum Umwandeln des ersten Erziehers eines Datenbank-DTOs {@link DTOSchuelerAllgemeineAdresse} 
	 * in einen Core-DTO {@link SchuelerBetriebsdaten}.  
	 */
	 private Function<DTOSchuelerAllgemeineAdresse, SchuelerBetriebsdaten> dtoMapper =(DTOSchuelerAllgemeineAdresse e) -> {
		 SchuelerBetriebsdaten eintrag = new SchuelerBetriebsdaten();
		 eintrag.id = e.ID;
		 eintrag.schueler_id = e.Schueler_ID;
		 eintrag.betrieb_id = e.Adresse_ID;
		 eintrag.beschaeftigungsart_id = e.Vertragsart_ID;
		 eintrag.vertragsbeginn = e.Vertragsbeginn;
		 eintrag.vertragsende = e.Vertragsende;
		 eintrag.ausbilder = e.Ausbilder;
		 eintrag.allgadranschreiben = e.AllgAdrAnschreiben;
		 eintrag.praktikum = e.Praktikum;
		 eintrag.sortierung = e.Sortierung;
		 eintrag.ansprechpartner_id = e.Ansprechpartner_ID;
		 eintrag.betreuungslehrer_id = e.Betreuungslehrer_ID;
		 return eintrag;
	 };
	
	 @Override
	 public Response getAll() {
		 List<DTOSchuelerAllgemeineAdresse> betriebe = conn.queryAll(DTOSchuelerAllgemeineAdresse.class);
		 if (betriebe == null)
			 return OperationError.NOT_FOUND.getResponse();
		 List<SchuelerBetriebsdaten> daten = betriebe.stream().map(dtoMapper).collect(Collectors.toList());
		 return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	 }
 
	 @Override
	 public Response getList() {
		throw new UnsupportedOperationException();
	 }
	
	@Override
	public Response get(Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse("Die erwartete ID zur Anfrage ist nicht vorhanden.");
		DTOSchuelerAllgemeineAdresse schuelerbetrieb = conn.queryByKey(DTOSchuelerAllgemeineAdresse.class, id);
		if ( schuelerbetrieb == null)
			return OperationError.NOT_FOUND.getResponse("Es existiert mit der ID kein Schülerbetrieb.");
		SchuelerBetriebsdaten daten = dtoMapper.apply(schuelerbetrieb);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0){
			try{
				conn.transactionBegin();
				DTOSchuelerAllgemeineAdresse s_betrieb = conn.queryByKey(DTOSchuelerAllgemeineAdresse.class, id);
				if (s_betrieb == null)
					return OperationError.NOT_FOUND.getResponse();
				for (Entry<String, Object> entry : map.entrySet()){
					String key = entry.getKey();
					Object value = entry.getValue();
					switch (key){
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.intValue() != id))
								throw OperationError.BAD_REQUEST.exception();
						}
						case "schueler_id" -> {
							Long schueler_id = JSONMapper.convertToLong(value, true);
							if (schueler_id == null) {	//TODO Darf eine Beschäftigung ohne Betrieb angeleget werden?
								throw OperationError.BAD_REQUEST.exception("SchülerID darf nicht fehlen.");
							} else {
								DTOSchueler schueler = conn.queryByKey(DTOSchueler.class,schueler_id);
								if (schueler == null)
									throw OperationError.NOT_FOUND.exception("Schüler mit der ID " + schueler_id + " wurde nicht gefunden.");
								s_betrieb.Schueler_ID = schueler_id;
							}
						}	
						case "betrieb_id" -> {
							
							Long betrieb_id = JSONMapper.convertToLong(value, true);
							if (betrieb_id == null) {	//TODO Darf eine Beschäftigung ohne Betrieb angeleget werden?
								s_betrieb.Adresse_ID = null;
							} else {
								DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, betrieb_id);
								if (betrieb == null)
									throw OperationError.NOT_FOUND.exception("Betrieb mit der ID " + betrieb_id + " wurde nicht gefunden.");
								s_betrieb.Adresse_ID = betrieb_id;
							}
						}
						case "beschaeftigungsart_id" -> {
							Long art_id = JSONMapper.convertToLong(value, true);
							if (art_id == null) {	//TODO Darf eine Beschäftigung ohne Art angeleget werden?
								s_betrieb.Vertragsart_ID = null;
							} else {
								DTOBeschaeftigungsart b_art = conn.queryByKey(DTOBeschaeftigungsart.class, art_id);
								if (b_art == null)
									throw OperationError.NOT_FOUND.exception("Beschäftigungsart mit der ID " + art_id + " wurde nicht gefunden.");
								s_betrieb.Vertragsart_ID = art_id;
							}
						}
						case "vertragsbeginn" -> s_betrieb.Vertragsbeginn = JSONMapper.convertToString(value, true, true);
						case "vertragsende" -> s_betrieb.Vertragsende = JSONMapper.convertToString(value, true, true);
						case "ausbilder" -> s_betrieb.Ausbilder = JSONMapper.convertToString(value, true, true);
						case "allgadranschreiben" -> s_betrieb.AllgAdrAnschreiben = JSONMapper.convertToBoolean(value, true);
						case "praktikum" -> s_betrieb.Praktikum = JSONMapper.convertToBoolean(value, true);
						case "sortierung" -> s_betrieb.Sortierung = JSONMapper.convertToInteger(value, true);
						case "ansprechpartner_id" -> {
							Long a_id = JSONMapper.convertToLong(value, true);
							if (a_id == null) {	//TODO Darf eine Beschäftigung ohne Ansprechpartner angeleget werden?
								s_betrieb.Ansprechpartner_ID = null;
							} else {
								DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = conn.queryByKey(DTOAnsprechpartnerAllgemeineAdresse.class, a_id);
								if (ansprechpartner == null)
									throw OperationError.NOT_FOUND.exception();
								s_betrieb.Ansprechpartner_ID = a_id;
							}
						}
						case "betreuungslehrer_id" -> {
							Long lehrer_id = JSONMapper.convertToLong(value, true);
							if (lehrer_id == null) {	//TODO Darf eine Beschäftigung ohne Betreuungslehrer angeleget werden?
								s_betrieb.Betreuungslehrer_ID = null;
							} else {
								DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, lehrer_id);
								if (lehrer == null)
									throw OperationError.NOT_FOUND.exception();
								s_betrieb.Betreuungslehrer_ID = lehrer_id;
							}
						}
						default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(s_betrieb);
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
	 * Ermittelt eine Liste der {@link SchuelerBetriebsdaten} für den Schüler mit der angegebenen ID.
	 * 
	 * @param schuelerID   die ID des Schülers, dessen {@link SchuelerBetriebsdaten	} ermittelt werden sollen 
	 * 
	 * @return eine Liste mit den {@link SchuelerBetriebsdaten} für den Schüler mit der angegebenen ID 
	 */
	public Response getListFromSchueler(long schuelerID) {
    	List<DTOSchuelerAllgemeineAdresse> betriebe = conn.queryNamed("DTOSchuelerAllgemeineAdresse.schueler_id", schuelerID, DTOSchuelerAllgemeineAdresse.class);
    	if (betriebe == null)
    		return OperationError.NOT_FOUND.getResponse();
        List<SchuelerBetriebsdaten> daten = betriebe.stream().map(dtoMapper).toList();
        /*
        // TODO Isak später bitte entfernen
        // Bestimme die Namen aller Betriebe, die bei den Schüler-Betriebsdaten vorkommen
        List<Long> betriebIDs = daten.stream().filter(sbd -> sbd.betrieb_id != null).map(sbd -> sbd.betrieb_id).toList();
        Map<Long, String> mapBetriebeIDName = (betriebIDs.size() <= 0) ? new HashMap<>() : 
        	conn.queryNamed("DTOKatalogAllgemeineAdresse.id.multiple", betriebIDs, DTOKatalogAllgemeineAdresse.class).stream()
        		.collect(Collectors.toMap(b -> b.ID, b -> b.name1));
        // Bestimme die Namen aller Ansprechpartner, die bei den Schüler-Betriebsdaten vorkommen
        List<Long> ansprechpartnerIDs = daten.stream().filter(sbd -> sbd.ansprechpartner_id != null).map(sbd -> sbd.ansprechpartner_id).toList(); 
        Map<Long, String> mapAnsprechpartnerIDName = (ansprechpartnerIDs.size() <= 0) ? new HashMap<>() : 
        	conn.queryNamed("DTOAnsprechpartnerAllgemeineAdresse.id.multiple", ansprechpartnerIDs, DTOAnsprechpartnerAllgemeineAdresse.class).stream()
        		.collect(Collectors.toMap(b -> b.ID, b -> b.Name));
        // Bestimme die Namen aller Betreuungslehrer, die bei den Schüler-Betriebsdaten vorkommen
        List<Long> betreuungslehrerIDs = daten.stream().filter(sbd -> sbd.betreuungslehrer_id != null).map(sbd -> sbd.betreuungslehrer_id).toList(); 
        Map<Long, String> mapBetreuungslehrerIDName = (betreuungslehrerIDs.size() <= 0) ? new HashMap<>() :
        	conn.queryNamed("DTOLehrer.id.multiple", betreuungslehrerIDs, DTOLehrer.class).stream()
        		.collect(Collectors.toMap(b -> b.ID, b -> b.Nachname));
        // Setze nun alle Namen, welche für die obigen IDs bestimmt wurden
        for (SchuelerBetriebsdaten sbd : daten) {
        	if (sbd.id != null)        		
        		sbd.betrieb_name = mapBetriebeIDName.get(sbd.betrieb_id);
        	if (sbd.ansprechpartner_id != null)
        		sbd.ansprechpartner_name = mapAnsprechpartnerIDName.get(sbd.ansprechpartner_id);
        	if (sbd.betreuungslehrer_id != null)
        		sbd.betreuungslehrer_name = mapBetreuungslehrerIDName.get(sbd.betreuungslehrer_id);
        }
        
        */
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}
	
	/** 
	 * Erstellt einen neuen Schülerbetrieb  * 
	 * @param schueler_id 	ID des Schülers, für den ein Schülerbetrieb erstellt wird.
	 * @param betrieb_id		ID des Betriebs
	 * @param is						Das JSON-Objekt mit den Daten
	* 
	 * @return Eine Response mit dem neuen Schülerbetrieb 
	 */
	public Response create(long schueler_id, long betrieb_id, InputStream is) {
		DTOSchuelerAllgemeineAdresse s_betrieb = null;
		Map<String, Object> map = JSONMapper.toMap(is);
			if(map.size() > 0) {
				try {
					conn.transactionBegin();
					
					DTOSchueler schueler = conn.queryByKey(DTOSchueler.class,schueler_id);
					if (schueler == null)
						throw OperationError.NOT_FOUND.exception("Schüler mit der ID " + schueler_id + " wurde nicht gefunden.");
					
					DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, betrieb_id);
					if (betrieb == null)
						throw OperationError.NOT_FOUND.exception("Betrieb mit der ID " + betrieb_id + " wurde nicht gefunden.");
					
					// Bestimme die ID des neuen Ansprechpartners
					DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Schueler_AllgAdr");
					Long ID = lastID == null ? 1 : lastID.MaxID + 1;
					
					
					//Schülerbetrieb anlegen
					// TODO Man braucht einen weiteren Konstruktor für die Klasse DTOSchuelerAllgemeineAdresse ohne Argumente
					s_betrieb = new DTOSchuelerAllgemeineAdresse(ID, schueler_id, betrieb_id);
					
					for(Entry<String, Object> entry : map.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						switch(key) {
							
						case "id" -> {
							Long create_id = JSONMapper.convertToLong(value, true);
							if ( create_id != null )
								throw OperationError.BAD_REQUEST.exception("ID muss leer sein.");
						}
						case "schueler_id" -> {
							Long sid = JSONMapper.convertToLong(value, true);
							if (sid == null)
								throw OperationError.BAD_REQUEST.exception("SchülerID darf nicht fehlen.");
							if (sid != schueler_id)
								throw OperationError.BAD_REQUEST.exception("SchülerID aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
						}	
						case "betrieb_id" -> {
							
							Long bid = JSONMapper.convertToLong(value, true);
							if (bid == null) 	
								s_betrieb.Adresse_ID = null;
							if(bid != betrieb_id)
								throw OperationError.BAD_REQUEST.exception("Betrieb-ID aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
						}
						case "beschaeftigungsart_id" -> {
							Long art_id = JSONMapper.convertToLong(value, true);
							if (art_id == null) {	//TODO Darf eine Beschäftigung ohne Art angeleget werden?
								s_betrieb.Vertragsart_ID = null;
							} else {
								DTOBeschaeftigungsart b_art = conn.queryByKey(DTOBeschaeftigungsart.class, art_id);
								if (b_art == null)
									throw OperationError.NOT_FOUND.exception("Beschäftigungsart mit der ID " + art_id + " wurde nicht gefunden.");
								s_betrieb.Vertragsart_ID = art_id;
							}
						}
						case "vertragsbeginn" -> s_betrieb.Vertragsbeginn = JSONMapper.convertToString(value, true, true);
						case "vertragsende" -> s_betrieb.Vertragsende = JSONMapper.convertToString(value, true, true);
						case "ausbilder" -> s_betrieb.Ausbilder = JSONMapper.convertToString(value, true, true);
						case "allgadranschreiben" -> s_betrieb.AllgAdrAnschreiben = JSONMapper.convertToBoolean(value, true);
						case "praktikum" -> s_betrieb.Praktikum = JSONMapper.convertToBoolean(value, true);
						case "sortierung" -> s_betrieb.Sortierung = JSONMapper.convertToInteger(value, true);
						case "ansprechpartner_id" -> {
							Long a_id = JSONMapper.convertToLong(value, true);
							if (a_id == null) {	//TODO Darf eine Beschäftigung ohne Ansprechpartner angeleget werden?
								s_betrieb.Ansprechpartner_ID = null;
							} else {
								DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = conn.queryByKey(DTOAnsprechpartnerAllgemeineAdresse.class, a_id);
								if (ansprechpartner == null)
									throw OperationError.NOT_FOUND.exception();
								s_betrieb.Ansprechpartner_ID = a_id;
							}
						}
						case "betreuungslehrer_id" -> {
							Long lehrer_id = JSONMapper.convertToLong(value, true);
							if (lehrer_id == null) {	//TODO Darf eine Beschäftigung ohne Betreuungslehrer angeleget werden?
								s_betrieb.Betreuungslehrer_ID = null;
							} else {
								DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, lehrer_id);
								if (lehrer == null)
									throw OperationError.NOT_FOUND.exception();
								s_betrieb.Betreuungslehrer_ID = lehrer_id;
							}
						}
						default -> throw OperationError.BAD_REQUEST.exception();
						}
					}
					conn.transactionPersist(s_betrieb);
					if( !conn.transactionCommit())
						return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Betriebansprechpartners");
				}catch (Exception e) {
					if(e instanceof WebApplicationException webApplicationException)
						return webApplicationException.getResponse();
					return OperationError.INTERNAL_SERVER_ERROR.getResponse();
				}finally {
					conn.transactionRollback();
				}
			}
			SchuelerBetriebsdaten daten = dtoMapper.apply(s_betrieb);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
			
	}

}

