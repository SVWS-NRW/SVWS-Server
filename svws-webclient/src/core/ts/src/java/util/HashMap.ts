import type { Collection } from './Collection';
import type { JavaMap } from './JavaMap';
import type { JavaMapEntry } from './JavaMapEntry';
import type { JavaSet } from './JavaSet';
import type { Cloneable } from '../../java/lang/Cloneable';
import type { Serializable } from '../../java/io/Serializable';

import { HashMapCollection } from './HashMapCollection';
import { JavaObject } from '../../java/lang/JavaObject';
import { UnsupportedOperationException } from '../lang/UnsupportedOperationException';
import { HashMapEntrySet } from './HashMapEntrySet';
import { HashMapEntry } from './HashMapEntry';
import { HashMapKeySet } from './HashMapKeySet';
import { JavaFunction } from './function/JavaFunction';


export class HashMap<K, V> extends JavaObject implements JavaMap<K, V>, Cloneable, Serializable {

	protected readonly _map : Map<K, JavaMapEntry<K, V>> = new Map();

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
		for (const [k, e] of this._map) {
			const v : V = e.getValue();
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
		const entry : JavaMapEntry<K, V> | undefined = this._map.get(key);
		return (entry === undefined) ? null : entry.getValue();
	}

	public put(key : K, value : V) : V | null {
		const oldValue : V | null = this.get(key);
		const newEntry = new HashMapEntry(key, value);
		this._map.set(key, newEntry);
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
		return new HashMapKeySet<K, V>(this._map);
	}

	public values() : Collection<V> {
		return new HashMapCollection<K, V>(this._map);
	}

	public entrySet() : JavaSet<JavaMapEntry<K, V>> {
		return new HashMapEntrySet<K, V>(this._map);
	}

	public equals(o : any) : boolean {
		throw new UnsupportedOperationException();
	}

	public hashCode() : number {
		throw new UnsupportedOperationException();
	}


	public computeIfAbsent(key : K, mappingFunction: JavaFunction<K, V> ) : V | null {
        const v : V | null = this.get(key);
		if (v != null)
			return v;
		const newValue : V = mappingFunction.apply(key);
		if (newValue == null)
			return null;
		this.put(key, newValue);
		return newValue;
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
