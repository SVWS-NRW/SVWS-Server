import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GEAbschlussFach, cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFach } from '../../../core/data/abschluss/GEAbschlussFach';
import { Service, cast_de_nrw_schule_svws_core_Service } from '../../../core/Service';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GELeistungsdifferenzierteKursart, cast_de_nrw_schule_svws_core_types_ge_GELeistungsdifferenzierteKursart } from '../../../core/types/ge/GELeistungsdifferenzierteKursart';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../../core/logger/LogLevel';
import { Predicate, cast_java_util_function_Predicate } from '../../../java/util/function/Predicate';
import { AbschlussFaecherGruppe, cast_de_nrw_schule_svws_core_abschluss_ge_AbschlussFaecherGruppe } from '../../../core/abschluss/ge/AbschlussFaecherGruppe';
import { GEAbschlussFaecher, cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFaecher } from '../../../core/data/abschluss/GEAbschlussFaecher';
import { AbschlussErgebnis, cast_de_nrw_schule_svws_core_data_abschluss_AbschlussErgebnis } from '../../../core/data/abschluss/AbschlussErgebnis';
import { SchulabschlussAllgemeinbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussAllgemeinbildend } from '../../../core/types/schule/SchulabschlussAllgemeinbildend';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { AbschlussManager, cast_de_nrw_schule_svws_core_abschluss_AbschlussManager } from '../../../core/abschluss/AbschlussManager';
import { AbschlussFaecherGruppen, cast_de_nrw_schule_svws_core_abschluss_ge_AbschlussFaecherGruppen } from '../../../core/abschluss/ge/AbschlussFaecherGruppen';

export class ServiceAbschlussHA10 extends Service<GEAbschlussFaecher, AbschlussErgebnis> {

