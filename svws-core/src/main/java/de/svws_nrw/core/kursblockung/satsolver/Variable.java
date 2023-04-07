package de.svws_nrw.core.kursblockung.satsolver;

import java.util.Random;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Variable führt Statistik über ihr Vorkommen in Klauseln. Zudem ist jede Variable, die noch nicht auf TRUE oder
 * FALSE gesetzt wurde, in einem Heap.
 *
 * @author Benjamin A. Bartsch
 */
public class Variable {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected final @NotNull Random _random;

	/**
	 * Die Nummer der Variablen.
	 */
	final int nr;

	/**
	 * Eine Statistik [sat][free] über Klauseln die nicht definierte Variablen haben und die schon erfüllte Variablen
	 * haben.
	 */
	final @NotNull int @NotNull [][] statSatFree;

	/**
	 * Liste aller Klauseln in denen diese Variable vorkommt.
	 */
	final @NotNull LinkedCollection<@NotNull Clause> clauses;

	/**
	 * Liste aller Variablen, die mit dieser Variable in einer Klausel vorkommen.
	 */
	final @NotNull LinkedCollection<@NotNull Variable> neighbours;

	/**
	 * Der Index der Variablen im Heap. Wenn der Index negativ ist, dann ist die Variable nicht mehr im Heap. Zudem
	 * bedeutet der Wert -1, dass die Variable SAT ist und der Wert -2 UNSAT. Ein Wert kleiner als -2 ist nur ein
	 * Dummy-Wert.
	 */
	int index;

	/**
	 * Eine Referenz zur negierten Variable.
	 */
	Variable negation;

	/**
	 * Konstruktor. Erzeugt eine neue Variable mit einer bestimmten Variablen-Nummer (ungleich 0).
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pNr     Die Nummer der Variablen (ungleich 0).
	 */
	public Variable(final @NotNull Random pRandom, final int pNr) {
		_random = pRandom;
		nr = pNr;
		statSatFree = new int[4][4];
		clauses = new LinkedCollection<>();
		neighbours = new LinkedCollection<>();
		index = -3; // Dummy Wert,
		negation = null;
	}

	/**
	 * Gibt die String-Repräsentation der Variable zurück.
	 *
	 * @return die String-Repräsentation der Veriable
	 */
	@Override
	public @NotNull String toString() {
		return "" + nr;
	}

	/**
	 * Überprüft, ob diese Variable noch auf TRUE gesetzt werden kann.
	 *
	 * @return TRUE, falls man diese Variable und einen logischen Widerspruch erfüllen kann.
	 */
	public boolean isUnsat() {
		// Es existiert eine nicht erfüllt (leere) Klausel.
		if (statSatFree[0][0] > 0) {
			System.out.println("FEHLER: Dieser Fall darf gar nicht passieren.");
			return true;
		}
		// Negation ist in einer 1-CNF-Klausel --> diese Variable darf nicht TRUE sein.
		return ((negation != null) && (negation.statSatFree[0][1] > 0));
	}

	/**
	 * Vergleicht die Statistik zweier Variablen und bestimmt, für welche man sich entscheiden sollte.
	 *
	 * @param b Die Variable, mit der verglichen werden soll.
	 *
	 * @return TRUE, wenn diese Instanz besser als "b" ist.
	 */
	public boolean isBetterThan(final @NotNull Variable b) {
		final @NotNull
		int @NotNull [][] statB = b.statSatFree;

		// #### SAT=0 ###

		// ... und FREE=0 (Formel hat einen Widerspruch)
		if (statSatFree[0][0] > statB[0][0])
			return true;
		if (statSatFree[0][0] < statB[0][0])
			return false;

		// ... und FREE=1 (Variable in mehr 1-CNF)
		if (statSatFree[0][1] > statB[0][1])
			return true;
		if (statSatFree[0][1] < statB[0][1])
			return false;

		// ... und FREE=2 (Variable in mehr 2-CNF)
		if (statSatFree[0][2] > statB[0][2])
			return true;
		if (statSatFree[0][2] < statB[0][2])
			return false;

		// ... und FREE=3 (Variable in mehr 3-CNF)
		if (statSatFree[0][3] > statB[0][3])
			return true;
		if (statSatFree[0][3] < statB[0][3])
			return false;

		// Letzte Priorität ist die Variablennummer.
		/*
		 * if (nr < b.nr) return true; if (nr > b.nr) return false;
		 */
		return _random.nextBoolean();
	}

	/**
	 * Debug-Ausgabe. Nur für Testzwecke.
	 */
	public void debug() {
		System.out.println("DEBUGGING VAR " + nr);
		for (int r = 0; r < statSatFree.length; r++) {
			for (int c = 0; c < statSatFree[r].length; c++) {
				System.out.print(" " + statSatFree[r][c]);
			}
			System.out.print("    ");
			if (negation != null) {
				for (int c = 0; c < negation.statSatFree[r].length; c++) {
					System.out.print(" " + negation.statSatFree[r][c]);
				}
			}
			System.out.println();
		}
	}

	/**
	 * Liefert die Anzahl an noch nicht erfüllten Klauseln.
	 *
	 * @return Die Anzahl an noch nicht erfüllten Klauseln.
	 */
	int getClauseOccurences() {
		int sum = 0;
		for (int free = 0; free < 4; free++) {
			sum += statSatFree[0][free];
		}
		return sum;
	}

}
