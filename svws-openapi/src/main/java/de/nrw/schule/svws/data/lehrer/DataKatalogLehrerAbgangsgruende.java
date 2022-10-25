package de.nrw.schule.svws.data.lehrer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogAbgangsgrundEintrag;
import de.nrw.schule.svws.core.types.lehrer.LehrerAbgangsgrund;
import de.nrw.schule.svws.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerKatalogAbgangsgrundEintrag}.
 */
public class DataKatalogLehrerAbgangsgruende extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerKatalogAbgangsgrundEintrag}.
	 */
	public DataKatalogLehrerAbgangsgruende() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<LehrerKatalogAbgangsgrundEintrag> daten = new Vector<>();
		for (LehrerAbgangsgrund grund : LehrerAbgangsgrund.values())
			daten.addAll(Arrays.asList(grund.historie));
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
