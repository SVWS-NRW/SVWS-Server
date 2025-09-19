package de.svws_nrw.module.reporting.sortierung;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Verwaltung erlaubter Attribute bzw. Attributspfade und Bereitstellung passender Comparatoren (inkl. Sortierrichtung)
 * für die Sortierung von Reporting-Objekten.
 * Mit dieser KLasse können Attributnamen (z. B. "nachname" oder "auswahlLernabschnitt.klasse.kuerzel") registriert werden
 * und sie ordnet ihnen typsichere Wertermittlungsfunktionen (Extraktorfunktionen) zu.
 * Zudem liefert sie für einen angefragten Attributnamen einen Comparator<T> in gewünschter Richtung,
 * basierend auf String-/Comparable-Vergleich (inkl. Null-Handling über Comparators).
 * Die Namen werden beim Registrieren und Suchen normalisiert (trim + toLowerCase), Zugriff ist case-insensitiv.
 * Hinweis: Sortierreihenfolgen werden über typsichere Funktionen (z. B. mit Sortierungsfunktion.start(...).then(...)) aufgebaut.
 *
 * @param <T> Der Typ, der sortiert werden soll.
 */
public final class SortierungRegistry<T> {

	/**
	 * Kennzeichnet den Typ der registrierten Werte (String vs. Comparable), damit die passende Vergleichsstrategie gewählt werden kann.
	 */
	public enum ValueType {
		/**
		 * Werte werden als Strings behandelt.
		 */
		STRING,
		/**
		 * Werte werden als Comparable behandelt.
		 */
		COMPARABLE
	}

	/**
	 * Repräsentiert einen Eintrag in der {@code AttributeRegistry}, der ein Attribut beschreibt.
	 * Jeder Eintrag besteht aus einer Extraktionsfunktion, die den entsprechenden Wert aus einem Objekt ableitet,
	 * sowie dem zugehörigen Werttyp, der angibt, wie das Attribut behandelt werden soll
	 * (z. B. als String oder als Comparable).
	 *
	 * @param <T> Der Typ des Objekts, aus dem die Werte extrahiert werden.
	 * @param wertermittlungsfunktion Die Funktion, die den Wert aus einem Objekt vom Typ {@code T} extrahiert.
	 * @param werttype Der Typ des Werts, der kennzeichnet, wie das Attribut behandelt wird (z. B. {@code STRING} oder {@code COMPARABLE}).
	 */
	public record Attributeintrag<T>(Function<T, ?> wertermittlungsfunktion, ValueType werttype) {
	}


	/**
	 * Eine Map, die Attribute mit ihren zugehörigen Einträgen speichert. Die Schlüssel der Map
	 * sind die Namen der Attribute als Strings und die Werte sind {@code Entry}-Objekte,
	 * die Funktionalitäten zur Extraktion und Typinformationen definieren.
	 * Diese Map wird verwendet, um die registrierten Attribute mit ihren
	 * Extraktions- und Vergleichsstrategien zentral zu verwalten.
	 */
	private final Map<String, Attributeintrag<T>> map = new HashMap<>();

	/**
	 * Registriert ein Attribut, das als String behandelt werden soll, in der Registry.
	 * Der gegebene Name wird normalisiert, und eine Funktion wird gespeichert, die den String-Wert aus einem Objekt extrahiert.
	 *
	 * @param attributName Der Name des Attributs, das registriert werden soll. Dieser wird normalisiert.
	 * @param wertermittlungsfunktion Eine Funktion, die für ein gegebenes Objekt den entsprechenden String-Wert extrahiert.
	 * @return Die aktuelle Instanz der {@code AttributeRegistry}, um method chaining zu ermöglichen.
	 */
	public SortierungRegistry<T> registiereString(final String attributName, final Function<T, String> wertermittlungsfunktion) {
		map.put(normalisieren(attributName), new Attributeintrag<>(wertermittlungsfunktion, ValueType.STRING));
		return this;
	}

