import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { Schulform, cast_de_nrw_schule_svws_core_types_statkue_Schulform } from '../../../core/types/statkue/Schulform';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { RGBFarbe, cast_de_nrw_schule_svws_core_types_RGBFarbe } from '../../../core/types/RGBFarbe';

export class Fachgruppe extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Fachgruppe> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, Fachgruppe> = new Map<String, Fachgruppe>();

	public static readonly FG1_D : Fachgruppe = new Fachgruppe("FG1_D", 0, 1, 1, 110, "Deutsch", "D", new RGBFarbe(253, 233, 217), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 0, true, null, null);

	public static readonly FG2_AL : Fachgruppe = new Fachgruppe("FG2_AL", 1, 2, 2, 400, "Arbeitslehre", "AL", new RGBFarbe(253, 221, 195), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 12, true, null, null);

	public static readonly FG3_FS : Fachgruppe = new Fachgruppe("FG3_FS", 2, 3, 2, 100, "Fremdsprachen", "FS", new RGBFarbe(253, 221, 195), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 9, true, null, null);

	public static readonly FG4_MS : Fachgruppe = new Fachgruppe("FG4_MS", 3, 4, 3, 500, "Kunst und Musik", "MS", new RGBFarbe(252, 204, 165), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 13, true, null, null);

	public static readonly FG5_ME : Fachgruppe = new Fachgruppe("FG5_ME", 4, 5, 4, null, "Literatur, instrumental- oder vokalpraktischer Kurs", "ME", new RGBFarbe(252, 204, 165), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 13, false, null, null);

	public static readonly FG6_GS : Fachgruppe = new Fachgruppe("FG6_GS", 5, 6, 5, 300, "Gesellschaftswissenschaft", "GS", new RGBFarbe(234, 241, 222), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 11, true, null, null);

	public static readonly FG7_PL : Fachgruppe = new Fachgruppe("FG7_PL", 6, 7, 5, null, "Philosophie", "PL", new RGBFarbe(234, 241, 222), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 11, false, null, null);

	public static readonly FG8_RE : Fachgruppe = new Fachgruppe("FG8_RE", 7, 8, 6, 900, "Religion", "RE", new RGBFarbe(215, 228, 188), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 6, true, null, null);

	public static readonly FG9_M : Fachgruppe = new Fachgruppe("FG9_M", 8, 9, 7, 700, "Mathematik", "M", new RGBFarbe(197, 217, 241), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 15, true, null, null);

	public static readonly FG10_NW : Fachgruppe = new Fachgruppe("FG10_NW", 9, 10, 8, 200, "Naturwissenschaften", "NW", new RGBFarbe(141, 180, 227), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 10, true, null, null);

	public static readonly FG11_WN : Fachgruppe = new Fachgruppe("FG11_WN", 10, 11, 8, null, "weiteres naturwissenschaftliches / technisches Fach", "WN", new RGBFarbe(141, 180, 227), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 10, false, null, null);

	public static readonly FG12_SP : Fachgruppe = new Fachgruppe("FG12_SP", 11, 12, 9, 600, "Sport", "SP", new RGBFarbe(255, 255, 255), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 14, true, null, null);

	public static readonly FG13_VX : Fachgruppe = new Fachgruppe("FG13_VX", 12, 13, 10, 1500, "Vertiefungskurs", "VX", new RGBFarbe(216, 216, 216), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 0, false, null, null);

	public static readonly FG14_PX : Fachgruppe = new Fachgruppe("FG14_PX", 13, 14, 11, 1600, "Projektkurs", "PX", new RGBFarbe(191, 191, 191), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 0, false, null, null);

	public static readonly FG15_BUE : Fachgruppe = new Fachgruppe("FG15_BUE", 14, 15, null, 10, "Berufsübergreifender Bereich", "BUE", null, Arrays.asList(Schulform.BK, Schulform.SB), 1, false, null, null);

	public static readonly FG16_BBS : Fachgruppe = new Fachgruppe("FG16_BBS", 15, 16, null, 20, "Berufsbezogener Bereich", "BBS", null, Arrays.asList(Schulform.BK, Schulform.SB), 2, false, null, null);

	public static readonly FG17_BBS : Fachgruppe = new Fachgruppe("FG17_BBS", 16, 17, null, 25, "Berufsbezogener Bereich (Schwerpunkt)", "BBS", null, Arrays.asList(Schulform.BK, Schulform.SB), 0, false, null, null);

	public static readonly FG18_DF : Fachgruppe = new Fachgruppe("FG18_DF", 17, 18, null, 30, "Differenzierungsbereich", "DF", null, Arrays.asList(Schulform.BK, Schulform.SB), 3, false, null, null);

	public static readonly FG19_BP : Fachgruppe = new Fachgruppe("FG19_BP", 18, 19, null, 40, "Berufspraktikum", "BP", null, Arrays.asList(Schulform.BK, Schulform.SB), 4, false, null, null);

	public static readonly FG20_BLL : Fachgruppe = new Fachgruppe("FG20_BLL", 19, 20, null, 60, "besondere Lernleistung", "BLL", null, Arrays.asList(Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WB), 8, false, null, null);

	public static readonly FG21_WP : Fachgruppe = new Fachgruppe("FG21_WP", 20, 21, null, 800, "Wahlpflichtbereich", "WP", null, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 16, false, null, null);

	public static readonly FG22_ZUV : Fachgruppe = new Fachgruppe("FG22_ZUV", 21, 22, null, 1000, "Zusätzliche Unterrichtsveranstaltungen", "ZUV", null, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 0, false, null, null);

	public static readonly FG23_ANG : Fachgruppe = new Fachgruppe("FG23_ANG", 22, 23, null, 1100, "Angleichungskurse", "ANG", null, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 0, false, null, null);

	public static readonly FG24_D_SP : Fachgruppe = new Fachgruppe("FG24_D_SP", 23, 24, null, 1200, "Sprache", "D_SP", null, Arrays.asList(Schulform.G, Schulform.KS, Schulform.S, Schulform.V), 0, true, null, null);

	public static readonly FG25_SU : Fachgruppe = new Fachgruppe("FG25_SU", 24, 25, null, 1300, "Sachunterricht", "SU", null, Arrays.asList(Schulform.G, Schulform.KS, Schulform.S, Schulform.V), 0, true, null, null);

	public static readonly FG26_FOE : Fachgruppe = new Fachgruppe("FG26_FOE", 25, 26, null, 1400, "Förderunterricht", "FOE", null, Arrays.asList(Schulform.G, Schulform.KS, Schulform.S, Schulform.V), 0, true, null, null);

	public static readonly FG27_ABA : Fachgruppe = new Fachgruppe("FG27_ABA", 26, 27, null, 1700, "Abschlussarbeit", "ABA", null, Arrays.asList(Schulform.BK, Schulform.SB), 0, false, null, null);

	public static readonly FG28_PA : Fachgruppe = new Fachgruppe("FG28_PA", 27, 28, null, 1800, "Projektarbeit", "PA", null, Arrays.asList(Schulform.BK, Schulform.SB), 0, false, null, null);

	public static readonly FG29_IF : Fachgruppe = new Fachgruppe("FG29_IF", 28, 29, null, 1900, "Informatik (Sek I)", "IF", new RGBFarbe(141, 180, 227), Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB), 10, true, null, null);

	public static VERSION : number = 1;

	private static readonly _mapByID : HashMap<Number, Fachgruppe> = new HashMap();

	private static readonly _mapByKuerzel : HashMap<String, Fachgruppe> = new HashMap();

	public readonly id : number;

	public readonly fachbereich : Number | null;

	public readonly idSchild : Number | null;

	public readonly bezeichnung : String;

	public readonly kuerzel : String;

	public readonly farbe : RGBFarbe;

	public readonly schulformenKuerzel : List<String> = new Vector();

	private schulformen : Vector<Schulform> | null = null;

	public readonly sortierung : number;

	public readonly fuerZeugnis : boolean;

	public readonly gueltigVon : Number | null;

	public readonly gueltigBis : Number | null;

	/**
	 * Erzeugt eine neue Fachgruppe in der Aufzählung.
	 *  
	 * @param id               die eindeutige ID der Fachgruppe
	 * @param fachbereich      die Nummer für den Fachbereich, sofern festgelegt, ansonsten null
	 * @param idSchild         die alte Fachgruppen-ID, welche in Schild_NRW 2.x verwendet wurde 
	 * @param bezeichnung      die Bezeichnung der Fachgruppe
	 * @param kuerzel          das Kürzel der Fachgruppe
	 * @param farbe            die Farbe, die der Fachgruppe zugeordnet wird oder null, falls die Standard-Farbe Weiss 
	 *                         zugeordnet werden soll. Diese 
	 * @param schulformen      eine Liste mit den Schulformen, bei denen die Fachgruppe vorkommt
	 * @param sortierung       ein Zahlwert, welche die Standard-Reihenfolge der Fachgruppen in der Visualisierung angibt
	 * @param fuerZeugnis      gibt an, ob die Fachgruppe für die Unterteilung auf Zeugnissen genutzt wird oder nicht
	 * @param gueltigVon       gibt an, in welchem Schuljahr die Fachgruppe einführt wurde - ist kein Schuljahr bekannt, so wird null gesetzt
	 * @param gueltigBis       gibt an, bis zu welchem Schuljahr die Fachgruppe gültig ist - ist kein Schuljahr bekannt, so wird null gesetzt
	 */
	private constructor(name : string, ordinal : number, id : number, fachbereich : Number | null, idSchild : Number | null, bezeichnung : String, kuerzel : String, farbe : RGBFarbe | null, schulformen : List<Schulform>, sortierung : number, fuerZeugnis : boolean, gueltigVon : Number | null, gueltigBis : Number | null) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Fachgruppe.all_values_by_ordinal.push(this);
		Fachgruppe.all_values_by_name.set(name, this);
		for (let schulform of schulformen) 
			this.schulformenKuerzel.add(schulform.daten.kuerzel);
		this.id = id;
		this.fachbereich = fachbereich;
		this.idSchild = idSchild;
		this.bezeichnung = bezeichnung;
		this.kuerzel = kuerzel;
		this.farbe = (farbe === null) ? new RGBFarbe(255, 255, 255) : farbe;
		this.sortierung = sortierung;
		this.fuerZeugnis = fuerZeugnis;
		this.gueltigVon = null;
		this.gueltigBis = null;
	}

	/**
	 * Gibt eine Map von den IDs der Fachgruppen auf die zugehörigen Fachgruppen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs der Fachgruppen auf die zugehörigen Fachgruppen
	 */
	private static getMapByID() : HashMap<Number, Fachgruppe> {
		if (Fachgruppe._mapByID.size() === 0) 
			for (let g of Fachgruppe.values()) 
				Fachgruppe._mapByID.put(g.id, g);
		return Fachgruppe._mapByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Fachgruppen auf die zugehörigen Fachgruppen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Fachgruppen auf die zugehörigen Fachgruppen
	 */
	private static getMapByKuerzel() : HashMap<String, Fachgruppe> {
		if (Fachgruppe._mapByKuerzel.size() === 0) 
			for (let g of Fachgruppe.values()) 
				Fachgruppe._mapByKuerzel.put(g.kuerzel, g);
		return Fachgruppe._mapByKuerzel;
	}

	/**
	 * Liefert alle Schulformen zurück, bei welchen die Schulgliederung vorkommt.
	 * 
	 * @return eine Liste der Schulformen
	 */
	public getSchulformen() : List<Schulform> {
		if (this.schulformen === null) {
			this.schulformen = new Vector();
			for (let i : number = 0; i < this.schulformenKuerzel.size(); i++){
				let schulform : Schulform | null = Schulform.getByKuerzel(this.schulformenKuerzel.get(i));
				if (schulform !== null) 
					this.schulformen.add(schulform);
			}
		}
		return this.schulformen;
	}

	/**
	 * Prüft, ob die Schulform bei diesem Fach in irgendeiner Gliederung der 
	 * angegebenen Schulform zulässig ist.
	 * 
	 * @param schulform    die Schulform
	 * 
	 * @return true, falls das Fach in der Schulform zulässig ist, ansonsten false.
	 */
	private hasSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null)) 
			return false;
		if (this.schulformenKuerzel !== null) {
			for (let i : number = 0; i < this.schulformenKuerzel.size(); i++){
				let sfKuerzel : String | null = this.schulformenKuerzel.get(i);
				if (JavaObject.equalsTranspiler(sfKuerzel, (schulform.daten.kuerzel))) 
					return true;
			}
		}
		return false;
	}

	/**
	 * Liefert die Fachgruppe zu der übergebenen ID der Fachgruppe zurück.
	 * 
	 * @param id   die ID der Fachgruppe
	 * 
	 * @return die Fachgruppe oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : Fachgruppe | null {
		return Fachgruppe.getMapByID().get(id);
	}

	/**
	 * Liefert die Fachgruppe zu der übergebenen ID der Fachgruppe zurück.
	 * 
	 * @param kuerzel   das Kürzel der Fachgruppe
	 * 
	 * @return die Fachgruppe oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : Fachgruppe | null {
		return Fachgruppe.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Bestimmt alle Fachgruppen, die in irgendeiner Gliederung der angegebenen Schulform
	 * zulässig sind. 
	 *  
	 * @param schulform    die Schulform
	 * 
	 * @return die Fachgruppen in der angegebenen Schulform
	 */
	public static get(schulform : Schulform | null) : List<Fachgruppe> {
		let faecher : Vector<Fachgruppe> = new Vector();
		if (schulform === null) 
			return faecher;
		let fachgruppen : Array<Fachgruppe> = Fachgruppe.values();
		for (let i : number = 0; i < fachgruppen.length; i++){
			let fg : Fachgruppe | null = fachgruppen[i];
			if (fg.hasSchulform(schulform)) 
				faecher.add(fg);
		}
		return faecher;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : String {
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
	public toString() : String {
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
		if (!(other instanceof Fachgruppe))
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
	public compareTo(other : Fachgruppe) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Fachgruppe> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : Fachgruppe | null {
		let tmp : Fachgruppe | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.statkue.Fachgruppe'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_statkue_Fachgruppe(obj : unknown) : Fachgruppe {
	return obj as Fachgruppe;
}
