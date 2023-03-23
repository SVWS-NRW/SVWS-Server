import { AbstractCollection } from './AbstractCollection';
import { Collection, cast_java_util_Collection } from './Collection';
import { JavaIterator } from './JavaIterator';
import { JavaSet } from './JavaSet';

import { JavaObject } from '../../java/lang/JavaObject';
import { NullPointerException } from '../../java/lang/NullPointerException';


export abstract class AbstractSet<E> extends AbstractCollection<E> implements JavaSet<E> {

	modCount : number = 0;

	public equals(obj : any) : boolean {
		if (obj === this)
			return true;
		if (!(obj instanceof JavaObject))
			return false;
		const javaObject : JavaObject = obj as JavaObject;
		if (!javaObject.isTranspiledInstanceOf('java.util.JavaSet'))
			return false;
		const coll : Collection<unknown> = cast_java_util_Collection(javaObject);
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
		const iter : JavaIterator<E> = this.iterator();
		while (iter.hasNext()) {
			const elem : E = iter.next();
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
			for (const elem of c)
				modified ||= this.remove(elem);
			if (modified)
				this.modCount++;
			return modified;
		}
		const iter : JavaIterator<E> = this.iterator();
		while (iter.hasNext()) {
			const elem : E = iter.next();
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
