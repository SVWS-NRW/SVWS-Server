package de.svws_nrw.davapi.data.dav;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;
import de.svws_nrw.davapi.util.icalendar.DateTimeUtil;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.DatumUhrzeitConverter;
import de.svws_nrw.db.dto.current.svws.dav.DTODavRessource;
import de.svws_nrw.db.dto.current.svws.dav.DTODavRessourceCollection;
import de.svws_nrw.db.dto.current.svws.dav.DTODavRessourceCollectionsACL;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse handhabt den Zugriff auf DAV-Ressource-Collections, Berechtigungen dazu und die zugehörigen Ressourcen, welche in
 * der SVWS-Datenbank gespeichert werden.
 */
public final class DavDBRepository {

	/** Die zu verwendende Datenbankverbindung */
	private final DBEntityManager conn;


	/**
	 * Erstellt ein neues Objekt für den Zugriff auf die in der Datenbank gespeicherten DAV-Ressource-Collections.
	 *
	 * @param conn   die Datenbankverbindung, welche für den Zugriff verwendet werden soll.
	 */
	public DavDBRepository(final DBEntityManager conn) {
		this.conn = conn;
	}


	/**
	 * Gibt wieder, ob der angemeldete Benutzer die gegebene Collection bearbeiten (aktualisieren oder einfügen) darf.
	 * Benutzer dürfen ihre eigene Sammlung bearbeiten, sofern sie die {@link BenutzerKompetenz} für eigene Kalender
	 * haben, Admins dürfen alle Kalender bearbeiten und neue anlegen
	 *
	 * @param collection   die Collection
	 *
	 * @return true, wenn der Benutzer die Collection bearbeiten darf, und ansonsten false
	 */
	private boolean allowModifaction(final DavCollection collection) {
		final Benutzer user = conn.getUser();
		if (user.istAdmin())
			return true;
		return user.pruefeKompetenz(BenutzerKompetenz.CALDAV_EIGENER_KALENDER)
				&& user.getId().equals(collection.besitzer) && (collection.id != null);
	}


	/**
	 * Erstellt ein neues Synctoken für den aktuellen Zeitpunkt als SQL-Timestamp-String
	 *
	 * @return das aktuelle Synctoken
	 */
	private static String getNewSyncToken() {
		final long now = Instant.now().toEpochMilli();
		return DatumUhrzeitConverter.instance.convertToEntityAttribute(new Timestamp(now));
	}


	/**
	 * Überträgt eine gegebene Ressource aus dem Datenbankformat in eine vereinfachte Repräsentation abhängig von gegebenen Query-Parametern
	 *
	 * @param dto           das Datenbankobjekt
	 * @param withPayload   gibt an, ob die Ressource mit dem Ressourceninhalt/Payload erzeugt werden soll
	 * @param permissions   die Berechtigungen des angemeldeten Nutzers auf diese Ressource
	 *
	 * @return die DavRessource ergänzt um Berechtigungen
	 */
	private static DavRessource mapDTODavRessource(final DTODavRessource dto, final boolean withPayload, final DavPermissions permissions) {
		final DavRessource r = new DavRessource();
		r.idCollection = dto.DavRessourceCollection_ID;
		r.id = dto.ID;
		r.uid = dto.UID;
		r.permissions = permissions;
		r.syncToken = DateTimeUtil.getTimeInMillis(dto.lastModified);
		if (withPayload) {
			r.kalenderEnde = dto.KalenderEnde;
			r.kalenderStart = dto.KalenderStart;
			r.kalenderTyp = dto.KalenderTyp;
			r.data = new String(dto.ressource, StandardCharsets.UTF_8);
			r.uid = dto.UID;
		}
		return r;
	}


	/**
	 * Überträgt eine Ressourcensammlung aus dem Datenbankformat in eine vereinfachte Repräsentation
	 *
	 * @param dto           das Datenbankobjekt
	 * @param permissions   die Berechtigungen des angemeldeten Nutzers
	 *
	 * @return eine vereinfachte Repräsentation des Datenbankobjekts ergänzt um die Berechtigungen
	 */
	private static @NotNull DavCollection mapDTODavRessourceCollection(final @NotNull DTODavRessourceCollection dto,
			final @NotNull DavPermissions permissions) {
		final DavCollection c = new DavCollection();
		c.anzeigename = dto.Anzeigename;
		c.beschreibung = dto.Beschreibung;
		c.id = dto.ID;
		c.syncToken = DateTimeUtil.getTimeInMillis(dto.SyncToken);
		c.typ = dto.Typ;
		c.besitzer = dto.Benutzer_ID;
		c.permissions = permissions;
		return c;
	}