	/**
	 * Registriert ein Attribut, das als {@link Comparable} behandelt werden soll, in der Registry.
	 * Der gegebene Name wird normalisiert, und eine Funktion wird gespeichert, die den
	 * vergleichbaren Wert aus einem Objekt extrahiert.
	 *
	 * @param <U> Der Typ des vergleichbaren Werts, der vom Typ {@link Comparable} implementiert werden muss.
	 * @param attributName Der Name des Attributs, das registriert werden soll. Dieser wird normalisiert.
	 * @param wertermittlungsfunktion Eine Funktion, die für ein gegebenes Objekt den entsprechenden
	 *                  vergleichbaren Wert extrahiert.
	 * @return Die aktuelle Instanz der {@code AttributeRegistry}, um method chaining zu ermöglichen.
	 */
	public <U extends Comparable<? super U>> SortierungRegistry<T> registiereComparable(final String attributName,
			final Function<T, U> wertermittlungsfunktion) {
		map.put(normalisieren(attributName), new Attributeintrag<>(wertermittlungsfunktion, ValueType.COMPARABLE));
		return this;
	}

	/**
	 * Sucht nach einem registrierten Attribut in der Registry anhand des übergebenen Namens.
	 * Der Name wird vor der Suche normalisiert. Falls ein entsprechender Eintrag gefunden wird,
	 * liefert die Methode ihn als {@code Optional}. Andernfalls wird ein leeres {@code Optional}
	 * zurückgegeben.
	 *
	 * @param attributName Der Name des Attributs, das gesucht wird. Dieser wird vor der Suche normalisiert.
	 * @return Ein {@code Optional}, das den Eintrag des gesuchten Attributs enthält, falls ein solcher existiert,
	 *         oder ein leeres {@code Optional}, falls kein entsprechender Eintrag gefunden wurde.
	 */
	public Optional<Attributeintrag<T>> attributeintragAsOptional(final String attributName) {
		return Optional.ofNullable(map.get(normalisieren(attributName)));
	}

	/**
	 * Gibt die Menge der unterstützten Attributnamen in der Registry zurück.
	 * Die Attributnamen sind die Schlüssel, die in der internen Datenstruktur gespeichert wurden.
	 *
	 * @return Ein unveränderliches Set von Strings, das die Namen der unterstützten Attribute enthält.
	 */
	public Set<String> unterstuetzteAttribute() {
		return Collections.unmodifiableSet(map.keySet());
	}


	/**
	 * Importiert Einträge aus einer Quell-Registry in die aktuelle Registry, indem Attribute
	 * mit entsprechend angegebenem Präfix ergänzt werden. Dabei werden die Attribute basierend
	 * auf ihrem Werttyp (z. B. STRING oder COMPARABLE) verarbeitet und registriert.
	 * So können bspw. alle Schülersortierungen in Registries, die ein ein Schülerobjekt enthalten,
	 * mit dem Prefix "schueler" importiert werden.
	 *
	 * @param <P> Der Typ der Objekte in der Quell-Registry.
	 * @param prefix Das Präfix, das den Attributnamen der Quell-Registry hinzugefügt wird.
	 * @param quellregistry Die Quell-Registry, deren Einträge importiert werden sollen. Wenn die Registry null ist, wird der Import abgebrochen.
	 * @param quellwertermittlungsfunktion Eine Funktion, die den entsprechenden Quellwert für ein gegebenes Objekt in der Ziel-Registry ermittelt.
	 */
	public <P> void importiereRegistryEintraege(final String prefix, final SortierungRegistry<P> quellregistry,
			final Function<T, P> quellwertermittlungsfunktion) {

		if (quellregistry == null)
			return;

		for (final String quellattribut : quellregistry.unterstuetzteAttribute()) {
			final Optional<Attributeintrag<P>> optionalAttributeintrag = quellregistry.attributeintragAsOptional(quellattribut);
			if (optionalAttributeintrag.isEmpty())
				continue;

			final Attributeintrag<P> attributeintrag = optionalAttributeintrag.get();
			final String vollstaendigerAttributname = ergaenzePrefix(prefix, quellattribut);

			switch (attributeintrag.werttype()) {
				case STRING -> {
					@SuppressWarnings("unchecked")
					final Function<P, String> wertermittlungsfunktion =
							(Function<P, String>) attributeintrag.wertermittlungsfunktion();
					this.registiereString(vollstaendigerAttributname, t -> {
						final P quellwert = quellwertermittlungsfunktion.apply(t);
						return (quellwert == null) ? null : wertermittlungsfunktion.apply(quellwert);
					});
				}
				case COMPARABLE -> {
					@SuppressWarnings({ "rawtypes", "unchecked" })
					final Function<P, Comparable> wertermittlungsfunktion =
							(Function) attributeintrag.wertermittlungsfunktion();

					@SuppressWarnings({ "rawtypes", "unchecked" })
					final SortierungRegistry sortierungRegistry = this.registiereComparable(vollstaendigerAttributname, t -> {
						final P quellwert = quellwertermittlungsfunktion.apply(t);
						return (quellwert == null) ? null : wertermittlungsfunktion.apply(quellwert);
					});
				}
			}
		}
	}

