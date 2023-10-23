package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Klasse für den Zugriff auf Attribute mit eingebauter Filter-Funktion.
 *
 * @param <K> der Typ des eindeutigen Schlüsselwertes für ein enthaltenes Objekt
 * @param <V> der Typ der enthaltenen Objekte
 */
public class AttributeWithFilter<@NotNull K, @NotNull V> {

	/** Eine Menge der zulässigen Werte */
	private final @NotNull List<@NotNull V> _values = new ArrayList<>();

	/** Eine Map mit der Menge der zulässigen Werte */
	private final @NotNull Map<@NotNull K, @NotNull V> _mapValuesByKey = new HashMap<>();

	/** Eine Map mit der Menge der Werte im Filter */
	private final @NotNull Map<@NotNull K, @NotNull V> _mapFilterValuesByKey = new HashMap<>();

	/** Eine Funktion, um aus einem Wert den zugehörigen Schlüssel zu extrahieren. */
	private final @NotNull Function<@NotNull V, @NotNull K> _toID;

	/** Ein Comparator für das Sortieren der enthaltenen Objekte */
	private final @NotNull Comparator<@NotNull V> _comparator;


	/**
	 * Erzeugt ein neues Objekt für ein Attribut mit Filter-Option
	 *
	 * @param values      die Menge der erlaubten Werte
	 * @param toId        eine Funktion zum Ermitteln des Schlüssels eines Objektes
	 * @param comparator  eine Vergleichsmethode zum Vergleichen von zwei enthaltenen Objekten
	 */
	public AttributeWithFilter(final @NotNull Collection<@NotNull V> values, final @NotNull Function<@NotNull V, @NotNull K> toId, final @NotNull Comparator<@NotNull V> comparator) {
		this._toID = toId;
		this._comparator = comparator;
		this._values.clear();
		this._values.addAll(values);
		this._values.sort(this._comparator);
		this._mapValuesByKey.clear();
		for (final @NotNull V v : this._values)
			this._mapValuesByKey.put(toId.apply(v), v);
	}


	/**
	 * Gibt die Liste der zulässigen Werte für dieses Attribut zurück.
	 *
	 * @return die Liste zulässigen Werte für dieses Attribut zurück.
	 */
	public @NotNull List<@NotNull V> list() {
		return this._values;
	}


	/**
	 * Gibt den Wert zu dem angegebenen Schlüssel zurück,
	 * sofern es sich um einen zulässigen Schlüssel handelt.
	 *
	 * @param key   der Schlüssel
	 *
	 * @return der Wert oder null, falls kein Wert enthalten ist.
	 */
	public V get(@NotNull final K key) {
		return this.get(key);
	}


	/**
	 * Gibt den Wert zu dem angegebenen Schlüssel zurück,
	 * sofern es sich um einen zulässigen Schlüssel handelt.
	 *
	 * @param key   der Schlüssel
	 *
	 * @return der Wert
	 *
	 * @throws DeveloperNotificationException falls der Schlüssel nicht zulässig ist
	 */
	public @NotNull V getOrException(@NotNull final K key) throws DeveloperNotificationException {
		final V value = this.get(key);
		if (value == null)
			throw new DeveloperNotificationException("Kein gültiger Schlüsselwert.");
		return value;
	}


	/**
	 * Gibt zurück, ob der Schlüssel erlaubt ist.
	 *
	 * @param key   der Schlüssel
	 *
	 * @return true, falls der Schlüssel erlaubt ist
	 */
	public boolean has(@NotNull final K key) {
		return this._mapValuesByKey.containsKey(key);
	}


	/**
	 * Gibt zurück, ob der Wert vorhanden ist.
	 *
	 * @param value   der Wert
	 *
	 * @return true, falls der Wert vorhanden ist
	 */
	public boolean hasValue(@NotNull final V value) {
		return this._mapValuesByKey.containsKey(this._toID.apply(value));
	}


	/**
	 * Prüft, ob der übergebene Wert im Filter vorhanden ist oder nicht.
	 *
	 * @param value   der zu prüfende Wert
	 *
	 * @return true, falls der Wert im Filter vorhanden ist, und ansonsten false
	 *
	 * @throws DeveloperNotificationException falls der Wert bei dem Filter nicht zulässig ist
	 */
	public boolean filterHas(final @NotNull V value) {
		if (!this.hasValue(value))
			throw new DeveloperNotificationException("Der Wert existiert nicht für dieses Attribut und kann daher nicht für den Filter verwendet werden.");
		final @NotNull K key = this._toID.apply(value);
		return this._mapFilterValuesByKey.containsKey(key);
	}


