package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenKalenderinformation;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKalenderinformationen;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurenKalenderinformation}.
 */
public final class DataGostKlausurenKalenderinformation extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurenKalenderinformation}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenKalenderinformation(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenKalenderinformationen} in einen Core-DTO
	 * {@link GostKlausurenKalenderinformation}.
	 */
	private final Function<DTOGostKlausurenKalenderinformationen, GostKlausurenKalenderinformation> dtoMapper = (final DTOGostKlausurenKalenderinformationen z) -> {
		final GostKlausurenKalenderinformation daten = new GostKlausurenKalenderinformation();
		daten.id = z.ID;
		daten.bemerkung = z.Bemerkungen;
		daten.bezeichnung = z.Bezeichnung;
		daten.startdatum = z.Startdatum;
		daten.startzeit = z.Startzeit;
		daten.enddatum = z.Enddatum;
		daten.endzeit = z.Endzeit;
		daten.istSperrtermin = z.IstSperrtermin;
		return daten;
	};

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @return die Liste der Kursklausuren
	 */
	private List<GostKlausurenKalenderinformation> getKalenderinformationen() {
		final List<DTOGostKlausurenKalenderinformationen> kalInfos = conn.queryAll(DTOGostKlausurenKalenderinformationen.class);
		final List<GostKlausurenKalenderinformation> daten = new ArrayList<>();
		for (final DTOGostKlausurenKalenderinformationen ki : kalInfos)
			daten.add(dtoMapper.apply(ki));
		return daten;
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			final DTOGostKlausurenKalenderinformationen kalInfo = conn.queryByKey(DTOGostKlausurenKalenderinformationen.class, id);
			if (kalInfo == null)
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
				case "bemerkung" -> kalInfo.Bemerkungen = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Kalenderinformationen.col_Bemerkungen.datenlaenge());
				case "bezeichnung" -> kalInfo.Bezeichnung = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Kalenderinformationen.col_Bezeichnung.datenlaenge());
				case "startdatum" -> kalInfo.Startdatum = JSONMapper.convertToString(value, false, false, null);
				case "startzeit" -> kalInfo.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
				case "enddatum" -> kalInfo.Enddatum = JSONMapper.convertToString(value, true, false, null);
				case "endzeit" -> kalInfo.Endzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
				case "istSperrtermin" -> kalInfo.IstSperrtermin = JSONMapper.convertToBoolean(value, false);
				default -> throw OperationError.BAD_REQUEST.exception();
				}
			}
			conn.transactionPersist(kalInfo);
		}
		return Response.status(Status.OK).build();
	}

	@Override
	public Response getList() {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKalenderinformationen()).build();
	}

	/**
	 * Erstellt eine neue Gost-KlausurenKalenderinformation
	 *
	 * @param is das Objekt
	 *
	 * @return Eine Response mit der neuen Gost-KlausurenKalenderinformation
	 */
	public Response create(final InputStream is) {
		DTOGostKlausurenKalenderinformationen kalInfo = null;
		// Bestimme die ID der neuen KlausurenKalenderinformation
		final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Klausuren_Kalenderinformationen");
		final Long id = lastID == null ? 1 : lastID.MaxID + 1;
		kalInfo = new DTOGostKlausurenKalenderinformationen(id, false);

		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
				case "id" -> {
					throw OperationError.BAD_REQUEST.exception();
				}
				case "bemerkung" -> kalInfo.Bemerkungen = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Kalenderinformationen.col_Bemerkungen.datenlaenge());
				case "bezeichnung" -> kalInfo.Bezeichnung = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Kalenderinformationen.col_Bezeichnung.datenlaenge());
				case "startdatum" -> kalInfo.Startdatum = JSONMapper.convertToString(value, false, false, null);
				case "startzeit" -> kalInfo.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
				case "enddatum" -> kalInfo.Enddatum = JSONMapper.convertToString(value, true, false, null);
				case "endzeit" -> kalInfo.Endzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
				case "istSperrtermin" -> kalInfo.IstSperrtermin = JSONMapper.convertToBoolean(value, false);
				default -> throw OperationError.BAD_REQUEST.exception();
				}
			}
		}

		conn.transactionPersist(kalInfo);
		final GostKlausurenKalenderinformation daten = dtoMapper.apply(kalInfo);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Löscht eine Gost-KlausurenKalenderinformation *
	 *
	 * @param id die ID der zu löschenden KlausurenKalenderinformation
	 *
	 * @return die Response
	 */
	public Response delete(final Long id) {
		// TODO use transaction
		// Bestimme die KlausurenKalenderinformation
		final DTOGostKlausurenKalenderinformationen kalInfo = conn.queryByKey(DTOGostKlausurenKalenderinformationen.class, id);
		if (kalInfo == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne die KlausurenKalenderinformation
		conn.remove(kalInfo);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}

}
