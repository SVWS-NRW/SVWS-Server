package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.benutzer.BenutzerDaten;
import de.nrw.schule.svws.core.data.benutzer.BenutzergruppeDaten;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzerKompetenz;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppenKompetenz;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.nrw.schule.svws.db.dto.current.svws.auth.DTOCredentials;
import de.nrw.schule.svws.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerDaten}.
 */
public class DataBenutzerDaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BenutzerDaten}.
	 * 
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBenutzerDaten(DBEntityManager conn) {
		super(conn);
	}
	
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOViewBenutzerdetails} in einen Core-DTO {@link BenutzerDaten}.  
	 */
	private Function<DTOViewBenutzerdetails, BenutzerDaten> dtoMapper = (DTOViewBenutzerdetails b) -> {
		BenutzerDaten daten = new BenutzerDaten();
		daten.id = b.ID;
		daten.typ = b.Typ.id;
		daten.typID = b.TypID;
		daten.anzeigename = b.AnzeigeName;
		daten.name = b.Benutzername;
		daten.istAdmin = b.IstAdmin == null ? false : b.IstAdmin;
		daten.idCredentials = b.credentialID;
		return daten;
	};	

	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOBenutzergruppe} in einen Core-DTO {@link BenutzergruppeDaten}.  
	 */
	private Function<DTOBenutzergruppe, BenutzergruppeDaten> dtoMapperGruppe = (DTOBenutzergruppe b) -> {
		BenutzergruppeDaten daten = new BenutzergruppeDaten();
		daten.id = b.ID;
		daten.bezeichnung = b.Bezeichnung;
		daten.istAdmin = b.IstAdmin == null ? false : b.IstAdmin;
		return daten;
	};	
	

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(Long id) {
		// Lese die Daten des Benutzers ein
		DTOViewBenutzerdetails benutzer = conn.queryByKey(DTOViewBenutzerdetails.class, id);
    	if (benutzer == null)
    		throw OperationError.NOT_FOUND.exception();
    	// Lese die Informationen zu den Gruppen ein
    	List<Long> gruppenIDs = conn.queryNamed("DTOBenutzergruppenMitglied.benutzer_id", id, DTOBenutzergruppenMitglied.class).stream().map(g -> g.Gruppe_ID).toList();
    	List<DTOBenutzergruppe> gruppen = conn.queryNamed("DTOBenutzergruppe.id.multiple", gruppenIDs, DTOBenutzergruppe.class);
    	// Lese die Informationen zu den Kompetenzen ein
    	List<Long> kompetenzIDs = conn.queryNamed("DTOBenutzerKompetenz.benutzer_id", id, DTOBenutzerKompetenz.class)
        		.stream().map(b -> b.Kompetenz_ID).sorted().toList();
    	List<DTOBenutzergruppenKompetenz> gruppenKompetenzen = conn.queryNamed("DTOBenutzergruppenKompetenz.gruppe_id.multiple", gruppenIDs, DTOBenutzergruppenKompetenz.class);
    	List<Long> alleKompetenzIDs = gruppenKompetenzen.stream().map(g -> g.Kompetenz_ID).distinct().toList();
    	Map<Long, List<DTOBenutzergruppenKompetenz>> mapGruppenKompetenzen = gruppenKompetenzen.stream().collect(Collectors.groupingBy(k -> k.Gruppe_ID));
    	alleKompetenzIDs = Stream.concat(kompetenzIDs.stream(), alleKompetenzIDs.stream()).distinct().sorted().toList();
    	// Erstelle den Core-DTO für die API und gebe diesen zurück
    	BenutzerDaten daten = dtoMapper.apply(benutzer);
    	for (Long kompetenzID : kompetenzIDs)
    		daten.kompetenzen.add(BenutzerKompetenz.getByID(kompetenzID).daten);
    	for (Long kompetenzID : alleKompetenzIDs)
    		daten.kompetenzenAlle.add(BenutzerKompetenz.getByID(kompetenzID).daten);
    	for (DTOBenutzergruppe gruppe : gruppen) {
    		BenutzergruppeDaten gdaten = dtoMapperGruppe.apply(gruppe);
    		List<DTOBenutzergruppenKompetenz> gruppenkompetenzen = mapGruppenKompetenzen.get(gruppe.ID);
    		for (Long kompetenzID : gruppenkompetenzen.stream().map(k -> k.Kompetenz_ID).distinct().sorted().toList())
    			gdaten.kompetenzen.add(BenutzerKompetenz.getByID(kompetenzID).daten);
    		daten.gruppen.add(gdaten);
    	}
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

	
	/**
	 * Erstellt für den Benutzer mit der übergebenen ID einen neuen Kennwort-Hash
	 * basierend auf dem übergebenen Kennwort
	 * 
	 * @param id         die ID des Benutzers, dessen Kennwort neu gesetzt werden soll
	 * @param is         der Input-Stream mit dem Kennwort
	 * 
	 * @return die HTTP-Response, welche einen Erfolg bzw. Misserfolg angibt. 
	 */
	public Response setPassword(Long id, InputStream is) {
		try {
			String password = JSONMapper.toString(is);
			// Bestimme die Schiene der Blockung
			conn.transactionBegin();
			String hash = Benutzer.erstellePasswortHash(password);
			DTOViewBenutzerdetails benutzer = conn.queryByKey(DTOViewBenutzerdetails.class, id);
	    	if ((benutzer == null) || (benutzer.credentialID == null))
	    		throw OperationError.NOT_FOUND.exception();
	    	DTOCredentials credential = conn.queryByKey(DTOCredentials.class, benutzer.credentialID);
	    	if (credential == null)
	    		throw OperationError.NOT_FOUND.exception();
	    	credential.PasswordHash = hash;
			conn.transactionCommit();
			return Response.status(Status.NO_CONTENT).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}
	
}
