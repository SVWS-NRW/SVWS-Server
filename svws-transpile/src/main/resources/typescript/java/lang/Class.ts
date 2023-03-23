import { JavaObject } from './JavaObject';
import { TranspiledObject } from './TranspiledObject';


export class Class<T extends TranspiledObject> implements TranspiledObject {

	private simplename : string;

	public constructor(obj : T) {
		if (obj instanceof Class)
			this.simplename = Object.getPrototypeOf(this);
		else if (obj instanceof Object)
			this.simplename = Object.getPrototypeOf(obj);
		else
			this.simplename = typeof obj;
	}


	public getSimpleName() : string {
		return this.simplename;
	}


	public getName() : string {
		return this.simplename;
	}


	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.Object',
			'java.lang.class',
			'java.io.Serializable',
			'java.lang.reflect.AnnotatedElement',
			'java.lang.reflect.GenericDeclaration',
			'java.lang.reflect.Type'
		].includes(name);
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
		return Class._hashCode(JSON.stringify(this.simplename));
	}


	public equals(obj : any) : boolean {
		if (!(typeof obj === "object"))
			return false;
		if (obj instanceof Class)
			return obj.simplename === this.simplename;
		return false;
	}

	public clone() : TranspiledObject {
		const result = new Class(this);
		result.simplename = this.simplename;
		return result;
	}

	public getClass<T extends TranspiledObject>() : Class<T> {
		return new Class(this);
	}

}

export function cast_java_lang_Class<T extends TranspiledObject>(obj : unknown) : Class<T> {
	return obj as Class<T>;
}
