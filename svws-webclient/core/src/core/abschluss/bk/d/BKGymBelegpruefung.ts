import { BKGymStundentafelManager } from '../../../../core/abschluss/bk/d/BKGymStundentafelManager';
import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { HashMap } from '../../../../java/util/HashMap';
import { BKGymFachbelegungManager } from '../../../../core/abschluss/bk/d/BKGymFachbelegungManager';
import { ArrayList } from '../../../../java/util/ArrayList';
import { BKGymAbiturFachbelegung } from '../../../../core/data/bk/abi/BKGymAbiturFachbelegung';
import { BeruflichesGymnasiumStundentafel } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafel';
import { BKGymFachbelegungZuStundentafelfachManager } from '../../../../core/abschluss/bk/d/BKGymFachbelegungZuStundentafelfachManager';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { BKGymBelegungsfehlerTyp } from '../../../../core/types/bk/BKGymBelegungsfehlerTyp';
import { BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';
import { BeruflichesGymnasiumStundentafelFach } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelFach';

export class BKGymBelegpruefung extends JavaObject {

	/**
	 * Der Abiturdaten-Manager
	 */
	protected readonly abidatenManager: BKGymAbiturdatenManager;

	/**
	 * Die Belegungsfehler, die für jede Stundentafel bei der Prüfung festgehalten werden.
	 */
	private readonly mapBelegungsfehler: HashMap<BeruflichesGymnasiumStundentafel, List<BKGymBelegungsfehler>> = new HashMap<BeruflichesGymnasiumStundentafel, List<BKGymBelegungsfehler>>();

	/**
	 * Die Liste von Belegungsfehlern der am besten passenden Stundentafel
	 */
	private besteFehlerliste: List<BKGymBelegungsfehler> = new ArrayList<BKGymBelegungsfehler>();

	/**
	 * Flag ob neue Fehler hinzugekommen sind
	 */
	private dirty: boolean = false;


	/**
	 * Erzeugt eine neue Belegprüfung mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public constructor(manager: BKGymAbiturdatenManager) {
		super();
		this.abidatenManager = manager;
	}

	/**
	 * Fügt einen Belegungsfehler zu der Belegprüfung hinzu.
	 *
	 * @param tafel       die Stundentafel
	 * @param fehlerTyp   der hinzuzufügende Belegungsfehlertyp
	 * @param params      die Parameter für den Belegungsfehlertyp
	 *
	 * @return true, falls ein Fehler vorliegt false, wenn nur ein Hinweis ausgegeben wurde.
	 */
	private addFehler(tafel: BeruflichesGymnasiumStundentafel, fehlerTyp: BKGymBelegungsfehlerTyp, ...params: Array<unknown>): boolean {
		const fehler: BKGymBelegungsfehler = new BKGymBelegungsfehler(fehlerTyp, params);
		const fehlerliste: List<BKGymBelegungsfehler> | null = this.mapBelegungsfehler.get(tafel);
		if (fehlerliste !== null && !fehlerliste.contains(fehler)) {
			fehlerliste.add(fehler);
			this.dirty = true;
		}
		return fehler.istFehler();
	}

	/**
	 * Ermittelt die Stundentafel mit den wenigsten Fehlern und gibt die zugehörigen Belegungsfehler aus
	 */
	private ermittleBesteTafel(): void {
		if (this.dirty) {
			let minFehlerZahl: number = JavaInteger.MAX_VALUE;
			for (const fehlerliste of this.mapBelegungsfehler.values()) {
				let fehlerZahl: number = 0;
				for (const fehler of fehlerliste)
					fehlerZahl += fehler.wert;
				if (fehlerZahl < minFehlerZahl) {
					minFehlerZahl = fehlerZahl;
					this.besteFehlerliste = fehlerliste;
				}
			}
		}
		this.dirty = false;
	}

	/**
	 * Ermittelt die Stundentafel mit den wenigsten Fehlern und gibt die zugehörigen Belegungsfehler aus
	 *
	 * @return die Belegungsfehler der Stundentafel
	 */
	public getBelegungsfehler(): List<BKGymBelegungsfehler> {
		this.ermittleBesteTafel();
		return this.besteFehlerliste;
	}

	/**
	 * Gibt zurück, ob mindestens eine Stundentafel existiert, die keine "echten" Belegungsfehler hat. Warnungen und Hinweise werden toleriert.
	 *
	 * @return true, wenn kein "echter" Belegungsfehler vorliegt, und ansonsten false.
	 */
	public istErfolgreich(): boolean {
		for (const fehler of this.getBelegungsfehler())
			if (!fehler.istInfo() && fehler.wert > 0)
				return false;
		return true;
	}

	/**
	 * Die Methode wird zur Durchführung der Belegprüfung aufgerufen.
	 *
	 * Sie führt zuerst die allgemeinen Prüfungen aus, die für alle Anlagen des beruflichen Gymnasiums identisch sind.
	 */
	public pruefe(): void {
		for (const tafel of this.abidatenManager.getStundentafelManager().getStundentafeln()) {
			this.mapBelegungsfehler.put(tafel, new ArrayList<BKGymBelegungsfehler>());
			this.nichtBelegteHalbjahreHinweis(tafel);
			this.pruefeEineTafel(tafel);
		}
	}

	/**
	 * Führt die Belegprüfung für eine Stundentafel durch.
	 *
	 * @param tafel   die zu überprüfende Stundentafel
	 */
	private pruefeEineTafel(tafel: BeruflichesGymnasiumStundentafel): void {
		let zweiteFremdspracheBelegt: boolean = true;
		let religionVollbelegt: boolean = false;
		const fbManager: BKGymFachbelegungManager = this.abidatenManager.getFachbelegungManager();
		const fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager = fbManager.newFachbelegungZuStundentafelfachManager(BKGymStundentafelManager.getMaximalSortierung(tafel));
		this.pruefeAbiGrundkurse(tafel);
		for (const fach of tafel.faecher) {
			if (BKGymStundentafelManager.istZweiteFremdsprache(fach.fachbezeichnung))
				zweiteFremdspracheBelegt = this.pruefeBelegungZweiteFremdsprache(fb2TafelManager, tafel, fach);
			else
				if (BKGymStundentafelManager.istNeueFremdsprache(fach.fachbezeichnung))
					this.pruefeBelegungNeueFremdsprache(fb2TafelManager, tafel, fach);
				else
					if (BKGymStundentafelManager.istReligion(fach.fachbezeichnung))
						religionVollbelegt = BKGymBelegpruefung.pruefeBelegungReligion(fb2TafelManager, fach);
					else
						if (BKGymStundentafelManager.istWahlfach(fach.fachbezeichnung)) {
							if (!zweiteFremdspracheBelegt)
								this.pruefeBelegungFremdsprachenErsatzfach(fb2TafelManager, tafel);
							if (!religionVollbelegt)
								this.pruefeBelegungReligionErsatzfach(fb2TafelManager, tafel);
							this.pruefeBelegungWahlfach(fb2TafelManager, tafel, fach);
						} else
							this.pruefeBelegungFach(fb2TafelManager, tafel, fach);
		}
	}

	/**
	 * Prüfe auf korrekte Belegung des 3. und 4. Abiturfachs
	 *
	 * @param tafel   die zu prüfende Stundentafel
	 */
	private pruefeAbiGrundkurse(tafel: BeruflichesGymnasiumStundentafel): void {
		const fbManager: BKGymFachbelegungManager = this.abidatenManager.getFachbelegungManager();
		const ab3: BKGymAbiturFachbelegung | null = fbManager.getAbiFachbelegung(GostAbiturFach.AB3);
		if (ab3 === null)
			this.addFehler(tafel, BKGymBelegungsfehlerTyp.AB_3);
		const ab4: BKGymAbiturFachbelegung | null = fbManager.getAbiFachbelegung(GostAbiturFach.AB4);
		if (ab4 === null)
			this.addFehler(tafel, BKGymBelegungsfehlerTyp.AB_4);
		const ab3Bezeichnung: string | null = ab3 === null ? null : this.abidatenManager.getFaecherManager().getBezeichnungByFachID(ab3.fachID);
		const ab4Bezeichnung: string | null = ab4 === null ? null : this.abidatenManager.getFaecherManager().getBezeichnungByFachID(ab4.fachID);
		if ((ab3Bezeichnung !== null) && (ab4Bezeichnung !== null) && !this.abidatenManager.getStundentafelManager().pruefeAbiGrundkurswahl(tafel, ab3Bezeichnung, ab4Bezeichnung))
			this.addFehler(tafel, BKGymBelegungsfehlerTyp.AB_5, ab3Bezeichnung, ab4Bezeichnung, this.abidatenManager.getGliederung().name(), this.abidatenManager.getFachklassenschluessel());
	}

	/**
	 * Gibt Hinweise ins Log aus, wenn Halbjahre nicht bewertet wurden.
	 *
	 * @param tafel   die zu prüfende Stundentafel
	 */
	private nichtBelegteHalbjahreHinweis(tafel: BeruflichesGymnasiumStundentafel): void {
		for (const hj of GostHalbjahr.values())
			if (!this.abidatenManager.istBewertet(hj))
				this.addFehler(tafel, BKGymBelegungsfehlerTyp.HJ_1_INFO, hj.kuerzel);
	}

	/**
	 * Führt die Belegung für die zweite Fremdsprache durch.
	 * Wenn keine zweite Fremdsprache belegt werden kann, wird das als false mitgeteilt, damit
	 * später geprüft wird, ob ein Ersatzfach belegt wurde. Das muss direkt vor der Prüfung des Wahlfachs durchgeführt werden
	 * und nach der Prüfung eines Ersatzfaches für Religion, da hier die möglichen Ersatzfächer eingeschränkt sind.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel           die zu überprüfende Stundentafel
	 * @param fach            das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn die Belegung erfolgreich war, sonst false
	 */
	private pruefeBelegungZweiteFremdsprache(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach): boolean {
		const bezeichnerFremdsprache: string | null = this.abidatenManager.getFachbelegungManager().getZweiteFremdspracheBezeichnung();
		if (bezeichnerFremdsprache === null)
			return false;
		const fachFremdsprache: BeruflichesGymnasiumStundentafelFach = BKGymBelegpruefung.copyStundentafelFach(fach, bezeichnerFremdsprache);
		return this.pruefeBelegungFach(fb2TafelManager, tafel, fachFremdsprache);
	}

	/**
	 * Führt die Belegung für die neue Fremdsprache durch.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel   die zu überprüfende Stundentafel
	 * @param fach    das zu prüfende Fach der Stundentafel
	 */
	private pruefeBelegungNeueFremdsprache(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach): void {
		const bezeichnerFremdsprache: string | null = this.abidatenManager.getFachbelegungManager().getZweiteFremdspracheBezeichnung();
		if (bezeichnerFremdsprache === null) {
			this.addFehler(tafel, BKGymBelegungsfehlerTyp.ST_4, fach.fachbezeichnung);
			return;
		}
		const fachFremdsprache: BeruflichesGymnasiumStundentafelFach = BKGymBelegpruefung.copyStundentafelFach(fach, bezeichnerFremdsprache);
		this.pruefeBelegungFach(fb2TafelManager, tafel, fachFremdsprache);
	}

	/**
	 * Führt die Belegung für das Ersatzfach der zweiten Fremdsprache durch. Dies ist beliebig muss aberfür Religion durch.
	 * für alle vier Halbjahre belegt werden.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel      die zu überprüfende Stundentafel
	 */
	private pruefeBelegungFremdsprachenErsatzfach(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel): void {
		const stManager: BKGymStundentafelManager = this.abidatenManager.getStundentafelManager();
		const fachZweiteFremdsprache: BeruflichesGymnasiumStundentafelFach | null = stManager.getFachByTafelAndBezeichnung(tafel, BKGymStundentafelManager.ZWEITE_FREMDSPRACHE);
		if (fachZweiteFremdsprache === null)
			throw new DeveloperNotificationException("Das Fach \"Zweite Fremdsprache\" fehlt in der Stundentafel.")
		for (const ersatzfachBezeichnung of fb2TafelManager.getFachbezeichnungenFreierBelegungen()) {
			const ersatzfach: BeruflichesGymnasiumStundentafelFach = BKGymBelegpruefung.copyStundentafelFach(fachZweiteFremdsprache, ersatzfachBezeichnung);
			if (fb2TafelManager.belegeErsatzfachVomEndeHer(ersatzfach))
				break;
		}
		this.pruefeStundenumfang(fb2TafelManager, tafel, fachZweiteFremdsprache);
	}

	/**
	 * Führt die Belegung für das Fach Religion durch.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param fach    das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn die Belegung erfolgreich war, sonst false
	 */
	private static pruefeBelegungReligion(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, fach: BeruflichesGymnasiumStundentafelFach): boolean {
		fb2TafelManager.belegeFach(fach);
		return fb2TafelManager.istVollbelegt(fach);
	}

	/**
	 * Führt die Belegung für das Ersatzfach für Religion durch. Dies ist beliebig muss aber
	 * für alle vier Halbjahre belegt werden.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel      die zu überprüfende Stundentafel
	 */
	private pruefeBelegungReligionErsatzfach(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel): void {
		const stManager: BKGymStundentafelManager = this.abidatenManager.getStundentafelManager();
		const fachReligion: BeruflichesGymnasiumStundentafelFach | null = stManager.getFachByTafelAndBezeichnung(tafel, BKGymStundentafelManager.RELIGION);
		if (fachReligion === null)
			throw new DeveloperNotificationException("Das Fach " + BKGymStundentafelManager.RELIGION + " fehlt in der Stundentafel.")
		for (const ersatzfachBezeichnung of BKGymStundentafelManager.ERSATZ_FUER_RELIGION) {
			const ersatzfach: BeruflichesGymnasiumStundentafelFach = BKGymBelegpruefung.copyStundentafelFach(fachReligion, ersatzfachBezeichnung);
			if (fb2TafelManager.belegeErsatzfach(ersatzfach))
				break;
		}
		this.pruefeStundenumfang(fb2TafelManager, tafel, fachReligion);
	}

	/**
	 * Führt die Belegung für das Wahlfach durch.
	 *
	 * @param fb2TafelManager   der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel             die zu überprüfende Stundentafel
	 * @param fach              das zu prüfende Fach der Stundentafel
	 */
	private pruefeBelegungWahlfach(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach): void {
		for (const hj of GostHalbjahr.getQualifikationsphase())
			fb2TafelManager.belegeBeliebigesFachFuerHalbjahr(hj, fach);
		this.pruefeStundenumfang(fb2TafelManager, tafel, fach);
	}

	/**
	 * Führt die Belegung des Fachs aus dem Pool der noch nicht verwendeten Belegungen durch.
	 * Dabei wird die Stundenumfang überprüft und Fehler werden in der Tafel eingetragen.
	 *
	 * @param fb2TafelManager   der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel             die zu überprüfende Stundentafel
	 * @param fach              das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn die Belegung erfolgreich war, sonst false
	 */
	private pruefeBelegungFach(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach): boolean {
		fb2TafelManager.belegeFach(fach);
		const resultStundenumfang: boolean = this.pruefeStundenumfang(fb2TafelManager, tafel, fach);
		const resultKursart: boolean = this.pruefeKursart(tafel, fach);
		const resultSchriftlich: boolean = this.pruefeSchriftlich(fb2TafelManager, tafel, fach);
		return resultStundenumfang && resultKursart && resultSchriftlich;
	}

	/**
	 * Prüft, ob der Stundenumfang einer Stundentafelposition erfüllt wird.
	 *
	 * @param fb2TafelManager   der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel             die zu überprüfende Stundentafel
	 * @param fach              das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn der Stundenumfang ausreichend ist, sonst false
	 */
	private pruefeStundenumfang(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach): boolean {
		let success: boolean = true;
		let summeTafel: number = 0;
		let summeBelegung: number = 0;
		let unterbelegung: boolean = false;
		for (const hj of GostHalbjahr.getQualifikationsphase()) {
			const belegteStunden: number = fb2TafelManager.getBelegteStundenByHalbjahrAndFach(hj, fach);
			summeTafel += fach.stundenumfang[hj.id];
			summeBelegung += belegteStunden;
			if (belegteStunden === -1) {
				if (fach.stundenumfang[hj.id] > 0) {
					success = !this.addFehler(tafel, BKGymBelegungsfehlerTyp.ST_2, fach.fachbezeichnung, hj.kuerzel) && success;
					unterbelegung = true;
				}
			} else
				if (belegteStunden < fach.stundenumfang[hj.id]) {
					unterbelegung = true;
					if (belegteStunden === 0)
						success = !this.addFehler(tafel, BKGymBelegungsfehlerTyp.ST_6, fach.fachbezeichnung, hj.kuerzel) && success;
				}
		}
		if (summeTafel > summeBelegung)
			return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.ST_3, fach.fachbezeichnung) && success;
		if (unterbelegung)
			success = !this.addFehler(tafel, BKGymBelegungsfehlerTyp.ST_5_INFO, fach.fachbezeichnung) && success;
		return success;
	}

	/**
	 * Prüft d, ob das Fach als Leistungskurs belegt wurde, wenn dies in der Stundentafel gefordert ist.
	 *
	 * @param tafel             die zu überprüfende Stundentafel
	 * @param fach              das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn in Stundentafel und Belegung die Kursarten zueinander passen, sonst false
	 */
	private pruefeKursart(tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach): boolean {
		const fachBelegung: BKGymAbiturFachbelegung | null = this.abidatenManager.getFachbelegungManager().getFachbelegungByBezeichnung(fach.fachbezeichnung);
		if (fachBelegung === null)
			return !BKGymStundentafelManager.brauchtBelegungInQPhase(fach);
		const kursartBelegung: string = fachBelegung.letzteKursart === null ? "" : fachBelegung.letzteKursart;
		let lkNummerTafel: number = 0;
		let lkNummerBelegung: number = 0;
		if (fach.abifach !== null)
			lkNummerTafel = fach.abifach.valueOf();
		if (fachBelegung.abiturFach !== null)
			lkNummerBelegung = fachBelegung.abiturFach.valueOf();
		switch (fach.kursart) {
			case "GK": {
				if (JavaObject.equalsTranspiler("LK", (kursartBelegung)))
					return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.LK_3, fach.fachbezeichnung);
				break;
			}
			case "LK": {
				if (!JavaObject.equalsTranspiler("LK", (kursartBelegung)) || (lkNummerTafel !== lkNummerBelegung))
					return !this.addFehler(tafel, lkNummerTafel === 1 ? BKGymBelegungsfehlerTyp.LK_1 : BKGymBelegungsfehlerTyp.LK_2, fach.fachbezeichnung);
				break;
			}
			default: {
				throw new DeveloperNotificationException("Ungültige Kursart '" + fach.kursart + "' in der Stundentafel.")
				break;
			}
		}
		return true;
	}

	/**
	 * Prüft, ob die Schriftlichkeit der Fächer korrekt erfüllt ist.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel         die Stundentafel der Anlage
	 * @param fach          das zu prüfende Fach aus der Stundentafel
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeSchriftlich(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach): boolean {
		let success: boolean;
		const fachBelegung: BKGymAbiturFachbelegung | null = this.abidatenManager.getFachbelegungManager().getFachbelegungByBezeichnung(fach.fachbezeichnung);
		if (fachBelegung === null)
			return true;
		success = this.pruefeSchriftlichEF(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.EF1);
		success = this.pruefeSchriftlichEF(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.EF2) && success;
		success = this.pruefeSchriftlichQ1(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.Q11) && success;
		success = this.pruefeSchriftlichQ1(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.Q12) && success;
		success = this.pruefeSchriftlichQ2(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.Q21) && success;
		success = this.pruefeSchriftlichQ2(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.Q22) && success;
		return success;
	}

	/**
	 * In der EF muss in mindestens vier Fächern, in den LK-Fächern, Deutsch, Mathematik und Fremdsprachen in jedem Fall
	 * Da es die LK-Kombination Mathe-Deutsch nicht gibt, sind mindestens vier Fächer gegeben, wenn die obligatorischen
	 * Klausurfächer geprüft sind.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel           die Stundentafel der Anlage
	 * @param fachBelegung    die Fachbelegung zur Halbjahresbelegung
	 * @param fach            das zu prüfende Fach aus der Stundentafel
	 * @param hj              das Oberstufenhalbjahr
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeSchriftlichEF(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach, fachBelegung: BKGymAbiturFachbelegung, hj: GostHalbjahr): boolean {
		if (fb2TafelManager.getSchriftlichBelegt(hj, fach))
			return true;
		if (JavaObject.equalsTranspiler("Deutsch", (fach.fachbezeichnung)) || JavaObject.equalsTranspiler("Mathematik", (fach.fachbezeichnung)))
			return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.KL_1_INFO, fach.fachbezeichnung, hj.kuerzel);
		if ((fachBelegung.abiturFach !== null) && (fachBelegung.abiturFach <= 2))
			return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.KL_2, fach.fachbezeichnung, hj.kuerzel);
		if (this.abidatenManager.istFremdsprachenbelegung(fachBelegung))
			return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.KL_3_INFO, fach.fachbezeichnung, hj.kuerzel);
		return true;
	}

	/**
	 * In der Q1 müssen allen Abiturfächer schriftlich belegt sein. Deutsch, Mathematik,
	 * Fremdsprachen und die Fächer der Berufsabschlussprüfung in jedem Fall
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel           die Stundentafel der Anlage
	 * @param fachBelegung    die Fachbelegung zur Halbjahresbelegung
	 * @param fach            das zu prüfende Fach aus der Stundentafel
	 * @param hj              das Oberstufenhalbjahr
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeSchriftlichQ1(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach, fachBelegung: BKGymAbiturFachbelegung, hj: GostHalbjahr): boolean {
		if (fb2TafelManager.getSchriftlichBelegt(hj, fach))
			return true;
		if (JavaObject.equalsTranspiler("Deutsch", (fach.fachbezeichnung)) || JavaObject.equalsTranspiler("Mathematik", (fach.fachbezeichnung)))
			return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.KL_1, fach.fachbezeichnung, hj.kuerzel);
		if ((fachBelegung.abiturFach !== null) && (fachBelegung.abiturFach <= 4))
			return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.KL_4, fach.fachbezeichnung, hj.kuerzel);
		if (this.abidatenManager.istFremdsprachenbelegung(fachBelegung))
			return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.KL_3, fach.fachbezeichnung, hj.kuerzel);
		return true;
	}

	/**
	 * In der Q2 müssen das erste bis dritte Abiturfach schriftlich belegt sein. Deutsch, Mathematik,
	 * Nur in der Q21 auch die Fremdsprachen
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel           die Stundentafel der Anlage
	 * @param fachBelegung    die Fachbelegung zur Halbjahresbelegung
	 * @param fach            das zu prüfende Fach aus der Stundentafel
	 * @param hj              das Oberstufenhalbjahr
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeSchriftlichQ2(fb2TafelManager: BKGymFachbelegungZuStundentafelfachManager, tafel: BeruflichesGymnasiumStundentafel, fach: BeruflichesGymnasiumStundentafelFach, fachBelegung: BKGymAbiturFachbelegung, hj: GostHalbjahr): boolean {
		if (fb2TafelManager.getSchriftlichBelegt(hj, fach))
			return true;
		if ((fachBelegung.abiturFach !== null) && (fachBelegung.abiturFach <= 3))
			return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.KL_4, fach.fachbezeichnung, hj.kuerzel);
		if ((GostHalbjahr.Q21 as unknown === hj as unknown) && this.abidatenManager.istFremdsprachenbelegung(fachBelegung))
			return !this.addFehler(tafel, BKGymBelegungsfehlerTyp.KL_3, fach.fachbezeichnung, hj.kuerzel);
		return true;
	}

	/**
	 * Hilfsmethode. Erstellt eine Kopie des Stundentafelfaches mit einer neuen Fachbezeichnung.
	 *
	 * @param fach              das zu kopierende Fach
	 * @param fachbezeichnung   die neue Fachbezeichnung
	 *
	 * @return die Kopie des Faches mit der neuen Fachbezeichnung
	 */
	private static copyStundentafelFach(fach: BeruflichesGymnasiumStundentafelFach, fachbezeichnung: string): BeruflichesGymnasiumStundentafelFach {
		const copy: BeruflichesGymnasiumStundentafelFach = new BeruflichesGymnasiumStundentafelFach();
		copy.fachbezeichnung = fachbezeichnung;
		copy.sortierung = fach.sortierung;
		copy.abifach = fach.abifach;
		copy.kursart = fach.kursart;
		copy.stundenumfang = fach.stundenumfang;
		copy.zeugnisbereich = fach.zeugnisbereich;
		return copy;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung'].includes(name);
	}

	public static readonly class = new Class<BKGymBelegpruefung>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefung(obj: unknown): BKGymBelegpruefung {
	return obj as BKGymBelegpruefung;
}
