package de.svws_nrw.davapi.data.caldav;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.kalender.Kalender;
import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;
import de.svws_nrw.davapi.data.dav.DavException;
import de.svws_nrw.davapi.data.dav.DavDBRepository;
import de.svws_nrw.davapi.data.dav.DavRessource;
import de.svws_nrw.davapi.data.dav.DavCollection;
import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;


/**
 * das DB-Repository für Kalender und Kalendereinträge. Greift auf ein
 * {@link DavDBRepository} zurück.
 */
public final class CalDavKalender {

	/** Die Datenbank-Verbindung */
	private final DBEntityManager conn;

	/** das DavRepository für den Zugriff auf schreibbare Kalender */
	private final DavDBRepository davRepository;


	/**
	 * Konstruktor für dieses Repository, erstellt eigenes {@link DavDBRepository}
	 *
	 * @param conn der {@link DBEntityManager} auf den zugegriffen werden soll
	 */
	public CalDavKalender(final DBEntityManager conn) {
		this.davRepository = new DavDBRepository(conn);
		this.conn = conn;
	}

	/**
	 * Stellt einen Kalender anhand der übergebenen Kalender-ID aus der Datenbank zusammen.
	 *
	 * @param idCal           die ID des Kalenders
	 * @param withEintraege   gibt an, ob die Einträge im Kalender enthalten sein sollen
	 * @param withPayload     gibt an, ob die eigentlichen Daten bei der Generierung der Einträge mit erstellt werden
	 *
	 * @return der Kalender oder null, falls kein Kalender mit der übergebenen ID gefunden wurde.
	 */
	public Kalender getKalenderById(final String idCal, final boolean withEintraege, final boolean withPayload) {
		// Bestimme zunächst den Typ für das Einlesen der Einträge
		final CalDavKalenderTyp typ = CalDavKalenderTyp.getByID(idCal);
		if (typ == null)
			return null;

		// Lese die Kontakte anhand des Typs und des Schuljahresabschnittes aus der DB ein
		final Kalender cal = switch (typ) {
			case PERSOENLICH -> new DataCalDavEigenerKalender(conn).getKalender(idCal);
			case OEFFENTLICH -> new DataCalDavOeffentlich(conn).getKalender(idCal);
			default -> null;
		};
		if ((cal != null) && withEintraege)
			cal.kalenderEintraege.addAll(getEintraegeByKalender(idCal, withPayload));
		return cal;
	}


