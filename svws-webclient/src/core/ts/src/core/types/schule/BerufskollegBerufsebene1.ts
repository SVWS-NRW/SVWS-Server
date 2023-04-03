import { JavaObject } from '../../../java/lang/JavaObject';
import { BerufskollegBerufsebeneKatalogEintrag } from '../../../core/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';

export class BerufskollegBerufsebene1 extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<BerufskollegBerufsebene1> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, BerufskollegBerufsebene1> = new Map<string, BerufskollegBerufsebene1>();

	/**
	 * Berufsebene 1 : Agrarwirtschaft
	 */
	public static readonly AW : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("AW", 0, [new BerufskollegBerufsebeneKatalogEintrag(100001000, 1, "AW", "Agrarwirtschaft", null, null)]);

	/**
	 * Berufsebene 1 : Agrarwirtschaft, Bio- und Umwelttechnologie
	 */
	public static readonly AB : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("AB", 1, [new BerufskollegBerufsebeneKatalogEintrag(100002000, 1, "AB", "Agrarwirtschaft, Bio- und Umwelttechnologie", null, null)]);

	/**
	 * Berufsebene 1 : Ernährung
	 */
	public static readonly ER : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("ER", 2, [new BerufskollegBerufsebeneKatalogEintrag(100003000, 1, "ER", "Ernährung", null, null)]);

	/**
	 * Berufsebene 1 : Ernährung und Hauswirtschaft
	 */
	public static readonly EU : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("EU", 3, [new BerufskollegBerufsebeneKatalogEintrag(100004000, 1, "EU", "Ernährung und Hauswirtschaft", null, null)]);

	/**
	 * Berufsebene 1 : Ernährung/Hauswirtschaft
	 */
	public static readonly EH : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("EH", 4, [new BerufskollegBerufsebeneKatalogEintrag(100005000, 1, "EH", "Ernährung/Hauswirtschaft", null, null)]);

	/**
	 * Berufsebene 1 : Ernährungs- und Versorgungsmanagement
	 */
	public static readonly EV : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("EV", 5, [new BerufskollegBerufsebeneKatalogEintrag(100006000, 1, "EV", "Ernährungs- und Versorgungsmanagement", null, null)]);

	/**
	 * Berufsebene 1 : Gestaltung
	 */
	public static readonly GT : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("GT", 6, [new BerufskollegBerufsebeneKatalogEintrag(100007000, 1, "GT", "Gestaltung", null, null)]);

	/**
	 * Berufsebene 1 : Gesundheit und Soziales
	 */
	public static readonly GU : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("GU", 7, [new BerufskollegBerufsebeneKatalogEintrag(100008000, 1, "GU", "Gesundheit und Soziales", null, null)]);

	/**
	 * Berufsebene 1 : Gesundheit/Erziehung und Soziales
	 */
	public static readonly GE : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("GE", 8, [new BerufskollegBerufsebeneKatalogEintrag(100009000, 1, "GE", "Gesundheit/Erziehung und Soziales", null, null)]);

	/**
	 * Berufsebene 1 : Gesundheit/Soziales
	 */
	public static readonly GS : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("GS", 9, [new BerufskollegBerufsebeneKatalogEintrag(100010000, 1, "GS", "Gesundheit/Soziales", null, null)]);

	/**
	 * Berufsebene 1 : Informatik
	 */
	public static readonly IF : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("IF", 10, [new BerufskollegBerufsebeneKatalogEintrag(100011000, 1, "IF", "Informatik", null, null)]);

	/**
	 * Berufsebene 1 : ohne besondere Zuordnung
	 */
	public static readonly OZ : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("OZ", 11, [new BerufskollegBerufsebeneKatalogEintrag(100012000, 1, "OZ", "ohne besondere Zuordnung", null, null)]);

	/**
	 * Berufsebene 1 : ohne Fachbereich / Schulversuch
	 */
	public static readonly SV : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("SV", 12, [new BerufskollegBerufsebeneKatalogEintrag(100013000, 1, "SV", "ohne Fachbereich / Schulversuch", null, null)]);

	/**
	 * Berufsebene 1 : Sozialwesen
	 */
	public static readonly SW : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("SW", 13, [new BerufskollegBerufsebeneKatalogEintrag(100014000, 1, "SW", "Sozialwesen", null, null)]);

	/**
	 * Berufsebene 1 : Technik
	 */
	public static readonly TE : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("TE", 14, [new BerufskollegBerufsebeneKatalogEintrag(100015000, 1, "TE", "Technik", null, null)]);

	/**
	 * Berufsebene 1 : Technik/Naturwissenschaften
	 */
	public static readonly TN : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("TN", 15, [new BerufskollegBerufsebeneKatalogEintrag(100016000, 1, "TN", "Technik/Naturwissenschaften", null, null)]);

	/**
	 * Berufsebene 1 : Wirtschaft
	 */
	public static readonly WI : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("WI", 16, [new BerufskollegBerufsebeneKatalogEintrag(100017000, 1, "WI", "Wirtschaft", null, null)]);

	/**
	 * Berufsebene 1 : Wirtschaft und Verwaltung
	 */
	public static readonly WV : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("WV", 17, [new BerufskollegBerufsebeneKatalogEintrag(100018000, 1, "WV", "Wirtschaft und Verwaltung", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Berufsebene, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : BerufskollegBerufsebeneKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Berufsebene
	 */
	public readonly historie : Array<BerufskollegBerufsebeneKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Berufsebenen der Ebene 1, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, BerufskollegBerufsebene1> = new HashMap();

	/**
	 * Erzeugt eine neue Berufsebene in der Aufzählung.
	 *
	 * @param historie   die Historie der Berufsebene, welches ein Array von {@link BerufskollegBerufsebeneKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<BerufskollegBerufsebeneKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BerufskollegBerufsebene1.all_values_by_ordinal.push(this);
		BerufskollegBerufsebene1.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzels der Berufsebenen auf die zugehörigen Berufsebenen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Berufsebene auf die zugehörigen Berufsebene
	 */
	private static getMapBerufsebenenByKuerzel() : HashMap<string, BerufskollegBerufsebene1> {
		if (BerufskollegBerufsebene1._ebenen.size() === 0) {
			for (const s of BerufskollegBerufsebene1.values()) {
				if (s.daten !== null)
					BerufskollegBerufsebene1._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return BerufskollegBerufsebene1._ebenen;
	}

	/**
	 * Gibt die Berufsebene für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Berufsebene
	 *
	 * @return die Berufsebene oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : BerufskollegBerufsebene1 | null {
		return BerufskollegBerufsebene1.getMapBerufsebenenByKuerzel().get(kuerzel);
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
		if (!(other instanceof BerufskollegBerufsebene1))
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
	public compareTo(other : BerufskollegBerufsebene1) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegBerufsebene1> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BerufskollegBerufsebene1 | null {
		const tmp : BerufskollegBerufsebene1 | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.BerufskollegBerufsebene1'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_BerufskollegBerufsebene1(obj : unknown) : BerufskollegBerufsebene1 {
	return obj as BerufskollegBerufsebene1;
}
