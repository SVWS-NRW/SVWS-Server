package de.svws_nrw.davapi.data.repos.dav;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IDavRepository;
import de.svws_nrw.davapi.data.repos.dav.DavException.ErrorCode;
import de.svws_nrw.davapi.util.icalendar.DateTimeUtil;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.DatumUhrzeitConverter;
import de.svws_nrw.db.dto.current.svws.dav.DTODavRessource;
import de.svws_nrw.db.dto.current.svws.dav.DTODavRessourceCollection;
import de.svws_nrw.db.dto.current.svws.dav.DTODavRessourceCollectionsACL;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import jakarta.persistence.TypedQuery;

/**
 * Dieses Repository implementiert das {@link IDavRepository} und kapselt die
 * Datenbankverbindung für Ressourcensammlungen, Berechtigungen auf
 * Ressourcensammlungen und Ressourcen
 */
public final class DavRepository implements IDavRepository {

	/** der zu verwendende {@link DBEntityManager} */
	private final DBEntityManager conn;
	private final Benutzer user;

	/**
	 * Konstruktor mit dem zu verwendenden {@link DBEntityManager}
	 *
	 * @param conn der {@link DBEntityManager}
	 */
	public DavRepository(final DBEntityManager conn) {
		this.conn = conn;
		this.user = conn.getUser();
	}

	@Override
	public Collection<DavRessourceCollection> getDavRessourceCollections(final DavRessourceCollectionTyp... typen) {
		return getReadableDavRessourceCollections().stream()
				.filter(c -> Arrays.stream(typen).anyMatch(typ -> c.typ == typ)).toList();
	}

	@Override
	public Collection<DavRessourceCollection> getDavRessourceCollections(final Collection<Long> ressourceCollectionIds) {
		return getReadableDavRessourceCollections().stream().filter(dto -> ressourceCollectionIds.contains(dto.id))
				.toList();
	}