	/**
	 * Gibt eine Map mit den Berechtigungen zu allen Collections zurück bei denen der aktuelle Benutzer mindestens Leserechte hat.
	 * Die Berechtigungen sind dabei der jeweiligen ID der Collections zugeordnet.
	 *
	 * @param idsFiltered   null, wenn keine Filterung vorliegt und ansonsten ein zusätzlicher Filter für die IDs der Collections,
	 *                      welche zurückgegeben werden
	 *
	 * @return die Map mit den Berechtigungen zu den Collection-IDs
	 */
	private Map<Long, DavPermissions> getReadableCollectionPermissionsById(final Collection<Long> idsFiltered) {
		return conn.queryList(DTODavRessourceCollectionsACL.QUERY_BY_BENUTZER_ID, DTODavRessourceCollectionsACL.class, conn.getUser().getId()).stream()
				.map(dto -> new DavPermissions(dto.berechtigungen, dto.RessourceCollection_ID, dto.Benutzer_ID))
				.filter(p -> ((idsFiltered == null) || (idsFiltered.contains(p.getCollectionID()))) && p.readable())
				.collect(Collectors.toMap(p -> p.getCollectionID(), p -> p));
	}


	/**
	 * Gibt alle Collections zurück, auf welche eine Lese-Berechtigung des Benutzers existiert.
	 *
	 * @return eine Liste mit den Collections
	 */
	private List<DavCollection> getReadableCollections() {
		final Map<Long, DavPermissions> readableCollectionPermissionsById = getReadableCollectionPermissionsById(null);
		if (readableCollectionPermissionsById.isEmpty())
			return new ArrayList<>();
		return conn.queryByKeyList(DTODavRessourceCollection.class, readableCollectionPermissionsById.keySet())
				.stream().filter(dto -> dto.geloeschtam == null)
				.map(dto -> mapDTODavRessourceCollection(dto, readableCollectionPermissionsById.get(dto.ID))).toList();
	}


	/**
	 * Gibt alle Collections für die angegebenen Typen zurück, auf welche eine Lese-Berechtigung des Benutzers existiert.
	 *
	 * @param typen   die {@link DavRessourceCollectionTyp} die aus der Datenbank gesucht werden.
	 *
	 * @return eine Liste mit den Collections
	 */
	public List<DavCollection> getCollectionsByTypes(final DavRessourceCollectionTyp... typen) {
		return getReadableCollections().stream().filter(c -> Arrays.stream(typen).anyMatch(typ -> c.typ == typ)).toList();
	}


	/**
	 * Gibt alle Collections für die angegebenen IDs zurück, auf welche eine Lese-Berechtigung des Benutzers existiert.
	 *
	 * @param idsCollection   die IDs der Collections
	 *
	 * @return eine Liste mit den Collections
	 */
	public List<DavCollection> getCollectionsByIDs(final Collection<Long> idsCollection) {
		return getReadableCollections().stream().filter(dto -> idsCollection.contains(dto.id)).toList();
	}


	/**
	 * Gibt die Collection für die angegebenen ID zurück, wenn darauf mindestens eine Lese-Berechtigung des Benutzers existiert.
	 *
	 * @param idCollection   die ID der Collection
	 *
	 * @return eine Collection oder null
	 */
	public DavCollection getCollectionByID(final long idCollection) {
		return getReadableCollections().stream().filter(dto -> idCollection == dto.id).findFirst().orElse(null);
	}


