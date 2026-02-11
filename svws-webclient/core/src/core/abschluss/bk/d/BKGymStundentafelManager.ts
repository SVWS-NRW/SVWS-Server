import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap2D } from '../../../../core/adt/map/HashMap2D';
import type { JavaSet } from '../../../../java/util/JavaSet';
import { BKGymFachbelegungManager } from '../../../../core/abschluss/bk/d/BKGymFachbelegungManager';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { BeruflichesGymnasiumStundentafel } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafel';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import { BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit';
import { BKGymAbiturUtils } from '../../../../core/utils/bk/BKGymAbiturUtils';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import type { JavaMap } from '../../../../java/util/JavaMap';
import { BeruflichesGymnasiumStundentafelFach } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelFach';
import { HashSet } from '../../../../java/util/HashSet';

export class BKGymStundentafelManager extends JavaObject {

	/**
	 * Die Bezeichnung für die zweite fortgeführte Fremdsprache
	 */
	public static readonly ZWEITE_FREMDSPRACHE: string = "Zweite Fremdsprache";

	/**
	 * Die Bezeichnung für die neueinsetzende Fremdsprache
	 */
	public static readonly NEUE_FREMDSPRACHE: string = "Neue Fremdsprache";

	/**
	 * Die Bezeichnung für das Fach Religion
	 */
	public static readonly RELIGION: string = "Religionslehre";

	/**
	 * Das Wahlfach
	 */
	public static readonly WAHLFACH: string = "Wahlfach";

	/**
	 * Die möglichen Ersatzfächer für Religion
	 */
	public static readonly ERSATZ_FUER_RELIGION: List<string> = ArrayList.of("Erziehungswissenschaften", "Geschichte", "Gesellschaftslehre mit Geschichte", "Philosophie", "Politik/Geschichte", "Psychologie", "Soziologie");

	/**
	 * Der Abiturdaten-Manager
	 */
	private readonly fachbelegungManager: BKGymFachbelegungManager;

	/**
	 * Die Liste der Stundentafeln im Manager.
	 */
	private readonly stundentafeln: List<BeruflichesGymnasiumStundentafel> = new ArrayList<BeruflichesGymnasiumStundentafel>();

	/**
	 * Eine HashMap2D für den schnellen Zugriff auf die Fächer der Stundentafeln anhand der Tafel und der Fachbezeichnung
	 */
	private readonly mapStundentafelFachByTafelAndFachbezeichnung: HashMap2D<BeruflichesGymnasiumStundentafel, string, BeruflichesGymnasiumStundentafelFach> = new HashMap2D<BeruflichesGymnasiumStundentafel, string, BeruflichesGymnasiumStundentafelFach>();


	/**
	 * Erstellt einen Manager mit den übergebenen Stundentafeln.
	 *
	 * @param belegungManager   der Fachbelegung-Manager
	 * @param tafeln            die Liste der Original-Stundentafeln eines Bildungsgangs aus dem CoreType
	 */
	public constructor(belegungManager: BKGymFachbelegungManager, tafeln: List<BeruflichesGymnasiumStundentafel>) {
		super();
		this.fachbelegungManager = belegungManager;
		this.stundentafeln.clear();
		for (const t of tafeln) {
			this.stundentafeln.addAll(this.createStundentafelnOhneWahlmoeglichkeit(t));
		}
		for (const t of this.stundentafeln)
			BKGymStundentafelManager.pruefeStundentafelAufDoppelte(t);
		for (const tafel of this.stundentafeln)
			for (const fach of tafel.faecher)
				this.mapStundentafelFachByTafelAndFachbezeichnung.put(tafel, fach.fachbezeichnung, fach);
	}

	/**
	 * Getter für die Liste der Stundentafeln
	 *
	 * @return die Liste der Stundentafeln
	 */
	public getStundentafeln(): List<BeruflichesGymnasiumStundentafel> {
		return this.stundentafeln;
	}

