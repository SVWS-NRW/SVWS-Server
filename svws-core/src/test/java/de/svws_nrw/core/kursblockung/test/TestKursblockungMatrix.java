package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.kursblockung.KursblockungMatrix;

/**
 * Diese Klasse testet {@link KursblockungMatrix}.
 */
@DisplayName("Diese Klasse testet {@link KursblockungMatrix}")
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestKursblockungMatrix {

	private static final long SEED = 1L;

	/** Matrix-Test 001 - Bisheriger Test für minimales Matching */
	@Test
	@DisplayName("Minimales Matching 001: Mit eindeutiger Lösung.")
	void test001_MinMatching_Eindeutige_Loesung() {
		final Random rnd = new Random(SEED);
		final KursblockungMatrix m = new KursblockungMatrix(rnd, 5, 6);
		final long[][] d = m.getMatrix();

		d[0][0] = 16;
		d[0][1] = 13;
		d[0][2] = 13;
		d[0][3] = 13;
		d[0][4] = 12;
		d[0][5] = 10;
		d[1][0] = 17;
		d[1][1] = 17;
		d[1][2] = 20;
		d[1][3] = 15;
		d[1][4] = 16;
		d[1][5] = 10;
		d[2][0] = 13;
		d[2][1] = 16;
		d[2][2] = 10;
		d[2][3] = 10;
		d[2][4] = 13;
		d[2][5] = 15;
		d[3][0] = 10;
		d[3][1] = 20;
		d[3][2] = 12;
		d[3][3] = 11;
		d[3][4] = 12;
		d[3][5] = 19;
		d[4][0] = 20;
		d[4][1] = 12;
		d[4][2] = 11;
		d[4][3] = 17;
		d[4][4] = 16;
		d[4][5] = 13;

		final int[] r2c = m.gibMinimalesBipartitesMatchingGewichtet(false);
		if ((r2c[0] != 4) || (r2c[1] != 5) || (r2c[2] != 3) || (r2c[3] != 0) || (r2c[4] != 2)) {
			fail("Matching schlug fehl.");
		}
	}

	/** Matrix-Test 002 - Bisheriger Test für Skalierung */
	@Test
	@DisplayName("Minimales Matching 002: Matrix[r][c] = r*c --> eindeutige Lösung")
	void test002_MinMatching_ProduktMatrix() {
		final Random rnd = new Random(SEED);

		for (int dimR = 1; dimR <= 30; dimR++) {
			for (int dimC = 1; dimC <= 30; dimC++) {
				final KursblockungMatrix m = new KursblockungMatrix(rnd, dimR, dimC);
				final long[][] d = m.getMatrix();
				for (int r = 0; r < dimR; r++) {
					for (int c = 0; c < dimC; c++) {
						d[r][c] = r * c;
					}
				}

				final int[] r2c = m.gibMinimalesBipartitesMatchingGewichtet(false);
				final int min = Math.min(dimR, dimC);
				for (int r = 0; r < dimR; r++) {
					if (r < min) {
						if (r2c[r] != (min - r - 1)) {
							fail("Matching schlug fehl für Dimension " + dimR + "x" + dimC);
						}
					} else {
						if (r2c[r] != -1) {
							fail("Zu viele Zuordnungen für Dimension " + dimR + "x" + dimC);
						}
					}
				}
			}
		}
	}

	/** Testet ein einfaches maximales Matching, bei dem ein Ringtausch erzwungen wird. */
	@Test
	@DisplayName("Maximales Matching 003: Quadratisch mit Ringtausch-Notwendigkeit")
	void test003_MaxMatching_Square() {
		final Random rnd = new Random(SEED);
		final KursblockungMatrix m = new KursblockungMatrix(rnd, 3, 3);
		final long[][] d = m.getMatrix();

		// R0 kann nur zu C0
		// R1 kann zu C0 und C1
		// R2 kann zu C1 und C2
		// Ein gieriger Ansatz würde R1 an C0 vergeben, was für R0 zur Sackgasse führt -> BFS nötig.
		d[0][0] = 1;
		d[1][0] = 1;
		d[1][1] = 1;
		d[2][1] = 1;
		d[2][2] = 1;

		final int[] r2c = m.gibMaximalesBipartitesMatching(false);

		assertEquals(0, r2c[0], "R0 muss C0 zugeordnet sein");
		assertEquals(1, r2c[1], "R1 muss C1 zugeordnet sein");
		assertEquals(2, r2c[2], "R2 muss C2 zugeordnet sein");
	}

	/** Testet den Sonderfall einer leeren Matrix. */
	@Test
	@DisplayName("Maximales Matching 004: Leere Matrix (keine Kanten)")
	void test004_MaxMatching_SonderfallEmpty() {
		final Random rnd = new Random(SEED);
		final KursblockungMatrix m = new KursblockungMatrix(rnd, 4, 4);
		// Matrix bleibt mit 0 initialisiert (keine Kanten).

		final int[] r2c = m.gibMaximalesBipartitesMatching(false);

		for (int i = 0; i < r2c.length; i++) {
			assertEquals(-1, r2c[i], "Zeile " + i + " darf keine Zuordnung haben.");
		}
	}

	/** Testet eine Matrix mit mehr Zeilen als Spalten. */
	@Test
	@DisplayName("Maximales Matching 005: Nichtquadratisch (Zeilen > Spalten)")
	void test005_MaxMatching_SonderfallZeilenUeberschuss() {
		final Random rnd = new Random(SEED);
		final KursblockungMatrix m = new KursblockungMatrix(rnd, 5, 2);
		final long[][] d = m.getMatrix();

		// Setze Kanten, alle Zeilen haben Verbindungen, aber es gibt nur 2 Spalten
		d[0][0] = 1;
		d[1][1] = 1;
		d[2][0] = 1;
		d[3][1] = 1;
		d[4][0] = 1;

		final int[] r2c = m.gibMaximalesBipartitesMatching(false);

		final int zuordnungen = berechneMatchingGroesse(r2c);
		assertEquals(2, zuordnungen, "Es dürfen maximal 2 Zuordnungen existieren, da nur 2 Spalten vorhanden sind.");
		pruefeKantenGueltigkeit(r2c, d);
	}

	/** Testet eine Matrix mit mehr Spalten als Zeilen. */
	@Test
	@DisplayName("Maximales Matching 006: Nichtquadratisch (Spalten > Zeilen)")
	void test006_MaxMatching_SonderfallSpaltenUeberschuss() {
		final Random rnd = new Random(SEED);
		final KursblockungMatrix m = new KursblockungMatrix(rnd, 3, 6);
		final long[][] d = m.getMatrix();

		// Jeder hat Auswahl, es muss ein Perfect-Matching der Zeilen geben
		d[0][1] = 5;
		d[0][3] = -2; // Auch andere Zahlen ungleich 0 gelten als Kante
		d[1][1] = 1;
		d[1][4] = 1;
		d[2][4] = 1;
		d[2][5] = 1;

		final int[] r2c = m.gibMaximalesBipartitesMatching(false);

		final int zuordnungen = berechneMatchingGroesse(r2c);
		assertEquals(3, zuordnungen, "Alle 3 Zeilen müssen zugeordnet sein.");
		pruefeKantenGueltigkeit(r2c, d);
	}

	/** Testet, ob nichtdeterministische Aufrufe valide Ergebnisse mit der identischen (maximalen) Größe liefern. */
	@Test
	@DisplayName("Maximales Matching 007: Deterministisch vs Nichtdeterministisch")
	void test007_MaxMatching_Nichtdeterministisch() {
		final Random rnd = new Random(SEED);

		for (int durchlauf = 0; durchlauf < 1000; durchlauf++) {
			final int dimR = rnd.nextInt(15) + 1;
			final int dimC = rnd.nextInt(15) + 1;

			final KursblockungMatrix m = new KursblockungMatrix(rnd, dimR, dimC);
			final long[][] d = m.getMatrix();

			// Zufälligen bipartiten Graphen erzeugen.
			for (int kante = 0; kante < 40; kante++) {
				d[rnd.nextInt(dimR)][rnd.nextInt(dimC)] = 1;
			}

			// Deterministisch berechnen (Basislinie).
			final int[] r2cBasis = m.gibMaximalesBipartitesMatching(false);
			final int maxSize = berechneMatchingGroesse(r2cBasis);
			pruefeKantenGueltigkeit(r2cBasis, d);

			// Nichtdeterministisch testen.
			for (int gegentests = 0; gegentests < 10; gegentests++) {
				final int[] r2cZufall = m.gibMaximalesBipartitesMatching(true);
				final int currentSize = berechneMatchingGroesse(r2cZufall);

				assertEquals(maxSize, currentSize,
						"Die maximale Größe des Matchings muss konstant sein, egal wie der Zufall entscheidet!");
				pruefeKantenGueltigkeit(r2cZufall, d);
			}
		}
	}


	/**
	 * Testet, ob das ungewichtete maximale Matching auf einer 0/1-Matrix
	 * exakt dieselbe Größe (Anzahl gefundener Kanten) hat wie das minimale
	 * gewichtete Matching derselben Matrix, bei der jede 1 durch eine -1 ersetzt wurde.
	 */
	@Test
	@DisplayName("Maximales Matching 008: Abgleich mit min. gewichtetem Matching (1 -> -1)")
	void test008_MaxMatching_vs_MinGewichtet() {
		final Random rnd = new Random(SEED);

		// Mehrere Durchläufe mit unterschiedlichen Dimensionen
		for (int durchlauf = 0; durchlauf < 1000; durchlauf++) {
			// Zufällige Dimensionen (zwischen 1 und 30)
			final int dimR = rnd.nextInt(30) + 1;
			final int dimC = rnd.nextInt(30) + 1;

			final KursblockungMatrix mMax = new KursblockungMatrix(rnd, dimR, dimC);
			final KursblockungMatrix mMin = new KursblockungMatrix(rnd, dimR, dimC);

			final long[][] dMax = mMax.getMatrix();
			final long[][] dMin = mMin.getMatrix();

			// Zufällige 0/1 bzw. 0/-1 Verteilung erzeugen
			for (int r = 0; r < dimR; r++) {
				for (int c = 0; c < dimC; c++) {
					if (rnd.nextBoolean()) {
						dMax[r][c] = 1;
						dMin[r][c] = -1;
					} else {
						dMax[r][c] = 0;
						dMin[r][c] = 0;
					}
				}
			}

			// 1. Maximales Matching berechnen
			final int[] r2cMax = mMax.gibMaximalesBipartitesMatching(false);
			final int sizeMax = berechneMatchingGroesse(r2cMax);

			// 2. Minimales gewichtetes Matching berechnen
			final int[] r2cMin = mMin.gibMinimalesBipartitesMatchingGewichtet(false);
			final int sizeMin = berechneMinMatchingGroesse(r2cMin, dMin);

			assertEquals(sizeMax, sizeMin,
					"Fehler im Durchlauf " + durchlauf + " bei Dimension " + dimR + "x" + dimC
							+ ": Max-Matching-Größe (" + sizeMax + ") weicht von Min-Matching-Größe (" + sizeMin + ") ab!");
		}
	}

	// --- Hilfsmethoden für die Tests ---

	/**
	 * Zählt, wie viele Zeilen erfolgreich einer Spalte zugewiesen wurden.
	 *
	 * @param r2c das Array mit der berechneten Zeilen- zu Spaltenzuordnung
	 * @return    die Anzahl der erfolgreichen Zuordnungen (Größe des Matchings)
	 */
	private static int berechneMatchingGroesse(final int[] r2c) {
		int count = 0;
		for (final int c : r2c) {
			if (c >= 0) {
				count++;
			}
		}
		return count;
	}


	/**
	 * Zählt, wie viele Zeilen beim minimalen gewichteten Matching erfolgreich über
	 * eine echte Kante (Gewicht = -1) einer Spalte zugewiesen wurden.
	 *
	 * @param r2c    das Array mit der berechneten Zeilen- zu Spaltenzuordnung
	 * @param matrix die zugrundeliegende Adjazenzmatrix mit den Kantengewichten
	 * @return       die Anzahl der Zuordnungen, die tatsächlich auf einer Kante (Gewicht -1) liegen
	 */
	private static int berechneMinMatchingGroesse(final int[] r2c, final long[][] matrix) {
		int count = 0;
		for (int r = 0; r < r2c.length; r++) {
			final int c = r2c[r];
			// Zähle nur Zuordnungen, die gültig sind (>= 0) und deren Kantengewicht -1 ist
			if ((c >= 0) && (matrix[r][c] == -1)) {
				count++;
			}
		}
		return count;
	}


	/**
	 * Prüft, ob jede erfolgte Zuordnung im Array r2c tatsächlich einer existierenden
	 * Kante (Wert != 0) in der Matrix entspricht.
	 *
	 * @param r2c    das Array mit der berechneten Zeilen- zu Spaltenzuordnung
	 * @param matrix die zugrundeliegende Adjazenzmatrix des bipartiten Graphen
	 */
	private static void pruefeKantenGueltigkeit(final int[] r2c, final long[][] matrix) {
		for (int r = 0; r < r2c.length; r++) {
			final int c = r2c[r];
			if (c >= 0) {
				assertNotEquals(0L, matrix[r][c],
						"Ungültige Zuordnung! Zeile " + r + " wurde Spalte " + c + " zugewiesen, aber Matrix-Wert ist 0.");
			}
		}
	}

}
