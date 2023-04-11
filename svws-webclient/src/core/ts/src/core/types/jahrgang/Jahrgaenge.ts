import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';
import { JahrgangsKatalogEintrag } from '../../../core/data/jahrgang/JahrgangsKatalogEintrag';
import { Arrays } from '../../../java/util/Arrays';
import { JahrgangsKatalogEintragBezeichnung } from '../../../core/data/jahrgang/JahrgangsKatalogEintragBezeichnung';

export class Jahrgaenge extends JavaObject implements JavaEnum<Jahrgaenge> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Jahrgaenge> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Jahrgaenge> = new Map<string, Jahrgaenge>();

	/**
	 *  Jahrgang 00: Frühkindliche Förderung, Förderschulkindergarten
	 */
	public static readonly JG_00 : Jahrgaenge = new Jahrgaenge("JG_00", 0, [new JahrgangsKatalogEintrag(0, "00", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Frühkindliche Förderung, Förderschulkindergarten"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Frühkindliche Förderung, Förderschulkindergarten")), null, null)]);

	/**
	 *  Jahrgang E1: Schuleingangsphase, 1. Schulbesuchsjahr
	 */
	public static readonly JG_E1 : Jahrgaenge = new Jahrgaenge("JG_E1", 1, [new JahrgangsKatalogEintrag(1101000000, "E1", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schuleingangsphase, 1. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "Schuleingangsphase, 1. Schulbesuchsjahr")), null, null)]);

	/**
	 *  Jahrgang E2: Schuleingangsphase, 2. Schulbesuchsjahr
	 */
	public static readonly JG_E2 : Jahrgaenge = new Jahrgaenge("JG_E2", 2, [new JahrgangsKatalogEintrag(1102000000, "E2", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schuleingangsphase, 2. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "Schuleingangsphase, 2. Schulbesuchsjahr")), null, null)]);

	/**
	 *  Jahrgang E3: Schuleingangsphase, 3. Schulbesuchsjahr
	 */
	public static readonly JG_E3 : Jahrgaenge = new Jahrgaenge("JG_E3", 3, [new JahrgangsKatalogEintrag(1103000000, "E3", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schuleingangsphase, 3. Schulbesuchsjahr"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "Schuleingangsphase, 3. Schulbesuchsjahr")), null, null)]);

	/**
	 *  Jahrgang 01: 1. Jahrgang / 1. Semester
	 */
	public static readonly JG_01 : Jahrgaenge = new Jahrgaenge("JG_01", 4, [new JahrgangsKatalogEintrag(1001000000, "01", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "1. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "1. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "1. Semester (Seiteneinsteiger)"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "1. Semester (Einführungs-/Hauptphase)")), null, null)]);

	/**
	 *  Jahrgang 02: 2. Jahrgang / 2. Semester
	 */
	public static readonly JG_02 : Jahrgaenge = new Jahrgaenge("JG_02", 5, [new JahrgangsKatalogEintrag(1002000000, "02", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "2. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "2. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "2. Semester (Seiteneinsteiger)"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "2. Semester (Einführungs-/Hauptphase)")), null, null)]);

	/**
	 *  Jahrgang 03: 3. Jahrgang / 3. Semester
	 */
	public static readonly JG_03 : Jahrgaenge = new Jahrgaenge("JG_03", 6, [new JahrgangsKatalogEintrag(1003000000, "03", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "3. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "3. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "3. Jahrgang / 3. Semester"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "3. Semester (Einführungs-/Hauptphase)"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "3. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "3. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 04: 4. Jahrgang / 4. Semester
	 */
	public static readonly JG_04 : Jahrgaenge = new Jahrgaenge("JG_04", 7, [new JahrgangsKatalogEintrag(1004000000, "04", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "4. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "4. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "4. Jahrgang / 4. Semester"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "4. Semester (Einführungs-/Hauptphase)"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.G, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "4. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "4. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 05: 5. Jahrgang / 5. Semester
	 */
	public static readonly JG_05 : Jahrgaenge = new Jahrgaenge("JG_05", 8, [new JahrgangsKatalogEintrag(2005000000, "05", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "5. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "5. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "5. Jahrgang / 5. Semester"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "5. Semester (Qualifikationsphase)"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "5. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "5. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 06: 6. Jahrgang / 6. Semester
	 */
	public static readonly JG_06 : Jahrgaenge = new Jahrgaenge("JG_06", 9, [new JahrgangsKatalogEintrag(2006000000, "06", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "6. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "6. Jahrgang dieser Schulgliederung"), new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "6. Jahrgang / 6. Semester"), new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "6. Semester (Qualifikationsphase)"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "6. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "6. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 07: 7. Jahrgang
	 */
	public static readonly JG_07 : Jahrgaenge = new Jahrgaenge("JG_07", 10, [new JahrgangsKatalogEintrag(2007000000, "07", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "7. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "7. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 08: 8. Jahrgang
	 */
	public static readonly JG_08 : Jahrgaenge = new Jahrgaenge("JG_08", 11, [new JahrgangsKatalogEintrag(2008000000, "08", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "8. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "8. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 09: 9. Jahrgang
	 */
	public static readonly JG_09 : Jahrgaenge = new Jahrgaenge("JG_09", 12, [new JahrgangsKatalogEintrag(2009000000, "09", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "9. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "9. Jahrgang dieser Schulgliederung")), null, null)]);

	/**
	 *  Jahrgang 10: 10. Jahrgang
	 */
	public static readonly JG_10 : Jahrgaenge = new Jahrgaenge("JG_10", 13, [new JahrgangsKatalogEintrag(2010000000, "10", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.V, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.H, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.R, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "10. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "10. Jahrgang dieser Schulgliederung")), null, null)]);

	/**
	 *  Jahrgang 11: 11. Jahrgang
	 */
	public static readonly JG_11 : Jahrgaenge = new Jahrgaenge("JG_11", 14, [new JahrgangsKatalogEintrag(2011000000, "11", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "11. Jahrgang, Berufskolleg"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "11. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "11. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 12: 12. Jahrgang
	 */
	public static readonly JG_12 : Jahrgaenge = new Jahrgaenge("JG_12", 15, [new JahrgangsKatalogEintrag(2012000000, "12", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "12. Jahrgang, Berufskolleg"), new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "12. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "12. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 13: 13. Jahrgang
	 */
	public static readonly JG_13 : Jahrgaenge = new Jahrgaenge("JG_13", 16, [new JahrgangsKatalogEintrag(2013000000, "13", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "13. Jahrgang"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "13. Jahrgang")), null, null)]);

	/**
	 *  Jahrgang 71: Schule für Kranke
	 */
	public static readonly JG_71 : Jahrgaenge = new Jahrgaenge("JG_71", 17, [new JahrgangsKatalogEintrag(4071000000, "71", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schule für Kranke"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schule für Kranke"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schule für Kranke"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schule für Kranke")), null, null)]);

	/**
	 *  Jahrgang 85: Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform
	 */
	public static readonly JG_85 : Jahrgaenge = new Jahrgaenge("JG_85", 18, [new JahrgangsKatalogEintrag(4085000000, "85", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform")), null, null)]);

	/**
	 *  Jahrgang 86: Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform
	 */
	public static readonly JG_86 : Jahrgaenge = new Jahrgaenge("JG_86", 19, [new JahrgangsKatalogEintrag(4086000000, "86", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform")), null, null)]);

	/**
	 *  Jahrgang 90: Hausfrüherziehung für Hör- bzw. Sehgeschädigte
	 */
	public static readonly JG_90 : Jahrgaenge = new Jahrgaenge("JG_90", 20, [new JahrgangsKatalogEintrag(4090000000, "90", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Hausfrüherziehung für Hör- bzw. Sehgeschädigte"), new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Hausfrüherziehung für Hör- bzw. Sehgeschädigte")), null, null)]);

	/**
	 *  Jahrgang 91: Vorkurs/ 1. Semester
	 */
	public static readonly JG_91 : Jahrgaenge = new Jahrgaenge("JG_91", 21, [new JahrgangsKatalogEintrag(4091000000, "91", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "Vorkurs / 1. Semester")), null, null)]);

	/**
	 *  Jahrgang 92: Vorkurs/ 2. Semester
	 */
	public static readonly JG_92 : Jahrgaenge = new Jahrgaenge("JG_92", 22, [new JahrgangsKatalogEintrag(4092000000, "92", Arrays.asList(new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "Vorkurs / 2. Semester")), null, null)]);

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
	private schulformen : Array<ArrayList<Schulform | null>>;

	/**
	 * Die Bezeichnungen bei den Schulformen, bei welchen der Jahrgang vorkommt, für die einzelnen Historieneinträge
	 */
	private bezeichnungen : Array<ArrayList<string>>;

	/**
	 * Erzeugt einen neuen Jahrgang in der Aufzählung.
	 *
	 * @param historie   die Historie des Jahrgangs, welches ein Array von {@link JahrgangsKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<JahrgangsKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
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
	 * Liefert die Bezeichnung des Jahrgangs für die angebenene Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die Bezeichung des Jahrgangs oder null, falls die Schulform nicht zulässig ist
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
		if (!(other instanceof Jahrgaenge))
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
	public compareTo(other : Jahrgaenge) : number {
		return this.__ordinal - other.__ordinal;
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
		const tmp : Jahrgaenge | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.jahrgang.Jahrgaenge', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_jahrgang_Jahrgaenge(obj : unknown) : Jahrgaenge {
	return obj as Jahrgaenge;
}
