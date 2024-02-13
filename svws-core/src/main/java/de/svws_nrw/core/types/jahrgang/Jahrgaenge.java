package de.svws_nrw.core.types.jahrgang;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.data.jahrgang.JahrgangsKatalogEintrag;
import de.svws_nrw.core.data.jahrgang.JahrgangsKatalogEintragBezeichnung;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Jahrgänge zur Verfügung.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Jahrgaenge {

	/**
	 * Jahrgang 00: Frühkindliche Förderung, Förderschulkindergarten
	 */
	JG_00(new JahrgangsKatalogEintrag[] {
			new JahrgangsKatalogEintrag(0L, "00", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Frühkindliche Förderung, Förderschulkindergarten"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Frühkindliche Förderung, Förderschulkindergarten")
					), null, null)
	}),


	/**
	 * Jahrgang 90: Hausfrüherziehung für Hör- bzw. Sehgeschädigte
	 */
	JG_90(new JahrgangsKatalogEintrag[]{
		new JahrgangsKatalogEintrag(4090000000L, "90", Arrays.asList(
			new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Hausfrüherziehung für Hör- bzw. Sehgeschädigte"),
			new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Hausfrüherziehung für Hör- bzw. Sehgeschädigte")
		), null, null)
	}),


	/**
	 * Jahrgang E1: Schuleingangsphase, 1. Schulbesuchsjahr
	 */
	JG_E1(new JahrgangsKatalogEintrag[] {
			new JahrgangsKatalogEintrag(1101000000L, "E1", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schuleingangsphase, 1. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "Schuleingangsphase, 1. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schuleingangsphase, 1. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.G, "Schuleingangsphase, 1. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Schuleingangsphase, 1. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schuleingangsphase, 1. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schuleingangsphase, 1. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "Schuleingangsphase, 1. Schulbesuchsjahr")
					), null, null)
	}),


	/**
	 * Jahrgang E2: Schuleingangsphase, 2. Schulbesuchsjahr
	 */
	JG_E2(new JahrgangsKatalogEintrag[] {
			new JahrgangsKatalogEintrag(1102000000L, "E2", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schuleingangsphase, 2. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "Schuleingangsphase, 2. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schuleingangsphase, 2. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.G, "Schuleingangsphase, 2. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Schuleingangsphase, 2. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schuleingangsphase, 2. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schuleingangsphase, 2. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "Schuleingangsphase, 2. Schulbesuchsjahr")
					), null, null)
	}),


	/**
	 * Jahrgang E3: Schuleingangsphase, 3. Schulbesuchsjahr
	 */
	JG_E3(new JahrgangsKatalogEintrag[] {
			new JahrgangsKatalogEintrag(1103000000L, "E3", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schuleingangsphase, 3. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "Schuleingangsphase, 3. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schuleingangsphase, 3. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.G, "Schuleingangsphase, 3. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Schuleingangsphase, 3. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schuleingangsphase, 3. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schuleingangsphase, 3. Schulbesuchsjahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "Schuleingangsphase, 3. Schulbesuchsjahr")
					), null, null)
	}),


	/**
	 * Jahrgang 91: Vorkurs/ 1. Semester
	 */
	JG_91(new JahrgangsKatalogEintrag[]{
		new JahrgangsKatalogEintrag(4091000000L, "91", Arrays.asList(
			new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "Vorkurs / 1. Semester")
		), null, null)
	}),


	/**
	 * Jahrgang 92: Vorkurs/ 2. Semester
	 */
	JG_92(new JahrgangsKatalogEintrag[]{
		new JahrgangsKatalogEintrag(4092000000L, "92", Arrays.asList(
			new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "Vorkurs / 2. Semester")
		), null, null)
	}),


	/**
	 * Jahrgang 01: 1. Jahrgang / 1. Semester
	 */
	JG_01(new JahrgangsKatalogEintrag[] {
			new JahrgangsKatalogEintrag(1001000000L, "01", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "1. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "1. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "1. Semester (Seiteneinsteiger)"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "1. Semester (Einführungs-/Hauptphase)")
					), null, null)
	}),


	/**
	 * Jahrgang 02: 2. Jahrgang / 2. Semester
	 */
	JG_02(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(1002000000L, "02", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "2. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "2. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "2. Semester (Seiteneinsteiger)"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "2. Semester (Einführungs-/Hauptphase)")
					), null, null)
	}),


	/**
	 * Jahrgang 03: 3. Jahrgang / 3. Semester
	 */
	JG_03(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(1003000000L, "03", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "3. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "3. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "3. Jahrgang / 3. Semester"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "3. Semester (Einführungs-/Hauptphase)"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "3. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "3. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.G, "3. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "3. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "3. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "3. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "3. Jahrgang")
					), null, null)
	}),


	/**
	 * Jahrgang 04: 4. Jahrgang / 4. Semester
	 */
	JG_04(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(1004000000L, "04", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "4. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "4. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "4. Jahrgang / 4. Semester"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "4. Semester (Einführungs-/Hauptphase)"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "4. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "4. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.G, "4. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "4. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "4. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "4. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "4. Jahrgang")
					), null, null)
	}),


	/**
	 * Jahrgang 05: 5. Jahrgang / 5. Semester
	 */
	JG_05(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(2005000000L, "05", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "5. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "5. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "5. Jahrgang / 5. Semester"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "5. Semester (Qualifikationsphase)"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.H, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.R, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "5. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "5. Jahrgang")
					), null, null)
	}),


	/**
	 * Jahrgang 06: 6. Jahrgang / 6. Semester
	 */
	JG_06(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(2006000000L, "06", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.BK, "6. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "6. Jahrgang dieser Schulgliederung"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "6. Jahrgang / 6. Semester"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WB, "6. Semester (Qualifikationsphase)"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.H, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.R, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "6. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "6. Jahrgang")
					), null, null)
	}),


	/**
	 * Jahrgang 07: 7. Jahrgang
	 */
	JG_07(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(2007000000L, "07", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.H, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.R, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "7. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "7. Jahrgang")
					), null, null)
	}),


	/**
	 * Jahrgang 08: 8. Jahrgang
	 */
	JG_08(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(2008000000L, "08", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.H, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.R, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "8. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "8. Jahrgang")
					), null, null)
	}),


	/**
	 * Jahrgang 09: 9. Jahrgang
	 */
	JG_09(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(2009000000L, "09", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.H, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.R, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "9. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "9. Jahrgang dieser Schulgliederung")
					), null, null)
	}),


	/**
	 * Jahrgang 10: 10. Jahrgang
	 */
	JG_10(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(2010000000L, "10", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.V, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.H, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.R, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SK, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SR, "10. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SB, "10. Jahrgang dieser Schulgliederung")
					), null, null)
	}),


	/**
	 * Jahrgang 11: 11. Jahrgang
	 */
	JG_11(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(2011000000L, "11", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "11. Jahrgang, Berufskolleg"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "11. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "11. Jahrgang")
					), null, null)
	}),


	/**
	 * Jahrgang 12: 12. Jahrgang
	 */
	JG_12(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(2012000000L, "12", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.HI, "12. Jahrgang, Berufskolleg"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "12. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "12. Jahrgang")
					), null, null)
	}),


	/**
	 * Jahrgang 13: 13. Jahrgang
	 */
	JG_13(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(2013000000L, "13", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "13. Jahrgang"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "13. Jahrgang")
					), null, null)
	}),


	/**
	 * Jahrgang 71: Schule für Kranke
	 */
	JG_71(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(4071000000L, "71", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schule für Kranke"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schule für Kranke"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schule für Kranke"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schule für Kranke")
					), null, null)
	}),


	/**
	 * Jahrgang 85: Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform
	 */
	JG_85(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(4085000000L, "85", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schule für Geistigbehinderte: Berufspraxisstufe in Vollzeitform")
					), null, null)
	}),


	/**
	 * Jahrgang 86: Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform
	 */
	JG_86(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(4086000000L, "86", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.FW, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.WF, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.KS, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.S, "Schule für Geistigbehinderte: Berufspraxisstufe in Teilzeitform")
					), null, null)
	}),


	/**
	 * Jahrgang EF: Einführungsphase
	 */
	JG_EF(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(3000000000L, "EF", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "Einführungsphase"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "Einführungsphase"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "Einführungsphase"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Einführungsphase"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "Einführungsphase")
					), null, null)
	}),


	/**
	 * Jahrgang Q1: Qualifikationsphase 1. Jahr
	 */
	JG_Q1(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(3001000000L, "Q1", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "Qualifikationsphase 1. Jahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "Qualifikationsphase 1. Jahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "Qualifikationsphase 1. Jahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Qualifikationsphase 1. Jahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "Qualifikationsphase 1. Jahr")
					), null, null)
	}),


	/**
	 * Jahrgang Q2: Qualifikationsphase 2. Jahr
	 */
	JG_Q2(new JahrgangsKatalogEintrag[]{
			new JahrgangsKatalogEintrag(3002000000L, "Q2", Arrays.asList(
					new JahrgangsKatalogEintragBezeichnung(Schulform.GE, "Qualifikationsphase 2. Jahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GM, "Qualifikationsphase 2. Jahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.GY, "Qualifikationsphase 2. Jahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.PS, "Qualifikationsphase 2. Jahr"),
					new JahrgangsKatalogEintragBezeichnung(Schulform.SG, "Qualifikationsphase 2. Jahr")
					), null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten des Jahrgangs, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull JahrgangsKatalogEintrag daten;

	/** Die Historie mit den Einträgen des Jahrgangs */
	public final @NotNull JahrgangsKatalogEintrag@NotNull[] historie;

	/** Eine Map mit der Zuordnung des Jahrgangs zu dem Kürzel des Jahrgangs */
	private static final @NotNull HashMap<@NotNull String, Jahrgaenge> _mapKuerzel = new HashMap<>();

	/** Eine Map mit der Zuordnung des Jahrgangs zu der ID des Jahrgangs */
	private static final @NotNull HashMap<@NotNull Long, Jahrgaenge> _mapID = new HashMap<>();

	/** Die Schulformen, bei welchen der Jahrgang vorkommt, für die einzelnen Historieneinträge */
	private @NotNull
	final ArrayList<Schulform> @NotNull[] schulformen;

	/** Die Bezeichnungen bei den Schulformen, bei welchen der Jahrgang vorkommt, für die einzelnen Historieneinträge */
	private @NotNull
	final ArrayList<@NotNull String> @NotNull[] bezeichnungen;


	/**
	 * Erzeugt einen neuen Jahrgang in der Aufzählung.
	 *
	 * @param historie   die Historie des Jahrgangs, welches ein Array von {@link JahrgangsKatalogEintrag} ist
	 */
	@SuppressWarnings("unchecked")
	Jahrgaenge(final @NotNull JahrgangsKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
		// Erzeuge zwei weitere Arrays mit der Schulformzuordnung und den Bezeichnungen für die Historie
		this.schulformen = (@NotNull ArrayList<Schulform> @NotNull[]) Array.newInstance(ArrayList.class, historie.length);
		this.bezeichnungen = (@NotNull ArrayList<@NotNull String> @NotNull[]) Array.newInstance(ArrayList.class, historie.length);
		for (int i = 0; i < historie.length; i++) {
			this.schulformen[i] = new ArrayList<>();
			this.bezeichnungen[i] = new ArrayList<>();
			for (final @NotNull JahrgangsKatalogEintragBezeichnung bez : historie[i].bezeichnungen) {
				final Schulform sf = Schulform.getByKuerzel(bez.schulform);
				if (sf != null)
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
	private static @NotNull HashMap<@NotNull String, Jahrgaenge> getMapJahrgangByKuerzel() {
		if (_mapKuerzel.size() == 0)
			for (final Jahrgaenge j : Jahrgaenge.values())
				_mapKuerzel.put(j.daten.kuerzel, j);
		return _mapKuerzel;
	}


	/**
	 * Gibt eine Map von den IDs der Jahrgänge auf die zugehörigen Jahrgänge
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Jahrgänge auf die zugehörigen Jahrgänge
	 */
	private static @NotNull HashMap<@NotNull Long, Jahrgaenge> getMapJahrgangByID() {
		if (_mapID.size() == 0)
			for (final Jahrgaenge j : Jahrgaenge.values()) {
				for (final JahrgangsKatalogEintrag k : j.historie)
					_mapID.put(k.id, j);
			}
		return _mapID;
	}


	/**
	 * Liefert den Jahrgang anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Jahrgangs
	 *
	 * @return der Jahrgang oder null, falls das Kürzel ungültig ist
	 */
	public static Jahrgaenge getByKuerzel(final String kuerzel) {
		return getMapJahrgangByKuerzel().get(kuerzel);
	}


	/**
	 * Liefert den Jahrgang anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID des Jahrgangs
	 *
	 * @return der Jahrgang oder null, falls die ID ungültig ist
	 */
	public static Jahrgaenge getByID(final Long id) {
		return getMapJahrgangByID().get(id);
	}


	/**
	 * Liefert die Bezeichnung des Jahrgangs für die angegebene Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die Bezeichnung des Jahrgangs oder null, falls die Schulform nicht zulässig ist
	 */
	public String getBezeichnung(final Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return null;
		if (daten.bezeichnungen != null) {
			for (int i = 0; i < daten.bezeichnungen.size(); i++) {
				final JahrgangsKatalogEintragBezeichnung bez = daten.bezeichnungen.get(i);
				final String sfKuerzel = bez.schulform;
				if (sfKuerzel.equals(schulform.daten.kuerzel))
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
	public @NotNull List<Schulform> getSchulformen() {
		return schulformen[historie.length - 1];
	}


	/**
	 * Liefert alle zulässigen Jahrgänge für die angegebene Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform zulässigen Jahrgänge
	 */
	public static @NotNull List<Jahrgaenge> get(final Schulform schulform) {
		final @NotNull ArrayList<Jahrgaenge> result = new ArrayList<>();
		if (schulform == null)
			return result;
		final @NotNull Jahrgaenge@NotNull[] jahrgaenge = Jahrgaenge.values();
		for (int i = 0; i < jahrgaenge.length; i++) {
			final @NotNull Jahrgaenge jahrgang = jahrgaenge[i];
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
	public boolean isKuerzel(final String kuerzel) {
		if (this.daten == null)
			return false;
		return this.daten.kuerzel.equals(kuerzel);
	}


	/**
	 * Prüft anhand des Schulform-Kürzels, ob die Schulform diesen Jahrgang
	 * hat oder nicht.
	 *
	 * @param kuerzel   das Kürzel der Schulform
	 *
	 * @return true, falls der Jahrgang bei der Schulform existiert und ansonsten false
	 */
	public boolean hasSchulformByKuerzel(final String kuerzel) {
		if ((kuerzel == null) || "".equals(kuerzel))
			return false;
		if (daten.bezeichnungen != null) {
			for (int i = 0; i < daten.bezeichnungen.size(); i++) {
				final String sfKuerzel = daten.bezeichnungen.get(i).schulform;
				if (sfKuerzel.equals(kuerzel))
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
	public boolean hasSchulform(final Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return false;
		if (daten.bezeichnungen != null) {
			for (int i = 0; i < daten.bezeichnungen.size(); i++) {
				final String sfKuerzel = daten.bezeichnungen.get(i).schulform;
				if (sfKuerzel.equals(schulform.daten.kuerzel))
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
	public boolean isNachfolgerVon(final Jahrgaenge vergleichsjahrgang, final Schulform schulform, final Schulgliederung gliederung) {
		if (schulform == null)
			return false;
		if (!this.hasSchulform(schulform) || ((vergleichsjahrgang != null) && (!vergleichsjahrgang.hasSchulform(schulform))))
			return false;
		final Schulgliederung gl = (gliederung == null) ? Schulgliederung.getDefault(schulform) : gliederung;
		switch (this) {
			case JG_00:
				return vergleichsjahrgang == null;
			case JG_01:
				return vergleichsjahrgang == null;
			case JG_02:
				return vergleichsjahrgang == JG_01;
			case JG_03:
				return (vergleichsjahrgang == JG_02) || (vergleichsjahrgang == JG_E2) || (vergleichsjahrgang == JG_E3);
			case JG_04:
				return (vergleichsjahrgang == JG_03);
			case JG_05:
				return (vergleichsjahrgang == JG_04);
			case JG_06:
				return (vergleichsjahrgang == JG_05);
			case JG_07:
				return (vergleichsjahrgang == JG_06);
			case JG_08:
				return (vergleichsjahrgang == JG_07);
			case JG_09:
				return (vergleichsjahrgang == JG_08);
			case JG_10:
				return (vergleichsjahrgang == JG_09);
			case JG_11:
				return (vergleichsjahrgang == JG_10);
			case JG_12:
				return (vergleichsjahrgang == JG_11);
			case JG_13:
				return (vergleichsjahrgang == JG_12);
			case JG_71:
				return vergleichsjahrgang == null;
			case JG_85:
				return vergleichsjahrgang == null;
			case JG_86:
				return vergleichsjahrgang == null;
			case JG_90:
				return vergleichsjahrgang == null;
			case JG_91:
				return vergleichsjahrgang == null;
			case JG_92:
				return vergleichsjahrgang == JG_91;
			case JG_E1:
				return (vergleichsjahrgang == JG_00) || (vergleichsjahrgang == null);
			case JG_E2:
				return vergleichsjahrgang == JG_E1;
			case JG_E3:
				return vergleichsjahrgang == JG_E2;
			case JG_EF:
				return (gl == Schulgliederung.GY8) ? (vergleichsjahrgang == JG_09) : (vergleichsjahrgang == JG_10);
			case JG_Q1:
				return vergleichsjahrgang == JG_EF;
			case JG_Q2:
				return vergleichsjahrgang == JG_Q1;
			default:
				return false;
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
	public boolean isVorgaengerVon(final Jahrgaenge vergleichsjahrgang, final Schulform schulform, final Schulgliederung gliederung) {
		if (schulform == null)
			return false;
		if (!this.hasSchulform(schulform) || ((vergleichsjahrgang != null) && (!vergleichsjahrgang.hasSchulform(schulform))))
			return false;
		final Schulgliederung gl = (gliederung == null) ? Schulgliederung.getDefault(schulform) : gliederung;
		switch (this) {
			case JG_00:
				return vergleichsjahrgang == JG_01;
			case JG_01:
				return vergleichsjahrgang == JG_02;
			case JG_02:
				return vergleichsjahrgang == JG_03;
			case JG_03:
				return vergleichsjahrgang == JG_04;
			case JG_04:
				return vergleichsjahrgang == JG_05;
			case JG_05:
				return vergleichsjahrgang == JG_06;
			case JG_06:
				return vergleichsjahrgang == JG_07;
			case JG_07:
				return vergleichsjahrgang == JG_08;
			case JG_08:
				return vergleichsjahrgang == JG_09;
			case JG_09:
				return (vergleichsjahrgang == JG_10) || ((schulform == Schulform.GY) && ((gl == Schulgliederung.GY8) || (gl == Schulgliederung.DEFAULT)) && (vergleichsjahrgang == JG_EF));
			case JG_10:
				return (vergleichsjahrgang == JG_11) || ((schulform.daten.hatGymOb) && (vergleichsjahrgang == JG_EF));
			case JG_11:
				return (vergleichsjahrgang == JG_12);
			case JG_12:
				return (vergleichsjahrgang == JG_13);
			case JG_13:
				return (vergleichsjahrgang == null);
			case JG_71:
				return vergleichsjahrgang == null;
			case JG_85:
				return vergleichsjahrgang == null;
			case JG_86:
				return vergleichsjahrgang == null;
			case JG_90:
				return vergleichsjahrgang == null;
			case JG_91:
				return (vergleichsjahrgang == JG_92);
			case JG_92:
				return vergleichsjahrgang == null;
			case JG_E1:
				return vergleichsjahrgang == JG_E2;
			case JG_E2:
				return (vergleichsjahrgang == JG_E3) || (vergleichsjahrgang == JG_03);
			case JG_E3:
				return vergleichsjahrgang == JG_03;
			case JG_EF:
				return vergleichsjahrgang == JG_Q1;
			case JG_Q1:
				return vergleichsjahrgang == JG_Q2;
			case JG_Q2:
				return vergleichsjahrgang == null;
			default:
				return false;
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
	public boolean isMoeglicherNachfolgerVon(final Jahrgaenge Vergleichsjahrgang) {
		switch (this) {
			case JG_00:
				return Vergleichsjahrgang == null;
			case JG_01:
				return Vergleichsjahrgang == null;
			case JG_02:
				return Vergleichsjahrgang == JG_01;
			case JG_03:
				return (Vergleichsjahrgang == JG_02) || (Vergleichsjahrgang == JG_E2) || (Vergleichsjahrgang == JG_E3);
			case JG_04:
				return (Vergleichsjahrgang == JG_03);
			case JG_05:
				return (Vergleichsjahrgang == JG_04);
			case JG_06:
				return (Vergleichsjahrgang == JG_05);
			case JG_07:
				return (Vergleichsjahrgang == JG_06);
			case JG_08:
				return (Vergleichsjahrgang == JG_07);
			case JG_09:
				return (Vergleichsjahrgang == JG_08);
			case JG_10:
				return (Vergleichsjahrgang == JG_09);
			case JG_11:
				return (Vergleichsjahrgang == JG_10);
			case JG_12:
				return (Vergleichsjahrgang == JG_11);
			case JG_13:
				return (Vergleichsjahrgang == JG_12);
			case JG_71:
				return Vergleichsjahrgang == null;
			case JG_85:
				return Vergleichsjahrgang == null;
			case JG_86:
				return Vergleichsjahrgang == null;
			case JG_90:
				return Vergleichsjahrgang == null;
			case JG_91:
				return Vergleichsjahrgang == null;
			case JG_92:
				return Vergleichsjahrgang == JG_91;
			case JG_E1:
				return (Vergleichsjahrgang == JG_00) || (Vergleichsjahrgang == null);
			case JG_E2:
				return Vergleichsjahrgang == JG_E1;
			case JG_E3:
				return Vergleichsjahrgang == JG_E2;
			case JG_EF:
				return (Vergleichsjahrgang == JG_09) || (Vergleichsjahrgang == JG_10);
			case JG_Q1:
				return Vergleichsjahrgang == JG_EF;
			case JG_Q2:
				return Vergleichsjahrgang == JG_Q1;
			default:
				return false;
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
	public boolean isMoeglicherVorgaengerVon(final Jahrgaenge Vergleichsjahrgang) {
		switch (this) {
			case JG_00:
				return Vergleichsjahrgang == JG_01;
			case JG_01:
				return Vergleichsjahrgang == JG_02;
			case JG_02:
				return Vergleichsjahrgang == JG_03;
			case JG_03:
				return Vergleichsjahrgang == JG_04;
			case JG_04:
				return Vergleichsjahrgang == JG_05;
			case JG_05:
				return Vergleichsjahrgang == JG_06;
			case JG_06:
				return Vergleichsjahrgang == JG_07;
			case JG_07:
				return Vergleichsjahrgang == JG_08;
			case JG_08:
				return Vergleichsjahrgang == JG_09;
			case JG_09:
				return (Vergleichsjahrgang == JG_10) || (Vergleichsjahrgang == JG_EF);
			case JG_10:
				return (Vergleichsjahrgang == JG_11) || (Vergleichsjahrgang == JG_EF);
			case JG_11:
				return (Vergleichsjahrgang == JG_12);
			case JG_12:
				return (Vergleichsjahrgang == JG_13);
			case JG_13:
				return (Vergleichsjahrgang == null);
			case JG_71:
				return Vergleichsjahrgang == null;
			case JG_85:
				return Vergleichsjahrgang == null;
			case JG_86:
				return Vergleichsjahrgang == null;
			case JG_90:
				return Vergleichsjahrgang == null;
			case JG_91:
				return (Vergleichsjahrgang == JG_92);
			case JG_92:
				return Vergleichsjahrgang == null;
			case JG_E1:
				return Vergleichsjahrgang == JG_E2;
			case JG_E2:
				return (Vergleichsjahrgang == JG_E3) || (Vergleichsjahrgang == JG_03);
			case JG_E3:
				return Vergleichsjahrgang == JG_03;
			case JG_EF:
				return Vergleichsjahrgang == JG_Q1;
			case JG_Q1:
				return Vergleichsjahrgang == JG_Q2;
			case JG_Q2:
				return Vergleichsjahrgang == null;
			default:
				return false;
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
	public boolean hatLernbereichsnote1(final @NotNull Schulform schulform, final Schulgliederung schulgliederung, final int schuljahr) {
		return switch (schulform) {
			case R, SR, H, S, FW, WF -> (this == Jahrgaenge.JG_10);
			case GY, SG -> (schulgliederung == Schulgliederung.GY8) || (schulgliederung == Schulgliederung.DEFAULT)
				? (this == Jahrgaenge.JG_EF) : (this == Jahrgaenge.JG_10);
			case GM, GE, PS, SK -> ((this == Jahrgaenge.JG_10) && (schuljahr <= 2024))
				|| ((this == Jahrgaenge.JG_09) && (schuljahr <= 2023))
				|| ((this == Jahrgaenge.JG_08) && (schuljahr <= 2022));
			case HI -> (this == Jahrgaenge.JG_10);
			case KS -> (this == Jahrgaenge.JG_10);
			case V -> (this == Jahrgaenge.JG_10);
			case BK, SB, WB, G -> false;
		};
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
	public String getLernbereichsnote1Bezeichnung(final @NotNull Schulform schulform, final Schulgliederung schulgliederung, final int schuljahr) {
		if (!hatLernbereichsnote1(schulform, schulgliederung, schuljahr))
			return null;
		return switch (schulform) {
			case H, GM, GE, PS, SK -> "Arbeitslehre";
			default -> "Gesellschaftslehre";
		};
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
	public boolean hatLernbereichsnote2(final @NotNull Schulform schulform, final Schulgliederung schulgliederung, final int schuljahr) {
		return switch (schulform) {
			case R, SR, H, S, FW, WF -> (this == Jahrgaenge.JG_10);
			case GY, SG -> (schulgliederung == Schulgliederung.GY8) || (schulgliederung == Schulgliederung.DEFAULT)
				? (this == Jahrgaenge.JG_EF) : (this == Jahrgaenge.JG_10);
			case GM, GE, PS, SK -> ((this == Jahrgaenge.JG_10) || (this == Jahrgaenge.JG_09) || (this == Jahrgaenge.JG_08));
			case HI -> (this == Jahrgaenge.JG_10);
			case KS -> (this == Jahrgaenge.JG_10);
			case V -> (this == Jahrgaenge.JG_10);
			case BK, SB, WB, G -> false;
		};
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
	public String getLernbereichsnote2Bezeichnung(final @NotNull Schulform schulform, final Schulgliederung schulgliederung, final int schuljahr) {
		if (!hatLernbereichsnote2(schulform, schulgliederung, schuljahr))
			return null;
		return "Naturwissenschaft";
	}

}