	/**
	 * Liefert zu einer Fachbezeichnung und Stundentafel das entsprechende BeruflichesGymnasiumStundentafelFach-Objekt
	 *
	 * @param tafel             die Stundentafel, in der gesucht wird.
	 * @param fachbezeichnung   die Bezeichnung des Fachs
	 *
	 * @return das gefundene Stundentafelfach oder null, wenn es nicht existiert.
	 */
	public getFachByTafelAndBezeichnung(tafel: BeruflichesGymnasiumStundentafel, fachbezeichnung: string): BeruflichesGymnasiumStundentafelFach | null {
		return this.mapStundentafelFachByTafelAndFachbezeichnung.getOrNull(tafel, fachbezeichnung);
	}

	/**
	 * Erzeugt die die Liste der Stundentafeln entsprechend der übergebenen Stundentafel unter Berücksichtigung der Belegungen.
	 *
	 * @param t   die Stundentafel mit Wahlmöglichkeiten
	 *
	 * @return die Liste der Stundentafeln ohne Wahlmöglichkeiten entsprechend der belegten Fächer
	 */
	private createStundentafelnOhneWahlmoeglichkeit(t: BeruflichesGymnasiumStundentafel): List<BeruflichesGymnasiumStundentafel> {
		const neueTafeln: List<BeruflichesGymnasiumStundentafel> = new ArrayList<BeruflichesGymnasiumStundentafel>();
		const alleMoeglichenFaecherlisten: List<List<BeruflichesGymnasiumStundentafelFach>> = this.getMoeglicheFaecherlisten(t);
		for (const fachListe of alleMoeglichenFaecherlisten) {
			const neueTafel: BeruflichesGymnasiumStundentafel | null = new BeruflichesGymnasiumStundentafel();
			neueTafel.variante = t.variante;
			neueTafel.faecher = fachListe;
			neueTafel.wahlmoeglichkeiten = t.wahlmoeglichkeiten;
			neueTafeln.add(neueTafel);
		}
		return neueTafeln;
	}

	/**
	 * Erstellt eine Map der Fächer gruppiert nach der Sortierung.
	 * In dieser Map sind nicht belegte Wahlmöglichkeiten einer Fachtafelposition entfernt.
	 * Mindestens eine Position bleibt erhalten, falls kein Fach der Position belegt wurde.
	 *
	 * @param t   die Stundentafel
	 *
	 * @return die Map der Fächer gruppiert nach der Sortierung
	 */
	private getMoeglicheFaecherlisten(t: BeruflichesGymnasiumStundentafel): List<List<BeruflichesGymnasiumStundentafelFach>> {
		const mapFachBySortierung: JavaMap<number, List<BeruflichesGymnasiumStundentafelFach>> = new HashMap<number, List<BeruflichesGymnasiumStundentafelFach>>();
		for (const fach of t.faecher) {
			let fachListe: List<BeruflichesGymnasiumStundentafelFach> | null = mapFachBySortierung.get(fach.sortierung);
			if (fachListe === null) {
				fachListe = new ArrayList();
				mapFachBySortierung.put(fach.sortierung, fachListe);
			}
			fachListe.add(fach);
		}
		const mapMultiFachBySortierung: JavaMap<number, List<BeruflichesGymnasiumStundentafelFach>> = new HashMap<number, List<BeruflichesGymnasiumStundentafelFach>>();
		const eindeutigeFaecher: List<BeruflichesGymnasiumStundentafelFach> = new ArrayList<BeruflichesGymnasiumStundentafelFach>();
		for (const entry of mapFachBySortierung.entrySet()) {
			const bereinigt: List<BeruflichesGymnasiumStundentafelFach> | null = this.bereinigteFaecherliste(entry.getValue());
			if (bereinigt.isEmpty())
				eindeutigeFaecher.add(entry.getValue().getFirst());
			else
				if (bereinigt.size() === 1)
					eindeutigeFaecher.add(bereinigt.getFirst());
				else
					if (bereinigt.size() > 1)
						mapMultiFachBySortierung.put(entry.getKey(), bereinigt);
		}
		return BKGymStundentafelManager.createStundentafelPermutation(eindeutigeFaecher, mapMultiFachBySortierung);
	}

