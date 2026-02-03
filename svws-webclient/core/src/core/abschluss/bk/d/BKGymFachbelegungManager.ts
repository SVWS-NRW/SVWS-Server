import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap2D } from '../../../../core/adt/map/HashMap2D';
import { BKGymFach } from '../../../../core/data/bk/abi/BKGymFach';
import { BKGymStundentafelManager } from '../../../../core/abschluss/bk/d/BKGymStundentafelManager';
import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { HashMap } from '../../../../java/util/HashMap';
import { BKGymAbiturFachbelegung } from '../../../../core/data/bk/abi/BKGymAbiturFachbelegung';
import { BKGymAbiturFachbelegungHalbjahr } from '../../../../core/data/bk/abi/BKGymAbiturFachbelegungHalbjahr';
import { BeruflichesGymnasiumStundentafel } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafel';
import { BKGymFachbelegungZuStundentafelfachManager } from '../../../../core/abschluss/bk/d/BKGymFachbelegungZuStundentafelfachManager';
import { BKGymAbiturdaten } from '../../../../core/data/bk/abi/BKGymAbiturdaten';
import { BKGymFaecherManager } from '../../../../core/utils/bk/BKGymFaecherManager';
import { Note } from '../../../../asd/types/Note';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import type { JavaMap } from '../../../../java/util/JavaMap';
import { BeruflichesGymnasiumStundentafelFach } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelFach';

export class BKGymFachbelegungManager extends JavaObject {

	/**
	 * Der Abiturdaten-Manager
	 */
	private readonly abidatenManager: BKGymAbiturdatenManager;

	/**
	 * Der Fächer-Manager
	 */
	private readonly faecherManager: BKGymFaecherManager;

	/**
	 * FachID der zweiten Fremdsprache
	 */
	private readonly zweiteFremdspracheID: number | null;

	/**
	 * Ob das Fach der Facharbeit ein LK ist
	 */
	private readonly istFacharbeitLK: boolean;

	/**
	 * Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen für ein Fach anhand der Bezeichnung ermöglicht
	 */
	private readonly mapFachbelegungenByFachbezeichnung: JavaMap<string, BKGymAbiturFachbelegung> = new HashMap<string, BKGymAbiturFachbelegung>();

	/**
	 * Eine Map, welche von der Nummer des Abiturfaches auf die Fachbelegung der Abiturdaten verweist.
	 */
	private readonly mapAbiturfachbelegungen: JavaMap<number, BKGymAbiturFachbelegung> = new HashMap<number, BKGymAbiturFachbelegung>();

	/**
	 * Eine HashMap2D für den schnelle Zugriff auf die Halbjahresbelegungen anhand des Halbjahres und der Fachbezeichnung
	 */
	private readonly mapBelegungByHalbjahrAndFachbezeichung: HashMap2D<string, number, BKGymAbiturFachbelegungHalbjahr> = new HashMap2D<string, number, BKGymAbiturFachbelegungHalbjahr>();


	/**
	 * Erstellt einen neuen Fachbelegungs-Manager für die Fachbelegungen eines Schülers im beruflichen Gymnasium
	 *
	 * @param abidatenManager   der Manager für die Abiturdaten
	 */
	public constructor(abidatenManager: BKGymAbiturdatenManager) {
		super();
		this.abidatenManager = abidatenManager;
		this.faecherManager = abidatenManager.getFaecherManager();
		this.init();
		this.zweiteFremdspracheID = this.ermittleZweiteFremdspracheID();
		this.istFacharbeitLK = this.pruefeIstFacharbeitLK();
	}