	/**
	 * Fügt eine neue Collection im Rahmen einer Datenbank-Transaktion hinzu.
	 *
	 * @param collection   die neu Collection
	 *
	 * @return die Collection
	 *
	 * @throws DavException   im Falle von fehlenden Berechtigungen oder Fehlern beim Schreiben in die Datenbank
	 */
	private DavCollection insertCollection(final DavCollection collection) throws DavException {
		conn.transactionBegin();

		// Erstelle zunächst die Collection
		final DTODavRessourceCollection dtoCollection = new DTODavRessourceCollection(collection.besitzer,
				conn.transactionGetNextID(DTODavRessourceCollection.class), collection.typ, collection.anzeigename,
				getNewSyncToken());
		dtoCollection.Beschreibung = collection.beschreibung;
		if (!conn.transactionPersist(dtoCollection)) {
			conn.transactionRollback();
			throw new DavException(Status.INTERNAL_SERVER_ERROR);
		}

		// Lege dann die Lese- und Schreib-Berechtigungen für den Besitzer an
		final DavPermissions permissions = new DavPermissions(true, true, dtoCollection.ID, collection.besitzer);
		final DTODavRessourceCollectionsACL acl = new DTODavRessourceCollectionsACL(conn.transactionGetNextID(DTODavRessourceCollectionsACL.class),
				collection.besitzer, dtoCollection.ID, permissions.toPermissionString());
		if (!conn.transactionPersist(acl)) {
			conn.transactionRollback();
			throw new DavException(Status.INTERNAL_SERVER_ERROR);
		}

		// Beende die Transaktion und geben die um IDs ergänzte Collection zurück.
		conn.transactionCommit();
		return mapDTODavRessourceCollection(dtoCollection, permissions);
	}


	/**
	 * Aktualisiert eine bestehende Collection im Rahmen einer Datenbank-Transaktion, sofern für diese die
	 * ID und Lese- und Schreibberechtigung gegeben ist.
	 *
	 * @param collection   die bestehende Collection
	 *
	 * @return die Collection
	 *
	 * @throws DavException   im Falle von fehlenden Berechtigungen oder Fehlern beim Schreiben in die Datenbank
	 */
	private DavCollection updateCollection(final DavCollection collection) throws DavException {
		conn.transactionBegin();

		// Lese die Collection aus der Datenbank ein
		final DTODavRessourceCollection dtoCollection = conn.queryByKey(DTODavRessourceCollection.class, collection.id);

		// Prüfe, ob eine zwischenzeitlich gelöschte Collection aktualisiert werden soll und gib ggf. eine Fehlermeldung NOT_FOUND zurück
		if (dtoCollection.geloeschtam != null) {
			conn.transactionRollback();
			throw new DavException(Status.NOT_FOUND);
		}
		// Prüfe das SyncToken. Ist dieses in den übergebenen Daten nicht gleich dem in den geladene Daten, so liegt auch ein Fehler vor
		if (DateTimeUtil.getTimeInMillis(dtoCollection.SyncToken) != collection.syncToken) {
			conn.transactionRollback();
			throw new DavException(Status.CONFLICT);
		}

		// Aktualisiere die daten
		dtoCollection.Anzeigename = collection.anzeigename;
		dtoCollection.Beschreibung = collection.beschreibung;
		dtoCollection.SyncToken = getNewSyncToken();
		dtoCollection.Typ = collection.typ;

		// Die Benutzer-ID des Besitzers darf allerdings nur von einem Administrator nachträglich geändert werden
		if (conn.getUser().istAdmin() && (collection.besitzer != null))
			dtoCollection.Benutzer_ID = collection.besitzer;

		// Persistiere die Daten
		if (!conn.transactionPersist(dtoCollection)) {
			conn.transactionRollback();
			throw new DavException(Status.INTERNAL_SERVER_ERROR);
		}

		// Beende die Transaktion und gib die aktualisierte Collection zurück.
		conn.transactionCommit();
		final DavPermissions permissions = new DavPermissions(true, true, dtoCollection.ID, collection.besitzer);
		return mapDTODavRessourceCollection(dtoCollection, permissions);
	}


	/**
	 * Fügt eine neue Collection hinzu oder aktualisiert eine bestehende, sofern für diese die ID und Lese- und
	 * Schreibberechtigung gegeben ist.
	 *
	 * @param collection   die Collection (neu oder bestehend)
	 *
	 * @return die Collection
	 *
	 * @throws DavException   im Falle von fehlenden Berechtigungen oder Fehlern beim Schreiben in die Datenbank
	 */
	public DavCollection insertOrUpdateCollection(final DavCollection collection) throws DavException {
		// Prüfe zunächst, ob die Berechtigung für das Aktualisieren bzw. Erzeugen der Collection vorhanden ist (Admin oder Besitzer)
		if (!allowModifaction(collection))
			throw new DavException(Status.FORBIDDEN);

		if (collection.id == null)
			return insertCollection(collection);
		return updateCollection(collection);
	}


