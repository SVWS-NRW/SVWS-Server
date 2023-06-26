package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeumeStunden;
import de.svws_nrw.db.dto.current.svws.db.DTODBAutoInkremente;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurraumstunde}.
 */
public final class DataGostKlausurenRaumstunde extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurraumstunde}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenRaumstunde(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaeumeStunden} in einen Core-DTO
	 * {@link GostKlausurraumstunde}.
	 */
	public static final Function<DTOGostKlausurenRaeumeStunden, GostKlausurraumstunde> dtoMapper = (final DTOGostKlausurenRaeumeStunden z) -> {
		final GostKlausurraumstunde daten = new GostKlausurraumstunde();
		daten.id = z.ID;
		daten.idRaum = z.Klausurraum_ID;
		daten.idZeitraster = z.Zeitraster_ID;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der Klausurraumstunden
	 */
	private List<GostKlausurraumstunde> getKlausurraumstunden(final Long idTermin) {

		final List<GostKlausurraum> listRaeume = new DataGostKlausurenRaum(conn).getKlausurraeume(idTermin);

		if (listRaeume.isEmpty()) {
			// TODO Errorhandling nötig?
			return new ArrayList<>();
		}

		final List<DTOGostKlausurenRaeumeStunden> stunden = conn.queryNamed("DTOGostKlausurenRaeumeStunden.klausurraum_id.multiple", listRaeume.stream().map(s -> s.idTermin).toList(), DTOGostKlausurenRaeumeStunden.class);

		final List<GostKlausurraumstunde> daten = new ArrayList<>();
		for (final DTOGostKlausurenRaeumeStunden s : stunden)
			daten.add(dtoMapper.apply(s));
		return daten;
	}

	@Override
	public Response get(final Long idTermin) {
		// Klausurraumstunden zu einem Klausurtermin
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKlausurraumstunden(idTermin)).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erstellt eine neue Gost-Klausurraumstunde
	 *
	 * @param is Das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit der neuen Gost-Klausurraumstunde
	 */
	public Response create(final InputStream is) {
		DTOGostKlausurenRaeumeStunden raumstunde = null;
		try {
			conn.transactionBegin();
			// Bestimme die ID der neuen Klausurraumstunde
			final DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Klausuren_Raeume_Stunden");
			final Long id = lastID == null ? 1 : lastID.MaxID + 1;

			long klausurraum_ID = -1;
			long zeitraster_ID = -1;

			final Map<String, Object> map = JSONMapper.toMap(is);
			if (map.size() > 0) {
				for (final Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					switch (key) {
					case "idRaum" -> klausurraum_ID = JSONMapper.convertToLong(value, false);
					case "idZeitraster" -> zeitraster_ID = JSONMapper.convertToLong(value, false);
					case "id" -> { /* do nothing */ }
					default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
			}

			raumstunde = new DTOGostKlausurenRaeumeStunden(id, klausurraum_ID, zeitraster_ID);

			conn.transactionPersist(raumstunde);
			if (!conn.transactionCommit())
				return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren der Gost-Klausurraumsstunde");
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webApplicationException)
				return webApplicationException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			conn.transactionRollback();
		}

		final GostKlausurraumstunde daten = dtoMapper.apply(raumstunde);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Löscht eine Gost-Klausurraumstunde *
	 *
	 * @param id die ID des zu löschenden Klausurraums
	 *
	 * @return die Response
	 */
	public Response delete(final Long id) {
		// TODO use transaction
		// Bestimme die Raumstunde
		final DTOGostKlausurenRaeumeStunden stunde = conn.queryByKey(DTOGostKlausurenRaeumeStunden.class, id);
		if (stunde == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne die Raumstunde
		conn.remove(stunde);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


}
