package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schule.NationalitaetenKatalogEintrag;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link NationalitaetenKatalogEintrag}.
 */
public final class DataKatalogNationalitaeten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link NationalitaetenKatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogNationalitaeten(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		final ArrayList<NationalitaetenKatalogEintrag> daten = new ArrayList<>();
		for (final Nationalitaeten nat : Nationalitaeten.values())
			daten.addAll(Arrays.asList(nat.historie));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
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