	/**
	 * Löscht eine Collection wenn das übergebene Synctoken aktuell ist und dem in der Datenbank entspricht.
	 * Außerdem wird geprüft, ob die Berechtigung als Administrator oder Besitzer vorliegt
	 *
	 * @param idCollection   die ID der Collection
	 * @param syncToken      das Synctoken für den Abgleich
	 *
	 * @return true, wenn das Löschen erfolgreich war, und ansonsten false
	 *
	 * @throws DavException   im Fehlerfall
	 */
	public boolean deleteCollection(final long idCollection, final long syncToken) throws DavException {
		conn.transactionBegin();

		// Bestimme zunächst die Collection
		final DTODavRessourceCollection collection = conn.queryByKey(DTODavRessourceCollection.class, idCollection);
		if (collection == null) {
			conn.transactionRollback();
			throw new DavException(Status.NOT_FOUND);
		}

		// Überprüfe, ob das SyncToken übereinstimmt
		if (DateTimeUtil.getTimeInMillis(collection.SyncToken) != syncToken) {
			conn.transactionRollback();
			throw new DavException(Status.CONFLICT);
		}

		// Prüfe, ob die Berechtigung als Administrator oder als Besitzer vorliegt
		if (!(conn.getUser().istAdmin() || conn.getUser().getId().equals(collection.Benutzer_ID))) {
			// weder besitzer noch admin angemeldet
			conn.transactionRollback();
			throw new DavException(Status.FORBIDDEN);
		}

		// Setze das neue SyncToken und markiere den Eintrag als Gelöscht.
		collection.SyncToken = getNewSyncToken();
		collection.geloeschtam = collection.SyncToken;
		if (!conn.transactionPersist(collection)) {
			conn.transactionRollback();
			throw new DavException(Status.INTERNAL_SERVER_ERROR);
		}
		conn.transactionCommit();
		return true;
	}


	/**
	 * Gibt die Ressourcen der Collections mit den übergebenen IDs zurück. Die übergebenen Abfrage-Parameter
	 * werden dabei für das Mapping verwendet. Collections, wo der Benutzer keine Zugriffsrechte drauf hat
	 * werden ausgelassen.
	 *
	 * @param idsCollection   die IDs der Collections, für welche die Ressourcen gelesen werden sollen
	 * @param withPayload     gibt an, ob die Ressource mit dem Ressourceninhalt/Payload erzeugt werden soll
	 *
	 * @return Eine Liste mit den Informationen zu den Ressourcen
	 */
	public List<DavRessource> getRessources(final Collection<Long> idsCollection, final boolean withPayload) {
		// Bestimme eine Map mit allen Berechitungen zu den angegebenen Collection-IDs
		final Map<Long, DavPermissions> mapPermissions = getReadableCollectionPermissionsById(idsCollection);
		if (mapPermissions.isEmpty())
			return new ArrayList<>();
		// Lese alle Collections ein, wo eine Berechtigung in der Map existiert und gib diese zurück
		return conn.queryList(DTODavRessource.QUERY_LIST_BY_DAVRESSOURCECOLLECTION_ID, DTODavRessource.class, mapPermissions.keySet()).stream()
				.filter(dto -> dto.geloeschtam == null)
				.map(dto -> mapDTODavRessource(dto, withPayload, mapPermissions.get(dto.DavRessourceCollection_ID))).toList();
	}


	/**
	 * Aktualisiert das Synctoken der angegeben Collection.
	 * Wichtig: Diese Methode setzt eine laufende Transaktion voraus und beendet diese.
	 *
	 * @param idCollection   die Collection
	 * @param syncToken      das neue Synctoken
	 *
	 * @return true, falls die Transaktion erfolgreich abgeschlossen wurde, und ansonsten false
	 */
	private boolean updateCollectionSynctoken(final long idCollection, final String syncToken) {
		final DTODavRessourceCollection collection = conn.queryByKey(DTODavRessourceCollection.class, idCollection);
		collection.SyncToken = syncToken;
		final boolean transactionPersist = conn.transactionPersist(collection);
		if (!transactionPersist) {
			conn.transactionRollback();
			return false;
		}
		conn.transactionCommit();
		return true;
	}


