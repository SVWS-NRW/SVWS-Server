package de.nrw.schule.svws.core.kursblockung;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungInputKurs;
import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungOutput;
import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungOutputFachwahlZuKurs;
import de.nrw.schule.svws.core.logger.LogLevel;
import de.nrw.schule.svws.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * In dieser Klasse werden die Eingabedaten {@link SchuelerblockungInput} auf ihre Konsistenz hin überprüft.
 * Danach wird die Datenstruktur für den schnellen Zugriff aufgebaut.
 * 
 * @author Benjamin A. Bartsch
 */
public class SchuelerblockungDynDaten {

	private static final int UNENDLICH = 1000000;

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger _logger;

	// Diese Attribute werden einmalig pro Blockung initialisiert.
	private final int nFachwahlen;
	private final int nSchienen;
	private final @NotNull Vector<@NotNull Vector<@NotNull SchuelerblockungInputKurs>> _fachwahlZuKurse;
	private final @NotNull boolean[] _fachwahlZuHatMultikurse;
	private final @NotNull long[] _fachwahlZuFachID;
	private final @NotNull int[] _fachwahlZuKursartID;

	// Diese Attribute werden pro Blockung reinitialisiert.
	private final @NotNull KursblockungMatrix _aktuellMatrix;
	private final @NotNull boolean[] _aktuellGesperrteSchiene;
	private final @NotNull long[] _aktuellFachwahlZuKurs; // -1 entspricht einer Nicht-Wahl
	private final @NotNull long[] _aktuellFachwahlZuKursBest; // -1 entspricht einer Nicht-Wahl
	private int _aktuellNichtwahlen;
	private int _aktuellNichtwahlenBest;
	private long _aktuellBewertung;
	private long _aktuellBewertungBest;

	/**
	 * Der Konstruktor der Klasse liest alle Daten von {@link SchuelerblockungInput} ein und baut die relevanten
	 * Datenstrukturen auf.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public SchuelerblockungDynDaten(@NotNull Random pRandom, @NotNull Logger pLogger, @NotNull SchuelerblockungInput pInput) {
		_logger = pLogger;
		_random = pRandom;
		aktionPruefeEingabedaten(pInput);

		// Datenstrukturen, die nur einmalig initialisiert werden müssen:
		nFachwahlen = pInput.fachwahlen.size();
		nSchienen = pInput.schienen;
		_fachwahlZuKurse = new Vector<>();
		_fachwahlZuHatMultikurse = new boolean[nFachwahlen];
		_fachwahlZuFachID = new long[nFachwahlen];
		_fachwahlZuKursartID = new int[nFachwahlen];
		aktionInitialisiereDatenstrukturen(pInput);

		// Datenstrukturen, die pro Blockung neu initialisiert werden müssen:
		_aktuellMatrix = new KursblockungMatrix(pRandom, nFachwahlen, nSchienen);
		_aktuellGesperrteSchiene = new boolean[nSchienen];
		_aktuellFachwahlZuKurs = new long[nFachwahlen];
		_aktuellFachwahlZuKursBest = new long[nFachwahlen];
		_aktuellBewertung = 0;
		_aktuellBewertungBest = 0;
	}

	/**
	 * Überprüft die Konsistenz und referentielle Integrität der Eingabedaten.
	 * 
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	void aktionPruefeEingabedaten(@NotNull SchuelerblockungInput pInput) {
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
		HashSet<Long> setKursID = new HashSet<>();
		for (@NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
			if (kurs.id < 0)
				throw fehler("kurs.id ist zu gering! --> " + kurs.id);
			if (setKursID.add(kurs.id) == false)
				throw fehler("kurs.id existiert doppelt! --> " + kurs.id);
			if (kurs.fach < 0)
				throw fehler("kurs.fach ist zu gering! --> " + kurs.fach);
			if (kurs.kursart < 0)
				throw fehler("kurs.kursart ist zu gering! --> " + kurs.kursart);
			if (kurs.anzahlSuS < 0)
				throw fehler("kurs.anzahlSuS ist zu gering! --> " + kurs.anzahlSuS);
			if (kurs.schienen.length < 1)
				throw fehler("kurs.schienen.length ist zu gering! --> " + kurs.schienen.length);
			if (kurs.schienen == null)
				throw fehler("kurs.schienen ist undefiniert! --> " + kurs.schienen);
			if (kurs.schienen.length <= 0)
				throw fehler("kurs.schienen.length ist zu klein! --> " + kurs.schienen.length);
			if (kurs.schienen.length > nSchienen)
				throw fehler("kurs.schienen.length ist zu groß! --> " + kurs.schienen.length);
			for (int schiene1 : kurs.schienen) {
				if (schiene1 < 1)
					throw fehler("Kurs " + kurs.id + " ist in zu kleiner Schiene! --> " + schiene1);
				if (schiene1 > nSchienen)
					throw fehler("Kurs " + kurs.id + " ist in zu großer Schiene! --> " + schiene1);
			}
			if (kurs.istFixiert && kurs.istGesperrt)
				throw fehler("kurs.istFixiert && kurs.istGesperrt ist unmöglich! --> " + kurs.id);
		}

		// Attribute der Fachwahlen überprüfen.
		for (@NotNull GostFachwahl fachwahl : pInput.fachwahlen) {
			if (fachwahl.fachID < 0)
				throw fehler("fachwahl.fachID ist zu gering! --> " + fachwahl.fachID);
			if (fachwahl.kursartID < 0)
				throw fehler("fachwahl.kursartID ist zu gering! --> " + fachwahl.kursartID);
		}

		// Pro Fachwahl auf Doppelfixierungen testen.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			@NotNull GostFachwahl fachwahl = pInput.fachwahlen.get(iFachwahl);
			String representation = fachwahl.fachID + ";" + fachwahl.kursartID;

			// Doppelfixierungen herausfinden.
			boolean kursWurdeFixiert = false;
			for (@NotNull SchuelerblockungInputKurs kurs : pInput.kurse)
				if ( (fachwahl.fachID == kurs.fach) && (fachwahl.kursartID == kurs.kursart) ) {
					if (kurs.istGesperrt)
						continue;
					if (kurs.istFixiert) {
						if (kursWurdeFixiert) 
							throw fehler("Die Fachart " + representation + " hat mehr als eine Fixierung!");
						kursWurdeFixiert = true;
					}
				}
		}

		// Kann jeder Kurs einer Fachwahl zugeordnet werden?
		for (@NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
			int gefunden = 0;

			for (int r = 0; r < nFachwahlen; r++) {
				@NotNull GostFachwahl fachwahl = pInput.fachwahlen.get(r);
				if ((fachwahl.fachID == kurs.fach) && (fachwahl.kursartID == kurs.kursart))
					gefunden++;
			}

			if (gefunden == 0)
				throw fehler("Der Kurs " + kurs.id + " konnte keiner Fachwahl zugeordnet werden!");
		}

	}

	/**
	 * Erzeugt einen Fehler und teilt dem Logger einen Fehler mit.
	 * 
	 * @param fehlermeldung Die Fehlermeldung.
	 */
	private SchuelerblockungException fehler(@NotNull String fehlermeldung) {
		_logger.logLn(LogLevel.ERROR, fehlermeldung);
		return new SchuelerblockungException(fehlermeldung);
	}

