import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { KursartKatalogEintrag } from '../../../core/data/kurse/KursartKatalogEintrag';
import { Schulgliederung, cast_de_svws_nrw_core_types_schule_Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { Pair } from '../../../core/adt/Pair';

export class ZulaessigeKursart extends JavaEnum<ZulaessigeKursart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<ZulaessigeKursart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, ZulaessigeKursart> = new Map<string, ZulaessigeKursart>();

	/**
	 * Kursart 3. Abiturfach
	 */
	public static readonly AB3 : ZulaessigeKursart = new ZulaessigeKursart("AB3", 0, [new KursartKatalogEintrag(0, "AB3", "71", "3. Abiturfach", null, "GK", "Grundkurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart 4. Abiturfach
	 */
	public static readonly AB4 : ZulaessigeKursart = new ZulaessigeKursart("AB4", 1, [new KursartKatalogEintrag(1000, "AB4", "71", "4. Abiturfach", null, "GK", "Grundkurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Arbeitsgemeinschaft gemäß APO SI
	 */
	public static readonly AG : ZulaessigeKursart = new ZulaessigeKursart("AG", 2, [new KursartKatalogEintrag(2000, "AG", "67", "Arbeitsgemeinschaft gemäß APO SI", "gemäß § 3 Abs. 6 APO-SI", null, null, false, Arrays.asList(new Pair(Schulform.GE, Schulgliederung.GY), new Pair(Schulform.GM, Schulgliederung.GY), new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.GM, Schulgliederung.R), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.GY), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.SK, Schulgliederung.R), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Arbeitsgemeinschaft im Ganztagsbereich
	 */
	public static readonly AGGT : ZulaessigeKursart = new ZulaessigeKursart("AGGT", 3, [new KursartKatalogEintrag(3000, "AGGT", "36", "Arbeitsgemeinschaft im Ganztagsbereich", "gemäß § 9 Abs. 2, 3  SchulG", null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Stütz- und Angleichungskurs / Förderunterricht
	 */
	public static readonly AGKWB : ZulaessigeKursart = new ZulaessigeKursart("AGKWB", 4, [new KursartKatalogEintrag(4000, "AGKWB", "28", "Stütz- und Angleichungskurs / Förderunterricht", null, null, null, false, Arrays.asList(new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Angleichungskurs
	 */
	public static readonly AGK : ZulaessigeKursart = new ZulaessigeKursart("AGK", 5, [new KursartKatalogEintrag(5000, "AGK", "73", "Angleichungskurs", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Arbeits- bzw. Übungsstunde
	 */
	public static readonly AST : ZulaessigeKursart = new ZulaessigeKursart("AST", 6, [new KursartKatalogEintrag(6000, "AST", "33", "Arbeits- bzw. Übungsstunde ", "gemäß § 9 Abs. 2, 3  SchulG", null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Begegnung mit Sprachen in der Primarstufe
	 */
	public static readonly BSP : ZulaessigeKursart = new ZulaessigeKursart("BSP", 7, [new KursartKatalogEintrag(7000, "BSP", "59", "Begegnung mit Sprachen in der Primarstufe", null, null, null, false, Arrays.asList(new Pair(Schulform.FW, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WF, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Unterricht im Rahmen von KAOA einschl. Schule trifft Arbeitswelt
	 */
	public static readonly BUS : ZulaessigeKursart = new ZulaessigeKursart("BUS", 8, [new KursartKatalogEintrag(8000, "BUS", "94", "Unterricht im Rahmen von KAOA einschl. Schule trifft Arbeitswelt", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Erweiterungsebene/-kurs
	 */
	public static readonly E : ZulaessigeKursart = new ZulaessigeKursart("E", 9, [new KursartKatalogEintrag(9000, "E", "02", "Erweiterungsebene/-kurs", null, "DK", "Differenzierungskurs", false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Erweiterungskurs – Bildungsgang Hauptschule
	 */
	public static readonly E_H : ZulaessigeKursart = new ZulaessigeKursart("E_H", 10, [new KursartKatalogEintrag(10000, "E_H", "16", "Erweiterungskurs – Bildungsgang Hauptschule", null, null, null, false, Arrays.asList(new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Erweitertes Bildungsangebot
	 */
	public static readonly EBA : ZulaessigeKursart = new ZulaessigeKursart("EBA", 11, [new KursartKatalogEintrag(11000, "EBA", "88", "Erweitertes Bildungsangebot", null, null, null, false, Arrays.asList(new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Einführung in die 2. Fremdsprache oder Ersatzfach
	 */
	public static readonly EF2 : ZulaessigeKursart = new ZulaessigeKursart("EF2", 12, [new KursartKatalogEintrag(12000, "EF2", "57", "Einführung in die 2. Fremdsprache oder Ersatzfach", null, null, null, false, Arrays.asList(new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Ersatzfach für Sport als 4. Abiturfach
	 */
	public static readonly EFSP : ZulaessigeKursart = new ZulaessigeKursart("EFSP", 13, [new KursartKatalogEintrag(13000, "EFSP", "71", "Ersatzfach für Sport als 4. Abiturfach", null, "GK", "Grundkurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Ergänzungsstunden
	 */
	public static readonly EGS1 : ZulaessigeKursart = new ZulaessigeKursart("EGS1", 14, [new KursartKatalogEintrag(14000, "EGS1", "96", "Ergänzungsstunden", "gemäß § 3 Abs. 1, 3 und § 15 Abs. 3 APO-SI", null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Ergänzungsstunden mit Benotung
	 */
	public static readonly EGSN : ZulaessigeKursart = new ZulaessigeKursart("EGSN", 15, [new KursartKatalogEintrag(15000, "EGSN", "97", "Ergänzungsstunden mit Benotung", "gemäß § 3 Abs. 1, 3 und § 19 Abs. 3 Nr.2 und 4 APO-SI", null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Ergänzungs- oder Vertiefungskurs
	 */
	public static readonly EV : ZulaessigeKursart = new ZulaessigeKursart("EV", 16, [new KursartKatalogEintrag(16000, "EV", "41", "Ergänzungs- oder Vertiefungskurs", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Erweiterungsunterricht Wahlbereich - berufsbezogen, fachübergreifend
	 */
	public static readonly EWBF : ZulaessigeKursart = new ZulaessigeKursart("EWBF", 17, [new KursartKatalogEintrag(17000, "EWBF", "12", "Erweiterungsunterricht Wahlbereich - berufsbezogen, fachübergreifend", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Erweiterungsunterricht Wahlbereich - fach-, jedoch nicht abschlussbezogen
	 */
	public static readonly EWF : ZulaessigeKursart = new ZulaessigeKursart("EWF", 18, [new KursartKatalogEintrag(18000, "EWF", "11", "Erweiterungsunterricht Wahlbereich - fach-, jedoch nicht abschlussbezogen", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Erweiterungsunterricht Wahlbereich - fach- und abschlussbezogen
	 */
	public static readonly EWFA : ZulaessigeKursart = new ZulaessigeKursart("EWFA", 19, [new KursartKatalogEintrag(19000, "EWFA", "10", "Erweiterungsunterricht Wahlbereich - fach- und abschlussbezogen", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlplflichtunterricht: Einzelfach im math.-naturw., techn., gesellschaftsw., künstlerischen Schwerpunkt
	 */
	public static readonly F3 : ZulaessigeKursart = new ZulaessigeKursart("F3", 20, [new KursartKatalogEintrag(20000, "F3", "64", "Wahlplflichtunterricht: Einzelfach im math.-naturw., techn., gesellschaftsw., künstlerischen Schwerpunkt", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, Schulgliederung.GY), new Pair(Schulform.GM, Schulgliederung.GY), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart freiwillige Arbeitsgemeinschaft
	 */
	public static readonly FAG : ZulaessigeKursart = new ZulaessigeKursart("FAG", 21, [new KursartKatalogEintrag(21000, "FAG", "58", "freiwillige Arbeitsgemeinschaft", null, null, null, false, Arrays.asList(new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Fachbezogener Förderunterricht
	 */
	public static readonly FFU : ZulaessigeKursart = new ZulaessigeKursart("FFU", 22, [new KursartKatalogEintrag(22000, "FFU", "04", "Fachbezogener Förderunterricht", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Förderangebot Ganztagsschule
	 */
	public static readonly FOGT : ZulaessigeKursart = new ZulaessigeKursart("FOGT", 23, [new KursartKatalogEintrag(23000, "FOGT", "37", "Förderangebot Ganztagsschule", "gemäß § 9 Abs. 2, 3  SchulG", null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Förderung neu zugewanderter Schüler in Deutschfördergruppen (teilweise äußere und innere Differenzierung)
	 */
	public static readonly DFG : ZulaessigeKursart = new ZulaessigeKursart("DFG", 24, [new KursartKatalogEintrag(24000, "DFG", "89", "Förderung neu zugewanderter Schüler in Deutschfördergruppen (teilweise äußere und innere Differenzierung)", "gemäß Erlass 13-63 Nr. 3", null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich II (Kl. 09 und 10, bei G8: 08 und 09): 3. Fremdsprache
	 */
	public static readonly FS3 : ZulaessigeKursart = new ZulaessigeKursart("FS3", 25, [new KursartKatalogEintrag(25000, "FS3", "65", "Wahlpflichtbereich II (Kl. 09 und 10, bei G8: 08 und 09): 3. Fremdsprache", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, Schulgliederung.GY), new Pair(Schulform.GM, Schulgliederung.GY), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.GY)), null, null)]);

	/**
	 * Kursart Förderung neu zugewanderter Schüler in Deutschförderklassen (vollständige äußere Differenzierung)
	 */
	public static readonly DFK : ZulaessigeKursart = new ZulaessigeKursart("DFK", 26, [new KursartKatalogEintrag(26000, "DFK", "90", "Förderung neu zugewanderter Schüler in Deutschförderklassen (vollständige äußere Differenzierung)", "gemäß Erlass 13-63 Nr. 3", null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Förderunterricht
	 */
	public static readonly FU : ZulaessigeKursart = new ZulaessigeKursart("FU", 27, [new KursartKatalogEintrag(27000, "FU", "01", "Förderunterricht", null, null, null, false, Arrays.asList(new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Stützunterricht Wahlbereich - Förderunterricht für ausländische u. ausgesiedelte Schüler
	 */
	public static readonly FUAUS : ZulaessigeKursart = new ZulaessigeKursart("FUAUS", 28, [new KursartKatalogEintrag(28000, "FUAUS", "03", "Stützunterricht Wahlbereich - Förderunterricht für ausländische u. ausgesiedelte Schüler", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Fachunabhängiger Förderunterricht
	 */
	public static readonly FUF : ZulaessigeKursart = new ZulaessigeKursart("FUF", 29, [new KursartKatalogEintrag(29000, "FUF", "05", "Fachunabhängiger Förderunterricht", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Förderunterricht im Klassenverband
	 */
	public static readonly FUK : ZulaessigeKursart = new ZulaessigeKursart("FUK", 30, [new KursartKatalogEintrag(30000, "FUK", "99", "Förderunterricht im Klassenverband", null, null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Förderunterricht für Teile von Klassen
	 */
	public static readonly FUT : ZulaessigeKursart = new ZulaessigeKursart("FUT", 31, [new KursartKatalogEintrag(31000, "FUT", "00", "Förderunterricht für Teile von Klassen", null, null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart fächerübergreifender Kurs
	 */
	public static readonly FUEK : ZulaessigeKursart = new ZulaessigeKursart("FUEK", 32, [new KursartKatalogEintrag(32000, "FUEK", "79", "fächerübergreifender Kurs", null, null, null, false, Arrays.asList(new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Grundebene/-kurs
	 */
	public static readonly G : ZulaessigeKursart = new ZulaessigeKursart("G", 33, [new KursartKatalogEintrag(33000, "G", "01", "Grundebene/-kurs", null, "DK", "Differenzierungskurs", false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Grundkurs – Bildungsgang Hauptschule
	 */
	public static readonly G_H : ZulaessigeKursart = new ZulaessigeKursart("G_H", 34, [new KursartKatalogEintrag(34000, "G_H", "13", "Grundkurs – Bildungsgang Hauptschule", null, null, null, false, Arrays.asList(new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Grundkurs mündlich
	 */
	public static readonly GKM : ZulaessigeKursart = new ZulaessigeKursart("GKM", 35, [new KursartKatalogEintrag(35000, "GKM", "71", "Grundkurs mündlich", null, "GK", "Grundkurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Grundkurs schriftlich
	 */
	public static readonly GKS : ZulaessigeKursart = new ZulaessigeKursart("GKS", 36, [new KursartKatalogEintrag(36000, "GKS", "71", "Grundkurs schriftlich", null, "GK", "Grundkurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Hausunterricht
	 */
	public static readonly HU : ZulaessigeKursart = new ZulaessigeKursart("HU", 37, [new KursartKatalogEintrag(37000, "HU", "99", "Hausunterricht", null, null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Zusätzlicher Förderunterricht im Rahmen der Initiative Komm mit
	 */
	public static readonly KMFOE : ZulaessigeKursart = new ZulaessigeKursart("KMFOE", 38, [new KursartKatalogEintrag(38000, "KMFOE", "95", "Zusätzlicher Förderunterricht im Rahmen der Initiative Komm mit", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, 2021)]);

	/**
	 * Kursart Leistungskurs I
	 */
	public static readonly LK1 : ZulaessigeKursart = new ZulaessigeKursart("LK1", 39, [new KursartKatalogEintrag(39000, "LK1", "72", "Leistungskurs I", null, "LK", "Leistungskurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Leistungskurs II
	 */
	public static readonly LK2 : ZulaessigeKursart = new ZulaessigeKursart("LK2", 40, [new KursartKatalogEintrag(40000, "LK2", "72", "Leistungskurs II", null, "LK", "Leistungskurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Schülerinnen und Schüler mit besonderen Schwierigkeiten im Erlernen des Lesens und Rechtschreibens (LRS)
	 */
	public static readonly LRS : ZulaessigeKursart = new ZulaessigeKursart("LRS", 41, [new KursartKatalogEintrag(41000, "LRS", "82", "Schülerinnen und Schüler mit besonderen Schwierigkeiten im Erlernen des Lesens und Rechtschreibens (LRS)", null, null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Unterricht in der Herkunftssprache (Muttersprachlicher Unterricht)
	 */
	public static readonly MEU : ZulaessigeKursart = new ZulaessigeKursart("MEU", 42, [new KursartKatalogEintrag(42000, "MEU", "84", "Unterricht in der Herkunftssprache (Muttersprachlicher Unterricht)", null, null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Förderung in der deutschen Sprache außerhalb von Sprachfördermaßnahmen
	 */
	public static readonly FDS : ZulaessigeKursart = new ZulaessigeKursart("FDS", 43, [new KursartKatalogEintrag(43000, "FDS", "85", "Förderung in der deutschen Sprache außerhalb von Sprachfördermaßnahmen", "gemäß Erlass 13-63 Nr. 3", null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Neigungs- und Projektgruppe
	 */
	public static readonly NPG : ZulaessigeKursart = new ZulaessigeKursart("NPG", 44, [new KursartKatalogEintrag(44000, "NPG", "31", "Neigungs- und Projektgruppe", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Projektkurs
	 */
	public static readonly PJK : ZulaessigeKursart = new ZulaessigeKursart("PJK", 45, [new KursartKatalogEintrag(45000, "PJK", "78", "Projektkurs", "gemäß § 11 Abs. 8  APO - GOSt", "PJK", "Projektkurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Profilklasse
	 */
	public static readonly PROJ : ZulaessigeKursart = new ZulaessigeKursart("PROJ", 46, [new KursartKatalogEintrag(46000, "PROJ", "55", "Profilklasse", "gemäß § 21 Abs. 3 APO-SI", null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Unterricht im Klassenverband
	 */
	public static readonly PUK : ZulaessigeKursart = new ZulaessigeKursart("PUK", 47, [new KursartKatalogEintrag(47000, "PUK", "99", "Unterricht im Klassenverband", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Pflichtunterricht für Teile von Klassen
	 */
	public static readonly PUT : ZulaessigeKursart = new ZulaessigeKursart("PUT", 48, [new KursartKatalogEintrag(48000, "PUT", "00", "Pflichtunterricht für Teile von Klassen", null, null, null, false, Arrays.asList(new Pair(Schulform.FW, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WF, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Stütz- oder Förderkurs
	 */
	public static readonly SF : ZulaessigeKursart = new ZulaessigeKursart("SF", 49, [new KursartKatalogEintrag(49000, "SF", "42", "Stütz- oder Förderkurs", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Selbstlernphase
	 */
	public static readonly SLP : ZulaessigeKursart = new ZulaessigeKursart("SLP", 50, [new KursartKatalogEintrag(50000, "SLP", "44", "Selbstlernphase", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtunterricht: Schwerpunktübergreifende Angebote
	 */
	public static readonly SPA : ZulaessigeKursart = new ZulaessigeKursart("SPA", 51, [new KursartKatalogEintrag(51000, "SPA", "63", "Wahlpflichtunterricht: Schwerpunktübergreifende Angebote", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, Schulgliederung.GY), new Pair(Schulform.GM, Schulgliederung.GY), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, Schulgliederung.GY)), null, null)]);

	/**
	 * Kursart Sportförderunterricht
	 */
	public static readonly SPFU : ZulaessigeKursart = new ZulaessigeKursart("SPFU", 52, [new KursartKatalogEintrag(52000, "SPFU", "81", "Sportförderunterricht", null, null, null, false, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart sonderpädagogische Förderung
	 */
	public static readonly SPF : ZulaessigeKursart = new ZulaessigeKursart("SPF", 53, [new KursartKatalogEintrag(53000, "SPF", "99", "sonderpädagogische Förderung", null, null, null, false, Arrays.asList(new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Stützunterricht Wahlbereich - fachbezogen
	 */
	public static readonly SWFB : ZulaessigeKursart = new ZulaessigeKursart("SWFB", 54, [new KursartKatalogEintrag(54000, "SWFB", "01", "Stützunterricht Wahlbereich - fachbezogen", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Stützunterricht Wahlbereich - fachübergreifend
	 */
	public static readonly SWFW : ZulaessigeKursart = new ZulaessigeKursart("SWFW", 55, [new KursartKatalogEintrag(55000, "SWFW", "02", "Stützunterricht Wahlbereich - fachübergreifend", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Unterricht im Rahmen des Schulversuchs Talentschule
	 */
	public static readonly TAL : ZulaessigeKursart = new ZulaessigeKursart("TAL", 56, [new KursartKatalogEintrag(56000, "TAL", "91", "Unterricht im Rahmen des Schulversuchs Talentschule", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Unterricht in der Herkunftssprache anstelle einer Pflichtfremdsprache oder eines Wahlpflichtfaches
	 */
	public static readonly UMPF : ZulaessigeKursart = new ZulaessigeKursart("UMPF", 57, [new KursartKatalogEintrag(57000, "UMPF", "56", "Unterricht in der Herkunftssprache anstelle einer Pflichtfremdsprache oder eines Wahlpflichtfaches", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Förderunterricht
	 */
	public static readonly VSU : ZulaessigeKursart = new ZulaessigeKursart("VSU", 58, [new KursartKatalogEintrag(58000, "VSU", "17", "Förderunterricht", null, null, null, false, Arrays.asList(new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Vertiefungsfach
	 */
	public static readonly VTF : ZulaessigeKursart = new ZulaessigeKursart("VTF", 59, [new KursartKatalogEintrag(59000, "VTF", "77", "Vertiefungsfach", "gemäß § 8, 11  APO - GOSt", "VTF", "Vertiefungskurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Vertiefungsunterricht Wahlbereich - berufsfeld- / berufsbezogener fachpraxisorientierter Kurs
	 */
	public static readonly VUW : ZulaessigeKursart = new ZulaessigeKursart("VUW", 60, [new KursartKatalogEintrag(60000, "VUW", "21", "Vertiefungsunterricht Wahlbereich - berufsfeld- / berufsbezogener fachpraxisorientierter Kurs", null, null, null, false, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich
	 */
	public static readonly WP : ZulaessigeKursart = new ZulaessigeKursart("WP", 61, [new KursartKatalogEintrag(61000, "WP", "27", "Wahlpflichtbereich", null, null, null, false, Arrays.asList(new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich: Fremdsprachlich
	 */
	public static readonly WP1FS : ZulaessigeKursart = new ZulaessigeKursart("WP1FS", 62, [new KursartKatalogEintrag(62000, "WP1FS", "61", "Wahlpflichtbereich: Fremdsprachlich", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, Schulgliederung.GY), new Pair(Schulform.GM, Schulgliederung.GY), new Pair(Schulform.GM, Schulgliederung.R), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, Schulgliederung.R), new Pair(Schulform.S, Schulgliederung.R), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.GY), new Pair(Schulform.SK, Schulgliederung.R), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich: Musisch-künstlerisch
	 */
	public static readonly WP1MU : ZulaessigeKursart = new ZulaessigeKursart("WP1MU", 63, [new KursartKatalogEintrag(63000, "WP1MU", "64", "Wahlpflichtbereich: Musisch-künstlerisch", null, null, null, false, Arrays.asList(new Pair(Schulform.GM, Schulgliederung.R), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, Schulgliederung.R), new Pair(Schulform.S, Schulgliederung.R), new Pair(Schulform.SK, Schulgliederung.R), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich: Naturwissenschaftlich - technisch
	 */
	public static readonly WP1NT : ZulaessigeKursart = new ZulaessigeKursart("WP1NT", 64, [new KursartKatalogEintrag(64000, "WP1NT", "63", "Wahlpflichtbereich: Naturwissenschaftlich - technisch", null, null, null, false, Arrays.asList(new Pair(Schulform.GM, Schulgliederung.R), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, Schulgliederung.R), new Pair(Schulform.S, Schulgliederung.R), new Pair(Schulform.SK, Schulgliederung.R), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich: Sozialwissenschaftlich
	 */
	public static readonly WP1SW : ZulaessigeKursart = new ZulaessigeKursart("WP1SW", 65, [new KursartKatalogEintrag(65000, "WP1SW", "62", "Wahlpflichtbereich: Sozialwissenschaftlich", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, Schulgliederung.GY), new Pair(Schulform.GM, Schulgliederung.R), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, Schulgliederung.R), new Pair(Schulform.S, Schulgliederung.R), new Pair(Schulform.SK, Schulgliederung.R), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich: Wirtschaftlich
	 */
	public static readonly WP1WW : ZulaessigeKursart = new ZulaessigeKursart("WP1WW", 66, [new KursartKatalogEintrag(66000, "WP1WW", "68", "Wahlpflichtbereich: Wirtschaftlich", null, null, null, false, Arrays.asList(new Pair(Schulform.GM, Schulgliederung.R), new Pair(Schulform.H, Schulgliederung.R), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, Schulgliederung.R), new Pair(Schulform.S, Schulgliederung.R), new Pair(Schulform.SK, Schulgliederung.R), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtfach
	 */
	public static readonly WPF : ZulaessigeKursart = new ZulaessigeKursart("WPF", 67, [new KursartKatalogEintrag(67000, "WPF", "29", "Wahlpflichtfach", null, null, null, false, Arrays.asList(new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich I
	 */
	public static readonly WPI : ZulaessigeKursart = new ZulaessigeKursart("WPI", 68, [new KursartKatalogEintrag(68000, "WPI", "10", "Wahlpflichtbereich I", null, null, null, false, Arrays.asList(new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich I: 2. Fremdsprache
	 */
	public static readonly WPIGY : ZulaessigeKursart = new ZulaessigeKursart("WPIGY", 69, [new KursartKatalogEintrag(69000, "WPIGY", "61", "Wahlpflichtbereich I: 2. Fremdsprache", null, null, null, false, Arrays.asList(new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Wahlpflichtbereich II - Fächerkombination im math.-naturwiss, gesellschaftswiss. oder künstlerischen Schwerpunkt
	 */
	public static readonly WPII : ZulaessigeKursart = new ZulaessigeKursart("WPII", 70, [new KursartKatalogEintrag(70000, "WPII", "62", "Wahlpflichtbereich II - Fächerkombination im math.-naturwiss, gesellschaftswiss. oder künstlerischen Schwerpunkt", null, null, null, false, Arrays.asList(new Pair(Schulform.GM, Schulgliederung.GY), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.GY)), null, null)]);

	/**
	 * Kursart Wahlpflichtunterricht
	 */
	public static readonly WPU : ZulaessigeKursart = new ZulaessigeKursart("WPU", 71, [new KursartKatalogEintrag(71000, "WPU", "26", "Wahlpflichtunterricht", null, null, null, false, Arrays.asList(new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart Zusatzkurs
	 */
	public static readonly ZK : ZulaessigeKursart = new ZulaessigeKursart("ZK", 72, [new KursartKatalogEintrag(72000, "ZK", "76", "Zusatzkurs", "gemäß § 12 APO-GOSt", "ZK", "Zusatzkurs", true, Arrays.asList(new Pair(Schulform.BK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Kursart zusätzliche Unterrichtsveranstaltung
	 */
	public static readonly ZUV : ZulaessigeKursart = new ZulaessigeKursart("ZUV", 73, [new KursartKatalogEintrag(73000, "ZUV", "99", "zusätzliche Unterrichtsveranstaltung", null, null, null, true, Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Kursart
	 */
	public readonly daten : KursartKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Kursart
	 */
	public readonly historie : Array<KursartKatalogEintrag>;

	/**
	 * Eine HashMap mit allen zulässigen Kursarten. Der Zugriff erfolgt dabei über die ID
	 */
	private static readonly _mapID : HashMap<number, ZulaessigeKursart> = new HashMap<number, ZulaessigeKursart>();

	/**
	 * Eine HashMap mit zulässigen Kursarten. Der Zugriff erfolgt dabei über das Kürzel
	 */
	private static readonly _mapKuerzel : HashMap<string, ZulaessigeKursart> = new HashMap<string, ZulaessigeKursart>();

	/**
	 * Die Informationen zu den Kombinationen aus Schulformen und -gliederungen, wo die Kursart zulässig ist
	 */
	private readonly zulaessig : Array<ArrayList<Pair<Schulform, Schulgliederung | null>>>;

	/**
	 * Die Zuordnung der speziellen Kursarten zu den allgemeinen Kursarten
	 */
	private static readonly _mapByAllgemein : HashMap<string, List<ZulaessigeKursart>> = new HashMap<string, List<ZulaessigeKursart>>();

	/**
	 * Erzeugt eine zulässige Kursart in der Aufzählung.
	 *
	 * @param historie   die Historie der Kursarten, welches ein Array von {@link KursartKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<KursartKatalogEintrag>) {
		super(name, ordinal);
		ZulaessigeKursart.all_values_by_ordinal.push(this);
		ZulaessigeKursart.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.zulaessig = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++) {
			this.zulaessig[i] = new ArrayList();
			for (const kuerzelSfSgl of historie[i].zulaessig) {
				const sf : Schulform | null = Schulform.getByKuerzel(kuerzelSfSgl.schulform);
				if (sf === null)
					continue;
				const sgl : Schulgliederung | null = (kuerzelSfSgl.gliederung === null) ? null : Schulgliederung.getByKuerzel(kuerzelSfSgl.gliederung);
				this.zulaessig[i].add(new Pair(sf, sgl));
			}
		}
	}

	/**
	 * Gibt eine Map von den IDs der Kursarten auf die zugehörigen Kursarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Kursarten auf die zugehörigen Kursarten
	 */
	private static getMapByID() : HashMap<number, ZulaessigeKursart> {
		if (ZulaessigeKursart._mapID.size() === 0)
			for (const s of ZulaessigeKursart.values())
				if (s.daten !== null)
					ZulaessigeKursart._mapID.put(s.daten.id, s);
		return ZulaessigeKursart._mapID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Kursarten auf die zugehörigen Kursarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Kursarten auf die zugehörigen Kursarten
	 */
	private static getMapByKuerzel() : HashMap<string, ZulaessigeKursart> {
		if (ZulaessigeKursart._mapKuerzel.size() === 0)
			for (const s of ZulaessigeKursart.values())
				if (s.daten !== null)
					ZulaessigeKursart._mapKuerzel.put(s.daten.kuerzel, s);
		return ZulaessigeKursart._mapKuerzel;
	}

	private static getMapByAllgemeinemKuerzel() : HashMap<string, List<ZulaessigeKursart>> {
		if (ZulaessigeKursart._mapByAllgemein.size() === 0) {
			for (const k of ZulaessigeKursart.values()) {
				if (k.daten !== null) {
					const allgKursart : string = (k.daten.kuerzelAllg !== null) ? k.daten.kuerzelAllg : "";
					let list : List<ZulaessigeKursart> | null = ZulaessigeKursart._mapByAllgemein.get(allgKursart);
					if (list === null) {
						list = new ArrayList();
						ZulaessigeKursart._mapByAllgemein.put(allgKursart, list);
					}
					list.add(k);
				}
				if (k.daten.kuerzelAllg === null) {
					let list : List<ZulaessigeKursart> | null = ZulaessigeKursart._mapByAllgemein.get(k.daten.kuerzel);
					if (list === null) {
						list = new ArrayList();
						ZulaessigeKursart._mapByAllgemein.put(k.daten.kuerzel, list);
					}
					list.add(k);
				}
			}
		}
		return ZulaessigeKursart._mapByAllgemein;
	}

	/**
	 * Prüft, ob die Schulform bei dieser Kursart in irgendeiner Gliederung der
	 * angegebenen Schulform zulässig ist.
	 *
	 * @param schulform    die Schulform
	 *
	 * @return true, falls die Kursart in der Schulform zulässig ist, ansonsten false.
	 */
	private hasSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null))
			return false;
		for (const sfsgl of this.zulaessig[0]) {
			if (sfsgl.a === schulform)
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
	public static get(schulform : Schulform | null) : List<ZulaessigeKursart> {
		const kursarten : ArrayList<ZulaessigeKursart> = new ArrayList<ZulaessigeKursart>();
		if (schulform === null)
			return kursarten;
		for (const kursart of ZulaessigeKursart.values())
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
	public getGliederungen() : List<Pair<Schulform, Schulgliederung | null>> {
		return this.zulaessig[0];
	}

	/**
	 * Bestimmt anhand des Statistik-Kürzels, die zulässige Kursart.
	 *
	 * @param kursart   das Statistik-Kürzel
	 *
	 * @return die Kursart oder null, wenn keine Zuordnung für das übergebene Kürzel vorhanden ist
	 */
	public static getByASDKursart(kursart : string | null) : ZulaessigeKursart | null {
		return ZulaessigeKursart.getMapByKuerzel().get(kursart);
	}

	/**
	 * Bestimmt die Liste der möglichen speziellen Kursarten für die angegebene allgemeine Kursart
	 *
	 * @param allgKursart   die allgemeine Kursart
	 *
	 * @return die Liste der möglichen speziellen Kursarten
	 */
	public static getByAllgemeinerKursart(allgKursart : string) : List<ZulaessigeKursart> {
		if (JavaObject.equalsTranspiler(ZulaessigeKursart.E.daten.kuerzel, (allgKursart))) {
			const result : List<ZulaessigeKursart> = new ArrayList<ZulaessigeKursart>();
			result.add(ZulaessigeKursart.E);
			return result;
		}
		if (JavaObject.equalsTranspiler(ZulaessigeKursart.G.daten.kuerzel, (allgKursart))) {
			const result : List<ZulaessigeKursart> = new ArrayList<ZulaessigeKursart>();
			result.add(ZulaessigeKursart.G);
			return result;
		}
		const result : List<ZulaessigeKursart> | null = ZulaessigeKursart.getMapByAllgemeinemKuerzel().get(allgKursart);
		if (result === null)
			throw new DeveloperNotificationException("Die allgemeine Kursart " + allgKursart! + " existiert nicht.")
		return result;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<ZulaessigeKursart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : ZulaessigeKursart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.kurse.ZulaessigeKursart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kurse.ZulaessigeKursart', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kurse_ZulaessigeKursart(obj : unknown) : ZulaessigeKursart {
	return obj as ZulaessigeKursart;
}
