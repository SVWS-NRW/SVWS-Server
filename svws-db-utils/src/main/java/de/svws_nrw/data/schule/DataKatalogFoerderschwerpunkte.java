package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schule.FoerderschwerpunktKatalogEintrag;
import de.svws_nrw.core.types.schueler.Foerderschwerpunkt;
import de.svws_nrw.data.DataManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link FoerderschwerpunktKatalogEintrag}.
 */
public final class DataKatalogFoerderschwerpunkte extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link FoerderschwerpunktKatalogEintrag}.
	 */
	public DataKatalogFoerderschwerpunkte() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<FoerderschwerpunktKatalogEintrag> daten = new ArrayList<>();
		for (final Foerderschwerpunkt eintrag : Foerderschwerpunkt.values())
			daten.addAll(Arrays.asList(eintrag.historie));
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
