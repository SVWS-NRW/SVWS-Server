package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.lehrer.LehrerKatalogFachrichtungAnerkennungEintrag;
import de.svws_nrw.core.types.lehrer.LehrerFachrichtungAnerkennung;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerKatalogFachrichtungAnerkennungEintrag}.
 */
public class DataKatalogLehrerFachrichtungAnerkennungen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerKatalogFachrichtungAnerkennungEintrag}.
	 */
	public DataKatalogLehrerFachrichtungAnerkennungen() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<LehrerKatalogFachrichtungAnerkennungEintrag> daten = new Vector<>();
		for (LehrerFachrichtungAnerkennung status : LehrerFachrichtungAnerkennung.values())
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
