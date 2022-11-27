package de.nrw.schule.svws.core.types.fach;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.core.data.RGBFarbe;
import de.nrw.schule.svws.core.data.fach.FachKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulformSchulgliederung;
import de.nrw.schule.svws.core.types.jahrgang.Jahrgaenge;
import de.nrw.schule.svws.core.types.schule.Schulform;
import de.nrw.schule.svws.core.types.schule.Schulgliederung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die zulässigen Fächer der einzelnen 
 * Schulformen zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum ZulaessigesFach {
	
	/** Fach Arbeits- und Betriebswirtschaftslehre */
	AB(new FachKatalogEintrag[] {
		new FachKatalogEintrag(1000000L, "AB", "Arbeits- und Betriebswirtschaftslehre",
		"AB", null, Fachgruppe.FG_AL, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Neigungs- und Projektgruppen */
	AG(new FachKatalogEintrag[] {
		new FachKatalogEintrag(2000000L, "AG", "Neigungs- und Projektgruppen",
		"AG", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Arbeitslehre - Schwerpunkt Hauswirtschaft */
	AH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(3000000L, "AH", "Arbeitslehre - Schwerpunkt Hauswirtschaft",
		"AH", null, Fachgruppe.FG_AL, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, Schulgliederung.H),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Arbeitslehre */
	AL(new FachKatalogEintrag[] {
		new FachKatalogEintrag(4000000L, "AL", "Arbeitslehre - Integration Hauswirtschaft, Technik, Wirtschaftslehre",
		"AL", null, Fachgruppe.FG_AL, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Arabisch */
	AM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(5000000L, "AM", "Unterricht in der Herkunftssprache - Arabisch",
		"AM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Alevitische Religionslehre nach den Grundsätzen des AABF */
	AR(new FachKatalogEintrag[] {
		new FachKatalogEintrag(6000000L, "AR", "Alevitische Religionslehre nach den Grundsätzen des AABF",
		"AR", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Arbeitslehre - Schwerpunkt Technik */
	AT(new FachKatalogEintrag[] {
		new FachKatalogEintrag(7000000L, "AT", "Arbeitslehre - Schwerpunkt Technik",
		"AT", null, Fachgruppe.FG_AL, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, Schulgliederung.H),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Arbeitsvorbereitung */
	AV(new FachKatalogEintrag[] {
		new FachKatalogEintrag(8000000L, "AV", "Arbeitsvorbereitung",
		"AV", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Arbeitslehre - Schwerpunkt Wirtschaft */
	AW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(9000000L, "AW", "Arbeitslehre - Schwerpunkt Wirtschaft",
		"AW", null, Fachgruppe.FG_AL, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, Schulgliederung.H),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Arbeitslehre - Technik/Wirtschaft (nur Wahlpflichtunterricht) */
	AX(new FachKatalogEintrag[] {
		new FachKatalogEintrag(10000000L, "AX", "Arbeitslehre - Technik/Wirtschaft (nur Wahlpflichtunterricht)",
		"AX", null, Fachgruppe.FG_AL, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Arbeitslehre - Hauswirtschaft/Wirtschaft (nur Wahlpflichtunterricht) */
	AY(new FachKatalogEintrag[] {
		new FachKatalogEintrag(11000000L, "AY", "Arbeitslehre - Hauswirtschaft/Wirtschaft (nur Wahlpflichtunterricht)",
		"AY", null, Fachgruppe.FG_AL, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Berufs- und Arbeitspädagogik */
	BA(new FachKatalogEintrag[] {
		new FachKatalogEintrag(12000000L, "BA", "Berufs- und Arbeitspädagogik",
		"BA", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Bürowirtschaft */
	BF(new FachKatalogEintrag[] {
		new FachKatalogEintrag(13000000L, "BF", "Bürowirtschaft",
		"BF", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Betrieb und Gesellschaft/Politik */
	BG(new FachKatalogEintrag[] {
		new FachKatalogEintrag(14000000L, "BG", "Betrieb und Gesellschaft/Politik",
		"BG", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bosnisch */
	BH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(15000000L, "BH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Bosnisch",
		"BH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Biologie */
	BI(new FachKatalogEintrag[] {
		new FachKatalogEintrag(16000000L, "BI", "Biologie",
		"BI", 3, Fachgruppe.FG_NW, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Betriebsinformatik */
	BK(new FachKatalogEintrag[] {
		new FachKatalogEintrag(17000000L, "BK", "Betriebsinformatik",
		"BK", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Bosnisch */
	BM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(18000000L, "BM", "Unterricht in der Herkunftssprache - Bosnisch",
		"BM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Braille'sche Punktschrift */
	BN(new FachKatalogEintrag[] {
		new FachKatalogEintrag(19000000L, "BN", "Braille'sche Punktschrift",
		"BN", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.S, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Betriebspraxis */
	BP(new FachKatalogEintrag[] {
		new FachKatalogEintrag(20000000L, "BP", "Betriebspraxis",
		"BP", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Betriebslehre */
	BR(new FachKatalogEintrag[] {
		new FachKatalogEintrag(21000000L, "BR", "Betriebslehre",
		"BR", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Betriebssoziologie / Arbeitsrecht */
	BS(new FachKatalogEintrag[] {
		new FachKatalogEintrag(22000000L, "BS", "Betriebssoziologie / Arbeitsrecht",
		"BS", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Berufsvorbereitung */
	BV(new FachKatalogEintrag[] {
		new FachKatalogEintrag(23000000L, "BV", "Berufsvorbereitung",
		"BV", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Betriebswirtschaftslehre */
	BW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(24000000L, "BW", "Betriebswirtschaftslehre",
		"BW", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Betriebssysteme / Netzwerke */
	BY(new FachKatalogEintrag[] {
		new FachKatalogEintrag(25000000L, "BY", "Betriebssysteme / Netzwerke",
		"BY", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Chinesisch */
	C(new FachKatalogEintrag[] {
		new FachKatalogEintrag(26000000L, "C", "Chinesisch",
		"C", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Chinesisch, regulärer Beginn in der Einführungsphase */
	C0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(27000000L, "C0", "Chinesisch, regulärer Beginn in der Einführungsphase",
		"C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Chinesisch, regulärer Beginn in Jahrgang 11 */
	C1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(28000000L, "C1", "Chinesisch, regulärer Beginn in Jahrgang 11",
		"C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Chinesisch, regulärer Beginn in Jahrgang 5 */
	C5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(29000000L, "C5", "Chinesisch, regulärer Beginn in Jahrgang 5",
		"C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Chinesisch, regulärer Beginn in Jahrgang 6 */
	C6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(30000000L, "C6", "Chinesisch, regulärer Beginn in Jahrgang 6",
		"C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Chinesisch, regulärer Beginn in Jahrgang 7 */
	C7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(31000000L, "C7", "Chinesisch, regulärer Beginn in Jahrgang 7",
		"C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Chinesisch, regulärer Beginn in Jahrgang 8 */
	C8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(32000000L, "C8", "Chinesisch, regulärer Beginn in Jahrgang 8",
		"C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Chinesisch, regulärer Beginn in Jahrgang 9 */
	C9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(33000000L, "C9", "Chinesisch, regulärer Beginn in Jahrgang 9",
		"C", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Chemie */
	CH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(34000000L, "CH", "Chemie",
		"CH", 3, Fachgruppe.FG_NW, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Kroatisch */
	CM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(35000000L, "CM", "Unterricht in der Herkunftssprache - Kroatisch",
		"CM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Chinesisch, außerhalb des regulären Fachunterrichts */
	CQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(36000000L, "CQ", "Chinesisch, außerhalb des regulären Fachunterrichts",
		"CQ", null, null, null,
		true, false, true, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Deutsch */
	D(new FachKatalogEintrag[] {
		new FachKatalogEintrag(37000000L, "D", "Deutsch",
		"D", 1, Fachgruppe.FG_D, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Datenbanken */
	DB(new FachKatalogEintrag[] {
		new FachKatalogEintrag(38000000L, "DB", "Datenbanken",
		"DB", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Rumänisch */
	DH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(39000000L, "DH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Rumänisch",
		"DH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Didaktik u. Methodik der soz.päd. Praxis mit Übungen */
	DM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(40000000L, "DM", "Didaktik u. Methodik der soz.päd. Praxis mit Übungen",
		"DM", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Darstellen und Gestalten */
	DS(new FachKatalogEintrag[] {
		new FachKatalogEintrag(41000000L, "DS", "Darstellen und Gestalten",
		"DS", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Datenverarbeitung */
	DV(new FachKatalogEintrag[] {
		new FachKatalogEintrag(42000000L, "DV", "Datenverarbeitung",
		"DV", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Englisch */
	E(new FachKatalogEintrag[] {
		new FachKatalogEintrag(43000000L, "E", "Englisch",
		"E", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Erweitertes Bildungsangebot */
	EB(new FachKatalogEintrag[] {
		new FachKatalogEintrag(44000000L, "EB", "Erweitertes Bildungsangebot",
		"EB", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.R, Schulgliederung.H),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Serbisch */
	EH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(45000000L, "EH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Serbisch",
		"EH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Erdkunde/ Geographie */
	EK(new FachKatalogEintrag[] {
		new FachKatalogEintrag(46000000L, "EK", "Erdkunde/ Geographie",
		"EK", 2, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Ernährungslehre */
	EL(new FachKatalogEintrag[] {
		new FachKatalogEintrag(47000000L, "EL", "Ernährungslehre",
		"EL", null, Fachgruppe.FG_WN, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, Schulgliederung.GY),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Serbisch */
	EM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(48000000L, "EM", "Unterricht in der Herkunftssprache - Serbisch",
		"EM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Evangelische Religionslehre (konfessionell kooperativ) */
	EN(new FachKatalogEintrag[] {
		new FachKatalogEintrag(49000000L, "EN", "Evangelische Religionslehre (konfessionell kooperativ)",
		"ER", null, null, null,
		false, false, false, false, true, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Evangelische Religionslehre */
	ER(new FachKatalogEintrag[] {
		new FachKatalogEintrag(50000000L, "ER", "Evangelische Religionslehre",
		"ER", null, Fachgruppe.FG_RE, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Französisch */
	F(new FachKatalogEintrag[] {
		new FachKatalogEintrag(51000000L, "F", "Französisch",
		"F", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SK, Schulgliederung.H),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Französisch, regulärer Beginn in der Einführungsphase */
	F0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(52000000L, "F0", "Französisch, regulärer Beginn in der Einführungsphase",
		"F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Französisch, regulärer Beginn in Jahrgang 11 */
	F1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(53000000L, "F1", "Französisch, regulärer Beginn in Jahrgang 11",
		"F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Französisch, regulärer Beginn in Jahrgang 5 */
	F5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(54000000L, "F5", "Französisch, regulärer Beginn in Jahrgang 5",
		"F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Französisch, regulärer Beginn in Jahrgang 6 */
	F6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(55000000L, "F6", "Französisch, regulärer Beginn in Jahrgang 6",
		"F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Französisch, regulärer Beginn in Jahrgang 7 */
	F7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(56000000L, "F7", "Französisch, regulärer Beginn in Jahrgang 7",
		"F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Französisch, regulärer Beginn in Jahrgang 8 */
	F8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(57000000L, "F8", "Französisch, regulärer Beginn in Jahrgang 8",
		"F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Französisch, regulärer Beginn in Jahrgang 9 */
	F9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(58000000L, "F9", "Französisch, regulärer Beginn in Jahrgang 9",
		"F", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Fächer des berufsbezogenen Bereichs */
	FB(new FachKatalogEintrag[] {
		new FachKatalogEintrag(59000000L, "FB", "Fächer des berufsbezogenen Bereichs",
		"FB", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Fremdsprachliche Kommunikation */
	FK(new FachKatalogEintrag[] {
		new FachKatalogEintrag(60000000L, "FK", "Fremdsprachliche Kommunikation",
		"FK", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Fachpraxis */
	FP(new FachKatalogEintrag[] {
		new FachKatalogEintrag(61000000L, "FP", "Fachpraxis",
		"FP", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Französisch, außerhalb des regulären Fachunterrichts */
	FQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(62000000L, "FQ", "Französisch, außerhalb des regulären Fachunterrichts",
		"FQ", null, null, null,
		true, false, true, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Förderunterricht */
	FU(new FachKatalogEintrag[] {
		new FachKatalogEintrag(63000000L, "FU", "Förderunterricht",
		"FU", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Griechisch */
	G(new FachKatalogEintrag[] {
		new FachKatalogEintrag(64000000L, "G", "Griechisch",
		"G", null, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Griechisch, regulärer Beginn in der Einführungsphase */
	G0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(65000000L, "G0", "Griechisch, regulärer Beginn in der Einführungsphase",
		"G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Griechisch, regulärer Beginn in Jahrgang 11 */
	G1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(66000000L, "G1", "Griechisch, regulärer Beginn in Jahrgang 11",
		"G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Griechisch, regulärer Beginn in Jahrgang 5 */
	G5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(67000000L, "G5", "Griechisch, regulärer Beginn in Jahrgang 5",
		"G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Griechisch, regulärer Beginn in Jahrgang 6 */
	G6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(68000000L, "G6", "Griechisch, regulärer Beginn in Jahrgang 6",
		"G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Griechisch, regulärer Beginn in Jahrgang 7 */
	G7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(69000000L, "G7", "Griechisch, regulärer Beginn in Jahrgang 7",
		"G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Griechisch, regulärer Beginn in Jahrgang 8 */
	G8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(70000000L, "G8", "Griechisch, regulärer Beginn in Jahrgang 8",
		"G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Griechisch, regulärer Beginn in Jahrgang 9 */
	G9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(71000000L, "G9", "Griechisch, regulärer Beginn in Jahrgang 9",
		"G", null, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Geräte- und Maschinenlehre */
	GA(new FachKatalogEintrag[] {
		new FachKatalogEintrag(72000000L, "GA", "Geräte- und Maschinenlehre",
		"GA", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Grundbildung */
	GB(new FachKatalogEintrag[] {
		new FachKatalogEintrag(73000000L, "GB", "Grundbildung",
		"GB", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Geschichte */
	GE(new FachKatalogEintrag[] {
		new FachKatalogEintrag(74000000L, "GE", "Geschichte",
		"GE", 2, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Neugriechisch */
	GH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(75000000L, "GH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Neugriechisch",
		"GH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Gesellschaftslehre */
	GL(new FachKatalogEintrag[] {
		new FachKatalogEintrag(76000000L, "GL", "Gesellschaftslehre",
		"GL", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Neugriechisch */
	GM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(77000000L, "GM", "Unterricht in der Herkunftssprache - Neugriechisch",
		"GM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Geologie (Oberstufenkolleg Bielefeld) */
	GO(new FachKatalogEintrag[] {
		new FachKatalogEintrag(78000000L, "GO", "Geologie (Oberstufenkolleg Bielefeld)",
		"GO", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Geschichte / Politik */
	GP(new FachKatalogEintrag[] {
		new FachKatalogEintrag(79000000L, "GP", "Geschichte / Politik",
		"GP", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Altgriechisch, außerhalb des regulären Fachunterrichts */
	GQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(80000000L, "GQ", "Altgriechisch, außerhalb des regulären Fachunterrichts",
		"GQ", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Gestaltungslehre */
	GS(new FachKatalogEintrag[] {
		new FachKatalogEintrag(81000000L, "GS", "Gestaltungslehre",
		"GS", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Gesamtunterricht (nur für Förderschulkindergarten) */
	GU(new FachKatalogEintrag[] {
		new FachKatalogEintrag(82000000L, "GU", "Gesamtunterricht (nur für Förderschulkindergarten)",
		"GU", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Geschichte und Sozialwissenschaft */
	GW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(83000000L, "GW", "Geschichte und Sozialwissenschaft",
		"GW", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hebräisch */
	H(new FachKatalogEintrag[] {
		new FachKatalogEintrag(84000000L, "H", "Hebräisch",
		"H", null, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hebräisch, regulärer Beginn in der Einführungsphase */
	H0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(85000000L, "H0", "Hebräisch, regulärer Beginn in der Einführungsphase",
		"H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hebräisch, regulärer Beginn in Jahrgang 11 */
	H1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(86000000L, "H1", "Hebräisch, regulärer Beginn in Jahrgang 11",
		"H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Hebräisch, regulärer Beginn in Jahrgang 5 */
	H5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(87000000L, "H5", "Hebräisch, regulärer Beginn in Jahrgang 5",
		"H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hebräisch, regulärer Beginn in Jahrgang 6 */
	H6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(88000000L, "H6", "Hebräisch, regulärer Beginn in Jahrgang 6",
		"H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hebräisch, regulärer Beginn in Jahrgang 7 */
	H7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(89000000L, "H7", "Hebräisch, regulärer Beginn in Jahrgang 7",
		"H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hebräisch, regulärer Beginn in Jahrgang 8 */
	H8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(90000000L, "H8", "Hebräisch, regulärer Beginn in Jahrgang 8",
		"H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hebräisch, regulärer Beginn in Jahrgang 9 */
	H9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(91000000L, "H9", "Hebräisch, regulärer Beginn in Jahrgang 9",
		"H", null, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kroatisch */
	HH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(92000000L, "HH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Kroatisch",
		"HH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hebräisch, außerhalb des regulären Fachunterrichts */
	HQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(93000000L, "HQ", "Hebräisch, außerhalb des regulären Fachunterrichts",
		"HQ", null, null, null,
		true, false, true, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Jüdische Religionslehre */
	HR(new FachKatalogEintrag[] {
		new FachKatalogEintrag(94000000L, "HR", "Jüdische Religionslehre",
		"HR", null, Fachgruppe.FG_RE, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hausunterricht */
	HU(new FachKatalogEintrag[] {
		new FachKatalogEintrag(95000000L, "HU", "Hausunterricht",
		"HU", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hauswirtschaft */
	HW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(96000000L, "HW", "Hauswirtschaft",
		"HW", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Hygiene */
	HY(new FachKatalogEintrag[] {
		new FachKatalogEintrag(97000000L, "HY", "Hygiene",
		"HY", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Italienisch */
	I(new FachKatalogEintrag[] {
		new FachKatalogEintrag(98000000L, "I", "Italienisch",
		"I", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Italienisch, regulärer Beginn in der Einführungsphase */
	I0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(99000000L, "I0", "Italienisch, regulärer Beginn in der Einführungsphase",
		"I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Italienisch, regulärer Beginn in Jahrgang 11 */
	I1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(100000000L, "I1", "Italienisch, regulärer Beginn in Jahrgang 11",
		"I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Italienisch, regulärer Beginn in Jahrgang 5 */
	I5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(101000000L, "I5", "Italienisch, regulärer Beginn in Jahrgang 5",
		"I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Italienisch, regulärer Beginn in Jahrgang 6 */
	I6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(102000000L, "I6", "Italienisch, regulärer Beginn in Jahrgang 6",
		"I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Italienisch, regulärer Beginn in Jahrgang 7 */
	I7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(103000000L, "I7", "Italienisch, regulärer Beginn in Jahrgang 7",
		"I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Italienisch, regulärer Beginn in Jahrgang 8 */
	I8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(104000000L, "I8", "Italienisch, regulärer Beginn in Jahrgang 8",
		"I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Italienisch, regulärer Beginn in Jahrgang 9 */
	I9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(105000000L, "I9", "Italienisch, regulärer Beginn in Jahrgang 9",
		"I", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Individuelles Lernen (Ergänzungsstunden, Ganztag- und Betreuungsangebote) */
	IE(new FachKatalogEintrag[] {
		new FachKatalogEintrag(106000000L, "IE", "Individuelles Lernen (Ergänzungsstunden, Ganztag- und Betreuungsangebote)",
		"IE", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Informatik */
	IF(new FachKatalogEintrag[] {
		new FachKatalogEintrag(107000000L, "IF", "Informatik",
		"IF", 3, Fachgruppe.FG_WN, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Italienisch */
	IH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(108000000L, "IH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Italienisch",
		"IH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Individuelles Lernen (dem Kernstundenkontingent entnommen) */
	IK(new FachKatalogEintrag[] {
		new FachKatalogEintrag(109000000L, "IK", "Individuelles Lernen (dem Kernstundenkontingent entnommen)",
		"IK", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Islamische Religionslehre */
	IL(new FachKatalogEintrag[] {
		new FachKatalogEintrag(110000000L, "IL", "Islamische Religionslehre",
		"IL", null, Fachgruppe.FG_RE, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Italienisch */
	IM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(111000000L, "IM", "Unterricht in der Herkunftssprache - Italienisch",
		"IM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Instrumentalpraktischer Grundkurs */
	IN(new FachKatalogEintrag[] {
		new FachKatalogEintrag(112000000L, "IN", "Instrumentalpraktischer Grundkurs",
		"IN", null, Fachgruppe.FG_ME, null,
		false, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Italienisch, außerhalb des regulären Fachunterrichts */
	IQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(113000000L, "IQ", "Italienisch, außerhalb des regulären Fachunterrichts",
		"I", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Niederländisch */
	JH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(114000000L, "JH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Niederländisch",
		"N", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Japanisch */
	K(new FachKatalogEintrag[] {
		new FachKatalogEintrag(115000000L, "K", "Japanisch",
		"K", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Japanisch, regulärer Beginn in der Einführungsphase */
	K0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(116000000L, "K0", "Japanisch, regulärer Beginn in der Einführungsphase",
		"K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Japanisch, regulärer Beginn in Jahrgang 11 */
	K1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(117000000L, "K1", "Japanisch, regulärer Beginn in Jahrgang 11",
		"K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Japanisch, regulärer Beginn in Jahrgang 5 */
	K5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(118000000L, "K5", "Japanisch, regulärer Beginn in Jahrgang 5",
		"K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Japanisch, regulärer Beginn in Jahrgang 6 */
	K6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(119000000L, "K6", "Japanisch, regulärer Beginn in Jahrgang 6",
		"K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Japanisch, regulärer Beginn in Jahrgang 7 */
	K7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(120000000L, "K7", "Japanisch, regulärer Beginn in Jahrgang 7",
		"K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Japanisch, regulärer Beginn in Jahrgang 8 */
	K8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(121000000L, "K8", "Japanisch, regulärer Beginn in Jahrgang 8",
		"K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Japanisch, regulärer Beginn in Jahrgang 9 */
	K9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(122000000L, "K9", "Japanisch, regulärer Beginn in Jahrgang 9",
		"K", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Katholische Religionslehre (konfessionell kooperativ) */
	KN(new FachKatalogEintrag[] {
		new FachKatalogEintrag(123000000L, "KN", "Katholische Religionslehre (konfessionell kooperativ)",
		"KR", null, null, null,
		false, false, false, false, true, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Kommunikation */
	KO(new FachKatalogEintrag[] {
		new FachKatalogEintrag(124000000L, "KO", "Kommunikation",
		"KO", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Japanisch, außerhalb des regulären Fachunterrichts */
	KQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(125000000L, "KQ", "Japanisch, außerhalb des regulären Fachunterrichts",
		"KQ", null, null, null,
		true, false, true, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Katholische Religionslehre */
	KR(new FachKatalogEintrag[] {
		new FachKatalogEintrag(126000000L, "KR", "Katholische Religionslehre",
		"KR", null, Fachgruppe.FG_RE, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Kurzschrift */
	KS(new FachKatalogEintrag[] {
		new FachKatalogEintrag(127000000L, "KS", "Kurzschrift",
		"KS", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Kunst */
	KU(new FachKatalogEintrag[] {
		new FachKatalogEintrag(128000000L, "KU", "Kunst",
		"KU", 1, Fachgruppe.FG_MS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Lateinisch */
	L(new FachKatalogEintrag[] {
		new FachKatalogEintrag(129000000L, "L", "Lateinisch",
		"L", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Lateinisch, regulärer Beginn in der Einführungsphase */
	L0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(130000000L, "L0", "Lateinisch, regulärer Beginn in der Einführungsphase",
		"L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Lateinisch, regulärer Beginn in Jahrgang 11 */
	L1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(131000000L, "L1", "Lateinisch, regulärer Beginn in Jahrgang 11",
		"L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Lateinisch, regulärer Beginn in Jahrgang 5 */
	L5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(132000000L, "L5", "Lateinisch, regulärer Beginn in Jahrgang 5",
		"L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Lateinisch, regulärer Beginn in Jahrgang 6 */
	L6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(133000000L, "L6", "Lateinisch, regulärer Beginn in Jahrgang 6",
		"L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Lateinisch, regulärer Beginn in Jahrgang 7 */
	L7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(134000000L, "L7", "Lateinisch, regulärer Beginn in Jahrgang 7",
		"L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Lateinisch, regulärer Beginn in Jahrgang 8 */
	L8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(135000000L, "L8", "Lateinisch, regulärer Beginn in Jahrgang 8",
		"L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Lateinisch, regulärer Beginn in Jahrgang 9 */
	L9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(136000000L, "L9", "Lateinisch, regulärer Beginn in Jahrgang 9",
		"L", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Albanisch */
	LH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(137000000L, "LH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Albanisch",
		"LH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Literatur */
	LI(new FachKatalogEintrag[] {
		new FachKatalogEintrag(138000000L, "LI", "Literatur",
		"LI", null, Fachgruppe.FG_ME, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Albanisch */
	LM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(139000000L, "LM", "Unterricht in der Herkunftssprache - Albanisch",
		"LM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Lateinisch, außerhalb des regulären Fachunterrichts */
	LQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(140000000L, "LQ", "Lateinisch, außerhalb des regulären Fachunterrichts",
		"LQ", null, null, null,
		true, false, true, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Mathematik */
	M(new FachKatalogEintrag[] {
		new FachKatalogEintrag(141000000L, "M", "Mathematik",
		"M", 3, Fachgruppe.FG_M, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Religionsunterricht der mennonitischen Brüdergemeinden in NRW als Schulversuch */
	MB(new FachKatalogEintrag[] {
		new FachKatalogEintrag(142000000L, "MB", "Religionsunterricht der mennonitischen Brüdergemeinden in NRW als Schulversuch",
		"MB", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Medienerziehung */
	MD(new FachKatalogEintrag[] {
		new FachKatalogEintrag(143000000L, "MD", "Medienerziehung",
		"MD", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Mechanik */
	ME(new FachKatalogEintrag[] {
		new FachKatalogEintrag(144000000L, "ME", "Mechanik",
		"ME", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Mazedonisch */
	MH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(145000000L, "MH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Mazedonisch",
		"MH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Mazedonisch */
	MM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(146000000L, "MM", "Unterricht in der Herkunftssprache - Mazedonisch",
		"MM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Meß- und Prüftechnik */
	MP(new FachKatalogEintrag[] {
		new FachKatalogEintrag(147000000L, "MP", "Meß- und Prüftechnik",
		"MP", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Musik */
	MU(new FachKatalogEintrag[] {
		new FachKatalogEintrag(148000000L, "MU", "Musik",
		"MU", 1, Fachgruppe.FG_MS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Methodik des wissenschaftl. orientierten Arbeitens */
	MW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(149000000L, "MW", "Methodik des wissenschaftl. orientierten Arbeitens",
		"MW", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spezielle sonderpädagogische Maßnahme */
	MX(new FachKatalogEintrag[] {
		new FachKatalogEintrag(150000000L, "MX", "Spezielle sonderpädagogische Maßnahmen",
		"MX", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Fächerübergreifender Unterricht in SI und SII (Berufspraxisstufe) */
	MY(new FachKatalogEintrag[] {
		new FachKatalogEintrag(151000000L, "MY", "Fächerübergreifender Unterricht in SI und SII (Berufspraxisstufe)",
		"MY", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Niederländisch */
	N(new FachKatalogEintrag[] {
		new FachKatalogEintrag(152000000L, "N", "Niederländisch",
		"N", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SK, Schulgliederung.H),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Niederländisch, regulärer Beginn in der Einführungsphase */
	N0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(153000000L, "N0", "Niederländisch, regulärer Beginn in der Einführungsphase",
		"N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Niederländisch, regulärer Beginn in Jahrgang 11 */
	N1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(154000000L, "N1", "Niederländisch, regulärer Beginn in Jahrgang 11",
		"N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Niederländisch, regulärer Beginn in Jahrgang 5 */
	N5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(155000000L, "N5", "Niederländisch, regulärer Beginn in Jahrgang 5",
		"N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Niederländisch, regulärer Beginn in Jahrgang 6 */
	N6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(156000000L, "N6", "Niederländisch, regulärer Beginn in Jahrgang 6",
		"N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Niederländisch, regulärer Beginn in Jahrgang 7 */
	N7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(157000000L, "N7", "Niederländisch, regulärer Beginn in Jahrgang 7",
		"N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Niederländisch, regulärer Beginn in Jahrgang 8 */
	N8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(158000000L, "N8", "Niederländisch, regulärer Beginn in Jahrgang 8",
		"N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Niederländisch, regulärer Beginn in Jahrgang 9 */
	N9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(159000000L, "N9", "Niederländisch, regulärer Beginn in Jahrgang 9",
		"N", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Polnisch */
	NH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(160000000L, "NH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Polnisch",
		"NH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Niederländisch */
	NM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(161000000L, "NM", "Unterricht in der Herkunftssprache - Niederländisch",
		"NM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Niederländisch, außerhalb des regulären Fachunterrichts */
	NQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(162000000L, "NQ", "Niederländisch, außerhalb des regulären Fachunterrichts",
		"NQ", null, null, null,
		true, false, true, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Naturwissenschaften */
	NW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(163000000L, "NW", "Naturwissenschaften",
		"NW", null, Fachgruppe.FG_NW, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Portugiesisch */
	O(new FachKatalogEintrag[] {
		new FachKatalogEintrag(164000000L, "O", "Portugiesisch",
		"O", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Portugiesisch, regulärer Beginn in der Einführungsphase */
	O0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(165000000L, "O0", "Portugiesisch, regulärer Beginn in der Einführungsphase",
		"O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 11 */
	O1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(166000000L, "O1", "Portugiesisch, regulärer Beginn in Jahrgang 11",
		"O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 5 */
	O5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(167000000L, "O5", "Portugiesisch, regulärer Beginn in Jahrgang 5",
		"O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 6 */
	O6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(168000000L, "O6", "Portugiesisch, regulärer Beginn in Jahrgang 6",
		"O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 7 */
	O7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(169000000L, "O7", "Portugiesisch, regulärer Beginn in Jahrgang 7",
		"O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 8 */
	O8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(170000000L, "O8", "Portugiesisch, regulärer Beginn in Jahrgang 8",
		"O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 9 */
	O9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(171000000L, "O9", "Portugiesisch, regulärer Beginn in Jahrgang 9",
		"O", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Portugiesisch */
	OH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(172000000L, "OH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Portugiesisch",
		"OH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Organisationslehre/Büroorganisation */
	OL(new FachKatalogEintrag[] {
		new FachKatalogEintrag(173000000L, "OL", "Organisationslehre/Büroorganisation",
		"OL", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Portugiesisch */
	OM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(174000000L, "OM", "Unterricht in der Herkunftssprache - Portugiesisch",
		"OM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Portugiesisch, außerhalb des regulären Fachunterrichts */
	OQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(175000000L, "OQ", "Portugiesisch, außerhalb des regulären Fachunterrichts",
		"OQ", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Orthodoxe Religionslehre */
	OR(new FachKatalogEintrag[] {
		new FachKatalogEintrag(176000000L, "OR", "Orthodoxe Religionslehre",
		"OR", null, Fachgruppe.FG_RE, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Pädagogik/ Erziehungswissenschaft */
	PA(new FachKatalogEintrag[] {
		new FachKatalogEintrag(177000000L, "PA", "Pädagogik/ Erziehungswissenschaft",
		"PA", 2, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Physik */
	PH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(178000000L, "PH", "Physik",
		"PH", 3, Fachgruppe.FG_NW, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Politik */
	PK(new FachKatalogEintrag[] {
		new FachKatalogEintrag(179000000L, "PK", "Politik",
		"PK", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Philosophie */
	PL(new FachKatalogEintrag[] {
		new FachKatalogEintrag(180000000L, "PL", "Philosophie",
		"PL", 2, Fachgruppe.FG_PL, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Polnisch */
	PM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(181000000L, "PM", "Unterricht in der Herkunftssprache - Polnisch",
		"PM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Praktische Philosophie */
	PP(new FachKatalogEintrag[] {
		new FachKatalogEintrag(182000000L, "PP", "Praktische Philosophie",
		"PP", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Psychologie */
	PS(new FachKatalogEintrag[] {
		new FachKatalogEintrag(183000000L, "PS", "Psychologie",
		"PS", 2, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Fachübergreifender Unterricht */
	PU(new FachKatalogEintrag[] {
		new FachKatalogEintrag(184000000L, "PU", "Fachübergreifender Unterricht",
		"PU", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Personalwirtschaft und Soziologie/Politik */
	PW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(185000000L, "PW", "Personalwirtschaft und Soziologie/Politik",
		"PW", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Projektkurs (mit einem oder zwei Leitfächern) */
	PX(new FachKatalogEintrag[] {
		new FachKatalogEintrag(186000000L, "PX", "Projektkurs (mit einem oder zwei Leitfächern)",
		"PX", null, Fachgruppe.FG_PX, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Politik/Ökonomische Grundbildung */
	POE(new FachKatalogEintrag[] {
		new FachKatalogEintrag(187000000L, "PÖ", "Politik/Ökonomische Grundbildung",
		"PÖ", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GM, Schulgliederung.R),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, Schulgliederung.R00),
			new Pair<>(Schulform.S, Schulgliederung.R00),
			new Pair<>(Schulform.SK, Schulgliederung.R),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Farsi */
	QH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(188000000L, "QH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Farsi",
		"QH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Farsi */
	QM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(189000000L, "QM", "Unterricht in der Herkunftssprache - Farsi",
		"QM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Russisch */
	R(new FachKatalogEintrag[] {
		new FachKatalogEintrag(190000000L, "R", "Russisch",
		"R", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SK, Schulgliederung.H),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Russisch, regulärer Beginn in der Einführungsphase */
	R0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(191000000L, "R0", "Russisch, regulärer Beginn in der Einführungsphase",
		"R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Russisch, regulärer Beginn in Jahrgang 11 */
	R1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(192000000L, "R1", "Russisch, regulärer Beginn in Jahrgang 11",
		"R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Russisch, regulärer Beginn in Jahrgang 5 */
	R5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(193000000L, "R5", "Russisch, regulärer Beginn in Jahrgang 5",
		"R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Russisch, regulärer Beginn in Jahrgang 6 */
	R6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(194000000L, "R6", "Russisch, regulärer Beginn in Jahrgang 6",
		"R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Russisch, regulärer Beginn in Jahrgang 7 */
	R7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(195000000L, "R7", "Russisch, regulärer Beginn in Jahrgang 7",
		"R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Russisch, regulärer Beginn in Jahrgang 8 */
	R8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(196000000L, "R8", "Russisch, regulärer Beginn in Jahrgang 8",
		"R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Russisch, regulärer Beginn in Jahrgang 9 */
	R9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(197000000L, "R9", "Russisch, regulärer Beginn in Jahrgang 9",
		"R", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Rechnungswesen */
	RE(new FachKatalogEintrag[] {
		new FachKatalogEintrag(198000000L, "RE", "Rechnungswesen",
		"RE", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch */
	RH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(199000000L, "RH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch",
		"RH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Rechtskunde */
	RK(new FachKatalogEintrag[] {
		new FachKatalogEintrag(200000000L, "RK", "Rechtskunde",
		"RK", 2, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Russisch */
	RM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(201000000L, "RM", "Unterricht in der Herkunftssprache - Russisch",
		"RM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Russisch, außerhalb des regulären Fachunterrichts */
	RQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(202000000L, "RQ", "Russisch, außerhalb des regulären Fachunterrichts",
		"RQ", null, null, null,
		true, false, true, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Recht und Verwaltung */
	RW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(203000000L, "RW", "Recht und Verwaltung",
		"RW", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spanisch */
	S(new FachKatalogEintrag[] {
		new FachKatalogEintrag(204000000L, "S", "Spanisch",
		"S", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spanisch, regulärer Beginn in der Einführungsphase */
	S0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(205000000L, "S0", "Spanisch, regulärer Beginn in der Einführungsphase",
		"S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spanisch, regulärer Beginn in Jahrgang 11 */
	S1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(206000000L, "S1", "Spanisch, regulärer Beginn in Jahrgang 11",
		"S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Sport für Jungen */
	S3(new FachKatalogEintrag[] {
		new FachKatalogEintrag(207000000L, "S3", "Sport für Jungen",
		"S3", null, Fachgruppe.FG_SP, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Sport für Mädchen */
	S4(new FachKatalogEintrag[] {
		new FachKatalogEintrag(208000000L, "S4", "Sport für Mädchen",
		"S4", null, Fachgruppe.FG_SP, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spanisch, regulärer Beginn in Jahrgang 5 */
	S5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(209000000L, "S5", "Spanisch, regulärer Beginn in Jahrgang 5",
		"S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spanisch, regulärer Beginn in Jahrgang 6 */
	S6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(210000000L, "S6", "Spanisch, regulärer Beginn in Jahrgang 6",
		"S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spanisch, regulärer Beginn in Jahrgang 7 */
	S7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(211000000L, "S7", "Spanisch, regulärer Beginn in Jahrgang 7",
		"S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spanisch, regulärer Beginn in Jahrgang 8 */
	S8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(212000000L, "S8", "Spanisch, regulärer Beginn in Jahrgang 8",
		"S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spanisch, regulärer Beginn in Jahrgang 9 */
	S9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(213000000L, "S9", "Spanisch, regulärer Beginn in Jahrgang 9",
		"S", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Softwareentwicklung und -engineering */
	SE(new FachKatalogEintrag[] {
		new FachKatalogEintrag(214000000L, "SE", "Softwareentwicklung und -engineering",
		"SE", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach sonderpädag. Förderung für Schüler/-innen mit sonderpädag. Förderbedarf */
	SG(new FachKatalogEintrag[] {
		new FachKatalogEintrag(215000000L, "SG", "Sonderpädag. Förderung für Schüler/-innen mit sonderpädag. Förderbedarf",
		"SG", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Spanisch */
	SH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(216000000L, "SH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Spanisch",
		"SH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spiel */
	SI(new FachKatalogEintrag[] {
		new FachKatalogEintrag(217000000L, "SI", "Spiel",
		"SI", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Soziologie */
	SL(new FachKatalogEintrag[] {
		new FachKatalogEintrag(218000000L, "SL", "Soziologie",
		"SL", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Spanisch */
	SM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(219000000L, "SM", "Unterricht in der Herkunftssprache - Spanisch",
		"SM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Sport */
	SP(new FachKatalogEintrag[] {
		new FachKatalogEintrag(220000000L, "SP", "Sport",
		"SP", null, Fachgruppe.FG_SP, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Spanisch, außerhalb des regulären Fachunterrichts */
	SQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(221000000L, "SQ", "Spanisch, außerhalb des regulären Fachunterrichts",
		"SQ", null, null, null,
		true, false, true, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Sonstige Fremdsprachen */
	SR(new FachKatalogEintrag[] {
		new FachKatalogEintrag(222000000L, "SR", "Sonstige Sprachen",
		"SR", null, null, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Sachunterricht */
	SU(new FachKatalogEintrag[] {
		new FachKatalogEintrag(223000000L, "SU", "Sachunterricht",
		"SU", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Sozialwissenschaften */
	SW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(224000000L, "SW", "Sozialwissenschaften",
		"SW", 2, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Sozialwissenschaften/Wirtschaft */
	SZ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(225000000L, "SZ", "Sozialwissenschaften/Wirtschaft",
		"SZ", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Türkisch */
	T(new FachKatalogEintrag[] {
		new FachKatalogEintrag(226000000L, "T", "Türkisch",
		"T", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SK, Schulgliederung.H),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Türkisch, regulärer Beginn in der Einführungsphase */
	T0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(227000000L, "T0", "Türkisch, regulärer Beginn in der Einführungsphase",
		"T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Türkisch, regulärer Beginn in Jahrgang 11 */
	T1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(228000000L, "T1", "Türkisch, regulärer Beginn in Jahrgang 11",
		"T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Türkisch, regulärer Beginn in Jahrgang 5 */
	T5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(229000000L, "T5", "Türkisch, regulärer Beginn in Jahrgang 5",
		"T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Türkisch, regulärer Beginn in Jahrgang 6 */
	T6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(230000000L, "T6", "Türkisch, regulärer Beginn in Jahrgang 6",
		"T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Türkisch, regulärer Beginn in Jahrgang 7 */
	T7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(231000000L, "T7", "Türkisch, regulärer Beginn in Jahrgang 7",
		"T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Türkisch, regulärer Beginn in Jahrgang 8 */
	T8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(232000000L, "T8", "Türkisch, regulärer Beginn in Jahrgang 8",
		"T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Türkisch, regulärer Beginn in Jahrgang 9 */
	T9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(233000000L, "T9", "Türkisch, regulärer Beginn in Jahrgang 9",
		"T", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Technik */
	TC(new FachKatalogEintrag[] {
		new FachKatalogEintrag(234000000L, "TC", "Technik",
		"TC", 3, Fachgruppe.FG_WN, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, Schulgliederung.GY),
			new Pair<>(Schulform.GM, Schulgliederung.R),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Technische Grundbildung */
	TG(new FachKatalogEintrag[] {
		new FachKatalogEintrag(235000000L, "TG", "Technische Grundbildung",
		"TG", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Türkisch */
	TH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(236000000L, "TH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Türkisch",
		"TH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Technische Informatik */
	TI(new FachKatalogEintrag[] {
		new FachKatalogEintrag(237000000L, "TI", "Technische Informatik",
		"TI", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Türkisch */
	TM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(238000000L, "TM", "Unterricht in der Herkunftssprache - Türkisch",
		"TM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Türkisch, außerhalb des regulären Fachunterrichts */
	TQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(239000000L, "TQ", "Türkisch, außerhalb des regulären Fachunterrichts",
		"T", null, null, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Textverarbeitung */
	TV(new FachKatalogEintrag[] {
		new FachKatalogEintrag(240000000L, "TV", "Textverarbeitung",
		"TV", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Textilgestaltung */
	TX(new FachKatalogEintrag[] {
		new FachKatalogEintrag(241000000L, "TX", "Textilgestaltung",
		"TX", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Technisches Zeichnen / Fachzeichnen */
	TZ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(242000000L, "TZ", "Technisches Zeichnen / Fachzeichnen",
		"TZ", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch */
	UH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(243000000L, "UH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch",
		"UH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Rumänisch */
	UM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(244000000L, "UM", "Unterricht in der Herkunftssprache - Rumänisch",
		"UM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach fächerübergreifender Unterricht (Sprache/Sachunt./Mathematik/Förderunt.) */
	UU(new FachKatalogEintrag[] {
		new FachKatalogEintrag(245000000L, "UU", "Fächerübergreifender Unterricht (Sprache/Sachunt./Mathematik/Förderunt.)",
		"UU", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterweisung */
	UW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(246000000L, "UW", "Unterweisung",
		"UW", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Verwaltungskunde */
	VE(new FachKatalogEintrag[] {
		new FachKatalogEintrag(247000000L, "VE", "Verwaltungskunde",
		"VE", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Fächer im genehmigten Schulversuch und sonstige Fächer */
	VF(new FachKatalogEintrag[] {
		new FachKatalogEintrag(248000000L, "VF", "Fächer im genehmigten Schulversuch und sonstige Fächer",
		"VF", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bulgarisch */
	VH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(249000000L, "VH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Bulgarisch",
		"VH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Verkaufskunde */
	VK(new FachKatalogEintrag[] {
		new FachKatalogEintrag(250000000L, "VK", "Verkaufskunde",
		"VK", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Bulgarisch */
	VM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(251000000L, "VM", "Unterricht in der Herkunftssprache - Bulgarisch",
		"VM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach vokalpraktischer Grundkurs */
	VO(new FachKatalogEintrag[] {
		new FachKatalogEintrag(252000000L, "VO", "vokalpraktischer Grundkurs",
		"VO", null, Fachgruppe.FG_ME, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Volkswirtschaftslehre/Politik */
	VP(new FachKatalogEintrag[] {
		new FachKatalogEintrag(253000000L, "VP", "Volkswirtschaftslehre/Politik",
		"VP", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Verfahrenstechnik */
	VT(new FachKatalogEintrag[] {
		new FachKatalogEintrag(254000000L, "VT", "Verfahrenstechnik",
		"VT", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Volkswirtschaftslehre */
	VW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(255000000L, "VW", "Volkswirtschaftslehre",
		"VW", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Vertiefungsfach */
	VX(new FachKatalogEintrag[] {
		new FachKatalogEintrag(256000000L, "VX", "Vertiefungsfach",
		"VX", null, Fachgruppe.FG_VX, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Werken */
	W(new FachKatalogEintrag[] {
		new FachKatalogEintrag(257000000L, "W", "Werken",
		"W", null, Fachgruppe.FG_MS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Wirtschaftsgeographie */
	WG(new FachKatalogEintrag[] {
		new FachKatalogEintrag(258000000L, "WG", "Wirtschaftsgeographie",
		"WG", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Wirtschaftsinformatik/Organisationslehre */
	WI(new FachKatalogEintrag[] {
		new FachKatalogEintrag(259000000L, "WI", "Wirtschaftsinformatik/Organisationslehre",
		"WI", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Wirtschaft-Politik */
	WP(new FachKatalogEintrag[] {
		new FachKatalogEintrag(260000000L, "WP", "Wirtschaft-Politik",
		"WP", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Wirtschaftslehre */
	WW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(261000000L, "WW", "Wirtschaftslehre",
		"WW", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Wirtschaft und Arbeitswelt - Schwerpunkt Hauswirtschaft */
	WX(new FachKatalogEintrag[] {
		new FachKatalogEintrag(262000000L, "WX", "Wirtschaft und Arbeitswelt - Schwerpunkt Hauswirtschaft",
		"WX", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), 2021, null)
	}),


	/** Fach Wirtschaft und Arbeitswelt - Schwerpunkt Technik */
	WY(new FachKatalogEintrag[] {
		new FachKatalogEintrag(263000000L, "WY", "Wirtschaft und Arbeitswelt - Schwerpunkt Technik",
		"WY", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), 2021, null)
	}),


	/** Fach Wirtschaft und Arbeitswelt - Schwerpunkt Wirtschaft */
	WZ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(264000000L, "WZ", "Wirtschaft und Arbeitswelt - Schwerpunkt Wirtschaft",
		"WZ", null, Fachgruppe.FG_GS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), 2021, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - sonstige Sprache */
	XH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(265000000L, "XH", "Herkunftssprache anstelle einer Pflichtfremdsprache - sonstige Sprache",
		"XH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - sonstige Sprache */
	XM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(266000000L, "XM", "Unterricht in der Herkunftssprache - sonstige Sprache",
		"XM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Sonstige Fächer ohne Fremdsprachen (kein Import nach ASDPC) */
	XX(new FachKatalogEintrag[] {
		new FachKatalogEintrag(267000000L, "XX", "Sonstige Fächer ohne Fremdsprachen (kein Import nach ASDPC)",
		"XX", null, null, null,
		false, false, false, false, false, false, false, 
		Arrays.asList(
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Koreanisch */
	YH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(268000000L, "YH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Koreanisch",
		"YH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Koreanisch */
	YM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(269000000L, "YM", "Unterricht in der Herkunftssprache - Koreanisch",
		"YM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach orthodoxe Religionslehre (Syrisch) */
	YR(new FachKatalogEintrag[] {
		new FachKatalogEintrag(270000000L, "YR", "Syrisch Orthodoxe Religionslehre",
		"YR", null, Fachgruppe.FG_RE, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Neugriechisch */
	Z(new FachKatalogEintrag[] {
		new FachKatalogEintrag(271000000L, "Z", "Neugriechisch",
		"Z", 1, Fachgruppe.FG_FS, null,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.H, Schulgliederung.R),
			new Pair<>(Schulform.SB, (Schulgliederung) null),
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Neugriechisch, regulärer Beginn in der Einführungsphase */
	Z0(new FachKatalogEintrag[] {
		new FachKatalogEintrag(272000000L, "Z0", "Neugriechisch, regulärer Beginn in der Einführungsphase",
		"Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_EF,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 11 */
	Z1(new FachKatalogEintrag[] {
		new FachKatalogEintrag(273000000L, "Z1", "Neugriechisch, regulärer Beginn in Jahrgang 11",
		"Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_11,
		true, false, false, false, false, true, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, 2012)
	}),


	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 5 */
	Z5(new FachKatalogEintrag[] {
		new FachKatalogEintrag(274000000L, "Z5", "Neugriechisch, regulärer Beginn in Jahrgang 5",
		"Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_05,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 6 */
	Z6(new FachKatalogEintrag[] {
		new FachKatalogEintrag(275000000L, "Z6", "Neugriechisch, regulärer Beginn in Jahrgang 6",
		"Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_06,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 7 */
	Z7(new FachKatalogEintrag[] {
		new FachKatalogEintrag(276000000L, "Z7", "Neugriechisch, regulärer Beginn in Jahrgang 7",
		"Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_07,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 8 */
	Z8(new FachKatalogEintrag[] {
		new FachKatalogEintrag(277000000L, "Z8", "Neugriechisch, regulärer Beginn in Jahrgang 8",
		"Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_08,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 9 */
	Z9(new FachKatalogEintrag[] {
		new FachKatalogEintrag(278000000L, "Z9", "Neugriechisch, regulärer Beginn in Jahrgang 9",
		"Z", 1, Fachgruppe.FG_FS, Jahrgaenge.JG_09,
		true, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Zusätzliche Förderung */
	ZF(new FachKatalogEintrag[] {
		new FachKatalogEintrag(279000000L, "ZF", "Zusätzliche Förderung",
		"ZF", null, null, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kurdische Sprachen */
	ZH(new FachKatalogEintrag[] {
		new FachKatalogEintrag(280000000L, "ZH", "Herkunftssprache anstelle einer Pflichtfremdsprache - Kurdische Sprachen",
		"ZH", null, null, null,
		true, true, false, true, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Unterricht in der Herkunftssprache - Kurdische Sprachen (Sorani, Komanci, Zaza) */
	ZM(new FachKatalogEintrag[] {
		new FachKatalogEintrag(281000000L, "ZM", "Unterricht in der Herkunftssprache - Kurdische Sprachen (Sorani, Komanci, Zaza)",
		"ZM", null, null, null,
		true, true, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.G, (Schulgliederung) null),
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.H, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.KS, (Schulgliederung) null),
			new Pair<>(Schulform.S, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null),
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Neugriechisch, außerhalb des regulären Fachunterrichts */
	ZQ(new FachKatalogEintrag[] {
		new FachKatalogEintrag(282000000L, "ZQ", "Neugriechisch, außerhalb des regulären Fachunterrichts",
		"ZQ", null, null, null,
		true, false, true, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null),
			new Pair<>(Schulform.GM, (Schulgliederung) null),
			new Pair<>(Schulform.GY, (Schulgliederung) null),
			new Pair<>(Schulform.PS, (Schulgliederung) null),
			new Pair<>(Schulform.R, (Schulgliederung) null),
			new Pair<>(Schulform.SG, (Schulgliederung) null),
			new Pair<>(Schulform.SK, (Schulgliederung) null),
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),


	/** Fach Zeichnen / Werken */
	ZW(new FachKatalogEintrag[] {
		new FachKatalogEintrag(283000000L, "ZW", "Zeichnen / Werken",
		"ZW", null, Fachgruppe.FG_MS, null,
		false, false, false, false, false, false, true, 
		Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null),
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	});



	/** Ein Standard-Wert, welcher u.a. gewählt wird, falls ein ungültiger Wert in der Datenbank eingetragen ist. */
	public static final @NotNull ZulaessigesFach DEFAULT = VF;


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Der aktuellen Daten des Faches */
	public final @NotNull FachKatalogEintrag daten;

	/** Die Historie mit den Einträgen des Faches */
	public final @NotNull FachKatalogEintrag@NotNull[] historie;	

	/** Eine HashMap mit allen zulässigen Fächern. Der Zugriff erfolgt dabei über die ID */ 
	private static final @NotNull HashMap<@NotNull Long, ZulaessigesFach> _mapID = new HashMap<>();

	/** Eine HashMap mit zulässigen Fächern. Der Zugriff erfolgt dabei das Statistik-Kürzel */ 
	private static final @NotNull HashMap<@NotNull String, ZulaessigesFach> _mapKuerzelASD = new HashMap<>();

	/** Die Informationen zu den Kombinationen aus Schulformen und -gliederungen, wo das Fach zulässig ist */
	private @NotNull Vector<@NotNull Pair<Schulform, Schulgliederung>>@NotNull[] zulaessig;


	/**
	 * Erzeugt eine zulässiges Fach in der Aufzählung.
	 * 
	 * @param historie   die Historie des Faches, welches ein Array von {@link FachKatalogEintrag} ist  
	 */
	@SuppressWarnings("unchecked")
	private ZulaessigesFach(@NotNull FachKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		// Erzeuge zwei Felder mit den Schulformen und Schulgliederungen für die Historie
		this.zulaessig = (@NotNull Vector<@NotNull Pair<Schulform, Schulgliederung>>@NotNull[])Array.newInstance(Vector.class, historie.length); 
		for (int i = 0; i < historie.length; i++) {
			this.zulaessig[i] = new Vector<>();
			for (@NotNull SchulformSchulgliederung kuerzelSfSgl : historie[i].zulaessig) {
				Schulform sf = Schulform.getByKuerzel(kuerzelSfSgl.schulform);
				if (sf == null)
					continue;
				Schulgliederung sgl = kuerzelSfSgl.gliederung == null ? null : Schulgliederung.getByKuerzel(kuerzelSfSgl.gliederung);
				this.zulaessig[i].add(new Pair<>(sf, sgl));
			}
		}
	}


	/**
	 * Gibt eine Map von den ASD-Kürzeln der Fächer auf die zugehörigen Fächer
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den ASD-Kürzeln der Fächer auf die zugehörigen Fächer
	 */
	private static @NotNull HashMap<@NotNull String, ZulaessigesFach> getMapByASDKuerzel() {
		if (_mapKuerzelASD.size() == 0)
			for (ZulaessigesFach s : ZulaessigesFach.values())
				_mapKuerzelASD.put(s.daten.kuerzelASD, s);				
		return _mapKuerzelASD;
	}
	
	
	/**
	 * Prüft, ob die Schulform bei diesem Fach in irgendeiner Gliederung der 
	 * angegebenen Schulform zulässig ist.
	 * 
	 * @param schulform    die Schulform
	 * 
	 * @return true, falls das Fach in der Schulform zulässig ist, ansonsten false.
	 */
	private boolean hasSchulform(Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return false;
		for (@NotNull Pair<Schulform, Schulgliederung> sfsgl : zulaessig[0]) {
			if (sfsgl.a == schulform)
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
	public static @NotNull List<ZulaessigesFach> get(Schulform schulform) {
		@NotNull Vector<ZulaessigesFach> faecher = new Vector<>();
		if (schulform == null)
			return faecher;
		for (@NotNull ZulaessigesFach fach : ZulaessigesFach.values())
			if (fach.hasSchulform(schulform))
				faecher.add(fach);
		return faecher;
	}


	/**
	 * Gibt die Fachgruppe dieses Faches zurück. 
	 * 
	 * @return die Fachgruppe des Faches
	 */
	public Fachgruppe getFachgruppe() {
		if (daten.fachgruppe == null)
			return null;
		return Fachgruppe.getByKuerzel(daten.fachgruppe);
	}


	/**
	 * Gibt den Jahrgang zurück, ab wann dieses Faches zulässig ist. 
	 * 
	 * @return der Jahrgang
	 */
	public Jahrgaenge getJahrgangAb() {
		if (daten.abJahrgang == null)
			return null;
		return Jahrgaenge.getByKuerzel(daten.abJahrgang);
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
	public static @NotNull ZulaessigesFach getByKuerzelASD(String kuerzel) {
		ZulaessigesFach result = getMapByASDKuerzel().get(kuerzel);
		return (result == null) ? ZulaessigesFach.DEFAULT : result;
	}


	/**
	 * Gibt die Farbe des zulässigen Faches zurück.
	 * 
	 * @return die Farbe des zulässigen Faches
	 */
	public @NotNull RGBFarbe getFarbe() {
	    Fachgruppe gruppe = Fachgruppe.getByKuerzel(this.daten.fachgruppe);
	    return gruppe == null ? new RGBFarbe() : gruppe.daten.farbe;
	}


    /**
     * Gibt die HTML-Farbe des zulässigen Faches als Aufruf der rgb-Funktion
     * mit der übergebenen Transparenz zurück.
     * 
     * @return die RGB-HTML-Farbdefinition als String
     */
    public @NotNull String getHMTLFarbeRGB() {
        @NotNull RGBFarbe farbe = getFarbe();
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
	public @NotNull String getHMTLFarbeRGBA(double alpha) {
	    @NotNull RGBFarbe farbe = getFarbe();
	    double a = (alpha < 0.0) ? 0.0 : ((alpha > 1.0) ? 1.0 : alpha);
	    return "rgba(" + farbe.red + "," + farbe.green + "," + farbe.blue + ", " + a + ")";
	}

}
