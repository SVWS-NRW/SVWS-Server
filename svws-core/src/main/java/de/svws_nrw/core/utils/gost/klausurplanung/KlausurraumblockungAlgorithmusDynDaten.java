package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
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

	private static final double MALUS_NICHT_VERTEILT = 1000000.0;
	private static final double MALUS_MOEGLICHST_WENIG_RAEUME = 1000.0;
	private static final double MALUS_MOEGLICHST_GLEICHVERTEILT_AUF_RAEUME = 1.0;

	private static final @NotNull Comparator<GostKlausurraumRich> _compRaeume =
			(final @NotNull GostKlausurraumRich o1, final @NotNull GostKlausurraumRich o2) -> {
				if (o1.groesse < o2.groesse)
					return -1;
				if (o1.groesse > o2.groesse)
					return +1;

				if (o1.klausurraum.id < o2.klausurraum.id)
					return -1;
				if (o1.klausurraum.id > o2.klausurraum.id)
					return +1;

				return 0;
			};

	private static final @NotNull Comparator<List<GostSchuelerklausurTerminRich>> _compKlausurGruppen =
			(final @NotNull List<GostSchuelerklausurTerminRich> o1, final @NotNull List<GostSchuelerklausurTerminRich> o2) -> {
				if (o1.size() < o2.size())
					return -1;
				if (o1.size() > o2.size())
					return +1;

				// Falls die Gruppen die gleiche Größe haben, wird nach der ID des ersten Repräsentanten sortiert.
				final @NotNull GostSchuelerklausurTerminRich k1 = ListUtils.getNonNullElementAtOrException(o1, 0);
				final @NotNull GostSchuelerklausurTerminRich k2 = ListUtils.getNonNullElementAtOrException(o2, 0);
				if (k1.id < k2.id)
					return -1;
				if (k1.id > k2.id)
					return +1;

				return 0;
			};

	private final @NotNull Random _random;
	private final @NotNull GostKlausurraumblockungKonfiguration _config;

	private final boolean _regel_optimiere_blocke_in_moeglichst_wenig_raeume;
	private final boolean _regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume;
	private final boolean _regel_forciere_selbe_kursklausur_im_selben_raum;
	private final boolean _regel_forciere_selbe_klausurdauer_pro_raum;
	private final boolean _regel_forciere_selben_klausurstart_pro_raum;

	private final int _raumAnzahl;                                  // statisch
	private final @NotNull GostKlausurraumRich @NotNull [] _raumAt; // statisch
	private final @NotNull int[] _raumSortiertAufsteigend;          // statisch
	private final @NotNull int[] _raumSortiertAbsteigend;           // statisch
	private final @NotNull int[] _raumZuBelegung;                   // dynamisch
	private final @NotNull int[] _raumZuKlausurdauer;               // dynamisch
	private final @NotNull int[] _raumZuKlausurstart;               // dynamisch

	private final int _klausurGruppenAnzahl;                                                            // statisch
	private final @NotNull List<List<GostSchuelerklausurTerminRich>> _klausurGruppen; // statisch
	private final @NotNull int[] _klausurGruppenAufsteigend;                                            // statisch
	private final @NotNull int[] _klausurGruppenAbsteigend;                                             // statisch
	private final @NotNull int[] _klausurGruppeZuKlausurdauer;                                          // statisch
	private final @NotNull int[] _klausurGruppeZuKlausurstart;                                          // statisch
	private final @NotNull GostKlausurraumRich[] _klausurGruppeZuRaum;                                  // dynamisch
	private final @NotNull GostKlausurraumRich[] _klausurGruppeZuRaumSave;                              // halb-dynamisch (nur zur Speicherung)

	/**
	 * Initialisiert alle Datenstrukturen um diese für schnelle Manipulation zur Verfügung zu stellen.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param config  Das {@link GostKlausurraumblockungKonfiguration}-Eingabr-Objekt der GUI.
	 */
	KlausurraumblockungAlgorithmusDynDaten(final @NotNull Random random, final @NotNull GostKlausurraumblockungKonfiguration config) {
		_random = random;
		_config = config;

		// Regeln kopieren
		_regel_optimiere_blocke_in_moeglichst_wenig_raeume = config._regel_optimiere_blocke_in_moeglichst_wenig_raeume;
		_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume = config._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume;
		_regel_forciere_selbe_kursklausur_im_selben_raum = config._regel_forciere_selbe_kursklausur_im_selben_raum;
		_regel_forciere_selbe_klausurdauer_pro_raum = config._regel_forciere_selbe_klausurdauer_pro_raum;
		_regel_forciere_selben_klausurstart_pro_raum = config._regel_forciere_selben_klausurstart_pro_raum;

		// Räume kopieren.
		_raumAnzahl = config.raeume.size();
		_raumAt = _erzeugeRaeumeSortiert(config.raeume);
		_raumZuBelegung = new int[_raumAnzahl];
		_raumZuKlausurdauer = new int[_raumAnzahl];
		_raumZuKlausurstart = new int[_raumAnzahl];
		_raumSortiertAufsteigend = new int[_raumAnzahl];
		_raumSortiertAbsteigend = new int[_raumAnzahl];
		for (int i = 0; i < _raumAnzahl; i++) {
			_raumSortiertAufsteigend[i] = i;
			_raumSortiertAbsteigend[i] = _raumAnzahl - 1 - i;
		}

		// Klausuren kopieren.
		_klausurGruppen = _erzeugeKlausurGruppenSortiert(config.schuelerklausurtermine);
		_klausurGruppenAnzahl = _klausurGruppen.size();
		_klausurGruppeZuRaum = new GostKlausurraumRich[_klausurGruppenAnzahl];
		_klausurGruppeZuRaumSave = new GostKlausurraumRich[_klausurGruppenAnzahl];
		_klausurGruppeZuKlausurdauer = new int[_klausurGruppenAnzahl];
		_klausurGruppeZuKlausurstart = new int[_klausurGruppenAnzahl];
		_klausurGruppenAufsteigend = new int[_klausurGruppenAnzahl];
		_klausurGruppenAbsteigend = new int[_klausurGruppenAnzahl];
		for (int kg = 0; kg < _klausurGruppenAnzahl; kg++) {
			_klausurGruppeZuKlausurdauer[kg] = _gibErsteKlausurDerGruppe(kg).dauer;
			_klausurGruppeZuKlausurstart[kg] = _gibErsteKlausurDerGruppe(kg).startzeit;
			_klausurGruppenAufsteigend[kg] = kg;
			_klausurGruppenAbsteigend[kg] = _klausurGruppenAnzahl - 1 - kg;
		}

		// Zuordnung erzeugen.
		aktionZustandClear();
	}

	private static @NotNull GostKlausurraumRich @NotNull [] _erzeugeRaeumeSortiert(final @NotNull List<GostKlausurraumRich> raeume) {
		final @NotNull List<GostKlausurraumRich> list = new ArrayList<>(raeume);
		list.sort(_compRaeume);


		final @NotNull GostKlausurraumRich @NotNull [] copy = new GostKlausurraumRich[list.size()];
		for (int i = 0; i < copy.length; i++)
			copy[i] = list.get(i);

		return copy;
	}

	private @NotNull GostSchuelerklausurTerminRich _gibErsteKlausurDerGruppe(final int kg) {
		final @NotNull List<GostSchuelerklausurTerminRich> list = ListUtils.getNonNullElementAtOrException(_klausurGruppen, kg);
		return ListUtils.getNonNullElementAtOrException(list, 0);
	}

	private @NotNull List<List<GostSchuelerklausurTerminRich>> _erzeugeKlausurGruppenSortiert(
			final @NotNull List<GostSchuelerklausurTerminRich> klausuren) {
		final @NotNull List<List<GostSchuelerklausurTerminRich>> gruppen = new ArrayList<>();

		if (_regel_forciere_selbe_kursklausur_im_selben_raum) {
			// Gruppiere Klausuren nach "idKursklausur".
			final @NotNull HashMap<Long, List<GostSchuelerklausurTerminRich>> map = new HashMap<>();
			for (final @NotNull GostSchuelerklausurTerminRich klausur : klausuren)
				MapUtils.addToList(map, klausur.idKursklausur, klausur);
			gruppen.addAll(map.values());
		} else {
			// Jede Klausur hat seine eigene Gruppe.
			for (final @NotNull GostSchuelerklausurTerminRich klausur : klausuren)
				gruppen.add(ListUtils.create1(klausur));
		}

		gruppen.sort(_compKlausurGruppen);
		return gruppen;
	}

	private void aktionZustandClear() {
		// Alle Räume leeren.
		for (int r = 0; r < _raumAnzahl; r++) {
			_raumZuBelegung[r] = 0;
			_raumZuKlausurdauer[r] = -1; // keine Zuordnung
			_raumZuKlausurstart[r] = -1; // keine Zuordnung
		}

		// Alle Klausuren leeren.
		for (int k = 0; k < _klausurGruppenAnzahl; k++)
			_klausurGruppeZuRaum[k] = null;
	}

	private boolean aktionSetzeKlausurgruppeInDenRaum(final int kg, final int r) {
		final @NotNull List<GostSchuelerklausurTerminRich> gruppe = _klausurGruppen.get(kg);

		// Ist noch Platz für alle Klausuren der Gruppe?
		if ((_raumZuBelegung[r] + gruppe.size()) > _raumAt[r].groesse)
			return false;

		// Ist die Klausur-Startzeit in dem Raum überhaupt erlaubt?
		if ((_regel_forciere_selben_klausurstart_pro_raum) && (_raumZuKlausurstart[r] >= 0) && (_klausurGruppeZuKlausurstart[kg] != _raumZuKlausurstart[r]))
			return false;

		// Ist die Klausur-Dauer in dem Raum überhaupt erlaubt?
		if ((_regel_forciere_selbe_klausurdauer_pro_raum) && (_raumZuKlausurdauer[r] >= 0) && (_klausurGruppeZuKlausurdauer[kg] != _raumZuKlausurdauer[r]))
			return false;

		// Raum-Zuordnungen
		_raumZuBelegung[r] += gruppe.size();
		if (_regel_forciere_selben_klausurstart_pro_raum)
			_raumZuKlausurstart[r] = _klausurGruppeZuKlausurstart[kg];
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
				malus += _klausurGruppen.get(i).size() * MALUS_NICHT_VERTEILT;
		return malus;
	}


	// Attribut "_raumZuBelegung" geht nicht, da man den lokalen Parameter analysieren muss!
	private double gibMalus_regel_optimiere_blocke_in_moeglichst_wenig_raeume(final @NotNull GostKlausurraumRich[] klausurGruppeZuRaum) {
		if (!_regel_optimiere_blocke_in_moeglichst_wenig_raeume)
			return 0.0;

		double malus = 0.0;

		for (int r = 0; r < _raumAnzahl; r++) {
			final @NotNull GostKlausurraumRich raum1 = _raumAt[r];

			// Zähle KlausurGruppen im Raum.
			int counterGruppen = 0;
			for (int k = 0; k < _klausurGruppenAnzahl; k++) {
				final GostKlausurraumRich raum2 = klausurGruppeZuRaum[k];
				if (raum2 == null)
					continue;
				if (raum1.klausurraum.id != raum2.klausurraum.id)
					continue;
				counterGruppen++;
			}

			// Wird der Raum genutzt?
			if (counterGruppen > 0)
				malus += MALUS_MOEGLICHST_WENIG_RAEUME;
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
			int counterKlausuren = 0;
			for (int k = 0; k < _klausurGruppenAnzahl; k++) {
				final GostKlausurraumRich raum2 = klausurGruppeZuRaum[k];
				if (raum2 == null)
					continue;
				if (raum1.klausurraum.id != raum2.klausurraum.id)
					continue;
				counterKlausuren += _klausurGruppen.get(k).size();
			}

			maximum = Math.max(maximum, counterKlausuren);
		}

		// Das Maximum aller Klausuren pro Raum muss minimiert werden.
		return maximum * MALUS_MOEGLICHST_GLEICHVERTEILT_AUF_RAEUME;
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

	private void aktionKlausurenVerteilenAlgorithmusGeneric(final int[] aRaum, final int[] aKlausurGruppe) {
		aktionZustandClear();

		for (final int kg : ((aKlausurGruppe == null) ? ArrayUtils.getIndexPermutation(_klausurGruppenAnzahl, _random) : aKlausurGruppe))
			for (final int r : ((aRaum == null) ? ArrayUtils.getIndexPermutation(_raumAnzahl, _random) : aRaum))
				if (aktionSetzeKlausurgruppeInDenRaum(kg, r))
					break;

		aktionSpeichernFallsBesser();
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in zufälliger Reihenfolge.
	 * <br>Klausurgruppen in zufälliger Reihenfolge.
	 */
	void aktionKlausurenVerteilenAlgorithmus01_raum_zufaellig_gruppe_zufaellig() {
		aktionKlausurenVerteilenAlgorithmusGeneric(null, null);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in aufsteigender Reihenfolge.
	 * <br>Klausurgruppen in zufälliger Reihenfolge.
	 */
	void aktionKlausurenVerteilenAlgorithmus02_raum_aufsteigend_gruppe_zufaellig() {
		aktionKlausurenVerteilenAlgorithmusGeneric(_raumSortiertAufsteigend, null);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in absteigender Reihenfolge.
	 * <br>Klausurgruppen in zufälliger Reihenfolge.
	 */
	void aktionKlausurenVerteilenAlgorithmus03_raum_absteigend_gruppe_zufaellig() {
		aktionKlausurenVerteilenAlgorithmusGeneric(_raumSortiertAbsteigend, null);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in zufälliger Reihenfolge.
	 * <br>Klausurgruppen in aufsteigender Reihenfolge.
	 */
	void aktionKlausurenVerteilenAlgorithmus04_raum_zufaellig_gruppe_aufsteigend() {
		aktionKlausurenVerteilenAlgorithmusGeneric(null, _klausurGruppenAufsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in aufsteigender Reihenfolge.
	 * <br>Klausurgruppen in aufsteigender Reihenfolge.
	 */
	void aktionKlausurenVerteilenAlgorithmus05_raum_aufsteigend_gruppe_aufsteigend() {
		aktionKlausurenVerteilenAlgorithmusGeneric(_raumSortiertAufsteigend, _klausurGruppenAufsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in absteigender Reihenfolge.
	 * <br>Klausurgruppen in aufsteigender Reihenfolge.
	 */
	void aktionKlausurenVerteilenAlgorithmus06_raum_absteigend_gruppe_aufsteigend() {
		aktionKlausurenVerteilenAlgorithmusGeneric(_raumSortiertAbsteigend, _klausurGruppenAufsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in zufälliger Reihenfolge.
	 * <br>Klausurgruppen in absteigender Reihenfolge.
	 */
	void aktionKlausurenVerteilenAlgorithmus07_raum_zufaellig_gruppe_absteigend() {
		aktionKlausurenVerteilenAlgorithmusGeneric(null, _klausurGruppenAbsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in aufsteigender Reihenfolge.
	 * <br>Klausurgruppen in absteigender Reihenfolge.
	 */
	void aktionKlausurenVerteilenAlgorithmus08_raum_aufsteigend_gruppe_absteigend() {
		aktionKlausurenVerteilenAlgorithmusGeneric(_raumSortiertAufsteigend, _klausurGruppenAbsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in absteigender Reihenfolge.
	 * <br>Klausurgruppen in absteigender Reihenfolge.
	 */
	void aktionKlausurenVerteilenAlgorithmus09_raum_absteigend_gruppe_absteigend() {
		aktionKlausurenVerteilenAlgorithmusGeneric(_raumSortiertAbsteigend, _klausurGruppenAbsteigend);
	}

	/**
	 * Lädt den gespeicherten Zustand die {@link GostKlausurraumRich#schuelerklausurterminIDs}-Liste.
	 * Überschreibt {@link GostKlausurraumblockungKonfiguration#schuelerklausurtermine} mit den Klausuren, die nicht verteilt wurden.
	 */
	void aktionLadeGespeichertenZustandInDieConfig() {
		// Verteile die Klausuren auf ihre Räume (falls es eine Zuordnung gibt).
		for (int r = 0; r < _raumAnzahl; r++) {
			// Leere die Klausuren-Liste des Raumes.
			final @NotNull GostKlausurraumRich raum = _raumAt[r];
			raum.schuelerklausurterminIDs.clear();

			// Fülle die Klausuren-Liste des Raumes.
			for (int kg = 0; kg < _klausurGruppenAnzahl; kg++) {
				final GostKlausurraumRich raum2 = _klausurGruppeZuRaumSave[kg];
				if (raum2 == null)
					continue;
				if (raum2.klausurraum.id != raum.klausurraum.id)
					continue;
				// Alle Klausuren der Gruppe dem Raum hinzufügen.
				for (final @NotNull GostSchuelerklausurTerminRich klausur : _klausurGruppen.get(kg))
					raum.schuelerklausurterminIDs.add(klausur.id);
			}
		}

		// Überschreibe die Klausur-Liste mit den Klausuren, die nicht verteilt werden konnten.
		_config.schuelerklausurtermine.clear();
		for (int kg = 0; kg < _klausurGruppenAnzahl; kg++)
			if (_klausurGruppeZuRaumSave[kg] == null)
				_config.schuelerklausurtermine.addAll(_klausurGruppen.get(kg));
	}

}
