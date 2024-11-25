import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { LongArrayKey } from '../../../core/adt/LongArrayKey';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { MapUtils } from '../../../core/utils/MapUtils';
import type { JavaMap } from '../../../java/util/JavaMap';

export class ListMap3DMultiKeyLong<V> extends JavaObject {

	private readonly _map123 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();

	private readonly _map12 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();

	private readonly _map13 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();

	private readonly _map23 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();

	private readonly _map1 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();

	private readonly _map2 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();

	private readonly _map3 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();


	/**
	 * Konstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Fügt das Element allen Listen hinzu.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels (key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels (key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels (key1, key2, key3).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public add(key1 : number, key2 : number, key3 : number, value : V) : void {
		MapUtils.getOrCreateArrayList(this._map123, new LongArrayKey(key1, key2, key3)).add(value);
		MapUtils.getOrCreateArrayList(this._map12, new LongArrayKey(key1, key2)).add(value);
		MapUtils.getOrCreateArrayList(this._map13, new LongArrayKey(key1, key3)).add(value);
		MapUtils.getOrCreateArrayList(this._map23, new LongArrayKey(key2, key3)).add(value);
		MapUtils.getOrCreateArrayList(this._map1, new LongArrayKey(key1)).add(value);
		MapUtils.getOrCreateArrayList(this._map2, new LongArrayKey(key2)).add(value);
		MapUtils.getOrCreateArrayList(this._map3, new LongArrayKey(key3)).add(value);
	}

	/**
	 * Entfernt das Element aus den Listen.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels (key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels (key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels (key1, key2, key3).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public remove(key1 : number, key2 : number, key3 : number, value : V) : void {
		MapUtils.getOrCreateArrayList(this._map123, new LongArrayKey(key1, key2, key3)).remove(value);
		MapUtils.getOrCreateArrayList(this._map12, new LongArrayKey(key1, key2)).remove(value);
		MapUtils.getOrCreateArrayList(this._map13, new LongArrayKey(key1, key3)).remove(value);
		MapUtils.getOrCreateArrayList(this._map23, new LongArrayKey(key2, key3)).remove(value);
		MapUtils.getOrCreateArrayList(this._map1, new LongArrayKey(key1)).remove(value);
		MapUtils.getOrCreateArrayList(this._map2, new LongArrayKey(key2)).remove(value);
		MapUtils.getOrCreateArrayList(this._map3, new LongArrayKey(key3)).remove(value);
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2, key3).
	 *
	 * @param key1  Der 1. Schlüssel des Tripels (key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels (key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels (key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get123(key1 : number, key2 : number, key3 : number) : List<V> {
		return MapUtils.getOrCreateArrayList(this._map123, new LongArrayKey(key1, key2, key3));
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2).
	 *
	 * @param key1  Der 1. Schlüssel des Tripels (key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels (key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get12(key1 : number, key2 : number) : List<V> {
		return MapUtils.getOrCreateArrayList(this._map12, new LongArrayKey(key1, key2));
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key3).
	 *
	 * @param key1  Der 1. Schlüssel des Tripels (key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels (key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get13(key1 : number, key3 : number) : List<V> {
		return MapUtils.getOrCreateArrayList(this._map13, new LongArrayKey(key1, key3));
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key2, key3).
	 *
	 * @param key2  Der 2. Schlüssel des Tripels (key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels (key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get23(key2 : number, key3 : number) : List<V> {
		return MapUtils.getOrCreateArrayList(this._map23, new LongArrayKey(key2, key3));
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1).
	 *
	 * @param key1  Der 1. Schlüssel des Tripels (key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get1(key1 : number) : List<V> {
		return MapUtils.getOrCreateArrayList(this._map1, new LongArrayKey(key1));
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key2).
	 *
	 * @param key2  Der 2. Schlüssel des Tripels (key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get2(key2 : number) : List<V> {
		return MapUtils.getOrCreateArrayList(this._map2, new LongArrayKey(key2));
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key3).
	 *
	 * @param key3  Der 3. Schlüssel des Tripels (key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get3(key3 : number) : List<V> {
		return MapUtils.getOrCreateArrayList(this._map3, new LongArrayKey(key3));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.ListMap3DMultiKeyLong';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.ListMap3DMultiKeyLong'].includes(name);
	}

	public static class = new Class<ListMap3DMultiKeyLong<any>>('de.svws_nrw.core.adt.map.ListMap3DMultiKeyLong');

}

export function cast_de_svws_nrw_core_adt_map_ListMap3DMultiKeyLong<V>(obj : unknown) : ListMap3DMultiKeyLong<V> {
	return obj as ListMap3DMultiKeyLong<V>;
}
