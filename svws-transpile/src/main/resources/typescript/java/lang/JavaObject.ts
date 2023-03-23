import { Class } from './Class';
import { Throwable } from './Throwable';
import { TranspiledObject } from './TranspiledObject';


function prepareAttributeOrderForStringify() {
	return function(key : any, value : any) {
		if (value instanceof Object && !(value instanceof Array) && !(value instanceof Function)) {
			return Object.entries(value).sort().reduce((result : any, e) => {
				result[e[0]] = e[1];
				return result;
			}, {});
		}
		return value;
	}
}


export abstract class JavaObject implements TranspiledObject {

	public constructor() {
	}

	public getClass<T extends TranspiledObject>() : Class<T> {
		return new Class(this);
	}

	static _hashCode(str: string) : number {
		let hash : number = 0;
		if (str.length === 0)
			return hash;
		for (let i : number = 0; i < str.length; i++)
			hash = (((hash << 5) - hash) + str.charCodeAt(i)) | 0;
		return hash;
	}

	public hashCode() : number {
		return JavaObject._hashCode(JSON.stringify(this, prepareAttributeOrderForStringify()));
	}

	public equals(obj : any) : boolean {
		if (!(typeof obj === "object"))
			return false;
		if (!(obj instanceof JavaObject))
			return false;
		if (obj === null)
			return false;
		return (this === obj);
	}


	public clone() : unknown {
		return { ...this };
	}

	public toString() : string | null {
		return JSON.stringify(this, prepareAttributeOrderForStringify());
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.Object'
		].includes(name);
	}

	public static equalsTranspiler(obj : any, other : any) : boolean {
		if (obj instanceof JavaObject)
			return obj.equals(other);
		if (obj instanceof Throwable)
			return obj.equals(other);
		if (obj instanceof Object)
			return obj.valueOf() === other.valueOf();
		return obj === other;
	}

	public static getTranspilerHashCode(obj : any) : number {
		if (obj === null)
			return 0;
		if (typeof obj === "undefined")
			return Number.NaN;   // unspecified in Java
		if (typeof obj === "string")
			return (JavaObject._hashCode(obj));
		if (typeof obj === "number")
			return obj;
		if (typeof obj === "boolean")
			return obj ? 1 : 0;
		if (typeof obj === "function")
			return JavaObject._hashCode(JSON.stringify(obj));
		if (typeof obj === "object") {
			if (obj instanceof JavaObject)
				return obj.hashCode();
			if (obj instanceof Object)
				return JavaObject._hashCode(JSON.stringify(obj));
			if (obj instanceof Date)
				return JavaObject._hashCode(JSON.stringify(obj));
			if (obj instanceof Array) {
				if (obj == null)
					return 0;
				let result : number = 1;
				for (const e of obj) {
					result *= 31;
					if (e !== null)
						result += JavaObject.getTranspilerHashCode(e);
				}
				return result;
			}
			if (obj instanceof String)
				return JavaObject._hashCode(obj.valueOf());
			if (obj instanceof Number)
				return obj.valueOf();
			if (obj instanceof Boolean)
				return obj.valueOf() ? 1 : 0;
		}
		// all cases should have been handled before
		return JavaObject._hashCode(JSON.stringify(obj));
	}

}


export function cast_java_lang_Object(obj : unknown) : JavaObject {
	return obj as JavaObject;
}
