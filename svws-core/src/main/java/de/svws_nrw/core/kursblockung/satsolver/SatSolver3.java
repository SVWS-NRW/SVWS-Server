package de.svws_nrw.core.kursblockung.satsolver;

import java.util.Arrays;
import java.util.Random;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.adt.set.AVLSet;
import jakarta.validation.constraints.NotNull;

/**
 * Ein SAT-Solver dient zum Lösen von Formeln in konjunktiver Normalform. Eine Formel besteht Klauseln die durch ein UND
 * verknüpft sind. Klauseln widerum sind Variablen, die mit ODER verknüpft sind. Die Variablen dürfen negiert vorkommen.
 * Beispiel: <br>
 * {@code  Formel = (x1 OR x2) AND (-x1 OR -x2)} Klauseln die größer als 3 sind, wandelt dieser Solver intern auf die
 * Größe 3 um.
 *
 * @author Benjamin A. Bartsch
 */
public final class SatSolver3 extends SatSolverA {

	private static final int MAX_LEARNED_CLAUSE_SIZE = 3;

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected final @NotNull Random _random;

	/**
	 * Im Heap werden Variablen gespeichert. Die oberste Variable wird als nächstes gewählt.
	 */
	private final @NotNull Heap heap;

	/**
	 * Die Anzahl an Variablen in den Arrays {@link #vArrayPos und #vArrayNeg}.
	 */
	private int vSize;

	/**
	 * Alle positiven Variablen (bzw. Literale).
	 */
	private @NotNull Variable @NotNull [] vArrayPos;

	/**
	 * Alle negativen Variablen (bzw. Literale).
	 */
	private @NotNull Variable @NotNull [] vArrayNeg;

	/**
	 * Die Anzahl an eingefügten Klauseln.
	 */
	private int cSize;

	private int learnClauseMin;

	/**
	 * Konstruktor.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	public SatSolver3(final @NotNull Random pRandom) {
		super();
		_random = pRandom;
		heap = new Heap(pRandom);
		vSize = 0;
		cSize = 0;
		vArrayPos = new Variable[1];
		vArrayNeg = new Variable[1];
	}

	@Override
	public boolean isVarTrue(final int pVar) {
		final @NotNull
		Variable v = getVarOf(pVar);
		return v.index == -1;
	}

	@Override
	public int createNewVar() {
		// Erhöhung der Variablenanzahl um 1.
		vSize++;

		// Verdopplung aller Arrays, falls kein Platz mehr vorhanden.
		if (vSize >= vArrayPos.length) {
			final int newSize = vSize * 2;
			vArrayPos = Arrays.copyOf(vArrayPos, newSize);
			vArrayNeg = Arrays.copyOf(vArrayNeg, newSize);
		}

		// Variable und ihrer Negation erzeugen.
		final @NotNull
		Variable vPos = new Variable(_random, +vSize);
		final @NotNull
		Variable vNeg = new Variable(_random, -vSize);
		vPos.negation = vNeg;
		vNeg.negation = vPos;
		vArrayPos[vSize] = vPos;
		vArrayNeg[vSize] = vNeg;

		// Variablen und ihrer Negation zum Stack hinzufügen.
		heap.insert(vPos);
		heap.insert(vNeg);

		return vSize;
	}

	@Override
	public void addClause(final @NotNull int[] pVars) {

		// Eine leere Klausel sollte es bei der Eingabe nicht geben --> Fehler.
		if (pVars.length == 0) {
			// TODO BAR Logger verwenden.
			System.out.println("WARNUNG: Leere Klausel bei SatSolver.addClause(int[] pVars)!");
			return;
		}

		// Doppelte Variablen entfernen und Tautologien ignorieren.
		final @NotNull
		AVLSet<@NotNull Integer> set = new AVLSet<>();
		for (final int v : pVars) {
			if (set.contains(-v)) {
				return; // Tautologie gefunden.
			}
			set.add(v); // Ignoriert doppelte Elemente.
		}

		// Set in LinkedList umwandeln.
		final @NotNull
		LinkedCollection<@NotNull Integer> list = new LinkedCollection<>();
		while (!set.isEmpty()) {
			list.addLast(set.pollFirst());
		}

		// N-CNF --> 3-CNF
		while (list.size() > 3) {
			final int x = list.removeFirst();
			final int y = list.removeFirst();
			final int z = createNewVar();
			list.addLast(z);
			// z = x OR y
			addClause2(-x, z);
			addClause2(-y, z);
			addClause3(x, y, -z);
		}

		if (list.size() == 3) {
			addClause3(list.removeFirst(), list.removeFirst(), list.removeFirst());
		}
		if (list.size() == 2) {
			addClause2(list.removeFirst(), list.removeFirst());
		}
		if (list.size() == 1) {
			addClause1(list.removeFirst());
		}

	}

	/**
	 * Fügt der Datenstruktur eine 1-CNF-Klausel hinzu.
	 *
	 * @param x Das 1. Literal (Variablennummer) der Klausel.
	 */
	private void addClause1(final int x) {
		final @NotNull
		Variable varX = getVarOf(x);
		if (varX.negation == null)
			throw new NullPointerException();

		heap.remove(varX);
		heap.remove(varX.negation);

		final @NotNull
		Clause c = new Clause(varX);
		cSize++;

		varX.clauses.addLast(c);
		varX.statSatFree[c.sat][c.free]++;

		heap.insert(varX);
		heap.insert(varX.negation);
	}

