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

export class AttributMitAuswahl<K, V> extends JavaObject {

	/**
	 * Eine Menge der zulässigen Werte
	 */
	private readonly _values : List<V> = new ArrayList();

	/**
	 * Eine Map mit der Menge der zulässigen Werte
	 */
	private readonly _mapValuesByKey : JavaMap<K, V> = new HashMap();

	/**
	 * Eine Map mit der Menge der Werte in der Auswahl
	 */
	private readonly _mapAuswahlValuesByKey : JavaMap<K, V> = new HashMap();

	/**
	 * Eine Funktion, um aus einem Wert den zugehörigen Schlüssel zu extrahieren.
	 */
	private readonly _toID : JavaFunction<V, K>;

	/**
	 * Ein Comparator für das Sortieren der enthaltenen Objekte
	 */
	private readonly _comparator : Comparator<V>;

	/**
	 * Ein Handler für das Ergebnis, dass die Auswahl verändert wurde
	 */
	private readonly _eventHandlerAuswahlGeandert : Runnable | null;


	/**
	 * Erzeugt ein neues Objekt für ein Attribut mit Auswahl-Option
	 *
	 * @param values        die Menge der erlaubten Werte
	 * @param toId          eine Funktion zum Ermitteln des Schlüssels eines Objektes
	 * @param comparator    eine Vergleichsmethode zum Vergleichen von zwei enthaltenen Objekten
	 * @param eventHandler  ein Runnable, welches aufgerufen wird, wenn der Status der Auswahl sich ändert
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
		this._eventHandlerAuswahlGeandert = eventHandler;
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
	 * Gibt die Anzahl der zulässigen Werte für dieses Attribut zurück.
	 *
	 * @return die Anzahl der zulässigen Werte für dieses Attribut
	 */
	public size() : number {
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
	 * Gibt die Liste der in der Auswahl ausgewählten Werte für dieses Attribut zurück.
	 * Ist die Liste leer, so ist keine Auswahl vorhanden.
	 *
	 * @return die Liste der in der Auswahl enthaltenen Werte für dieses Attribut.
	 */
	public auswahl() : List<V> {
		return new ArrayList(this._mapAuswahlValuesByKey.values());
	}

	/**
	 * Gibt die Liste der in der Auswahl enthaltenen Schlüssel für dieses Attribut zurück.
	 * Ist die Liste leer, so ist kein Auswahl vorhanden.
	 *
	 * @return die Liste der in der Auswahl enthaltenen Schlüssel für dieses Attribut zurück.
	 */
	public auswahlKeyList() : List<K> {
		return new ArrayList(this._mapAuswahlValuesByKey.keySet());
	}

	/**
	 * Gibt zurück, ob eine Auswahl vorhanden ist und Auswahl-Werte hat.
	 *
	 * @return true, falls eine Auswahl vorhanden ist, und ansonsten false
	 */
	public auswahlExists() : boolean {
		return !this._mapAuswahlValuesByKey.isEmpty();
	}

	/**
	 * Gibt die Anzahl der Elemente in der Auswahl zurück.
	 *
	 * @return die Anzahl der Elemente in der Auswahl
	 */
	public auswahlSize() : number {
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
	public auswahlHas(value : V) : boolean {
		if (!this.hasValue(value))
			throw new DeveloperNotificationException("Der Wert existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.")
		const key : K = this._toID.apply(value);
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
	public auswahlHasKey(key : K) : boolean {
		if (!this.has(key))
			throw new DeveloperNotificationException("Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.")
		return this._mapAuswahlValuesByKey.containsKey(key);
	}

	/**
	 * Leert die Auswahl.
	 */
	public auswahlClear() : void {
		this._mapAuswahlValuesByKey.clear();
		if (this._eventHandlerAuswahlGeandert !== null)
			this._eventHandlerAuswahlGeandert.run();
	}

	/**
	 * Fügt den Wert zu der Auswahl hinzu, wenn er nicht bereits vorhanden ist.
	 *
	 * @param value   der Wert für die Auswahl
	 *
	 * @throws DeveloperNotificationException falls der Wert bei der Auswahl nicht zulässig ist
	 */
	public auswahlAdd(value : V) : void {
		if (!this.hasValue(value))
			throw new DeveloperNotificationException("Der Wert existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.")
		this._mapAuswahlValuesByKey.put(this._toID.apply(value), value);
		if (this._eventHandlerAuswahlGeandert !== null)
			this._eventHandlerAuswahlGeandert.run();
	}

	/**
	 * Erntfernt den Wert aus der Auswahl, sofern er vorhanden ist.
	 *
	 * @param value   der Wert der aus der Auswahl entfernt wird
	 */
	public auswahlRemove(value : V) : void {
		this._mapAuswahlValuesByKey.remove(this._toID.apply(value));
		if (this._eventHandlerAuswahlGeandert !== null)
			this._eventHandlerAuswahlGeandert.run();
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
	public auswahlToggle(value : V) : boolean {
		const key : K = this._toID.apply(value);
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
	public auswahlAddByKey(key : K) : void {
		if (!this.has(key))
			throw new DeveloperNotificationException("Der Schlüssel existiert nicht für dieses Attribut und kann daher nicht für die Auswahl verwendet werden.")
		this._mapAuswahlValuesByKey.put(key, this.getOrException(key));
		if (this._eventHandlerAuswahlGeandert !== null)
			this._eventHandlerAuswahlGeandert.run();
	}

	/**
	 * Erntfernt den Wert für den Schlüssel aus der Auswahl, sofern er vorhanden ist.
	 *
	 * @param key   der Schlüssel für den Wert der aus der Auswahl entfernt wird
	 */
	public auswahlRemoveByKey(key : K) : void {
		this._mapAuswahlValuesByKey.remove(key);
		if (this._eventHandlerAuswahlGeandert !== null)
			this._eventHandlerAuswahlGeandert.run();
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
	public auswahlToggleByKey(key : K) : boolean {
		if (this._mapAuswahlValuesByKey.containsKey(key)) {
			this.auswahlRemoveByKey(key);
			return false;
		}
		this.auswahlAddByKey(key);
		return true;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AttributMitAuswahl'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_AttributMitAuswahl<K, V>(obj : unknown) : AttributMitAuswahl<K, V> {
	return obj as AttributMitAuswahl<K, V>;
}
