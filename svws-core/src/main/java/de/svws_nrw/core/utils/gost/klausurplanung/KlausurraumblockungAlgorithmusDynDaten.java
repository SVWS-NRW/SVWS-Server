package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.Random;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTerminRich;
import de.svws_nrw.core.utils.ArrayUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Algorithmus der Klausuren (der Schüler) eines bestimmten Klausurtermins auf vorgegebene Räume blockt
 * und dabei bestimmte Regeln beachtet bzw. optimiert.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurraumblockungAlgorithmusDynDaten {

	private final @NotNull Random _random;

	private final double _regel_aehnliche_klausurdauer_pro_raum;
	private final double _regel_blocke_in_moeglichst_wenig_raeume;
	private final double _regel_selbe_kursklausur_in_selben_raum;

	private final int _raumAnzahl;
	private final @NotNull GostKlausurraumRich @NotNull [] _raumAt;
	private final @NotNull int @NotNull [] _raumAtBelegt;

	private final int _klausurAnzahl;
	private final @NotNull GostSchuelerklausurTerminRich @NotNull [] _klausurAt;
	private final @NotNull GostKlausurraumRich[] _klausurZuRaum;
	private final @NotNull GostKlausurraumRich[] _klausurZuRaumSave;

	/**
	 * Initialisiert alle Datenstrukturen um diese für schnelle Manipulation zur Verfügung zu stellen.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param config  Das {@link GostKlausurraumblockungKonfiguration}-Eingabr-Objekt der GUI.
	 */
	KlausurraumblockungAlgorithmusDynDaten(final @NotNull Random random, final @NotNull GostKlausurraumblockungKonfiguration config) {
		_random = random;

		_regel_aehnliche_klausurdauer_pro_raum =  config._regel_aehnliche_klausurdauer_pro_raum;
		_regel_blocke_in_moeglichst_wenig_raeume =  config._regel_blocke_in_moeglichst_wenig_raeume;
		_regel_selbe_kursklausur_in_selben_raum =  config._regel_selbe_kursklausur_in_selben_raum;

		// Räume kopieren.
		_raumAnzahl = config.raeume.size();
		_raumAt = new GostKlausurraumRich[_raumAnzahl];
		_raumAtBelegt = new int[_raumAnzahl];
		for (int i = 0; i < _raumAnzahl; i++)
			_raumAt[i] = config.raeume.get(i);

		// Klausuren kopieren.
		_klausurAnzahl = config.schuelerklausurtermine.size();
		_klausurAt = new GostSchuelerklausurTerminRich[_klausurAnzahl];
		_klausurZuRaum = new GostKlausurraumRich[_klausurAnzahl];
		_klausurZuRaumSave = new GostKlausurraumRich[_klausurAnzahl];
		for (int i = 0; i < _klausurAnzahl; i++)
			_klausurAt[i] = config.schuelerklausurtermine.get(i);

		// Zuordnung erzeugen.
		aktionZustandClear();
	}

	private void aktionZustandClear() {
		// Alle Räume leeren.
		for (int r = 0; r < _raumAnzahl; r++)
			_raumAtBelegt[r] = 0;

		// Alle Klausuren leeren.
		for (int k = 0; k < _klausurAnzahl; k++)
			_klausurZuRaum[k] = null;
	}

	private double gibMalus(final @NotNull GostKlausurraumRich[] klausurZuRaum) {
		double malus = 0.0;

		// MALUS: nicht verteilte Klausuren.
		for (int i = 0; i < _klausurAnzahl; i++)
			if (klausurZuRaum[i] == null)
				malus += 1000.0;

		malus += gibMalus_regel_aehnliche_klausurdauer_pro_raum(klausurZuRaum);
		malus += gibMalus_regel_blocke_in_moeglichst_wenig_raeume(klausurZuRaum);
		malus += gibMalus_regel_selbe_kursklausur_in_selben_raum(klausurZuRaum);

		return malus;
	}

	private double gibMalus_regel_aehnliche_klausurdauer_pro_raum(final @NotNull GostKlausurraumRich[] klausurZuRaum) {
		if (_regel_aehnliche_klausurdauer_pro_raum <= 0.0)
			return 0;

		double malus = 0.0;

		for (int k1 = 0; k1 < _klausurAnzahl; k1++)
			for (int k2 = k1 + 1; k2 < _klausurAnzahl; k2++)
				if ((_klausurAt[k1].dauer != _klausurAt[k2].dauer) && gibKlausurImSelbenRaum(klausurZuRaum, k1, k2))
					malus += _regel_aehnliche_klausurdauer_pro_raum;

		return malus;
	}

	private double gibMalus_regel_blocke_in_moeglichst_wenig_raeume(@NotNull final GostKlausurraumRich[] klausurZuRaum) {
		if (_regel_blocke_in_moeglichst_wenig_raeume <= 0.0)
			return 0.0;

		double malus = 0.0;

		for (int r = 0; r < _raumAnzahl; r++) {
			final @NotNull GostKlausurraumRich raum1 = _raumAt[r];

			int counter = 0;
			for (int k = 0; k < _klausurAnzahl; k++) {
				final GostKlausurraumRich raum2 = klausurZuRaum[k];
				if (raum2 == null)
					continue;
				if (raum1.id != raum2.id)
					continue;
				counter++;
			}

			if (counter > 0)
				malus += _regel_blocke_in_moeglichst_wenig_raeume;
		}

		return malus;
	}

	private double gibMalus_regel_selbe_kursklausur_in_selben_raum(@NotNull final GostKlausurraumRich[] klausurZuRaum) {
		if (_regel_selbe_kursklausur_in_selben_raum <= 0.0)
			return 0.0;

		double malus = 0.0;

		for (int k1 = 0; k1 < _klausurAnzahl; k1++)
			for (int k2 = k1 + 1; k2 < _klausurAnzahl; k2++)
				if ((_klausurAt[k1].idKursklausur == _klausurAt[k2].idKursklausur) && !gibKlausurImSelbenRaum(klausurZuRaum, k1, k2))
					malus += _regel_selbe_kursklausur_in_selben_raum;

		return malus;
	}

	private static boolean gibKlausurImSelbenRaum(final @NotNull GostKlausurraumRich[] klausurZuRaum, final int k1, final int k2) {
		final GostKlausurraumRich raum1 = klausurZuRaum[k1];
		final GostKlausurraumRich raum2 = klausurZuRaum[k2];
		if (raum1 == null)
			return false;
		if (raum2 == null)
			return false;
		return raum1.id == raum2.id;
	}

	private boolean aktionSetzeKlausurInDenRaum(final int k, final int r) {
		if (_raumAtBelegt[r] >= _raumAt[r].groesse)
			return false;

		if (_klausurZuRaum[k] != null)
			return false;

		_klausurZuRaum[k] = _raumAt[r];
		_raumAtBelegt[r]++;
		return true;
	}

	/**
	 * Speichert die aktuelle Raum-Klausur-Zuordnung sowie die Güte der Bewertung,
	 * falls er besser ist als der zuvor gespeicherte Zustand.
	 */
	private void aktionSpeichernFallsBesser() {
		final double malusSave = gibMalus(_klausurZuRaumSave);
		final double malus = gibMalus(_klausurZuRaum);

		if (malus >= malusSave)
			return;

		// Der jetzige Zustand ist besser --> Speichere die Klausur-Schienen-Zuordnung.
		System.arraycopy(_klausurZuRaum, 0, _klausurZuRaumSave, 0, _klausurAnzahl);
	}

	/**
	 * Verteilt alle Klausuren zufällig auf die Räume.
	 * Dabei werden die Räume nacheinander aufgefüllt.
	 */
	void aktionKlausurenVerteilenAlgorithmus00_zufaellig() {
		aktionZustandClear();

		final int[] randomR = ArrayUtils.getIndexPermutation(_raumAnzahl, _random);
		final int[] randomK = ArrayUtils.getIndexPermutation(_klausurAnzahl, _random);

		for (final int k : randomK)
			for (final int r : randomR)
				if (aktionSetzeKlausurInDenRaum(k, r))
					break;

		aktionSpeichernFallsBesser();
	}

	/**
	 * Verteilt die Klausuren mit Algorithmus 01.
	 */
	void aktionKlausurenVerteilenAlgorithmus01() {
		// empty
	}

	/**
	 * Verteilt die Klausuren mit Algorithmus 02.
	 */
	void aktionKlausurenVerteilenAlgorithmus02() {
		// empty
	}

	/**
	 * Verteilt die Klausuren mit Algorithmus 03.
	 */
	void aktionKlausurenVerteilenAlgorithmus03() {
		// empty
	}

	/**
	 * Lädt den gespeicherten Zustand die {@link GostKlausurraumRich#schuelerklausurterminIDs}-Liste.
	 */
	void aktionLadeGespeichertenZustand() {
		for (int r = 0; r < _raumAnzahl; r++) {
			// Leere die Klausuren-Liste des Raumes.
			final @NotNull GostKlausurraumRich raum = _raumAt[r];
			raum.schuelerklausurterminIDs.clear();

			// Fülle die Klausuren-Liste des Raumes.
			for (int k = 0; k < _klausurAnzahl; k++) {
				final GostKlausurraumRich raum2 = _klausurZuRaumSave[k];
				if (raum2 == null)
					continue;
				if (raum2.id == raum.id)
					raum.schuelerklausurterminIDs.add(_klausurAt[k].id);
			}
		}
	}

}
