package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.lehrer.LehrerKatalogAnrechnungsgrundEintrag;
import de.svws_nrw.core.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerKatalogAnrechnungsgrundEintrag}.
 */
public class DataKatalogLehrerAnrechnungsgruende extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerKatalogAnrechnungsgrundEintrag}.
	 */
	public DataKatalogLehrerAnrechnungsgruende() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<LehrerKatalogAnrechnungsgrundEintrag> daten = new Vector<>();
		for (LehrerAnrechnungsgrund grund : LehrerAnrechnungsgrund.values())
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
