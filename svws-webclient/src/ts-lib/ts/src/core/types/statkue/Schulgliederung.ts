import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SchulabschlussAllgemeinbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussAllgemeinbildend } from '../../../core/types/schule/SchulabschlussAllgemeinbildend';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { Schulform, cast_de_nrw_schule_svws_core_types_statkue_Schulform } from '../../../core/types/statkue/Schulform';
import { BerufskollegAnlage, cast_de_nrw_schule_svws_core_types_schule_BerufskollegAnlage } from '../../../core/types/schule/BerufskollegAnlage';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { SchulgliederungKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_SchulgliederungKatalogEintrag } from '../../../core/data/schule/SchulgliederungKatalogEintrag';
import { SchulabschlussBerufsbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussBerufsbildend } from '../../../core/types/schule/SchulabschlussBerufsbildend';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class Schulgliederung extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Schulgliederung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, Schulgliederung> = new Map<String, Schulgliederung>();

	public static readonly DEFAULT : Schulgliederung = new Schulgliederung("DEFAULT", 0, [new SchulgliederungKatalogEintrag(0, "***", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V), false, false, "Standard für diese Schulform", null, null, null, false, null, null, null, null)]);

	public static readonly A01 : Schulgliederung = new Schulgliederung("A01", 1, [new SchulgliederungKatalogEintrag(1001000, "A01", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen (BS; TZ)", BerufskollegAnlage.A, "01", 10, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly A02 : Schulgliederung = new Schulgliederung("A02", 2, [new SchulgliederungKatalogEintrag(1002000, "A02", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen/Fachhochschulreife (BS/FHR; TZ)", BerufskollegAnlage.A, "02", 10, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly A03 : Schulgliederung = new Schulgliederung("A03", 3, [new SchulgliederungKatalogEintrag(1003000, "A03", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen/erweiterte Zusatzqualifikation (BS/ZQ; TZ)", BerufskollegAnlage.A, "03", 10, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly A04 : Schulgliederung = new Schulgliederung("A04", 4, [new SchulgliederungKatalogEintrag(1004000, "A04", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen mit erweitertem Stützunterricht (BS/Stütz; TZ)", BerufskollegAnlage.A, "04", 10, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly A05 : Schulgliederung = new Schulgliederung("A05", 5, [new SchulgliederungKatalogEintrag(1005000, "A05", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsorientierungsjahr (BV; VZ)", BerufskollegAnlage.A, "05", 20, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BV), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly A06 : Schulgliederung = new Schulgliederung("A06", 6, [new SchulgliederungKatalogEintrag(1006000, "A06", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsgrundschuljahr (BG; VZ)", BerufskollegAnlage.A, "06", 30, true, Arrays.asList(SchulabschlussBerufsbildend.BG), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly A07 : Schulgliederung = new Schulgliederung("A07", 7, [new SchulgliederungKatalogEintrag(1007000, "A07", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; TZ) bzw. Werkstattjahr (BS 1j;TZ)", BerufskollegAnlage.A, "07", 40, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly A08 : Schulgliederung = new Schulgliederung("A08", 8, [new SchulgliederungKatalogEintrag(1008000, "A08", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Vorpraktikum (VP)", BerufskollegAnlage.A, "08", 50, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly A09 : Schulgliederung = new Schulgliederung("A09", 9, [new SchulgliederungKatalogEintrag(1009000, "A09", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; VZ)", BerufskollegAnlage.A, "09", 55, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly A10 : Schulgliederung = new Schulgliederung("A10", 10, [new SchulgliederungKatalogEintrag(1010000, "A10", true, Arrays.asList(Schulform.BK, Schulform.SB), true, false, "Berufsabschluss/mittlerer Schulabschluss (BKAZVO) (BAB/FOR; VZ)", BerufskollegAnlage.A, "10", 56, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.MSA), null, null)]);

	public static readonly A11 : Schulgliederung = new Schulgliederung("A11", 11, [new SchulgliederungKatalogEintrag(1011000, "A11", true, Arrays.asList(Schulform.BK, Schulform.SB), true, false, "Berufsabschluss/Fachhochschulreife (BKAZVO) (BAB/FHR; VZ)", BerufskollegAnlage.A, "11", 56, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly A12 : Schulgliederung = new Schulgliederung("A12", 12, [new SchulgliederungKatalogEintrag(1012000, "A12", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Ausbildungsvorbereitung (BS 1j; VZ)", BerufskollegAnlage.A, "12", 57, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VORB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA9_FOE), null, null)]);

	public static readonly A13 : Schulgliederung = new Schulgliederung("A13", 13, [new SchulgliederungKatalogEintrag(1013000, "A13", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Ausbildungsvorbereitung (BS 1j; TZ)", BerufskollegAnlage.A, "13", 58, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VORB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A), null, null)]);

	public static readonly A14 : Schulgliederung = new Schulgliederung("A14", 14, [new SchulgliederungKatalogEintrag(1014000, "A14", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss (nach §50 BBiG/§40 HwO)/mittlerer Schulabschluss (BAB/FOR; VZ)", BerufskollegAnlage.A, "14", 59, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly A15 : Schulgliederung = new Schulgliederung("A15", 15, [new SchulgliederungKatalogEintrag(1015000, "A15", true, Arrays.asList(Schulform.BK), false, false, "Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife BAB/FHR; VZ", BerufskollegAnlage.A, "15", 59, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly A16 : Schulgliederung = new Schulgliederung("A16", 16, [new SchulgliederungKatalogEintrag(1016000, "A16", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen (nach §2 BKAZVO) BAB; VZ", BerufskollegAnlage.A, "16", 59, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly AB : Schulgliederung = new Schulgliederung("AB", 17, [new SchulgliederungKatalogEintrag(90001000, "AB", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.KS, Schulform.S), false, false, "Schule für Kranke: Allgemeinbildend", null, null, null, false, null, null, null, null)]);

	public static readonly B01 : Schulgliederung = new Schulgliederung("B01", 18, [new SchulgliederungKatalogEintrag(2001000, "B01", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss/Fachoberschulreife (BAB/FOR 2j; VZ)", BerufskollegAnlage.B, "01", 60, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly B02 : Schulgliederung = new Schulgliederung("B02", 19, [new SchulgliederungKatalogEintrag(2002000, "B02", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsgrundbildung/Fachoberschulreife (BG/FOR 2j; VZ)", BerufskollegAnlage.B, "02", 70, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BG), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly B03 : Schulgliederung = new Schulgliederung("B03", 20, [new SchulgliederungKatalogEintrag(2003000, "B03", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsgrundbildung (für Schüler mit FOR) (BG 1j; VZ)", BerufskollegAnlage.B, "03", 80, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BG), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly B04 : Schulgliederung = new Schulgliederung("B04", 21, [new SchulgliederungKatalogEintrag(2004000, "B04", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss/Fachoberschulreife (BAB/FOR; VZ)", BerufskollegAnlage.B, "04", 90, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly B05 : Schulgliederung = new Schulgliederung("B05", 22, [new SchulgliederungKatalogEintrag(2005000, "B05", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss/Fachhochschulreife (BAB/FHR; VZ)", BerufskollegAnlage.B, "05", 90, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly B06 : Schulgliederung = new Schulgliederung("B06", 23, [new SchulgliederungKatalogEintrag(2006000, "B06", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufliche Kenntnisse/Hauptschulabschluss Kl. 10 (BK/HSA10; 1j. VZ)", BerufskollegAnlage.B, "06", 91, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10), null, null)]);

	public static readonly B07 : Schulgliederung = new Schulgliederung("B07", 24, [new SchulgliederungKatalogEintrag(2007000, "B07", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufliche Kenntnisse/mittlerer Schulabschluss (BK/FOR; 1j. VZ)", BerufskollegAnlage.B, "07", 92, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly B08 : Schulgliederung = new Schulgliederung("B08", 25, [new SchulgliederungKatalogEintrag(2008000, "B08", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 2j. VZ)", BerufskollegAnlage.B, "08", 93, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly B09 : Schulgliederung = new Schulgliederung("B09", 26, [new SchulgliederungKatalogEintrag(2009000, "B09", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 3j. TZ)", BerufskollegAnlage.B, "09", 93, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly B10 : Schulgliederung = new Schulgliederung("B10", 27, [new SchulgliederungKatalogEintrag(2010000, "B10", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 4j. TZ)", BerufskollegAnlage.B, "10", 93, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly BT : Schulgliederung = new Schulgliederung("BT", 28, [new SchulgliederungKatalogEintrag(90002000, "BT", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.KS, Schulform.S), false, false, "Schule für Kranke: Berufsbildend (Teilzeit)", null, null, null, false, Arrays.asList(), Arrays.asList(), null, null)]);

	public static readonly BV : Schulgliederung = new Schulgliederung("BV", 29, [new SchulgliederungKatalogEintrag(90003000, "BV", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.KS, Schulform.S), false, false, "Schule für Kranke: Berufsbildend (Vollzeit)", null, null, null, true, Arrays.asList(), Arrays.asList(), null, null)]);

	public static readonly C01 : Schulgliederung = new Schulgliederung("C01", 30, [new SchulgliederungKatalogEintrag(3001000, "C01", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum) BAB/FHR 3j; VZ BFS", BerufskollegAnlage.C, "01", 100, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly C02 : Schulgliederung = new Schulgliederung("C02", 31, [new SchulgliederungKatalogEintrag(3002000, "C02", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss f. Hochschulzugangsberechtigte (BAB 2j; VZ) BFS", BerufskollegAnlage.C, "02", 110, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly C03 : Schulgliederung = new Schulgliederung("C03", 32, [new SchulgliederungKatalogEintrag(3003000, "C03", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufliche Kenntnisse/FHR (BK/FHR 2j; VZ) HBFS", BerufskollegAnlage.C, "03", 120, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly C04 : Schulgliederung = new Schulgliederung("C04", 33, [new SchulgliederungKatalogEintrag(3004000, "C04", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufliche Kenntnisse/Sonderform für Abiturienten (BK 1j; VZ) HBFS", BerufskollegAnlage.C, "04", 130, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly C05 : Schulgliederung = new Schulgliederung("C05", 34, [new SchulgliederungKatalogEintrag(3005000, "C05", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachoberschule Kl. 11 (BK/FHR 1j; TZ)", BerufskollegAnlage.C, "05", 140, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VERS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.VS_11), null, null)]);

	public static readonly C06 : Schulgliederung = new Schulgliederung("C06", 35, [new SchulgliederungKatalogEintrag(3006000, "C06", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachoberschule Kl. 12S (BK/FHR 1j; VZ)", BerufskollegAnlage.C, "06", 140, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly C07 : Schulgliederung = new Schulgliederung("C07", 36, [new SchulgliederungKatalogEintrag(3007000, "C07", true, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.WB), false, false, "Fachoberschule Kl. 12B (BK/FHR 2j; TZ)", BerufskollegAnlage.C, "07", 140, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly C08 : Schulgliederung = new Schulgliederung("C08", 37, [new SchulgliederungKatalogEintrag(3008000, "C08", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachoberschule Kl. 12B (BK/FHR 1j; VZ)", BerufskollegAnlage.C, "08", 140, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly C09 : Schulgliederung = new Schulgliederung("C09", 38, [new SchulgliederungKatalogEintrag(3009000, "C09", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (Vollzeit) (BP/Erz 1j; VZ)", BerufskollegAnlage.C, "09", 150, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly C10 : Schulgliederung = new Schulgliederung("C10", 39, [new SchulgliederungKatalogEintrag(3010000, "C10", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ)", BerufskollegAnlage.C, "10", 150, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly C11 : Schulgliederung = new Schulgliederung("C11", 40, [new SchulgliederungKatalogEintrag(3011000, "C11", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachoberschule Kl. 12B (BK/FHR 3j; TZ)", BerufskollegAnlage.C, "11", 141, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly C12 : Schulgliederung = new Schulgliederung("C12", 41, [new SchulgliederungKatalogEintrag(3012000, "C12", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss/Fachhochschulreife (mit  Berufspraktikum) BAB/FHR 3,5j; VZ", BerufskollegAnlage.C, "12", 145, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly C13 : Schulgliederung = new Schulgliederung("C13", 42, [new SchulgliederungKatalogEintrag(3013000, "C13", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss/Fachhochschulreife (gestuft) (BAB/FHR 3j; VZ)", BerufskollegAnlage.C, "13", 146, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly D01 : Schulgliederung = new Schulgliederung("D01", 43, [new SchulgliederungKatalogEintrag(4001000, "D01", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss/Allg. Hochschulreife (mit Berufspraktikum) (BAB/AHR 4j; VZ)", BerufskollegAnlage.D, "01", 160, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.FHR, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	public static readonly D02 : Schulgliederung = new Schulgliederung("D02", 44, [new SchulgliederungKatalogEintrag(4002000, "D02", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufl. Kenntnisse/Allg. Hochschulreife (BK/AHR 3j; VZ)", BerufskollegAnlage.D, "02", 170, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	public static readonly D03 : Schulgliederung = new Schulgliederung("D03", 45, [new SchulgliederungKatalogEintrag(4003000, "D03", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum (Vollzeit) (BP 1j; VZ)", BerufskollegAnlage.D, "03", 180, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly D04 : Schulgliederung = new Schulgliederung("D04", 46, [new SchulgliederungKatalogEintrag(4004000, "D04", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ)", BerufskollegAnlage.D, "04", 190, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly D05 : Schulgliederung = new Schulgliederung("D05", 47, [new SchulgliederungKatalogEintrag(4005000, "D05", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "AHR (gem. § 2 Abs. 3 Anlage D) (AHR 1j; VZ) FOS13", BerufskollegAnlage.D, "05", 200, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.FGHR, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	public static readonly D06 : Schulgliederung = new Schulgliederung("D06", 48, [new SchulgliederungKatalogEintrag(4006000, "D06", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "AHR (gem. § 2 Abs. 3 Anlage D) (AHR 2j; TZ) FOS13", BerufskollegAnlage.D, "06", 200, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.FGHR, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	public static readonly E01 : Schulgliederung = new Schulgliederung("E01", 49, [new SchulgliederungKatalogEintrag(5001000, "E01", true, Arrays.asList(Schulform.BK), false, false, "Fachschule Vollzeit (BW 2j; VZ)", BerufskollegAnlage.E, "01", 210, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly E02 : Schulgliederung = new Schulgliederung("E02", 50, [new SchulgliederungKatalogEintrag(5002000, "E02", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachschule Teilzeit (BW 4j; TZ)", BerufskollegAnlage.E, "02", 210, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly E03 : Schulgliederung = new Schulgliederung("E03", 51, [new SchulgliederungKatalogEintrag(5003000, "E03", true, Arrays.asList(Schulform.BK), false, false, "Fachschule (verkürzt) Vollzeit (BW 1j; VZ)", BerufskollegAnlage.E, "03", 220, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.AUFB, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly E04 : Schulgliederung = new Schulgliederung("E04", 52, [new SchulgliederungKatalogEintrag(5004000, "E04", true, Arrays.asList(Schulform.BK), false, false, "Fachschule (verkürzt) Teilzeit (BW 2j; TZ)", BerufskollegAnlage.E, "04", 220, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.AUFB, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly E05 : Schulgliederung = new Schulgliederung("E05", 53, [new SchulgliederungKatalogEintrag(5005000, "E05", true, Arrays.asList(Schulform.BK), false, false, "Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 3j; VZ)", BerufskollegAnlage.E, "05", 230, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly E06 : Schulgliederung = new Schulgliederung("E06", 54, [new SchulgliederungKatalogEintrag(5006000, "E06", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 1j; VZ)", BerufskollegAnlage.E, "06", 240, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly E07 : Schulgliederung = new Schulgliederung("E07", 55, [new SchulgliederungKatalogEintrag(5007000, "E07", true, Arrays.asList(Schulform.BK), false, false, "Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 6j; TZ)", BerufskollegAnlage.E, "07", 230, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly E08 : Schulgliederung = new Schulgliederung("E08", 56, [new SchulgliederungKatalogEintrag(5008000, "E08", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 2j; TZ)", BerufskollegAnlage.E, "08", 240, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly E09 : Schulgliederung = new Schulgliederung("E09", 57, [new SchulgliederungKatalogEintrag(5009000, "E09", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachschule (Sonderform) Vollzeit (BW 3j; VZ)", BerufskollegAnlage.E, "09", 250, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly E10 : Schulgliederung = new Schulgliederung("E10", 58, [new SchulgliederungKatalogEintrag(5010000, "E10", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachschule (Sonderform) Teilzeit (BW 6j; TZ)", BerufskollegAnlage.E, "10", 260, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly E11 : Schulgliederung = new Schulgliederung("E11", 59, [new SchulgliederungKatalogEintrag(5011000, "E11", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (FS/BP/Erz 1j; VZ)", BerufskollegAnlage.E, "11", 270, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly E12 : Schulgliederung = new Schulgliederung("E12", 60, [new SchulgliederungKatalogEintrag(5012000, "E12", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (FS/BP/Erz 2j; TZ)", BerufskollegAnlage.E, "12", 270, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly E13 : Schulgliederung = new Schulgliederung("E13", 61, [new SchulgliederungKatalogEintrag(5013000, "E13", true, Arrays.asList(Schulform.BK), false, false, "Fachschule Teilzeit (BW 3j; TZ)", BerufskollegAnlage.E, "13", 210, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly ER : Schulgliederung = new Schulgliederung("ER", 62, [new SchulgliederungKatalogEintrag(14001000, "ER", false, Arrays.asList(Schulform.SK), false, false, "kooperative Form: Erweiterungsebene", null, null, null, false, null, null, null, null)]);

	public static readonly EVB : Schulgliederung = new Schulgliederung("EVB", 63, [new SchulgliederungKatalogEintrag(80001000, "EVB", false, Arrays.asList(Schulform.G), false, false, "Evangelische Bekenntnisschule", null, null, null, false, null, null, null, null)]);

	public static readonly G01 : Schulgliederung = new Schulgliederung("G01", 64, [new SchulgliederungKatalogEintrag(60001000, "G01", false, Arrays.asList(Schulform.GY), false, false, "Aufbaugymnasium", null, null, null, false, null, null, null, null)]);

	public static readonly G02 : Schulgliederung = new Schulgliederung("G02", 65, [new SchulgliederungKatalogEintrag(18001000, "G02", false, Arrays.asList(Schulform.WB), false, false, "Bildungsgang Abendgymnasium", null, null, null, false, null, null, null, null)]);

	public static readonly GGS : Schulgliederung = new Schulgliederung("GGS", 66, [new SchulgliederungKatalogEintrag(15001000, "GGS", false, Arrays.asList(Schulform.GE), true, false, "Gemeinschaftsschule (auslaufend) integrierte Form", null, null, null, false, null, null, null, null)]);

	public static readonly GGY : Schulgliederung = new Schulgliederung("GGY", 67, [new SchulgliederungKatalogEintrag(15002000, "GGY", false, Arrays.asList(Schulform.GE), true, false, "Gemeinschaftsschule (auslaufend) Gymnasialbildungsgang", null, null, null, false, null, null, null, null)]);

	public static readonly GMS : Schulgliederung = new Schulgliederung("GMS", 68, [new SchulgliederungKatalogEintrag(80002000, "GMS", false, Arrays.asList(Schulform.G), false, false, "Gemeinschaftsschule", null, null, null, false, null, null, null, null)]);

	public static readonly GR : Schulgliederung = new Schulgliederung("GR", 69, [new SchulgliederungKatalogEintrag(15003000, "GR", false, Arrays.asList(Schulform.GE, Schulform.SK), false, false, "kooperative Form: Grundebene", null, null, null, false, null, null, null, null)]);

	public static readonly GRH : Schulgliederung = new Schulgliederung("GRH", 70, [new SchulgliederungKatalogEintrag(15004000, "GRH", false, Arrays.asList(Schulform.GE), true, false, "Gemeinschaftsschule auslaufend: teilintegrierte Form", null, null, null, false, null, null, null, null)]);

	public static readonly GS : Schulgliederung = new Schulgliederung("GS", 71, [new SchulgliederungKatalogEintrag(50001000, "GS", false, Arrays.asList(Schulform.GM), false, false, "integrierte Form (Binnendifferenzierung)", null, null, null, false, null, null, null, null)]);

	public static readonly GY : Schulgliederung = new Schulgliederung("GY", 72, [new SchulgliederungKatalogEintrag(15005000, "GY", false, Arrays.asList(Schulform.GE, Schulform.GM, Schulform.SK), false, false, "Bildungsgang Gymnasium", null, null, null, false, null, null, null, null)]);

	public static readonly GY8 : Schulgliederung = new Schulgliederung("GY8", 73, [new SchulgliederungKatalogEintrag(60002000, "GY8", false, Arrays.asList(Schulform.GE, Schulform.GY), false, false, "Bildungsgang G8-Gymnasium", null, null, null, false, null, null, null, null)]);

	public static readonly GY9 : Schulgliederung = new Schulgliederung("GY9", 74, [new SchulgliederungKatalogEintrag(60003000, "GY9", false, Arrays.asList(Schulform.GE, Schulform.GY), false, false, "Bildungsgang G9-Gymnasium", null, null, null, false, null, null, null, null)]);

	public static readonly H : Schulgliederung = new Schulgliederung("H", 75, [new SchulgliederungKatalogEintrag(10001000, "H", false, Arrays.asList(Schulform.GM, Schulform.R, Schulform.SK), false, false, "Bildungsgang Hauptschule", null, "  ", null, false, null, null, null, null)]);

	public static readonly H01 : Schulgliederung = new Schulgliederung("H01", 76, [new SchulgliederungKatalogEintrag(17001000, "H01", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF), false, false, "Berufsgrundbildung (Jahrgang 07 bis 10)", BerufskollegAnlage.H, "01", 980, false, null, null, null, null)]);

	public static readonly H02 : Schulgliederung = new Schulgliederung("H02", 77, [new SchulgliederungKatalogEintrag(17002000, "H02", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF), false, false, "Berufsausbildung (Jahrgang 11 und 12)", BerufskollegAnlage.H, "02", 940, false, null, null, null, null)]);

	public static readonly K02 : Schulgliederung = new Schulgliederung("K02", 78, [new SchulgliederungKatalogEintrag(18002000, "K02", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.WB), false, false, "Bildungsgang Kolleg", null, null, null, false, null, null, null, null)]);

	public static readonly R : Schulgliederung = new Schulgliederung("R", 79, [new SchulgliederungKatalogEintrag(10002000, "R", false, Arrays.asList(Schulform.GM, Schulform.H, Schulform.SK), false, false, "Bildungsgang Realschule", null, null, null, false, null, null, null, null)]);

	public static readonly R00 : Schulgliederung = new Schulgliederung("R00", 80, [new SchulgliederungKatalogEintrag(10003000, "R00", false, Arrays.asList(Schulform.KS, Schulform.S), false, false, "Realschule", null, null, null, false, null, null, null, null)]);

	public static readonly R01 : Schulgliederung = new Schulgliederung("R01", 81, [new SchulgliederungKatalogEintrag(10004000, "R01", false, Arrays.asList(Schulform.R), false, false, "Aufbaurealschule", null, null, null, false, null, null, null, null)]);

	public static readonly R02 : Schulgliederung = new Schulgliederung("R02", 82, [new SchulgliederungKatalogEintrag(18003000, "R02", false, Arrays.asList(Schulform.WB), false, false, "Bildungsgang Abendrealschule", null, null, null, false, null, null, null, null)]);

	public static readonly RH : Schulgliederung = new Schulgliederung("RH", 83, [new SchulgliederungKatalogEintrag(10005000, "RH", false, Arrays.asList(Schulform.GE, Schulform.GM, Schulform.SK), false, false, "teilintegrierte Form", null, null, null, false, null, null, null, null)]);

	public static readonly RKB : Schulgliederung = new Schulgliederung("RKB", 84, [new SchulgliederungKatalogEintrag(80003000, "RKB", false, Arrays.asList(Schulform.G), false, false, "Katholische  Bekenntnisschule", null, null, null, false, null, null, null, null)]);

	public static readonly SRH : Schulgliederung = new Schulgliederung("SRH", 85, [new SchulgliederungKatalogEintrag(10006000, "SRH", false, Arrays.asList(Schulform.GE), true, false, "Sekundarschule, teilintegrierte Form (auslaufend)", null, null, null, false, null, null, null, null)]);

	public static readonly SSI : Schulgliederung = new Schulgliederung("SSI", 86, [new SchulgliederungKatalogEintrag(10007000, "SSI", false, Arrays.asList(Schulform.GE), true, false, "Sekundarschule, integrierte Form (auslaufend)", null, null, null, false, null, null, null, null)]);

	public static readonly X01 : Schulgliederung = new Schulgliederung("X01", 87, [new SchulgliederungKatalogEintrag(6001000, "X01", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Allgemeine Hochschulreife (ausgelaufen) (AHR 3j; VZ)", BerufskollegAnlage.X, "01", 280, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.FHR, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	public static readonly X02 : Schulgliederung = new Schulgliederung("X02", 88, [new SchulgliederungKatalogEintrag(6002000, "X02", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachhochschulreife (ausgelaufen) (FHR 2j; VZ)", BerufskollegAnlage.X, "02", 290, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly X03 : Schulgliederung = new Schulgliederung("X03", 89, [new SchulgliederungKatalogEintrag(6003000, "X03", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachhochschulreife (ausgelaufen) (FHR 3j; TZ)", BerufskollegAnlage.X, "03", 300, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	public static readonly X04 : Schulgliederung = new Schulgliederung("X04", 90, [new SchulgliederungKatalogEintrag(6004000, "X04", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Fremdsprachenassistent (ausgelaufen) (BP/FAss 1j; TZ)", BerufskollegAnlage.X, "04", 310, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly X05 : Schulgliederung = new Schulgliederung("X05", 91, [new SchulgliederungKatalogEintrag(6005000, "X05", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss/Assistent (ausgelaufen) (BAB/Ass 3j; TZ)", BerufskollegAnlage.X, "05", 320, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly X06 : Schulgliederung = new Schulgliederung("X06", 92, [new SchulgliederungKatalogEintrag(6006000, "X06", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss und Allgemeine Hochschulreife (ausgelaufen) (BAB/AHR 3j; VZ)", BerufskollegAnlage.X, "06", 330, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.FHR, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	public static readonly X07 : Schulgliederung = new Schulgliederung("X07", 93, [new SchulgliederungKatalogEintrag(6007000, "X07", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss und Fachoberschulreife (ausgelaufen) (BAB/FOR 3j; VZ)", BerufskollegAnlage.X, "07", 340, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	public static readonly X08 : Schulgliederung = new Schulgliederung("X08", 94, [new SchulgliederungKatalogEintrag(6008000, "X08", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss mit Zusatzqualifikation (ausgelaufen) (BAB/ZQ 3j; VZ)", BerufskollegAnlage.X, "08", 350, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static readonly Y8 : Schulgliederung = new Schulgliederung("Y8", 95, [new SchulgliederungKatalogEintrag(10008000, "Y8", false, Arrays.asList(Schulform.GE), false, false, "Lehrplan G8", null, null, null, false, null, null, null, null)]);

	public static readonly Z01 : Schulgliederung = new Schulgliederung("Z01", 96, [new SchulgliederungKatalogEintrag(7001000, "Z01", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Kooperationsklasse Hauptschule (HS 2j; VZ)", BerufskollegAnlage.Z, "01", 370, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	public static VERSION : number = 1;

	public readonly daten : SchulgliederungKatalogEintrag;

	public readonly historie : Array<SchulgliederungKatalogEintrag>;

	private static readonly _schulgliederungenKuerzel : HashMap<String, Schulgliederung> = new HashMap();

	private static readonly _schulgliederungenID : HashMap<Number, Schulgliederung> = new HashMap();

	private schulformen : Array<Vector<Schulform>>;

	/**
	 * Erzeugt eine neue Schulgliederung in der Aufzählung.
	 * 
	 * @param historie   die Historie der Schulgliederung, welches ein Array von {@link SchulgliederungKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<SchulgliederungKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Schulgliederung.all_values_by_ordinal.push(this);
		Schulgliederung.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.schulformen = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++){
			this.schulformen[i] = new Vector();
			for (let kuerzel of historie[i].schulformen) {
				let sf : Schulform | null = Schulform.getByKuerzel(kuerzel);
				if (sf !== null) 
					this.schulformen[i].add(sf);
			}
		}
	}

	/**
	 * Gibt eine Map von den Kürzels der Schulgliederungen auf die zugehörigen Schulgliederungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzels der Schulgliederungen auf die zugehörigen Schulgliederungen
	 */
	private static getMapSchulgliederungByKuerzel() : HashMap<String, Schulgliederung> {
		if (Schulgliederung._schulgliederungenKuerzel.size() === 0) 
			for (let s of Schulgliederung.values()) 
				Schulgliederung._schulgliederungenKuerzel.put(s.daten.kuerzel, s);
		return Schulgliederung._schulgliederungenKuerzel;
	}

	/**
	 * Gibt eine Map von den IDs der Schulgliederungen auf die zugehörigen Schulgliederungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Schulgliederungen auf die zugehörigen Schulgliederungen
	 */
	private static getMapSchulgliederungByID() : HashMap<Number, Schulgliederung> {
		if (Schulgliederung._schulgliederungenID.size() === 0) 
			for (let s of Schulgliederung.values()) {
				for (let k of s.historie) 
					Schulgliederung._schulgliederungenID.put(k.id, s);
			}
		return Schulgliederung._schulgliederungenID;
	}

	/**
	 * Liefert die Schulgliederung anhand des übergebenen Kürzels zurück. 
	 * 
	 * @param kuerzel   das Kürzel der Schulgliederung
	 * 
	 * @return die Schulgliederung oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : Schulgliederung | null {
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel))) 
			return Schulgliederung.DEFAULT;
		return Schulgliederung.getMapSchulgliederungByKuerzel().get(kuerzel);
	}

	/**
	 * Liefert die Schulgliederung anhand der übergebenen ID zurück. 
	 * 
	 * @param id   die ID der Schulgliederung
	 * 
	 * @return die Schulgliederung oder null, falls die ID ungültig ist
	 */
	public static getByID(id : Number | null) : Schulgliederung | null {
		return Schulgliederung.getMapSchulgliederungByID().get(id);
	}

	/**
	 * Liefert alle Schulformen zurück, bei welchen die Schulgliederung vorkommt.
	 * 
	 * @return eine Liste der Schulformen
	 */
	public getSchulformen() : List<Schulform> {
		return this.schulformen[this.historie.length - 1];
	}

	/**
	 * Liefert alle zulässigen Gliederungen für die angegeben Schulform.
	 * 
	 * @param schulform   die Schulform
	 * 
	 * @return die bei der Schulform zulässigen Gliederungen
	 */
	public static get(schulform : Schulform | null) : List<Schulgliederung> {
		let result : Vector<Schulgliederung> = new Vector();
		if (schulform === null) 
			return result;
		let gliederungen : Array<Schulgliederung> = Schulgliederung.values();
		for (let i : number = 0; i < gliederungen.length; i++){
			let gliederung : Schulgliederung = gliederungen[i];
			if (gliederung.hasSchulform(schulform)) 
				result.add(gliederung);
		}
		return result;
	}

	/**
	 * Prüft anhand des Schulform-Kürzels, ob die Schulform diese Gliederung
	 * hat oder nicht.
	 * 
	 * @param kuerzel   das Kürzel der Schulform
	 * 
	 * @return true, falls die Gliederung bei der Schulform existiert und ansonsten false
	 */
	public hasSchulformByKuerzel(kuerzel : String | null) : boolean {
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel))) 
			return false;
		if (this.daten.schulformen !== null) {
			for (let i : number = 0; i < this.daten.schulformen.size(); i++){
				let sfKuerzel : String | null = this.daten.schulformen.get(i);
				if (JavaObject.equalsTranspiler(sfKuerzel, (kuerzel))) 
					return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob die Schulform diese Gliederung hat oder nicht.
	 * 
	 * @param schulform   die Schulform
	 * 
	 * @return true, falls die Gliederung bei der Schulform existiert und ansonsten false
	 */
	public hasSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null)) 
			return false;
		if (this.daten.schulformen !== null) {
			for (let i : number = 0; i < this.daten.schulformen.size(); i++){
				let sfKuerzel : String | null = this.daten.schulformen.get(i);
				if (JavaObject.equalsTranspiler(sfKuerzel, (schulform.daten.kuerzel))) 
					return true;
			}
		}
		return false;
	}

	/**
	 * Gibt zurück, ob es sich um einen 8-jährigen gymnasialen Bildungsgang
	 * handelt oder nicht.
	 *  
	 * @return true, falls es sich um einen 8-jährigen gymnasialen Bildungsgang
	 *         handelt, sonst false 
	 */
	public istG8() : boolean {
		return (this as unknown === Schulgliederung.GY8 as unknown) || (this as unknown === Schulgliederung.Y8 as unknown);
	}

	/**
	 * Gibt die Standard-Gliederung der angegebenen Schulform zurück.
	 * 
	 * @param sf        die Schulform
	 * 
	 * @return die Schulgliederung, falls die Schulform gültig ist und ansonsten null 
	 */
	public static getDefault(sf : Schulform | null) : Schulgliederung | null {
		if (sf === null) 
			return null;
		if ((sf as unknown === Schulform.GY as unknown) || (sf as unknown === Schulform.SK as unknown) || (sf as unknown === Schulform.GM as unknown) || (sf as unknown === Schulform.G as unknown) || (sf as unknown === Schulform.S as unknown) || (sf as unknown === Schulform.PS as unknown) || (sf as unknown === Schulform.V as unknown) || (sf as unknown === Schulform.FW as unknown) || (sf as unknown === Schulform.H as unknown) || (sf as unknown === Schulform.R as unknown) || (sf as unknown === Schulform.GE as unknown) || (sf as unknown === Schulform.SR as unknown) || (sf as unknown === Schulform.SG as unknown)) 
			return Schulgliederung.DEFAULT;
		return null;
	}

	/**
	 * Gibt die Gliederung der angegebenen Schulform mit dem übergegebenen Kürzel zurück.
	 * Dabei setzt sich das zu prüfende Kürzel aus Anlage und Typ zusammen.
	 * 
	 * @param sf        die Schulform
	 * @param kuerzel   Anlage und Typ als String aneinandergehängt
	 * 
	 * @return die Schulgliederung, falls die Parameter gültige Werte sind und ansonsten null 
	 */
	public static getBySchulformAndKuerzel(sf : Schulform | null, kuerzel : String | null) : Schulgliederung | null {
		if (sf === null) 
			return null;
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel))) 
			return Schulgliederung.getDefault(sf);
		let gliederungen : List<Schulgliederung> = Schulgliederung.get(sf);
		for (let i : number = 0; i < gliederungen.size(); i++){
			let sg : Schulgliederung | null = gliederungen.get(i);
			if (JavaString.equalsIgnoreCase((sg.daten.kuerzel), kuerzel)) 
				return sg;
		}
		return null;
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
		if (!(other instanceof Schulgliederung))
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
	public compareTo(other : Schulgliederung) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Schulgliederung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : Schulgliederung | null {
		let tmp : Schulgliederung | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.statkue.Schulgliederung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_statkue_Schulgliederung(obj : unknown) : Schulgliederung {
	return obj as Schulgliederung;
}