	/**
	 * Fügt eine neue Ressource zu einer Collection hinzu.
	 * Wichtig: Diese Methode erfordert eine begonnene Transaktion. Diese wird dann hier abgeschlossen.
	 *
	 * @param davRessource   die Ressource die hinzugefügt werden soll.
	 * @param permissions    die Berechtigungen auf der Collection, zu welcher die Ressource gehört
	 *
	 * @return die aktualisierte Ressource mit gesetzer ID oder null im Fehlerfall
	 */
	private DavRessource insertRessource(final DavRessource davRessource, final DavPermissions permissions) {
		final String newSynctoken = getNewSyncToken();
		final DTODavRessource dtoDavRessource = new DTODavRessource(conn.transactionGetNextID(DTODavRessource.class),
				davRessource.idCollection, davRessource.uid, newSynctoken,
				davRessource.kalenderTyp, davRessource.kalenderStart, davRessource.kalenderEnde,
				davRessource.data.getBytes(StandardCharsets.UTF_8));
		if (!conn.transactionPersist(dtoDavRessource)) {
			conn.transactionRollback();
			return null;
		}
		if (!updateCollectionSynctoken(davRessource.idCollection, newSynctoken))
			return null;
		return mapDTODavRessource(dtoDavRessource, true, permissions);
	}


	/**
	 * Aktualisiert eine vorhandene Ressource.
	 * Wichtig: Diese Methode erfordert eine begonnene Transaktion. Diese wird dann hier abgeschlossen.
	 *
	 * @param davRessource   die Ressource die aktualisiert werden soll.
	 * @param permissions    die Berechtigungen auf der Collection, zu welcher die Ressource gehört
	 *
	 * @return die aktualisierte Ressource oder null im Fehlerfall
	 */
	private DavRessource updateRessource(final DavRessource davRessource, final DavPermissions permissions) {
		final String newSynctoken = getNewSyncToken();
		final DTODavRessource dtoDavRessource = conn.queryByKey(DTODavRessource.class, davRessource.id);
		// Prüfe, ob der Client die letzte Version hatte
		if (DateTimeUtil.getTimeInMillis(dtoDavRessource.lastModified) != davRessource.syncToken) {
			conn.transactionRollback();
			return null;
		}
		dtoDavRessource.DavRessourceCollection_ID = davRessource.idCollection;
		dtoDavRessource.KalenderEnde = davRessource.kalenderEnde;
		dtoDavRessource.KalenderStart = davRessource.kalenderStart;
		dtoDavRessource.KalenderTyp = davRessource.kalenderTyp;
		dtoDavRessource.lastModified = newSynctoken;
		dtoDavRessource.UID = davRessource.uid;
		dtoDavRessource.ressource = davRessource.data.getBytes(StandardCharsets.UTF_8);
		if (!conn.transactionPersist(dtoDavRessource)) {
			conn.transactionRollback();
			return null;
		}
		if (!updateCollectionSynctoken(davRessource.idCollection, newSynctoken))
			return null;
		return mapDTODavRessource(dtoDavRessource, true, permissions);
	}


	/**
	 * Fügt eine neue Ressource hinzu oder aktualisiert eine vorhandene auf Basis der UID, sofern die
	 * Lese- und Schreibberechtigungen auf der zugehörigen Collection vorhanden sind.
	 *
	 * @param davRessource   die Ressource die hinzugefügt bzw. aktualisiert werden soll.
	 *
	 * @return die aktuelle Ressource mit gesetzer ID oder null im Fehlerfall, z.B. bei
	 *         fehlenden Berechtigungen oder Fehlern beim Schreiben in die Datenbank
	 */
	public DavRessource insertOrUpdateRessource(final DavRessource davRessource) {
		// Prüfe zunächst, ob die Collection read only ist.
		final DavPermissions permissions = isWritableCollection(davRessource.idCollection);
		if (permissions == null)
			return null;

		// Starte die Transaktion und prüfe, ob es eine neue Ressource hinzugefügt wurde oder eine bestehende aktualisiert wird
		conn.transactionBegin();
		final List<DTODavRessource> dtosWithSameUID = conn.queryList(DTODavRessource.QUERY_BY_DAVRESSOURCECOLLECTION_ID, DTODavRessource.class,
				davRessource.idCollection).stream().filter(r -> (r.geloeschtam == null) && r.UID.equals(davRessource.uid)).toList();
		if (dtosWithSameUID.size() == 1)
			davRessource.id = dtosWithSameUID.get(0).ID;

		// Füge eine neue Ressource hinzu oder aktualisiere die Bestehende. Die Transaktion wird dabei abgechlossen
		if (davRessource.id == null)
			return insertRessource(davRessource, permissions);
		return updateRessource(davRessource, permissions);
	}


