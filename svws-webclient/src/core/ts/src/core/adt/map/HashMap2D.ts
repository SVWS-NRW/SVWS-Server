import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { JavaMap } from '../../../java/util/JavaMap';

export class HashMap2D<K1, K2, V> extends JavaObject {

	private readonly _map : JavaMap<K1, JavaMap<K2, V | null>> = new HashMap();


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
	public put(key1 : K1, key2 : K2, value : V | null) : void {
		let map2 : JavaMap<K2, V | null> | null = this._map.get(key1);
		if (map2 === null) {
			map2 = new HashMap();
			this._map.put(key1, map2);
		}
		map2.put(key2, value);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2). <br>
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return Den Wert zum Mapping (key1, key2).
	 * @throws NullPointerException Falls ein Teilpfad (key1, key2) nicht existiert!
	 */
	public getOrException(key1 : K1, key2 : K2) : V | null {
		const map2 : JavaMap<K2, V | null> = this.getSubMapOrException(key1);
		if (!map2.containsKey(key2))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ") ungültig!")
		return map2.get(key2);
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
		const map2 : JavaMap<K2, V | null> | null = this._map.get(key1);
		if (map2 === null)
			return null;
		return map2.get(key2);
	}

	/**
	 * Liefert den Nicht-Null-Wert zum Mapping (key1, key2).<br>
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.<br>
	 * Falls der zugeordnete Wert NULL ist, wird eine Exception geworfen.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return Den Nicht-Null-Wert zum Mapping (key1, key2).
	 * @throws NullPointerException Falls ein Teilpfad (key1, key2) nicht existiert!
	 */
	public getNonNullOrException(key1 : K1, key2 : K2) : V {
		const value : V | null = this.getOrException(key1, key2);
		if (value === null)
			throw new DeveloperNotificationException("value is NULL!")
		return value;
	}

	/**
	 * Liefert für den Schlüssel (key1) die Map (key2 --> V) oder eine Exception.
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return Für den Schlüssel (key1) die Map (key2 --> V) oder eine Exception.
	 */
	public getSubMapOrException(key1 : K1) : JavaMap<K2, V | null> {
		const map2 : JavaMap<K2, V | null> | null = this._map.get(key1);
		if (map2 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!")
		return map2;
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
		const map2 : JavaMap<K2, V | null> | null = this._map.get(key1);
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
	 */
	public removeOrException(key1 : K1, key2 : K2) : void {
		const map2 : JavaMap<K2, V | null> = this.getSubMapOrException(key1);
		if (!map2.containsKey(key2))
			throw new DeveloperNotificationException("Mapping " + key1 + ", " + key2 + " existiert nicht")
		map2.remove(key2);
		if (map2.isEmpty())
			this._map.remove(key1);
	}

	/**
	 * Liefert eine Liste aller Values des 1. Keys in dieser Map.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return eine Liste aller Values des 1. Keys in dieser Map.
	 */
	public getNonNullValuesOfKey1AsList(key1 : K1) : List<V> {
		const list : ArrayList<V> = new ArrayList();
		for (const value of this.getSubMapOrException(key1).values()) {
			if (value === null)
				throw new DeveloperNotificationException("Liste hat ungewünschte NULL Elemente!")
			list.add(value);
		}
		return list;
	}

	/**
	 * Liefert eine Liste aller Values in dieser Map.
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public getNonNullValuesAsList() : List<V> {
		const list : ArrayList<V> = new ArrayList();
		for (const map2 of this._map.values()) {
			for (const value of map2.values()) {
				if (value === null)
					throw new DeveloperNotificationException("Liste hat ungewünschte NULL Elemente!")
				list.add(value);
			}
		}
		return list;
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.HashMap2D'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_map_HashMap2D<K1, K2, V>(obj : unknown) : HashMap2D<K1, K2, V> {
	return obj as HashMap2D<K1, K2, V>;
}
