import { AbstractList } from './AbstractList';
import { AbstractListIterator } from './AbstractListIterator';
import { ConcurrentModificationException } from './ConcurrentModificationException';
import { ListIterator } from './ListIterator';
import { NoSuchElementException } from './NoSuchElementException';

import { IllegalStateException } from '../lang/IllegalStateException';


export class AbstractListListIterator<E> extends AbstractListIterator<E> implements ListIterator<E> {

    constructor(list : AbstractList<E>, index : number) {
        super(list);
        this.current = index;
    }

    public nextIndex() : number {
        return this.current;
    }


    public hasPrevious() : boolean {
        return this.current != 0;
    }


    public previous() : E {
        this.checkForComodification();
        try {
            let i : number = this.current - 1;
            let previous : E = this.list.get(i);
            this.lastReturned = this.current = i;
            return previous;
        } catch (e) {
            this.checkForComodification();
            throw new NoSuchElementException();
        }
    }


    public previousIndex() : number {
        return this.current - 1;
    }


    public set(e : E) : void {
        if (this.lastReturned < 0)
            throw new IllegalStateException();
        this.checkForComodification();
        try {
            this.list.set(this.lastReturned, e);
            this.modCount = this.list.modCount;
        } catch (e) {
            throw new ConcurrentModificationException();
        }
    }


    public add(e : E) : void {
        this.checkForComodification();
        try {
            let i : number = this.current;
            this.list.add(i, e);
            this.lastReturned = -1;
            this.current = i + 1;
            this.modCount = this.list.modCount;
        } catch (e) {
            throw new ConcurrentModificationException();
        }
    }


	public isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.util.AbstractListListIterator',
            'java.util.AbstractListIterator',
            'java.util.ListIterator',
            'java.util.JavaIterator',
            'java.lang.Object'
        ].includes(name);
	}

}


export function cast_java_util_AbstractListListIterator<E>(obj : unknown) : AbstractListListIterator<E> {
	return obj as AbstractListListIterator<E>;
}
