package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import de.svws_nrw.core.data.schule.ReformpaedagogikKatalogEintrag;
import de.svws_nrw.core.types.schule.Reformpaedagogik;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-Type {@link Reformpaedagogik}.
 */
public class DataKatalogReformpaedagogik extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-Type {@link Reformpaedagogik}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff 
	 *               (hier: Abfrage der Schuldaten, zur Ermittlung der Schulform)
	 */
	public DataKatalogReformpaedagogik(DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
        Vector<ReformpaedagogikKatalogEintrag> daten = new Vector<>();
        for (Reformpaedagogik p : Reformpaedagogik.values())
            daten.addAll(Arrays.asList(p.historie));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
        DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
        if (schule == null)
            return OperationError.NOT_FOUND.getResponse();
    	var liste = Reformpaedagogik.get(schule.Schulform);
    	if (liste == null)
    		return OperationError.NOT_FOUND.getResponse();
        Vector<ReformpaedagogikKatalogEintrag> daten = new Vector<>();
        for (Reformpaedagogik p : liste)
            daten.addAll(Arrays.asList(p.historie));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(Long id) {
	    if (id == null)
            return OperationError.NOT_FOUND.getResponse();
	    Reformpaedagogik p = Reformpaedagogik.getByID(id);
	    if (p == null)
            return OperationError.NOT_FOUND.getResponse();
	    ReformpaedagogikKatalogEintrag daten = p.daten;
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

}
