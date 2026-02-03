import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap2D } from '../../../../core/adt/map/HashMap2D';
import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { BKGymFachbelegungManager } from '../../../../core/abschluss/bk/d/BKGymFachbelegungManager';
import { ArrayList } from '../../../../java/util/ArrayList';
import { BKGymAbiturFachbelegungHalbjahr } from '../../../../core/data/bk/abi/BKGymAbiturFachbelegungHalbjahr';
import { Note } from '../../../../asd/types/Note';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { Arrays } from '../../../../java/util/Arrays';
import { Collections } from '../../../../java/util/Collections';
import { BeruflichesGymnasiumStundentafelFach } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelFach';

export class BKGymFachbelegungZuStundentafelfachManager extends JavaObject {

	/**
	 * Der Abiturdaten-Manager
	 */
	private readonly abidatenManager: BKGymAbiturdatenManager;

	/**
	 * Eine HashMap2D für den schnelle Zugriff auf die Halbjahresbelegungen anhand des Halbjahres und der Fachbezeichnung
	 */
	private readonly mapBelegungByHalbjahrAndFachbezeichung: HashMap2D<string, number, BKGymAbiturFachbelegungHalbjahr>;

	/**
	 * Eine HashMap2D für den schnelle Zugriff auf die Halbjahresbelegungen anhand des Halbjahres und der Fachbezeichnung
	 */
	private readonly mapUsedBelegungByHalbjahrAndFachbezeichung: HashMap2D<string, number, BKGymAbiturFachbelegungHalbjahr> = new HashMap2D<string, number, BKGymAbiturFachbelegungHalbjahr>();

	/**
	 * Ein Array, das die belegten Fächer je Halbjahr der Qualifikationsphase speichert.
	 */
	private readonly belegteStundenByHalbjahrUndFachposition: Array<Array<number>>;


	/**
	 * Erstellt einen neuen Fachbelegungs-Manager für die Fachbelegungen eines Schülers im beruflichen Gymnasium
	 *
	 * @param fachbelegungManager                      der Manager für eine Stundentafelvariante
	 * @param mapBelegungByHalbjahrAndFachbezeichung   eine HashMap2D für den schnellen Zugriff auf die Halbjahresbelegungen
	 * @param abidatenManager
	 * @param maxFachposition
	 */
	public constructor(fachbelegungManager: BKGymFachbelegungManager, mapBelegungByHalbjahrAndFachbezeichung: HashMap2D<string, number, BKGymAbiturFachbelegungHalbjahr>, abidatenManager: BKGymAbiturdatenManager, maxFachposition: number) {
		super();
		this.abidatenManager = abidatenManager;
		this.mapBelegungByHalbjahrAndFachbezeichung = mapBelegungByHalbjahrAndFachbezeichung;
		this.belegteStundenByHalbjahrUndFachposition = [...Array(maxFachposition + 1)].map(e => Array(GostHalbjahr.maxHalbjahre).fill(0));
	}

