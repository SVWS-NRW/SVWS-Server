package de.svws_nrw.controller.uv.schueler;

import de.svws_nrw.core.data.uv.UvSchuelerImportOptions;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Import von Schülern in UV-Planungsabschnitte.
 */
public interface UvPlanungsabschnittSchuelerImportController {

	/**
	 * Importiert Schüler anhand von Importoptionen in den Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 * @param options               die Importoptionen
	 *
	 * @return die Response
	 */
	Response importSchueler(long idPlanungsabschnitt, UvSchuelerImportOptions options);

}
