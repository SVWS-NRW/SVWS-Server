package de.nrw.schule.svws.data.betriebe;

import java.io.InputStream;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.nrw.schule.svws.core.data.betrieb.BetriebAnsprechpartner;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.data.JSONMapper;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOAnsprechpartnerAllgemeineAdresse;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOKatalogAllgemeineAdresse;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BetriebAnsprechpartner}.
 */

public class DataBetriebAnsprechpartner  extends DataManager<Long>{

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BetriebAnsprechpartner}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBetriebAnsprechpartner(DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOAnsprechpartnerAllgemeineAdresse}
	 */
	private Function<DTOAnsprechpartnerAllgemeineAdresse, BetriebAnsprechpartner> dtoMapper  = (DTOAnsprechpartnerAllgemeineAdresse k) -> {
		BetriebAnsprechpartner eintrag = new BetriebAnsprechpartner();
		eintrag.id = k.ID;
		eintrag.betrieb_id = k.Adresse_ID;
		eintrag.titel = k.Titel;
		eintrag.anrede = k.Anrede;
		eintrag.name = k.Name;
		eintrag.vorname = k.Vorname;
		eintrag.email = k.Email;
		eintrag.telefon = k.Telefon;
		eintrag.abteilung = k.Abteilung;
		eintrag.GU_ID = k.GU_ID;
		return eintrag;
	};
	
	@Override
	public Response getAll() {
		List<DTOAnsprechpartnerAllgemeineAdresse> katalog = conn.queryAll(DTOAnsprechpartnerAllgemeineAdresse.class);
		if(katalog == null)
			return OperationError.NOT_FOUND.getResponse();
		List<BetriebAnsprechpartner> daten = katalog.stream().map(dtoMapper).collect(Collectors.toList());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		// TODO Auto-generated method stub
		return this.getAll();
	}

	@Override
	public Response get(Long id) {
		if ( id == null )
				return OperationError.NOT_FOUND.getResponse("Die Methode erwartet einen Wert für id. Sie ist nicht übergeben!!");
		DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = conn.queryByKey(DTOAnsprechpartnerAllgemeineAdresse.class , id);
		BetriebAnsprechpartner daten = dtoMapper.apply(ansprechpartner);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		Map<String, Object> map = JSONMapper.toMap(is);
		if(map.size() > 0) {
			try {
				conn.transactionBegin();
				DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = conn.queryByKey(DTOAnsprechpartnerAllgemeineAdresse.class, id);
				if ( ansprechpartner == null)
					throw OperationError.NOT_FOUND.exception();
				for(Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					switch(key) {
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true) ;
							if( (patch_id == null) || (patch_id.intValue() != id.intValue()))
								throw OperationError.BAD_REQUEST.exception();
						}
						case "betrieb_id" -> {
							Long betrieb_id = JSONMapper.convertToLong( value , true );
							if (betrieb_id == null) {
								ansprechpartner.Adresse_ID = null;
							} else {
								DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, betrieb_id);
								if( betrieb == null)
									throw OperationError.NOT_FOUND.exception();
								ansprechpartner.Adresse_ID = betrieb.ID;
							}
						}
						case "titel" -> ansprechpartner.Titel = JSONMapper.convertToString(value, true, true);
						case "anrede" -> ansprechpartner.Anrede = JSONMapper.convertToString(value, true, true);
						case "name" -> ansprechpartner.Name = JSONMapper.convertToString(value, true, true);
						case "vorname" -> ansprechpartner.Vorname = JSONMapper.convertToString(value, true, true);
						case "email" -> ansprechpartner.Email = JSONMapper.convertToString(value, true, true);
						case "telefon" -> ansprechpartner.Telefon = JSONMapper.convertToString(value, true, true);
						case "abteilung" -> ansprechpartner.Abteilung= JSONMapper.convertToString(value, true, true);
						case "GU_ID" -> ansprechpartner.GU_ID= JSONMapper.convertToString(value, true, true);
						default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(ansprechpartner);
				if (!conn.transactionCommit())
					return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Betriebansprechpartners");
			} catch (Exception e) {
				if(e instanceof WebApplicationException webApplicationException)
					return webApplicationException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
			} finally {
				conn.transactionRollback();
			}
		}
		return Response.status(Status.OK).build();
	}
	
	/** 
	 * Erstellt eine neue Blockung auf Basis der aktuellen Fachwahlen, dem angegeben Namen der neuen Blockung und der
	 * Anzahl der Schienen mit Vorgabe-Werten.
	 * 
	 * @param  betrieb_id  ID des Betriebs
	 * @param  is					JSON-Objekt mit den Daten
	 * @return Eine Response mit dem neuen Ansprechpartner 
	 */
	public Response create(Long betrieb_id, InputStream is) {
		DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = null;
		if (betrieb_id == null)
		    throw OperationError.NOT_FOUND.exception("Parameter betrieb_id darf nicht leer sein.");
		Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			try {
				conn.transactionBegin();
				// Bestimme die ID des neuen Ansprechpartners
				DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "AllgAdrAnsprechpartner");
				Long ID = lastID == null ? 1 : lastID.MaxID + 1;
				DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, betrieb_id);
				if (betrieb == null)
					throw OperationError.NOT_FOUND.exception("Ein Betrieb mit der ID " + betrieb_id + " existiert in der Datenbank nicht.");
				// Ansprechpartner anlegen
				ansprechpartner = new DTOAnsprechpartnerAllgemeineAdresse(ID, betrieb_id);
				for (Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					switch(key) {
						case "id" -> {
							// ignoriere die Angabe einer ID
						}
						case "betrieb_id" -> {
							Long bid = JSONMapper.convertToLong( value , true );
							if (bid == null )
								throw OperationError.BAD_REQUEST.exception("Betireb_ID darf nicht fehlen.");
							if (bid.longValue() != betrieb_id.longValue())
								throw OperationError.BAD_REQUEST.exception("Betrieb_ID aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
						}
						case "titel" -> ansprechpartner.Titel = JSONMapper.convertToString(value, true, true);
						case "anrede" -> ansprechpartner.Anrede = JSONMapper.convertToString(value, true, true);
						case "name" -> ansprechpartner.Name = JSONMapper.convertToString(value, true, true);
						case "vorname" -> ansprechpartner.Vorname = JSONMapper.convertToString(value, true, true);
						case "email" -> ansprechpartner.Email = JSONMapper.convertToString(value, true, true);
						case "telefon" -> ansprechpartner.Telefon = JSONMapper.convertToString(value, true, true);
						case "abteilung" -> ansprechpartner.Abteilung= JSONMapper.convertToString(value, true, true);
						case "GU_ID" -> ansprechpartner.GU_ID= JSONMapper.convertToString(value, true, true);
					
						default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(ansprechpartner);
				if (!conn.transactionCommit())
					return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Betriebansprechpartners");
			} catch (Exception e) {
				if (e instanceof WebApplicationException webApplicationException)
					return webApplicationException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
			} finally {
				conn.transactionRollback();
			}
		}
		BetriebAnsprechpartner daten = dtoMapper.apply(ansprechpartner);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
