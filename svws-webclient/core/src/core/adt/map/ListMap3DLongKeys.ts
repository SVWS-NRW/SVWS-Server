import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { LongArrayKey } from '../../../core/adt/LongArrayKey';
import { Class } from '../../../java/lang/Class';
import { MapUtils } from '../../../core/utils/MapUtils';
import type { JavaMap } from '../../../java/util/JavaMap';
import { Pair } from '../../../asd/adt/Pair';

export class ListMap3DLongKeys<V> extends JavaObject {

	private _map1 : JavaMap<number, List<V>> | null = null;

	private _map2 : JavaMap<number, List<V>> | null = null;

	private _map3 : JavaMap<number, List<V>> | null = null;

	private _map12 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map13 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map23 : JavaMap<LongArrayKey, List<V>> | null = null;

	private readonly _map123 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();

	private readonly _list : List<Pair<LongArrayKey, V>> = new ArrayList<Pair<LongArrayKey, V>>();


	/**
	 * Konstruktor.
	 */
	public constructor() {
		super();
	}

	private _lazyLoad1() : JavaMap<number, List<V>> {
		const map : JavaMap<number, List<V>> | null = new HashMap<number, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, key1);
			else
				MapUtils.getOrCreateArrayList(map, key1).add(e.b);
		}
		return map;
	}

	private _lazyLoad2() : JavaMap<number, List<V>> {
		const map : JavaMap<number, List<V>> | null = new HashMap<number, List<V>>();
		for (const e of this._list) {
			const key2 : number = e.a.getKeyAt(1);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, key2);
			else
				MapUtils.getOrCreateArrayList(map, key2).add(e.b);
		}
		return map;
	}

	private _lazyLoad3() : JavaMap<number, List<V>> {
		const map : JavaMap<number, List<V>> | null = new HashMap<number, List<V>>();
		for (const e of this._list) {
			const key3 : number = e.a.getKeyAt(2);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, key3);
			else
				MapUtils.getOrCreateArrayList(map, key3).add(e.b);
		}
		return map;
	}

	private _lazyLoad12() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key2 : number = e.a.getKeyAt(1);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2)).add(e.b);
		}
		return map;
	}

	private _lazyLoad13() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key3 : number = e.a.getKeyAt(2);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3)).add(e.b);
		}
		return map;
	}

	private _lazyLoad23() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key2 : number = e.a.getKeyAt(1);
			const key3 : number = e.a.getKeyAt(2);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3)).add(e.b);
		}
		return map;
	}

	/**
	 * Fügt das Element hinzu.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 *
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public add(key1 : number, key2 : number, key3 : number, value : V) : void {
		const key : LongArrayKey = new LongArrayKey(key1, key2, key3);
		MapUtils.getOrCreateArrayList(this._map123, key).add(value);
		this._list.add(new Pair<LongArrayKey, V>(key, value));
		if (this._map1 !== null)
			MapUtils.getOrCreateArrayList(this._map1, key1).add(value);
		if (this._map2 !== null)
			MapUtils.getOrCreateArrayList(this._map2, key2).add(value);
		if (this._map3 !== null)
			MapUtils.getOrCreateArrayList(this._map3, key3).add(value);
		if (this._map12 !== null)
			MapUtils.getOrCreateArrayList(this._map12, new LongArrayKey(key1, key2)).add(value);
		if (this._map13 !== null)
			MapUtils.getOrCreateArrayList(this._map13, new LongArrayKey(key1, key3)).add(value);
		if (this._map23 !== null)
			MapUtils.getOrCreateArrayList(this._map23, new LongArrayKey(key2, key3)).add(value);
	}

	/**
	 * Erzeugt den Pfad (key1, key2, key3) fügt aber nichts hinzu.
	 * Alle Pfad, die es vorher nicht gab, verweisen dann auf leere Listen.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 */
	public addEmpty(key1 : number, key2 : number, key3 : number) : void {
		const key : LongArrayKey = new LongArrayKey(key1, key2, key3);
		MapUtils.getOrCreateArrayList(this._map123, key);
		this._list.add(new Pair<LongArrayKey, V>(key, null));
		if (this._map1 !== null)
			MapUtils.getOrCreateArrayList(this._map1, key1);
		if (this._map2 !== null)
			MapUtils.getOrCreateArrayList(this._map2, key2);
		if (this._map3 !== null)
			MapUtils.getOrCreateArrayList(this._map3, key3);
		if (this._map12 !== null)
			MapUtils.getOrCreateArrayList(this._map12, new LongArrayKey(key1, key2));
		if (this._map13 !== null)
			MapUtils.getOrCreateArrayList(this._map13, new LongArrayKey(key1, key3));
		if (this._map23 !== null)
			MapUtils.getOrCreateArrayList(this._map23, new LongArrayKey(key2, key3));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1) gibt.
	 */
	public containsKey1(key1 : number) : boolean {
		if (this._map1 === null)
			this._map1 = this._lazyLoad1();
		return this._map1.containsKey(key1);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2) gibt.
	 */
	public containsKey2(key2 : number) : boolean {
		if (this._map2 === null)
			this._map2 = this._lazyLoad2();
		return this._map2.containsKey(key2);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key3) gibt.
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key3) gibt.
	 */
	public containsKey3(key3 : number) : boolean {
		if (this._map3 === null)
			this._map3 = this._lazyLoad3();
		return this._map3.containsKey(key3);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2) gibt.
	 */
	public containsKey12(key1 : number, key2 : number) : boolean {
		if (this._map12 === null)
			this._map12 = this._lazyLoad12();
		return this._map12.containsKey(new LongArrayKey(key1, key2));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key3) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key3) gibt.
	 */
	public containsKey13(key1 : number, key3 : number) : boolean {
		if (this._map13 === null)
			this._map13 = this._lazyLoad13();
		return this._map13.containsKey(new LongArrayKey(key1, key3));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key3) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key3) gibt.
	 */
	public containsKey23(key2 : number, key3 : number) : boolean {
		if (this._map23 === null)
			this._map23 = this._lazyLoad23();
		return this._map23.containsKey(new LongArrayKey(key2, key3));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key3) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key3) gibt.
	 */
	public containsKey123(key1 : number, key2 : number, key3 : number) : boolean {
		return this._map123.containsKey(new LongArrayKey(key1, key2, key3));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1).
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get1(key1 : number) : List<V> {
		if (this._map1 === null)
			this._map1 = this._lazyLoad1();
		if (!this._map1.containsKey(key1))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map1, key1));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2).
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get2(key2 : number) : List<V> {
		if (this._map2 === null)
			this._map2 = this._lazyLoad2();
		if (!this._map2.containsKey(key2))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map2, key2));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3).
	 *
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key3).
	 */
	public get3(key3 : number) : List<V> {
		if (this._map3 === null)
			this._map3 = this._lazyLoad3();
		if (!this._map3.containsKey(key3))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map3, key3));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key2   Der 2. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 */
	public get12(key1 : number, key2 : number) : List<V> {
		if (this._map12 === null)
			this._map12 = this._lazyLoad12();
		const key : LongArrayKey = new LongArrayKey(key1, key2);
		if (!this._map12.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map12, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3).
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key3).
	 */
	public get13(key1 : number, key3 : number) : List<V> {
		if (this._map13 === null)
			this._map13 = this._lazyLoad13();
		const key : LongArrayKey = new LongArrayKey(key1, key3);
		if (!this._map13.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map13, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3).
	 *
	 * @param key2   Der 2. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key2, key3).
	 */
	public get23(key2 : number, key3 : number) : List<V> {
		if (this._map23 === null)
			this._map23 = this._lazyLoad23();
		const key : LongArrayKey = new LongArrayKey(key2, key3);
		if (!this._map23.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map23, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3).
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key2   Der 2. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key2, key3).
	 */
	public get123(key1 : number, key2 : number, key3 : number) : List<V> {
		const key : LongArrayKey = new LongArrayKey(key1, key2, key3);
		if (!this._map123.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map123, key));
	}

	/**
	 * Liefert das Key-Set der Map1.
	 *
	 * @return das Key-Set der Map1.
	 */
	public keySet1() : JavaSet<number> {
		if (this._map1 === null)
			this._map1 = this._lazyLoad1();
		return this._map1.keySet();
	}

	/**
	 * Liefert das Key-Set der Map2.
	 *
	 * @return das Key-Set der Map2.
	 */
	public keySet2() : JavaSet<number> {
		if (this._map2 === null)
			this._map2 = this._lazyLoad2();
		return this._map2.keySet();
	}

	/**
	 * Liefert das Key-Set der Map3.
	 *
	 * @return das Key-Set der Map3.
	 */
	public keySet3() : JavaSet<number> {
		if (this._map3 === null)
			this._map3 = this._lazyLoad3();
		return this._map3.keySet();
	}

	/**
	 * Liefert das Key-Set der Map12.
	 *
	 * @return das Key-Set der Map12.
	 */
	public keySet12() : JavaSet<LongArrayKey> {
		if (this._map12 === null)
			this._map12 = this._lazyLoad12();
		return this._map12.keySet();
	}

	/**
	 * Liefert das Key-Set der Map13.
	 *
	 * @return das Key-Set der Map13.
	 */
	public keySet13() : JavaSet<LongArrayKey> {
		if (this._map13 === null)
			this._map13 = this._lazyLoad13();
		return this._map13.keySet();
	}

	/**
	 * Liefert das Key-Set der Map23.
	 *
	 * @return das Key-Set der Map23.
	 */
	public keySet23() : JavaSet<LongArrayKey> {
		if (this._map23 === null)
			this._map23 = this._lazyLoad23();
		return this._map23.keySet();
	}

	/**
	 * Liefert das Key-Set der Map123.
	 *
	 * @return das Key-Set der Map123.
	 */
	public keySet123() : JavaSet<LongArrayKey> {
		return this._map123.keySet();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.ListMap3DLongKeys';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.ListMap3DLongKeys'].includes(name);
	}

	public static class = new Class<ListMap3DLongKeys<any>>('de.svws_nrw.core.adt.map.ListMap3DLongKeys');

}

export function cast_de_svws_nrw_core_adt_map_ListMap3DLongKeys<V>(obj : unknown) : ListMap3DLongKeys<V> {
	return obj as ListMap3DLongKeys<V>;
}
