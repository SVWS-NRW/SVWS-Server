import type { List } from './List';

import { ArrayList } from './ArrayList';
import { JavaObject } from '../../java/lang/JavaObject';


export class Arrays extends JavaObject {

	public static asList<T>(arg : Array<T>) : List<T>;
	public static asList<T>(...args : Array<T>) : List<T>;

	public static asList<T>(...args : Array<T>) : List<T> {
		const v : ArrayList<T> = new ArrayList<T>();
		if ((args.length == 1) && Array.isArray(args[0])) {
			for (const e of args[0])
				v.add(e);
		} else {
			for (const e of args)
				v.add(e);
		}
		return v;
	}

	public static copyOf<T>(original : Array<T>, newLength : number) : Array<T> {
		return original.slice();
	}

	public static deepEquals(a1 : Array<unknown> | null, a2 : Array<unknown> | null) : boolean {
		if (a1 === a2)
			return true;
		if ((a1 === null) || (a2 === null))
			return false;
		if (a1.length !== a2.length)
			return false;
		for (let i = 0; i < a1.length; i++) {
			const elem1 = a1[i];
			const elem2 = a2[i];
			if (Array.isArray(elem1) && Array.isArray(elem2)) {
				if (!Arrays.deepEquals(elem1, elem2))
					return false;
			} else {
				if (!JavaObject.equalsTranspiler(elem1, elem2))
					return false;
			}
		}
		return true;
	}

	public static deepHashCode(a : Array<unknown> | null) : number {
		if (!Array.isArray(a))
			return 0;
		return JavaObject.getTranspilerHashCode(a);
	}

	public static fill(a : Array<unknown>, value : unknown) : void;
	public static fill(a : Array<unknown>, fromIndex: number, toIndex : number, value : unknown) : void;
	public static fill(a : Array<unknown>, param1 : number | unknown, param2? : number, param3? : unknown) : void {
		if ((typeof param1 === "number") && (typeof param2 !== "undefined") && (typeof param3 !== "undefined")) {
			a.fill(param3, param1, param2);
		} else {
			a.fill(param1);
		}
	}

	public static toString(a : Array<any>) : string {
		return JSON.stringify(a);
	}


	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.Arrays',
			'java.lang.Object'
		].includes(name);
	}

}


export function cast_java_util_Arrays(obj : unknown) : Arrays {
	return obj as Arrays;
}
