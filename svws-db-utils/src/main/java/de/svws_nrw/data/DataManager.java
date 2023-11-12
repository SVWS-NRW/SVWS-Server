package de.svws_nrw.data;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese abstrakte Klasse ist die Grundlage für das einheitliche Aggregieren von
 * Informationen für die OpenAPI und das einheitliche Bereitstellen von
 * Funktionen, welche Daten für GET oder PATCH-Zugriff zur Verfügung stellen.
 *
 * @param <ID> die Typ, welcher als ID für die Informationen verwendet wird.
 */
public abstract class DataManager<ID> {

	/**
	 * Die Datenbank-Verbindung zum Aggregieren der Informationen aus der DB und zum
	 * Schreiben der Informationen bzw. Teilinformationen
	 */
	protected final DBEntityManager conn;

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden
	 *             soll
	 */
	protected DataManager(final DBEntityManager conn) {
		this.conn = conn;
	}

	/**
	 * Ermittelt eine Liste mit allen Informationen in der DB. Wird üblicherweise
	 * durch GET-Methoden für Listen verwendet. Meist ist die Methode getList zu
	 * bevorzugen.
	 *
	 * @return eine Liste mit den Informationen
	 */
	public abstract Response getAll();

	/**
	 * Ermittelt eine Liste mit Informationen. Wird üblicherweise durch GET-Methoden
	 * für Listen verwendet. Bei dieser Liste werden ggf. Filter verwendet (z.B. nur
	 * als sichtbar markierte Einträge)
	 *
	 * @return eine Liste mit den Informationen
	 */
	public abstract Response getList();

	/**
	 * Ermittelt die Informationen anhand der angegebenen ID. Wird üblicherweise
	 * durch GET-Methoden verwendet.
	 *
	 * @param id die ID der gesuchten Informationen
	 *
	 * @return die Information mit der angebenen ID
	 */
	public abstract Response get(ID id);

