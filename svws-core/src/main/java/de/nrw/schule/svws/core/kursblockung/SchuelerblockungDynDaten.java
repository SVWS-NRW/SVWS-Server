package de.nrw.schule.svws.core.kursblockung;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungInputFachwahl;
import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungInputKurs;
import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungOutput;
import de.nrw.schule.svws.logger.LogLevel;
import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** In dieser Klasse werden die Eingabedaten {@link SchuelerblockungInput} auf ihre Konsistenz hin überprüft. Danach
 * wird die Datenstruktur für den schnellen Zugriff aufgebaut.
 * 
 * @author Benjamin A. Bartsch */
public class SchuelerblockungDynDaten {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger _logger;

	private final @NotNull KursblockungMatrix _matrix;

	private final @NotNull Vector<@NotNull Long> _fachwahlen;

	private final @NotNull Vector<@NotNull Integer> _fachwahlZuZeile;

	private final @NotNull Vector<@NotNull Vector<@NotNull SchuelerblockungInputKurs>> _fachwahlZuKurse;

	private final @NotNull boolean[] _fachwahlZuHatMultikurse;
	private final @NotNull long[] _fachwahlZuKurs; // -1 entspricht einer Nicht-Wahl
	private final @NotNull boolean[] _gesperrteSchiene;

	private final int nFachwahlen;

	private final int nSchienen;

	/** Der Konstruktor der Klasse liest alle Daten von {@link SchuelerblockungInput} ein und baut die relevanten
	 * Datenstrukturen auf.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI). */
	public SchuelerblockungDynDaten(@NotNull Random pRandom, @NotNull Logger pLogger,
			@NotNull SchuelerblockungInput pInput) {
		_logger = pLogger;
		_random = pRandom;
		nFachwahlen = pInput.fachwahlen.size();
		nSchienen = pInput.schienen;
		pruefeDaten(pInput);
		_matrix = new KursblockungMatrix(pRandom, nFachwahlen, nSchienen);

		_fachwahlen = new Vector<>();
		_fachwahlZuZeile = new Vector<>();
		_fachwahlZuKurse = new Vector<>();
		_fachwahlZuHatMultikurse = new boolean[nFachwahlen];
		_fachwahlZuKurs = new long[nFachwahlen];
		_gesperrteSchiene = new boolean[nSchienen];

	}

	/** Erzeugt einen Fehler und teilt dem Logger einen Fehler mit.
	 * 
	 * @param fehlermeldung Die Fehlermeldung. */
	private SchuelerblockungException fehler(@NotNull String fehlermeldung) {
		_logger.logLn(LogLevel.ERROR, fehlermeldung);
		return new SchuelerblockungException(fehlermeldung);
	}

