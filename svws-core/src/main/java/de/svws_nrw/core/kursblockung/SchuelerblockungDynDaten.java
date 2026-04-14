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

	// Diese Attribute werden einmalig pro Blockung initialisiert.
	private final int nFachwahlen;
	private final int nSchienen;
	private final @NotNull ArrayList<ArrayList<SchuelerblockungInputKurs>> fachwahlZuKurse;
	private final @NotNull boolean[] fachwahlZuHatMultikurse;
	private final @NotNull long[] fachwahlZuFachID;
	private final @NotNull int[] fachwahlZuKursartID;

	// Diese Attribute werden pro Blockung reinitialisiert.
	private final @NotNull KursblockungMatrix dynMatrix;
	private final @NotNull boolean[] dynGesperrteSchiene;
	private final @NotNull long[] dynFachwahlZuKurs; // -1 entspricht einer Nicht-Wahl
	private final @NotNull long[] dynFachwahlZuKursBest; // -1 entspricht einer Nicht-Wahl
	private int dynNichtwahlen;
	private int dynNichtwahlenBest;
	private long dynBewertung;
	private long dynBewertungBest;

	/**
	 * Der Konstruktor der Klasse liest alle Daten von {@link SchuelerblockungInput} ein und baut die relevanten
	 * Datenstrukturen auf.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public SchuelerblockungDynDaten(final @NotNull Random pRandom, final @NotNull SchuelerblockungInput pInput) {
		aktionPruefeEingabedaten(pInput);

		// Datenstrukturen, die nur einmalig initialisiert werden müssen:
		nFachwahlen = pInput.fachwahlen.size();
		nSchienen = pInput.schienen;
		fachwahlZuKurse = new ArrayList<>();
		fachwahlZuHatMultikurse = new boolean[nFachwahlen];
		fachwahlZuFachID = new long[nFachwahlen];
		fachwahlZuKursartID = new int[nFachwahlen];
		aktionInitialisiereDatenstrukturen(pInput);

		// Datenstrukturen, die pro Blockung neu initialisiert werden müssen:
		dynMatrix = new KursblockungMatrix(pRandom, nFachwahlen, nSchienen);
		dynGesperrteSchiene = new boolean[nSchienen];
		dynFachwahlZuKurs = new long[nFachwahlen];
		dynFachwahlZuKursBest = new long[nFachwahlen];
		dynBewertung = 0;
		dynBewertungBest = 0;
	}

	/**
	 * Überprüft die Konsistenz und referentielle Integrität der Eingabedaten.
	 *
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	void aktionPruefeEingabedaten(final @NotNull SchuelerblockungInput pInput) {
		// NULL-Referenzen überprüfen.
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
	 * Initialisiert {@link #fachwahlZuFachID}, {@link #fachwahlZuFachID} und
	 *  {@link #fachwahlZuKurse}, {@link #fachwahlZuHatMultikurse}.
	 *
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	private void aktionInitialisiereDatenstrukturen(final @NotNull SchuelerblockungInput pInput) {
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			final @NotNull GostFachwahl fachwahl = pInput.fachwahlen.get(iFachwahl);
			fachwahlZuFachID[iFachwahl] = fachwahl.fachID;
			fachwahlZuKursartID[iFachwahl] = fachwahl.kursartID;

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
			fachwahlZuKurse.add(kurse);

			// Hat die Fachwahl (mindestens) einen Multikurs?
			int max = 1;
			for (final @NotNull SchuelerblockungInputKurs kurs : kurse) {
				max = Math.max(max, kurs.schienen.length);
			}
			fachwahlZuHatMultikurse[iFachwahl] = max >= 2;
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
	@NotNull
	SchuelerblockungOutput gibBestesMatching() {
		// Datenstrukturen resetten.
		dynNichtwahlen = 0;
		dynBewertung = 0;
		dynNichtwahlenBest = UNENDLICH;
		dynBewertungBest = UNENDLICH;
		Arrays.fill(dynFachwahlZuKurs, -1L);
		Arrays.fill(dynFachwahlZuKursBest, -1L);
		Arrays.fill(dynGesperrteSchiene, false);

		// Multikurse verteilen. Ruft pro Rekursionsende "aktionVerteileMitMatching()" auf.
		aktionVerteileMultikurseRekursiv(0);

		// Das beste Ergebnis zurückgeben.
		final @NotNull SchuelerblockungOutput out = new SchuelerblockungOutput();
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			final @NotNull SchuelerblockungOutputFachwahlZuKurs wahl = new SchuelerblockungOutputFachwahlZuKurs();
			wahl.fachID = fachwahlZuFachID[iFachwahl];
			wahl.kursartID = fachwahlZuKursartID[iFachwahl];
			wahl.kursID = dynFachwahlZuKursBest[iFachwahl];
			out.fachwahlenZuKurs.add(wahl);
		}

		return out;
	}

	private void aktionVerteileMultikurseRekursiv(final int iFachwahl) {
		if (iFachwahl >= nFachwahlen) {
			aktionVerteileMitMatching();
			return;
		}

		if (!fachwahlZuHatMultikurse[iFachwahl]) {
			aktionVerteileMultikurseRekursiv(iFachwahl + 1);
			return;
		}

		// Kurswahl ist möglich
		int schienenAnzahl = 2;
		for (final @NotNull SchuelerblockungInputKurs kurs : fachwahlZuKurse.get(iFachwahl)) {
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
		dynNichtwahlen += schienenAnzahl;
		if (dynNichtwahlen <= dynNichtwahlenBest) { // Rekursion nur falls Verbesserung möglich.
			aktionVerteileMultikurseRekursiv(iFachwahl + 1);
		}
		dynNichtwahlen -= schienenAnzahl;
	}


	private static long gibKursBewertung(final @NotNull SchuelerblockungInputKurs kurs) {
		long bewertung = 0;
		bewertung += kurs.anzahlSuS * (long) kurs.anzahlSuS;
		bewertung += kurs.anzahlZusammenMitWuensche * MALUS_ZUSAMMEN_MIT_IM_KURS;
		bewertung += kurs.anzahlVerbotenMitWuensche * MALUS_VERBOTEN_MIT_IM_KURS;
		return bewertung;
	}

	private void aktionVerteileMitMatching() {
		final @NotNull long @NotNull [][] matrix = dynMatrix.getMatrix();
		aktionVerteileMitMatchingFuelleMatrix();

		// Matching berechnen lassen.
		final @NotNull int[] r2c = dynMatrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// Die Kurse hinzufügen.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			aktionVerteileMitMatchingKursHinzufuegen(iFachwahl, matrix, r2c);
		}

		// Besseren Zustand speichern?
		if ((dynNichtwahlen < dynNichtwahlenBest)
				|| ((dynNichtwahlen == dynNichtwahlenBest) && (dynBewertung < dynBewertungBest))) {
			dynNichtwahlenBest = dynNichtwahlen;
			dynBewertungBest = dynBewertung;
			System.arraycopy(dynFachwahlZuKurs, 0, dynFachwahlZuKursBest, 0, nFachwahlen);
		}

		// Die Kurse entfernen, da die Methode mehrfach aufgerufen wird und das beste Matching gespeichert wird.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			aktionVerteileMitMatchingKursEntfernen(iFachwahl, matrix, r2c);
		}
	}


	private void aktionVerteileMitMatchingFuelleMatrix() {
		// Matrix Zellen auf UNENDLICH setzen.
		final @NotNull long @NotNull [][] data = dynMatrix.getMatrix();
		dynMatrix.fuelleMitWert(UNENDLICH);

		// Zellen der Matrix bewerten.
		for (int iFachwahl = 0; iFachwahl < nFachwahlen; iFachwahl++) {
			if (!fachwahlZuHatMultikurse[iFachwahl]) { // Zeile gültig?
				for (int schiene = 0; schiene < nSchienen; schiene++) {
					if (!dynGesperrteSchiene[schiene]) { // Spalte gültig?
						final SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(fachwahlZuKurse.get(iFachwahl), schiene);
						if (kurs != null) {
							data[iFachwahl][schiene] = gibKursBewertung(kurs);
						}
					}
				}
			}
		}
	}

	private void aktionVerteileMitMatchingKursHinzufuegen(final int iFachwahl, final @NotNull long @NotNull [][] matrix, final @NotNull int[] r2c) {
		// Multikurse überspringen.
		if (fachwahlZuHatMultikurse[iFachwahl]) {
			return;
		}

		final int schiene = r2c[iFachwahl];
		if ((schiene < 0) || (matrix[iFachwahl][schiene] == UNENDLICH)) {
			dynNichtwahlen++;
			return;
		}

		final SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(fachwahlZuKurse.get(iFachwahl), schiene);
		if (kurs == null) {
			throw new DeveloperNotificationException("Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! "
					+ "Diesen Fehler kann nur das Programmier-Team beheben.");
		}

		if (!aktionBelegeKurs(iFachwahl, kurs)) {
			throw new DeveloperNotificationException("Der Kurs (" + kurs.id + ") konnte nicht belegt werden! "
					+ "Diesen Fehler kann nur das Programmier-Team beheben.");
		}

	}


	private void aktionVerteileMitMatchingKursEntfernen(final int iFachwahl, final @NotNull long @NotNull [][] matrix, final @NotNull int[] r2c) {
		// Multikurse überspringen.
		if (fachwahlZuHatMultikurse[iFachwahl]) {
			return;
		}

		final int schiene = r2c[iFachwahl];
		if ((schiene < 0) || (matrix[iFachwahl][schiene] == UNENDLICH)) {
			dynNichtwahlen--;
			return;
		}

		final SchuelerblockungInputKurs kurs = gibKleinstenKursInSchiene(fachwahlZuKurse.get(iFachwahl), schiene);
		if (kurs == null) {
			throw new DeveloperNotificationException("Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! "
					+ "Diesen Fehler kann nur das Programmier-Team beheben.");
		}

		if (!aktionBelegeKursUndo(iFachwahl, kurs)) {
			throw new DeveloperNotificationException("Der Kurs (" + kurs.id + ") konnte nicht entfernt werden! "
					+ "Diesen Fehler kann nur das Programmier-Team beheben.");
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
			if (dynGesperrteSchiene[schiene1 - 1]) { // 1-Indizierung --> 0-Indizierung
				return false;
			}
		}

		// Zu denen Schiene(n) hinzufügen.
		dynFachwahlZuKurs[iFachwahl] = kurs.id;
		for (final int schiene1 : kurs.schienen) {
			dynGesperrteSchiene[schiene1 - 1] = true; // 1-Indizierung --> 0-Indizierung
		}

		// Bewertung aktualisieren
		dynBewertung += gibKursBewertung(kurs);

		return true;
	}

	private boolean aktionBelegeKursUndo(final int iFachwahl, final @NotNull SchuelerblockungInputKurs kurs) {
		// Kann der Kurs überhaupt entfernt werden?
		if (dynFachwahlZuKurs[iFachwahl] < 0) {
			return false;
		}

		for (final int schiene1 : kurs.schienen) {
			if (!dynGesperrteSchiene[schiene1 - 1]) { // 1-Indizierung --> 0-Indizierung
				return false;
			}
		}

		// Entfernen aus den Schiene(n).
		dynFachwahlZuKurs[iFachwahl] = -1;
		for (final int schiene1 : kurs.schienen) {
			dynGesperrteSchiene[schiene1 - 1] = false; // 1-Indizierung --> 0-Indizierung
		}

		// Bewertung aktualisieren
		dynBewertung -= gibKursBewertung(kurs);

		return true;
	}

}
