package de.svws_nrw.core.adt.sat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert eine aussagenlogische Formel in konjunktiver Normalform (CNF).
 * Die Formel besteht aus einer Menge von Klauseln, welche mit einem logischen UND verbunden sind.
 * Jede Klausel beinhaltet booleschen Variablen (auch negiert), welche mit einem logischen ODER verbunden sind.
 *  <br>
 * Ein sogenannter SAT-Solver erhält diese Formel als Eingabe und sucht nach einer Belegung der Variablen,
 * so dass jede Klausel erfüllt (also TRUE) ist.
 *
 * @author Benjamin A. Bartsch
 */
public final class SatInput {

    /** Die aktuelle Anzahl an Variablen. */
    private int _nVars;

    /** Eine Variable, die mit Hilfe einer Klausel auf TRUE forciert wird und somit eine Konstante ist.*/
    private int _varTRUE;

    /** Eine Variable, die mit Hilfe einer Klausel auf FALSE forciert wird und somit eine Konstante ist. */
    private int _varFALSE;

    /** Die aktuelle Anzahl an Variablen. */
    private final @NotNull List<@NotNull Integer @NotNull[]> _clauses;

    /**
     * Erzeugt eine neues Objekt. Anschließend lassen sich Variablen erzeugen und Klauseln hinzufügen.
     * Möchte man die Formel f = (x1 OR x2 OR NOT x3) AND (NOT x2 OR x3) AND (x5) kodieren, so funktioniert das so:  <br>
     * <pre>
     *     SatFormula f = new SatFormula();
     *     int x1 = f.createNewVar();
     *     int x2 = f.createNewVar();
     *     int x3 = f.createNewVar();
     *     f.createNewVar(); // not used
     *     int x5 = f.createNewVar();
     *
     *     f.addClause(new int[] {x1, x2, -x3}); // adds {1, 2, -3}
     *     f.addClause(new int[] {-x2, x3});     // adds {-2, 3}
     *     f.addClause(new int[] {x5});          // adds {5}
     * </pre>
     */
    public SatInput() {
        _nVars = 0;
        _clauses = new ArrayList<>();
        _varTRUE = 0;  // 0 ist ein ungültiger Dummy-Wert, der nach dem ersten Aufruf definiert wird.
        _varFALSE = 0; // 0 ist ein ungültiger Dummy-Wert, der nach dem ersten Aufruf definiert wird.
    }

    @Override
    public @NotNull String toString() {
        return getDimacsHeader();
    }

    /**
     * Liefert eine Variable, die zuvor auf TRUE forciert wurde.
     *
     * @return Eine Variable, die zuvor auf TRUE forciert wurde.
     */
    public int getVarTRUE() {
        if (_varTRUE == 0) { // Definiere _varTRUE, falls dies der erste Aufruf ist.
            _varTRUE = create_var();
            add_clause_1(+_varTRUE);
        }
        return _varTRUE;
    }

    /**
     * Liefert eine Variable, die zuvor auf FALSE forciert wurde.
     *
     * @return Eine Variable, die zuvor auf FALSE forciert wurde.
     */
    public int getVarFALSE() {
        if (_varFALSE == 0) { // Definiere _varFALSE, falls dies der erste Aufruf ist.
            _varFALSE = create_var();
            add_clause_1(-_varFALSE);
        }
        return _varFALSE;
    }

    /**
     * Liefert die interne Anzahl an erzeugten Variablen.
     *
     * @return Die interne Anzahl an erzeugten Variablen.
     */
    public int getVarCount() {
        return _nVars;
    }

    /**
     * Liefert die Menge aller Klauseln.
     *
     * @return die Menge aller Klauseln.
     */
    public @NotNull List<@NotNull Integer @NotNull[]> getClauses() {
        return _clauses;
    }

    private @NotNull String getDimacsHeader() {
        return "p cnf " + _nVars + " " + _clauses.size();
    }

    /**
     * Erzeugte eine neue Variable. Den zurückgegebenen Integer-Wert darf man nun in Klauseln (auch negiert)
     * benutzen. Eine Variable hat niemals den Wert 0, da dieser Wert nicht negiert werden kann. Ebenso darf
     * eine Variable nicht 0 sein, da im DIMACS CNF FORMAT das Symbol 0 zum Kodieren eines Zeilenendes benutzt wird.
     *
     * @return Die Nummer der neuen Variablen.
     */
    public int create_var() {
        _nVars++;
        return _nVars;
    }

