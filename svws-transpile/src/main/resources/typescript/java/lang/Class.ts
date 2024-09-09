import type { TranspiledObject } from './TranspiledObject';


export class Class<T> implements TranspiledObject {

	protected simplename : string;
	protected canonicalname : string;

	public constructor(canonicalname : string) {
		this.canonicalname = canonicalname;
		const period = canonicalname.lastIndexOf('.');
		this.simplename = canonicalname.substring(period + 1);
	}


	public getCanonicalName() : string {
		return this.canonicalname;
	}


	public getSimpleName() : string {
		return this.simplename;
	}


	public getName() : string {
		return this.simplename;
	}


	transpilerCanonicalName(): string {
		return 'java.lang.Class';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.Object',
			'java.lang.Class',
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
		return Class._hashCode(this.canonicalname);
	}


	public equals(obj : any) : boolean {
		if (typeof obj !== "object")
			return false;
		if (obj instanceof Class)
			return obj.canonicalname === this.canonicalname;
		return false;
	}

	public clone() : TranspiledObject {
		const result = this.getClass();
		result.simplename = this.simplename;
		result.canonicalname = this.canonicalname;
		return result;
	}

	public getClass<T extends TranspiledObject>() : Class<T> {
		return new Class(this.transpilerCanonicalName());
	}

}

export function cast_java_lang_Class<T extends TranspiledObject>(obj : unknown) : Class<T> {
	return obj as Class<T>;
}
