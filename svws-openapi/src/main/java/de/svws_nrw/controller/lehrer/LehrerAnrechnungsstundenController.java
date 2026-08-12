package de.svws_nrw.controller.lehrer;

import java.util.Collection;

import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeCreateRequest;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundePatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die Anrechnungsstunden von Lehrern
 */
public interface LehrerAnrechnungsstundenController {

	/**
	 * Ermittelt den Eintrag für allgemeine Anrechnungsstunden eines Lehrers anhand der ID.
	 *
	 * @param id   die ID des Eintrages
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Einträge für allgemeine Anrechnungsstunden von Lehrern anhand der IDs.
	 *
	 * @param ids   die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response getList(Collection<Long> ids);

	/**
	 * Führt einen Patch für den Eintrag für allgemeine Anrechnungsstunden eines Lehrers aus.
	 * Der Patch enthält die ID des Eintrages auf welchen er sich bezieht.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(LehrerAnrechnungsstundePatchRequest patch);

	/**
	 * Führt einen Patch für die Einträge für allgemeine Anrechnungsstunden von Lehrern aus.
	 * Die Patches enthalten die IDs der Einträge auf welche sie sich beziehen.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<LehrerAnrechnungsstundePatchRequest> patches);

	/**
	 * Erstellt einen neuen Eintrag für allgemeine Anrechnungsstunden eines Lehrers mithilfe des Patches
	 * und gibt das Ergebnis zurück.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response create(LehrerAnrechnungsstundeCreateRequest patch);

	/**
	 * Erstellt neue Einträge für allgemeine Anrechnungsstunden eines Lehrers mithilfe der Patches
	 * und gibt die Ergebnisse zurück.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<LehrerAnrechnungsstundeCreateRequest> patches);

	/**
	 * Löscht den Eintrag für allgemeine Anrechnungsstunden eines Lehrers mit der
	 * angegebenen ID und gibt den gelöschten Eintrag zurück.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht die Eintrag für allgemeine Anrechnungsstunden eines Lehrers mit den
	 * angegebenen IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
