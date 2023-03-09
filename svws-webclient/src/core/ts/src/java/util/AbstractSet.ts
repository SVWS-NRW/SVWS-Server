import { AbstractCollection } from './AbstractCollection';
import { Collection, cast_java_util_Collection } from './Collection';
import { JavaIterator } from './JavaIterator';
import { JavaSet } from './JavaSet';

import { JavaObject } from '../lang/JavaObject';
import { NullPointerException } from '../lang/NullPointerException';


export abstract class AbstractSet<E> extends AbstractCollection<E> implements JavaSet<E> {

    modCount : number = 0;

    public equals(obj : any) : boolean {
        if (obj === this)
            return true;
        if (!(obj instanceof JavaObject))
            return false;
        let javaObject : JavaObject = obj as JavaObject;
        if (!javaObject.isTranspiledInstanceOf('java.util.JavaSet'))
            return false;
        let coll : Collection<unknown> = cast_java_util_Collection(javaObject);
        if (coll.size() !== this.size())
            return false;
        try {
            return this.containsAll(coll);
        } catch (e) {
            return false;
        }
    }

    public hashCode() : number {
        let hash : number = 0;
        let iter : JavaIterator<E> = this.iterator();
        while (iter.hasNext()) {
            let elem : E = iter.next();
            if (elem !== null)
                hash += JavaObject.getTranspilerHashCode(elem);
        }
        return hash;
    }

    public removeAll(c : Collection<any> | null) : boolean {
        if (c === null)
            throw new NullPointerException();
        let modified : boolean = false;
        if (this.size() > c.size()) {
            for (let elem of c)
                modified ||= this.remove(elem);
            if (modified)
                this.modCount++;
            return modified;
        }
        let iter : JavaIterator<E> = this.iterator();
        while (iter.hasNext()) {
            let elem : E = iter.next();
            if (c.contains(elem)) {
                iter.remove();
                modified = true;
            }
        }
        if (modified)
            this.modCount++;
        return modified;
    }

}
