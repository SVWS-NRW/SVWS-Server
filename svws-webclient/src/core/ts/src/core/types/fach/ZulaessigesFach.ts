import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Fachgruppe, cast_de_nrw_schule_svws_core_types_fach_Fachgruppe } from '../../../core/types/fach/Fachgruppe';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { RGBFarbe, cast_de_nrw_schule_svws_core_data_RGBFarbe } from '../../../core/data/RGBFarbe';
import { Jahrgaenge, cast_de_nrw_schule_svws_core_types_jahrgang_Jahrgaenge } from '../../../core/types/jahrgang/Jahrgaenge';
import { SchulformSchulgliederung, cast_de_nrw_schule_svws_core_data_schule_SchulformSchulgliederung } from '../../../core/data/schule/SchulformSchulgliederung';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { Schulgliederung, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { FachKatalogEintrag, cast_de_nrw_schule_svws_core_data_fach_FachKatalogEintrag } from '../../../core/data/fach/FachKatalogEintrag';
import { Pair, cast_de_nrw_schule_svws_core_adt_Pair } from '../../../core/adt/Pair';

export class ZulaessigesFach extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<ZulaessigesFach> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, ZulaessigesFach> = new Map<string, ZulaessigesFach>();

	/**
	 * Fach Arbeits- und Betriebswirtschaftslehre 
	 */
	public static readonly AB : ZulaessigesFach = new ZulaessigesFach("AB", 0, [new FachKatalogEintrag(1000000, "AB", "Arbeits- und Betriebswirtschaftslehre", "AB", null, Fachgruppe.FG_AL, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Neigungs- und Projektgruppen 
	 */
	public static readonly AG : ZulaessigesFach = new ZulaessigesFach("AG", 1, [new FachKatalogEintrag(2000000, "AG", "Neigungs- und Projektgruppen", "AG", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Arbeitslehre - Schwerpunkt Hauswirtschaft 
	 */
	public static readonly AH : ZulaessigesFach = new ZulaessigesFach("AH", 2, [new FachKatalogEintrag(3000000, "AH", "Arbeitslehre - Schwerpunkt Hauswirtschaft", "AH", null, Fachgruppe.FG_AL, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Arbeitslehre 
	 */
	public static readonly AL : ZulaessigesFach = new ZulaessigesFach("AL", 3, [new FachKatalogEintrag(4000000, "AL", "Arbeitslehre - Integration Hauswirtschaft, Technik, Wirtschaftslehre", "AL", null, Fachgruppe.FG_AL, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Arabisch 
	 */
	public static readonly AM : ZulaessigesFach = new ZulaessigesFach("AM", 4, [new FachKatalogEintrag(5000000, "AM", "Unterricht in der Herkunftssprache - Arabisch", "AM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Alevitische Religionslehre nach den Grundsätzen des AABF 
	 */
	public static readonly AR : ZulaessigesFach = new ZulaessigesFach("AR", 5, [new FachKatalogEintrag(6000000, "AR", "Alevitische Religionslehre nach den Grundsätzen des AABF", "AR", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Arbeitslehre - Schwerpunkt Technik 
	 */
	public static readonly AT : ZulaessigesFach = new ZulaessigesFach("AT", 6, [new FachKatalogEintrag(7000000, "AT", "Arbeitslehre - Schwerpunkt Technik", "AT", null, Fachgruppe.FG_AL, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Arbeitsvorbereitung 
	 */
	public static readonly AV : ZulaessigesFach = new ZulaessigesFach("AV", 7, [new FachKatalogEintrag(8000000, "AV", "Arbeitsvorbereitung", "AV", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Arbeitslehre - Schwerpunkt Wirtschaft 
	 */
	public static readonly AW : ZulaessigesFach = new ZulaessigesFach("AW", 8, [new FachKatalogEintrag(9000000, "AW", "Arbeitslehre - Schwerpunkt Wirtschaft", "AW", null, Fachgruppe.FG_AL, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Arbeitslehre - Technik/Wirtschaft (nur Wahlpflichtunterricht) 
	 */
	public static readonly AX : ZulaessigesFach = new ZulaessigesFach("AX", 9, [new FachKatalogEintrag(10000000, "AX", "Arbeitslehre - Technik/Wirtschaft (nur Wahlpflichtunterricht)", "AX", null, Fachgruppe.FG_AL, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Arbeitslehre - Hauswirtschaft/Wirtschaft (nur Wahlpflichtunterricht) 
	 */
	public static readonly AY : ZulaessigesFach = new ZulaessigesFach("AY", 10, [new FachKatalogEintrag(11000000, "AY", "Arbeitslehre - Hauswirtschaft/Wirtschaft (nur Wahlpflichtunterricht)", "AY", null, Fachgruppe.FG_AL, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Berufs- und Arbeitspädagogik 
	 */
	public static readonly BA : ZulaessigesFach = new ZulaessigesFach("BA", 11, [new FachKatalogEintrag(12000000, "BA", "Berufs- und Arbeitspädagogik", "BA", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Bürowirtschaft 
	 */
	public static readonly BF : ZulaessigesFach = new ZulaessigesFach("BF", 12, [new FachKatalogEintrag(13000000, "BF", "Bürowirtschaft", "BF", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Betrieb und Gesellschaft/Politik 
	 */
	public static readonly BG : ZulaessigesFach = new ZulaessigesFach("BG", 13, [new FachKatalogEintrag(14000000, "BG", "Betrieb und Gesellschaft/Politik", "BG", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bosnisch 
	 */
	public static readonly BH : ZulaessigesFach = new ZulaessigesFach("BH", 14, [new FachKatalogEintrag(15000000, "BH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Bosnisch", "BH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Biologie 
	 */
	public static readonly BI : ZulaessigesFach = new ZulaessigesFach("BI", 15, [new FachKatalogEintrag(16000000, "BI", "Biologie", "BI", 3, Fachgruppe.FG_NW, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Betriebsinformatik 
	 */
	public static readonly BK : ZulaessigesFach = new ZulaessigesFach("BK", 16, [new FachKatalogEintrag(17000000, "BK", "Betriebsinformatik", "BK", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Bosnisch 
	 */
	public static readonly BM : ZulaessigesFach = new ZulaessigesFach("BM", 17, [new FachKatalogEintrag(18000000, "BM", "Unterricht in der Herkunftssprache - Bosnisch", "BM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Braille'sche Punktschrift 
	 */
	public static readonly BN : ZulaessigesFach = new ZulaessigesFach("BN", 18, [new FachKatalogEintrag(19000000, "BN", "Braille\'sche Punktschrift", "BN", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Betriebspraxis 
	 */
	public static readonly BP : ZulaessigesFach = new ZulaessigesFach("BP", 19, [new FachKatalogEintrag(20000000, "BP", "Betriebspraxis", "BP", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Betriebslehre 
	 */
	public static readonly BR : ZulaessigesFach = new ZulaessigesFach("BR", 20, [new FachKatalogEintrag(21000000, "BR", "Betriebslehre", "BR", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Betriebssoziologie / Arbeitsrecht 
	 */
	public static readonly BS : ZulaessigesFach = new ZulaessigesFach("BS", 21, [new FachKatalogEintrag(22000000, "BS", "Betriebssoziologie / Arbeitsrecht", "BS", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Berufsvorbereitung 
	 */
	public static readonly BV : ZulaessigesFach = new ZulaessigesFach("BV", 22, [new FachKatalogEintrag(23000000, "BV", "Berufsvorbereitung", "BV", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Betriebswirtschaftslehre 
	 */
	public static readonly BW : ZulaessigesFach = new ZulaessigesFach("BW", 23, [new FachKatalogEintrag(24000000, "BW", "Betriebswirtschaftslehre", "BW", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Betriebssysteme / Netzwerke 
	 */
	public static readonly BY : ZulaessigesFach = new ZulaessigesFach("BY", 24, [new FachKatalogEintrag(25000000, "BY", "Betriebssysteme / Netzwerke", "BY", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Chinesisch 
	 */
	public static readonly C : ZulaessigesFach = new ZulaessigesFach("C", 25, [new FachKatalogEintrag(26000000, "C", "Chinesisch", "C", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Chinesisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly C0 : ZulaessigesFach = new ZulaessigesFach("C0", 26, [new FachKatalogEintrag(27000000, "C0", "Chinesisch, regulärer Beginn in der Einführungsphase", "C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly C1 : ZulaessigesFach = new ZulaessigesFach("C1", 27, [new FachKatalogEintrag(28000000, "C1", "Chinesisch, regulärer Beginn in Jahrgang 11", "C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly C5 : ZulaessigesFach = new ZulaessigesFach("C5", 28, [new FachKatalogEintrag(29000000, "C5", "Chinesisch, regulärer Beginn in Jahrgang 5", "C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly C6 : ZulaessigesFach = new ZulaessigesFach("C6", 29, [new FachKatalogEintrag(30000000, "C6", "Chinesisch, regulärer Beginn in Jahrgang 6", "C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly C7 : ZulaessigesFach = new ZulaessigesFach("C7", 30, [new FachKatalogEintrag(31000000, "C7", "Chinesisch, regulärer Beginn in Jahrgang 7", "C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly C8 : ZulaessigesFach = new ZulaessigesFach("C8", 31, [new FachKatalogEintrag(32000000, "C8", "Chinesisch, regulärer Beginn in Jahrgang 8", "C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly C9 : ZulaessigesFach = new ZulaessigesFach("C9", 32, [new FachKatalogEintrag(33000000, "C9", "Chinesisch, regulärer Beginn in Jahrgang 9", "C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Chemie 
	 */
	public static readonly CH : ZulaessigesFach = new ZulaessigesFach("CH", 33, [new FachKatalogEintrag(34000000, "CH", "Chemie", "CH", 3, Fachgruppe.FG_NW, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Kroatisch 
	 */
	public static readonly CM : ZulaessigesFach = new ZulaessigesFach("CM", 34, [new FachKatalogEintrag(35000000, "CM", "Unterricht in der Herkunftssprache - Kroatisch", "CM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Chinesisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly CQ : ZulaessigesFach = new ZulaessigesFach("CQ", 35, [new FachKatalogEintrag(36000000, "CQ", "Chinesisch, außerhalb des regulären Fachunterrichts", "CQ", null, null, null, true, false, true, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Deutsch 
	 */
	public static readonly D : ZulaessigesFach = new ZulaessigesFach("D", 36, [new FachKatalogEintrag(37000000, "D", "Deutsch", "D", 1, Fachgruppe.FG_D, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Datenbanken 
	 */
	public static readonly DB : ZulaessigesFach = new ZulaessigesFach("DB", 37, [new FachKatalogEintrag(38000000, "DB", "Datenbanken", "DB", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Rumänisch 
	 */
	public static readonly DH : ZulaessigesFach = new ZulaessigesFach("DH", 38, [new FachKatalogEintrag(39000000, "DH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Rumänisch", "DH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Didaktik u. Methodik der soz.päd. Praxis mit Übungen 
	 */
	public static readonly DM : ZulaessigesFach = new ZulaessigesFach("DM", 39, [new FachKatalogEintrag(40000000, "DM", "Didaktik u. Methodik der soz.päd. Praxis mit Übungen", "DM", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Darstellen und Gestalten 
	 */
	public static readonly DS : ZulaessigesFach = new ZulaessigesFach("DS", 40, [new FachKatalogEintrag(41000000, "DS", "Darstellen und Gestalten", "DS", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Datenverarbeitung 
	 */
	public static readonly DV : ZulaessigesFach = new ZulaessigesFach("DV", 41, [new FachKatalogEintrag(42000000, "DV", "Datenverarbeitung", "DV", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Englisch 
	 */
	public static readonly E : ZulaessigesFach = new ZulaessigesFach("E", 42, [new FachKatalogEintrag(43000000, "E", "Englisch", "E", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.FW, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Erweitertes Bildungsangebot 
	 */
	public static readonly EB : ZulaessigesFach = new ZulaessigesFach("EB", 43, [new FachKatalogEintrag(44000000, "EB", "Erweitertes Bildungsangebot", "EB", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Serbisch 
	 */
	public static readonly EH : ZulaessigesFach = new ZulaessigesFach("EH", 44, [new FachKatalogEintrag(45000000, "EH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Serbisch", "EH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Erdkunde/ Geographie 
	 */
	public static readonly EK : ZulaessigesFach = new ZulaessigesFach("EK", 45, [new FachKatalogEintrag(46000000, "EK", "Erdkunde/ Geographie", "EK", 2, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Ernährungslehre 
	 */
	public static readonly EL : ZulaessigesFach = new ZulaessigesFach("EL", 46, [new FachKatalogEintrag(47000000, "EL", "Ernährungslehre", "EL", null, Fachgruppe.FG_WN, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, Schulgliederung.GY), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Serbisch 
	 */
	public static readonly EM : ZulaessigesFach = new ZulaessigesFach("EM", 47, [new FachKatalogEintrag(48000000, "EM", "Unterricht in der Herkunftssprache - Serbisch", "EM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Evangelische Religionslehre (konfessionell kooperativ) 
	 */
	public static readonly EN : ZulaessigesFach = new ZulaessigesFach("EN", 48, [new FachKatalogEintrag(49000000, "EN", "Evangelische Religionslehre (konfessionell kooperativ)", "ER", null, null, null, false, false, false, false, true, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Evangelische Religionslehre 
	 */
	public static readonly ER : ZulaessigesFach = new ZulaessigesFach("ER", 49, [new FachKatalogEintrag(50000000, "ER", "Evangelische Religionslehre", "ER", null, Fachgruppe.FG_RE, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Französisch 
	 */
	public static readonly F : ZulaessigesFach = new ZulaessigesFach("F", 50, [new FachKatalogEintrag(51000000, "F", "Französisch", "F", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.FW, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Französisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly F0 : ZulaessigesFach = new ZulaessigesFach("F0", 51, [new FachKatalogEintrag(52000000, "F0", "Französisch, regulärer Beginn in der Einführungsphase", "F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly F1 : ZulaessigesFach = new ZulaessigesFach("F1", 52, [new FachKatalogEintrag(53000000, "F1", "Französisch, regulärer Beginn in Jahrgang 11", "F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly F5 : ZulaessigesFach = new ZulaessigesFach("F5", 53, [new FachKatalogEintrag(54000000, "F5", "Französisch, regulärer Beginn in Jahrgang 5", "F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly F6 : ZulaessigesFach = new ZulaessigesFach("F6", 54, [new FachKatalogEintrag(55000000, "F6", "Französisch, regulärer Beginn in Jahrgang 6", "F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly F7 : ZulaessigesFach = new ZulaessigesFach("F7", 55, [new FachKatalogEintrag(56000000, "F7", "Französisch, regulärer Beginn in Jahrgang 7", "F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly F8 : ZulaessigesFach = new ZulaessigesFach("F8", 56, [new FachKatalogEintrag(57000000, "F8", "Französisch, regulärer Beginn in Jahrgang 8", "F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly F9 : ZulaessigesFach = new ZulaessigesFach("F9", 57, [new FachKatalogEintrag(58000000, "F9", "Französisch, regulärer Beginn in Jahrgang 9", "F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Fächer des berufsbezogenen Bereichs 
	 */
	public static readonly FB : ZulaessigesFach = new ZulaessigesFach("FB", 58, [new FachKatalogEintrag(59000000, "FB", "Fächer des berufsbezogenen Bereichs", "FB", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Fremdsprachliche Kommunikation 
	 */
	public static readonly FK : ZulaessigesFach = new ZulaessigesFach("FK", 59, [new FachKatalogEintrag(60000000, "FK", "Fremdsprachliche Kommunikation", "FK", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Fachpraxis 
	 */
	public static readonly FP : ZulaessigesFach = new ZulaessigesFach("FP", 60, [new FachKatalogEintrag(61000000, "FP", "Fachpraxis", "FP", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Französisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly FQ : ZulaessigesFach = new ZulaessigesFach("FQ", 61, [new FachKatalogEintrag(62000000, "FQ", "Französisch, außerhalb des regulären Fachunterrichts", "FQ", null, null, null, true, false, true, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Förderunterricht 
	 */
	public static readonly FU : ZulaessigesFach = new ZulaessigesFach("FU", 62, [new FachKatalogEintrag(63000000, "FU", "Förderunterricht", "FU", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Griechisch 
	 */
	public static readonly G : ZulaessigesFach = new ZulaessigesFach("G", 63, [new FachKatalogEintrag(64000000, "G", "Griechisch", "G", null, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.FW, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Griechisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly G0 : ZulaessigesFach = new ZulaessigesFach("G0", 64, [new FachKatalogEintrag(65000000, "G0", "Griechisch, regulärer Beginn in der Einführungsphase", "G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly G1 : ZulaessigesFach = new ZulaessigesFach("G1", 65, [new FachKatalogEintrag(66000000, "G1", "Griechisch, regulärer Beginn in Jahrgang 11", "G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly G5 : ZulaessigesFach = new ZulaessigesFach("G5", 66, [new FachKatalogEintrag(67000000, "G5", "Griechisch, regulärer Beginn in Jahrgang 5", "G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly G6 : ZulaessigesFach = new ZulaessigesFach("G6", 67, [new FachKatalogEintrag(68000000, "G6", "Griechisch, regulärer Beginn in Jahrgang 6", "G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly G7 : ZulaessigesFach = new ZulaessigesFach("G7", 68, [new FachKatalogEintrag(69000000, "G7", "Griechisch, regulärer Beginn in Jahrgang 7", "G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly G8 : ZulaessigesFach = new ZulaessigesFach("G8", 69, [new FachKatalogEintrag(70000000, "G8", "Griechisch, regulärer Beginn in Jahrgang 8", "G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly G9 : ZulaessigesFach = new ZulaessigesFach("G9", 70, [new FachKatalogEintrag(71000000, "G9", "Griechisch, regulärer Beginn in Jahrgang 9", "G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Geräte- und Maschinenlehre 
	 */
	public static readonly GA : ZulaessigesFach = new ZulaessigesFach("GA", 71, [new FachKatalogEintrag(72000000, "GA", "Geräte- und Maschinenlehre", "GA", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Grundbildung 
	 */
	public static readonly GB : ZulaessigesFach = new ZulaessigesFach("GB", 72, [new FachKatalogEintrag(73000000, "GB", "Grundbildung", "GB", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Geschichte 
	 */
	public static readonly GE : ZulaessigesFach = new ZulaessigesFach("GE", 73, [new FachKatalogEintrag(74000000, "GE", "Geschichte", "GE", 2, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Neugriechisch 
	 */
	public static readonly GH : ZulaessigesFach = new ZulaessigesFach("GH", 74, [new FachKatalogEintrag(75000000, "GH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Neugriechisch", "GH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Gesellschaftslehre 
	 */
	public static readonly GL : ZulaessigesFach = new ZulaessigesFach("GL", 75, [new FachKatalogEintrag(76000000, "GL", "Gesellschaftslehre", "GL", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Neugriechisch 
	 */
	public static readonly GM : ZulaessigesFach = new ZulaessigesFach("GM", 76, [new FachKatalogEintrag(77000000, "GM", "Unterricht in der Herkunftssprache - Neugriechisch", "GM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Geologie (Oberstufenkolleg Bielefeld) 
	 */
	public static readonly GO : ZulaessigesFach = new ZulaessigesFach("GO", 77, [new FachKatalogEintrag(78000000, "GO", "Geologie (Oberstufenkolleg Bielefeld)", "GO", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Geschichte / Politik 
	 */
	public static readonly GP : ZulaessigesFach = new ZulaessigesFach("GP", 78, [new FachKatalogEintrag(79000000, "GP", "Geschichte / Politik", "GP", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Altgriechisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly GQ : ZulaessigesFach = new ZulaessigesFach("GQ", 79, [new FachKatalogEintrag(80000000, "GQ", "Altgriechisch, außerhalb des regulären Fachunterrichts", "GQ", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Gestaltungslehre 
	 */
	public static readonly GS : ZulaessigesFach = new ZulaessigesFach("GS", 80, [new FachKatalogEintrag(81000000, "GS", "Gestaltungslehre", "GS", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Gesamtunterricht (nur für Förderschulkindergarten) 
	 */
	public static readonly GU : ZulaessigesFach = new ZulaessigesFach("GU", 81, [new FachKatalogEintrag(82000000, "GU", "Gesamtunterricht (nur für Förderschulkindergarten)", "GU", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Geschichte und Sozialwissenschaft 
	 */
	public static readonly GW : ZulaessigesFach = new ZulaessigesFach("GW", 82, [new FachKatalogEintrag(83000000, "GW", "Geschichte und Sozialwissenschaft", "GW", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hebräisch 
	 */
	public static readonly H : ZulaessigesFach = new ZulaessigesFach("H", 83, [new FachKatalogEintrag(84000000, "H", "Hebräisch", "H", null, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hebräisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly H0 : ZulaessigesFach = new ZulaessigesFach("H0", 84, [new FachKatalogEintrag(85000000, "H0", "Hebräisch, regulärer Beginn in der Einführungsphase", "H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly H1 : ZulaessigesFach = new ZulaessigesFach("H1", 85, [new FachKatalogEintrag(86000000, "H1", "Hebräisch, regulärer Beginn in Jahrgang 11", "H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly H5 : ZulaessigesFach = new ZulaessigesFach("H5", 86, [new FachKatalogEintrag(87000000, "H5", "Hebräisch, regulärer Beginn in Jahrgang 5", "H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly H6 : ZulaessigesFach = new ZulaessigesFach("H6", 87, [new FachKatalogEintrag(88000000, "H6", "Hebräisch, regulärer Beginn in Jahrgang 6", "H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly H7 : ZulaessigesFach = new ZulaessigesFach("H7", 88, [new FachKatalogEintrag(89000000, "H7", "Hebräisch, regulärer Beginn in Jahrgang 7", "H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly H8 : ZulaessigesFach = new ZulaessigesFach("H8", 89, [new FachKatalogEintrag(90000000, "H8", "Hebräisch, regulärer Beginn in Jahrgang 8", "H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly H9 : ZulaessigesFach = new ZulaessigesFach("H9", 90, [new FachKatalogEintrag(91000000, "H9", "Hebräisch, regulärer Beginn in Jahrgang 9", "H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kroatisch 
	 */
	public static readonly HH : ZulaessigesFach = new ZulaessigesFach("HH", 91, [new FachKatalogEintrag(92000000, "HH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Kroatisch", "HH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hebräisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly HQ : ZulaessigesFach = new ZulaessigesFach("HQ", 92, [new FachKatalogEintrag(93000000, "HQ", "Hebräisch, außerhalb des regulären Fachunterrichts", "HQ", null, null, null, true, false, true, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Jüdische Religionslehre 
	 */
	public static readonly HR : ZulaessigesFach = new ZulaessigesFach("HR", 93, [new FachKatalogEintrag(94000000, "HR", "Jüdische Religionslehre", "HR", null, Fachgruppe.FG_RE, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hausunterricht 
	 */
	public static readonly HU : ZulaessigesFach = new ZulaessigesFach("HU", 94, [new FachKatalogEintrag(95000000, "HU", "Hausunterricht", "HU", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hauswirtschaft 
	 */
	public static readonly HW : ZulaessigesFach = new ZulaessigesFach("HW", 95, [new FachKatalogEintrag(96000000, "HW", "Hauswirtschaft", "HW", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Hygiene 
	 */
	public static readonly HY : ZulaessigesFach = new ZulaessigesFach("HY", 96, [new FachKatalogEintrag(97000000, "HY", "Hygiene", "HY", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Italienisch 
	 */
	public static readonly I : ZulaessigesFach = new ZulaessigesFach("I", 97, [new FachKatalogEintrag(98000000, "I", "Italienisch", "I", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.FW, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Italienisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly I0 : ZulaessigesFach = new ZulaessigesFach("I0", 98, [new FachKatalogEintrag(99000000, "I0", "Italienisch, regulärer Beginn in der Einführungsphase", "I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly I1 : ZulaessigesFach = new ZulaessigesFach("I1", 99, [new FachKatalogEintrag(100000000, "I1", "Italienisch, regulärer Beginn in Jahrgang 11", "I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly I5 : ZulaessigesFach = new ZulaessigesFach("I5", 100, [new FachKatalogEintrag(101000000, "I5", "Italienisch, regulärer Beginn in Jahrgang 5", "I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly I6 : ZulaessigesFach = new ZulaessigesFach("I6", 101, [new FachKatalogEintrag(102000000, "I6", "Italienisch, regulärer Beginn in Jahrgang 6", "I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly I7 : ZulaessigesFach = new ZulaessigesFach("I7", 102, [new FachKatalogEintrag(103000000, "I7", "Italienisch, regulärer Beginn in Jahrgang 7", "I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly I8 : ZulaessigesFach = new ZulaessigesFach("I8", 103, [new FachKatalogEintrag(104000000, "I8", "Italienisch, regulärer Beginn in Jahrgang 8", "I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly I9 : ZulaessigesFach = new ZulaessigesFach("I9", 104, [new FachKatalogEintrag(105000000, "I9", "Italienisch, regulärer Beginn in Jahrgang 9", "I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Individuelles Lernen (Ergänzungsstunden, Ganztag- und Betreuungsangebote) 
	 */
	public static readonly IE : ZulaessigesFach = new ZulaessigesFach("IE", 105, [new FachKatalogEintrag(106000000, "IE", "Individuelles Lernen (Ergänzungsstunden, Ganztag- und Betreuungsangebote)", "IE", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Informatik 
	 */
	public static readonly IF : ZulaessigesFach = new ZulaessigesFach("IF", 106, [new FachKatalogEintrag(107000000, "IF", "Informatik", "IF", 3, Fachgruppe.FG_WN, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Italienisch 
	 */
	public static readonly IH : ZulaessigesFach = new ZulaessigesFach("IH", 107, [new FachKatalogEintrag(108000000, "IH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Italienisch", "IH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Individuelles Lernen (dem Kernstundenkontingent entnommen) 
	 */
	public static readonly IK : ZulaessigesFach = new ZulaessigesFach("IK", 108, [new FachKatalogEintrag(109000000, "IK", "Individuelles Lernen (dem Kernstundenkontingent entnommen)", "IK", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Islamische Religionslehre 
	 */
	public static readonly IL : ZulaessigesFach = new ZulaessigesFach("IL", 109, [new FachKatalogEintrag(110000000, "IL", "Islamische Religionslehre", "IL", null, Fachgruppe.FG_RE, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Italienisch 
	 */
	public static readonly IM : ZulaessigesFach = new ZulaessigesFach("IM", 110, [new FachKatalogEintrag(111000000, "IM", "Unterricht in der Herkunftssprache - Italienisch", "IM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Instrumentalpraktischer Grundkurs 
	 */
	public static readonly IN : ZulaessigesFach = new ZulaessigesFach("IN", 111, [new FachKatalogEintrag(112000000, "IN", "Instrumentalpraktischer Grundkurs", "IN", null, Fachgruppe.FG_ME, null, false, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Italienisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly IQ : ZulaessigesFach = new ZulaessigesFach("IQ", 112, [new FachKatalogEintrag(113000000, "IQ", "Italienisch, außerhalb des regulären Fachunterrichts", "I", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Niederländisch 
	 */
	public static readonly JH : ZulaessigesFach = new ZulaessigesFach("JH", 113, [new FachKatalogEintrag(114000000, "JH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Niederländisch", "N", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Japanisch 
	 */
	public static readonly K : ZulaessigesFach = new ZulaessigesFach("K", 114, [new FachKatalogEintrag(115000000, "K", "Japanisch", "K", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Japanisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly K0 : ZulaessigesFach = new ZulaessigesFach("K0", 115, [new FachKatalogEintrag(116000000, "K0", "Japanisch, regulärer Beginn in der Einführungsphase", "K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly K1 : ZulaessigesFach = new ZulaessigesFach("K1", 116, [new FachKatalogEintrag(117000000, "K1", "Japanisch, regulärer Beginn in Jahrgang 11", "K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly K5 : ZulaessigesFach = new ZulaessigesFach("K5", 117, [new FachKatalogEintrag(118000000, "K5", "Japanisch, regulärer Beginn in Jahrgang 5", "K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly K6 : ZulaessigesFach = new ZulaessigesFach("K6", 118, [new FachKatalogEintrag(119000000, "K6", "Japanisch, regulärer Beginn in Jahrgang 6", "K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly K7 : ZulaessigesFach = new ZulaessigesFach("K7", 119, [new FachKatalogEintrag(120000000, "K7", "Japanisch, regulärer Beginn in Jahrgang 7", "K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly K8 : ZulaessigesFach = new ZulaessigesFach("K8", 120, [new FachKatalogEintrag(121000000, "K8", "Japanisch, regulärer Beginn in Jahrgang 8", "K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly K9 : ZulaessigesFach = new ZulaessigesFach("K9", 121, [new FachKatalogEintrag(122000000, "K9", "Japanisch, regulärer Beginn in Jahrgang 9", "K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Katholische Religionslehre (konfessionell kooperativ) 
	 */
	public static readonly KN : ZulaessigesFach = new ZulaessigesFach("KN", 122, [new FachKatalogEintrag(123000000, "KN", "Katholische Religionslehre (konfessionell kooperativ)", "KR", null, null, null, false, false, false, false, true, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Kommunikation 
	 */
	public static readonly KO : ZulaessigesFach = new ZulaessigesFach("KO", 123, [new FachKatalogEintrag(124000000, "KO", "Kommunikation", "KO", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Japanisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly KQ : ZulaessigesFach = new ZulaessigesFach("KQ", 124, [new FachKatalogEintrag(125000000, "KQ", "Japanisch, außerhalb des regulären Fachunterrichts", "KQ", null, null, null, true, false, true, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Katholische Religionslehre 
	 */
	public static readonly KR : ZulaessigesFach = new ZulaessigesFach("KR", 125, [new FachKatalogEintrag(126000000, "KR", "Katholische Religionslehre", "KR", null, Fachgruppe.FG_RE, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Kurzschrift 
	 */
	public static readonly KS : ZulaessigesFach = new ZulaessigesFach("KS", 126, [new FachKatalogEintrag(127000000, "KS", "Kurzschrift", "KS", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Kunst 
	 */
	public static readonly KU : ZulaessigesFach = new ZulaessigesFach("KU", 127, [new FachKatalogEintrag(128000000, "KU", "Kunst", "KU", 1, Fachgruppe.FG_MS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Lateinisch 
	 */
	public static readonly L : ZulaessigesFach = new ZulaessigesFach("L", 128, [new FachKatalogEintrag(129000000, "L", "Lateinisch", "L", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.FW, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Lateinisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly L0 : ZulaessigesFach = new ZulaessigesFach("L0", 129, [new FachKatalogEintrag(130000000, "L0", "Lateinisch, regulärer Beginn in der Einführungsphase", "L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly L1 : ZulaessigesFach = new ZulaessigesFach("L1", 130, [new FachKatalogEintrag(131000000, "L1", "Lateinisch, regulärer Beginn in Jahrgang 11", "L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly L5 : ZulaessigesFach = new ZulaessigesFach("L5", 131, [new FachKatalogEintrag(132000000, "L5", "Lateinisch, regulärer Beginn in Jahrgang 5", "L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly L6 : ZulaessigesFach = new ZulaessigesFach("L6", 132, [new FachKatalogEintrag(133000000, "L6", "Lateinisch, regulärer Beginn in Jahrgang 6", "L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly L7 : ZulaessigesFach = new ZulaessigesFach("L7", 133, [new FachKatalogEintrag(134000000, "L7", "Lateinisch, regulärer Beginn in Jahrgang 7", "L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly L8 : ZulaessigesFach = new ZulaessigesFach("L8", 134, [new FachKatalogEintrag(135000000, "L8", "Lateinisch, regulärer Beginn in Jahrgang 8", "L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly L9 : ZulaessigesFach = new ZulaessigesFach("L9", 135, [new FachKatalogEintrag(136000000, "L9", "Lateinisch, regulärer Beginn in Jahrgang 9", "L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Albanisch 
	 */
	public static readonly LH : ZulaessigesFach = new ZulaessigesFach("LH", 136, [new FachKatalogEintrag(137000000, "LH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Albanisch", "LH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Literatur 
	 */
	public static readonly LI : ZulaessigesFach = new ZulaessigesFach("LI", 137, [new FachKatalogEintrag(138000000, "LI", "Literatur", "LI", null, Fachgruppe.FG_ME, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Albanisch 
	 */
	public static readonly LM : ZulaessigesFach = new ZulaessigesFach("LM", 138, [new FachKatalogEintrag(139000000, "LM", "Unterricht in der Herkunftssprache - Albanisch", "LM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Lateinisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly LQ : ZulaessigesFach = new ZulaessigesFach("LQ", 139, [new FachKatalogEintrag(140000000, "LQ", "Lateinisch, außerhalb des regulären Fachunterrichts", "LQ", null, null, null, true, false, true, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Mathematik 
	 */
	public static readonly M : ZulaessigesFach = new ZulaessigesFach("M", 140, [new FachKatalogEintrag(141000000, "M", "Mathematik", "M", 3, Fachgruppe.FG_M, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Religionsunterricht der mennonitischen Brüdergemeinden in NRW als Schulversuch 
	 */
	public static readonly MB : ZulaessigesFach = new ZulaessigesFach("MB", 141, [new FachKatalogEintrag(142000000, "MB", "Religionsunterricht der mennonitischen Brüdergemeinden in NRW als Schulversuch", "MB", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Medienerziehung 
	 */
	public static readonly MD : ZulaessigesFach = new ZulaessigesFach("MD", 142, [new FachKatalogEintrag(143000000, "MD", "Medienerziehung", "MD", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Mechanik 
	 */
	public static readonly ME : ZulaessigesFach = new ZulaessigesFach("ME", 143, [new FachKatalogEintrag(144000000, "ME", "Mechanik", "ME", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Mazedonisch 
	 */
	public static readonly MH : ZulaessigesFach = new ZulaessigesFach("MH", 144, [new FachKatalogEintrag(145000000, "MH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Mazedonisch", "MH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Mazedonisch 
	 */
	public static readonly MM : ZulaessigesFach = new ZulaessigesFach("MM", 145, [new FachKatalogEintrag(146000000, "MM", "Unterricht in der Herkunftssprache - Mazedonisch", "MM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Meß- und Prüftechnik 
	 */
	public static readonly MP : ZulaessigesFach = new ZulaessigesFach("MP", 146, [new FachKatalogEintrag(147000000, "MP", "Meß- und Prüftechnik", "MP", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Musik 
	 */
	public static readonly MU : ZulaessigesFach = new ZulaessigesFach("MU", 147, [new FachKatalogEintrag(148000000, "MU", "Musik", "MU", 1, Fachgruppe.FG_MS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Methodik des wissenschaftl. orientierten Arbeitens 
	 */
	public static readonly MW : ZulaessigesFach = new ZulaessigesFach("MW", 148, [new FachKatalogEintrag(149000000, "MW", "Methodik des wissenschaftl. orientierten Arbeitens", "MW", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spezielle sonderpädagogische Maßnahme 
	 */
	public static readonly MX : ZulaessigesFach = new ZulaessigesFach("MX", 149, [new FachKatalogEintrag(150000000, "MX", "Spezielle sonderpädagogische Maßnahmen", "MX", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Fächerübergreifender Unterricht in SI und SII (Berufspraxisstufe) 
	 */
	public static readonly MY : ZulaessigesFach = new ZulaessigesFach("MY", 150, [new FachKatalogEintrag(151000000, "MY", "Fächerübergreifender Unterricht in SI und SII (Berufspraxisstufe)", "MY", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Niederländisch 
	 */
	public static readonly N : ZulaessigesFach = new ZulaessigesFach("N", 151, [new FachKatalogEintrag(152000000, "N", "Niederländisch", "N", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Niederländisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly N0 : ZulaessigesFach = new ZulaessigesFach("N0", 152, [new FachKatalogEintrag(153000000, "N0", "Niederländisch, regulärer Beginn in der Einführungsphase", "N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly N1 : ZulaessigesFach = new ZulaessigesFach("N1", 153, [new FachKatalogEintrag(154000000, "N1", "Niederländisch, regulärer Beginn in Jahrgang 11", "N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly N5 : ZulaessigesFach = new ZulaessigesFach("N5", 154, [new FachKatalogEintrag(155000000, "N5", "Niederländisch, regulärer Beginn in Jahrgang 5", "N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly N6 : ZulaessigesFach = new ZulaessigesFach("N6", 155, [new FachKatalogEintrag(156000000, "N6", "Niederländisch, regulärer Beginn in Jahrgang 6", "N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly N7 : ZulaessigesFach = new ZulaessigesFach("N7", 156, [new FachKatalogEintrag(157000000, "N7", "Niederländisch, regulärer Beginn in Jahrgang 7", "N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly N8 : ZulaessigesFach = new ZulaessigesFach("N8", 157, [new FachKatalogEintrag(158000000, "N8", "Niederländisch, regulärer Beginn in Jahrgang 8", "N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly N9 : ZulaessigesFach = new ZulaessigesFach("N9", 158, [new FachKatalogEintrag(159000000, "N9", "Niederländisch, regulärer Beginn in Jahrgang 9", "N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Polnisch 
	 */
	public static readonly NH : ZulaessigesFach = new ZulaessigesFach("NH", 159, [new FachKatalogEintrag(160000000, "NH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Polnisch", "NH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Niederländisch 
	 */
	public static readonly NM : ZulaessigesFach = new ZulaessigesFach("NM", 160, [new FachKatalogEintrag(161000000, "NM", "Unterricht in der Herkunftssprache - Niederländisch", "NM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Niederländisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly NQ : ZulaessigesFach = new ZulaessigesFach("NQ", 161, [new FachKatalogEintrag(162000000, "NQ", "Niederländisch, außerhalb des regulären Fachunterrichts", "NQ", null, null, null, true, false, true, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Naturwissenschaften 
	 */
	public static readonly NW : ZulaessigesFach = new ZulaessigesFach("NW", 162, [new FachKatalogEintrag(163000000, "NW", "Naturwissenschaften", "NW", null, Fachgruppe.FG_NW, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Portugiesisch 
	 */
	public static readonly O : ZulaessigesFach = new ZulaessigesFach("O", 163, [new FachKatalogEintrag(164000000, "O", "Portugiesisch", "O", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Portugiesisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly O0 : ZulaessigesFach = new ZulaessigesFach("O0", 164, [new FachKatalogEintrag(165000000, "O0", "Portugiesisch, regulärer Beginn in der Einführungsphase", "O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly O1 : ZulaessigesFach = new ZulaessigesFach("O1", 165, [new FachKatalogEintrag(166000000, "O1", "Portugiesisch, regulärer Beginn in Jahrgang 11", "O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly O5 : ZulaessigesFach = new ZulaessigesFach("O5", 166, [new FachKatalogEintrag(167000000, "O5", "Portugiesisch, regulärer Beginn in Jahrgang 5", "O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly O6 : ZulaessigesFach = new ZulaessigesFach("O6", 167, [new FachKatalogEintrag(168000000, "O6", "Portugiesisch, regulärer Beginn in Jahrgang 6", "O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly O7 : ZulaessigesFach = new ZulaessigesFach("O7", 168, [new FachKatalogEintrag(169000000, "O7", "Portugiesisch, regulärer Beginn in Jahrgang 7", "O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly O8 : ZulaessigesFach = new ZulaessigesFach("O8", 169, [new FachKatalogEintrag(170000000, "O8", "Portugiesisch, regulärer Beginn in Jahrgang 8", "O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly O9 : ZulaessigesFach = new ZulaessigesFach("O9", 170, [new FachKatalogEintrag(171000000, "O9", "Portugiesisch, regulärer Beginn in Jahrgang 9", "O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Portugiesisch 
	 */
	public static readonly OH : ZulaessigesFach = new ZulaessigesFach("OH", 171, [new FachKatalogEintrag(172000000, "OH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Portugiesisch", "OH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Organisationslehre/Büroorganisation 
	 */
	public static readonly OL : ZulaessigesFach = new ZulaessigesFach("OL", 172, [new FachKatalogEintrag(173000000, "OL", "Organisationslehre/Büroorganisation", "OL", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Portugiesisch 
	 */
	public static readonly OM : ZulaessigesFach = new ZulaessigesFach("OM", 173, [new FachKatalogEintrag(174000000, "OM", "Unterricht in der Herkunftssprache - Portugiesisch", "OM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Portugiesisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly OQ : ZulaessigesFach = new ZulaessigesFach("OQ", 174, [new FachKatalogEintrag(175000000, "OQ", "Portugiesisch, außerhalb des regulären Fachunterrichts", "OQ", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Orthodoxe Religionslehre 
	 */
	public static readonly OR : ZulaessigesFach = new ZulaessigesFach("OR", 175, [new FachKatalogEintrag(176000000, "OR", "Orthodoxe Religionslehre", "OR", null, Fachgruppe.FG_RE, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Pädagogik/ Erziehungswissenschaft 
	 */
	public static readonly PA : ZulaessigesFach = new ZulaessigesFach("PA", 176, [new FachKatalogEintrag(177000000, "PA", "Pädagogik/ Erziehungswissenschaft", "PA", 2, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Physik 
	 */
	public static readonly PH : ZulaessigesFach = new ZulaessigesFach("PH", 177, [new FachKatalogEintrag(178000000, "PH", "Physik", "PH", 3, Fachgruppe.FG_NW, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Politik 
	 */
	public static readonly PK : ZulaessigesFach = new ZulaessigesFach("PK", 178, [new FachKatalogEintrag(179000000, "PK", "Politik", "PK", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Philosophie 
	 */
	public static readonly PL : ZulaessigesFach = new ZulaessigesFach("PL", 179, [new FachKatalogEintrag(180000000, "PL", "Philosophie", "PL", 2, Fachgruppe.FG_PL, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Polnisch 
	 */
	public static readonly PM : ZulaessigesFach = new ZulaessigesFach("PM", 180, [new FachKatalogEintrag(181000000, "PM", "Unterricht in der Herkunftssprache - Polnisch", "PM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Praktische Philosophie 
	 */
	public static readonly PP : ZulaessigesFach = new ZulaessigesFach("PP", 181, [new FachKatalogEintrag(182000000, "PP", "Praktische Philosophie", "PP", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Psychologie 
	 */
	public static readonly PS : ZulaessigesFach = new ZulaessigesFach("PS", 182, [new FachKatalogEintrag(183000000, "PS", "Psychologie", "PS", 2, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Fachübergreifender Unterricht 
	 */
	public static readonly PU : ZulaessigesFach = new ZulaessigesFach("PU", 183, [new FachKatalogEintrag(184000000, "PU", "Fachübergreifender Unterricht", "PU", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Personalwirtschaft und Soziologie/Politik 
	 */
	public static readonly PW : ZulaessigesFach = new ZulaessigesFach("PW", 184, [new FachKatalogEintrag(185000000, "PW", "Personalwirtschaft und Soziologie/Politik", "PW", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Projektkurs (mit einem oder zwei Leitfächern) 
	 */
	public static readonly PX : ZulaessigesFach = new ZulaessigesFach("PX", 185, [new FachKatalogEintrag(186000000, "PX", "Projektkurs (mit einem oder zwei Leitfächern)", "PX", null, Fachgruppe.FG_PX, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Politik/Ökonomische Grundbildung 
	 */
	public static readonly POE : ZulaessigesFach = new ZulaessigesFach("POE", 186, [new FachKatalogEintrag(187000000, "PÖ", "Politik/Ökonomische Grundbildung", "PÖ", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GM, Schulgliederung.R), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, Schulgliederung.R00), new Pair(Schulform.S, Schulgliederung.R00), new Pair(Schulform.SK, Schulgliederung.R), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Farsi 
	 */
	public static readonly QH : ZulaessigesFach = new ZulaessigesFach("QH", 187, [new FachKatalogEintrag(188000000, "QH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Farsi", "QH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Farsi 
	 */
	public static readonly QM : ZulaessigesFach = new ZulaessigesFach("QM", 188, [new FachKatalogEintrag(189000000, "QM", "Unterricht in der Herkunftssprache - Farsi", "QM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Russisch 
	 */
	public static readonly R : ZulaessigesFach = new ZulaessigesFach("R", 189, [new FachKatalogEintrag(190000000, "R", "Russisch", "R", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.FW, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Russisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly R0 : ZulaessigesFach = new ZulaessigesFach("R0", 190, [new FachKatalogEintrag(191000000, "R0", "Russisch, regulärer Beginn in der Einführungsphase", "R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly R1 : ZulaessigesFach = new ZulaessigesFach("R1", 191, [new FachKatalogEintrag(192000000, "R1", "Russisch, regulärer Beginn in Jahrgang 11", "R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly R5 : ZulaessigesFach = new ZulaessigesFach("R5", 192, [new FachKatalogEintrag(193000000, "R5", "Russisch, regulärer Beginn in Jahrgang 5", "R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly R6 : ZulaessigesFach = new ZulaessigesFach("R6", 193, [new FachKatalogEintrag(194000000, "R6", "Russisch, regulärer Beginn in Jahrgang 6", "R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly R7 : ZulaessigesFach = new ZulaessigesFach("R7", 194, [new FachKatalogEintrag(195000000, "R7", "Russisch, regulärer Beginn in Jahrgang 7", "R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly R8 : ZulaessigesFach = new ZulaessigesFach("R8", 195, [new FachKatalogEintrag(196000000, "R8", "Russisch, regulärer Beginn in Jahrgang 8", "R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly R9 : ZulaessigesFach = new ZulaessigesFach("R9", 196, [new FachKatalogEintrag(197000000, "R9", "Russisch, regulärer Beginn in Jahrgang 9", "R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Rechnungswesen 
	 */
	public static readonly RE : ZulaessigesFach = new ZulaessigesFach("RE", 197, [new FachKatalogEintrag(198000000, "RE", "Rechnungswesen", "RE", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch 
	 */
	public static readonly RH : ZulaessigesFach = new ZulaessigesFach("RH", 198, [new FachKatalogEintrag(199000000, "RH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch", "RH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Rechtskunde 
	 */
	public static readonly RK : ZulaessigesFach = new ZulaessigesFach("RK", 199, [new FachKatalogEintrag(200000000, "RK", "Rechtskunde", "RK", 2, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Russisch 
	 */
	public static readonly RM : ZulaessigesFach = new ZulaessigesFach("RM", 200, [new FachKatalogEintrag(201000000, "RM", "Unterricht in der Herkunftssprache - Russisch", "RM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Russisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly RQ : ZulaessigesFach = new ZulaessigesFach("RQ", 201, [new FachKatalogEintrag(202000000, "RQ", "Russisch, außerhalb des regulären Fachunterrichts", "RQ", null, null, null, true, false, true, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Recht und Verwaltung 
	 */
	public static readonly RW : ZulaessigesFach = new ZulaessigesFach("RW", 202, [new FachKatalogEintrag(203000000, "RW", "Recht und Verwaltung", "RW", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spanisch 
	 */
	public static readonly S : ZulaessigesFach = new ZulaessigesFach("S", 203, [new FachKatalogEintrag(204000000, "S", "Spanisch", "S", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spanisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly S0 : ZulaessigesFach = new ZulaessigesFach("S0", 204, [new FachKatalogEintrag(205000000, "S0", "Spanisch, regulärer Beginn in der Einführungsphase", "S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly S1 : ZulaessigesFach = new ZulaessigesFach("S1", 205, [new FachKatalogEintrag(206000000, "S1", "Spanisch, regulärer Beginn in Jahrgang 11", "S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Sport für Jungen 
	 */
	public static readonly S3 : ZulaessigesFach = new ZulaessigesFach("S3", 206, [new FachKatalogEintrag(207000000, "S3", "Sport für Jungen", "S3", null, Fachgruppe.FG_SP, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Sport für Mädchen 
	 */
	public static readonly S4 : ZulaessigesFach = new ZulaessigesFach("S4", 207, [new FachKatalogEintrag(208000000, "S4", "Sport für Mädchen", "S4", null, Fachgruppe.FG_SP, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly S5 : ZulaessigesFach = new ZulaessigesFach("S5", 208, [new FachKatalogEintrag(209000000, "S5", "Spanisch, regulärer Beginn in Jahrgang 5", "S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly S6 : ZulaessigesFach = new ZulaessigesFach("S6", 209, [new FachKatalogEintrag(210000000, "S6", "Spanisch, regulärer Beginn in Jahrgang 6", "S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly S7 : ZulaessigesFach = new ZulaessigesFach("S7", 210, [new FachKatalogEintrag(211000000, "S7", "Spanisch, regulärer Beginn in Jahrgang 7", "S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly S8 : ZulaessigesFach = new ZulaessigesFach("S8", 211, [new FachKatalogEintrag(212000000, "S8", "Spanisch, regulärer Beginn in Jahrgang 8", "S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly S9 : ZulaessigesFach = new ZulaessigesFach("S9", 212, [new FachKatalogEintrag(213000000, "S9", "Spanisch, regulärer Beginn in Jahrgang 9", "S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Softwareentwicklung und -engineering 
	 */
	public static readonly SE : ZulaessigesFach = new ZulaessigesFach("SE", 213, [new FachKatalogEintrag(214000000, "SE", "Softwareentwicklung und -engineering", "SE", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach sonderpädag. Förderung für Schüler/-innen mit sonderpädag. Förderbedarf 
	 */
	public static readonly SG : ZulaessigesFach = new ZulaessigesFach("SG", 214, [new FachKatalogEintrag(215000000, "SG", "Sonderpädag. Förderung für Schüler/-innen mit sonderpädag. Förderbedarf", "SG", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Spanisch 
	 */
	public static readonly SH : ZulaessigesFach = new ZulaessigesFach("SH", 215, [new FachKatalogEintrag(216000000, "SH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Spanisch", "SH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spiel 
	 */
	public static readonly SI : ZulaessigesFach = new ZulaessigesFach("SI", 216, [new FachKatalogEintrag(217000000, "SI", "Spiel", "SI", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Soziologie 
	 */
	public static readonly SL : ZulaessigesFach = new ZulaessigesFach("SL", 217, [new FachKatalogEintrag(218000000, "SL", "Soziologie", "SL", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Spanisch 
	 */
	public static readonly SM : ZulaessigesFach = new ZulaessigesFach("SM", 218, [new FachKatalogEintrag(219000000, "SM", "Unterricht in der Herkunftssprache - Spanisch", "SM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Sport 
	 */
	public static readonly SP : ZulaessigesFach = new ZulaessigesFach("SP", 219, [new FachKatalogEintrag(220000000, "SP", "Sport", "SP", null, Fachgruppe.FG_SP, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Spanisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly SQ : ZulaessigesFach = new ZulaessigesFach("SQ", 220, [new FachKatalogEintrag(221000000, "SQ", "Spanisch, außerhalb des regulären Fachunterrichts", "SQ", null, null, null, true, false, true, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Sonstige Fremdsprachen 
	 */
	public static readonly SR : ZulaessigesFach = new ZulaessigesFach("SR", 221, [new FachKatalogEintrag(222000000, "SR", "Sonstige Sprachen", "SR", null, null, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.FW, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Sachunterricht 
	 */
	public static readonly SU : ZulaessigesFach = new ZulaessigesFach("SU", 222, [new FachKatalogEintrag(223000000, "SU", "Sachunterricht", "SU", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Sozialwissenschaften 
	 */
	public static readonly SW : ZulaessigesFach = new ZulaessigesFach("SW", 223, [new FachKatalogEintrag(224000000, "SW", "Sozialwissenschaften", "SW", 2, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Sozialwissenschaften/Wirtschaft 
	 */
	public static readonly SZ : ZulaessigesFach = new ZulaessigesFach("SZ", 224, [new FachKatalogEintrag(225000000, "SZ", "Sozialwissenschaften/Wirtschaft", "SZ", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Türkisch 
	 */
	public static readonly T : ZulaessigesFach = new ZulaessigesFach("T", 225, [new FachKatalogEintrag(226000000, "T", "Türkisch", "T", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Türkisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly T0 : ZulaessigesFach = new ZulaessigesFach("T0", 226, [new FachKatalogEintrag(227000000, "T0", "Türkisch, regulärer Beginn in der Einführungsphase", "T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly T1 : ZulaessigesFach = new ZulaessigesFach("T1", 227, [new FachKatalogEintrag(228000000, "T1", "Türkisch, regulärer Beginn in Jahrgang 11", "T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly T5 : ZulaessigesFach = new ZulaessigesFach("T5", 228, [new FachKatalogEintrag(229000000, "T5", "Türkisch, regulärer Beginn in Jahrgang 5", "T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly T6 : ZulaessigesFach = new ZulaessigesFach("T6", 229, [new FachKatalogEintrag(230000000, "T6", "Türkisch, regulärer Beginn in Jahrgang 6", "T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly T7 : ZulaessigesFach = new ZulaessigesFach("T7", 230, [new FachKatalogEintrag(231000000, "T7", "Türkisch, regulärer Beginn in Jahrgang 7", "T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly T8 : ZulaessigesFach = new ZulaessigesFach("T8", 231, [new FachKatalogEintrag(232000000, "T8", "Türkisch, regulärer Beginn in Jahrgang 8", "T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly T9 : ZulaessigesFach = new ZulaessigesFach("T9", 232, [new FachKatalogEintrag(233000000, "T9", "Türkisch, regulärer Beginn in Jahrgang 9", "T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Technik 
	 */
	public static readonly TC : ZulaessigesFach = new ZulaessigesFach("TC", 233, [new FachKatalogEintrag(234000000, "TC", "Technik", "TC", 3, Fachgruppe.FG_WN, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, Schulgliederung.GY), new Pair(Schulform.GM, Schulgliederung.R), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Technische Grundbildung 
	 */
	public static readonly TG : ZulaessigesFach = new ZulaessigesFach("TG", 234, [new FachKatalogEintrag(235000000, "TG", "Technische Grundbildung", "TG", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Türkisch 
	 */
	public static readonly TH : ZulaessigesFach = new ZulaessigesFach("TH", 235, [new FachKatalogEintrag(236000000, "TH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Türkisch", "TH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Technische Informatik 
	 */
	public static readonly TI : ZulaessigesFach = new ZulaessigesFach("TI", 236, [new FachKatalogEintrag(237000000, "TI", "Technische Informatik", "TI", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Türkisch 
	 */
	public static readonly TM : ZulaessigesFach = new ZulaessigesFach("TM", 237, [new FachKatalogEintrag(238000000, "TM", "Unterricht in der Herkunftssprache - Türkisch", "TM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Türkisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly TQ : ZulaessigesFach = new ZulaessigesFach("TQ", 238, [new FachKatalogEintrag(239000000, "TQ", "Türkisch, außerhalb des regulären Fachunterrichts", "T", null, null, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Textverarbeitung 
	 */
	public static readonly TV : ZulaessigesFach = new ZulaessigesFach("TV", 239, [new FachKatalogEintrag(240000000, "TV", "Textverarbeitung", "TV", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Textilgestaltung 
	 */
	public static readonly TX : ZulaessigesFach = new ZulaessigesFach("TX", 240, [new FachKatalogEintrag(241000000, "TX", "Textilgestaltung", "TX", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Technisches Zeichnen / Fachzeichnen 
	 */
	public static readonly TZ : ZulaessigesFach = new ZulaessigesFach("TZ", 241, [new FachKatalogEintrag(242000000, "TZ", "Technisches Zeichnen / Fachzeichnen", "TZ", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch 
	 */
	public static readonly UH : ZulaessigesFach = new ZulaessigesFach("UH", 242, [new FachKatalogEintrag(243000000, "UH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch", "UH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Rumänisch 
	 */
	public static readonly UM : ZulaessigesFach = new ZulaessigesFach("UM", 243, [new FachKatalogEintrag(244000000, "UM", "Unterricht in der Herkunftssprache - Rumänisch", "UM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach fächerübergreifender Unterricht (Sprache/Sachunt./Mathematik/Förderunt.) 
	 */
	public static readonly UU : ZulaessigesFach = new ZulaessigesFach("UU", 244, [new FachKatalogEintrag(245000000, "UU", "Fächerübergreifender Unterricht (Sprache/Sachunt./Mathematik/Förderunt.)", "UU", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterweisung 
	 */
	public static readonly UW : ZulaessigesFach = new ZulaessigesFach("UW", 245, [new FachKatalogEintrag(246000000, "UW", "Unterweisung", "UW", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Verwaltungskunde 
	 */
	public static readonly VE : ZulaessigesFach = new ZulaessigesFach("VE", 246, [new FachKatalogEintrag(247000000, "VE", "Verwaltungskunde", "VE", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Fächer im genehmigten Schulversuch und sonstige Fächer 
	 */
	public static readonly VF : ZulaessigesFach = new ZulaessigesFach("VF", 247, [new FachKatalogEintrag(248000000, "VF", "Fächer im genehmigten Schulversuch und sonstige Fächer", "VF", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bulgarisch 
	 */
	public static readonly VH : ZulaessigesFach = new ZulaessigesFach("VH", 248, [new FachKatalogEintrag(249000000, "VH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Bulgarisch", "VH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Verkaufskunde 
	 */
	public static readonly VK : ZulaessigesFach = new ZulaessigesFach("VK", 249, [new FachKatalogEintrag(250000000, "VK", "Verkaufskunde", "VK", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Bulgarisch 
	 */
	public static readonly VM : ZulaessigesFach = new ZulaessigesFach("VM", 250, [new FachKatalogEintrag(251000000, "VM", "Unterricht in der Herkunftssprache - Bulgarisch", "VM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach vokalpraktischer Grundkurs 
	 */
	public static readonly VO : ZulaessigesFach = new ZulaessigesFach("VO", 251, [new FachKatalogEintrag(252000000, "VO", "vokalpraktischer Grundkurs", "VO", null, Fachgruppe.FG_ME, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Volkswirtschaftslehre/Politik 
	 */
	public static readonly VP : ZulaessigesFach = new ZulaessigesFach("VP", 252, [new FachKatalogEintrag(253000000, "VP", "Volkswirtschaftslehre/Politik", "VP", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Verfahrenstechnik 
	 */
	public static readonly VT : ZulaessigesFach = new ZulaessigesFach("VT", 253, [new FachKatalogEintrag(254000000, "VT", "Verfahrenstechnik", "VT", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Volkswirtschaftslehre 
	 */
	public static readonly VW : ZulaessigesFach = new ZulaessigesFach("VW", 254, [new FachKatalogEintrag(255000000, "VW", "Volkswirtschaftslehre", "VW", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Vertiefungsfach 
	 */
	public static readonly VX : ZulaessigesFach = new ZulaessigesFach("VX", 255, [new FachKatalogEintrag(256000000, "VX", "Vertiefungsfach", "VX", null, Fachgruppe.FG_VX, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Werken 
	 */
	public static readonly W : ZulaessigesFach = new ZulaessigesFach("W", 256, [new FachKatalogEintrag(257000000, "W", "Werken", "W", null, Fachgruppe.FG_MS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Wirtschaftsgeographie 
	 */
	public static readonly WG : ZulaessigesFach = new ZulaessigesFach("WG", 257, [new FachKatalogEintrag(258000000, "WG", "Wirtschaftsgeographie", "WG", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Wirtschaftsinformatik/Organisationslehre 
	 */
	public static readonly WI : ZulaessigesFach = new ZulaessigesFach("WI", 258, [new FachKatalogEintrag(259000000, "WI", "Wirtschaftsinformatik/Organisationslehre", "WI", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Wirtschaft-Politik 
	 */
	public static readonly WP : ZulaessigesFach = new ZulaessigesFach("WP", 259, [new FachKatalogEintrag(260000000, "WP", "Wirtschaft-Politik", "WP", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Wirtschaftslehre 
	 */
	public static readonly WW : ZulaessigesFach = new ZulaessigesFach("WW", 260, [new FachKatalogEintrag(261000000, "WW", "Wirtschaftslehre", "WW", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Wirtschaft und Arbeitswelt - Schwerpunkt Hauswirtschaft 
	 */
	public static readonly WX : ZulaessigesFach = new ZulaessigesFach("WX", 261, [new FachKatalogEintrag(262000000, "WX", "Wirtschaft und Arbeitswelt - Schwerpunkt Hauswirtschaft", "WX", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), 2021, null)]);

	/**
	 * Fach Wirtschaft und Arbeitswelt - Schwerpunkt Technik 
	 */
	public static readonly WY : ZulaessigesFach = new ZulaessigesFach("WY", 262, [new FachKatalogEintrag(263000000, "WY", "Wirtschaft und Arbeitswelt - Schwerpunkt Technik", "WY", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), 2021, null)]);

	/**
	 * Fach Wirtschaft und Arbeitswelt - Schwerpunkt Wirtschaft 
	 */
	public static readonly WZ : ZulaessigesFach = new ZulaessigesFach("WZ", 263, [new FachKatalogEintrag(264000000, "WZ", "Wirtschaft und Arbeitswelt - Schwerpunkt Wirtschaft", "WZ", null, Fachgruppe.FG_GS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), 2021, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - sonstige Sprache 
	 */
	public static readonly XH : ZulaessigesFach = new ZulaessigesFach("XH", 264, [new FachKatalogEintrag(265000000, "XH", "Herkunftssprache anstelle einer Pflichtfremdsprache - sonstige Sprache", "XH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - sonstige Sprache 
	 */
	public static readonly XM : ZulaessigesFach = new ZulaessigesFach("XM", 265, [new FachKatalogEintrag(266000000, "XM", "Unterricht in der Herkunftssprache - sonstige Sprache", "XM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Sonstige Fächer ohne Fremdsprachen (kein Import nach ASDPC) 
	 */
	public static readonly XX : ZulaessigesFach = new ZulaessigesFach("XX", 266, [new FachKatalogEintrag(267000000, "XX", "Sonstige Fächer ohne Fremdsprachen (kein Import nach ASDPC)", "XX", null, null, null, false, false, false, false, false, false, false, Arrays.asList(new Pair(Schulform.FW, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Koreanisch 
	 */
	public static readonly YH : ZulaessigesFach = new ZulaessigesFach("YH", 267, [new FachKatalogEintrag(268000000, "YH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Koreanisch", "YH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Koreanisch 
	 */
	public static readonly YM : ZulaessigesFach = new ZulaessigesFach("YM", 268, [new FachKatalogEintrag(269000000, "YM", "Unterricht in der Herkunftssprache - Koreanisch", "YM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach orthodoxe Religionslehre (Syrisch) 
	 */
	public static readonly YR : ZulaessigesFach = new ZulaessigesFach("YR", 269, [new FachKatalogEintrag(270000000, "YR", "Syrisch Orthodoxe Religionslehre", "YR", null, Fachgruppe.FG_RE, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Neugriechisch 
	 */
	public static readonly Z : ZulaessigesFach = new ZulaessigesFach("Z", 270, [new FachKatalogEintrag(271000000, "Z", "Neugriechisch", "Z", 1, Fachgruppe.FG_FS, null, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Neugriechisch, regulärer Beginn in der Einführungsphase 
	 */
	public static readonly Z0 : ZulaessigesFach = new ZulaessigesFach("Z0", 271, [new FachKatalogEintrag(272000000, "Z0", "Neugriechisch, regulärer Beginn in der Einführungsphase", "Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 11 
	 */
	public static readonly Z1 : ZulaessigesFach = new ZulaessigesFach("Z1", 272, [new FachKatalogEintrag(273000000, "Z1", "Neugriechisch, regulärer Beginn in Jahrgang 11", "Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11, true, false, false, false, false, true, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, 2012)]);

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 5 
	 */
	public static readonly Z5 : ZulaessigesFach = new ZulaessigesFach("Z5", 273, [new FachKatalogEintrag(274000000, "Z5", "Neugriechisch, regulärer Beginn in Jahrgang 5", "Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 6 
	 */
	public static readonly Z6 : ZulaessigesFach = new ZulaessigesFach("Z6", 274, [new FachKatalogEintrag(275000000, "Z6", "Neugriechisch, regulärer Beginn in Jahrgang 6", "Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 7 
	 */
	public static readonly Z7 : ZulaessigesFach = new ZulaessigesFach("Z7", 275, [new FachKatalogEintrag(276000000, "Z7", "Neugriechisch, regulärer Beginn in Jahrgang 7", "Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 8 
	 */
	public static readonly Z8 : ZulaessigesFach = new ZulaessigesFach("Z8", 276, [new FachKatalogEintrag(277000000, "Z8", "Neugriechisch, regulärer Beginn in Jahrgang 8", "Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 9 
	 */
	public static readonly Z9 : ZulaessigesFach = new ZulaessigesFach("Z9", 277, [new FachKatalogEintrag(278000000, "Z9", "Neugriechisch, regulärer Beginn in Jahrgang 9", "Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09, true, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Zusätzliche Förderung 
	 */
	public static readonly ZF : ZulaessigesFach = new ZulaessigesFach("ZF", 278, [new FachKatalogEintrag(279000000, "ZF", "Zusätzliche Förderung", "ZF", null, null, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kurdische Sprachen 
	 */
	public static readonly ZH : ZulaessigesFach = new ZulaessigesFach("ZH", 279, [new FachKatalogEintrag(280000000, "ZH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Kurdische Sprachen", "ZH", null, null, null, true, true, false, true, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Unterricht in der Herkunftssprache - Kurdische Sprachen (Sorani, Komanci, Zaza) 
	 */
	public static readonly ZM : ZulaessigesFach = new ZulaessigesFach("ZM", 280, [new FachKatalogEintrag(281000000, "ZM", "Unterricht in der Herkunftssprache - Kurdische Sprachen (Sorani, Komanci, Zaza)", "ZM", null, null, null, true, true, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Neugriechisch, außerhalb des regulären Fachunterrichts 
	 */
	public static readonly ZQ : ZulaessigesFach = new ZulaessigesFach("ZQ", 281, [new FachKatalogEintrag(282000000, "ZQ", "Neugriechisch, außerhalb des regulären Fachunterrichts", "ZQ", null, null, null, true, false, true, false, false, false, true, Arrays.asList(new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Fach Zeichnen / Werken 
	 */
	public static readonly ZW : ZulaessigesFach = new ZulaessigesFach("ZW", 282, [new FachKatalogEintrag(283000000, "ZW", "Zeichnen / Werken", "ZW", null, Fachgruppe.FG_MS, null, false, false, false, false, false, false, true, Arrays.asList(new Pair(Schulform.BK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Ein Standard-Wert, welcher u.a. gewählt wird, falls ein ungültiger Wert in der Datenbank eingetragen ist. 
	 */
	public static readonly DEFAULT : ZulaessigesFach = ZulaessigesFach.VF;

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. 
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Faches 
	 */
	public readonly daten : FachKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen des Faches 
	 */
	public readonly historie : Array<FachKatalogEintrag>;

	/**
	 * Eine HashMap mit allen zulässigen Fächern. Der Zugriff erfolgt dabei über die ID 
	 */
	private static readonly _mapID : HashMap<number, ZulaessigesFach | null> = new HashMap();

	/**
	 * Eine HashMap mit zulässigen Fächern. Der Zugriff erfolgt dabei das Statistik-Kürzel 
	 */
	private static readonly _mapKuerzelASD : HashMap<string, ZulaessigesFach | null> = new HashMap();

	/**
	 * Die Informationen zu den Kombinationen aus Schulformen und -gliederungen, wo das Fach zulässig ist 
	 */
	private zulaessig : Array<Vector<Pair<Schulform | null, Schulgliederung | null>>>;

	/**
	 * Erzeugt eine zulässiges Fach in der Aufzählung.
	 * 
	 * @param historie   die Historie des Faches, welches ein Array von {@link FachKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<FachKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		ZulaessigesFach.all_values_by_ordinal.push(this);
		ZulaessigesFach.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.zulaessig = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++){
			this.zulaessig[i] = new Vector();
			for (let kuerzelSfSgl of historie[i].zulaessig) {
				let sf : Schulform | null = Schulform.getByKuerzel(kuerzelSfSgl.schulform);
				if (sf === null) 
					continue;
				let sgl : Schulgliederung | null = kuerzelSfSgl.gliederung === null ? null : Schulgliederung.getByKuerzel(kuerzelSfSgl.gliederung);
				this.zulaessig[i].add(new Pair(sf, sgl));
			}
		}
	}

	/**
	 * Gibt eine Map von den ASD-Kürzeln der Fächer auf die zugehörigen Fächer
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den ASD-Kürzeln der Fächer auf die zugehörigen Fächer
	 */
	private static getMapByASDKuerzel() : HashMap<string, ZulaessigesFach | null> {
		if (ZulaessigesFach._mapKuerzelASD.size() === 0) 
			for (let s of ZulaessigesFach.values()) 
				ZulaessigesFach._mapKuerzelASD.put(s.daten.kuerzelASD, s);
		return ZulaessigesFach._mapKuerzelASD;
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
		for (let sfsgl of this.zulaessig[0]) {
			if (sfsgl.a === schulform) 
				return true;
		}
		return false;
	}

	/**
	 * Bestimmt alle Fächer, die in irgendeiner Gliederung der angegebenen Schulform
	 * zulässig sind. 
	 *  
	 * @param schulform    die Schulform
	 * 
	 * @return die zulässigen Fächer in der angegebenen Schulform
	 */
	public static get(schulform : Schulform | null) : List<ZulaessigesFach | null> {
		let faecher : Vector<ZulaessigesFach | null> = new Vector();
		if (schulform === null) 
			return faecher;
		for (let fach of ZulaessigesFach.values()) 
			if (fach.hasSchulform(schulform)) 
				faecher.add(fach);
		return faecher;
	}

	/**
	 * Gibt die Fachgruppe dieses Faches zurück. 
	 * 
	 * @return die Fachgruppe des Faches
	 */
	public getFachgruppe() : Fachgruppe | null {
		if (this.daten.fachgruppe === null) 
			return null;
		return Fachgruppe.getByKuerzel(this.daten.fachgruppe);
	}

	/**
	 * Gibt den Jahrgang zurück, ab wann dieses Faches zulässig ist. 
	 * 
	 * @return der Jahrgang
	 */
	public getJahrgangAb() : Jahrgaenge | null {
		if (this.daten.abJahrgang === null) 
			return null;
		return Jahrgaenge.getByKuerzel(this.daten.abJahrgang);
	}

	/**
	 * Gibt das Fach zurück, welches dem übergebenen Kürzel zuzuordnet ist.
	 * Ist der übergebene Wert ungültig, so wird {@link ZulaessigesFach#DEFAULT}
	 * zurückgeben.
	 * 
	 * @param kuerzel   das Statistik-Kürzel des Faches
	 * 
	 * @return das zugehörige Fach oder {@link ZulaessigesFach#DEFAULT} 
	 */
	public static getByKuerzelASD(kuerzel : string | null) : ZulaessigesFach {
		let result : ZulaessigesFach | null = ZulaessigesFach.getMapByASDKuerzel().get(kuerzel);
		return (result === null) ? ZulaessigesFach.DEFAULT : result;
	}

	/**
	 * Gibt die Farbe des zulässigen Faches zurück.
	 * 
	 * @return die Farbe des zulässigen Faches
	 */
	public getFarbe() : RGBFarbe {
		let gruppe : Fachgruppe | null = Fachgruppe.getByKuerzel(this.daten.fachgruppe);
		return gruppe === null ? new RGBFarbe() : gruppe.daten.farbe;
	}

	/**
	 * Gibt die HTML-Farbe des zulässigen Faches als Aufruf der rgb-Funktion
	 * mit der übergebenen Transparenz zurück.
	 * 
	 * @return die RGB-HTML-Farbdefinition als String
	 */
	public getHMTLFarbeRGB() : string {
		let farbe : RGBFarbe = this.getFarbe();
		return "rgba(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
	}

	/**
	 * Gibt die HTML-Farbe des zulässigen Faches als Aufruf der rgba-Funktion
	 * mit der übergebenen Transparenz zurück.
	 * 
	 * @param alpha   gibt die Deckkraft der Farbe an 
	 * 
	 * @return die RGBA-HTML-Farbdefinition als String
	 */
	public getHMTLFarbeRGBA(alpha : number) : string {
		let farbe : RGBFarbe = this.getFarbe();
		let a : number = (alpha < 0.0) ? 0.0 : ((alpha > 1.0) ? 1.0 : alpha);
		return "rgba(" + farbe.red + "," + farbe.green + "," + farbe.blue + ", " + a + ")";
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
		if (!(other instanceof ZulaessigesFach))
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
	public compareTo(other : ZulaessigesFach) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<ZulaessigesFach> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : ZulaessigesFach | null {
		let tmp : ZulaessigesFach | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.fach.ZulaessigesFach'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_fach_ZulaessigesFach(obj : unknown) : ZulaessigesFach {
	return obj as ZulaessigesFach;
}
