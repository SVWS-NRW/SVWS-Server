package de.svws_nrw.data.betriebe;

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
import de.svws_nrw.core.data.betrieb.BetriebStammdaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAdressart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAllgemeineAdresse;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.utils.OperationError;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BetriebStammdaten}.
 */
public class DataBetriebsStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BetriebStammdaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBetriebsStammdaten(DBEntityManager conn) {
		super(conn);
	}

	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKatalogAllgemeineAdresse} in einen Core-DTO {@link BetriebStammdaten}.  
	 */
	private Function<DTOKatalogAllgemeineAdresse, BetriebStammdaten> dtoMapper = (DTOKatalogAllgemeineAdresse betrieb) -> {
		BetriebStammdaten daten = new BetriebStammdaten();
		daten.id = betrieb.ID;
		daten.adressArt = betrieb.adressArt;
		daten.name1 = betrieb.name1;
		daten.name2 = betrieb.name2;
		daten.strassenname = betrieb.strassenname;
		daten.hausnr = betrieb.hausnr;
		daten.hausnrzusatz = betrieb.hausnrzusatz;
		daten.ort_id = betrieb.ort_id;
		daten.telefon1 = betrieb.telefon1;
		daten.telefon2 = betrieb.telefon2;
		daten.fax = betrieb.fax;
		daten.email = betrieb.email;
		daten.bemerkungen = betrieb.bemerkungen;
		daten.sortierung = betrieb.sortierung;
		daten.ausbildungsbetrieb = betrieb.ausbildungsbetrieb;
		daten.bietetPraktika = betrieb.bietetPraktika;
		daten.branche = betrieb.branche;
		daten.zusatz1 = betrieb.zusatz1;
		daten.zusatz2= betrieb.zusatz2;
		daten.Sichtbar = betrieb.Sichtbar;
		daten.Aenderbar = betrieb.Aenderbar;
		daten.BelehrungISG= betrieb.BelehrungISG;
		daten.GU_ID = betrieb.GU_ID;
		daten.ErwFuehrungszeugnis = betrieb.ErwFuehrungszeugnis;
		daten.ExtID = betrieb.ExtID;
		return daten;
	};

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
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, id);
		if (betrieb == null)
			return OperationError.NOT_FOUND.getResponse();
		BetriebStammdaten daten = dtoMapper.apply(betrieb);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		Map<String, Object> map = JSONMapper.toMap(is);
		if(map.size() > 0){
			try{
				conn.transactionBegin();
				DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, id);
				
				if (betrieb == null)
					throw OperationError.NOT_FOUND.exception();
				for (Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					switch(key){
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
								throw OperationError.BAD_REQUEST.exception();
						}
						case "adressArt" -> {
							Long adressartID = JSONMapper.convertToLong(value,true);
							if(adressartID == null){
								betrieb.adressArt = null;
							}else {
								DTOKatalogAdressart adressart = conn.queryByKey(DTOKatalogAdressart.class, adressartID);
								if( adressart == null)
									throw OperationError.NOT_FOUND.exception();
								betrieb.adressArt =adressartID;
							}
						}
						case "name1" -> betrieb.name1 = JSONMapper.convertToString(value,true,true);
						case "name2" -> betrieb.name2 = JSONMapper.convertToString(value,true,true);
						case "strassenname" -> betrieb.strassenname = JSONMapper.convertToString(value,true,true);
						case "hausnr" -> betrieb.hausnr = JSONMapper.convertToString(value,true,true);
						case "hausnrzusatz" -> betrieb.hausnrzusatz = JSONMapper.convertToString(value,true,true);
						case "ort_id" -> {
							Long ort_id = JSONMapper.convertToLong(value,true);
							if(ort_id == null){
								betrieb.ort_id = null;
							}else {
								DTOOrt adressart = conn.queryByKey(DTOOrt.class, ort_id);
								if( adressart == null)
									throw OperationError.NOT_FOUND.exception();
							}
							betrieb.ort_id = JSONMapper.convertToLong(value,true);
						}    
						case "telefon1" -> betrieb.telefon1 = JSONMapper.convertToString(value,true,true);
						case "telefon2" -> betrieb.telefon2= JSONMapper.convertToString(value,true,true);
						case "fax" -> betrieb.fax = JSONMapper.convertToString(value,true,true);
						case "email" -> betrieb.email = JSONMapper.convertToString(value,true,true);
						case "bemerkungen" -> betrieb.bemerkungen = JSONMapper.convertToString(value,true,true);
						case "sortierung" -> betrieb.sortierung = JSONMapper.convertToInteger(value,true);
						case "ausbildungsbetrieb" -> betrieb.ausbildungsbetrieb = JSONMapper.convertToBoolean(value,true);
						case "bietetPraktika" -> betrieb.bietetPraktika = JSONMapper.convertToBoolean(value,true);
						case "branche" -> betrieb.branche = JSONMapper.convertToString(value,true,true);
						case "zusatz1" -> betrieb.zusatz1 = JSONMapper.convertToString(value,true,true);
						case "zusatz2" -> betrieb.zusatz2 = JSONMapper.convertToString(value,true,true);
						case "Sichtbar" -> betrieb.Sichtbar= JSONMapper.convertToBoolean(value,true);
						case "Aenderbar" -> betrieb.Aenderbar = JSONMapper.convertToBoolean(value,true);
						case "Massnahmentraeger" -> betrieb.Massnahmentraeger = JSONMapper.convertToBoolean(value,true); 
						case "BelehrungISG" -> betrieb.BelehrungISG = JSONMapper.convertToBoolean(value,true);
						case "GU_ID" -> betrieb.GU_ID = JSONMapper.convertToString(value,true,true); 
						case "ErwFuehrungszeugnis" -> betrieb.ErwFuehrungszeugnis = JSONMapper.convertToBoolean(value,true);
						case "ExtID" -> betrieb.ExtID = JSONMapper.convertToString(value,true,true);
						
						default -> throw OperationError.BAD_REQUEST.exception();
						 
					}    
				}
				conn.transactionPersist(betrieb);
				if (!conn.transactionCommit())
					return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren der Betriebsdaten.");
			}catch(Exception e){
				if(e instanceof WebApplicationException webAppException)
					return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
			}finally{
				conn.transactionRollback();
			}
			
		}
		return Response.status(Status.OK).build();
	}
	

	/**
	 * Liefert eine Liste der Stammdaten aller Betriebe, die einem Schüler zugeordnet sind. Bei dem
	 * Stammdaten wird ggf. ein ausgewählter Ansprechpartner des Schülers angegeben. 
	 * 
	 * @param schueler_id   die ID des Schülers
	 * 
	 * @return die HTTP-Antwort mit den Stammdaten aller Betriebe, die dem Schüler zugeordnet sind.
	 */
	public Response getSchuelerBetriebe(Long schueler_id) {
		if(schueler_id == null)
			return OperationError.NOT_FOUND.getResponse("Die Id des Schülers darf nicht leer sein.");
		
		List<DTOKatalogAllgemeineAdresse> betriebe = conn.queryList("SELECT dtoa FROM DTOKatalogAllgemeineAdresse dtoa, DTOSchuelerAllgemeineAdresse dtos WHERE dtoa.ID=dtos.Adresse_ID and dtos.Schueler_ID = ?1 ", DTOKatalogAllgemeineAdresse.class, schueler_id);
		
		if (betriebe == null || betriebe.size()<1)
			return OperationError.NOT_FOUND.getResponse("Schüler mit der ID"+schueler_id+" hat keine Betriebe");
		List<BetriebStammdaten> daten = betriebe.stream().map(dtoMapper).collect(Collectors.toList());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();

	}
}
