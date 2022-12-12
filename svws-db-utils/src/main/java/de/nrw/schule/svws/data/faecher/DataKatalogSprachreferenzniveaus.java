package de.nrw.schule.svws.data.faecher;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.nrw.schule.svws.core.data.fach.SprachreferenzniveauKatalogEintrag;
import de.nrw.schule.svws.core.types.fach.Sprachreferenzniveau;
import de.nrw.schule.svws.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-Type {@link Sprachreferenzniveau}.
 */
public class DataKatalogSprachreferenzniveaus extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-Type {@link Sprachreferenzniveau}.
	 */
	public DataKatalogSprachreferenzniveaus() {
		super(null);
	}

	@Override
	public Response getAll() {
        Vector<SprachreferenzniveauKatalogEintrag> daten = new Vector<>();
        for (Sprachreferenzniveau ref : Sprachreferenzniveau.values())
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
