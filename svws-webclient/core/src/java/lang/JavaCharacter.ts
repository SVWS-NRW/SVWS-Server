import { JavaObject } from './JavaObject';

export class JavaCharacter extends JavaObject {

	/**
	 * Prüft, ob das übergebene Zeichen (oder hier auch ein längere String) vollständig
	 * in Großschrift ist oder nicht.
	 *
	 * @param ch   das Zeichen
	 *
	 * @returns true, falls es in Großschrift ist, und ansonsten false
	 */
	public static isUpperCase(ch: string) : boolean {
		if ((ch.length === 0) || (ch.toLowerCase() === ch.toUpperCase()))
			return false;
		return (ch.toUpperCase() === ch);
	}

	/**
	 * Prüft, ob das übergebene Zeichen (oder hier auch ein längere String) vollständig
	 * in Kleinschrift ist oder nicht.
	 *
	 * @param ch   das Zeichen
	 *
	 * @returns true, falls es in Kleinschrift ist, und ansonsten false
	 */
	public static isLowerCase(ch: string) : boolean {
		if ((ch.length === 0) || (ch.toLowerCase() === ch.toUpperCase()))
			return false;
		return (ch.toLowerCase() === ch);
	}

	public transpilerCanonicalName(): string {
		return 'java.lang.Character';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.Character',
			'java.lang.Object'
		].includes(name);
	}

}

export function cast_java_lang_Character(obj : unknown) : JavaCharacter {
	return obj as JavaCharacter;
}
