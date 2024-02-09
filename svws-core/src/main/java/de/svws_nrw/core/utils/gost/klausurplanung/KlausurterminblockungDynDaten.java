package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnisTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungKonfiguration;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungAlgorithmen;
import de.svws_nrw.core.utils.ArrayUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Eine dynamische Datenstruktur zum Speichern der aktuellen Klausur-Termin-Lage.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurterminblockungDynDaten {

	/** Ein Maximal-Wert für die maximale Anzahl an Terminen. */
	private static final int MAX_TERMINE = 1000;

	/** Ein {@link Logger}-Objekt für Debug-Zwecke. */
	private final @NotNull Logger _logger;

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Die Anzahl der derzeitigen verwendeten Termine. */
	private int _terminAnzahl = 0;
	private int _terminAnzahl1 = MAX_TERMINE; // Speicherzustand 1
	private int _terminAnzahl2 = MAX_TERMINE; // Speicherzustand 2

	/**
	 * Jeder KlausurNr wird eine TerminNr zugeordnet. Der Wert -1 definiert eine temporäre Nicht-Zuordnung.
	 * Am Ende des Algorithmus hat jede Klausur eine zugeordnete TerminNr >= 0.
	 */
	private final @NotNull int @NotNull [] _klausurZuTermin;
	private final @NotNull int @NotNull [] _klausurZuTermin1; // Speicherzustand 1
	private final @NotNull int @NotNull [] _klausurZuTermin2; // Speicherzustand 2

	/** Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _mapKlausurZuNummer = new HashMap<>();

	/** Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln. */
	private final @NotNull HashMap<@NotNull Integer, @NotNull GostKursklausurRich> _mapNummerZuKlausur = new HashMap<>();

	/** Die Anzahl der Klausuren. */
	private final int _klausurenAnzahl;

	/** Bestimmt, ob ein Klausurnummer-Paar am selben Termin verboten ist. */
	private final @NotNull boolean @NotNull [] @NotNull [] _verboten;

	/** Alle Klausurgruppen. */
	private final @NotNull List<@NotNull List<@NotNull Integer>> _klausurGruppen = new ArrayList<>();

	/** Alle Klausurgruppen, sortiert nach ihrem Knotengrad (Anzahl an Nachbarn). */
	private final @NotNull List<@NotNull List<@NotNull Integer>> _klausurGruppenGrad = new ArrayList<>();

	/**
	 * Der Konstruktor konvertiert die Eingabedaten der GUI in eine dynamische Datenstruktur,
	 * welche als Basis für alle Algorithmen zur schnellen Manipulation dient.
	 *
	 * @param pLogger Ein {@link Logger}-Objekt für Debug-Zwecke.
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 * @param pConfig Informationen zur Konfiguration des Algorithmus.
	 */
	public KlausurterminblockungDynDaten(final @NotNull Logger pLogger, final @NotNull Random pRandom, final @NotNull List<@NotNull GostKursklausurRich> pInput, final @NotNull GostKlausurterminblockungKonfiguration pConfig) {
		_logger = pLogger;
		_random = pRandom;

		initialisiereMapKlausuren(pInput);
		_klausurenAnzahl = _mapKlausurZuNummer.size();
		_klausurZuTermin = new int[_klausurenAnzahl];
		_klausurZuTermin1 = new int[_klausurenAnzahl];
		_klausurZuTermin2 = new int[_klausurenAnzahl];

		_verboten = new boolean[_klausurenAnzahl][_klausurenAnzahl];
		initialisiereMatrixVerboten(pInput);

		initialisiereKlausurgruppen(pInput, pConfig);
		checkKlausurgruppenOrException();

		initialisiereKlausurgruppenGrad();

		aktionClear();
	}

	private void checkKlausurgruppenOrException() {
		for (final @NotNull List<@NotNull Integer> gruppe : _klausurGruppen) {
			DeveloperNotificationException.ifTrue("Es wurde eine leere Gruppe gefunden!", gruppe.isEmpty());
			for (final int klausurNr1 : gruppe)
				for (final int klausurNr2 : gruppe)
					if (_verboten[klausurNr1][klausurNr2]) {
						// Analysiere das Problem genauer, warum es hier zum Fehler kommt.
						final @NotNull GostKursklausurRich klausur1 = gibKlausurOrException(klausurNr1);
						final @NotNull GostKursklausurRich klausur2 = gibKlausurOrException(klausurNr2);
						final @NotNull List<@NotNull Long> schnittmenge = ListUtils.getIntersection(klausur1.schuelerIds, klausur2.schuelerIds);
						DeveloperNotificationException.ifTrue("Die Schüler-Schnittmenge der Klausuren darf hier nicht leer sein!", schnittmenge.isEmpty());
						throw new UserNotificationException("Klausur " + klausur1.kursKurzbezeichnung + " und " + klausur2.kursKurzbezeichnung + " sind in einer Gruppe. Schüler-ID-Schnittmenge: " + schnittmenge);
					}
		}
	}

	private @NotNull Integer gibKlausurNrOrException(final @NotNull GostKursklausurRich pGostKursklausurRich) {
		return DeveloperNotificationException.ifNull("Kein Mapping zu gostKursklausur.id(" + pGostKursklausurRich.id + ")", _mapKlausurZuNummer.get(pGostKursklausurRich.id));
	}

	private @NotNull GostKursklausurRich gibKlausurOrException(final int klausurNr) {
		return DeveloperNotificationException.ifNull("Kein Mapping zur Klausur Nr. " + klausurNr, _mapNummerZuKlausur.get(klausurNr));
	}

	private void initialisiereKlausurgruppen(final @NotNull List<@NotNull GostKursklausurRich> pInput, final @NotNull GostKlausurterminblockungKonfiguration pConfig) {
		final @NotNull KlausurterminblockungAlgorithmen algo = KlausurterminblockungAlgorithmen.getOrException(pConfig.algorithmus);
		switch (algo) {
			case NORMAL:
				// Jede Gruppe besteht aus einer einzelnen Klausur
				initialisiereKlausurgruppenNormal(pInput);
				break;
			case FAECHERWEISE:
				// Jede Gruppe besteht aus allen Klausuren des selben Faches.
				initialisiereKlausurgruppenFaecherweise(pInput);
				break;
			case SCHIENENWEISE:
				// Jede Gruppe besteht aus allen Klausuren der selben Schiene.
				initialisiereKlausurgruppenSchienenweise(pInput);
				break;
		}

	}

	private void initialisiereKlausurgruppenNormal(final @NotNull List<@NotNull GostKursklausurRich> pInput) {
		for (final @NotNull GostKursklausurRich gostKursklausur : pInput) {
			final @NotNull Integer klausurNr = gibKlausurNrOrException(gostKursklausur);
			final @NotNull List<@NotNull Integer> gruppe = new ArrayList<>();
			gruppe.add(klausurNr); // Jede Gruppe besteht aus genau einer Klausur.
			_klausurGruppen.add(gruppe);
		}
	}

	private void initialisiereKlausurgruppenFaecherweise(final @NotNull List<@NotNull GostKursklausurRich> pInput) {
		final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull Integer>> mapFachZuKlausurGruppe = new HashMap<>();

		for (final @NotNull GostKursklausurRich gostKursklausur : pInput) {
			final @NotNull Integer klausurNr = gibKlausurNrOrException(gostKursklausur);

			final long fachID = gostKursklausur.idFach;
			if (fachID < 0) {
				// Ohne FachID --> Erzeuge eine Einzelgruppe.
				_klausurGruppen.add(ListUtils.create1(klausurNr));
			} else {
				// Mit FachID --> Suche zugehörige Gruppe.
				MapUtils.addToList(mapFachZuKlausurGruppe, fachID, klausurNr);
			}
		}

		// Alle Gruppen der Map hinzufügen.
		_klausurGruppen.addAll(mapFachZuKlausurGruppe.values());
	}

	private void initialisiereKlausurgruppenSchienenweise(final @NotNull List<@NotNull GostKursklausurRich> pInput) {
		final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull Integer>> mapSchieneZuKlausurGruppe = new HashMap<>();

		for (final @NotNull GostKursklausurRich gostKursklausur : pInput) {
			final @NotNull Integer klausurNr = gibKlausurNrOrException(gostKursklausur);

			final long schienenID = gostKursklausur.kursSchiene.length < 1 ? -1 : gostKursklausur.kursSchiene[0]; // TODO BAR Wie wählt man bei Multi-Schienen?
			if (schienenID < 0) {
				// Ohne schienenID --> Erzeuge eine Einzelgruppe.
				_klausurGruppen.add(ListUtils.create1(klausurNr));
			} else {
				// Mit schienenID --> Suche zugehörige Gruppe
				MapUtils.addToList(mapSchieneZuKlausurGruppe, schienenID, klausurNr);
			}
		}

		// Alle Gruppen der Map hinzufügen.
		_klausurGruppen.addAll(mapSchieneZuKlausurGruppe.values());
	}

	private void initialisiereKlausurgruppenGrad() {
		// Kopieren
		_klausurGruppenGrad.addAll(_klausurGruppen);

		// InsertionSort von '_klausurenSortiertGrad'.
		for (int i = 1; i < _klausurGruppenGrad.size(); i++)
			for (int j = i; j >= 1; j--) {
				final @NotNull List<@NotNull Integer> gruppeR = _klausurGruppenGrad.get(j);
				final @NotNull List<@NotNull Integer> gruppeL = _klausurGruppenGrad.get(j - 1);
				final int gradR = gibKnotengrad(gruppeR);
				final int gradL = gibKnotengrad(gruppeL);
				if (gradL >= gradR) break; // bereits richtig einsortiert.
				_klausurGruppenGrad.set(j, gruppeL);
				_klausurGruppenGrad.set(j - 1, gruppeR);
			}
	}

	private int gibKnotengrad(final @NotNull List<@NotNull Integer> pGruppe) {
		int grad = 0;
		for (final @NotNull List<@NotNull Integer> gruppe : _klausurGruppen)
			if (gibIstVerboten(pGruppe, gruppe))
				grad++;
		return grad;
	}

	private boolean gibIstVerboten(final @NotNull List<@NotNull Integer> pGruppe1, final @NotNull List<@NotNull Integer> pGruppe2) {
		for (final int klausurNr1 : pGruppe1)
			for (final int klausurNr2 : pGruppe2)
				if (_verboten[klausurNr1][klausurNr2])
					return true;
		return false;
	}

	private void initialisiereMapKlausuren(final @NotNull List<@NotNull GostKursklausurRich> pInput) {
		for (final @NotNull GostKursklausurRich gostKursklausur : pInput) {
			if (gostKursklausur.id < 0) throw new DeveloperNotificationException("Klausur-ID=" + gostKursklausur.id + " ist negativ!");
			if (_mapKlausurZuNummer.containsKey(gostKursklausur.id)) throw new DeveloperNotificationException("Klausur-ID=" + gostKursklausur.id + " ist doppelt!");
			// Mapping: datenbankKlausurID --> laufende Nummer
			final int klausurNummer = _mapKlausurZuNummer.size();
			_mapKlausurZuNummer.put(gostKursklausur.id, klausurNummer);
			_mapNummerZuKlausur.put(klausurNummer, gostKursklausur);
		}
	}

	private void initialisiereMatrixVerboten(final @NotNull List<@NotNull GostKursklausurRich> pInput) {

		// Erzeuge eine Map: SchülerID --> Liste seiner KlausurNummern
		final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull Integer>> mapSchuelerKlausuren = new HashMap<>();
		for (final @NotNull GostKursklausurRich gostKursklausur : pInput)
			for (final @NotNull Long schuelerID : gostKursklausur.schuelerIds) {
				final @NotNull Integer klausurNr = gibKlausurNrOrException(gostKursklausur);
				MapUtils.addToList(mapSchuelerKlausuren, schuelerID, klausurNr);
			}

		// Verbiete Klausur-Paare
		for (final @NotNull Entry<@NotNull Long, @NotNull List<@NotNull Integer>> e : mapSchuelerKlausuren.entrySet()) {
			final @NotNull List<@NotNull Integer> list = e.getValue();
			for (final int klausurNr1 : list)
				for (final int klausurNr2 : list)
					if (klausurNr1 != klausurNr2)
						_verboten[klausurNr1][klausurNr2] = true;
		}

	}

	/**
	 * Liefert die aktuelle Anzahl an Terminen.
	 *
	 * @return die aktuelle Anzahl an Terminen.
	 */
	int gibTerminAnzahl() {
		return _terminAnzahl;
	}

	/**
	 * Liefert die Anzahl an Klausurgruppen. Dies ist ein theoretisches Maximum
	 * für die größte Anzahl an Terminen.
	 *
	 * @return die Anzahl an Klausurgruppen.
	 */
	int gibKlausurgruppenAnzahl() {
		return _klausurGruppen.size();
	}

	/**
	 * Liefert die Anzahl noch nicht verteilter Klausuren.
	 *
	 * @return die Anzahl noch nicht verteilter Klausuren.
	 */
	boolean gibExistierenNichtverteilteKlausuren() {
		for (int klausurNr = 0; klausurNr < _klausurenAnzahl; klausurNr++)
			if (_klausurZuTermin[klausurNr] < 0)
				return true;
		return false;
	}

	/**
	 * Liefert die Nummer eines neu erzeugten Termins.
	 *
	 * @return die Nummer eines neu erzeugten Termins.
	 */
	int gibErzeugeNeuenTermin() {
		_terminAnzahl++;
		return _terminAnzahl - 1;
	}

	/**
	 * Löscht den letzten Termin.
	 */
	void entferneLetztenTermin() {
		_terminAnzahl--;
	}

	/**
	 * Liefert ein Array aller derzeit verwendeten Termine in zufälliger Reihenfolge.
	 *
	 * @return ein Array aller derzeit verwendeten Termine in zufälliger Reihenfolge.
	 */
	private @NotNull int[] gibTermineInZufaelligerReihenfolge() {
		final int[] temp = new int[_terminAnzahl];

		for (int i = 0; i < _terminAnzahl; i++)
			temp[i] = i;

		for (int i1 = 0; i1 < _terminAnzahl; i1++) {
			final int i2 = _random.nextInt(_terminAnzahl);
			final int save1 = temp[i1];
			final int save2 = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}

		return temp;
	}

	/**
	 * Liefert die Klausur-Gruppen in zufälliger Reihenfolge.
	 *
	 * @return die Klausur-Gruppen in zufälliger Reihenfolge.
	 */
	private @NotNull List<@NotNull List<@NotNull Integer>> gibKlausurgruppenInZufaelligerReihenfolge() {
		final @NotNull List<@NotNull List<@NotNull Integer>> temp = new ArrayList<>();

		final @NotNull int[] perm = ArrayUtils.getIndexPermutation(_klausurGruppen.size(), _random);
		for (int i = 0; i < perm.length; i++)
			temp.add(_klausurGruppen.get(perm[i]));

		return temp;
	}

	/**
	 * Liefert ein leicht permutiertes Array aller Klausurgruppen sortiert nach höheren Knotengrad zuerst.
	 *
	 * @return ein leicht permutiertes Array aller Klausurgruppen sortiert nach höheren Knotengrad zuerst.
	 */
	@NotNull @NotNull List<@NotNull List<@NotNull Integer>> gibKlausurgruppenMitHoeheremGradZuerstEtwasPermutiert() {
		// Kopieren
		final @NotNull List<@NotNull List<@NotNull Integer>> temp = new ArrayList<>();
		temp.addAll(_klausurGruppenGrad);

		// Permutiere nahe beieinander liegende Indizes.
		final int size = temp.size();
		for (int i1 = 0; i1 < size; i1++) {
			final int i2 = _random.nextInt(size);
			if ((i1 - i2) * (i1 - i2) >= size) continue;
			// Tausche nur dann, wenn Abstand der Indizes kleiner als Wurzel(size) ist.
			final @NotNull List<@NotNull Integer> save1 = temp.get(i1);
			final @NotNull List<@NotNull Integer> save2 = temp.get(i2);
			temp.set(i1, save2);
			temp.set(i2, save1);
		}

		return temp;
	}


	/**
	 * Liefert die Klausurgruppe mit der geringsten Anzahl an Terminmöglichkeiten.
	 *
	 * @return die Klausurgruppe mit der geringsten Anzahl an Terminmöglichkeiten.
	 */
	@NotNull List<@NotNull Integer> gibKlausurgruppeMitMinimalenTerminmoeglichkeiten() {
		int min = _klausurenAnzahl;
		List<@NotNull Integer> gruppeMin = null;

		for (final @NotNull List<@NotNull Integer> gruppe : gibKlausurgruppenMitHoeheremGradZuerstEtwasPermutiert())
			if (gibIstKlausurgruppeUnverteilt(gruppe)) {
				final int terminmoeglichekeiten = gibTerminmoeglichkeiten(gruppe);
				if (terminmoeglichekeiten < min) {
					min = terminmoeglichekeiten;
					gruppeMin = gruppe;
				}
			}

		if (gruppeMin == null) throw new DeveloperNotificationException("Das darf nicht passieren!");
		return gruppeMin;
	}

	private int gibTerminmoeglichkeiten(final @NotNull List<@NotNull Integer> gruppe) {
		int summe = 0;
		for (int terminNr = 0; terminNr < _terminAnzahl; terminNr++)
			if (aktionSetzeKlausurgruppeInTermin(gruppe, terminNr)) {
				summe++;
				aktionEntferneKlausurgruppeAusTermin(gruppe, terminNr);
			}
		return summe;
	}

	private boolean gibVergleicheMitAktuellemZustand(final int terminAnzahlX, final @NotNull int @NotNull [] klausurZuTerminX) {
		// Kriterium 1: Die Anzahl an Terminen.
		if (_terminAnzahl < terminAnzahlX) return true;
		if (_terminAnzahl > terminAnzahlX) return false;

		// Kriterium 2: Besser, wenn mehr Paare bevorzugt

		// Kriterium 3: Besser ist, wenn ein Termin möglichst wenig Klausuren hat.
		final int[] histogramm = new int[_terminAnzahl];
		final int[] histogrammX = new int[_terminAnzahl];
		for (int i = 0; i < _klausurenAnzahl; i++) {
			histogramm[_klausurZuTermin[i]]++;
			histogrammX[klausurZuTerminX[i]]++;
		}

		int minHisto = _klausurenAnzahl;
		int minHistoX = _klausurenAnzahl;
		for (int i = 0; i < _terminAnzahl; i++) {
			minHisto = Math.min(minHisto, histogramm[i]);
			minHistoX = Math.min(minHistoX, histogrammX[i]);
		}

		return minHisto < minHistoX;
	}

	/**
	 * Liefert TRUE, falls alle Klausuren der Gruppe noch nicht verteilt wurden.
	 *
	 * @param pGruppe die Gruppe aller Klausuren.
	 * @return TRUE, falls alle Klausuren der Gruppe noch nicht verteilt wurden.
	 */
	private boolean gibIstKlausurgruppeUnverteilt(final @NotNull List<@NotNull Integer> pGruppe) {
		for (final int klausurNr : pGruppe)
			if (_klausurZuTermin[klausurNr] >= 0)
				return false;
		return true;
	}

	/**
	 * Liefert TRUE, falls alle Klausuren der Gruppe in den übergebenen Termin gesetzt werden konnten.
	 *
	 * @param  pGruppe die Gruppe aller Klausuren.
	 * @param  pTermin der Termin
	 * @return TRUE, falls alle Klausuren der Gruppe in den übergebenen Termin gesetzt werden konnten.
	 */
	boolean aktionSetzeKlausurgruppeInTermin(final @NotNull List<@NotNull Integer> pGruppe, final int pTermin) {
		if (pTermin <              0) throw new DeveloperNotificationException("aktionSetzeKlausurGruppeInTermin(" + pGruppe + ", " + pTermin + ") --> Termin zu klein!");
		if (pTermin >= _terminAnzahl) throw new DeveloperNotificationException("aktionSetzeKlausurGruppeInTermin(" + pGruppe + ", " + pTermin + ") --> Termin zu groß!");

		// Überprüfe, ob das setzen aller Klausuren möglich ist.
		for (int nr2 = 0; nr2 < _klausurenAnzahl; nr2++)
			if (_klausurZuTermin[nr2] == pTermin)
				for (final int nr : pGruppe)
					if (_verboten[nr][nr2])
						return false;

		// Setze alle Klausuren.
		for (final int nr : pGruppe)
			_klausurZuTermin[nr] = pTermin;

		return true;
	}

	/**
	 * Entfernt die Klausuren der Gruppe aus ihrem Termin.
	 * @param  pGruppe die Gruppe aller Klausuren.
	 * @param  pTermin der Termin
	 */
	void aktionEntferneKlausurgruppeAusTermin(final @NotNull List<@NotNull Integer> pGruppe, final int pTermin) {
		if (pTermin <              0) throw new DeveloperNotificationException("aktionEntferneKlausurgruppeAusTermin(" + pGruppe + ", " + pTermin + ") --> Termin zu klein!");
		if (pTermin >= _terminAnzahl) throw new DeveloperNotificationException("aktionEntferneKlausurgruppeAusTermin(" + pGruppe + ", " + pTermin + ") --> Termin zu groß!");
		for (final int nr : pGruppe) {
			if (_klausurZuTermin[nr] != pTermin) throw new DeveloperNotificationException("aktionEntferneKlausurgruppeAusTermin: Die Gruppe war gar nicht im Termin " + pTermin + "!");
			_klausurZuTermin[nr] = -1;
		}
	}

	/**
	 * Erhöht die Termin-Anzahl um 1, setzt alle Klausuren der übergebenen Gruppe in den neuen Termin.
	 *
	 * @param pGruppe die Gruppe aller Klausuren
	 */
	void aktionSetzeKlausurgruppeInNeuenTermin(final @NotNull List<@NotNull Integer> pGruppe) {
		for (final int klausurNr : pGruppe)
			if (_klausurZuTermin[klausurNr] >= 0) throw new DeveloperNotificationException("aktionSetzeKlausurGruppeInNeuenTermin(" + klausurNr + ") --> Die Klausur ist bereits einem Termin zugeordnet!");

		for (final int klausurNr : pGruppe)
			_klausurZuTermin[klausurNr] = _terminAnzahl;

		_terminAnzahl++;
	}

	/**
	 * Setzt alle Klausuren in einen zufälligen Termin. <br>
	 * Falls dies nicht möglich ist, wird die Gruppe in einen neuen Termin gesetzt.
	 *
	 * @param pGruppe die Gruppe aller Klausuren, die in einen zufälligen Termin gesetzt werden sollen.
	 */
	private void aktionSetzeKlausurgruppeInZufallsterminOderErzeugeNeuenTermin(final @NotNull List<@NotNull Integer> pGruppe) {
		for (final int terminNr : gibTermineInZufaelligerReihenfolge())
			if (aktionSetzeKlausurgruppeInTermin(pGruppe, terminNr))
				return; // Alle Klausuren wurden erfolgreich in den Termin gesetzt.

		aktionSetzeKlausurgruppeInNeuenTermin(pGruppe);
	}

	/**
	 * Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist.
	 *
	 * @return TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist.
	 */
	boolean gibIstBesserAlsZustand1() {
		return gibVergleicheMitAktuellemZustand(_terminAnzahl1, _klausurZuTermin1);
	}

	/**
	 * Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 2 ist.
	 *
	 * @return TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 2 ist.
	 */
	boolean gibIstBesserAlsZustand2() {
		return gibVergleicheMitAktuellemZustand(_terminAnzahl2, _klausurZuTermin2);
	}


	/**
	 * Erzeugt das Ergebnis mit der berechneten Zuordnung als Liste (Termine)
	 * von Listen (KlausurIDs).
	 *
	 * @return das Ergebnis
	 */
	@NotNull GostKlausurterminblockungErgebnis gibErzeugeOutput() {
		final @NotNull GostKlausurterminblockungErgebnis out = new GostKlausurterminblockungErgebnis();
		for (int i = 0; i < _terminAnzahl; i++)
			out.termine.add(new @NotNull GostKlausurterminblockungErgebnisTermin());

		for (final @NotNull Entry<@NotNull Long, @NotNull Integer> e : _mapKlausurZuNummer.entrySet()) {
			final @NotNull Long klausurID = e.getKey();
			final @NotNull Integer klausurNr = e.getValue();
			final int terminNr = _klausurZuTermin[klausurNr];
			DeveloperNotificationException.ifTrue("terminNr(" + terminNr + ") < 0", terminNr < 0);
			DeveloperNotificationException.ifTrue("terminNr(" + terminNr + ") >= _terminAnzahl", terminNr >= _terminAnzahl);
			// Termin-Klausur-Zuordnung
			out.termine.get(terminNr).kursklausuren.add(klausurID);
		}
		return out;
	}

	/**
	 * Entfernt alle Klausur-Termin-Zuordnungen und passt die Datenstrukturen entsprechend an.
	 */
	void aktionClear() {
		for (int i = 0; i < _klausurenAnzahl; i++)
			_klausurZuTermin[i] = -1;
		_terminAnzahl = 0;
	}

	/**
	 * Speichert die aktuelle Klausur-Termin-Lage in Zustand 1.
	 */
	void aktionZustand1Speichern() {
		_terminAnzahl1 = _terminAnzahl;
		System.arraycopy(_klausurZuTermin, 0, _klausurZuTermin1, 0, _klausurenAnzahl);
	}

	/**
	 * Lädt die aktuelle Klausur-Termin-Lage aus Zustand 1.
	 */
	void aktionZustand1Laden() {
		aktionClear();
		_terminAnzahl = _terminAnzahl1;
		System.arraycopy(_klausurZuTermin1, 0, _klausurZuTermin, 0, _klausurenAnzahl);
	}

	/**
	 * Speichert die aktuelle Klausur-Termin-Lage in Zustand 2.
	 */
	void aktionZustand2Speichern() {
		_terminAnzahl2 = _terminAnzahl;
		System.arraycopy(_klausurZuTermin, 0, _klausurZuTermin2, 0, _klausurenAnzahl);
		_logger.logLn("BESSER");
	}

	/**
	 * Lädt die aktuelle Klausur-Termin-Lage aus Zustand 2.
	 */
	void aktionZustand2Laden() {
		aktionClear();
		_terminAnzahl = _terminAnzahl2;
		System.arraycopy(_klausurZuTermin2, 0, _klausurZuTermin, 0, _klausurenAnzahl);
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Terminen. <br>
	 * Geht dann alle Klausuren in zufälliger Reihenfolge durch und setzt sie dann in einen zufälligen Termin. <br>
	 * Falls dies nicht klappt, wird ein neuer Termin erzeugt.
	 */
	void aktion_Clear_KlausurgruppenZufaellig_TermineZufaellig() {
		aktionClear();

		for (final @NotNull List<@NotNull Integer> gruppe : gibKlausurgruppenInZufaelligerReihenfolge())
			aktionSetzeKlausurgruppeInZufallsterminOderErzeugeNeuenTermin(gruppe);
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Terminen. <br>
	 * Füllt dann die Termine nacheinander auf und wählt die Klausurgruppen zufällig.
	 */
	void aktion_Clear_TermineNacheinander_GruppeZufaellig() {
		aktionClear();

		while (gibExistierenNichtverteilteKlausuren()) {
			final int terminNr = gibErzeugeNeuenTermin();

			for (final @NotNull List<@NotNull Integer> gruppe : gibKlausurgruppenInZufaelligerReihenfolge())
				if (gibIstKlausurgruppeUnverteilt(gruppe))
					aktionSetzeKlausurgruppeInTermin(gruppe, terminNr);
		}

	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Terminen. <br>
	 * Verteilt dann die Klausurgruppen mit höherem Knoten-Grad zuerst auf eine zufällige Schiene.
	 * Falls dies nicht klappt, wird eine neue Schiene erzeugt.
	 */
	void aktion_Clear_GruppeHoeherGradZuerst_TermineZufaellig() {
		aktionClear();

		for (final @NotNull List<@NotNull Integer> gruppe : gibKlausurgruppenMitHoeheremGradZuerstEtwasPermutiert())
			aktionSetzeKlausurgruppeInZufallsterminOderErzeugeNeuenTermin(gruppe);
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Terminen. <br>
	 * Füllt dann die Termine nacheinander auf und wählt die Klausurgruppen nach ihrem Grad.
	 */
	public void aktion_Clear_TermineNacheinander_GruppeNachGrad() {
		aktionClear();

		while (gibExistierenNichtverteilteKlausuren()) {
			final int terminNr = gibErzeugeNeuenTermin();

			for (final @NotNull List<@NotNull Integer> gruppe : gibKlausurgruppenMitHoeheremGradZuerstEtwasPermutiert())
				if (gibIstKlausurgruppeUnverteilt(gruppe))
					aktionSetzeKlausurgruppeInTermin(gruppe, terminNr);
		}
	}

	/**
	 * Ausgabe zum Debuggen der Tests.
	 *
	 * @param header Überschrift der Debug-Ausgabe.
	 */
	void debug(final @NotNull String header) {
		_logger.modifyIndent(+4);
		_logger.logLn(header);

		for (int s = 0; s < _terminAnzahl; s++) {
			_logger.log("    Schiene " + (s + 1) + ": ");
			for (int nr = 0; nr < _klausurenAnzahl; nr++)
				if (_klausurZuTermin[nr] == s) {
					final @NotNull GostKursklausurRich gostKlausur = DeveloperNotificationException.ifNull("Mapping _mapNummerZuKlausur.get(" + nr + ") ist NULL!", _mapNummerZuKlausur.get(nr));
					_logger.log(" " + gostKlausur.kursKurzbezeichnung + "/" + Arrays.toString(gostKlausur.kursSchiene));
				}
			_logger.logLn("");
		}

		for (int nr = 0; nr < _klausurenAnzahl; nr++)
			DeveloperNotificationException.ifTrue("Klausur " + (nr + 1) + " --> ohne Schiene!", _klausurZuTermin[nr] < 0);

		_logger.modifyIndent(-4);
	}


}
