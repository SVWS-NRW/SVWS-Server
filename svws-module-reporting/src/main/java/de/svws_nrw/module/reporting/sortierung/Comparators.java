package de.svws_nrw.module.reporting.sortierung;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * Eine Hilfsklasse zur Erstellung und Nutzung von vordefinierten und zusammengesetzten {@link Comparator} Instanzen.
 * Diese Comparatoren erlauben die flexible und mehrschichtige Sortierung von Objekten und unterstützen sowohl
 * Strings mit deutschen Lokalisierungseinstellungen als auch generische Werte, die das Interface {@link Comparable}
 * implementieren.
 * Die Klasse ist unveränderlich und kann nicht instanziiert werden.
 */
public final class Comparators {
	private Comparators() {
	}

	/**
	 * Eine statische Instanz der Klasse {@link Collator}, die mit den deutschen Lokalisierungseinstellungen
	 * ({@link Locale#GERMAN}) erstellt wurde. Diese Instanz wird zur Zeichenfolgensortierung unter Berücksichtigung
	 * der deutschen sprachlichen Sortierreihenfolge verwendet.
	 *
	 * Die Sortierung erfolgt standardmäßig mit sekundärer Stärke. Dabei wird die Groß-/Kleinschreibung
	 * ignoriert und diakritische Zeichen wie Umlaute werden korrekt berücksichtigt.
	 */
	private static final Collator collatorDeutsch = Collator.getInstance(Locale.GERMAN);
	static {
		collatorDeutsch.setStrength(Collator.SECONDARY);
	}

	/**
	 * Erstellt einen {@link Comparator} für Strings basierend auf der deutschen Spracheinstellung.
	 * Die Sortierung erfolgt unter Beachtung der sekundären Stärke (z. B. wird die Groß-/Kleinschreibung
	 * ignoriert, Umlaute werden korrekt berücksichtigt). Null-Werte werden dabei zuletzt sortiert.
	 *
	 * @return Ein {@link Comparator}, der Strings gemäß der deutschen Sprachregelung sortiert,
	 *         wobei {@code null}-Werte zuletzt eingeordnet werden.
	 */
	public static Comparator<String> stringComparatorDeutsch() {
		return Comparator.nullsLast(collatorDeutsch::compare);
	}

	/**
	 * Erstellt einen {@link Comparator} für Objekte des Typs {@code T}, basierend auf
	 * einem String-Wert, der durch die angegebene Funktion geliefert wird. Die Sortierung
	 * erfolgt in aufsteigender oder absteigender Reihenfolge, abhängig vom angegebenen Flag.
	 *
	 * @param <T> Der Typ der Objekte, die verglichen werden sollen
	 * @param wertermittlungsfunktion Eine Funktion, die für ein Objekt vom Typ {@code T} ein {@code String}-Feld extrahiert,
	 *               welches für die Sortierung verwendet wird
	 * @param sortiereAufsteigend   Ein {@code boolean}-Flag, das bestimmt, ob in aufsteigender ({@code true})
	 *              oder absteigender ({@code false}) Reihenfolge sortiert werden soll
	 * @return Ein {@link Comparator}, der Objekte des Typs {@code T} unter Verwendung des
	 *         extrahierten Strings sortiert, abhängig von der angegebenen Sortierrichtung
	 */
	public static <T> Comparator<T> stringComparator(final Function<T, String> wertermittlungsfunktion, final boolean sortiereAufsteigend) {
		final Comparator<T> cmp = Comparator.comparing(wertermittlungsfunktion, stringComparatorDeutsch());
		return sortiereAufsteigend ? cmp : cmp.reversed();
	}

	/**
	 * Erstellt einen {@link Comparator} für Objekte des Typs {@code T}, basierend auf
	 * einem Wert vom Typ {@code U}, der durch die angegebene Funktion extrahiert wird.
	 * Der zu vergleichende Wert muss das Interface {@link Comparable} implementieren.
	 * Null-Werte werden zuletzt sortiert. Die Sortierung kann in aufsteigender oder,
	 * falls angegeben, absteigender Reihenfolge erfolgen.
	 *
	 * @param <T> Der Typ der Objekte, die verglichen werden sollen
	 * @param <U> Der Typ des von {@code wertermittlungsfunktion} extrahierten Wertes, der verglichen wird.
	 *             Dieser Typ muss {@link Comparable} implementieren.
	 * @param wertermittlungsfunktion Eine Funktion, die für ein Objekt vom Typ {@code T} einen
	 *               vergleichbaren Wert vom Typ {@code U} extrahiert
	 * @param sortiereAufsteigend    Ein {@code boolean}-Flag, das angibt, ob in aufsteigender
	 *               ({@code true}) oder absteigender ({@code false}) Reihenfolge
	 *               sortiert werden soll
	 * @return Ein {@link Comparator}, der Objekte des Typs {@code T} anhand des extrahierten
	 *         vergleichbaren Wertes sortiert, abhängig von der angegebenen Sortierrichtung
	 */
	public static <T, U extends Comparable<? super U>> Comparator<T> comparableComparator(final Function<T, U> wertermittlungsfunktion, final boolean sortiereAufsteigend) {
		final Comparator<T> cmp = Comparator.comparing(wertermittlungsfunktion, Comparator.nullsLast(Comparator.naturalOrder()));
		return sortiereAufsteigend ? cmp : cmp.reversed();
	}

	/**
	 * Verkettet eine Liste von {@link Comparator} Objekten, sodass sie nacheinander zur Sortierung
	 * verwendet werden. Jeder übergebene {@link Comparator} wird dabei in der Reihenfolge
	 * der Iteration berücksichtigt.
	 *
	 * @param <T> Der Typ der Objekte, die verglichen werden sollen
	 * @param comparators Eine Liste von {@link Comparator} Objekten, die verkettet werden sollen
	 * @return Ein {@link Comparator}, der die übergebenen Comparatoren in der definierten Reihenfolge verwendet
	 */
	public static <T> Comparator<T> verketten(final List<Comparator<T>> comparators) {
		Comparator<T> result = (a, b) -> 0;
		for (final Comparator<T> c : comparators)
			result = result.thenComparing(c);
		return result;
	}
}
