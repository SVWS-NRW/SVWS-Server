import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { OrganisationsformKatalogEintrag } from '../../../core/data/schule/OrganisationsformKatalogEintrag';
import { Arrays } from '../../../java/util/Arrays';

export class WeiterbildungskollegOrganisationsformen extends JavaObject implements JavaEnum<WeiterbildungskollegOrganisationsformen> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<WeiterbildungskollegOrganisationsformen> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, WeiterbildungskollegOrganisationsformen> = new Map<string, WeiterbildungskollegOrganisationsformen>();

	/**
	 * Organisationsform: Teilbeleger
	 */
	public static readonly TEILZEIT : WeiterbildungskollegOrganisationsformen = new WeiterbildungskollegOrganisationsformen("TEILZEIT", 0, [new OrganisationsformKatalogEintrag(2001000, "T", "Teilbeleger", Arrays.asList(Schulform.WB), null, null)]);

	/**
	 * Organisationsform: Vollbeleger
	 */
	public static readonly VOLLZEIT : WeiterbildungskollegOrganisationsformen = new WeiterbildungskollegOrganisationsformen("VOLLZEIT", 1, [new OrganisationsformKatalogEintrag(2002000, "V", "Vollbeleger", Arrays.asList(Schulform.WB), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Organisationsform
	 */
	public readonly daten : OrganisationsformKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Organisationsform
	 */
	public readonly historie : Array<OrganisationsformKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Organisationsformen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _mapKuerzel : HashMap<string, WeiterbildungskollegOrganisationsformen> = new HashMap();

	/**
	 * Erzeugt eine neue Organisationsform in der Aufzählung.
	 *
	 * @param historie   die Historie der Organisationsform, welche ein Array von
	 *                   {@link OrganisationsformKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<OrganisationsformKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		WeiterbildungskollegOrganisationsformen.all_values_by_ordinal.push(this);
		WeiterbildungskollegOrganisationsformen.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Organisationsformen auf die
	 * zugehörigen Organisationsformen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Organisationsformen
	 */
	private static getMapByKuerzel() : HashMap<string, WeiterbildungskollegOrganisationsformen> {
		if (WeiterbildungskollegOrganisationsformen._mapKuerzel.size() === 0) {
			for (const s of WeiterbildungskollegOrganisationsformen.values()) {
				if (s.daten !== null)
					WeiterbildungskollegOrganisationsformen._mapKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return WeiterbildungskollegOrganisationsformen._mapKuerzel;
	}

	/**
	 * Gibt die Organisationsform für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Organisationsform
	 *
	 * @return die Organisationsform oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : WeiterbildungskollegOrganisationsformen | null {
		return WeiterbildungskollegOrganisationsformen.getMapByKuerzel().get(kuerzel);
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
		if (!(other instanceof WeiterbildungskollegOrganisationsformen))
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
	public compareTo(other : WeiterbildungskollegOrganisationsformen) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<WeiterbildungskollegOrganisationsformen> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : WeiterbildungskollegOrganisationsformen | null {
		const tmp : WeiterbildungskollegOrganisationsformen | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.WeiterbildungskollegOrganisationsformen', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_WeiterbildungskollegOrganisationsformen(obj : unknown) : WeiterbildungskollegOrganisationsformen {
	return obj as WeiterbildungskollegOrganisationsformen;
}
