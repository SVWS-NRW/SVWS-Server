import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { EinschulungsartKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_EinschulungsartKatalogEintrag } from '../../../core/data/schule/EinschulungsartKatalogEintrag';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Einschulungsart extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Einschulungsart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Einschulungsart> = new Map<string, Einschulungsart>();

	/**
	 * Einschulungsart: Kinder, die bis zum gültigen Einschulungsstichtag das 6. Lebensjahr vollendet haben
	 */
	public static readonly E51 : Einschulungsart = new Einschulungsart("E51", 0, [new EinschulungsartKatalogEintrag(1000, "51", "älter als 6. Jahre", "Kinder, die bis zum gültigen Einschulungsstichtag das 6. Lebensjahr vollendet haben", null, null)]);

	/**
	 * Einschulungsart: Kinder, die nach dem gültigen Einschulungsstichtag das 6. Lebensjahr vollenden
	 */
	public static readonly E52 : Einschulungsart = new Einschulungsart("E52", 1, [new EinschulungsartKatalogEintrag(2000, "52", "jünger als 6 Jahre", "Kinder, die nach dem gültigen Einschulungsstichtag das 6. Lebensjahr vollenden", null, null)]);

	/**
	 * Einschulungsart: Kinder, die in diesem Schuljahr erstmals gemäß §35 Abs. 3 SchulG eine Schule besuchen
	 */
	public static readonly E53 : Einschulungsart = new Einschulungsart("E53", 2, [new EinschulungsartKatalogEintrag(3000, "53", "zurückgestellt (§35 Abs. 3 SchulG)", "Kinder, die in diesem Schuljahr erstmals gemäß §35 Abs. 3 SchulG eine Schule besuchen", null, null)]);

	/**
	 * Einschulungsart: Kinder, die erstmals eine Früherziehung besuchen
	 */
	public static readonly E54 : Einschulungsart = new Einschulungsart("E54", 3, [new EinschulungsartKatalogEintrag(4000, "54", "Früherziehung", "Kinder, die erstmals eine Früherziehung besuchen", null, null)]);

	/**
	 * Einschulungsart: Im abgelaufenen Schuljahr: Teilnahme an einer Früherziehung
	 */
	public static readonly E18 : Einschulungsart = new Einschulungsart("E18", 4, [new EinschulungsartKatalogEintrag(7000, "18", "vorher: Früherziehung", "Im abgelaufenen Schuljahr: Teilnahme an einer Früherziehung", null, null)]);

	/**
	 * Einschulungsart: Im abgelaufenen Schuljahr: Besuch eines Förderschul-(nicht Sonder)kindergarten
	 */
	public static readonly E19 : Einschulungsart = new Einschulungsart("E19", 5, [new EinschulungsartKatalogEintrag(8000, "19", "vorher: Förderschulkindergarten", "Im abgelaufenen Schuljahr: Besuch eines Förderschul-(nicht Sonder)kindergarten", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Einschulungsart, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : EinschulungsartKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Einschulungsart
	 */
	public readonly historie : Array<EinschulungsartKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Einschulungsarten, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, Einschulungsart | null> = new HashMap();

	/**
	 * Erzeugt eine neuen Einschulungsart in der Aufzählung.
	 * 
	 * @param historie   die Historie der Einschulungsart, welche ein Array von 
	 *                   {@link EinschulungsartKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<EinschulungsartKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Einschulungsart.all_values_by_ordinal.push(this);
		Einschulungsart.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Einschulungsarten auf die 
	 * zugehörigen Einschulungsarten zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen Einschulungsarten
	 */
	private static getMapByKuerzel() : HashMap<string, Einschulungsart | null> {
		if (Einschulungsart._ebenen.size() === 0) {
			for (let s of Einschulungsart.values()) {
				if (s.daten !== null) 
					Einschulungsart._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return Einschulungsart._ebenen;
	}

	/**
	 * Gibt die Einschulungsart für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Einschulungsart
	 * 
	 * @return die Einschulungsart oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Einschulungsart | null {
		return Einschulungsart.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
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
		if (!(other instanceof Einschulungsart))
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
	public compareTo(other : Einschulungsart) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Einschulungsart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Einschulungsart | null {
		let tmp : Einschulungsart | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schueler.Einschulungsart'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schueler_Einschulungsart(obj : unknown) : Einschulungsart {
	return obj as Einschulungsart;
}