    /**
     * Erzeugt mehrere Variablen auf einmal und liefert ein Array mit diesen zurück. <br>
     * Siehe auch: {@link SatInput#create_var()}
     *
     * @param n die Anzahl an zu erzeugenden Variablen.
     *
     * @return ein Array mit den neuen Variablen.
     */
    public @NotNull int[] create_vars1D(final int n) {
        final @NotNull int @NotNull [] temp = new int[n];
        for (int i = 0; i < temp.length; i++)
            temp[i] = create_var();
        return temp;
    }

    /**
     * Erzeugt mehrere Variablen auf einmal und liefert ein Array mit diesen zurück. <br>
     * Siehe auch: {@link SatInput#create_var()}
     *
     * @param rows die Anzahl an Zeilen eines 2D-Arrays.
     * @param cols die Anzahl an Spalten eines 2D-Arrays.
     *
     * @return ein Array mit den neuen Variablen.
     */
    public @NotNull int @NotNull [][] create_vars2D(final int rows, final int cols) {
        final @NotNull int @NotNull [] @NotNull [] temp = new int[rows][cols];
        for (int r = 0; r < rows; r++)
        	temp[r] = create_vars1D(cols);
        return temp;
    }

	/**
	 * Liefert die neu erzeugte Variable z für die 'z = x AND y' gilt.
	 *
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 *
	 * @return die neu erzeugte Variable z für die 'z = x AND y' gilt.
	 */
	public int create_var_AND(final int x, final int y) {
		final int c = create_var();
		add_clause_2(x, -c);
		add_clause_2(y, -c);
		add_clause_3(-x, -y, c);
		return c;
	}

	/**
	 * Liefert die neu erzeugte Variable z für die 'z = x OR y' gilt.
	 *
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 *
	 * @return die neu erzeugte Variable z für die 'z = x OR y' gilt.
	 */
	public int create_var_OR(final int x, final int y) {
		final int z = create_var();
		add_clause_2(-x, z);
		add_clause_2(-y, z);
		add_clause_3(x, y, -z);
		return z;
	}


    /**
	 * Forciert, dass in der Liste maximal eine Variable TRUE ist.
	 * Die Ergebnisvariable ist eine OR-Verknüpfung aller Variablen der Liste.
	 *
	 * @param pList Forciert, dass maximal eine Variable der Liste TRUE ist.
	 *
	 * @return Die Ergebnisvariable ist eine OR-Verknüpfung aller Variablen der Liste.
	 */
	public int create_var_at_most_one_tree(final @NotNull LinkedCollection<@NotNull Integer> pList) {
		// Sonderfall: Wenn die Liste leer ist, dann ist die OR-Verknüpfung die Konstante FALSE.
		if (pList.isEmpty())
			return getVarFALSE();

		// Liste kopieren
		final @NotNull LinkedCollection<@NotNull Integer> list = new LinkedCollection<>(pList);

		// Solange es zwei Variablen gibt
		while (list.size() >= 2) {
			final int a = list.removeFirst();
			final int b = list.removeFirst();
			add_clause_not_both(a, b);
			final int c = create_var_OR(a, b);
			list.addLast(c);
		}

		// Wurzel
		return list.removeFirst();
	}

	/**
     * Hinzufügen einer Klausel. Eine Klausel ist eine nicht leere Menge von Variablen,
     * die mit einem logischen ODER verknüpft sind. Die Variablen dürfen negiert sein. <br>
     * <pre>
     * Das Array [-3, 8, 2]
     * wird als  (NOT x3) OR x8 OR x2 interpretiert.
     * </pre>
     * Die Menge aller Klauseln sind mit einem AND verknüpft.
     *
     * @param pVars Die Variablen (auch negiert) der Klausel mit den zuvor generierten Variablen.
     *
     * @throws DeveloperNotificationException falls die angegebenen Variablen ungültig sind.
     */
    public void add_clause(final @NotNull Integer @NotNull[] pVars) throws DeveloperNotificationException {
        DeveloperNotificationException.ifTrue("Die Klausel darf nicht leer sein!", pVars.length == 0);

        for (final int literal : pVars) {
            DeveloperNotificationException.ifTrue("Variable 0 ist nicht erlaubt!", literal == 0);
            final int absL = Math.abs(literal);
            DeveloperNotificationException.ifTrue("Variable " + absL + " wurde vorher nicht erzeugt!", absL > _nVars);
        }

        _clauses.add(pVars);
    }

    /**
     * Fügt eine Klausel der Größe 1 hinzu. Forciert damit die übergebene Variable auf TRUE.
     *
     * @param x Die Variable wird auf TRUE gesetzt.
     */
    public void add_clause_1(final int x) {
        add_clause(new @NotNull Integer @NotNull[] {x});
    }

