import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GEAbschlussFach, cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFach } from '../../data/abschluss/GEAbschlussFach';
import { Service, cast_de_nrw_schule_svws_core_Service } from '../../Service';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GELeistungsdifferenzierteKursart, cast_de_nrw_schule_svws_core_types_ge_GELeistungsdifferenzierteKursart } from '../../types/ge/GELeistungsdifferenzierteKursart';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../logger/LogLevel';
import { Predicate, cast_java_util_function_Predicate } from '../../../java/util/function/Predicate';
import { GEAbschlussFaecher, cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFaecher } from '../../data/abschluss/GEAbschlussFaecher';
import { AbschlussErgebnis, cast_de_nrw_schule_svws_core_data_abschluss_AbschlussErgebnis } from '../../data/abschluss/AbschlussErgebnis';
import { SchulabschlussAllgemeinbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussAllgemeinbildend } from '../../types/schule/SchulabschlussAllgemeinbildend';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { ServiceAbschlussMSA, cast_de_nrw_schule_svws_core_abschluss_ge_ServiceAbschlussMSA } from './ServiceAbschlussMSA';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { AbschlussManager, cast_de_nrw_schule_svws_core_abschluss_AbschlussManager } from '../AbschlussManager';
import { AbschlussFaecherGruppen, cast_de_nrw_schule_svws_core_abschluss_ge_AbschlussFaecherGruppen } from './AbschlussFaecherGruppen';

export class ServiceBerechtigungMSAQ extends Service<GEAbschlussFaecher, AbschlussErgebnis> {