	private void pruefeDaten(@NotNull SchuelerblockungInput pInput) {

		// NULL-Referenzen überprüfen.

		if (pInput == null)
			throw fehler("pInput == NULL");

		if (pInput.fachwahlen == null)
			throw fehler("pInput.fachwahlen == NULL");

		if (pInput.kurse == null)
			throw fehler("pInput.kurse == NULL");

		// Anzahl an Elementen überprüfen.

		int nFachwahlen = pInput.fachwahlen.size();
		if (nFachwahlen < 1)
			throw fehler("Der Schüler hat zu wenig Fachwahlen! --> " + nFachwahlen);

		int nSchienen = pInput.schienen;
		if (nSchienen < 1)
			throw fehler("Die Schienenanzahl ist zu gering! --> " + nSchienen);

		int nKurse = pInput.kurse.size();
		if (nKurse < 1)
			throw fehler("Die Kursanzahl ist zu gering! --> " + nKurse);

		// Attribute der Kurse überprüfen.
		for (@NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
			if (kurs.id < 0)
				throw fehler("kurs.id ist zu gering! --> " + kurs.id);
			if (kurs.fach < 0)
				throw fehler("kurs.fach ist zu gering! --> " + kurs.fach);
			if (kurs.kursart < 0)
				throw fehler("kurs.kursart ist zu gering! --> " + kurs.kursart);
			if (kurs.anzahlSuS < 0)
				throw fehler("kurs.anzahlSuS ist zu gering! --> " + kurs.anzahlSuS);
			if (kurs.representation == null)
				throw fehler("kurs.representation ist undefiniert! --> " + kurs.representation);
			if (kurs.representation.equals(""))
				throw fehler("kurs.representation ist leer! --> " + kurs.representation);
			if (kurs.schienen.length < 1)
				throw fehler("kurs.schienen.length ist zu gering! --> " + kurs.schienen.length);
			if (kurs.schienen == null)
				throw fehler("kurs.schienen ist undefiniert! --> " + kurs.schienen);
			if (kurs.schienen.length > nSchienen)
				throw fehler("kurs.schienen.length ist zu groß! --> " + kurs.schienen.length);
		}

		// Attribute der Fachwahlen überprüfen.
		for (@NotNull SchuelerblockungInputFachwahl fachwahl : pInput.fachwahlen) {
			if (fachwahl.id < 0)
				throw fehler("fachwahl.id ist zu gering! --> " + fachwahl.id);
			if (fachwahl.fach < 0)
				throw fehler("fachwahl.fach ist zu gering! --> " + fachwahl.fach);
			if (fachwahl.kursart < 0)
				throw fehler("fachwahl.kursart ist zu gering! --> " + fachwahl.kursart);
			if (fachwahl.representation == null)
				throw fehler("fachwahl.representation ist undefiniert! --> " + fachwahl.representation);
			if (fachwahl.representation.equals(""))
				throw fehler("fachwahl.representation ist leer! --> " + fachwahl.representation);
		}

		// Kurse den Fachwahlen zuordnen

		Vector<@NotNull Vector<@NotNull SchuelerblockungInputKurs>> fachwahlZuKurse = new Vector<>();
		for (int r = 0; r < nFachwahlen; r++)
			fachwahlZuKurse.add(new Vector<>());

		for (@NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
			int gefunden = 0;

			for (int r = 0; r < nFachwahlen; r++) {
				@NotNull SchuelerblockungInputFachwahl fachwahl = pInput.fachwahlen.get(r);
				if ((fachwahl.fach == kurs.fach) && (fachwahl.kursart == kurs.kursart)) {
					fachwahlZuKurse.get(r).add(kurs);
					gefunden++;
				}
			}

			if (gefunden == 0) // TODO BAR Warnen bei gefunden > 1?
				throw fehler("Der Kurs " + kurs.representation + " konnte keiner Fachwahl zugeordnet werden!");
		}

		// Gesperrte Kurse (pro Fachwahl) --> Kurse entfernen

		// Fixierte Kurse (pro Fachwahl) --> Kurse entfernen

		for (SchuelerblockungInputKurs kurs : pInput.kurse) {
			// kurs.id
			// kurs.anzahlSuS
			// kurs.fach
			// kurs.kursart
			// kurs.istFixiert
			// kurs.istGesperrt
			// kurs.schienen
			// kurs.representation
		}

	}

	/** Entfernt den Schüler zunächst aus allen seinen Kursen. Verteilt dann die Multikurse zuerst, da ein Matching hier
	 * nicht funktioniert. Verteilt zuletzt die übrigen Kurse mit einem gewichteten bipartiten Matching. */
	void aktionFachwahlenEntfernenUndVerteilen() {

		// (1) Datenstrukturen aufbauen
		@NotNull long @NotNull [][] data = _matrix.getMatrix();
		Arrays.fill(_fachwahlZuKurs, -1L);
		Arrays.fill(_gesperrteSchiene, false);

		int UNENDLICH = Integer.MAX_VALUE;
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++)
			for (int iSchiene = 0; iSchiene < nSchienen; iSchiene++)
				data[iFachwahl][iSchiene] = UNENDLICH;

		// (2) Multikurse verteilen.
		for (int iFachwahl : KursblockungStatic.gibPermutation(_random, nFachwahlen))
			if (_fachwahlZuHatMultikurse[iFachwahl])
				for (int iKurs : KursblockungStatic.gibPermutation(_random, _fachwahlZuKurse.get(iFachwahl).size())) {
					@NotNull SchuelerblockungInputKurs kurs = _fachwahlZuKurse.get(iFachwahl).get(iKurs);
					// int[] x = kurs.schienen
					// wenn schiene(n) frei, dann belegen.
				}

		/*
		 * 
		 * // (3) Matrix füllen.
		 * 
		 * for (int iFachart = 0; iFachart < nFachwahlen; iFachart++) { for (int iSchiene = 0; iSchiene < nSchienen;
		 * iSchiene++) { data[iFachart][iSchiene] = UNENDLICH; } if (gesperrteFachart[iFachart]) continue;
		 * 
		 * // TODO [r][c] bewerten.... }
		 * 
		 * // (4) Matching berechnen lassen. _matrix.gibMaximalesBipartitesMatching(true);
		 * 
		 * // (5) Kurse zuteilen und eine Bewertung berechnen.
		 */
	}

	void aktionZustand1Speichern() {

	}

	int gibCompareZustand1() {
		return 0;
	}

	@NotNull
	SchuelerblockungOutput gibErzeugeZustand1() {
		return new SchuelerblockungOutput();
	}

}
