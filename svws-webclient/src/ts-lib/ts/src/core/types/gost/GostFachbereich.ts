import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../core/data/gost/GostFach';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { System, cast_java_lang_System } from '../../../java/lang/System';

export class GostFachbereich extends JavaObject {

	private static all : Vector<GostFachbereich> = new Vector();

	private static kuerzel_DEUTSCH : Array<String> = ["D"];

	private static kuerzel_FREMDSPRACHE : Array<String> = ["A", "A0", "A5", "A6", "A7", "A8", "A9", "C", "C0", "C5", "C6", "C7", "C8", "C9", "E", "E0", "E5", "E6", "E7", "E8", "E9", "F", "F0", "F5", "F6", "F7", "F8", "F9", "G", "G0", "G5", "G6", "G7", "G8", "G9", "H", "H0", "H5", "H6", "H7", "H8", "H9", "I", "I0", "I5", "I6", "I7", "I8", "I9", "K", "K0", "K5", "K6", "K7", "K8", "K9", "L", "L0", "L5", "L6", "L7", "L8", "L9", "N", "N0", "N5", "N6", "N7", "N8", "N9", "O", "O0", "O5", "O6", "O7", "O8", "O9", "P", "P0", "P5", "P6", "P7", "P8", "P9", "R", "R0", "R5", "R6", "R7", "R8", "R9", "S", "S0", "S5", "S6", "S7", "S8", "S9", "T", "T0", "T5", "T6", "T7", "T8", "T9", "U", "U0", "U5", "U6", "U7", "U8", "U9", "Z", "Z0", "Z5", "Z6", "Z7", "Z8", "Z9"];

	private static kuerzel_KUNST_MUSIK : Array<String> = ["KU", "MU"];

	private static kuerzel_LITERARISCH_KUENSTLERISCH_ERSATZ : Array<String> = ["LI", "IN", "IV", "VO"];

	private static kuerzel_GESCHICHTE : Array<String> = ["GE"];

	private static kuerzel_SOZIALWISSENSCHAFTEN : Array<String> = ["SW"];

	private static kuerzel_PHILOSOPHIE : Array<String> = ["PL"];

	private static kuerzel_GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE : Array<String> = ["EK", "PA", "PS", "RK"];

	private static kuerzel_MATHEMATIK : Array<String> = ["M"];

	private static kuerzel_NATURWISSENSCHAFTLICH_KLASSISCH : Array<String> = ["BI", "CH", "PH"];

	private static kuerzel_NATURWISSENSCHAFTLICH_NEU_EINSETZEND : Array<String> = ["EL", "IF", "TC"];

	private static kuerzel_RELIGION : Array<String> = ["HR", "OR", "YR", "ER", "KR", "IL"];

	private static kuerzel_SPORT : Array<String> = ["SP"];

