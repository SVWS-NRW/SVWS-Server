package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.schule.Schulleitung;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Schulleitung}.
 */
public final class DataSchulleitung extends DataManager<Long> {

	/** Die ID des Lehrers */
	private final Long idLehrer;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Schulleitung}.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param idLehrer   die ID des Lehrers, auf den sich die Anfragen beziehen
	 */
	public DataSchulleitung(final DBEntityManager conn, final Long idLehrer) {
		super(conn);
		this.idLehrer = idLehrer;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchulleitung} in einen Core-DTO {@link Schulleitung}.
	 */
	public static final DTOMapper<DTOSchulleitung, Schulleitung> dtoMapper = (final DTOSchulleitung dto) -> {
		final Schulleitung daten = new Schulleitung();
		daten.id = dto.ID;
		daten.idLehrer = dto.LehrerID;
		daten.idLeitungsfunktion = dto.LeitungsfunktionID;
		daten.bezeichnung = dto.Funktionstext;
		daten.beginn = dto.Von;
		daten.ende = dto.Bis;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	/**
	 * Gibt die Liste der Schulleitungsfunktionen für den Lehrer mit der angegebenen ID zurück.
	 * Ist die ID null, so werden alle zurückgegeben.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param idLehrer   die ID des Lehrers oder null
	 *
	 * @return die Liste der Schulleitungsfunktionen
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<Schulleitung> getSchulleitungsfunktionen(final @NotNull DBEntityManager conn, final Long idLehrer) throws ApiOperationException {
		// Ermittle die Schulleitungsfunktionen ...
		final List<DTOSchulleitung> funktionen = (idLehrer == null) ? conn.queryAll(DTOSchulleitung.class)
				: conn.queryList(DTOSchulleitung.QUERY_BY_LEHRERID, DTOSchulleitung.class, idLehrer);
		if (funktionen.isEmpty())
			return new ArrayList<>();
		final List<Schulleitung> result = new ArrayList<>();
		for (final DTOSchulleitung funktion : funktionen)
			result.add(dtoMapper.apply(funktion));
		return result;
	}


	/**
	 * Gibt die Schulleitungsfunktion für die übergebene ID zurück.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param id     die ID der Schulleitungsfunktion
	 *
	 * @return die Informationen zu der Schulleitungsfunktion
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Schulleitung getSchulleitungsfunktion(final @NotNull DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOSchulleitung funktion = conn.queryByKey(DTOSchulleitung.class, id);
		if (funktion == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Eine Schulleitungsfunktion mit der ID %d konnte nicht gefunden werden.".formatted(id));
		return dtoMapper.apply(funktion);
	}


	@Override
	public Response getList() throws ApiOperationException {
		final List<Schulleitung> daten = getSchulleitungsfunktionen(conn, this.idLehrer);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final Schulleitung daten = getSchulleitungsfunktion(conn, id);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		// Umsetzung mit patchBasic
		throw new UnsupportedOperationException();
	}

	// TODO add

	// TODO addMultiple

	// TODO delete

	// TODO deleteMultiple

}
