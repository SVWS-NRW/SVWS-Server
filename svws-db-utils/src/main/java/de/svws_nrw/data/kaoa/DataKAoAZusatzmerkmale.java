package de.svws_nrw.data.kaoa;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.kaoa.KAOAZusatzmerkmalEintrag;
import de.svws_nrw.core.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KAOAZusatzmerkmalEintrag}.
 */
public class DataKAoAZusatzmerkmale extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KAOAZusatzmerkmalEintrag}.
	 */
	public DataKAoAZusatzmerkmale() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<KAOAZusatzmerkmalEintrag> daten = new Vector<>();
		for (KAOAZusatzmerkmal z : KAOAZusatzmerkmal.values())
			daten.addAll(Arrays.asList(z.historie));
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
