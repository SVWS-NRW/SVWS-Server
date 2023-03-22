import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../core/data/gost/GostFach';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { ZulaessigesFach, cast_de_nrw_schule_svws_core_types_fach_ZulaessigesFach } from '../../../core/types/fach/ZulaessigesFach';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostFachbereich extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GostFachbereich> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, GostFachbereich> = new Map<string, GostFachbereich>();

	/**
	 * Fachbereich deutsch
	 */
	public static readonly DEUTSCH : GostFachbereich = new GostFachbereich("DEUTSCH", 0, null, ZulaessigesFach.D);

	/**
	 * Fachbereich fremdsprachllich
	 */
	public static readonly FREMDSPRACHE : GostFachbereich = new GostFachbereich("FREMDSPRACHE", 1, null, ZulaessigesFach.C, ZulaessigesFach.C0, ZulaessigesFach.C5, ZulaessigesFach.C6, ZulaessigesFach.C7, ZulaessigesFach.C8, ZulaessigesFach.C9, ZulaessigesFach.E, ZulaessigesFach.F, ZulaessigesFach.F0, ZulaessigesFach.F5, ZulaessigesFach.F6, ZulaessigesFach.F7, ZulaessigesFach.F8, ZulaessigesFach.F9, ZulaessigesFach.G, ZulaessigesFach.G0, ZulaessigesFach.G5, ZulaessigesFach.G6, ZulaessigesFach.G7, ZulaessigesFach.G8, ZulaessigesFach.G9, ZulaessigesFach.H, ZulaessigesFach.H0, ZulaessigesFach.H5, ZulaessigesFach.H6, ZulaessigesFach.H7, ZulaessigesFach.H8, ZulaessigesFach.H9, ZulaessigesFach.I, ZulaessigesFach.I0, ZulaessigesFach.I5, ZulaessigesFach.I6, ZulaessigesFach.I7, ZulaessigesFach.I8, ZulaessigesFach.I9, ZulaessigesFach.K, ZulaessigesFach.K0, ZulaessigesFach.K5, ZulaessigesFach.K6, ZulaessigesFach.K7, ZulaessigesFach.K8, ZulaessigesFach.K9, ZulaessigesFach.L, ZulaessigesFach.L0, ZulaessigesFach.L5, ZulaessigesFach.L6, ZulaessigesFach.L7, ZulaessigesFach.L8, ZulaessigesFach.L9, ZulaessigesFach.N, ZulaessigesFach.N0, ZulaessigesFach.N5, ZulaessigesFach.N6, ZulaessigesFach.N7, ZulaessigesFach.N8, ZulaessigesFach.N9, ZulaessigesFach.O, ZulaessigesFach.O0, ZulaessigesFach.O5, ZulaessigesFach.O6, ZulaessigesFach.O7, ZulaessigesFach.O8, ZulaessigesFach.O9, ZulaessigesFach.R, ZulaessigesFach.R0, ZulaessigesFach.R5, ZulaessigesFach.R6, ZulaessigesFach.R7, ZulaessigesFach.R8, ZulaessigesFach.R9, ZulaessigesFach.S, ZulaessigesFach.S0, ZulaessigesFach.S5, ZulaessigesFach.S6, ZulaessigesFach.S7, ZulaessigesFach.S8, ZulaessigesFach.S9, ZulaessigesFach.T, ZulaessigesFach.T0, ZulaessigesFach.T5, ZulaessigesFach.T6, ZulaessigesFach.T7, ZulaessigesFach.T8, ZulaessigesFach.T9, ZulaessigesFach.Z, ZulaessigesFach.Z0, ZulaessigesFach.Z5, ZulaessigesFach.Z6, ZulaessigesFach.Z7, ZulaessigesFach.Z8, ZulaessigesFach.Z9);

	/**
	 * Fachbereich künstlerisch musikalisch
	 */
	public static readonly KUNST_MUSIK : GostFachbereich = new GostFachbereich("KUNST_MUSIK", 2, null, ZulaessigesFach.KU, ZulaessigesFach.MU);

	/**
	 * Fachbereich Ersatz für literarisch künstlerisch
	 */
	public static readonly LITERARISCH_KUENSTLERISCH_ERSATZ : GostFachbereich = new GostFachbereich("LITERARISCH_KUENSTLERISCH_ERSATZ", 3, null, ZulaessigesFach.LI, ZulaessigesFach.IN, ZulaessigesFach.VO);

	/**
	 * Fachbereich literarisch künstlerisch
	 */
	public static readonly LITERARISCH_KUENSTLERISCH : GostFachbereich = new GostFachbereich("LITERARISCH_KUENSTLERISCH", 4, Arrays.asList(GostFachbereich.KUNST_MUSIK, GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ));

	/**
	 * Fachbereich sprachlich literarisch künstlerisch
	 */
	public static readonly SPRACHLICH_LITERARISCH_KUENSTLERISCH : GostFachbereich = new GostFachbereich("SPRACHLICH_LITERARISCH_KUENSTLERISCH", 5, Arrays.asList(GostFachbereich.DEUTSCH, GostFachbereich.FREMDSPRACHE, GostFachbereich.KUNST_MUSIK, GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ));

	/**
	 * Fachbereich geschichtlich
	 */
	public static readonly GESCHICHTE : GostFachbereich = new GostFachbereich("GESCHICHTE", 6, null, ZulaessigesFach.GE);

	/**
	 * Fachbereich sozialwissenschaftlich
	 */
	public static readonly SOZIALWISSENSCHAFTEN : GostFachbereich = new GostFachbereich("SOZIALWISSENSCHAFTEN", 7, null, ZulaessigesFach.SW);

	/**
	 * Fachbereich philosophisch
	 */
	public static readonly PHILOSOPHIE : GostFachbereich = new GostFachbereich("PHILOSOPHIE", 8, null, ZulaessigesFach.PL);

	/**
	 * Fachbereich Sonstige gesellschaftswissenschaftliche
	 */
	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE : GostFachbereich = new GostFachbereich("GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE", 9, null, ZulaessigesFach.EK, ZulaessigesFach.PA, ZulaessigesFach.PS, ZulaessigesFach.RK);

	/**
	 * Fachbereich gesellschaftswissenschaftlich
	 */
	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH : GostFachbereich = new GostFachbereich("GESELLSCHAFTSWISSENSCHAFTLICH", 10, Arrays.asList(GostFachbereich.GESCHICHTE, GostFachbereich.SOZIALWISSENSCHAFTEN, GostFachbereich.PHILOSOPHIE, GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE));

	/**
	 * Fachbereich Religion
	 */
	public static readonly RELIGION : GostFachbereich = new GostFachbereich("RELIGION", 11, null, ZulaessigesFach.HR, ZulaessigesFach.OR, ZulaessigesFach.YR, ZulaessigesFach.ER, ZulaessigesFach.KR, ZulaessigesFach.IL);

	/**
	 * Fachbereich gesellschaftswissenschaftlich mit Religion
	 */
	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION : GostFachbereich = new GostFachbereich("GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION", 12, Arrays.asList(GostFachbereich.GESCHICHTE, GostFachbereich.SOZIALWISSENSCHAFTEN, GostFachbereich.PHILOSOPHIE, GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE, GostFachbereich.RELIGION));

	/**
	 * Fachbereich mathematisch
	 */
	public static readonly MATHEMATIK : GostFachbereich = new GostFachbereich("MATHEMATIK", 13, null, ZulaessigesFach.M);

	/**
	 * Fachbereich klassisch naturwissenschaftlich
	 */
	public static readonly NATURWISSENSCHAFTLICH_KLASSISCH : GostFachbereich = new GostFachbereich("NATURWISSENSCHAFTLICH_KLASSISCH", 14, null, ZulaessigesFach.BI, ZulaessigesFach.CH, ZulaessigesFach.PH);

	/**
	 * Fachbereich naturwissenschaftlich neueinsetzend
	 */
	public static readonly NATURWISSENSCHAFTLICH_NEU_EINSETZEND : GostFachbereich = new GostFachbereich("NATURWISSENSCHAFTLICH_NEU_EINSETZEND", 15, null, ZulaessigesFach.EL, ZulaessigesFach.IF, ZulaessigesFach.TC);

	/**
	 * Fachbereich naturwissenschaftlich
	 */
	public static readonly NATURWISSENSCHAFTLICH : GostFachbereich = new GostFachbereich("NATURWISSENSCHAFTLICH", 16, Arrays.asList(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH, GostFachbereich.NATURWISSENSCHAFTLICH_NEU_EINSETZEND));

	/**
	 * Fachbereich mathematisch naturwissenschaftlich
	 */
	public static readonly MATHEMATISCH_NATURWISSENSCHAFTLICH : GostFachbereich = new GostFachbereich("MATHEMATISCH_NATURWISSENSCHAFTLICH", 17, Arrays.asList(GostFachbereich.MATHEMATIK, GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH, GostFachbereich.NATURWISSENSCHAFTLICH_NEU_EINSETZEND));

	/**
	 * Fachbereich sportlich
	 */
	public static readonly SPORT : GostFachbereich = new GostFachbereich("SPORT", 18, null, ZulaessigesFach.SP);

	/**
	 * Eine Map, welche dem zulässigen Fach alle seine Fachbereiche zuordnet.
	 */
	private static readonly _mapFachbereichByFach : HashMap<ZulaessigesFach, List<GostFachbereich>> = new HashMap();

	/**
	 * Eine Liste der Fächern dieses Fachbereichs
	 */
	private readonly faecher : Vector<ZulaessigesFach> = new Vector();

	/**
	 * Eine Liste der Fächerkürzel dieses Fachbereichs
	 */
	private readonly kuerzel : Vector<string> = new Vector();

	/**
	 * Erstellt einen neuen Fachbereich als Kombination der übergebenen Fachbereiche
	 * und der übergebenen Fächer
	 * 
	 * @param fachbereiche   die Fachbereiche
	 * @param kuerzel        die zusätzlichen Kürzel des Fachbereichs
	 */
	private constructor(name : string, ordinal : number, fachbereiche : List<GostFachbereich> | null, ...faecher : Array<ZulaessigesFach>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostFachbereich.all_values_by_ordinal.push(this);
		GostFachbereich.all_values_by_name.set(name, this);
		if (fachbereiche !== null) {
			for (let fb of fachbereiche) {
				for (let fach of fb.faecher) {
					this.faecher.add(fach);
					this.kuerzel.add(fach.daten.kuerzelASD);
				}
			}
		}
		for (let fach of faecher) {
			this.faecher.add(fach);
			this.kuerzel.add(fach.daten.kuerzelASD);
		}
	}

	/**
	 * Gibt eine Map von den Fächern auf die zugehörigen Fachbereiche
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Fächern auf die zugehörigen Fachbereiche
	 */
	private static getMapFachbereichByFach() : HashMap<ZulaessigesFach, List<GostFachbereich>> {
		if (GostFachbereich._mapFachbereichByFach.size() === 0) {
			for (let fb of GostFachbereich.values()) {
				for (let fach of fb.faecher) {
					let listFachbereichByFach : List<GostFachbereich> | null = GostFachbereich._mapFachbereichByFach.get(fach);
					if (listFachbereichByFach === null) {
						listFachbereichByFach = new Vector();
						GostFachbereich._mapFachbereichByFach.put(fach, listFachbereichByFach);
					}
					listFachbereichByFach.add(fb);
				}
			}
		}
		return GostFachbereich._mapFachbereichByFach;
	}

	/**
	 * Gibt die Liste der Fächer des Fachbereichs zurück.
	 * 
	 * @return die Liste der Fächer des Fachbereichs
	 */
	public getFaecher() : List<ZulaessigesFach> {
		return this.faecher;
	}

	/**
	 * Prüft, ob das übergebene Fach zu diesem Fachbereich gehört.
	 * 
	 * @param fach   das zu prüfende Fach
	 * 
	 * @return true, falls das Fach zu dem Fachbereich gehört, sonst false
	 */
	public hat(fach : GostFach | null) : boolean;

	/**
	 * Prüft, ob das Fach mit dem übergebenen Kürzel zu diesem Fachbereich gehört.
	 * 
	 * @param kuerzel   das Kürzel des zu prüfenden Faches
	 * 
	 * @return true, falls das Fach zu dem Fachbereich gehört, sonst false
	 */
	public hat(kuerzel : string | null) : boolean;

	/**
	 * Implementation for method overloads of 'hat'
	 */
	public hat(__param0 : GostFach | null | string) : boolean {
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostFach'))) || (__param0 === null))) {
			let fach : GostFach | null = cast_de_nrw_schule_svws_core_data_gost_GostFach(__param0);
			return fach === null ? false : this.hat(fach.kuerzel);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 === "string") || (__param0 === null))) {
			let kuerzel : string | null = __param0;
			if (kuerzel === null) 
				return false;
			return this.kuerzel.contains(kuerzel);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Ermittelt die, dem Fach zugehörigen, Fachbereiche
	 *  
	 * @param fach   das Fach
	 * 
	 * @return die zugehörigen Fachbereiche
	 */
	public static getBereiche(fach : GostFach | null) : List<GostFachbereich> {
		if (fach === null) 
			return new Vector();
		let zulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
		let bereiche : List<GostFachbereich> | null = GostFachbereich.getMapFachbereichByFach().get(zulFach);
		if (bereiche !== null) 
			return bereiche;
		return new Vector();
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
		if (!(other instanceof GostFachbereich))
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
	public compareTo(other : GostFachbereich) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostFachbereich> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostFachbereich | null {
		let tmp : GostFachbereich | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.gost.GostFachbereich'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_gost_GostFachbereich(obj : unknown) : GostFachbereich {
	return obj as GostFachbereich;
}
