/**
 * Dieses interface dient der Emulation der abstrakten Java-Klasse java.lang.Enum
 */
export interface JavaEnum<K> {

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	name() : string;

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	ordinal() : number;

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	compareTo(other : JavaEnum<K>) : number;

    isTranspiledInstanceOf(name: string) : boolean;

}


export function cast_java_lang_Enum<K>(obj : unknown) : JavaEnum<K> {
	return obj as JavaEnum<K>;
}
