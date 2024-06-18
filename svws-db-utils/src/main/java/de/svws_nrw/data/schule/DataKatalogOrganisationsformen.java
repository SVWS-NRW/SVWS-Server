package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;

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
public final class DataKatalogOrganisationsformen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link OrganisationsformKatalogEintrag}.
	 */
	public DataKatalogOrganisationsformen() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<OrganisationsformKatalogEintrag> daten = new ArrayList<>();
		for (final BerufskollegOrganisationsformen eintrag : BerufskollegOrganisationsformen.values())
			daten.addAll(Arrays.asList(eintrag.historie));
		for (final WeiterbildungskollegOrganisationsformen eintrag : WeiterbildungskollegOrganisationsformen.values())
			daten.addAll(Arrays.asList(eintrag.historie));
		for (final AllgemeinbildendOrganisationsformen eintrag : AllgemeinbildendOrganisationsformen.values())
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
