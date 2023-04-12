package de.svws_nrw.core.kursblockung;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.ArrayList;

import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungInput;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungInputKurs;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungOutput;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungOutputFachwahlZuKurs;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
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

	// Diese Attribute werden einmalig pro Blockung initialisiert.
	private final int nFachwahlen;
	private final int nSchienen;
	private final @NotNull ArrayList<@NotNull ArrayList<@NotNull SchuelerblockungInputKurs>> _fachwahlZuKurse;
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
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public SchuelerblockungDynDaten(final @NotNull Random pRandom, final @NotNull SchuelerblockungInput pInput) {
		_random = pRandom;
		aktionPruefeEingabedaten(pInput);

		// Datenstrukturen, die nur einmalig initialisiert werden müssen:
		nFachwahlen = pInput.fachwahlen.size();
		nSchienen = pInput.schienen;
		_fachwahlZuKurse = new ArrayList<>();
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
	void aktionPruefeEingabedaten(final @NotNull SchuelerblockungInput pInput) {
		// NULL-Referenzen überprüfen.

		if (pInput == null)
			throw new DeveloperNotificationException("pInput == NULL");

		if (pInput.fachwahlen == null)
			throw new DeveloperNotificationException("pInput.fachwahlen == NULL");

		if (pInput.kurse == null)
			throw new DeveloperNotificationException("pInput.kurse == NULL");

		// Anzahl an Elementen überprüfen.

		final int tmpNFachwahlen = pInput.fachwahlen.size();
		if (tmpNFachwahlen < 1)
			throw new DeveloperNotificationException("Der Schüler hat zu wenig Fachwahlen (" + tmpNFachwahlen + "), ein Blocken sollte gar nicht angeboten werden!");

		final int tmpNSchienen = pInput.schienen;
		if (tmpNSchienen < 1)
			throw new DeveloperNotificationException("Die Schienenanzahl (" + tmpNSchienen + ") ist zu gering!");

		final int nKurse = pInput.kurse.size();
		if (nKurse < 1)
			throw new DeveloperNotificationException("Die Kursanzahl (" + nKurse + ") ist zu gering!");

		// Attribute der Kurse überprüfen.
		final HashSet<Long> setKursID = new HashSet<>();
		for (final @NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
			if (kurs.id < 0)
				throw new DeveloperNotificationException("kurs.id (" + kurs.id + ") ist zu gering!");
			if (!setKursID.add(kurs.id))
				throw new DeveloperNotificationException("kurs.id (" + kurs.id + ") existiert doppelt!");
			if (kurs.fach < 0)
				throw new DeveloperNotificationException("kurs.fach (" + kurs.fach + ") ist zu gering!");
			if (kurs.kursart < 0)
				throw new DeveloperNotificationException("kurs.kursart (" + kurs.kursart + ") ist zu gering!");
			if (kurs.anzahlSuS < 0)
				throw new DeveloperNotificationException("kurs.anzahlSuS (" + kurs.anzahlSuS + ") ist zu gering!");
			if (kurs.schienen == null)
				throw new DeveloperNotificationException("kurs.schienen == (" + kurs.schienen + ") ist nicht definiert!");
			if (kurs.schienen.length <= 0)
				throw new DeveloperNotificationException("kurs.schienen.length (" + kurs.schienen.length + ") ist zu gering!");
			if (kurs.schienen.length > tmpNSchienen)
				throw new DeveloperNotificationException("kurs.schienen.length (" + kurs.schienen.length + ") ist zu groß!");
			for (final int schiene1 : kurs.schienen) {
				if (schiene1 < 1)
					throw new DeveloperNotificationException("Kurs " + kurs.id + " ist in zu kleiner Schiene (" + schiene1 + ")!");
				if (schiene1 > tmpNSchienen)
					throw new DeveloperNotificationException("Kurs " + kurs.id + " ist in zu großer Schiene (" + schiene1 + ")!");
			}
			if (kurs.istFixiert && kurs.istGesperrt)
				throw new DeveloperNotificationException("Kurs " + kurs.id + " ist fixiert und gesperrt, das sollte nicht möglich sein!");
		}

		// Attribute der Fachwahlen überprüfen.
		for (final @NotNull GostFachwahl fachwahl : pInput.fachwahlen) {
			if (fachwahl.schuelerID < 0)
				throw new DeveloperNotificationException("fachwahl.schuelerID (" + fachwahl.schuelerID + ") ist zu gering!");
			if (fachwahl.fachID < 0)
				throw new DeveloperNotificationException("fachwahl.fachID (" + fachwahl.fachID + ") ist zu gering!");
			if (fachwahl.kursartID < 0)
				throw new DeveloperNotificationException("fachwahl.kursartID (" + fachwahl.kursartID + ") ist zu gering!");
		}

		// Pro Fachwahl auf Doppelfixierungen testen.
		for (int iFachwahl = 0; iFachwahl < tmpNFachwahlen; iFachwahl++) {
			final @NotNull GostFachwahl fachwahl = pInput.fachwahlen.get(iFachwahl);
			if (iFachwahl >= pInput.fachwahlenText.size())
				throw new DeveloperNotificationException("pInput.fachwahlenText: Es fehlt der Text zur Fachwahl (" + iFachwahl + ")!");
			final String representation = pInput.fachwahlenText.get(iFachwahl);

			// Doppelfixierungen herausfinden.
			boolean kursWurdeFixiert = false;
			for (final @NotNull SchuelerblockungInputKurs kurs : pInput.kurse)
				if ((fachwahl.fachID == kurs.fach) && (fachwahl.kursartID == kurs.kursart)) {
					if (kurs.istGesperrt)
						continue;
					if (kurs.istFixiert) {
						if (kursWurdeFixiert)
							throw new UserNotificationException("Die Fachart/Fachwahl (" + representation + ") hat mehr als eine Fixierung!");
						kursWurdeFixiert = true;
					}
				}
		}

		// Kann jeder Kurs einer Fachwahl zugeordnet werden?
		for (final @NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
			int gefunden = 0;

			for (int r = 0; r < tmpNFachwahlen; r++) {
				final @NotNull GostFachwahl fachwahl = pInput.fachwahlen.get(r);
				if ((fachwahl.fachID == kurs.fach) && (fachwahl.kursartID == kurs.kursart))
					gefunden++;
			}

			if (gefunden == 0)
				throw new DeveloperNotificationException("Der Kurs (" + kurs.id + ") konnte keiner Fachart/Fachwahl zugeordnet werden!");
		}

	}

	/**
	 * Initialisiert {@link #_fachwahlen}, {@link #_fachwahlZuKurse} und {@link #_fachwahlZuHatMultikurse}.
	 *
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	private void aktionInitialisiereDatenstrukturen(final @NotNull SchuelerblockungInput pInput) {
		// Initialisiert '_fachwahlZuKurse' und '_fachwahlZuFachID' und '_fachwahlZuKursartID'.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			final @NotNull GostFachwahl fachwahl = pInput.fachwahlen.get(iFachwahl);
			_fachwahlZuFachID[iFachwahl] = fachwahl.fachID;
			_fachwahlZuKursartID[iFachwahl] = fachwahl.kursartID;

			// Kurse dieser Fachwahl sammeln...
			final ArrayList<@NotNull SchuelerblockungInputKurs> kurse = new ArrayList<>();
			boolean hatFixiertenKurs = false;
			for (final @NotNull SchuelerblockungInputKurs kurs : pInput.kurse)
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
			for (final @NotNull SchuelerblockungInputKurs kurs : kurse)
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
		final @NotNull SchuelerblockungOutput out = new SchuelerblockungOutput();
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			final @NotNull SchuelerblockungOutputFachwahlZuKurs wahl = new SchuelerblockungOutputFachwahlZuKurs();
			wahl.fachID = _fachwahlZuFachID[iFachwahl];
			wahl.kursartID = _fachwahlZuKursartID[iFachwahl];
			wahl.kursID = _aktuellFachwahlZuKursBest[iFachwahl];
			out.fachwahlenZuKurs.add(wahl);
		}

		return out;
	}

	private void aktionVerteileMultikurseRekursiv(final int iFachwahl) {
		if (iFachwahl >= nFachwahlen) {
			aktionVerteileMitMatching();
			return;
		}

		if (!_fachwahlZuHatMultikurse[iFachwahl]) {
			aktionVerteileMultikurseRekursiv(iFachwahl + 1);
			return;
		}

		// Kurswahl ist möglich
		int schienenAnzahl = 2;
		for (final @NotNull SchuelerblockungInputKurs kurs : _fachwahlZuKurse.get(iFachwahl)) {
			schienenAnzahl = Math.max(schienenAnzahl, kurs.schienen.length);
			if (aktionBelegeKurs(iFachwahl, kurs)) {
				aktionVerteileMultikurseRekursiv(iFachwahl + 1);
				if (!aktionBelegeKursUndo(iFachwahl, kurs))
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMultikurseRekursiv' ist ein unerwarteter Fehler passiert: "
							+ "Der Kurs (" + kurs.id + ") konnte vom Algorithmus nicht entfernt werden! "
							+ "Diesen Fehler kann nur das Programmier-Team beheben.");
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
		final @NotNull long @NotNull [][] data = _aktuellMatrix.getMatrix();
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++)
			for (int iSchiene = 0; iSchiene < nSchienen; iSchiene++)
				data[iFachwahl][iSchiene] = UNENDLICH;

		// Zellen der Matrix bewerten.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++)
			if (!_fachwahlZuHatMultikurse[iFachwahl])
				for (int schiene = 0; schiene < nSchienen; schiene++)
					if (!_aktuellGesperrteSchiene[schiene]) {
						final SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(_fachwahlZuKurse.get(iFachwahl), schiene);
						if (kurs != null)
							data[iFachwahl][schiene] = kurs.anzahlSuS * (long) kurs.anzahlSuS;
					}

		// Matching berechnen lassen.
		final @NotNull int[] r2c = _aktuellMatrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// Die Kurse hinzufügen.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++)
			if (!_fachwahlZuHatMultikurse[iFachwahl]) {
				final int schiene = r2c[iFachwahl];
				if ((schiene < 0) || (data[iFachwahl][schiene] == UNENDLICH)) {
					_aktuellNichtwahlen++;
					continue;
				}
				final SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(_fachwahlZuKurse.get(iFachwahl), schiene);
				if (kurs == null)
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: "
							+ "Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! "
							+ "Diesen Fehler kann nur das Programmier-Team beheben.");
				if (!aktionBelegeKurs(iFachwahl, kurs))
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: "
							+ "Der Kurs (" + kurs.id + ") konnte nicht belegt werden! "
							+ "Diesen Fehler kann nur das Programmier-Team beheben.");
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
			if (!_fachwahlZuHatMultikurse[iFachwahl]) {
				final int schiene = r2c[iFachwahl];
				if ((schiene < 0) || (data[iFachwahl][schiene] == UNENDLICH)) {
					_aktuellNichtwahlen--;
					continue;
				}
				final SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(_fachwahlZuKurse.get(iFachwahl), schiene);
				if (kurs == null)
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: "
							+ "Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! "
							+ "Diesen Fehler kann nur das Programmier-Team beheben.");
				if (!aktionBelegeKursUndo(iFachwahl, kurs))
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: "
							+ "Der Kurs (" + kurs.id + ") konnte nicht entfernt werden! "
							+ "Diesen Fehler kann nur das Programmier-Team beheben.");
			}
	}

	private static SchuelerblockungInputKurs gibKleinstenKursInSchiene(final @NotNull ArrayList<@NotNull SchuelerblockungInputKurs> pKurse, final int pSchiene) {
		final long maxSuS = Integer.MAX_VALUE;
		SchuelerblockungInputKurs best = null;
		for (final SchuelerblockungInputKurs kurs : pKurse)
			if ((kurs.schienen[0] - 1 == pSchiene) // Es handelt sich nicht um einen Multikurs!
				&& (kurs.anzahlSuS < maxSuS))
					best = kurs;
		return best;
	}

	private boolean aktionBelegeKurs(final int iFachwahl, final @NotNull SchuelerblockungInputKurs kurs) {
		// Ist eine Belegung möglich?
		for (final int schiene1 : kurs.schienen)
			if (_aktuellGesperrteSchiene[schiene1 - 1]) // 1-Indizierung --> 0-Indizierung
				return false;
		// Fügen den Schienen Schiene(n) hinzu.
		_aktuellFachwahlZuKurs[iFachwahl] = kurs.id;
		for (final int schiene1 : kurs.schienen)
			_aktuellGesperrteSchiene[schiene1 - 1] = true; // 1-Indizierung --> 0-Indizierung
		_aktuellBewertung += kurs.anzahlSuS * kurs.anzahlSuS;
		return true;
	}

	private boolean aktionBelegeKursUndo(final int iFachwahl, final @NotNull SchuelerblockungInputKurs kurs) {
		// Kann der Kurs überhaupt entfernt werden?
		if (_aktuellFachwahlZuKurs[iFachwahl] < 0)
			return false;
		for (final int schiene1 : kurs.schienen)
			if (!_aktuellGesperrteSchiene[schiene1 - 1])
				return false;
		// Entfernen aus den Schiene(n).
		_aktuellFachwahlZuKurs[iFachwahl] = -1;
		for (final int schiene1 : kurs.schienen)
			_aktuellGesperrteSchiene[schiene1 - 1] = false; // 1-Indizierung --> 0-Indizierung
		_aktuellBewertung -= kurs.anzahlSuS * kurs.anzahlSuS;
		return true;
	}

	@SuppressWarnings("unused")
	private void debug(final @NotNull String pHeader, final boolean pPrintMatrix) {
		System.out.println();
		System.out.println("#################### " + pHeader + " ####################");
		System.out.println("Bewertung      = " + _aktuellNichtwahlen + " / " + _aktuellBewertung);
		System.out.println("Fachwahlen     = " + Arrays.toString(_aktuellFachwahlZuKurs));
		System.out.println("BewertungBest  = " + _aktuellNichtwahlenBest + " / " + _aktuellBewertungBest);
		System.out.println("FachwahlenBest = " + Arrays.toString(_aktuellFachwahlZuKursBest));

		if (!pPrintMatrix)
			return;

		final @NotNull long @NotNull [][] data = _aktuellMatrix.getMatrix();
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
