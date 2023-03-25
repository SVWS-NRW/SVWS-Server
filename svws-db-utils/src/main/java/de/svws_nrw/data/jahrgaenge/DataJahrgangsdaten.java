package de.svws_nrw.data.jahrgaenge;

import java.io.InputStream;
import java.util.function.Function;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link JahrgangsDaten}.
 */
public class DataJahrgangsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link JahrgangsDaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataJahrgangsdaten(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOJahrgang} in einen Core-DTO {@link JahrgangsDaten}.  
	 */
	private Function<DTOJahrgang, JahrgangsDaten> dtoMapperJahrgang = (DTOJahrgang jahrgang) -> {
		JahrgangsDaten daten = new JahrgangsDaten();
		daten.id = jahrgang.ID;
		daten.kuerzel = jahrgang.InternKrz;
		daten.kuerzelStatistik = jahrgang.ASDJahrgang; // TODO Anpassung beim DTO, so dass ein Konverter zu dem Statistik-Jahrgangs-Objekt genutzt wird - hierbei auch die Bezeichnung miteinbeziehen
		daten.bezeichnung = jahrgang.ASDBezeichnung;
		daten.kuerzelSchulgliederung = jahrgang.Gliederung.daten.kuerzel;
		daten.idFolgejahrgang = jahrgang.Folgejahrgang_ID;
		daten.sortierung = jahrgang.Sortierung;
		daten.istSichtbar = jahrgang.Sichtbar;
		return daten;
	};

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
    	DTOJahrgang jahrgang = conn.queryByKey(DTOJahrgang.class, id);
    	if (jahrgang == null)
    		return OperationError.NOT_FOUND.getResponse();
		JahrgangsDaten daten = dtoMapperJahrgang.apply(jahrgang);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
}
