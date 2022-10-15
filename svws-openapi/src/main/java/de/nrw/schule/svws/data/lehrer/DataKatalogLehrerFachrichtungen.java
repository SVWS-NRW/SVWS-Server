package de.nrw.schule.svws.data.lehrer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogFachrichtungEintrag;
import de.nrw.schule.svws.core.types.statkue.LehrerFachrichtung;
import de.nrw.schule.svws.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerKatalogFachrichtungEintrag}.
 */
public class DataKatalogLehrerFachrichtungen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerKatalogFachrichtungEintrag}.
	 */
	public DataKatalogLehrerFachrichtungen() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<LehrerKatalogFachrichtungEintrag> daten = new Vector<>();
		for (LehrerFachrichtung status : LehrerFachrichtung.values())
			daten.addAll(Arrays.asList(status.historie));
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
