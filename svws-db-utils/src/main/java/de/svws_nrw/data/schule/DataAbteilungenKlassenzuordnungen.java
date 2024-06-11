package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link AbteilungKlassenzuordnung}.
 */
public final class DataAbteilungenKlassenzuordnungen extends DataManager<Long> {

	/** Die ID der Abteilung */
	private final Long idAbteilung;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link AbteilungKlassenzuordnung}.
	 *
	 * @param conn          die Datenbankverbindung
	 * @param idAbteilung   die ID der Abteilung, auf die sich die Anfragen beziehen
	 */
	public DataAbteilungenKlassenzuordnungen(final DBEntityManager conn, final Long idAbteilung) {
		super(conn);
		this.idAbteilung = idAbteilung;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOAbteilungsKlassen} in einen Core-DTO {@link AbteilungKlassenzuordnung}.
	 */
	public static final DTOMapper<DTOAbteilungsKlassen, AbteilungKlassenzuordnung> dtoMapper = (final DTOAbteilungsKlassen dto) -> {
		final AbteilungKlassenzuordnung daten = new AbteilungKlassenzuordnung();
		daten.id = dto.ID;
		daten.idAbteilung = dto.Abteilung_ID;
		daten.idKlasse = dto.Klassen_ID;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	/**
	 * Gibt die Liste der Abteilungszuordnungen für die Abteilung mit der angegebenen ID zurück.
	 *
	 * @param conn          die Datenbankverbindung
	 * @param idAbteilung   die ID der Abteilung
	 *
	 * @return die Liste der Abteilungen
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<AbteilungKlassenzuordnung> getZuordnungen(final @NotNull DBEntityManager conn, final long idAbteilung) throws ApiOperationException {
		final List<DTOAbteilungsKlassen> zuordnungen = conn.queryList(DTOAbteilungsKlassen.QUERY_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, idAbteilung);
		final List<AbteilungKlassenzuordnung> result = new ArrayList<>();
		for (final DTOAbteilungsKlassen zuordnung : zuordnungen)
			result.add(dtoMapper.apply(zuordnung));
		return result;
	}


	/**
	 * Gibt die Klassenzuordnungen für die übergebene ID zurück.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param id     die ID der Abteilung
	 *
	 * @return die Klassenzuordnungen
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static AbteilungKlassenzuordnung getZuordung(final @NotNull DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOAbteilungsKlassen zuordnung = conn.queryByKey(DTOAbteilungsKlassen.class, id);
		if (zuordnung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Eine Zuordnung mit der ID %d von einer Klasse zu einer Abteilung konnte nicht gefunden werden.".formatted(id));
		return dtoMapper.apply(zuordnung);
	}


	@Override
	public Response getList() throws ApiOperationException {
		final List<AbteilungKlassenzuordnung> daten = getZuordnungen(conn, this.idAbteilung);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final AbteilungKlassenzuordnung daten = getZuordung(conn, id);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		// Das Verändern der Zuordnung wird hier nicht unterstützt
		throw new UnsupportedOperationException();
	}

	// TODO add

	// TODO addMultiple

	// TODO delete

	// TODO deleteMultiple

}
