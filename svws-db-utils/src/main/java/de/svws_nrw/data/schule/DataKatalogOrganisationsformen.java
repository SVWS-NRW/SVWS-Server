package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.core.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.core.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.core.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.data.DataManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link OrganisationsformKatalogEintrag}.
 */
public class DataKatalogOrganisationsformen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link OrganisationsformKatalogEintrag}.
	 */
	public DataKatalogOrganisationsformen() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		Vector<OrganisationsformKatalogEintrag> daten = new Vector<>();
		for (BerufskollegOrganisationsformen eintrag : BerufskollegOrganisationsformen.values())
			daten.addAll(Arrays.asList(eintrag.historie));
		for (WeiterbildungskollegOrganisationsformen eintrag : WeiterbildungskollegOrganisationsformen.values())
			daten.addAll(Arrays.asList(eintrag.historie));
		for (AllgemeinbildendOrganisationsformen eintrag : AllgemeinbildendOrganisationsformen.values())
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
