package de.svws_nrw.data;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese abstrakte Klasse ist die Grundlage für das einheitliche Aggregieren von
 * Informationen für die OpenAPI und das einheitliche Bereitstellen von
 * Funktionen, welche Daten für GET oder PATCH-Zugriff zur Verfügung stellen.
 *
 * @param <ID>            die Typ, welcher als ID für die Informationen verwendet wird.
 * @param <DatabaseDTO>   der Typ des zugrundeliegenden Datenbank-DTOs
 * @param <CoreDTO>       der Typ des zugrundeliegenden Core-DTOs
 */
public abstract class DataManagerRevised<ID, DatabaseDTO, CoreDTO> {

	/** Die Klasse des zugehörigen Datenbank-DTOs */
	final Class<DatabaseDTO> classDatabaseDTO;

	/** Die Datenbank-Verbindung zum Aggregieren der Informationen aus der DB und zum Schreiben der Informationen bzw. Teilinformationen */
	protected final DBEntityManager conn;

	/** Eine Menge von Attributen, welche neben der ID explizit notwendig sind und beim Erstellen eines DTOs gemappt werden müssen. */
	private final Set<String> attributesRequiredOnCreation = new HashSet<>();

	/** Eine Menge von Attributen, wo das Patchen explizit verboten ist und nur beim Erstellen von DTOs ein Mapping erlaubt ist. */
	private final Set<String> attributesNotPatchable = new HashSet<>();

	/** Eine Menge von Attributen, wo das Mapping beim Hinzufügen in einem Zweiten Schritt passiert, nachdem das Datenbank-DTO ein erstes
	 * mal persistiert wurde. */
	private final Set<String> attributesDelayedWhileAdding = new HashSet<>();



	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn               die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 * @param classDatabaseDTO   die Klasse des zugrundeliegenden Datenbank-DTOs
	 */
	protected DataManagerRevised(final DBEntityManager conn, final Class<DatabaseDTO> classDatabaseDTO) {
		this.conn = conn;
		this.classDatabaseDTO = classDatabaseDTO;
	}


	/**
	 * Setzt die Attribute, welche neben der ID explizit notwendig sind und beim Erstellen eines DTOs gemappt werden müssen.
	 *
	 * @param attrs   die Attribute
	 */
	protected void setAttributesRequiredOnCreation(final String... attrs) {
		attributesRequiredOnCreation.clear();
		attributesRequiredOnCreation.addAll(Arrays.asList(attrs));
	}


	/**
	 * Setzt die Attribute, wo das Patchen explizit verboten ist und nur beim Erstellen von DTOs ein Mapping erlaubt ist.
	 *
	 * @param attrs   die Attribute
	 */
	protected void setAttributesNotPatchable(final String... attrs) {
		attributesNotPatchable.clear();
		attributesNotPatchable.addAll(Arrays.asList(attrs));
	}


	/**
	 * Setzt die Attribute, wo das Mapping beim Hinzufügen in einem Zweiten Schritt passiert, nachdem das Datenbank-DTO ein erstes
	 * mal persistiert wurde. Dies dient z.B. dazu, dass das Patchen eines Attributes ggf. auch Einfluss auf andere Datenbank-Tabellen
	 * hat, wo eine Fremdschlüsselbeziehung besteht.
	 *
	 * @param attrs   die Attribute
	 */
	protected void setAttributesDelayedWhileAdding(final String... attrs) {
		attributesDelayedWhileAdding.clear();
		attributesDelayedWhileAdding.addAll(Arrays.asList(attrs));
	}


	/**
	 * Gibt die ID des Datenbank-DTOs zurück.
	 *
	 * @return die ID des Datenbank-DTOs
	 */
	protected abstract ID getIDFromDatabaseDTO(final DatabaseDTO dto);


	/**
	 * Bestimmt die nächste ID für ein Datenbank-DTO. Ist der Typ der ID nicht Long, so muss diese Methode überschrieben
	 * werden, damit das Hinzufügen funktionieren kann.
	 *
	 * @return die neue ID
	 */
	@SuppressWarnings("unchecked")
	protected ID getNextID(final ID last) {
		final Long id = (last == null)
				? conn.transactionGetNextID(classDatabaseDTO)
				: (((Long) last).longValue() + 1);
		return (ID) id;
	}


