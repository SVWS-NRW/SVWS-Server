package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

import de.svws_nrw.core.data.fach.SprachpruefungsniveauKatalogEintrag;
import de.svws_nrw.core.types.fach.Sprachpruefungniveau;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-Type {@link Sprachpruefungniveau}.
 */
public final class DataKatalogSprachpruefungsniveaus extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-Type {@link Sprachpruefungniveau}.
	 */
	public DataKatalogSprachpruefungsniveaus() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<SprachpruefungsniveauKatalogEintrag> daten = new ArrayList<>();
		for (final Sprachpruefungniveau ref : Sprachpruefungniveau.values())
			daten.addAll(Arrays.asList(ref.historie));
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
