package de.svws_nrw.controller.lehrer;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungCreateRequest;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die Mehrleistung von Lehrern
 */
public interface LehrerMehrleistungController {

	/**
	 * Ermittelt den Eintrag für allgemeine Mehrleistung eines Lehrers anhand der ID.
	 *
	 * @param id   die ID des Eintrages
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Einträge für allgemeine Mehrleistung von Lehrern anhand der IDs.
	 *
	 * @param ids   die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response getList(Collection<Long> ids);

	/**
	 * Führt auf dem Eintrag für allgemeine Mehrleistung eines Lehrers mit der angegebenen ID
	 * einen Patch aus und gibt das Ergebnis zurück.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(LehrerMehrleistungPatchRequest patch);

	/**
	 * Führt auf mehreren Einträgen für allgemeine Mehrleistung eines Lehrers mit den angegebenen IDs
	 * die zugeordneten Patches aus und gibt die Ergebnisse zurück.
	 *
	 * @param patches   eine Map mit der Zuordnung der Patches zu den IDs
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<LehrerMehrleistungPatchRequest> patches);

	/**
	 * Erstellt einen neuen Eintrag für allgemeine Mehrleistung eines Lehrers mithilfe des Patches
	 * und gibt das Ergebnis zurück.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response create(LehrerMehrleistungCreateRequest patch);

	/**
	 * Erstellt neue Einträge für allgemeine Mehrleistung eines Lehrers mithilfe der Patches
	 * und gibt die Ergebnisse zurück.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response createMultiple(List<LehrerMehrleistungCreateRequest> patches);

	/**
	 * Löscht den Eintrag für allgemeine Mehrleistung eines Lehrers mit der
	 * angegebenen ID und gibt den gelöschten Eintrag zurück.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht die Eintrag für allgemeine Mehrleistung eines Lehrers mit den
	 * angegebenen IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
