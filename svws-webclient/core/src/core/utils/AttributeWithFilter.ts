import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaFunction } from '../../java/util/function/JavaFunction';
import { HashMap } from '../../java/util/HashMap';
import type { Runnable } from '../../java/lang/Runnable';
import { ArrayList } from '../../java/util/ArrayList';
import type { Collection } from '../../java/util/Collection';
import type { List } from '../../java/util/List';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import type { JavaMap } from '../../java/util/JavaMap';
import type { Comparator } from '../../java/util/Comparator';

export class AttributeWithFilter<K, V> extends JavaObject {

	/**
	 * Eine Menge der zulässigen Werte
	 */
	private readonly _values : List<V> = new ArrayList();

	/**
	 * Eine Map mit der Menge der zulässigen Werte
	 */
	private readonly _mapValuesByKey : JavaMap<K, V> = new HashMap();

	/**
	 * Eine Map mit der Menge der Werte im Filter
	 */
	private readonly _mapFilterValuesByKey : JavaMap<K, V> = new HashMap();

	/**
	 * Eine Funktion, um aus einem Wert den zugehörigen Schlüssel zu extrahieren.
	 */
	private readonly _toID : JavaFunction<V, K>;

	/**
	 * Ein Comparator für das Sortieren der enthaltenen Objekte
	 */
	private readonly _comparator : Comparator<V>;

	/**
	 * Ein Handler für das Ergebnis, dass der Filter verändert wurde
	 */
	private readonly _eventHandlerFilterChanged : Runnable | null;


	/**
	 * Erzeugt ein neues Objekt für ein Attribut mit Filter-Option
	 *
	 * @param values        die Menge der erlaubten Werte
	 * @param toId          eine Funktion zum Ermitteln des Schlüssels eines Objektes
	 * @param comparator    eine Vergleichsmethode zum Vergleichen von zwei enthaltenen Objekten
	 * @param eventHandler  ein Runnable, welches aufgerufen wird, wenn der Status des Filters sich ändert
	 */
	public constructor(values : Collection<V>, toId : JavaFunction<V, K>, comparator : Comparator<V>, eventHandler : Runnable | null) {
		super();
		this._toID = toId;
		this._comparator = comparator;
		this._values.clear();
		this._values.addAll(values);
		this._values.sort(this._comparator);
		this._mapValuesByKey.clear();
		for (const v of this._values)
			this._mapValuesByKey.put(toId.apply(v), v);
		this._eventHandlerFilterChanged = eventHandler;
	}