	/**
     * Fügt eine Klausel der Größe 2 hinzu. Forciert damit, dass mindestens eine der beiden Variablen TRUE ist.
     *
     * @param x Die Variable x der Klausel (x OR y).
     * @param y Die Variable y der Klausel (x OR y).
     */
    public void add_clause_2(final int x, final int y) {
        add_clause(new @NotNull Integer @NotNull[] {x, y});
    }

    /**
     * Fügt eine Klausel der Größe 3 hinzu. Forciert damit, dass mindestens eine der drei Variablen TRUE ist.
     *
     * @param x Die Variable x der Klausel (x OR y OR z).
     * @param y Die Variable y der Klausel (x OR y OR z).
     * @param z Die Variable z der Klausel (x OR y OR z).
     */
    public void add_clause_3(final int x, final int y, final int z) {
        add_clause(new @NotNull Integer @NotNull[] {x, y, z});
    }

    /**
     * Forciert, dass nicht beide Variablen TRUE sind.
     *
     * @param x Die Variable x der Klausel (-x OR -y).
     * @param y Die Variable y der Klausel (-x OR -y).
     */
    public void add_clause_not_both(final int x, final int y) {
        add_clause_2(-x, -y);
    }

    /**
     * Forciert, dass beide Variablen gleich sind.
     *
     * @param x Die Variable x der Bedingung (x = y).
     * @param y Die Variable y der Bedingung (x = y).
     */
    public void add_clause_equal(final int x, final int y) {
    	add_clause_2(-x, +y);
    	add_clause_2(+x, -y);
	}

    /**
     * Forciert, dass beide Variablen ungleich sind.
     *
     * @param x Die Variable x der Bedingung (x != y).
     * @param y Die Variable y der Bedingung (x != y).
     */
    public void add_clause_unequal(final int x, final int y) {
    	add_clause_equal(x, -y);
	}

	/**
	 * Forciert, dass genau {@code pAmount} Variablen der Matrix {@code pData} in Zeile {@code pRow} den Wert TRUE haben.
	 *
	 * @param pData   Die Matrix.
	 * @param pRow    Die Zeile der Matrix.
	 * @param pAmount Die Anzahl an TRUEs.
	 */
	public void add_clause_exactly_in_row(final @NotNull int @NotNull [] @NotNull [] pData, final int pRow, final int pAmount) {
		final @NotNull LinkedCollection<@NotNull Integer> pList = new  LinkedCollection<>();
		for (int c = 0; c < pData[pRow].length; c++)
			pList.add(pData[pRow][c]);
		add_clause_exactly(pList, pAmount);
	}

	/**
	 * Forciert, dass genau {@code pAmount} Variablen der Matrix {@code pData} in Spalte {@code pCol} den Wert TRUE haben.
	 *
	 * @param pData   Die Matrix.
	 * @param pCol    Die Spalte der Matrix.
	 * @param pAmount Die Anzahl an TRUEs.
	 */
	public void add_clause_exactly_in_column(final @NotNull int @NotNull [] @NotNull [] pData, final int pCol, final int pAmount) {
		final @NotNull LinkedCollection<@NotNull Integer> pList = new  LinkedCollection<>();
		for (int r = 0; r < pData.length; r++)
			pList.add(pData[r][pCol]);
		add_clause_exactly(pList, pAmount);
	}

	/**
	 * Forciert, dass genau {@code pAmount} Variablen der Variablenliste den Wert TRUE haben.
	 *
	 * @param pList   Die Variablenliste.
	 * @param pAmount Die Anzahl an TRUEs in der Variablenliste.
	 */
	public void add_clause_exactly(final @NotNull LinkedCollection<@NotNull Integer> pList, final int pAmount) {
		// Liste kopieren
		final @NotNull LinkedCollection<@NotNull Integer> list = new LinkedCollection<>(pList);

		// Datenkonsistenz überprüfen.
		final int size = list.size();
		DeveloperNotificationException.ifTrue("add_clause_exactly: " + pAmount + " > " + size, pAmount > size);


		// Spezialfall: Genau 0 --> Alles FALSE
		if  (pAmount == 0) {
			for (final int x : list)
				add_clause_1(-x);
			return;
		}

		// Spezialfall: Genau N --> Alles TRUE
		if (pAmount == size) {
			for (final int x : list)
				add_clause_1(+x);
			return;
		}

		// Spezialfall: Genau 1
		if (pAmount == 1) {
			add_clause_exactly_one(list);
			return;
		}

		// Andernfalls muss ein Sortiernetzwerk aufgebaut werden.
		_bitonic_exactly(list, pAmount);
	}

