package de.svws_nrw.controller.gost.klausuren;

import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumRich;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für Raumzuweisungen von GOSt-Schülerklausurterminen.
 */
public interface GostKlausurenRaumzuweisungController {

	/**
	 * Setzt Raumzuweisungen.
	 *
	 * @param raumSchuelerZuteilung die Raumzuweisungen
	 *
	 * @return die Response
	 */
	Response setzeRaumzuweisungenFuerSchuelerklausurtermine(List<GostKlausurraumRich> raumSchuelerZuteilung);

	/**
	 * Löscht Raumzuweisungen.
	 *
	 * @param schuelerklausurterminIds die IDs der Schülerklausurtermine
	 *
	 * @return die Response
	 */
	Response loescheRaumzuweisungenFuerSchuelerklausurtermine(List<Long> schuelerklausurterminIds);

}
