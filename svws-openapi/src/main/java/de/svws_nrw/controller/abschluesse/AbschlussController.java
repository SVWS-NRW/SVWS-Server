package de.svws_nrw.controller.abschluesse;

import de.svws_nrw.service.abschluesse.AbschlussPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Das Interface für den Controller mit den Methoden für die Abschlüsse
 */
public interface AbschlussController {

	/**
	 * Ermittelt die Abschlussinformationen für den angebenen Schüler in dem angegebenen Schuljahrsabschnitt
	 *
	 * @param idSchueler               die ID des Schülers
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Response
	 */
	Response getByIdSchuelerAndIdSchuljahresabschnitt(long idSchueler, long idSchuljahresabschnitt);

	/**
	 * Ermittelt die Abschlussinformationen für den angegebenen Lernabschnitt
	 *
	 * @param idLernabschnitt   die ID des Schülerlernabschnittes
	 *
	 * @return die Response
	 */
	Response getByIdLernabschnitt(long idLernabschnitt);

	/**
	 * Führt einen Patch auf Abschlussinformationen zu dem angegebenen Lernabschnitt aus
	 *
	 * @param idLernabschnitt      die ID des Lernabschnittes
	 * @param patch                der Patch für die Abschlussinformationen
	 *
	 * @return die Response
	 */
	Response patch(long idLernabschnitt, AbschlussPatchRequest patch);

}
