import type { HashSet } from './HashSet';
import type { JavaIterator } from './JavaIterator';
import type { Consumer } from '../../java/util/function/Consumer';

import { ConcurrentModificationException } from './ConcurrentModificationException';
import { NoSuchElementException } from './NoSuchElementException';
import { IllegalStateException } from '../../java/lang/IllegalStateException';
import { JavaObject } from '../../java/lang/JavaObject';


export class HashSetIterator<E> extends JavaObject implements JavaIterator<E> {

	public cur : number = 0;

	public last : number = -1;

	protected modCount : number;

	protected readonly hashSet : HashSet<E>;

	protected readonly elements : Array<E> = [];


	constructor(hashSet : HashSet<E>) {
		super();
		this.modCount = hashSet.modCount;
		this.hashSet = hashSet;
		this.elements = this.hashSet.toArray() as Array<E>;
	}


	public hasNext() : boolean {
		return this.cur !== this.elements.length;
	}


	public next(): E {
		this.checkForComodification();
		try {
			if (!this.hasNext())
				throw new NoSuchElementException();
			const i : number = this.cur;
			const d : E = this.elements[i];
			this.last = i;
			this.cur = i + 1;
			return d;
		} catch (e) {
			this.checkForComodification();
			throw new NoSuchElementException();
		}
	}


	public remove() : void {
		if (this.last < 0)
			throw new IllegalStateException();
		this.checkForComodification();
		try {
			this.hashSet.remove(this.elements[this.last]);
			this.last = -1;
			this.modCount = this.hashSet.modCount;
		} catch (e) {
			throw new ConcurrentModificationException();
		}
	}


	public checkForComodification() : void {
		if (this.hashSet.modCount !== this.modCount)
			throw new ConcurrentModificationException();
	}


	forEachRemaining(action: Consumer<E>): void {
		while (this.hasNext())
			action.accept(this.next());
	}

	public transpilerCanonicalName(): string {
		return 'java.util.HashSetIterator';
	}

	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.HashSetIterator',
			'java.util.JavaIterator',
			'java.lang.Object'
		].includes(name);
	}

}


export function cast_java_util_HashSetIterator<E>(obj : unknown) : HashSetIterator<E> {
	return obj as HashSetIterator<E>;
}
