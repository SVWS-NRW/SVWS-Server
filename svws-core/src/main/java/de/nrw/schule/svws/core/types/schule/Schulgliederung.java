package de.nrw.schule.svws.core.types.schule;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.nrw.schule.svws.core.data.schule.SchulgliederungKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Schulgliederungen zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Schulgliederung {

	/**
	 * Schulgliederung DEFAULT: 
	 *   Standard für diese Schulform
	 */
	DEFAULT(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(0, "***", false, Arrays.asList(
				Schulform.FW, Schulform.HI, Schulform.WF,
				Schulform.G, 
				Schulform.GE, 
				Schulform.GY, 
				Schulform.H, 
				Schulform.PS, 
				Schulform.R, 
				Schulform.SG, 
				Schulform.SK, 
				Schulform.SR, 
				Schulform.V
			), false, false, "Standard für diese Schulform", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung A01: 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 01 (Fachklassen (BS; TZ))
	 */
	A01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1001000, "A01", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachklassen (BS; TZ)", BerufskollegAnlage.A, "01", 10, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BS 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA9A,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung A02: 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 02 (Fachklassen/Fachhochschulreife (BS/FHR; TZ))
	 */
	A02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1002000, "A02", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachklassen/Fachhochschulreife (BS/FHR; TZ)", BerufskollegAnlage.A, "02", 10, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BS 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA9A,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung A03: 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 03 (Fachklassen/erweiterte Zusatzqualifikation (BS/ZQ; TZ))
	 */
	A03(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1003000, "A03", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachklassen/erweiterte Zusatzqualifikation (BS/ZQ; TZ)", BerufskollegAnlage.A, "03", 10, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BS 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA9A,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung A04: 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 04 (Fachklassen mit erweitertem Stützunterricht (BS/Stütz; TZ))
	 */
	A04(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1004000, "A04", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachklassen mit erweitertem Stützunterricht (BS/Stütz; TZ)", BerufskollegAnlage.A, "04", 10, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BS 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA9A,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung A05 (ausgelaufen): 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 05 (Berufsorientierungsjahr (BV; VZ))
	 */
	A05(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1005000, "A05", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsorientierungsjahr (BV; VZ)", BerufskollegAnlage.A, "05", 20, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BV 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung A06 (ausgelaufen): 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 06 (Berufsgrundschuljahr (BG; VZ))
	 */
	A06(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1006000, "A06", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsgrundschuljahr (BG; VZ)", BerufskollegAnlage.A, "06", 30, true, Arrays.asList(
				SchulabschlussBerufsbildend.BG 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung A07 (ausgelaufen): 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 07 (Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; TZ) bzw. Werkstattjahr (BS 1j;TZ))
	 */
	A07(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1007000, "A07", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; TZ) bzw. Werkstattjahr (BS 1j;TZ)", BerufskollegAnlage.A, "07", 40, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BS 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung A08 (ausgelaufen): 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 08 (Vorpraktikum (VP))
	 */
	A08(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1008000, "A08", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Vorpraktikum (VP)", BerufskollegAnlage.A, "08", 50, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.VP 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung A09 (ausgelaufen): 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 09 (Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; VZ))
	 */
	A09(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1009000, "A09", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; VZ)", BerufskollegAnlage.A, "09", 55, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BS 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung A10 (auslaufend): 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 10 (Berufsabschluss/mittlerer Schulabschluss (BKAZVO) (BAB/FOR; VZ))
	 */
	A10(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1010000, "A10", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, false, "Berufsabschluss/mittlerer Schulabschluss (BKAZVO) (BAB/FOR; VZ)", BerufskollegAnlage.A, "10", 56, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.MSA
			), null, null)
	}),

	/**
	 * Schulgliederung A11 (auslaufend): 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 11 (Berufsabschluss/Fachhochschulreife (BKAZVO) (BAB/FHR; VZ))
	 */
	A11(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1011000, "A11", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, false, "Berufsabschluss/Fachhochschulreife (BKAZVO) (BAB/FHR; VZ)", BerufskollegAnlage.A, "11", 56, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung A12: 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 12 (Ausbildungsvorbereitung (BS 1j; VZ))
	 */
	A12(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1012000, "A12", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Ausbildungsvorbereitung (BS 1j; VZ)", BerufskollegAnlage.A, "12", 57, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.VORB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA9A,
				SchulabschlussAllgemeinbildend.HA9_FOE
			), null, null)
	}),

	/**
	 * Schulgliederung A13: 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 13 (Ausbildungsvorbereitung (BS 1j; TZ))
	 */
	A13(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1013000, "A13", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Ausbildungsvorbereitung (BS 1j; TZ)", BerufskollegAnlage.A, "13", 58, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.VORB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA9A
			), null, null)
	}),

	/**
	 * Schulgliederung A14: 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 14 (Berufsabschluss (nach §50 BBiG/§40 HwO)/mittlerer Schulabschluss (BAB/FOR; VZ))
	 */
	A14(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1014000, "A14", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufsabschluss (nach §50 BBiG/§40 HwO)/mittlerer Schulabschluss (BAB/FOR; VZ)", BerufskollegAnlage.A, "14", 59, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung A15: 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 15 (Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife BAB/FHR; VZ)
	 */
	A15(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1015000, "A15", true, Arrays.asList(
				Schulform.BK
			), false, false, "Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife BAB/FHR; VZ", BerufskollegAnlage.A, "15", 59, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung A16: 
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung), 
	 *   Typ 16 (Fachklassen (nach §2 BKAZVO) BAB; VZ)
	 */
	A16(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(1016000, "A16", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachklassen (nach §2 BKAZVO) BAB; VZ", BerufskollegAnlage.A, "16", 59, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung AB: 
	 *   Schule für Kranke: Allgemeinbildend
	 */
	AB(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(90001000, "AB", false, Arrays.asList(
				Schulform.FW, Schulform.HI, Schulform.WF,
				Schulform.KS, Schulform.S
			), false, false, "Schule für Kranke: Allgemeinbildend", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung B01 (ausgelaufen): 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 01 (Berufsabschluss/Fachoberschulreife (BAB/FOR 2j; VZ))
	 */
	B01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2001000, "B01", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsabschluss/Fachoberschulreife (BAB/FOR 2j; VZ)", BerufskollegAnlage.B, "01", 60, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung B02 (ausgelaufen): 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 02 (Berufsgrundbildung/Fachoberschulreife (BG/FOR 2j; VZ))
	 */
	B02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2002000, "B02", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsgrundbildung/Fachoberschulreife (BG/FOR 2j; VZ)", BerufskollegAnlage.B, "02", 70, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BG 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung B03 (ausgelaufen): 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 03 (Berufsgrundbildung (für Schüler mit FOR) (BG 1j; VZ))
	 */
	B03(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2003000, "B03", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsgrundbildung (für Schüler mit FOR) (BG 1j; VZ)", BerufskollegAnlage.B, "03", 80, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BG 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung B04 (ausgelaufen): 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 04 (Berufsabschluss/Fachoberschulreife (BAB/FOR; VZ))
	 */
	B04(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2004000, "B04", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsabschluss/Fachoberschulreife (BAB/FOR; VZ)", BerufskollegAnlage.B, "04", 90, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung B05 (ausgelaufen): 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 05 (Berufsabschluss/Fachhochschulreife (BAB/FHR; VZ))
	 */
	B05(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2005000, "B05", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsabschluss/Fachhochschulreife (BAB/FHR; VZ)", BerufskollegAnlage.B, "05", 90, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung B06: 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 06 (Berufliche Kenntnisse/Hauptschulabschluss Kl. 10 (BK/HSA10; 1j. VZ))
	 */
	B06(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2006000, "B06", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufliche Kenntnisse/Hauptschulabschluss Kl. 10 (BK/HSA10; 1j. VZ)", BerufskollegAnlage.B, "06", 91, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BK 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10
			), null, null)
	}),

	/**
	 * Schulgliederung B07: 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 07 (Berufliche Kenntnisse/mittlerer Schulabschluss (BK/FOR; 1j. VZ))
	 */
	B07(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2007000, "B07", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufliche Kenntnisse/mittlerer Schulabschluss (BK/FOR; 1j. VZ)", BerufskollegAnlage.B, "07", 92, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BK 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung B08: 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 08 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 2j. VZ))
	 */
	B08(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2008000, "B08", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 2j. VZ)", BerufskollegAnlage.B, "08", 93, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung B09: 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 09 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 3j. TZ))
	 */
	B09(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2009000, "B09", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 3j. TZ)", BerufskollegAnlage.B, "09", 93, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung B10: 
	 *   Anlage B (Berufsfachschule), 
	 *   Typ 10 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 4j. TZ))
	 */
	B10(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(2010000, "B10", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 4j. TZ)", BerufskollegAnlage.B, "10", 93, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung BT: 
	 *   Schule für Kranke: Berufsbildend (Teilzeit)
	 */
	BT(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(90002000, "BT", false, Arrays.asList(
				Schulform.FW, Schulform.HI, Schulform.WF,
				Schulform.KS, Schulform.S
			), false, false, "Schule für Kranke: Berufsbildend (Teilzeit)", null, null, null, false, Arrays.asList(
				// TODO
			), Arrays.asList(
				// TODO
			), null, null)
	}),

	/**
	 * Schulgliederung BV: 
	 *   Schule für Kranke: Berufsbildend (Vollzeit)
	 */
	BV(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(90003000, "BV", false, Arrays.asList(
				Schulform.FW, Schulform.HI,Schulform.WF,
				Schulform.KS, Schulform.S
			), false, false, "Schule für Kranke: Berufsbildend (Vollzeit)", null, null, null, true, Arrays.asList(
				// TODO
			), Arrays.asList(
				// TODO
			), null, null)
	}),

	/**
	 * Schulgliederung C01: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 01 (Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum) BAB/FHR 3j; VZ BFS)
	 */
	C01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3001000, "C01", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum) BAB/FHR 3j; VZ BFS", BerufskollegAnlage.C, "01", 100, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR_S,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung C02: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 02 (Berufsabschluss f. Hochschulzugangsberechtigte (BAB 2j; VZ) BFS)
	 */
	C02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3002000, "C02", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufsabschluss f. Hochschulzugangsberechtigte (BAB 2j; VZ) BFS", BerufskollegAnlage.C, "02", 110, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung C03: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 03 (Berufliche Kenntnisse/FHR (BK/FHR 2j; VZ) HBFS)
	 */
	C03(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3003000, "C03", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufliche Kenntnisse/FHR (BK/FHR 2j; VZ) HBFS", BerufskollegAnlage.C, "03", 120, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BK 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung C04 (ausgelaufen): 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 04 (Berufliche Kenntnisse/Sonderform für Abiturienten (BK 1j; VZ) HBFS)
	 */
	C04(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3004000, "C04", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufliche Kenntnisse/Sonderform für Abiturienten (BK 1j; VZ) HBFS", BerufskollegAnlage.C, "04", 130, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BK 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung C05: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 05 (Fachoberschule Kl. 11 (BK/FHR 1j; TZ))
	 */
	C05(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3005000, "C05", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachoberschule Kl. 11 (BK/FHR 1j; TZ)", BerufskollegAnlage.C, "05", 140, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.VERS 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.VS_11
			), null, null)
	}),

	/**
	 * Schulgliederung C06: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 06 (Fachoberschule Kl. 12S (BK/FHR 1j; VZ))
	 */
	C06(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3006000, "C06", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachoberschule Kl. 12S (BK/FHR 1j; VZ)", BerufskollegAnlage.C, "06", 140, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BK 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung C07: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 07 (Fachoberschule Kl. 12B (BK/FHR 2j; TZ))
	 */
	C07(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3007000, "C07", true, Arrays.asList(
				Schulform.BK, Schulform.SB,
				Schulform.WB
			), false, false, "Fachoberschule Kl. 12B (BK/FHR 2j; TZ)", BerufskollegAnlage.C, "07", 140, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.VBK 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung C08: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 08 (Fachoberschule Kl. 12B (BK/FHR 1j; VZ))
	 */
	C08(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3008000, "C08", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachoberschule Kl. 12B (BK/FHR 1j; VZ)", BerufskollegAnlage.C, "08", 140, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.VBK 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung C09 (ausgelaufen): 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 09 (Berufspraktikum Erzieher/innen (Vollzeit) (BP/Erz 1j; VZ))
	 */
	C09(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3009000, "C09", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufspraktikum Erzieher/innen (Vollzeit) (BP/Erz 1j; VZ)", BerufskollegAnlage.C, "09", 150, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BP 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung C10 (ausgelaufen): 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 10 (Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ))
	 */
	C10(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3010000, "C10", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ)", BerufskollegAnlage.C, "10", 150, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BP 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung C11: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 11 (Fachoberschule Kl. 12B (BK/FHR 3j; TZ))
	 */
	C11(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3011000, "C11", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachoberschule Kl. 12B (BK/FHR 3j; TZ)", BerufskollegAnlage.C, "11", 141, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.VBK 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung C12: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 12 (Berufsabschluss/Fachhochschulreife (mit  Berufspraktikum) BAB/FHR 3,5j; VZ)
	 */
	C12(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3012000, "C12", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufsabschluss/Fachhochschulreife (mit  Berufspraktikum) BAB/FHR 3,5j; VZ", BerufskollegAnlage.C, "12", 145, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR_S,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung C13: 
	 *   Anlage C (Berufsfachschule und Fachoberschule), 
	 *   Typ 13 (Berufsabschluss/Fachhochschulreife (gestuft) (BAB/FHR 3j; VZ))
	 */
	C13(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(3013000, "C13", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufsabschluss/Fachhochschulreife (gestuft) (BAB/FHR 3j; VZ)", BerufskollegAnlage.C, "13", 146, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BK, 
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR_S,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung D01:
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *   Typ 01 (Berufsabschluss/Allg. Hochschulreife (mit Berufspraktikum) (BAB/AHR 4j; VZ))
	 */
	D01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(4001000, "D01", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufsabschluss/Allg. Hochschulreife (mit Berufspraktikum) (BAB/AHR 4j; VZ)", BerufskollegAnlage.D, "01", 160, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BK, 
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR_S,
				SchulabschlussAllgemeinbildend.FHR,
				SchulabschlussAllgemeinbildend.ABITUR
			), null, null)
	}),

	/**
	 * Schulgliederung D02: 
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule), 
	 *   Typ 02 (Berufl. Kenntnisse/Allg. Hochschulreife (BK/AHR 3j; VZ))
	 */
	D02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(4002000, "D02", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Berufl. Kenntnisse/Allg. Hochschulreife (BK/AHR 3j; VZ)", BerufskollegAnlage.D, "02", 170, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BK 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.HA10,
				SchulabschlussAllgemeinbildend.MSA_Q,
				SchulabschlussAllgemeinbildend.FHR_S,
				SchulabschlussAllgemeinbildend.ABITUR
			), null, null)
	}),

	/**
	 * Schulgliederung D03 (ausgelaufen): 
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule), 
	 *   Typ 03 (Berufspraktikum (Vollzeit) (BP 1j; VZ))
	 */
	D03(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(4003000, "D03", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufspraktikum (Vollzeit) (BP 1j; VZ)", BerufskollegAnlage.D, "03", 180, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BP 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung D04 (ausgelaufen): 
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule), 
	 *   Typ 04 (Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ))
	 */
	D04(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(4004000, "D04", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ)", BerufskollegAnlage.D, "04", 190, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BP 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung D05: 
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule), 
	 *   Typ 05 (AHR (gem. § 2 Abs. 3 Anlage D) (AHR 1j; VZ) FOS13)
	 */
	D05(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(4005000, "D05", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "AHR (gem. § 2 Abs. 3 Anlage D) (AHR 1j; VZ) FOS13", BerufskollegAnlage.D, "05", 200, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.VBK
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.FGHR,
				SchulabschlussAllgemeinbildend.ABITUR
			), null, null)
	}),

	/**
	 * Schulgliederung D06: 
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule), 
	 *   Typ 06 (AHR (gem. § 2 Abs. 3 Anlage D) (AHR 2j; TZ) FOS13)
	 */
	D06(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(4006000, "D06", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "AHR (gem. § 2 Abs. 3 Anlage D) (AHR 2j; TZ) FOS13", BerufskollegAnlage.D, "06", 200, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.VBK
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.FGHR,
				SchulabschlussAllgemeinbildend.ABITUR
			), null, null)
	}),

	/**
	 * Schulgliederung E01: 
	 *   Anlage E (Fachschule), 
	 *   Typ 01 (Fachschule Vollzeit (BW 2j; VZ))
	 */
	E01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5001000, "E01", true, Arrays.asList(
				Schulform.BK
			), false, false, "Fachschule Vollzeit (BW 2j; VZ)", BerufskollegAnlage.E, "01", 210, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung E02: 
	 *   Anlage E (Fachschule), 
	 *   Typ 02 (Fachschule Teilzeit (BW 4j; TZ))
	 */
	E02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5002000, "E02", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Fachschule Teilzeit (BW 4j; TZ)", BerufskollegAnlage.E, "02", 210, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung E03: 
	 *   Anlage E (Fachschule), 
	 *   Typ 03 (Fachschule (verkürzt) Vollzeit (BW 1j; VZ))
	 */
	E03(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5003000, "E03", true, Arrays.asList(
				Schulform.BK
			), false, false, "Fachschule (verkürzt) Vollzeit (BW 1j; VZ)", BerufskollegAnlage.E, "03", 220, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.AUFB,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung E04: 
	 *   Anlage E (Fachschule), 
	 *   Typ 04 (Fachschule (verkürzt) Teilzeit (BW 2j; TZ))
	 */
	E04(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5004000, "E04", true, Arrays.asList(
				Schulform.BK
			), false, false, "Fachschule (verkürzt) Teilzeit (BW 2j; TZ)", BerufskollegAnlage.E, "04", 220, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.AUFB,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung E05: 
	 *   Anlage E (Fachschule), 
	 *   Typ 05 (Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 3j; VZ))
	 */
	E05(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5005000, "E05", true, Arrays.asList(
				Schulform.BK
			), false, false, "Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 3j; VZ)", BerufskollegAnlage.E, "05", 230, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung E06 (ausgelaufen): 
	 *   Anlage E (Fachschule), 
	 *   Typ 06 (Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 1j; VZ))
	 */
	E06(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5006000, "E06", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 1j; VZ)", BerufskollegAnlage.E, "06", 240, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung E07: 
	 *   Anlage E (Fachschule), 
	 *   Typ 07 (Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 6j; TZ))
	 */
	E07(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5007000, "E07", true, Arrays.asList(
				Schulform.BK
			), false, false, "Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 6j; TZ)", BerufskollegAnlage.E, "07", 230, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung E08 (ausgelaufen): 
	 *   Anlage E (Fachschule), 
	 *   Typ 08 (Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 2j; TZ))
	 */
	E08(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5008000, "E08", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 2j; TZ)", BerufskollegAnlage.E, "08", 240, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung E09 (ausgelaufen): 
	 *   Anlage E (Fachschule), 
	 *   Typ 09 (Fachschule (Sonderform) Vollzeit (BW 3j; VZ))
	 */
	E09(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5009000, "E09", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Fachschule (Sonderform) Vollzeit (BW 3j; VZ)", BerufskollegAnlage.E, "09", 250, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung E10 (ausgelaufen): 
	 *   Anlage E (Fachschule), 
	 *   Typ 10 (Fachschule (Sonderform) Teilzeit (BW 6j; TZ))
	 */
	E10(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5010000, "E10", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Fachschule (Sonderform) Teilzeit (BW 6j; TZ)", BerufskollegAnlage.E, "10", 260, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BW 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung E11 (ausgelaufen): 
	 *   Anlage E (Fachschule), 
	 *   Typ 11 (Berufspraktikum Erzieher/innen (FS/BP/Erz 1j; VZ))
	 */
	E11(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5011000, "E11", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufspraktikum Erzieher/innen (FS/BP/Erz 1j; VZ)", BerufskollegAnlage.E, "11", 270, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BP 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung E12 (ausgelaufen): 
	 *   Anlage E (Fachschule), 
	 *   Typ 12 (Berufspraktikum Erzieher/innen (FS/BP/Erz 2j; TZ))
	 */
	E12(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5012000, "E12", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufspraktikum Erzieher/innen (FS/BP/Erz 2j; TZ)", BerufskollegAnlage.E, "12", 270, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BP 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung E13: 
	 *   Anlage E (Fachschule), 
	 *   Typ 13 (Fachschule Teilzeit (BW 3j; TZ))
	 */
	E13(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(5013000, "E13", true, Arrays.asList(
				Schulform.BK
			), false, false, "Fachschule Teilzeit (BW 3j; TZ)", BerufskollegAnlage.E, "13", 210, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BW
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung ER: 
	 *   kooperative Form: Erweiterungsebene
	 */
	ER(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(14001000, "ER", false, Arrays.asList(
				Schulform.SK
			), false, false, "kooperative Form: Erweiterungsebene", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung EVB: 
	 *   Evangelische Bekenntnisschule
	 */
	EVB(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(80001000, "EVB", false, Arrays.asList(
				Schulform.G
			), false, false, "Evangelische Bekenntnisschule", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung G01: 
	 *   Aufbaugymnasium
	 */
	G01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(60001000, "G01", false, Arrays.asList(
				Schulform.GY
			), false, false, "Aufbaugymnasium", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung G02: 
	 *   Bildungsgang Abendgymnasium
	 */
	G02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(18001000, "G02", false, Arrays.asList(
				Schulform.WB
			), false, false, "Bildungsgang Abendgymnasium", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung GGS (auslaufend): 
	 *   Gemeinschaftsschule (auslaufend) integrierte Form
	 */
	GGS(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(15001000, "GGS", false, Arrays.asList(
				Schulform.GE
			), true, false, "Gemeinschaftsschule (auslaufend) integrierte Form", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung GGY (auslaufend): 
	 *   Gemeinschaftsschule (auslaufend) Gymnasialbildungsgang
	 */
	GGY(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(15002000, "GGY", false, Arrays.asList(
				Schulform.GE
			), true, false, "Gemeinschaftsschule (auslaufend) Gymnasialbildungsgang", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung GMS: 
	 *   Gemeinschaftsschule
	 */
	GMS(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(80002000, "GMS", false, Arrays.asList(
				Schulform.G
			), false, false, "Gemeinschaftsschule", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung GR: 
	 *   kooperative Form: Grundebene
	 */
	GR(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(15003000, "GR", false, Arrays.asList(
				Schulform.GE, 
				Schulform.SK
			), false, false, "kooperative Form: Grundebene", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung GRH (auslaufend): 
	 *   Gemeinschaftsschule auslaufend: teilintegrierte Form
	 */
	GRH(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(15004000, "GRH", false, Arrays.asList(
				Schulform.GE
			), true, false, "Gemeinschaftsschule auslaufend: teilintegrierte Form", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung GS: 
	 *   integrierte Form (Binnendifferenzierung)
	 */
	GS(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(50001000, "GS", false, Arrays.asList(
				Schulform.GM
			), false, false, "integrierte Form (Binnendifferenzierung)", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung GY: 
	 *   Bildungsgang Gymnasium
	 */
	GY(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(15005000, "GY", false, Arrays.asList(
				Schulform.GE, 
				Schulform.GM, 
				Schulform.SK
			), false, false, "Bildungsgang Gymnasium", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung GY8: 
	 *   Bildungsgang G8-Gymnasium
	 */
	GY8(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(60002000, "GY8", false, Arrays.asList(
				Schulform.GE, 
				Schulform.GY
			), false, false, "Bildungsgang G8-Gymnasium", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung GY9: 
	 *   Bildungsgang G9-Gymnasium
	 */
	GY9(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(60003000, "GY9", false, Arrays.asList(
				Schulform.GE, 
				Schulform.GY
			), false, false, "Bildungsgang G9-Gymnasium", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung H: 
	 *   Bildungsgang Hauptschule
	 */
	H(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(10001000, "H", false, Arrays.asList(
				Schulform.GM, 
				Schulform.R, 
				Schulform.SK
			), false, false, "Bildungsgang Hauptschule", null, "  ", null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung H01: 
	 *   Berufsgrundbildung (Jahrgang 07 bis 10)
	 */
	H01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(17001000, "H01", false, Arrays.asList(
				Schulform.FW, Schulform.HI, Schulform.WF
			), false, false, "Berufsgrundbildung (Jahrgang 07 bis 10)", BerufskollegAnlage.H, "01", 980, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung H02: 
	 *   Berufsausbildung (Jahrgang 11 und 12)
	 */
	H02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(17002000, "H02", false, Arrays.asList(
				Schulform.FW, Schulform.HI, Schulform.WF
			), false, false, "Berufsausbildung (Jahrgang 11 und 12)", BerufskollegAnlage.H, "02", 940, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung K02: 
	 *   Bildungsgang Kolleg
	 */
	K02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(18002000, "K02", false, Arrays.asList(
				Schulform.FW, Schulform.HI, Schulform.WF,
				Schulform.WB
			), false, false, "Bildungsgang Kolleg", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung R: 
	 *   Bildungsgang Realschule
	 */
	R(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(10002000, "R", false, Arrays.asList(
				Schulform.GM, 
				Schulform.H, 
				Schulform.SK
			), false, false, "Bildungsgang Realschule", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung R00: 
	 *   Realschule
	 */
	R00(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(10003000, "R00", false, Arrays.asList(
				Schulform.KS, Schulform.S
			), false, false, "Realschule", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung R01: 
	 *   Aufbaurealschule
	 */
	R01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(10004000, "R01", false, Arrays.asList(
				Schulform.R
			), false, false, "Aufbaurealschule", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung R02: 
	 *   Bildungsgang Abendrealschule
	 */
	R02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(18003000, "R02", false, Arrays.asList(
				Schulform.WB
			), false, false, "Bildungsgang Abendrealschule", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung RH: 
	 *   teilintegrierte Form
	 */
	RH(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(10005000, "RH", false, Arrays.asList(
				Schulform.GE, 
				Schulform.GM, 
				Schulform.SK
			), false, false, "teilintegrierte Form", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung RKB: 
	 *   Katholische  Bekenntnisschule
	 */
	RKB(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(80003000, "RKB", false, Arrays.asList(
				Schulform.G
			), false, false, "Katholische  Bekenntnisschule", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung SRH (auslaufend): 
	 *   Sekundarschule, teilintegrierte Form (auslaufend)
	 */
	SRH(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(10006000, "SRH", false, Arrays.asList(
				Schulform.GE
			), true, false, "Sekundarschule, teilintegrierte Form (auslaufend)", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung SSI (auslaufend): 
	 *   Sekundarschule, integrierte Form (auslaufend)
	 */
	SSI(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(10007000, "SSI", false, Arrays.asList(
				Schulform.GE
			), true, false, "Sekundarschule, integrierte Form (auslaufend)", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung X01 (ausgelaufen): 
	 *   Anlage X (Ehemalige Kollegschule), 
	 *   Typ 01 (Allgemeine Hochschulreife (ausgelaufen) (AHR 3j; VZ))
	 */
	X01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(6001000, "X01", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Allgemeine Hochschulreife (ausgelaufen) (AHR 3j; VZ)", BerufskollegAnlage.X, "01", 280, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.FHR,
				SchulabschlussAllgemeinbildend.ABITUR
			), null, null)
	}),

	/**
	 * Schulgliederung X02 (ausgelaufen): 
	 *   Anlage X (Ehemalige Kollegschule), 
	 *   Typ 02 (Fachhochschulreife (ausgelaufen) (FHR 2j; VZ))
	 */
	X02(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(6002000, "X02", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Fachhochschulreife (ausgelaufen) (FHR 2j; VZ)", BerufskollegAnlage.X, "02", 290, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung X03 (ausgelaufen): 
	 *   Anlage X (Ehemalige Kollegschule), 
	 *   Typ 03 (Fachhochschulreife (ausgelaufen) (FHR 3j; TZ))
	 */
	X03(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(6003000, "X03", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Fachhochschulreife (ausgelaufen) (FHR 3j; TZ)", BerufskollegAnlage.X, "03", 300, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.FHR
			), null, null)
	}),

	/**
	 * Schulgliederung X04 (ausgelaufen): 
	 *   Anlage X (Ehemalige Kollegschule), 
	 *   Typ 04 (Berufspraktikum Fremdsprachenassistent (ausgelaufen) (BP/FAss 1j; TZ))
	 */
	X04(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(6004000, "X04", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufspraktikum Fremdsprachenassistent (ausgelaufen) (BP/FAss 1j; TZ)", BerufskollegAnlage.X, "04", 310, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BP 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung X05 (ausgelaufen): 
	 *   Anlage X (Ehemalige Kollegschule), 
	 *   Typ 05 (Berufsabschluss/Assistent (ausgelaufen) (BAB/Ass 3j; TZ))
	 */
	X05(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(6005000, "X05", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsabschluss/Assistent (ausgelaufen) (BAB/Ass 3j; TZ)", BerufskollegAnlage.X, "05", 320, false, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung X06 (ausgelaufen): 
	 *   Anlage X (Ehemalige Kollegschule), 
	 *   Typ 06 (Berufsabschluss und Allgemeine Hochschulreife (ausgelaufen) (BAB/AHR 3j; VZ))
	 */
	X06(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(6006000, "X06", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsabschluss und Allgemeine Hochschulreife (ausgelaufen) (BAB/AHR 3j; VZ)", BerufskollegAnlage.X, "06", 330, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.FHR,
				SchulabschlussAllgemeinbildend.ABITUR
			), null, null)
	}),

	/**
	 * Schulgliederung X07 (ausgelaufen): 
	 *   Anlage X (Ehemalige Kollegschule), 
	 *   Typ 07 (Berufsabschluss und Fachoberschulreife (ausgelaufen) (BAB/FOR 3j; VZ))
	 */
	X07(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(6007000, "X07", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsabschluss und Fachoberschulreife (ausgelaufen) (BAB/FOR 3j; VZ)", BerufskollegAnlage.X, "07", 340, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA,
				SchulabschlussAllgemeinbildend.MSA,
				SchulabschlussAllgemeinbildend.MSA_Q
			), null, null)
	}),

	/**
	 * Schulgliederung X08 (ausgelaufen): 
	 *   Anlage X (Ehemalige Kollegschule), 
	 *   Typ 08 (Berufsabschluss mit Zusatzqualifikation (ausgelaufen) (BAB/ZQ 3j; VZ))
	 */
	X08(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(6008000, "X08", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), true, true, "Berufsabschluss mit Zusatzqualifikation (ausgelaufen) (BAB/ZQ 3j; VZ)", BerufskollegAnlage.X, "08", 350, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL,
				SchulabschlussBerufsbildend.BAB 
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	}),

	/**
	 * Schulgliederung Y8: 
	 *   Lehrplan G8
	 */
	Y8(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(10008000, "Y8", false, Arrays.asList(
				Schulform.GE
			), false, false, "Lehrplan G8", null, null, null, false, null, null, null, null)
	}),

	/**
	 * Schulgliederung Z01: 
	 *   Anlage Z, 
	 *   Typ 01 (Kooperationsklasse Hauptschule (HS 2j; VZ))
	 */
	Z01(new SchulgliederungKatalogEintrag[]{
		new SchulgliederungKatalogEintrag(7001000, "Z01", true, Arrays.asList(
				Schulform.BK, Schulform.SB
			), false, false, "Kooperationsklasse Hauptschule (HS 2j; VZ)", BerufskollegAnlage.Z, "01", 370, true, Arrays.asList(
				SchulabschlussBerufsbildend.OA,
				SchulabschlussBerufsbildend.WECHSEL
			), Arrays.asList(
				SchulabschlussAllgemeinbildend.OA
			), null, null)
	});


	
	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Schulgliederung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull SchulgliederungKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Schulgliederungen */
	public final @NotNull SchulgliederungKatalogEintrag@NotNull[] historie;	

	/** Eine Map mit der Zuordnung der Schulgliederung zu dem Kürzel der Schulgliederung */
	private static final @NotNull HashMap<@NotNull String, @NotNull Schulgliederung> _schulgliederungenKuerzel = new HashMap<>();

	/** Eine Map mit der Zuordnung der Schulgliederung zu der ID der Schulgliederung */
	private static final @NotNull HashMap<@NotNull Long, @NotNull Schulgliederung> _schulgliederungenID = new HashMap<>();
	
	/** Die Schulformen, bei welchen die Schulgliederung vorkommt */
	private @NotNull Vector<@NotNull Schulform>@NotNull[] schulformen;
	
	
	/**
	 * Erzeugt eine neue Schulgliederung in der Aufzählung.
	 * 
	 * @param historie   die Historie der Schulgliederung, welches ein Array von {@link SchulgliederungKatalogEintrag} ist  
	 */
	@SuppressWarnings("unchecked")
	private Schulgliederung(@NotNull SchulgliederungKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
		// Erzeuge ein zweites Array mit der Schulformzuordnung für dei Historie
		this.schulformen = (@NotNull Vector<@NotNull Schulform>@NotNull[])Array.newInstance(Vector.class, historie.length); 
		for (int i = 0; i < historie.length; i++) {
			this.schulformen[i] = new Vector<>();
			for (@NotNull String kuerzel : historie[i].schulformen) {
				Schulform sf = Schulform.getByKuerzel(kuerzel);
				if (sf != null)
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
	private static @NotNull HashMap<@NotNull String, @NotNull Schulgliederung> getMapSchulgliederungByKuerzel() {
		if (_schulgliederungenKuerzel.size() == 0)
			for (Schulgliederung s : Schulgliederung.values())
				_schulgliederungenKuerzel.put(s.daten.kuerzel, s);				
		return _schulgliederungenKuerzel;
	}
	

	/**
	 * Gibt eine Map von den IDs der Schulgliederungen auf die zugehörigen Schulgliederungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Schulgliederungen auf die zugehörigen Schulgliederungen
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull Schulgliederung> getMapSchulgliederungByID() {
		if (_schulgliederungenID.size() == 0)
			for (Schulgliederung s : Schulgliederung.values()) {
				for (SchulgliederungKatalogEintrag k : s.historie)
					_schulgliederungenID.put(k.id, s);
			}
		return _schulgliederungenID;
	}
	

	/**
	 * Liefert die Schulgliederung anhand des übergebenen Kürzels zurück. 
	 * 
	 * @param kuerzel   das Kürzel der Schulgliederung
	 * 
	 * @return die Schulgliederung oder null, falls das Kürzel ungültig ist
	 */
	public static Schulgliederung getByKuerzel(String kuerzel) {
		if ((kuerzel == null) || "".equals(kuerzel))
			return Schulgliederung.DEFAULT;
		return getMapSchulgliederungByKuerzel().get(kuerzel);
	}



	/**
	 * Liefert die Schulgliederung anhand der übergebenen ID zurück. 
	 * 
	 * @param id   die ID der Schulgliederung
	 * 
	 * @return die Schulgliederung oder null, falls die ID ungültig ist
	 */
	public static Schulgliederung getByID(Long id) {
		return getMapSchulgliederungByID().get(id);
	}


	/**
	 * Gibt alle Schulgliderungen zurück, die zu dem angebenen
	 * Fachklassen-Index am Berufskolleg gehören.
	 * 
	 * @param index   der Fachklassen-Index
	 * 
	 * @return die zugehörigen Schulgliederungen
	 */
	public static @NotNull List<@NotNull Schulgliederung> getByBkIndex(int index) {
		@NotNull Vector<@NotNull Schulgliederung> result = new Vector<>();
		@NotNull Schulgliederung@NotNull[] gliederungen = Schulgliederung.values();
		for (int i = 0; i < gliederungen.length; i++) {
			@NotNull Schulgliederung gliederung = gliederungen[i];
			if ((gliederung.daten.bkIndex != null) && (gliederung.daten.bkIndex == index))
				result.add(gliederung);
		}
		return result;
	}


	/**
	 * Liefert alle Schulformen zurück, bei welchen die Schulgliederung vorkommt.
	 * 
	 * @return eine Liste der Schulformen
	 */
	@JsonIgnore
	public @NotNull List<@NotNull Schulform> getSchulformen() {
		return schulformen[historie.length - 1];
	}



	/**
	 * Liefert alle zulässigen Gliederungen für die angegeben Schulform.
	 * 
	 * @param schulform   die Schulform
	 * 
	 * @return die bei der Schulform zulässigen Gliederungen
	 */
	public static @NotNull List<@NotNull Schulgliederung> get(Schulform schulform) {
		@NotNull Vector<@NotNull Schulgliederung> result = new Vector<>();
		if (schulform == null)
			return result;
		@NotNull Schulgliederung@NotNull[] gliederungen = Schulgliederung.values();
		for (int i = 0; i < gliederungen.length; i++) {
			@NotNull Schulgliederung gliederung = gliederungen[i];
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
	@JsonIgnore
	public boolean hasSchulformByKuerzel(String kuerzel) {
		if ((kuerzel == null) || "".equals(kuerzel))
			return false;
		if (daten.schulformen != null) {
			for (int i = 0; i < daten.schulformen.size(); i++) {
				String sfKuerzel = daten.schulformen.get(i);
				if (sfKuerzel.equals(kuerzel))
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
	@JsonIgnore
	public boolean hasSchulform(Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return false;
		if (daten.schulformen != null) {
			for (int i = 0; i < daten.schulformen.size(); i++) {
				String sfKuerzel = daten.schulformen.get(i);
				if (sfKuerzel.equals(schulform.daten.kuerzel))
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
	public boolean istG8() {
		return (this == Schulgliederung.GY8) || (this == Schulgliederung.Y8);
	}
	
	
	/**
	 * Gibt die Standard-Gliederung der angegebenen Schulform zurück.
	 * 
	 * @param sf        die Schulform
	 * 
	 * @return die Schulgliederung, falls die Schulform gültig ist und ansonsten null 
	 */
	public static Schulgliederung getDefault(Schulform sf) {
		if (sf == null)
			return null;
		if ((sf == Schulform.GY) ||
		    (sf == Schulform.SK) ||
		    (sf == Schulform.GM) ||
		    (sf == Schulform.G) ||
			(sf == Schulform.S) ||
			(sf == Schulform.PS) ||
			(sf == Schulform.V) ||
			(sf == Schulform.FW) ||
			(sf == Schulform.H) ||
			(sf == Schulform.R) ||
			(sf == Schulform.GE) ||
			(sf == Schulform.SR) ||
			(sf == Schulform.SG))
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
	public static Schulgliederung getBySchulformAndKuerzel(Schulform sf, String kuerzel) {
		if (sf == null)
			return null;
		// Ist das Kürzel null, so ist der Standard für die Schulform zurückzugeben
		if ((kuerzel == null) || "".equals(kuerzel))
			return getDefault(sf);
		// Prüfe, ob die Gliederung bei der Schulform existiert
		@NotNull List<@NotNull Schulgliederung> gliederungen = get(sf); 
		for (int i = 0; i < gliederungen.size(); i++) {
			Schulgliederung sg = gliederungen.get(i);
			if ((sg.daten.kuerzel).equalsIgnoreCase(kuerzel))
				return sg;
		}
		return null;
	}

}
