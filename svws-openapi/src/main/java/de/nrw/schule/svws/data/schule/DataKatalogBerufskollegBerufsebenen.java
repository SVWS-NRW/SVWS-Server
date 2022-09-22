package de.nrw.schule.svws.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import de.nrw.schule.svws.core.types.schule.BerufskollegBerufsebene1;
import de.nrw.schule.svws.core.types.schule.BerufskollegBerufsebene2;
import de.nrw.schule.svws.core.types.schule.BerufskollegBerufsebene3;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BerufskollegBerufsebeneKatalogEintrag}.
 */
public class DataKatalogBerufskollegBerufsebenen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BerufskollegBerufsebeneKatalogEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogBerufskollegBerufsebenen(DBEntityManager conn) {
		super(conn);
	}
	
	@Override
	public Response getAll() {
		Vector<BerufskollegBerufsebeneKatalogEintrag> daten = new Vector<>();
		for (BerufskollegBerufsebene1 ebenen : BerufskollegBerufsebene1.values())
			daten.addAll(Arrays.asList(ebenen.historie));
		for (BerufskollegBerufsebene2 ebenen : BerufskollegBerufsebene2.values())
			daten.addAll(Arrays.asList(ebenen.historie));
		for (BerufskollegBerufsebene3 ebenen : BerufskollegBerufsebene3.values())
			daten.addAll(Arrays.asList(ebenen.historie));
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
