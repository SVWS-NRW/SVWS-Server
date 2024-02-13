import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { JahrgangsKatalogEintrag } from '../../../core/data/jahrgang/JahrgangsKatalogEintrag';
import { Arrays } from '../../../java/util/Arrays';
import { JahrgangsKatalogEintragBezeichnung } from '../../../core/data/jahrgang/JahrgangsKatalogEintragBezeichnung';

export class Jahrgaenge extends JavaEnum<Jahrgaenge> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Jahrgaenge> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Jahrgaenge> = new Map<string, Jahrgaenge>();

	/**
	 *  Jahrgang 00: Frühkindliche Förderung, Förderschulkindergarten
	 */
	public static readonly JG_00 : Jahrgaenge = new Jahrgaenge("JG_00", 0, [new JahrgangsKatalogEintrag(0, "00", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Frühkindliche Förderung, Förderschulkindergarten"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Frühkindliche Förderung, Förderschulkindergarten")), null, null)]);

	/**
	 *  Jahrgang 90: Hausfrüherziehung für Hör- bzw. Sehgeschädigte
	 */
	public static readonly JG_90 : Jahrgaenge = new Jahrgaenge("JG_90", 1, [new JahrgangsKatalogEintrag(4090000000, "90", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Hausfrüherziehung für Hör- bzw. Sehgeschädigte"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Hausfrüherziehung für Hör- bzw. Sehgeschädigte")), null, null)]);

	/**
	 *  Jahrgang E1: Schuleingangsphase, 1. Schulbesuchsjahr
	 */
	public static readonly JG_E1 : Jahrgaenge = new Jahrgaenge("JG_E1", 2, [new JahrgangsKatalogEintrag(1101000000, "E1", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "Schuleingangsphase, 1. Schulbesuchsjahr")), null, null)]);

	/**
	 *  Jahrgang E2: Schuleingangsphase, 2. Schulbesuchsjahr
	 */
	public static readonly JG_E2 : Jahrgaenge = new Jahrgaenge("JG_E2", 3, [new JahrgangsKatalogEintrag(1102000000, "E2", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "Schuleingangsphase, 2. Schulbesuchsjahr")), null, null)]);

	/**
	 *  Jahrgang E3: Schuleingangsphase, 3. Schulbesuchsjahr
	 */
	public static readonly JG_E3 : Jahrgaenge = new Jahrgaenge("JG_E3", 4, [new JahrgangsKatalogEintrag(1103000000, "E3", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "Schuleingangsphase, 3. Schulbesuchsjahr")), null, null)]);

	/**
	 *  Jahrgang 91: Vorkurs/ 1. Semester
	 */
	public static readonly JG_91 : Jahrgaenge = new Jahrgaenge("JG_91", 5, [new JahrgangsKatalogEintrag(4091000000, "91", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "Vorkurs / 1. Semester")), null, null)]);

	/**
	 *  Jahrgang 92: Vorkurs/ 2. Semester
	 */
	public static readonly JG_92 : Jahrgaenge = new Jahrgaenge("JG_92", 6, [new JahrgangsKatalogEintrag(4092000000, "92", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "Vorkurs / 2. Semester")), null, null)]);

	/**
	 *  Jahrgang 01: 1. Jahrgang / 1. Semester
	 */
	public static readonly JG_01 : Jahrgaenge = new Jahrgaenge("JG_01", 7, [new JahrgangsKatalogEintrag(1001000000, "01", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "1. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "1. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "1. Semester (Seiteneinsteiger)"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "1. Semester (Einführungs-/Hauptphase)")), null, null)]);

	/**
	 *  Jahrgang 02: 2. Jahrgang / 2. Semester
	 */
	public static readonly JG_02 : Jahrgaenge = new Jahrgaenge("JG_02", 8, [new JahrgangsKatalogEintrag(1002000000, "02", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "2. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "2. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "2. Semester (Seiteneinsteiger)"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "2. Semester (Einführungs-/Hauptphase)")), null, null)]);

	/**
	 *  Jahrgang 03: 3. Jahrgang / 3. Semester
	 */
	public static readonly JG_03 : Jahrgaenge = new Jahrgaenge("JG_03", 9, [new JahrgangsKatalogEintrag(1003000000, "03", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "3. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "3. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "3. Jahrgang / 3. Semester"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "3. Semester (Einführungs-/Hauptphase)"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "3. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 04: 4. Jahrgang / 4. Semester
	 */
	public static readonly JG_04 : Jahrgaenge = new Jahrgaenge("JG_04", 10, [new JahrgangsKatalogEintrag(1004000000, "04", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "4. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "4. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "4. Jahrgang / 4. Semester"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "4. Semester (Einführungs-/Hauptphase)"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "4. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 05: 5. Jahrgang / 5. Semester
	 */
	public static readonly JG_05 : Jahrgaenge = new Jahrgaenge("JG_05", 11, [new JahrgangsKatalogEintrag(2005000000, "05", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "5. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "5. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "5. Jahrgang / 5. Semester"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "5. Semester (Qualifikationsphase)"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "5. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 06: 6. Jahrgang / 6. Semester
	 */
	public static readonly JG_06 : Jahrgaenge = new Jahrgaenge("JG_06", 12, [new JahrgangsKatalogEintrag(2006000000, "06", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "6. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "6. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "6. Jahrgang / 6. Semester"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "6. Semester (Qualifikationsphase)"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "6. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 07: 7. Jahrgang
	 */
	public static readonly JG_07 : Jahrgaenge = new Jahrgaenge("JG_07", 13, [new JahrgangsKatalogEintrag(2007000000, "07", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "7. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 08: 8. Jahrgang
	 */
	public static readonly JG_08 : Jahrgaenge = new Jahrgaenge("JG_08", 14, [new JahrgangsKatalogEintrag(2008000000, "08", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "8. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 09: 9. Jahrgang
	 */
	public static readonly JG_09 : Jahrgaenge = new Jahrgaenge("JG_09", 15, [new JahrgangsKatalogEintrag(2009000000, "09", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "9. Jahrgang dieser Schulgliederung")), null, null)]);

	/**
	 *  Jahrgang 10: 10. Jahrgang
	 */
	public static readonly JG_10 : Jahrgaenge = new Jahrgaenge("JG_10", 16, [new JahrgangsKatalogEintrag(2010000000, "10", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "10. Jahrgang dieser Schulgliederung")), null, null)]);

	/**
	 *  Jahrgang 11: 11. Jahrgang
	 */
	public static readonly JG_11 : Jahrgaenge = new Jahrgaenge("JG_11", 17, [new JahrgangsKatalogEintrag(2011000000, "11", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "11. Jahrgang, Berufskolleg"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "11. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "11. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 12: 12. Jahrgang
	 */
	public static readonly JG_12 : Jahrgaenge = new Jahrgaenge("JG_12", 18, [new JahrgangsKatalogEintrag(2012000000, "12", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "12. Jahrgang, Berufskolleg"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "12. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "12. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 13: 13. Jahrgang
	 */
	public static readonly JG_13 : Jahrgaenge = new Jahrgaenge("JG_13", 19, [new JahrgangsKatalogEintrag(2013000000, "13", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "13. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "13. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 71: Schule für Kranke
	 */
	public static readonly JG_71 : Jahrgaenge = new Jahrgaenge("JG_71", 20, [new JahrgangsKatalogEintrag(4071000000, "71", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schule für Kranke"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schule für Kranke"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schule für Kranke"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schule für Kranke")), null, null)]);

	/**
	 *  Jahrgang 85: Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform
	 */
	public static readonly JG_85 : Jahrgaenge = new Jahrgaenge("JG_85", 21, [new JahrgangsKatalogEintrag(4085000000, "85", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform")), null, null)]);

	/**
	 *  Jahrgang 86: Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform
	 */
	public static readonly JG_86 : Jahrgaenge = new Jahrgaenge("JG_86", 22, [new JahrgangsKatalogEintrag(4086000000, "86", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform")), null, null)]);

	/**
	 *  Jahrgang EF: Einführungsphase
	 */
	public static readonly JG_EF : Jahrgaenge = new Jahrgaenge("JG_EF", 23, [new JahrgangsKatalogEintrag(3000000000, "EF", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "Einführungsphase"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "Einführungsphase"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "Einführungsphase"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Einführungsphase"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "Einführungsphase")), null, null)]);

	/**
	 *  Jahrgang Q1: Qualifikationsphase 1. Jahr
	 */
	public static readonly JG_Q1 : Jahrgaenge = new Jahrgaenge("JG_Q1", 24, [new JahrgangsKatalogEintrag(3001000000, "Q1", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "Qualifikationsphase 1. Jahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "Qualifikationsphase 1. Jahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "Qualifikationsphase 1. Jahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Qualifikationsphase 1. Jahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "Qualifikationsphase 1. Jahr")), null, null)]);

	/**
	 *  Jahrgang Q2: Qualifikationsphase 2. Jahr
	 */
	public static readonly JG_Q2 : Jahrgaenge = new Jahrgaenge("JG_Q2", 25, [new JahrgangsKatalogEintrag(3002000000, "Q2", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "Qualifikationsphase 2. Jahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "Qualifikationsphase 2. Jahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "Qualifikationsphase 2. Jahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Qualifikationsphase 2. Jahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "Qualifikationsphase 2. Jahr")), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Jahrgangs, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : JahrgangsKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen des Jahrgangs
	 */
	public readonly historie : Array<JahrgangsKatalogEintrag>;

	/**
	 * Eine Map mit der Zuordnung des Jahrgangs zu dem Kürzel des Jahrgangs
	 */
	private static readonly _mapKuerzel : HashMap<string, Jahrgaenge | null> = new HashMap();

	/**
	 * Eine Map mit der Zuordnung des Jahrgangs zu der ID des Jahrgangs
	 */
	private static readonly _mapID : HashMap<number, Jahrgaenge | null> = new HashMap();

	/**
	 * Die Schulformen, bei welchen der Jahrgang vorkommt, für die einzelnen Historieneinträge
	 */
	private readonly schulformen : Array<ArrayList<Schulform | null>>;

	/**
	 * Die Bezeichnungen bei den Schulformen, bei welchen der Jahrgang vorkommt, für die einzelnen Historieneinträge
	 */
	private readonly bezeichnungen : Array<ArrayList<string>>;

	/**
	 * Erzeugt einen neuen Jahrgang in der Aufzählung.
	 *
	 * @param historie   die Historie des Jahrgangs, welches ein Array von {@link JahrgangsKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<JahrgangsKatalogEintrag>) {
		super(name, ordinal);
		Jahrgaenge.all_values_by_ordinal.push(this);
		Jahrgaenge.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.schulformen = Array(historie.length).fill(null);
		this.bezeichnungen = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++) {
			this.schulformen[i] = new ArrayList();
			this.bezeichnungen[i] = new ArrayList();
			for (const bez of historie[i].bezeichnungen) {
				const sf : Schulform | null = Schulform.getByKuerzel(bez.schulform);
				if (sf !== null)
					this.schulformen[i].add(sf);
				this.bezeichnungen[i].add(bez.bezeichnung);
			}
		}
	}

	/**
	 * Gibt eine Map von den Kürzels der Jahrgänge auf die zugehörigen Jahrgänge
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzel der Jahrgänge auf die zugehörigen Jahrgänge
	 */
	private static getMapJahrgangByKuerzel() : HashMap<string, Jahrgaenge | null> {
		if (Jahrgaenge._mapKuerzel.size() === 0)
			for (const j of Jahrgaenge.values())
				Jahrgaenge._mapKuerzel.put(j.daten.kuerzel, j);
		return Jahrgaenge._mapKuerzel;
	}

	/**
	 * Gibt eine Map von den IDs der Jahrgänge auf die zugehörigen Jahrgänge
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Jahrgänge auf die zugehörigen Jahrgänge
	 */
	private static getMapJahrgangByID() : HashMap<number, Jahrgaenge | null> {
		if (Jahrgaenge._mapID.size() === 0)
			for (const j of Jahrgaenge.values()) {
				for (const k of j.historie)
					Jahrgaenge._mapID.put(k.id, j);
			}
		return Jahrgaenge._mapID;
	}

	/**
	 * Liefert den Jahrgang anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Jahrgangs
	 *
	 * @return der Jahrgang oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Jahrgaenge | null {
		return Jahrgaenge.getMapJahrgangByKuerzel().get(kuerzel);
	}

	/**
	 * Liefert den Jahrgang anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID des Jahrgangs
	 *
	 * @return der Jahrgang oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number | null) : Jahrgaenge | null {
		return Jahrgaenge.getMapJahrgangByID().get(id);
	}

	/**
	 * Liefert die Bezeichnung des Jahrgangs für die angegebene Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die Bezeichnung des Jahrgangs oder null, falls die Schulform nicht zulässig ist
	 */
	public getBezeichnung(schulform : Schulform | null) : string | null {
		if ((schulform === null) || (schulform.daten === null))
			return null;
		if (this.daten.bezeichnungen !== null) {
			for (let i : number = 0; i < this.daten.bezeichnungen.size(); i++) {
				const bez : JahrgangsKatalogEintragBezeichnung | null = this.daten.bezeichnungen.get(i);
				const sfKuerzel : string | null = bez.schulform;
				if (JavaObject.equalsTranspiler(sfKuerzel, (schulform.daten.kuerzel)))
					return bez.bezeichnung;
			}
		}
		return null;
	}

	/**
	 * Liefert alle Schulformen zurück, bei welchen der Jahrgang vorkommt.
	 *
	 * @return eine Liste der Schulformen
	 */
	public getSchulformen() : List<Schulform | null> {
		return this.schulformen[this.historie.length - 1];
	}

	/**
	 * Liefert alle zulässigen Jahrgänge für die angegebene Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform zulässigen Jahrgänge
	 */
	public static get(schulform : Schulform | null) : List<Jahrgaenge | null> {
		const result : ArrayList<Jahrgaenge | null> = new ArrayList();
		if (schulform === null)
			return result;
		const jahrgaenge : Array<Jahrgaenge> = Jahrgaenge.values();
		for (let i : number = 0; i < jahrgaenge.length; i++) {
			const jahrgang : Jahrgaenge = jahrgaenge[i];
			if (jahrgang.hasSchulform(schulform))
				result.add(jahrgang);
		}
		return result;
	}

	/**
	 * Prüft, ob das übergebene Kürzel dem Kürzel dieses Jahrgangs entspricht.
	 *
	 * @param kuerzel   das zu prüfende Kürzel
	 *
	 * @return true, falls das übergebene Kürzel das Kürzel dieses Jahrgangs ist.
	 */
	public isKuerzel(kuerzel : string | null) : boolean {
		if (this.daten === null)
			return false;
		return JavaObject.equalsTranspiler(this.daten.kuerzel, (kuerzel));
	}

	/**
	 * Prüft anhand des Schulform-Kürzels, ob die Schulform diesen Jahrgang
	 * hat oder nicht.
	 *
	 * @param kuerzel   das Kürzel der Schulform
	 *
	 * @return true, falls der Jahrgang bei der Schulform existiert und ansonsten false
	 */
	public hasSchulformByKuerzel(kuerzel : string | null) : boolean {
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel)))
			return false;
		if (this.daten.bezeichnungen !== null) {
			for (let i : number = 0; i < this.daten.bezeichnungen.size(); i++) {
				const sfKuerzel : string | null = this.daten.bezeichnungen.get(i).schulform;
				if (JavaObject.equalsTranspiler(sfKuerzel, (kuerzel)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob die Schulform diesen Jahrgang hat oder nicht.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return true, falls der Jahrgang bei der Schulform existiert und ansonsten false
	 */
	public hasSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null))
			return false;
		if (this.daten.bezeichnungen !== null) {
			for (let i : number = 0; i < this.daten.bezeichnungen.size(); i++) {
				const sfKuerzel : string | null = this.daten.bezeichnungen.get(i).schulform;
				if (JavaObject.equalsTranspiler(sfKuerzel, (schulform.daten.kuerzel)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob der angegebene Jahrgang bei der angegebenen Schulform und Gliederung ein gültiger Vorgänger-Jahrgang
	 * dieses Jahrgangs ist.
	 *
	 * @param vergleichsjahrgang     der zu prüfende Jahrgang
	 * @param schulform    die Schulform
	 * @param gliederung   die Schulgliederung
	 *
	 * @return true, falls jgVorher ein gültiger Vorgänger-Jahrgang dieses Jahrgangs ist.
	 */
	public isNachfolgerVon(vergleichsjahrgang : Jahrgaenge | null, schulform : Schulform | null, gliederung : Schulgliederung | null) : boolean {
		if (schulform === null)
			return false;
		if (!this.hasSchulform(schulform) || ((vergleichsjahrgang !== null) && (!vergleichsjahrgang.hasSchulform(schulform))))
			return false;
		const gl : Schulgliederung | null = (gliederung === null) ? Schulgliederung.getDefault(schulform) : gliederung;
		switch (this) {
			case Jahrgaenge.JG_00: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_01: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_02: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_01 as unknown;
			}
			case Jahrgaenge.JG_03: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_02 as unknown) || (vergleichsjahrgang as unknown === Jahrgaenge.JG_E2 as unknown) || (vergleichsjahrgang as unknown === Jahrgaenge.JG_E3 as unknown);
			}
			case Jahrgaenge.JG_04: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_03 as unknown);
			}
			case Jahrgaenge.JG_05: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_04 as unknown);
			}
			case Jahrgaenge.JG_06: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_05 as unknown);
			}
			case Jahrgaenge.JG_07: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_06 as unknown);
			}
			case Jahrgaenge.JG_08: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_07 as unknown);
			}
			case Jahrgaenge.JG_09: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_08 as unknown);
			}
			case Jahrgaenge.JG_10: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_09 as unknown);
			}
			case Jahrgaenge.JG_11: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_10 as unknown);
			}
			case Jahrgaenge.JG_12: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_11 as unknown);
			}
			case Jahrgaenge.JG_13: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_12 as unknown);
			}
			case Jahrgaenge.JG_71: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_85: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_86: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_90: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_91: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_92: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_91 as unknown;
			}
			case Jahrgaenge.JG_E1: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_00 as unknown) || (vergleichsjahrgang === null);
			}
			case Jahrgaenge.JG_E2: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_E1 as unknown;
			}
			case Jahrgaenge.JG_E3: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_E2 as unknown;
			}
			case Jahrgaenge.JG_EF: {
				return (gl as unknown === Schulgliederung.GY8 as unknown) ? (vergleichsjahrgang as unknown === Jahrgaenge.JG_09 as unknown) : (vergleichsjahrgang as unknown === Jahrgaenge.JG_10 as unknown);
			}
			case Jahrgaenge.JG_Q1: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_EF as unknown;
			}
			case Jahrgaenge.JG_Q2: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_Q1 as unknown;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * Prüft, ob der angegebene Jahrgang bei der angegebenen Schulform und Gliederung ein gültiger Nachfolger-Jahrgang
	 * dieses Jahrgangs ist.
	 *
	 * @param vergleichsjahrgang    der zu prüfende Jahrgang
	 * @param schulform    die Schulform
	 * @param gliederung   die Schulgliederung
	 *
	 * @return true, falls jgNachher ein gültiger Nachfolger-Jahrgang dieses Jahrgangs ist.
	 */
	public isVorgaengerVon(vergleichsjahrgang : Jahrgaenge | null, schulform : Schulform | null, gliederung : Schulgliederung | null) : boolean {
		if (schulform === null)
			return false;
		if (!this.hasSchulform(schulform) || ((vergleichsjahrgang !== null) && (!vergleichsjahrgang.hasSchulform(schulform))))
			return false;
		const gl : Schulgliederung | null = (gliederung === null) ? Schulgliederung.getDefault(schulform) : gliederung;
		switch (this) {
			case Jahrgaenge.JG_00: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_01 as unknown;
			}
			case Jahrgaenge.JG_01: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_02 as unknown;
			}
			case Jahrgaenge.JG_02: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_03 as unknown;
			}
			case Jahrgaenge.JG_03: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_04 as unknown;
			}
			case Jahrgaenge.JG_04: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_05 as unknown;
			}
			case Jahrgaenge.JG_05: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_06 as unknown;
			}
			case Jahrgaenge.JG_06: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_07 as unknown;
			}
			case Jahrgaenge.JG_07: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_08 as unknown;
			}
			case Jahrgaenge.JG_08: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_09 as unknown;
			}
			case Jahrgaenge.JG_09: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_10 as unknown) || ((schulform as unknown === Schulform.GY as unknown) && ((gl as unknown === Schulgliederung.GY8 as unknown) || (gl as unknown === Schulgliederung.DEFAULT as unknown)) && (vergleichsjahrgang as unknown === Jahrgaenge.JG_EF as unknown));
			}
			case Jahrgaenge.JG_10: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_11 as unknown) || ((schulform.daten.hatGymOb) && (vergleichsjahrgang as unknown === Jahrgaenge.JG_EF as unknown));
			}
			case Jahrgaenge.JG_11: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_12 as unknown);
			}
			case Jahrgaenge.JG_12: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_13 as unknown);
			}
			case Jahrgaenge.JG_13: {
				return (vergleichsjahrgang === null);
			}
			case Jahrgaenge.JG_71: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_85: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_86: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_90: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_91: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_92 as unknown);
			}
			case Jahrgaenge.JG_92: {
				return vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_E1: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_E2 as unknown;
			}
			case Jahrgaenge.JG_E2: {
				return (vergleichsjahrgang as unknown === Jahrgaenge.JG_E3 as unknown) || (vergleichsjahrgang as unknown === Jahrgaenge.JG_03 as unknown);
			}
			case Jahrgaenge.JG_E3: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_03 as unknown;
			}
			case Jahrgaenge.JG_EF: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_Q1 as unknown;
			}
			case Jahrgaenge.JG_Q1: {
				return vergleichsjahrgang as unknown === Jahrgaenge.JG_Q2 as unknown;
			}
			case Jahrgaenge.JG_Q2: {
				return vergleichsjahrgang === null;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * Prüft, ob dieser Jahrgang ein möglicher Nachfolger des übergebenen Vergleichsjahrgangs ist.
	 * Da durch den Wechsel von G8 und G9 und den Wechsel von Sek-I (10) in die Sek-II eine Zuordnung unter Umständen
	 * nur Schüler individuell entschieden werden kann, wird hier nur auf mögliche Nachfolger geprüft.
	 *
	 * Diese Methode berücksichtigt keine Schulformen und -gliederungen und liefert daher nur ein grobes Ergebnis
	 *
	 * @param Vergleichsjahrgang    der Jahrgang für den Vergleich zum gegebenen Jahrgang.
	 *
	 * @return true, falls dieser Jahrgang ein möglicher Vorgänger des übergebenen Vergleichsjahrgangs ist.
	 */
	public isMoeglicherNachfolgerVon(Vergleichsjahrgang : Jahrgaenge | null) : boolean {
		switch (this) {
			case Jahrgaenge.JG_00: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_01: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_02: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_01 as unknown;
			}
			case Jahrgaenge.JG_03: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_02 as unknown) || (Vergleichsjahrgang as unknown === Jahrgaenge.JG_E2 as unknown) || (Vergleichsjahrgang as unknown === Jahrgaenge.JG_E3 as unknown);
			}
			case Jahrgaenge.JG_04: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_03 as unknown);
			}
			case Jahrgaenge.JG_05: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_04 as unknown);
			}
			case Jahrgaenge.JG_06: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_05 as unknown);
			}
			case Jahrgaenge.JG_07: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_06 as unknown);
			}
			case Jahrgaenge.JG_08: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_07 as unknown);
			}
			case Jahrgaenge.JG_09: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_08 as unknown);
			}
			case Jahrgaenge.JG_10: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_09 as unknown);
			}
			case Jahrgaenge.JG_11: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_10 as unknown);
			}
			case Jahrgaenge.JG_12: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_11 as unknown);
			}
			case Jahrgaenge.JG_13: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_12 as unknown);
			}
			case Jahrgaenge.JG_71: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_85: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_86: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_90: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_91: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_92: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_91 as unknown;
			}
			case Jahrgaenge.JG_E1: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_00 as unknown) || (Vergleichsjahrgang === null);
			}
			case Jahrgaenge.JG_E2: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_E1 as unknown;
			}
			case Jahrgaenge.JG_E3: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_E2 as unknown;
			}
			case Jahrgaenge.JG_EF: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_09 as unknown) || (Vergleichsjahrgang as unknown === Jahrgaenge.JG_10 as unknown);
			}
			case Jahrgaenge.JG_Q1: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_EF as unknown;
			}
			case Jahrgaenge.JG_Q2: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_Q1 as unknown;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * Prüft, ob dieser Jahrgang ein möglicher Vorgänger des übergebenen Vergleichsjahrgangs ist.
	 * Da durch den Wechsel von G8 und G9 und den Wechsel von Sek-I (10) in die Sek-II eine Zuordnung unter Umständen
	 * nur Schüler individuell entschieden werden kann, wird hier nur auf mögliche Vorgänger geprüft.
	 *
	 * Diese Methode berücksichtigt keine Schulformen und -gliederungen und liefert daher nur ein grobes Ergebnis
	 *
	 * @param Vergleichsjahrgang    der Jahrgang für den Vergleich zum gegebenen Jahrgang.
	 *
	 * @return true, falls dieser Jahrgang ein möglicher Vorgänger des übergebenen Vergleichsjahrgangs ist.
	 */
	public isMoeglicherVorgaengerVon(Vergleichsjahrgang : Jahrgaenge | null) : boolean {
		switch (this) {
			case Jahrgaenge.JG_00: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_01 as unknown;
			}
			case Jahrgaenge.JG_01: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_02 as unknown;
			}
			case Jahrgaenge.JG_02: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_03 as unknown;
			}
			case Jahrgaenge.JG_03: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_04 as unknown;
			}
			case Jahrgaenge.JG_04: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_05 as unknown;
			}
			case Jahrgaenge.JG_05: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_06 as unknown;
			}
			case Jahrgaenge.JG_06: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_07 as unknown;
			}
			case Jahrgaenge.JG_07: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_08 as unknown;
			}
			case Jahrgaenge.JG_08: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_09 as unknown;
			}
			case Jahrgaenge.JG_09: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_10 as unknown) || (Vergleichsjahrgang as unknown === Jahrgaenge.JG_EF as unknown);
			}
			case Jahrgaenge.JG_10: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_11 as unknown) || (Vergleichsjahrgang as unknown === Jahrgaenge.JG_EF as unknown);
			}
			case Jahrgaenge.JG_11: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_12 as unknown);
			}
			case Jahrgaenge.JG_12: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_13 as unknown);
			}
			case Jahrgaenge.JG_13: {
				return (Vergleichsjahrgang === null);
			}
			case Jahrgaenge.JG_71: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_85: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_86: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_90: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_91: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_92 as unknown);
			}
			case Jahrgaenge.JG_92: {
				return Vergleichsjahrgang === null;
			}
			case Jahrgaenge.JG_E1: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_E2 as unknown;
			}
			case Jahrgaenge.JG_E2: {
				return (Vergleichsjahrgang as unknown === Jahrgaenge.JG_E3 as unknown) || (Vergleichsjahrgang as unknown === Jahrgaenge.JG_03 as unknown);
			}
			case Jahrgaenge.JG_E3: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_03 as unknown;
			}
			case Jahrgaenge.JG_EF: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_Q1 as unknown;
			}
			case Jahrgaenge.JG_Q1: {
				return Vergleichsjahrgang as unknown === Jahrgaenge.JG_Q2 as unknown;
			}
			case Jahrgaenge.JG_Q2: {
				return Vergleichsjahrgang === null;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * Gibt zurück, ob bei diesem Jahrgang für die angebebene Schulform und Schulgliederung in dem Schuljahr eine
	 * Lernbereichsnote 1 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return true, falls eine Lernbereichsnote vorhanden ist.
	 */
	public hatLernbereichsnote1(schulform : Schulform, schulgliederung : Schulgliederung | null, schuljahr : number) : boolean {
		let _sevar_1515493925 : any;
		const _seexpr_1515493925 = (schulform);
		if (_seexpr_1515493925 === Schulform.R) {
			_sevar_1515493925 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.SR) {
			_sevar_1515493925 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.H) {
			_sevar_1515493925 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.S) {
			_sevar_1515493925 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.FW) {
			_sevar_1515493925 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.WF) {
			_sevar_1515493925 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.GY) {
			_sevar_1515493925 = (schulgliederung as unknown === Schulgliederung.GY8 as unknown) || (schulgliederung as unknown === Schulgliederung.DEFAULT as unknown) ? (this as unknown === Jahrgaenge.JG_EF as unknown) : (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.SG) {
			_sevar_1515493925 = (schulgliederung as unknown === Schulgliederung.GY8 as unknown) || (schulgliederung as unknown === Schulgliederung.DEFAULT as unknown) ? (this as unknown === Jahrgaenge.JG_EF as unknown) : (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.GM) {
			_sevar_1515493925 = ((this as unknown === Jahrgaenge.JG_10 as unknown) && (schuljahr <= 2024)) || ((this as unknown === Jahrgaenge.JG_09 as unknown) && (schuljahr <= 2023)) || ((this as unknown === Jahrgaenge.JG_08 as unknown) && (schuljahr <= 2022));
		} else if (_seexpr_1515493925 === Schulform.GE) {
			_sevar_1515493925 = ((this as unknown === Jahrgaenge.JG_10 as unknown) && (schuljahr <= 2024)) || ((this as unknown === Jahrgaenge.JG_09 as unknown) && (schuljahr <= 2023)) || ((this as unknown === Jahrgaenge.JG_08 as unknown) && (schuljahr <= 2022));
		} else if (_seexpr_1515493925 === Schulform.PS) {
			_sevar_1515493925 = ((this as unknown === Jahrgaenge.JG_10 as unknown) && (schuljahr <= 2024)) || ((this as unknown === Jahrgaenge.JG_09 as unknown) && (schuljahr <= 2023)) || ((this as unknown === Jahrgaenge.JG_08 as unknown) && (schuljahr <= 2022));
		} else if (_seexpr_1515493925 === Schulform.SK) {
			_sevar_1515493925 = ((this as unknown === Jahrgaenge.JG_10 as unknown) && (schuljahr <= 2024)) || ((this as unknown === Jahrgaenge.JG_09 as unknown) && (schuljahr <= 2023)) || ((this as unknown === Jahrgaenge.JG_08 as unknown) && (schuljahr <= 2022));
		} else if (_seexpr_1515493925 === Schulform.HI) {
			_sevar_1515493925 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.KS) {
			_sevar_1515493925 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.V) {
			_sevar_1515493925 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1515493925 === Schulform.BK) {
			_sevar_1515493925 = false;
		} else if (_seexpr_1515493925 === Schulform.SB) {
			_sevar_1515493925 = false;
		} else if (_seexpr_1515493925 === Schulform.WB) {
			_sevar_1515493925 = false;
		} else if (_seexpr_1515493925 === Schulform.G) {
			_sevar_1515493925 = false;
		}
		return _sevar_1515493925;
	}

	/**
	 * Gibt die Bezeichnung der Lernbereichtsnote 1 zurück, sofern bei diesem Jahrgang für die angebebene Schulform
	 * und Schulgliederung in dem Schuljahr eine Lernbereichsnote 1 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return die Bezeichnung der Lernbereichsnote, falls eine vorhanden ist und ansonsten null.
	 */
	public getLernbereichsnote1Bezeichnung(schulform : Schulform, schulgliederung : Schulgliederung | null, schuljahr : number) : string | null {
		if (!this.hatLernbereichsnote1(schulform, schulgliederung, schuljahr))
			return null;
		let _sevar_825399400 : any;
		const _seexpr_825399400 = (schulform);
		if (_seexpr_825399400 === Schulform.H) {
			_sevar_825399400 = "Arbeitslehre";
		} else if (_seexpr_825399400 === Schulform.GM) {
			_sevar_825399400 = "Arbeitslehre";
		} else if (_seexpr_825399400 === Schulform.GE) {
			_sevar_825399400 = "Arbeitslehre";
		} else if (_seexpr_825399400 === Schulform.PS) {
			_sevar_825399400 = "Arbeitslehre";
		} else if (_seexpr_825399400 === Schulform.SK) {
			_sevar_825399400 = "Arbeitslehre";
		} else {
			_sevar_825399400 = "Gesellschaftslehre";
		}
		return _sevar_825399400;
	}

	/**
	 * Gibt zurück, ob bei diesem Jahrgang für die angebebene Schulform und Schulgliederung in dem Schuljahr eine
	 * Lernbereichsnote 2 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return true, falls eine Lernbereichsnote vorhanden ist.
	 */
	public hatLernbereichsnote2(schulform : Schulform, schulgliederung : Schulgliederung | null, schuljahr : number) : boolean {
		let _sevar_1174212382 : any;
		const _seexpr_1174212382 = (schulform);
		if (_seexpr_1174212382 === Schulform.R) {
			_sevar_1174212382 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.SR) {
			_sevar_1174212382 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.H) {
			_sevar_1174212382 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.S) {
			_sevar_1174212382 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.FW) {
			_sevar_1174212382 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.WF) {
			_sevar_1174212382 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.GY) {
			_sevar_1174212382 = (schulgliederung as unknown === Schulgliederung.GY8 as unknown) || (schulgliederung as unknown === Schulgliederung.DEFAULT as unknown) ? (this as unknown === Jahrgaenge.JG_EF as unknown) : (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.SG) {
			_sevar_1174212382 = (schulgliederung as unknown === Schulgliederung.GY8 as unknown) || (schulgliederung as unknown === Schulgliederung.DEFAULT as unknown) ? (this as unknown === Jahrgaenge.JG_EF as unknown) : (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.GM) {
			_sevar_1174212382 = ((this as unknown === Jahrgaenge.JG_10 as unknown) || (this as unknown === Jahrgaenge.JG_09 as unknown) || (this as unknown === Jahrgaenge.JG_08 as unknown));
		} else if (_seexpr_1174212382 === Schulform.GE) {
			_sevar_1174212382 = ((this as unknown === Jahrgaenge.JG_10 as unknown) || (this as unknown === Jahrgaenge.JG_09 as unknown) || (this as unknown === Jahrgaenge.JG_08 as unknown));
		} else if (_seexpr_1174212382 === Schulform.PS) {
			_sevar_1174212382 = ((this as unknown === Jahrgaenge.JG_10 as unknown) || (this as unknown === Jahrgaenge.JG_09 as unknown) || (this as unknown === Jahrgaenge.JG_08 as unknown));
		} else if (_seexpr_1174212382 === Schulform.SK) {
			_sevar_1174212382 = ((this as unknown === Jahrgaenge.JG_10 as unknown) || (this as unknown === Jahrgaenge.JG_09 as unknown) || (this as unknown === Jahrgaenge.JG_08 as unknown));
		} else if (_seexpr_1174212382 === Schulform.HI) {
			_sevar_1174212382 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.KS) {
			_sevar_1174212382 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.V) {
			_sevar_1174212382 = (this as unknown === Jahrgaenge.JG_10 as unknown);
		} else if (_seexpr_1174212382 === Schulform.BK) {
			_sevar_1174212382 = false;
		} else if (_seexpr_1174212382 === Schulform.SB) {
			_sevar_1174212382 = false;
		} else if (_seexpr_1174212382 === Schulform.WB) {
			_sevar_1174212382 = false;
		} else if (_seexpr_1174212382 === Schulform.G) {
			_sevar_1174212382 = false;
		}
		return _sevar_1174212382;
	}

	/**
	 * Gibt die Bezeichnung der Lernbereichtsnote 2 zurück, sofern bei diesem Jahrgang für die angebebene Schulform
	 * und Schulgliederung in dem Schuljahr eine Lernbereichsnote 2 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return die Bezeichnung der Lernbereichsnote, falls eine vorhanden ist und ansonsten null.
	 */
	public getLernbereichsnote2Bezeichnung(schulform : Schulform, schulgliederung : Schulgliederung | null, schuljahr : number) : string | null {
		if (!this.hatLernbereichsnote2(schulform, schulgliederung, schuljahr))
			return null;
		return "Naturwissenschaft";
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Jahrgaenge> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Jahrgaenge | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.jahrgang.Jahrgaenge';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.jahrgang.Jahrgaenge', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_jahrgang_Jahrgaenge(obj : unknown) : Jahrgaenge {
	return obj as Jahrgaenge;
}
