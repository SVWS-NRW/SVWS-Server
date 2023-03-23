import { GEAbschlussFach } from '../../../core/data/abschluss/GEAbschlussFach';
import { Service } from '../../../core/Service';
import { JavaString } from '../../../java/lang/JavaString';
import { GELeistungsdifferenzierteKursart } from '../../../core/types/ge/GELeistungsdifferenzierteKursart';
import { LogLevel } from '../../../core/logger/LogLevel';
import { Predicate } from '../../../java/util/function/Predicate';
import { AbschlussFaecherGruppe } from '../../../core/abschluss/ge/AbschlussFaecherGruppe';
import { GEAbschlussFaecher } from '../../../core/data/abschluss/GEAbschlussFaecher';
import { AbschlussErgebnis } from '../../../core/data/abschluss/AbschlussErgebnis';
import { SchulabschlussAllgemeinbildend } from '../../../core/types/schule/SchulabschlussAllgemeinbildend';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { Vector } from '../../../java/util/Vector';
import { AbschlussManager } from '../../../core/abschluss/AbschlussManager';
import { AbschlussFaecherGruppen } from '../../../core/abschluss/ge/AbschlussFaecherGruppen';

export class ServiceAbschlussMSA extends Service<GEAbschlussFaecher, AbschlussErgebnis> {

