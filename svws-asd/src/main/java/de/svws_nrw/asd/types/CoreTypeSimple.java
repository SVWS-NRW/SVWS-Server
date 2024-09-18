package de.svws_nrw.asd.types;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.CoreTypeException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese abstracte Klasse stellt den Bezeichner für einen CoreTypeSimple bereit,
 * da das ENUM Interface nicht verwendet wird.
 *
 * @param <T> der Typ der Historieneinträge des Core-Types
 * @param <U> der Typ des Core-Types
 */
public abstract class CoreTypeSimple<T extends CoreTypeData, U extends CoreTypeSimple<T, U>> implements CoreType<T, U> {

	/** Verwaltung der Bezeichner, um die Liste der erstellten CoreType Elemente zurück geben zu können */
	private static @NotNull HashMap<Class<?>, @NotNull Object@NotNull[]> _values = new HashMap<>();

	/** der Bezeichner des CoreTypeSimple */
	private @NotNull String _name = "";

	/** die lfd. Nummer des CoreTypeSimple */
	private int _ordinal;


	/**
	 * Erstellt einen CoreTypeSimple mit Standardwerten
	 */
	protected CoreTypeSimple() {
	}

	/**
	 * Initialisiert den Simple-Core-Type vom Typ U und erstellt die einzelnen Instanzen für die Bezeichner und deren Ordinal-Werten
	 *
	 * @param <T>    der Typ der Historien-Einträge des Core-Types
	 * @param <U>    der Core-Type
	 * @param obj    Ein Objekt der Klasse clazz
	 * @param clazz  das Klassen-Objekt für den Core-Type
	 * @param data   die Date mit denen der Core-Type initialisiert wird
	 */
	public static <T extends CoreTypeData, U extends CoreTypeSimple<T, U>> void initValues(final @NotNull U obj, final @NotNull Class<U> clazz,
			final @NotNull Map<String, List<T>> data) {
		try {
			@SuppressWarnings("unchecked") final @NotNull U @NotNull [] values = (@NotNull U @NotNull []) Array.newInstance(clazz, data.size());
			_values.put(clazz, values);
			int i = 0;
			for (final @NotNull String bezeichner : data.keySet()) {
				final U u = obj.getInstance();
				if (u != null) {
					values[i] = u;
					final CoreTypeSimple<T, U> coreTypeValue = values[i];
					coreTypeValue._name = bezeichner;
					coreTypeValue._ordinal = i++;
				}
			}
		} catch (final Exception e) {
			throw new CoreTypeException(e);
		}
	}


	/**
	 * Gibt die einzelnen Werte dieser Aufzählung als Array zurück.
	 *
	 * @param <S>     der Core-Type
	 * @param clazz   das Klassen-Objekt zum Core-Type
	 *
	 * @return die Werte des Core-Types als Array
	 */
	@SuppressWarnings("unchecked")
	public static <S extends CoreTypeSimple<?, ?>> @NotNull S @NotNull [] valuesByClass(final @NotNull Class<S> clazz) {
		final var list = _values.get(clazz);
		if (list == null)
			return (@NotNull S @NotNull []) Array.newInstance(clazz, 0);
		return (@NotNull S @NotNull []) list;
	}


	/**
	 * Gibt den unveränderlichen Bezeichner des Core-Types zurück.
	 *
	 * @return der name (Bezeichner)
	 */
	@Override
	public final @NotNull String name() {
		return this._name;
	}

	/**
	 * Gibt einen Ordinal-Wert für den CoreType-Wert ähnlich wie bei Enum-Konstanten zurück.
	 *
	 * @return der Ordinal-Wert
	 */
	@Override
	public final int ordinal() {
		return this._ordinal;
	}

	/**
	 * Gibt den Bezeichner des CoreType-Wertes zurück.
	 *
	 * @return der Bezeichner
	 */
	@Override
	public @NotNull String toString() {
		return this._name;
	}


	/**
	 * Vergleicht diesen CoreType-Wert mit dem anderen Coretype-Wert
	 *
	 * @param other   der andere Coretype-Wert
	 *
	 * @return kleiner 0, wenn dieser Wert kleiner ist, 0, wenn sie gleich sind
	 *     und größer 0, wenn dieser Wert größer ist
	 */
	@Override
	public int compareTo(final @NotNull U other) {
		if (other == null)
			throw new NullPointerException();
		return Integer.compare(this.ordinal(), other.ordinal());
	}


	/**
	 * Der Hash-Code des CoreType-Wertes.
	 *
	 * @return der Hash-Code
	 */
	@Override
	public int hashCode() {
		return _name.hashCode();
	}


	/**
	 * Prüft, ob die CoreType-Werte gleich sind.
	 */
	@Override
	public boolean equals(final Object obj) {
		return (this == obj);
	}

	/**
	 * Erzeugt ein Objekt der Klasse
	 * @return Ein Objekt der Klasse U
	 */
	public U getInstance() {
		return null;
	}

}
