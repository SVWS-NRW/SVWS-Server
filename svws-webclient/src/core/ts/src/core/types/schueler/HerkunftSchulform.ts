import { JavaObject } from '../../../java/lang/JavaObject';
import { HerkunftSchulformKatalogEintrag } from '../../../core/data/schule/HerkunftSchulformKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { Arrays } from '../../../java/util/Arrays';

export class HerkunftSchulform extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<HerkunftSchulform> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, HerkunftSchulform> = new Map<string, HerkunftSchulform>();

	/**
	 * Berufskolleg
	 */
	public static readonly BK : HerkunftSchulform = new HerkunftSchulform("BK", 0, [new HerkunftSchulformKatalogEintrag(1000, "BK", "BK", Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GE, Schulform.GY, Schulform.SG), "Berufskolleg", null, null)]);

	/**
	 * Freie Waldorfschule
	 */
	public static readonly FW : HerkunftSchulform = new HerkunftSchulform("FW", 1, [new HerkunftSchulformKatalogEintrag(2000, "FW", "FW", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Freie Waldorfschule", null, null)]);

	/**
	 * Grundschule (auch Primarstufe der Volksschule)
	 */
	public static readonly G : HerkunftSchulform = new HerkunftSchulform("G", 2, [new HerkunftSchulformKatalogEintrag(3000, "G", "G", Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V), "Grundschule (auch Primarstufe der Volksschule)", null, null)]);

	/**
	 * Gesamtschule
	 */
	public static readonly GE : HerkunftSchulform = new HerkunftSchulform("GE", 3, [new HerkunftSchulformKatalogEintrag(4000, "GE", "GE", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Gesamtschule", null, null)]);

	/**
	 * Gemeinschaftsschule
	 */
	public static readonly GM : HerkunftSchulform = new HerkunftSchulform("GM", 4, [new HerkunftSchulformKatalogEintrag(5000, "GM", "GM", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Gemeinschaftsschule", null, null)]);

	/**
	 * Gymnasium (auch Aufbaugymnasium)
	 */
	public static readonly GY : HerkunftSchulform = new HerkunftSchulform("GY", 5, [new HerkunftSchulformKatalogEintrag(6000, "GY", "GY", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Gymnasium (auch Aufbaugymnasium)", null, null)]);

	/**
	 * Hauptschule (auch Sekundarstufe I der Volksschule)
	 */
	public static readonly H : HerkunftSchulform = new HerkunftSchulform("H", 6, [new HerkunftSchulformKatalogEintrag(7000, "H", "H", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Hauptschule (auch Sekundarstufe I der Volksschule)", null, null)]);

	/**
	 * Hiberniaschule
	 */
	public static readonly HI : HerkunftSchulform = new HerkunftSchulform("HI", 7, [new HerkunftSchulformKatalogEintrag(8000, "HI", "FW", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Hiberniaschule", null, null)]);

	/**
	 * Schulversuch PRIMUS
	 */
	public static readonly PS : HerkunftSchulform = new HerkunftSchulform("PS", 8, [new HerkunftSchulformKatalogEintrag(9000, "PS", "PS", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Schulversuch PRIMUS", null, null)]);

	/**
	 * Realschule (auch Aufbaurealschule)
	 */
	public static readonly R : HerkunftSchulform = new HerkunftSchulform("R", 9, [new HerkunftSchulformKatalogEintrag(10000, "R", "R", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Realschule (auch Aufbaurealschule)", null, null)]);

	/**
	 * Förderschule oder Klinikschule
	 */
	public static readonly S : HerkunftSchulform = new HerkunftSchulform("S", 10, [new HerkunftSchulformKatalogEintrag(11000, "S", "S", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Förderschule oder Klinikschule", null, null)]);

	/**
	 * Sekundarschule
	 */
	public static readonly SK : HerkunftSchulform = new HerkunftSchulform("SK", 11, [new HerkunftSchulformKatalogEintrag(15000, "SK", "SE", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), "Sekundarschule", null, null)]);

	/**
	 * Weiterbildungskolleg
	 */
	public static readonly WB : HerkunftSchulform = new HerkunftSchulform("WB", 12, [new HerkunftSchulformKatalogEintrag(18000, "WB", "WB", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GE, Schulform.GY, Schulform.PS, Schulform.SG), "Weiterbildungskolleg", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Herkunftsschulform, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : HerkunftSchulformKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Herkunftsschulform
	 */
	public readonly historie : Array<HerkunftSchulformKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Herkunftsschulformen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, HerkunftSchulform | null> = new HashMap();

	/**
	 * Erzeugt eine neue Herkunftsschulform in der Aufzählung.
	 *
	 * @param historie   die Historie der Herkunftsschulform, welche ein Array von
	 *                   {@link HerkunftSchulformKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<HerkunftSchulformKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		HerkunftSchulform.all_values_by_ordinal.push(this);
		HerkunftSchulform.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Herkunftsschulformen auf die
	 * zugehörigen Herkunftsschulformen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Herkunftsschulformen
	 */
	private static getMapByKuerzel() : HashMap<string, HerkunftSchulform | null> {
		if (HerkunftSchulform._ebenen.size() === 0) {
			for (const s of HerkunftSchulform.values()) {
				if (s.daten !== null)
					HerkunftSchulform._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return HerkunftSchulform._ebenen;
	}

	/**
	 * Gibt die Herkunftsschulform für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Herkunftsschulform
	 *
	 * @return die Herkunftsschulform oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : HerkunftSchulform | null {
		return HerkunftSchulform.getMapByKuerzel().get(kuerzel);
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
		if (!(other instanceof HerkunftSchulform))
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
	public compareTo(other : HerkunftSchulform) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<HerkunftSchulform> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : HerkunftSchulform | null {
		const tmp : HerkunftSchulform | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schueler.HerkunftSchulform'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schueler_HerkunftSchulform(obj : unknown) : HerkunftSchulform {
	return obj as HerkunftSchulform;
}
