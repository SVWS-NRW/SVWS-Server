package de.svws_nrw.data.lehrer;

import java.util.Collection;
import java.util.Map;

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
	 * Führt auf dem Eintrag für allgemeine Anrechnungsstunden eines Lehrers mit der angegebenen ID
	 * einen Patch aus und gibt das Ergebnis zurück.
	 *
	 * @param id      die ID des Eintrages
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, LehrerAnrechnungsstundenPatchRequest patch);

	/**
	 * Führt auf mehreren Einträgen für allgemeine Anrechnungsstunden eines Lehrers mit den angegebenen IDs
	 * die zugeordneten Patches aus und gibt die Ergebnisse zurück.
	 *
	 * @param patches   eine Map mit der Zuordnung der Patches zu den IDs
	 *
	 * @return die Response
	 */
	Response patchMultiple(Map<Long, LehrerAnrechnungsstundenPatchRequest> patches);

	/**
	 * Erstellt einen neuen Eintrag für allgemeine Anrechnungsstunden eines Lehrers mithilfe des Patches
	 * und gibt das Ergebnis zurück.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response create(LehrerAnrechnungsstundenCreateRequest patch);

	/**
	 * Erstellt neue Einträge für allgemeine Anrechnungsstunden eines Lehrers mithilfe der Patches
	 * und gibt die Ergebnisse zurück.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<LehrerAnrechnungsstundenCreateRequest> patches);

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
