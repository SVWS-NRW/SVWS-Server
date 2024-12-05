import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { LongArrayKey } from '../../../core/adt/LongArrayKey';
import { Class } from '../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { MapUtils } from '../../../core/utils/MapUtils';
import type { JavaMap } from '../../../java/util/JavaMap';
import { Pair } from '../../../asd/adt/Pair';

export class ListMap2DLongKeys<V> extends JavaObject {

	private _map1 : JavaMap<number, List<V>> | null = null;

	private _map2 : JavaMap<number, List<V>> | null = null;

	private readonly _map12 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();

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

	/**
	 * Fügt das Element hinzu.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public add(key1 : number, key2 : number, value : V) : void {
		const key : LongArrayKey = new LongArrayKey(key1, key2);
		MapUtils.getOrCreateArrayList(this._map12, key).add(value);
		this._list.add(new Pair<LongArrayKey, V>(key, value));
		if (this._map1 !== null)
			MapUtils.getOrCreateArrayList(this._map1, key1).add(value);
		if (this._map2 !== null)
			MapUtils.getOrCreateArrayList(this._map2, key2).add(value);
	}

	/**
	 * Erzeugt den Pfad (key1, key2) fügt aber nichts hinzu.
	 * Alle Pfad, die es vorher nicht gab, verweisen dann auf leere Listen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 */
	public addEmpty(key1 : number, key2 : number) : void {
		const key12 : LongArrayKey = new LongArrayKey(key1, key2);
		MapUtils.getOrCreateArrayList(this._map12, key12);
		this._list.add(new Pair<LongArrayKey, V>(key12, null));
		if (this._map1 !== null)
			MapUtils.getOrCreateArrayList(this._map1, key1);
		if (this._map2 !== null)
			MapUtils.getOrCreateArrayList(this._map2, key2);
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
	 * Liefert TRUE, falls es den Schlüssel (key1, key2) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2) gibt.
	 */
	public containsKey12(key1 : number, key2 : number) : boolean {
		return this._map12.containsKey(new LongArrayKey(key1, key2));
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
		let list : List<V> | null = this._map1.get(key1);
		if (list === null)
			return new ArrayList();
		return new ArrayList<V>(list);
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
		let list : List<V> | null = this._map2.get(key2);
		if (list === null)
			return new ArrayList();
		return new ArrayList<V>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get12(key1 : number, key2 : number) : List<V> {
		const key12 : LongArrayKey = new LongArrayKey(key1, key2);
		let list : List<V> | null = this._map12.get(key12);
		if (list === null)
			return new ArrayList();
		return new ArrayList<V>(list);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle1OrNull(key1 : number) : V | null {
		if (this._map1 === null)
			this._map1 = this._lazyLoad1();
		let list : List<V> | null = this._map1.get(key1);
		if (list === null)
			return null;
		if (list.size() !== 1)
			return null;
		return list.getFirst();
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle2OrNull(key2 : number) : V | null {
		if (this._map2 === null)
			this._map2 = this._lazyLoad2();
		let list : List<V> | null = this._map2.get(key2);
		if (list === null)
			return null;
		if (list.size() !== 1)
			return null;
		return list.getFirst();
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle12OrNull(key1 : number, key2 : number) : V | null {
		const key12 : LongArrayKey = new LongArrayKey(key1, key2);
		let list : List<V> | null = this._map12.get(key12);
		if (list === null)
			return null;
		if (list.size() !== 1)
			return null;
		return list.getFirst();
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle1OrException(key1 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle1OrNull(key1));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle2OrException(key2 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle2OrNull(key2));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle12OrException(key1 : number, key2 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle12OrNull(key1, key2));
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
	 * Liefert das Key-Set der Map12.
	 *
	 * @return das Key-Set der Map12.
	 */
	public keySet12() : JavaSet<LongArrayKey> {
		return this._map12.keySet();
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1).
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get1OrException(key1 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey1(key1));
		return this.get1(key1);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2).
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get2OrException(key2 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey2(key2));
		return this.get2(key2);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get12OrException(key1 : number, key2 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey12(key1, key2));
		return this.get12(key1, key2);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.ListMap2DLongKeys';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.ListMap2DLongKeys'].includes(name);
	}

	public static class = new Class<ListMap2DLongKeys<any>>('de.svws_nrw.core.adt.map.ListMap2DLongKeys');

}

export function cast_de_svws_nrw_core_adt_map_ListMap2DLongKeys<V>(obj : unknown) : ListMap2DLongKeys<V> {
	return obj as ListMap2DLongKeys<V>;
}