	/**
	 * Fügt der Datenstruktur eine 2-CNF-Klausel hinzu.
	 *
	 * @param x Das 1. Literal (Variablennummer) der Klausel.
	 * @param y Das 2. Literal (Variablennummer) der Klausel.
	 */
	private void addClause2(final int x, final int y) {
		final @NotNull
		Variable varX = getVarOf(x);
		final @NotNull
		Variable varY = getVarOf(y);
		if ((varX.negation == null) || (varY.negation == null))
			throw new NullPointerException();

		heap.remove(varX);
		heap.remove(varY);
		heap.remove(varX.negation);
		heap.remove(varY.negation);

		final @NotNull
		Clause c = new Clause(varX, varY);
		cSize++;

		varX.clauses.addLast(c);
		varX.statSatFree[c.sat][c.free]++;
		varX.neighbours.addLast(varY);

		varY.clauses.addLast(c);
		varY.statSatFree[c.sat][c.free]++;
		varY.neighbours.addLast(varX);

		heap.insert(varX);
		heap.insert(varY);
		heap.insert(varX.negation);
		heap.insert(varY.negation);
	}

	/**
	 * Fügt der Datenstruktur eine 3-CNF-Klausel hinzu.
	 *
	 * @param x Das 1. Literal (Variablennummer) der Klausel.
	 * @param y Das 2. Literal (Variablennummer) der Klausel.
	 * @param z Das 3. Literal (Variablennummer) der Klausel.
	 */
	private void addClause3(final int x, final int y, final int z) {
		final @NotNull
		Variable varX = getVarOf(x);
		final @NotNull
		Variable varY = getVarOf(y);
		final @NotNull
		Variable varZ = getVarOf(z);
		if ((varX.negation == null) || (varY.negation == null) || (varZ.negation == null))
			throw new NullPointerException();

		heap.remove(varX);
		heap.remove(varY);
		heap.remove(varZ);
		heap.remove(varX.negation);
		heap.remove(varY.negation);
		heap.remove(varZ.negation);

		final @NotNull
		Clause c = new Clause(varX, varY, varZ);
		cSize++;

		varX.clauses.addLast(c);
		varX.statSatFree[c.sat][c.free]++;
		varX.neighbours.addLast(varY);
		varX.neighbours.addLast(varZ);

		varY.clauses.addLast(c);
		varY.statSatFree[c.sat][c.free]++;
		varY.neighbours.addLast(varX);
		varY.neighbours.addLast(varZ);

		varZ.clauses.addLast(c);
		varZ.statSatFree[c.sat][c.free]++;
		varZ.neighbours.addLast(varX);
		varZ.neighbours.addLast(varY);

		heap.insert(varX);
		heap.insert(varY);
		heap.insert(varZ);
		heap.insert(varX.negation);
		heap.insert(varY.negation);
		heap.insert(varZ.negation);
	}

	@Override
	public int getVarCount() {
		return vSize;
	}

	@Override
	public int getClauseCount() {
		return cSize;
	}