	/**
	 * Löscht eine Ressource anhand UID aus der Collection mit der übergebenen ID, sofern das gespeicherte Synctoken mit dem
	 * übergebenen übereinstimmt, d.h. der Benutzer der Anfrage die aktuelle Ressource hatte.
	 *
	 * @param idCollection   die ID der Collection
	 * @param uid            die UID der Ressource
	 * @param lastModified   der SyncToken aus der Anfrage für den Abgleich der letzten Änderung an der Ressource
	 *
	 * @return true, wenn das Löschen erfolgreich war, und ansonsten false
	 *
	 * @throws DavException   im Fehlerfall, z.B. bei fehlenden Rechten auf der Collection, einer fehlenden Ressource, einem abweichende Synctoken
	 *                        oder bei Fehlern beim Datenbankzugriff
	 */
	public boolean deleteRessource(final long idCollection, final String uid, final Long lastModified) throws DavException {
		conn.transactionBegin();

		// Prüfe, ob die Ressource durch den Benutzer beschreibbar ist
		if (isWritableCollection(idCollection) == null) {
			conn.transactionRollback();
			throw new DavException(Status.FORBIDDEN);
		}

		// Bestimme die Ressourcen mit übereinstimender UI und prüft, ob diese genau einmal vorkommt.
		final List<DTODavRessource> listRessourcesWithUID = conn.queryList(DTODavRessource.QUERY_BY_DAVRESSOURCECOLLECTION_ID,
				DTODavRessource.class, idCollection).stream().filter(r -> r.UID.equals(uid)).toList();
		if ((listRessourcesWithUID.size() != 1) || (listRessourcesWithUID.get(0).geloeschtam != null)) {
			conn.transactionRollback();
			throw new DavException(Status.NOT_FOUND);
		}
		final DTODavRessource resourceToDelete = listRessourcesWithUID.get(0);

		// Prüfe, ob der übergebene SyncToken mit dem der letzten Änderung übereinstimmt.
		if (DateTimeUtil.getTimeInMillis(resourceToDelete.lastModified) != lastModified) {
			conn.transactionRollback();
			throw new DavException(Status.CONFLICT);
		}

		// Aktualisiere die Ressource und setze den Löschzeitpunkt
		final String newSyncToken = getNewSyncToken();
		resourceToDelete.lastModified = newSyncToken;
		resourceToDelete.geloeschtam = newSyncToken;
		if (!conn.transactionPersist(resourceToDelete)) {
			conn.transactionRollback();
			throw new DavException(Status.INTERNAL_SERVER_ERROR);
		}
		return updateCollectionSynctoken(idCollection, newSyncToken);
	}


	/**
	 * Aktualisiert oder erzeugt einen Eintrag für Berechtigungen auf einer Collection. Dies ist nur erlaubt, sofern der
	 * Benutzer ein Administrator ist oder im Besitz der Collection.
	 *
	 * @param permissions   die Informationen zu der neuen bzw. angepassten Berechtigung
	 *
	 * @return der aktualisierte Berechtigungseintrag
	 *
	 * @throws DavException   im Fehlerfall, z.B. bei fehlenden Rechten oder bei Fehlern beim Schreiben in die Datenbank
	 */
	public DavPermissions insertOrUpdatePermissions(final DavPermissions permissions) throws DavException {
		conn.transactionBegin();

		// Prüfe, ob die Collection, deren Berechtigung aktualisiert werden soll überhaupt existiert
		final DTODavRessourceCollection dtoCollection = conn.queryByKey(DTODavRessourceCollection.class, permissions.getCollectionID());
		if (dtoCollection == null) {
			conn.transactionRollback();
			throw new DavException(Status.NOT_FOUND);
		}

		// Prüfe, ob die Berechtigung auf der Collection vorhanden ist: Administrator oder Besitzer
		if (!conn.getUser().istAdmin() && (dtoCollection.Benutzer_ID != conn.getUser().getId())) {
			conn.transactionRollback();
			throw new DavException(Status.FORBIDDEN);
		}

		// Prüfe, ob bereits eine Berechtigung des Benutzers auf der Ressource vorliegt
		final List<DTODavRessourceCollectionsACL> dtosPermissions =
				conn.queryList("SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.Benutzer_ID = ?1 AND e.RessourceCollection_ID = ?1",
						DTODavRessourceCollectionsACL.class, conn.getUser().getId(), dtoCollection.ID);
		// Prüfe, ob es mehr als einen Eintrag für einen Benutzer auf einer Collection gibt. Dies ist nicht zulässig
		if (dtosPermissions.size() > 1) {
			conn.transactionRollback();
			throw new DavException(Status.CONFLICT);
		}

		// Erstelle oder aktualisiere die Berechtigungen ...
		final DTODavRessourceCollectionsACL dtoPermissions;
		if (dtosPermissions.isEmpty()) {
			dtoPermissions = new DTODavRessourceCollectionsACL(conn.transactionGetNextID(DTODavRessourceCollectionsACL.class),
					permissions.getUserID(), permissions.getCollectionID(), permissions.toPermissionString());
		} else {
			dtoPermissions = dtosPermissions.get(0);
			dtoPermissions.berechtigungen = permissions.toPermissionString();
		}

		// ... und schreibe diese in die Datenbank
		if (!conn.transactionPersist(dtoPermissions)) {
			conn.transactionRollback();
			throw new DavException(Status.INTERNAL_SERVER_ERROR);
		}
		conn.transactionCommit();
		return permissions;
	}


