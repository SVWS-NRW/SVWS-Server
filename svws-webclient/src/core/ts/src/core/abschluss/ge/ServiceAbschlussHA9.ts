import { JavaObject } from '../../../java/lang/JavaObject';
import { GEAbschlussFach } from '../../../core/data/abschluss/GEAbschlussFach';
import { Service } from '../../../core/Service';
import { GELeistungsdifferenzierteKursart } from '../../../core/types/ge/GELeistungsdifferenzierteKursart';
import { LogLevel } from '../../../core/logger/LogLevel';
import { Predicate } from '../../../java/util/function/Predicate';
import { AbschlussFaecherGruppe } from '../../../core/abschluss/ge/AbschlussFaecherGruppe';
import { GEAbschlussFaecher } from '../../../core/data/abschluss/GEAbschlussFaecher';
import { AbschlussErgebnis } from '../../../core/data/abschluss/AbschlussErgebnis';
import { SchulabschlussAllgemeinbildend } from '../../../core/types/schule/SchulabschlussAllgemeinbildend';
import { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { AbschlussManager } from '../../../core/abschluss/AbschlussManager';
import { AbschlussFaecherGruppen } from '../../../core/abschluss/ge/AbschlussFaecherGruppen';

export class ServiceAbschlussHA9 extends Service<GEAbschlussFaecher, AbschlussErgebnis> {

	/**
	 * Filter für alle nicht ausgeglichenen Defizite
	 */
	private static readonly filterDefizit : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => f.note > 4 && (!f.ausgeglichen) };

	/**
	 * Filter für alle mangelhaften Fächer
	 */
	private static readonly filterMangelhaft : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => f.note === 5 };

	/**
	 * Filter für alle ungenügenden Fächer
	 */
	private static readonly filterUngenuegend : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => f.note === 6 };

	/**
	 * Filter für alle Fächer, welche als E-Kurs belegt wurden.
	 */
	private static readonly filterEKurse : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (GELeistungsdifferenzierteKursart.E.hat(f.kursart)) };

	/**
	 * Filter zur Bestimmung aller Fremdsprachen, die nicht als E-Kurs belegt wurden.
	 */
	private static readonly filterWeitereFremdsprachen : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (!JavaObject.equalsTranspiler("E", (f.kuerzel)) && (f.istFremdsprache !== null) && (f.istFremdsprache)) };


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
		if (JavaObject.equalsTranspiler("10", (input.jahrgang))) {
			this.logger.logLn(LogLevel.INFO, "Im Jahrgang 10 gibt es keinen HA9-Abschluss mehr.");
			return AbschlussManager.getErgebnis(null, false);
		}
		this.logger.logLn(LogLevel.INFO, "Prüfe HA9:");
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
		const faecher : AbschlussFaecherGruppen = new AbschlussFaecherGruppen(new AbschlussFaecherGruppe(input.faecher, Arrays.asList("D", "M"), null), new AbschlussFaecherGruppe(input.faecher, null, Arrays.asList("D", "M", "LBNW", "LBAL")));
		if (!faecher.fg1.istVollstaendig(Arrays.asList("D", "M"))) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht alle nötigen Leistungen für die Fächergruppe 1 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		if (faecher.fg2.isEmpty()) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Keine Leistungen für die Fächergruppe 2 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		const weitereFS : List<GEAbschlussFach> = faecher.fg2.entferneFaecher(ServiceAbschlussHA9.filterWeitereFremdsprachen);
		if (!weitereFS.isEmpty()) {
			for (const fs of weitereFS) {
				if (fs.bezeichnung === null)
					continue;
				this.logger.logLn(LogLevel.DEBUG, " -> Ignoriere weitere Fremdsprache: " + fs.bezeichnung + "(" + fs.note + ")");
			}
		}
		this.logger.logLn(LogLevel.DEBUG, " - ggf. Verbessern der E-Kurs-Noten für die Defizitberechnung:");
		const tmpFaecher : List<GEAbschlussFach> = faecher.getFaecher(ServiceAbschlussHA9.filterEKurse);
		for (const f of tmpFaecher) {
			if (f.kuerzel === null)
				continue;
			const note : number = f.note;
			const note_neu : number = (note === 1) ? 1 : note - 1;
			this.logger.logLn(LogLevel.DEBUG, "   " + f.kuerzel + "(E):" + note + "->" + note_neu);
			f.note = note_neu;
		}
		this.logger.logLn(LogLevel.DEBUG, " -> FG1: Fächer " + faecher.fg1.toString()!);
		this.logger.logLn(LogLevel.DEBUG, " -> FG2: Fächer " + faecher.fg2.toString()!);
		const abschlussergebnis : AbschlussErgebnis = this.pruefeDefizite(faecher, "");
		if (abschlussergebnis.erworben) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.INFO, " => HA 9: APO-SI §40 (3)");
		} else
			if (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)) {
				this.logger.logLn(LogLevel.INFO, " => kein HA9 - Nachprüfungsmöglichkeite(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis)!);
			} else {
				this.logger.logLn(LogLevel.INFO, " => kein HA9 - KEINE Nachprüfungsmöglichkeiten!");
			}
		return abschlussergebnis;
	}

	/**
	 * Prüft in Bezug auf Defizite, ob der Abschluss erworben wurde.
	 *
	 * @param faecher      die Abschlussfächer nach Fächergruppen sortiert
	 * @param logIndent    die Einrückung für das Logging
	 *
	 * @return das Ergebnis der Abschlussberechnung in Bezug die Defizitberechnung
	 */
	private pruefeDefizite(faecher : AbschlussFaecherGruppen, logIndent : string) : AbschlussErgebnis {
		const fg1_defizite : number = faecher.fg1.getFaecherAnzahl(ServiceAbschlussHA9.filterDefizit);
		const fg2_defizite : number = faecher.fg2.getFaecherAnzahl(ServiceAbschlussHA9.filterDefizit);
		const ges_defizite : number = fg1_defizite + fg2_defizite;
		const fg1_mangelhaft : number = faecher.fg1.getFaecherAnzahl(ServiceAbschlussHA9.filterMangelhaft);
		const fg1_ungenuegend : number = faecher.fg1.getFaecherAnzahl(ServiceAbschlussHA9.filterUngenuegend);
		const fg2_ungenuegend : number = faecher.fg2.getFaecherAnzahl(ServiceAbschlussHA9.filterUngenuegend);
		if (fg1_defizite > 0)
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> FG1: Defizit" + (fg1_defizite > 1 ? "e" : "") + ": " + faecher.fg1.getKuerzelListe(ServiceAbschlussHA9.filterDefizit)!);
		if (fg2_defizite > 0)
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> FG2: Defizit" + (fg2_defizite > 1 ? "e" : "") + ": " + faecher.fg2.getKuerzelListe(ServiceAbschlussHA9.filterDefizit)!);
		if ((fg1_ungenuegend > 0) || (fg2_ungenuegend > 1)) {
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> zu oft ungenügend (6) - 0x6 in FG1 und max. 1x6 in FG2 erlaubt.");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, false);
		}
		this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> " + ((fg2_ungenuegend === 1) ? "1x6 in FG2 erlaubt" : "0x6 in FG1 und FG2") + " -> prüfe weitere Defizite");
		if (fg1_mangelhaft > 2) {
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> zu viele Defizite: Mehr als 2x5 in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, false);
		}
		if ((fg1_mangelhaft === 2) && (fg2_defizite > 1)) {
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> zu viele Defizite: 2x5 in FG1 und mind. ein weiteres Defizit in FG2");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, false);
		}
		if (ges_defizite > 3) {
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> zu viele Defizite: Insgesamt mehr als 3 Defizite");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, false);
		}
		const hatNP : boolean = (fg1_mangelhaft === 2) || (ges_defizite === 3);
		if (hatNP) {
			const np_faecher : List<string> = (fg1_mangelhaft === 2) ? faecher.fg1.getKuerzel(ServiceAbschlussHA9.filterMangelhaft) : faecher.getKuerzel(ServiceAbschlussHA9.filterMangelhaft);
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> zu viele Defizite: " + ((fg1_mangelhaft === 2) ? "2x5 in FG1, aber kein weiteres Defizit in FG2" : "3 Defizite nicht erlaubt"));
			const abschlussergebnis : AbschlussErgebnis = AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.HA9, np_faecher);
			this.logger.logLn(LogLevel.INFO, " -> Nachprüfungsmöglichkeit(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis)!);
			return abschlussergebnis;
		}
		if ((fg1_defizite === 0) && (fg2_defizite === 0)) {
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> keine Defizite in FG1 und FG2");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, true);
		}
		this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> zwei Defizite erlaubt (solange nicht beide in FG1)");
		return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, true);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.ge.ServiceAbschlussHA9', 'de.svws_nrw.core.Service'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_ge_ServiceAbschlussHA9(obj : unknown) : ServiceAbschlussHA9 {
	return obj as ServiceAbschlussHA9;
}
