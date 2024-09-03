import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaMapEntry } from '../../../java/util/JavaMapEntry';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { JavaMap } from '../../../java/util/JavaMap';

export class HashMap2D<K1, K2, V> extends JavaObject {

	private readonly _map : JavaMap<K1, JavaMap<K2, V>> = new HashMap<K1, JavaMap<K2, V>>();


	/**
	 * Konstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Fügt die Zuordnung der Map hinzu.
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird er erzeugt.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public put(key1 : K1, key2 : K2, value : V) : void {
		const map2 : JavaMap<K2, V> | null = this._map.computeIfAbsent(key1, { apply : (k: K1 | null) => new HashMap() });
		if (map2 === null)
			throw new NullPointerException()
		map2.put(key2, value);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2) oder NULL. <br>
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return den Wert zum Mapping (key1, key2) oder NULL. <br>
	 */
	public getOrNull(key1 : K1, key2 : K2) : V | null {
		const map2 : JavaMap<K2, V> | null = this._map.get(key1);
		if (map2 === null)
			return null;
		return map2.get(key2);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2). <br>
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return Den Wert zum Mapping (key1, key2).
	 * @throws DeveloperNotificationException Falls ein Teilpfad (key1, key2) nicht existiert!
	 */
	public getOrException(key1 : K1, key2 : K2) : V {
		const map2 : JavaMap<K2, V> = this.getSubMapOrException(key1);
		if (!map2.containsKey(key2))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ") ungültig!")
		const value : V | null = map2.get(key2);
		if (value === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ") ungültig!")
		return value;
	}

	/**
	 * Liefert für den Schlüssel (key1) die Map (key2 --> V) oder eine Exception.
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return Für den Schlüssel (key1) die Map (key2 --> V) oder eine Exception.
	 */
	public getSubMapOrException(key1 : K1) : JavaMap<K2, V> {
		const map2 : JavaMap<K2, V> | null = this._map.get(key1);
		if (map2 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!")
		return map2;
	}

	/**
	 * Liefert für den Schlüssel (key1) die Map (key2 --> V) oder NULL.
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return Für den Schlüssel (key1) die Map (key2 --> V) oder NULL.
	 */
	public getSubMapOrNull(key1 : K1) : JavaMap<K2, V> | null {
		return this._map.get(key1);
	}

	/**
	 * Liefert TRUE, falls für den Schlüssel (key1, key2) ein Mapping existiert.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return TRUE, falls für den Schlüssel (key1, key2) ein Mapping existiert.
	 */
	public contains(key1 : K1, key2 : K2) : boolean {
		const map2 : JavaMap<K2, V> | null = this._map.get(key1);
		if (map2 === null)
			return false;
		return map2.containsKey(key2);
	}

	/**
	 * Liefert TRUE, falls es den Teilpfad gibt.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return TRUE, falls es den Teilpfad gibt.
	 */
	public containsKey1(key1 : K1) : boolean {
		return this._map.containsKey(key1);
	}

	/**
	 * Löscht alle Zuordnungen der Map.
	 */
	public clear() : void {
		this._map.clear();
	}

	/**
	 * Entfernt das Mapping (key1, key2) falls es existiert, andernfalls wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return Den Wert zum Mapping (key1, key2) vor dem Löschen.
	 */
	public removeOrException(key1 : K1, key2 : K2) : V {
		const map2 : JavaMap<K2, V> = this.getSubMapOrException(key1);
		if (!map2.containsKey(key2))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ") ungültig!")
		const value : V | null = map2.remove(key2);
		if (map2.isEmpty())
			this._map.remove(key1);
		if (value === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ") ungültig!")
		return value;
	}

	/**
	 * Entfernt für den Schlüssel (key1) die Submap, falls key1 existiert, andernfalls passiert nichts.
	 *
	 * @param key1  Der 1. Schlüssel.
	 *
	 * @return Für den Schlüssel (key1) die Map (key2 --> V) oder null.
	 */
	public removeSubMap(key1 : K1) : JavaMap<K2, V> | null {
		return this._map.remove(key1);
	}

	/**
	 * Entfernt für den Schlüssel (key1) die Submap, falls key1 existiert, andernfalls wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param key1  Der 1. Schlüssel.
	 *
	 * @return Für den Schlüssel (key1) die Map (key2 --> V) oder eine Exception.
	 */
	public removeSubMapOrException(key1 : K1) : JavaMap<K2, V> {
		const map2 : JavaMap<K2, V> | null = this._map.remove(key1);
		if (map2 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") existiert nicht!")
		return map2;
	}

	/**
	 * Liefert eine Liste aller Values des 1. Keys in dieser Map.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return eine Liste aller Values des 1. Keys in dieser Map.
	 */
	public getNonNullValuesOfKey1AsList(key1 : K1) : List<V> {
		return new ArrayList<V>(this.getSubMapOrException(key1).values());
	}

	/**
	 * Liefert eine Liste aller Values des 1. Keys in dieser Map.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return eine Liste aller Values des 1. Keys in dieser Map.
	 */
	public getNonNullValuesOfKey1AsListOrNull(key1 : K1) : List<V> | null {
		const map1 : JavaMap<K2, V> | null = this.getSubMapOrNull(key1);
		return map1 === null ? null : new ArrayList(map1.values());
	}

	/**
	 * Liefert eine Liste aller Values in dieser Map.
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public getNonNullValuesAsList() : List<V> {
		const list : ArrayList<V> = new ArrayList<V>();
		for (const map2 of this._map.values())
			for (const value of map2.values())
				list.add(value);
		return list;
	}

	/**
	 * Liefert das KeySet des 1. Schlüssels.
	 *
	 * @return das KeySet der SubMap des 1. Schlüssels.
	 */
	public getKeySet() : JavaSet<K1> {
		return this._map.keySet();
	}

	/**
	 * Liefert das EntrySet des 1. Schlüssels.
	 *
	 * @return das EntrySet der SubMap des 1. Schlüssels.
	 */
	public getEntrySet() : JavaSet<JavaMapEntry<K1, JavaMap<K2, V>>> {
		return this._map.entrySet();
	}

	/**
	 * Liefert das KeySet der SubMap des 1. Schlüssels.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return das KeySet der SubMap des 1. Schlüssels.
	 */
	public getKeySetOf(key1 : K1) : JavaSet<K2> {
		return this.getSubMapOrException(key1).keySet();
	}

	/**
	 * Liefert die Anzahl an Mappings, der des Pfades (key1) oder 0, falls der Pfad nicht existiert.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return die Anzahl an Mappings, der des Pfades (key1) oder 0, falls der Pfad nicht existiert.
	 */
	public getSubMapSizeOrZero(key1 : K1) : number {
		const map2 : JavaMap<K2, V> | null = this._map.get(key1);
		if (map2 === null)
			return 0;
		return map2.size();
	}

	/**
	 * Liefert die Anzahl an gespeicherten Mappings.
	 *
	 * @return die Anzahl an gespeicherten Mappings.
	 */
	public size() : number {
		let size : number = 0;
		for (const map2 of this._map.values())
			size += map2.size();
		return size;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.HashMap2D';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.HashMap2D'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_map_HashMap2D<K1, K2, V>(obj : unknown) : HashMap2D<K1, K2, V> {
	return obj as HashMap2D<K1, K2, V>;
}
