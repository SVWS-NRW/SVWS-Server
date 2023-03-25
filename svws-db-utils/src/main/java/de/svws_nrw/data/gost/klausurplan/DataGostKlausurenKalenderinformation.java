package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKalenderinformation;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKalenderinformationen;
import de.svws_nrw.db.dto.current.svws.db.DTODBAutoInkremente;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurenKalenderinformation}.
 */
public class DataGostKlausurenKalenderinformation extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurenKalenderinformation}.
	 * 
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenKalenderinformation(DBEntityManager conn) {
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
	private Function<DTOGostKlausurenKalenderinformationen, GostKlausurenKalenderinformation> dtoMapper = (
			DTOGostKlausurenKalenderinformationen z) -> {
		GostKlausurenKalenderinformation daten = new GostKlausurenKalenderinformation();
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
	 * @param halbjahr das Gost-Halbjahr
	 * 
	 * @return die Liste der Kursklausuren
	 */
	private List<GostKlausurenKalenderinformation> getKalenderinformationen() {
		List<DTOGostKlausurenKalenderinformationen> kalInfos = conn
				.queryAll(DTOGostKlausurenKalenderinformationen.class);
		List<GostKlausurenKalenderinformation> daten = new Vector<>();
		for (DTOGostKlausurenKalenderinformationen ki : kalInfos)
			daten.add(dtoMapper.apply(ki));
		return daten;
	}

	@Override
	public Response get(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			try {
				conn.transactionBegin();
				DTOGostKlausurenKalenderinformationen kalInfo = conn
						.queryByKey(DTOGostKlausurenKalenderinformationen.class, id);
				if (kalInfo == null)
					throw OperationError.NOT_FOUND.exception();
				for (Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					switch (key) {
					case "id" -> {
						Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "bemerkung" -> kalInfo.Bemerkungen = JSONMapper.convertToString(value, true, false);
					case "bezeichnung" -> kalInfo.Bezeichnung = JSONMapper.convertToString(value, true, false);
					case "startdatum" -> kalInfo.Startdatum = JSONMapper.convertToString(value, false, false);
					case "startzeit" -> kalInfo.Startzeit = JSONMapper.convertToString(value, true, false);
					case "enddatum" -> kalInfo.Enddatum = JSONMapper.convertToString(value, true, false);
					case "endzeit" -> kalInfo.Endzeit = JSONMapper.convertToString(value, true, false);
					case "istSperrtermin" -> kalInfo.IstSperrtermin = JSONMapper.convertToBoolean(value, false);
					default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(kalInfo);
				conn.transactionCommit();
			} catch (Exception e) {
				if (e instanceof WebApplicationException webAppException)
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
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKalenderinformationen())
				.build();
	}

	/**
	 * Erstellt eine neue Gost-KlausurenKalenderinformation
	 * 
	 * @param  is das Objekt
	 * 
	 * @return Eine Response mit der neuen Gost-KlausurenKalenderinformation
	 */
	public Response create(InputStream is) {
		DTOGostKlausurenKalenderinformationen kalInfo = null;
		try {
			conn.transactionBegin();
			// Bestimme die ID der neuen KlausurenKalenderinformation
			DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class,
					"Gost_Klausuren_Kalenderinformationen");
			Long ID = lastID == null ? 1 : lastID.MaxID + 1;
			kalInfo = new DTOGostKlausurenKalenderinformationen(ID, false);

			Map<String, Object> map = JSONMapper.toMap(is);
			if (map.size() > 0) {
				for (Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					switch (key) {
					case "id" -> {
						throw OperationError.BAD_REQUEST.exception();
					}
					case "bemerkung" -> kalInfo.Bemerkungen = JSONMapper.convertToString(value, true, false);
					case "bezeichnung" -> kalInfo.Bezeichnung = JSONMapper.convertToString(value, true, false);
					case "startdatum" -> kalInfo.Startdatum = JSONMapper.convertToString(value, false, false);
					case "startzeit" -> kalInfo.Startzeit = JSONMapper.convertToString(value, true, false);
					case "enddatum" -> kalInfo.Enddatum = JSONMapper.convertToString(value, true, false);
					case "endzeit" -> kalInfo.Endzeit = JSONMapper.convertToString(value, true, false);
					case "istSperrtermin" -> kalInfo.IstSperrtermin = JSONMapper.convertToBoolean(value, false);
					default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
			}

			conn.transactionPersist(kalInfo);
			if (!conn.transactionCommit())
				return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Gost-KlausurenKalenderinformationen");
		} catch (Exception e) {
			if (e instanceof WebApplicationException webApplicationException)
				return webApplicationException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			conn.transactionRollback();
		}

		GostKlausurenKalenderinformation daten = dtoMapper.apply(kalInfo);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Löscht eine Gost-KlausurenKalenderinformation *
	 * 
	 * @param id die ID der zu löschenden KlausurenKalenderinformation
	 * 
	 * @return die Response
	 */
	public Response delete(Long id) {
		// TODO use transaction
		// Bestimme die KlausurenKalenderinformation
		DTOGostKlausurenKalenderinformationen kalInfo = conn.queryByKey(DTOGostKlausurenKalenderinformationen.class, id);
		if (kalInfo == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne die KlausurenKalenderinformation
		conn.remove(kalInfo);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}

}