	/**
	 * Löscht einen Eintrag für Berechtigungen auf eine Collection durch einen Benutzer.
	 *
	 * @param idCollection   die ID der Collection
	 * @param idBenutzer     die ID des Benutzers
	 *
	 * @return true, wenn der Löschvorgang erfolgreich war, und ansonsten false
	 *
	 * @throws DavException   im Fehlerfall, z.B. bei fehlenden Rechten, Ressource oder Benutzers oder bei Fehlern beim Schreiben in die Datenbank
	 */
	public boolean deletePermission(final long idCollection, final long idBenutzer) throws DavException {
		conn.transactionBegin();

		// Prüfe, ob die Collection zur angegebenen ID existiert
		final DTODavRessourceCollection dtoCollection = conn.queryByKey(DTODavRessourceCollection.class, idCollection);
		if (dtoCollection == null) {
			conn.transactionRollback();
			throw new DavException(Status.NOT_FOUND);
		}

		// Prüfe, ob der Benutzer ein Admin ist oder der Besitzer der Collection
		if (!conn.getUser().istAdmin() && (dtoCollection.Benutzer_ID != conn.getUser().getId())) {
			conn.transactionRollback();
			throw new DavException(Status.FORBIDDEN);
		}

		// Prüfe, ob bereits eine Berechtigung des Benutzers auf der Ressource vorliegt - es wird genau ein Ergebnis erwartet
		final List<DTODavRessourceCollectionsACL> dtosPermissions =
				conn.queryList("SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.Benutzer_ID = ?1 AND e.RessourceCollection_ID = ?1",
						DTODavRessourceCollectionsACL.class, idBenutzer, dtoCollection.ID);
		if (dtosPermissions.size() != 1) {
			conn.transactionRollback();
			throw new DavException(Status.NOT_FOUND);
		}
		final DTODavRessourceCollectionsACL dtoPermissions = dtosPermissions.get(0);

		// Entferne die Berechtigung und beende die Transaktion
		if (!conn.remove(dtoPermissions)) {
			conn.transactionRollback();
			throw new DavException(Status.INTERNAL_SERVER_ERROR);
		}
		conn.transactionCommit();
		return true;
	}


	/**
	 * Erstellt eine neue Ressourcensammlung mit dem angemeldeten Nutzer als Besitzer, sofern eine Ressourcensammlung vom gegebenen Typ
	 * noch nicht existiert. Nutzbar um einen eigenen Kalender oder ein eigenes Adressbuch anzulegen.
	 *
	 * @param typ der Typ der Ressourcensammlung
	 */
	public void tryCreateOwnedCollectionIfNotExists(final DavRessourceCollectionTyp typ) {
		// das prüfen auf vorhandensein und erstellen eines eigenen Kalenders muss in
		// einer transaktion geschehen
		conn.transactionBegin();
		final TypedQuery<DTODavRessourceCollection> query = conn.query(
				"SELECT c FROM DTODavRessourceCollection c WHERE c.Benutzer_ID = :nutzer_id AND c.Typ = :typ AND c.geloeschtam IS NULL",
				DTODavRessourceCollection.class);
		query.setParameter("nutzer_id", conn.getUser().getId());
		query.setParameter("typ", typ);
		final List<DTODavRessourceCollection> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			return;
		}
		// keine eigene Collection vom Typ vorhanden, also anlegen

