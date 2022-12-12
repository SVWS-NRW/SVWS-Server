package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.nrw.schule.svws.core.data.schueler.UebergangsempfehlungKatalogEintrag;
import de.nrw.schule.svws.core.types.schueler.Uebergangsempfehlung;
import de.nrw.schule.svws.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link UebergangsempfehlungKatalogEintrag}.
 */
public class DataKatalogUebergangsempfehlung extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link UebergangsempfehlungKatalogEintrag}.
	 */
	public DataKatalogUebergangsempfehlung() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<UebergangsempfehlungKatalogEintrag> daten = new Vector<>();
		for (Uebergangsempfehlung eintrag : Uebergangsempfehlung.values())
			daten.addAll(Arrays.asList(eintrag.historie));
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
