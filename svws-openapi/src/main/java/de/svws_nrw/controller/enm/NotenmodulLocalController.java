package de.svws_nrw.controller.enm;

import de.svws_nrw.core.data.enm.ENMServerConfigElement;
import de.svws_nrw.service.enm.NotenmodulLocalLeistungBemerkungenPatchRequest;
import de.svws_nrw.service.enm.NotenmodulLocalLeistungPatchRequest;
import de.svws_nrw.service.enm.NotenmodulLocalLernabschnittPatchRequest;
import de.svws_nrw.service.enm.NotenmodulLocalTeilleistungPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Das Interface für die API-Zugriffe des lokalen Notenmoduls
 */
public interface NotenmodulLocalController {

	/**
	 * Liest die Konfiguration des lokalen Notenmoduls aus der Datenbank.
	 *
	 * @return die HTTP-Response
	 */
	Response getConfig();


	/**
	 * Schreibt ein Konfigurationselement in die Notenmodul-Konfiguration des Servers.
	 *
	 * @param elem   das Konfigurationselement
	 *
	 * @return die HTTP-Response
	 */
	Response setConfigElement(ENMServerConfigElement elem);


	/**
	 * Prüft, ob ein Patchen der Leistungsdaten durch den aktuell angemeldeten Benutzer erlaubt ist
	 * und passt die Leistungsdaten eines Schüler dann ggf. an.
	 *
	 * @param patch               der Patch mit den Leistungsdaten
	 *
	 * @return die HTTP-Response
	 */
	Response patchLeistung(NotenmodulLocalLeistungPatchRequest patch);


	/**
	 * Prüft, ob ein Patchen der Teilleistungen durch den aktuell angemeldeten Benutzer erlaubt ist
	 * und passt die Teilleistung eines Schüler dann ggf. an.
	 *
	 * @param patch               der Patch mit den Lernabschnittsdaten
	 *
	 * @return die HTTP-Response
	 */
	Response patchTeilleistung(NotenmodulLocalTeilleistungPatchRequest patch);


	/**
	 * Prüft, ob ein Patchen der Bemerkungen zu einem Schüler-Lernabschnitt durch den aktuell angemeldeten
	 * Benutzer erlaubt ist und passt die Bemerkungen dann ggf. an.
	 *
	 * @param id                  die ID des Schülers, dessen Bemerkungen angepasst werden sollen
	 * @param patch               der Patch zu den Bemerkungen
	 *
	 * @return die HTTP-Response
	 */
	Response patchBemerkungen(long id, NotenmodulLocalLeistungBemerkungenPatchRequest patch);


	/**
	 * Prüft, ob ein Patchen eines Schüler-Lernabschnittes durch den aktuell angemeldeten
	 * Benutzer erlaubt ist und passt diesen dann ggf. an.
	 *
	 * @param patch               der Patch zu dem Lernabschnitt
	 *
	 * @return die HTTP-Response
	 */
	Response patchLernabschnitt(NotenmodulLocalLernabschnittPatchRequest patch);

}
