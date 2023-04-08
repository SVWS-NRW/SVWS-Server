package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import de.svws_nrw.core.types.schule.BerufskollegBerufsebene1;
import de.svws_nrw.core.types.schule.BerufskollegBerufsebene2;
import de.svws_nrw.core.types.schule.BerufskollegBerufsebene3;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BerufskollegBerufsebeneKatalogEintrag}.
 */
public final class DataKatalogBerufskollegBerufsebenen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BerufskollegBerufsebeneKatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogBerufskollegBerufsebenen(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		final ArrayList<BerufskollegBerufsebeneKatalogEintrag> daten = new ArrayList<>();
		for (final BerufskollegBerufsebene1 ebenen : BerufskollegBerufsebene1.values())
			daten.addAll(Arrays.asList(ebenen.historie));
		for (final BerufskollegBerufsebene2 ebenen : BerufskollegBerufsebene2.values())
			daten.addAll(Arrays.asList(ebenen.historie));
		for (final BerufskollegBerufsebene3 ebenen : BerufskollegBerufsebene3.values())
			daten.addAll(Arrays.asList(ebenen.historie));
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
