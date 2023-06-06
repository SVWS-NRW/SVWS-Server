import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { SchulabschlussAllgemeinbildend } from '../../../core/types/schule/SchulabschlussAllgemeinbildend';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { BerufskollegAnlage } from '../../../core/types/schule/BerufskollegAnlage';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchulgliederungKatalogEintrag } from '../../../core/data/schule/SchulgliederungKatalogEintrag';
import { SchulabschlussBerufsbildend } from '../../../core/types/schule/SchulabschlussBerufsbildend';
import type { List } from '../../../java/util/List';
import { JavaString } from '../../../java/lang/JavaString';
import { Arrays } from '../../../java/util/Arrays';

export class Schulgliederung extends JavaObject implements JavaEnum<Schulgliederung> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Schulgliederung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Schulgliederung> = new Map<string, Schulgliederung>();

	/**
	 *  Schulgliederung DEFAULT:
	 *    Standard für diese Schulform
	 */
	public static readonly DEFAULT : Schulgliederung = new Schulgliederung("DEFAULT", 0, [new SchulgliederungKatalogEintrag(0, "***", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V), false, false, "Standard für diese Schulform", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung A01:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 01 (Fachklassen (BS; TZ))
	 */
	public static readonly A01 : Schulgliederung = new Schulgliederung("A01", 1, [new SchulgliederungKatalogEintrag(1001000, "A01", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen (BS; TZ)", BerufskollegAnlage.A, "01", 10, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung A02:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 02 (Fachklassen/Fachhochschulreife (BS/FHR; TZ))
	 */
	public static readonly A02 : Schulgliederung = new Schulgliederung("A02", 2, [new SchulgliederungKatalogEintrag(1002000, "A02", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen/Fachhochschulreife (BS/FHR; TZ)", BerufskollegAnlage.A, "02", 10, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung A03:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 03 (Fachklassen/erweiterte Zusatzqualifikation (BS/ZQ; TZ))
	 */
	public static readonly A03 : Schulgliederung = new Schulgliederung("A03", 3, [new SchulgliederungKatalogEintrag(1003000, "A03", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen/erweiterte Zusatzqualifikation (BS/ZQ; TZ)", BerufskollegAnlage.A, "03", 10, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung A04:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 04 (Fachklassen mit erweitertem Stützunterricht (BS/Stütz; TZ))
	 */
	public static readonly A04 : Schulgliederung = new Schulgliederung("A04", 4, [new SchulgliederungKatalogEintrag(1004000, "A04", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen mit erweitertem Stützunterricht (BS/Stütz; TZ)", BerufskollegAnlage.A, "04", 10, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung A05 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 05 (Berufsorientierungsjahr (BV; VZ))
	 */
	public static readonly A05 : Schulgliederung = new Schulgliederung("A05", 5, [new SchulgliederungKatalogEintrag(1005000, "A05", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsorientierungsjahr (BV; VZ)", BerufskollegAnlage.A, "05", 20, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BV), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung A06 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 06 (Berufsgrundschuljahr (BG; VZ))
	 */
	public static readonly A06 : Schulgliederung = new Schulgliederung("A06", 6, [new SchulgliederungKatalogEintrag(1006000, "A06", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsgrundschuljahr (BG; VZ)", BerufskollegAnlage.A, "06", 30, true, Arrays.asList(SchulabschlussBerufsbildend.BG), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung A07 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 07 (Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; TZ) bzw. Werkstattjahr (BS 1j;TZ))
	 */
	public static readonly A07 : Schulgliederung = new Schulgliederung("A07", 7, [new SchulgliederungKatalogEintrag(1007000, "A07", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; TZ) bzw. Werkstattjahr (BS 1j;TZ)", BerufskollegAnlage.A, "07", 40, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung A08 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 08 (Vorpraktikum (VP))
	 */
	public static readonly A08 : Schulgliederung = new Schulgliederung("A08", 8, [new SchulgliederungKatalogEintrag(1008000, "A08", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Vorpraktikum (VP)", BerufskollegAnlage.A, "08", 50, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung A09 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 09 (Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; VZ))
	 */
	public static readonly A09 : Schulgliederung = new Schulgliederung("A09", 9, [new SchulgliederungKatalogEintrag(1009000, "A09", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; VZ)", BerufskollegAnlage.A, "09", 55, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BS), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung A10 (auslaufend):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 10 (Berufsabschluss/mittlerer Schulabschluss (BKAZVO) (BAB/FOR; VZ))
	 */
	public static readonly A10 : Schulgliederung = new Schulgliederung("A10", 10, [new SchulgliederungKatalogEintrag(1010000, "A10", true, Arrays.asList(Schulform.BK, Schulform.SB), true, false, "Berufsabschluss/Mittlerer Schulabschluss (BKAZVO) (BAB/FOR; VZ)", BerufskollegAnlage.A, "10", 56, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.MSA), null, null)]);

	/**
	 *  Schulgliederung A11 (auslaufend):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 11 (Berufsabschluss/Fachhochschulreife (BKAZVO) (BAB/FHR; VZ))
	 */
	public static readonly A11 : Schulgliederung = new Schulgliederung("A11", 11, [new SchulgliederungKatalogEintrag(1011000, "A11", true, Arrays.asList(Schulform.BK, Schulform.SB), true, false, "Berufsabschluss/Fachhochschulreife (BKAZVO) (BAB/FHR; VZ)", BerufskollegAnlage.A, "11", 56, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung A12:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 12 (Ausbildungsvorbereitung (BS 1j; VZ))
	 */
	public static readonly A12 : Schulgliederung = new Schulgliederung("A12", 12, [new SchulgliederungKatalogEintrag(1012000, "A12", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Ausbildungsvorbereitung (BS 1j; VZ)", BerufskollegAnlage.A, "12", 57, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VORB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A, SchulabschlussAllgemeinbildend.HA9_FOE), null, null)]);

	/**
	 *  Schulgliederung A13:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 13 (Ausbildungsvorbereitung (BS 1j; TZ))
	 */
	public static readonly A13 : Schulgliederung = new Schulgliederung("A13", 13, [new SchulgliederungKatalogEintrag(1013000, "A13", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Ausbildungsvorbereitung (BS 1j; TZ)", BerufskollegAnlage.A, "13", 58, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VORB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA9A), null, null)]);

	/**
	 *  Schulgliederung A14:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 14 (Berufsabschluss (nach §50 BBiG/§40 HwO)/mittlerer Schulabschluss (BAB/FOR; VZ))
	 */
	public static readonly A14 : Schulgliederung = new Schulgliederung("A14", 14, [new SchulgliederungKatalogEintrag(1014000, "A14", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss (nach §50 BBiG/§40 HwO)/Mittlerer Schulabschluss (BAB/FOR; VZ)", BerufskollegAnlage.A, "14", 59, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung A15:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 15 (Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife BAB/FHR; VZ)
	 */
	public static readonly A15 : Schulgliederung = new Schulgliederung("A15", 15, [new SchulgliederungKatalogEintrag(1015000, "A15", true, Arrays.asList(Schulform.BK), false, false, "Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife (BAB/FHR; VZ)", BerufskollegAnlage.A, "15", 59, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung A16:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 16 (Fachklassen (nach §2 BKAZVO) BAB; VZ)
	 */
	public static readonly A16 : Schulgliederung = new Schulgliederung("A16", 16, [new SchulgliederungKatalogEintrag(1016000, "A16", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachklassen (nach §2 BKAZVO) BAB; VZ", BerufskollegAnlage.A, "16", 59, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung AB:
	 *    Schule für Kranke: Allgemeinbildend
	 */
	public static readonly AB : Schulgliederung = new Schulgliederung("AB", 17, [new SchulgliederungKatalogEintrag(90001000, "AB", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.KS, Schulform.S), false, false, "Schule für Kranke: Allgemeinbildend", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung B01 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 01 (Berufsabschluss/Fachoberschulreife (BAB/FOR 2j; VZ))
	 */
	public static readonly B01 : Schulgliederung = new Schulgliederung("B01", 18, [new SchulgliederungKatalogEintrag(2001000, "B01", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss/Fachoberschulreife (BAB/FOR 2j; VZ)", BerufskollegAnlage.B, "01", 60, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung B02 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 02 (Berufsgrundbildung/Fachoberschulreife (BG/FOR 2j; VZ))
	 */
	public static readonly B02 : Schulgliederung = new Schulgliederung("B02", 19, [new SchulgliederungKatalogEintrag(2002000, "B02", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsgrundbildung/Fachoberschulreife (BG/FOR 2j; VZ)", BerufskollegAnlage.B, "02", 70, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BG), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung B03 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 03 (Berufsgrundbildung (für Schüler mit FOR) (BG 1j; VZ))
	 */
	public static readonly B03 : Schulgliederung = new Schulgliederung("B03", 20, [new SchulgliederungKatalogEintrag(2003000, "B03", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsgrundbildung (für Schüler mit FOR) (BG 1j; VZ)", BerufskollegAnlage.B, "03", 80, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BG), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung B04 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 04 (Berufsabschluss/Fachoberschulreife (BAB/FOR; VZ))
	 */
	public static readonly B04 : Schulgliederung = new Schulgliederung("B04", 21, [new SchulgliederungKatalogEintrag(2004000, "B04", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss/Fachoberschulreife (BAB/FOR; VZ)", BerufskollegAnlage.B, "04", 90, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung B05 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 05 (Berufsabschluss/Fachhochschulreife (BAB/FHR; VZ))
	 */
	public static readonly B05 : Schulgliederung = new Schulgliederung("B05", 22, [new SchulgliederungKatalogEintrag(2005000, "B05", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufsabschluss/Fachhochschulreife (BAB/FHR; VZ)", BerufskollegAnlage.B, "05", 90, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung B06:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 06 (Berufliche Kenntnisse/Hauptschulabschluss Kl. 10 (BK/HSA10; 1j. VZ))
	 */
	public static readonly B06 : Schulgliederung = new Schulgliederung("B06", 23, [new SchulgliederungKatalogEintrag(2006000, "B06", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufliche Kenntnisse/Hauptschulabschluss Kl. 10 (BK/HSA10; 1j. VZ)", BerufskollegAnlage.B, "06", 91, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10), null, 2021), new SchulgliederungKatalogEintrag(2006001, "B06", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufliche Kenntnisse/Erweiterter Erster Schulabschluss (BK/EESA; 1j. VZ)", BerufskollegAnlage.B, "06", 91, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10), 2022, null)]);

	/**
	 *  Schulgliederung B07:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 07 (Berufliche Kenntnisse/mittlerer Schulabschluss (BK/FOR; 1j. VZ))
	 */
	public static readonly B07 : Schulgliederung = new Schulgliederung("B07", 24, [new SchulgliederungKatalogEintrag(2007000, "B07", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufliche Kenntnisse/Mittlerer Schulabschluss (BK/FOR; 1j. VZ)", BerufskollegAnlage.B, "07", 92, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung B08:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 08 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 2j. VZ))
	 */
	public static readonly B08 : Schulgliederung = new Schulgliederung("B08", 25, [new SchulgliederungKatalogEintrag(2008000, "B08", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 2j. VZ)", BerufskollegAnlage.B, "08", 93, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, 2021), new SchulgliederungKatalogEintrag(2008001, "B08", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschl./Erweiterter Erster Schulabschluss oder Mittl. Schulab. (BAB/EESA-FOR, 2j. VZ)", BerufskollegAnlage.B, "08", 93, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), 2022, null)]);

	/**
	 *  Schulgliederung B09:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 09 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 3j. TZ))
	 */
	public static readonly B09 : Schulgliederung = new Schulgliederung("B09", 26, [new SchulgliederungKatalogEintrag(2009000, "B09", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 3j. TZ)", BerufskollegAnlage.B, "09", 93, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, 2021), new SchulgliederungKatalogEintrag(2009001, "B09", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschl./Erweiterter Erster Schulabschluss oder Mittl. Schulab. (BAB/EESA-FOR, 3j. TZ)", BerufskollegAnlage.B, "09", 93, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), 2022, null)]);

	/**
	 *  Schulgliederung B10:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 10 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 4j. TZ))
	 */
	public static readonly B10 : Schulgliederung = new Schulgliederung("B10", 27, [new SchulgliederungKatalogEintrag(2010000, "B10", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 4j. TZ)", BerufskollegAnlage.B, "10", 93, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null), new SchulgliederungKatalogEintrag(2010001, "B10", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschl./Erweiterter Erster Schulabschluss oder Mittl. Schulab. (BAB/EESA-FOR, 4j. TZ)", BerufskollegAnlage.B, "10", 93, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung BT:
	 *    Schule für Kranke: Berufsbildend (Teilzeit)
	 */
	public static readonly BT : Schulgliederung = new Schulgliederung("BT", 28, [new SchulgliederungKatalogEintrag(90002000, "BT", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.KS, Schulform.S), false, false, "Schule für Kranke: Berufsbildend (Teilzeit)", null, null, null, false, Arrays.asList(), Arrays.asList(), null, null)]);

	/**
	 *  Schulgliederung BV:
	 *    Schule für Kranke: Berufsbildend (Vollzeit)
	 */
	public static readonly BV : Schulgliederung = new Schulgliederung("BV", 29, [new SchulgliederungKatalogEintrag(90003000, "BV", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.KS, Schulform.S), false, false, "Schule für Kranke: Berufsbildend (Vollzeit)", null, null, null, true, Arrays.asList(), Arrays.asList(), null, null)]);

	/**
	 *  Schulgliederung C01:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 01 (Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum) BAB/FHR 3j; VZ BFS)
	 */
	public static readonly C01 : Schulgliederung = new Schulgliederung("C01", 30, [new SchulgliederungKatalogEintrag(3001000, "C01", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum) BAB/FHR 3j; VZ BFS", BerufskollegAnlage.C, "01", 100, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung C02:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 02 (Berufsabschluss f. Hochschulzugangsberechtigte (BAB 2j; VZ) BFS)
	 */
	public static readonly C02 : Schulgliederung = new Schulgliederung("C02", 31, [new SchulgliederungKatalogEintrag(3002000, "C02", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss f. Hochschulzugangsberechtigte (BAB 2j; VZ) BFS", BerufskollegAnlage.C, "02", 110, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung C03:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 03 (Berufliche Kenntnisse/FHR (BK/FHR 2j; VZ) HBFS)
	 */
	public static readonly C03 : Schulgliederung = new Schulgliederung("C03", 32, [new SchulgliederungKatalogEintrag(3003000, "C03", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufliche Kenntnisse/FHR (BK/FHR 2j; VZ) HBFS", BerufskollegAnlage.C, "03", 120, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q), null, null)]);

	/**
	 *  Schulgliederung C04 (ausgelaufen):
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 04 (Berufliche Kenntnisse/Sonderform für Abiturienten (BK 1j; VZ) HBFS)
	 */
	public static readonly C04 : Schulgliederung = new Schulgliederung("C04", 33, [new SchulgliederungKatalogEintrag(3004000, "C04", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufliche Kenntnisse/Sonderform für Abiturienten (BK 1j; VZ) HBFS", BerufskollegAnlage.C, "04", 130, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung C05:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 05 (Fachoberschule Kl. 11 (BK/FHR 1j; TZ))
	 */
	public static readonly C05 : Schulgliederung = new Schulgliederung("C05", 34, [new SchulgliederungKatalogEintrag(3005000, "C05", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachoberschule Kl. 11 (BK/FHR 1j; TZ)", BerufskollegAnlage.C, "05", 140, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VERS), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.VS_11), null, null)]);

	/**
	 *  Schulgliederung C06:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 06 (Fachoberschule Kl. 12S (BK/FHR 1j; VZ))
	 */
	public static readonly C06 : Schulgliederung = new Schulgliederung("C06", 35, [new SchulgliederungKatalogEintrag(3006000, "C06", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachoberschule Kl. 12S (BK/FHR 1j; VZ)", BerufskollegAnlage.C, "06", 140, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung C07:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 07 (Fachoberschule Kl. 12B (BK/FHR 2j; TZ))
	 */
	public static readonly C07 : Schulgliederung = new Schulgliederung("C07", 36, [new SchulgliederungKatalogEintrag(3007000, "C07", true, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.WB), false, false, "Fachoberschule Kl. 12B (BK/FHR 2j; TZ)", BerufskollegAnlage.C, "07", 140, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung C08:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 08 (Fachoberschule Kl. 12B (BK/FHR 1j; VZ))
	 */
	public static readonly C08 : Schulgliederung = new Schulgliederung("C08", 37, [new SchulgliederungKatalogEintrag(3008000, "C08", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachoberschule Kl. 12B (BK/FHR 1j; VZ)", BerufskollegAnlage.C, "08", 140, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung C09 (ausgelaufen):
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 09 (Berufspraktikum Erzieher/innen (Vollzeit) (BP/Erz 1j; VZ))
	 */
	public static readonly C09 : Schulgliederung = new Schulgliederung("C09", 38, [new SchulgliederungKatalogEintrag(3009000, "C09", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (Vollzeit) (BP/Erz 1j; VZ)", BerufskollegAnlage.C, "09", 150, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung C10 (ausgelaufen):
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 10 (Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ))
	 */
	public static readonly C10 : Schulgliederung = new Schulgliederung("C10", 39, [new SchulgliederungKatalogEintrag(3010000, "C10", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ)", BerufskollegAnlage.C, "10", 150, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung C11:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 11 (Fachoberschule Kl. 12B (BK/FHR 3j; TZ))
	 */
	public static readonly C11 : Schulgliederung = new Schulgliederung("C11", 40, [new SchulgliederungKatalogEintrag(3011000, "C11", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachoberschule Kl. 12B (BK/FHR 3j; TZ)", BerufskollegAnlage.C, "11", 141, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung C12:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 12 (Berufsabschluss/Fachhochschulreife (mit  Berufspraktikum) BAB/FHR 3,5j; VZ)
	 */
	public static readonly C12 : Schulgliederung = new Schulgliederung("C12", 41, [new SchulgliederungKatalogEintrag(3012000, "C12", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss/Fachhochschulreife (mit  Berufspraktikum) BAB/FHR 3,5j; VZ", BerufskollegAnlage.C, "12", 145, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung C13:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 13 (Berufsabschluss/Fachhochschulreife (gestuft) (BAB/FHR 3j; VZ))
	 */
	public static readonly C13 : Schulgliederung = new Schulgliederung("C13", 42, [new SchulgliederungKatalogEintrag(3013000, "C13", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss/Fachhochschulreife (gestuft) (BAB/FHR 3j; VZ)", BerufskollegAnlage.C, "13", 146, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung D01:
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 01 (Berufsabschluss/Allg. Hochschulreife (mit Berufspraktikum) (BAB/AHR 4j; VZ))
	 */
	public static readonly D01 : Schulgliederung = new Schulgliederung("D01", 43, [new SchulgliederungKatalogEintrag(4001000, "D01", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufsabschluss/Allg. Hochschulreife (mit Berufspraktikum) (BAB/AHR 4j; VZ)", BerufskollegAnlage.D, "01", 160, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK, SchulabschlussBerufsbildend.BAB), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.FHR, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	/**
	 *  Schulgliederung D02:
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 02 (Berufl. Kenntnisse/Allg. Hochschulreife (BK/AHR 3j; VZ))
	 */
	public static readonly D02 : Schulgliederung = new Schulgliederung("D02", 44, [new SchulgliederungKatalogEintrag(4002000, "D02", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Berufl. Kenntnisse/Allg. Hochschulreife (BK/AHR 3j; VZ)", BerufskollegAnlage.D, "02", 170, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BK), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.HA10, SchulabschlussAllgemeinbildend.MSA_Q, SchulabschlussAllgemeinbildend.FHR_S, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	/**
	 *  Schulgliederung D03 (ausgelaufen):
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 03 (Berufspraktikum (Vollzeit) (BP 1j; VZ))
	 */
	public static readonly D03 : Schulgliederung = new Schulgliederung("D03", 45, [new SchulgliederungKatalogEintrag(4003000, "D03", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum (Vollzeit) (BP 1j; VZ)", BerufskollegAnlage.D, "03", 180, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung D04 (ausgelaufen):
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 04 (Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ))
	 */
	public static readonly D04 : Schulgliederung = new Schulgliederung("D04", 46, [new SchulgliederungKatalogEintrag(4004000, "D04", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ)", BerufskollegAnlage.D, "04", 190, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung D05:
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 05 (AHR (gem. § 2 Abs. 3 Anlage D) (AHR 1j; VZ) FOS13)
	 */
	public static readonly D05 : Schulgliederung = new Schulgliederung("D05", 47, [new SchulgliederungKatalogEintrag(4005000, "D05", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "AHR (gem. § 2 Abs. 3 Anlage D) (AHR 1j; VZ) FOS13", BerufskollegAnlage.D, "05", 200, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.FGHR, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	/**
	 *  Schulgliederung D06:
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 06 (AHR (gem. § 2 Abs. 3 Anlage D) (AHR 2j; TZ) FOS13)
	 */
	public static readonly D06 : Schulgliederung = new Schulgliederung("D06", 48, [new SchulgliederungKatalogEintrag(4006000, "D06", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "AHR (gem. § 2 Abs. 3 Anlage D) (AHR 2j; TZ) FOS13", BerufskollegAnlage.D, "06", 200, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.VBK), Arrays.asList(SchulabschlussAllgemeinbildend.FGHR, SchulabschlussAllgemeinbildend.ABITUR), null, null)]);

	/**
	 *  Schulgliederung E01:
	 *    Anlage E (Fachschule),
	 *    Typ 01 (Fachschule Vollzeit (BW 2j; VZ))
	 */
	public static readonly E01 : Schulgliederung = new Schulgliederung("E01", 49, [new SchulgliederungKatalogEintrag(5001000, "E01", true, Arrays.asList(Schulform.BK), false, false, "Fachschule Vollzeit (BW 2j; VZ)", BerufskollegAnlage.E, "01", 210, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung E02:
	 *    Anlage E (Fachschule),
	 *    Typ 02 (Fachschule Teilzeit (BW 4j; TZ))
	 */
	public static readonly E02 : Schulgliederung = new Schulgliederung("E02", 50, [new SchulgliederungKatalogEintrag(5002000, "E02", true, Arrays.asList(Schulform.BK, Schulform.SB), false, false, "Fachschule Teilzeit (BW 4j; TZ)", BerufskollegAnlage.E, "02", 210, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung E03:
	 *    Anlage E (Fachschule),
	 *    Typ 03 (Fachschule (verkürzt) Vollzeit (BW 1j; VZ))
	 */
	public static readonly E03 : Schulgliederung = new Schulgliederung("E03", 51, [new SchulgliederungKatalogEintrag(5003000, "E03", true, Arrays.asList(Schulform.BK), false, false, "Fachschule (verkürzt) Vollzeit (BW 1j; VZ)", BerufskollegAnlage.E, "03", 220, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.AUFB, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung E04:
	 *    Anlage E (Fachschule),
	 *    Typ 04 (Fachschule (verkürzt) Teilzeit (BW 2j; TZ))
	 */
	public static readonly E04 : Schulgliederung = new Schulgliederung("E04", 52, [new SchulgliederungKatalogEintrag(5004000, "E04", true, Arrays.asList(Schulform.BK), false, false, "Fachschule (verkürzt) Teilzeit (BW 2j; TZ)", BerufskollegAnlage.E, "04", 220, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.AUFB, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung E05:
	 *    Anlage E (Fachschule),
	 *    Typ 05 (Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 3j; VZ))
	 */
	public static readonly E05 : Schulgliederung = new Schulgliederung("E05", 53, [new SchulgliederungKatalogEintrag(5005000, "E05", true, Arrays.asList(Schulform.BK), false, false, "Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 3j; VZ)", BerufskollegAnlage.E, "05", 230, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung E06 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 06 (Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 1j; VZ))
	 */
	public static readonly E06 : Schulgliederung = new Schulgliederung("E06", 54, [new SchulgliederungKatalogEintrag(5006000, "E06", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 1j; VZ)", BerufskollegAnlage.E, "06", 240, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung E07:
	 *    Anlage E (Fachschule),
	 *    Typ 07 (Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 6j; TZ))
	 */
	public static readonly E07 : Schulgliederung = new Schulgliederung("E07", 55, [new SchulgliederungKatalogEintrag(5007000, "E07", true, Arrays.asList(Schulform.BK), false, false, "Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 6j; TZ)", BerufskollegAnlage.E, "07", 230, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung E08 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 08 (Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 2j; TZ))
	 */
	public static readonly E08 : Schulgliederung = new Schulgliederung("E08", 56, [new SchulgliederungKatalogEintrag(5008000, "E08", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 2j; TZ)", BerufskollegAnlage.E, "08", 240, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung E09 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 09 (Fachschule (Sonderform) Vollzeit (BW 3j; VZ))
	 */
	public static readonly E09 : Schulgliederung = new Schulgliederung("E09", 57, [new SchulgliederungKatalogEintrag(5009000, "E09", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachschule (Sonderform) Vollzeit (BW 3j; VZ)", BerufskollegAnlage.E, "09", 250, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung E10 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 10 (Fachschule (Sonderform) Teilzeit (BW 6j; TZ))
	 */
	public static readonly E10 : Schulgliederung = new Schulgliederung("E10", 58, [new SchulgliederungKatalogEintrag(5010000, "E10", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Fachschule (Sonderform) Teilzeit (BW 6j; TZ)", BerufskollegAnlage.E, "10", 260, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung E11 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 11 (Berufspraktikum Erzieher/innen (FS/BP/Erz 1j; VZ))
	 */
	public static readonly E11 : Schulgliederung = new Schulgliederung("E11", 59, [new SchulgliederungKatalogEintrag(5011000, "E11", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (FS/BP/Erz 1j; VZ)", BerufskollegAnlage.E, "11", 270, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung E12 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 12 (Berufspraktikum Erzieher/innen (FS/BP/Erz 2j; TZ))
	 */
	public static readonly E12 : Schulgliederung = new Schulgliederung("E12", 60, [new SchulgliederungKatalogEintrag(5012000, "E12", true, Arrays.asList(Schulform.BK, Schulform.SB), true, true, "Berufspraktikum Erzieher/innen (FS/BP/Erz 2j; TZ)", BerufskollegAnlage.E, "12", 270, false, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BP), Arrays.asList(SchulabschlussAllgemeinbildend.OA), null, null)]);

	/**
	 *  Schulgliederung E13:
	 *    Anlage E (Fachschule),
	 *    Typ 13 (Fachschule Teilzeit (BW 3j; TZ))
	 */
	public static readonly E13 : Schulgliederung = new Schulgliederung("E13", 61, [new SchulgliederungKatalogEintrag(5013000, "E13", true, Arrays.asList(Schulform.BK), false, false, "Fachschule Teilzeit (BW 3j; TZ)", BerufskollegAnlage.E, "13", 210, true, Arrays.asList(SchulabschlussBerufsbildend.OA, SchulabschlussBerufsbildend.WECHSEL, SchulabschlussBerufsbildend.BW), Arrays.asList(SchulabschlussAllgemeinbildend.OA, SchulabschlussAllgemeinbildend.MSA, SchulabschlussAllgemeinbildend.FHR), null, null)]);

	/**
	 *  Schulgliederung ER:
	 *    kooperative Form: Erweiterungsebene
	 */
	public static readonly ER : Schulgliederung = new Schulgliederung("ER", 62, [new SchulgliederungKatalogEintrag(14001000, "ER", false, Arrays.asList(Schulform.SK), false, false, "kooperative Form: Erweiterungsebene", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung EVB:
	 *    Evangelische Bekenntnisschule
	 */
	public static readonly EVB : Schulgliederung = new Schulgliederung("EVB", 63, [new SchulgliederungKatalogEintrag(80001000, "EVB", false, Arrays.asList(Schulform.G), false, false, "Evangelische Bekenntnisschule", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung G01:
	 *    Aufbaugymnasium
	 */
	public static readonly G01 : Schulgliederung = new Schulgliederung("G01", 64, [new SchulgliederungKatalogEintrag(60001000, "G01", false, Arrays.asList(Schulform.GY), false, false, "Aufbaugymnasium", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung G02:
	 *    Bildungsgang Abendgymnasium
	 */
	public static readonly G02 : Schulgliederung = new Schulgliederung("G02", 65, [new SchulgliederungKatalogEintrag(18001000, "G02", false, Arrays.asList(Schulform.WB), false, false, "Bildungsgang Abendgymnasium", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung GGS (auslaufend):
	 *    Gemeinschaftsschule (auslaufend) integrierte Form
	 */
	public static readonly GGS : Schulgliederung = new Schulgliederung("GGS", 66, [new SchulgliederungKatalogEintrag(15001000, "GGS", false, Arrays.asList(Schulform.GE), true, false, "Gemeinschaftsschule (auslaufend) integrierte Form", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung GGY (auslaufend):
	 *    Gemeinschaftsschule (auslaufend) Gymnasialbildungsgang
	 */
	public static readonly GGY : Schulgliederung = new Schulgliederung("GGY", 67, [new SchulgliederungKatalogEintrag(15002000, "GGY", false, Arrays.asList(Schulform.GE), true, false, "Gemeinschaftsschule (auslaufend) Gymnasialbildungsgang", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung GMS:
	 *    Gemeinschaftsschule
	 */
	public static readonly GMS : Schulgliederung = new Schulgliederung("GMS", 68, [new SchulgliederungKatalogEintrag(80002000, "GMS", false, Arrays.asList(Schulform.G), false, false, "Gemeinschaftsschule", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung GR:
	 *    kooperative Form: Grundebene
	 */
	public static readonly GR : Schulgliederung = new Schulgliederung("GR", 69, [new SchulgliederungKatalogEintrag(15003000, "GR", false, Arrays.asList(Schulform.GE, Schulform.SK), false, false, "kooperative Form: Grundebene", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung GRH (auslaufend):
	 *    Gemeinschaftsschule auslaufend: teilintegrierte Form
	 */
	public static readonly GRH : Schulgliederung = new Schulgliederung("GRH", 70, [new SchulgliederungKatalogEintrag(15004000, "GRH", false, Arrays.asList(Schulform.GE), true, false, "Gemeinschaftsschule auslaufend: teilintegrierte Form", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung GS:
	 *    integrierte Form (Binnendifferenzierung)
	 */
	public static readonly GS : Schulgliederung = new Schulgliederung("GS", 71, [new SchulgliederungKatalogEintrag(50001000, "GS", false, Arrays.asList(Schulform.GM), false, false, "integrierte Form (Binnendifferenzierung)", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung GY:
	 *    Bildungsgang Gymnasium
	 */
	public static readonly GY : Schulgliederung = new Schulgliederung("GY", 72, [new SchulgliederungKatalogEintrag(15005000, "GY", false, Arrays.asList(Schulform.GE, Schulform.GM, Schulform.SK), false, false, "Bildungsgang Gymnasium", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung GY8:
	 *    Bildungsgang G8-Gymnasium
	 */
	public static readonly GY8 : Schulgliederung = new Schulgliederung("GY8", 73, [new SchulgliederungKatalogEintrag(60002000, "GY8", false, Arrays.asList(Schulform.GE, Schulform.GY), false, false, "Bildungsgang G8-Gymnasium", null, null, null, false, null, null, null, 2022), new SchulgliederungKatalogEintrag(60002001, "GY8", false, Arrays.asList(Schulform.GY), false, false, "Bildungsgang G8-Gymnasium", null, null, null, false, null, null, 2023, null)]);

	/**
	 *  Schulgliederung GY9:
	 *    Bildungsgang G9-Gymnasium
	 */
	public static readonly GY9 : Schulgliederung = new Schulgliederung("GY9", 74, [new SchulgliederungKatalogEintrag(60003000, "GY9", false, Arrays.asList(Schulform.GE, Schulform.GY), false, false, "Bildungsgang G9-Gymnasium", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung H:
	 *    Bildungsgang Hauptschule
	 */
	public static readonly H : Schulgliederung = new Schulgliederung("H", 75, [new SchulgliederungKatalogEintrag(10001000, "H", false, Arrays.asList(Schulform.GM, Schulform.R, Schulform.SK), false, false, "Bildungsgang Hauptschule", null, "  ", null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung H01:
	 *    Berufsgrundbildung (Jahrgang 07 bis 10)
	 */
	public static readonly H01 : Schulgliederung = new Schulgliederung("H01", 76, [new SchulgliederungKatalogEintrag(17001000, "H01", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF), false, false, "Berufsgrundbildung (Jahrgang 07 bis 10)", BerufskollegAnlage.H, "01", 980, false, null, null, null, null)]);

	/**
	 *  Schulgliederung H02:
	 *    Berufsausbildung (Jahrgang 11 und 12)
	 */
	public static readonly H02 : Schulgliederung = new Schulgliederung("H02", 77, [new SchulgliederungKatalogEintrag(17002000, "H02", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF), false, false, "Berufsausbildung (Jahrgang 11 und 12)", BerufskollegAnlage.H, "02", 940, false, null, null, null, null)]);

	/**
	 *  Schulgliederung K02:
	 *    Bildungsgang Kolleg
	 */
	public static readonly K02 : Schulgliederung = new Schulgliederung("K02", 78, [new SchulgliederungKatalogEintrag(18002000, "K02", false, Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.WB), false, false, "Bildungsgang Kolleg", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung R:
	 *    Bildungsgang Realschule
	 */
	public static readonly R : Schulgliederung = new Schulgliederung("R", 79, [new SchulgliederungKatalogEintrag(10002000, "R", false, Arrays.asList(Schulform.GM, Schulform.H, Schulform.SK), false, false, "Bildungsgang Realschule", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung R00:
	 *    Realschule
	 */
	public static readonly R00 : Schulgliederung = new Schulgliederung("R00", 80, [new SchulgliederungKatalogEintrag(10003000, "R00", false, Arrays.asList(Schulform.KS, Schulform.S), false, false, "Realschule", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung R01:
	 *    Aufbaurealschule
	 */
	public static readonly R01 : Schulgliederung = new Schulgliederung("R01", 81, [new SchulgliederungKatalogEintrag(10004000, "R01", false, Arrays.asList(Schulform.R), false, false, "Aufbaurealschule", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung R02:
	 *    Bildungsgang Abendrealschule
	 */
	public static readonly R02 : Schulgliederung = new Schulgliederung("R02", 82, [new SchulgliederungKatalogEintrag(18003000, "R02", false, Arrays.asList(Schulform.WB), false, false, "Bildungsgang Abendrealschule", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung RH:
	 *    teilintegrierte Form
	 */
	public static readonly RH : Schulgliederung = new Schulgliederung("RH", 83, [new SchulgliederungKatalogEintrag(10005000, "RH", false, Arrays.asList(Schulform.GE, Schulform.GM, Schulform.SK), false, false, "teilintegrierte Form", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung RKB:
	 *    Katholische  Bekenntnisschule
	 */
	public static readonly RKB : Schulgliederung = new Schulgliederung("RKB", 84, [new SchulgliederungKatalogEintrag(80003000, "RKB", false, Arrays.asList(Schulform.G), false, false, "Katholische  Bekenntnisschule", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung SRH (auslaufend):
	 *    Sekundarschule, teilintegrierte Form (auslaufend)
	 */
	public static readonly SRH : Schulgliederung = new Schulgliederung("SRH", 85, [new SchulgliederungKatalogEintrag(10006000, "SRH", false, Arrays.asList(Schulform.GE), true, false, "Sekundarschule, teilintegrierte Form (auslaufend)", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung SSI (auslaufend):
	 *    Sekundarschule, integrierte Form (auslaufend)
	 */
	public static readonly SSI : Schulgliederung = new Schulgliederung("SSI", 86, [new SchulgliederungKatalogEintrag(10007000, "SSI", false, Arrays.asList(Schulform.GE), true, false, "Sekundarschule, integrierte Form (auslaufend)", null, null, null, false, null, null, null, null)]);

	/**
	 *  Schulgliederung Y8:
	 *    Lehrplan G8
	 */
	public static readonly Y8 : Schulgliederung = new Schulgliederung("Y8", 87, [new SchulgliederungKatalogEintrag(10008000, "Y8", false, Arrays.asList(Schulform.GE), false, false, "Lehrplan G8", null, null, null, false, null, null, null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 2;

	/**
	 * Der aktuellen Daten der Schulgliederung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : SchulgliederungKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Schulgliederungen
	 */
	public readonly historie : Array<SchulgliederungKatalogEintrag>;

	/**
	 * Eine Map mit der Zuordnung der Schulgliederung zu dem Kürzel der Schulgliederung
	 */
	private static readonly _schulgliederungenKuerzel : HashMap<string, Schulgliederung> = new HashMap();

	/**
	 * Eine Map mit der Zuordnung der Schulgliederung zu der ID der Schulgliederung
	 */
	private static readonly _schulgliederungenID : HashMap<number, Schulgliederung> = new HashMap();

	/**
	 * Die Schulformen, bei welchen die Schulgliederung vorkommt
	 */
	private schulformen : Array<ArrayList<Schulform>>;

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
		for (let i : number = 0; i < historie.length; i++) {
			this.schulformen[i] = new ArrayList();
			for (const kuerzel of historie[i].schulformen) {
				const sf : Schulform | null = Schulform.getByKuerzel(kuerzel);
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
	private static getMapSchulgliederungByKuerzel() : HashMap<string, Schulgliederung> {
		if (Schulgliederung._schulgliederungenKuerzel.size() === 0)
			for (const s of Schulgliederung.values())
				Schulgliederung._schulgliederungenKuerzel.put(s.daten.kuerzel, s);
		return Schulgliederung._schulgliederungenKuerzel;
	}

	/**
	 * Gibt eine Map von den IDs der Schulgliederungen auf die zugehörigen Schulgliederungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den IDs der Schulgliederungen auf die zugehörigen Schulgliederungen
	 */
	private static getMapSchulgliederungByID() : HashMap<number, Schulgliederung> {
		if (Schulgliederung._schulgliederungenID.size() === 0)
			for (const s of Schulgliederung.values()) {
				for (const k of s.historie)
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
	public static getByKuerzel(kuerzel : string | null) : Schulgliederung | null {
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
	public static getByID(id : number | null) : Schulgliederung | null {
		return Schulgliederung.getMapSchulgliederungByID().get(id);
	}

	/**
	 * Gibt alle Schulgliderungen zurück, die zu dem angebenen
	 * Fachklassen-Index am Berufskolleg gehören.
	 *
	 * @param index   der Fachklassen-Index
	 *
	 * @return die zugehörigen Schulgliederungen
	 */
	public static getByBkIndex(index : number) : List<Schulgliederung> {
		const result : ArrayList<Schulgliederung> = new ArrayList();
		const gliederungen : Array<Schulgliederung> = Schulgliederung.values();
		for (let i : number = 0; i < gliederungen.length; i++) {
			const gliederung : Schulgliederung = gliederungen[i];
			if ((gliederung.daten.bkIndex !== null) && (gliederung.daten.bkIndex === index))
				result.add(gliederung);
		}
		return result;
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
		const result : ArrayList<Schulgliederung> = new ArrayList();
		if (schulform === null)
			return result;
		const gliederungen : Array<Schulgliederung> = Schulgliederung.values();
		for (let i : number = 0; i < gliederungen.length; i++) {
			const gliederung : Schulgliederung = gliederungen[i];
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
	public hasSchulformByKuerzel(kuerzel : string | null) : boolean {
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel)))
			return false;
		if (this.daten.schulformen !== null) {
			for (let i : number = 0; i < this.daten.schulformen.size(); i++) {
				const sfKuerzel : string | null = this.daten.schulformen.get(i);
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
			for (let i : number = 0; i < this.daten.schulformen.size(); i++) {
				const sfKuerzel : string | null = this.daten.schulformen.get(i);
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
	public static getBySchulformAndKuerzel(sf : Schulform | null, kuerzel : string | null) : Schulgliederung | null {
		if (sf === null)
			return null;
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel)))
			return Schulgliederung.getDefault(sf);
		const gliederungen : List<Schulgliederung> = Schulgliederung.get(sf);
		for (let i : number = 0; i < gliederungen.size(); i++) {
			const sg : Schulgliederung | null = gliederungen.get(i);
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
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
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
	public static valueOf(name : string) : Schulgliederung | null {
		const tmp : Schulgliederung | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.Schulgliederung', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_Schulgliederung(obj : unknown) : Schulgliederung {
	return obj as Schulgliederung;
}
