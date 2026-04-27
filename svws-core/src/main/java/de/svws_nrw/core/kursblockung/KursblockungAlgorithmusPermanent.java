package de.svws_nrw.core.kursblockung;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

	private static final long MILLIS_START = 4000; // Lineare Inkrementierung ist nicht gut.
	private static final int TOP_ERGEBNISSE = 3;

	private final @NotNull Random rnd = new Random();
	private final @NotNull Logger log = new Logger();

	/** Die TOP-Ergebnisse werden als {@link KursblockungDynDaten}-Objekt gespeichert, da diese sortierbar sind. */
	private final @NotNull ArrayList<KursblockungDynDaten> topErgebnisse;

	/** Jeder Algorithmus hat sein eigenes {@link KursblockungDynDaten}-Objekt. Das ist wichtig. */
	private @NotNull KursblockungAlgorithmusPermanentK @NotNull [] algorithmenK;

	/** Die Eingabe-Daten von der GUI. */
	private final @NotNull GostBlockungsdatenManager input;

	/** Die Zeitspanne nachdem alle Algorithmen neu erzeugt werden. */
	private long zeitMax;

	/** Die Zeitspanne reduziert sich schrittweise, da die GUI nur kurze Rechenintervalle dem Algorithmus gibt.*/
	private long zeitRest;

	/** Der Index des aktuellen Algorithmus der als nächstes ausgeführt wird.*/
	private int currentIndex;

	/**
	 * Initialisiert den Blockungsalgorithmus für eine vom Clienten initiierte dauerhafte Berechnung.
	 *
	 * @param pInput  Das Eingabe-Objekt (der Daten-Manager).
	 */
	public KursblockungAlgorithmusPermanent(final @NotNull GostBlockungsdatenManager pInput) {
		// Random-Objekt erzeugen (Größter Integer Wert in TypeScript --> 9007199254740991L).
		final long seed = rnd.nextLong();
		log.logLn("KursblockungAlgorithmusPermanent: Seed = " + seed);

		input = pInput;
		zeitMax = MILLIS_START;
		zeitRest = MILLIS_START;
		currentIndex = 0;
		topErgebnisse = new ArrayList<>();

		algorithmenK = erzeugeAlgorithmenNeu();
	}


	private @NotNull KursblockungAlgorithmusPermanentK @NotNull [] erzeugeAlgorithmenNeu() {
		return new KursblockungAlgorithmusPermanentK @NotNull [] {
				// Alle Algorithmen zur Verteilung von Kursen auf ihre Schienen ...
				new KursblockungAlgorithmusPermanentKMatching(rnd, log, input),
				new KursblockungAlgorithmusPermanentKSchuelervorschlag(rnd, log, input),
				new KursblockungAlgorithmusPermanentKSchuelervorschlagSingle(rnd, log, input),
				new KursblockungAlgorithmusPermanentKOptimiereBest(rnd, log, input, gibTopElementOrNull()),
				new KursblockungAlgorithmusPermanentKOptimiereBest(rnd, log, input, gibTopElementOrNull()),
				// ... Ende der K-Algorithmen.
		};
	}


	/**
	 * Liefert TRUE, falls die GUI die TOP-Liste aktualisieren soll.
	 *
	 * @param zeitProAufruf  Die zur Verfügung stehende Zeit (in Millisekunden), um die ehemaligen Ergebnisse zu optimieren.
	 * @return TRUE, falls die GUI die TOP-Liste aktualisieren soll.
	 */
	public boolean next(final long zeitProAufruf) {
		// Rechnen innerhalb des Zeitlimits (ca.100 ms).
		final long zeitStart = System.currentTimeMillis();
		final long zeitEnde = zeitStart + zeitProAufruf;
		algorithmenK[currentIndex].next(zeitEnde);
		currentIndex = (currentIndex + 1) % algorithmenK.length;
		zeitRest -= (System.currentTimeMillis() - zeitStart);

		// Neustart und GUI soll die Liste aktualisieren.
		if (zeitRest < 100) {
			neustart();
			return true;
		}

		// GUI soll nicht aktualisieren.
		return false;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 *
	 * @return TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 */
	private int neustart() {
		int verbesserungen = 0;
		for (int iK = 0; iK < algorithmenK.length; iK++) {
			// Lädt beim Algorithmus den besten Zustand. Einige Algorithmen verteilen hier erst die SuS.
			algorithmenK[iK].ladeBestMitSchuelerverteilung();

			// Sortiert einfügen.
			boolean eingefuegt = false;
			for (int i = 0; (i < topErgebnisse.size()) && (!eingefuegt); i++) {
				if (algorithmenK[iK].dynDaten.gibIstBesserAls1NW2KD3FW(topErgebnisse.get(i))) {
					topErgebnisse.add(i, algorithmenK[iK].dynDaten);
					eingefuegt = true;
				}
			}

			// Sonderfälle
			if (eingefuegt) {
				verbesserungen++;
				if (topErgebnisse.size() > TOP_ERGEBNISSE) {
					// Es wurde eingefügt, aber die Liste ist nun zu groß.
					topErgebnisse.removeLast();
				}
			} else if (topErgebnisse.size() < TOP_ERGEBNISSE) {
				// Es wurde nicht eingefügt, aber die Liste ist zu klein.
				topErgebnisse.addLast(algorithmenK[iK].dynDaten);
				verbesserungen++;
			}

		}

		algorithmenK = erzeugeAlgorithmenNeu();

		// Die Berechnungszeit steigt exponentiell. Mehrere Tests ergaben, dass dies besser ist als linear.
		zeitMax = (int) (zeitMax * 1.5);
		zeitRest = zeitMax;

		return verbesserungen;
	}

	/**
	 * Liefert ein zufälliges Element aus der TOP-Liste (oder NULL);
	 *
	 * @return ein zufälliges Element aus der TOP-Liste (oder NULL);
	 */
	private KursblockungDynDaten gibTopElementOrNull() {
		if (topErgebnisse.isEmpty()) {
			return null;
		}
		final int index = rnd.nextInt(topErgebnisse.size());
		return topErgebnisse.get(index);
	}

	/**
	 * Liefert die Liste der aktuellen Top-Blockungsergebnisse.
	 * <br> Die ID der Blockungsergebnisse entspricht dem Index in der TOP-Liste.
	 *
	 * @return die Liste der aktuellen Top-Blockungsergebnisse.
	 */
	public @NotNull List<GostBlockungsergebnisManager> getBlockungsergebnisse() {
		// Konvertiere "KursblockungDynDaten" zu "GostBlockungsergebnisManager".
		final @NotNull List<GostBlockungsergebnisManager> list = new ArrayList<>();

		for (int i = 0; i < topErgebnisse.size(); i++) {
			list.add(topErgebnisse.get(i).gibErzeugtesKursblockungOutput(input, i));
		}

		return list;
	}

}
