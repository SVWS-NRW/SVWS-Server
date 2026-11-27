package de.svws_nrw.controller.uv.regeln;

import java.util.Collection;


import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import jakarta.ws.rs.core.Response;

/** Controller für die Blockungsregeln eines UV-Planungsabschnitts. */
public interface UvRegelController {

	/**
	 * Liefert die Regeln eines Planungsabschnitts.
	 *
	 * @param idPlan die ID des Planungsabschnitts
	 * @return die HTTP-Antwort
	 */
	Response getList(long idPlan);

	/**
	 * Erstellt eine Regel.
	 *
	 * @param idPlan die ID des Planungsabschnitts
	 * @param regel die neue Regel
	 * @return die HTTP-Antwort
	 */
	Response create(long idPlan, UvBlockungRegel regel);

	/**
	 * Ersetzt eine Regel.
	 *
	 * @param idPlan die ID des Planungsabschnitts
	 * @param idRegel die ID der Regel
	 * @param regel die neuen Werte
	 * @return die HTTP-Antwort
	 */
	Response update(long idPlan, long idRegel, UvBlockungRegel regel);

	/**
	 * Löscht eine Regel.
	 *
	 * @param idPlan die ID des Planungsabschnitts
	 * @param idRegel die ID der Regel
	 * @return die HTTP-Antwort
	 */
	Response delete(long idPlan, long idRegel);
	/**
	 * Speichert die zusammengehörigen Änderungen atomar.
	 *
	 * @param idPlan die zugehörigen Daten
	 * @param regeln die zugehörigen Daten
	 * @return die HTTP-Antwort
	 */
	Response updateMultiple(long idPlan, Collection<UvBlockungRegel> regeln);

}
