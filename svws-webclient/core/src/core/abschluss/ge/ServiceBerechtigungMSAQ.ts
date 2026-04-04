import { GEAbschlussFach } from '../../../core/data/abschluss/GEAbschlussFach';
import { ArrayList } from '../../../java/util/ArrayList';
import { Service } from '../../../core/Service';
import { JavaString } from '../../../java/lang/JavaString';
import { GELeistungsdifferenzierteKursart } from '../../../core/types/ge/GELeistungsdifferenzierteKursart';
import { LogLevel } from '../../../core/logger/LogLevel';
import type { Predicate } from '../../../java/util/function/Predicate';
import { AbschlussErgebnis } from '../../../core/data/abschluss/AbschlussErgebnis';
import { GEAbschlussFaecher } from '../../../core/data/abschluss/GEAbschlussFaecher';
import { SchulabschlussAllgemeinbildend } from '../../../asd/types/schule/SchulabschlussAllgemeinbildend';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ServiceAbschlussMSA } from '../../../core/abschluss/ge/ServiceAbschlussMSA';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import { AbschlussManager } from '../../../core/abschluss/AbschlussManager';
import { AbschlussFaecherGruppen } from '../../../core/abschluss/ge/AbschlussFaecherGruppen';

export class ServiceBerechtigungMSAQ extends Service {

