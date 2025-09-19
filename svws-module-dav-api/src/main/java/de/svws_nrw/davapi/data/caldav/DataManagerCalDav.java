package de.svws_nrw.davapi.data.caldav;

import java.sql.Timestamp;
import java.util.List;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.kalender.Kalender;
import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.davapi.data.dav.DavCollection;
import de.svws_nrw.davapi.data.dav.DavException;
import de.svws_nrw.davapi.data.dav.DavRessource;
import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;

/**
 * Eine abstrakte Basisklasse für den Zugriff auf Daten für einen Kalendar.
 */
public abstract class DataManagerCalDav {

	/** Die Datenbank-Verbindung */
	protected final @NotNull DBEntityManager conn;

	/** Die ID des Schuljahresabschnittes bei generierten Kalendern für welche die Daten ermittelt werden sollen */
	protected final @NotNull Schuljahresabschnitt schuljahresabschnitt;

	/** Der Typ des Kalenders */
	protected final @NotNull CalDavKalenderTyp typ;


	/**
	 * Erstellt einen neuen Daten-Manager für den Zugriff auf einen Kalender
	 *
	 * @param conn                      die Verbindung für den Datenbank-Zugriff
	 * @param idSchuljahresabschnitt    die ID des Schuljahresabschnittes, auf welchen sich die Anfrage bezieht
	 * @param typ                       der Typ des Kalenders
	 */
	protected DataManagerCalDav(final @NotNull DBEntityManager conn, final long idSchuljahresabschnitt, final @NotNull CalDavKalenderTyp typ) {
		this.conn = conn;
		this.schuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(idSchuljahresabschnitt);
		this.typ = typ;
	}


	/**
	 * Erstellt einen Kontakt ohne Payload, d.h. nur mit der Kontakt-ID der zugrundliegenden Daten.
	 *
	 * @param id   die ID der zugrundliegenden Daten (z.B. Schüler-ID)
	 *
	 * @return der Kalender-Eintrag ohne Payload
	 */
	public @NotNull KalenderEintrag mapEmptyContact(final long id) {
		final KalenderEintrag a = new KalenderEintrag();
		a.id = getEintragId(id);
		return a;
	}


	/**
	 * Gibt den Kalender mit der übergebenen ID ohne Einträge zurück.
	 *
	 * @param idCal         die ID des Kalenders
	 *
	 * @return der Kalender oder null, wenn die ID ungültig ist
	 */
	public abstract Kalender getKalender(String idCal);


	/**
	 * Gibt die ID für den Eintrag auf Grundlage der übergebenen ID zurück.
	 *
	 * @param id   die ID der zugrundeliegenden Daten
	 *
	 * @return die ID für den Eintrag, z.B. "Stundenplan_2024_1_4711"
	 */
	public static String getEintragId(final long id) {
		return String.valueOf(id);
	}


	/**
	 * Gibt die Liste der Einträge aus dem Kalender mit der übergebenen ID zurück.
	 *
	 * @param idCal         die ID des Kalenders
	 * @param withPayload   gibt an, ob die eigentlichen Daten bei der Generierung der Einträge mit erstellt werden
	 *
	 * @return die Liste mit Einträgen des angegebenen Kalenders.
	 */
	public abstract @NotNull List<KalenderEintrag> getEintraege(String idCal, boolean withPayload);


	/**
	 * Erzeugt einen neuen Eintrag im Kalender mit den Daten aus dem übergebenen Kalender-Eintrag
	 *
	 * @param eintrag   der Kalender-Eintrag
	 *
	 * @return die neue Version des gespeicherten Kalender-Eintrags (Entity-Tag) oder null im Fehlerfall
	 */
	public abstract String persistEintrag(@NotNull KalenderEintrag eintrag);


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
	public abstract @NotNull List<String> getDeletedEintragUIDs(@NotNull String idCal, long syncToken) throws DavException;


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
	public abstract boolean deleteEintrag(@NotNull String idCal, String uid, Long syncToken) throws DavException;


	/**
	 * Mappt die Informationen einer DAV-Collection auf einen Kalender.
	 *
	 * @param collection   die DAV-Collection
	 *
	 * @return der Kalender
	 */
	public static Kalender mapCollectionToKalender(final @NotNull DavCollection collection) {
		final CalDavKalenderTyp type = CalDavKalenderTyp.getByCollectionType(collection.typ);
		if (type == null)
			return null;
		final Kalender k = new Kalender();
		k.displayname = collection.anzeigename;
		k.beschreibung = collection.beschreibung;
		k.id = type.getId(collection.id);
		k.kalenderTyp = "DavRessource";
		k.synctoken = collection.syncToken;
		k.darfLesen = collection.permissions.readable();
		k.darfSchreiben = collection.permissions.writable();
		k.besitzer = collection.besitzer;
		return k;
	}


	/**
	 * Mappt die Informationen einer DAV-Ressource auf einen Kalender-Eintrag.
	 *
	 * @param typ   der Typ des Kalenders
	 * @param res   die DAV-Ressource
	 *
	 * @return der Kalender-Eintrag
	 */
	public static @NotNull KalenderEintrag mapRessourceToEintrag(final @NotNull CalDavKalenderTyp typ, final DavRessource res) {
		final KalenderEintrag e = new KalenderEintrag();
		e.kalenderId = typ.getId(res.idCollection);
		e.id = getEintragId(res.id);
		e.uid = res.uid;
		e.version = String.valueOf(res.syncToken);
		e.data = res.data;
		e.kalenderStart = res.kalenderStart;
		e.kalenderEnde = res.kalenderEnde;
		e.kalenderTyp = res.kalenderTyp;
		if (res.permissions != null) {
			e.darfLesen = res.permissions.readable();
			e.darfSchreiben = res.permissions.writable();
		}
		return e;
	}

}
