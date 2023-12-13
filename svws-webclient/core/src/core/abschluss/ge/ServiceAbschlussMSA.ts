import { GEAbschlussFach } from '../../../core/data/abschluss/GEAbschlussFach';
import { ArrayList } from '../../../java/util/ArrayList';
import { Service } from '../../../core/Service';
import { JavaString } from '../../../java/lang/JavaString';
import { GELeistungsdifferenzierteKursart } from '../../../core/types/ge/GELeistungsdifferenzierteKursart';
import { LogLevel } from '../../../core/logger/LogLevel';
import type { Predicate } from '../../../java/util/function/Predicate';
import { AbschlussFaecherGruppe } from '../../../core/abschluss/ge/AbschlussFaecherGruppe';
import { GEAbschlussFaecher } from '../../../core/data/abschluss/GEAbschlussFaecher';
import { AbschlussErgebnis } from '../../../core/data/abschluss/AbschlussErgebnis';
import { SchulabschlussAllgemeinbildend } from '../../../core/types/schule/SchulabschlussAllgemeinbildend';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { AbschlussManager } from '../../../core/abschluss/AbschlussManager';
import { AbschlussFaecherGruppen } from '../../../core/abschluss/ge/AbschlussFaecherGruppen';

export class ServiceAbschlussMSA extends Service<GEAbschlussFaecher, AbschlussErgebnis> {