	private filterDefizite : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (f.note > 3) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 2)) };

	private filterDefizite1NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 4)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 3)) };

	private filterDefizite2NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 5)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 4)) };

	private filterDefizitWP : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (f.note > 3) && JavaString.equalsIgnoreCase("WP", f.kuerzel) };

	private filterDefizitNichtWP : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (f.note > 3) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 2)) && !JavaString.equalsIgnoreCase("WP", f.kuerzel) };

	private filterFG1NichtAusgleichbar : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (f.note > 4) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3)) };

	private filterFG2NichtAusgleichbar : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (f.note > 5) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3)) };

	private filterAusgleiche : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgleich && ((f.note < 2) || ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note < 3))) };

	private filterEKurse : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (GELeistungsdifferenzierteKursart.E.hat(f.kursart)) };


	public constructor() {
		super();
	}

	/**
	 * Führt die Abschlussberechnung (bzw. Berechtigungsberechnung) anhand der übergebenen 
	 * Abschlussfächer durch und gibt das Berechnungsergebnis zurück.
	 * 
	 * @param input    die Abschlussfächer
	 * 
	 * @return das Ergebnis der Abschlussberechnung
	 */
	public handle(input : GEAbschlussFaecher) : AbschlussErgebnis {
		this.logger.logLn(LogLevel.INFO, "Prüfe MSA-Q:");
		this.logger.logLn(LogLevel.DEBUG, "============");
		if ((input.faecher === null) || (!AbschlussManager.pruefeHat4LeistungsdifferenzierteFaecher(input))) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht genügend leistungsdifferenzierte Fächer gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		if (!AbschlussManager.pruefeKuerzelDuplikate(input)) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden Fächer mit dem gleichen Kürzel zur Abschlussprüfung übergeben. Dies ist nicht zulässig.");
			return AbschlussManager.getErgebnis(null, false);
		}
		let faecher : AbschlussFaecherGruppen = ServiceAbschlussMSA.getFaechergruppen(input.faecher);
		if (!faecher.fg1.istVollstaendig(Arrays.asList("D", "M", "E", "WP"))) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht alle nötigen Leistungen für die Fächergruppe 1 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		if (faecher.fg2.isEmpty()) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Keine Leistungen für die Fächergruppe 2 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		let anzahlEKurse : number = faecher.getFaecherAnzahl(this.filterEKurse);
		if (anzahlEKurse < 3) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.INFO, " => kein MSA-Q (FOR-Q) - nicht genügend E-Kurse belegt");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		} else 
			if (anzahlEKurse > 3) {
				this.logger.logLn(LogLevel.DEBUG, " - Verbessern der E-Kurs-Noten für die Defizitberechnung, falls mehr als 3 E-Kurse vorhanden sind:");
				let tmpFaecher : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterEKurse);
				for (let f of tmpFaecher) {
					let note : number = f.note;
					let note_neu : number = (note === 1) ? 1 : note - 1;
					this.logger.logLn(LogLevel.DEBUG, "   " + f.kuerzel + ":(E)" + note + "->(G)" + note_neu);
					f.note = note_neu;
					f.kursart = GELeistungsdifferenzierteKursart.G.kuerzel;
				}
			}
		this.logger.logLn(LogLevel.DEBUG, " -> FG1: Fächer " + faecher.fg1.toString()!);
		this.logger.logLn(LogLevel.DEBUG, " -> FG2: Fächer " + faecher.fg2.toString()!);
		let abschlussergebnis : AbschlussErgebnis = this.pruefeDefizite(faecher, "");
		if (abschlussergebnis.erworben) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.INFO, " => MSA-Q (FOR-Q): APO-SI §43 (4)");
		} else 
			if (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)) {
				this.logger.logLn(LogLevel.INFO, " => kein MSA-Q (FOR-Q) - Nachprüfungsmöglichkeite(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis)!);
			} else {
				this.logger.logLn(LogLevel.INFO, " => kein MSA-Q (FOR-Q) - KEINE Nachprüfungsmöglichkeiten!");
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
		let fg1_defizite : List<GEAbschlussFach> = faecher.fg1.getFaecher(this.filterDefizite);
		let fg2_defizite : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterDefizite);
		if (fg1_defizite.size() > 0) 
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> FG1: Defizit" + (fg1_defizite.size() > 1 ? "e" : "") + ": " + faecher.fg1.getKuerzelListe(this.filterDefizite)!);
		if (fg2_defizite.size() > 0) 
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> FG2: Defizit" + (fg2_defizite.size() > 1 ? "e" : "") + ": " + faecher.fg2.getKuerzelListe(this.filterDefizite)!);
		let nachpruefung_genutzt : boolean = false;
		let npFaecher : List<GEAbschlussFach> = new Vector();
		let fg1_nicht_ausgleichbar : List<GEAbschlussFach> = faecher.fg1.getFaecher(this.filterFG1NichtAusgleichbar);
		let fg2_nicht_ausgleichbar : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterFG2NichtAusgleichbar);
		if ((fg1_nicht_ausgleichbar.size() > 0) || (fg2_nicht_ausgleichbar.size() > 0)) {
			let str_faecher : string = faecher.getKuerzelListe(this.filterFG1NichtAusgleichbar, this.filterFG2NichtAusgleichbar);
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Defizit(e) in " + str_faecher! + " aufgrund zu hoher Abweichungen nicht ausgleichbar.");
			if ((fg1_nicht_ausgleichbar.size() === 0) && (fg2_nicht_ausgleichbar.size() === 1) && (GELeistungsdifferenzierteKursart.G.hat(fg2_nicht_ausgleichbar.get(0).kursart)) && (fg2_nicht_ausgleichbar.get(0).note === 4)) {
				this.logger.logLn(LogLevel.DEBUG, log_indent! + "   -> Nachprüfung muss falls möglich in " + fg2_nicht_ausgleichbar.get(0).kuerzel + " stattfinden!");
				nachpruefung_genutzt = true;
				npFaecher.add(fg2_nicht_ausgleichbar.get(0));
			} else {
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
			}
		}
		let fg1_ausgleichsfaecher : List<GEAbschlussFach> = faecher.fg1.getFaecher(this.filterAusgleiche);
		let wp_defizit : GEAbschlussFach | null = faecher.fg1.getFach(this.filterDefizitWP);
		if ((fg1_defizite.size() > 2) || ((fg1_defizite.size() === 2) && (wp_defizit === null))) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		} else 
			if ((fg1_defizite.size() === 2) && (wp_defizit !== null) && (fg1_ausgleichsfaecher.size() === 0)) {
				this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite in FG1 - kein Ausgleich möglich");
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
			} else 
				if ((fg1_defizite.size() === 2) && (wp_defizit !== null) && (fg1_ausgleichsfaecher.size() > 0) && (!nachpruefung_genutzt)) {
					let defizitFach : GEAbschlussFach | null = faecher.fg1.getFach(this.filterDefizitNichtWP);
					if (defizitFach === null) 
						throw new NullPointerException()
					let ausgleichsFach : GEAbschlussFach = fg1_ausgleichsfaecher.get(0);
					defizitFach.ausgeglichen = true;
					ausgleichsFach.ausgleich = true;
					this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
					nachpruefung_genutzt = true;
					npFaecher.add(wp_defizit);
					let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent, npFaecher, nachpruefung_genutzt);
					if (abschlussergebnis.erworben) {
						return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
					}
					return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
				}
		if ((fg1_defizite.size() === 1) && (wp_defizit === null) && (fg1_ausgleichsfaecher.size() === 0)) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> kein Defizit-Ausgleich in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}
		if ((fg1_defizite.size() === 1) && (wp_defizit === null)) {
			let defizitFach : GEAbschlussFach | null = faecher.fg1.getFach(this.filterDefizitNichtWP);
			if (defizitFach === null) 
				throw new NullPointerException()
			let ausgleichsFach : GEAbschlussFach = fg1_ausgleichsfaecher.get(0);
			defizitFach.ausgeglichen = true;
			ausgleichsFach.ausgleich = true;
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
		}
		if ((fg1_defizite.size() === 1) && (wp_defizit !== null)) {
			if ((fg1_ausgleichsfaecher.size() > 0)) {
				let defizitFach : GEAbschlussFach = wp_defizit;
				let ausgleichsFach : GEAbschlussFach = fg1_ausgleichsfaecher.get(0);
				defizitFach.ausgeglichen = true;
				ausgleichsFach.ausgleich = true;
				this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe FG2 mit der Option Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
				let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent! + "  ", npFaecher, nachpruefung_genutzt);
				if (abschlussergebnis.erworben) 
					return abschlussergebnis;
				defizitFach.ausgeglichen = false;
				ausgleichsFach.ausgleich = false;
			}
			if (nachpruefung_genutzt) {
				this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Eine Nachprüfung im WP-Fach und in dem leistungsdifferenzierten Fach der FG2 ist nicht gleichzeitig möglich.");
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
			}
			wp_defizit.ausgleich = true;
			wp_defizit.note--;
			let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent, npFaecher, true);
			wp_defizit.note++;
			wp_defizit.ausgleich = false;
			if (abschlussergebnis.erworben) {
				nachpruefung_genutzt = true;
				npFaecher.add(wp_defizit);
			}
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
		}
		let log_fg2_indent : string = log_indent;
		if (fg2_nicht_ausgleichbar.size() === 1) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe FG2 mit Nachprüfung in " + fg2_nicht_ausgleichbar.get(0).kuerzel);
			log_fg2_indent += "  ";
		}
		let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_fg2_indent, npFaecher, nachpruefung_genutzt);
		if (((fg2_nicht_ausgleichbar.size() === 1) && abschlussergebnis.erworben) || ((!abschlussergebnis.erworben) && (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)))) {
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
		}
		return abschlussergebnis;
	}

	/**
	 * Führt eine Detailprüfung in der Fächergruppe 2 durch. Diese Methode wird ggf. mehrfach - auch rekursiv - aufgerufen. 
	 * 
	 * @param faecher                die Abschlussfächer nach Fächergruppen sortiert
	 * @param log_indent             die Einrückung für das Logging
	 * @param npFaecher              die Liste der Nachprüfungsfächer, die bisher schon feststehen
	 * @param nachpruefung_genutzt   gibt an, ob die Nachprüfungsmöglichkeit bereits eingesetzt werden musste
	 * 
	 * @return das Ergebnis der Abschlussberechnung in Bezug auf den Stand dieser Detailprüfung
	 */
	private pruefeFG2(faecher : AbschlussFaecherGruppen, log_indent : string, npFaecher : List<GEAbschlussFach>, nachpruefung_genutzt : boolean) : AbschlussErgebnis {
		let ges_ausgleichsfaecher : List<GEAbschlussFach> = faecher.getFaecher(this.filterAusgleiche);
		let fg2_defizite_1NS : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterDefizite1NS);
		let fg2_defizite_2NS : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterDefizite2NS);
		let fg2_defizit_anzahl : number = fg2_defizite_1NS.size() + fg2_defizite_2NS.size();
		if (fg2_defizit_anzahl === 0) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> keine Defizite in FG2");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, true);
		}
		if ((fg2_defizite_2NS.size() > 2) || (fg2_defizit_anzahl > (nachpruefung_genutzt ? 3 : 4))) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite in FG2 - mit Ausgleich und Nachprüfung kein Abschluss möglich");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}
		if (ges_ausgleichsfaecher.size() < fg2_defizit_anzahl - (nachpruefung_genutzt ? 0 : 1)) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite in FG2 - nicht genügend Ausgleichsfächer vorhanden");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}
		if (fg2_defizite_2NS.size() === 2) {
			for (let defizitFach of fg2_defizite_2NS) {
				defizitFach.ausgeglichen = true;
				defizitFach.ausgleich = true;
				defizitFach.note--;
				this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe erneut mit Nachprüfung in " + defizitFach.kuerzel);
				let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent! + "  ", npFaecher, true);
				this.logger.logLn(LogLevel.DEBUG, log_indent! + "   -> Nachprüfung in " + defizitFach.kuerzel + (abschlussergebnis.erworben ? " möglich" : " nicht möglich"));
				if (abschlussergebnis.erworben) 
					npFaecher.add(defizitFach);
				defizitFach.ausgeglichen = true;
				defizitFach.ausgleich = true;
				defizitFach.note++;
			}
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
		}
		if (ges_ausgleichsfaecher.size() >= fg2_defizit_anzahl) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> genug Ausgleichsfächer vorhanden." + (nachpruefung_genutzt ? "" : " Nachprüfung nicht nötig."));
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, true);
		}
		for (let defizitFach of fg2_defizite_1NS) {
			defizitFach.ausgeglichen = true;
			defizitFach.ausgleich = true;
			defizitFach.note--;
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe erneut mit Nachprüfung in " + defizitFach.kuerzel);
			let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent! + "  ", npFaecher, true);
			this.logger.logLn(LogLevel.DEBUG, log_indent! + "   -> Nachprüfung in " + defizitFach.kuerzel + (abschlussergebnis.erworben ? " möglich" : " nicht möglich"));
			if (abschlussergebnis.erworben) 
				npFaecher.add(defizitFach);
			defizitFach.ausgeglichen = true;
			defizitFach.ausgleich = true;
			defizitFach.note++;
		}
		return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.Service', 'de.nrw.schule.svws.core.abschluss.ge.ServiceBerechtigungMSAQ'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_ge_ServiceBerechtigungMSAQ(obj : unknown) : ServiceBerechtigungMSAQ {
	return obj as ServiceBerechtigungMSAQ;
}
