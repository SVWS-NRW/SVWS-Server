package de.svws_nrw.data.klassen;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.core.types.klassen.Klassenart;
import de.svws_nrw.data.DataManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KlassenartKatalogEintrag}.
 */
public class DataKatalogKlassenarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KlassenartKatalogEintrag}.
	 */
	public DataKatalogKlassenarten() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<KlassenartKatalogEintrag> daten = new Vector<>();
		for (Klassenart ka : Klassenart.values())
			daten.addAll(Arrays.asList(ka.historie));
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
