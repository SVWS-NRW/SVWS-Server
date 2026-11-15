package de.svws_nrw.core.kursblockung.benchmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentK;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKFachgruppe;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKFachwahlmatrix;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKMatching;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKMixVier;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKOptimiereBest;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKSchnellW;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKSchuelervorschlag;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentKSchuelervorschlagSingle;
import de.svws_nrw.core.kursblockung.KursblockungDynStatistik;
import de.svws_nrw.core.kursblockung.test.Kurs42Converter;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * Lokaler Benchmark für Datensatz 1 oder 2:
 * 10 Seeds, Average pro Metrik über die Seeds, 10/20 s, maximal 5 Seeds parallel; im DEV-Betrieb deaktiviert.
 *
 * @author Benjamin A. Bartsch
 */
@Disabled("Lokaler Benchmark - nicht Teil des DEV-Testlaufs")
class TestKursblockungKDatensatz {

	private static final long[] SEEDS = { 1111L, 2222L, 3333L, 4444L, 5555L, 6666L, 7777L, 8888L, 9999L, 11110L };
	private static final int[] MESSZEITEN = { 10, 20 };
	/** Maximale Anzahl paralleler Seed-Läufe. */
	private static final int MAX_PARALLEL = 5;
	private static final String PFAD_DATEN_001 = "de/svws_nrw/core/kursblockung/blockung001/";
	private static final String PFAD_DATEN_002 = "de/svws_nrw/core/kursblockung/blockung002/";
	private static final int[] ALGORITHMEN = { 100, 1, 3, 9, 4, 5, 6, 7 };
	private static final int[] DATENSAETZE = { 1, 2 };

	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("Kursblockung Datensatz: alle Permanent-K-Algorithmen, 10 Seeds, Average pro Metrik, 10/20 s, maximal 5 parallel")
	void benchmark() throws Exception {
		System.out.println("Seeds: " + Arrays.toString(SEEDS));
		System.out.println("Java: " + System.getProperty("java.version") + " / " + System.getProperty("java.vendor"));
		System.out.println("VM: " + System.getProperty("java.vm.name") + " / " + System.getProperty("java.vm.version"));
		System.out.println("OS: " + System.getProperty("os.name") + " / " + System.getProperty("os.arch")
				+ " / CPUs: " + Runtime.getRuntime().availableProcessors()
				+ " / maxMB: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024));
		for (final int datensatz : DATENSAETZE) {
			System.out.println();
			System.out.println("=== Kursblockung Datensatz(" + datensatz + "): Permanent-K-Algorithmen ===");
			System.out.printf("%-52s", "Algorithmus");
			for (final int zeit : MESSZEITEN) {
				System.out.printf(" | %-20s", zeit + "s");
			}
            System.out.println("(Werte: Average pro Metrik über die 10 Seeds; 10 s, 20 s)");
			for (final int algo : ALGORITHMEN) {
				final String algorithmusName = erzeugeAlgorithmus(algo, new Random(1L), new Logger(), ladeDatensatz(datensatz)).getClass().getSimpleName();
				final double[][] summe = new double[MESSZEITEN.length][3];
				final ExecutorService pool = Executors.newFixedThreadPool(Math.min(SEEDS.length, MAX_PARALLEL));
				final List<Future<long[][]>> futures = new ArrayList<>();
				for (final long seed : SEEDS) {
					futures.add(pool.submit(() -> bewerteSeed(algo, seed, datensatz)));
				}
				for (final Future<long[][]> future : futures) {
					final long[][] scores = future.get();
					for (int i = 0; i < MESSZEITEN.length; i++) {
						for (int j = 0; j < 3; j++) {
							summe[i][j] += alsDouble(scores[i][j]);
						}
					}
				}
				pool.shutdownNow();
				for (int i = 0; i < MESSZEITEN.length; i++) {
					for (int j = 0; j < 3; j++) {
						summe[i][j] /= SEEDS.length;
					}
				}
				System.out.printf("%-52s", algorithmusName);
				for (int i = 0; i < MESSZEITEN.length; i++) {
					System.out.printf(" | %-20s", gibZeile(summe[i]));
				}
				System.out.println();
			}
		}
	}

	private static KursblockungAlgorithmusPermanentK erzeugeAlgorithmus(final int index, final Random random, final Logger logger,
			final GostBlockungsdatenManager input) {
		return switch (index) {
			case 100 -> new KursblockungAlgorithmusPermanentKMixVier(random, logger, input);
			case 1 -> new KursblockungAlgorithmusPermanentKMatching(random, logger, input);
			case 3 -> new KursblockungAlgorithmusPermanentKSchuelervorschlag(random, logger, input);
			case 9 -> new KursblockungAlgorithmusPermanentKOptimiereBest(random, logger, input, null);
			case 4 -> new KursblockungAlgorithmusPermanentKSchuelervorschlagSingle(random, logger, input);
			case 5 -> new KursblockungAlgorithmusPermanentKFachgruppe(random, logger, input);
			case 6 -> new KursblockungAlgorithmusPermanentKFachwahlmatrix(random, logger, input);
			case 7 -> new KursblockungAlgorithmusPermanentKSchnellW(random, logger, input);
			default -> throw new IllegalArgumentException("Unbekannter Algorithmus-Index: " + index);
		};
	}

	private static long[][] bewerteSeed(final int algoIndex, final long seed, final int datensatz) {
		final KursblockungAlgorithmusPermanentK algorithmus = erzeugeAlgorithmus(algoIndex, new Random(seed), new Logger(), ladeDatensatz(datensatz));
		final long[][] scores = new long[MESSZEITEN.length][3];
		final long[] best = { Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE };
		final long start = System.currentTimeMillis();
		for (int messIndex = 0; messIndex < MESSZEITEN.length; messIndex++) {
			algorithmus.next(start + (MESSZEITEN[messIndex] * 1000L));
			algorithmus.ladeBestMitSchuelerverteilung();
			final KursblockungDynStatistik statistik = algorithmus.gibDynDaten().gibStatistik();
			final long[] aktuell = {
					statistik.gibBewertungRegelverletzungen(),
					statistik.gibBewertungNichtwahlen(),
					statistik.gibBewertungKursdifferenz()
			};
			if (istBesser(aktuell, best)) {
				System.arraycopy(aktuell, 0, best, 0, best.length);
			}
			System.arraycopy(best, 0, scores[messIndex], 0, best.length);
		}
		return scores;
	}

	private static synchronized @NotNull GostBlockungsdatenManager ladeDatensatz(final int datensatz) {
		final String pfad = (datensatz == 1) ? PFAD_DATEN_001 : PFAD_DATEN_002;
		final GostBlockungsdatenManager manager = new Kurs42Converter(pfad, 999L, false).gibKursblockungInput();
		if (datensatz == 1) {
			regelSperreSchieneFuerKursart(manager, "LK", 3, manager.schieneGetAnzahl());
			regelSperreSchieneFuerKursart(manager, "GK", 1, 2);
			regelSperreSchieneFuerKursart(manager, "PJK", 1, 2);
			regelFixiereKurseInSchieneSonstNichts(manager, new long[] { 35, 36, 37 }, 12);
			regelFixiereKurseInSchieneSonstNichts(manager, new long[] { 68 }, 13);
			regelFixiereKurseInSchieneSonstNichts(manager, new long[] { 18, 33 }, 14);
		} else {
			fixiereKursInSchiene(manager, "BI", 1, 1, 1L);
			fixiereKursInSchiene(manager, "M", 1, 1, 2L);
			fixiereKursInSchiene(manager, "L", 1, 1, 3L);
		}
		return manager;
	}

	private static void regelSperreSchieneFuerKursart(final @NotNull GostBlockungsdatenManager manager,
			final @NotNull String kursartKuerzel, final int von, final int bis) {
		final GostKursart kursart = GostKursart.fromKuerzel(kursartKuerzel);
		if (kursart == null) {
			throw new IllegalStateException("Kursart nicht gefunden: " + kursartKuerzel);
		}
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = manager.regelGetAnzahl() + 1;
		regel.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		regel.parameter.add((long) kursart.id);
		regel.parameter.add((long) von);
		regel.parameter.add((long) bis);
		manager.regelAdd(regel);
	}

	private static void regelFixiereKurseInSchieneSonstNichts(final @NotNull GostBlockungsdatenManager manager,
			final long[] kursIDs, final int schiene) {
		for (final GostBlockungKurs kurs : manager.daten().kurse) {
			boolean gefunden = false;
			for (final long kursID : kursIDs) {
				if (kurs.id == kursID) {
					gefunden = true;
					break;
				}
			}
			final GostBlockungRegel regel = new GostBlockungRegel();
			regel.id = manager.regelGetAnzahl() + 1;
			regel.typ = gefunden ? GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ : GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
			regel.parameter.add(kurs.id);
			regel.parameter.add((long) schiene);
			manager.regelAdd(regel);
		}
	}

	private static void fixiereKursInSchiene(final @NotNull GostBlockungsdatenManager manager,
			final @NotNull String fachKuerzel, final int nummer, final int schiene, final long regelId) {
		final GostFach fach = manager.faecherManager().faecher().stream()
				.filter(f -> fachKuerzel.equals(f.kuerzel) || fachKuerzel.equals(f.kuerzelAnzeige))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Fach nicht gefunden: " + fachKuerzel));
		final GostBlockungKurs kurs = manager.daten().kurse.stream()
				.filter(k -> (k.fach_id == fach.id) && (k.kursart == GostKursart.GK.id) && (k.nummer == nummer))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Kurs nicht gefunden: " + fachKuerzel + "-GK" + nummer));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = regelId;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(kurs.id);
		regel.parameter.add((long) schiene);
		manager.regelAdd(regel);
	}

	private static boolean istBesser(final long[] aktuell, final long[] best) {
		for (int i = 0; i < best.length; i++) {
			if (aktuell[i] != best[i]) {
				return aktuell[i] < best[i];
			}
		}
		return false;
	}

	private static String gibZeile(final double[] werte) {
		return gibZahl(werte[0]) + "/"
				+ gibZahl(werte[1]) + "/"
				+ gibZahl(werte[2]);
	}

	private static double alsDouble(final Long wert) {
		return wert.doubleValue();
	}

	private static String gibZahl(final double wert) {
		return (wert == 0.0) ? "0" : String.format("%.1f", wert);
	}
}
