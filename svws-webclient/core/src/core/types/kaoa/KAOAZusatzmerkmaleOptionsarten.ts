import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';

export class KAOAZusatzmerkmaleOptionsarten extends JavaEnum<KAOAZusatzmerkmaleOptionsarten> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAZusatzmerkmaleOptionsarten> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAZusatzmerkmaleOptionsarten> = new Map<string, KAOAZusatzmerkmaleOptionsarten>();

	/**
	 * Keine Option für das KAoA-Zusatzmerkmal
	 */
	public static readonly KEINE : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("KEINE", 0, null);

	/**
	 * Anschlussoptionen laut SBO 10.7
	 */
	public static readonly ANSCHLUSSOPTION : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("ANSCHLUSSOPTION", 1, "Anschlussoption");

	/**
	 * Berufsfelder
	 */
	public static readonly BERUFSFELD : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("BERUFSFELD", 2, "Berufsfeld");

	/**
	 * Freitext
	 */
	public static readonly FREITEXT : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("FREITEXT", 3, "Freitext");

	/**
	 * Freitext Beruf
	 */
	public static readonly FREITEXT_BERUF : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("FREITEXT_BERUF", 4, "Freitext Beruf");

	/**
	 * SBO der Ebene 4 (SBO x.x.x.y)
	 */
	public static readonly SBO_EBENE_4 : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("SBO_EBENE_4", 5, "SBO EB4");

	/**
	 * Das Kürzel für die Optionsart
	 */
	public readonly kuerzel : string | null;

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 *
	 * @param kuerzel   das Kürzel
	 */
	private constructor(name : string, ordinal : number, kuerzel : string | null) {
		super(name, ordinal);
		KAOAZusatzmerkmaleOptionsarten.all_values_by_ordinal.push(this);
		KAOAZusatzmerkmaleOptionsarten.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die Optionsart anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel
	 *
	 * @return die Optionsart oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : KAOAZusatzmerkmaleOptionsarten | null {
		if (kuerzel === null)
			return KAOAZusatzmerkmaleOptionsarten.KEINE;
		for (const art of KAOAZusatzmerkmaleOptionsarten.values())
			if (JavaObject.equalsTranspiler(kuerzel, (art.kuerzel)))
				return art;
		return null;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAZusatzmerkmaleOptionsarten> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAZusatzmerkmaleOptionsarten | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kaoa.KAOAZusatzmerkmaleOptionsarten', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kaoa_KAOAZusatzmerkmaleOptionsarten(obj : unknown) : KAOAZusatzmerkmaleOptionsarten {
	return obj as KAOAZusatzmerkmaleOptionsarten;
}
