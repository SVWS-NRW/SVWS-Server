import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { JavaMap } from '../../../java/util/JavaMap';

export class HashMap4D<K1, K2, K3, K4, V> extends JavaObject {

	private readonly _map1 : JavaMap<K1, JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>>> = new HashMap();


	/**
	 * Konstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Fügt die Zuordnung der Map hinzu. Falls es einen Teil-Pfad von (key1, key2,
	 * key3, key4) nicht gibt, wird er erzeugt.
	 *
	 * @param key1  Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2  Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3  Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4  Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public put(key1 : K1, key2 : K2, key3 : K3, key4 : K4, value : V) : void {
		let map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 === null) {
			map2 = new HashMap();
			this._map1.put(key1, map2);
		}
		let map3 : JavaMap<K3, JavaMap<K4, V>> | null = map2.get(key2);
		if (map3 === null) {
			map3 = new HashMap();
			map2.put(key2, map3);
		}
		let map4 : JavaMap<K4, V> | null = map3.get(key3);
		if (map4 === null) {
			map4 = new HashMap();
			map3.put(key3, map4);
		}
		map4.put(key4, value);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3, key4). <br>
	 * Wirft eine Exception, falls es den Pfad (key1, key2, key3, key4) nicht gibt.
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @return Den Wert zum Mapping (key1, key2, key3, key4).
	 *
	 * @throws NullPointerException falls es den Pfad (key1, key2, key3, key4) nicht gibt.
	 */
	public getOrException(key1 : K1, key2 : K2, key3 : K3, key4 : K4) : V | null {
		const map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!")
		const map3 : JavaMap<K3, JavaMap<K4, V>> | null = map2.get(key2);
		if (map3 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", " + key2 + ") ungültig!")
		const map4 : JavaMap<K4, V> | null = map3.get(key3);
		if (map4 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ") ungültig!")
		if (!map4.containsKey(key4))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ", key4=" + key4 + ") ungültig!")
		return map4.get(key4);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3, key4) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @return den Wert zum Mapping (key1, key2, key3, key4) oder NULL. <br>
	 */
	public getOrNull(key1 : K1, key2 : K2, key3 : K3, key4 : K4) : V | null {
		const map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 === null)
			return null;
		const map3 : JavaMap<K3, JavaMap<K4, V>> | null = map2.get(key2);
		if (map3 === null)
			return null;
		const map4 : JavaMap<K4, V> | null = map3.get(key3);
		if (map4 === null)
			return null;
		return map4.get(key4);
	}

	/**
	 * Liefert die Map zum Mapping (key1) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel.
	 *
	 * @return die Map zum Mapping key1 oder NULL. <br>
	 */
	public getMap2OrNull(key1 : K1) : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null {
		return this._map1.get(key1);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2 Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public getMap3OrNull(key1 : K1, key2 : K2) : JavaMap<K3, JavaMap<K4, V>> | null {
		const map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 === null)
			return null;
		return map2.get(key2);
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
	public getMap4OrNull(key1 : K1, key2 : K2, key3 : K3) : JavaMap<K4, V> | null {
		const map3 : JavaMap<K3, JavaMap<K4, V>> | null = this.getMap3OrNull(key1, key2);
		if (map3 === null)
			return null;
		return map3.get(key3);
	}

	/**
	 * Liefert den Nicht-Null-Wert zum Mapping (key1, key2, key3, key4).<br>
	 * Wirft eine Exception, falls der Pfad (key1, key2, key3, key4) nicht existiert, oder
	 * NULL zugeordnet ist.
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @return den Nicht-Null-Wert zum Mapping (key1, key2, key3).
	 * @throws NullPointerException falls der Pfad (key1, key2, key3) nicht
	 *                              existiert, oder NULL zugeordnet ist.
	 */
	public getNonNullOrException(key1 : K1, key2 : K2, key3 : K3, key4 : K4) : V {
		const value : V | null = this.getOrException(key1, key2, key3, key4);
		if (value === null)
			throw new DeveloperNotificationException("value is NULL!")
		return value;
	}

	/**
	 * Liefert TRUE, falls für das Quadrupel (key1, key2, key3, key4) ein Mapping existiert.
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @return TRUE, falls für das Quadrupel (key1, key2, key3, key4) ein Mapping existiert.
	 */
	public contains(key1 : K1, key2 : K2, key3 : K3, key4 : K4) : boolean {
		const map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 === null)
			return false;
		const map3 : JavaMap<K3, JavaMap<K4, V>> | null = map2.get(key2);
		if (map3 === null)
			return false;
		const map4 : JavaMap<K4, V> | null = map3.get(key3);
		if (map4 === null)
			return false;
		return map4.containsKey(key4);
	}

	/**
	 * Löscht alle Zuordnungen der Map.
	 */
	public clear() : void {
		this._map1.clear();
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3, key4) falls es existiert<br>
	 * . Wirft eine {@link DeveloperNotificationException}, falls das Mapping nicht
	 * existiert.
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 */
	public removeOrException(key1 : K1, key2 : K2, key3 : K3, key4 : K4) : void {
		const map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!")
		const map3 : JavaMap<K3, JavaMap<K4, V>> | null = map2.get(key2);
		if (map3 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", " + key2 + ") ungültig!")
		const map4 : JavaMap<K4, V> | null = map3.get(key3);
		if (map4 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ") ungültig!")
		if (!map4.containsKey(key4))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ", key4=" + key4 + ") ungültig!")
		map4.remove(key4);
		if (map4.isEmpty()) {
			map3.remove(key3);
			if (map3.isEmpty()) {
				map2.remove(key2);
				if (map2.isEmpty()) {
					this._map1.remove(key1);
				}
			}
		}
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3, key4) falls es existiert<br>
	 * .
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 */
	public remove(key1 : K1, key2 : K2, key3 : K3, key4 : K4) : void {
		const map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 === null)
			return;
		const map3 : JavaMap<K3, JavaMap<K4, V>> | null = map2.get(key2);
		if (map3 === null)
			return;
		const map4 : JavaMap<K4, V> | null = map3.get(key3);
		if (map4 === null)
			return;
		if (!map4.containsKey(key4))
			return;
		map4.remove(key4);
		if (map4.isEmpty()) {
			map3.remove(key3);
			if (map3.isEmpty()) {
				map2.remove(key2);
				if (map2.isEmpty()) {
					this._map1.remove(key1);
				}
			}
		}
	}

	/**
	 * Liefert das KeySet des 1. Schlüssels.
	 *
	 * @return das KeySet der SubMap des 1. Schlüssels.
	 */
	public getKeySet() : JavaSet<K1> {
		return this._map1.keySet();
	}

	/**
	 * Liefert eine Liste aller Values in dieser Map.
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public getNonNullValuesAsList() : List<V> {
		const list : ArrayList<V> = new ArrayList();
		for (const map2 of this._map1.values())
			for (const map3 of map2.values())
				for (const map4 of map3.values())
					for (const value of map4.values())
						list.add(value);
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
		const map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 !== null) {
			for (const map3 of map2.values())
				for (const map4 of map3.values())
					for (const value of map4.values())
						list.add(value);
		}
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
		const map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 !== null) {
			const map3 : JavaMap<K3, JavaMap<K4, V>> | null = map2.get(key2);
			if (map3 !== null)
				for (const map4 of map3.values())
					for (const value of map4.values())
						list.add(value);
		}
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
		const map2 : JavaMap<K2, JavaMap<K3, JavaMap<K4, V>>> | null = this._map1.get(key1);
		if (map2 !== null) {
			const map3 : JavaMap<K3, JavaMap<K4, V>> | null = map2.get(key2);
			if (map3 !== null) {
				const map4 : JavaMap<K4, V> | null = map3.get(key3);
				if (map4 !== null)
					for (const value of map4.values())
						list.add(value);
			}
		}
		return list;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.HashMap4D';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.HashMap4D'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_map_HashMap4D<K1, K2, K3, K4, V>(obj : unknown) : HashMap4D<K1, K2, K3, K4, V> {
	return obj as HashMap4D<K1, K2, K3, K4, V>;
}
