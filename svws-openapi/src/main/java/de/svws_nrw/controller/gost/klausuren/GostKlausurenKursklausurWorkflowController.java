package de.svws_nrw.controller.gost.klausuren;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungDaten;
import de.svws_nrw.service.gost.klausuren.GostKlausurenKursklausurPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für höherwertige Kursklausur-Workflows.
 */
public interface GostKlausurenKursklausurWorkflowController {

	/**
	 * Patcht eine Kursklausur.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die Response
	 */
	Response patch(GostKlausurenKursklausurPatchRequest patchRequest);

	/**
	 * Erzeugt Kursklausuren für Jahrgang, Halbjahr und Quartal.
	 *
	 * @param abiturjahr der Abiturjahrgang
	 * @param halbjahr das GOSt-Halbjahr
	 * @param quartal das Quartal
	 *
	 * @return die Response
	 */
	Response create(int abiturjahr, int halbjahr, int quartal);

	/**
	 * Blockt Kursklausuren.
	 *
	 * @param blockungDaten die Blockungsdaten
	 *
	 * @return die Response
	 */
	Response blocken(GostKlausurterminblockungDaten blockungDaten);

}
