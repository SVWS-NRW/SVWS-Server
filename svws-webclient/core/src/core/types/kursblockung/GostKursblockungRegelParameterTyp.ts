import { JavaEnum } from '../../../java/lang/JavaEnum';

export class GostKursblockungRegelParameterTyp extends JavaEnum<GostKursblockungRegelParameterTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostKursblockungRegelParameterTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostKursblockungRegelParameterTyp> = new Map<string, GostKursblockungRegelParameterTyp>();

	/**
	 * Der Parameter-Typ Kursart.
	 */
	public static readonly KURSART : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("KURSART", 0, );

	/**
	 * Der Parameter-Typ Schienennummer.
	 */
	public static readonly SCHIENEN_NR : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("SCHIENEN_NR", 1, );

	/**
	 * Der Parameter-Typ Kurs-ID.
	 */
	public static readonly KURS_ID : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("KURS_ID", 2, );

	/**
	 * Der Parameter Typ Schüler-ID.
	 */
	public static readonly SCHUELER_ID : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("SCHUELER_ID", 3, );

	/**
	 * Der Parameter Typ für eine Ja=1/Nein=0 Entscheidung.
	 */
	public static readonly BOOLEAN : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("BOOLEAN", 4, );

	/**
	 * Der Parameter Typ für eine ganze Zahl.
	 */
	public static readonly GANZZAHL : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("GANZZAHL", 5, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		GostKursblockungRegelParameterTyp.all_values_by_ordinal.push(this);
		GostKursblockungRegelParameterTyp.all_values_by_name.set(name, this);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostKursblockungRegelParameterTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostKursblockungRegelParameterTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kursblockung_GostKursblockungRegelParameterTyp(obj : unknown) : GostKursblockungRegelParameterTyp {
	return obj as GostKursblockungRegelParameterTyp;
}