	/**
	 * Erstellt und gibt einen {@link Comparator} für ein bestimmtes Attribut zurück, falls das Attribut
	 * in der Registry registriert ist. Der Comparator kann entweder aufsteigend oder absteigend sortieren,
	 * je nach Wert des Parameters {@code sortiereAufsteigend}. Falls das Attribut nicht gefunden wird oder ungültig ist,
	 * wird ein leeres {@code Optional} zurückgegeben.
	 *
	 * @param attribut Der Name des Attributs, für das ein Comparator erstellt werden soll. Der Name
	 *                  darf nicht null oder leer sein.
	 * @param sortiereAufsteigend Eine boolesche Angabe, ob die Sortierung aufsteigend (true) oder absteigend (false)
	 *                  sein soll.
	 * @return Ein {@code Optional}, das den {@link Comparator} für das angegebene Attribut enthält,
	 *         falls das Attribut registriert ist. Ein leeres {@code Optional}, falls das Attribut
	 *         nicht existiert oder ungültig ist.
	 */
	public Optional<Comparator<T>> attributComparator(final String attribut, final boolean sortiereAufsteigend) {
		if ((attribut == null) || attribut.isBlank())
			return Optional.empty();
		final Optional<Attributeintrag<T>> entry = attributeintragAsOptional(attribut);
		if (entry.isEmpty())
			return Optional.empty();

		final Attributeintrag<T> e = entry.get();
		return switch (e.werttype()) {
			case STRING -> {
				@SuppressWarnings("unchecked") final Comparator<T> cmp =
						Comparators.stringComparator((Function<T, String>) e.wertermittlungsfunktion(), sortiereAufsteigend);
				yield Optional.of(cmp);
			}
			case COMPARABLE -> {
				@SuppressWarnings({ "rawtypes", "unchecked" }) final Comparator<T> cmp =
						Comparators.comparableComparator((Function) e.wertermittlungsfunktion(), sortiereAufsteigend);
				yield Optional.of(cmp);
			}
		};
	}


	// ##### Hilfsfunktionen #####

	private static String normalisieren(final String s) {
		return (s == null) ? "" : s.trim().toLowerCase(Locale.ROOT);
	}

	private static String ergaenzePrefix(final String prefix, final String name) {
		if ((prefix == null) || prefix.isBlank())
			return name;
		return prefix + name;
	}

	/**
	 * Functional Interface für serialisierbare Methodenreferenzen, damit der Methodenname über eine SerializedLambda ermittelt werden kann.
	 * Beispiel: methodenreferenzToString(ReportingKlasse::sortierung) liefert "sortierung"
	 *
	 * @param <E> Typ des Eingabeparameters der Funktion
	 * @param <R> Rückgabetyp der Funktion
	 */
	@FunctionalInterface
	public interface SerializableFunction<E, R> extends Function<E, R>, Serializable {
	}

	/**
	 * Hilfsmethode zur Ermittelung der Methodennamen als String einer übergebenen Methodenreferenz. Wird für die Attributnamen bei der Sortierung verwendet.
	 *
	 * @param methodenreferenz eine Methodenreferenz, z. B. ReportingKlasse::sortierung
	 * @param <E> Typ des Eingabeparameters der Funktion
	 * @param <R> Rückgabetyp der Funktion
	 * @return der Name der implementierenden Methode (z. B. "sortierung"). Bei einem Fehler wird ein leerer String zurückgegeben.
	 */
	public <E, R> String methodeToString(final SerializableFunction<E, R> methodenreferenz) {
		try {
			final Method writeReplace = methodenreferenz.getClass().getDeclaredMethod("writeReplace");
			writeReplace.setAccessible(true);
			final Object serializedForm = writeReplace.invoke(methodenreferenz);
			if (serializedForm instanceof final SerializedLambda lambda)
				return lambda.getImplMethodName();
			return "";
		} catch (final ReflectiveOperationException e) {
			return "";
		}
	}
}
