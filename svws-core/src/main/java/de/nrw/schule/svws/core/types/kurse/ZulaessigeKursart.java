package de.nrw.schule.svws.core.types.kurse;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.core.data.kurse.KursartKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulformSchulgliederung;
import de.nrw.schule.svws.core.types.schule.Schulform;
import de.nrw.schule.svws.core.types.schule.Schulgliederung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die zulässigen Kursarten der einzelnen 
 * Schulformen und Schulgliederungen zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum ZulaessigeKursart {

	/** Kursart 3. Abiturfach */
	AB3(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(0, "AB3", "71", "3. Abiturfach", null, "GK", "Grundkurs", true, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null), 
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart 4. Abiturfach */
	AB4(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(1000, "AB4", "71", "4. Abiturfach", null, "GK", "Grundkurs", true, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null), 
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Arbeitsgemeinschaft gemäß APO SI */
	AG(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(2000, "AG", "67", "Arbeitsgemeinschaft gemäß APO SI", "gemäß § 3 Abs. 6 APO-SI", null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, Schulgliederung.GY), 
			new Pair<>(Schulform.GM, Schulgliederung.GY), 
			new Pair<>(Schulform.GM, Schulgliederung.H), 
			new Pair<>(Schulform.GM, Schulgliederung.R), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.H, (Schulgliederung) null), 
			new Pair<>(Schulform.R, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, Schulgliederung.GY), 
			new Pair<>(Schulform.SK, Schulgliederung.H), 
			new Pair<>(Schulform.SK, Schulgliederung.R), 
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Arbeitsgemeinschaft im Ganztagsbereich */
	AGGT(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(3000, "AGGT", "36", "Arbeitsgemeinschaft im Ganztagsbereich", "gemäß § 9 Abs. 2, 3  SchulG", null, null, false, Arrays.asList(
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

	/** Kursart Stütz- und Angleichungskurs / Förderunterricht */
	AGKWB(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(4000, "AGKWB", "28", "Stütz- und Angleichungskurs / Förderunterricht", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Angleichungskurs */
	AGK(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(5000, "AGK", "73", "Angleichungskurs", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Arbeits- bzw. Übungsstunde  */
	AST(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(6000, "AST", "33", "Arbeits- bzw. Übungsstunde ", "gemäß § 9 Abs. 2, 3  SchulG", null, null, false, Arrays.asList(
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

	/** Kursart Begegnung mit Sprachen in der Primarstufe */
	BSP(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(7000, "BSP", "59", "Begegnung mit Sprachen in der Primarstufe", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.FW, (Schulgliederung) null),
			new Pair<>(Schulform.HI, (Schulgliederung) null),
			new Pair<>(Schulform.WF, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Unterricht im Rahmen von KAOA einschl. Schule trifft Arbeitswelt */
	BUS(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(8000, "BUS", "94", "Unterricht im Rahmen von KAOA einschl. Schule trifft Arbeitswelt", null, null, null, false, Arrays.asList(
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

	/** Kursart Erweiterungsebene/-kurs */
	E(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(9000, "E", "02", "Erweiterungsebene/-kurs", null, "DK", "Differenzierungskurs", false, Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Erweiterungskurs – Bildungsgang Hauptschule */
	E_H(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(10000, "E_H", "16", "Erweiterungskurs – Bildungsgang Hauptschule", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GM, Schulgliederung.H), 
			new Pair<>(Schulform.H, (Schulgliederung) null), 
			new Pair<>(Schulform.R, Schulgliederung.H), 
			new Pair<>(Schulform.KS, (Schulgliederung) null), 
			new Pair<>(Schulform.S, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, Schulgliederung.H), 
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Erweitertes Bildungsangebot */
	EBA(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(11000, "EBA", "88", "Erweitertes Bildungsangebot", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GM, Schulgliederung.H), 
			new Pair<>(Schulform.H, (Schulgliederung) null), 
			new Pair<>(Schulform.R, Schulgliederung.H), 
			new Pair<>(Schulform.SK, Schulgliederung.H), 
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Einführung in die 2. Fremdsprache oder Ersatzfach */
	EF2(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(12000, "EF2", "57", "Einführung in die 2. Fremdsprache oder Ersatzfach", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Ersatzfach für Sport als 4. Abiturfach */
	EFSP(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(13000, "EFSP", "71", "Ersatzfach für Sport als 4. Abiturfach", null, "GK", "Grundkurs", true, Arrays.asList(
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

	/** Kursart Ergänzungsstunden */
	EGS1(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(14000, "EGS1", "96", "Ergänzungsstunden", "gemäß § 3 Abs. 1, 3 und § 15 Abs. 3 APO-SI", null, null, false, Arrays.asList(
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

	/** Kursart Ergänzungsstunden mit Benotung */
	EGSN(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(15000, "EGSN", "97", "Ergänzungsstunden mit Benotung", "gemäß § 3 Abs. 1, 3 und § 19 Abs. 3 Nr.2 und 4 APO-SI", null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Ergänzungs- oder Vertiefungskurs */
	EV(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(16000, "EV", "41", "Ergänzungs- oder Vertiefungskurs", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Erweiterungsunterricht Wahlbereich - berufsbezogen, fachübergreifend */
	EWBF(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(17000, "EWBF", "12", "Erweiterungsunterricht Wahlbereich - berufsbezogen, fachübergreifend", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Erweiterungsunterricht Wahlbereich - fach-, jedoch nicht abschlussbezogen */
	EWF(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(18000, "EWF", "11", "Erweiterungsunterricht Wahlbereich - fach-, jedoch nicht abschlussbezogen", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Erweiterungsunterricht Wahlbereich - fach- und abschlussbezogen */
	EWFA(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(19000, "EWFA", "10", "Erweiterungsunterricht Wahlbereich - fach- und abschlussbezogen", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlplflichtunterricht: Einzelfach im math.-naturw., techn., gesellschaftsw., künstlerischen Schwerpunkt */
	F3(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(20000, "F3", "64", "Wahlplflichtunterricht: Einzelfach im math.-naturw., techn., gesellschaftsw., künstlerischen Schwerpunkt", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, Schulgliederung.GY), 
			new Pair<>(Schulform.GM, Schulgliederung.GY), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart freiwillige Arbeitsgemeinschaft */
	FAG(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(21000, "FAG", "58", "freiwillige Arbeitsgemeinschaft", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Fachbezogener Förderunterricht */
	FFU(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(22000, "FFU", "04", "Fachbezogener Förderunterricht", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Förderangebot Ganztagsschule */
	FOGT(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(23000, "FOGT", "37", "Förderangebot Ganztagsschule", "gemäß § 9 Abs. 2, 3  SchulG", null, null, false, Arrays.asList(
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

	/** Kursart Förderung neu zugewanderter Schüler in Deutschfördergruppen (teilweise äußere und innere Differenzierung) */
	DFG(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(24000, "DFG", "89", "Förderung neu zugewanderter Schüler in Deutschfördergruppen (teilweise äußere und innere Differenzierung)", "gemäß Erlass 13-63 Nr. 3", null, null, false, Arrays.asList(
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

	/** Kursart Wahlpflichtbereich II (Kl. 09 und 10, bei G8: 08 und 09): 3. Fremdsprache */
	FS3(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(25000, "FS3", "65", "Wahlpflichtbereich II (Kl. 09 und 10, bei G8: 08 und 09): 3. Fremdsprache", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, Schulgliederung.GY), 
			new Pair<>(Schulform.GM, Schulgliederung.GY), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, Schulgliederung.GY)
		), null, null)
	}),

	/** Kursart Förderung neu zugewanderter Schüler in Deutschförderklassen (vollständige äußere Differenzierung) */
	DFK(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(26000, "DFK", "90", "Förderung neu zugewanderter Schüler in Deutschförderklassen (vollständige äußere Differenzierung)", "gemäß Erlass 13-63 Nr. 3", null, null, false, Arrays.asList(
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

	/** Kursart Förderunterricht */
	FU(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(27000, "FU", "01", "Förderunterricht", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null), 
			new Pair<>(Schulform.S, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Stützunterricht Wahlbereich - Förderunterricht für ausländische u. ausgesiedelte Schüler */
	FUAUS(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(28000, "FUAUS", "03", "Stützunterricht Wahlbereich - Förderunterricht für ausländische u. ausgesiedelte Schüler", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Fachunabhängiger Förderunterricht */
	FUF(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(29000, "FUF", "05", "Fachunabhängiger Förderunterricht", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Förderunterricht im Klassenverband */
	FUK(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(30000, "FUK", "99", "Förderunterricht im Klassenverband", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Förderunterricht für Teile von Klassen */
	FUT(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(31000, "FUT", "00", "Förderunterricht für Teile von Klassen", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart fächerübergreifender Kurs */
	FUEK(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(32000, "FUEK", "79", "fächerübergreifender Kurs", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Grundebene/-kurs */
	G(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(33000, "G", "01", "Grundebene/-kurs", null, "DK", "Differenzierungskurs", false, Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Grundkurs – Bildungsgang Hauptschule */
	G_H(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(34000, "G_H", "13", "Grundkurs – Bildungsgang Hauptschule", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GM, Schulgliederung.H), 
			new Pair<>(Schulform.H, (Schulgliederung) null), 
			new Pair<>(Schulform.R, Schulgliederung.H), 
			new Pair<>(Schulform.KS, (Schulgliederung) null), 
			new Pair<>(Schulform.S, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, Schulgliederung.H), 
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Grundkurs mündlich */
	GKM(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(35000, "GKM", "71", "Grundkurs mündlich", null, "GK", "Grundkurs", true, Arrays.asList(
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

	/** Kursart Grundkurs schriftlich */
	GKS(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(36000, "GKS", "71", "Grundkurs schriftlich", null, "GK", "Grundkurs", true, Arrays.asList(
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

	/** Kursart Hausunterricht */
	HU(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(37000, "HU", "99", "Hausunterricht", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.G, (Schulgliederung) null), 
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
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

	/** Kursart Zusätzlicher Förderunterricht im Rahmen der Initiative Komm mit */
	KMFOE(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(38000, "KMFOE", "95", "Zusätzlicher Förderunterricht im Rahmen der Initiative Komm mit", null, null, null, false, Arrays.asList(
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
		), null, 2021)
	}),

	/** Kursart Leistungskurs I */
	LK1(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(39000, "LK1", "72", "Leistungskurs I", null, "LK", "Leistungskurs", true, Arrays.asList(
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

	/** Kursart Leistungskurs II */
	LK2(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(40000, "LK2", "72", "Leistungskurs II", null, "LK", "Leistungskurs", true, Arrays.asList(
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

	/** Kursart Schülerinnen und Schüler mit besonderen Schwierigkeiten im Erlernen des Lesens und Rechtschreibens (LRS) */
	LRS(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(41000, "LRS", "82", "Schülerinnen und Schüler mit besonderen Schwierigkeiten im Erlernen des Lesens und Rechtschreibens (LRS)", null, null, null, false, Arrays.asList(
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

	/** Kursart Unterricht in der Herkunftssprache (Muttersprachlicher Unterricht) */
	MEU(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(42000, "MEU", "84", "Unterricht in der Herkunftssprache (Muttersprachlicher Unterricht)", null, null, null, false, Arrays.asList(
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

	/** Kursart Förderung in der deutschen Sprache außerhalb von Sprachfördermaßnahmen */
	FDS(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(43000, "FDS", "85", "Förderung in der deutschen Sprache außerhalb von Sprachfördermaßnahmen", "gemäß Erlass 13-63 Nr. 3", null, null, false, Arrays.asList(
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

	/** Kursart Neigungs- und Projektgruppe */
	NPG(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(44000, "NPG", "31", "Neigungs- und Projektgruppe", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Projektkurs */
	PJK(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(45000, "PJK", "78", "Projektkurs", "gemäß § 11 Abs. 8  APO - GOSt", "PJK", "Projektkurs", true, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null), 
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Profilklasse */
	PROJ(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(46000, "PROJ", "55", "Profilklasse", "gemäß § 21 Abs. 3 APO-SI", null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Unterricht im Klassenverband */
	PUK(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(47000, "PUK", "99", "Unterricht im Klassenverband", null, null, null, false, Arrays.asList(
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
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Pflichtunterricht für Teile von Klassen */
	PUT(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(48000, "PUT", "00", "Pflichtunterricht für Teile von Klassen", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.FW, (Schulgliederung) null), 
			new Pair<>(Schulform.HI, (Schulgliederung) null), 
			new Pair<>(Schulform.WF, (Schulgliederung) null), 
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

	/** Kursart Stütz- oder Förderkurs */
	SF(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(49000, "SF", "42", "Stütz- oder Förderkurs", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Selbstlernphase */
	SLP(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(50000, "SLP", "44", "Selbstlernphase", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtunterricht: Schwerpunktübergreifende Angebote */
	SPA(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(51000, "SPA", "63", "Wahlpflichtunterricht: Schwerpunktübergreifende Angebote", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, Schulgliederung.GY), 
			new Pair<>(Schulform.GM, Schulgliederung.GY), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, Schulgliederung.GY)
		), null, null)
	}),

	/** Kursart Sportförderunterricht */
	SPFU(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(52000, "SPFU", "81", "Sportförderunterricht", null, null, null, false, Arrays.asList(
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

	/** Kursart sonderpädagogische Förderung */
	SPF(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(53000, "SPF", "99", "sonderpädagogische Förderung", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.KS, (Schulgliederung) null), 
			new Pair<>(Schulform.S, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Stützunterricht Wahlbereich - fachbezogen */
	SWFB(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(54000, "SWFB", "01", "Stützunterricht Wahlbereich - fachbezogen", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Stützunterricht Wahlbereich - fachübergreifend */
	SWFW(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(55000, "SWFW", "02", "Stützunterricht Wahlbereich - fachübergreifend", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Unterricht im Rahmen des Schulversuchs Talentschule */
	TAL(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(56000, "TAL", "91", "Unterricht im Rahmen des Schulversuchs Talentschule", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.H, (Schulgliederung) null), 
			new Pair<>(Schulform.R, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Unterricht in der Herkunftssprache anstelle einer Pflichtfremdsprache oder eines Wahlpflichtfaches */
	UMPF(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(57000, "UMPF", "56", "Unterricht in der Herkunftssprache anstelle einer Pflichtfremdsprache oder eines Wahlpflichtfaches", null, null, null, false, Arrays.asList(
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

	/** Kursart Förderunterricht */
	VSU(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(58000, "VSU", "17", "Förderunterricht", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.H, (Schulgliederung) null), 
			new Pair<>(Schulform.R, Schulgliederung.H), 
			new Pair<>(Schulform.SK, Schulgliederung.H), 
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Vertiefungsfach */
	VTF(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(59000, "VTF", "77", "Vertiefungsfach", "gemäß § 8, 11  APO - GOSt", "VTF", "Vertiefungskurs", true, Arrays.asList(
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

	/** Kursart Vertiefungsunterricht Wahlbereich - berufsfeld- / berufsbezogener fachpraxisorientierter Kurs */
	VUW(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(60000, "VUW", "21", "Vertiefungsunterricht Wahlbereich - berufsfeld- / berufsbezogener fachpraxisorientierter Kurs", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtbereich */
	WP(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(61000, "WP", "27", "Wahlpflichtbereich", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.WB, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtbereich: Fremdsprachlich */
	WP1FS(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(62000, "WP1FS", "61", "Wahlpflichtbereich: Fremdsprachlich", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, Schulgliederung.GY), 
			new Pair<>(Schulform.GM, Schulgliederung.GY), 
			new Pair<>(Schulform.GM, Schulgliederung.R), 
			new Pair<>(Schulform.H, Schulgliederung.R), 
			new Pair<>(Schulform.R, (Schulgliederung) null), 
			new Pair<>(Schulform.KS, Schulgliederung.R), 
			new Pair<>(Schulform.S, Schulgliederung.R), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, Schulgliederung.GY), 
			new Pair<>(Schulform.SK, Schulgliederung.R), 
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtbereich: Musisch-künstlerisch */
	WP1MU(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(63000, "WP1MU", "64", "Wahlpflichtbereich: Musisch-künstlerisch", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GM, Schulgliederung.R), 
			new Pair<>(Schulform.H, Schulgliederung.R), 
			new Pair<>(Schulform.R, (Schulgliederung) null), 
			new Pair<>(Schulform.KS, Schulgliederung.R), 
			new Pair<>(Schulform.S, Schulgliederung.R), 
			new Pair<>(Schulform.SK, Schulgliederung.R), 
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtbereich: Naturwissenschaftlich - technisch */
	WP1NT(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(64000, "WP1NT", "63", "Wahlpflichtbereich: Naturwissenschaftlich - technisch", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GM, Schulgliederung.R), 
			new Pair<>(Schulform.H, Schulgliederung.R), 
			new Pair<>(Schulform.R, (Schulgliederung) null), 
			new Pair<>(Schulform.KS, Schulgliederung.R), 
			new Pair<>(Schulform.S, Schulgliederung.R), 
			new Pair<>(Schulform.SK, Schulgliederung.R), 
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtbereich: Sozialwissenschaftlich */
	WP1SW(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(65000, "WP1SW", "62", "Wahlpflichtbereich: Sozialwissenschaftlich", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, Schulgliederung.GY), 
			new Pair<>(Schulform.GM, Schulgliederung.R), 
			new Pair<>(Schulform.H, Schulgliederung.R), 
			new Pair<>(Schulform.R, (Schulgliederung) null), 
			new Pair<>(Schulform.KS, Schulgliederung.R), 
			new Pair<>(Schulform.S, Schulgliederung.R), 
			new Pair<>(Schulform.SK, Schulgliederung.R), 
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtbereich: Wirtschaftlich */
	WP1WW(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(66000, "WP1WW", "68", "Wahlpflichtbereich: Wirtschaftlich", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GM, Schulgliederung.R), 
			new Pair<>(Schulform.H, Schulgliederung.R), 
			new Pair<>(Schulform.R, (Schulgliederung) null), 
			new Pair<>(Schulform.KS, Schulgliederung.R), 
			new Pair<>(Schulform.S, Schulgliederung.R), 
			new Pair<>(Schulform.SK, Schulgliederung.R), 
			new Pair<>(Schulform.SR, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtfach */
	WPF(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(67000, "WPF", "29", "Wahlpflichtfach", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.H, (Schulgliederung) null), 
			new Pair<>(Schulform.R, Schulgliederung.H), 
			new Pair<>(Schulform.KS, (Schulgliederung) null), 
			new Pair<>(Schulform.S, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, Schulgliederung.H), 
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtbereich I */
	WPI(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(68000, "WPI", "10", "Wahlpflichtbereich I", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtbereich I: 2. Fremdsprache */
	WPIGY(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(69000, "WPIGY", "61", "Wahlpflichtbereich I: 2. Fremdsprache", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GY, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Wahlpflichtbereich II - Fächerkombination im math.-naturwiss, gesellschaftswiss. oder künstlerischen Schwerpunkt */
	WPII(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(70000, "WPII", "62", "Wahlpflichtbereich II - Fächerkombination im math.-naturwiss, gesellschaftswiss. oder künstlerischen Schwerpunkt", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GM, Schulgliederung.GY), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.H, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, Schulgliederung.GY)
		), null, null)
	}),

	/** Kursart Wahlpflichtunterricht */
	WPU(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(71000, "WPU", "26", "Wahlpflichtunterricht", null, null, null, false, Arrays.asList(
			new Pair<>(Schulform.GM, Schulgliederung.H), 
			new Pair<>(Schulform.H, (Schulgliederung) null), 
			new Pair<>(Schulform.R, Schulgliederung.H), 
			new Pair<>(Schulform.KS, (Schulgliederung) null), 
			new Pair<>(Schulform.S, (Schulgliederung) null), 
			new Pair<>(Schulform.SK, Schulgliederung.H), 
			new Pair<>(Schulform.V, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart Zusatzkurs */
	ZK(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(72000, "ZK", "76", "Zusatzkurs", "gemäß § 12 APO-GOSt", "ZK", "Zusatzkurs", true, Arrays.asList(
			new Pair<>(Schulform.BK, (Schulgliederung) null), 
			new Pair<>(Schulform.GE, (Schulgliederung) null), 
			new Pair<>(Schulform.GM, (Schulgliederung) null), 
			new Pair<>(Schulform.GY, (Schulgliederung) null), 
			new Pair<>(Schulform.PS, (Schulgliederung) null), 
			new Pair<>(Schulform.SB, (Schulgliederung) null), 
			new Pair<>(Schulform.SG, (Schulgliederung) null)
		), null, null)
	}),

	/** Kursart zusätzliche Unterrichtsveranstaltung */
	ZUV(new KursartKatalogEintrag[] {
		new KursartKatalogEintrag(73000, "ZUV", "99", "zusätzliche Unterrichtsveranstaltung", null, null, null, true, Arrays.asList(
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
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Kursart */
	public final @NotNull KursartKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Kursart */
	public final @NotNull KursartKatalogEintrag@NotNull[] historie;	

	/** Eine HashMap mit allen zulässigen Kursarten. Der Zugriff erfolgt dabei über die ID */ 
	private static final @NotNull HashMap<@NotNull Long, ZulaessigeKursart> _mapID = new HashMap<>();

	/** Eine HashMap mit zulässigen Kursarten. Der Zugriff erfolgt dabei über das Kürzel */ 
	private static final @NotNull HashMap<@NotNull String, ZulaessigeKursart> _mapKuerzel = new HashMap<>();

	/** Die Informationen zu den Kombinationen aus Schulformen und -gliederungen, wo die Kursart zulässig ist */
	private @NotNull Vector<@NotNull Pair<Schulform, Schulgliederung>>@NotNull[] zulaessig;

	
	/**
	 * Erzeugt eine zulässige Kursart in der Aufzählung.
	 * 
	 * @param historie   die Historie der Kursarten, welches ein Array von {@link KursartKatalogEintrag} ist  
	 */
	@SuppressWarnings("unchecked")
	private ZulaessigeKursart(final @NotNull KursartKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
		// Erzeuge zwei Felder mit den Schulformen und Schulgliederungen für die Historie
		this.zulaessig = (@NotNull Vector<@NotNull Pair<Schulform, Schulgliederung>>@NotNull[])Array.newInstance(Vector.class, historie.length); 
		for (int i = 0; i < historie.length; i++) {
			this.zulaessig[i] = new Vector<>();
			for (final @NotNull SchulformSchulgliederung kuerzelSfSgl : historie[i].zulaessig) {
				final Schulform sf = Schulform.getByKuerzel(kuerzelSfSgl.schulform);
				if (sf == null)
					continue;
				final Schulgliederung sgl = kuerzelSfSgl.gliederung == null ? null : Schulgliederung.getByKuerzel(kuerzelSfSgl.gliederung);
				this.zulaessig[i].add(new Pair<>(sf, sgl));
			}
		}
	}

	
	/**
	 * Gibt eine Map von den IDs der Kursarten auf die zugehörigen Kursarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs der Kursarten auf die zugehörigen Kursarten
	 */
	private static @NotNull HashMap<@NotNull Long, ZulaessigeKursart> getMapByID() {
		if (_mapID.size() == 0)
			for (final ZulaessigeKursart s : ZulaessigeKursart.values())
				if (s.daten != null)
					_mapID.put(s.daten.id, s);				
		return _mapID;
	}
	
	
	/**
	 * Gibt eine Map von den Kürzeln der Kursarten auf die zugehörigen Kursarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Kursarten auf die zugehörigen Kursarten
	 */
	private static @NotNull HashMap<@NotNull String, ZulaessigeKursart> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0)
			for (final ZulaessigeKursart s : ZulaessigeKursart.values())
				if (s.daten != null)
					_mapKuerzel.put(s.daten.kuerzel, s);				
		return _mapKuerzel;
	}
	
	
	/**
	 * Prüft, ob die Schulform bei dieser Kursart in irgendeiner Gliederung der 
	 * angegebenen Schulform zulässig ist.
	 * 
	 * @param schulform    die Schulform
	 * 
	 * @return true, falls die Kursart in der Schulform zulässig ist, ansonsten false.
	 */
	private boolean hasSchulform(final Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return false;
		for (final @NotNull Pair<Schulform, Schulgliederung> sfsgl : zulaessig[0]) {
			if (sfsgl.a == schulform)
				return true;
		}
		return false;
	}


	/**
	 * Bestimmt alle Kursarten, die in irgendeiner Gliederung der angegebenen Schulform
	 * zulässig sind.
	 *  
	 * @param schulform    die Schulform
	 * 
	 * @return die zulässigen Kursarten in der angegebenen Schulform
	 */
	public static @NotNull List<ZulaessigeKursart> get(final Schulform schulform) {
		final @NotNull Vector<ZulaessigeKursart> kursarten = new Vector<>();
		if (schulform == null)
			return kursarten;
		for (final ZulaessigeKursart kursart : ZulaessigeKursart.values())
			if (kursart.hasSchulform(schulform))
				kursarten.add(kursart);
		return kursarten;
	}


	/**
	 * Liefert alle Kombinationen aus Schulformen und Schulgliederungen zurück,
	 * bei denen die Kursart zulässig ist.
	 * 
	 * @return eine Liste der Kombinationen aus Schulformen und Schulgliederungen
	 */
	public @NotNull List<@NotNull Pair<Schulform, Schulgliederung>> getGliederungen() {
		return zulaessig[0];
	}


	/**
	 * Bestimmt anhand des Statistik-Kürzels, die zulässige Kursart. 
	 * 
	 * @param kursart   das Statistik-Kürzel
	 * 
	 * @return die Kursart oder null, wenn keine Zuordnung für das übergebene Kürzel vorhanden ist
	 */
	public static ZulaessigeKursart getByASDKursart(final String kursart) {
		return getMapByKuerzel().get(kursart);
	}

}
