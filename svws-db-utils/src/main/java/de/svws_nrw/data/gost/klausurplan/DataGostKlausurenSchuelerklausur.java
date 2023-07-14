package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostSchuelerklausur}.
 */
public final class DataGostKlausurenSchuelerklausur extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostSchuelerklausur}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausur(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Liste der Schuelerklausuren einer Jahrgangsstufe zu den gegebenen Kursklausuren zurück.
	 *
	 * @param kursKlausurIds die Liste der KlausurIds
	 *
	 * @return die Liste der Kursklausuren
	 */
	public Response getSchuelerKlausuren(final List<Long> kursKlausurIds) {
		final List<DTOGostKlausurenSchuelerklausuren> listSchuelerklausuren = conn
				.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", kursKlausurIds, DTOGostKlausurenSchuelerklausuren.class);

		final List<GostSchuelerklausur> daten = new ArrayList<>();
		for (final DTOGostKlausurenSchuelerklausuren s : listSchuelerklausuren)
			daten.add(dtoMapper.apply(s));

		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenSchuelerklausuren} in einen Core-DTO
	 * {@link GostSchuelerklausur}.
	 */
	public static final Function<DTOGostKlausurenSchuelerklausuren, GostSchuelerklausur> dtoMapper = (final DTOGostKlausurenSchuelerklausuren z) -> {
		final GostSchuelerklausur daten = new GostSchuelerklausur();
		daten.idKursklausur = z.Kursklausur_ID;
		daten.idSchueler = z.Schueler_ID;
		daten.idSchuelerklausur = z.ID;
		daten.idTermin = z.Termin_ID;
		daten.startzeit = z.Startzeit;
		return daten;
	};

	@Override public Response patch(final Long id, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			try {
				conn.transactionBegin();
				final DTOGostKlausurenSchuelerklausuren schuelerklausur = conn.queryByKey(DTOGostKlausurenSchuelerklausuren.class, id);
				if (schuelerklausur == null)
					throw OperationError.NOT_FOUND.exception();
				for (final Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					switch (key) {
					case "idSchuelerklausur" -> {
						final Long patch_id = JSONMapper.convertToLong(value, false);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "idKursklausur" -> {
						final Long patch_id = JSONMapper.convertToLong(value, false);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "idSchueler" -> {
						final Long patch_id = JSONMapper.convertToLong(value, false);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "idTermin" -> {
						final Long newTermin = JSONMapper.convertToLong(value, true);
						schuelerklausur.Termin_ID = newTermin;
					}
					case "startzeit" -> schuelerklausur.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
					default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(schuelerklausur);
				if (!conn.transactionCommit()) {
					throw OperationError.CONFLICT.exception();
				}
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

	@Override public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

}
