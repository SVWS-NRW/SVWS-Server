package de.nrw.schule.svws.data.klassen;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.klassen.KlassenDaten;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KlassenDaten}.
 */
public class DataKlassendaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KlassenDaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKlassendaten(DBEntityManager conn) {
		super(conn);
	}
	
	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, id);
    	if (klasse == null)
			return OperationError.NOT_FOUND.getResponse();
    	List<DTOKlassenLeitung> klassenLeitungen = conn.queryNamed("DTOKlassenLeitung.klassen_id", klasse.ID, DTOKlassenLeitung.class);
		KlassenDaten daten = new KlassenDaten();
		daten.id = klasse.ID;
		daten.kuerzel = klasse.Klasse;
		daten.idJahrgang = klasse.Jahrgang_ID;
		daten.parallelitaet = klasse.ASDKlasse.length() < 3 ? null : klasse.ASDKlasse.substring(2, 3);
		daten.sortierung = klasse.Sortierung;
		daten.istSichtbar = klasse.Sichtbar;
		if (klassenLeitungen != null)
			for (DTOKlassenLeitung kl : klassenLeitungen)
				daten.klassenLeitungen.add(kl.Lehrer_ID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
}