	/**
	 * Prüft, ob der übergebene Schlüssel im Filter vorhanden ist oder nicht.
	 *
	 * @param key   der zu prüfende Schlüssel
	 *
	 * @return true, falls der Schlüssel im Filter vorhanden ist, und ansonsten false
	 *
	 * @throws DeveloperNotificationException falls der Wert bei dem Filter nicht zulässig ist
	 */
	public boolean filterHasKey(final @NotNull K key) {
		if (!this.has(key))
			throw new DeveloperNotificationException("Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für den Filter verwendet werden.");
		return this._mapFilterValuesByKey.containsKey(key);
	}


	/**
	 * Leert den Filter.
	 */
	public void filterClear() {
		this._mapFilterValuesByKey.clear();
	}


	/**
	 * Fügt den Wert zum Filter hinzu, wenn er nicht bereits vorhanden ist.
	 *
	 * @param value   der Wert für den Filter
	 *
	 * @throws DeveloperNotificationException falls der Wert bei dem Filter nicht zulässig ist
	 */
	public void filterAdd(final @NotNull V value) {
		if (!this.hasValue(value))
			throw new DeveloperNotificationException("Der Wert existiert nicht für dieses Attribut und kann daher nicht für den Filter verwendet werden.");
		this._mapFilterValuesByKey.put(this._toID.apply(value), value);
	}


	/**
	 * Erntfernt den Wert aus dem Filter, sofern er vorhanden ist.
	 *
	 * @param value   der Wert der aus dem Filter entfernt wird
	 */
	public void filterRemove(final @NotNull V value) {
		this._mapFilterValuesByKey.remove(this._toID.apply(value));
	}


	/**
	 * Fügt einen Wert zum Filter hinzu, wenn er nicht vorhanden ist, und entfernt
	 * ihn, wenn er bereits vorhanden ist.
	 *
	 * @param value   der Wert für den Filter
	 *
	 * @return true, falls der Wert anschließend im Filter gesetzt ist, und ansonsten false
	 *
	 * @throws DeveloperNotificationException falls der Wert für das Setzen bei dem Filter nicht zulässig ist
	 */
	public boolean filterToggle(final @NotNull V value) {
		final @NotNull K key = this._toID.apply(value);
		if (this._mapFilterValuesByKey.containsKey(key)) {
			this.filterRemove(value);
			return false;
		}
		this.filterAdd(value);
		return true;
	}


	/**
	 * Fügt den Wert von de Schlüssel zum Filter hinzu, wenn er nicht bereits vorhanden ist.
	 *
	 * @param key   der Schlüssel für den Filter
	 *
	 * @throws DeveloperNotificationException falls der Schlüssel bei dem Filter nicht zulässig ist
	 */
	public void filterAddByKey(final @NotNull K key) {
		if (!this.has(key))
			throw new DeveloperNotificationException("Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für den Filter verwendet werden.");
		this._mapFilterValuesByKey.put(key, this.getOrException(key));
	}


	/**
	 * Erntfernt den Wert für den Schlüssel aus dem Filter, sofern er vorhanden ist.
	 *
	 * @param key   der Schlüssel für den Wert der aus dem Filter entfernt wird
	 */
	public void filterRemoveByKey(final @NotNull K key) {
		this._mapFilterValuesByKey.remove(key);
	}


	/**
	 * Fügt einen Wert zum Filter hinzu, wenn der Schlüssel nicht vorhanden ist, und entfernt
	 * ihn, wenn der Schlüssel bereits vorhanden ist.
	 *
	 * @param key   der Schlüssel für den Wert im Filter
	 *
	 * @return true, falls der Wert anschließend im Filter gesetzt ist, und ansonsten false
	 *
	 * @throws DeveloperNotificationException falls der Wert für das Setzen bei dem Filter nicht zulässig ist
	 */
	public boolean filterToggleByKey(final @NotNull K key) {
		if (this._mapFilterValuesByKey.containsKey(key)) {
			this.filterRemoveByKey(key);
			return false;
		}
		this.filterAddByKey(key);
		return true;
	}

}
