package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.nrw.schule.svws.core.data.schule.HerkunftKatalogEintrag;
import de.nrw.schule.svws.core.types.schueler.Herkunft;
import de.nrw.schule.svws.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link HerkunftKatalogEintrag}.
 */
public class DataKatalogHerkuenfte extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link HerkunftKatalogEintrag}.
	 */
	public DataKatalogHerkuenfte() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<HerkunftKatalogEintrag> daten = new Vector<>();
		for (Herkunft eintrag : Herkunft.values())
			daten.addAll(Arrays.asList(eintrag.historie));
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