	public static readonly SPRACHLICH_LITERARISCH_KUENSTLERISCH : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_DEUTSCH, GostFachbereich.kuerzel_FREMDSPRACHE, GostFachbereich.kuerzel_KUNST_MUSIK, GostFachbereich.kuerzel_LITERARISCH_KUENSTLERISCH_ERSATZ);

	public static readonly DEUTSCH : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_DEUTSCH);

	public static readonly FREMDSPRACHE : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_FREMDSPRACHE);

	public static readonly KUNST_MUSIK : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_KUNST_MUSIK);

	public static readonly LITERARISCH_KUENSTLERISCH : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_KUNST_MUSIK, GostFachbereich.kuerzel_LITERARISCH_KUENSTLERISCH_ERSATZ);

	public static readonly LITERARISCH_KUENSTLERISCH_ERSATZ : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_LITERARISCH_KUENSTLERISCH_ERSATZ);

	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_GESCHICHTE, GostFachbereich.kuerzel_SOZIALWISSENSCHAFTEN, GostFachbereich.kuerzel_PHILOSOPHIE, GostFachbereich.kuerzel_GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);

	public static readonly GESCHICHTE : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_GESCHICHTE);

	public static readonly SOZIALWISSENSCHAFTEN : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_SOZIALWISSENSCHAFTEN);

	public static readonly PHILOSOPHIE : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_PHILOSOPHIE);

	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);

	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_GESCHICHTE, GostFachbereich.kuerzel_SOZIALWISSENSCHAFTEN, GostFachbereich.kuerzel_PHILOSOPHIE, GostFachbereich.kuerzel_GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE, GostFachbereich.kuerzel_RELIGION);

	public static readonly MATHEMATIK : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_MATHEMATIK);

	public static readonly MATHEMATISCH_NATURWISSENSCHAFTLICH : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_MATHEMATIK, GostFachbereich.kuerzel_NATURWISSENSCHAFTLICH_KLASSISCH, GostFachbereich.kuerzel_NATURWISSENSCHAFTLICH_NEU_EINSETZEND);

	public static readonly NATURWISSENSCHAFTLICH : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_NATURWISSENSCHAFTLICH_KLASSISCH, GostFachbereich.kuerzel_NATURWISSENSCHAFTLICH_NEU_EINSETZEND);

	public static readonly NATURWISSENSCHAFTLICH_KLASSISCH : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_NATURWISSENSCHAFTLICH_KLASSISCH);

	public static readonly NATURWISSENSCHAFTLICH_NEU_EINSETZEND : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_NATURWISSENSCHAFTLICH_NEU_EINSETZEND);

	public static readonly RELIGION : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_RELIGION);

	public static readonly SPORT : GostFachbereich = new GostFachbereich(GostFachbereich.kuerzel_SPORT);

	private readonly kuerzel : Array<String>;


	/**
	 * Erstellt einen neuen Fachbereich mit den übergebenen Kürzeln von Fächern
	 * 
	 * @param kuerzel   die Kürzel der Fächer des Fachbereichs
	 */
	private constructor(...kuerzelArrays : Array<Array<String | null>>) {
		super();
		let pos : number = 0;
		for (let a of kuerzelArrays) {
			pos += a.length;
		}
		this.kuerzel = Array(pos).fill(null);
		pos = 0;
		for (let a of kuerzelArrays) {
			System.arraycopy(a, 0, this.kuerzel, pos, a.length);
			pos += a.length;
		}
		GostFachbereich.all.add(this);
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
	public hat(kuerzel : String | null) : boolean;

	/**
	 * Implementation for method overloads of 'hat'
	 */
	public hat(__param0 : GostFach | String | null) : boolean {
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostFach'))) || (__param0 === null))) {
			let fach : GostFach | null = cast_de_nrw_schule_svws_core_data_gost_GostFach(__param0);
			return fach === null ? false : this.hat(fach.kuerzel);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof String) || (typeof __param0 === "string")) || (__param0 === null))) {
			let kuerzel : String | null = __param0;
			if (kuerzel === null) 
				return false;
			let fbAlleKuerzel : Array<String> = this.getAlleKuerzel();
			for (let fbKuerzel of fbAlleKuerzel) {
				if (JavaObject.equalsTranspiler(kuerzel, (fbKuerzel))) 
					return true;
			}
			return false;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Liefert alle Kürzel der Fächer zurück, die zu diesem Fachbereich gehören.
	 *  
	 * @return alle Kürzel der Fächer dieses Fachbereichs
	 */
	public getAlleKuerzel() : Array<String> {
		return this.kuerzel;
	}

	/**
	 * Ermittelt die, dem Fach zugehörigen, Fachbereiche anhand des Statistik-Kürzels
	 *  
	 * @param kuerzel   das Statistik-Kürzel des Faches
	 * 
	 * @return die zugehörigen Fachbereiche
	 */
	public static getBereiche(kuerzel : String | null) : List<GostFachbereich>;

	/**
	 * Ermittelt die, dem Fach zugehörigen, Fachbereiche
	 *  
	 * @param fach   das Fach
	 * 
	 * @return die zugehörigen Fachbereiche
	 */
	public static getBereiche(fach : GostFach | null) : List<GostFachbereich>;

	/**
	 * Implementation for method overloads of 'getBereiche'
	 */
	public static getBereiche(__param0 : GostFach | String | null) : List<GostFachbereich> {
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof String) || (typeof __param0 === "string")) || (__param0 === null))) {
			let kuerzel : String | null = __param0;
			let result : Vector<GostFachbereich> = new Vector();
			for (let i : number = 0; i < GostFachbereich.all.size(); i++){
				if (GostFachbereich.all.get(i).hat(kuerzel)) 
					result.add(GostFachbereich.all.get(i));
			}
			return result;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostFach'))) || (__param0 === null))) {
			let fach : GostFach | null = cast_de_nrw_schule_svws_core_data_gost_GostFach(__param0);
			let result : Vector<GostFachbereich> = new Vector();
			for (let i : number = 0; i < GostFachbereich.all.size(); i++){
				if (GostFachbereich.all.get(i).hat(fach)) 
					result.add(GostFachbereich.all.get(i));
			}
			return result;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Liefert eine Liste mit allen Fachbereichen zurück.
	 *  
	 * @return eine Liste mit allen Fachbereichen.
	 */
	public static values() : List<GostFachbereich> {
		return new Vector(GostFachbereich.all);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.gost.GostFachbereich'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_gost_GostFachbereich(obj : unknown) : GostFachbereich {
	return obj as GostFachbereich;
}
