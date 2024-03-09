import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { JavaMap } from '../../../java/util/JavaMap';
import { HashMap4D } from '../../../core/adt/map/HashMap4D';

export class HashMap5D<K1, K2, K3, K4, K5, V> extends JavaObject {

	private readonly _map : JavaMap<K1, HashMap4D<K2, K3, K4, K5, V>> = new HashMap();


	/**
	 * Konstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Fügt die Zuordnung der Map hinzu. Falls es einen Teil-Pfad von (key1, key2,
	 * key3, key4, key5) nicht gibt, wird er erzeugt.
	 *
	 * @param key1  Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2  Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3  Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4  Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5  Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public put(key1 : K1, key2 : K2, key3 : K3, key4 : K4, key5 : K5, value : V) : void {
		let map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 === null) {
			map2 = new HashMap4D();
			this._map.put(key1, map2);
		}
		map2.put(key2, key3, key4, key5, value);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3, key4). <br>
	 * Wirft eine Exception, falls es den Pfad (key1, key2, key3, key4) nicht gibt.
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @return Den Wert zum Mapping (key1, key2, key3, key4).
	 *
	 * @throws NullPointerException falls es den Pfad (key1, key2, key3, key4) nicht gibt.
	 */
	public getOrException(key1 : K1, key2 : K2, key3 : K3, key4 : K4, key5 : K5) : V | null {
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!")
		return map2.getOrException(key2, key3, key4, key5);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3, key4) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @return den Wert zum Mapping (key1, key2, key3, key4) oder NULL. <br>
	 */
	public getOrNull(key1 : K1, key2 : K2, key3 : K3, key4 : K4, key5 : K5) : V | null {
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 === null)
			return null;
		return map2.getOrNull(key2, key3, key4, key5);
	}

	/**
	 * Liefert die Map zum Mapping (key1) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel.
	 *
	 * @return die Map zum Mapping key1 oder NULL. <br>
	 */
	public getMap2OrNull(key1 : K1) : HashMap4D<K2, K3, K4, K5, V> | null {
		return this._map.get(key1);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2 Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public getMap3OrNull(key1 : K1, key2 : K2) : JavaMap<K3, JavaMap<K4, JavaMap<K5, V>>> | null {
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 === null)
			return null;
		return map2.getMap2OrNull(key2);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2, key3) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2 Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3 Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public getMap4OrNull(key1 : K1, key2 : K2, key3 : K3) : JavaMap<K4, JavaMap<K5, V>> | null {
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 === null)
			return null;
		return map2.getMap3OrNull(key2, key3);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2, key3) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2 Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3 Der 3. Schlüssel des Tripels(key1, key2, key3).
	 * @param key4 Der 4. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public getMap5OrNull(key1 : K1, key2 : K2, key3 : K3, key4 : K4) : JavaMap<K5, V> | null {
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 === null)
			return null;
		return map2.getMap4OrNull(key2, key3, key4);
	}

	/**
	 * Liefert den Nicht-Null-Wert zum Mapping (key1, key2, key3, key4).<br>
	 * Wirft eine Exception, falls der Pfad (key1, key2, key3, key4) nicht existiert, oder
	 * NULL zugeordnet ist.
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @return den Nicht-Null-Wert zum Mapping (key1, key2, key3).
	 * @throws NullPointerException falls der Pfad (key1, key2, key3) nicht
	 *                              existiert, oder NULL zugeordnet ist.
	 */
	public getNonNullOrException(key1 : K1, key2 : K2, key3 : K3, key4 : K4, key5 : K5) : V {
		const value : V | null = this.getOrException(key1, key2, key3, key4, key5);
		if (value === null)
			throw new DeveloperNotificationException("value is NULL!")
		return value;
	}

	/**
	 * Liefert TRUE, falls für das Quadrupel (key1, key2, key3, key4) ein Mapping existiert.
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @return TRUE, falls für das Quadrupel (key1, key2, key3, key4) ein Mapping existiert.
	 */
	public contains(key1 : K1, key2 : K2, key3 : K3, key4 : K4, key5 : K5) : boolean {
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 === null)
			return false;
		return map2.contains(key2, key3, key4, key5);
	}

	/**
	 * Löscht alle Zuordnungen der Map.
	 */
	public clear() : void {
		this._map.clear();
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3, key4) falls es existiert<br>
	 * . Wirft eine {@link DeveloperNotificationException}, falls das Mapping nicht
	 * existiert.
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 */
	public removeOrException(key1 : K1, key2 : K2, key3 : K3, key4 : K4, key5 : K5) : void {
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!")
		map2.removeOrException(key2, key3, key4, key5);
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3, key4) falls es existiert<br>
	 * .
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 */
	public remove(key1 : K1, key2 : K2, key3 : K3, key4 : K4, key5 : K5) : void {
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 === null)
			return;
		map2.remove(key2, key3, key4, key5);
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
	 * Liefert eine Liste aller Values in dieser Map.
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public getNonNullValuesAsList() : List<V> {
		const list : ArrayList<V> = new ArrayList();
		for (const map2 of this._map.values())
			list.addAll(map2.getNonNullValuesAsList());
		return list;
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1)
	 *
	 * @param key1 Schlüssel
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public getNonNullValuesOfMap2AsList(key1 : K1) : List<V> {
		const list : ArrayList<V> = new ArrayList();
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 !== null)
			list.addAll(map2.getNonNullValuesAsList());
		return list;
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2)
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2 Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public getNonNullValuesOfMap3AsList(key1 : K1, key2 : K2) : List<V> {
		const list : ArrayList<V> = new ArrayList();
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 !== null)
			list.addAll(map2.getNonNullValuesOfMap2AsList(key2));
		return list;
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2, key3)
	 *
	 * @param key1 Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2 Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3 Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public getNonNullValuesOfMap4AsList(key1 : K1, key2 : K2, key3 : K3) : List<V> {
		const list : ArrayList<V> = new ArrayList();
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 !== null)
			list.addAll(map2.getNonNullValuesOfMap3AsList(key2, key3));
		return list;
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2, key3)
	 *
	 * @param key1 Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2 Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3 Der 3. Schlüssel des Tripels(key1, key2, key3).
	 * @param key4 Der 4. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public getNonNullValuesOfMap5AsList(key1 : K1, key2 : K2, key3 : K3, key4 : K4) : List<V> {
		const list : ArrayList<V> = new ArrayList();
		const map2 : HashMap4D<K2, K3, K4, K5, V> | null = this._map.get(key1);
		if (map2 !== null)
			list.addAll(map2.getNonNullValuesOfMap4AsList(key2, key3, key4));
		return list;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.HashMap5D';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.HashMap5D'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_map_HashMap5D<K1, K2, K3, K4, K5, V>(obj : unknown) : HashMap5D<K1, K2, K3, K4, K5, V> {
	return obj as HashMap5D<K1, K2, K3, K4, K5, V>;
}
