package de.nrw.schule.svws.data.kaoa;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.nrw.schule.svws.core.data.kaoa.KAOAAnschlussoptionEintrag;
import de.nrw.schule.svws.core.types.kaoa.KAOAAnschlussoptionen;
import de.nrw.schule.svws.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KAOAAnschlussoptionEintrag}.
 */
public class DataKAoAAnschlussoptionen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KAOAAnschlussoptionEintrag}.
	 */
	public DataKAoAAnschlussoptionen() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<KAOAAnschlussoptionEintrag> daten = new Vector<>();
		for (KAOAAnschlussoptionen a : KAOAAnschlussoptionen.values())
			daten.addAll(Arrays.asList(a.historie));
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
