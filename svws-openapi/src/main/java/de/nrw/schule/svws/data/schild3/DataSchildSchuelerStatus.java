package de.nrw.schule.svws.data.schild3;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragSchuelerStatus;
import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Schild3KatalogEintragSchuelerStatus}.
 */
public class DataSchildSchuelerStatus extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Schild3KatalogEintragSchuelerStatus}.
	 */
	public DataSchildSchuelerStatus() {
		super(null);
	}
	
	@Override
	public Response getAll() {
		List<Schild3KatalogEintragSchuelerStatus> katalog = Arrays.stream(SchuelerStatus.values()).map(s -> {
		    var eintrag = new Schild3KatalogEintragSchuelerStatus();
		    eintrag.StatusNr = s.id;
		    eintrag.Bezeichnung = s.bezeichnung;
		    eintrag.Sortierung = s.ordinal();
		    eintrag.InSimExp = (s == SchuelerStatus.AKTIV) || (s == SchuelerStatus.ABGANG) 
		            || (s == SchuelerStatus.ABSCHLUSS) || (s == SchuelerStatus.EXTERN);
		    eintrag.SIMAbschnitt = (s == SchuelerStatus.ABGANG) || (s == SchuelerStatus.ABSCHLUSS) ? "V" : null;
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
