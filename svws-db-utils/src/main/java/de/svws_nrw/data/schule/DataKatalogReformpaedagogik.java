package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import de.svws_nrw.core.data.schule.ReformpaedagogikKatalogEintrag;
import de.svws_nrw.core.types.schule.Reformpaedagogik;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-Type {@link Reformpaedagogik}.
 */
public final class DataKatalogReformpaedagogik extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-Type {@link Reformpaedagogik}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 *               (hier: Abfrage der Schuldaten, zur Ermittlung der Schulform)
	 */
	public DataKatalogReformpaedagogik(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		final ArrayList<ReformpaedagogikKatalogEintrag> daten = new ArrayList<>();
		for (final Reformpaedagogik p : Reformpaedagogik.values())
			daten.addAll(Arrays.asList(p.historie));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final var liste = Reformpaedagogik.get(schule.Schulform);
		if (liste == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final ArrayList<ReformpaedagogikKatalogEintrag> daten = new ArrayList<>();
		for (final Reformpaedagogik p : liste)
			daten.addAll(Arrays.asList(p.historie));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Reformpaedagogik p = Reformpaedagogik.getByID(id);
		if (p == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final ReformpaedagogikKatalogEintrag daten = p.daten;
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