	/**
	 * Erstellt alle möglichen Permutationen der Stundentafeln basierend auf den eindeutigen Fächern
	 * und den Fächern mit mehreren Belegungsmöglichkeiten.
	 *
	 * @param eindeutigeFaecher          die Liste der eindeutigen Fächer
	 * @param mapMultiFachBySortierung   die Map der Fächer mit mehreren Belegungsmöglichkeiten gruppiert nach Sortierung
	 *
	 * @return die Liste der möglichen Stundentafel-Permutationen
	 */
	private static createStundentafelPermutation(eindeutigeFaecher: List<BeruflichesGymnasiumStundentafelFach>, mapMultiFachBySortierung: JavaMap<number, List<BeruflichesGymnasiumStundentafelFach>>): List<List<BeruflichesGymnasiumStundentafelFach>> {
		const result: List<List<BeruflichesGymnasiumStundentafelFach>> = new ArrayList<List<BeruflichesGymnasiumStundentafelFach>>();
		if (mapMultiFachBySortierung.isEmpty()) {
			result.add(eindeutigeFaecher);
			return result;
		}
		const keys: List<number> = new ArrayList<number>(mapMultiFachBySortierung.keySet());
		BKGymStundentafelManager.permutiereFaecher(result, eindeutigeFaecher, mapMultiFachBySortierung, keys, 0, new ArrayList());
		return result;
	}

	/**
	 * Rekursive Methode zur Permutation der Fächer.
	 *
	 * @param result            die Liste der Ergebnisse
	 * @param eindeutigeFaecher die Liste der eindeutigen Fächer
	 * @param map               die Map der Fächer mit mehreren Belegungsmöglichkeiten
	 * @param keys              die sortierten Schlüssel der Map
	 * @param index             der aktuelle Index in den Schlüsseln
	 * @param current           die aktuelle Kombination von Fächern
	 */
	private static permutiereFaecher(result: List<List<BeruflichesGymnasiumStundentafelFach>>, eindeutigeFaecher: List<BeruflichesGymnasiumStundentafelFach>, map: JavaMap<number, List<BeruflichesGymnasiumStundentafelFach>>, keys: List<number>, index: number, current: List<BeruflichesGymnasiumStundentafelFach>): void {
		if (index === keys.size()) {
			const kombi: List<BeruflichesGymnasiumStundentafelFach> | null = new ArrayList<BeruflichesGymnasiumStundentafelFach>(eindeutigeFaecher);
			kombi.addAll(current);
			kombi.sort(BKGymAbiturUtils.comparatorStundentafelFach);
			result.add(kombi);
			return;
		}
		const currentFaecher: List<BeruflichesGymnasiumStundentafelFach> | null = map.get(keys.get(index));
		if (currentFaecher === null)
			return;
		for (const fach of currentFaecher) {
			current.add(fach);
			BKGymStundentafelManager.permutiereFaecher(result, eindeutigeFaecher, map, keys, index + 1, current);
			current.remove(current.size() - 1);
		}
	}

	/**
	 * Gibt die Liste mit den Fächern der übergebenen faecher zurück, die entweder belegt sind oder ein gültiges Spezialfach wie "Wahlfach" ist.
	 * Bei dem speziellen Stundentafeleintrag für die zweite bzw. neue Fremdsprache wird nur das Fach zurückgegeben, das auch tatsächlich zu verwenden ist.
	 *
	 * @param faecher   die Liste der Fächer einer Fachtafelposition
	 *
	 * @return das eindeutige Fach oder null, wenn kein eindeutiges Fach gefunden wurde
	 */
	private bereinigteFaecherliste(faecher: List<BeruflichesGymnasiumStundentafelFach>): List<BeruflichesGymnasiumStundentafelFach> {
		const bereinigt: List<BeruflichesGymnasiumStundentafelFach> = new ArrayList<BeruflichesGymnasiumStundentafelFach>();
		for (const fach of faecher) {
			if (this.istGueltigesSpezialfach(fach.fachbezeichnung) || this.fachbelegungManager.getFachbelegungByBezeichnung(fach.fachbezeichnung) !== null)
				bereinigt.add(fach);
		}
		return bereinigt;
	}

