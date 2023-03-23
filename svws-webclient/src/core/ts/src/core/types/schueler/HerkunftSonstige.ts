import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { HerkunftSonstigeKatalogEintrag } from '../../../core/data/schule/HerkunftSonstigeKatalogEintrag';
import { Arrays } from '../../../java/util/Arrays';

export class HerkunftSonstige extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<HerkunftSonstige> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, HerkunftSonstige> = new Map<string, HerkunftSonstige>();

	/**
	 * Ausländische Schüler, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind
	 */
	public static readonly AS : HerkunftSonstige = new HerkunftSonstige("AS", 0, [new HerkunftSonstigeKatalogEintrag(1000, "AS", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.WB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V), "Ausländische Schüler, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind", null, null)]);

	/**
	 * Keine Schule bzw. kein Förderschulkindergarten (Einschulung)
	 */
	public static readonly ES : HerkunftSonstige = new HerkunftSonstige("ES", 1, [new HerkunftSonstigeKatalogEintrag(2000, "ES", Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.PS, Schulform.KS, Schulform.S, Schulform.V), "Keine Schule bzw. kein Förderschulkindergarten (Einschulung)", null, null)]);

	/**
	 * Hausfrüherziehung für Hör- bzw. Sehgeschädigte
	 */
	public static readonly FE : HerkunftSonstige = new HerkunftSonstige("FE", 2, [new HerkunftSonstigeKatalogEintrag(3000, "FE", Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.KS, Schulform.S), "Hausfrüherziehung für Hör- bzw. Sehgeschädigte", null, null)]);

	/**
	 * Hochschule, Universität
	 */
	public static readonly HU : HerkunftSonstige = new HerkunftSonstige("HU", 3, [new HerkunftSonstigeKatalogEintrag(4000, "HU", Arrays.asList(Schulform.BK, Schulform.SB), "Hochschule, Universität", null, null)]);

	/**
	 * Förderschulkindergarten (einschließlich frühkindliche Förderung)
	 */
	public static readonly SK : HerkunftSonstige = new HerkunftSonstige("SK", 4, [new HerkunftSonstigeKatalogEintrag(5000, "SK", Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.PS, Schulform.KS, Schulform.S, Schulform.V), "Förderschulkindergarten (einschließlich frühkindliche Förderung)", null, null)]);

	/**
	 * Herkunft noch unbekannt (nur Gliederung A12, A13)
	 */
	public static readonly UN : HerkunftSonstige = new HerkunftSonstige("UN", 5, [new HerkunftSonstigeKatalogEintrag(6000, "UN", Arrays.asList(Schulform.BK, Schulform.SB), "Herkunft noch unbekannt (nur Gliederung A12, A13)", null, null)]);

	/**
	 * Wehr-, Zivil- oder Bundesfreiwilligendienst
	 */
	public static readonly WZ : HerkunftSonstige = new HerkunftSonstige("WZ", 6, [new HerkunftSonstigeKatalogEintrag(8000, "WZ", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.WB), "Wehr-, Zivil- oder Bundesfreiwilligendienst", null, null)]);

	/**
	 * Berufstätigkeit (z. B. vor Besuch einer Fachschule)
	 */
	public static readonly XB : HerkunftSonstige = new HerkunftSonstige("XB", 7, [new HerkunftSonstigeKatalogEintrag(9000, "XB", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.WB), "Berufstätigkeit (z. B. vor Besuch einer Fachschule)", null, null)]);

	/**
	 * Sonstige Schule bzw. keine Schule, auch seit den letzten amtlichen Schuldaten aus dem Ausland zugezogene deutsche Schüler
	 */
	public static readonly XS : HerkunftSonstige = new HerkunftSonstige("XS", 8, [new HerkunftSonstigeKatalogEintrag(10000, "XS", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.WB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V), "Sonstige Schule bzw. keine Schule, auch seit den letzten amtlichen Schuldaten aus dem Ausland zugezogene deutsche Schüler", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der sonstigen Herkunft, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : HerkunftSonstigeKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der sonstigen Herkunft
	 */
	public readonly historie : Array<HerkunftSonstigeKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten sonstigen Herkünfte, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, HerkunftSonstige | null> = new HashMap();

	/**
	 * Erzeugt eine neue sonstige Herkunft in der Aufzählung.
	 *
	 * @param historie   die Historie der sonstigen Herkunft, welches ein Array von
	 *                   {@link HerkunftSonstigeKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<HerkunftSonstigeKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		HerkunftSonstige.all_values_by_ordinal.push(this);
		HerkunftSonstige.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der sonstigen Herkünfte auf die
	 * zugehörigen sonstigen Herkünfte zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen sonstigen Herkünfte
	 */
	private static getMapByKuerzel() : HashMap<string, HerkunftSonstige | null> {
		if (HerkunftSonstige._ebenen.size() === 0) {
			for (let s of HerkunftSonstige.values()) {
				if (s.daten !== null)
					HerkunftSonstige._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return HerkunftSonstige._ebenen;
	}

	/**
	 * Gibt die sonstige Herkunft für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der sonstigen Herkunft
	 *
	 * @return die sonstige Herkunft oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : HerkunftSonstige | null {
		return HerkunftSonstige.getMapByKuerzel().get(kuerzel);
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
		if (!(other instanceof HerkunftSonstige))
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
	public compareTo(other : HerkunftSonstige) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<HerkunftSonstige> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : HerkunftSonstige | null {
		const tmp : HerkunftSonstige | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schueler.HerkunftSonstige'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schueler_HerkunftSonstige(obj : unknown) : HerkunftSonstige {
	return obj as HerkunftSonstige;
}
