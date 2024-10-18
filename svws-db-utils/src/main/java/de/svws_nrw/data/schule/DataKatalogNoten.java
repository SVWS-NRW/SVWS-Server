package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.ArrayList;

import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link NoteKatalogEintrag}.
 */
public final class DataKatalogNoten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link NoteKatalogEintrag}.
	 */
	public DataKatalogNoten() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<NoteKatalogEintrag> daten = new ArrayList<>();
		for (final Note note : Note.values())
			daten.addAll(note.historie());
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
