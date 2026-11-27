package de.svws_nrw.core.utils.uvblockung;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.uv.UvManager;
import de.svws_nrw.core.utils.uv.UvRegelManager;
import jakarta.validation.constraints.NotNull;

/**
 * Benchmark zum Vergleich der UV-Blockungsalgorithmen auf den beiden großen Testdatensätzen
 * {@link UvDatensatzGymnasium} und {@link UvDatensatzGymnasium2} (nacheinander).
 * Die Algorithmen laufen zeilenweise: In jeder Runde rechnet jeder Algorithmus ein Zeitintervall
 * (sequenziell, kein Threading), anschließend wird eine Zeile mit allen Bewertungen ausgegeben, so
 * dass die Tabelle von oben nach unten wächst und der jeweils beste Algorithmus direkt erkennbar ist.
 * Jeder Algorithmus erhält eine eigene {@link UvAlgorithmusDynDaten}-Instanz auf demselben (nicht
 * mutierten) {@link UvManager} und denselben festen Seed.
 *
 * Die Bewertung erfolgt über den Malus-Vektor in Prioritäts-Reihenfolge (kleiner Index = höhere
 * Priorität), d. h. [1, 0, ...] ist besser als [0, 99, ...].
 *
 * Der Test ist standardmäßig deaktiviert und wird manuell gestartet.
 *
 * @author Benjamin A. Bartsch
 */
@Disabled("Benchmark - manuell starten.")
@SuppressWarnings("java:S2699") // Benchmark ohne Assertions.
public class TestUvBenchmark {

	/** Die Laufzeit pro Algorithmus in Millisekunden. */
	private static final long LAUFZEIT_MS = 10_000L;

	/** Die Rechenzeit pro Algorithmus und Runde in Millisekunden. */
	private static final long INTERVALL_MS = 1_000L;

	/** Der feste Seed aller Algorithmen. */
	private static final long SEED = 1111L;

	/** Ein Logger ohne Ausgabe. */
	private static final Logger LOG = new Logger();


	@Test
	@DisplayName("Benchmark: Vergleich der UV-Algorithmen über die Zeit (zeilenweise)")
	void benchmark() {
		fuehreAus(LAUFZEIT_MS, INTERVALL_MS, SEED);
	}


	static void fuehreAus(final long laufzeitMs, final long intervallMs, final long seed) {
		fuehreAusDatensatz(UvDatensatzGymnasium.erzeugeUvManager(), UvDatensatzGymnasium.erzeugeRegeln(), laufzeitMs, intervallMs, seed);
		fuehreAusDatensatz(UvDatensatzGymnasium2.erzeugeUvManager(), UvDatensatzGymnasium2.erzeugeRegeln(), laufzeitMs, intervallMs, seed);
	}