	/**
	 * Initialisiert den Fachbelegungs-Manager.
	 */
	private init(): void {
		const abidaten: BKGymAbiturdaten = this.abidatenManager.getAbidaten();
		this.mapAbiturfachbelegungen.clear();
		this.mapFachbelegungenByFachbezeichnung.clear();
		for (const fb of abidaten.fachbelegungen) {
			const fachbezeichnung: string = this.faecherManager.getBezeichnungByFachID(fb.fachID);
			const fach: BKGymFach | null = this.faecherManager.get(fb.fachID);
			if ((fach === null) || (fach.bezeichnung === null))
				continue;
			this.mapFachbelegungenByFachbezeichnung.put(fach.bezeichnung, fb);
			if (fb.abiturFach !== null)
				this.mapAbiturfachbelegungen.put(fb.abiturFach, fb);
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				const belegungHalbjahr: BKGymAbiturFachbelegungHalbjahr | null = fb.belegungen[halbjahr.id];
				if ((belegungHalbjahr !== null) && (belegungHalbjahr.kursartKuerzel !== null))
					this.mapBelegungByHalbjahrAndFachbezeichung.put(fachbezeichnung, halbjahr.id, belegungHalbjahr);
			}
		}
	}

	/**
	 * Gibt das Abiturfachdaten für das geforderte Abiturfach zurück.
	 *
	 * @param abiFach Das n. Abiturfach, das gewünscht ist
	 *
	 * @return die entsprechende Fachbelegung des Abiturfachs
	 */
	public getAbiFachbelegung(abiFach: GostAbiturFach): BKGymAbiturFachbelegung | null {
		return this.mapAbiturfachbelegungen.get(abiFach.id);
	}

	/**
	 * Gibt die FachID für das geforderte Abiturfach zurück.
	 *
	 * @param abiFach Das n. Abiturfach, das gewünscht ist
	 *
	 * @return die entsprechende FachID des Abiturfachs oder null wenn es nicht gefunden wird.
	 */
	public getAbiFachID(abiFach: GostAbiturFach): number | null {
		const abifach: BKGymAbiturFachbelegung | null = this.getAbiFachbelegung(abiFach);
		if (abifach === null)
			return null;
		return abifach.fachID;
	}

	/**
	 * Liefert die FachID der zweiten Fremdsprache oder null, falls nicht vorhanden
	 *
	 * @return die ID der zweiten Fremdsprache oder null
	 */
	public ermittleZweiteFremdspracheID(): number | null {
		for (const entry of this.mapFachbelegungenByFachbezeichnung.entrySet()) {
			const fach: BKGymFach | null = this.faecherManager.get(entry.getValue().fachID);
			if ((fach !== null) && fach.istFremdsprache && !JavaObject.equalsTranspiler(fach.bezeichnung, ("Englisch")))
				return entry.getValue().fachID;
		}
		return null;
	}

	/**
	 * Getter für den Zugriff auf die FachID der zweiten Fremdsprache
	 *
	 * @return die FachID
	 */
	public getZweiteFremdspracheID(): number | null {
		return this.zweiteFremdspracheID;
	}

	/**
	 * liefert die Bezeichnung der zweiten Fremdsprache
	 *
	 * @return die Bezeichnung der zweiten Fremdsprache
	 */
	public getZweiteFremdspracheBezeichnung(): string | null {
		return this.zweiteFremdspracheID === null ? null : this.faecherManager.getBezeichnungByFachID(this.zweiteFremdspracheID);
	}

	/**
	 * Liefert eine Belegung anhand der Fachbezeichnung zurück
	 *
	 * @param bezeichnung   das Fach
	 *
	 * @return die Fachbelegung
	 */
	public getFachbelegungByBezeichnung(bezeichnung: string): BKGymAbiturFachbelegung | null {
		return this.mapFachbelegungenByFachbezeichnung.get(bezeichnung);
	}

	/**
	 * Liefert die FachID anhand der Fachbezeichnung zurück
	 *
	 * @param bezeichnung   das Fach
	 *
	 * @return die FachID oder null, wenn die Bezeichnung nicht existiert.
	 */
	public getFachIDByBezeichnung(bezeichnung: string): number | null {
		const fach: BKGymAbiturFachbelegung | null = this.mapFachbelegungenByFachbezeichnung.get(bezeichnung);
		if (fach === null)
			return null;
		return fach.fachID;
	}

	/**
	 * Prüft, ob die übergebene Fachbelegung als Fach in der Stundentafel vorkommt bzw. vorkommen kann.
	 *
	 * @param tafel   die Stundentafel
	 * @param fb      die Fachbelegung
	 *
	 * @return der Eintrag der Stundentafel, bei welchem die Fachbelegung vorkommt, oder null, wenn keine Zuordnung zur Stundentafel möglich ist
	 */
	public getFachByBelegung(tafel: BeruflichesGymnasiumStundentafel, fb: BKGymAbiturFachbelegung): BeruflichesGymnasiumStundentafelFach | null {
		const fbFach: BKGymFach | null = this.faecherManager.get(fb.fachID);
		if ((fbFach === null) || (fbFach.bezeichnung === null))
			return null;
		for (const tafelFach of tafel.faecher)
			if (JavaObject.equalsTranspiler(tafelFach.fachbezeichnung, (fbFach.bezeichnung)))
				return tafelFach;
		if (fbFach.istFremdsprache)
			for (const tafelFach of tafel.faecher)
				if (BKGymStundentafelManager.istZweiteFremdsprache(tafelFach.fachbezeichnung))
					return tafelFach;
		for (const tafelFach of tafel.faecher)
			if (BKGymStundentafelManager.istWahlfach(tafelFach.fachbezeichnung))
				return tafelFach;
		return null;
	}

	/**
	 * Prüft, ob es sich bei der zweiten Fremdsprache um eine neu einsetzende Fremdsprache handelt.
	 *
	 * @return true, wenn es sich um eine neu einsetzende Fremdsprachenbelegung handelt, und ansonsten false
	 */
	public istZweiteFremdspracheNeuEinsetzend(): boolean {
		if (this.abidatenManager.getZweiteFremdspracheInSekIErfuellt())
			return false;
		if (this.zweiteFremdspracheID === null)
			return false;
		const fbFach: BKGymFach | null = this.faecherManager.get(this.zweiteFremdspracheID);
		if ((fbFach === null) || (fbFach.bezeichnung === null))
			return false;
		const fb: BKGymAbiturFachbelegung | null = this.getFachbelegungByBezeichnung(fbFach.bezeichnung);
		return fbFach.istFremdSpracheNeuEinsetzend || (fb !== null && fb.istFSNeu);
	}

	/**
	 * Prüft, ob die übergebene Fachbezeichnung der zweiten Fremdsprache entspricht.
	 *
	 * @param fachBezeichnung   die Fachbezeichnung
	 *
	 * @return true, wenn die Fachbezeichnung der zweiten Fremdsprache entspricht, sonst false
	 */
	public istZweiteFremdsprache(fachBezeichnung: string): boolean {
		const zweiteFremdspracheBezeichnung: string | null = this.getZweiteFremdspracheBezeichnung();
		if (zweiteFremdspracheBezeichnung === null)
			return false;
		return JavaObject.equalsTranspiler(zweiteFremdspracheBezeichnung, (fachBezeichnung));
	}

	/**
	 * Ermittelt ob die Facharbeit einem LK-Fach zugeordnet ist.
	 * Wird dann auf false gesetzt, wenn eine Facharbeit vorhanden ist und die Fachbezeichnung
	 * für die Facharbeit nicht dem LK1 oder LK2 zugeordnet werden kann.
	 *
	 * @return false wenn Facharbeit vorhanden und nicht einem LK zugeordnet sonst true
	 */
	private pruefeIstFacharbeitLK(): boolean {
		if (this.abidatenManager.getAbidaten().facharbeitFachbezeichnung === null)
			return true;
		const fachbezeichnung: string | null = this.abidatenManager.getAbidaten().facharbeitFachbezeichnung;
		const facharbeitFachID: number | null = fachbezeichnung === null ? null : this.getFachIDByBezeichnung(fachbezeichnung);
		if (facharbeitFachID === null)
			return false;
		const fachIDLK1: number | null = this.getAbiFachID(GostAbiturFach.LK1);
		if (fachIDLK1 !== null && JavaObject.equalsTranspiler(facharbeitFachID, (fachIDLK1)))
			return true;
		const fachIDLK2: number | null = this.getAbiFachID(GostAbiturFach.LK2);
		if (fachIDLK2 === null)
			return false;
		return JavaObject.equalsTranspiler(facharbeitFachID, (fachIDLK2));
	}

	/**
	 * Getter für den Zugriff auf istFacharbeitLK
	 *
	 * @return ob ggfs. die Facharbeit einem LK-Fach zugeordnet ist
	 */
	public getIstFacharbeitLK(): boolean {
		return this.istFacharbeitLK;
	}

	/**
	 * Prüft, ob das Fach in allen angegebenen Halbjahren belegt wurde.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung   die zu prüfende Fachbelegung
	 * @param leereHje       die ggfs.nicht gewerteten Halbjahre
	 * @param halbjahre      die zu prüfenden Halbjahre
	 *
	 * @return true, falls das Fach in den Halbjahren belegt wurde, sonst false
	 */
	public pruefeBelegung(fachbelegung: BKGymAbiturFachbelegung | null, leereHje: List<GostHalbjahr>, ...halbjahre: Array<GostHalbjahr>): boolean {
		if (fachbelegung === null)
			return false;
		if (halbjahre.length === 0)
			return true;
		for (const halbjahr of halbjahre) {
			if (leereHje.contains(halbjahr))
				continue;
			const belegungHalbjahr: BKGymAbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (belegungHalbjahr.kursartKuerzel === null))
				return false;
			if (BKGymFachbelegungManager.istNullPunkteBelegungInQPhase(belegungHalbjahr))
				return false;
		}
		return true;
	}

	/**
	 * Gibt zurück, ob es sich bei der Halbjahresbelegung um eine Belegung handelt, welche mit
	 * null Punkten abgeschlossen wurde und welche daher als nicht belegter Kurs zu werten ist.
	 *
	 * @param halbjahresbelegung   die Halbjahresbelegung eines Kurses
	 *
	 * @return true, fall es sich um einen Null-Punkte-Kurs in der Qualifikationsphase handelt.
	 */
	public static istNullPunkteBelegungInQPhase(halbjahresbelegung: BKGymAbiturFachbelegungHalbjahr): boolean {
		const hj: GostHalbjahr | null = GostHalbjahr.fromKuerzel(halbjahresbelegung.halbjahrKuerzel);
		if ((hj === null) || (hj.istEinfuehrungsphase()))
			return false;
		return Note.fromKuerzel(halbjahresbelegung.notenkuerzel) as unknown === Note.UNGENUEGEND as unknown;
	}

	/**
	 * Erstellt einen neuen Fachbelegung-zu-Stundentafelfach-Manager
	 *
	 * @param maxFachposition   die maximale Fachposition in der Stundentafel
	 *
	 * @return der neue Manager
	 */
	public newFachbelegungZuStundentafelfachManager(maxFachposition: number): BKGymFachbelegungZuStundentafelfachManager {
		return new BKGymFachbelegungZuStundentafelfachManager(this, new HashMap2D(this.mapBelegungByHalbjahrAndFachbezeichung), this.abidatenManager, maxFachposition);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymFachbelegungManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymFachbelegungManager'].includes(name);
	}

	public static readonly class = new Class<BKGymFachbelegungManager>('de.svws_nrw.core.abschluss.bk.d.BKGymFachbelegungManager');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymFachbelegungManager(obj: unknown): BKGymFachbelegungManager {
	return obj as BKGymFachbelegungManager;
}
