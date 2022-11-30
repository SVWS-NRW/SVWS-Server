package de.nrw.schule.svws.davapi.data;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.nrw.schule.svws.core.types.dav.DavRessourceCollectionTyp;
import de.nrw.schule.svws.davapi.data.repos.dav.DavException;
import de.nrw.schule.svws.davapi.data.repos.dav.DavRessource;
import de.nrw.schule.svws.davapi.data.repos.dav.DavRessourceCollection;
import de.nrw.schule.svws.davapi.data.repos.dav.DavRessourceCollectionACLPermissions;

/**
 * Dieses Interface ebschreibt Methoden, welche von einem Dav Repository, bspw.
 * für die Datenbank bereitgestellt werden sollen.
 *
 */
public interface IDavRepository {

	/**
	 * Gibt die Ressourcensammlungen anhand der angegebenen Typen zurück, auf die
	 * der Nutzer Leserecht hat.
	 * 
	 * @param typen die {@link DavRessourceCollectionTyp} die aus der Datenbank
	 *              gesucht werden.
	 * @return die gesuchten Ressourcensammlungen
	 */
	Collection<DavRessourceCollection> getDavRessourceCollections(DavRessourceCollectionTyp... typen);

	/**
	 * gibt die Ressourcensammlungen anhand der gegebenen IDs zurück, auf die der
	 * Nutzer Leserecht hat.
	 * 
	 * @param ressourceCollectionIds die IDs der gesuchten Ressourcensammlungen
	 * @return die gesuchten Ressourcensammlungen
	 * 
	 */
	Collection<DavRessourceCollection> getDavRessourceCollections(Collection<Long> ressourceCollectionIds);

	/**
	 * Gibt die Ressourcen der gesuchten Ressourcensammlungen zurück. Anhand der
	 * {@link CollectionRessourceQueryParameters} wird entschieden, ob ggf. nur die
	 * ID und UID des Objekts oder auch der Inhalt zurückgegeben werden.
	 * 
	 * Hat der Nutzer nicht das nötige Leserecht auf eine gegebene
	 * Ressourcensammlung, werden keine Ressourcen für diese Sammlung zurückgegeben.
	 * 
	 * @param ressourceCollectionIds die IDs der Ressourcensammlungen, für die die
	 *                               Ressourcen gesucht werden sollen
	 * @param parameters             Filterkriterium, um beispielsweise auf Payload
	 *                               zu verzichten.
	 * @return die gesuchten Ressourcen
	 */
	Collection<DavRessource> getDavRessources(Collection<Long> ressourceCollectionIds,
			CollectionRessourceQueryParameters parameters);

	/**
	 * Fügt eine neue Ressourcensammlung hinzu oder updated eine vorhandene, sofern
	 * die ID und Lese- und Schreibrecht gegeben ist.
	 * 
	 * @param davRessourceCollection die Ressourcensammlung, welche hinzugefügt oder
	 *                               aktualisiert werden soll.
	 * @return die Ressourcensammlung, sofern die Aktualisierung erfolgreich war.
	 * @throws DavException Ausnahme bei fehlenden Rechten oder Fehlern beim
	 *                      Schreiben in der Datenbank
	 */
	Optional<DavRessourceCollection> upsertDavRessourceCollection(DavRessourceCollection davRessourceCollection)
			throws DavException;

	/**
	 * Fügt eine neue Ressource hinzu oder aktualisiert eine vorhandene auf Basis
	 * der UID, sofern Lese- und Schreibrecht auf der Ressourcensammlung vorhanden
	 * ist.
	 * 
	 * @param davRessource die Davressource die hinzugefügt oder aktualisiert werden
	 *                     soll.
	 * @return die aktualisierte DavRessource mit gesetzer ID bei Erfolg
	 * @throws DavException Ausnahme bei fehlenden Rechten oder Fehlern beim
	 *                      Schreiben in der Datenbank
	 */
	Optional<DavRessource> upsertDavRessource(DavRessource davRessource) throws DavException;

	/**
	 * Fügt einen neuen ACL-Eintrag für eine Ressourcensammlung und einen Nutzer
	 * hinzu oder aktualisiert einen vorhandenen, sofern der Nutzer Besitzer der
	 * Ressource oder Admin ist.
	 * 
	 * @param davRessourceCollectionACLPermissions der zuzufügende oder zu
	 *                                             aktualisierende ACL-Eintrag
	 * @return der aktualisierte ACL-Eintrag
	 * @throws DavException bei fehlenden Rechten oder Fehlern beim Schreiben in der
	 *                      Datenbank
	 */
	Optional<DavRessourceCollectionACLPermissions> upsertDavRessourceCollectionACLPermissions(
			DavRessourceCollectionACLPermissions davRessourceCollectionACLPermissions) throws DavException;

	/**
	 * Löscht eine Ressourcensammlung sofern das gegebene Synctoken dem Synctoken in
	 * der Datenbank entspricht.
	 * 
	 * @param id        die ID der Ressourcensammlung
	 * @param syncToken das Synctoken, auf das verglichen werden soll.
	 * @return ob das Löschen erfolgreich war
	 * @throws DavException Ausnahme bei fehlenden Rechten, nicht-vorhandensein der
	 *                      Ressourcensammlung oder nicht-aktuellem Synctoken
	 */
	boolean deleteRessourceCollectionIfUpToDate(long id, long syncToken) throws DavException;

	/**
	 * Löscht eine DavRessource anhand der RessourcenUID und der Ressourcensammlung,
	 * sofern das gespeicherte Synctoken mit dem gegebenen übereinstimmt, der
	 * Aufrufer also über die aktuelle Version der Ressource verfügt
	 * 
	 * @param ressourceCollectionId die ID der Ressourcensammlung
	 * @param ressourceUID          die UID der Ressource
	 * @param ifMatchToken          das SyncToken, welches dem Aufrufer zuletzt
	 *                              bekannt war
	 * @return ob das Löschen erfolgreich war
	 * @throws DavException bei fehlenden Rechten auf der Ressourcensammlung,
	 *                      nicht-vorhandensein der Ressource, nichtaktuellem
	 *                      Synctoken oder Fehlern beim Schreiben in die Datenbank
	 */
	boolean deleteRessourceIfUpToDate(long ressourceCollectionId, String ressourceUID, Long ifMatchToken)
			throws DavException;

	/**
	 * Löscht einen ACL-Eintrag auf Basis der Nutzer-ID und der ID der
	 * Ressourcensammlung
	 * 
	 * @param collectionId die ID der Ressourcensammlung
	 * @param benutzerId   die ID des Benutzers
	 * @return ob der Löschvorgang erfolgreich war
	 * @throws DavException bei fehlenden Rechten, nicht-vorhandensein der Ressource
	 *                      oder des Nutzers oder Fehlern beim Schreiben in der
	 *                      Datenbank
	 */
	boolean deleteRessourceCollectionACL(long collectionId, long benutzerId) throws DavException;

	/**
	 * Erstellt eine neue Ressourcensammlung mit dem angemeldeten Nutzer als
	 * Besitzer, sofern eine Ressourcensammlung vom gegebenen Typ noch nicht
	 * existiert. Nutzbar um einen eigenen Kalender oder ein eigenes Adressbuch
	 * anzulegen.
	 * 
	 * @param typ der Typ der Ressourcensammlung
	 */
	void tryCreateOwnedCollectionIfNotExists(DavRessourceCollectionTyp typ);

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
	List<String> getDeletedResourceUIDsSince(Long collectionId, Long syncTokenMillis);
}
