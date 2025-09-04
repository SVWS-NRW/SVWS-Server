package de.svws_nrw.module.reporting.sortierung;

import java.util.function.Function;

/**
 * Die Klasse FunktionenBuilder stellt Methoden zur Verfügung, um Transformationen
 * und Verarbeitungsschritte für Wertermittlungsfunktionen bei der Sortierung zu erzeugen.
 * Diese Klasse ist nicht instanziierbar und dient ausschließlich als Utility-Klasse.
 * Mit ihr können null-sichere Verarbeitungsfunktionen erstellt und verkettet werden.
 */
public final class FunktionBuilder {
	private FunktionBuilder() {
	}

	/**
	 * Gibt eine neue Teilfunktion für eine zusammengesetze Wertermittlungsfunktion bei der Sortierung zurück. Diese kapselt die Identitätsfunktion.
	 * Diese Funktion gibt den Eingabewert unverändert zurück.
	 *
	 * @param <T> Der Typ des Eingabewerts und des Rückgabewerts
	 * @return Ein neuer Sortierschritt, der die Identitätsfunktion repräsentiert
	 */
	public static <T> Teilfunktion<T, T> identity() {
		return new Teilfunktion<>(t -> t);
	}

	/**
	 * Startet einen null-sicheren Sortierpfad mit einer ersten Getter-/Extraktionsfunktion.
	 * Dies ist der Einstiegspunkt für verschachtelte Sortierkriterien. Ab hier können weitere Getter mit then(...) angehängt werden (z. B.
	 * auswahlLernabschnitt → klasse → kuerzel). Ist das Eingabeobjekt null, liefert der Pfad null als Sortierschlüssel, wodurch der Comparator
	 * (z. B. nullsLast) das finale Verhalten bestimmt.
	 * Beispiel:
	 * Comparator<ReportingSchueler> cmp = Comparator.comparing(Sortierungsfunktion.start(Schueler::auswahlLernabschnitt).then(Lernabschnitt::klasse).then(Klasse::kuerzel).toFunction(), Comparator.nullsLast(deutscherCollator));
	 *
	 * @param <T> Der Typ des Eingabeobjekts der Transformation
	 * @param <A> Der Typ des Ausgabeobjekts nach der Transformations
	 * @param f1  Erste Getter-/Transformationsfunktion (z. B. obj -> obj.getAuswahlLernabschnitt())
	 * @return Ein neuer Sortierschritt, der die angegebene Transformation null-sicher ausführt
	 */
	public static <T, A> Teilfunktion<T, A> start(final Function<? super T, ? extends A> f1) {
		return new Teilfunktion<>(t -> {
			if (t == null)
				return null;
			return f1.apply(t);
		});
	}

	/**
	 * Die Klasse Sortierschritt dient als Baustein für die Erstellung einer transformierenden
	 * Sortierlogik. Sie kapselt Transformationen einer Eingabe in ein Ausgabeformat, das
	 * als Sortierschlüssel verwendet werden kann. Die Klasse unterstützt die Verkettung
	 * mehrerer Transformationsschritte und garantiert dabei Null-Sicherheit bei der
	 * Verarbeitung der Transformationskette.
	 *
	 * @param f Transformationsfunktion, die aus einer Eingabe vom Typ T einen (möglicherweise nullbaren) Sortierschlüssel vom Typ R erzeugt. Diese Funktion
	 * bildet die Grundlage für die Verkettung in then(...). Die Rückgabe erfolgt später über toFunction().
	 * @param <T> Der Typ der Eingabe, die transformiert wird
	 * @param <R> Der Typ des Ergebnisses nach der Anwendung der Transformation
	 */
	public record Teilfunktion<T, R>(Function<T, R> f) {

		/**
		 * Verkettet die aktuelle Transformationsfunktion mit einer weiteren Funktion, um einen
		 * Sortierschlüssel aus einem verschachtelten Attribut zu ermitteln. Die Null-Semantik
		 * bleibt hierbei erhalten: Wenn das Ergebnis der bisherigen Schritte {@code null} ist,
		 * wird {@code null} zurückgegeben, ohne die nächste Funktion auszuführen.
		 *
		 * @param <S> Der Typ des Sortierschlüssels, der durch die zusätzliche Funktion ermittelt wird
		 * @param next Die nächste Funktion, die auf das Zwischenergebnis angewendet werden soll
		 * @return Ein neuer Sortierschritt, der die bisher definierten Schritte mit der übergebenen
		 *         Funktion verkettet
		 */
		public <S> Teilfunktion<T, S> then(final Function<? super R, ? extends S> next) {
			return new Teilfunktion<>(t -> {
				final R r = f.apply(t);
				if (r == null)
					return null;
				return next.apply(r);
			});
		}

		/**
		 * Gibt die aktuell konfigurierte Funktion zurück, die den Sortierschlüssel eines
		 * Eingabeobjekts ermittelt.
		 *
		 * @return Eine Funktion, die basierend auf den definierten Schritten den
		 *         Sortierschlüssel für ein gegebenes Eingabeobjekt ermittelt
		 */
		public Function<T, R> toFunction() {
			return f;
		}
	}

	/**
	 * Nimmt eine Reihe von Funktionen und versucht, den ersten nicht-{@code null}
	 * Rückgabewert zurückzugeben, den eine der Funktionen liefert. Falls alle
	 * Funktionen {@code null} zurückgeben, wird {@code null} zurückgegeben.
	 * Beispiel: registry.registiereString("klasseMitFallback",
	 * 				  FunktionBuilder.firstNonNull(
	 * 						FunktionBuilder.start(ReportingSchueler::auswahlLernabschnitt)
	 * 								.then(ReportingSchuelerLernabschnitt::klasse)
	 * 								.then(ReportingKlasse::kuerzel)
	 * 								.toFunction(),
	 * 						FunktionBuilder.start(ReportingSchueler::aktuellerLernabschnitt)
	 * 								.then(ReportingSchuelerLernabschnitt::klasse)
	 * 								.then(ReportingKlasse::kuerzel)
	 * 								.toFunction())
	 * @param <T> Der Typ des Eingabewerts, den die Funktionen akzeptieren
	 * @param <R> Der Typ des Rückgabewerts, den die Funktionen liefern
	 * @param alternativfunktionen Die Funktionen, die nacheinander ausgewertet werden, bis eine Funktion einen nicht-{@code null} Rückgabewert liefert
	 * @return Eine Funktion, die den ersten nicht-{@code null} Rückgabewert der
	 *         gegebenen Funktionen zurückgibt oder {@code null}, falls alle
	 *         Funktionen {@code null} zurückgeben
	 */
	@SafeVarargs
	public static <T, R> Function<T, R> firstNonNull(final Function<T, R>... alternativfunktionen) {
		return t -> {
			for (final Function<T, R> f : alternativfunktionen) {
				final R r = f.apply(t);
				if (r != null)
					return r;
			}
			return null;
		};
	}
}
