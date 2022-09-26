package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.benutzer.BenutzergruppeDaten;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppenKompetenz;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzergruppeDaten}.
 */
public class DataBenutzergruppeDaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BenutzergruppeDaten}.
	 * 
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBenutzergruppeDaten(DBEntityManager conn) {
		super(conn);
	}
	
	
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
		// Bestimme die Benutzergruppe
		DTOBenutzergruppe benutzergruppe = conn.queryByKey(DTOBenutzergruppe.class, id);
    	if (benutzergruppe == null)
    		throw OperationError.NOT_FOUND.exception();
    	List<Long> kompetenzIDs = conn.queryNamed("DTOBenutzergruppenKompetenz.gruppe_id", benutzergruppe.ID, DTOBenutzergruppenKompetenz.class)
    		.stream().map(g -> g.Kompetenz_ID).sorted().toList();
    	// Erstelle die daten und gebe diese in einer Response zurück
    	BenutzergruppeDaten daten = dtoMapper.apply(benutzergruppe);
    	for (Long kompetenzID : kompetenzIDs)
    		daten.kompetenzen.add(BenutzerKompetenz.getByID(kompetenzID).daten);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

	
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOBenutzergruppe} in einen Core-DTO {@link BenutzergruppeDaten}.  
	 */
	private Function<DTOBenutzergruppe, BenutzergruppeDaten> dtoMapper = (DTOBenutzergruppe b) -> {
		BenutzergruppeDaten daten = new BenutzergruppeDaten();
		daten.id = b.ID;
		daten.bezeichnung = b.Bezeichnung;
		daten.istAdmin = b.IstAdmin == null ? false : b.IstAdmin;
		return daten;
	};	
	
}