	/**
	 * Initialisiert {@link #_fachwahlen}, {@link #_fachwahlZuKurse} und {@link #_fachwahlZuHatMultikurse}.
	 * 
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	private void aktionInitialisiereDatenstrukturen(@NotNull SchuelerblockungInput pInput) {
		// Initialisiert '_fachwahlZuKurse' und '_fachwahlZuFachID' und '_fachwahlZuKursartID'.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			@NotNull GostFachwahl fachwahl = pInput.fachwahlen.get(iFachwahl);
			_fachwahlZuFachID[iFachwahl] = fachwahl.fachID;
			_fachwahlZuKursartID[iFachwahl] = fachwahl.kursartID;

			// Kurse dieser Fachwahl sammeln...
			Vector<@NotNull SchuelerblockungInputKurs> kurse = new Vector<>();
			boolean hatFixiertenKurs = false;
			for (@NotNull SchuelerblockungInputKurs kurs : pInput.kurse)
				if ((fachwahl.fachID == kurs.fach) && (fachwahl.kursartID == kurs.kursart) && (!kurs.istGesperrt) && (!hatFixiertenKurs)) {
					if (kurs.istFixiert) {
						hatFixiertenKurs = true;
						kurse.clear();
					}
					kurse.add(kurs);
				}
			_fachwahlZuKurse.add(kurse);

			// Initialisiert '_fachwahlZuHatMultikurse'.
			int max = 1;
			for (@NotNull SchuelerblockungInputKurs kurs : kurse)
				max = Math.max(max, kurs.schienen.length);
			_fachwahlZuHatMultikurse[iFachwahl] = max >= 2;
		}

	}

	/**
	 * Berechnet das optimale Matching. Zuerst werden die Multikurse verteilt, indem alle Kombination
	 * durchgegangen werden. Dann wird pro Verteilung der Multikurse die anderen Kurse mit einem bipartiten
	 * gewichteten Matching-Algorithmus verteilt. Das beste Ergebnis wird zurückgeliefert. Gibt es mehrere beste
	 * Ergebnisse wird ein zufälliges gewählt.
	 * 
	 * @return Eine optimale Zuordnung des Schülers auf seine gewählten Kurse.
	 */
	@NotNull SchuelerblockungOutput gibBestesMatching() {

		// Datenstrukturen resetten.
		_aktuellNichtwahlen = 0;
		_aktuellBewertung = 0;
		_aktuellNichtwahlenBest = UNENDLICH;
		_aktuellBewertungBest = UNENDLICH;
		Arrays.fill(_aktuellFachwahlZuKurs, -1L);
		Arrays.fill(_aktuellFachwahlZuKursBest, -1L);
		Arrays.fill(_aktuellGesperrteSchiene, false);

		// Multikurse verteilen.
		aktionVerteileMultikurseRekursiv(0);

		// Das beste Ergebnis zurückgeben.
		@NotNull SchuelerblockungOutput out = new SchuelerblockungOutput();
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			@NotNull SchuelerblockungOutputFachwahlZuKurs wahl = new SchuelerblockungOutputFachwahlZuKurs();
			wahl.fachID = _fachwahlZuFachID[iFachwahl];
			wahl.kursartID = _fachwahlZuKursartID[iFachwahl];
			wahl.kursID = _aktuellFachwahlZuKursBest[iFachwahl];
			out.fachwahlenZuKurs.add(wahl);
		}
		