	/**
	 * Gibt die Liste der zulässigen Werte für dieses Attribut zurück.
	 *
	 * @return die Liste zulässigen Werte für dieses Attribut zurück.
	 */
	public list() : List<V> {
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
	public get(key : K) : V | null {
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
	public getOrException(key : K) : V {
		const value : V | null = this.get(key);
		if (value === null)
			throw new DeveloperNotificationException("Kein gültiger Schlüsselwert.")
		return value;
	}

	/**
	 * Gibt zurück, ob der Schlüssel erlaubt ist.
	 *
	 * @param key   der Schlüssel
	 *
	 * @return true, falls der Schlüssel erlaubt ist
	 */
	public has(key : K) : boolean {
		return this._mapValuesByKey.containsKey(key);
	}

	/**
	 * Gibt zurück, ob der Wert vorhanden ist.
	 *
	 * @param value   der Wert
	 *
	 * @return true, falls der Wert vorhanden ist
	 */
	public hasValue(value : V) : boolean {
		return this._mapValuesByKey.containsKey(this._toID.apply(value));
	}

	/**
	 * Gibt die Liste der im Filter ausgewählten Werte für dieses Attribut zurück.
	 * Ist die Liste leer, so ist kein Filter gesetzt.
	 *
	 * @return die Liste der im Filter ausgewählten Werte für dieses Attribut zurück.
	 */
	public filterList() : List<V> {
		return new ArrayList(this._mapFilterValuesByKey.values());
	}

	/**
	 * Gibt die Liste der im Filter ausgewählten Schlüssel für dieses Attribut zurück.
	 * Ist die Liste leer, so ist kein Filter gesetzt.
	 *
	 * @return die Liste der im Filter ausgewählten Schlüssel für dieses Attribut zurück.
	 */
	public filterKeyList() : List<K> {
		return new ArrayList(this._mapFilterValuesByKey.keySet());
	}

	/**
	 * Gibt zurück, ob der Filter aktiv ist und Filter-Werte hat.
	 *
	 * @return true, falls der Filter aktiv ist, und ansonsten false
	 */
	public filterAktiv() : boolean {
		return !this._mapFilterValuesByKey.isEmpty();
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
	public filterHas(value : V) : boolean {
		if (!this.hasValue(value))
			throw new DeveloperNotificationException("Der Wert existiert nicht für dieses Attribut und kann daher nicht für den Filter verwendet werden.")
		const key : K = this._toID.apply(value);
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
	public filterHasKey(key : K) : boolean {
		if (!this.has(key))
			throw new DeveloperNotificationException("Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für den Filter verwendet werden.")
		return this._mapFilterValuesByKey.containsKey(key);
	}

	/**
	 * Leert den Filter.
	 */
	public filterClear() : void {
		this._mapFilterValuesByKey.clear();
		if (this._eventHandlerFilterChanged !== null)
			this._eventHandlerFilterChanged.run();
	}

	/**
	 * Fügt den Wert zum Filter hinzu, wenn er nicht bereits vorhanden ist.
	 *
	 * @param value   der Wert für den Filter
	 *
	 * @throws DeveloperNotificationException falls der Wert bei dem Filter nicht zulässig ist
	 */
	public filterAdd(value : V) : void {
		if (!this.hasValue(value))
			throw new DeveloperNotificationException("Der Wert existiert nicht für dieses Attribut und kann daher nicht für den Filter verwendet werden.")
		this._mapFilterValuesByKey.put(this._toID.apply(value), value);
		if (this._eventHandlerFilterChanged !== null)
			this._eventHandlerFilterChanged.run();
	}

	/**
	 * Erntfernt den Wert aus dem Filter, sofern er vorhanden ist.
	 *
	 * @param value   der Wert der aus dem Filter entfernt wird
	 */
	public filterRemove(value : V) : void {
		this._mapFilterValuesByKey.remove(this._toID.apply(value));
		if (this._eventHandlerFilterChanged !== null)
			this._eventHandlerFilterChanged.run();
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
	public filterToggle(value : V) : boolean {
		const key : K = this._toID.apply(value);
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
	public filterAddByKey(key : K) : void {
		if (!this.has(key))
			throw new DeveloperNotificationException("Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für den Filter verwendet werden.")
		this._mapFilterValuesByKey.put(key, this.getOrException(key));
		if (this._eventHandlerFilterChanged !== null)
			this._eventHandlerFilterChanged.run();
	}

	/**
	 * Erntfernt den Wert für den Schlüssel aus dem Filter, sofern er vorhanden ist.
	 *
	 * @param key   der Schlüssel für den Wert der aus dem Filter entfernt wird
	 */
	public filterRemoveByKey(key : K) : void {
		this._mapFilterValuesByKey.remove(key);
		if (this._eventHandlerFilterChanged !== null)
			this._eventHandlerFilterChanged.run();
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
	public filterToggleByKey(key : K) : boolean {
		if (this._mapFilterValuesByKey.containsKey(key)) {
			this.filterRemoveByKey(key);
			return false;
		}
		this.filterAddByKey(key);
		return true;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AttributeWithFilter'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_AttributeWithFilter<K, V>(obj : unknown) : AttributeWithFilter<K, V> {
	return obj as AttributeWithFilter<K, V>;
}
