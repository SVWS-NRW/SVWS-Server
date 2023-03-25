package de.svws_nrw.data.kaoa;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.kaoa.KAOAKategorieEintrag;
import de.svws_nrw.core.types.kaoa.KAOAKategorie;
import de.svws_nrw.data.DataManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KAOAKategorieEintrag}.
 */
public class DataKAoAKategorien extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KAOAKategorieEintrag}.
	 */
	public DataKAoAKategorien() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<KAOAKategorieEintrag> daten = new Vector<>();
		for (KAOAKategorie kategorie : KAOAKategorie.values())
			daten.addAll(Arrays.asList(kategorie.historie));
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
