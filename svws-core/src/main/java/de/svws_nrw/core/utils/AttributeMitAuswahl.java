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
 * Eine Klasse für den Zugriff auf Attribute mit eingebauter Auswahl-Funktion,
 * welche u.a. für Filter genutzt werden kann.
 *
 * @param <K> der Typ des eindeutigen Schlüsselwertes für ein enthaltenes Objekt
 * @param <V> der Typ der enthaltenen Objekte
 */
public class AttributeMitAuswahl<@NotNull K, @NotNull V> {

	/** Eine Menge der zulässigen Werte */
	private final @NotNull List<@NotNull V> _values = new ArrayList<>();

	/** Eine Map mit der Menge der zulässigen Werte */
	private final @NotNull Map<@NotNull K, @NotNull V> _mapValuesByKey = new HashMap<>();

	/** Eine Map mit der Menge der Werte in der Auswahl */
	private final @NotNull Map<@NotNull K, @NotNull V> _mapAuswahlValuesByKey = new HashMap<>();

	/** Eine Funktion, um aus einem Wert den zugehörigen Schlüssel zu extrahieren. */
	private final @NotNull Function<@NotNull V, @NotNull K> _toID;

	/** Ein Comparator für das Sortieren der enthaltenen Objekte */
	private final @NotNull Comparator<@NotNull V> _comparator;

	/** Ein Handler für das Ergebnis, dass die Auswahl verändert wurde */
	private final Runnable _eventHandlerAuswahlGeandert;


