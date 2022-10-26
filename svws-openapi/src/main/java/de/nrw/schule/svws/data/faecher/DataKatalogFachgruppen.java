package de.nrw.schule.svws.data.faecher;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.nrw.schule.svws.core.data.fach.FachgruppenKatalogEintrag;
import de.nrw.schule.svws.core.types.fach.Fachgruppe;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-Type {@link Fachgruppe}.
 */
public class DataKatalogFachgruppen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-Type {@link Fachgruppe}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff 
	 *               (hier: Abfrage der Schuldaten, zur Ermittlung der Schulform)
	 */
	public DataKatalogFachgruppen(DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
        Vector<FachgruppenKatalogEintrag> daten = new Vector<>();
        for (Fachgruppe gruppe : Fachgruppe.values())
            daten.addAll(Arrays.asList(gruppe.historie));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
        DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
        if (schule == null)
            return OperationError.NOT_FOUND.getResponse();
    	var gruppen = Fachgruppe.get(schule.Schulform);
    	if (gruppen == null)
    		return OperationError.NOT_FOUND.getResponse();
        Vector<FachgruppenKatalogEintrag> daten = new Vector<>();
        for (Fachgruppe gruppe : gruppen)
            daten.addAll(Arrays.asList(gruppe.historie));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(Long id) {
	    if (id == null)
            return OperationError.NOT_FOUND.getResponse();
	    FachgruppenKatalogEintrag daten = Fachgruppe.getKatalogEintragByID(id);
	    if (daten == null)
            return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
}
