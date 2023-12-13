import type { Appendable } from './Appendable';
import type { CharSequence } from './CharSequence';
import { IndexOutOfBoundsException } from './IndexOutOfBoundsException';
import { JavaObject } from './JavaObject';
import { StringIndexOutOfBoundsException } from './StringIndexOutOfBoundsException';


export class AbstractStringBuilder extends JavaObject implements Appendable, CharSequence {

	protected value: string;

	constructor(param? : string | number) {
		super();
		if (typeof param === "string") {
			this.value = param;
		} else {
			this.value = "";
		}
	}

	compareTo(other : AbstractStringBuilder) : number {
		if (this === other)
			return 0;
		const minLength = Math.min(this.value.length, other.value.length);
		for (let pos : number = 0; pos < minLength; pos++) {
			const cp1 = this.value.codePointAt(pos);
			const cp2 = other.value.codePointAt(pos);
			if (!cp1)
				return -1;
			if (!cp2)
				return 1;
			if (cp1 !== cp2)
				return cp1 - cp2;
		}
		return this.value.length - other.value.length;
	}


	public isEmpty() : boolean {
		return this.value.length === 0;
	}


	public length() : number {
		return this.value.length;
	}


	public capacity() : number {
		return 0;
	}


	public ensureCapacity(minimumCapacity : number) : void {
		// Not necessary for TypeScript environments
	}


	public trimToSize() : void {
		// Not necessary for TypeScript environments
	}


	public setLength(newLength : number) : void {
		if (newLength < 0)
			throw new StringIndexOutOfBoundsException(newLength);
		if (this.value.length > newLength) {
			this.value = this.value.slice(0, newLength);
		}
	}


	public charAt(index : number) : string {
		if ((index < 0) || (index >= this.value.length))
			throw new StringIndexOutOfBoundsException(index);
		return this.value.charAt(index);
	}


	public codePointAt(index : number) : number {
		if ((index < 0) || (index >= this.value.length))
			throw new StringIndexOutOfBoundsException(index);
		const result : number | undefined = this.value.codePointAt(index);
		if (!result)
			throw new StringIndexOutOfBoundsException(index);
		return result;
	}


	public codePointBefore(index : number) : number {
		return this.codePointAt(index - 1);
	}


	public codePointCount(beginIndex : number, endIndex : number) : number {
		if ((beginIndex < 0)  || (beginIndex > endIndex) || (endIndex > this.value.length))
			throw new IndexOutOfBoundsException();
		return endIndex - beginIndex;
	}


	public offsetByCodePoints(index : number, codePointOffset : number) : number {
		if ((index < 0) || (index > this.value.length) || (index + codePointOffset > this.value.length))
			throw new IndexOutOfBoundsException();
		return index + codePointOffset;
	}


	public getChars(srcBegin : number, srcEnd : number, dst : string[], dstBegin : number) : void {
		if ((srcBegin < 0) || (dstBegin < 0) || (srcBegin > srcEnd) || (srcEnd > this.value.length))
			throw new IndexOutOfBoundsException();
		for (let i : number = 0; i < srcEnd - srcBegin; i++)
			dst[dstBegin + i] = this.value.charAt(srcBegin + i);
	}


	public setCharAt(index : number, chr : string) : void {
		if ((index < 0) || (index >= this.value.length))
			throw new IndexOutOfBoundsException();
		this.value = this.value.substring(0, index) + chr + this.value.substring(index + 1);
	}

	public append(param : any) : AbstractStringBuilder;
	public append(param : CharSequence | AbstractStringBuilder, start : number, end : number) : AbstractStringBuilder;
	public append(param : string[], offset : number, len : number) : AbstractStringBuilder;
	public append(param : any, start? : number, end? : number) : AbstractStringBuilder {
		if ((typeof param === "object") && Array.isArray(param) && (param.length > 0) && (typeof param[0] === "string")) {
			if ((typeof start !== "undefined") && (typeof end !== "undefined")) {
				for (let i : number = start; i < start + end; i++)
					this.value += param[i];
			} else {
				for (let i : number = 0; i < param.length; i++)
					this.value += param[i];
			}
		} else if ((typeof param === "object") && (param instanceof AbstractStringBuilder)) { // TODO CharSequence ... use type guards ?
			if ((typeof start !== "undefined") && (typeof end !== "undefined")) {
				this.value += param.toString()?.substring(start, end) || "";
			} else {
				this.value += (param.toString() || "");
			}
		} else if ((typeof param === "function") || (typeof param === "object") || (typeof param === "symbol")) {
			this.value += JSON.stringify(param);
		} else {
			this.value += param;
		}
		return this;
	}


	public delete(start : number, end : number) : AbstractStringBuilder {
		if ((start < 0) || (start > this.value.length) || (start > end))
			throw new StringIndexOutOfBoundsException(start);
		if (end > this.value.length)
			end = this.value.length;
		this.value = this.value.substring(0, start) + this.value.substring(end, this.value.length);
		return this;
	}


	public appendCodePoint(codePoint : number) : AbstractStringBuilder {
		return this.append(String.fromCodePoint(codePoint));
	}


	public deleteCharAt(index : number) : AbstractStringBuilder {
		this.value = this.value.substring(0, index) + this.value.substring(index + 1, this.value.length);
		return this;
	}


	public replace(start : number, end : number, str : string) : AbstractStringBuilder {
		this.value = this.value.substring(0, start) + str + this.value.substring(end, this.value.length);
		return this;
	}


	public subSequence(start : number, end : number) : CharSequence {
		return new AbstractStringBuilder(this.substring(start, end));
	}


	public substring(start : number, end? : number) : string {
		return this.value.substring(start, end);
	}


	public insert(offset : number, obj : any) : AbstractStringBuilder;
	public insert(index : number, str : string[], offset : number, len : number) : AbstractStringBuilder;
	public insert(param1 : number, str : any, offset? : number, len? : number) : AbstractStringBuilder {
		throw new Error("not yet implemented in transpiler");
	}



	public indexOf(str : string, fromIndex? : number) : number {
		return str.indexOf(str, fromIndex);
	}


	public lastIndexOf(str : string, fromIndex? : number) : number {
		return str.lastIndexOf(str, fromIndex);
	}


	public reverse() : AbstractStringBuilder {
		let str = "";
		for (let i = this.value.length - 1; i >= 0; i--)
			str = str.concat(this.value.charAt(i));
		this.value = str;
		return this;
	}


	public toString() : string {
		return this.value;
	}

	transpilerCanonicalName(): string {
		return 'java.lang.AbstractStringBuilder';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.AbstractStringBuilder',
			'java.lang.Appendable',
			'java.lang.CharSequence',
			'java.lang.Object'
		].includes(name);
	}

}

export function cast_java_lang_AbstractStringBuilder(obj : unknown) : AbstractStringBuilder {
	return obj as AbstractStringBuilder;
}

