package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.Random;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Algorithmus der Klausuren (der Schüler) eines bestimmten Klausurtermins auf vorgegebene Räume blockt
 * und dabei bestimmte Regeln beachtet bzw. optimiert.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurraumblockungAlgorithmus {


	private final @NotNull Random random;

	/**
	 * Konstruktor.
	 */
	KlausurraumblockungAlgorithmus() {
		random = new Random();
	}

	/**
	 * Verteilt die Klausuren auf die zur Verfügung stehenden Räume.
	 * <br>Die Zuordnung ist im {@link GostKlausurraumRich#schuelerklausurterminIDs}-Objekt zu finden.
	 *
	 * <br>
	 * <br>Harte Kriterien:
	 * <br>- Die Raumkapazität darf nicht überschritten werden
	 * <br>- Es dürfen nur Klausuren in einen Raum geblockt werden, die dieselbe Startzeit haben.
	 * <br>
	 * <br>Weiche Kriterien (mit zugeordneter Güte von 0 ... 1):
	 * <br>- Möglichst gleiche Klausurlängen in einem Raum.
	 * <br>- Möglichst geringe Raumanzahl.
	 * <br>- Möglichst Klausuren des selben Kurses im selben Raum.
	 *
	 * @param config   		  Die Konfiguration und die Eingabedaten.
	 */
	public void berechne(final @NotNull GostKlausurraumblockungKonfiguration config) {

		final KlausurraumblockungAlgorithmusDynDaten dynDaten = new KlausurraumblockungAlgorithmusDynDaten(random, config);
		dynDaten.aktionKlausurenVerteilenAlgorithmus00_zufaellig();

		final long zeitEnde = System.currentTimeMillis() + config.maxTimeMillis;
		do {
			dynDaten.aktionKlausurenVerteilenAlgorithmus00_zufaellig();
			dynDaten.aktionKlausurenVerteilenAlgorithmus01();
			dynDaten.aktionKlausurenVerteilenAlgorithmus02();
			dynDaten.aktionKlausurenVerteilenAlgorithmus03();
		} while (System.currentTimeMillis() < zeitEnde);


		dynDaten.aktionLadeGespeichertenZustand();
	}

}
