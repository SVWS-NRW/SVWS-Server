package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.schule.KindergartenbesuchKatalogEintrag;
import de.svws_nrw.core.types.schule.Kindergartenbesuch;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KindergartenbesuchKatalogEintrag}.
 */
public class DataKatalogKindergartenbesuch extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KindergartenbesuchKatalogEintrag}.
	 */
	public DataKatalogKindergartenbesuch() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<KindergartenbesuchKatalogEintrag> daten = new Vector<>();
		for (Kindergartenbesuch eintrag : Kindergartenbesuch.values())
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
