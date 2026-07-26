package de.svws_nrw.controller.gost.klausuren;

import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für aggregierte GOSt-Klausurdaten.
 */
public interface GostKlausurenKlausurdatenController {

	/**
	 * Liefert alle Klausurplanungsdaten.
	 *
	 * @param hjData die Halbjahresdaten
	 *
	 * @return die Response
	 */
	Response getAllData(List<GostKlausurenHalbjahresdaten> hjData);

	/**
	 * Liefert alle Klausurplanungsdaten als GZip-Response.
	 *
	 * @param hjData die Halbjahresdaten
	 *
	 * @return die Response
	 */
	Response getAllDataGZip(List<GostKlausurenHalbjahresdaten> hjData);

	/**
	 * Liefert Klausurdaten-Issues.
	 *
	 * @param abiturjahr der Abiturjahrgang
	 * @param halbjahr das GOSt-Halbjahr
	 *
	 * @return die Response
	 */
	Response getKlausurdatenIssues(int abiturjahr, GostHalbjahr halbjahr);

	/**
	 * Liefert Klausurdaten-Issues als GZip-Response.
	 *
	 * @param abiturjahr der Abiturjahrgang
	 * @param halbjahr das GOSt-Halbjahr
	 *
	 * @return die Response
	 */
	Response getKlausurdatenIssuesGZip(int abiturjahr, GostHalbjahr halbjahr);

	/**
	 * Liefert die Klausurdaten eines Schülers.
	 *
	 * @param idSchueler die ID des Schülers
	 * @param abiturjahr der Abiturjahrgang
	 * @param halbjahr das GOSt-Halbjahr
	 *
	 * @return die Response
	 */
	Response getKlausurdatenBySchuelerId(long idSchueler, int abiturjahr, int halbjahr);

}
