package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import de.svws_nrw.core.data.schule.SchuelerstatusKatalogEintrag;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerstatusKatalogEintrag}.
 */
public class DataSchuelerStatus extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerstatusKatalogEintrag}.
	 */
	public DataSchuelerStatus() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		List<SchuelerstatusKatalogEintrag> katalog = Arrays.stream(SchuelerStatus.values()).map(s -> {
		    var eintrag = new SchuelerstatusKatalogEintrag();
		    eintrag.StatusNr = s.id;
		    eintrag.Bezeichnung = s.bezeichnung;
		    eintrag.Sortierung = s.ordinal();
		    return eintrag;
		}).toList();
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(katalog).build();
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
