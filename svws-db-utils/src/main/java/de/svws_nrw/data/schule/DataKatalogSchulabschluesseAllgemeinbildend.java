package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schule.SchulabschlussAllgemeinbildendKatalogEintrag;
import de.svws_nrw.core.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchulabschlussAllgemeinbildendKatalogEintrag}.
 */
public class DataKatalogSchulabschluesseAllgemeinbildend extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchulabschlussAllgemeinbildendKatalogEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogSchulabschluesseAllgemeinbildend(DBEntityManager conn) {
		super(conn);
	}
	
	@Override
	public Response getAll() {
		Vector<SchulabschlussAllgemeinbildendKatalogEintrag> daten = new Vector<>();
		for (SchulabschlussAllgemeinbildend abschlussart : SchulabschlussAllgemeinbildend.values())
			daten.addAll(Arrays.asList(abschlussart.historie));
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
