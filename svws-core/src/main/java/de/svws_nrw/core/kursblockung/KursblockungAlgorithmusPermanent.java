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
public final class KursblockungAlgorithmusPermanent {
	private static final long MILLIS_START = 1000;
	// private static final long MILLIS_INCREMENT = 1000 --> lineares Wachstum doch nicht so gut.
	private static final int TOP_ERGEBNISSE = 10;

	private final @NotNull Random _random = new Random();
	private final @NotNull Logger _logger = new Logger();

	/** Die TOP-Ergebnisse werden als {@link KursblockungDynDaten}-Objekt gespeichert, da diese sortierbar sind. */
	private final @NotNull ArrayList<@NotNull KursblockungDynDaten> _top;

	/** Jeder Algorithmus hat sein eigenes {@link KursblockungDynDaten}-Objekt. Das ist wichtig. */
	private final @NotNull KursblockungAlgorithmusPermanentK @NotNull [] algorithmenK;

	/** Die Eingabe-Daten von der GUI. */
	private final @NotNull GostBlockungsdatenManager _input;

	/** Die Zeitspanne nachdem alle Algorithmen neu erzeugt werden. */
	private long _zeitMax;

	/** Die Zeitspanne reduziert sich schrittweise, da die GUI nur kurze Rechenintervalle dem Algorithmus gibt.*/
	private long _zeitRest;

	/**
	 * Initialisiert den Blockungsalgorithmus für eine vom Clienten initiierte dauerhafte Berechnung.
	 *
	 * @param pInput  Das Eingabe-Objekt (der Daten-Manager).
	 */
	public KursblockungAlgorithmusPermanent(final @NotNull GostBlockungsdatenManager pInput) {
		// Random-Objekt erzeugen (Größter Integer Wert in TypeScript --> 9007199254740991L).
		final long seed = _random.nextLong();
		_logger.logLn("KursblockungAlgorithmusPermanent: Seed = " + seed);

		_input = pInput;
		_zeitMax = MILLIS_START;
		_zeitRest = MILLIS_START;
		_top = new ArrayList<>();

		algorithmenK = new KursblockungAlgorithmusPermanentK @NotNull [] {
				// Alle Algorithmen zur Verteilung von Kursen auf ihre Schienen ...
				new KursblockungAlgorithmusPermanentKSchnellW(_random, _logger, _input),
				new KursblockungAlgorithmusPermanentKFachwahlmatrix(_random, _logger, _input),
				new KursblockungAlgorithmusPermanentKMatching(_random, _logger, _input),
				new KursblockungAlgorithmusPermanentKSchuelervorschlag(_random, _logger, _input),
				new KursblockungAlgorithmusPermanentKOptimiereBest(_random, _logger, _input, null),
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
		final long zeitStart = System.currentTimeMillis();
		final long zeitFuerBerechnung = Math.min(zeitProAufruf, _zeitRest);
		final long zeitEnde = zeitStart + zeitFuerBerechnung;

		// Jeder Algorithmus optimiert innerhalb seiner Zeit sein eigenes "KursblockungDynDaten"-Objekt.
		for (int iK = 0; (iK < algorithmenK.length) && (System.currentTimeMillis() < zeitEnde); iK++)
			algorithmenK[iK].next(zeitStart + (zeitFuerBerechnung * (iK + 1)) / algorithmenK.length); // Übermittle die individuelle Endzeit.

		// Die verwendete Zeit abziehen.
		_zeitRest -= (System.currentTimeMillis() - zeitStart);

		// Neustart aller Algorithmen, falls nur noch weniger als 100 Millisekunden zur Verfügung stehen.
		return (_zeitRest <= 100) ? _neustart() : false;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 *
	 * @return TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 */
	private boolean _neustart() {

		// Verteile aber vorher die SuS.
		int verbesserung = 0;
		for (int iK = 0; iK < algorithmenK.length; iK++) {
			_verteileSuS(algorithmenK[iK]);
			if (_fuegeHinzuFallsBesser(iK))
				verbesserung++;
		}

		// Alle K-Algorithmen neu erzeugen, da jeweils ein neues "KursblockungDynDaten"-Objekt erzeugt werden muss.
		algorithmenK[0] = new KursblockungAlgorithmusPermanentKSchnellW(_random, _logger, _input);
		algorithmenK[1] = new KursblockungAlgorithmusPermanentKFachwahlmatrix(_random, _logger, _input);
		algorithmenK[2] = new KursblockungAlgorithmusPermanentKMatching(_random, _logger, _input);
		algorithmenK[3] = new KursblockungAlgorithmusPermanentKSchuelervorschlag(_random, _logger, _input);
		algorithmenK[4] = new KursblockungAlgorithmusPermanentKOptimiereBest(_random, _logger, _input, _gibBestOrNull());

		// Die Berechnungszeit steigt exponentiell. Mehrere Tests ergaben, dass dies besser ist als linear.
		_zeitMax = _zeitMax + _zeitMax / 2;
		_zeitRest = _zeitMax;

		return verbesserung > 0;
	}

	private KursblockungDynDaten _gibBestOrNull() {
		return _top.isEmpty() ? null : _top.get(0);
	}

	private boolean _fuegeHinzuFallsBesser(final int algNr) {
		final @NotNull KursblockungDynDaten neueDynDaten = algorithmenK[algNr].gibDynDaten();

		// Gibt es ein besseres Objekt als in der Liste?
		for (int i = 0; i < _top.size(); i++)
			if (neueDynDaten.gibIstBesser_NW_KD_FW_Als(_top.get(i))) {
				// Besseres Element (sortiert) einfügen.
				_top.add(i, neueDynDaten);
				// Letztes Element entfernen?
				if (_top.size() > TOP_ERGEBNISSE)
					_top.removeLast();
				return true;
			}

		// Ist die Liste noch nicht voll?
		if (_top.size() < TOP_ERGEBNISSE) {
			_top.add(neueDynDaten);
			return true;
		}

		return false;
	}

	private void _verteileSuS(final @NotNull KursblockungAlgorithmusPermanentK k) {
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

	/**
	 * Liefert die Liste der aktuellen Top-Blockungsergebnisse.
	 * <br> Die ID der Blockungsergebnisse entspricht dem Index in der TOP-Liste.
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
