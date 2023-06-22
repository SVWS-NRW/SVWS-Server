import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { JavaMap } from '../../../java/util/JavaMap';

export class HashMap3D<K1, K2, K3, V> extends JavaObject {

	private readonly _map1 : JavaMap<K1, JavaMap<K2, JavaMap<K3, V | null>>> = new HashMap();


	/**
	 * Konstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Fügt die Zuordnung der Map hinzu.
	 * Falls es einen Teil-Pfad von (key1, key2, key3) nicht gibt, wird er erzeugt.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public put(key1 : K1, key2 : K2, key3 : K3, value : V | null) : void {
		let map2 : JavaMap<K2, JavaMap<K3, V | null>> | null = this._map1.get(key1);
		if (map2 === null) {
			map2 = new HashMap();
			this._map1.put(key1, map2);
		}
		let map3 : JavaMap<K3, V | null> | null = map2.get(key2);
		if (map3 === null) {
			map3 = new HashMap();
			map2.put(key2, map3);
		}
		map3.put(key3, value);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3). <br>
	 * Wirft eine Exception, falls es den Pfad (key1, key2, key3) nicht gibt.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return Den Wert zum Mapping (key1, key2, key3).
	 *
	 * @throws NullPointerException falls es den Pfad (key1, key2, key3) nicht gibt.
	 */
	public getOrException(key1 : K1, key2 : K2, key3 : K3) : V | null {
		const map2 : JavaMap<K2, JavaMap<K3, V | null>> | null = this._map1.get(key1);
		if (map2 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!")
		const map3 : JavaMap<K3, V | null> | null = map2.get(key2);
		if (map3 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", " + key2 + ") ungültig!")
		if (!map3.containsKey(key3))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ") ungültig!")
		return map3.get(key3);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3) oder NULL. <br>
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return den Wert zum Mapping (key1, key2, key3) oder NULL. <br>
	 */
	public getOrNull(key1 : K1, key2 : K2, key3 : K3) : V | null {
		const map2 : JavaMap<K2, JavaMap<K3, V | null>> | null = this._map1.get(key1);
		if (map2 === null)
			return null;
		const map3 : JavaMap<K3, V | null> | null = map2.get(key2);
		if (map3 === null)
			return null;
		return map3.get(key3);
	}

	/**
	 * Liefert die Map zum Mapping (key1) oder NULL. <br>
	 *
	 * @param key1  Der 1. Schlüssel.
	 *
	 * @return die Map zum Mapping key1 oder NULL. <br>
	 */
	public getMap2OrNull(key1 : K1) : JavaMap<K2, JavaMap<K3, V | null>> | null {
		return this._map1.get(key1);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2) oder NULL. <br>
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public getMap3OrNull(key1 : K1, key2 : K2) : JavaMap<K3, V | null> | null {
		const map2 : JavaMap<K2, JavaMap<K3, V | null>> | null = this._map1.get(key1);
		if (map2 === null)
			return null;
		return map2.get(key2);
	}

	/**
	 * Liefert den Nicht-Null-Wert zum Mapping (key1, key2, key3).<br>
	 * Wirft eine Exception, falls der Pfad (key1, key2, key3) nicht existiert, oder NULL zugeordnet ist.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return den Nicht-Null-Wert zum Mapping (key1, key2, key3).
	 * @throws NullPointerException falls der Pfad (key1, key2, key3) nicht existiert, oder NULL zugeordnet ist.
	 */
	public getNonNullOrException(key1 : K1, key2 : K2, key3 : K3) : V {
		const value : V | null = this.getOrException(key1, key2, key3);
		if (value === null)
			throw new DeveloperNotificationException("value is NULL!")
		return value;
	}

	/**
	 * Liefert TRUE, falls für das Tripel (key1, key2, key3) ein Mapping existiert.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return TRUE, falls für das Tripel (key1, key2, key3) ein Mapping existiert.
	 */
	public contains(key1 : K1, key2 : K2, key3 : K3) : boolean {
		const map2 : JavaMap<K2, JavaMap<K3, V | null>> | null = this._map1.get(key1);
		if (map2 === null)
			return false;
		const map3 : JavaMap<K3, V | null> | null = map2.get(key2);
		if (map3 === null)
			return false;
		return map3.containsKey(key3);
	}

	/**
	 * Löscht alle Zuordnungen der Map.
	 */
	public clear() : void {
		this._map1.clear();
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3) falls es existiert<br>.
	 * Wirft eine {@link DeveloperNotificationException}, falls das Mapping nicht existiert.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 */
	public removeOrException(key1 : K1, key2 : K2, key3 : K3) : void {
		const map2 : JavaMap<K2, JavaMap<K3, V | null>> | null = this._map1.get(key1);
		if (map2 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!")
		const map3 : JavaMap<K3, V | null> | null = map2.get(key2);
		if (map3 === null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", " + key2 + ") ungültig!")
		if (!map3.containsKey(key3))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ") ungültig!")
		map3.remove(key3);
		if (map3.isEmpty()) {
			map2.remove(key2);
			if (map2.isEmpty()) {
				this._map1.remove(key1);
			}
		}
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3) falls es existiert<br>.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 */
	public remove(key1 : K1, key2 : K2, key3 : K3) : void {
		const map2 : JavaMap<K2, JavaMap<K3, V | null>> | null = this._map1.get(key1);
		if (map2 === null)
			return;
		const map3 : JavaMap<K3, V | null> | null = map2.get(key2);
		if (map3 === null)
			return;
		if (!map3.containsKey(key3))
			return;
		map3.remove(key3);
		if (map3.isEmpty()) {
			map2.remove(key2);
			if (map2.isEmpty()) {
				this._map1.remove(key1);
			}
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.HashMap3D'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_map_HashMap3D<K1, K2, K3, V>(obj : unknown) : HashMap3D<K1, K2, K3, V> {
	return obj as HashMap3D<K1, K2, K3, V>;
}