	/**
	 * Filter für alle nicht ausgeglichenen Defizite
	 */
	filterDefizit : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => f.note > 4 && (!f.ausgeglichen) };

	/**
	 * Filter für alle mangelhaften Fächer
	 */
	filterMangelhaft : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => f.note === 5 };

	/**
	 * Filter für alle mangelhaften Fächer, die keine ZP10-Fächer sind.
	 */
	filterMangelhaftOhneZP10Faecher : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (f.note === 5) && (!JavaObject.equalsTranspiler("D", (f.kuerzel))) && (!JavaObject.equalsTranspiler("E", (f.kuerzel))) && (!JavaObject.equalsTranspiler("M", (f.kuerzel))) };

	/**
	 * Filter für alle ungenügenden Fächer
	 */
	filterUngenuegend : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => f.note === 6 };

	/**
	 * Filter für alle Fächer, welche als E-Kurs belegt wurden.
	 */
	filterEKurse : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (GELeistungsdifferenzierteKursart.E.hat(f.kursart)) };

	/**
	 * Filter zur Bestimmung aller Fremdsprachen, die nicht als E-Kurs belegt wurden.
	 */
	filterWeitereFremdsprachen : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (!JavaObject.equalsTranspiler("E", (f.kuerzel)) && (f.istFremdsprache !== null) && (f.istFremdsprache === true)) };


	public constructor() {
		super();
	}

	/**
	 * Führt die Abschlussberechnung anhand der übergebenen Abschlussfächer durch
	 * und gibt das Berechnungsergebnis zurück.
	 *
	 * @param input    die Abschlussfächer
	 *
	 * @return das Ergebnis der Abschlussberechnung
	 */
	public handle(input : GEAbschlussFaecher) : AbschlussErgebnis {
		this.logger.logLn(LogLevel.INFO, "Prüfe HA10:");
		this.logger.logLn(LogLevel.DEBUG, "==========");
		if ((input.faecher === null) || (!AbschlussManager.pruefeHat4LeistungsdifferenzierteFaecher(input))) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht genügend leistungsdiffernzierte Fächer gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		if (!AbschlussManager.pruefeKuerzelDuplikate(input)) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden Fächer mit dem gleichen Kürzel zur Abschlussprüfung übergeben. Dies ist nicht zulässig.");
			return AbschlussManager.getErgebnis(null, false);
		}
		let faecher : AbschlussFaecherGruppen = new AbschlussFaecherGruppen(new AbschlussFaecherGruppe(input.faecher, Arrays.asList("D", "M", "LBNW", "LBAL"), null), new AbschlussFaecherGruppe(input.faecher, null, Arrays.asList("D", "M", "LBNW", "LBAL", "BI", "PH", "CH", "AT", "AW", "AH")));
		if (!faecher.fg1.istVollstaendig(Arrays.asList("D", "M", "LBNW", "LBAL"))) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht alle nötigen Leistungen für die Fächergruppe 1 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		if (faecher.fg2.isEmpty()) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Keine Leistungen für die Fächergruppe 2 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		let weitereFS : List<GEAbschlussFach> = faecher.fg2.entferneFaecher(this.filterWeitereFremdsprachen);
		if (weitereFS.size() > 0) {
			for (let fs of weitereFS) {
				if (fs.bezeichnung === null)
					continue;
				this.logger.logLn(LogLevel.DEBUG, " -> Ignoriere weitere Fremdsprache: " + fs.bezeichnung + "(" + fs.note + ")");
			}
		}
		this.logger.logLn(LogLevel.DEBUG, " - ggf. Verbessern der E-Kurs-Noten für die Defizitberechnung:");
		let tmpFaecher : List<GEAbschlussFach> = faecher.getFaecher(this.filterEKurse);
		for (let f of tmpFaecher) {
			if (f.kuerzel === null)
				continue;
			let note : number = f.note;
			let note_neu : number = (note === 1) ? 1 : note - 1;
			this.logger.logLn(LogLevel.DEBUG, "   " + f.kuerzel + "(E):" + note + "->" + note_neu);
			f.note = note_neu;
		}
		this.logger.logLn(LogLevel.DEBUG, " -> FG1: Fächer " + faecher.fg1.toString()!);
		this.logger.logLn(LogLevel.DEBUG, " -> FG2: Fächer " + faecher.fg2.toString()!);
		let abschlussergebnis : AbschlussErgebnis = this.pruefeDefizite(faecher, "");
		if (abschlussergebnis.erworben) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.INFO, " => HA 10: APO-SI §41 (1)");
		} else
			if (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)) {
				this.logger.logLn(LogLevel.INFO, " => kein HA10 - Nachprüfungsmöglichkeite(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis)!);
			} else {
				this.logger.logLn(LogLevel.INFO, " => kein HA10 - KEINE Nachprüfungsmöglichkeiten!");
			}
		return abschlussergebnis;
	}

	/**
	 * Prüft in Bezug auf Defizite, ob der Abschluss erworben wurde.
	 *
	 * @param faecher      die Abschlussfächer nach Fächergruppen sortiert
	 * @param log_indent   die Einrückung für das Logging
	 *
	 * @return das Ergebnis der Abschlussberechnung in Bezug die Defizitberechnung
	 */
	private pruefeDefizite(faecher : AbschlussFaecherGruppen, log_indent : string) : AbschlussErgebnis {
		let fg1_defizite : number = faecher.fg1.getFaecherAnzahl(this.filterDefizit);
		let fg2_defizite : number = faecher.fg2.getFaecherAnzahl(this.filterDefizit);
		let ges_defizite : number = fg1_defizite + fg2_defizite;
		let fg1_mangelhaft : number = faecher.fg1.getFaecherAnzahl(this.filterMangelhaft);
		let fg1_ungenuegend : number = faecher.fg1.getFaecherAnzahl(this.filterUngenuegend);
		let fg2_ungenuegend : number = faecher.fg2.getFaecherAnzahl(this.filterUngenuegend);
		if (fg1_defizite > 0)
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> FG1: Defizit" + (fg1_defizite > 1 ? "e" : "") + ": " + faecher.fg1.getKuerzelListe(this.filterDefizit)!);
		if (fg2_defizite > 0)
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> FG2: Defizit" + (fg2_defizite > 1 ? "e" : "") + ": " + faecher.fg2.getKuerzelListe(this.filterDefizit)!);
		if ((fg1_ungenuegend > 0) || (fg2_ungenuegend > 1)) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu oft ungenügend (6) - 0x6 in FG1 und max. 1x6 in FG2 erlaubt.");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA10, false);
		}
		this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> " + ((fg2_ungenuegend === 1) ? "1x6 in FG2 erlaubt" : "0x6 in FG1 und FG2") + " -> prüfe weitere Defizite");
		if (fg1_mangelhaft > 2) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite: Mehr als 2x5 in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA10, false);
		}
		if ((fg1_mangelhaft === 2) && (fg2_defizite > 1)) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite: 2x5 in FG1 und mind. ein weiteres Defizit in FG2");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA10, false);
		}
		if (ges_defizite > 3) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite: Insgesamt mehr als 3 Defizite");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA10, false);
		}
		let hatNP : boolean = (fg1_mangelhaft === 2) || (ges_defizite === 3);
		if (hatNP) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite: " + ((fg1_mangelhaft === 2) ? "2x5 in FG1, aber kein weiteres Defizit in FG2" : "3 Defizite nicht erlaubt"));
			this.logger.logLn(LogLevel.INFO, " -> Hinweis: Nachprüfungen in ZP10-Fächern nicht möglich");
			let np_faecher : List<string> = (fg1_mangelhaft === 2) ? faecher.fg1.getKuerzel(this.filterMangelhaftOhneZP10Faecher) : faecher.getKuerzel(this.filterMangelhaftOhneZP10Faecher);
			let abschlussergebnis : AbschlussErgebnis = AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.HA10, np_faecher);
			this.logger.logLn(LogLevel.INFO, AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis) ? (" -> Nachprüfungsmöglichkeit(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis)!) : " -> also: kein Nachprüfungsmöglichkeit.");
			return abschlussergebnis;
		}
		if ((fg1_defizite === 0) && (fg2_defizite === 0)) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> keine Defizite in FG1 und FG2");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA10, true);
		}
		this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zwei Defizite erlaubt (solange nicht beide in FG1)");
		return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA10, true);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.Service', 'de.nrw.schule.svws.core.abschluss.ge.ServiceAbschlussHA10'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_ge_ServiceAbschlussHA10(obj : unknown) : ServiceAbschlussHA10 {
	return obj as ServiceAbschlussHA10;
}
