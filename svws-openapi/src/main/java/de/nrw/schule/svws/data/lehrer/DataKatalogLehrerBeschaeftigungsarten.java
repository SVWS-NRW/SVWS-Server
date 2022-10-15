package de.nrw.schule.svws.data.lehrer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogBeschaeftigungsartEintrag;
import de.nrw.schule.svws.core.types.statkue.LehrerBeschaeftigungsart;
import de.nrw.schule.svws.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerKatalogBeschaeftigungsartEintrag}.
 */
public class DataKatalogLehrerBeschaeftigungsarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerKatalogBeschaeftigungsartEintrag}.
	 */
	public DataKatalogLehrerBeschaeftigungsarten() {
		super(null);
	}
	

	@Override
	public Response getAll() {
		Vector<LehrerKatalogBeschaeftigungsartEintrag> daten = new Vector<>();
		for (LehrerBeschaeftigungsart art : LehrerBeschaeftigungsart.values())
			daten.addAll(Arrays.asList(art.historie));
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
