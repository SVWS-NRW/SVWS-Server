package de.svws_nrw.core.kursblockung;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient zur Berechnung von Blockungsergebnissen.
 * <br>Die Methode {@link #next(long)} dient dazu, den Rechenprozess beliebig fortzuführen.
 * <br>Die Methode {@link #getBlockungsergebnisse()} liefert eine Liste der bisher besten Blockungsergebnisse.
 *
 * @author Benjamin A. Bartsch
 */
public final class KursblockungAlgorithmusPermanent  {
	private static final int TOP_ERGEBNISSE = 10;

	private final @NotNull Random _random = new Random();
	private final @NotNull Logger _logger;
	private final @NotNull ArrayList<@NotNull KursblockungDynDaten> _top;
	private final @NotNull GostBlockungsdatenManager _input;
	private final @NotNull KursblockungAlgorithmusPermanentK @NotNull [] algorithmenK;
	private long _zeitBisNeustart;
	private long _zeitRest;

	/**
	 * Initialisiert den Blockungsalgorithmus für eine vom Clienten initiierte dauerhafte Berechnung.
	 *
	 * @param pInput  Das Eingabe-Objekt (der Daten-Manager).
	 */
	public KursblockungAlgorithmusPermanent(final @NotNull GostBlockungsdatenManager pInput) {
		// Random-Objekt erzeugen (Größter Integer Wert in TypeScript --> 9007199254740991L).
		final long seed =  _random.nextLong();
		System.out.println("KursblockungAlgorithmusPermanent: seed = " + seed);

		_input = pInput;
		_zeitBisNeustart = 1000;
		_zeitRest = _zeitBisNeustart;
		_top = new ArrayList<>();
		_logger = new Logger();

		algorithmenK = new KursblockungAlgorithmusPermanentK @NotNull [] {
			// Alle Algorithmen zur Verteilung von Kursen auf ihre Schienen ...
			new KursblockungAlgorithmusPermanentKSchnellW(_random, _logger, _input),
			new KursblockungAlgorithmusPermanentKFachwahlmatrix(_random, _logger, _input),
			new KursblockungAlgorithmusPermanentKMatching(_random, _logger, _input),
			new KursblockungAlgorithmusPermanentKSchuelervorschlag(_random, _logger, _input),
			// ... Ende der K-Algorithmen.
		};
	}

	/**
	 * Liefert TRUE, falls der Blockungsalgorithmus innerhalb der erlaubten Zeit seine Ergebnisse verbessern konnte.
	 *
	 * @param zeitProAufruf  Die zur Verfügung stehende Zeit (in Millisekunden), um die ehemaligen Ergebnisse zu optimieren.
	 *
	 * @return TRUE, falls der Blockungsalgorithmus innerhalb der erlaubten Zeit seine Ergebnisse verbessern konnte.
	 */
	public boolean next(final long zeitProAufruf) {
		final long zeitFuerBerechnung = Math.min(zeitProAufruf, _zeitRest);

		final long zeitStart = System.currentTimeMillis();
		final long zeitEnde = zeitStart + zeitFuerBerechnung;

		// Jeder Algorithmus optimiert innerhalb seiner Zeit sein eigenes "KursblockungDynDaten"-Objekt.
		for (int iK = 0; (iK < algorithmenK.length) && (System.currentTimeMillis() < zeitEnde); iK++)
			algorithmenK[iK].next(zeitStart + (zeitFuerBerechnung * (iK + 1)) / algorithmenK.length); // Übermittle die individuelle Endzeit.

		// Die verwendete Zeit abziehen.
		_zeitRest -= (System.currentTimeMillis() - zeitStart);

		// Neustart aller Algorithmen, falls nur noch weniger als 100 Millisekunden zur Verfügung stehen.
		return (_zeitRest <= 100) ? _neustart() : false;
	}

	private boolean _neustart() {
		// Überprüfe, ob einer der Algorithmen ein besseres Ergebnis gefunden hat. Verteile aber vorher die SuS.
		boolean verbesserung = false;
		for (int iK = 0; iK < algorithmenK.length; iK++) {
			verteileSuS(algorithmenK[iK]);
			verbesserung |= _fuegeHinzuFallsBesser(algorithmenK[iK].gibDynDaten(), iK);
		}
		System.out.println("    Ende der Berechnungszeit. Verbesserung: " + verbesserung);

		// Alle K-Algorithmen neu erzeugen, da jeweils ein neues "KursblockungDynDaten"-Objekt erzeugt werden muss.
		algorithmenK[0] = new KursblockungAlgorithmusPermanentKSchnellW(_random, _logger, _input);
		algorithmenK[1] = new KursblockungAlgorithmusPermanentKFachwahlmatrix(_random, _logger, _input);
		algorithmenK[2] = new KursblockungAlgorithmusPermanentKMatching(_random, _logger, _input);
		algorithmenK[3] = new KursblockungAlgorithmusPermanentKSchuelervorschlag(_random, _logger, _input);

		// Die Berechnungszeit steigt.
		_zeitBisNeustart += 1000;
		_zeitRest = _zeitBisNeustart;
		System.out.println("    Berechnungszeit erhöht sich auf: " + _zeitBisNeustart + " Millisekunden.");

		for (int i = 0; i < _top.size(); i++) {
			System.out.println("TOP " + (i + 1) + ": " + _top.get(i).gibStatistik().debugRow());
		}

		return verbesserung;
	}

	private void verteileSuS(final @NotNull KursblockungAlgorithmusPermanentK k) {
		final @NotNull KursblockungDynDaten dynDaten = k.gibDynDaten();

		final @NotNull KursblockungAlgorithmusS @NotNull [] algorithmenS = new KursblockungAlgorithmusS @NotNull [] {
			// Alle Algorithmen zur Verteilung von SuS auf ihre Kurse ...
			new KursblockungAlgorithmusSSchnellW(_random, _logger, dynDaten),
			new KursblockungAlgorithmusSZufaellig(_random, _logger, dynDaten),
			new KursblockungAlgorithmusSMatching(_random, _logger, dynDaten),
			new KursblockungAlgorithmusSMatchingW(_random, _logger, dynDaten),
			// ... Ende der S-Algorithmen.
		};

		// Verteilung der SuS (nur die beste Verteilung bleibt im Zustand K).
		dynDaten.aktionZustandSpeichernK();

		for (int iS = 0; iS < algorithmenS.length; iS++) {
			// Verteilung der SuS
			algorithmenS[iS].berechne();

			// Bessere SuS-Verteilung gefunden?
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0)
				dynDaten.aktionZustandSpeichernK();
		}

		// Bestes Ergebnis laden (Zustand K).
		dynDaten.aktionZustandLadenK();

		// Gibt es einen neuen besten globalen Zustand?
		if (dynDaten.gibCompareZustandG_NW_KD_FW() > 0)
			dynDaten.aktionZustandSpeichernG();
	}

	private boolean _fuegeHinzuFallsBesser(final @NotNull KursblockungDynDaten neueDynDaten, final int algNr) {
		// Gibt es ein besseres Objekt als in der Liste?
		for (int i = 0; i < _top.size(); i++)
			if (neueDynDaten.gibIstBesser_NW_KD_FW_Als(_top.get(i))) {
				// Besseres Element (sortiert) einfügen.
				_top.add(i, neueDynDaten);
				System.out.println("    Index " + i + " verbessert! Algo = " + algNr);
				// Letztes Element entfernen?
				if (_top.size() > TOP_ERGEBNISSE)
					_top.removeLast();
				System.out.println("    Liste hat Länge " + _top.size() + ".");
				return true;
			}

		// Ist die Liste noch nicht voll?
		if (_top.size() < TOP_ERGEBNISSE) {
			System.out.println("    Index " + _top.size() + " verbessert (hinten)! Algo = " + algNr);
			_top.add(neueDynDaten);
			return true;
		}

		return false;
	}

	/**
	 * Liefert die Liste der aktuellen Top-Blockungsergebnisse.
	 *
	 * @return die Liste der aktuellen Top-Blockungsergebnisse.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisManager> getBlockungsergebnisse() {
		// Konvertiere "KursblockungDynDaten" zu "GostBlockungsergebnisManager".
		final @NotNull List<@NotNull GostBlockungsergebnisManager> list = new ArrayList<>();

		for (int i = 0; i < _top.size(); i++)
			list.add(_top.get(i).gibErzeugtesKursblockungOutput(_input, i));

		return list;
	}

}
