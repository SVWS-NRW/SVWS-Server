package de.svws_nrw.data;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese abstrakte Klasse ist die Grundlage für das einheitliche Aggregieren von
 * Informationen für die OpenAPI und das einheitliche Bereitstellen von
 * Funktionalitäten für GET-, CREATE-, PATCH- und DELETE-Operationen.
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
	private final Set<String> attributesDelayedOnCreation = new HashSet<>();


	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn               die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	protected DataManagerRevised(final DBEntityManager conn) {
		this.conn = Objects.requireNonNull(conn, "DBEntityManager is required");
		this.classDatabaseDTO = getClassDatabaseDTO();
	}


	@SuppressWarnings("unchecked")
	private Class<DatabaseDTO> getClassDatabaseDTO() {
		final ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		return ((Class<DatabaseDTO>) type.getActualTypeArguments()[1]);
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
	protected void setAttributesDelayedOnCreation(final String... attrs) {
		attributesDelayedOnCreation.clear();
		attributesDelayedOnCreation.addAll(Arrays.asList(attrs));
	}


	/**
	 * Bestimmt die nächste ID für ein Datenbank-DTO. Ist der Typ der ID nicht Long, so muss diese Methode überschrieben
	 * werden, damit das Hinzufügen funktionieren kann.
	 *
	 * @param last   die letzte ID
	 *
	 * @return die neue ID
	 */
	@SuppressWarnings("unchecked")
	protected ID getNextID(final ID last) {
		final Long id = (last == null)
				? conn.transactionGetNextID(classDatabaseDTO)
				: (((Long) last) + 1L);
		return (ID) id;
	}


	/**
	 * Erstellt und initialisiert ein neues Datenbank-DTO.
	 *
	 * @param newID      	die neue ID für das DTO
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
	 * Initialisiert das Datenbank-DTO mit der übergebenen ID.<br>
	 * <b>Wichtig:</b> Diese Methode muss überschrieben werden, damit die Add-Methoden ausführbar sind.
	 *
	 * @param dto   das Datenbank-DTO
	 * @param id    die ID
	 */
	protected void initDTO(final DatabaseDTO dto, final ID id) throws ApiOperationException {
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
	protected abstract CoreDTO map(DatabaseDTO dto) throws ApiOperationException;


	/**
	 * Führt das Mapping eines Attributes des Core-DTOs auf das zugehörige Datenbank-DTO durch.<br>
	 * <b>Wichtig:</b> Diese Methode muss überschrieben werden, damit die Add-Methoden und Patch-Methoden ausführbar sind.
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
	 * verwendet, wenn auch eine Filterung bei der Methode {@link #getList()} implementiert wird.
	 * <b>Wichtig:</b> Diese Methode muss überschrieben werden, damit die Methode {@link #getAllAsResponse()} ausführbar ist.
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
	 * verwendet, wenn auch eine Filterung bei der Methode {@link #getList()} implementiert wird.
	 * Diese wird im Erfolgsfall als JSON eingebettet in einer HTTP-Response 200 zurückgegeben.
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
	 * Filterung in der abgeleiteten Klasse genutzt wird, so steht als zweite Option
	 * die Methode {@link #getAll()} zur Verfügung, um den Abruf aller Core-DTOs zu
	 * implementieren.
	 * <b>Wichtig:</b> Diese Methode muss überschrieben werden, damit die Methode {@link #getListAsResponse()} ausführbar ist.
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
	 * Diese wird im Erfolgsfall als JSON eingebettet in einer HTTP-Response 200 zurückgegeben.
	 *
	 * @return eine HTTP-Response mit der Liste der Core-DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getListAsResponse() throws ApiOperationException {
		final List<CoreDTO> coreDTOs = getList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(coreDTOs).build();
	}


	/**
	 * Ermittelt das Core-DTO mit der angegebenen ID.
	 * <b>Wichtig:</b> Diese Methode muss überschrieben werden, damit die Methode {@link #getByIdAsResponse(Object)} ausführbar ist.
	 *
	 * @param id   die ID
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public CoreDTO getById(final ID id) throws ApiOperationException {
		throw new UnsupportedOperationException();
	}


	/**
	 * Ermittelt das Core-DTO mit der angegebenen ID.
	 * Dieses wird im Erfolgsfall als JSON eingebettet in einer HTTP-Response 200 zurückgegeben.
	 *
	 * @param id   die ID
	 *
	 * @return eine HTTP-Response mit dem Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getByIdAsResponse(final ID id) throws ApiOperationException {
		final CoreDTO coreDTO = getById(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(coreDTO).build();
	}


	/**
	 * Wendet die angegebenen Mappings für die Attribute des Core-DTOs (übergebene Map) auf das übergebene Datenbank-DTO an.
	 *
	 * @param conn               die Datenbankverbindung
	 * @param dto                das Datenbank-DTO
	 * @param patchMappings      eine Map mit den Attributen und den Attributwerten des Core-DTOs
	 * @param attributesToPatch  eine Menge von Attributen, die gepatched werden sollen; <code>null</code> wenn alle Attribute berücksichtigt werden sollen
	 * @param attributesToSkip   eine Menge von Attributen, die beim Patch ausgelassen werden sollen
	 * @param isCreation         gibt an, ob es sich um ein neues DTO handelt. Wenn <code>true</code>, dann werden die Attribute aus
	 *                           {@link #attributesNotPatchable} ignoriert.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected void applyPatchMappings(final DBEntityManager conn, final DatabaseDTO dto, final Map<String, Object> patchMappings,
			final Set<String> attributesToPatch, final Set<String> attributesToSkip, final boolean isCreation) throws ApiOperationException {
		for (final Entry<String, Object> patchMapping : patchMappings.entrySet()) {
			final String key = patchMapping.getKey();
			final Object value = patchMapping.getValue();

			// Es wird geprüft, ob das Attribut ausgelassen werden soll oder nicht bei den zu patchenden Attributen enthalten ist
			if ((attributesToSkip.contains(key)) || ((attributesToPatch != null) && !attributesToPatch.contains(key)))
				continue;

			// Beim initialen Erstellen eines DTOs werden die "not patchable" Attribute ignoriert
			if (!isCreation && attributesNotPatchable.contains(key))
				throw new ApiOperationException(Status.BAD_REQUEST, "Attribut %s darf nicht im Patch enthalten sein.".formatted(key));

			mapAttribute(conn, dto, key, value, patchMappings);
		}
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
		// TODO ggf. Anpassung, so dass Status.OK mit den veränderten Daten zurückgegeben wird
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
		// TODO ggf. Anpassung, so dass Status.OK mit den veränderten Daten zurückgegeben wird
		return Response.status(Status.NO_CONTENT).build();
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
	public Response addMultipleAsResponse(final InputStream is) throws ApiOperationException {
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
	 * @param dtos   die Datanbank-DTOs, die gelöscht werden sollen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public void checkBeforeDeletion(final List<DatabaseDTO> dtos) throws ApiOperationException {
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
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Es muss eine ID angegeben werden. Null ist nicht zulässig.");

		// DTO mit Hilfe der ID ermitteln
		final DatabaseDTO dbDTO = conn.queryByKey(classDatabaseDTO, id);
		if (dbDTO == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein DTO mit der ID %s gefunden.".formatted(id));

		// Prüfen, ob das DTO gelöscht werden darf
		checkBeforeDeletion(List.of(dbDTO));

		// DTO erst komplett laden für die spätere Response und anschließend löschen
		final CoreDTO deletedCoreDTO = map(dbDTO);
		deleteDatabaseDTO(dbDTO);

		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(deletedCoreDTO).build();
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
		if (ids == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Es müssen IDs angegeben werden. Null ist nicht zulässig.");

		final List<CoreDTO> deletedCoreDTOs = new ArrayList<>();
		if (!ids.isEmpty()) {
			// DTOs mit Hilfe der IDs ermitteln
			final List<DatabaseDTO> dbDTOs = conn.queryByKeyList(classDatabaseDTO, ids);
			if (dbDTOs.isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine DTOs zu den IDs gefunden.");

			// Prüfen, ob alle DTOs gelöscht werden dürfen
			checkBeforeDeletion(dbDTOs);

			// DTOs erst komplett laden für die spätere Response und anschließend löschen
			for (final DatabaseDTO dbDTO : dbDTOs) {
				deletedCoreDTOs.add(map(dbDTO));
				deleteDatabaseDTO(dbDTO);
			}
		}

		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(deletedCoreDTOs).build();
	}


	/**
	 * Passt die Informationen des Datenbank-DTO mit der angegebenen ID mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an. Dabei werden nur die
	 * übergebenen Mappings zugelassen.
	 *
	 * @param id              die ID des zu patchenden DTOs
	 * @param attributeMap    die Map mit dem Mapping der Attributnamen auf die Werte der Attribute im Patch
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void patch(final ID id, final Map<String, Object> attributeMap) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Patch mit der ID null ist nicht möglich.");
		if (attributeMap.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "In dem Patch sind keine Daten enthalten.");
		final DatabaseDTO dto = conn.queryByKey(classDatabaseDTO, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Daten für die angegebene ID wurden in der Datenbank nicht gefunden.");
		applyPatchMappings(conn, dto, attributeMap, null, Collections.emptySet(), false);
		saveDatabaseDTO(dto);
	}


	/**
	 * Fügt ein neues DTO des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden. Um zu gewährleisten, dass der Primärschlüssel
	 * angelegt ist, wird das Patchen von einzelnen Attributen zurückgestellt und erst nach
	 * dem Persistieren des Objektes in einem zweiten Schritt durchgeführt.
	 *
	 * @param newID           die neue ID für das DTO
	 * @param mapAttributes   die Map für den Zugriff auf die Attribute
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private CoreDTO addBasic(final ID newID, final Map<String, Object> mapAttributes) throws ApiOperationException {
		// Prüfe, ob alle relevanten Attribute im JSON-Inputstream vorhanden sind
		for (final String attr : attributesRequiredOnCreation)
			if (!mapAttributes.containsKey(attr))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s wird für das Erstellen des DTOs benötigt.".formatted(attr));
		// Erstelle ein neues DTO für die DB und wende Initialisierung und das Mapping der Attribute an
		final DatabaseDTO dto = newDTO(newID);
		applyPatchMappings(conn, dto, mapAttributes, null, attributesDelayedOnCreation, true);
		// Persistiere das DTO in der Datenbank
		saveDatabaseDTO(dto);
		if (!attributesDelayedOnCreation.isEmpty()) {
			applyPatchMappings(conn, dto, mapAttributes, attributesDelayedOnCreation, Collections.emptySet(), true);
			saveDatabaseDTO(dto);
		}
		return getById(newID);
	}


	/**
	 * Methode persistiert das übergebene Datenbank-DTO in der Datenbank.
	 *
	 * @param dto Datenbank-DTO, das persistiert werden soll
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private void saveDatabaseDTO(final DatabaseDTO dto) throws ApiOperationException {
		if (!conn.transactionPersist(dto))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Daten.");
		conn.transactionFlush();
	}


	/**
	 * Methode löscht das übergebene Datenbank-DTO aus der Datenbank.
	 *
	 * @param dto Datenbank-DTO, das gelöscht werden soll
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private void deleteDatabaseDTO(final DatabaseDTO dto) throws ApiOperationException {
		if (!conn.transactionRemove(dto))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Entfernen der Daten.");
		conn.transactionFlush();
	}

}