	@Override
	public Collection<DavRessource> getDavRessources(final Collection<Long> ressourceCollectionIds,
			final CollectionRessourceQueryParameters parameters) {
		// filter für angefragte IDs nach denen, die gelesen werden dürfen
		final Map<Long, DavRessourceCollectionACLPermissions> readableCollectionACLPermissionsByCollectionId = getReadableCollectionPermissionsById()
				.entrySet().stream().filter(e -> ressourceCollectionIds.contains(e.getKey()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		if (readableCollectionACLPermissionsByCollectionId.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryList(DTODavRessource.QUERY_LIST_BY_DAVRESSOURCECOLLECTION_ID, DTODavRessource.class,
				readableCollectionACLPermissionsByCollectionId.keySet())
				.stream().filter(dto -> dto.geloeschtam == null).map(dto -> mapDTODavRessource(dto, parameters,
						readableCollectionACLPermissionsByCollectionId.get(dto.DavRessourceCollection_ID)))
				.toList();
	}

	@Override
	public Optional<DavRessourceCollection> upsertDavRessourceCollection(final DavRessourceCollection davRessourceCollection)
			throws DavException {
		if (allowUpsertCollection(davRessourceCollection)) {
			// nur admins dürfen Sammlungen anlegen, besitzer dürfen vorhandene Sammlungen
			// ändern, wenn sie die Benutzerkompetenz haben
			throw new DavException(ErrorCode.FORBIDDEN);
		}
		final DTODavRessourceCollection dtoCollection;
		final String currentSynctoken = getNewSyncTokenTimestampAsString();
		conn.transactionBegin();
		boolean aclEntryNeeded = false;
		if (davRessourceCollection.id == null) {
			final Long id = getNextId("DavRessourceCollections");
			dtoCollection = new DTODavRessourceCollection(davRessourceCollection.besitzer, id,
					davRessourceCollection.typ, davRessourceCollection.anzeigename, currentSynctoken);
			dtoCollection.Beschreibung = davRessourceCollection.beschreibung;
			// erzeugen eines neuen ACL-Eintrags benötigt, wenn eine neue Sammlung angelegt
			// wird
			aclEntryNeeded = true;
		} else {
			dtoCollection = conn.queryByKey(DTODavRessourceCollection.class, davRessourceCollection.id);
			if (dtoCollection.geloeschtam != null) {
				// update auf bestehende collection, aber ist bereits gelöscht
				conn.transactionRollback();
				throw new DavException(ErrorCode.NOT_FOUND);
			}
			if (DateTimeUtil.getTimeInMillis(dtoCollection.SyncToken) != davRessourceCollection.syncToken) {
				conn.transactionRollback();
				throw new DavException(ErrorCode.CONFLICT);
			}
			dtoCollection.Anzeigename = davRessourceCollection.anzeigename;
			dtoCollection.Beschreibung = davRessourceCollection.beschreibung;
			dtoCollection.SyncToken = currentSynctoken;
			dtoCollection.Typ = davRessourceCollection.typ;
			if (user.istAdmin() && (davRessourceCollection.besitzer != null)) {
				// Benutzer id darf nur vom Admin geändert werden.
				dtoCollection.Benutzer_ID = davRessourceCollection.besitzer;
			}
		}
		if (!conn.transactionPersist(dtoCollection)) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		final DavRessourceCollectionACLPermissions permissions = new DavRessourceCollectionACLPermissions(true, true,
				dtoCollection.ID, davRessourceCollection.besitzer);
		if (aclEntryNeeded) {
			// für neuangelegte Collection wird ein initialer ACL-Eintrag für den besitzer
			// benötigt
			final Long id = getNextId("DavRessourceCollectionsACL");

			final DTODavRessourceCollectionsACL acl = new DTODavRessourceCollectionsACL(id, davRessourceCollection.besitzer,
					dtoCollection.ID, permissions.toPermissionString());
			if (!conn.transactionPersist(acl)) {
				conn.transactionRollback();
				throw new DavException(ErrorCode.INTERNAL_SERVER_ERROR);
			}
		}
		conn.transactionCommit();
		return Optional.of(mapDTODavRessourceCollection(dtoCollection, permissions));
	}

	/**
	 * sucht die nächste id für den gegebenen Tabellennamen
	 *
	 * @param tableName der Tabellenname
	 * @return 1, wenn die Tabelle in {@link DTOSchemaAutoInkremente} noch nicht
	 *         auftaucht, ansonsten die nächsthöhere ID
	 */
	private Long getNextId(final String tableName) {
		final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, tableName);
		return (lastID == null) ? 1 : (lastID.MaxID + 1);
	}

	/**
	 * Gibt wieder, ob der angemeldete Nutzer die gegebene Collection bearbeiten
	 * (aktualisieren oder einfügen) darf. Nutzer dürfen ihre eigene Sammlung
	 * bearbeiten, sofern sie die {@link BenutzerKompetenz} für eigene Kalender
	 * haben, Admins dürfen alle Kalender bearbeiten und neue anlegen
	 *
	 * @param davRessourceCollection die Sammlung
	 * @return ob der Nutzer die Sammlung bearbeiten darf
	 */
	private boolean allowUpsertCollection(final DavRessourceCollection davRessourceCollection) {
		return !user.istAdmin() && !(user.pruefeKompetenz(BenutzerKompetenz.CALDAV_EIGENER_KALENDER)
				&& user.getId().equals(davRessourceCollection.besitzer) && (davRessourceCollection.id != null));
	}

	/**
	 * Erstellt ein neues Synctoken für den aktuellen Zeitpunkt als
	 * SQL-Timestamp-String
	 *
	 * @return das aktuelle Synctoken
	 */
	public static String getNewSyncTokenTimestampAsString() {
		final long now = Instant.now().toEpochMilli();
		return DatumUhrzeitConverter.instance.convertToEntityAttribute(new Timestamp(now));
	}

	@Override
	public Optional<DavRessource> upsertDavRessource(final DavRessource davRessource) throws DavException {
		final Optional<DavRessourceCollectionACLPermissions> optionalCollectionPermissions = isWritableCollection(
				davRessource.ressourceCollectionId);
		if (optionalCollectionPermissions.isEmpty()) {
			throw new DavException(ErrorCode.FORBIDDEN);
		}
		final String currentSynctoken = getNewSyncTokenTimestampAsString();
		final DTODavRessource dtoDavRessource;
		conn.transactionBegin();
		final List<DTODavRessource> davRessourcesWithSameUID = conn
				.queryList(DTODavRessource.QUERY_BY_DAVRESSOURCECOLLECTION_ID, DTODavRessource.class, davRessource.ressourceCollectionId)
				.stream().filter(r -> (r.geloeschtam == null) && r.UID.equals(davRessource.uid)).toList();
		if (davRessourcesWithSameUID.size() == 1) {
			davRessource.id = davRessourcesWithSameUID.get(0).ID;
		}
		if (davRessource.id == null) {
			final Long id = getNextId("DavRessources");

			dtoDavRessource = new DTODavRessource(id, davRessource.ressourceCollectionId, davRessource.uid,
					currentSynctoken, davRessource.kalenderTyp, davRessource.kalenderStart, davRessource.kalenderEnde,
					davRessource.data.getBytes(StandardCharsets.UTF_8));
		} else {
			dtoDavRessource = conn.queryByKey(DTODavRessource.class, davRessource.id);
			if (DateTimeUtil.getTimeInMillis(dtoDavRessource.lastModified) != davRessource.syncToken) {
				// Konflikt, client hatte nicht die aktuellste version
				conn.transactionRollback();
				throw new DavException(ErrorCode.CONFLICT);
			}
			dtoDavRessource.DavRessourceCollection_ID = davRessource.ressourceCollectionId;
			dtoDavRessource.KalenderEnde = davRessource.kalenderEnde;
			dtoDavRessource.KalenderStart = davRessource.kalenderStart;
			dtoDavRessource.KalenderTyp = davRessource.kalenderTyp;
			dtoDavRessource.lastModified = currentSynctoken;
			dtoDavRessource.UID = davRessource.uid;
			dtoDavRessource.ressource = davRessource.data.getBytes(StandardCharsets.UTF_8);
		}
		if (!conn.transactionPersist(dtoDavRessource)) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		finishTransactionUpdateCollectionSynctoken(davRessource.ressourceCollectionId, currentSynctoken);
		return Optional.of(mapDTODavRessource(dtoDavRessource,
				CollectionRessourceQueryParameters.INCLUDE_RESSOURCES_INCLUDE_PAYLOAD,
				optionalCollectionPermissions.get()));
	}

	@Override
	public Optional<DavRessourceCollectionACLPermissions> upsertDavRessourceCollectionACLPermissions(
			final DavRessourceCollectionACLPermissions davRessourceCollectionACLPermissions) throws DavException {
		// user dürfen nur ACLs für eigene RessourceCollections ändern
		conn.transactionBegin();
		final DTODavRessourceCollection dtoDavRessourceCollection = conn.queryByKey(DTODavRessourceCollection.class,
				davRessourceCollectionACLPermissions.getRessourceCollectionId());
		if (!user.istAdmin() && (dtoDavRessourceCollection.Benutzer_ID != user.getId())) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.FORBIDDEN);
		}
		final List<DTODavRessourceCollectionsACL> dtoACLs = conn
				.queryList(DTODavRessourceCollectionsACL.QUERY_BY_RESSOURCECOLLECTION_ID, DTODavRessourceCollectionsACL.class, dtoDavRessourceCollection.ID)
				.stream().filter(dto -> dto.Benutzer_ID == conn.getUser().getId()).toList();
		final DTODavRessourceCollectionsACL dtoACL;
		if (dtoACLs.size() == 1) {
			dtoACL = dtoACLs.get(0);
			dtoACL.berechtigungen = davRessourceCollectionACLPermissions.toPermissionString();
		} else {

			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "DavRessourceCollectionsACL");
			final Long id = (lastID == null) ? 1 : (lastID.MaxID + 1);

			dtoACL = new DTODavRessourceCollectionsACL(id, davRessourceCollectionACLPermissions.getBenutzerId(),
					davRessourceCollectionACLPermissions.getRessourceCollectionId(),
					davRessourceCollectionACLPermissions.toPermissionString());
		}
		if (!conn.transactionPersist(dtoACL)) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		conn.transactionCommit();
		return Optional.of(davRessourceCollectionACLPermissions);
	}

	@Override
	public boolean deleteRessourceCollectionIfUpToDate(final long id, final long syncToken) throws DavException {
		conn.transactionBegin();
		final DTODavRessourceCollection queryByKey = conn.queryByKey(DTODavRessourceCollection.class, id);
		if (queryByKey == null) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.NOT_FOUND);
		}
		if (DateTimeUtil.getTimeInMillis(queryByKey.SyncToken) != syncToken) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.CONFLICT);
		}
		if (!(user.istAdmin() || user.getId().equals(queryByKey.Benutzer_ID))) {
			// weder besitzer noch admin angemeldet
			conn.transactionRollback();
			throw new DavException(ErrorCode.FORBIDDEN);
		}
		final String newSyncTokenTimestampAsString = getNewSyncTokenTimestampAsString();
		queryByKey.geloeschtam = newSyncTokenTimestampAsString;
		queryByKey.SyncToken = newSyncTokenTimestampAsString;
		if (!conn.transactionPersist(queryByKey)) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		conn.transactionCommit();
		return true;
	}

	@Override
	public boolean deleteRessourceIfUpToDate(final long collectionId, final String ressourceUID, final Long ifMatchToken)
			throws DavException {
		if (!isWritableCollection(collectionId).isPresent()) {
			throw new DavException(ErrorCode.FORBIDDEN);
		}
		conn.transactionBegin();
		final List<DTODavRessource> listDavRessourcesSameUID = conn.queryList(DTODavRessource.QUERY_BY_DAVRESSOURCECOLLECTION_ID,
				DTODavRessource.class, collectionId).stream().filter(r -> r.UID.equals(ressourceUID)).toList();
		if ((listDavRessourcesSameUID.size() != 1) || (listDavRessourcesSameUID.get(0).geloeschtam != null)) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.NOT_FOUND);
		}
		final DTODavRessource resourceToDelete = listDavRessourcesSameUID.get(0);
		if (DateTimeUtil.getTimeInMillis(resourceToDelete.lastModified) != ifMatchToken) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.CONFLICT);
		}
		final String newSyncTokenTimestampAsString = getNewSyncTokenTimestampAsString();
		resourceToDelete.geloeschtam = newSyncTokenTimestampAsString;
		resourceToDelete.lastModified = newSyncTokenTimestampAsString;
		if (!conn.transactionPersist(resourceToDelete)) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		return finishTransactionUpdateCollectionSynctoken(collectionId, newSyncTokenTimestampAsString);
	}

	@Override
	public boolean deleteRessourceCollectionACL(final long collectionId, final long benutzerId) throws DavException {
		if (!user.istAdmin() && !user.pruefeKompetenz(BenutzerKompetenz.CALDAV_EIGENER_KALENDER)) {
			throw new DavException(ErrorCode.FORBIDDEN);
		}
		conn.transactionBegin();
		final TypedQuery<DTODavRessourceCollection> queryRessourceCollection = conn.query(
				"SELECT c FROM DTODavRessourceCollection c WHERE c.ID = :collectionID",
				DTODavRessourceCollection.class);
		final List<DTODavRessourceCollection> ressourceCollections = queryRessourceCollection.getResultList();
		if (ressourceCollections.size() != 1) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.NOT_FOUND);
		}
		final TypedQuery<DTODavRessourceCollectionsACL> queryACLs = conn.query(
				"SELECT acl FROM DTODavRessourceCollectionACL acl WHERE acl.Benutzer_ID = :benutzerid AND acl.ressourcecollection_id = :ressourceCollectionID",
				DTODavRessourceCollectionsACL.class);
		queryACLs.setParameter("benutzerid", benutzerId);
		queryACLs.setParameter("ressourceCollectionID", collectionId);
		final List<DTODavRessourceCollectionsACL> relevantACLEntries = queryACLs.getResultList();
		if (relevantACLEntries.size() != 1) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.NOT_FOUND);
		}
		if (!conn.remove(relevantACLEntries.get(0))) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		conn.transactionCommit();
		return true;
	}

	@Override
	public void tryCreateOwnedCollectionIfNotExists(final DavRessourceCollectionTyp typ) {
		// das prüfen auf vorhandensein und erstellen eines eigenen Kalenders muss in
		// einer transaktion geschehen
		conn.transactionBegin();
		final TypedQuery<DTODavRessourceCollection> query = conn.query(
				"SELECT c FROM DTODavRessourceCollection c WHERE c.Benutzer_ID = :nutzer_id AND c.Typ = :typ AND c.geloeschtam IS NULL",
				DTODavRessourceCollection.class);
		query.setParameter("nutzer_id", user.getId());
		query.setParameter("typ", typ);
		final List<DTODavRessourceCollection> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			return;
		}
		// keine eigene Collection vom Typ vorhanden, also anlegen

		final DTOSchemaAutoInkremente lastCollectionsID = conn.queryByKey(DTOSchemaAutoInkremente.class, "DavRessourceCollections");
		final DTODavRessourceCollection eigenerKalender = new DTODavRessourceCollection(user.getId(),
				(lastCollectionsID == null) ? 1 : (lastCollectionsID.MaxID + 1), typ, "Eigener Kalender",
				getNewSyncTokenTimestampAsString());
		eigenerKalender.Beschreibung = "Ein eigener Kalender mit Lese- und Schreibrechten für Sie.";
		if (!conn.transactionPersist(eigenerKalender)) {
			conn.transactionRollback();
			return;
		}

		final DTOSchemaAutoInkremente lastACLID = conn.queryByKey(DTOSchemaAutoInkremente.class, "DavRessourceCollectionsACL");
		final Long newAclID = (lastACLID == null) ? 1 : (lastACLID.MaxID + 1);

		final DavRessourceCollectionACLPermissions permissions = new DavRessourceCollectionACLPermissions(true, true,
				eigenerKalender.ID, user.getId());
		final DTODavRessourceCollectionsACL acl = new DTODavRessourceCollectionsACL(newAclID, user.getId(),
				eigenerKalender.ID, permissions.toPermissionString());
		if (!conn.transactionPersist(acl)) {
			conn.transactionRollback();
			return;
		}
		conn.transactionCommit();

	}

	@Override
	public List<String> getDeletedResourceUIDsSince(final Long collectionId, final Long syncTokenMillis) {
		return conn.queryList(DTODavRessource.QUERY_BY_DAVRESSOURCECOLLECTION_ID, DTODavRessource.class, collectionId)
				.stream().filter(dto -> (dto.geloeschtam != null) && (DateTimeUtil.getTimeInMillis(dto.geloeschtam) >= syncTokenMillis))
				.map(dto -> dto.UID).toList();
	}

	/**
	 * mappt eine gegebene Ressource aus dem Datenbankformat in eine vereinfachte
	 * Repräsentation abhängig von gegebenen Query-Parametern
	 *
	 * @param dto         das Datenbankobjekt
	 * @param parameters  die Parameter der Abfrage um unnötiges setzen von Daten zu
	 *                    vermeiden
	 * @param permissions die Berechtigungen des angemeldeten Nutzers auf diese
	 *                    Ressourec
	 * @return die DavRessource ergänzt um Berechtigungen
	 */
	private static DavRessource mapDTODavRessource(final DTODavRessource dto, final CollectionRessourceQueryParameters parameters,
			final DavRessourceCollectionACLPermissions permissions) {
		final DavRessource r = new DavRessource();

		r.ressourceCollectionId = dto.DavRessourceCollection_ID;
		r.id = dto.ID;
		r.uid = dto.UID;
		r.permissions = permissions;
		r.syncToken = DateTimeUtil.getTimeInMillis(dto.lastModified);
		if ((parameters != null) && parameters.includeEintragPayload) {
			r.kalenderEnde = dto.KalenderEnde;
			r.kalenderStart = dto.KalenderStart;
			r.kalenderTyp = dto.KalenderTyp;
			r.data = new String(dto.ressource, StandardCharsets.UTF_8);
			r.uid = dto.UID;
		}
		return r;
	}

	/**
	 * mappt eine Ressourcensammlung aus dem Datenbankformat in eine vereinfachte
	 * Repräsentation
	 *
	 * @param dto                                  das Datenbankobjekt
	 * @param davRessourceCollectionACLPermissions die Berechtigungen des
	 *                                             angemeldeten Nutzers
	 * @return eine vereinfachte Repräsentation des Datenbankobjekts ergänzt um die
	 *         Berechtigungen
	 */
	private static DavRessourceCollection mapDTODavRessourceCollection(final DTODavRessourceCollection dto,
			final DavRessourceCollectionACLPermissions davRessourceCollectionACLPermissions) {
		final DavRessourceCollection c = new DavRessourceCollection();
		c.anzeigename = dto.Anzeigename;
		c.beschreibung = dto.Beschreibung;
		c.id = dto.ID;
		c.syncToken = DateTimeUtil.getTimeInMillis(dto.SyncToken);
		c.typ = dto.Typ;
		c.besitzer = dto.Benutzer_ID;
		c.permissions = davRessourceCollectionACLPermissions;
		return c;
	}

	/**
	 * liest die Ressourcensammlungen für die der Nutzer zumindest Leserecht hat aus
	 * der Datenbank
	 *
	 * @return eine Liste der Ressourcensammlungen
	 */
	private Collection<DavRessourceCollection> getReadableDavRessourceCollections() {
		final Map<Long, DavRessourceCollectionACLPermissions> readableCollectionPermissionsById = getReadableCollectionPermissionsById();
		if (readableCollectionPermissionsById.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryByKeyList(DTODavRessourceCollection.class, readableCollectionPermissionsById.keySet())
				.stream().filter(dto -> dto.geloeschtam == null)
				.map(dto -> mapDTODavRessourceCollection(dto, readableCollectionPermissionsById.get(dto.ID))).toList();
	}

	/**
	 * Liest alle Leserechte des aktuellen Nutzers aus der Datenbank
	 *
	 * @return die Berechtigungen des aktuellen Nutzers gemappt auf die ID der
	 *         Ressourcensammlung
	 */
	private Map<Long, DavRessourceCollectionACLPermissions> getReadableCollectionPermissionsById() {
		// suche alle ACL-Einträge für den Benutzer
		final List<DTODavRessourceCollectionsACL> dtoDavRessourceCollectionsACLs = conn.queryList(
				DTODavRessourceCollectionsACL.QUERY_BY_BENUTZER_ID, DTODavRessourceCollectionsACL.class, user.getId());
		final Map<Long, DavRessourceCollectionACLPermissions> readableCollectionPermissionsById = new HashMap<>();
		for (final DTODavRessourceCollectionsACL dtoCollectionsACL : dtoDavRessourceCollectionsACLs) {
			final DavRessourceCollectionACLPermissions davRessourceCollectionACLPermissions = new DavRessourceCollectionACLPermissions(
					dtoCollectionsACL.berechtigungen, dtoCollectionsACL.RessourceCollection_ID,
					dtoCollectionsACL.Benutzer_ID);
			if (davRessourceCollectionACLPermissions.darfLesen()) {
				readableCollectionPermissionsById.put(dtoCollectionsACL.RessourceCollection_ID,
						davRessourceCollectionACLPermissions);
			}
		}
		return readableCollectionPermissionsById;
	}

	/**
	 * prüft, ob der angemeldete Nutzer Schreibrecht auf der durch die ID gegebenen
	 * Ressourcensammlung hat
	 *
	 * @param ressourceCollectionId die id der Ressourcensammlung
	 * @return das Berechtigungsobjekt, sofern gefunden
	 */
	private Optional<DavRessourceCollectionACLPermissions> isWritableCollection(final Long ressourceCollectionId) {
		final DTODavRessourceCollection queryByKey = conn.queryByKey(DTODavRessourceCollection.class, ressourceCollectionId);
		if ((queryByKey != null) && (queryByKey.geloeschtam != null)
				&& (user.getId().equals(queryByKey.Benutzer_ID) || user.istAdmin())) {
			// besitzer und admin dürfen schreiben
			return Optional.of(new DavRessourceCollectionACLPermissions(true, true, ressourceCollectionId,
					queryByKey.Benutzer_ID));
		}
		final List<DTODavRessourceCollectionsACL> dtoDavRessourceCollectionACLs = conn.queryList(
				DTODavRessourceCollectionsACL.QUERY_BY_RESSOURCECOLLECTION_ID, DTODavRessourceCollectionsACL.class, ressourceCollectionId)
				.stream().filter(dto -> dto.Benutzer_ID == conn.getUser().getId()).toList();
		if (dtoDavRessourceCollectionACLs.size() == 1) {
			final DTODavRessourceCollectionsACL dtoDavRessourceCollectionsACL = dtoDavRessourceCollectionACLs.get(0);
			final DavRessourceCollectionACLPermissions davRessourceCollectionACLPermissions = new DavRessourceCollectionACLPermissions(
					dtoDavRessourceCollectionsACL.berechtigungen, dtoDavRessourceCollectionsACL.RessourceCollection_ID,
					dtoDavRessourceCollectionsACL.Benutzer_ID);
			if (davRessourceCollectionACLPermissions.darfLesen()
					&& davRessourceCollectionACLPermissions.darfSchreiben()) {
				return Optional.of(davRessourceCollectionACLPermissions);
			}
		}
		return Optional.empty();
	}

	/**
	 * schließt eine Transaktion ab und aktualisiert das Synctoken an der
	 * betroffenen Ressourcensammlung
	 *
	 * @param collectionId die Ressourcensammlung
	 * @param syncToken    das Synctoken
	 * @return true, falls die Transaktion erfolgreich abgeschlossen wurde
	 * @throws DavException falls die Transaktion nicht erfolgreich abgeschlossen
	 *                      wurde
	 */
	private boolean finishTransactionUpdateCollectionSynctoken(final long collectionId, final String syncToken)
			throws DavException {
		final DTODavRessourceCollection queryByKey = conn.queryByKey(DTODavRessourceCollection.class, collectionId);
		queryByKey.SyncToken = syncToken;
		final boolean transactionPersist = conn.transactionPersist(queryByKey);
		if (!transactionPersist) {
			conn.transactionRollback();
			throw new DavException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		conn.transactionCommit();
		return true;
	}
}
