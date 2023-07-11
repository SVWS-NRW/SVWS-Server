import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostFach } from '../../../core/data/gost/GostFach';
import { GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { MapUtils } from '../../../core/utils/MapUtils';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import type { Comparator } from '../../../java/util/Comparator';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { GostKlausurvorgabe } from '../../../core/data/gost/klausuren/GostKlausurvorgabe';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';
import { HashMap3D } from '../../../core/adt/map/HashMap3D';

export class GostKlausurvorgabenManager extends JavaObject {

	private _faecherManager : GostFaecherManager = new GostFaecherManager();

	/**
	 * Die GostKlausurvorgaben, die im Manager vorhanden sind
	 */
	private readonly _vorgaben : List<GostKlausurvorgabe> = new ArrayList();

	/**
	 * Eine Map quartal -> Liste von GostKlausurvorgaben
	 */
	private readonly _mapQuartalKlausurvorgaben : JavaMap<number, List<GostKlausurvorgabe>> = new HashMap();

	/**
	 * Eine Map id -> GostKlausurvorgabe
	 */
	private readonly _mapIdKlausurvorgabe : JavaMap<number, GostKlausurvorgabe> = new HashMap();

	/**
	 * Eine Map quartal -> kursartAllg -> fachId -> GostKlausurvorgabe
	 */
	private readonly _mapQuartalKursartFachKlausurvorgabe : HashMap3D<number, string, number, GostKlausurvorgabe> = new HashMap3D();

	/**
	 * Eine Map kursartAllg -> fachId -> Liste von GostKlausurvorgabe
	 */
	private readonly _mapKursartFachKlausurvorgaben : HashMap2D<string, number, List<GostKlausurvorgabe>> = new HashMap2D();

	/**
	 * Ein Comparator für die Klausurvorgaben.
	 */
	private readonly _compVorgabe : Comparator<GostKlausurvorgabe> = { compare : (a: GostKlausurvorgabe, b: GostKlausurvorgabe) => {
		const aFach : GostFach | null = this._faecherManager.get(a.idFach);
		const bFach : GostFach | null = this._faecherManager.get(b.idFach);
		if (aFach !== null && bFach !== null) {
			if (aFach.sortierung > bFach.sortierung)
				return +1;
			if (aFach.sortierung < bFach.sortierung)
				return -1;
		}
		if (JavaString.compareTo(a.kursart, b.kursart) < 0)
			return +1;
		if (JavaString.compareTo(a.kursart, b.kursart) > 0)
			return -1;
		return JavaInteger.compare(a.quartal, b.quartal);
	} };


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen
	 * GostKlausurvorgaben und erzeugt die privaten Attribute.
	 *
	 * @param vorgaben       die Liste der GostKlausurvorgaben eines Abiturjahrgangs
	 *                       und Gost-Halbjahres
	 * @param faecherManager der Gost-Fächermanager
	 */
	public constructor(vorgaben : List<GostKlausurvorgabe>, faecherManager : GostFaecherManager) {
		super();
		this._faecherManager = faecherManager;
		for (const v of vorgaben) {
			this.addKlausurvorgabe(v);
		}
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich Informationen einer
	 * Klausurvorgabe geändert hat.
	 *
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public updateKlausurvorgabe(vorgabe : GostKlausurvorgabe) : void {
		const vorgabeOrig : GostKlausurvorgabe = DeveloperNotificationException.ifMapGetIsNull(this._mapIdKlausurvorgabe, vorgabe.idVorgabe);
		this.removeUpdateKlausurvorgabeCommons(vorgabeOrig);
		this.addKlausurvorgabe(vorgabe);
	}

	/**
	 * Fügt die Klausurvorgabe den internen Strukturen hinzu.
	 *
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public addKlausurvorgabe(vorgabe : GostKlausurvorgabe) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_vorgaben", this._vorgaben, vorgabe);
		this._vorgaben.sort(this._compVorgabe);
		DeveloperNotificationException.ifMapPutOverwrites(this._mapIdKlausurvorgabe, vorgabe.idVorgabe, vorgabe);
		DeveloperNotificationException.ifListAddsDuplicate("_mapQuartalKlausurvorgabenList", MapUtils.getOrCreateArrayList(this._mapQuartalKlausurvorgaben, vorgabe.quartal), vorgabe);
		this._mapQuartalKursartFachKlausurvorgabe.put(vorgabe.quartal, vorgabe.kursart, vorgabe.idFach, vorgabe);
		DeveloperNotificationException.ifListAddsDuplicate("_mapKursartFachKlausurvorgabenList", Map2DUtils.getOrCreateArrayList(this._mapKursartFachKlausurvorgaben, vorgabe.kursart, vorgabe.idFach), vorgabe);
		this._mapKursartFachKlausurvorgaben.getNonNullOrException(vorgabe.kursart, vorgabe.idFach).sort(this._compVorgabe);
	}

	private removeUpdateKlausurvorgabeCommons(vorgabe : GostKlausurvorgabe) : void {
		DeveloperNotificationException.ifListRemoveFailes("_vorgaben", this._vorgaben, vorgabe);
		DeveloperNotificationException.ifMapRemoveFailes(this._mapIdKlausurvorgabe, vorgabe.idVorgabe);
		DeveloperNotificationException.ifListRemoveFailes("_mapQuartalKlausurvorgabenList", DeveloperNotificationException.ifMapGetIsNull(this._mapQuartalKlausurvorgaben, vorgabe.quartal), vorgabe);
		this._mapQuartalKursartFachKlausurvorgabe.removeOrException(vorgabe.quartal, vorgabe.kursart, vorgabe.idFach);
		DeveloperNotificationException.ifListRemoveFailes("_mapQuartalKlausurvorgabenList", DeveloperNotificationException.ifMap2DGetIsNull(this._mapKursartFachKlausurvorgaben, vorgabe.kursart, vorgabe.idFach), vorgabe);
	}

	/**
	 * Löscht eine Klausurvorgabe aus den internen Strukturen
	 *
	 * @param vId das GostKlausurvorgabe-Objekt
	 */
	public removeVorgabe(vId : number) : void {
		const vorgabe : GostKlausurvorgabe = DeveloperNotificationException.ifMapGetIsNull(this._mapIdKlausurvorgabe, vId);
		this.removeUpdateKlausurvorgabeCommons(vorgabe);
	}

	/**
	 * Liefert eine Liste von GostKlausurvorgabe-Objekten des Halbjahres
	 *
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public getKlausurvorgaben() : List<GostKlausurvorgabe>;

	/**
	 * Liefert eine Liste von GostKlausurvorgabe-Objekten zum übergebenen Quartal
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public getKlausurvorgaben(quartal : number) : List<GostKlausurvorgabe> | null;

	/**
	 * Implementation for method overloads of 'getKlausurvorgaben'
	 */
	public getKlausurvorgaben(__param0? : number) : List<GostKlausurvorgabe> | null {
		if ((typeof __param0 === "undefined")) {
			return this._vorgaben;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			const quartal : number = __param0 as number;
			return this._mapQuartalKlausurvorgaben.get(quartal);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zur übergebenen id zurück.
	 *
	 * @param idVorgabe die ID der Klausurvorgabe
	 *
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public gibGostKlausurvorgabe(idVorgabe : number) : GostKlausurvorgabe {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapIdKlausurvorgabe, idVorgabe);
	}

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param quartal     das Quartal
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public gibGostKlausurvorgabeByQuartalKursartFach(quartal : number, kursartAllg : string, idFach : number) : GostKlausurvorgabe | null {
		return this._mapQuartalKursartFachKlausurvorgabe.getOrNull(quartal, kursartAllg, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param quartal     das Quartal, wenn 0, Vorgaben für alle Quartale
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public gibGostKlausurvorgabenByQuartalKursartFach(quartal : number, kursartAllg : string, idFach : number) : List<GostKlausurvorgabe> | null {
		if (quartal > 0) {
			const retList : List<GostKlausurvorgabe> | null = new ArrayList();
			const vorgabe : GostKlausurvorgabe | null = this.gibGostKlausurvorgabeByQuartalKursartFach(quartal, kursartAllg, idFach);
			if (vorgabe !== null)
				retList.add(vorgabe);
			return retList;
		}
		return this.gibGostKlausurvorgabenByKursartFach(kursartAllg, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public gibGostKlausurvorgabenByKursartFach(kursartAllg : string, idFach : number) : List<GostKlausurvorgabe> | null {
		const list : List<GostKlausurvorgabe> | null = this._mapKursartFachKlausurvorgaben.getOrNull(kursartAllg, idFach);
		return list !== null ? list : new ArrayList();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.GostKlausurvorgabenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_GostKlausurvorgabenManager(obj : unknown) : GostKlausurvorgabenManager {
	return obj as GostKlausurvorgabenManager;
}
