package de.nrw.schule.svws.data.kataloge;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.kurse.KursartKatalogEintrag;
import de.nrw.schule.svws.core.types.statkue.ZulaessigeKursart;
import de.nrw.schule.svws.data.DataManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KursartKatalogEintrag}.
 */
public class DataKatalogKursarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KursartKatalogEintrag}.
	 */
	public DataKatalogKursarten() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<KursartKatalogEintrag> daten = new Vector<>();
		for (ZulaessigeKursart ka : ZulaessigeKursart.values())
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
