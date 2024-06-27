package de.svws_nrw.transpiler.test;

import java.util.function.Function;

import jakarta.validation.constraints.NotNull;


/**
 * Eine Klasse mit mehreren statischen Methoden zum Testen von
 * Transpiler-Fähigkeiten.
 */
public class Utils {

	private static final @NotNull Runnable runnable = () -> {
		/* nicht zu tun */
	};

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

//	/**
//	 * Testet das Transpilieren einer Switch-Anweisungen
//	 *
//	 * @param sf   eine Schulform
//	 *
//	 * @return ein String
//	 */
//	public static String switchByTyp(final @NotNull Schulform sf) {
//		final @NotNull String value = switch (sf) {
//			case GY -> {
//				final var tmp = sf.daten(2018);
//				yield tmp == null ? null : tmp.kuerzel;
//			}
//			case GE -> {
//				final var tmp = sf.daten(2018);
//				yield tmp == null ? null : tmp.kuerzel;
//			}
//			default -> "none";
//		};
//		return value;
//	}

	/**
	 * Testet das Transpilieren der neuen Switch-Pattern-Anweisungen
	 *
	 * @param data   ein Basis-Typ für das Testen
	 *
	 * @return ein String
	 */
	public static String switchByType(final @NotNull CoreTypeData data) {
		return switch (data) {
			case @NotNull final SchulformKatalogEintrag sfke -> "Schulform: " + sfke.kuerzel;
			case @NotNull final SchulstufeKatalogEintrag sske -> "Schulstufe: " + sske.kuerzel;
			default -> {
				yield "None";
			}
		};
	}


	/**
	 * Gibt den Wert des übergebenen Objektes auf der Konsole aus.
	 *
	 * @param <T> der Typ des Wertes
	 *
	 * @param o   das Objekt
	 */
	public static <@NotNull T> void printObjectValue(final ClassWithParameter<@NotNull T> o) {
		System.out.println(o.getValue().toString());
	}


	/**
	 * Eine Test-Methode, für das Erzeugen einer Instanz einer generischen Klasse
	 */
	public static void testPrintObjectValue() {
		printObjectValue(new ClassWithParameter<>(5));
	}



}
