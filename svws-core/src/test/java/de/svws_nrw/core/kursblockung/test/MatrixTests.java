package de.svws_nrw.core.kursblockung.test;

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
class MatrixTests {

	private static final long _SEED = 1L;

	/** Matrix-Test 001 */
	@Test
	@DisplayName("Matrix-Test 001")
	void test001() {
		final Random rnd = new Random(_SEED);
		final KursblockungMatrix m = new KursblockungMatrix(rnd, 5, 6);
		final long[][] d = m.getMatrix();

		//		 16|13|13|13|12|10
		//		 17|17|20|15|16|10
		//		 13|16|10|10|13|15
		//		 10|20|12|11|12|19
		//		 20|12|11|17|16|13
		d[0][0] = 16; d[0][1] = 13; d[0][2] = 13; d[0][3] = 13; d[0][4] = 12; d[0][5] = 10;
		d[1][0] = 17; d[1][1] = 17; d[1][2] = 20; d[1][3] = 15; d[1][4] = 16; d[1][5] = 10;
		d[2][0] = 13; d[2][1] = 16; d[2][2] = 10; d[2][3] = 10; d[2][4] = 13; d[2][5] = 15;
		d[3][0] = 10; d[3][1] = 20; d[3][2] = 12; d[3][3] = 11; d[3][4] = 12; d[3][5] = 19;
		d[4][0] = 20; d[4][1] = 12; d[4][2] = 11; d[4][3] = 17; d[4][4] = 16; d[4][5] = 13;

		final int[] r2c = m.gibMinimalesBipartitesMatchingGewichtet(false);
		if ((r2c[0] != 4) || (r2c[1] != 5) || (r2c[2] != 3) || (r2c[3] != 0) || (r2c[4] != 2))
			fail("Matching schlug fehl.");
	}

	/** Matrix-Test 001 */
	@Test
	@DisplayName("Matrix-Test 002")
	void test002() {
		final Random rnd = new Random(_SEED);


		for (int dimR = 1; dimR <= 30; dimR++) {
			for (int dimC = 1; dimC <= 30; dimC++) {
				final KursblockungMatrix m = new KursblockungMatrix(rnd, dimR, dimC);
				final long[][] d = m.getMatrix();
				for (int r = 0; r < dimR; r++) {
					for (int c = 0; c < dimC; c++) {
						d[r][c] = r * c;
					}
				}
				// Der Algorithmus muss deterministisch arbeiten,
				// andernfalls ist nicht immer die Antidiagonale das beste Ergebnis.
				final int[] r2c = m.gibMinimalesBipartitesMatchingGewichtet(false);
				final int min = Math.min(dimR, dimC);
				for (int r = 0; r < dimR; r++) {
					if (r < min) {
						if (r2c[r] != min - r - 1) {
							System.out.println(m.convertToString("", 5, false));
							fail("Matching schlug fehl.");
						}
					} else {
						if (r2c[r] != -1) {
							System.out.println(m.convertToString("", 5, false));
							fail("Matching schlug fehl.");
						}
					}
				}
			}
		}
	}


}
