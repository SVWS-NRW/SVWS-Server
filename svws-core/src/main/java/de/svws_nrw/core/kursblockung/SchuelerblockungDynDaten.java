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
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * In dieser Klasse werden die Eingabedaten {@link SchuelerblockungInput} auf ihre Konsistenz hin überprüft.
 * Danach wird die Datenstruktur für den schnellen Zugriff aufgebaut,
 * mit dem Ziel einen Schüler auf seine Kurs neu zu verteilen.
 *
 * @author Benjamin A. Bartsch
 */
public class SchuelerblockungDynDaten {

	private static final int UNENDLICH = 1000000;
	private static final int MALUS_ZUSAMMEN_MIT_IM_KURS = -1000; // Bewertungen sind meistens im Bereich 2000-4000.
	private static final int MALUS_VERBOTEN_MIT_IM_KURS = 1000;

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	// Diese Attribute werden einmalig pro Blockung initialisiert.
	private final int nFachwahlen;
	private final int nSchienen;
	private final @NotNull ArrayList<ArrayList<SchuelerblockungInputKurs>> _fachwahlZuKurse;
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
		if (pInput == null) {
			throw new DeveloperNotificationException("pInput == NULL");
		}
		if (pInput.fachwahlen == null) {
			throw new DeveloperNotificationException("pInput.fachwahlen == NULL");
		}
		if (pInput.kurse == null) {
			throw new DeveloperNotificationException("pInput.kurse == NULL");
		}

		// Anzahl an Elementen überprüfen.
		aktionPruefeEingabedatenAnzahlen(pInput);

		// Attribute der Kurse überprüfen.
		aktionPruefeEingabedatenKurse(pInput);


		// Attribute der Fachwahlen überprüfen.
		aktionPruefeEingabedatenFachwahlenAttribute(pInput);

		// Pro Fachwahl auf Doppel-Kurs-Fixierungen testen.
		aktionPruefeEingabedatenFachwahlenDoppelfixierungen(pInput);

