import { AbstractList } from './AbstractList';
import { ConcurrentModificationException } from './ConcurrentModificationException';
import { JavaIterator } from './JavaIterator';
import { NoSuchElementException } from './NoSuchElementException';

import { Consumer } from './function/Consumer';

import { IllegalStateException } from '../../java/lang/IllegalStateException';
import { JavaObject } from '../../java/lang/JavaObject';


export class AbstractListIterator<E> extends JavaObject implements JavaIterator<E> {

    public current : number = 0;

    public lastReturned : number = -1;

    protected modCount : number;

    protected readonly list : AbstractList<E>;


    constructor(list : AbstractList<E>) {
        super();
        this.modCount = list.modCount;
        this.list = list;
    }


    public hasNext() : boolean {
        return this.current != this.list.size();
    }


    public next(): E {
        this.checkForComodification();
        try {
            if (!this.hasNext)
                throw new NoSuchElementException();
            let i : number = this.current;
            let next : E = this.list.get(i);
            this.lastReturned = i;
            this.current = i + 1;
            return next;
        } catch (e) {
            this.checkForComodification();
            throw new NoSuchElementException();
        }
    }


    public remove() : void {
        if (this.lastReturned < 0)
            throw new IllegalStateException();
        this.checkForComodification();
        try {
            this.list.remove(this.lastReturned);
            if (this.lastReturned < this.current)
                this.current--;
            this.lastReturned = -1;
            this.modCount = this.list.modCount;
        } catch (e) {
            throw new ConcurrentModificationException();
        }
    }


    public checkForComodification() : void {
        if (this.list.modCount != this.modCount)
            throw new ConcurrentModificationException();
    }


    forEachRemaining(action: Consumer<E>): void {
        while (this.hasNext())
            action.accept(this.next());
    }


	public isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.util.AbstractListIterator',
            'java.util.JavaIterator',
            'java.lang.Object'
        ].includes(name);
	}

}


export function cast_java_util_AbstractListIterator<E>(obj : unknown) : AbstractListIterator<E> {
	return obj as AbstractListIterator<E>;
}