	static void fuehreAusDatensatz(final UvManager man, final List<UvBlockungRegel> regeln, final long laufzeitMs,
			final long intervallMs, final long seed) {
		final UvPlanungsabschnitt plan = man.planungsabschnittGetByIdOrException(UvDatensatzGymnasium.PLANUNGSABSCHNITT);
		final int runden = (int) (laufzeitMs / intervallMs);

		final String[] namen = { "RW1" };
		final int anzahl = namen.length;
		final UvAlgorithmusDynDaten[] dyns = new UvAlgorithmusDynDaten[anzahl];
		final UvAlgorithmusImplAbstract[] algorithmen = new UvAlgorithmusImplAbstract[anzahl];
		final int[][] aktuell = new int[anzahl][];
		final int[][] beste = new int[anzahl][];
		final long[] besteZeit = new long[anzahl];

		System.out.println("=== UV-Benchmark ===");
		System.out.println("Datensatz:    " + man.lerngruppeGetMengeByPlanungsabschnitt(plan).size() + " Lerngruppen, "
				+ man.lehrerGetMengeAsList().size() + " Lehrkräfte, " + man.klasseGetMengeAsList().size() + " Klassen, "
				+ man.schieneGetMengeAsList().size() + " Schienen");
		System.out.println("Regeln:       " + regeln.size() + " Regeln (Fixierung und Verbot, Priorität HOCH)");
		System.out.println("Algorithmen:  " + String.join(", ", namen) + " (Seed " + seed + ")");
		System.out.println("Ablauf:       zeilenweise, je Runde " + (intervallMs / 1000) + "s pro Algorithmus, gesamt " + (laufzeitMs / 1000)
				+ "s je Algorithmus");
		System.out.println("Bewertung:    Malus-Vektor in Prioritäts-Reihenfolge (kleiner = besser), ' *' markiert den Spaltenbesten der Zeile.");
		System.out.println();

		for (int a = 0; a < anzahl; a++) {
			final Random rnd = new Random(seed);
			final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
			dyns[a] = new UvAlgorithmusDynDaten(LOG, rnd, man, manRegeln, plan);
			algorithmen[a] = switch (a) {
				case 0 -> new UvAlgorithmusImplRandomWalk1(LOG, rnd, dyns[a]);
				default -> throw new IllegalStateException("Unbekannter Algorithmus-Index: " + a);
			};
			aktuell[a] = dyns[a].gibMalusKopie();
			beste[a] = aktuell[a];
			besteZeit[a] = 0L;
		}

		System.out.println(gibKopfzeile(namen));
		System.out.println(gibZeile(0L, aktuell));

		for (int runde = 1; runde <= runden; runde++) {
			for (int a = 0; a < anzahl; a++) {
				algorithmen[a].berechneInnerhalb(intervallMs);
				aktuell[a] = dyns[a].gibMalusKopie();
				if (vergleiche(aktuell[a], beste[a]) < 0) {
					beste[a] = aktuell[a];
					besteZeit[a] = runde * intervallMs;
				}
			}
			System.out.println(gibZeile(runde * intervallMs, aktuell));
		}
		System.out.println();

		System.out.println("=== Bestwerte (Prioritäts-Reihenfolge, kleiner = besser) ===");
		int gesamtSieger = 0;
		for (int a = 0; a < anzahl; a++) {
			System.out.println(String.format("%-6s: %s bei %ds", namen[a], gibVektor(beste[a]), besteZeit[a] / 1000));
			if (vergleiche(beste[a], beste[gesamtSieger]) < 0) {
				gesamtSieger = a;
			}
		}
		System.out.println("Sieger: " + namen[gesamtSieger] + " mit " + gibVektor(beste[gesamtSieger]) + " bei " + (besteZeit[gesamtSieger] / 1000) + "s");
	}


	private static @NotNull String gibKopfzeile(final @NotNull String @NotNull [] namen) {
		final StringBuilder sb = new StringBuilder(String.format("%5s", "Zeit"));
		for (final @NotNull String name : namen) {
			sb.append(" | ").append(String.format("%-22s", name));
		}
		return sb.toString();
	}


	private static @NotNull String gibZeile(final long zeitMs, final int @NotNull [] @NotNull [] aktuell) {
		int bester = 0;
		for (int a = 1; a < aktuell.length; a++) {
			if (vergleiche(aktuell[a], aktuell[bester]) < 0) {
				bester = a;
			}
		}
		final StringBuilder sb = new StringBuilder(String.format("%4ds", zeitMs / 1000));
		for (int a = 0; a < aktuell.length; a++) {
			final String markierung = (vergleiche(aktuell[a], aktuell[bester]) == 0) ? " *" : "  ";
			sb.append(" | ").append(String.format("%-20s", gibVektor(aktuell[a]))).append(markierung);
		}
		return sb.toString();
	}


	private static @NotNull String gibVektor(final int @NotNull [] malus) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < malus.length; i++) {
			if (i > 0) {
				sb.append('/');
			}
			sb.append(malus[i]);
		}
		return sb.toString();
	}


	private static int vergleiche(final int @NotNull [] a, final int @NotNull [] b) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return (a[i] < b[i]) ? -1 : 1;
			}
		}
		return 0;
	}

}
