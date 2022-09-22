import { Collection } from './Collection';
import { JavaIterator } from './JavaIterator';

import { Class } from '../../java/lang/Class';
import { JavaObject } from '../../java/lang/JavaObject';
import { TranspiledObject } from '../../java/lang/TranspiledObject';
import { UnsupportedOperationException } from '../../java/lang/UnsupportedOperationException';


export class HashMapCollection<K, V> implements Collection<V> {

    readonly #_map : Map<K, V>;

    public constructor(map : Map<K,V>) {
        this.#_map = map;
    }

    size(): number {
        return this.#_map.size;
    }

    isEmpty(): boolean {
        return this.#_map.size === 0;
    }

    contains(value: any): boolean {
        for (let [k, v] of this.#_map) {
            if (v === value)
                return true;
            if ((v instanceof JavaObject) && (v.equals(value)))
                return true;
        }
        return false;
    }

    iterator(): JavaIterator<V> {
        const it = this[Symbol.iterator]()
        let next_item = it.next()
        return {
            hasNext():boolean {
                return next_item.done ? false : true
            }, 
            next(): V {
                const v = next_item.value
                next_item = it.next()
                return v
            }, 
            remove() {
            },
        }
    }

    public toArray() : Array<unknown>;
    public toArray<U>(a: Array<U>) : Array<U>;
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((typeof __param0 === "undefined") || (__param0 == null) || (__param0.length < this.size())) {
            let r : Array<V> = new Array<V>(this.size());
            let it : JavaIterator<V> = this.iterator();
            let i : number = 0
            while (it.hasNext()) {
                r[i] = it.next();
                i++;
            }
            return r;
        } else if (Array.isArray(__param0)) {
            // TODO handle the case where a is not null and try to fill into the parameter array if possible - see JavaDoc for implementation            
            throw new Error('not yet implemented')
		} else throw new Error('invalid method overload');
    }

    add(e: V): boolean {
        throw new UnsupportedOperationException();
    }

    remove(o: any): boolean {
        throw new UnsupportedOperationException();
    }

    containsAll(c: Collection<any>): boolean {
        throw new UnsupportedOperationException();
    }

    addAll(c: Collection<V>): boolean {
        throw new UnsupportedOperationException();
    }

    removeAll(c: Collection<any>): boolean {
        throw new UnsupportedOperationException();
    }

    retainAll(c: Collection<any>): boolean {
        throw new UnsupportedOperationException();
    }

    clear(): void {
        throw new UnsupportedOperationException();
    }

    equals(obj: any): boolean {
        throw new UnsupportedOperationException();
    }

    hashCode(): number {
        throw new UnsupportedOperationException();
    }

    [Symbol.iterator](): Iterator<V> {
        return this.#_map.values();
    }

    getClass<T extends TranspiledObject>(): Class<T> {
        throw new UnsupportedOperationException();
    }

    clone(): unknown {
        throw new UnsupportedOperationException();
    }

    toString(): string {
        let res = '[';
        this.#_map.forEach(v => res + (v as unknown as JavaObject).toString() + ', ');
        res = res.substring(-2, 0);
        res + ']';
        return res;
    }

    isTranspiledInstanceOf(name: string): boolean {
        throw new UnsupportedOperationException();
    }

}
