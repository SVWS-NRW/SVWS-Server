import { NavigableSet, cast_java_util_NavigableSet } from '../../../java/util/NavigableSet';
import { JavaIterator, cast_java_util_Iterator } from '../../../java/util/JavaIterator';
import { Collection, cast_java_util_Collection } from '../../../java/util/Collection';
import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SortedSet, cast_java_util_SortedSet } from '../../../java/util/SortedSet';
import { AVLMap, cast_de_nrw_schule_svws_core_adt_map_AVLMap } from '../map/AVLMap';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';

export class AVLSet<E> extends JavaObject implements NavigableSet<E> {

	private readonly _set : NavigableSet<E>;


	/**
	 * Erzeugt ein leeres Set, welche bei den Schlüsselwerten die natürliche Ordnung des {@link Comparable} -
	 * Interface nutzt.
	 */
	public constructor();

	/**
	 * Erstellt eine neues Set und nutzt dabei die angegeben Ordnung der Schlüssel.
	 * 
	 * @param comparator Die Ordnung für die Schlüssel.
	 */
	public constructor(comparator : Comparator<E>);

	/**
	 * Erstellt ein neues Set mit den Daten des angegebenen Sets und nutzt dabei die Ordnung dieses Sets.
	 * 
	 * @param set Die Map mit den Daten.
	 */
	public constructor(set : SortedSet<E>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Comparator<E> | SortedSet<E>) {
		super();
		if ((typeof __param0 === "undefined")) {
			let map : AVLMap<E, E> = new AVLMap();
			map.allowKeyAlone(true);
			this._set = map.navigableKeySet();
		} else if (((typeof __param0 !== "undefined") && ((typeof __param0 !== 'undefined') && (__param0 instanceof Object) && (__param0 !== null) && ('compare' in __param0) && (typeof __param0.compare === 'function')) || (__param0 === null))) {
			let comparator : Comparator<E> = cast_java_util_Comparator(__param0);
			let map : AVLMap<E, E> = new AVLMap(comparator);
			map.allowKeyAlone(true);
			this._set = map.navigableKeySet();
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.SortedSet'))) || (__param0 === null))) {
			let set : SortedSet<E> = cast_java_util_SortedSet(__param0);
			let map : AVLMap<E, E> = new AVLMap();
			map.allowKeyAlone(true);
			this._set = map.navigableKeySet();
			this._set.addAll(set);
		} else throw new Error('invalid method overload');
	}

	public comparator() : Comparator<Partial<E>> {
		return this._set.comparator();
	}

	public first() : E {
		return this._set.first();
	}

	public last() : E {
		return this._set.last();
	}

	public size() : number {
		return this._set.size();
	}

	public isEmpty() : boolean {
		return this._set.isEmpty();
	}

	public contains(o : unknown) : boolean {
		return this._set.contains(o);
	}

	public toArray() : Array<unknown>;

	public toArray<T>(a : Array<T>) : Array<T>;

	/**
	 * Implementation for method overloads of 'toArray'
	 */
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((typeof __param0 === "undefined")) {
			return this._set.toArray();
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			let a : Array<T> = __param0;
			return this._set.toArray(a);
		} else throw new Error('invalid method overload');
	}

	public add(e : E) : boolean {
		return this._set.add(e);
	}

	public remove(o : unknown) : boolean {
		return this._set.remove(o);
	}

	public containsAll(c : Collection<unknown>) : boolean {
		return this._set.containsAll(c);
	}

	public addAll(c : Collection<E>) : boolean {
		return this._set.addAll(c);
	}

	public retainAll(c : Collection<unknown>) : boolean {
		return this._set.retainAll(c);
	}

	public removeAll(c : Collection<unknown>) : boolean {
		return this._set.removeAll(c);
	}

	public clear() : void {
		this._set.clear();
	}

	public lower(e : E) : E | null {
		return this._set.lower(e);
	}

	public floor(e : E) : E | null {
		return this._set.floor(e);
	}

	public ceiling(e : E) : E | null {
		return this._set.ceiling(e);
	}

	public higher(e : E) : E | null {
		return this._set.higher(e);
	}

	public pollFirst() : E | null {
		return this._set.pollFirst();
	}

	public pollLast() : E | null {
		return this._set.pollLast();
	}

	public iterator() : JavaIterator<E> {
		return this._set.iterator();
	}

	public descendingSet() : NavigableSet<E> {
		return this._set.descendingSet();
	}

	public descendingIterator() : JavaIterator<E> {
		return this._set.descendingIterator();
	}

	public subSet(fromElement : E, fromInclusive : boolean, toElement : E, toInclusive : boolean) : NavigableSet<E>;

	public subSet(fromElement : E, toElement : E) : SortedSet<E>;

	/**
	 * Implementation for method overloads of 'subSet'
	 */
	public subSet(__param0 : E, __param1 : E | boolean, __param2? : E, __param3? : boolean) : NavigableSet<E> | SortedSet<E> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean") && ((typeof __param2 !== "undefined") && (typeof __param2 !== "undefined")) && ((typeof __param3 !== "undefined") && typeof __param3 === "boolean")) {
			let fromElement : E = __param0 as unknown as E;
			let fromInclusive : boolean = __param1 as boolean;
			let toElement : E = __param2 as unknown as E;
			let toInclusive : boolean = __param3 as boolean;
			return this._set.subSet(fromElement, fromInclusive, toElement, toInclusive);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && (typeof __param1 !== "undefined")) && (typeof __param2 === "undefined") && (typeof __param3 === "undefined")) {
			let fromElement : E = __param0 as unknown as E;
			let toElement : E = __param1 as unknown as E;
			return this._set.subSet(fromElement, toElement);
		} else throw new Error('invalid method overload');
	}

	public headSet(toElement : E, inclusive : boolean) : NavigableSet<E>;

	public headSet(toElement : E) : SortedSet<E>;

	/**
	 * Implementation for method overloads of 'headSet'
	 */
	public headSet(__param0 : E, __param1? : boolean) : NavigableSet<E> | SortedSet<E> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			let toElement : E = __param0 as unknown as E;
			let inclusive : boolean = __param1 as boolean;
			return this._set.headSet(toElement, inclusive);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			let toElement : E = __param0 as unknown as E;
			return this._set.headSet(toElement);
		} else throw new Error('invalid method overload');
	}

	public tailSet(fromElement : E, inclusive : boolean) : NavigableSet<E>;

	public tailSet(fromElement : E) : SortedSet<E>;

	/**
	 * Implementation for method overloads of 'tailSet'
	 */
	public tailSet(__param0 : E, __param1? : boolean) : NavigableSet<E> | SortedSet<E> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			let fromElement : E = __param0 as unknown as E;
			let inclusive : boolean = __param1 as boolean;
			return this._set.tailSet(fromElement, inclusive);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			let fromElement : E = __param0 as unknown as E;
			return this._set.tailSet(fromElement);
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.SortedSet', 'java.util.Collection', 'de.nrw.schule.svws.core.adt.set.AVLSet', 'java.util.Set', 'java.util.NavigableSet', 'java.lang.Iterable'].includes(name);
	}

	public [Symbol.iterator](): Iterator<E> {
		let iter : JavaIterator<E> = this.iterator();
		const result : Iterator<E> = {
			next() : IteratorResult<E> {
				if (iter.hasNext())
					return { value : iter.next(), done : false };
				return { value : null, done : true };
			}
		};
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_adt_set_AVLSet<E>(obj : unknown) : AVLSet<E> {
	return obj as AVLSet<E>;
}
