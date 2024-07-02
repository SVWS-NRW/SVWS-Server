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
public class AttributMitAuswahl<K, V> {

	/** Die Menge der zulässigen Werte */
	private @NotNull List<V> _values = new ArrayList<>();

	/** Eine Map mit der Menge der zulässigen Werte */
	private final @NotNull Map<K, V> _mapValuesByKey = new HashMap<>();

	/** Eine Map mit der Menge der Werte in der Auswahl */
	private final @NotNull Map<K, V> _mapAuswahlValuesByKey = new HashMap<>();

	/** Eine Funktion, um aus einem Wert den zugehörigen Schlüssel zu extrahieren. */
	private final @NotNull Function<V, K> _toID;

	/** Ein Comparator für das Sortieren der enthaltenen Objekte */
	private final @NotNull Comparator<V> _comparator;

	/** Ein Handler für das Ereignis, dass die Auswahl verändert wurde */
	private final Runnable _eventHandlerAuswahlGeandert;

	/** Ein Handler für das Ereignis, dass die zugrundeliegende Liste verändert wurde */
	private Runnable _eventHandlerListeGeaendert = null;


	/**
	 * Erzeugt ein neues Objekt für ein Attribut mit Auswahl-Option
	 *
	 * @param values        die Menge der erlaubten Werte
	 * @param toId          eine Funktion zum Ermitteln des Schlüssels eines Objektes
	 * @param comparator    eine Vergleichsmethode zum Vergleichen von zwei enthaltenen Objekten
	 * @param eventHandler  ein Runnable, welches aufgerufen wird, wenn der Status der Auswahl sich ändert
	 */
	public AttributMitAuswahl(final @NotNull Collection<V> values, final @NotNull Function<V, K> toId,
			final @NotNull Comparator<V> comparator, final Runnable eventHandler) {
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
	 * Setzt den Event-Handler für das Ereignis, dass die zugrundeliegende Liste verändert
	 * wurde.
	 *
	 * @param eventHandler   der Event-Handler
	 */
	void setEventHandlerListeGeaendert(final Runnable eventHandler) {
		this._eventHandlerListeGeaendert = eventHandler;
	}


	/**
	 * Gibt die Liste der zulässigen Werte für dieses Attribut zurück.
	 *
	 * @return die Liste zulässigen Werte für dieses Attribut zurück.
	 */
	public @NotNull List<V> list() {
		return this._values;
	}


	/**
	 * Gibt die Anzahl der zulässigen Werte für dieses Attribut zurück.
	 *
	 * @return die Anzahl der zulässigen Werte für dieses Attribut
	 */
	public int size() {
		return this._values.size();
	}


	/**
	 * Gibt den Wert zu dem angegebenen Schlüssel zurück,
	 * sofern es sich um einen zulässigen Schlüssel handelt.
	 *
	 * @param key   der Schlüssel
	 *
	 * @return der Wert oder null, falls kein Wert enthalten ist.
	 */
	public V get(final @NotNull K key) {
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
	public @NotNull V getOrException(final @NotNull K key) throws DeveloperNotificationException {
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
	public boolean has(final @NotNull K key) {
		return this._mapValuesByKey.containsKey(key);
	}


	/**
	 * Gibt zurück, ob der Wert vorhanden ist.
	 *
	 * @param value   der Wert
	 *
	 * @return true, falls der Wert vorhanden ist
	 */
	public boolean hasValue(final @NotNull V value) {
		return this._mapValuesByKey.containsKey(this._toID.apply(value));
	}


	/**
	 * Fügt einen weiteren zulässigen Wert für das Attribut hinzu.
	 *
	 * @param value  der hinzuzufügende Wert
	 *
	 * @return true, wenn ein Wert entfernt wurde
	 */
	private boolean addInternal(final @NotNull V value) {
		final @NotNull K key = _toID.apply(value);
		if (this._mapValuesByKey.containsKey(key))
			return false;
		final @NotNull List<V> values = new ArrayList<>();
		values.addAll(this._values);
		values.add(value);
		this._values = values;
		this._values.sort(this._comparator);
		this._mapValuesByKey.put(key, value);
		return true;
	}


	/**
	 * Fügt einen weiteren zulässigen Wert für das Attribut hinzu.
	 *
	 * @param value  der hinzuzufügende Wert
	 */
	public void add(final @NotNull V value) {
		if ((addInternal(value)) && (_eventHandlerListeGeaendert != null))
			this._eventHandlerListeGeaendert.run();
	}


	/**
	 * Fügt weitere zulässige Werte für das Attribut hinzu.
	 *
	 * @param values  die hinzuzufügenden Werte
	 */
	public void addAll(final @NotNull List<V> values) {
		boolean added = false;
		for (final @NotNull V value : values)
			added = added || addInternal(value);
		if ((added) && (_eventHandlerListeGeaendert != null))
			this._eventHandlerListeGeaendert.run();
	}


	/**
	 * Entfernt den Wert als zulässigen Wert für das Attribut.
	 * Sollte der Wert zusätzlich zu der Auswahl gehören, so
	 * wird dieser aus der Auswahl entfernt.
	 *
	 * @param value   der zu entferndende Wert
	 *
	 * @return true, falls der Wert entfernt wurde
	 */
	private boolean removeInternal(final @NotNull V value) {
		final @NotNull K key = _toID.apply(value);
		final @NotNull List<V> values = new ArrayList<>();
		for (final @NotNull V v : this._values) {
			if (key.equals(_toID.apply(v)))
				continue;
			values.add(v);
		}
		if (values.size() == this._values.size())
			return false;
		this._values = values;
		this._mapValuesByKey.remove(key);
		this._mapAuswahlValuesByKey.remove(key);
		return true;
	}


	/**
	 * Entfernt den Wert als zulässigen Wert für das Attribut.
	 * Sollte der Wert zusätzlich zu der Auswahl gehören, so
	 * wird dieser aus der Auswahl entfernt.
	 *
	 * @param value   der zu entferndende Wert
	 */
	public void remove(final @NotNull V value) {
		if (!removeInternal(value))
			return;
		if (_eventHandlerListeGeaendert != null)
			this._eventHandlerListeGeaendert.run();
		if (_eventHandlerAuswahlGeandert != null)
			this._eventHandlerAuswahlGeandert.run();
	}


	/**
	 * Entfernt die angegebenen Werte als zulässigen Werte für das Attribut.
	 * Sollte die Werte zusätzlich zu der Auswahl gehören, so werden diese
	 * aus der Auswahl entfernt.
	 *
	 * @param values   die zu entferndende Werte
	 */
	public void removeAll(final @NotNull List<V> values) {
		boolean removed = false;
		for (final @NotNull V value : values)
			removed = removed || removeInternal(value);
		if (!removed)
			return;
		if (_eventHandlerListeGeaendert != null)
			this._eventHandlerListeGeaendert.run();
		if (_eventHandlerAuswahlGeandert != null)
			this._eventHandlerAuswahlGeandert.run();
	}


	/**
	 * Gibt die Liste der in der Auswahl ausgewählten Werte für dieses Attribut zurück.
	 * Ist die Liste leer, so ist keine Auswahl vorhanden.
	 *
	 * @return die Liste der in der Auswahl enthaltenen Werte für dieses Attribut.
	 */
	public @NotNull List<V> auswahl() {
		return new ArrayList<>(this._mapAuswahlValuesByKey.values());
	}


	/**
	 * Gibt eine sortierte Liste der in der Auswahl ausgewählten Werte für dieses Attribut zurück.
	 * Ist die Liste leer, so ist keine Auswahl vorhanden.
	 *
	 * @return die Liste der in der Auswahl enthaltenen Werte für dieses Attribut.
	 */
	public @NotNull List<V> auswahlSorted() {
		final @NotNull List<V> list = this.auswahl();
		list.sort(this._comparator);
		return list;
	}


	/**
	 * Gibt die Liste der in der Auswahl enthaltenen Schlüssel für dieses Attribut zurück.
	 * Ist die Liste leer, so ist kein Auswahl vorhanden.
	 *
	 * @return die Liste der in der Auswahl enthaltenen Schlüssel für dieses Attribut zurück.
	 */
	public @NotNull List<K> auswahlKeyList() {
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
	 * Gibt die Anzahl der Elemente in der Auswahl zurück.
	 *
	 * @return die Anzahl der Elemente in der Auswahl
	 */
	public int auswahlSize() {
		return this._mapAuswahlValuesByKey.size();
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
			throw new DeveloperNotificationException(
					"Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.");
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
			throw new DeveloperNotificationException(
					"Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.");
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


	/**
	 * Diese Methode übernimmt die Auswahl des übergebenen {@link AttributMitAuswahl}.
	 *
	 * @param srcAuswahl   Die Auswahl des AttributMitAuswahl, die übernommen wird.
	 */
	public void setAuswahl(final @NotNull AttributMitAuswahl<K, V> srcAuswahl) {
		for (final @NotNull K key : srcAuswahl.auswahlKeyList())
			if (this.has(key))
				this.auswahlAddByKey(key);
	}

}
