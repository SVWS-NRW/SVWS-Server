package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import de.svws_nrw.core.data.fach.FachgruppenKatalogEintrag;
import de.svws_nrw.core.types.fach.Fachgruppe;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-Type {@link Fachgruppe}.
 */
public final class DataKatalogFachgruppen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-Type {@link Fachgruppe}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 *               (hier: Abfrage der Schuldaten, zur Ermittlung der Schulform)
	 */
	public DataKatalogFachgruppen(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
        final ArrayList<FachgruppenKatalogEintrag> daten = new ArrayList<>();
        for (final Fachgruppe gruppe : Fachgruppe.values())
            daten.addAll(Arrays.asList(gruppe.historie));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
        final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
        if (schule == null)
            return OperationError.NOT_FOUND.getResponse();
    	final var gruppen = Fachgruppe.get(schule.Schulform);
    	if (gruppen == null)
    		return OperationError.NOT_FOUND.getResponse();
        final ArrayList<FachgruppenKatalogEintrag> daten = new ArrayList<>();
        for (final Fachgruppe gruppe : gruppen)
            daten.addAll(Arrays.asList(gruppe.historie));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
	    if (id == null)
            return OperationError.NOT_FOUND.getResponse();
	    final FachgruppenKatalogEintrag daten = Fachgruppe.getKatalogEintragByID(id);
	    if (daten == null)
            return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