	/**
	 * Prüft, ob das Spezialfach zu übernehmen ist.
	 * Bei Wahlfach wird true zurück gegeben, da es nur einmal in einer Fachtafel vorkommt.
	 * Bei Zweiter Fremdsprache wird true geliefert, wenn keine zweite Fremdsprache erkannt wurde (zweiteFremdspracheID==null)
	 * oder die Fremdsprache erkannt wurde und die Fremdsprachenbelegung in der SI erfüllt wurde.
	 * Bei Neue Fremdsprache wird nur dann true geliefert, wenn eine zweite Fremdsprache erkannt wurde und die
	 * Fremdsprachenbelegung in der SI nicht erfüllt wurde.
	 *
	 * @param fachbezeichnung   die Bezeichnung des zu prüfenden Fachs,
	 *
	 * @return siehe oben
	 */
	private istGueltigesSpezialfach(fachbezeichnung: string): boolean {
		if (BKGymStundentafelManager.istWahlfach(fachbezeichnung))
			return true;
		if (BKGymStundentafelManager.istZweiteFremdsprache(fachbezeichnung))
			return !this.fachbelegungManager.istZweiteFremdspracheNeuEinsetzend();
		if (BKGymStundentafelManager.istNeueFremdsprache(fachbezeichnung))
			return this.fachbelegungManager.istZweiteFremdspracheNeuEinsetzend();
		return false;
	}

	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Wahlfach handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für das Wahlfach ist, sonst false
	 */
	public static istWahlfach(bezeichnung: string): boolean {
		return JavaObject.equalsTranspiler(bezeichnung, (BKGymStundentafelManager.WAHLFACH));
	}

	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Zweite Fremdsprache handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für die zweite Fremdsprache ist, sonst false
	 */
	public static istZweiteFremdsprache(bezeichnung: string): boolean {
		return JavaObject.equalsTranspiler(bezeichnung, (BKGymStundentafelManager.ZWEITE_FREMDSPRACHE));
	}

	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Neue Fremdsprache handelt.
	 *
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true, wenn es die Repräsentation für die Neue Fremdsprache ist, sonst false
	 */
	public static istNeueFremdsprache(bezeichnung: string): boolean {
		return JavaObject.equalsTranspiler(bezeichnung, (BKGymStundentafelManager.NEUE_FREMDSPRACHE));
	}

	/**
	 * Prüft ob es sich um die Bezeichnung für das Fach Religion handelt
	 *
	 * @param bezeichnung   die Fachbezeichnung
	 *
	 * @return true, wenn es sich um die Fachbezeichnung für Religion handelt.
	 */
	public static istReligion(bezeichnung: string): boolean {
		return JavaObject.equalsTranspiler(bezeichnung, (BKGymStundentafelManager.RELIGION));
	}

