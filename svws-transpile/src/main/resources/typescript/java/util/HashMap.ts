import { Collection } from './Collection';
import { JavaMap } from './JavaMap';
import { JavaMapEntry } from './JavaMapEntry';
import { JavaSet } from './JavaSet';
import { HashMapCollection } from './HashMapCollection';
import { HashSet } from './HashSet';

import { Cloneable } from '../../java/lang/Cloneable';
import { JavaObject } from '../../java/lang/JavaObject';

import { Serializable } from '../../java/io/Serializable';
import { UnsupportedOperationException } from '../lang/UnsupportedOperationException';

export class HashMap<K, V> extends JavaObject implements JavaMap<K, V>, Cloneable, Serializable {

	private readonly _map : Map<K, V> = new Map();

	public constructor() {
		super();
	}

	public size() : number {
		return this._map.size;
	}

	public isEmpty() : boolean {
		return this._map.size === 0;
	}

	public containsKey(key : K) : boolean {
		return this._map.has(key);
	}

	public containsValue(value : V) : boolean {
		for (const [k, v] of this._map) {
			if (v === value)
				return true;
			if ((v instanceof JavaObject) && (v.equals(value)))
				return true;
		}
		return false;
	}

	public get(key : any | null) : V | null {
		if (key === null)
			return null;
		const value : V | undefined = this._map.get(key);
		return (value === undefined) ? null : value;
	}

	public put(key : K, value : V) : V | null {
		const oldValue : V | null = this.get(key);
		this._map.set(key, value);
		return oldValue;
	}

	public remove(key : any) : V | null {
		if (key === null)
			return null;
		const oldValue : V | null = this.get(key);
		this._map.delete(key);
		return oldValue;
	}

	public putAll(m : JavaMap<K, V>) : void {
		for (const entry of m.entrySet()) {
			if (entry === null)
				return;
			this.put(entry.getKey(), entry.getValue());
		}
	}

	public clear() : void {
		this._map.clear();
	}

	public keySet() : JavaSet<K> {
		const result = new HashSet<K>();
		for (const [key, value] of this._map)
			result.add(key);
		return result;
	}

	public values() : Collection<V> {
		return new HashMapCollection<K, V>(this._map);
	}

	public entrySet() : JavaSet<JavaMapEntry<K, V>> {
		throw new UnsupportedOperationException();
	}

	public equals(o : any) : boolean {
		throw new UnsupportedOperationException();
	}

	public hashCode() : number {
		throw new UnsupportedOperationException();
	}


	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.HashMap',
			'java.util.AbstractMap',
			'java.util.Map',
			'java.lang.Object',
			'java.lang.Cloneable',
			'java.io.Serializable'
		].includes(name);
	}

}


export function cast_java_util_HashMap<K, V>(obj : unknown) : HashMap<K, V> {
	return obj as HashMap<K, V>;
}
