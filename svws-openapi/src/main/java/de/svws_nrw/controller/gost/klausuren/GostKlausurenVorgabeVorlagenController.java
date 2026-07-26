package de.svws_nrw.controller.gost.klausuren;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für Vorlagenoperationen auf GOSt-Klausurvorgaben.
 */
public interface GostKlausurenVorgabeVorlagenController {

	/**
	 * Kopiert Klausurvorgaben aus der Vorlage in einen Abiturjahrgang.
	 *
	 * @param abiturjahr das Abiturjahr
	 * @param halbjahr das GOSt-Halbjahr
	 * @param quartal das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return die Response
	 */
	Response copyVorlagenToJahrgang(int abiturjahr, int halbjahr, int quartal);

	/**
	 * Erstellt fehlende Klausurvorgaben in der Vorlage.
	 *
	 * @param halbjahr das GOSt-Halbjahr
	 * @param quartal das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return die Response
	 */
	Response createMissingVorlagen(int halbjahr, int quartal);

}
