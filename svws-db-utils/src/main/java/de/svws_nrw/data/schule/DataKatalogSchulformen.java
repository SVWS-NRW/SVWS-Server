package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchulformKatalogEintrag}.
 */
public final class DataKatalogSchulformen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchulformKatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogSchulformen(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getListAll()).build();
	}

	/**
	 * Liefert eine Liste aller SchulformKatalogEintrag-Objekte, die auf den
	 * historischen Einträgen aller Schulformen basieren.
	 *
	 * @return Eine Liste vom Typ List<SchulformKatalogEintrag>, die alle
	 *         historischen Katalogeinträge der Schulformen enthält.
	 */
	public List<SchulformKatalogEintrag> getListAll() {
		final ArrayList<SchulformKatalogEintrag> daten = new ArrayList<>();
		for (final Schulform schulform : Schulform.values())
			daten.addAll(schulform.historie());
		return daten;
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
