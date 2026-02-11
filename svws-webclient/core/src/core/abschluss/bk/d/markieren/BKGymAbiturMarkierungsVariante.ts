import { JavaObject } from '../../../../../java/lang/JavaObject';
import { BKGymAbiturMarkierungsalgorithmusErgebnis } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusErgebnis';
import { BKGymAbiturMarkierungsVarianten, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsVarianten } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVarianten';
import { GostAbiturFach } from '../../../../../core/types/gost/GostAbiturFach';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { BKGymAbiturFachbelegung } from '../../../../../core/data/bk/abi/BKGymAbiturFachbelegung';
import { BKGymAbiturFachbelegungHalbjahr } from '../../../../../core/data/bk/abi/BKGymAbiturFachbelegungHalbjahr';
import { JavaString } from '../../../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../core/exceptions/DeveloperNotificationException';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import type { Comparator } from '../../../../../java/util/Comparator';
import { BKGymAbiturUtils } from '../../../../../core/utils/bk/BKGymAbiturUtils';
import { Note } from '../../../../../asd/types/Note';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';

export class BKGymAbiturMarkierungsVariante extends JavaObject {

	/**
	 * Die Verwaltung der Varianten
	 */
	public readonly varianten: BKGymAbiturMarkierungsVarianten;

	/**
	 * Kennung der Variante
	 */
	private readonly kennung: string;

	/**
	 * Gibt an, ob die Zulassung mit diesem Ergebnis erreicht wurde oder nicht.
	 */
	private hatZulassung: boolean = false;

	/**
	 * Markierung stoppen
	 */
	private gestoppt: boolean = false;

	/**
	 * Defizitregeln abschließend geprüft
	 */
	private defizitregelnAbgeschlossen: boolean = false;

	/**
	 * Die Summe der Notenpunkte aller Markierungen, LKs sind doppelt gezählt
	 */
	private summeNotenpunkte: number = 0;

	/**
	 * Die Summe der markierten Kurse, LKs sind doppelt gezählt
	 */
	private anzahlKurse: number = 0;

	/**
	 * Die Anzahl der defizitären markierten Leistungskurse
	 */
	private defiziteLK: number = 0;

	/**
	 * Die Anzahl der defizitären markierten Grundkurse
	 */
	private defiziteGK: number = 0;

	/**
	 * Facharbeit einbeziehen
	 */
	private readonly facharbeitEinbeziehen: boolean;

	/**
	 * eine Liste der vorgenommenen Markierungen von Halbjahres-Belegungen in der Qualifikationsphase
	 */
	private readonly markiert: List<BKGymAbiturMarkierungsalgorithmusMarkierung> = new ArrayList<BKGymAbiturMarkierungsalgorithmusMarkierung>();

	/**
	 * eine Liste der Halbjahres-Belegungen in der Qualifikationsphase, die nicht markiert sind
	 */
	private readonly unmarkiert: List<BKGymAbiturMarkierungsalgorithmusMarkierung> = new ArrayList<BKGymAbiturMarkierungsalgorithmusMarkierung>();

	/**
	 * Ein Log, der den Ablauf des Markierungsalgorithmus verdeutlicht
	 */
	private readonly log: List<string> = new ArrayList<string>();

	/**
	 *  Comparator für dieses Objekt.
	 *  Kriterien: ZulassungJa, größte PunktzahlBlockI
	 */
	public static readonly comparator: Comparator<BKGymAbiturMarkierungsVariante> = { compare: (a: BKGymAbiturMarkierungsVariante, b: BKGymAbiturMarkierungsVariante) => {
		if (a.istErfolgreich() !== b.istErfolgreich())
			return a.istErfolgreich() ? -1 : 1;
		return b.getPunktzahlBlockI() - a.getPunktzahlBlockI();
	} };


	/**
	 * Konstruktor für die Root Markierungsvariante. Diese sollte nur einmal vom Markierungsalgorithmus
	 * aufgerufen werden.
	 * Die Fachbelegungen zur Initialisierung wird via Varianten aus dem Manager-Objekt bezogen.
	 *
	 * @param v   die Kollektion der Varianten, zu der auch dieses Objekt gehört.
	 */
	public constructor(v: BKGymAbiturMarkierungsVarianten);

