import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';

export class KAOAMerkmaleOptionsarten extends JavaEnum<KAOAMerkmaleOptionsarten> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAMerkmaleOptionsarten> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAMerkmaleOptionsarten> = new Map<string, KAOAMerkmaleOptionsarten>();

	/**
	 * Keine Option für das KAoA-Merkmal
	 */
	public static readonly KEINE : KAOAMerkmaleOptionsarten = new KAOAMerkmaleOptionsarten("KEINE", 0, null);

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
		KAOAMerkmaleOptionsarten.all_values_by_ordinal.push(this);
		KAOAMerkmaleOptionsarten.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die Optionsart anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel
	 *
	 * @return die Optionsart oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : KAOAMerkmaleOptionsarten | null {
		for (const art of KAOAMerkmaleOptionsarten.values())
			if (JavaObject.equalsTranspiler(art.kuerzel, (kuerzel)))
				return art;
		return null;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAMerkmaleOptionsarten> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAMerkmaleOptionsarten | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.kaoa.KAOAMerkmaleOptionsarten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kaoa.KAOAMerkmaleOptionsarten', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kaoa_KAOAMerkmaleOptionsarten(obj : unknown) : KAOAMerkmaleOptionsarten {
	return obj as KAOAMerkmaleOptionsarten;
}
