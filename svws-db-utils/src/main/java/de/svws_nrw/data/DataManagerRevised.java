package de.svws_nrw.data;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
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

	/** Ein Set von Attributen, welche neben der ID explizit notwendig sind und beim Erstellen eines DTOs gemappt werden müssen. */
	private final Set<String> attributesRequiredOnCreation = new HashSet<>();

	/** Ein Set von Attributen, wo das Patchen explizit verboten ist und nur beim Erstellen von DTOs ein Mapping erlaubt ist. */
	private final Set<String> attributesNotPatchable = new HashSet<>();

	/** Ein Set von Attributen, wo das Mapping beim Hinzufügen in einem zweiten Schritt passiert, nachdem das Datenbank-DTO ein erstes Mal persistiert wurde. */
	private final Set<String> attributesDelayedOnCreation = new HashSet<>();


	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn               die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	protected DataManagerRevised(final DBEntityManager conn) {
		this.conn = Objects.requireNonNull(conn, "DBEntityManager darf nicht null sein.");
		this.classDatabaseDTO = getClassDatabaseDTO();
	}


	/**
	 * Methode liefert die Class des Generics für das Datenbank-DTO.
	 *
	 * @return Class des Datenbank-DTO
	 */
	@SuppressWarnings("unchecked")
	Class<DatabaseDTO> getClassDatabaseDTO() {
		final ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		return ((Class<DatabaseDTO>) type.getActualTypeArguments()[1]);
	}


	/**
	 * Methode liefert die Class des Generics für die ID.
	 *
	 * @return Class der ID
	 */
	@SuppressWarnings("unchecked")
	Class<ID> getClassID() {
		final ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		return ((Class<ID>) type.getActualTypeArguments()[0]);
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
	 * Erzeugt bzw. ermittelt die custom ID für ein neues oder zu veränderndes Datenbank-DTO anhand der übergebenen Attribute.<br>
	 * <b>Wichtig:</b> Wird eine neue ID benötigt, die abweichend von der Default Implementierung in der Methode {@link #createNextLongID(Long)} ist oder
	 * nicht vom Typ {@link Long} ist, muss diese Methode überschrieben werden. Ebenfalls muss die Methode implementiert werden, wenn die Patch-Operation
	 * {@link #patchMultipleAsResponse(InputStream)} genutzt wird.
	 *
	 * @param attributes die Map mit initialen oder zu patchenden Attributen für ein DTO
	 *
	 * @return die custom ID
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected ID getID(final Map<String, Object> attributes) throws ApiOperationException {
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Methode getID() ist standardmäßig nicht implementiert.");
	}


	/**
	 * Erstellt und initialisiert ein neues Datenbank-DTO.
	 *
	 * @param newID      	   die neue ID für das DTO
	 * @param initAttributes   die Attribute zur Initialisierung
	 *
	 * @return das neue DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected DatabaseDTO newDTO(final ID newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		try {
			// Erstelle ein neues DTO für die DB und wende Initialisierung und das Mapping der Attribute an
			final Constructor<DatabaseDTO> constructor = classDatabaseDTO.getDeclaredConstructor();
			constructor.setAccessible(true);
			final DatabaseDTO dto = constructor.newInstance();
			initDTO(dto, newID, initAttributes);
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
	 * @param dto              das Datenbank-DTO
	 * @param id               die ID
	 * @param initAttributes   die Attribute zur Initialisierung
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected void initDTO(final DatabaseDTO dto, final ID id, final Map<String, Object> initAttributes) throws ApiOperationException {
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Methode initDTO() ist standardmäßig nicht implementiert.");
	}


	/**
	 * Wandelt das Datenbank-DTO in das Core-DTO um, soweit die Daten
	 * in dem Datenbank-DTO enthalten sind.
	 *
	 * @param dto   das Datenbank-DTO
	 *
	 * @return das neu erstellte Core-DTO
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected abstract CoreDTO map(DatabaseDTO dto) throws ApiOperationException;

	/**
	 * Wandelt die Datenbank-DTOs in der übergebenen Collection in Core-DTOs und gibt die Liste dieser umgewandelten DTOs zurück.
	 *
	 * @param dtos   die Collection der Datenbank-DTOs
	 *
	 * @return die Liste der Core-DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<CoreDTO> mapList(final Collection<DatabaseDTO> dtos) throws ApiOperationException {
		final List<CoreDTO> daten = new ArrayList<>();
		for (final DatabaseDTO dto : dtos)
			daten.add(map(dto));
		return daten;
	}


	/**
	 * Führt das Mapping eines Attributes des Core-DTOs auf das zugehörige Datenbank-DTO durch.<br>
	 * <b>Wichtig:</b> Diese Methode muss überschrieben werden, damit die Add-Methoden und Patch-Methoden ausführbar sind.
	 *
	 * @param dto     das Datenbank-DTO
	 * @param name    der Name des Core-DTO-Attributes
	 * @param value   der Wert des Core-DTO-Attributes
	 * @param map     die map von Attribut-Namen des Core-DTOs auf den zugehörigen Attributwertes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	protected void mapAttribute(final DatabaseDTO dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Methode mapAttribute() ist standardmäßig nicht implementiert.");
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
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Methode getAll() ist standardmäßig nicht implementiert.");
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
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Methode getList() ist standardmäßig nicht implementiert.");
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
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Methode getById() ist standardmäßig nicht implementiert.");
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
	 * @param dto                das Datenbank-DTO
	 * @param patchMappings      eine Map mit den Attributen und den Attributwerten des Core-DTOs
	 * @param attributesToPatch  eine Menge von Attributen, die gepatched werden sollen; <code>null</code> wenn alle Attribute berücksichtigt werden sollen
	 * @param attributesToSkip   eine Menge von Attributen, die beim Patch ausgelassen werden sollen
	 * @param isCreation         gibt an, ob es sich um ein neues DTO handelt. Wenn <code>true</code>, dann werden die Attribute aus
	 *                           {@link #attributesNotPatchable} ignoriert.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected void applyPatchMappings(final DatabaseDTO dto, final Map<String, Object> patchMappings, final Set<String> attributesToPatch,
			final Set<String> attributesToSkip, final boolean isCreation) throws ApiOperationException {

		// Beim initialen Erstellen eines DTOs werden die "not patchable" Attribute ignoriert
		if (!isCreation) {
			// Prüfe, ob Attribute enthalten sind, die nicht gepacht werden dürfen und es werfe ggf. eine ApiOperationException
			final String notPatchableAttrsStr = patchMappings.keySet().stream()
					.filter(key -> (attributesToPatch == null) || attributesToPatch.contains(key))
					.filter(attributesNotPatchable::contains)
					.collect(Collectors.joining(","));

			if (!notPatchableAttrsStr.isBlank())
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Folgende Attribute werden für ein Patch nicht zugelassen: %s.".formatted(String.join(",", notPatchableAttrsStr)));
		}

		for (final Entry<String, Object> patchMapping : patchMappings.entrySet()) {
			final String key = patchMapping.getKey();
			final Object value = patchMapping.getValue();

			// Es wird geprüft, ob das Attribut ausgelassen werden soll oder nicht bei den zu patchenden Attributen dabei ist
			if (attributesToSkip.contains(key) || ((attributesToPatch != null) && !attributesToPatch.contains(key)))
				continue;

			// Patching für Attribut auf DTO anwenden
			mapAttribute(dto, key, value, patchMappings);
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
	 * Passt die Informationen des Datenbank-DTO mit der angegebenen ID mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an. Dabei werden nur die
	 * übergebenen Mappings zugelassen.
	 *
	 * @param id              die ID des zu patchenden DTOs
	 * @param is              der Input-Stream
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public CoreDTO patchFromStream(final ID id, final InputStream is) throws ApiOperationException {
		return patch(id, JSONMapper.toMap(is));
	}


	/**
	 * Passt die Informationen der Datenbank-DTOs mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an. Dabei werden nur die
	 * übergebenen Mappings zugelassen.
	 *
	 * @param is              der Input-Stream
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response patchMultipleAsResponse(final InputStream is) throws ApiOperationException {
		for (final Map<String, Object> map : JSONMapper.toMultipleMaps(is))
			patch(getID(map), map);
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
		final Map<String, Object> initAttributes = JSONMapper.toMap(is);
		final var daten = this.addBasic(getNextID(null, initAttributes), initAttributes);
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
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(addMultiple(is)).build();
	}

	/**
	 * Fügt mehrere neue DTOs in die Datenbank hinzu, indem in der Datenbank jeweils eine neue ID
	 * abgefragt wird und die Attribute des JSON-Objektes gemäß dem Attribut-Mapper integriert werden.
	 *
	 * @param is   der Input-Stream
	 *
	 * @return die Liste mit den Core-DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<CoreDTO> addMultiple(final InputStream is) throws ApiOperationException {
		// initiale Attribute der anzulegenden DTOs durchlaufen
		final List<Map<String, Object>> multipleMaps = JSONMapper.toMultipleMaps(is);
		final List<CoreDTO> daten = new ArrayList<>();
		ID newID = null;
		for (final Map<String, Object> attributeMap : multipleMaps) {
			newID = getNextID(newID, attributeMap);
			daten.add(this.addBasic(newID, attributeMap));
		}
		return daten;
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
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen muss eine ID angegeben werden. Null ist nicht zulässig.");

		// DTO mit Hilfe der ID ermitteln
		final DatabaseDTO dbDTO = conn.queryByKey(classDatabaseDTO, id);
		if (dbDTO == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Entität mit der ID %s gefunden.".formatted(id));

		// Prüfen, ob das Datenbank-DTO gelöscht werden darf
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
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");

		final List<CoreDTO> deletedCoreDTOs = new ArrayList<>();
		if (!ids.isEmpty()) {
			// DTOs mit Hilfe der IDs ermitteln
			final List<DatabaseDTO> dbDTOs = conn.queryByKeyList(classDatabaseDTO, ids);
			if (dbDTOs.isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Entitäten zu den IDs gefunden.");

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
	 * Prüft, ob der Benutzer ausschließlich eine funktionsbezogene Kompetenz besitzt und nicht noch zusätzlich eine übergreifende Kompetenz.
	 *
	 * @param kompetenzFunktionsbezogen   die zu prüfende funktionsbezogene Kompetenz
	 * @param kompetenzenUebergreifend   die zu prüfenden übergreifenden Kompetenzen
	 *
	 * @return <code>true</code>, wenn der Benutzer ausschließlich die funktionsbezogene Kompetenz besitzt.
	 * Ansonsten <code>false</code> wenn der Benutzer eine übergreifende Kompetenz besitzt,
	 * bei der eine weiterführende Prüfung nicht mehr notwendig ist.
	 * <br>
	 * Throws {@link IllegalArgumentException} wenn einer der Methodenparameter fehlt.
	 */
	public boolean hatBenutzerNurFunktionsbezogeneKompetenz(final @NotNull BenutzerKompetenz kompetenzFunktionsbezogen,
			final @NotNull Set<BenutzerKompetenz> kompetenzenUebergreifend) {
		if ((kompetenzFunktionsbezogen == null) || (kompetenzenUebergreifend == null) || kompetenzenUebergreifend.isEmpty())
			throw new IllegalArgumentException("Die Parameter kompetenzFunktionsbezogen und kompetenzenUebergreifend dürfen nicht null oder leer sein.");

		final Benutzer user = conn.getUser();
		final boolean hatUebergreifendeKompetenz = kompetenzenUebergreifend.stream().anyMatch(user::hatVerwendeteKompetenz);
		if (hatUebergreifendeKompetenz)
			return false;

		return user.hatVerwendeteKompetenz(kompetenzFunktionsbezogen);
	}


	/**
	 * Prüft, ob der Benutzer für die angegebene Klasse funktionsbezogene Rechte hat oder nicht.
	 *
	 * @param idKlasse   die ID der zu prüfenden Klasse
	 *
	 * @throws ApiOperationException wenn der Benutzer nicht die Kompetenz für den funktionsbezogenen Zugriff auf die Daten der Klasse hat (403 - FORBIDDEN).
	 */
	public void checkBenutzerFunktionsbezogeneKompetenzKlasse(final Long idKlasse) throws ApiOperationException {
		if (idKlasse == null)
			throw new ApiOperationException(Status.FORBIDDEN,
					"Der Benutzer kann keine funktionsbezogene Kompetenz nutzen, um auf Daten zuzugreifen, die keiner Klasse zugeordnet sind.");

		final boolean hatKompetenzFuerKlasse = conn.getUser().getKlassenIDs().contains(idKlasse);
		if (!hatKompetenzFuerKlasse) {
			final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, idKlasse);
			DTOSchuljahresabschnitte schuljahresabschnitt = null;
			if (klasse != null) {
				schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, klasse.Schuljahresabschnitts_ID);
			}

			final String kuerzel = (klasse != null) ? klasse.Klasse : "N/A";
			final String jahrUndAbschnitt = (schuljahresabschnitt != null)
					? (schuljahresabschnitt.Jahr + "." + schuljahresabschnitt.Abschnitt)
					: "N/A";

			throw new ApiOperationException(Status.FORBIDDEN,
					"Der Benutzer hat keine funktionsbezogene Kompetenz für den Zugriff auf die Daten der Klasse %s (ID: %d) des Schuljahresabschnittes %s"
							.formatted(kuerzel, idKlasse, jahrUndAbschnitt));
		}
	}


	/**
	 * Prüft, ob der Benutzer bei dem angegeben Abiturjahrgang als Beratungslehrer funktionsbezogene Rechte hat oder nicht.
	 *
	 * @param abijahrgang   der zu prüfende Abiturjahrgang
	 *
	 * @throws ApiOperationException wenn der Benutzer nicht die Kompetenz für den funktionsbezogenen Zugriff auf den Abiturjahrgang hat (503 - FORBIDDEN).
	 */
	public void checkBenutzerFunktionsbezogeneKompetenzAbiturjahrgang(final Integer abijahrgang) throws ApiOperationException {
		if (abijahrgang == null)
			throw new ApiOperationException(Status.FORBIDDEN,
					"Der Benutzer kann keine funktionsbezogene Kompetenz nutzen, um auf Daten zuzugreifen, die keinem Abiturjahrgang zugeordnet sind.");
		final boolean hatKompetenzFuerAbijahrgang = conn.getUser().getAbiturjahrgaenge().contains(abijahrgang);
		if (!hatKompetenzFuerAbijahrgang)
			throw new ApiOperationException(Status.FORBIDDEN,
					"Der Benutzer hat keine funktionsbezogene Kompetenz für den Zugriff als Beratungslehrer auf den Abiturjahrgang " + abijahrgang);
	}


	/**
	 * Methode prüft vor dem Löschen, ob alle Vorbedingungen zum Löschen erfüllt sind.
	 * Standardmäßig hat diese Methode keine Implementierung.
	 * Wenn eine Prüfung durchgeführt werden soll, muss diese Methode überschrieben werden.
	 *
	 * @param dtos   die Datanbank-DTOs, die gelöscht werden sollen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public void checkBeforeDeletion(final List<DatabaseDTO> dtos) throws ApiOperationException {
		// Methode kann überschrieben werden
	}


	/**
	 * Methode prüft vor dem Erstellen eines neuen Datenbank-DTOs, ob alle Vorbedingungen zum Erstellen erfüllt sind.
	 * Standardmäßig hat diese Methode keine Implementierung.
	 * Wenn eine Prüfung durchgeführt werden soll, muss diese Methode überschrieben werden.
	 *
	 * @param newID            die neue ID für das DTO
	 * @param initAttributes   die Map mit den initialen Attributen für das neue DTO
	 *
	 * @throws ApiOperationException wird geworfen, wenn eine Vorbedingung nicht erfüllt wurde
	 */
	public void checkBeforeCreation(final ID newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		// Methode kann überschrieben werden
	}


	/**
	 * Methode prüft vor dem Patchen eines Datenbank-DTOs, ob alle Vorbedingungen zum Patch erfüllt sind.
	 * Standardmäßig hat diese Methode keine Implementierung.
	 * Wenn eine Prüfung durchgeführt werden soll, muss diese Methode überschrieben werden.
	 *
	 * @param dto               das DTO
	 * @param patchAttributes   die Map mit den zu patchenden Attributen für das DTO
	 *
	 * @throws ApiOperationException wird geworfen, wenn eine Vorbedingung nicht erfüllt wurde
	 */
	public void checkBeforePatch(final DatabaseDTO dto, final Map<String, Object> patchAttributes) throws ApiOperationException {
		// Methode kann überschrieben werden
	}


	/**
	 * Passt die Informationen des Datenbank-DTO mit der angegebenen ID mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an. Dabei werden nur die
	 * übergebenen Mappings zugelassen.
	 *
	 * @param id              die ID des zu patchenden DTOs
	 * @param attributesToPatch    die Map mit dem Mapping der Attributnamen auf die Werte der Attribute im Patch
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public CoreDTO patch(final ID id, final Map<String, Object> attributesToPatch) throws ApiOperationException {
		// Prüfe, ob benötigte Parameter übergeben wurden
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Patchen muss eine ID angegeben werden. Null ist nicht zulässig.");
		if (attributesToPatch.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "In dem Patch sind keine Daten enthalten.");

		// zu patchendes DTO mit ID aus der Datenbank laden
		final DatabaseDTO dto = conn.queryByKey(classDatabaseDTO, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Entität für die angegebene ID wurden in der Datenbank nicht gefunden.");

		checkBeforePatch(dto, attributesToPatch);

		// Patchings auf DTO anwenden und anschließend in DB persistieren
		applyPatchMappings(dto, attributesToPatch, null, Collections.emptySet(), false);
		saveDatabaseDTO(dto);
		return map(dto);
	}

	/**
	 * Fügt ein neues DTO des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden. Um zu gewährleisten, dass der Primärschlüssel
	 * angelegt ist, wird das Patchen von einzelnen Attributen zurückgestellt und erst nach
	 * dem Persistieren des Objektes in einem zweiten Schritt durchgeführt.
	 *
	 * @param initAttributes  die Map mit den initialen Attributen für das neue DTO
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public CoreDTO add(final Map<String, Object> initAttributes) throws ApiOperationException {
		return addBasic(getNextID(null, initAttributes), initAttributes);
	}

	/**
	 * Fügt ein neues DTO des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden. Um zu gewährleisten, dass der Primärschlüssel
	 * angelegt ist, wird das Patchen von einzelnen Attributen zurückgestellt und erst nach
	 * dem Persistieren des Objektes in einem zweiten Schritt durchgeführt.
	 *
	 * @param initAttributes  die Map mit den initialen Attributen für das neue DTO
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response addFromMapAsResponse(final Map<String, Object> initAttributes) throws ApiOperationException {
		final CoreDTO dto = addBasic(getNextID(null, initAttributes), initAttributes);
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(dto).build();
	}

	/**
	 * Fügt ein neues DTO des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden. Um zu gewährleisten, dass der Primärschlüssel
	 * angelegt ist, wird das Patchen von einzelnen Attributen zurückgestellt und erst nach
	 * dem Persistieren des Objektes in einem zweiten Schritt durchgeführt.
	 *
	 * @param newID           die neue ID für das DTO
	 * @param initAttributes  die Map mit den initialen Attributen für das neue DTO
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected CoreDTO addBasic(final ID newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		// Prüfe, ob alle relevanten Attribute für das Erstellen des DTOs übergeben wurden
		final String missingAttrsStr = attributesRequiredOnCreation.stream().filter(attr -> !initAttributes.containsKey(attr)).collect(Collectors.joining(","));
		if (!missingAttrsStr.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es werden weitere Attribute (%s) benötigt, damit die Entität erstellt werden kann.".formatted(missingAttrsStr));

		// Prüfe, ob alle Vorbedingung für das Erstellen des DTOs erfüllt sind
		checkBeforeCreation(newID, initAttributes);

		// Erstelle ein neues DTO für die DB und wende Initialisierung und das Mapping der Attribute an
		final DatabaseDTO dto = newDTO(newID, initAttributes);
		applyPatchMappings(dto, initAttributes, null, attributesDelayedOnCreation, true);

		// Persistiere das DTO in der Datenbank
		saveDatabaseDTO(dto);
		if (!attributesDelayedOnCreation.isEmpty()) {
			applyPatchMappings(dto, initAttributes, attributesDelayedOnCreation, Collections.emptySet(), true);
			saveDatabaseDTO(dto);
		}
		return getById(newID);
	}

	/**
	 * Fügt ein neues DTO des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden. Um zu gewährleisten, dass der Primärschlüssel
	 * angelegt ist, wird das Patchen von einzelnen Attributen zurückgestellt und erst nach
	 * dem Persistieren des Objektes in einem zweiten Schritt durchgeführt.
	 *
	 * @param is   der Input-Stream
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public CoreDTO addFromStream(final InputStream is) throws ApiOperationException {
		// Prüfe, ob alle relevanten Attribute für das Erstellen des DTOs übergeben wurden
		final Map<String, Object> initAttributes = JSONMapper.toMap(is);
		return this.addBasic(getNextID(null, initAttributes), initAttributes);
	}


	/**
	 * Methode persistiert das übergebene Datenbank-DTO in der Datenbank.
	 *
	 * @param dto Datenbank-DTO, das persistiert werden soll
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected void saveDatabaseDTO(final DatabaseDTO dto) throws ApiOperationException {
		if (!conn.transactionPersist(dto))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Entität.");
		conn.transactionFlush();
	}


	/**
	 * Methode löscht das übergebene Datenbank-DTO aus der Datenbank.
	 *
	 * @param dto Datenbank-DTO, das gelöscht werden soll
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected void deleteDatabaseDTO(final DatabaseDTO dto) throws ApiOperationException {
		if (!conn.transactionRemove(dto))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Entfernen der Entität.");
		conn.transactionFlush();
	}


	/**
	 * Bestimmt die nächste ID für ein neues Datenbank-DTO.
	 *
	 * @param lastID         die letzte ID
	 * @param initAttributes die Map mit den initialen Attributen für das neue DTO
	 *
	 * @return die neue ID
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@SuppressWarnings("unchecked")
	protected ID getNextID(final ID lastID, final Map<String, Object> initAttributes) throws ApiOperationException {
		if (getClassID().isAssignableFrom(Long.class))
			return (ID) createNextLongID((Long) lastID);
		return getID(initAttributes);
	}


	/**
	 * Erzeugt die nächste default ID vom Typ {@link Long} für ein neues Datenbank-DTO.
	 *
	 * @param lastID         die letzte ID
	 *
	 * @return die neue default ID
	 */
	protected Long createNextLongID(final Long lastID) {
		return (lastID == null) ? conn.transactionGetNextID(classDatabaseDTO) : (lastID + 1L);
	}

}
