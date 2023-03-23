import { AbstractStringBuilder } from './AbstractStringBuilder';
import { CharSequence } from './CharSequence';
import { Comparable } from './Comparable';

export class StringBuilder extends AbstractStringBuilder implements Comparable<StringBuilder>, CharSequence {

	constructor(param? : string | number) {
		if (typeof param === "undefined") {
			super();
		} else {
			super(param);
		}
	}


	public compareTo(other : StringBuilder) : number {
		return super.compareTo(other);
	}


	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.StringBuilder',
			'java.lang.AbstractStringBuilder',
			'java.lang.Appendable',
			'java.lang.Comparable',
			'java.lang.CharSequence',
			'java.lang.Object',
			'java.io.Serializable'
		].includes(name);
	}

}

export function cast_java_lang_StringBuilder(obj : unknown) : StringBuilder {
	return obj as StringBuilder;
}
