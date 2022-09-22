import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { Schulform, cast_de_nrw_schule_svws_core_types_statkue_Schulform } from '../../../core/types/statkue/Schulform';
import { AllgemeineMerkmaleKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_AllgemeineMerkmaleKatalogEintrag } from '../../../core/data/schule/AllgemeineMerkmaleKatalogEintrag';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';

export class AllgemeineMerkmale extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<AllgemeineMerkmale> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, AllgemeineMerkmale> = new Map<String, AllgemeineMerkmale>();

	public static readonly GANZTAG : AllgemeineMerkmale = new AllgemeineMerkmale("GANZTAG", 0, [new AllgemeineMerkmaleKatalogEintrag(100000, "GANZTAG", "Ganztagsschule", true, true, null, Arrays.asList(Schulform.H, Schulform.V, Schulform.R, Schulform.GY, Schulform.GE, Schulform.KS, Schulform.S, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G), null, null)]);

	public static readonly GANZTAG_OFFEN : AllgemeineMerkmale = new AllgemeineMerkmale("GANZTAG_OFFEN", 1, [new AllgemeineMerkmaleKatalogEintrag(200000, "OFFGANZ", "Offene Ganztagsschule", true, true, null, Arrays.asList(Schulform.KS, Schulform.S, Schulform.G, Schulform.V, Schulform.FW, Schulform.HI, Schulform.WF), null, null)]);

	public static readonly UEBERMITTAGSBETREUUNG : AllgemeineMerkmale = new AllgemeineMerkmale("UEBERMITTAGSBETREUUNG", 2, [new AllgemeineMerkmaleKatalogEintrag(250000, "ÜMI", "Übermittagsbetreuung", true, true, null, Arrays.asList(Schulform.G, Schulform.KS, Schulform.S), 2012, 2017), new AllgemeineMerkmaleKatalogEintrag(250001, "ÜMI", "Übermittagsbetreuung", true, true, null, Arrays.asList(Schulform.G, Schulform.KS, Schulform.S, Schulform.H, Schulform.R), 2018, null)]);

	public static readonly SELBSTAENDIGE_SCHULE : AllgemeineMerkmale = new AllgemeineMerkmale("SELBSTAENDIGE_SCHULE", 3, [new AllgemeineMerkmaleKatalogEintrag(300000, "SELBST", "Selbstständige Schule", true, false, null, Arrays.asList(Schulform.H, Schulform.R, Schulform.KS, Schulform.S, Schulform.GE, Schulform.GY, Schulform.WB, Schulform.BK, Schulform.SB, Schulform.G, Schulform.V), null, null)]);

	public static readonly VERBUNDSCHULE : AllgemeineMerkmale = new AllgemeineMerkmale("VERBUNDSCHULE", 4, [new AllgemeineMerkmaleKatalogEintrag(350000, "VERBUND", "Verbundschule", true, false, null, Arrays.asList(Schulform.GY, Schulform.GE, Schulform.R, Schulform.H, Schulform.G), null, null)]);

	public static readonly BUS : AllgemeineMerkmale = new AllgemeineMerkmale("BUS", 5, [new AllgemeineMerkmaleKatalogEintrag(400000, "BUS", "BUS-Projekt", true, true, null, Arrays.asList(Schulform.GE, Schulform.KS, Schulform.S, Schulform.H, Schulform.V), null, null)]);

	public static readonly VON_8_BIS_1 : AllgemeineMerkmale = new AllgemeineMerkmale("VON_8_BIS_1", 6, [new AllgemeineMerkmaleKatalogEintrag(500000, "8BIS1", "Schule von 8 bis 1", true, true, null, Arrays.asList(Schulform.KS, Schulform.S, Schulform.G), null, null)]);

	public static readonly VON_8_BIS_1_ANDERE : AllgemeineMerkmale = new AllgemeineMerkmale("VON_8_BIS_1_ANDERE", 7, [new AllgemeineMerkmaleKatalogEintrag(550000, "8BIS1AS", "Schule von 8 bis 1 an anderer Schule", true, true, null, Arrays.asList(Schulform.KS, Schulform.S, Schulform.G, Schulform.V), null, null)]);

	public static readonly DREIZEHN_PLUS : AllgemeineMerkmale = new AllgemeineMerkmale("DREIZEHN_PLUS", 8, [new AllgemeineMerkmaleKatalogEintrag(600000, "13+", "13+", true, true, null, Arrays.asList(Schulform.H, Schulform.KS, Schulform.S, Schulform.GY, Schulform.R, Schulform.G, Schulform.GE, Schulform.V), null, null)]);

	public static readonly DREIZEHN_PLUS_ANDERE : AllgemeineMerkmale = new AllgemeineMerkmale("DREIZEHN_PLUS_ANDERE", 9, [new AllgemeineMerkmaleKatalogEintrag(650000, "13+AS", "13+ an anderer Schule", true, true, null, Arrays.asList(Schulform.H, Schulform.KS, Schulform.S, Schulform.GY, Schulform.R, Schulform.G, Schulform.GE, Schulform.V), null, null)]);

	public static readonly JVA : AllgemeineMerkmale = new AllgemeineMerkmale("JVA", 10, [new AllgemeineMerkmaleKatalogEintrag(700000, "JVA", "JVA-Schüler", true, true, null, Arrays.asList(Schulform.BK), null, null)]);

	public static readonly REFORMPAEDAGOGIK : AllgemeineMerkmale = new AllgemeineMerkmale("REFORMPAEDAGOGIK", 11, [new AllgemeineMerkmaleKatalogEintrag(800000, "RefPäd", "Reformpädagogik", true, false, null, Arrays.asList(Schulform.KS, Schulform.S, Schulform.H, Schulform.R, Schulform.GE, Schulform.GY, Schulform.G, Schulform.V), null, null)]);

	public static readonly BEGEGNUNG_MIT_SPRACHEN_F : AllgemeineMerkmale = new AllgemeineMerkmale("BEGEGNUNG_MIT_SPRACHEN_F", 12, [new AllgemeineMerkmaleKatalogEintrag(1410000, "BegSprF", "Begegnung mit Sprachen -Französisch-", true, true, "F", Arrays.asList(Schulform.KS, Schulform.S, Schulform.G), null, null)]);

	public static readonly BEGEGNUNG_MIT_SPRACHEN_I : AllgemeineMerkmale = new AllgemeineMerkmale("BEGEGNUNG_MIT_SPRACHEN_I", 13, [new AllgemeineMerkmaleKatalogEintrag(1420000, "BegSprI", "Begegnung mit Sprachen -Italienisch-", true, true, "I", Arrays.asList(Schulform.KS, Schulform.S, Schulform.G, Schulform.V), null, null)]);

	public static readonly BEGEGNUNG_MIT_SPRACHEN_T : AllgemeineMerkmale = new AllgemeineMerkmale("BEGEGNUNG_MIT_SPRACHEN_T", 14, [new AllgemeineMerkmaleKatalogEintrag(1430000, "BegSprT", "Begegnung mit Sprachen -Türkisch-", true, true, "T", Arrays.asList(Schulform.KS, Schulform.S, Schulform.G, Schulform.V), null, null)]);

	public static readonly BEGEGNUNG_MIT_SPRACHEN_N : AllgemeineMerkmale = new AllgemeineMerkmale("BEGEGNUNG_MIT_SPRACHEN_N", 15, [new AllgemeineMerkmaleKatalogEintrag(1440000, "BegSprN", "Begegnung mit Sprachen -Niederländisch-", true, true, "N", Arrays.asList(Schulform.KS, Schulform.S, Schulform.G, Schulform.V), null, null)]);

	public static readonly BEGEGNUNG_MIT_SPRACHEN_SONSTIGE : AllgemeineMerkmale = new AllgemeineMerkmale("BEGEGNUNG_MIT_SPRACHEN_SONSTIGE", 16, [new AllgemeineMerkmaleKatalogEintrag(1450000, "BegSprSon", "Begegnung mit Sprachen -Sonstige Fremdsprache-", true, true, "X", Arrays.asList(Schulform.KS, Schulform.S, Schulform.G), null, null)]);

	public static readonly ARBEITSSPRACHE_GOST_E : AllgemeineMerkmale = new AllgemeineMerkmale("ARBEITSSPRACHE_GOST_E", 17, [new AllgemeineMerkmaleKatalogEintrag(1700000, "ArbSprE", "Arbeitssprache in der gym. Oberstufe -Englisch-", true, true, null, Arrays.asList(Schulform.GE, Schulform.GY), null, null)]);

	public static readonly ARBEITSSPRACHE_GOST_F : AllgemeineMerkmale = new AllgemeineMerkmale("ARBEITSSPRACHE_GOST_F", 18, [new AllgemeineMerkmaleKatalogEintrag(1710000, "ArbSprF", "Arbeitssprache in der gym. Oberstufe -Französisch-", true, true, null, Arrays.asList(Schulform.GE, Schulform.GY), null, null)]);

	public static readonly ARBEITSSPRACHE_GOST_SONSTIGE : AllgemeineMerkmale = new AllgemeineMerkmale("ARBEITSSPRACHE_GOST_SONSTIGE", 19, [new AllgemeineMerkmaleKatalogEintrag(1720000, "ArbSprSon", "Arbeitssprache in der gym. Oberstufe -Sonstige Sprache-", true, true, null, Arrays.asList(Schulform.GE, Schulform.GY, Schulform.V), null, null)]);

	public static readonly INTERNAT : AllgemeineMerkmale = new AllgemeineMerkmale("INTERNAT", 20, [new AllgemeineMerkmaleKatalogEintrag(2000000, "INTERNAT", "Internat", true, true, null, Arrays.asList(Schulform.H, Schulform.V, Schulform.R, Schulform.GY, Schulform.GE, Schulform.KS, Schulform.S, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.BK, Schulform.SB, Schulform.SR, Schulform.SG), null, null)]);

	public static VERSION : number = 1;

	public readonly daten : AllgemeineMerkmaleKatalogEintrag;

	public readonly historie : Array<AllgemeineMerkmaleKatalogEintrag>;

	private static readonly _mapByKuerzel : HashMap<String, AllgemeineMerkmale> = new HashMap();

	/**
	 * Erzeugt ein neues allgemeines Merkmal in der Aufzählung.
	 * 
	 * @param historie   die Historie der allgemeinen Merkmale, welche ein Array von 
	 *                   {@link AllgemeineMerkmaleKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<AllgemeineMerkmaleKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		AllgemeineMerkmale.all_values_by_ordinal.push(this);
		AllgemeineMerkmale.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der allgemeinen Merkmale auf die 
	 * zugehörigen allgemeinen Merkmale zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen allgemeinen Merkmale
	 */
	private static getMapByKuerzel() : HashMap<String, AllgemeineMerkmale> {
		if (AllgemeineMerkmale._mapByKuerzel.size() === 0) {
			for (let s of AllgemeineMerkmale.values()) {
				if (s.daten !== null) 
					AllgemeineMerkmale._mapByKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return AllgemeineMerkmale._mapByKuerzel;
	}

	/**
	 * Gibt das allgemeine Merkmal für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel des allgemeinen Merkmals
	 * 
	 * @return das allgemeine Merkmal oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : AllgemeineMerkmale | null {
		return AllgemeineMerkmale.getMapByKuerzel().get(kuerzel);
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
		if (!(other instanceof AllgemeineMerkmale))
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
	public compareTo(other : AllgemeineMerkmale) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<AllgemeineMerkmale> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : AllgemeineMerkmale | null {
		let tmp : AllgemeineMerkmale | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.statkue.AllgemeineMerkmale'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_statkue_AllgemeineMerkmale(obj : unknown) : AllgemeineMerkmale {
	return obj as AllgemeineMerkmale;
}
