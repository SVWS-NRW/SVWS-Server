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

export class ListMap5DLongKeys<V> extends JavaObject {

	private _map1 : JavaMap<number, List<V>> | null = null;

	private _map2 : JavaMap<number, List<V>> | null = null;

	private _map3 : JavaMap<number, List<V>> | null = null;

	private _map4 : JavaMap<number, List<V>> | null = null;

	private _map5 : JavaMap<number, List<V>> | null = null;

	private _map12 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map13 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map14 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map15 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map23 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map24 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map25 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map34 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map35 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map45 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map123 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map124 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map125 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map134 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map135 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map145 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map234 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map235 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map245 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map345 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map1234 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map1235 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map1245 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map1345 : JavaMap<LongArrayKey, List<V>> | null = null;

	private _map2345 : JavaMap<LongArrayKey, List<V>> | null = null;

	private readonly _map12345 : JavaMap<LongArrayKey, List<V>> = new HashMap<LongArrayKey, List<V>>();

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

	private _lazyLoad4() : JavaMap<number, List<V>> {
		const map : JavaMap<number, List<V>> | null = new HashMap<number, List<V>>();
		for (const e of this._list) {
			const key4 : number = e.a.getKeyAt(3);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, key4);
			else
				MapUtils.getOrCreateArrayList(map, key4).add(e.b);
		}
		return map;
	}

	private _lazyLoad5() : JavaMap<number, List<V>> {
		const map : JavaMap<number, List<V>> | null = new HashMap<number, List<V>>();
		for (const e of this._list) {
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, key5);
			else
				MapUtils.getOrCreateArrayList(map, key5).add(e.b);
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

	private _lazyLoad14() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key4 : number = e.a.getKeyAt(3);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4)).add(e.b);
		}
		return map;
	}

	private _lazyLoad15() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key5)).add(e.b);
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

	private _lazyLoad24() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key2 : number = e.a.getKeyAt(1);
			const key4 : number = e.a.getKeyAt(3);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4)).add(e.b);
		}
		return map;
	}

	private _lazyLoad25() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key2 : number = e.a.getKeyAt(1);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad34() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key3 : number = e.a.getKeyAt(2);
			const key4 : number = e.a.getKeyAt(3);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4)).add(e.b);
		}
		return map;
	}

	private _lazyLoad35() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key3 : number = e.a.getKeyAt(2);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad45() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key4 : number = e.a.getKeyAt(3);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key4, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad125() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key2 : number = e.a.getKeyAt(1);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad135() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key3 : number = e.a.getKeyAt(2);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad145() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key4 : number = e.a.getKeyAt(3);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad235() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key2 : number = e.a.getKeyAt(1);
			const key3 : number = e.a.getKeyAt(2);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad245() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key2 : number = e.a.getKeyAt(1);
			const key4 : number = e.a.getKeyAt(3);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad123() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key2 : number = e.a.getKeyAt(1);
			const key3 : number = e.a.getKeyAt(2);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3)).add(e.b);
		}
		return map;
	}

	private _lazyLoad124() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key2 : number = e.a.getKeyAt(1);
			const key4 : number = e.a.getKeyAt(3);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4)).add(e.b);
		}
		return map;
	}

	private _lazyLoad134() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key3 : number = e.a.getKeyAt(2);
			const key4 : number = e.a.getKeyAt(3);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4)).add(e.b);
		}
		return map;
	}

	private _lazyLoad234() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key2 : number = e.a.getKeyAt(1);
			const key3 : number = e.a.getKeyAt(2);
			const key4 : number = e.a.getKeyAt(3);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4)).add(e.b);
		}
		return map;
	}

	private _lazyLoad1234() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key2 : number = e.a.getKeyAt(1);
			const key3 : number = e.a.getKeyAt(2);
			const key4 : number = e.a.getKeyAt(3);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key4)).add(e.b);
		}
		return map;
	}

	private _lazyLoad1235() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key2 : number = e.a.getKeyAt(1);
			const key3 : number = e.a.getKeyAt(2);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad1245() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key2 : number = e.a.getKeyAt(1);
			const key4 : number = e.a.getKeyAt(3);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad1345() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key3 : number = e.a.getKeyAt(2);
			const key4 : number = e.a.getKeyAt(3);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad2345() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key2 : number = e.a.getKeyAt(1);
			const key3 : number = e.a.getKeyAt(2);
			const key4 : number = e.a.getKeyAt(3);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad12345() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key1 : number = e.a.getKeyAt(0);
			const key2 : number = e.a.getKeyAt(1);
			const key3 : number = e.a.getKeyAt(2);
			const key4 : number = e.a.getKeyAt(3);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key4, key5)).add(e.b);
		}
		return map;
	}

	private _lazyLoad345() : JavaMap<LongArrayKey, List<V>> {
		const map : JavaMap<LongArrayKey, List<V>> | null = new HashMap<LongArrayKey, List<V>>();
		for (const e of this._list) {
			const key3 : number = e.a.getKeyAt(2);
			const key4 : number = e.a.getKeyAt(3);
			const key5 : number = e.a.getKeyAt(4);
			if (e.b === null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4, key5)).add(e.b);
		}
		return map;
	}

	private getSingleOrNullHelperLong(map : JavaMap<number, List<V>>, key : number) : V | null {
		let list : List<V> | null = map.get(key);
		if (list === null)
			return null;
		if (list.size() !== 1)
			return null;
		return list.getFirst();
	}

	private getSingleOrNullHelperLongArray(map : JavaMap<LongArrayKey, List<V>>, key : LongArrayKey) : V | null {
		let list : List<V> | null = map.get(key);
		if (list === null)
			return null;
		if (list.size() !== 1)
			return null;
		return list.getFirst();
	}

	/**
	 * Fügt das Element hinzu.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 * @param key4  Der 4. Schlüssel.
	 * @param key5  Der 5. Schlüssel.
	 *
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public add(key1 : number, key2 : number, key3 : number, key4 : number, key5 : number, value : V) : void {
		const key : LongArrayKey = new LongArrayKey(key1, key2, key3, key4, key5);
		MapUtils.getOrCreateArrayList(this._map12345, key).add(value);
		this._list.add(new Pair<LongArrayKey, V>(key, value));
		if (this._map1 !== null)
			MapUtils.getOrCreateArrayList(this._map1, key1).add(value);
		if (this._map2 !== null)
			MapUtils.getOrCreateArrayList(this._map2, key2).add(value);
		if (this._map3 !== null)
			MapUtils.getOrCreateArrayList(this._map3, key3).add(value);
		if (this._map4 !== null)
			MapUtils.getOrCreateArrayList(this._map4, key4).add(value);
		if (this._map5 !== null)
			MapUtils.getOrCreateArrayList(this._map5, key5).add(value);
		if (this._map12 !== null)
			MapUtils.getOrCreateArrayList(this._map12, new LongArrayKey(key1, key2)).add(value);
		if (this._map13 !== null)
			MapUtils.getOrCreateArrayList(this._map13, new LongArrayKey(key1, key3)).add(value);
		if (this._map14 !== null)
			MapUtils.getOrCreateArrayList(this._map14, new LongArrayKey(key1, key4)).add(value);
		if (this._map15 !== null)
			MapUtils.getOrCreateArrayList(this._map15, new LongArrayKey(key1, key5)).add(value);
		if (this._map23 !== null)
			MapUtils.getOrCreateArrayList(this._map23, new LongArrayKey(key2, key3)).add(value);
		if (this._map24 !== null)
			MapUtils.getOrCreateArrayList(this._map24, new LongArrayKey(key2, key4)).add(value);
		if (this._map25 !== null)
			MapUtils.getOrCreateArrayList(this._map25, new LongArrayKey(key2, key5)).add(value);
		if (this._map34 !== null)
			MapUtils.getOrCreateArrayList(this._map34, new LongArrayKey(key3, key4)).add(value);
		if (this._map35 !== null)
			MapUtils.getOrCreateArrayList(this._map35, new LongArrayKey(key3, key5)).add(value);
		if (this._map45 !== null)
			MapUtils.getOrCreateArrayList(this._map45, new LongArrayKey(key4, key5)).add(value);
		if (this._map123 !== null)
			MapUtils.getOrCreateArrayList(this._map123, new LongArrayKey(key1, key2, key3)).add(value);
		if (this._map124 !== null)
			MapUtils.getOrCreateArrayList(this._map124, new LongArrayKey(key1, key2, key4)).add(value);
		if (this._map125 !== null)
			MapUtils.getOrCreateArrayList(this._map125, new LongArrayKey(key1, key2, key5)).add(value);
		if (this._map134 !== null)
			MapUtils.getOrCreateArrayList(this._map134, new LongArrayKey(key1, key3, key4)).add(value);
		if (this._map135 !== null)
			MapUtils.getOrCreateArrayList(this._map135, new LongArrayKey(key1, key3, key5)).add(value);
		if (this._map145 !== null)
			MapUtils.getOrCreateArrayList(this._map145, new LongArrayKey(key1, key4, key5)).add(value);
		if (this._map234 !== null)
			MapUtils.getOrCreateArrayList(this._map234, new LongArrayKey(key2, key3, key4)).add(value);
		if (this._map235 !== null)
			MapUtils.getOrCreateArrayList(this._map235, new LongArrayKey(key2, key3, key5)).add(value);
		if (this._map245 !== null)
			MapUtils.getOrCreateArrayList(this._map245, new LongArrayKey(key2, key4, key5)).add(value);
		if (this._map345 !== null)
			MapUtils.getOrCreateArrayList(this._map345, new LongArrayKey(key3, key4, key5)).add(value);
		if (this._map1234 !== null)
			MapUtils.getOrCreateArrayList(this._map1234, new LongArrayKey(key1, key2, key3, key4)).add(value);
		if (this._map1235 !== null)
			MapUtils.getOrCreateArrayList(this._map1235, new LongArrayKey(key1, key2, key3, key5)).add(value);
		if (this._map1245 !== null)
			MapUtils.getOrCreateArrayList(this._map1245, new LongArrayKey(key1, key2, key4, key5)).add(value);
		if (this._map1345 !== null)
			MapUtils.getOrCreateArrayList(this._map1345, new LongArrayKey(key1, key3, key4, key5)).add(value);
		if (this._map2345 !== null)
			MapUtils.getOrCreateArrayList(this._map2345, new LongArrayKey(key2, key3, key4, key5)).add(value);
	}

	/**
	 * Erzeugt den Pfad (key1, key2, key3, key4, key5) fügt aber nichts hinzu.
	 * Alle Pfad, die es vorher nicht gab, verweisen dann auf leere Listen.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 * @param key4  Der 4. Schlüssel.
	 * @param key5  Der 5. Schlüssel.
	 */
	public addEmpty(key1 : number, key2 : number, key3 : number, key4 : number, key5 : number) : void {
		const key : LongArrayKey = new LongArrayKey(key1, key2, key3, key4, key5);
		MapUtils.getOrCreateArrayList(this._map12345, key);
		this._list.add(new Pair<LongArrayKey, V>(key, null));
		if (this._map1 !== null)
			MapUtils.getOrCreateArrayList(this._map1, key1);
		if (this._map2 !== null)
			MapUtils.getOrCreateArrayList(this._map2, key2);
		if (this._map3 !== null)
			MapUtils.getOrCreateArrayList(this._map3, key3);
		if (this._map4 !== null)
			MapUtils.getOrCreateArrayList(this._map4, key4);
		if (this._map5 !== null)
			MapUtils.getOrCreateArrayList(this._map5, key5);
		if (this._map12 !== null)
			MapUtils.getOrCreateArrayList(this._map12, new LongArrayKey(key1, key2));
		if (this._map13 !== null)
			MapUtils.getOrCreateArrayList(this._map13, new LongArrayKey(key1, key3));
		if (this._map14 !== null)
			MapUtils.getOrCreateArrayList(this._map14, new LongArrayKey(key1, key4));
		if (this._map15 !== null)
			MapUtils.getOrCreateArrayList(this._map15, new LongArrayKey(key1, key5));
		if (this._map23 !== null)
			MapUtils.getOrCreateArrayList(this._map23, new LongArrayKey(key2, key3));
		if (this._map24 !== null)
			MapUtils.getOrCreateArrayList(this._map24, new LongArrayKey(key2, key4));
		if (this._map25 !== null)
			MapUtils.getOrCreateArrayList(this._map25, new LongArrayKey(key2, key5));
		if (this._map34 !== null)
			MapUtils.getOrCreateArrayList(this._map34, new LongArrayKey(key3, key4));
		if (this._map35 !== null)
			MapUtils.getOrCreateArrayList(this._map35, new LongArrayKey(key3, key5));
		if (this._map45 !== null)
			MapUtils.getOrCreateArrayList(this._map45, new LongArrayKey(key4, key5));
		if (this._map123 !== null)
			MapUtils.getOrCreateArrayList(this._map123, new LongArrayKey(key1, key2, key3));
		if (this._map124 !== null)
			MapUtils.getOrCreateArrayList(this._map124, new LongArrayKey(key1, key2, key4));
		if (this._map125 !== null)
			MapUtils.getOrCreateArrayList(this._map125, new LongArrayKey(key1, key2, key5));
		if (this._map134 !== null)
			MapUtils.getOrCreateArrayList(this._map134, new LongArrayKey(key1, key3, key4));
		if (this._map135 !== null)
			MapUtils.getOrCreateArrayList(this._map135, new LongArrayKey(key1, key3, key5));
		if (this._map145 !== null)
			MapUtils.getOrCreateArrayList(this._map145, new LongArrayKey(key1, key4, key5));
		if (this._map234 !== null)
			MapUtils.getOrCreateArrayList(this._map234, new LongArrayKey(key2, key3, key4));
		if (this._map235 !== null)
			MapUtils.getOrCreateArrayList(this._map235, new LongArrayKey(key2, key3, key5));
		if (this._map245 !== null)
			MapUtils.getOrCreateArrayList(this._map245, new LongArrayKey(key2, key4, key5));
		if (this._map345 !== null)
			MapUtils.getOrCreateArrayList(this._map345, new LongArrayKey(key3, key4, key5));
		if (this._map1234 !== null)
			MapUtils.getOrCreateArrayList(this._map1234, new LongArrayKey(key1, key2, key3, key4));
		if (this._map1235 !== null)
			MapUtils.getOrCreateArrayList(this._map1235, new LongArrayKey(key1, key2, key3, key5));
		if (this._map1245 !== null)
			MapUtils.getOrCreateArrayList(this._map1245, new LongArrayKey(key1, key2, key4, key5));
		if (this._map1345 !== null)
			MapUtils.getOrCreateArrayList(this._map1345, new LongArrayKey(key1, key3, key4, key5));
		if (this._map2345 !== null)
			MapUtils.getOrCreateArrayList(this._map2345, new LongArrayKey(key2, key3, key4, key5));
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
	 * Liefert TRUE, falls es den Schlüssel (key4) gibt.
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key4) gibt.
	 */
	public containsKey4(key4 : number) : boolean {
		if (this._map4 === null)
			this._map4 = this._lazyLoad4();
		return this._map4.containsKey(key4);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key5) gibt.
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key5) gibt.
	 */
	public containsKey5(key5 : number) : boolean {
		if (this._map5 === null)
			this._map5 = this._lazyLoad5();
		return this._map5.containsKey(key5);
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
	 * Liefert TRUE, falls es den Schlüssel (key1, key4) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key4) gibt.
	 */
	public containsKey14(key1 : number, key4 : number) : boolean {
		if (this._map14 === null)
			this._map14 = this._lazyLoad14();
		return this._map14.containsKey(new LongArrayKey(key1, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key5) gibt.
	 */
	public containsKey15(key1 : number, key5 : number) : boolean {
		if (this._map15 === null)
			this._map15 = this._lazyLoad15();
		return this._map15.containsKey(new LongArrayKey(key1, key5));
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
	 * Liefert TRUE, falls es den Schlüssel (key2, key4) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key4) gibt.
	 */
	public containsKey24(key2 : number, key4 : number) : boolean {
		if (this._map24 === null)
			this._map24 = this._lazyLoad24();
		return this._map24.containsKey(new LongArrayKey(key2, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key5) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key5) gibt.
	 */
	public containsKey25(key2 : number, key5 : number) : boolean {
		if (this._map25 === null)
			this._map25 = this._lazyLoad25();
		return this._map25.containsKey(new LongArrayKey(key2, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key3, key4) gibt.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key3, key4) gibt.
	 */
	public containsKey34(key3 : number, key4 : number) : boolean {
		if (this._map34 === null)
			this._map34 = this._lazyLoad34();
		return this._map34.containsKey(new LongArrayKey(key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key3, key5) gibt.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key3, key5) gibt.
	 */
	public containsKey35(key3 : number, key5 : number) : boolean {
		if (this._map35 === null)
			this._map35 = this._lazyLoad35();
		return this._map35.containsKey(new LongArrayKey(key3, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key4, key5) gibt.
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key4, key5) gibt.
	 */
	public containsKey45(key4 : number, key5 : number) : boolean {
		if (this._map45 === null)
			this._map45 = this._lazyLoad45();
		return this._map45.containsKey(new LongArrayKey(key4, key5));
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
		if (this._map123 === null)
			this._map123 = this._lazyLoad123();
		return this._map123.containsKey(new LongArrayKey(key1, key2, key3));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key4) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key4) gibt.
	 */
	public containsKey124(key1 : number, key2 : number, key4 : number) : boolean {
		if (this._map124 === null)
			this._map124 = this._lazyLoad124();
		return this._map124.containsKey(new LongArrayKey(key1, key2, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key5) gibt.
	 */
	public containsKey125(key1 : number, key2 : number, key5 : number) : boolean {
		if (this._map125 === null)
			this._map125 = this._lazyLoad125();
		return this._map125.containsKey(new LongArrayKey(key1, key2, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key3, key4) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key3, key4) gibt.
	 */
	public containsKey134(key1 : number, key3 : number, key4 : number) : boolean {
		if (this._map134 === null)
			this._map134 = this._lazyLoad134();
		return this._map134.containsKey(new LongArrayKey(key1, key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key3, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key3, key5) gibt.
	 */
	public containsKey135(key1 : number, key3 : number, key5 : number) : boolean {
		if (this._map135 === null)
			this._map135 = this._lazyLoad135();
		return this._map135.containsKey(new LongArrayKey(key1, key3, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key4, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key4, key5) gibt.
	 */
	public containsKey145(key1 : number, key4 : number, key5 : number) : boolean {
		if (this._map145 === null)
			this._map145 = this._lazyLoad145();
		return this._map145.containsKey(new LongArrayKey(key1, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key3, key4) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key3, key4) gibt.
	 */
	public containsKey234(key2 : number, key3 : number, key4 : number) : boolean {
		if (this._map234 === null)
			this._map234 = this._lazyLoad234();
		return this._map234.containsKey(new LongArrayKey(key2, key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key3, key5) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key3, key5) gibt.
	 */
	public containsKey235(key2 : number, key3 : number, key5 : number) : boolean {
		if (this._map235 === null)
			this._map235 = this._lazyLoad235();
		return this._map235.containsKey(new LongArrayKey(key2, key3, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key4, key5) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key4, key5) gibt.
	 */
	public containsKey245(key2 : number, key4 : number, key5 : number) : boolean {
		if (this._map245 === null)
			this._map245 = this._lazyLoad245();
		return this._map245.containsKey(new LongArrayKey(key2, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key3, key4, key5) gibt.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key3, key4, key5) gibt.
	 */
	public containsKey345(key3 : number, key4 : number, key5 : number) : boolean {
		if (this._map345 === null)
			this._map345 = this._lazyLoad345();
		return this._map345.containsKey(new LongArrayKey(key3, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key3, key4) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key3, key4) gibt.
	 */
	public containsKey1234(key1 : number, key2 : number, key3 : number, key4 : number) : boolean {
		if (this._map1234 === null)
			this._map1234 = this._lazyLoad1234();
		return this._map1234.containsKey(new LongArrayKey(key1, key2, key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key3, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key3, key5) gibt.
	 */
	public containsKey1235(key1 : number, key2 : number, key3 : number, key5 : number) : boolean {
		if (this._map1235 === null)
			this._map1235 = this._lazyLoad1235();
		return this._map1235.containsKey(new LongArrayKey(key1, key2, key3, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key4, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key4, key5) gibt.
	 */
	public containsKey1245(key1 : number, key2 : number, key4 : number, key5 : number) : boolean {
		if (this._map1245 === null)
			this._map1245 = this._lazyLoad1245();
		return this._map1245.containsKey(new LongArrayKey(key1, key2, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key3, key4, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key3, key4, key5) gibt.
	 */
	public containsKey1345(key1 : number, key3 : number, key4 : number, key5 : number) : boolean {
		if (this._map1345 === null)
			this._map1345 = this._lazyLoad1345();
		return this._map1345.containsKey(new LongArrayKey(key1, key3, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key3, key4, key5) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key3, key4, key5) gibt.
	 */
	public containsKey2345(key2 : number, key3 : number, key4 : number, key5 : number) : boolean {
		if (this._map2345 === null)
			this._map2345 = this._lazyLoad2345();
		return this._map2345.containsKey(new LongArrayKey(key2, key3, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key3, key4, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (ky1, key2, key3, key4, key5) gibt.
	 */
	public containsKey12345(key1 : number, key2 : number, key3 : number, key4 : number, key5 : number) : boolean {
		return this._map12345.containsKey(new LongArrayKey(key1, key2, key3, key4, key5));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
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
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
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
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get3(key3 : number) : List<V> {
		if (this._map3 === null)
			this._map3 = this._lazyLoad3();
		if (!this._map3.containsKey(key3))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map3, key3));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get4(key4 : number) : List<V> {
		if (this._map4 === null)
			this._map4 = this._lazyLoad4();
		if (!this._map4.containsKey(key4))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map4, key4));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get5(key5 : number) : List<V> {
		if (this._map5 === null)
			this._map5 = this._lazyLoad5();
		if (!this._map5.containsKey(key5))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map5, key5));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get12(key1 : number, key2 : number) : List<V> {
		if (this._map12 === null)
			this._map12 = this._lazyLoad12();
		const key : LongArrayKey | null = new LongArrayKey(key1, key2);
		if (!this._map12.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map12, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get13(key1 : number, key3 : number) : List<V> {
		if (this._map13 === null)
			this._map13 = this._lazyLoad13();
		const key : LongArrayKey | null = new LongArrayKey(key1, key3);
		if (!this._map13.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map13, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get14(key1 : number, key4 : number) : List<V> {
		if (this._map14 === null)
			this._map14 = this._lazyLoad14();
		const key : LongArrayKey | null = new LongArrayKey(key1, key4);
		if (!this._map14.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map14, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get15(key1 : number, key5 : number) : List<V> {
		if (this._map15 === null)
			this._map15 = this._lazyLoad15();
		const key : LongArrayKey | null = new LongArrayKey(key1, key5);
		if (!this._map15.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map15, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get23(key2 : number, key3 : number) : List<V> {
		if (this._map23 === null)
			this._map23 = this._lazyLoad23();
		const key : LongArrayKey | null = new LongArrayKey(key2, key3);
		if (!this._map23.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map23, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get24(key2 : number, key4 : number) : List<V> {
		if (this._map24 === null)
			this._map24 = this._lazyLoad24();
		const key : LongArrayKey | null = new LongArrayKey(key2, key4);
		if (!this._map24.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map24, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get25(key2 : number, key5 : number) : List<V> {
		if (this._map25 === null)
			this._map25 = this._lazyLoad25();
		const key : LongArrayKey | null = new LongArrayKey(key2, key5);
		if (!this._map25.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map25, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get34(key3 : number, key4 : number) : List<V> {
		if (this._map34 === null)
			this._map34 = this._lazyLoad34();
		const key : LongArrayKey | null = new LongArrayKey(key3, key4);
		if (!this._map34.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map34, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get35(key3 : number, key5 : number) : List<V> {
		if (this._map35 === null)
			this._map35 = this._lazyLoad35();
		const key : LongArrayKey | null = new LongArrayKey(key3, key5);
		if (!this._map35.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map35, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get45(key4 : number, key5 : number) : List<V> {
		if (this._map45 === null)
			this._map45 = this._lazyLoad45();
		const key : LongArrayKey | null = new LongArrayKey(key4, key5);
		if (!this._map45.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map45, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get123(key1 : number, key2 : number, key3 : number) : List<V> {
		if (this._map123 === null)
			this._map123 = this._lazyLoad123();
		const key : LongArrayKey | null = new LongArrayKey(key1, key2, key3);
		if (!this._map123.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map123, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get124(key1 : number, key2 : number, key4 : number) : List<V> {
		if (this._map124 === null)
			this._map124 = this._lazyLoad124();
		const key : LongArrayKey | null = new LongArrayKey(key1, key2, key4);
		if (!this._map124.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map124, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get125(key1 : number, key2 : number, key5 : number) : List<V> {
		if (this._map125 === null)
			this._map125 = this._lazyLoad125();
		const key : LongArrayKey | null = new LongArrayKey(key1, key2, key5);
		if (!this._map125.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map125, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get134(key1 : number, key3 : number, key4 : number) : List<V> {
		if (this._map134 === null)
			this._map134 = this._lazyLoad134();
		const key : LongArrayKey | null = new LongArrayKey(key1, key3, key4);
		if (!this._map134.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map134, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get135(key1 : number, key3 : number, key5 : number) : List<V> {
		if (this._map135 === null)
			this._map135 = this._lazyLoad135();
		const key : LongArrayKey | null = new LongArrayKey(key1, key3, key5);
		if (!this._map135.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map135, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get145(key1 : number, key4 : number, key5 : number) : List<V> {
		if (this._map145 === null)
			this._map145 = this._lazyLoad145();
		const key : LongArrayKey | null = new LongArrayKey(key1, key4, key5);
		if (!this._map145.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map145, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get234(key2 : number, key3 : number, key4 : number) : List<V> {
		if (this._map234 === null)
			this._map234 = this._lazyLoad234();
		const key : LongArrayKey | null = new LongArrayKey(key2, key3, key4);
		if (!this._map234.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map234, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get235(key2 : number, key3 : number, key5 : number) : List<V> {
		if (this._map235 === null)
			this._map235 = this._lazyLoad235();
		const key : LongArrayKey | null = new LongArrayKey(key2, key3, key5);
		if (!this._map235.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map235, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get245(key2 : number, key4 : number, key5 : number) : List<V> {
		if (this._map245 === null)
			this._map245 = this._lazyLoad245();
		const key : LongArrayKey | null = new LongArrayKey(key2, key4, key5);
		if (!this._map245.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map245, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get345(key3 : number, key4 : number, key5 : number) : List<V> {
		if (this._map345 === null)
			this._map345 = this._lazyLoad345();
		const key : LongArrayKey | null = new LongArrayKey(key3, key4, key5);
		if (!this._map345.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map345, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get1234(key1 : number, key2 : number, key3 : number, key4 : number) : List<V> {
		if (this._map1234 === null)
			this._map1234 = this._lazyLoad1234();
		const key : LongArrayKey | null = new LongArrayKey(key1, key2, key3, key4);
		if (!this._map1234.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map1234, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get1235(key1 : number, key2 : number, key3 : number, key5 : number) : List<V> {
		if (this._map1235 === null)
			this._map1235 = this._lazyLoad1235();
		const key : LongArrayKey | null = new LongArrayKey(key1, key2, key3, key5);
		if (!this._map1235.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map1235, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get1245(key1 : number, key2 : number, key4 : number, key5 : number) : List<V> {
		if (this._map1245 === null)
			this._map1245 = this._lazyLoad1245();
		const key : LongArrayKey | null = new LongArrayKey(key1, key2, key4, key5);
		if (!this._map1245.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map1245, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get1345(key1 : number, key3 : number, key4 : number, key5 : number) : List<V> {
		if (this._map1345 === null)
			this._map1345 = this._lazyLoad1345();
		const key : LongArrayKey | null = new LongArrayKey(key1, key3, key4, key5);
		if (!this._map1345.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map1345, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get2345(key2 : number, key3 : number, key4 : number, key5 : number) : List<V> {
		if (this._map2345 === null)
			this._map2345 = this._lazyLoad2345();
		const key : LongArrayKey | null = new LongArrayKey(key2, key3, key4, key5);
		if (!this._map2345.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map2345, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public get12345(key1 : number, key2 : number, key3 : number, key4 : number, key5 : number) : List<V> {
		const key : LongArrayKey | null = new LongArrayKey(key1, key2, key3, key4, key5);
		if (!this._map12345.containsKey(key))
			return new ArrayList();
		return new ArrayList<V>(MapUtils.getOrCreateArrayList(this._map12345, key));
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
		return this.getSingleOrNullHelperLong(this._map1, key1);
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
		return this.getSingleOrNullHelperLong(this._map2, key2);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle3OrNull(key3 : number) : V | null {
		if (this._map3 === null)
			this._map3 = this._lazyLoad3();
		return this.getSingleOrNullHelperLong(this._map3, key3);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle4OrNull(key4 : number) : V | null {
		if (this._map4 === null)
			this._map4 = this._lazyLoad4();
		return this.getSingleOrNullHelperLong(this._map4, key4);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle5OrNull(key5 : number) : V | null {
		if (this._map5 === null)
			this._map5 = this._lazyLoad5();
		return this.getSingleOrNullHelperLong(this._map5, key5);
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
		if (this._map12 === null)
			this._map12 = this._lazyLoad12();
		return this.getSingleOrNullHelperLongArray(this._map12, new LongArrayKey(key1, key2));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle13OrNull(key1 : number, key3 : number) : V | null {
		if (this._map13 === null)
			this._map13 = this._lazyLoad13();
		return this.getSingleOrNullHelperLongArray(this._map13, new LongArrayKey(key1, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle14OrNull(key1 : number, key4 : number) : V | null {
		if (this._map14 === null)
			this._map14 = this._lazyLoad14();
		return this.getSingleOrNullHelperLongArray(this._map14, new LongArrayKey(key1, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle15OrNull(key1 : number, key5 : number) : V | null {
		if (this._map15 === null)
			this._map15 = this._lazyLoad15();
		return this.getSingleOrNullHelperLongArray(this._map15, new LongArrayKey(key1, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle23OrNull(key2 : number, key3 : number) : V | null {
		if (this._map23 === null)
			this._map23 = this._lazyLoad23();
		return this.getSingleOrNullHelperLongArray(this._map23, new LongArrayKey(key2, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle24OrNull(key2 : number, key4 : number) : V | null {
		if (this._map24 === null)
			this._map24 = this._lazyLoad24();
		return this.getSingleOrNullHelperLongArray(this._map24, new LongArrayKey(key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle25OrNull(key2 : number, key5 : number) : V | null {
		if (this._map25 === null)
			this._map25 = this._lazyLoad25();
		return this.getSingleOrNullHelperLongArray(this._map25, new LongArrayKey(key2, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle34OrNull(key3 : number, key4 : number) : V | null {
		if (this._map34 === null)
			this._map34 = this._lazyLoad34();
		return this.getSingleOrNullHelperLongArray(this._map34, new LongArrayKey(key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle35OrNull(key3 : number, key5 : number) : V | null {
		if (this._map35 === null)
			this._map35 = this._lazyLoad35();
		return this.getSingleOrNullHelperLongArray(this._map35, new LongArrayKey(key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle45OrNull(key4 : number, key5 : number) : V | null {
		if (this._map45 === null)
			this._map45 = this._lazyLoad45();
		return this.getSingleOrNullHelperLongArray(this._map45, new LongArrayKey(key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle123OrNull(key1 : number, key2 : number, key3 : number) : V | null {
		if (this._map123 === null)
			this._map123 = this._lazyLoad123();
		return this.getSingleOrNullHelperLongArray(this._map123, new LongArrayKey(key1, key2, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle124OrNull(key1 : number, key2 : number, key4 : number) : V | null {
		if (this._map124 === null)
			this._map124 = this._lazyLoad124();
		return this.getSingleOrNullHelperLongArray(this._map124, new LongArrayKey(key1, key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle125OrNull(key1 : number, key2 : number, key5 : number) : V | null {
		if (this._map125 === null)
			this._map125 = this._lazyLoad125();
		return this.getSingleOrNullHelperLongArray(this._map125, new LongArrayKey(key1, key2, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle134OrNull(key1 : number, key3 : number, key4 : number) : V | null {
		if (this._map134 === null)
			this._map134 = this._lazyLoad134();
		return this.getSingleOrNullHelperLongArray(this._map134, new LongArrayKey(key1, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle135OrNull(key1 : number, key3 : number, key5 : number) : V | null {
		if (this._map135 === null)
			this._map135 = this._lazyLoad135();
		return this.getSingleOrNullHelperLongArray(this._map135, new LongArrayKey(key1, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle145OrNull(key1 : number, key4 : number, key5 : number) : V | null {
		if (this._map145 === null)
			this._map145 = this._lazyLoad145();
		return this.getSingleOrNullHelperLongArray(this._map145, new LongArrayKey(key1, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle234OrNull(key2 : number, key3 : number, key4 : number) : V | null {
		if (this._map234 === null)
			this._map234 = this._lazyLoad234();
		return this.getSingleOrNullHelperLongArray(this._map234, new LongArrayKey(key2, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle235OrNull(key2 : number, key3 : number, key5 : number) : V | null {
		if (this._map235 === null)
			this._map235 = this._lazyLoad235();
		return this.getSingleOrNullHelperLongArray(this._map235, new LongArrayKey(key2, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle245OrNull(key2 : number, key4 : number, key5 : number) : V | null {
		if (this._map245 === null)
			this._map245 = this._lazyLoad245();
		return this.getSingleOrNullHelperLongArray(this._map245, new LongArrayKey(key2, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle345OrNull(key3 : number, key4 : number, key5 : number) : V | null {
		if (this._map345 === null)
			this._map345 = this._lazyLoad345();
		return this.getSingleOrNullHelperLongArray(this._map345, new LongArrayKey(key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle1234OrNull(key1 : number, key2 : number, key3 : number, key4 : number) : V | null {
		if (this._map1234 === null)
			this._map1234 = this._lazyLoad1234();
		return this.getSingleOrNullHelperLongArray(this._map1234, new LongArrayKey(key1, key2, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle1235OrNull(key1 : number, key2 : number, key3 : number, key5 : number) : V | null {
		if (this._map1235 === null)
			this._map1235 = this._lazyLoad1235();
		return this.getSingleOrNullHelperLongArray(this._map1235, new LongArrayKey(key1, key2, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle1245OrNull(key1 : number, key2 : number, key4 : number, key5 : number) : V | null {
		if (this._map1245 === null)
			this._map1245 = this._lazyLoad1245();
		return this.getSingleOrNullHelperLongArray(this._map1245, new LongArrayKey(key1, key2, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle1345OrNull(key1 : number, key3 : number, key4 : number, key5 : number) : V | null {
		if (this._map1345 === null)
			this._map1345 = this._lazyLoad1345();
		return this.getSingleOrNullHelperLongArray(this._map1345, new LongArrayKey(key1, key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle2345OrNull(key2 : number, key3 : number, key4 : number, key5 : number) : V | null {
		if (this._map2345 === null)
			this._map2345 = this._lazyLoad2345();
		return this.getSingleOrNullHelperLongArray(this._map2345, new LongArrayKey(key2, key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public getSingle12345OrNull(key1 : number, key2 : number, key3 : number, key4 : number, key5 : number) : V | null {
		return this.getSingleOrNullHelperLongArray(this._map12345, new LongArrayKey(key1, key2, key3, key4, key5));
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
	 * Liefert das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle3OrException(key3 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle3OrNull(key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle4OrException(key4 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle4OrNull(key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle5OrException(key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle5OrNull(key5));
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
	 * Liefert das zugeordnete Element zum Mapping (key1, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle13OrException(key1 : number, key3 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle13OrNull(key1, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle14OrException(key1 : number, key4 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle14OrNull(key1, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle15OrException(key1 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle15OrNull(key1, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle23OrException(key2 : number, key3 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle23OrNull(key2, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle24OrException(key2 : number, key4 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle24OrNull(key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle25OrException(key2 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle25OrNull(key2, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle34OrException(key3 : number, key4 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle34OrNull(key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle35OrException(key3 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle35OrNull(key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle45OrException(key4 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle45OrNull(key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle123OrException(key1 : number, key2 : number, key3 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle123OrNull(key1, key2, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle124OrException(key1 : number, key2 : number, key4 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle124OrNull(key1, key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle125OrException(key1 : number, key2 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle125OrNull(key1, key2, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle134OrException(key1 : number, key3 : number, key4 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle134OrNull(key1, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle135OrException(key1 : number, key3 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle135OrNull(key1, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle145OrException(key1 : number, key4 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle145OrNull(key1, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle234OrException(key2 : number, key3 : number, key4 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle234OrNull(key2, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle235OrException(key2 : number, key3 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle235OrNull(key2, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle245OrException(key2 : number, key4 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle245OrNull(key2, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle345OrException(key3 : number, key4 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle345OrNull(key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle1234OrException(key1 : number, key2 : number, key3 : number, key4 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle1234OrNull(key1, key2, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle1235OrException(key1 : number, key2 : number, key3 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle1235OrNull(key1, key2, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle1245OrException(key1 : number, key2 : number, key4 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle1245OrNull(key1, key2, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle1345OrException(key1 : number, key3 : number, key4 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle1345OrNull(key1, key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle2345OrException(key2 : number, key3 : number, key4 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle2345OrNull(key2, key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public getSingle12345OrException(key1 : number, key2 : number, key3 : number, key4 : number, key5 : number) : V {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", this.getSingle12345OrNull(key1, key2, key3, key4, key5));
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
	 * Liefert das Key-Set der Map4.
	 *
	 * @return das Key-Set der Map4.
	 */
	public keySet4() : JavaSet<number> {
		if (this._map4 === null)
			this._map4 = this._lazyLoad4();
		return this._map4.keySet();
	}

	/**
	 * Liefert das Key-Set der Map5.
	 *
	 * @return das Key-Set der Map5.
	 */
	public keySet5() : JavaSet<number> {
		if (this._map5 === null)
			this._map5 = this._lazyLoad5();
		return this._map5.keySet();
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
	 * Liefert das Key-Set der Map14.
	 *
	 * @return das Key-Set der Map14.
	 */
	public keySet14() : JavaSet<LongArrayKey> {
		if (this._map14 === null)
			this._map14 = this._lazyLoad14();
		return this._map14.keySet();
	}

	/**
	 * Liefert das Key-Set der Map15.
	 *
	 * @return das Key-Set der Map15.
	 */
	public keySet15() : JavaSet<LongArrayKey> {
		if (this._map15 === null)
			this._map15 = this._lazyLoad15();
		return this._map15.keySet();
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
	 * Liefert das Key-Set der Map24.
	 *
	 * @return das Key-Set der Map24.
	 */
	public keySet24() : JavaSet<LongArrayKey> {
		if (this._map24 === null)
			this._map24 = this._lazyLoad24();
		return this._map24.keySet();
	}

	/**
	 * Liefert das Key-Set der Map25.
	 *
	 * @return das Key-Set der Map25.
	 */
	public keySet25() : JavaSet<LongArrayKey> {
		if (this._map25 === null)
			this._map25 = this._lazyLoad25();
		return this._map25.keySet();
	}

	/**
	 * Liefert das Key-Set der Map34.
	 *
	 * @return das Key-Set der Map34.
	 */
	public keySet34() : JavaSet<LongArrayKey> {
		if (this._map34 === null)
			this._map34 = this._lazyLoad34();
		return this._map34.keySet();
	}

	/**
	 * Liefert das Key-Set der Map35.
	 *
	 * @return das Key-Set der Map35.
	 */
	public keySet35() : JavaSet<LongArrayKey> {
		if (this._map35 === null)
			this._map35 = this._lazyLoad35();
		return this._map35.keySet();
	}

	/**
	 * Liefert das Key-Set der Map45.
	 *
	 * @return das Key-Set der Map45.
	 */
	public keySet45() : JavaSet<LongArrayKey> {
		if (this._map45 === null)
			this._map45 = this._lazyLoad45();
		return this._map45.keySet();
	}

	/**
	 * Liefert das Key-Set der Map123.
	 *
	 * @return das Key-Set der Map123.
	 */
	public keySet123() : JavaSet<LongArrayKey> {
		if (this._map123 === null)
			this._map123 = this._lazyLoad123();
		return this._map123.keySet();
	}

	/**
	 * Liefert das Key-Set der Map124.
	 *
	 * @return das Key-Set der Map124.
	 */
	public keySet124() : JavaSet<LongArrayKey> {
		if (this._map124 === null)
			this._map124 = this._lazyLoad124();
		return this._map124.keySet();
	}

	/**
	 * Liefert das Key-Set der Map125.
	 *
	 * @return das Key-Set der Map125.
	 */
	public keySet125() : JavaSet<LongArrayKey> {
		if (this._map125 === null)
			this._map125 = this._lazyLoad125();
		return this._map125.keySet();
	}

	/**
	 * Liefert das Key-Set der Map134.
	 *
	 * @return das Key-Set der Map134.
	 */
	public keySet134() : JavaSet<LongArrayKey> {
		if (this._map134 === null)
			this._map134 = this._lazyLoad134();
		return this._map134.keySet();
	}

	/**
	 * Liefert das Key-Set der Map135.
	 *
	 * @return das Key-Set der Map135.
	 */
	public keySet135() : JavaSet<LongArrayKey> {
		if (this._map135 === null)
			this._map135 = this._lazyLoad135();
		return this._map135.keySet();
	}

	/**
	 * Liefert das Key-Set der Map145.
	 *
	 * @return das Key-Set der Map145.
	 */
	public keySet145() : JavaSet<LongArrayKey> {
		if (this._map145 === null)
			this._map145 = this._lazyLoad145();
		return this._map145.keySet();
	}

	/**
	 * Liefert das Key-Set der Map234.
	 *
	 * @return das Key-Set der Map234.
	 */
	public keySet234() : JavaSet<LongArrayKey> {
		if (this._map234 === null)
			this._map234 = this._lazyLoad234();
		return this._map234.keySet();
	}

	/**
	 * Liefert das Key-Set der Map235.
	 *
	 * @return das Key-Set der Map235.
	 */
	public keySet235() : JavaSet<LongArrayKey> {
		if (this._map235 === null)
			this._map235 = this._lazyLoad235();
		return this._map235.keySet();
	}

	/**
	 * Liefert das Key-Set der Map245.
	 *
	 * @return das Key-Set der Map245.
	 */
	public keySet245() : JavaSet<LongArrayKey> {
		if (this._map245 === null)
			this._map245 = this._lazyLoad245();
		return this._map245.keySet();
	}

	/**
	 * Liefert das Key-Set der Map345.
	 *
	 * @return das Key-Set der Map345.
	 */
	public keySet345() : JavaSet<LongArrayKey> {
		if (this._map345 === null)
			this._map345 = this._lazyLoad345();
		return this._map345.keySet();
	}

	/**
	 * Liefert das Key-Set der Map1234.
	 *
	 * @return das Key-Set der Map1234.
	 */
	public keySet1234() : JavaSet<LongArrayKey> {
		if (this._map1234 === null)
			this._map1234 = this._lazyLoad1234();
		return this._map1234.keySet();
	}

	/**
	 * Liefert das Key-Set der Map1235.
	 *
	 * @return das Key-Set der Map1235.
	 */
	public keySet1235() : JavaSet<LongArrayKey> {
		if (this._map1235 === null)
			this._map1235 = this._lazyLoad1235();
		return this._map1235.keySet();
	}

	/**
	 * Liefert das Key-Set der Map1245.
	 *
	 * @return das Key-Set der Map1245.
	 */
	public keySet1245() : JavaSet<LongArrayKey> {
		if (this._map1245 === null)
			this._map1245 = this._lazyLoad1245();
		return this._map1245.keySet();
	}

	/**
	 * Liefert das Key-Set der Map1345.
	 *
	 * @return das Key-Set der Map1345.
	 */
	public keySet1345() : JavaSet<LongArrayKey> {
		if (this._map1345 === null)
			this._map1345 = this._lazyLoad1345();
		return this._map1345.keySet();
	}

	/**
	 * Liefert das Key-Set der Map2345.
	 *
	 * @return das Key-Set der Map2345.
	 */
	public keySet2345() : JavaSet<LongArrayKey> {
		if (this._map2345 === null)
			this._map2345 = this._lazyLoad2345();
		return this._map2345.keySet();
	}

	/**
	 * Liefert das Key-Set der Map12345.
	 *
	 * @return das Key-Set der Map12345.
	 */
	public keySet12345() : JavaSet<LongArrayKey> {
		return this._map12345.keySet();
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
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key3).
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get3OrException(key3 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey3(key3));
		return this.get3(key3);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key4).
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get4OrException(key4 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey4(key4));
		return this.get4(key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key5).
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get5OrException(key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey5(key5));
		return this.get5(key5);
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

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key3).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get13OrException(key1 : number, key3 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey13(key1, key3));
		return this.get13(key1, key3);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key4).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get14OrException(key1 : number, key4 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey14(key1, key4));
		return this.get14(key1, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get15OrException(key1 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey15(key1, key5));
		return this.get15(key1, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key3).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get23OrException(key2 : number, key3 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey23(key2, key3));
		return this.get23(key2, key3);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key4).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get24OrException(key2 : number, key4 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey24(key2, key4));
		return this.get24(key2, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key5).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get25OrException(key2 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey25(key2, key5));
		return this.get25(key2, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key3, key4).
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get34OrException(key3 : number, key4 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey34(key3, key4));
		return this.get34(key3, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key3, key5).
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get35OrException(key3 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey35(key3, key5));
		return this.get35(key3, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key4, key5).
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get45OrException(key4 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey45(key4, key5));
		return this.get45(key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key3).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get123OrException(key1 : number, key2 : number, key3 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey123(key1, key2, key3));
		return this.get123(key1, key2, key3);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key4).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get124OrException(key1 : number, key2 : number, key4 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey124(key1, key2, key4));
		return this.get124(key1, key2, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get125OrException(key1 : number, key2 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey125(key1, key2, key5));
		return this.get125(key1, key2, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key3, key4).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get134OrException(key1 : number, key3 : number, key4 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey134(key1, key3, key4));
		return this.get134(key1, key3, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key3, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get135OrException(key1 : number, key3 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey135(key1, key3, key5));
		return this.get135(key1, key3, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key4, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get145OrException(key1 : number, key4 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey145(key1, key4, key5));
		return this.get145(key1, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key3, key4).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get234OrException(key2 : number, key3 : number, key4 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey234(key2, key3, key4));
		return this.get234(key2, key3, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key3, key5).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get235OrException(key2 : number, key3 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey235(key2, key3, key5));
		return this.get235(key2, key3, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key4, key5).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get245OrException(key2 : number, key4 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey245(key2, key4, key5));
		return this.get245(key2, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key3, key4, key5).
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get345OrException(key3 : number, key4 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey345(key3, key4, key5));
		return this.get345(key3, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key3, key4).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get1234OrException(key1 : number, key2 : number, key3 : number, key4 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey1234(key1, key2, key3, key4));
		return this.get1234(key1, key2, key3, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key3, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get1235OrException(key1 : number, key2 : number, key3 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey1235(key1, key2, key3, key5));
		return this.get1235(key1, key2, key3, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key4, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get1245OrException(key1 : number, key2 : number, key4 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey1245(key1, key2, key4, key5));
		return this.get1245(key1, key2, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key3, key4, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get1345OrException(key1 : number, key3 : number, key4 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey1345(key1, key3, key4, key5));
		return this.get1345(key1, key3, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key3, key4, key5).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get2345OrException(key2 : number, key3 : number, key4 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey2345(key2, key3, key4, key5));
		return this.get2345(key2, key3, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key3, key4, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public get12345OrException(key1 : number, key2 : number, key3 : number, key4 : number, key5 : number) : List<V> {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !this.containsKey12345(key1, key2, key3, key4, key5));
		return this.get12345(key1, key2, key3, key4, key5);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.ListMap5DLongKeys';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.ListMap5DLongKeys'].includes(name);
	}

	public static class = new Class<ListMap5DLongKeys<any>>('de.svws_nrw.core.adt.map.ListMap5DLongKeys');

}

export function cast_de_svws_nrw_core_adt_map_ListMap5DLongKeys<V>(obj : unknown) : ListMap5DLongKeys<V> {
	return obj as ListMap5DLongKeys<V>;
}
