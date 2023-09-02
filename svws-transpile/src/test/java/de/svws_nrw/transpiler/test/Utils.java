package de.svws_nrw.transpiler.test;

import java.util.function.Function;

import jakarta.validation.constraints.NotNull;


/**
 * Eine Klasse mit mehreren statischen Methoden zum Testen von
 * Transpiler-Fähigkeiten.
 */
public class Utils {

	/**
	 * Wendet die übergenene Aufgabe auf die übergebene Eingabe an.
	 *
	 * @param task    die Aufgabe
	 * @param input   die Eingabe
	 *
	 * @return das Ergebnis
	 */
	public static int runTask(@NotNull final Function<@NotNull Long, @NotNull Integer> task, final long input) {
		return task.apply(input);
	}

	/**
	 * Testet den Aufruf von runTask.
	 */
	public static void testRunTask() {
		final long a = 5;
		final int b = runTask(input -> input.intValue(), a);
		if (a != b)
			System.out.println("Fehler: Die Werte sind nicht gleich!");
	}

}
