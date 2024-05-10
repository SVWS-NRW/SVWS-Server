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
	 * <br>Obligatorische Kriterien:
	 * <br>- Die Raumkapazität darf nicht überschritten werden
	 * <br>- Es dürfen nur Klausuren in einen Raum geblockt werden, die dieselbe Startzeit haben.
	 * <br>
	 * <br>Fakultative Kriterien:
	 * <br>- {@link GostKlausurraumblockungKonfiguration#_regel_forciere_selbe_klausurdauer_pro_raum}
	 * <br>- {@link GostKlausurraumblockungKonfiguration#_regel_forciere_selbe_kursklausur_im_selben_raum}
	 * <br>- {@link GostKlausurraumblockungKonfiguration#_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume}
	 * <br>- {@link GostKlausurraumblockungKonfiguration#_regel_optimiere_blocke_in_moeglichst_wenig_raeume}
	 *
	 * @param config   		  Die Konfiguration und die Eingabedaten.
	 */
	public void berechne(final @NotNull GostKlausurraumblockungKonfiguration config) {
		final KlausurraumblockungAlgorithmusDynDaten dynDaten = new KlausurraumblockungAlgorithmusDynDaten(random, config);
		dynDaten.aktionKlausurenVerteilenAlgorithmus01_raum_zufaellig_gruppe_zufaellig();

		final long zeitEnde = System.currentTimeMillis() + config.maxTimeMillis;
		do {
			dynDaten.aktionKlausurenVerteilenAlgorithmus01_raum_zufaellig_gruppe_zufaellig();
			dynDaten.aktionKlausurenVerteilenAlgorithmus02_raum_aufsteigend_gruppe_zufaellig();
			dynDaten.aktionKlausurenVerteilenAlgorithmus03_raum_absteigend_gruppe_zufaellig();
			dynDaten.aktionKlausurenVerteilenAlgorithmus04_raum_zufaellig_gruppe_aufsteigend();
			dynDaten.aktionKlausurenVerteilenAlgorithmus05_raum_aufsteigend_gruppe_aufsteigend();
			dynDaten.aktionKlausurenVerteilenAlgorithmus06_raum_absteigend_gruppe_aufsteigend();
			dynDaten.aktionKlausurenVerteilenAlgorithmus07_raum_zufaellig_gruppe_absteigend();
			dynDaten.aktionKlausurenVerteilenAlgorithmus08_raum_aufsteigend_gruppe_absteigend();
			dynDaten.aktionKlausurenVerteilenAlgorithmus09_raum_absteigend_gruppe_absteigend();
		} while (System.currentTimeMillis() < zeitEnde);

		dynDaten.aktionLadeGespeichertenZustandInDieConfig();
	}

}
