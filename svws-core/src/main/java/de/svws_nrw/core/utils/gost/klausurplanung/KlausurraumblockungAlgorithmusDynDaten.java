package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTerminRich;
import de.svws_nrw.core.utils.ArrayUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Algorithmus der Klausuren (der Schüler) eines bestimmten Klausurtermins auf vorgegebene Räume blockt
 * und dabei bestimmte Regeln beachtet bzw. optimiert.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurraumblockungAlgorithmusDynDaten {

	private final @NotNull Random _random;

	private final boolean _regel_optimiere_blocke_in_moeglichst_wenig_raeume;
	private final boolean _regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume;
	private final boolean _regel_forciere_selbe_kursklausur_im_selben_raum;
	private final boolean _regel_forciere_selbe_klausurdauer_pro_raum;

	private final int _raumAnzahl;
	private final @NotNull GostKlausurraumRich @NotNull [] _raumAt;
	private final @NotNull int[] _raumZuBelegung;     // dynamisch
	private final @NotNull int[] _raumZuKlausurdauer; // dynamisch

	private final int _klausurGruppenAnzahl;
	private final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTerminRich>> _klausurGruppen;
	private final @NotNull int[] _klausurGruppeZuKlausurdauer;
	private final @NotNull GostKlausurraumRich[] _klausurGruppeZuRaum;     // dynamisch
	private final @NotNull GostKlausurraumRich[] _klausurGruppeZuRaumSave; // dynamisch (nur zur Speicherung)

	/**
	 * Initialisiert alle Datenstrukturen um diese für schnelle Manipulation zur Verfügung zu stellen.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param config  Das {@link GostKlausurraumblockungKonfiguration}-Eingabr-Objekt der GUI.
	 */
	KlausurraumblockungAlgorithmusDynDaten(final @NotNull Random random, final @NotNull GostKlausurraumblockungKonfiguration config) {
		_random = random;

		// Regeln kopieren
		_regel_optimiere_blocke_in_moeglichst_wenig_raeume =  config._regel_optimiere_blocke_in_moeglichst_wenig_raeume;
		_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume =  config._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume;
		_regel_forciere_selbe_kursklausur_im_selben_raum =  config._regel_forciere_selbe_kursklausur_im_selben_raum;
		_regel_forciere_selbe_klausurdauer_pro_raum =  config._regel_forciere_selbe_klausurdauer_pro_raum;

		// Räume kopieren.
		_raumAnzahl = config.raeume.size();
		_raumAt = new GostKlausurraumRich[_raumAnzahl];
		_raumZuBelegung = new int[_raumAnzahl];
		_raumZuKlausurdauer = new int[_raumAnzahl];
		for (int i = 0; i < _raumAnzahl; i++)
			_raumAt[i] = config.raeume.get(i);

		// Klausuren kopieren.
		_klausurGruppen = _erzeugeKlausurGruppen(config.schuelerklausurtermine);
		_klausurGruppenAnzahl = _klausurGruppen.size();
		_klausurGruppeZuRaum = new GostKlausurraumRich[_klausurGruppenAnzahl];
		_klausurGruppeZuRaumSave = new GostKlausurraumRich[_klausurGruppenAnzahl];
		_klausurGruppeZuKlausurdauer = new int[_klausurGruppenAnzahl];
		for (int kg = 0; kg < _klausurGruppenAnzahl; kg++)
			_klausurGruppeZuKlausurdauer[kg] = _gibErsteKlausurDerGruppe(kg).dauer;

		// Zuordnung erzeugen.
		aktionZustandClear();
	}

	private @NotNull GostSchuelerklausurTerminRich _gibErsteKlausurDerGruppe(final int kg) {
		final @NotNull List<@NotNull GostSchuelerklausurTerminRich> list = ListUtils.getNonNullElementAtOrException(_klausurGruppen, kg);
		return ListUtils.getNonNullElementAtOrException(list, 0);
	}

	private @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTerminRich>> _erzeugeKlausurGruppen(final @NotNull List<@NotNull GostSchuelerklausurTerminRich> klausuren) {
		final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTerminRich>> gruppen = new ArrayList<>();

		if (_regel_forciere_selbe_kursklausur_im_selben_raum) {
			// Gruppiere Klausuren nach "idKursklausur".
			final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull GostSchuelerklausurTerminRich>> map = new HashMap<>();
			for (final @NotNull GostSchuelerklausurTerminRich klausur : klausuren)
				MapUtils.addToList(map, klausur.idKursklausur, klausur);
			gruppen.addAll(map.values());
		} else {
			// Jede Klausur hat eine Gruppe.
			for (final @NotNull GostSchuelerklausurTerminRich klausur : klausuren)
				gruppen.add(ListUtils.create1(klausur));
		}

		return gruppen;
	}

	private void aktionZustandClear() {
		// Alle Räume leeren.
		for (int r = 0; r < _raumAnzahl; r++) {
			_raumZuBelegung[r] = 0;
			_raumZuKlausurdauer[r] = -1; // keine Zuordnung
		}

		// Alle Klausuren leeren.
		for (int k = 0; k < _klausurGruppenAnzahl; k++)
			_klausurGruppeZuRaum[k] = null;
	}

	private boolean aktionSetzeKlausurgruppeInDenRaum(final int kg, final int r) {
		final @NotNull List<GostSchuelerklausurTerminRich> gruppe = _klausurGruppen.get(kg);

		// Ist noch Platz für alle Klausuren der Gruppe?
		if (_raumZuBelegung[r] + gruppe.size() > _raumAt[r].groesse)
			return false;

		// Ist die Klausurdauer in dem Raum überhaupt erlaubt?
		if ((_regel_forciere_selbe_klausurdauer_pro_raum) && (_raumZuKlausurdauer[r] >= 0) && (_klausurGruppeZuKlausurdauer[kg] != _raumZuKlausurdauer[r]))
			return false;

		// Raum-Zuordnungen
		_raumZuBelegung[r] += gruppe.size();
		if (_regel_forciere_selbe_klausurdauer_pro_raum)
			_raumZuKlausurdauer[r] = _klausurGruppeZuKlausurdauer[kg];

		// Klausur-Zuordnungen
		_klausurGruppeZuRaum[kg] = _raumAt[r];

		return true;
	}

	private double gibMalus(final @NotNull GostKlausurraumRich[] klausurGruppeZuRaum) {
		double malus = 0.0;
		malus += gibMalus_nicht_verteiler_klausuren(klausurGruppeZuRaum);
		malus += gibMalus_regel_optimiere_blocke_in_moeglichst_wenig_raeume(klausurGruppeZuRaum);
		malus += gibMalus_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume(klausurGruppeZuRaum);
		return malus;
	}

	private double gibMalus_nicht_verteiler_klausuren(final @NotNull GostKlausurraumRich[] klausurGruppeZuRaum) {
		double malus = 0.0;
		for (int i = 0; i < klausurGruppeZuRaum.length; i++)
			if (klausurGruppeZuRaum[i] == null)
				malus += _klausurGruppen.get(i).size() * 1000.0;
		return malus;
	}


	// Attribut "_raumZuBelegung" geht nicht, da man den lokalen Parameter analysieren muss!
	private double gibMalus_regel_optimiere_blocke_in_moeglichst_wenig_raeume(final @NotNull GostKlausurraumRich[] klausurGruppeZuRaum) {
		if (!_regel_optimiere_blocke_in_moeglichst_wenig_raeume)
			return 0.0;

		double malus = 0.0;

		for (int r = 0; r < _raumAnzahl; r++) {
			final @NotNull GostKlausurraumRich raum1 = _raumAt[r];

			// Zähle Klausuren im Raum.
			int counter = 0;
			for (int k = 0; k < _klausurGruppenAnzahl; k++) {
				final GostKlausurraumRich raum2 = klausurGruppeZuRaum[k];
				if (raum2 == null)
					continue;
				if (raum1.id != raum2.id)
					continue;
				counter++;
			}

			// Wird der Raum genutzt?
			if (counter > 0)
				malus += 1;
		}

		return malus;
	}

	// Attribut "_raumZuBelegung" geht nicht, da man den lokalen Parameter analysieren muss!
	private double gibMalus_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume(final @NotNull GostKlausurraumRich[] klausurGruppeZuRaum) {
		if (!_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume)
			return 0.0;

		int maximum = 0; // Der Raum mit den meisten Klausuren.

		for (int r = 0; r < _raumAnzahl; r++) {
			final @NotNull GostKlausurraumRich raum1 = _raumAt[r];

			// Zähle Klausuren im Raum.
			int counter = 0;
			for (int k = 0; k < _klausurGruppenAnzahl; k++) {
				final GostKlausurraumRich raum2 = klausurGruppeZuRaum[k];
				if (raum2 == null)
					continue;
				if (raum1.id != raum2.id)
					continue;
				counter++;
			}

			maximum = Math.max(maximum, counter);
		}

		return 1.0 * maximum;
	}

	/**
	 * Speichert die aktuelle Raum-Klausur-Zuordnung sowie die Güte der Bewertung,
	 * falls er besser ist als der zuvor gespeicherte Zustand.
	 */
	private void aktionSpeichernFallsBesser() {
		final double malusSave = gibMalus(_klausurGruppeZuRaumSave);
		final double malus = gibMalus(_klausurGruppeZuRaum);

		if (malus >= malusSave)
			return;

		// Der jetzige Zustand ist besser --> Speichere die Klausur-Schienen-Zuordnung.
		System.arraycopy(_klausurGruppeZuRaum, 0, _klausurGruppeZuRaumSave, 0, _klausurGruppenAnzahl);
	}

	/**
	 * Verteilt alle Klausuren zufällig auf die Räume.
	 * Dabei werden die Räume nacheinander aufgefüllt.
	 */
	void aktionKlausurenVerteilenAlgorithmus00_zufaellig() {
		aktionZustandClear();

		final int[] randomR = ArrayUtils.getIndexPermutation(_raumAnzahl, _random);
		final int[] randomKG = ArrayUtils.getIndexPermutation(_klausurGruppen.size(), _random);

		for (final int kg : randomKG)
			for (final int r : randomR)
				if (aktionSetzeKlausurgruppeInDenRaum(kg, r))
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
			for (int kg = 0; kg < _klausurGruppenAnzahl; kg++) {
				final GostKlausurraumRich raum2 = _klausurGruppeZuRaumSave[kg];
				if (raum2 == null)
					continue;
				if (raum2.id != raum.id)
					continue;
				// Alle Klausuren der Gruppe dem Raum hinzufügen.
				for (final @NotNull GostSchuelerklausurTerminRich klausur : _klausurGruppen.get(kg))
					raum.schuelerklausurterminIDs.add(klausur.id);
			}
		}
	}

}
