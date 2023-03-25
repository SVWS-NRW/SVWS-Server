package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.fach.SprachpruefungsniveauKatalogEintrag;
import de.svws_nrw.core.types.fach.Sprachpruefungniveau;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-Type {@link Sprachpruefungniveau}.
 */
public class DataKatalogSprachpruefungsniveaus extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-Type {@link Sprachpruefungniveau}.
	 */
	public DataKatalogSprachpruefungsniveaus() {
		super(null);
	}

	@Override
	public Response getAll() {
        Vector<SprachpruefungsniveauKatalogEintrag> daten = new Vector<>();
        for (Sprachpruefungniveau ref : Sprachpruefungniveau.values())
            daten.addAll(Arrays.asList(ref.historie));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
	    return this.getAll();
	}

	@Override
	public Response get(Long id) {
        throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

}
