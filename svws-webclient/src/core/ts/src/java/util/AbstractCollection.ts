import { Collection } from './Collection';
import { JavaIterator } from './JavaIterator';

import { JavaObject } from '../lang/JavaObject';
import { UnsupportedOperationException } from '../lang/UnsupportedOperationException';
import { NullPointerException } from '../lang/NullPointerException';
import { Consumer } from './function/Consumer';


export abstract class AbstractCollection<E> extends JavaObject implements Collection<E> {

    protected constructor() {
    	super();
    }

    public abstract size() : number;


    public isEmpty() : boolean {
        return this.size() == 0;
    }


    public contains(e : any) : boolean {
        let it : JavaIterator<E> = this.iterator();
        if (e === null)
            return false;
        while (it.hasNext())
            if (((e instanceof JavaObject) && (e.equals(it.next()))) || (e === it.next()))
                return true;
        return false;
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


    public abstract iterator() : JavaIterator<E>;



    public toArray() : Array<unknown>;
    public toArray<U>(a: Array<U>) : Array<U>;
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((typeof __param0 === "undefined") || (__param0 == null) || (__param0.length < this.size())) {
            let r : Array<E> = new Array<E>(this.size());
            let it : JavaIterator<E> = this.iterator();
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



    public add(e : E | null) : boolean {
        throw new UnsupportedOperationException();
    }


    public remove(o : any) : boolean {
        let it : JavaIterator<E> = this.iterator();
        if (o === null)
            return false;
        while (it.hasNext()) {
            if (((o instanceof JavaObject) && o.equals(it.next())) || (o === it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
    }


    public containsAll(c : Collection<any> | null) : boolean {
    	if (c === null)
    		return false;
        let it : JavaIterator<any> = c.iterator();
        while (it.hasNext()) {
            if (!this.contains(it.next()))
                return false;
        }
        return true;
    }


    public addAll(c : Collection<E> | null) : boolean {
    	if (c === null)
    		return false;
        let modified : boolean = false;
        let it : JavaIterator<E> = c.iterator();
        while (it.hasNext()) {
            if (this.add(it.next()))
                modified = true;
        }
        return modified;
    }


    public removeAll(c : Collection<any> | null) : boolean {
        if (c === null)
            throw new NullPointerException();
        let modified : boolean = false;
        let iter : JavaIterator<E> = this.iterator();
        while (iter.hasNext()) {
            if (c.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }


    public retainAll(c : Collection<any> | null) : boolean {
        if (c === null)
            throw new NullPointerException();
        let modified : boolean = false;
        let iter : JavaIterator<E> = this.iterator();
        while (iter.hasNext()) {
            if (!c.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }


    public clear() : void {
        let it : JavaIterator<E> = this.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }


    public toString() : string {
        let iter : JavaIterator<E> = this.iterator();
        if (!iter.hasNext())
            return "[]";
        let result : string = "[";
        while (true) {
            result += JSON.stringify(iter.next());
            if (!iter.hasNext())
                return result + "]";
            result += ", ";
        }
    }


    public equals(obj : any): boolean {
        // TODO check equality based on the elements in this collection
        return (this === obj);
    }


    public hashCode(): number {
        // TODO we need another implementation based on the elements in this collection
        let str : string = JSON.stringify(this);
        let hash : number = 0;
        if (str.length === 0)
            return hash;
        for (let i : number = 0; i < str.length; i++)
            hash = (((hash << 5) - hash) + str.charCodeAt(i)) | 0;
        return hash;        
    }


    public forEach(action: Consumer<E>): void {
        let it : JavaIterator<E> = this.iterator();
        while (it.hasNext())
            action.accept(it.next());
    }


	public isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.util.AbstractCollection',
            'java.util.Collection',
            'java.lang.Iterable',
            'java.lang.Object'
        ].includes(name);
	}

}


export function cast_java_util_AbstractCollection<E>(obj : unknown) : AbstractCollection<E> {
	return obj as AbstractCollection<E>;
}