	/**
	 * Prüft ob die Fachkombination für das dritte und vierte Abiturfach gültig ist.
	 *
	 * @param tafel   die zu prüfende Stundentafel mit ihren Wahlmöglichkeiten
	 * @param ab3Bezeichnung   die Bezeichnung des dritten Abiturfaches
	 * @param ab4Bezeichnung   die Bezeichnung vierten Abiturfaches
	 *
	 * @return true, wenn die Wahlmöglichkeit besteht, ansonsten false
	 */
	public pruefeAbiGrundkurswahl(tafel: BeruflichesGymnasiumStundentafel, ab3Bezeichnung: string, ab4Bezeichnung: string): boolean {
		for (const wm of tafel.wahlmoeglichkeiten) {
			if (this.istGueltigeWahlmoeglichkeit(wm, ab3Bezeichnung, ab4Bezeichnung))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die übergebene Kombination aus drittem und viertem Abiturfach gültig ist.
	 * Dabei werden die Spezialfälle für eine zweite Fremdsprache und ein mögliches Wahlfach (Zukunftstauglichkeit)
	 * berücksichtigt
	 *
	 * @param wm    die Wahlmöglichkeit
	 * @param ab3Bezeichnung   die Bezeichnung des dritten Abiturfaches
	 * @param ab4Bezeichnung   die Bezeichnung vierten Abiturfaches
	 *
	 * @return true, wenn sie gültig ist, und ansonsten false
	 */
	private istGueltigeWahlmoeglichkeit(wm: BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit, ab3Bezeichnung: string, ab4Bezeichnung: string): boolean {
		let wm3: string | null = null;
		for (const fachBez3 of wm.abifach3)
			if (JavaObject.equalsTranspiler(fachBez3, (ab3Bezeichnung)) || BKGymStundentafelManager.istWahlfach(fachBez3) || (BKGymStundentafelManager.istZweiteFremdsprache(fachBez3) && this.fachbelegungManager.istZweiteFremdsprache(ab3Bezeichnung)))
				wm3 = fachBez3;
		if (wm3 === null)
			return false;
		for (const fachBez4 of wm.abifach4)
			if (JavaObject.equalsTranspiler(fachBez4, (ab4Bezeichnung)) || BKGymStundentafelManager.istWahlfach(fachBez4) || (BKGymStundentafelManager.istZweiteFremdsprache(fachBez4) && this.fachbelegungManager.istZweiteFremdsprache(ab4Bezeichnung)))
				return true;
		return false;
	}

	/**
	 * Prüft, dass keine Sortierung doppelt vorkommt. Falls es vorkommt wird einer DeveloperNotificationException geworfen.
	 *
	 * @param t   die Stundentafel
	 */
	private static pruefeStundentafelAufDoppelte(t: BeruflichesGymnasiumStundentafel): void {
		const single: JavaSet<number> = new HashSet<number>();
		for (const fach of t.faecher) {
			if (single.contains(fach.sortierung))
				throw new DeveloperNotificationException("In der Belegprüfung ist ein interner Fehler aufgetreten: In der Stundentafel sind noch Wahlmöglichkeiten enthalten.")
			single.add(fach.sortierung);
		}
	}

	/**
	 * Ermittelt den maximalen Wert des Attributes sortierung in der Stundentafel
	 *
	 * @param tafel   die Stundentafel
	 *
	 * @return der maximale Wert von sortierung
	 */
	public static getMaximalSortierung(tafel: BeruflichesGymnasiumStundentafel): number {
		let max: number = JavaInteger.MIN_VALUE;
		for (const fach of tafel.faecher)
			if (fach.sortierung > max)
				max = fach.sortierung;
		return max;
	}

	/**
	 * Prüft, ob das Fach in der Stundentafel eine Belegung benötigt.
	 *
	 * @param fach   das Fach der Stundentafel
	 *
	 * @return true, wenn das Fach eine Belegung benötigt, sonst false
	 */
	public static brauchtBelegungInQPhase(fach: BeruflichesGymnasiumStundentafelFach): boolean {
		let summeStunden: number = 0;
		for (const hj of GostHalbjahr.getQualifikationsphase())
			summeStunden += fach.stundenumfang[hj.id];
		return summeStunden > 0;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymStundentafelManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymStundentafelManager'].includes(name);
	}

	public static readonly class = new Class<BKGymStundentafelManager>('de.svws_nrw.core.abschluss.bk.d.BKGymStundentafelManager');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymStundentafelManager(obj: unknown): BKGymStundentafelManager {
	return obj as BKGymStundentafelManager;
}