	/**
	 * Erzeugt ein neues Objekt für ein Attribut mit Auswahl-Option
	 *
	 * @param values        die Menge der erlaubten Werte
	 * @param toId          eine Funktion zum Ermitteln des Schlüssels eines Objektes
	 * @param comparator    eine Vergleichsmethode zum Vergleichen von zwei enthaltenen Objekten
	 * @param eventHandler  ein Runnable, welches aufgerufen wird, wenn der Status der Auswahl sich ändert
	 */
	public AttributeMitAuswahl(final @NotNull Collection<@NotNull V> values, final @NotNull Function<@NotNull V, @NotNull K> toId,
			final @NotNull Comparator<@NotNull V> comparator, final Runnable eventHandler) {
		this._toID = toId;
		this._comparator = comparator;
		this._values.clear();
		this._values.addAll(values);
		this._values.sort(this._comparator);
		this._mapValuesByKey.clear();
		for (final @NotNull V v : this._values)
			this._mapValuesByKey.put(toId.apply(v), v);
		this._eventHandlerAuswahlGeandert = eventHandler;
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
		return this._mapValuesByKey.get(key);
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
	 * Gibt die Liste der in der Auswahl ausgewählten Werte für dieses Attribut zurück.
	 * Ist die Liste leer, so ist keine Auswahl vorhanden.
	 *
	 * @return die Liste der in der Auswahl enthaltenen Werte für dieses Attribut.
	 */
	public @NotNull List<@NotNull V> auswahl() {
		return new ArrayList<>(this._mapAuswahlValuesByKey.values());
	}


	/**
	 * Gibt die Liste der in der Auswahl enthaltenen Schlüssel für dieses Attribut zurück.
	 * Ist die Liste leer, so ist kein Auswahl vorhanden.
	 *
	 * @return die Liste der in der Auswahl enthaltenen Schlüssel für dieses Attribut zurück.
	 */
	public @NotNull List<@NotNull K> auswahlKeyList() {
		return new ArrayList<>(this._mapAuswahlValuesByKey.keySet());
	}


	/**
	 * Gibt zurück, ob eine Auswahl vorhanden ist und Auswahl-Werte hat.
	 *
	 * @return true, falls eine Auswahl vorhanden ist, und ansonsten false
	 */
	public boolean auswahlExists() {
		return !this._mapAuswahlValuesByKey.isEmpty();
	}


	/**
	 * Prüft, ob der übergebene Wert in der Auswahl vorhanden ist oder nicht.
	 *
	 * @param value   der zu prüfende Wert
	 *
	 * @return true, falls der Wert in der Auswahl vorhanden ist, und ansonsten false
	 *
	 * @throws DeveloperNotificationException falls der Wert bei der Auswahl nicht zulässig ist
	 */
	public boolean auswahlHas(final @NotNull V value) {
		if (!this.hasValue(value))
			throw new DeveloperNotificationException("Der Wert existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.");
		final @NotNull K key = this._toID.apply(value);
		return this._mapAuswahlValuesByKey.containsKey(key);
	}


	/**
	 * Prüft, ob der übergebene Schlüssel in der Auswahl vorhanden ist oder nicht.
	 *
	 * @param key   der zu prüfende Schlüssel
	 *
	 * @return true, falls der Schlüssel in der Auswahl vorhanden ist, und ansonsten false
	 *
	 * @throws DeveloperNotificationException falls der Wert bei der Auswahl nicht zulässig ist
	 */
	public boolean auswahlHasKey(final @NotNull K key) {
		if (!this.has(key))
			throw new DeveloperNotificationException("Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.");
		return this._mapAuswahlValuesByKey.containsKey(key);
	}


	/**
	 * Leert die Auswahl.
	 */
	public void auswahlClear() {
		this._mapAuswahlValuesByKey.clear();
		if (_eventHandlerAuswahlGeandert != null)
			_eventHandlerAuswahlGeandert.run();
	}


	/**
	 * Fügt den Wert zu der Auswahl hinzu, wenn er nicht bereits vorhanden ist.
	 *
	 * @param value   der Wert für die Auswahl
	 *
	 * @throws DeveloperNotificationException falls der Wert bei der Auswahl nicht zulässig ist
	 */
	public void auswahlAdd(final @NotNull V value) {
		if (!this.hasValue(value))
			throw new DeveloperNotificationException("Der Wert existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.");
		this._mapAuswahlValuesByKey.put(this._toID.apply(value), value);
		if (_eventHandlerAuswahlGeandert != null)
			_eventHandlerAuswahlGeandert.run();
	}


	/**
	 * Erntfernt den Wert aus der Auswahl, sofern er vorhanden ist.
	 *
	 * @param value   der Wert der aus der Auswahl entfernt wird
	 */
	public void auswahlRemove(final @NotNull V value) {
		this._mapAuswahlValuesByKey.remove(this._toID.apply(value));
		if (_eventHandlerAuswahlGeandert != null)
			_eventHandlerAuswahlGeandert.run();
	}


	/**
	 * Fügt einen Wert zu der Auswahl hinzu, wenn er nicht vorhanden ist, und entfernt
	 * ihn, wenn er bereits vorhanden ist.
	 *
	 * @param value   der Wert für die Auswahl
	 *
	 * @return true, falls der Wert anschließend in der Auswahl vorhanden ist, und ansonsten false
	 *
	 * @throws DeveloperNotificationException falls der Wert für das Setzen bei der Auswahl nicht zulässig ist
	 */
	public boolean auswahlToggle(final @NotNull V value) {
		final @NotNull K key = this._toID.apply(value);
		if (this._mapAuswahlValuesByKey.containsKey(key)) {
			this.auswahlRemove(value);
			return false;
		}
		this.auswahlAdd(value);
		return true;
	}


	/**
	 * Fügt den Wert von dem Schlüssel zu der Auswahl hinzu, wenn er nicht bereits vorhanden ist.
	 *
	 * @param key   der Schlüssel für die Auswahl
	 *
	 * @throws DeveloperNotificationException falls der Schlüssel bei der Auswahl nicht zulässig ist
	 */
	public void auswahlAddByKey(final @NotNull K key) {
		if (!this.has(key))
			throw new DeveloperNotificationException("Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.");
		this._mapAuswahlValuesByKey.put(key, this.getOrException(key));
		if (_eventHandlerAuswahlGeandert != null)
			_eventHandlerAuswahlGeandert.run();
	}


	/**
	 * Erntfernt den Wert für den Schlüssel aus der Auswahl, sofern er vorhanden ist.
	 *
	 * @param key   der Schlüssel für den Wert der aus der Auswahl entfernt wird
	 */
	public void auswahlRemoveByKey(final @NotNull K key) {
		this._mapAuswahlValuesByKey.remove(key);
		if (_eventHandlerAuswahlGeandert != null)
			_eventHandlerAuswahlGeandert.run();
	}


	/**
	 * Fügt einen Wert zu der Auswahl hinzu, wenn der Schlüssel nicht vorhanden ist, und entfernt
	 * ihn, wenn der Schlüssel bereits vorhanden ist.
	 *
	 * @param key   der Schlüssel für den Wert in der Auswahl
	 *
	 * @return true, falls der Wert anschließend in der Auswahl gesetzt ist, und ansonsten false
	 *
	 * @throws DeveloperNotificationException falls der Wert für das Setzen bei der Auswahl nicht zulässig ist
	 */
	public boolean auswahlToggleByKey(final @NotNull K key) {
		if (this._mapAuswahlValuesByKey.containsKey(key)) {
			this.auswahlRemoveByKey(key);
			return false;
		}
		this.auswahlAddByKey(key);
		return true;
	}

}