	/**
	 * Copy-Konstruktor für neue Varianten. Die Kennung wird an die Kopie angehängt.
	 * Dieser Konstruktor ist zur Verwendung durch die Regel BKGymAbiturMarkierungsregelKopie vorgesehen.
	 *
	 * @param other        das zu kopierende Objekt
	 * @param kennung      die Kennung der neuen Variante
	 * @param facharbeit   ob die Facharbeit in dieser Variante einbezogen wird
	 */
	public constructor(other: BKGymAbiturMarkierungsVariante, kennung: string, facharbeit: boolean);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0: BKGymAbiturMarkierungsVariante | BKGymAbiturMarkierungsVarianten, __param1?: string, __param2?: boolean) {
		super();
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsVarianten')))) && (__param1 === undefined) && (__param2 === undefined)) {
			const v: BKGymAbiturMarkierungsVarianten = cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsVarianten(__param0);
			this.varianten = v;
			this.kennung = "Root";
			this.facharbeitEinbeziehen = false;
			this.defizitregelnAbgeschlossen = false;
			this.init();
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsVariante')))) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean")) {
			const other: BKGymAbiturMarkierungsVariante = cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsVariante(__param0);
			const kennung: string = __param1;
			const facharbeit: boolean = __param2 as boolean;
			this.varianten = other.varianten;
			this.gestoppt = other.gestoppt;
			this.defizitregelnAbgeschlossen = other.defizitregelnAbgeschlossen;
			this.kennung = other.kennung + "#" + kennung;
			this.summeNotenpunkte = other.summeNotenpunkte;
			this.anzahlKurse = other.anzahlKurse;
			this.defiziteLK = other.defiziteLK;
			this.defiziteGK = other.defiziteGK;
			if (facharbeit) {
				this.facharbeitEinbeziehen = true;
				const punkte: number | null = this.varianten.abiturdatenManager.getAbidaten().facharbeitNotenpunkte;
				this.anzahlKurse += 2;
				if (punkte !== null)
					this.summeNotenpunkte += 2 * punkte;
				if (!this.varianten.abiturdatenManager.getFachbelegungManager().getIstFacharbeitLK())
					this.setHatZulassung(false);
			} else {
				this.facharbeitEinbeziehen = other.facharbeitEinbeziehen;
			}
			this.hatZulassung = other.hatZulassung;
			this.markiert.addAll(other.markiert);
			this.unmarkiert.addAll(other.unmarkiert);
			this.log.addAll(other.log);
			this.addLogEintrag(1, "Die Variante " + this.kennung + " wurde erzeugt.");
		} else throw new Error('invalid method overload');
	}

	/**
	 * Initialisiert die Liste unmarkiert mit allen Belegungen und sortiert diese
	 * absteigend nach erreichter Punktzahl
	 */
	public init(): void {
		const fachbelegungen: List<BKGymAbiturFachbelegung> = this.varianten.abiturdatenManager.getAbidaten().fachbelegungen;
		const schuljahr: number = this.varianten.abiturdatenManager.getSchuljahrAbitur();
		this.hatZulassung = true;
		this.gestoppt = false;
		this.summeNotenpunkte = 0;
		this.anzahlKurse = 0;
		this.defiziteLK = 0;
		this.defiziteGK = 0;
		for (const fachbelegung of fachbelegungen) {
			for (const hj of GostHalbjahr.getQualifikationsphase()) {
				const belegung: BKGymAbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[hj.id];
				if (belegung === null)
					continue;
				if ((belegung.notenkuerzel !== null) && (!JavaString.isEmpty(belegung.notenkuerzel))) {
					const markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null = new BKGymAbiturMarkierungsalgorithmusMarkierung();
					markierung.fachID = fachbelegung.fachID;
					markierung.halbjahrID = hj.id;
					markierung.punkte = Note.getPunkteFromNotenkuerzel(belegung.notenkuerzel, schuljahr);
					if (markierung.punkte !== null)
						this.unmarkiert.add(markierung);
				}
			}
		}
		BKGymAbiturMarkierungsVariante.sortMarkierungsliste(this.unmarkiert);
	}

	/**
	 * Getter für erfolgreich
	 *
	 * @return den Erfolgsstatus
	 */
	public istErfolgreich(): boolean {
		return this.hatZulassung;
	}

	/**
	 * Getter für kennung
	 *
	 * @return die Kennung
	 */
	public getKennung(): string | null {
		return this.kennung;
	}

	/**
	 * Setter für gestoppt
	 * @param gestoppt   ob fortgesetzt werden soll oder nicht.
	 */
	public setGestoppt(gestoppt: boolean): void {
		this.gestoppt = gestoppt;
	}

	/**
	 * Getter für gestoppt
	 *
	 * @return gestoppt
	 */
	public istGestoppt(): boolean {
		return this.gestoppt;
	}

	/**
	 *Getter für defizitregelnAbgeschlossen
	 *
	 * @return defizitregelnAbgeschlossen
	 */
	public sindDefizitregelnAbgeschlossen(): boolean {
		return this.defizitregelnAbgeschlossen;
	}

	/**
	 * Setter für defizitregelnAbgeschlossen
	 *
	 * @param defizitregelnAbgeschlossen   ob die Defizitregeln bereits geprüft wurden
	 */
	public setDefizitregelnAbgeschlossen(defizitregelnAbgeschlossen: boolean): void {
		this.defizitregelnAbgeschlossen = defizitregelnAbgeschlossen;
	}

	/**
	 * Liefert die Gesamtanzahl der eingebrachten Defizite
	 *
	 * @return Anzahl der eingebrachten Defizite
	 */
	public getDefizite(): number {
		return this.defiziteLK + this.defiziteGK;
	}

	/**
	 * Liefert die Anzahl der eingebrachten Kurse ohne doppelte Gewichtung der LKs
	 *
	 * @return die Anzahl der eingebrachten Kurse
	 */
	public anzahlEingebrachteKurse(): number {
		return this.markiert.size();
	}

	/**
	 * Setzt diese Ergebnisvariante auf nicht erfolgreich
	 *
	 * @param hatZulassung   der entsprechende boolesche Wert
	 */
	public setHatZulassung(hatZulassung: boolean): void {
		this.hatZulassung = hatZulassung;
	}

	/**
	 * Fügt den angegebenen Eintrag der Liste der markierten Einträge hinzu.
	 * Die Auswertungsdaten werden entsprechend der Markierung aktualisiert.
	 *
	 * @param markierung   der zu markierende Eintrag
	 */
	public markiereEintrag(markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null): void {
		if (markierung === null)
			return;
		const defizit: number = (markierung.punkte === null) || (markierung.punkte < 5) ? 1 : 0;
		this.markiert.add(markierung);
		this.anzahlKurse++;
		this.summeNotenpunkte += (markierung.punkte === null ? 0 : markierung.punkte);
		const fachIDLK1: number | null = this.varianten.abiturdatenManager.getFachbelegungManager().getAbiFachID(GostAbiturFach.LK1);
		const fachIDLK2: number | null = this.varianten.abiturdatenManager.getFachbelegungManager().getAbiFachID(GostAbiturFach.LK2);
		if ((markierung.punkte !== null) && (((fachIDLK1 !== null) && (fachIDLK1 === markierung.fachID)) || ((fachIDLK2 !== null) && (fachIDLK2 === markierung.fachID)))) {
			this.anzahlKurse++;
			this.summeNotenpunkte += markierung.punkte;
			this.defiziteLK += defizit;
		} else {
			this.defiziteGK += defizit;
		}
	}

	/**
	 * Markiert maximal bis zur angegeben Kursanzahl Kurse, wenn die Bedingung erfüllt ist
	 *
	 * @param kursanzahl   die maximale Anzahl zu markierender Kurse
	 * @param bedingung    die Bedingung, unter der ein Eintrag markiert wird
	 *
	 * @return die Anzahl verbleibender Kurse, die nicht markiert werden konnte
	 */
	public markiereKursanzahl(kursanzahl: number, bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> | null): number {
		if (kursanzahl <= 0)
			return 0;
		if (bedingung === null)
			return kursanzahl;
		let verbleibend: number = kursanzahl;
		const weiterUnmarkiert: List<BKGymAbiturMarkierungsalgorithmusMarkierung> | null = new ArrayList<BKGymAbiturMarkierungsalgorithmusMarkierung>(this.unmarkiert.size());
		for (const unmarked of this.unmarkiert) {
			if ((verbleibend > 0) && bedingung.test(unmarked)) {
				this.markiereEintrag(unmarked);
				verbleibend--;
			} else {
				weiterUnmarkiert.add(unmarked);
			}
		}
		this.unmarkiert.clear();
		this.unmarkiert.addAll(weiterUnmarkiert);
		return verbleibend;
	}

	/**
	 * prüft, ob die angegebene Anzahl an unmarkierten Kursen der Bedingung genügen.
	 *
	 * @param kursanzahl   die geforderte Anzahl an Kursen, mit der Bedingung
	 * @param bedingung    die Bedingung, unter der ein Eintrag gezählt wird
	 *
	 * @return die Anzahl verbleibender Kurse, die nicht der Bedingung genügen
	 */
	public pruefeKursanzahl(kursanzahl: number, bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> | null): number {
		if (bedingung === null)
			return kursanzahl;
		let verbleibend: number = kursanzahl;
		for (const marked of this.markiert)
			if ((verbleibend > 0) && bedingung.test(marked))
				verbleibend--;
		for (const unmarked of this.unmarkiert)
			if ((verbleibend > 0) && bedingung.test(unmarked))
				verbleibend--;
		return verbleibend;
	}

	/**
	 * zählt die markierten Kurse, die die übergebene Bedingung erfüllen
	 *
	 * @param bedingung    die Bedingung, unter der ein Eintrag gezählt wird
	 *
	 * @return die Anzahl der markierten Kurse, die die Bedingung erfüllen
	 */
	public zaehleMarkierte(bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> | null): number {
		if (bedingung === null)
			return 0;
		let anzahl: number = 0;
		for (const marked of this.markiert)
			if (bedingung.test(marked))
				anzahl++;
		return anzahl;
	}

	/**
	 * Ermittelt die Punktzahl für das Fach, wenn alle Belegungen vorhanden sind.
	 *
	 * @param fachID     die ID des Fachs
	 * @param anzahl     die Anzahl einzubringender Kurse aus der Q-Phase
	 *
	 * @return die Anzahl der Punkte der eingebrachten Kurse oder 0, wenn die Bedingung belegtSeit nicht erfüllt ist
	 * oder Anzahl nicht erreicht wird.
	 */
	public punktsummeFuerFach(fachID: number, anzahl: number): number {
		let summe: number = 0;
		let verbleibend: number = anzahl;
		for (const unmarked of this.unmarkiert) {
			if (verbleibend <= 0)
				break;
			if (fachID === unmarked.fachID) {
				summe += (unmarked.punkte === null ? 0 : unmarked.punkte);
				verbleibend--;
			}
		}
		if (verbleibend <= 0)
			return summe;
		return 0;
	}

	/**
	 * Ergänzt den Logeintrag mit der angegebenen Einrückung
	 *
	 * @param text     der zu loggende Text
	 * @param indent   die Einrückung mal 4 Leerzeichen des Logeintrags.
	 */
	public addLogEintrag(indent: number, text: string): void {
		switch (indent) {
			case 0: {
				this.log.add(text)
				break;
			}
			case 1: {
				this.log.add("    " + text)
				break;
			}
			case 2: {
				this.log.add("        " + text)
				break;
			}
			default: {
				throw new DeveloperNotificationException("Der Indent " + indent + " ist nicht vorgesehen.")
				break;
			}
		}
	}

	/**
	 * Wertet aus ob noch zu markierende Kurse verbleiben
	 *
	 * @param restAnzahl   Anzahl noch zu markierender Kurse
	 * @param sollAnzahl   Anzahl der notwendig einzubringenden Kurse
	 * @param indent       Einrückung für den Log-Eintrag
	 */
	public addLogAnzahlMarkierungen(restAnzahl: number, sollAnzahl: number, indent: number): void {
		if (restAnzahl > 0) {
			this.addLogEintrag(1, "Fehler: Konnte nicht die nötige Anzahl von " + sollAnzahl + " Kursen einbringen.");
			this.setHatZulassung(false);
		} else {
			this.addLogEintrag(1, "Erforderliche Anzahl von " + sollAnzahl + " Kursen konnten eingebracht werden.");
		}
	}

	/**
	 * Berechnet den Punktedurchschnitt, wobei die doppelte Gewichtung der LKs
	 * in den Werten berücksichtigt ist.
	 *
	 * @return der Punktedurchschnitt
	 */
	public getDurchschnitt(): number {
		if (this.anzahlKurse === 0)
			return 0;
		return this.summeNotenpunkte / this.anzahlKurse as number;
	}

	/**
	 * Berechnet die normierte Punktezahl in Block I.
	 * ab 0.5 wird aufgerundet.
	 *
	 * @return die Punktanzahl
	 */
	public getPunktzahlBlockI(): number {
		return (this.getDurchschnitt() * 40 + 0.5) as number;
	}

	/**
	 * Liefert das Ergebnis als BKGymAbiturMarkierungsalgorithmusErgebnis DTO
	 *
	 * @return das Ergebnis
	 */
	public getErgebnis(): BKGymAbiturMarkierungsalgorithmusErgebnis | null {
		const ergebnis: BKGymAbiturMarkierungsalgorithmusErgebnis | null = new BKGymAbiturMarkierungsalgorithmusErgebnis();
		ergebnis.erfolgreich = this.istErfolgreich();
		ergebnis.eingebrachteKurse = this.anzahlEingebrachteKurse();
		ergebnis.gesamtDefizite = this.getDefizite();
		ergebnis.lkDefizite = this.defiziteLK;
		ergebnis.punkteBlockI = this.getPunktzahlBlockI();
		this.erzeugeFehlerlog(ergebnis.fehlerLog);
		ergebnis.log.addAll(this.log);
		ergebnis.markierungen.addAll(this.markiert);
		return ergebnis;
	}

	/**
	 * Extrahiert die Fehlermeldungen aus dem log
	 * Einträge, die mit Hinweis: beginnen, werden übernommen
	 * Einträge, die mit Fehler: beginnen, werden übernommen und zusätzlich die Zeile davor.
	 * 	 *
	 * @param fehlerLog   die Liste, in die die Fehler eingetragen werden
	 */
	private erzeugeFehlerlog(fehlerLog: List<string>): void {
		let vorherigeZeile: string = "";
		for (const zeile of this.log) {
			if (zeile.startsWith("Hinweis:"))
				fehlerLog.add(zeile);
			else
				if (JavaString.contains(zeile, "Fehler:")) {
					fehlerLog.add(vorherigeZeile);
					fehlerLog.add(zeile);
				}
			if (zeile.startsWith("Regel"))
				vorherigeZeile = zeile;
		}
	}

	/**
	 * Sortiert die übergebene Liste an Markierungen, so dass die höchsten Punktwerte am Anfang der Liste stehen.
	 * Es wird auch nach den weiteren Attributen einer BKGymAbiturMarkierungsalgorithmusMarkierung sortiert,
	 * so dass die Reihenfolge immer eindeutig ist.
	 *
	 * @param markierungen   die zu sortierende Liste mit den Markierungen
	 */
	public static sortMarkierungsliste(markierungen: List<BKGymAbiturMarkierungsalgorithmusMarkierung>): void {
		markierungen.sort(BKGymAbiturUtils.comparatorMarkierung);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsVariante';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsVariante'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsVariante>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsVariante');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsVariante(obj: unknown): BKGymAbiturMarkierungsVariante {
	return obj as BKGymAbiturMarkierungsVariante;
}
