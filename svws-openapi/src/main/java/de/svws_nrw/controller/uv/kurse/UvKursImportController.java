package de.svws_nrw.controller.uv.kurse;

import de.svws_nrw.core.data.uv.UvKursImportDaten;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Import bestätigter Kursdaten in UV-Planungsabschnitte.
 */
public interface UvKursImportController {

	/**
	 * Persistiert die vom Client bestätigten Kursimportdaten.
	 *
	 * @param idPlanungsabschnitt der Zielplanungsabschnitt
	 * @param daten die anzulegenden Importdaten
	 * @return die angelegten Daten
	 */
	Response importDaten(long idPlanungsabschnitt, UvKursImportDaten daten);


}