	@Override
	public int solve(final long pMaxTimeMillis) {
		final long timeStart = System.currentTimeMillis();

		// Backtracking-Datenstruktur
		final @NotNull
		Variable[] backtrackV = new Variable[vSize];
		final @NotNull
		Variable @NotNull [] backtrackLearn = new Variable[MAX_LEARNED_CLAUSE_SIZE];
		final @NotNull
		boolean @NotNull [] backtrackB = new boolean[vSize];
		int index = 0;

		// Berechnungsschleife
		int max = 1;
		int countDown = 0; // start with direct simplification
		learnClauseMin = MAX_LEARNED_CLAUSE_SIZE + 1;
		int maxIndex = 0;

		while ((index >= 0) && !heap.isEmpty()) {
			if (index > maxIndex) {
				maxIndex = index;
			}

			if (countDown == 0) {
				// UNDO ALL
				for (int i = index; i >= 0; i--) {
					if (backtrackV[i] != null) {
						@SuppressWarnings("cast")
						final Variable bvi = (Variable) backtrackV[i];
						unitpropagation_undo(bvi); // TODO BAR Transpiler Cast Meldung
					}
					backtrackV[i] = null;
					backtrackB[i] = false;
				}
				index = 0;

				// LEARNED SOMETHING?
				if ((learnClauseMin >= 1) && (learnClauseMin <= MAX_LEARNED_CLAUSE_SIZE)) {
					final @NotNull
					int[] clause = new int[learnClauseMin];
					for (int i = 0; i < clause.length; i++) {
						clause[i] = -backtrackLearn[i].nr;
					}
					// System.out.println("LEARNED " + Arrays.toString(clause));
					addClause(clause);
				}

				// SIMPLIFY
				final int result = simplify();
				if (result != SatSolverA.RESULT_UNKNOWN) {
					return result;
				}

				// RESET VALUES
				max = max + 1 + max / 2;
				countDown = max;
				learnClauseMin = MAX_LEARNED_CLAUSE_SIZE + 1;

				// Zeit abgelaufen?
				if (System.currentTimeMillis() - timeStart > pMaxTimeMillis) {
					return SatSolverA.RESULT_UNKNOWN;
				}
				continue;
			}

			countDown--;

			Variable varP = backtrackV[index];

			// Index abgearbeitet?
			if ((varP != null) && (backtrackB[index])) {
				// System.out.println(fill(index) + "XX Wahl " + var);
				unitpropagation_undo(varP);
				backtrackV[index] = null;
				backtrackB[index] = false;
				learnClause(backtrackV, backtrackLearn, index);
				index--;
				continue;
			}

			// Woher kommt die Variable?
			if (varP == null) {
				varP = heap.top();
				if (!varP.isUnsat()) {
					// 1. Wahl
					// System.out.println(fill(index) + "1. Wahl " + var);
					backtrackV[index] = varP;
					unitpropagation(varP);
					index++;
					continue;
				}
				// nichts
			} else {
				// System.out.println(fill(index) + "UNDO " + var);
				unitpropagation_undo(varP);

			}

			// 2. Wahl
			if (varP.negation == null)
				throw new NullPointerException();
			varP = varP.negation;

			if (varP.isUnsat()) {
				// System.out.println(fill(index) + "2. Wahl UNSAT " + var);
				backtrackV[index] = null;
				learnClause(backtrackV, backtrackLearn, index);
				index--;
				continue;
			}
			// System.out.println(fill(index) + "2. Wahl " + var);
			backtrackV[index] = varP;
			backtrackB[index] = true;
			unitpropagation(varP);
			index++;
			continue;
		}

		// SAT or UNSAT?
		return heap.isEmpty() ? SatSolverA.RESULT_SATISFIABLE : SatSolverA.RESULT_UNSATISFIABLE;
	}

	private void learnClause(final @NotNull Variable[] backtrackV, final @NotNull Variable[] backtrackLearn, final int size) {
		if (size < 1) {
			return;
		}
		if (size >= learnClauseMin) {
			return;
		}
		learnClauseMin = size;
		for (int i = 0; i < size; i++) {
			backtrackLearn[i] = backtrackV[i];
		}
		// System.out.println("BETTER "+learnClauseMin+" : "+ Arrays.toString(Arrays.copyOf(backtrackV, size)));
	}

