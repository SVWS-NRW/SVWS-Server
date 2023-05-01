import { Enumeration } from "./Enumeration";
import { NoSuchElementException } from './NoSuchElementException';
import { ArrayList } from "./ArrayList";

export class ArrayListEnumerator<E> implements Enumeration<E> {

	protected _list : ArrayList<E>;
	public count : number = 0;

	public constructor(list : ArrayList<E>) {
		this._list = list;
	}

	public hasMoreElements() : boolean  {
		return this.count < this._list.size();
	}

	public nextElement() : E {
		if (this.count < this._list.size()) {
			return this._list.get(this.count++);
		}
		throw new NoSuchElementException("ArrayList Enumeration");
	}

	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.ArrayListEnumerator',
			'java.util.Enumeration',
			'java.lang.Object'
		].includes(name);
	}

}


export function cast_java_util_ArrayListEnumerator<E>(obj : unknown) : ArrayListEnumerator<E> {
	return obj as ArrayListEnumerator<E>;
}