	/**
	 * Forciert, dass genau eine Variable der Liste TRUE ist. <br>
	 * Falls die Liste leer ist, führt das zur direkten Unlösbarkeit der Formel.
	 *
	 * @param pList Menge an Variablen von denen genau eine TRUE sein soll.
	 */
	private void add_clause_exactly_one(final @NotNull LinkedCollection<@NotNull Integer> pList) {
		final int size = pList.size();

		// Spezialfall: Unlösbar
		if (size == 0) {
			add_clause_1(getVarFALSE());
			return;
		}

		// Spezialfall: TRUE
		if (size == 1) {
			add_clause_1(pList.getFirst());
			return;
		}

		// Spezialfall: UNEQUAL
		if (size == 2) {
			add_clause_unequal(pList.getFirst(), pList.getLast());
			return;
		}

		// Andernfalls
		final int x = create_var_at_most_one_tree(pList);
		add_clause_1(x);
	}

	private void _bitonic_exactly(final @NotNull LinkedCollection<@NotNull Integer> list, final int amount) {
		// Sortieren
		_bitonic_sort(list);

		// Forciere TRUE / FALSE in richtiger Anzahl
		int i = 0;
		final @NotNull Iterator<@NotNull Integer> iter = list.iterator();
		while (iter.hasNext()) {
			final @NotNull
			Integer value = iter.next();
			if (i < amount) {
				add_clause_1(+value);
			} else {
				add_clause_1(-value);
			}
			i++;
		}
	}

	private void _bitonic_sort(final @NotNull LinkedCollection<@NotNull Integer> list) {
		_bitonic_fill_FALSE_until_power_two(list);
		_bitonic_sort_power_two(list);
	}

	private void _bitonic_fill_FALSE_until_power_two(final @NotNull LinkedCollection<@NotNull Integer> list) {
		int size = 1;
		while (size < list.size())
			size *= 2;

		while (list.size() < size)
			list.addLast(getVarFALSE());
	}

	private void _bitonic_sort_power_two(final @NotNull LinkedCollection<@NotNull Integer> list) {
		for (int window = 2; window <= list.size(); window *= 2) {
			_bitonic_sort_spiral(list, window);
			for (int difference = window / 2; difference >= 2; difference /= 2)
				_bitonic_sort_difference(list, difference);
		}
	}

	private void _bitonic_sort_spiral(final @NotNull LinkedCollection<@NotNull Integer> list, final int size) {
		for (int i = 0; i < list.size(); i += size)
			for (int i1 = i, i2 = i + size - 1; i1 < i2; i1++, i2--)
				_bitonic_comparator(list, i1, i2);
	}

	private void _bitonic_sort_difference(final @NotNull LinkedCollection<@NotNull Integer> list, final int size) {
		final int half = size / 2;
		for (int i = 0; i < list.size(); i += size)
			for (int j = 0; j < half; j++)
				_bitonic_comparator(list, i + j, i + j + half);
	}

	private void _bitonic_comparator(final @NotNull LinkedCollection<@NotNull Integer> result, final int i1, final int i2) {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("c_bitonic_comparator: i1=" + i1 + " nicht kleiner als i2=" + i2 + "!", i1 >= i2);

		// Indizes durch OR/AND Verknüpfung ersetzen.
		final int a = result.get(i1);
		final int b = result.get(i2);
		result.set(i1, create_var_OR(a, b));
		result.set(i2, create_var_AND(a, b));
	}

	/**
	 * Überprüft, ob die übergebene Lösung valide ist.
	 *
	 * @param solution Die übergebene Lösung.
	 * @return TRUE, falls die Lösung alle Klauseln des Inputs erfüllt.
	 */
	public boolean isValidSolution(final @NotNull int @NotNull[] solution) {
		DeveloperNotificationException.ifTrue("Arraygröße " + solution.length + " passt nicht zur Variablenanzahl " + _nVars + "!", solution.length != _nVars + 1);

		for (final @NotNull Integer @NotNull [] clause : _clauses) {
			int countTRUE = 0;

			for (final @NotNull int literal : clause) {
				final int abs = Math.abs(literal);
				final int assignment = solution[abs];
				DeveloperNotificationException.ifTrue("x_" + abs + " == 0", assignment == 0);
				if (assignment == literal)
					countTRUE++;
			}

			if (countTRUE == 0)
				return false;
		}

		return true;
	}

}
