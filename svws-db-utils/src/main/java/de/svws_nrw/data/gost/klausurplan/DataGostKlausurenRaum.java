package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeume;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurraum}.
 */
public final class DataGostKlausurenRaum extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurraum}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenRaum(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaeume} in einen Core-DTO
	 * {@link GostKlausurraum}.
	 */
	public static final Function<DTOGostKlausurenRaeume, GostKlausurraum> dtoMapper = (final DTOGostKlausurenRaeume z) -> {
		final GostKlausurraum daten = new GostKlausurraum();
		daten.id = z.ID;
		daten.idTermin = z.Termin_ID;
		daten.idStundenplanRaum = z.Stundenplan_Raum_ID;
		daten.bemerkung = z.Bemerkungen;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der Klausurräume
	 */
	public List<GostKlausurraum> getKlausurraeume(final Long idTermin) {
		final List<DTOGostKlausurenRaeume> raeume = conn.queryNamed("DTOGostKlausurenRaeume.termin_id", idTermin, DTOGostKlausurenRaeume.class);
		final List<GostKlausurraum> daten = new ArrayList<>();
		for (final DTOGostKlausurenRaeume r : raeume)
			daten.add(dtoMapper.apply(r));
		return daten;
	}

	@Override
	public Response get(final Long idTermin) {
		// Klausurräume zu einem Klausurtermin
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKlausurraeume(idTermin)).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			try {
				conn.transactionBegin();
				final DTOGostKlausurenRaeume raum = conn.queryByKey(DTOGostKlausurenRaeume.class, id);
				if (raum == null)
					throw OperationError.NOT_FOUND.exception();
				for (final Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					switch (key) {
					case "id" -> {
						final Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "idTermin" -> {
						final int patch_idTermin = JSONMapper.convertToInteger(value, false);
						if ((patch_idTermin != raum.Termin_ID))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "idStundenplanRaum" -> raum.Stundenplan_Raum_ID = JSONMapper.convertToLong(value, true);
					case "bemerkung" -> raum.Bemerkungen = JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Raeume.col_Bemerkungen.datenlaenge());

					default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(raum);
				conn.transactionCommit();
			} catch (final Exception e) {
				if (e instanceof final WebApplicationException webAppException)
					return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
			} finally {
				// Perform a rollback if necessary
				conn.transactionRollback();
			}
		}
		return Response.status(Status.OK).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erstellt einen neue Gost-Klausurraum
	 *
	 * @param is Das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Gost-Klausurraum
	 */
	public Response create(final InputStream is) {
		DTOGostKlausurenRaeume raum = null;
		try {
			conn.transactionBegin();
			// Bestimme die ID des neuen Klausurraums
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Klausuren_Raeume");
			final Long id = lastID == null ? 1 : lastID.MaxID + 1;

			long termin_ID = -1;
			Long stundenplan_Raum_ID = null;
			String bemerkungen = null;

			final Map<String, Object> map = JSONMapper.toMap(is);
			if (map.size() > 0) {
				for (final Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					switch (key) {
					case "idTermin" -> termin_ID = JSONMapper.convertToLong(value, false);
					case "idStundenplanRaum" -> stundenplan_Raum_ID = JSONMapper.convertToLong(value, true);
					case "bemerkung" -> bemerkungen = JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Raeume.col_Bemerkungen.datenlaenge());
					case "id" -> { /* do nothing */ }
					default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
			}

			raum = new DTOGostKlausurenRaeume(id, termin_ID);
			raum.Stundenplan_Raum_ID = stundenplan_Raum_ID;
			raum.Bemerkungen = bemerkungen;

			conn.transactionPersist(raum);
			if (!conn.transactionCommit())
				return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Gost-Klausurraums");
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webApplicationException)
				return webApplicationException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			conn.transactionRollback();
		}

		final GostKlausurraum daten = dtoMapper.apply(raum);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();

	}

	/**
	 * Löscht einen Gost-Klausurraum *
	 *
	 * @param id die ID des zu löschenden Klausurraums
	 *
	 * @return die Response
	 */
	public Response delete(final Long id) {
		// TODO use transaction
		// Bestimme den Raum
		final DTOGostKlausurenRaeume raum = conn.queryByKey(DTOGostKlausurenRaeume.class, id);
		if (raum == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne den Raum
		conn.remove(raum);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}


}
