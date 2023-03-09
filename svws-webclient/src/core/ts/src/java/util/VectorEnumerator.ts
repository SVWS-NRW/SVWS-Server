import { Enumeration } from "./Enumeration";
import { NoSuchElementException } from './NoSuchElementException';
import { Vector } from "./Vector";

export class VectorEnumerator<E> implements Enumeration<E> {

    private vector : Vector<E>;
    public count : number = 0;

    public constructor(vector : Vector<E>) {
        this.vector = vector;
    }

    public hasMoreElements() : boolean  {
        return this.count < this.vector.size();
    }

    public nextElement() : E {
        if (this.count < this.vector.size()) {
            return this.vector.get(this.count++);
        }
        throw new NoSuchElementException("Vector Enumeration");
    }

	public isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.util.VectorEnumerator',
            'java.util.Enumeration',
            'java.lang.Object'
        ].includes(name);
	}

}


export function cast_java_util_VectorEnumerator<E>(obj : unknown) : VectorEnumerator<E> {
	return obj as VectorEnumerator<E>;
}