	/**
	 * Erstellt und initialisiert eine neues DTO.
	 *
	 * @param newID      die neue ID für das DTO
	 *
	 * @return das neue DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected DatabaseDTO newDTO(final ID newID) throws ApiOperationException {
		try {
			// Erstelle ein neues DTO für die DB und wende Initialisierung und das Mapping der Attribute an
			final Constructor<DatabaseDTO> constructor = classDatabaseDTO.getDeclaredConstructor();
			constructor.setAccessible(true);
			final DatabaseDTO dto = constructor.newInstance();
			initDTO(dto, newID);
			return dto;
		} catch (final Exception e) {
			if (e instanceof final ApiOperationException apiOperationException)
				throw apiOperationException;
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e);
		}
	}


	/**
	 * Initialisiert das Datenbank-DTO mit der übergebenen ID. Diese Methode muss überschrieben werden,
	 * damit die Add-Methoden ausführbar sind.
	 *
	 * @param dto   das Datenbank-DTO
	 * @param id    die ID
	 */
	protected void initDTO(final DatabaseDTO dto, final ID id) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Wandelt das Datenbank-DTO in das Core-DTO um, soweit die Daten
	 * in dem Datenbank-DTO enthalten sind.
	 *
	 * @param dto   das Datenbank-DTO
	 *
	 * @return das neu erstellte Core-DTO
	 */
	public abstract CoreDTO map(final DatabaseDTO dto);


