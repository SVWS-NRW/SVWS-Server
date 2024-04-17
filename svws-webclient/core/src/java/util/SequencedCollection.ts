import { UnsupportedOperationException } from '../../java/lang/UnsupportedOperationException';
import type { Collection } from './Collection';
import { NoSuchElementException } from './NoSuchElementException';


export interface SequencedCollection<E> extends Collection<E> {

	reversed() : SequencedCollection<E>;

}


/* Implementierung der Default-Methode addFirst */
export function java_util_SequencedCollection_addFirst<E>(this: SequencedCollection<E>, e: E | null) : void {
	throw new UnsupportedOperationException();
}


/* Implementierung der Default-Methode addLast */
export function java_util_SequencedCollection_addLast<E>(this: SequencedCollection<E>, e: E | null) : void {
	throw new UnsupportedOperationException();
}


/* Implementierung der Default-Methode getFirst */
export function java_util_SequencedCollection_getFirst<E>(this: SequencedCollection<E>) : E {
	const iter = this.iterator();
	if (!iter.hasNext())
		throw new NoSuchElementException();
	return iter.next();
}


/* Implementierung der Default-Methode getLast */
export function java_util_SequencedCollection_getLast<E>(this: SequencedCollection<E>) : E {
	const iter = this.reversed().iterator();
	if (!iter.hasNext())
		throw new NoSuchElementException();
	return iter.next();
}


/* Implementierung der Default-Methode removeFirst */
export function java_util_SequencedCollection_removeFirst<E>(this: SequencedCollection<E>) : E {
	const iter = this.iterator();
	if (!iter.hasNext())
		throw new NoSuchElementException();
	const e: E = iter.next();
	iter.remove();
	return e;
}


/* Implementierung der Default-Methode removeLast */
export function java_util_SequencedCollection_removeLast<E>(this: SequencedCollection<E>) : E {
	const iter = this.reversed().iterator();
	if (!iter.hasNext())
		throw new NoSuchElementException();
	const e: E = iter.next();
	iter.remove();
	return e;
}


export function cast_java_util_SequencedCollection<E>(obj : unknown) : SequencedCollection<E> {
	return obj as SequencedCollection<E>;
}