	/**
	 * Ermittelt die Informationen ohne eine gültige ID (null). Wird üblicherweise
	 * durch GET-Methoden verwendet.
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
	 * @param id die ID der anzupassenden Informationen
	 * @param is der {@link InputStream} mit dem JSON-Patch
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public abstract Response patch(ID id, InputStream is);

	/**
	 * Passt die Informationen mithilfe des JSON-Patches aus dem übergebenen
	 * {@link InputStream} an. Eine ID wird in diesem Fall nicht verwendet und als
	 * null angenommen.
	 *
	 * @param is der {@link InputStream} mit dem JSON-Patch
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public Response patch(final InputStream is) {
		return this.patch(null, is);
	}

	/**
	 * Wendet die angegebenen Mappings für die Attribute des Core-DTOs (übergebene
	 * Map) auf das übergebene DatenbankDTO an.
	 *
	 * @param <DTO>           Der Typ des Datenbank-DTOs
	 *
	 * @param conn            die Datenbankverbindung
	 * @param dto             das Datenbank-DTO
	 * @param map             eine Map mit den Attributen und den Attributwerten des
	 *                        Core-DTOs
	 * @param attributeMapper eine Map mit den Mappingfunktionen zum mappen von
	 *                        Core-DTO-Attributen auf Datenbank-DTO-Attributen
	 * @param attributesForbidden eine Menge von Attributen, die nicht im JSON-Inputstream enthalten sein dürfen, null falls nicht gefiltert werden soll
	 */
	protected static <DTO> void applyPatchMappings(final DBEntityManager conn, final DTO dto, final Map<String, Object> map, final Map<String, DataBasicMapper<DTO>> attributeMapper, final Set<String> attributesForbidden) {
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			if (attributesForbidden != null && attributesForbidden.contains(key))
				throw OperationError.FORBIDDEN.exception("Attribut %s darf nicht im Patch enthalten sein.".formatted(key));
			final DataBasicMapper<DTO> mapper = attributeMapper.get(key);
			if (mapper == null)
				throw OperationError.BAD_REQUEST.exception();
			mapper.map(conn, dto, value, map);
		}
	}

	/**
	 * Passt die Informationen des Datenbank-DTO mit der angegebenen ID mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an. Dabei werden nur die
	 * übergebenen Mappings zugelassen.
	 *
	 * @param <DTO>           der Typ des DTOs
	 * @param id              die ID des zu patchenden DTOs
	 * @param is              der Input-Stream
	 * @param dtoClass        die Klasse des DTOs
	 * @param attributeMapper die Mapper für das Anpassen des DTOs
	 * @param attributesForbidden eine Menge von Attributen, die nicht im JSON-Inputstream enthalten sein dürfen, null falls nicht gefiltert werden soll
	 *
	 * @return die Response
	 */
	protected <DTO> Response patchBasicFiltered(final ID id, final InputStream is, final Class<DTO> dtoClass, final Map<String, DataBasicMapper<DTO>> attributeMapper, final Set<String> attributesForbidden) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Ein Patch mit der ID null ist nicht möglich.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return OperationError.NOT_FOUND.getResponse("In dem Patch sind keine Daten enthalten.");
		final DTO dto = conn.queryByKey(dtoClass, id);
		if (dto == null)
			throw OperationError.NOT_FOUND.exception();
		applyPatchMappings(conn, dto, map, attributeMapper, attributesForbidden);
		conn.transactionPersist(dto);
		conn.transactionFlush();
		return Response.status(Status.OK).build();
	}

	/**
	 * Passt die Informationen des Datenbank-DTO mit der angegebenen ID mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an. Dabei werden nur die
	 * übergebenen Mappings zugelassen.
	 *
	 * @param <DTO>           der Typ des DTOs
	 * @param id              die ID des zu patchenden DTOs
	 * @param is              der Input-Stream
	 * @param dtoClass        die Klasse des DTOs
	 * @param attributeMapper die Mapper für das Anpassen des DTOs
	 *
	 * @return die Response
	 */
	protected <DTO> Response patchBasic(final ID id, final InputStream is, final Class<DTO> dtoClass, final Map<String, DataBasicMapper<DTO>> attributeMapper) {
		return patchBasicFiltered(id, is, dtoClass, attributeMapper, null);
	}


	/**
	 * Erstellt und initialisiert eine neues DTO.
	 *
	 * @param <DTO>      der Type des DTO
	 * @param dtoClass   die Klasse des DTO
	 * @param initDTO    der Consumer zum Initialisieren des DTO
	 *
	 * @return das neue DTO
	 */
	protected <DTO> DTO newDTO(final Class<DTO> dtoClass, final ObjLongConsumer<DTO> initDTO) {
		return newDTO(dtoClass, conn.transactionGetNextID(dtoClass), initDTO);
	}


	/**
	 * Erstellt und initialisiert eine neues DTO.
	 *
	 * @param <DTO>      der Type des DTO
	 * @param dtoClass   die Klasse des DTO
	 * @param newID      die neue ID für das DTO
	 * @param initDTO    der Consumer zum Initialisieren des DTO
	 *
	 * @return das neue DTO
	 */
	protected <DTO> DTO newDTO(final Class<DTO> dtoClass, final long newID, final ObjLongConsumer<DTO> initDTO) {
		try {
			// Erstelle ein neues DTO für die DB und wende Initialisierung und das Mapping der Attribute an
			final Constructor<DTO> constructor = dtoClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			final DTO dto = constructor.newInstance();
			initDTO.accept(dto, newID);
			return dto;
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				throw webAppException;
			throw OperationError.INTERNAL_SERVER_ERROR.exception(e);
		}
	}


	/**
	 * Fügt ein neues DTO des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden
	 *
	 * @param <DTO>              der Typ des Datenbank-DTOs
	 * @param <CoreData>         der Typ des Core-DTOs
	 * @param newID              die neue ID für das DTO
	 * @param map                die Map für den Zugriff auf die Attribute
	 * @param dtoClass           die Klasse des DTOs
	 * @param initDTO            ein BiConsumer zum Initialisieren des
	 *                           Datenbank-DTOs
	 * @param dtoMapper          die Funktion zum Erstellen
	 * @param attributesRequired eine Menge der benötigten Attribute im
	 *                           JSON-Inputstream, um das Objekt zu initialisiesen
	 * @param attributeMapper    die Mapper für das Anpassen des DTOs
	 *
	 * @return das Core-DTO
	 */
	protected <DTO, CoreData> CoreData addBasic(final long newID, final Map<String, Object> map, final Class<DTO> dtoClass, final ObjLongConsumer<DTO> initDTO, final Function<DTO, CoreData> dtoMapper,
			final Set<String> attributesRequired, final Map<String, DataBasicMapper<DTO>> attributeMapper) {
		// Prüfe, ob alle relevanten Attribute im JSON-Inputstream vorhanden sind
		for (final String attr : attributesRequired)
			if (!map.containsKey(attr))
				throw OperationError.BAD_REQUEST.exception("Das Attribut %s fehlt in der Anfrage".formatted(attr));
		// Erstelle ein neues DTO für die DB und wende Initialisierung und das Mapping der Attribute an
		final DTO dto = newDTO(dtoClass, newID, initDTO);
		applyPatchMappings(conn, dto, map, attributeMapper, null);
		// Persistiere das DTO in der Datenbank
		if (!conn.transactionPersist(dto))
			throw OperationError.INTERNAL_SERVER_ERROR.exception();
		conn.transactionFlush();
		return dtoMapper.apply(dto);
	}


	/**
	 * Fügt ein neues DTO des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden
	 *
	 * @param <DTO>              der Typ des Datenbank-DTOs
	 * @param <CoreData>         der Typ des Core-DTOs
	 * @param is                 der Input-Stream
	 * @param dtoClass           die Klasse des DTOs
	 * @param initDTO            ein BiConsumer zum Initialisieren des
	 *                           Datenbank-DTOs
	 * @param dtoMapper          die Funktion zum Erstellen
	 * @param attributesRequired eine Menge der benötigten Attribute im
	 *                           JSON-Inputstream, um das Objekt zu initialisiesen
	 * @param attributeMapper    die Mapper für das Anpassen des DTOs
	 *
	 * @return die Response mit dem Core-DTO
	 */
	protected <DTO, CoreData> Response addBasic(final InputStream is, final Class<DTO> dtoClass, final ObjLongConsumer<DTO> initDTO, final Function<DTO, CoreData> dtoMapper,
			final Set<String> attributesRequired, final Map<String, DataBasicMapper<DTO>> attributeMapper) {
		final long newID = conn.transactionGetNextID(dtoClass);
		final var daten = this.addBasic(newID, JSONMapper.toMap(is), dtoClass, initDTO, dtoMapper, attributesRequired, attributeMapper);
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Fügt mehrere neue DTOs des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank jeweils eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden.
	 *
	 * @param <DTO>              der Typ des Datenbank-DTOs
	 * @param <CoreData>         der Typ des Core-DTOs
	 * @param is                 der Input-Stream
	 * @param dtoClass           die Klasse des DTOs
	 * @param initDTO            ein BiConsumer zum Initialisieren des
	 *                           Datenbank-DTOs
	 * @param dtoMapper          die Funktion zum Erstellen
	 * @param attributesRequired eine Menge der benötigten Attribute im
	 *                           JSON-Inputstream, um das Objekt zu initialisiesen
	 * @param attributeMapper    die Mapper für das Anpassen des DTOs
	 *
	 * @return die Response mit dem Core-DTO
	 */
	protected <DTO, CoreData> Response addBasicMultiple(final InputStream is, final Class<DTO> dtoClass, final ObjLongConsumer<DTO> initDTO, final Function<DTO, CoreData> dtoMapper,
			final Set<String> attributesRequired, final Map<String, DataBasicMapper<DTO>> attributeMapper) {
		// Bestimme die nächste verfügbare ID für das DTO
		long newID = conn.transactionGetNextID(dtoClass);
		// Und jetzt durchwandere die einzelnen hinzuzufügenden Objekte
		final List<Map<String, Object>> multipleMaps = JSONMapper.toMultipleMaps(is);
		final List<CoreData> daten = new ArrayList<>();
		for (final Map<String, Object> map : multipleMaps)
			daten.add(this.addBasic(newID++, map, dtoClass, initDTO, dtoMapper, attributesRequired, attributeMapper));
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}




	/**
	 * Entfernt das Datenbank-DTO mit der angegebenen ID und gibt das zugehörige
	 * Core-DTO in der Response zurück.
	 *
	 * @param <DTO>      der Typ des Datenbank-DTOs
	 * @param <CoreData> der Typ des Core-DTOs
	 * @param id         die ID
	 * @param dtoClass   die Klasse des Datenbank-DTOs
	 * @param dtoMapper  der Mapper für das Mapping eines Datenbank-DTOs auf ein
	 *                   Core-DTO
	 *
	 * @return die Response - im Erfolgsfall mit dem gelöschten Core-DTO
	 */
	protected <DTO, CoreData> Response deleteBasic(final Object id, final Class<DTO> dtoClass, final Function<DTO, CoreData> dtoMapper) {
		// Bestimme das DTO
		if (id == null)
			throw OperationError.NOT_FOUND.exception("Es muss eine ID angegeben werden. Null ist nicht zulässig.");
		final DTO dto = conn.queryByKey(dtoClass, id);
		if (dto == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein DTO mit der ID %s gefunden.".formatted(id));
		final CoreData daten = dtoMapper.apply(dto);
		// Entferne das DTO
		conn.transactionRemove(dto);
		conn.transactionFlush();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Entfernt die Datenbank-DTOs mit den angegebenen IDs und gibt die zugehörigen
	 * Core-DTOs in der Response zurück.
	 *
	 * @param <DTO>      der Typ des Datenbank-DTOs
	 * @param <CoreData> der Typ des Core-DTOs
	 * @param ids        die IDs
	 * @param dtoClass   die Klasse der Datenbank-DTOs
	 * @param dtoMapper  der Mapper für das Mapping eines Datenbank-DTOs auf ein
	 *                   Core-DTO
	 *
	 * @return die Response - im Erfolgsfall mit den gelöschten Core-DTOs
	 */
	protected <DTO, CoreData> Response deleteBasicMultiple(final List<? extends Object> ids, final Class<DTO> dtoClass, final Function<DTO, CoreData> dtoMapper) {
		// Bestimme die DTOs
		if (ids == null)
			throw OperationError.NOT_FOUND.exception("Es müssen IDs angegeben werden. Null ist nicht zulässig.");
		final List<DTO> dtos = conn.queryNamed(dtoClass.getSimpleName() + ".primaryKeyQuery.multiple", ids, dtoClass);
		if (dtos == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine DTOs mit den angegebenen IDs gefunden.");
		final List<CoreData> daten = new ArrayList<>();
		for (final DTO dto : dtos) {
			daten.add(dtoMapper.apply(dto));
			conn.transactionRemove(dto);
			conn.transactionFlush();
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
