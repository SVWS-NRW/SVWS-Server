package de.svws_nrw.data;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese abstrakte Klasse ist die Grundlage für das einheitliche Aggregieren von
 * Informationen für die OpenAPI und das einheitliche Bereitstellen von Funktionen,
 * welche Daten für GET oder PATCH-Zugriff zur Verfügung stellen.
 *
 * @param <ID> die Typ, welcher als ID für die Informationen verwendet wird.
 */
public abstract class DataManager<ID> {

	/** Die Datenbank-Verbindung zum Aggregieren der Informationen aus der DB und zum Schreiben der Informationen bzw. Teilinformationen */
	protected final DBEntityManager conn;

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn   die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	protected DataManager(final DBEntityManager conn) {
		this.conn = conn;
	}


	/**
	 * Ermittelt eine Liste mit allen Informationen in der DB. Wird üblicherweise
	 * durch GET-Methoden für Listen verwendet. Meist ist die Methode getList zu bevorzugen.
	 *
	 * @return eine Liste mit den Informationen
	 */
	public abstract Response getAll();



	/**
	 * Ermittelt eine Liste mit Informationen. Wird üblicherweise durch GET-Methoden
	 * für Listen verwendet. Bei dieser Liste werden ggf. Filter verwendet (z.B. nur als sichtbar
	 * markierte Einträge)
	 *
	 * @return eine Liste mit den Informationen
	 */
	public abstract Response getList();


	/**
	 * Ermittelt die Informationen anhand der angegebenen ID. Wird
	 * üblicherweise durch GET-Methoden verwendet.
	 *
	 * @param id   die ID der gesuchten Informationen
	 *
	 * @return die Information mit der angebenen ID
	 */
	public abstract Response get(ID id);

	/**
	 * Ermittelt die Informationen ohne eine gültige ID (null). Wird
	 * üblicherweise durch GET-Methoden verwendet.
	 *
	 * @return die Information mit der angebenen ID
	 */
	public Response get() {
		return this.get(null);
	}

	/**
	 * Passt die Informationen mithilfe des JSON-Patches aus dem übergebenen
	 * {@link InputStream} an.
	 *
	 * @param id   die ID der anzupassenden Informationen
	 * @param is   der {@link InputStream} mit dem JSON-Patch
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public abstract Response patch(ID id, InputStream is);

	/**
	 * Passt die Informationen mithilfe des JSON-Patches aus dem übergebenen
	 * {@link InputStream} an. Eine ID wird in diesem Fall nicht verwendet und
	 * als null angenommen.
	 *
	 * @param is   der {@link InputStream} mit dem JSON-Patch
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public Response patch(final InputStream is) {
		return this.patch(null, is);
	}


	/**
	 * Passt die Informationen des Datenbank-DTO mit der angegebenen ID
	 * mithilfe des JSON-Patches aus dem übergebenen {@link InputStream} an.
	 * Dabei werden nur die übergebenen Mappings zugelassen.
	 *
	 * @param <DTO>   der Typ des DTOs
	 * @param id   die ID des zu patchenden DTOs
	 * @param is   der Input-Stream
	 * @param dtoClass   die Klasse des DTOs
	 * @param attributeMapper   die Mapper für deas Anpassen des DTOs
	 *
	 * @return die Response
	 */
	protected <DTO> Response patchBasic(final ID id, final InputStream is, final Class<DTO> dtoClass, final Map<String, BiConsumer<DTO, Object>> attributeMapper) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Patch mit der ID null ist nicht möglich.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return OperationError.NOT_FOUND.getResponse("In dem Patch sind keine Daten enthalten.");
		try {
			conn.transactionBegin();
			final DTO dto = conn.queryByKey(dtoClass, id);
			if (dto == null)
				throw OperationError.NOT_FOUND.exception();
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				final BiConsumer<DTO, Object> mapper = attributeMapper.get(key);
				if (mapper == null)
					throw OperationError.BAD_REQUEST.exception();
				mapper.accept(dto, value);
			}
			conn.transactionPersist(dto);
			conn.transactionCommit();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			conn.transactionRollback();
		}
		return Response.status(Status.OK).build();
	}

}
