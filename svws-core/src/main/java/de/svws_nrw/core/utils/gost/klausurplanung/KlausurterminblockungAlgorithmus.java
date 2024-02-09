package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungKonfiguration;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusKursarten;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusQuartale;
import jakarta.validation.constraints.NotNull;

/**
 * Algorithmus zur Blockung von Klausuren auf eine minimale Anzahl von Terminen.
 * Die Termine sind noch keinem Datum zugeordnet.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurterminblockungAlgorithmus {

	private static final @NotNull Random _random = new Random();

	private static final @NotNull Comparator<@NotNull GostKursklausurRich> _compGostKursklausurRich = (final @NotNull GostKursklausurRich a, final @NotNull GostKursklausurRich b) -> {
		if (a.halbjahr < b.halbjahr) return -1;
		if (a.halbjahr > b.halbjahr) return +1;

		if (a.quartal < b.quartal) return -1;
		if (a.quartal > b.quartal) return +1;

		return 0;
	};


	/** Ein Logger für Debug-Zwecke. */
	private final @NotNull Logger _logger;

	/**
	 * Der Konstruktor.
	 */
	public KlausurterminblockungAlgorithmus() {
		_logger = new Logger();
	}

	/**
	 * Der Konstruktor.
	 *
	 * @param pLogger  Ein Logger für Debug-Zwecke.
	 */
	public KlausurterminblockungAlgorithmus(final @NotNull Logger pLogger) {
		_logger = pLogger;
	}

	/**
	 * Berechnet eine Liste von Terminen, denen die IDs der übergebenen Kurs-Klausuren zugeordnet sind.
	 *
	 * @param daten   die Kurs-Klausuren mit Schülern und die Konfiguration als Eingabe für den Blockungs-Algorithmus
	 *
	 * @return die Liste der Termine
	 */
	public @NotNull GostKlausurterminblockungErgebnis apply(final @NotNull GostKlausurterminblockungDaten daten) {
		final @NotNull GostKlausurterminblockungErgebnis out = new GostKlausurterminblockungErgebnis();
		berechneRekursivQuartalsModus(daten.richKlausuren, daten.konfiguration, out);
		return out;
	}

	private void berechneRekursivQuartalsModus(final @NotNull List<@NotNull GostKursklausurRich> input, final @NotNull GostKlausurterminblockungKonfiguration config, final @NotNull GostKlausurterminblockungErgebnis out) {

		// Keine Daten vorhanden.
		if (input.isEmpty())
			return;

		// Keine Filterung angefordert.
		if (config.modusQuartale == KlausurterminblockungModusQuartale.ZUSAMMEN.id) {
			berechneRekursivLkGkModus(input, config, out);
			return;
		}

		// Sortieren nach (Halbjahr, Quartal).
		input.sort(_compGostKursklausurRich);

		// Blocken nach (Halbjahr, Quartal).
		final @NotNull List<@NotNull GostKursklausurRich> temp = new ArrayList<>();
		for (final @NotNull GostKursklausurRich klausur : input) {
			// Neue Gruppe (Sonderfall) erstellen.
			if (temp.isEmpty()) {
				temp.add(klausur);
				continue;
			}

			// Gleiche Gruppe.
			if (_compGostKursklausurRich.compare(klausur, temp.get(0)) == 0) {
				temp.add(klausur);
				continue;
			}

			// Gruppe blocken
			berechneRekursivLkGkModus(temp, config, out);

			// Neue Gruppe erstellen.
			temp.clear();
			temp.add(klausur);
		}

		if (!temp.isEmpty()) {
			berechneRekursivLkGkModus(temp, config, out);
			temp.clear();
		}
	}



	private void berechneRekursivLkGkModus(final @NotNull List<@NotNull GostKursklausurRich> input, final @NotNull GostKlausurterminblockungKonfiguration config, final @NotNull GostKlausurterminblockungErgebnis out) {
		// Der LK-GK-Modus bestimmt, welche Klausuren aus der Liste potentiell geblockt werden sollen.
		final @NotNull KlausurterminblockungModusKursarten modus = KlausurterminblockungModusKursarten.getOrException(config.modusKursarten);
		switch (modus) {
			case BEIDE:
				berechne_helper(input, config, out); // keine Filterung
				break;
			case NUR_LK:
				berechne_helper(filter(input, true), config, out);  // nur LK
				break;
			case NUR_GK:
				berechne_helper(filter(input, false), config, out);  // nur GK (bzw. nicht LK)
				break;
			case GETRENNT:
				berechne_helper(filter(input, true), config, out);  // nur LK
				berechne_helper(filter(input, false), config, out);  // nur GK (bzw. nicht LK)
				break;
			default:
				throw new DeveloperNotificationException("Der LK-GK-Modus ist unbekannt!");
		}
	}

	/**
	 * Liefert die Liste pInput nach LK-Klausuren (oder dem Gegenteil) gefiltert heraus.
	 *
	 * @param input   Die Liste, die gefiltert werden soll.
	 * @param istLK   Falls TRUE, werden die LK-Klausuren herausgefiltert, andernfalls das Gegenteil.
	 *
	 * @return die Liste pInput nach LK-Klausuren (oder dem Gegenteil) gefiltert heraus.
	 */
	private static @NotNull List<@NotNull GostKursklausurRich> filter(final @NotNull List<@NotNull GostKursklausurRich> input, final boolean istLK) {
		final @NotNull List<@NotNull GostKursklausurRich> temp = new ArrayList<>();

		for (final GostKursklausurRich gostKursklausur : input)
			if (gostKursklausur.kursart.equals("LK") == istLK)
				temp.add(gostKursklausur);

		return temp;
	}

	/**
	 * Berechnet eine Klausurblockung unter Verwendung verschiedener internen Algorithmen.
	 *
	 * @param input   die Menge der Klausuren (Eingabe).
	 * @param config  die Konfiguration der Blockung.
	 * @param out     die Termin-Klausur-Zuordnung (Ausgabe).
	 */
	private void berechne_helper(final @NotNull List<@NotNull GostKursklausurRich> input, final @NotNull GostKlausurterminblockungKonfiguration config, final @NotNull GostKlausurterminblockungErgebnis out) {
		// Logger +4
		_logger.log("KlausurterminblockungAlgorithmus");
		_logger.modifyIndent(+4);

		// End-Zeitpunkt berechnet.
		final long zeitEndeGesamt = System.currentTimeMillis() + config.maxTimeMillis;

		// Random-Objekt erzeugen
		final long seed = _random.nextLong(); // Trick, um den Seed zu kennen.
		final @NotNull Random random = new Random(seed);

		// Erstellung eines Objektes, welches sich um alle dynamischen Daten während des Blockungsvorgangs kümmert.
		final KlausurterminblockungDynDaten dynDaten = new KlausurterminblockungDynDaten(_logger, random, input, config);

		// Algorithmen erzeugen
		final @NotNull KlausurterminblockungAlgorithmusAbstract @NotNull [] algorithmen = new KlausurterminblockungAlgorithmusAbstract @NotNull [] {
				// Alle Algorithmen zur Verteilung von Klausuren auf ihre Termine ...
				new KlausurterminblockungAlgorithmusGreedy1(random, dynDaten), // Klausurgruppen zufällig, Termine zufällig
				new KlausurterminblockungAlgorithmusGreedy1b(random, dynDaten), // Termine nacheinander, Klausurgruppen zufällig
				new KlausurterminblockungAlgorithmusGreedy2(random, dynDaten), // Klausurgruppen nach Grad, Termine zufällig
				new KlausurterminblockungAlgorithmusGreedy2b(random, dynDaten), // Termine nacheinander, Klausurgruppen nach Grad
				new KlausurterminblockungAlgorithmusGreedy3(random, dynDaten), // Termine nacheinander, Klausurgruppen mit Backtracking
				// ... Ende der Algorithmen.
		};


		// Generiere das erste Ergebnis und speichere es in Zustand2 (bisheriges globales Optimum).
		dynDaten.aktion_Clear_TermineNacheinander_GruppeZufaellig();
		dynDaten.aktionZustand2Speichern();

		long zeitProAlgorithmus = 10L; // Weniger ist nicht gut.
		do {
			_logger.log("zeitProAlgorithmus --> " + zeitProAlgorithmus);

			// Alle Algorithmus starten (pro Algorithmus ggf. auch mehrfach).
			for (int iAlgo = 0; iAlgo < algorithmen.length; iAlgo++) {
				final long zeitEndeRunde = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmen[iAlgo].berechne(zeitEndeRunde);
				_logger.log(algorithmen[iAlgo].toString() + " --> " + dynDaten.gibTerminAnzahl());
			}

			zeitProAlgorithmus *= 2; // Nächste Runde hat mehr Zeit.
		} while (System.currentTimeMillis() + algorithmen.length * zeitProAlgorithmus <= zeitEndeGesamt); // noch Zeit?

		// Lade besten globalen Zustand
		dynDaten.aktionZustand2Laden();

		// Fülle die Liste mit Termin-Listen.
		out.termine.addAll(dynDaten.gibErzeugeOutput().termine);

		// Logger -4
		_logger.modifyIndent(-4);
	}

}
