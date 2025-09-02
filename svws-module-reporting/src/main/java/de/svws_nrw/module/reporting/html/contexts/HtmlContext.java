package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import org.thymeleaf.context.Context;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Abstrakte Basisklasse für die Thymeleaf-html-Daten-Contexts.
 *
 * @param <T> der Datentyp der vom Context verwalteten Elemente in contextData
 */
public abstract class HtmlContext<T> {

	/** Der zu diesem Context gehörige Thymeleaf-Context. */
	private Context context;

	/** Generische Liste der Context-Daten. */
	private List<T> contextData = new ArrayList<>();

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingRepository reportingRepository;

	/** Legt fest, ob der Context als Haupt-Daten-Context verwendet werden soll (true) oder als Detail-Daten-Conetxt (false). */
	private boolean istHauptdatenContext = true;

	/** Liste von Attributnamen, nach denen der Context sortiert werden soll. */
	private List<String> sortierungAttribute = new ArrayList<>();


	/**
	 * Konstruktor für die Klasse HtmlContext, der das Repository und den Flag zur Hauptdatenquelle initialisiert.
	 *
	 * @param reportingRepository Das Reporting-Repository, welches verwendet wird, um die Daten zu verwalten.
	 * @param istHauptdatenContext Ein Boolean-Wert, der angibt, ob der aktuelle Kontext ein Hauptdatenkontext ist.
	 */
	protected HtmlContext(final ReportingRepository reportingRepository, final Boolean istHauptdatenContext) {
		this.reportingRepository = reportingRepository;
		this.istHauptdatenContext = istHauptdatenContext;
		if (!this.reportingRepository.reportingParameter().verwendeStandardsortierung) {
			if (this.istHauptdatenContext)
				setSortierungAttribute(this.reportingRepository.reportingParameter().sortierungHauptdaten);
			else
				setSortierungAttribute(this.reportingRepository.reportingParameter().sortierungDetaildaten);
		} else
			setSortierungAttribute(standardsortierung());
	}



	// ### Getter und Setter-Methoden ###

	/**
	 * Rückgabe des Thymeleaf-Daten-Context
	 *
	 * @return Gibt einen Thymeleaf-Daten-Context zurück.
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Setzen des Thymeleaf-Daten-Context
	 *
	 * @param context Thymeleaf-Daten-Context mit den Daten.
	 */
	public void setContext(final Context context) {
		this.context = context;
	}

	/**
	 * Gibt die Context-Daten zurück.
	 *
	 * @return die Liste der in diesem Context verwalteten Daten
	 */
	public List<T> getContextData() {
		return contextData;
	}

	/**
	 * Setzt die Context-Daten. Null-Werte werden herausgefiltert. Bei Übergabe von null wird eine leere Liste gesetzt.
	 *
	 * @param data die zu setzende Datenliste
	 */
	public void setContextData(final List<T> data) {
		if (data == null) {
			this.contextData = new ArrayList<>();
			return;
		}
		this.contextData = new ArrayList<>(data.stream().filter(Objects::nonNull).toList());
	}

	/**
	 * Gibt die Liste der Sortierungsattribute zurück, die definiert wurden, um den Kontext entsprechend zu sortieren.
	 *
	 * @return Eine Liste von Attributnamen, die für die Sortierung verwendet werden.
	 */
	public List<String> getSortierungAttribute() {
		return sortierungAttribute;
	}


	/**
	 * Diese abstrakte Methode wird in der abgeleiteten Klasse implementiert, indem dort in dieser Methode eine Liste erzeugt und zurückgegeben wird, die die
	 * Attributnamen enthält, nach denen der Kontext sortiert werden soll.
	 *
	 * @return Die Attributnamen, nach denen der Kontext sortiert werden soll.
	 */
	public abstract List<String> standardsortierung();

	/**
	 * Legt die Sortierungsattribute für den Kontext fest. Null-Werte und leere Strings
	 * in der übergebenen Liste der Attribute werden herausgefiltert. Wenn die gefilterte Liste leer ist
	 * oder null übergeben wird, wird eine leere Liste gesetzt.
	 *
	 * @param sortierungAttribute Liste von Attributnamen, die für die Sortierung des Kontexts verwendet werden sollen. Null-Werte und leere Strings werden
	 * ignoriert.
	 */
	public void setSortierungAttribute(final List<String> sortierungAttribute) {
		if ((sortierungAttribute == null) || sortierungAttribute.isEmpty()) {
			this.sortierungAttribute = new ArrayList<>();
			return;
		}

		final List<String> filterSortierungsattribute =
				new ArrayList<>(sortierungAttribute.stream().filter(Objects::nonNull).filter(sa -> !sa.isBlank()).toList());

		filterSortierungsattribute.forEach(sa -> sa = sa.replace("()", "").trim());

		if (!filterSortierungsattribute.isEmpty())
			this.sortierungAttribute = filterSortierungsattribute;
		else
			this.sortierungAttribute = new ArrayList<>();
	}



