import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap2D } from '../../../../core/adt/map/HashMap2D';
import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { BKGymAbiturFachbelegung } from '../../../../core/abschluss/bk/d/BKGymAbiturFachbelegung';
import { BKGymAbiturFachbelegungHalbjahr } from '../../../../core/abschluss/bk/d/BKGymAbiturFachbelegungHalbjahr';
import { BeruflichesGymnasiumStundentafel } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafel';
import { JavaString } from '../../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { BKGymBelegungsfehlerTyp } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehlerTyp';
import { BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import type { JavaIterator } from '../../../../java/util/JavaIterator';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import type { JavaMap } from '../../../../java/util/JavaMap';
import { BeruflichesGymnasiumStundentafelFach } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelFach';

export class BKGymBelegpruefung extends JavaObject {

	/**
	 * Die Abiturdaten-Manager
	 */
	protected readonly manager: BKGymAbiturdatenManager;

	/**
	 * Die Stundentafeln laut APO-BK der Anlage
	 */
	readonly stundentafeln: List<BeruflichesGymnasiumStundentafel>;

	/**
	 * Eine HashMap2D für den schnellen Zugriff auf die Fächer der Stundentafeln anhand der Tafel und der Fachbezeichnung
	 */
	private readonly mapStundentafelFachByTafelAndFachbezeichnung: HashMap2D<BeruflichesGymnasiumStundentafel, string, BeruflichesGymnasiumStundentafelFach> = new HashMap2D<BeruflichesGymnasiumStundentafel, string, BeruflichesGymnasiumStundentafelFach>();

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
		this.manager = manager;
		this.stundentafeln = manager.getStundentafeln();
		this.init();
	}

	/**
	 * Initialisiert die Maps für schnelle Zugriffe
	 */
	private init(): void {
		for (const tafel of this.stundentafeln) {
			for (const fach of tafel.faecher) {
				this.mapStundentafelFachByTafelAndFachbezeichnung.put(tafel, fach.fachbezeichnung, fach);
			}
		}
	}

	/**
	 * Fügt einen Belegungsfehler zu der Belegprüfung hinzu. Diese Methode wird von den Sub-Klassen
	 * aufgerufen, wenn dort ein Belegungsfehler erkannt wird.
	 *
	 * @param tafel    die Stundentafel
	 * @param fehler   der hinzuzufügende Belegungsfehler
	 *
	 * @return true, falls ein Fehler vorliegt false, wenn nur ein Hinweis ausgegeben wurde.
	 */
	protected addFehler(tafel: BeruflichesGymnasiumStundentafel | null, fehler: BKGymBelegungsfehler): boolean {
		if (tafel !== null) {
			const fehlerliste: List<BKGymBelegungsfehler> | null = this.mapBelegungsfehler.get(tafel);
			if (fehlerliste !== null && !fehlerliste.contains(fehler)) {
				fehlerliste.add(fehler);
				this.dirty = true;
			}
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
			if (!fehler.istInfo())
				return false;
		return true;
	}

	/**
	 * Die Methode wird zur Durchführung der Belegprüfung aufgerufen.
	 *
	 * Sie führt zuerst die allgemeinen Prüfungen aus, die für alle Anlagen des beruflichen Gymnasiums identisch sind.
	 */
	public pruefe(): void {
		for (const tafel of this.stundentafeln) {
			this.mapBelegungsfehler.put(tafel, new ArrayList<BKGymBelegungsfehler>());
			const mapFaecherByIndex: JavaMap<number, List<BeruflichesGymnasiumStundentafelFach>> = this.manager.getMapFaecherFromTafelByIndex(tafel);
			const mapBelegungByFach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> = this.manager.getMapBelegungenForTafelByFach(tafel);
			this.bewegeBelegungZuWahlfach(tafel, mapFaecherByIndex, mapBelegungByFach);
			this.allgemeinePruefungen(tafel, mapFaecherByIndex, mapBelegungByFach);
		}
	}

	/**
	 * Diese Methode identifiziert für Positionen einer Stundentafel, die Wahlmöglichkeiten vorsieht,
	 * ob mehrere Belegungen vorliegen, von denen nur eine der Position zugewiesen wird und die anderen
	 * dem Wahlfach zugeschlagen wird.
	 * Es sollte nur ein Fach pro Position vorhanden sein, dass in der Gruppe Berufsbezogen bzw. Berufsübergreifend ist.
	 * Die anderen Belegungen müssten Differenzierung zugewiesen sein.
	 * Dafür wird ermittelt welche Belegung den Vorgaben der Position entspricht. Die erste wird behalten, die
	 * anderen werden nach Wahlfach verschoben.
	 *
	 * @param tafel               die zu überprüfende Stundentafel
	 * @param mapFaecherByIndex   die Map mit den Fächern der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 */
	private bewegeBelegungZuWahlfach(tafel: BeruflichesGymnasiumStundentafel, mapFaecherByIndex: JavaMap<number, List<BeruflichesGymnasiumStundentafelFach>>, mapBelegungByFach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>>): void {
		const wahlBelegungen: List<BKGymAbiturFachbelegung> = this.getBelegungenWahlfach(tafel, mapBelegungByFach);
		for (const faecher of mapFaecherByIndex.values()) {
			if (faecher === null)
				continue;
			if (BKGymBelegpruefung.hatStundentafelpositionWahloption(faecher, mapBelegungByFach))
				this.bewegeWahlfach(faecher, mapBelegungByFach, wahlBelegungen);
		}
	}

	/**
	 * Prüft, ob die Stundentafel eine Wahloption hat anhand der gegebenen Fächerliste der Position
	 *
	 * @param faecher             die Liste der Fächer einer Position der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 *
	 * @return true, wenn ein Wahlfach in der Position der Stundentafel vorhanden ist, sonst false
	 */
	private static hatStundentafelpositionWahloption(faecher: List<BeruflichesGymnasiumStundentafelFach>, mapBelegungByFach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>>): boolean {
		if (faecher.size() === 1)
			return false;
		const iter: JavaIterator<BeruflichesGymnasiumStundentafelFach> | null = faecher.iterator();
		while (iter.hasNext()) {
			const tafelFach: BeruflichesGymnasiumStundentafelFach | null = iter.next();
			const belegungen: List<BKGymAbiturFachbelegung> | null = mapBelegungByFach.get(tafelFach);
			if ((belegungen === null) || (belegungen.isEmpty()))
				iter.remove();
		}
		return faecher.size() >= 2;
	}

	/**
	 * Bei Wahlmöglichkeiten für ein Pflichtfach wird bei Belegung mehrerer Fächer das erstbeste behalten und
	 * die anderen Belegungen zum Wahlfach übertragen
	 *
	 * @param faecher             die Liste der Fächer einer Position der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 * @param wahlBelegungen      die Liste der Belegungen des Wahlfachs
	 */
	private bewegeWahlfach(faecher: List<BeruflichesGymnasiumStundentafelFach>, mapBelegungByFach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>>, wahlBelegungen: List<BKGymAbiturFachbelegung>): void {
		if (!this.bewegeFortgefuehrteFS(faecher, mapBelegungByFach, wahlBelegungen)) {
			const iter: JavaIterator<BeruflichesGymnasiumStundentafelFach> | null = faecher.iterator();
			let gefunden: boolean = false;
			while (iter.hasNext()) {
				const tafelFach: BeruflichesGymnasiumStundentafelFach | null = iter.next();
				if (gefunden) {
					wahlBelegungen.addAll(mapBelegungByFach.get(tafelFach));
					iter.remove();
				} else {
					gefunden = this.istPflichtFach(tafelFach, mapBelegungByFach, !iter.hasNext());
					if (!gefunden) {
						wahlBelegungen.addAll(mapBelegungByFach.get(tafelFach));
						iter.remove();
					}
				}
			}
		}
	}

	/**
	 * Wird nur aufgerufen, wenn fortgeführte und neu einsetzende Fremdsprachen belegt sind.
	 * Die fortgeführte Fremdsprache wird zum Wahlfach verschoben
	 *
	 * @param faecher             die Liste der Fächer einer Position der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 * @param wahlBelegungen      die Liste der Belegungen für das Wahlfach
	 *
	 * @return true, wenn die Fremdsprachen behandelt wurden, sonst false
	 */
	private bewegeFortgefuehrteFS(faecher: List<BeruflichesGymnasiumStundentafelFach>, mapBelegungByFach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>>, wahlBelegungen: List<BKGymAbiturFachbelegung>): boolean {
		const tafelFach: BeruflichesGymnasiumStundentafelFach | null = faecher.getFirst();
		if ((tafelFach !== null) && (this.manager.istZweiteFremdsprache(tafelFach.fachbezeichnung) || this.manager.istNeueFremdsprache(tafelFach.fachbezeichnung))) {
			const iter: JavaIterator<BeruflichesGymnasiumStundentafelFach> | null = faecher.iterator();
			while (iter.hasNext()) {
				const tf: BeruflichesGymnasiumStundentafelFach | null = iter.next();
				if (this.manager.istZweiteFremdsprache(tf.fachbezeichnung)) {
					wahlBelegungen.addAll(mapBelegungByFach.get(tf));
					iter.remove();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Gilt für die Positionen der Stundentafel, die Wahlmöglichkeiten vorsehen wie Physik/Chemie/Biologie.
	 * Hier wird geprüft, ob die Belegung in Ordnung ist.
	 *
	 * @param tafelFach           ein Fach der Stundentafel
	 * @param mapBelegungByFach   die Map mit den Fächern der Belegungen
	 * @param letztesFach         ob es das letzte Fach der Position in der Stundentafel ist.
	 *
	 * @return true, wenn als Pflichtfach erkannt, sonst false
	 */
	private istPflichtFach(tafelFach: BeruflichesGymnasiumStundentafelFach | null, mapBelegungByFach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>>, letztesFach: boolean): boolean {
		if (tafelFach === null)
			return false;
		const belegungen: List<BKGymAbiturFachbelegung> | null = mapBelegungByFach.get(tafelFach);
		if ((belegungen === null) || (belegungen.size() !== 1))
			return letztesFach;
		const belegung: BKGymAbiturFachbelegung | null = belegungen.getFirst();
		if (belegung !== null)
			return this.pruefeBelegung(null, tafelFach, belegung);
		throw new DeveloperNotificationException("In der Belegungsprüfung ist ein interner Fehler aufgetreten: Die Belegung für ein Fach ist nicht vorhanden.")
	}

	/**
	 * allgemeinePruefungen führt die Belegprüfungen durch, die für alle Anlagen des beruflichen Gymnasiums identisch ausgeführt werden können.
	 * Aufgetretene Fehler werden in die Map mapBelegungsfehler eingetragen.
	 *
	 * @param tafel                    die zu überprüfende Stundentafel
	 * @param mapFaecherByIndex        die Map mit den Fächern der Stundentafel
	 * @param mapBelegungByTafelfach   die Map mit den Fächern der Belegungen
	 */
	private allgemeinePruefungen(tafel: BeruflichesGymnasiumStundentafel, mapFaecherByIndex: JavaMap<number, List<BeruflichesGymnasiumStundentafelFach>>, mapBelegungByTafelfach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>>): void {
		this.pruefeLKs(tafel);
		this.pruefeAbiGrundkurse(tafel);
		for (const faecher of mapFaecherByIndex.values()) {
			if (faecher !== null)
				this.allgemeinePruefungenTafelfach(tafel, faecher, mapBelegungByTafelfach);
		}
	}

	/**
	 * Führt für eine Position der Stundentafel die Prüfung der Belegung durch.
	 *
	 * @param tafel                    die zu überprüfende Stundentafel
	 * @param faecher                  die Liste der Fächer einer Position der Stundentafel
	 * @param mapBelegungByTafelfach   die Map mit den Fächern der Belegungen
	 */
	private allgemeinePruefungenTafelfach(tafel: BeruflichesGymnasiumStundentafel, faecher: List<BeruflichesGymnasiumStundentafelFach>, mapBelegungByTafelfach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>>): void {
		if (faecher.size() > 1)
			throw new DeveloperNotificationException("In der Belegungsprüfung ist ein interner Fehler aufgetreten: Für eine Position der Stundentafel gibt es mehr als einen Eintrag.")
		if (faecher.isEmpty())
			throw new DeveloperNotificationException("In der Belegungsprüfung ist ein interner Fehler aufgetreten: Für eine Position der Stundentafel gibt keinen Eintrag.")
		const tafelFach: BeruflichesGymnasiumStundentafelFach | null = faecher.getFirst();
		if (tafelFach === null)
			return;
		const belegungen: List<BKGymAbiturFachbelegung> | null = mapBelegungByTafelfach.get(tafelFach);
		if ((belegungen === null) || belegungen.isEmpty()) {
			this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_4, tafelFach.fachbezeichnung));
			return;
		}
		if (this.manager.istWahlfach(tafelFach.fachbezeichnung))
			this.pruefeBelegungWahlfach(tafel, tafelFach, belegungen);
		else {
			if (belegungen.size() > 1)
				this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_1, tafelFach.fachbezeichnung));
			else
				this.pruefeBelegung(tafel, tafelFach, belegungen.getFirst());
		}
	}

	/**
	 * Führt die Prüfungen für eine Belegung durch
	 *
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeBelegung(tafel: BeruflichesGymnasiumStundentafel | null, fachTafel: BeruflichesGymnasiumStundentafelFach, fachBelegung: BKGymAbiturFachbelegung): boolean {
		const successStundenumfang: boolean = this.pruefeStundenumfang(tafel, fachTafel, fachBelegung);
		const successSchriftlichkeit: boolean = this.pruefeSchriftlich(tafel, fachTafel, fachBelegung);
		return successStundenumfang && successSchriftlichkeit;
	}

	/**
	 * Schlägt die Belegungen für das Wahlfach nach
	 *
	 * @param tafel                    die betreffende Stundentafel
	 * @param mapBelegungByTafelfach   das zugehörige Mapping der Belegungen auf die Fächer der Stundentafel
	 *
	 * @return Die Liste der Belegungen für das Wahlfach
	 */
	private getBelegungenWahlfach(tafel: BeruflichesGymnasiumStundentafel, mapBelegungByTafelfach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>>): List<BKGymAbiturFachbelegung> {
		const tafelWahlfach: BeruflichesGymnasiumStundentafelFach | null = this.mapStundentafelFachByTafelAndFachbezeichnung.getOrNull(tafel, this.manager.wahlfach);
		let wahlfachBelegungen: List<BKGymAbiturFachbelegung> | null = null;
		if (tafelWahlfach !== null)
			wahlfachBelegungen = mapBelegungByTafelfach.computeIfAbsent(tafelWahlfach, { apply: (k: BeruflichesGymnasiumStundentafelFach | null) => new ArrayList() });
		if (wahlfachBelegungen !== null)
			return wahlfachBelegungen;
		return new ArrayList<BKGymAbiturFachbelegung>();
	}

	/**
	 * Führt die Belegungsprüfung für das Wahlfach aus.
	 * Hier muss nur die Summe der Wochenstunden pro Halbjahr erfüllt sein.
	 *
	 * @param tafel        die Stundentafel der Anlage
	 * @param fachTafel    das zu prüfende Fach aus der Stundentafel
	 * @param belegungen   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeBelegungWahlfach(tafel: BeruflichesGymnasiumStundentafel | null, fachTafel: BeruflichesGymnasiumStundentafelFach, belegungen: List<BKGymAbiturFachbelegung>): boolean {
		let success: boolean = true;
		const summeWS: Array<number> = Array(GostHalbjahr.maxHalbjahre).fill(0);
		for (const fachBelegung of belegungen)
			for (const hj of GostHalbjahr.getQualifikationsphase()) {
				const belegung: BKGymAbiturFachbelegungHalbjahr | null = fachBelegung.belegungen[hj.id];
				if (belegung !== null)
					summeWS[hj.id] += belegung.wochenstunden;
			}
		for (const hj of GostHalbjahr.getQualifikationsphase())
			if ((summeWS[hj.id] < fachTafel.stundenumfang[hj.id]) && this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_3, fachTafel.fachbezeichnung)))
				success = false;
		return success;
	}

	/**
	 * Prüft, ob Unterricht im erforderlichen Umfang erteilt wurde.
	 *
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true bei fehlerloser Prüfung sonst false
	 */
	private pruefeStundenumfang(tafel: BeruflichesGymnasiumStundentafel | null, fachTafel: BeruflichesGymnasiumStundentafelFach, fachBelegung: BKGymAbiturFachbelegung): boolean {
		let success: boolean = true;
		let summeTafel: number = 0;
		let summeBelegung: number = 0;
		let unterbelegung: boolean = false;
		for (const hj of GostHalbjahr.getQualifikationsphase()) {
			summeTafel += fachTafel.stundenumfang[hj.id];
			const belegung: BKGymAbiturFachbelegungHalbjahr | null = fachBelegung.belegungen[hj.id];
			if (belegung === null) {
				if (fachTafel.stundenumfang[hj.id] > 0)
					success = !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_6, fachTafel.fachbezeichnung, hj.kuerzel)) && success;
				continue;
			}
			if ((belegung.notenkuerzel !== null) && (!JavaString.isEmpty(belegung.notenkuerzel))) {
				summeBelegung += belegung.wochenstunden;
				unterbelegung = unterbelegung || (fachTafel.stundenumfang[hj.id] > belegung.wochenstunden);
			} else
				if (fachTafel.stundenumfang[hj.id] > 0) {
					success = !(this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_2, fachTafel.fachbezeichnung, hj.kuerzel))) && success;
					unterbelegung = true;
				}
		}
		if (summeTafel > summeBelegung) {
			success = !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_3, fachTafel.fachbezeichnung)) && success;
			return success;
		}
		success = !(unterbelegung && this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.ST_5_INFO, fachTafel.fachbezeichnung))) && success;
		return success;
	}

	/**
	 * Prüft, ob die Schriftlichkeit der Fächer korrekt erfüllt ist.
	 *
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeSchriftlich(tafel: BeruflichesGymnasiumStundentafel | null, fachTafel: BeruflichesGymnasiumStundentafelFach, fachBelegung: BKGymAbiturFachbelegung): boolean {
		let success: boolean;
		success = this.pruefeSchriftlichEF(GostHalbjahr.EF1, tafel, fachTafel, fachBelegung);
		success = this.pruefeSchriftlichEF(GostHalbjahr.EF2, tafel, fachTafel, fachBelegung) && success;
		success = this.pruefeSchriftlichQ1(GostHalbjahr.Q11, tafel, fachTafel, fachBelegung) && success;
		success = this.pruefeSchriftlichQ1(GostHalbjahr.Q12, tafel, fachTafel, fachBelegung) && success;
		success = this.pruefeSchriftlichQ2(GostHalbjahr.Q21, tafel, fachTafel, fachBelegung) && success;
		success = this.pruefeSchriftlichQ2(GostHalbjahr.Q22, tafel, fachTafel, fachBelegung) && success;
		return success;
	}

	/**
	 * In der EF muss in mindestens vier Fächern, in den LK-Fächern, Deutsch, Mathematik und Fremdsprachen in jedem Fall
	 *
	 * @param hj             das Oberstufenhalbjahr
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeSchriftlichEF(hj: GostHalbjahr, tafel: BeruflichesGymnasiumStundentafel | null, fachTafel: BeruflichesGymnasiumStundentafelFach, fachBelegung: BKGymAbiturFachbelegung): boolean {
		const belegungHj: BKGymAbiturFachbelegungHalbjahr | null = fachBelegung.belegungen[hj.id];
		if ((belegungHj === null) || (belegungHj.schriftlich))
			return true;
		if (JavaObject.equalsTranspiler("Deutsch", (fachTafel.fachbezeichnung)) || JavaObject.equalsTranspiler("Mathematik", (fachTafel.fachbezeichnung)))
			return !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_1, fachTafel.fachbezeichnung, hj.kuerzel));
		if ((fachBelegung.abiturFach !== null) && (fachBelegung.abiturFach <= 2))
			return !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_2, fachTafel.fachbezeichnung, hj.kuerzel));
		if (this.manager.istFremdsprachenbelegung(fachBelegung))
			return !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_3, fachTafel.fachbezeichnung, hj.kuerzel));
		return true;
	}

	/**
	 * In der Q1 müssen allen Abiturfächer schriftlich belegt sein. Deutsch, Mathematik,
	 * Fremdsprachen und die Fächer der Berufsabschlussprüfung in jedem Fall
	 *
	 * @param hj             das Oberstufenhalbjahr
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeSchriftlichQ1(hj: GostHalbjahr, tafel: BeruflichesGymnasiumStundentafel | null, fachTafel: BeruflichesGymnasiumStundentafelFach, fachBelegung: BKGymAbiturFachbelegung): boolean {
		const belegungHj: BKGymAbiturFachbelegungHalbjahr | null = fachBelegung.belegungen[hj.id];
		if ((belegungHj === null) || (belegungHj.schriftlich))
			return true;
		if (JavaObject.equalsTranspiler("Deutsch", (fachTafel.fachbezeichnung)) || JavaObject.equalsTranspiler("Mathematik", (fachTafel.fachbezeichnung)))
			return !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_1, fachTafel.fachbezeichnung, hj.kuerzel));
		if ((fachBelegung.abiturFach !== null) && (fachBelegung.abiturFach <= 4))
			return !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_4, fachTafel.fachbezeichnung, hj.kuerzel));
		if (this.manager.istFremdsprachenbelegung(fachBelegung))
			return !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_3, fachTafel.fachbezeichnung, hj.kuerzel));
		return true;
	}

	/**
	 * In der Q2 müssen das erste bis dritte Abiturfach schriftlich belegt sein. Deutsch, Mathematik,
	 * Nur in der Q21 auch die Fremdsprachen
	 *
	 * @param hj             das Oberstufenhalbjahr
	 * @param tafel          die Stundentafel der Anlage
	 * @param fachTafel      das zu prüfende Fach aus der Stundentafel
	 * @param fachBelegung   die zugeordnete Fachbelegung
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private pruefeSchriftlichQ2(hj: GostHalbjahr, tafel: BeruflichesGymnasiumStundentafel | null, fachTafel: BeruflichesGymnasiumStundentafelFach, fachBelegung: BKGymAbiturFachbelegung): boolean {
		const belegungHj: BKGymAbiturFachbelegungHalbjahr | null = fachBelegung.belegungen[hj.id];
		if ((belegungHj === null) || (belegungHj.schriftlich))
			return true;
		if ((fachBelegung.abiturFach !== null) && (fachBelegung.abiturFach <= 3))
			return !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_4, fachTafel.fachbezeichnung, hj.kuerzel));
		if ((GostHalbjahr.Q21 as unknown === hj as unknown) && this.manager.istFremdsprachenbelegung(fachBelegung))
			return !this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.KL_3, fachTafel.fachbezeichnung, hj.kuerzel));
		return true;
	}

	/**
	 * Diese Methode prüft die LK-Belegung in der Stundentafel.
	 *
	 * @param tafel   die zu prüfende Stundentafel
	 */
	private pruefeLKs(tafel: BeruflichesGymnasiumStundentafel): void {
		const lk1: BKGymAbiturFachbelegung | null = this.manager.getAbiFachbelegung(GostAbiturFach.LK1);
		if (lk1 === null)
			this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.LK_1));
		const lk2: BKGymAbiturFachbelegung | null = this.manager.getAbiFachbelegung(GostAbiturFach.LK2);
		if (lk2 === null)
			this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.LK_2));
		if (lk1 !== null && lk2 !== null && !(this.manager.isValidKursartFachbelegung(tafel, lk1, GostAbiturFach.LK1) && this.manager.isValidKursartFachbelegung(tafel, lk2, GostAbiturFach.LK2)))
			this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.LK_3, this.manager.getFachkuerzelFromFachbelegung(lk1), this.manager.getFachkuerzelFromFachbelegung(lk2), this.manager.getGliederung().name(), this.manager.getFachklassenschluessel()));
	}

	/**
	 * pruefe die Belegung des 3. und 4. Abiturfachs
	 *
	 * @param tafel
	 */
	private pruefeAbiGrundkurse(tafel: BeruflichesGymnasiumStundentafel): void {
		const ab3: BKGymAbiturFachbelegung | null = this.manager.getAbiFachbelegung(GostAbiturFach.AB3);
		if (ab3 === null)
			this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.AB_3));
		const ab4: BKGymAbiturFachbelegung | null = this.manager.getAbiFachbelegung(GostAbiturFach.AB4);
		if (ab4 === null)
			this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.AB_4));
		if ((ab3 !== null) && (ab4 !== null) && !this.manager.pruefeAbiGrundkurswahl(tafel, ab3, ab4))
			this.addFehler(tafel, new BKGymBelegungsfehler(BKGymBelegungsfehlerTyp.AB_5, this.manager.getFachkuerzelFromFachbelegung(ab3), this.manager.getFachkuerzelFromFachbelegung(ab4), this.manager.getGliederung().name(), this.manager.getFachklassenschluessel()));
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