	private static readonly filterDefizite : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && ((f.note > 4) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3))) };

	private static readonly filterDefizite1NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 5)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 4))) };

	private static readonly filterDefizite2NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 6)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 5))) };

	private static readonly filterDefiziteMehrAls1NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 6)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note >= 5))) };

	private static readonly filterDefiziteMehrAls2NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 6)) };

	private static readonly filterDefiziteMitNPOption : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 5)) };

	private static readonly filterDefizitWP : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (f.note > 4) && JavaString.equalsIgnoreCase("WP", f.kuerzel) };

	private static readonly filterDefizitNichtWP : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (f.note > 4) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3)) && !JavaString.equalsIgnoreCase("WP", f.kuerzel) };

	private static readonly filterBenoetigte3er : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgleich && (f.note <= 3) && (GELeistungsdifferenzierteKursart.Sonstige.hat(f.kursart)) };

	private static readonly filterDefiziteBenoetigte3erMitNPOption : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgleich && (f.note === 4) && (GELeistungsdifferenzierteKursart.Sonstige.hat(f.kursart)) };

	private static readonly filterAusgleiche : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgleich && ((f.note < 3) || ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note < 4))) };

	private static readonly filterAusgleiche3er : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgleich && (f.note < 3) };

	private static readonly filterEKurse : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (GELeistungsdifferenzierteKursart.E.hat(f.kursart)) };

	/**
	 * Die Zeichenkette, welche zum Trennen von Teilen des Logs verwendet wird.
	 */
	private static readonly LOG_SEPERATOR : string = "______________________________";


	public constructor() {
		super();
	}

	/**
	 * Bestimmt anhand der übergebenen Fächer die Zuordnung zu den beiden Fächergruppen.
	 *
	 * @param input   die Abschlussfächer
	 *
	 * @return die Zuordnung der Abschlussfächer zu beiden Fachgruppen 1 und 2
	 */
	public static getFaechergruppen(input : List<GEAbschlussFach>) : AbschlussFaecherGruppen {
		const faecher : AbschlussFaecherGruppen = new AbschlussFaecherGruppen(new AbschlussFaecherGruppe(input, Arrays.asList("D", "M", "E", "WP"), null), new AbschlussFaecherGruppe(input, null, Arrays.asList("D", "M", "E", "WP", "LBNW", "LBAL")));
		return faecher;
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
		this.logger.logLn(LogLevel.INFO, "Prüfe MSA:");
		this.logger.logLn(LogLevel.DEBUG, "==========");
		if ((input.faecher === null) || (!AbschlussManager.pruefeHat4LeistungsdifferenzierteFaecher(input))) {
			this.logger.logLn(LogLevel.DEBUG, ServiceAbschlussMSA.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht genügend leistungsdiffernzierte Fächer gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		if (!AbschlussManager.pruefeKuerzelDuplikate(input)) {
			this.logger.logLn(LogLevel.DEBUG, ServiceAbschlussMSA.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden Fächer mit dem gleichen Kürzel zur Abschlussprüfung übergeben. Dies ist nicht zulässig.");
			return AbschlussManager.getErgebnis(null, false);
		}
		const faecher : AbschlussFaecherGruppen = ServiceAbschlussMSA.getFaechergruppen(input.faecher);
		if (!faecher.fg1.istVollstaendig(Arrays.asList("D", "M", "E", "WP"))) {
			this.logger.logLn(LogLevel.DEBUG, ServiceAbschlussMSA.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht alle nötigen Leistungen für die Fächergruppe 1 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		if (faecher.fg2.isEmpty()) {
			this.logger.logLn(LogLevel.DEBUG, ServiceAbschlussMSA.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Keine Leistungen für die Fächergruppe 2 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}
		const anzahlEKurse : number = faecher.getFaecherAnzahl(ServiceAbschlussMSA.filterEKurse);
		if (anzahlEKurse < 2) {
			this.logger.logLn(LogLevel.DEBUG, ServiceAbschlussMSA.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.INFO, " => kein MSA (FOR) - nicht genügend E-Kurse belegt");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		} else
			if (anzahlEKurse > 2) {
				let zuviel : number = anzahlEKurse - 2;
				const eKursFG2 : GEAbschlussFach | null = faecher.fg2.getFach(ServiceAbschlussMSA.filterEKurse);
				if (eKursFG2 !== null) {
					const note : number = eKursFG2.note;
					const note_neu : number = (note === 1) ? 1 : note - 1;
					this.logger.logLn(LogLevel.DEBUG, "   " + eKursFG2.kuerzel + ":(E)" + note + "->(G)" + note_neu);
					eKursFG2.note = note_neu;
					eKursFG2.kursart = GELeistungsdifferenzierteKursart.G.kuerzel;
					zuviel--;
				}
				while (zuviel > 0) {
					const eKursFG1 : GEAbschlussFach | null = faecher.fg1.getFach(ServiceAbschlussMSA.filterEKurse);
					if (eKursFG1 !== null) {
						const note : number = eKursFG1.note;
						const note_neu : number = (note === 1) ? 1 : note - 1;
						this.logger.logLn(LogLevel.DEBUG, "   " + eKursFG1.kuerzel + ":(E)" + note + "->(G)" + note_neu);
						eKursFG1.note = note_neu;
						eKursFG1.kursart = GELeistungsdifferenzierteKursart.G.kuerzel;
						zuviel--;
					}
					zuviel--;
				}
			}
		this.logger.logLn(LogLevel.DEBUG, " -> FG1: Fächer " + faecher.fg1.toString()!);
		this.logger.logLn(LogLevel.DEBUG, " -> FG2: Fächer " + faecher.fg2.toString()!);
		const abschlussergebnis : AbschlussErgebnis = this.pruefeDefizite(faecher, "");
		if (abschlussergebnis.erworben) {
			this.logger.logLn(LogLevel.DEBUG, ServiceAbschlussMSA.LOG_SEPERATOR);
			this.logger.logLn(LogLevel.INFO, " => MSA (FOR): APO-SI §42 (3)");
		} else
			if (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)) {
				this.logger.logLn(LogLevel.INFO, " => kein MSA (FOR) - Nachprüfungsmöglichkeite(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis)!);
			} else {
				this.logger.logLn(LogLevel.INFO, " => kein MSA (FOR) - KEINE Nachprüfungsmöglichkeiten!");
			}
		return abschlussergebnis;
	}

	/**
	 * Prüft in Bezug auf Defizite, ob der Abschluss erworben wurde.
	 *
	 * @param faecher      die Asbchlussfächer nach Fächergruppen sortiert
	 * @param logIndent    die Einrückung für das Logging
	 *
	 * @return das Ergebnis der Abschlussberechnung in Bezug die Defizitberechnung
	 */
	private pruefeDefizite(faecher : AbschlussFaecherGruppen, logIndent : string) : AbschlussErgebnis {
		let ignorieren_genutzt : boolean = false;
		let ausgleich_genutzt : boolean = false;
		let nachpruefung_genutzt : boolean = false;
		const npFaecher : List<GEAbschlussFach> = new ArrayList();
		const fg1_defizite : number = faecher.fg1.getFaecherAnzahl(ServiceAbschlussMSA.filterDefizite);
		const fg2_defizite : number = faecher.fg2.getFaecherAnzahl(ServiceAbschlussMSA.filterDefizite);
		const fg1_anzahlAusgleiche : number = faecher.fg1.getFaecherAnzahl(ServiceAbschlussMSA.filterAusgleiche);
		if (fg1_defizite > 0)
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> FG1: Defizit" + (fg1_defizite > 1 ? "e" : "") + ": " + faecher.fg1.getKuerzelListe(ServiceAbschlussMSA.filterDefizite)!);
		if (fg2_defizite > 0)
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> FG2: Defizit" + (fg2_defizite > 1 ? "e" : "") + ": " + faecher.fg2.getKuerzelListe(ServiceAbschlussMSA.filterDefizite)!);
		if (faecher.fg1.getFaecherAnzahl(ServiceAbschlussMSA.filterDefiziteMehrAls1NS) > 0) {
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> in FG1 unzulässig: mind. 1x6 oder bei einem G-Kurs 1x5");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		}
		if (faecher.fg2.getFaecherAnzahl(ServiceAbschlussMSA.filterDefiziteMehrAls2NS) > 0) {
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> in FG2 unzulässig: in einem G-Kurs 1x6");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		}
		const sonstige_ungenuegend : List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceAbschlussMSA.filterDefizite2NS);
		if (sonstige_ungenuegend.size() > 1) {
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> zu viele Defizite, kann nicht mehr als eine Note mit 6 (bzw. 5 bei einem G-Kurs) in FG2 unberücksichtigt lassen");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		} else
			if (sonstige_ungenuegend.size() === 1) {
				const defizitFach : GEAbschlussFach = sonstige_ungenuegend.get(0);
				if (GELeistungsdifferenzierteKursart.Sonstige.hat(defizitFach.kursart)) {
					defizitFach.ausgeglichen = true;
					this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> unberücksichtigt: Defizit in " + defizitFach.kuerzel + " (2 Notenstufen)");
					ignorieren_genutzt = true;
				} else
					if ((GELeistungsdifferenzierteKursart.E.hat(defizitFach.kursart)) && (defizitFach.note === 6)) {
						this.logger.logLn(LogLevel.DEBUG, logIndent! + "   -> Ein ungenügend in dem E-Kurs " + defizitFach.kuerzel + " kann nicht ausgelichen werden und eine Nachprüfung ist nicht zulässig!");
						return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
					} else {
						this.logger.logLn(LogLevel.DEBUG, logIndent! + "   -> Nachprüfung muss falls möglich in " + defizitFach.kuerzel + " stattfinden!");
						nachpruefung_genutzt = true;
						npFaecher.add(defizitFach);
						defizitFach.note--;
					}
			}
		const wp_defizit : GEAbschlussFach | null = faecher.fg1.getFach(ServiceAbschlussMSA.filterDefizitWP);
		if ((fg1_defizite > 2) || ((fg1_defizite === 2) && (wp_defizit === null)) || ((fg1_defizite === 2) && (fg1_anzahlAusgleiche === 0)) || ((fg1_defizite === 1) && (wp_defizit === null) && (fg1_anzahlAusgleiche === 0))) {
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> zu viele Defizite in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		}
		if ((fg1_defizite === 2) && (wp_defizit !== null)) {
			if (nachpruefung_genutzt) {
				this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> zu viele Defizite in FG1, eine Nachprüfung in mehreren Fächern (WP, " + sonstige_ungenuegend.get(0).kuerzel + ") ist nicht möglich!");
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
			}
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> WP-Defizite in FG1, eine Nachprüfung ist, sofern möglich, in WP nötig!");
			npFaecher.add(wp_defizit);
			nachpruefung_genutzt = true;
		}
		if ((fg1_defizite === 2) || ((fg1_defizite === 1) && (wp_defizit === null))) {
			ausgleich_genutzt = true;
			const defizitFach : GEAbschlussFach | null = faecher.fg1.getFach(ServiceAbschlussMSA.filterDefizitNichtWP);
			if (defizitFach === null)
				throw new NullPointerException()
			defizitFach.ausgeglichen = true;
			const ausgleichsFach : GEAbschlussFach | null = faecher.fg1.getFach(ServiceAbschlussMSA.filterAusgleiche);
			if (ausgleichsFach === null)
				throw new NullPointerException()
			ausgleichsFach.ausgleich = true;
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> Gleiche das Defizit (FG1) in " + defizitFach.kuerzel + " mit " + ausgleichsFach.kuerzel + " (FG1) aus.");
		}
		if (((fg1_defizite === 1) && (wp_defizit !== null))) {
			const defizitFach : GEAbschlussFach = wp_defizit;
			const ausgleichsFach : GEAbschlussFach | null = faecher.fg1.getFach(ServiceAbschlussMSA.filterAusgleiche);
			if (ausgleichsFach !== null) {
				ausgleich_genutzt = true;
				defizitFach.ausgeglichen = true;
				ausgleichsFach.ausgleich = true;
				this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> Prüfe mit Ausgleich: Gleiche das Defizit (FG1) in " + defizitFach.kuerzel + " mit " + ausgleichsFach.kuerzel + " (FG1) aus. " + defizitFach.kuerzel + " alternativ als Nachprüfungsfach denkbar.");
				const abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, logIndent! + "  ", npFaecher, 2, ignorieren_genutzt, ausgleich_genutzt, nachpruefung_genutzt);
				if (!abschlussergebnis.erworben && abschlussergebnis.npFaecher !== null && AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis) && wp_defizit.kuerzel !== null)
					abschlussergebnis.npFaecher.add(wp_defizit.kuerzel);
				return abschlussergebnis;
			}
			if ((sonstige_ungenuegend.size() === 1) && (!sonstige_ungenuegend.get(0).ausgeglichen)) {
				this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> das Defizit in WP kann nicht ausgeglichen werden und eine Nachprüfung in mehreren Fächern (WP, " + sonstige_ungenuegend.get(0).kuerzel + ") ist nicht möglich!");
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
			}
			this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> WP-Defizite in FG1 ohne Ausgleichsmöglichkeit, eine Nachprüfung ist, sofern möglich, in WP nötig!");
			npFaecher.add(wp_defizit);
			nachpruefung_genutzt = true;
		}
		const abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, logIndent, npFaecher, 2, ignorieren_genutzt, ausgleich_genutzt, nachpruefung_genutzt);
		if ((nachpruefung_genutzt) && abschlussergebnis.erworben)
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA, AbschlussManager.getKuerzel(npFaecher));
		return abschlussergebnis;
	}

	/**
	 * Führt eine Detailprüfung in der Fächergruppe 2 durch. Diese Methode wird ggf. mehrfach - auch rekursiv - aufgerufen.
	 *
	 * @param faecher               die Abschlussfächer nach Fächergruppen sortiert
	 * @param logIndent             die Einrückung für das Logging
	 * @param npFaecher             die Liste der Nachprüfungsfächer, die bisher schon feststehen
	 * @param benoetige3er          die Anzahl der 3er, die noch in FG2 benötigt werden
	 * @param ignorierenGenutzt     gibt an, ob die Möglichkeit eine defizitäre Leistung in FG2 zu ignorieren schon genutzt wurde
	 * @param ausgleichGenutzt      gibt an, ob die Möglichkeit des Ausgleichs über ein anderes Fach schon genutzt wurde
	 * @param nachpruefungGenutzt   gibt an, ob die Nachprüfungsmöglichkeit bereits eingesetzt werden musste
	 *
	 * @return das Ergebnis der Abschlussberechnung in Bezug auf den Stand dieser Detailprüfung
	 */
	private pruefeFG2(faecher : AbschlussFaecherGruppen, logIndent : string, npFaecher : List<GEAbschlussFach>, benoetige3er : number, ignorierenGenutzt : boolean, ausgleichGenutzt : boolean, nachpruefungGenutzt : boolean) : AbschlussErgebnis {
		const defizite : List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceAbschlussMSA.filterDefizite);
		const mangelhaft : List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceAbschlussMSA.filterDefizite1NS);
		const hat_defizit : boolean = !defizite.isEmpty();
		const hat_defizit_sonstige_3er : boolean = faecher.fg2.getFaecherAnzahl(ServiceAbschlussMSA.filterBenoetigte3er) < benoetige3er;
		if ((!hat_defizit) && (!hat_defizit_sonstige_3er))
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, true);
		if (!ignorierenGenutzt) {
			for (const defizitFach of mangelhaft) {
				if (!GELeistungsdifferenzierteKursart.Sonstige.hat(defizitFach.kursart))
					continue;
				defizitFach.ausgeglichen = true;
				this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> Prüfe: Defizit unberücksichtigt in " + defizitFach.kuerzel);
				const abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, logIndent! + "  ", npFaecher, benoetige3er, true, ausgleichGenutzt, nachpruefungGenutzt);
				if (abschlussergebnis.erworben)
					return abschlussergebnis;
				defizitFach.ausgeglichen = false;
			}
		}
		if (!ausgleichGenutzt) {
			if (hat_defizit_sonstige_3er) {
				const ausgleichsFach : GEAbschlussFach | null = faecher.fg2.getFach(ServiceAbschlussMSA.filterAusgleiche3er);
				if (ausgleichsFach === null) {
					this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> Kein Ausgleich für eine fehlende 3 vorhanden. ");
				} else {
					this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> Prüfe: Ausgleich einer fehlende 3 durch " + ausgleichsFach.kuerzel);
					ausgleichsFach.ausgleich = true;
					const abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, logIndent! + "  ", npFaecher, benoetige3er - 1, ignorierenGenutzt, true, nachpruefungGenutzt);
					if (abschlussergebnis.erworben)
						return abschlussergebnis;
					ausgleichsFach.ausgleich = false;
				}
			} else {
				const ausgleichsFaecher : List<GEAbschlussFach> = faecher.getFaecher(ServiceAbschlussMSA.filterAusgleiche);
				if (ausgleichsFaecher.size() <= benoetige3er) {
					this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> kann Ausgleichsregelung nicht nutzen, da nicht genügend 3er-Fächer vorhanden sind.");
				} else {
					const anzahlSonstigeFaecherMind3 : number = faecher.fg2.getFaecherAnzahl(ServiceAbschlussMSA.filterBenoetigte3er);
					for (const defizitFach of defizite) {
						for (const ausgleichsFach of ausgleichsFaecher) {
							this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> Prüfe: Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
							if ((GELeistungsdifferenzierteKursart.Sonstige.hat(ausgleichsFach.kursart)) && (anzahlSonstigeFaecherMind3 <= benoetige3er)) {
								this.logger.logLn(LogLevel.DEBUG, logIndent! + "   -> " + ausgleichsFach.kuerzel + " nicht als Ausgleich möglich, da für die Mindestanforderung mind. " + benoetige3er + "x3 benötigt wird, aber nur " + anzahlSonstigeFaecherMind3 + "x3 zur Verfügung steht.");
							} else {
								defizitFach.ausgeglichen = true;
								ausgleichsFach.ausgleich = true;
								const abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, logIndent! + "  ", npFaecher, benoetige3er, ignorierenGenutzt, true, nachpruefungGenutzt);
								if (abschlussergebnis.erworben)
									return abschlussergebnis;
								defizitFach.ausgeglichen = false;
								ausgleichsFach.ausgleich = false;
							}
						}
					}
				}
			}
		}
		if (!nachpruefungGenutzt) {
			if (hat_defizit_sonstige_3er) {
				const npKandidaten : List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceAbschlussMSA.filterDefiziteBenoetigte3erMitNPOption);
				for (const defizitFach of npKandidaten) {
					this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> Prüfe: Nachprüfung in " + defizitFach.kuerzel + " auf befriedigend möglich?");
					defizitFach.ausgeglichen = true;
					defizitFach.note--;
					const abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, logIndent! + "  ", npFaecher, benoetige3er, ignorierenGenutzt, ausgleichGenutzt, true);
					this.logger.logLn(LogLevel.DEBUG, logIndent! + (abschlussergebnis.erworben ? "   -> Ja!" : "   -> Nein!"));
					if (abschlussergebnis.erworben)
						npFaecher.add(defizitFach);
					defizitFach.note++;
					defizitFach.ausgeglichen = false;
				}
			} else {
				const npKandidaten : List<GEAbschlussFach> = faecher.fg2.getFaecher(ServiceAbschlussMSA.filterDefiziteMitNPOption);
				for (const defizitFach of npKandidaten) {
					this.logger.logLn(LogLevel.DEBUG, logIndent! + " -> Prüfe: Nachprüfung in " + defizitFach.kuerzel + " möglich?");
					defizitFach.ausgeglichen = true;
					defizitFach.note--;
					const abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, logIndent! + "  ", npFaecher, benoetige3er, ignorierenGenutzt, ausgleichGenutzt, true);
					this.logger.logLn(LogLevel.DEBUG, logIndent! + (abschlussergebnis.erworben ? "   -> Ja!" : "   -> Nein!"));
					if (abschlussergebnis.erworben)
						npFaecher.add(defizitFach);
					defizitFach.note++;
					defizitFach.ausgeglichen = false;
				}
			}
		}
		if ((!nachpruefungGenutzt) && (!npFaecher.isEmpty()))
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA, AbschlussManager.getKuerzel(npFaecher));
		return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.ge.ServiceAbschlussMSA';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.Service', 'de.svws_nrw.core.abschluss.ge.ServiceAbschlussMSA'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_ge_ServiceAbschlussMSA(obj : unknown) : ServiceAbschlussMSA {
	return obj as ServiceAbschlussMSA;
}