	// ### Methoden zur Sortierung der Context-Daten ###

	/**
	 * Sortiert die im Context gespeicherten Daten (contextData) basierend auf den im Context definierten Sortierungsattributen.
	 * Die Methode filtert null-Werte aus contextData heraus. Für Strings wird die deutsche Sortierreihenfolge angewandt.
	 * Falls die Attribute nicht gefunden werden, werden sie für die Sortierung ignoriert.
	 */
	protected void sortiereContext() {
		if ((this.contextData == null) || this.contextData.isEmpty())
			return;
		final List<T> sorted = sortiereContext(this.contextData);
		this.contextData = (sorted == null) ? new ArrayList<>() : sorted;
	}

	/**
	 * Sortiert eine Liste von Objekten basierend auf den im Context definierten Sortierungsattributen.
	 * Die Methode filtert null-Werte aus der übergebenen Liste heraus. Anschließend wird die Liste nach den Werten der angegebenen Attribute sortiert. Für
	 * Strings wird dabei die deutsche Sortierreihenfolge angewandt.
	 * Falls die Attribute nicht gefunden werden, werden sie für die Sortierung ignoriert.
	 *
	 * @param <T> Typ der Objekte in der zu sortiereden Liste.
	 * @param list Die Liste von Objekten, die sortiert werden sollen. Null-Werte werden bei der Rückgabe entfernt.
	 * @return Die sortierte Liste. Falls die Eingabeliste oder die Sortierungsattribute null oder leer sind oder keine Attribute für die Sortierung gefunden
	 * werden konnten, dann wird die Original-Liste zurückgegeben. Null-Werte aus der originalen Liste werden nicht in der sortierten Liste zurückgegeben.
	 */
	protected <T> List<T> sortiereContext(final List<T> list) {
		if ((list == null) || this.sortierungAttribute.isEmpty())
			return list;

		final List<T> listNonNull = new ArrayList<>(list.stream().filter(Objects::nonNull).toList());

		if (listNonNull.isEmpty())
			return list;

		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		Comparator<T> comparator = null;

		for (final String attribut : this.sortierungAttribute) {
			try {
				// Ein Eintrag der sortierungAttribute-Liste kann durch einen Punkt separierte Attribute von Unterobjekten enthalten. Daher splitte diese
				// Attributkette hier in einzelne Attribute auf und speichere diese in einer Liste.
				final List<String> attributkette = Arrays.stream(attribut.split("\\.")).map(String::trim).filter(a -> !a.isBlank()).toList();
				if (attributkette.isEmpty())
					continue;

				// Erzeuge eine Methoden-Kette zur Attributkette, sowohl für einfache (Kette der Länge 1) als auch für Punkt-separierte Attribute. Diese
				// Kette wird benötigt, um später beim Vergleichen auf die Werte am Ende der Kette zuzugreifen.
				final List<Method> methodenkette = new ArrayList<>();
				Class<?> aktuelleClass = listNonNull.getFirst().getClass();
				for (final String teilattribut : attributkette) {
					final Method m = aktuelleClass.getMethod(teilattribut);
					methodenkette.add(m);
					aktuelleClass = m.getReturnType();
				}

				// Setze nun den Comparator zusammen.
				if (comparator == null) {
					// Die oberste Ebene der Sortierung definiert den Comparator.
					comparator = (o1, o2) -> {
						try {
							// Für die beiden zu vergleichenden Objekte wird die Methodenkette aufgerufen, um die zu vergleichenden Werte zu ermitteln.
							final Object value1 = invokeMethodenkette(o1, methodenkette);
							final Object value2 = invokeMethodenkette(o2, methodenkette);
							return compareValues(value1, value2, colGerman);
						} catch (final Exception e) {
							return 0;
						}
					};
				} else {
					// Die weiteren Ebenen der Sortierung werden mit thenComparing angehängt.
					final Comparator<T> next = (o1, o2) -> {
						try {
							// Für die beiden zu vergleichenden Objekte wird die Methodenkette aufgerufen, um die zu vergleichenden Werte zu ermitteln.
							final Object value1 = invokeMethodenkette(o1, methodenkette);
							final Object value2 = invokeMethodenkette(o2, methodenkette);
							return compareValues(value1, value2, colGerman);
						} catch (final Exception e) {
							return 0;
						}
					};
					comparator = comparator.thenComparing(next);
				}
			} catch (final Exception e) {
				// Wenn keine Methode zum Attribut gefunden wird, wird ein Fehler geworfen. Ignoriere diese Exception und gehe weiter.
			}
		}

		// Wenn ein Comparator erzeugt wurde, sortiere damit die gefilterte Liste. Andernfalls wird die originale Liste zurückgegeben.
		if (comparator != null) {
			listNonNull.sort(comparator);
			return listNonNull;
		} else
			return list;
	}