		final DTOSchemaAutoInkremente lastCollectionsID = conn.queryByKey(DTOSchemaAutoInkremente.class, "DavRessourceCollections");
		final DTODavRessourceCollection eigenerKalender = new DTODavRessourceCollection(conn.getUser().getId(),
				(lastCollectionsID == null) ? 1 : (lastCollectionsID.MaxID + 1), typ, "Eigener Kalender",
				getNewSyncToken());
		eigenerKalender.Beschreibung = "Ein eigener Kalender mit Lese- und Schreibrechten für Sie.";
		if (!conn.transactionPersist(eigenerKalender)) {
			conn.transactionRollback();
			return;
		}

		final DTOSchemaAutoInkremente lastACLID = conn.queryByKey(DTOSchemaAutoInkremente.class, "DavRessourceCollectionsACL");
		final Long newAclID = (lastACLID == null) ? 1 : (lastACLID.MaxID + 1);

		final DavPermissions permissions = new DavPermissions(true, true,
				eigenerKalender.ID, conn.getUser().getId());
		final DTODavRessourceCollectionsACL acl = new DTODavRessourceCollectionsACL(newAclID, conn.getUser().getId(),
				eigenerKalender.ID, permissions.toPermissionString());
		if (!conn.transactionPersist(acl)) {
			conn.transactionRollback();
			return;
		}
		conn.transactionCommit();
	}


	/**
	 * Gibt eine Liste aller Ressourcen-UIDs einer Sammlung zurück, welche seit dem
	 * gegebenen Synctoken gelöscht wurden.
	 *
	 * @param collectionId    die Sammlung auf der nach gelöschten Ressourcen
	 *                        gesucht werden soll.
	 * @param syncTokenMillis das Synctoken, seit dem nach Löschvorgängen gesucht
	 *                        werden soll
	 * @return eine Liste aller UIDs, welche gelöscht wurden.
	 */
	public List<String> getDeletedResourceUIDsSince(final Long collectionId, final Long syncTokenMillis) {
		return conn.queryList(DTODavRessource.QUERY_BY_DAVRESSOURCECOLLECTION_ID, DTODavRessource.class, collectionId)
				.stream().filter(dto -> (dto.geloeschtam != null) && (DateTimeUtil.getTimeInMillis(dto.geloeschtam) >= syncTokenMillis))
				.map(dto -> dto.UID).toList();
	}


	/**
	 * Prüft, ob der angemeldete Nutzer Schreibrecht auf der durch die ID gegebenen Ressourcensammlung hat
	 *
	 * @param collectionId   die ID der Ressourcensammlung
	 *
	 * @return das Berechtigungsobjekt, sofern gefunden und ansonsten null
	 */
	private DavPermissions isWritableCollection(final Long collectionId) {
		final DTODavRessourceCollection queryByKey = conn.queryByKey(DTODavRessourceCollection.class, collectionId);
		if ((queryByKey != null) && (queryByKey.geloeschtam != null) && (conn.getUser().getId().equals(queryByKey.Benutzer_ID) || conn.getUser().istAdmin())) {
			// besitzer und admin dürfen schreiben
			return new DavPermissions(true, true, collectionId, queryByKey.Benutzer_ID);
		}
		final List<DTODavRessourceCollectionsACL> dtoDavRessourceCollectionACLs = conn.queryList(
				DTODavRessourceCollectionsACL.QUERY_BY_RESSOURCECOLLECTION_ID, DTODavRessourceCollectionsACL.class, collectionId)
				.stream().filter(dto -> dto.Benutzer_ID == conn.getUser().getId()).toList();
		if (dtoDavRessourceCollectionACLs.size() == 1) {
			final DTODavRessourceCollectionsACL dtoDavRessourceCollectionsACL = dtoDavRessourceCollectionACLs.get(0);
			final DavPermissions davRessourceCollectionACLPermissions = new DavPermissions(
					dtoDavRessourceCollectionsACL.berechtigungen, dtoDavRessourceCollectionsACL.RessourceCollection_ID,
					dtoDavRessourceCollectionsACL.Benutzer_ID);
			if (davRessourceCollectionACLPermissions.readable()
					&& davRessourceCollectionACLPermissions.writable()) {
				return davRessourceCollectionACLPermissions;
			}
		}
		return null;
	}

}