	/**
	 * Gibt die Liste der für den angemeldeten Benutzer verfügbaren Kalender zurück.
	 *
	 * @return die Liste der Kalender
	 */
	public List<Kalender> getAvailableKalender() {
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.CALDAV_NUTZEN))
			return new ArrayList<>();
		// TODO Hier muss später noch gefiltert werden, so dass anhand der Berechtigungen des Connection-Users die Adressbücher zusammengestellt werden
		return CalDavKalenderTyp.getKalender();
	}


	/**
	 * Stellt die Liste aller Einträge aus dem Kalender mit der angegebenen ID zusammen.
	 *
	 * @param idCal         die ID des Kalenders
	 * @param withPayload   gibt an, ob die eigentlichen Daten der Einträge bei der Generierung der Einträge mit erstellt werden
	 *
	 * @return die Liste mit den Einträgen des Kalenders.
	 */
	private List<KalenderEintrag> getEintraegeByKalender(final @NotNull String idCal, final boolean withPayload) {
		// Prüfe die allgemeine Benutzerkompetenz für den Zugriff auf Cal-DAV
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.CALDAV_NUTZEN))
			return Collections.emptyList();

		// Bestimme zunächst den Typ für das Einlesen der Einträge
		final CalDavKalenderTyp typ = CalDavKalenderTyp.getByID(idCal);

		// Lese die Kontakte anhand des Typs und des Schuljahresabschnittes aus der DB ein
		final List<KalenderEintrag> result = switch (typ) {
			case PERSOENLICH -> new DataCalDavEigenerKalender(conn).getEintraege(idCal, withPayload);
			case OEFFENTLICH -> new DataCalDavOeffentlich(conn).getEintraege(idCal, withPayload);
			default -> Collections.emptyList();
		};

		// Setze bei allen Kontakten die ID des Kalenders
		result.forEach(k -> k.kalenderId = idCal);
		return result;
	}




	/**
	 * Ermittelt eine Liste aller für den angemeldeten Benutzer verfügbaren Kalender.
	 *
	 * @param withEintraege   gibt an, ob die Einträge mit bestimmt werden sollen oder nicht
	 * @param withPayload     gibt an, ob bei Einträgen auch der tatsächliche Inhalt zurückgegeben werden soll
	 *
	 * @return die Liste der Kalender.
	 *
	 * @throws DavException wenn beim Bestimmen der Kalender ein Fehler auftritt
	 */
	public @NotNull List<Kalender> getAvailableKalender(final boolean withEintraege, final boolean withPayload) throws DavException {
		final List<Kalender> result = new ArrayList<>();

		// Prüfe zunächst, ob der Benutzer die CalDAV-API nutzen darf
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.CALDAV_NUTZEN))
			return result;

		// Prüfe, ob bereits ein eigener Kalender existiert. Wenn nicht, dann lege ihn an
		if (conn.getUser().pruefeKompetenz(BenutzerKompetenz.CALDAV_EIGENER_KALENDER))
			davRepository.tryCreateOwnedCollectionIfNotExists(DavRessourceCollectionTyp.EIGENER_KALENDER);

		// Bestimme zunächst alle Kalender mit Leseberechtigung und Benutzereinträgen aus der Datenbank und lese ggf. auch die Ressourcen ein
		final List<DavCollection> collections =
				davRepository.getCollectionsByTypes(DavRessourceCollectionTyp.KALENDER, DavRessourceCollectionTyp.EIGENER_KALENDER);
		final Map<Long, List<DavRessource>> mapDavRessources = new HashMap<>();
		if (withEintraege) {
			final List<Long> idsCollections = collections.stream().map(c -> c.id).toList();
			final Collection<DavRessource> davRessources = davRepository.getRessources(idsCollections, withPayload);
			mapDavRessources.putAll(davRessources.stream().collect(Collectors.groupingBy(r -> r.idCollection)));
		}

		// Wandle die Collections in Kalender um und ordne diese dabei ihrer Collection zu
		for (final DavCollection collection : collections) {
			// Bestimme den Kalender und füge ihn zur Liste hinzu
			final CalDavKalenderTyp typ = CalDavKalenderTyp.getByCollectionType(collection.typ);
			if (typ == null)
				continue;
			final Kalender cal = DataManagerCalDav.mapCollectionToKalender(collection);
			result.add(cal);

			// Füge ggf. noch die Einträge zum Kalender hinzu (sofern zuvor welche bestimmt wurden)
			final List<DavRessource> ressources = mapDavRessources.get(collection.id);
			if ((ressources == null) || ressources.isEmpty())
				continue;
			for (final DavRessource res : ressources)
				cal.kalenderEintraege.add(DataManagerCalDav.mapRessourceToEintrag(typ, res));
		}

		// Ergänze ggf. noch generierte Kalender, wo der Benutzer Berechtigungen drauf hat
		// ... noch keine implementiert

		return result;
	}


	/**
	 * Speichert einen Kalender-Eintrag in der Datenbank. Existiert bereits ein Eintrag mit den zugehörigen IDs
	 * von Kalender und Eintrag, so wird dieser aktualisiert. Ansonsten wird ein neuer Eintrag angelegt.
	 *
	 * @param eintrag   der Kalendereintrag
	 *
	 * @return die neue Version des gespeicherten Kalendereintrags (Entity-Tag) oder null im Fehlerfall
	 */
	public String persistEintrag(final KalenderEintrag eintrag) {
		final String idCal = eintrag.kalenderId;
		if (idCal == null)
			return null;
		final CalDavKalenderTyp typ = CalDavKalenderTyp.getByID(idCal);
		return switch (typ) {
			case PERSOENLICH -> new DataCalDavEigenerKalender(conn).persistEintrag(eintrag);
			case OEFFENTLICH -> new DataCalDavOeffentlich(conn).persistEintrag(eintrag);
			default -> null;
		};
	}


	/**
	 * Gibt die UIDs von gelöschten Einträgen eines Kalenders seit dem gegegebenen Zeitpunkt zurück.
	 *
	 * @param idCal       die ID des Kalenders
	 * @param syncToken   der Zeitpunkt als Millisekunden seit 1970, {@link Timestamp#getTime()}
	 *
	 * @return eine Liste der Ressourcen-UIDs, welche seit dem gesuchten Zeitpunkt als gelöscht markiert wurden
	 *
	 * @throws DavException im Fehlerfall
	 */
	public List<String> getDeletedEintragUIDs(final @NotNull String idCal, final long syncToken) throws DavException {
		final CalDavKalenderTyp typ = CalDavKalenderTyp.getByID(idCal);
		return switch (typ) {
			case PERSOENLICH -> new DataCalDavEigenerKalender(conn).getDeletedEintragUIDs(idCal, syncToken);
			case OEFFENTLICH -> new DataCalDavOeffentlich(conn).getDeletedEintragUIDs(idCal, syncToken);
			default -> throw new DavException(Status.NOT_FOUND);
		};
	}


	/**
	 * Entfernt einen Kalendereintrag anhand der UID aus dem Kalender mit der übergebenen ID, sofern der
	 * übergebene SyncToken mit dem gespeicherten übereinstimmt, d.h. die Anfrage auf dem aktuellen Kalendereintrag
	 * basiert.
	 *
	 * @param idCal       die ID des Kalenders
	 * @param uid         die UID des Kalendereintrags
	 * @param syncToken   der SyncToken aus der Anfrage für den Abgleich der letzten Änderung an der Ressource
	 *
	 * @return true, wenn das Löschen erfolgreich war, und ansonsten false
	 *
	 * @throws DavException im Fehlerfall (z.B. fehlende Rechte auf dem Kalender, fehlender Kalendereintrag, abweichendes Synctoken, ...)
	 */
	public boolean deleteKalenderEintrag(final @NotNull String idCal, final String uid, final Long syncToken) throws DavException {
		final CalDavKalenderTyp typ = CalDavKalenderTyp.getByID(idCal);
		return switch (typ) {
			case PERSOENLICH -> new DataCalDavEigenerKalender(conn).deleteEintrag(idCal, uid, syncToken);
			case OEFFENTLICH -> new DataCalDavOeffentlich(conn).deleteEintrag(idCal, uid, syncToken);
			default -> throw new DavException(Status.NOT_FOUND);
		};
	}

}