	/**
	 * Führt eine Kette von Methodenaufrufen auf einem gegebenen Startobjekt aus.
	 * Die Methoden werden in der Reihenfolge der angegebenen Liste aufgerufen.
	 * Wenn eines der Zwischenergebnisse null ist, wird der Vorgang abgebrochen und null zurückgegeben.
	 *
	 * @param root Das Ausgangsobjekt, auf dem die Methode beginnt.
	 * @param kette Eine Liste von Methoden, die aufeinanderfolgend aufgerufen werden sollen.
	 * @return Das Ergebnis des letzten Methodenaufrufs in der Kette oder null, wenn ein Zwischenergebnis null ist.
	 * @throws Exception Falls während eines Methodenaufrufs eine Ausnahme auftritt.
	 */
	private static Object invokeMethodenkette(final Object root, final List<Method> kette) throws Exception {
		Object aktuell = root;
		for (final Method m : kette) {
			if (aktuell == null)
				return null;
			aktuell = m.invoke(aktuell);
		}
		return aktuell;
	}

	/**
	 * Vergleicht zwei Werte unter Berücksichtigung der deutschen Sortierreihenfolge.
	 * Falls beide Werte Strings sind, wird der Vergleich mit dem übergebenen deutschen Collator durchgeführt.
	 * Sind die Werte Comparable, wird ein separater Vergleich durchgeführt.
	 * Falls die Werte nicht vergleichbar sind, wird 0 zurückgegeben.
	 *
	 * @param value1 Der erste zu vergleichende Wert.
	 * @param value2 Der zweite zu vergleichende Wert.
	 * @param colGerman Der Collator, der für die deutsche Sortierreihenfolge verwendet wird.
	 * @return Ein negativer Wert, wenn der erste Wert kleiner ist als der zweite; 0, wenn beide gleich sind;
	 * ein positiver Wert, wenn der erste Wert größer ist als der zweite.
	 */
	private int compareValues(final Object value1, final Object value2, final Collator colGerman) {
		if ((value1 instanceof String) && (value2 instanceof String)) {
			return colGerman.compare((String) value1, (String) value2);
		} else if ((value1 instanceof Comparable<?>) && (value2 instanceof Comparable<?>)) {
			return compareComparableValues(value1, value2); // Separate Methode implementiert, um die Warnung vor einem "unchecked cast" unterdrücken zu können.
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	private int compareComparableValues(final Object value1, final Object value2) {
		try {
			return ((Comparable<Object>) value1).compareTo(value2);
		} catch (final ClassCastException e) {
			return 0;
		}
	}


	/**
	 * Functional Interface für serialisierbare Methodenreferenzen, damit der Methodenname über eine SerializedLambda ermittelt werden kann. Es wird von der
	 * Methode {@link #methodenreferenzToString(SerializableFunction)} verwendet.
	 * Beispiel: methodenreferenzToString(ReportingKlasse::sortierung) -> "sortierung"
	 *
	 * @param <T> Typ des Eingabeparameters der Funktion
	 * @param <R> Rückgabetyp der Funktion
	 */
	@FunctionalInterface
	protected interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
	}

	/**
	 * Hilfsmethode zur Ermittelung der Methodennamen als String einer übergebenen Methodenreferenz. Wird für die Attributnamen bei der Sortierung verwendet.
	 *
	 * @param methodRef eine Methodenreferenz, z. B. ReportingKlasse::sortierung
	 * @param <T> Typ des Eingabeparameters der Funktion
	 * @param <R> Rückgabetyp der Funktion
	 * @return der Name der implementierenden Methode (z. B. "sortierung"). Bei einem Fehler wird ein leerer String zurückgegeben.
	 */
	protected static <T, R> String methodenreferenzToString(final SerializableFunction<T, R> methodRef) {
		try {
			final Method writeReplace = methodRef.getClass().getDeclaredMethod("writeReplace");
			writeReplace.setAccessible(true);
			final Object serializedForm = writeReplace.invoke(methodRef);
			if (serializedForm instanceof final SerializedLambda lambda)
				return lambda.getImplMethodName();
			return "";
		} catch (final ReflectiveOperationException e) {
			return "";
		}
	}




}
