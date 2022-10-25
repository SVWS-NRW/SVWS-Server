import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { Collection, cast_java_util_Collection } from '../../../java/util/Collection';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Collections, cast_java_util_Collections } from '../../../java/util/Collections';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { GostKursblockungRegelParameterTyp, cast_de_nrw_schule_svws_core_types_kursblockung_GostKursblockungRegelParameterTyp } from '../../../core/types/kursblockung/GostKursblockungRegelParameterTyp';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class GostKursblockungRegelTyp extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GostKursblockungRegelTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, GostKursblockungRegelTyp> = new Map<String, GostKursblockungRegelTyp>();

	public static readonly UNDEFINIERT : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("UNDEFINIERT", 0, 0, "Undefiniert", Collections.emptyList());

	public static readonly KURSART_SPERRE_SCHIENEN_VON_BIS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURSART_SPERRE_SCHIENEN_VON_BIS", 1, 1, "Kursart: Sperre Schienen (von/bis)", Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	public static readonly KURSART_ALLEIN_IN_SCHIENEN_VON_BIS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS", 2, 6, "Kursart: Allein in Schienen (von/bis)", Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	public static readonly KURS_FIXIERE_IN_SCHIENE : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_FIXIERE_IN_SCHIENE", 3, 2, "Kurs: Fixiere in Schiene", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	public static readonly KURS_SPERRE_IN_SCHIENE : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_SPERRE_IN_SCHIENE", 4, 3, "Kurs: Sperre in Schiene", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	public static readonly SCHUELER_FIXIEREN_IN_KURS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("SCHUELER_FIXIEREN_IN_KURS", 5, 4, "Schüler: Fixiere in Kurs", Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.KURS_ID));

	public static readonly SCHUELER_VERBIETEN_IN_KURS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("SCHUELER_VERBIETEN_IN_KURS", 6, 5, "Schüler: Verbiete in Kurs", Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.KURS_ID));

	public readonly typ : number;

	public readonly bezeichnung : String | null;

	private readonly paramTypes : List<GostKursblockungRegelParameterTyp>;

	private static readonly map : HashMap<Number, GostKursblockungRegelTyp> = new HashMap();

	/**
	 *
	 * Erstellt einen neuen Regel-Typ mit der angegeben ID.
	 * 
	 * @param id            die ID des Regel-Typs
	 * @param paramCount    die Anzahl der Parameter für diesen Regel-Typ
	 * @param bezeichnung   die textuelle Bezeichnung für diesen Regel-Typ 
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : String, paramTypes : List<GostKursblockungRegelParameterTyp>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostKursblockungRegelTyp.all_values_by_ordinal.push(this);
		GostKursblockungRegelTyp.all_values_by_name.set(name, this);
		this.typ = id;
		this.bezeichnung = bezeichnung;
		this.paramTypes = paramTypes;
	}

	/**
	 * Liefert die Menge aller existierender Regeln.
	 * 
	 * @return Die Menge aller existierender Regeln.
	 */
	public static getCollection() : Collection<GostKursblockungRegelTyp> {
		return GostKursblockungRegelTyp.map.values();
	}

	/**
	 *
	 * Ermittelt den Regel-Typ anhand seiner ID und gibt diesen zurück.
	 *
	 * @param id   die ID des Regel-Typs
	 * 
	 * @return der Regel-Typ 
	 */
	public static fromTyp(id : Number | null) : GostKursblockungRegelTyp {
		if (id === null) 
			return GostKursblockungRegelTyp.UNDEFINIERT;
		let gostTyp : GostKursblockungRegelTyp | null = GostKursblockungRegelTyp.map.get(id);
		if (gostTyp === null) 
			return GostKursblockungRegelTyp.UNDEFINIERT;
		return gostTyp;
	}

	/**
	 *
	 * Gibt die Anzahl der Parameter für diesen Regel-Type zurück.
	 * 
	 * @return die Anzahl der Parameter für diesen Regel-Type zurück. 
	 */
	public getParamCount() : number {
		return this.paramTypes.size();
	}

	/**
	 * Gibt den i-ten Parameter-Typ der Regel zurück.
	 * 
	 * @param i   der Index des Parameters
	 * 
	 * @return der Parameter-Typ
	 * 
	 * @throws IllegalArgumentException falls der angegebene Index ungültig ist 
	 */
	public getParamType(i : number) : GostKursblockungRegelParameterTyp {
		if ((i < 0) || (i >= this.paramTypes.size())) 
			throw new IllegalArgumentException("Ein Parameter mit dem Index i existiert nicht für den Regel-Typ " + this.name().valueOf())
		return this.paramTypes.get(i);
	}

	/**
	 * Prüft, ob der Regeltyp einen Parameter von dem angegebenen
	 * Parametertyp hat.
	 *  
	 * @param paramType   der Parametertyp
	 * 
	 * @return true, falls die Regel einen solchen Parametertyp hat und ansonsten false 
	 */
	public hasParamType(paramType : GostKursblockungRegelParameterTyp | null) : boolean {
		for (let cur of this.paramTypes) 
			if (paramType as unknown === cur as unknown) 
				return true;
		return false;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : String {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : String {
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
		if (!(other instanceof GostKursblockungRegelTyp))
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
	public compareTo(other : GostKursblockungRegelTyp) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostKursblockungRegelTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : GostKursblockungRegelTyp | null {
		let tmp : GostKursblockungRegelTyp | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_kursblockung_GostKursblockungRegelTyp(obj : unknown) : GostKursblockungRegelTyp {
	return obj as GostKursblockungRegelTyp;
}
