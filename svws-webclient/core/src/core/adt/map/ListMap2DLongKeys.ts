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

export class ListMap2DLongKeys<V> extends JavaObject {

	private _map1 : JavaMap<number, List<V>> | null = null;

	private _map2 : JavaMap<number, List<V>> | null = null;

	private readonly _map12 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();


	/**
	 * Konstruktor.
	 */
	public constructor() {
		super();
	}

	private getMap1() : JavaMap<number, List<V>> {
		if (this._map1 === null)
			this._map1 = this._lazyLoad1();
		return this._map1;
	}

	private getMap2() : JavaMap<number, List<V>> {
		if (this._map2 === null)
			this._map2 = this._lazyLoad2();
		return this._map2;
	}

	private _lazyLoad1() : JavaMap<number, List<V>> {
		const map : JavaMap<number, List<V>> | null = new HashMap<number, List<V>>();
		for (const entry12 of this._map12.entrySet()) {
			const key1 : number = entry12.getKey().getKeyAt(0);
			if (entry12.getValue().isEmpty())
				MapUtils.getOrCreateArrayList(map, key1);
			else
				MapUtils.getOrCreateArrayList(map, key1).addAll(entry12.getValue());
		}
		return map;
	}

	private _lazyLoad2() : JavaMap<number, List<V>> {
		const map : JavaMap<number, List<V>> | null = new HashMap<number, List<V>>();
		for (const entry12 of this._map12.entrySet()) {
			const key2 : number = entry12.getKey().getKeyAt(1);
			if (entry12.getValue().isEmpty())
				MapUtils.getOrCreateArrayList(map, key2);
			else
				MapUtils.getOrCreateArrayList(map, key2).addAll(entry12.getValue());
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
		if (this._map1 !== null)
			MapUtils.getOrCreateArrayList(this._map1, key1);
		if (this._map2 !== null)
			MapUtils.getOrCreateArrayList(this._map2, key2);
	}

	private invalidateCaches() : void {
		this._map1 = null;
		this._map2 = null;
	}

	/**
	 * Entfernt den Wert aus der zur Zuordnung (key1, key2) gehörenden Value-Liste.
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param value  Der zu entfernende Wert.
	 */
	public removeValueOrException(key1 : number, key2 : number, value : V) : void {
		const key : LongArrayKey = new LongArrayKey(key1, key2);
		MapUtils.removeFromListAndTrimOrException(this._map12, key, value);
		this.invalidateCaches();
	}

	/**
	 * Entfernt den Pfad (key1, key2) aus der Map.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return der bisherige Wert zu (key1, key2) oder {@code null}, falls nicht existent.
	 */
	public remove(key1 : number, key2 : number) : List<V> | null {
		const key : LongArrayKey = new LongArrayKey(key1, key2);
		const values : List<V> | null = this._map12.remove(key);
		if (values !== null)
			this.invalidateCaches();
		return values;
	}

	/**
	 * Entfernt den Pfad (key1, key2) aus der Map.
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return der bisherige Wert zu (key1, key2)
	 */
	public removeOrException(key1 : number, key2 : number) : List<V> {
		return DeveloperNotificationException.ifNull("Pfad (" + key1 + ", " + key2 + ") existiert nicht!", this.remove(key1, key2));
	}

	/**
	 * Entfernt den Pfad (key1, key2) aus der Map.
	 * Wirft eine DeveloperNotificationException, falls in der gemappten Liste das Element nicht als einziges enthalten ist.
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return der bisherige Wert zu (key1, key2)
	 */
	public removeSingleOrException(key1 : number, key2 : number) : V {
		const values : List<V> | null = this.removeOrException(key1, key2);
		DeveloperNotificationException.ifTrue("Pfad (" + key1 + ", " + key2 + ") enthält nicht genau ein Element (tatsächlich " + values.size() + ")!", values.size() !== 1);
		return values.getFirst();
	}

	/**
	 * Entfernt alle Einträge, bei denen der erste Schlüssel (key1) übereinstimmt.
	 * Falls kein Eintrag zu key1 existiert, passiert nichts.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 */
	public removeAllByKey1(key1 : number) : void {
		this.removeAllByKeyX(key1, 0);
	}

	/**
	 * Entfernt alle Einträge, bei denen der zweite Schlüssel (key2) übereinstimmt.
	 * Falls kein Eintrag zu key2 existiert, passiert nichts.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key2   Der 2. Schlüssel.
	 */
	public removeAllByKey2(key2 : number) : void {
		this.removeAllByKeyX(key2, 1);
	}

	private removeAllByKeyX(key : number, x : number) : void {
		const toRemove : List<LongArrayKey> | null = new ArrayList<LongArrayKey>();
		for (const keyEntry of this._map12.keySet())
			if (keyEntry.getKeyAt(x) === key)
				toRemove.add(keyEntry);
		for (const keyEntry of toRemove)
			this._map12.remove(keyEntry);
		if (!toRemove.isEmpty())
			this.invalidateCaches();
	}

	/**
	 * Gibt eine flache Liste aller Values in dieser 2D-ListMap zurück.
	 * Die Einfüge-Reihenfolge der einzelnen Listen bleibt erhalten.
	 *
	 * @return eine flache Liste aller enthaltenen Werte
	 */
	public getAllValues() : List<V> {
		const result : List<V> | null = new ArrayList<V>();
		for (const values of this._map12.values()) {
			result.addAll(values);
		}
		return result;
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1) gibt.
	 */
	public containsKey1(key1 : number) : boolean {
		return this.getMap1().containsKey(key1);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2) gibt.
	 */
	public containsKey2(key2 : number) : boolean {
		return this.getMap2().containsKey(key2);
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
		let list : List<V> | null = this.getMap1().get(key1);
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
		let list : List<V> | null = this.getMap2().get(key2);
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
		let list : List<V> | null = this.getMap1().get(key1);
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
		let list : List<V> | null = this.getMap2().get(key2);
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
		return this.getMap1().keySet();
	}

	/**
	 * Liefert das Key-Set der Map2.
	 *
	 * @return das Key-Set der Map2.
	 */
	public keySet2() : JavaSet<number> {
		return this.getMap2().keySet();
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