	private filterDefizite : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && ((f.note > 4) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3))) };

	private filterDefizite1NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 5)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 4))) };

	private filterDefizite2NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 6)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 5))) };

	private filterDefiziteMehrAls1NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 6)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note >= 5))) };

	private filterDefiziteMehrAls2NS : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 6)) };

	private filterDefiziteMitNPOption : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note === 5)) };

	private filterDefizitWP : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (f.note > 4) && JavaString.equalsIgnoreCase("WP", f.kuerzel) };

	private filterDefizitNichtWP : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgeglichen && (f.note > 4) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3)) && !JavaString.equalsIgnoreCase("WP", f.kuerzel) };

	private filterBenoetigte3er : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgleich && (f.note <= 3) && (GELeistungsdifferenzierteKursart.Sonstige.hat(f.kursart)) };

	private filterDefiziteBenoetigte3erMitNPOption : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgleich && (f.note === 4) && (GELeistungsdifferenzierteKursart.Sonstige.hat(f.kursart)) };

	private filterAusgleiche : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgleich && ((f.note < 3) || ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note < 4))) };

	private filterAusgleiche3er : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => !f.ausgleich && (f.note < 3) };

	private filterEKurse : Predicate<GEAbschlussFach> = { test : (f: GEAbschlussFach) => (GELeistungsdifferenzierteKursart.E.hat(f.kursart)) };


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
		let faecher : AbschlussFaecherGruppen = new AbschlussFaecherGruppen(new AbschlussFaecherGruppe(input, Arrays.asList("D", "M", "E", "WP"), null), new AbschlussFaecherGruppe(input, null, Arrays.asList("D", "M", "E", "WP", "LBNW", "LBAL")));
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
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht genügend leistungsdiffernzierte Fächer gefunden.");
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
		if (anzahlEKurse < 2) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
			this.logger.logLn(LogLevel.INFO, " => kein MSA (FOR) - nicht genügend E-Kurse belegt");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		} else
			if (anzahlEKurse > 2) {
				let zuviel : number = anzahlEKurse - 2;
				let eKursFG2 : GEAbschlussFach | null = faecher.fg2.getFach(this.filterEKurse);
				if (eKursFG2 !== null) {
					let note : number = eKursFG2.note;
					let note_neu : number = (note === 1) ? 1 : note - 1;
					this.logger.logLn(LogLevel.DEBUG, "   " + eKursFG2.kuerzel + ":(E)" + note + "->(G)" + note_neu);
					eKursFG2.note = note_neu;
					eKursFG2.kursart = GELeistungsdifferenzierteKursart.G.kuerzel;
					zuviel--;
				}
				while (zuviel > 0) {
					let eKursFG1 : GEAbschlussFach | null = faecher.fg1.getFach(this.filterEKurse);
					if (eKursFG1 !== null) {
						let note : number = eKursFG1.note;
						let note_neu : number = (note === 1) ? 1 : note - 1;
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
		let abschlussergebnis : AbschlussErgebnis = this.pruefeDefizite(faecher, "");
		if (abschlussergebnis.erworben) {
			this.logger.logLn(LogLevel.DEBUG, "______________________________");
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
	 * @param log_indent   die Einrückung für das Logging
	 *
	 * @return das Ergebnis der Abschlussberechnung in Bezug die Defizitberechnung
	 */
	private pruefeDefizite(faecher : AbschlussFaecherGruppen, log_indent : string) : AbschlussErgebnis {
		let ignorieren_genutzt : boolean = false;
		let ausgleich_genutzt : boolean = false;
		let nachpruefung_genutzt : boolean = false;
		let npFaecher : List<GEAbschlussFach> = new Vector();
		let fg1_defizite : number = faecher.fg1.getFaecherAnzahl(this.filterDefizite);
		let fg2_defizite : number = faecher.fg2.getFaecherAnzahl(this.filterDefizite);
		let fg1_anzahlAusgleiche : number = faecher.fg1.getFaecherAnzahl(this.filterAusgleiche);
		if (fg1_defizite > 0)
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> FG1: Defizit" + (fg1_defizite > 1 ? "e" : "") + ": " + faecher.fg1.getKuerzelListe(this.filterDefizite)!);
		if (fg2_defizite > 0)
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> FG2: Defizit" + (fg2_defizite > 1 ? "e" : "") + ": " + faecher.fg2.getKuerzelListe(this.filterDefizite)!);
		if (faecher.fg1.getFaecherAnzahl(this.filterDefiziteMehrAls1NS) > 0) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> in FG1 unzulässig: mind. 1x6 oder bei einem G-Kurs 1x5");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		}
		if (faecher.fg2.getFaecherAnzahl(this.filterDefiziteMehrAls2NS) > 0) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> in FG2 unzulässig: in einem G-Kurs 1x6");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		}
		let sonstige_ungenuegend : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterDefizite2NS);
		if (sonstige_ungenuegend.size() > 1) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite, kann nicht mehr als eine Note mit 6 (bzw. 5 bei einem G-Kurs) in FG2 unberücksichtigt lassen");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		} else
			if (sonstige_ungenuegend.size() === 1) {
				let defizitFach : GEAbschlussFach = sonstige_ungenuegend.get(0);
				if (GELeistungsdifferenzierteKursart.Sonstige.hat(defizitFach.kursart)) {
					defizitFach.ausgeglichen = true;
					this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> unberücksichtigt: Defizit in " + defizitFach.kuerzel + " (2 Notenstufen)");
					ignorieren_genutzt = true;
				} else
					if ((GELeistungsdifferenzierteKursart.E.hat(defizitFach.kursart)) && (defizitFach.note === 6)) {
						this.logger.logLn(LogLevel.DEBUG, log_indent! + "   -> Ein ungenügend in dem E-Kurs " + defizitFach.kuerzel + " kann nicht ausgelichen werden und eine Nachprüfung ist nicht zulässig!");
						return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
					} else {
						this.logger.logLn(LogLevel.DEBUG, log_indent! + "   -> Nachprüfung muss falls möglich in " + defizitFach.kuerzel + " stattfinden!");
						nachpruefung_genutzt = true;
						npFaecher.add(defizitFach);
						defizitFach.note--;
					}
			}
		let wp_defizit : GEAbschlussFach | null = faecher.fg1.getFach(this.filterDefizitWP);
		if ((fg1_defizite > 2) || ((fg1_defizite === 2) && (wp_defizit === null)) || ((fg1_defizite === 2) && (fg1_anzahlAusgleiche === 0)) || ((fg1_defizite === 1) && (wp_defizit === null) && (fg1_anzahlAusgleiche === 0))) {
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
		}
		if ((fg1_defizite === 2) && (wp_defizit !== null)) {
			if (nachpruefung_genutzt) {
				this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> zu viele Defizite in FG1, eine Nachprüfung in mehreren Fächern (WP, " + sonstige_ungenuegend.get(0).kuerzel + ") ist nicht möglich!");
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
			}
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> WP-Defizite in FG1, eine Nachprüfung ist, sofern möglich, in WP nötig!");
			npFaecher.add(wp_defizit);
			nachpruefung_genutzt = true;
		}
		if ((fg1_defizite === 2) || ((fg1_defizite === 1) && (wp_defizit === null))) {
			ausgleich_genutzt = true;
			let defizitFach : GEAbschlussFach | null = faecher.fg1.getFach(this.filterDefizitNichtWP);
			if (defizitFach === null)
				throw new NullPointerException()
			defizitFach.ausgeglichen = true;
			let ausgleichsFach : GEAbschlussFach | null = faecher.fg1.getFach(this.filterAusgleiche);
			if (ausgleichsFach === null)
				throw new NullPointerException()
			ausgleichsFach.ausgleich = true;
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Gleiche das Defizit (FG1) in " + defizitFach.kuerzel + " mit " + ausgleichsFach.kuerzel + " (FG1) aus.");
		}
		if (((fg1_defizite === 1) && (wp_defizit !== null))) {
			let defizitFach : GEAbschlussFach = wp_defizit;
			let ausgleichsFach : GEAbschlussFach | null = faecher.fg1.getFach(this.filterAusgleiche);
			if (ausgleichsFach !== null) {
				ausgleich_genutzt = true;
				defizitFach.ausgeglichen = true;
				ausgleichsFach.ausgleich = true;
				this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe mit Ausgleich: Gleiche das Defizit (FG1) in " + defizitFach.kuerzel + " mit " + ausgleichsFach.kuerzel + " (FG1) aus. " + defizitFach.kuerzel + " alternativ als Nachprüfungsfach denkbar.");
				let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent! + "  ", npFaecher, 2, ignorieren_genutzt, ausgleich_genutzt, nachpruefung_genutzt);
				if (!abschlussergebnis.erworben && abschlussergebnis.npFaecher !== null && AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis) && wp_defizit.kuerzel !== null)
					abschlussergebnis.npFaecher.add(wp_defizit.kuerzel);
				return abschlussergebnis;
			}
			if ((sonstige_ungenuegend.size() === 1) && (!sonstige_ungenuegend.get(0).ausgeglichen)) {
				this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> das Defizit in WP kann nicht ausgeglichen werden und eine Nachprüfung in mehreren Fächern (WP, " + sonstige_ungenuegend.get(0).kuerzel + ") ist nicht möglich!");
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
			}
			this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> WP-Defizite in FG1 ohne Ausgleichsmöglichkeit, eine Nachprüfung ist, sofern möglich, in WP nötig!");
			npFaecher.add(wp_defizit);
			nachpruefung_genutzt = true;
		}
		let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent, npFaecher, 2, ignorieren_genutzt, ausgleich_genutzt, nachpruefung_genutzt);
		if ((nachpruefung_genutzt) && abschlussergebnis.erworben)
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA, AbschlussManager.getKuerzel(npFaecher));
		return abschlussergebnis;
	}

	/**
	 * Führt eine Detailprüfung in der Fächergruppe 2 durch. Diese Methode wird ggf. mehrfach - auch rekursiv - aufgerufen.
	 *
	 * @param faecher                die Abschlussfächer nach Fächergruppen sortiert
	 * @param log_indent             die Einrückung für das Logging
	 * @param npFaecher              die Liste der Nachprüfungsfächer, die bisher schon feststehen
	 * @param benoetige3er           die Anzahl der 3er, die noch in FG2 benötigt werden
	 * @param ignorieren_genutzt     gibt an, ob die Möglichkeit eine defizitäre Leistung in FG2 zu ignorieren schon genutzt wurde
	 * @param ausgleich_genutzt      gibt an, ob die Möglichkeit des Ausgleichs über ein anderes Fach schon genutzt wurde
	 * @param nachpruefung_genutzt   gibt an, ob die Nachprüfungsmöglichkeit bereits eingesetzt werden musste
	 *
	 * @return das Ergebnis der Abschlussberechnung in Bezug auf den Stand dieser Detailprüfung
	 */
	private pruefeFG2(faecher : AbschlussFaecherGruppen, log_indent : string, npFaecher : List<GEAbschlussFach>, benoetige3er : number, ignorieren_genutzt : boolean, ausgleich_genutzt : boolean, nachpruefung_genutzt : boolean) : AbschlussErgebnis {
		let defizite : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterDefizite);
		let mangelhaft : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterDefizite1NS);
		let hat_defizit : boolean = defizite.size() > 0;
		let hat_defizit_sonstige_3er : boolean = faecher.fg2.getFaecherAnzahl(this.filterBenoetigte3er) < benoetige3er;
		if ((!hat_defizit) && (!hat_defizit_sonstige_3er))
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, true);
		if (!ignorieren_genutzt) {
			for (let defizitFach of mangelhaft) {
				if (!GELeistungsdifferenzierteKursart.Sonstige.hat(defizitFach.kursart))
					continue;
				defizitFach.ausgeglichen = true;
				this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe: Defizit unberücksichtigt in " + defizitFach.kuerzel);
				let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent! + "  ", npFaecher, benoetige3er, true, ausgleich_genutzt, nachpruefung_genutzt);
				if (abschlussergebnis.erworben)
					return abschlussergebnis;
				defizitFach.ausgeglichen = false;
			}
		}
		if (!ausgleich_genutzt) {
			if (hat_defizit_sonstige_3er) {
				let ausgleichsFach : GEAbschlussFach | null = faecher.fg2.getFach(this.filterAusgleiche3er);
				if (ausgleichsFach === null) {
					this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Kein Ausgleich für eine fehlende 3 vorhanden. ");
				} else {
					this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe: Ausgleich einer fehlende 3 durch " + ausgleichsFach.kuerzel);
					ausgleichsFach.ausgleich = true;
					let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent! + "  ", npFaecher, benoetige3er - 1, ignorieren_genutzt, true, nachpruefung_genutzt);
					if (abschlussergebnis.erworben)
						return abschlussergebnis;
					ausgleichsFach.ausgleich = false;
				}
			} else {
				let ausgleichsFaecher : List<GEAbschlussFach> = faecher.getFaecher(this.filterAusgleiche);
				if (ausgleichsFaecher.size() <= benoetige3er) {
					this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> kann Ausgleichsregelung nicht nutzen, da nicht genügend 3er-Fächer vorhanden sind.");
				} else {
					let anzahlSonstigeFaecherMind3 : number = faecher.fg2.getFaecherAnzahl(this.filterBenoetigte3er);
					for (let defizitFach of defizite) {
						for (let ausgleichsFach of ausgleichsFaecher) {
							this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe: Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
							if ((GELeistungsdifferenzierteKursart.Sonstige.hat(ausgleichsFach.kursart)) && (anzahlSonstigeFaecherMind3 <= benoetige3er)) {
								this.logger.logLn(LogLevel.DEBUG, log_indent! + "   -> " + ausgleichsFach.kuerzel + " nicht als Ausgleich möglich, da für die Mindestanforderung mind. " + benoetige3er + "x3 benötigt wird, aber nur " + anzahlSonstigeFaecherMind3 + "x3 zur Verfügung steht.");
							} else {
								defizitFach.ausgeglichen = true;
								ausgleichsFach.ausgleich = true;
								let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent! + "  ", npFaecher, benoetige3er, ignorieren_genutzt, true, nachpruefung_genutzt);
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
		if (!nachpruefung_genutzt) {
			if (hat_defizit_sonstige_3er) {
				let npKandidaten : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterDefiziteBenoetigte3erMitNPOption);
				for (let defizitFach of npKandidaten) {
					this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe: Nachprüfung in " + defizitFach.kuerzel + " auf befriedigend möglich?");
					defizitFach.ausgeglichen = true;
					defizitFach.note--;
					let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent! + "  ", npFaecher, benoetige3er, ignorieren_genutzt, ausgleich_genutzt, true);
					this.logger.logLn(LogLevel.DEBUG, log_indent! + (abschlussergebnis.erworben ? "   -> Ja!" : "   -> Nein!"));
					if (abschlussergebnis.erworben)
						npFaecher.add(defizitFach);
					defizitFach.note++;
					defizitFach.ausgeglichen = false;
				}
			} else {
				let npKandidaten : List<GEAbschlussFach> = faecher.fg2.getFaecher(this.filterDefiziteMitNPOption);
				for (let defizitFach of npKandidaten) {
					this.logger.logLn(LogLevel.DEBUG, log_indent! + " -> Prüfe: Nachprüfung in " + defizitFach.kuerzel + " möglich?");
					defizitFach.ausgeglichen = true;
					defizitFach.note--;
					let abschlussergebnis : AbschlussErgebnis = this.pruefeFG2(faecher, log_indent! + "  ", npFaecher, benoetige3er, ignorieren_genutzt, ausgleich_genutzt, true);
					this.logger.logLn(LogLevel.DEBUG, log_indent! + (abschlussergebnis.erworben ? "   -> Ja!" : "   -> Nein!"));
					if (abschlussergebnis.erworben)
						npFaecher.add(defizitFach);
					defizitFach.note++;
					defizitFach.ausgeglichen = false;
				}
			}
		}
		if ((!nachpruefung_genutzt) && (npFaecher.size() > 0))
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA, AbschlussManager.getKuerzel(npFaecher));
		return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA, false);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.ge.ServiceAbschlussMSA', 'de.nrw.schule.svws.core.Service'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_ge_ServiceAbschlussMSA(obj : unknown) : ServiceAbschlussMSA {
	return obj as ServiceAbschlussMSA;
}
