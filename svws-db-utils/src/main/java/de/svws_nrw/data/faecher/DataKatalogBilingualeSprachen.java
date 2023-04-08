package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import de.svws_nrw.core.data.fach.BilingualeSpracheKatalogEintrag;
import de.svws_nrw.core.types.fach.BilingualeSprache;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-Type {@link BilingualeSprache}.
 */
public final class DataKatalogBilingualeSprachen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-Type {@link BilingualeSprache}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 *               (hier: Abfrage der Schuldaten, zur Ermittlung der Schulform)
	 */
	public DataKatalogBilingualeSprachen(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
        final ArrayList<BilingualeSpracheKatalogEintrag> daten = new ArrayList<>();
        for (final BilingualeSprache gruppe : BilingualeSprache.values())
            daten.addAll(Arrays.asList(gruppe.historie));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
        final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
        if (schule == null)
            return OperationError.NOT_FOUND.getResponse();
    	final var sprachen = BilingualeSprache.get(schule.Schulform);
    	if (sprachen == null)
    		return OperationError.NOT_FOUND.getResponse();
        final ArrayList<BilingualeSpracheKatalogEintrag> daten = new ArrayList<>();
        for (final BilingualeSprache sprache : sprachen)
            daten.addAll(Arrays.asList(sprache.historie));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
	    if (id == null)
            return OperationError.NOT_FOUND.getResponse();
	    final BilingualeSpracheKatalogEintrag daten = BilingualeSprache.getKatalogEintragByID(id);
	    if (daten == null)
            return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
