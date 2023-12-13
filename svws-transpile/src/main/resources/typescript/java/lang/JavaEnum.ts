import { Class } from './Class';
import { JavaObject } from './JavaObject';
import { JavaString } from './JavaString';
import { Comparable } from './Comparable';

/**
 * Dieses Abstrakte Klasse dient der Emulation der abstrakten Java-Klasse java.lang.Enum
 */
export abstract class JavaEnum<K extends JavaEnum<K>> extends JavaObject implements Comparable<K> {

	/** the name of the enumeration value */
	readonly __name : string;

	/** the ordinal value for the enumeration value */
	readonly __ordinal : number;

	/**
	 * Erzeugt eine neuen Enum-Eintrag mit den übergebenen Informationen.
	 * Die Einträge werden unter dem Namen des Enums und
	 * dem Namen des Eintrags bzw. abgespeichtert.
	 *
	 * @param name       der Bezeichner des Enum-Eintrags
	 * @param ordinal    die eindeutige Nummer des Enum-Eintrags
	 */
	protected constructor(name : string, ordinal : number) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
		return this.__ordinal;
	}


	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : string {
		return this.__name;
	}


	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof JavaEnum))
			return false;
		return this === other;
	}


	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}


	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : JavaEnum<K>) : number {
		const result : number = JavaString.compareTo(this.__name, other.__name);
		if (result !== 0)
			return result;
		return this.__ordinal - other.__ordinal;
	}

	transpilerCanonicalName(): string {
		return 'java.lang.Enum';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.Enum',
			'java.lang.Constable',
			'java.lang.Comparable',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_lang_Enum<K extends JavaEnum<K>>(obj : unknown) : JavaEnum<K> {
	return obj as JavaEnum<K>;
}