	/**
	 * Führt das Mapping eines Attributes des Core-DTOs auf das zugehörige Datenbank-DTO durch.
	 *
	 * @param conn    die Datenbank-Verbindung für Datenbankzugriffe
	 * @param dto     das Datenbank-DTO
	 * @param name    der Name des Core-DTO-Attributes
	 * @param value   der Wert des Core-DTO-Attributes
	 * @param map     die map von Attribut-Namen des Core-DTOs auf den zugehörigen Attributwertes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	protected void mapAttribute(final DBEntityManager conn, final DatabaseDTO dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
	}


	/**
	 * Ermittelt eine Liste mit allen Core-DTOs aus der DB. Wird in seltenen Fällen
	 * verwendet, wenn auch eine Filterung bei der Methode getList implementieert wird.
	 *
	 * @return eine Liste der Core-DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<CoreDTO> getAll() throws ApiOperationException {
		throw new UnsupportedOperationException();
	}


	/**
	 * Ermittelt eine Liste mit allen Core-DTOs aus der DB. Wird in seltenen Fällen
	 * verwendet, wenn auch eine Filterung bei der Methode getList implementieert wird.
	 * Diese wird im Erfolgsfall als JSON eingebettet in einer HTTP-Response 200
	 * zurückgegeben.
	 *
	 * @return eine HTTP-Response mit der Liste der Core-DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getAllAsResponse() throws ApiOperationException {
		final var daten = getAll();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Ermittelt eine Liste mit Core-DTOs aus der DB. Wenn bei dieser Methode eine
	 * Filterung in der abgeleiteten Klasse genutzt wird, so seht als zweite Option
	 * die Methode getAll zur Verfügung, um den Abruf aller Datenbank-Werte zu
	 * implementieren.
	 *
	 * @return eine Liste der Core-DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<CoreDTO> getList() throws ApiOperationException {
		throw new UnsupportedOperationException();
	}


	/**
	 * Ermittelt eine Liste mit Core-DTOs aus der DB. Wenn bei dieser Methode eine
	 * Filterung in der abgeleiteten Klasse genutzt wird, so seht als zweite Option
	 * die Methode getAll zur Verfügung, um den Abruf aller Datenbank-Werte zu
	 * implementieren.
	 * Diese wird im Erfolgsfall als JSON eingebettet in einer HTTP-Response 200
	 * zurückgegeben.
	 *
	 * @return eine HTTP-Response mit der Liste der Core-DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getListAsResponse() throws ApiOperationException {
		final var daten = getList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Ermittelt das Core-DTO mit der angegebenen ID.
	 *
	 * @param id   die ID
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public CoreDTO get(final ID id) throws ApiOperationException {
		throw new UnsupportedOperationException();
	}


	/**
	 * Ermittelt das Core-DTO mit der angegebenen ID.
	 * Dieses wird im Erfolgsfall als JSON eingebettet in einer HTTP-Response 200
	 * zurückgegeben.
	 *
	 * @param id   die ID
	 *
	 * @return eine HTTP-Response mit dem Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getAsResponse(final ID id) throws ApiOperationException {
		final var daten = get(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Wendet die angegebenen Mappings für die Attribute des Core-DTOs (übergebene Map) auf das übergebene Datenbank-DTO an.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param dto             das Datenbank-DTO
	 * @param map             eine Map mit den Attributen und den Attributwerten des Core-DTOs
	 * @param attributes      ggf. eine Menge von Attributen, die gepatched werden sollen, null für alle
	 * @param attributesSkip  eine Menge von Attributen, die ausgelassen wird
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected void applyPatchMappings(final DBEntityManager conn, final DatabaseDTO dto, final Map<String, Object> map,
			final Set<String> attributes, final Set<String> attributesSkip) throws ApiOperationException {
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			if ((attributesSkip.contains(key)) || ((attributes != null) && !attributes.contains(key)))
				continue;
			if (attributesNotPatchable.contains(key))
				throw new ApiOperationException(Status.BAD_REQUEST, "Attribut %s darf nicht im Patch enthalten sein.".formatted(key));
			mapAttribute(conn, dto, key, value, map);
		}
	}


	/**
	 * Passt die Informationen des Datenbank-DTO mit der angegebenen ID mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an. Dabei werden nur die
	 * übergebenen Mappings zugelassen.
	 *
	 * @param id              die ID des zu patchenden DTOs
	 * @param map             die Map mit dem Mapping der Attributnamen auf die Werte der Attribute im Patch
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected void patch(final ID id, final Map<String, Object> map) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Patch mit der ID null ist nicht möglich.");
		if (map.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "In dem Patch sind keine Daten enthalten.");
		final DatabaseDTO dto = conn.queryByKey(classDatabaseDTO, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		applyPatchMappings(conn, dto, map, null, Collections.emptySet());
		conn.transactionPersist(dto);
		conn.transactionFlush();
	}


	/**
	 * Passt die Informationen des Datenbank-DTO mit der angegebenen ID mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an. Dabei werden nur die
	 * übergebenen Mappings zugelassen.
	 *
	 * @param id              die ID des zu patchenden DTOs
	 * @param is              der Input-Stream
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response patchAsResponse(final ID id, final InputStream is) throws ApiOperationException {
		patch(id, JSONMapper.toMap(is));
		return Response.status(Status.NO_CONTENT).build();
	}


	/**
	 * Passt die Informationen der Datenbank-DTOs mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an. Dabei werden nur die
	 * übergebenen Mappings zugelassen.
	 *
	 * @param idAttr          der Name des ID-Attributes der DTOs
	 * @param is              der Input-Stream
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@SuppressWarnings("unchecked")
	public Response patchMultipleAsResponse(final String idAttr, final InputStream is) throws ApiOperationException {
		final List<Map<String, Object>> multipleMaps = JSONMapper.toMultipleMaps(is);
		for (final Map<String, Object> map : multipleMaps)
			patch((ID) map.get(idAttr), map);
		return Response.status(Status.NO_CONTENT).build();
	}


	/**
	 * Fügt ein neues DTO des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden. Um zu gewährleisten, dass der Primärschlüssels
	 * angelegt ist, wird das Patchen von einzelnen Attributen zurückgestellt und erst nach
	 * dem Persistieren des Objektes in einem zweiten Schritt gepatched.
	 *
	 * @param newID              die neue ID für das DTO
	 * @param map                die Map für den Zugriff auf die Attribute
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private CoreDTO addBasic(final ID newID, final Map<String, Object> map) throws ApiOperationException {
		// Prüfe, ob alle relevanten Attribute im JSON-Inputstream vorhanden sind
		for (final String attr : attributesRequiredOnCreation)
			if (!map.containsKey(attr))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s fehlt in der Anfrage".formatted(attr));
		// Erstelle ein neues DTO für die DB und wende Initialisierung und das Mapping der Attribute an
		final DatabaseDTO dto = newDTO(newID);
		applyPatchMappings(conn, dto, map, null, attributesDelayedWhileAdding);
		// Persistiere das DTO in der Datenbank
		if (!conn.transactionPersist(dto))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		conn.transactionFlush();
		if (!attributesDelayedWhileAdding.isEmpty()) {
			applyPatchMappings(conn, dto, map, attributesDelayedWhileAdding, Collections.emptySet());
			if (!conn.transactionPersist(dto))
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
			conn.transactionFlush();
		}
		return get(newID);
	}


	/**
	 * Fügt ein neues DTO in die Datenbank hinzu, indem in der Datenbank eine neue ID abgefragt wird
	 * und die Attribute des JSON-Objektes gemäß dem Attribut-Mapper integriert werden.
	 *
	 * @param is   der Input-Stream
	 *
	 * @return die Response mit dem Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response addAsResponse(final InputStream is) throws ApiOperationException {
		final var daten = this.addBasic(getNextID(null), JSONMapper.toMap(is));
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Fügt mehrere neue DTOs in die Datenbank hinzu, indem in der Datenbank jeweils eine neue ID
	 * abgefragt wird und die Attribute des JSON-Objektes gemäß dem Attribut-Mapper integriert werden.
	 *
	 * @param is   der Input-Stream
	 *
	 * @return die Response mit dem Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response addMultipleAsResponse(final InputStream is)
			throws ApiOperationException {
		// Bestimme die nächste verfügbare ID für das DTO
		ID newID = getNextID(null);
		// Und jetzt durchwandere die einzelnen hinzuzufügenden Objekte
		final List<Map<String, Object>> multipleMaps = JSONMapper.toMultipleMaps(is);
		final List<CoreDTO> daten = new ArrayList<>();
		for (final Map<String, Object> map : multipleMaps) {
			daten.add(this.addBasic(newID, map));
			newID = getNextID(newID);
		}
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Prüft ggf. vor dem Löschen, ob die Daten gelöscht werden dürfen.
	 *
	 * @param ids   die IDs der Daten, die gelöscht werden sollen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public void checkBeforeDeletion(final List<ID> ids) throws ApiOperationException {
		// diese Methode kann für Checks vor dem Entfernen überschrieben werden
	}


	/**
	 * Entfernt das Datenbank-DTO mit der angegebenen ID und gibt das zugehörige
	 * Core-DTO in der Response zurück.
	 *
	 * @param id         die ID
	 *
	 * @return die Response - im Erfolgsfall mit dem gelöschten Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response deleteAsResponse(final ID id) throws ApiOperationException {
		// Bestimme das DTO
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es muss eine ID angegeben werden. Null ist nicht zulässig.");
		checkBeforeDeletion(List.of(id));
		final DatabaseDTO dto = conn.queryByKey(classDatabaseDTO, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein DTO mit der ID %s gefunden.".formatted(id));
		final CoreDTO daten = get(id);
		// Entferne das DTO
		conn.transactionRemove(dto);
		conn.transactionFlush();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Entfernt die Datenbank-DTOs mit den angegebenen IDs und gibt die zugehörigen
	 * Core-DTOs in der Response zurück.
	 *
	 * @param ids        die IDs
	 *
	 * @return die Response - im Erfolgsfall mit den gelöschten Core-DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response deleteMultipleAsResponse(final List<ID> ids) throws ApiOperationException {
		// Bestimme die DTOs
		if (ids == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es müssen IDs angegeben werden. Null ist nicht zulässig.");
		if (ids.isEmpty())
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new ArrayList<>()).build();
		checkBeforeDeletion(ids);
		final List<CoreDTO> daten = new ArrayList<>();
		if (ids.isEmpty())
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		final List<DatabaseDTO> dtos = conn.queryByKeyList(classDatabaseDTO, ids);
		if (dtos == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine DTOs mit den angegebenen IDs gefunden.");
		for (final DatabaseDTO dto : dtos) {
			final ID id = getIDFromDatabaseDTO(dto);
			daten.add(get(id));
			conn.transactionRemove(dto);
			conn.transactionFlush();
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
