import { IllegalArgumentException } from './IllegalArgumentException';
import { JavaObject } from './JavaObject';

export class JavaMath extends JavaObject {

	public static clamp(value: number, min: number, max: number) : number {
		if (min <= max)
			return Math.min(max, Math.max(value, min));
		if (isNaN(min))
			throw new IllegalArgumentException("NaN is not allowed for min value");
		if (isNaN(max))
			throw new IllegalArgumentException("NaN is not allowed for max value");
		throw new IllegalArgumentException("min is greater than max: " + min.toString() + " > " + max.toString());
	}

	public transpilerCanonicalName(): string {
		return 'java.lang.Math';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.Math',
			'java.lang.Object'
		].includes(name);
	}

}

export function cast_java_lang_Math(obj : unknown) : JavaMath {
	return obj as JavaMath;
}