	/**
	 * Gibt die Anzahl der belegten Stunden für eine Fachposition und Halbjahr zurück
	 *
	 * @param hj     das Halbjahr
	 * @param fach   die Fach der Stundentafel
	 *
	 * @return 0, wenn nicht belegt sonst > 0, wenn belegt
	 */
	public getBelegteStundenByHalbjahrAndFach(hj: GostHalbjahr, fach: BeruflichesGymnasiumStundentafelFach): number {
		return this.belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id];
	}

	/**
	 * Gibt zurück, ob ein Fach in einem Halbjahr schriftlich belegt wurde.
	 * Falls es nicht belegt wurde, wird trotzdem true geliefert, damit nicht
	 * zum Fehler des Faches noch die nicht erfüllte Schriftlichkeit ausgegeben wird.
	 *
	 * @param hj
	 * @param fach
	 *
	 * @return true, wenn schriftlich belegt oder keine Belegung vorliegt, sonst false
	 */
	public getSchriftlichBelegt(hj: GostHalbjahr, fach: BeruflichesGymnasiumStundentafelFach): boolean {
		let belegungHj: BKGymAbiturFachbelegungHalbjahr | null = this.mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
		if (belegungHj === null)
			belegungHj = this.mapUsedBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
		return (belegungHj === null) || belegungHj.schriftlich;
	}

	/**
	 * Belegt das angegebene Fach mit den vorhandenen Belegungen in der Qualifikationsphase.
	 *
	 * @param fach   die Fach der Stundentafel
	 */
	public belegeFach(fach: BeruflichesGymnasiumStundentafelFach): void {
		for (const hj of GostHalbjahr.getQualifikationsphase()) {
			const belegungHj: BKGymAbiturFachbelegungHalbjahr | null = this.mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if (belegungHj !== null) {
				const note: Note = Note.fromKuerzel(belegungHj.notenkuerzel);
				this.belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id] = BKGymFachbelegungZuStundentafelfachManager.giltNoteFuerBelegung(note) ? belegungHj.wochenstunden : -1;
				this.mapBelegungByHalbjahrAndFachbezeichung.removeOrException(fach.fachbezeichnung, hj.id);
				this.mapUsedBelegungByHalbjahrAndFachbezeichung.put(fach.fachbezeichnung, hj.id, belegungHj);
			}
		}
	}

	/**
	 * Prüft ob die eingetragene Note zur Belegung führt.
	 * Eine Belegung mit ungenügend wird auch eingetragen.
	 * Das wird in der Markierung geprüft.
	 *
	 * @param note   die zu prüfende Note
	 *
	 * @return true, wenn die Note zu einer Belegung führt.
	 */
	private static giltNoteFuerBelegung(note: Note): boolean {
		let _sevar_1448546682 : any;
		const _seexpr_1448546682 = (note);
		if (_seexpr_1448546682 === Note.KEINE) {
			_sevar_1448546682 = false;
		} else if (_seexpr_1448546682 === Note.ABGEMELDET) {
			_sevar_1448546682 = false;
		} else if (_seexpr_1448546682 === Note.NICHT_ERTEILT) {
			_sevar_1448546682 = false;
		} else if (_seexpr_1448546682 === Note.NICHT_TEILGENOMMEN) {
			_sevar_1448546682 = false;
		} else {
			_sevar_1448546682 = true;
		}
		return _sevar_1448546682;
	}

	/**
	 * Belegt ein Ersatzfach. Es wird das Fach nur dann belegt, wenn der Stundenumfang auch abgedeckt wird.
	 * Für den Fall, dass ein Halbjahr schon mit dem originalen Fach belegt ist, wird es nicht erneut belegt.
	 *
	 * @param fach   das zu belegende Fach
	 *
	 * @return true, wenn das Fach in den freien Halbjahren mit komplettem Umfang belegt wird.
	 */
	public belegeErsatzfach(fach: BeruflichesGymnasiumStundentafelFach): boolean {
		for (const hj of GostHalbjahr.getQualifikationsphase()) {
			const belegungHj: BKGymAbiturFachbelegungHalbjahr | null = this.mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if ((this.getBelegteStundenByHalbjahrAndFach(hj, fach) <= 0) && ((belegungHj === null) || (belegungHj.wochenstunden < fach.stundenumfang[hj.id]) || !BKGymFachbelegungZuStundentafelfachManager.giltNoteFuerBelegung(Note.fromKuerzel(belegungHj.notenkuerzel))))
				return false;
		}
		for (const hj of GostHalbjahr.getQualifikationsphase()) {
			const belegungHj: BKGymAbiturFachbelegungHalbjahr | null = this.mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if ((this.getBelegteStundenByHalbjahrAndFach(hj, fach) <= 0) && (belegungHj !== null)) {
				this.belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id] = belegungHj.wochenstunden;
				this.mapBelegungByHalbjahrAndFachbezeichung.removeOrException(fach.fachbezeichnung, hj.id);
				this.mapUsedBelegungByHalbjahrAndFachbezeichung.put(fach.fachbezeichnung, hj.id, belegungHj);
			}
		}
		return true;
	}

	/**
	 * Belegt ein Ersatzfach. Es wird das Fach nur dann belegt, wenn der Stundenumfang auch abgedeckt wird.
	 * Für den Fall, dass ein Halbjahr schon mit dem originalen Fach belegt ist, wird es nicht erneut belegt.
	 * Hinzu kommt, dass nur dann belegt wird, wenn nicht noch eine Belegung des regulären Faches folgt.
	 *
	 * @param fach   das zu belegende Fach
	 *
	 * @return true, wenn das Fach in den freien Halbjahren mit komplettem Umfang belegt wird.
	 */
	public belegeErsatzfachVomEndeHer(fach: BeruflichesGymnasiumStundentafelFach): boolean {
		const hjeReversed: List<GostHalbjahr> = Arrays.asList(...GostHalbjahr.getQualifikationsphase());
		Collections.reverse(hjeReversed);
		for (const hj of hjeReversed) {
			const belegungHj: BKGymAbiturFachbelegungHalbjahr | null = this.mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if (this.getBelegteStundenByHalbjahrAndFach(hj, fach) > 0)
				break;
			if ((belegungHj === null) || (belegungHj.wochenstunden < fach.stundenumfang[hj.id]) || !BKGymFachbelegungZuStundentafelfachManager.giltNoteFuerBelegung(Note.fromKuerzel(belegungHj.notenkuerzel)))
				return false;
		}
		for (const hj of hjeReversed) {
			const belegungHj: BKGymAbiturFachbelegungHalbjahr | null = this.mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if (this.getBelegteStundenByHalbjahrAndFach(hj, fach) > 0)
				break;
			if (belegungHj !== null) {
				this.belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id] = belegungHj.wochenstunden;
				this.mapBelegungByHalbjahrAndFachbezeichung.removeOrException(fach.fachbezeichnung, hj.id);
				this.mapUsedBelegungByHalbjahrAndFachbezeichung.put(fach.fachbezeichnung, hj.id, belegungHj);
			}
		}
		return true;
	}

	/**
	 * Belegt ein beliebiges Fach für ein Halbjahr.
	 * Die Bezeichnung des Fachs ist dabei egal. Es wird der Position zugeordet.
	 * Die Methode wird für die Belegung des Wahlfachs verwendet
	 *
	 * @param hj     das Halbjahr
	 * @param fach   die Fach der Stundentafel der zweiten Fremdsprache
	 */
	public belegeBeliebigesFachFuerHalbjahr(hj: GostHalbjahr, fach: BeruflichesGymnasiumStundentafelFach): void {
		for (const fachbezeichnung of this.getFachbezeichnungenFreierBelegungen()) {
			const belegungHj: BKGymAbiturFachbelegungHalbjahr | null = this.mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fachbezeichnung, hj.id);
			if (belegungHj !== null) {
				const note: Note = Note.fromKuerzel(belegungHj.notenkuerzel);
				if (BKGymFachbelegungZuStundentafelfachManager.giltNoteFuerBelegung(note) && (fach.stundenumfang[hj.id] <= belegungHj.wochenstunden)) {
					this.belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id] = belegungHj.wochenstunden;
					this.mapBelegungByHalbjahrAndFachbezeichung.removeOrException(fachbezeichnung, hj.id);
					this.mapUsedBelegungByHalbjahrAndFachbezeichung.put(fach.fachbezeichnung, hj.id, belegungHj);
					return;
				}
			}
		}
	}

	/**
	 * Prüft ob ein Fach voll belegt ist in der Qualifikationsphase
	 *
	 * @param fach   das zu prüfende Fach
	 *
	 * @return true, wenn die Belegung vollständig auch im Stundenumfang ist.
	 */
	public istVollbelegt(fach: BeruflichesGymnasiumStundentafelFach): boolean {
		for (const hj of GostHalbjahr.getQualifikationsphase())
			if (this.getBelegteStundenByHalbjahrAndFach(hj, fach) < fach.stundenumfang[hj.id])
				return false;
		return true;
	}

	/**
	 * Liefert alle noch verbliebenen Fachbezeichnungen
	 *
	 * @return die verbleibenden Fachbezeichnungen
	 */
	public getFachbezeichnungenFreierBelegungen(): List<string> {
		return new ArrayList<string>(this.mapBelegungByHalbjahrAndFachbezeichung.getKeySet());
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymFachbelegungZuStundentafelfachManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymFachbelegungZuStundentafelfachManager'].includes(name);
	}

	public static readonly class = new Class<BKGymFachbelegungZuStundentafelfachManager>('de.svws_nrw.core.abschluss.bk.d.BKGymFachbelegungZuStundentafelfachManager');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymFachbelegungZuStundentafelfachManager(obj: unknown): BKGymFachbelegungZuStundentafelfachManager {
	return obj as BKGymFachbelegungZuStundentafelfachManager;
}