	private static readonly filterDefizite: Predicate<GEAbschlussFach> = { test: (f: GEAbschlussFach) => (f.note > 3) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 2)) };

	private static readonly filterDefizite1NS: Predicate<GEAbschlussFach> = { test: (f: GEAbschlussFach) => ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 4)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 3)) };

	private static readonly filterDefizite2NS: Predicate<GEAbschlussFach> = { test: (f: GEAbschlussFach) => ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 5)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 4)) };

	private static readonly filterDefizitWP: Predicate<GEAbschlussFach> = { test: (f: GEAbschlussFach) => (f.note > 3) && JavaString.equalsIgnoreCase("WP", f.kuerzel) };

	private static readonly filterDefizitNichtWP: Predicate<GEAbschlussFach> = { test: (f: GEAbschlussFach) => (f.note > 3) || (((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 2)) && !JavaString.equalsIgnoreCase("WP", f.kuerzel)) };

	private static readonly filterFG1NichtAusgleichbar: Predicate<GEAbschlussFach> = { test: (f: GEAbschlussFach) => (f.note > 4) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3)) };

	private static readonly filterFG2NichtAusgleichbar: Predicate<GEAbschlussFach> = { test: (f: GEAbschlussFach) => (f.note > 5) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3)) };

	private static readonly filterAusgleiche: Predicate<GEAbschlussFach> = { test: (f: GEAbschlussFach) => !f.ausgleich && ((f.note < 2) || ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note < 3))) };

	private static readonly filterEKurse: Predicate<GEAbschlussFach> = { test: (f: GEAbschlussFach) => (GELeistungsdifferenzierteKursart.E.hat(f.kursart)) };

	/**
	 * Die Zeichenkette, welche zum Trennen von Teilen des Logs verwendet wird.
	 */
	private static readonly LOG_SEPERATOR: string = "______________________________";


	/**
	 * Leerer Standardkonstruktor.
	 */
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
	public berechne(input: GEAbschlussFaecher): AbschlussErgebnis {
		this.logger.logLn(LogLevel.INFO, "Prüfe MSA-Q:");
		this.logger.logLn(LogLevel.DEBUG, "============");
		if ((input.faecher === null) || (!AbschlussManager.pruefeHat4LeistungsdifferenzierteFaecher(input))) {
			this.logger.logLn(LogLevel.DEBUG, ServiceBerechtigungMSAQ.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht genügend leistungsdifferenzierte Fächer gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		if (!AbschlussManager.pruefeKuerzelDuplikate(input)) {
			this.logger.logLn(LogLevel.DEBUG, ServiceBerechtigungMSAQ.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden Fächer mit dem gleichen Kürzel zur Abschlussprüfung übergeben. Dies ist nicht zulässig.");
			return AbschlussManager.getErgebnis(null, false);
		}
		const faecher: AbschlussFaecherGruppen = ServiceAbschlussMSA.getFaechergruppen(input.faecher);
		if (!faecher.fg1.istVollstaendig(Arrays.asList("D", "M", "E", "WP"))) {
			this.logger.logLn(LogLevel.DEBUG, ServiceBerechtigungMSAQ.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht alle nötigen Leistungen für die Fächergruppe 1 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		if (faecher.fg2.isEmpty()) {
			this.logger.logLn(LogLevel.DEBUG, ServiceBerechtigungMSAQ.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Keine Leistungen für die Fächergruppe 2 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		const anzahlEKurse: number = faecher.getFaecherAnzahl(ServiceBerechtigungMSAQ.filterEKurse);
		if (anzahlEKurse < 3) {
			this.logger.logLn(LogLevel.DEBUG, ServiceBerechtigungMSAQ.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.INFO, " => kein MSA-Q (FOR-Q) - nicht genügend E-Kurse belegt");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		} else
			if (anzahlEKurse > 3) {
				this.logger.logLn(LogLevel.DEBUG, " - Verbessern der E-Kurs-Noten für die Defizitberechnung, falls mehr als 3 E-Kurse vorhanden sind:");
				const tmpFaecher: List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceBerechtigungMSAQ.filterEKurse);
				for (const f of tmpFaecher) {
					const note: number = f.note;
					const note_neu: number = (note === 1) ? 1 : (note - 1);
					this.logger.logLn(LogLevel.DEBUG, "   " + f.kuerzel + ":(E)" + note + "->(G)" + note_neu);
					f.note = note_neu;
					f.kursart = GELeistungsdifferenzierteKursart.G.kuerzel;
				}
			}
		this.logger.logLn(LogLevel.DEBUG, " -> FG1: Fächer " + faecher.fg1.toString());
		this.logger.logLn(LogLevel.DEBUG, " -> FG2: Fächer " + faecher.fg2.toString());
		const abschlussergebnis: AbschlussErgebnis = this.pruefeDefizite(faecher, "");
		if (abschlussergebnis.erworben) {
			this.logger.logLn(LogLevel.DEBUG, ServiceBerechtigungMSAQ.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.INFO, " => MSA-Q (FOR-Q): APO-SI §43 (4)");
		} else
			if (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)) {
				this.logger.logLn(LogLevel.INFO, " => kein MSA-Q (FOR-Q) - Nachprüfungsmöglichkeite(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis));
			} else {
				this.logger.logLn(LogLevel.INFO, " => kein MSA-Q (FOR-Q) - KEINE Nachprüfungsmöglichkeiten!");
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
	private pruefeDefizite(faecher: AbschlussFaecherGruppen, logIndent: string): AbschlussErgebnis {
		const fg1Defizite: List<GEAbschlussFach> = faecher.fg1.getFaecher(ServiceBerechtigungMSAQ.filterDefizite);
		const fg2Defizite: List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceBerechtigungMSAQ.filterDefizite);
		if (!fg1Defizite.isEmpty()) {
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> FG1: Defizit" + (fg1Defizite.size() > 1 ? "e" : "") + ": " + faecher.fg1.getKuerzelListe(ServiceBerechtigungMSAQ.filterDefizite));
		}
		if (!fg2Defizite.isEmpty()) {
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> FG2: Defizit" + (fg2Defizite.size() > 1 ? "e" : "") + ": " + faecher.fg2.getKuerzelListe(ServiceBerechtigungMSAQ.filterDefizite));
		}
		let nachpruefungGenutzt: boolean = false;
		const npFaecher: List<GEAbschlussFach> = new ArrayList<GEAbschlussFach>();
		const fg1NichtAusgleichbar: List<GEAbschlussFach> = faecher.fg1.getFaecher(ServiceBerechtigungMSAQ.filterFG1NichtAusgleichbar);
		const fg2NichtAusgleichbar: List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceBerechtigungMSAQ.filterFG2NichtAusgleichbar);
		if ((!fg1NichtAusgleichbar.isEmpty()) || (!fg2NichtAusgleichbar.isEmpty())) {
			const str_faecher: string = faecher.getKuerzelListe(ServiceBerechtigungMSAQ.filterFG1NichtAusgleichbar, ServiceBerechtigungMSAQ.filterFG2NichtAusgleichbar);
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> Defizit(e) in " + str_faecher + " aufgrund zu hoher Abweichungen nicht ausgleichbar.");
			if ((fg1NichtAusgleichbar.isEmpty()) && (fg2NichtAusgleichbar.size() === 1) && (GELeistungsdifferenzierteKursart.G.hat(fg2NichtAusgleichbar.get(0).kursart)) && (fg2NichtAusgleichbar.get(0).note === 4)) {
				this.logger.logLn(LogLevel.DEBUG, logIndent + "   -> Nachprüfung muss falls möglich in " + fg2NichtAusgleichbar.get(0).kuerzel + " stattfinden!");
				nachpruefungGenutzt = true;
				npFaecher.add(fg2NichtAusgleichbar.get(0));
			} else {
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
			}
		}
		const fg1Ausgleichsfaecher: List<GEAbschlussFach> = faecher.fg1.getFaecher(ServiceBerechtigungMSAQ.filterAusgleiche);
		const wpDefizit: GEAbschlussFach | null = faecher.fg1.getFach(ServiceBerechtigungMSAQ.filterDefizitWP);
		if ((fg1Defizite.size() > 2) || ((fg1Defizite.size() === 2) && (wpDefizit === null))) {
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		} else
			if ((fg1Defizite.size() === 2) && (wpDefizit !== null) && (fg1Ausgleichsfaecher.isEmpty())) {
				this.logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite in FG1 - kein Ausgleich möglich");
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
			} else
				if ((fg1Defizite.size() === 2) && (wpDefizit !== null) && (!fg1Ausgleichsfaecher.isEmpty()) && (!nachpruefungGenutzt)) {
					const defizitFach: GEAbschlussFach | null = faecher.fg1.getFach(ServiceBerechtigungMSAQ.filterDefizitNichtWP);
					if (defizitFach === null) {
						throw new NullPointerException()
					}
					const ausgleichsFach: GEAbschlussFach = fg1Ausgleichsfaecher.get(0);
					defizitFach.ausgeglichen = true;
					ausgleichsFach.ausgleich = true;
					this.logger.logLn(LogLevel.DEBUG, JavaString.format("%s -> Ausgleich von %s durch %s.", logIndent, defizitFach.kuerzel, ausgleichsFach.kuerzel));
					nachpruefungGenutzt = true;
					npFaecher.add(wpDefizit);
					const abschlussergebnis: AbschlussErgebnis = this.pruefeFG2(faecher, logIndent, npFaecher, nachpruefungGenutzt);
					if (abschlussergebnis.erworben) {
						return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
					}
					return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
				}
		if ((fg1Defizite.size() === 1) && (wpDefizit === null) && (fg1Ausgleichsfaecher.isEmpty())) {
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> kein Defizit-Ausgleich in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}
		if ((fg1Defizite.size() === 1) && (wpDefizit === null)) {
			const defizitFach: GEAbschlussFach | null = faecher.fg1.getFach(ServiceBerechtigungMSAQ.filterDefizitNichtWP);
			if (defizitFach === null) {
				throw new NullPointerException()
			}
			const ausgleichsFach: GEAbschlussFach = fg1Ausgleichsfaecher.get(0);
			defizitFach.ausgeglichen = true;
			ausgleichsFach.ausgleich = true;
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
		}
		if ((fg1Defizite.size() === 1) && (wpDefizit !== null)) {
			if (!fg1Ausgleichsfaecher.isEmpty()) {
				const defizitFach: GEAbschlussFach = wpDefizit;
				const ausgleichsFach: GEAbschlussFach = fg1Ausgleichsfaecher.get(0);
				defizitFach.ausgeglichen = true;
				ausgleichsFach.ausgleich = true;
				this.logger.logLn(LogLevel.DEBUG, logIndent + " -> Prüfe FG2 mit der Option Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
				const abschlussergebnis: AbschlussErgebnis = this.pruefeFG2(faecher, logIndent + "  ", npFaecher, nachpruefungGenutzt);
				if (abschlussergebnis.erworben) {
					return abschlussergebnis;
				}
				defizitFach.ausgeglichen = false;
				ausgleichsFach.ausgleich = false;
			}
			if (nachpruefungGenutzt) {
				this.logger.logLn(LogLevel.DEBUG, logIndent + " -> Eine Nachprüfung im WP-Fach und in dem leistungsdifferenzierten Fach der FG2 ist nicht gleichzeitig möglich.");
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
			}
			wpDefizit.ausgleich = true;
			wpDefizit.note--;
			const abschlussergebnis: AbschlussErgebnis = this.pruefeFG2(faecher, logIndent, npFaecher, true);
			wpDefizit.note++;
			wpDefizit.ausgleich = false;
			if (abschlussergebnis.erworben) {
				npFaecher.add(wpDefizit);
			}
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
		}
		let logFg2Indent: string = logIndent;
		if (fg2NichtAusgleichbar.size() === 1) {
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> Prüfe FG2 mit Nachprüfung in " + fg2NichtAusgleichbar.get(0).kuerzel);
			logFg2Indent += "  ";
		}
		const abschlussergebnis: AbschlussErgebnis = this.pruefeFG2(faecher, logFg2Indent, npFaecher, nachpruefungGenutzt);
		if (((fg2NichtAusgleichbar.size() === 1) && abschlussergebnis.erworben) || ((!abschlussergebnis.erworben) && (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)))) {
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
		}
		return abschlussergebnis;
	}

	/**
	 * Führt eine Detailprüfung in der Fächergruppe 2 durch. Diese Methode wird ggf. mehrfach - auch rekursiv - aufgerufen.
	 *
	 * @param faecher               die Abschlussfächer nach Fächergruppen sortiert
	 * @param logIndent             die Einrückung für das Logging
	 * @param npFaecher             die Liste der Nachprüfungsfächer, die bisher schon feststehen
	 * @param nachpruefungGenutzt   gibt an, ob die Nachprüfungsmöglichkeit bereits eingesetzt werden musste
	 *
	 * @return das Ergebnis der Abschlussberechnung in Bezug auf den Stand dieser Detailprüfung
	 */
	private pruefeFG2(faecher: AbschlussFaecherGruppen, logIndent: string, npFaecher: List<GEAbschlussFach>, nachpruefungGenutzt: boolean): AbschlussErgebnis {
		const gesAusgleichsfaecher: List<GEAbschlussFach> = faecher.getFaecher(ServiceBerechtigungMSAQ.filterAusgleiche);
		const fg2Defizite1NS: List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceBerechtigungMSAQ.filterDefizite1NS);
		const fg2Defizite2NS: List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceBerechtigungMSAQ.filterDefizite2NS);
		const fg2_defizit_anzahl: number = fg2Defizite1NS.size() + fg2Defizite2NS.size();
		if (fg2_defizit_anzahl === 0) {
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> keine Defizite in FG2");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, true);
		}
		if ((fg2Defizite2NS.size() > 2) || (fg2_defizit_anzahl > (nachpruefungGenutzt ? 3 : 4))) {
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite in FG2 - mit Ausgleich und Nachprüfung kein Abschluss möglich");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}
		if (gesAusgleichsfaecher.size() < (fg2_defizit_anzahl - (nachpruefungGenutzt ? 0 : 1))) {
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite in FG2 - nicht genügend Ausgleichsfächer vorhanden");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}
		if (fg2Defizite2NS.size() === 2) {
			for (const defizitFach of fg2Defizite2NS) {
				defizitFach.ausgeglichen = true;
				defizitFach.ausgleich = true;
				defizitFach.note--;
				this.logger.logLn(LogLevel.DEBUG, logIndent + " -> Prüfe erneut mit Nachprüfung in " + defizitFach.kuerzel);
				const abschlussergebnis: AbschlussErgebnis = this.pruefeFG2(faecher, logIndent + "  ", npFaecher, true);
				this.logger.logLn(LogLevel.DEBUG, logIndent + "   -> Nachprüfung in " + defizitFach.kuerzel + (abschlussergebnis.erworben ? " möglich" : " nicht möglich"));
				if (abschlussergebnis.erworben) {
					npFaecher.add(defizitFach);
				}
				defizitFach.ausgeglichen = true;
				defizitFach.ausgleich = true;
				defizitFach.note++;
			}
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
		}
		if (gesAusgleichsfaecher.size() >= fg2_defizit_anzahl) {
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> genug Ausgleichsfächer vorhanden." + (nachpruefungGenutzt ? "" : " Nachprüfung nicht nötig."));
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, true);
		}
		for (const defizitFach of fg2Defizite1NS) {
			defizitFach.ausgeglichen = true;
			defizitFach.ausgleich = true;
			defizitFach.note--;
			this.logger.logLn(LogLevel.DEBUG, logIndent + " -> Prüfe erneut mit Nachprüfung in " + defizitFach.kuerzel);
			const abschlussergebnis: AbschlussErgebnis = this.pruefeFG2(faecher, logIndent + "  ", npFaecher, true);
			this.logger.logLn(LogLevel.DEBUG, logIndent + "   -> Nachprüfung in " + defizitFach.kuerzel + (abschlussergebnis.erworben ? " möglich" : " nicht möglich"));
			if (abschlussergebnis.erworben) {
				npFaecher.add(defizitFach);
			}
			defizitFach.ausgeglichen = true;
			defizitFach.ausgleich = true;
			defizitFach.note++;
		}
		return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.ge.ServiceBerechtigungMSAQ';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.Service', 'de.svws_nrw.core.abschluss.ge.ServiceBerechtigungMSAQ'].includes(name);
	}

	public static readonly class = new Class<ServiceBerechtigungMSAQ>('de.svws_nrw.core.abschluss.ge.ServiceBerechtigungMSAQ');

}

export function cast_de_svws_nrw_core_abschluss_ge_ServiceBerechtigungMSAQ(obj: unknown): ServiceBerechtigungMSAQ {
	return obj as ServiceBerechtigungMSAQ;
}