	private int simplify() {

		@SuppressWarnings("unused")
		int count1 = 0;

		boolean changed = true;
		while (changed) {
			changed = false;

			for (int nr = 1; nr <= vSize; nr++) {
				final Variable varP = vArrayPos[nr];
				if (varP.index < 0) {
					continue;
				}
				// UNSAT?
				if (varP.statSatFree[0][0] > 0) {
					return SatSolverA.RESULT_UNSATISFIABLE;
				}
				if (varP.negation == null)
					throw new NullPointerException();
				if (varP.negation.statSatFree[0][0] > 0) {
					return SatSolverA.RESULT_UNSATISFIABLE;
				}
				if ((varP.statSatFree[0][1] > 0) && (varP.negation.statSatFree[0][1] > 0)) {
					return SatSolverA.RESULT_UNSATISFIABLE;
				}

				// 1-CNF
				if (varP.statSatFree[0][1] > 0) {
					unitpropagation(varP);
					count1++;
					changed = true;
					continue;
				}
				if (varP.negation.statSatFree[0][1] > 0) {
					unitpropagation(varP.negation);
					count1++;
					changed = true;
					continue;
				}
				// No Negation
				if (varP.getClauseOccurences() == 0) {
					unitpropagation(varP.negation);
					count1++;
					changed = true;
					continue;
				}
				if (varP.negation.getClauseOccurences() == 0) {
					unitpropagation(varP);
					count1++;
					changed = true;
					continue;
				}

			}
		}
		// System.out.println("SIMPLIFICATION: 1-CNF = " + count1);

		return SatSolverA.RESULT_UNKNOWN;
	}

	@SuppressWarnings("unused")
	private static String fill(final int index) {
		String s = "";
		for (int i = 0; i < index; i++) {
			s = s + "    ";
		}
		return s;
	}

	/**
	 * Setzt die Variable {@code varP} auf TRUE und aktualisiert die gesamte Datenstruktur entsprechend.
	 *
	 * @param varP Die Variable, die auf TRUE gesetzt wird.
	 */
	private void unitpropagation(final @NotNull Variable varP) {
		final Variable varN = varP.negation;
		if (varN == null)
			throw new NullPointerException();

		// Variable und ihre Negation entfernen.
		heap.remove(varP);
		heap.remove(varN);

		// Merke dir den Status der Variablen.
		varP.index = -1;
		varN.index = -2;

		// Propagiere VarP=TRUE und varN=FALSE.
		unitpropagationHelper(varP.clauses, +1, -1);
		unitpropagationHelper(varN.clauses, +0, -1);
	}

	/**
	 * Setzt die Variable {@code varP} von TRUE auf FREI und aktualisiert die gesamte Datenstruktur entsprechend.
	 *
	 * @param varP Die Variable, die von TRUE auf FREI gesetzt wird.
	 */
	private void unitpropagation_undo(final @NotNull Variable varP) {
		final Variable varN = varP.negation;
		if (varN == null)
			throw new NullPointerException();

		// UNDO VarP=TRUE und varN=FALSE.
		unitpropagationHelper(varP.clauses, -1, +1);
		unitpropagationHelper(varN.clauses, +0, +1);

		// Variable und ihre Negation dem Heap hinzufügen.
		heap.insert(varP);
		heap.insert(varN);
	}

	/**
	 * Eine Hilsmethode, die alle Klauseln der Liste aktualisiert.
	 *
	 * @param clauses    Alle zu informmierenden Klausel.
	 * @param pDeltaSat  Die Veränderung der erfüllten Klauseln, relativ zum 1. Index im 2D-Array
	 *                   {@link Variable#statSatFree}.
	 * @param pDeltaFree Die Veränderung der freien Variablen, relativ zum 2. Index im 2D-Array
	 *                   {@link Variable#statSatFree}.
	 */
	private void unitpropagationHelper(final @NotNull LinkedCollection<@NotNull Clause> clauses, final int pDeltaSat,
			final int pDeltaFree) {
		for (final @NotNull
				Clause c : clauses) {
			final int sat1 = c.sat;
			final int free1 = c.free;
			final int sat2 = sat1 + pDeltaSat;
			final int free2 = free1 + pDeltaFree;
			for (final @NotNull
					Variable v : c.variables) {
				v.statSatFree[sat1][free1]--;
				v.statSatFree[sat2][free2]++;
				heap.update(v);
			}
			c.sat = sat2;
			c.free = free2;
		}
	}

	/**
	 * Liefert das zugehörige Variablenobjekt.
	 *
	 * @param pNr Der Variablennummer.
	 *
	 * @return Das zugehörige Variablenobjekt.
	 */
	private @NotNull Variable getVarOf(final int pNr) {
		return (pNr > 0) ? vArrayPos[pNr] : vArrayNeg[-pNr];
	}

}