		return out;
	}

	private void aktionVerteileMultikurseRekursiv(int iFachwahl) {
		if (iFachwahl >= nFachwahlen) {
			aktionVerteileMitMatching();
			return;
		}

		if (_fachwahlZuHatMultikurse[iFachwahl] == false) {
			aktionVerteileMultikurseRekursiv(iFachwahl + 1);
			return;
		}

		// Kurswahl ist möglich
		int schienenAnzahl = 2;
		for (@NotNull SchuelerblockungInputKurs kurs : _fachwahlZuKurse.get(iFachwahl)) {
			schienenAnzahl = Math.max(schienenAnzahl, kurs.schienen.length);
			if (aktionBelegeKurs(iFachwahl, kurs) == true) {
				aktionVerteileMultikurseRekursiv(iFachwahl + 1);
				if (aktionBelegeKursUndo(iFachwahl, kurs) == false)
					throw fehler("Der Kurs " + kurs.id + " konnte nicht entfernt werden!");
			}
		}

		// Nichtwahl
		_aktuellNichtwahlen += schienenAnzahl;
		if (_aktuellNichtwahlen <= _aktuellNichtwahlenBest) // Rekursion nur falls Verbesserung möglich.
			aktionVerteileMultikurseRekursiv(iFachwahl + 1);
		_aktuellNichtwahlen -= schienenAnzahl;
	}

	private void aktionVerteileMitMatching() {
		
		// Matrix Zellen auf UNENDLICH setzen.
		@NotNull long @NotNull [][] data = _aktuellMatrix.getMatrix();
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++)
			for (int iSchiene = 0; iSchiene < nSchienen; iSchiene++)
				data[iFachwahl][iSchiene] = UNENDLICH;

		// Zellen der Matrix bewerten.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++)
			if (_fachwahlZuHatMultikurse[iFachwahl] == false)
				for (int schiene = 0; schiene < nSchienen; schiene++)
					if (!_aktuellGesperrteSchiene[schiene]) {
						SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(_fachwahlZuKurse.get(iFachwahl), schiene);
						if (kurs != null)
							data[iFachwahl][schiene] = kurs.anzahlSuS * kurs.anzahlSuS;
					}
		
		// Matching berechnen lassen.
		@NotNull int[] r2c = _aktuellMatrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// Die Kurse hinzufügen.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++)
			if (_fachwahlZuHatMultikurse[iFachwahl] == false) {
				int schiene = r2c[iFachwahl];
				if ( (schiene < 0) || (data[iFachwahl][schiene] == UNENDLICH) ) {
					_aktuellNichtwahlen++;
					continue;
				}
				SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(_fachwahlZuKurse.get(iFachwahl), schiene);
				if (kurs == null)
					throw fehler("Der Fachart " + iFachwahl + " wurde ein NULL-Kurs zugeordnet!");
				if (aktionBelegeKurs(iFachwahl, kurs) == false)
					throw fehler("Der Kurs " + kurs.id + " konnte nicht belegt werden!");
			}

		// Besseren Zustand speichern?
		if ((_aktuellNichtwahlen < _aktuellNichtwahlenBest)
				|| ((_aktuellNichtwahlen == _aktuellNichtwahlenBest) && (_aktuellBewertung < _aktuellBewertungBest))) {
			_aktuellNichtwahlenBest = _aktuellNichtwahlen;
			_aktuellBewertungBest = _aktuellBewertung;
			for (int i = 0; i < nFachwahlen; i++)
				_aktuellFachwahlZuKursBest[i] = _aktuellFachwahlZuKurs[i];
		}

		// Die Kurse entfernen.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++)
			if (_fachwahlZuHatMultikurse[iFachwahl] == false) {
				int schiene = r2c[iFachwahl];
				if ((schiene < 0) || (data[iFachwahl][schiene] == UNENDLICH)) {
					_aktuellNichtwahlen--;
					continue;
				}
				SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(_fachwahlZuKurse.get(iFachwahl), schiene);
				if (kurs == null)
					throw fehler("Der Fachart " + iFachwahl + " wurde ein NULL-Kurs zugeordnet!");
				if (aktionBelegeKursUndo(iFachwahl, kurs) == false)
					throw fehler("Der Kurs " + kurs.id + " konnte nicht entfernt werden!");
			}
	}

	private static SchuelerblockungInputKurs gibKleinstenKursInSchiene(@NotNull Vector<@NotNull SchuelerblockungInputKurs> pKurse, int pSchiene) {
		long maxSuS = Integer.MAX_VALUE;
		SchuelerblockungInputKurs best = null;
		for (SchuelerblockungInputKurs kurs : pKurse)
			if (kurs.schienen[0] - 1 == pSchiene) // Es handelt sich nicht um einen Multikurs!
				if (kurs.anzahlSuS < maxSuS)
					best = kurs;
		return best;
	}

	private boolean aktionBelegeKurs(int iFachwahl, @NotNull SchuelerblockungInputKurs kurs) {
		// Ist eine Belegung möglich?
		for (int schiene1 : kurs.schienen)
			if (_aktuellGesperrteSchiene[schiene1 - 1]) // 1-Indizierung --> 0-Indizierung
				return false;
		// Fügen den Schienen Schiene(n) hinzu.
		_aktuellFachwahlZuKurs[iFachwahl] = kurs.id;
		for (int schiene1 : kurs.schienen)
			_aktuellGesperrteSchiene[schiene1 - 1] = true; // 1-Indizierung --> 0-Indizierung
		_aktuellBewertung += kurs.anzahlSuS * kurs.anzahlSuS;
		return true;
	}

	private boolean aktionBelegeKursUndo(int iFachwahl, @NotNull SchuelerblockungInputKurs kurs) {
		// Kann der Kurs überhaupt entfernt werden?
		if (_aktuellFachwahlZuKurs[iFachwahl] < 0)
			return false;
		for (int schiene1 : kurs.schienen)
			if (_aktuellGesperrteSchiene[schiene1 - 1] == false)
				return false;
		// Entfernen aus den Schiene(n).
		_aktuellFachwahlZuKurs[iFachwahl] = -1;
		for (int schiene1 : kurs.schienen)
			_aktuellGesperrteSchiene[schiene1 - 1] = false; // 1-Indizierung --> 0-Indizierung
		_aktuellBewertung -= kurs.anzahlSuS * kurs.anzahlSuS;
		return true;
	}

	@SuppressWarnings("unused")
	private void debug(@NotNull String pHeader, boolean pPrintMatrix) {
		System.out.println();
		System.out.println("#################### " + pHeader + " ####################");
		System.out.println("Bewertung      = " + _aktuellNichtwahlen + " / " + _aktuellBewertung);
		System.out.println("Fachwahlen     = " + Arrays.toString(_aktuellFachwahlZuKurs));
		System.out.println("BewertungBest  = " + _aktuellNichtwahlenBest + " / " + _aktuellBewertungBest);
		System.out.println("FachwahlenBest = " + Arrays.toString(_aktuellFachwahlZuKursBest));

		if (!pPrintMatrix)
			return;

		@NotNull long @NotNull [][] data = _aktuellMatrix.getMatrix();
		for (int schiene = 0; schiene < nSchienen; schiene++) {
			String sData = _aktuellGesperrteSchiene[schiene] ? "1" : "0";
			while (sData.length() < 5)
				sData = " " + sData;
			System.out.print(sData);
		}
		System.out.println();

		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			for (int schiene = 0; schiene < nSchienen; schiene++) {
				@NotNull String sData = "" + data[iFachwahl][schiene];
				if (data[iFachwahl][schiene] == UNENDLICH)
					sData = "INF";
				while (sData.length() < 5)
					sData = " " + sData;
				System.out.print(sData);
			}
			System.out.println();
		}

	}

}
