import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { BildungsgangTypKatalogEintrag } from '../../../core/data/schule/BildungsgangTypKatalogEintrag';

export class BerufskollegBildungsgangTyp extends JavaObject implements JavaEnum<BerufskollegBildungsgangTyp> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<BerufskollegBildungsgangTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, BerufskollegBildungsgangTyp> = new Map<string, BerufskollegBildungsgangTyp>();

	/**
	 * Berufsfachschulen
	 */
	public static readonly BERUFSFACHSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("BERUFSFACHSCHULE", 0, [new BildungsgangTypKatalogEintrag(1000, "BF", "Berufsfachschulen", null, null)]);

	/**
	 * Berufsfachschulen
	 */
	public static readonly BERUFSSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("BERUFSSCHULE", 1, [new BildungsgangTypKatalogEintrag(2000, "BS", "Berufsschule", null, null)]);

	/**
	 * Berufliches Gymnasium
	 */
	public static readonly BERUFLICHES_GYMNASIUM : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("BERUFLICHES_GYMNASIUM", 2, [new BildungsgangTypKatalogEintrag(3000, "BY", "Berufliches Gymnasium", null, null)]);

	/**
	 * Fachoberschule
	 */
	public static readonly FACHOBERSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("FACHOBERSCHULE", 3, [new BildungsgangTypKatalogEintrag(4000, "FO", "Fachoberschule", null, null)]);

	/**
	 * Fachschule
	 */
	public static readonly FACHSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("FACHSCHULE", 4, [new BildungsgangTypKatalogEintrag(5000, "FS", "Fachschule", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Berufsschultypen von Bildungsgängen, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : BildungsgangTypKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Berufsschultypen von Bildungsgängen
	 */
	public readonly historie : Array<BildungsgangTypKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Berufsschultypen von Bildungsgängen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, BerufskollegBildungsgangTyp> = new HashMap();

	/**
	 * Erzeugt einen neuen Berufsschultyp von Bildungsgängen in der Aufzählung.
	 *
	 * @param historie   die Historie der Berufsschultypen von Bildungsgängen, welches ein Array von
	 *                   {@link BildungsgangTypKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<BildungsgangTypKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BerufskollegBildungsgangTyp.all_values_by_ordinal.push(this);
		BerufskollegBildungsgangTyp.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Berufsschultypen von Bildungsgängen auf die
	 * zugehörigen Berufsschultypen von Bildungsgängen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Typen auf die zugehörigen Berufsschultypen von Bildungsgängen
	 */
	private static getMapByKuerzel() : HashMap<string, BerufskollegBildungsgangTyp> {
		if (BerufskollegBildungsgangTyp._ebenen.size() === 0) {
			for (const s of BerufskollegBildungsgangTyp.values()) {
				if (s.daten !== null)
					BerufskollegBildungsgangTyp._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return BerufskollegBildungsgangTyp._ebenen;
	}

	/**
	 * Gibt den Berufsschultyp von Bildungsgängen für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Berufsschultyps von Bildungsgängen
	 *
	 * @return der Berufsschultyp von Bildungsgängen oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : BerufskollegBildungsgangTyp | null {
		return BerufskollegBildungsgangTyp.getMapByKuerzel().get(kuerzel);
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
		if (!(other instanceof BerufskollegBildungsgangTyp))
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
	public compareTo(other : BerufskollegBildungsgangTyp) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegBildungsgangTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BerufskollegBildungsgangTyp | null {
		const tmp : BerufskollegBildungsgangTyp | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.BerufskollegBildungsgangTyp', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_BerufskollegBildungsgangTyp(obj : unknown) : BerufskollegBildungsgangTyp {
	return obj as BerufskollegBildungsgangTyp;
}
