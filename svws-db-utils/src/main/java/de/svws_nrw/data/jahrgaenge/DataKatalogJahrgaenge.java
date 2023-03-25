package de.svws_nrw.data.jahrgaenge;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.jahrgang.JahrgangsKatalogEintrag;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link JahrgangsKatalogEintrag}.
 */
public class DataKatalogJahrgaenge extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link JahrgangsKatalogEintrag}.
	 */
	public DataKatalogJahrgaenge() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<JahrgangsKatalogEintrag> daten = new Vector<>();
		for (Jahrgaenge jg : Jahrgaenge.values())
			daten.addAll(Arrays.asList(jg.historie));
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