		// Kann jeder Kurs einer Fachwahl zugeordnet werden?
		aktionPruefeEingabedatenFachwahlenZuordnungen(pInput);
	}


	private static void aktionPruefeEingabedatenFachwahlenZuordnungen(@NotNull final SchuelerblockungInput pInput) {

		for (final @NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
			int gefunden = 0;
			for (int iFachwahl = 0; iFachwahl < pInput.fachwahlen.size(); iFachwahl++) {
				final @NotNull GostFachwahl fachwahl = pInput.fachwahlen.get(iFachwahl);
				if ((fachwahl.fachID == kurs.fach) && (fachwahl.kursartID == kurs.kursart)) {
					gefunden++;
				}
			}

			DeveloperNotificationException.ifTrue(
					"Der Kurs (%d) konnte keiner Fachart/Fachwahl zugeordnet werden!".formatted(kurs.id),
					gefunden == 0);
		}
	}

	private static void aktionPruefeEingabedatenFachwahlenDoppelfixierungen(final @NotNull SchuelerblockungInput pInput) {
		// Prüfe jede Fachwahl ...
		for (int iFachwahl = 0; iFachwahl < pInput.fachwahlen.size(); iFachwahl++) {

			DeveloperNotificationException.ifTrue(
					"pInput.fachwahlenText: Es fehlt der Text zur Fachwahl (%d)!".formatted(iFachwahl),
					iFachwahl >= pInput.fachwahlenText.size());

			final @NotNull String representation = pInput.fachwahlenText.get(iFachwahl);
			final @NotNull GostFachwahl fachwahl = pInput.fachwahlen.get(iFachwahl);

			// ... und suche den zur Fachwahl zugehörigen Kurs, welcher fixiert ist.
			boolean kursWurdeFixiert = false;
			for (final @NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
				if ((fachwahl.fachID == kurs.fach) && (fachwahl.kursartID == kurs.kursart) && (kurs.istFixiert)) {
					DeveloperNotificationException.ifTrue(
							"Die Fachart/Fachwahl (%s) hat mehr als eine Fixierung!".formatted(representation),
							kursWurdeFixiert);

					kursWurdeFixiert = true;
				}
			}
		}
	}


	private static void aktionPruefeEingabedatenFachwahlenAttribute(final @NotNull SchuelerblockungInput pInput) {
		for (final @NotNull GostFachwahl fachwahl : pInput.fachwahlen) {
			DeveloperNotificationException.ifInvalidID("fachwahl.schuelerID", fachwahl.schuelerID);

			DeveloperNotificationException.ifInvalidID("fachwahl.fachID", fachwahl.fachID);

			DeveloperNotificationException.ifInvalidID("fachwahl.kursartID", fachwahl.kursartID);
		}
	}


	private static void aktionPruefeEingabedatenKurse(final @NotNull SchuelerblockungInput pInput) {
		final HashSet<Long> setKursID = new HashSet<>();
		for (final @NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
			DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);

			DeveloperNotificationException.ifSetAddsDuplicate("setKursID", setKursID, kurs.id);

			DeveloperNotificationException.ifInvalidID("kurs.fach", kurs.fach);

			DeveloperNotificationException.ifTrue(
					"kurs.kursart (%d) ist zu gering!".formatted(kurs.kursart),
					kurs.kursart < 0);

			DeveloperNotificationException.ifTrue(
					"kurs.anzahlSuS (%d) ist zu gering!".formatted(kurs.anzahlSuS),
					kurs.anzahlSuS < 0);

			DeveloperNotificationException.ifTrue(
					"kurs.schienen == null, also nicht definiert!",
					kurs.schienen == null);

			DeveloperNotificationException.ifTrue(
					"kurs.schienen.length (%d) ist zu gering!".formatted(kurs.schienen.length),
					kurs.schienen.length <= 0);

			DeveloperNotificationException.ifTrue(
					"kurs.schienen.length (%d > %d) ist zu groß!".formatted(kurs.schienen.length, pInput.schienen),
					kurs.schienen.length > pInput.schienen);

			for (final int schiene1 : kurs.schienen) {
				DeveloperNotificationException.ifTrue(
						"Kurs %d ist in zu kleiner Schiene (%d)!".formatted(kurs.id, schiene1),
						schiene1 < 1);

				DeveloperNotificationException.ifTrue(
						"Kurs %d ist in zu großer Schiene (%d)!".formatted(kurs.id, schiene1),
						schiene1 > pInput.schienen);
			}

			DeveloperNotificationException.ifTrue(
					"Kurs %d ist fixiert und gesperrt, das sollte nicht möglich sein!".formatted(kurs.id),
					kurs.istFixiert && kurs.istGesperrt);
		}
	}


	private static void aktionPruefeEingabedatenAnzahlen(final @NotNull SchuelerblockungInput pInput) {
		DeveloperNotificationException.ifTrue(
				"Der Schüler hat keine Fachwahlen, ein Blocken sollte gar nicht angeboten werden!",
				pInput.fachwahlen.isEmpty());

		final int nSchienen = pInput.schienen;
		DeveloperNotificationException.ifTrue(
				"Die Schienenanzahl (%d) ist zu gering!".formatted(nSchienen),
				nSchienen < 1);

		final int nKurse = pInput.kurse.size();
		DeveloperNotificationException.ifTrue(
				"Die Kursanzahl (%d) ist zu gering!".formatted(nKurse),
				nKurse < 1);
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
			final ArrayList<SchuelerblockungInputKurs> kurse = new ArrayList<>();
			boolean hatFixiertenKurs = false;
			for (final @NotNull SchuelerblockungInputKurs kurs : pInput.kurse) {
				if ((fachwahl.fachID == kurs.fach) && (fachwahl.kursartID == kurs.kursart) && (!kurs.istGesperrt) && (!hatFixiertenKurs)) {
					if (kurs.istFixiert) {
						hatFixiertenKurs = true;
						kurse.clear();
					}
					kurse.add(kurs);
				}
			}
			_fachwahlZuKurse.add(kurse);

			// Initialisiert '_fachwahlZuHatMultikurse'.
			int max = 1;
			for (final @NotNull SchuelerblockungInputKurs kurs : kurse) {
				max = Math.max(max, kurs.schienen.length);
			}
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

		// Multikurse verteilen. Ruft pro Rekursionsende "aktionVerteileMitMatching()" auf.
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
				if (!aktionBelegeKursUndo(iFachwahl, kurs)) {
					throw new DeveloperNotificationException(
							"In der Methode 'SchuelerblockungDynDaten.aktionVerteileMultikurseRekursiv' ist ein unerwarteter Fehler passiert: "
									+ "Der Kurs (" + kurs.id + ") konnte vom Algorithmus nicht entfernt werden! "
									+ "Diesen Fehler kann nur das Programmier-Team beheben.");
				}
			}
		}

		// Nichtwahl
		_aktuellNichtwahlen += schienenAnzahl;
		if (_aktuellNichtwahlen <= _aktuellNichtwahlenBest) { // Rekursion nur falls Verbesserung möglich.
			aktionVerteileMultikurseRekursiv(iFachwahl + 1);
		}
		_aktuellNichtwahlen -= schienenAnzahl;
	}


	private static long gibKursBewertung(final @NotNull SchuelerblockungInputKurs kurs) {
		long bewertung = 0;
		bewertung += kurs.anzahlSuS * (long) kurs.anzahlSuS;
		bewertung += kurs.anzahlZusammenMitWuensche * MALUS_ZUSAMMEN_MIT_IM_KURS;
		bewertung += kurs.anzahlVerbotenMitWuensche * MALUS_VERBOTEN_MIT_IM_KURS;
		return bewertung;
	}

	private void aktionVerteileMitMatchingFuelleMatrix() {
		// Matrix Zellen auf UNENDLICH setzen.
		final @NotNull long @NotNull [][] data = _aktuellMatrix.getMatrix();
		_aktuellMatrix.fuelleMitWert(UNENDLICH);

		// Zellen der Matrix bewerten.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			if (!_fachwahlZuHatMultikurse[iFachwahl]) { // Zeile gültig?
				for (int schiene = 0; schiene < nSchienen; schiene++) {
					if (!_aktuellGesperrteSchiene[schiene]) { // Spalte gültig?
						final SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(_fachwahlZuKurse.get(iFachwahl), schiene);
						if (kurs != null) {
							data[iFachwahl][schiene] = gibKursBewertung(kurs);
						}
					}
				}
			}
		}
	}


	private void aktionVerteileMitMatching() {
		final @NotNull long @NotNull [][] data = _aktuellMatrix.getMatrix();
		aktionVerteileMitMatchingFuelleMatrix();

		// Matching berechnen lassen.
		final @NotNull int[] r2c = _aktuellMatrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// Die Kurse hinzufügen.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			// Multikurse überspringen.
			if (_fachwahlZuHatMultikurse[iFachwahl]) {
				continue;
			}

			final int schiene = r2c[iFachwahl];
			if ((schiene < 0) || (data[iFachwahl][schiene] == UNENDLICH)) {
				_aktuellNichtwahlen++;
				continue;
			}

			final SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(_fachwahlZuKurse.get(iFachwahl), schiene);
			if (kurs == null) {
				throw new DeveloperNotificationException(
						"In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: "
								+ "Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! "
								+ "Diesen Fehler kann nur das Programmier-Team beheben.");
			}

			if (!aktionBelegeKurs(iFachwahl, kurs)) {
				throw new DeveloperNotificationException(
						"In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: "
								+ "Der Kurs (" + kurs.id + ") konnte nicht belegt werden! "
								+ "Diesen Fehler kann nur das Programmier-Team beheben.");
			}
		}

		// Besseren Zustand speichern?
		if ((_aktuellNichtwahlen < _aktuellNichtwahlenBest)
				|| ((_aktuellNichtwahlen == _aktuellNichtwahlenBest) && (_aktuellBewertung < _aktuellBewertungBest))) {
			_aktuellNichtwahlenBest = _aktuellNichtwahlen;
			_aktuellBewertungBest = _aktuellBewertung;
			System.arraycopy(_aktuellFachwahlZuKurs, 0, _aktuellFachwahlZuKursBest, 0, nFachwahlen);
		}

		// Die Kurse entfernen.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			// Multikurse überspringen.
			if (_fachwahlZuHatMultikurse[iFachwahl]) {
				continue;
			}

			final int schiene = r2c[iFachwahl];
			if ((schiene < 0) || (data[iFachwahl][schiene] == UNENDLICH)) {
				_aktuellNichtwahlen--;
				continue;
			}
			final SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(_fachwahlZuKurse.get(iFachwahl), schiene);
			if (kurs == null) {
				throw new DeveloperNotificationException(
						"In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: "
								+ "Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! "
								+ "Diesen Fehler kann nur das Programmier-Team beheben.");
			}
			if (!aktionBelegeKursUndo(iFachwahl, kurs)) {
				throw new DeveloperNotificationException(
						"In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: "
								+ "Der Kurs (" + kurs.id + ") konnte nicht entfernt werden! "
								+ "Diesen Fehler kann nur das Programmier-Team beheben.");
			}
		}
	}


	private static SchuelerblockungInputKurs gibKleinstenKursInSchiene(
			final @NotNull ArrayList<SchuelerblockungInputKurs> pKurse,
			final int pSchiene) {

		long maxSuS = Integer.MAX_VALUE;
		SchuelerblockungInputKurs best = null;

		// Es ist garantiert, dass es kein Multikurs ist (ergo: kurs.schienen.length == 1).
		for (final SchuelerblockungInputKurs kurs : pKurse) {
			if (((kurs.schienen[0] - 1) == pSchiene) && (kurs.anzahlSuS < maxSuS)) {
				best = kurs;
				maxSuS = kurs.anzahlSuS;
			}
		}

		return best;
	}

	private boolean aktionBelegeKurs(final int iFachwahl, final @NotNull SchuelerblockungInputKurs kurs) {
		// Ist eine Belegung möglich?
		for (final int schiene1 : kurs.schienen) {
			if (_aktuellGesperrteSchiene[schiene1 - 1]) { // 1-Indizierung --> 0-Indizierung
				return false;
			}
		}

		// Zu denen Schiene(n) hinzufügen.
		_aktuellFachwahlZuKurs[iFachwahl] = kurs.id;
		for (final int schiene1 : kurs.schienen) {
			_aktuellGesperrteSchiene[schiene1 - 1] = true; // 1-Indizierung --> 0-Indizierung
		}

		// Bewertung aktualisieren
		_aktuellBewertung += gibKursBewertung(kurs);

		return true;
	}

	private boolean aktionBelegeKursUndo(final int iFachwahl, final @NotNull SchuelerblockungInputKurs kurs) {
		// Kann der Kurs überhaupt entfernt werden?
		if (_aktuellFachwahlZuKurs[iFachwahl] < 0) {
			return false;
		}

		for (final int schiene1 : kurs.schienen) {
			if (!_aktuellGesperrteSchiene[schiene1 - 1]) { // 1-Indizierung --> 0-Indizierung
				return false;
			}
		}

		// Entfernen aus den Schiene(n).
		_aktuellFachwahlZuKurs[iFachwahl] = -1;
		for (final int schiene1 : kurs.schienen) {
			_aktuellGesperrteSchiene[schiene1 - 1] = false; // 1-Indizierung --> 0-Indizierung
		}

		// Bewertung aktualisieren
		_aktuellBewertung -= gibKursBewertung(kurs);

		return true;
	}

	@SuppressWarnings("unused")
	private void debug(final @NotNull Logger logger, final @NotNull String pHeader, final boolean pPrintMatrix) {
		logger.logLn("");
		logger.logLn("#################### " + pHeader + " ####################");
		logger.logLn("Bewertung      = " + _aktuellNichtwahlen + " / " + _aktuellBewertung);
		logger.logLn("Fachwahlen     = " + Arrays.toString(_aktuellFachwahlZuKurs));
		logger.logLn("BewertungBest  = " + _aktuellNichtwahlenBest + " / " + _aktuellBewertungBest);
		logger.logLn("FachwahlenBest = " + Arrays.toString(_aktuellFachwahlZuKursBest));

		if (!pPrintMatrix) {
			return;
		}

		final @NotNull long @NotNull [][] data = _aktuellMatrix.getMatrix();
		for (int schiene = 0; schiene < nSchienen; schiene++) {
			final String sData = _aktuellGesperrteSchiene[schiene] ? "1" : "0";
			logger.log(String.format("%5s", sData));
		}
		logger.logLn("");

		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			for (int schiene = 0; schiene < nSchienen; schiene++) {
				@NotNull String sData = "" + data[iFachwahl][schiene];
				if (data[iFachwahl][schiene] == UNENDLICH) {
					sData = "INF";
				}
				logger.log(String.format("%5s", sData));
			}
			logger.logLn("");
		}

	}

}
