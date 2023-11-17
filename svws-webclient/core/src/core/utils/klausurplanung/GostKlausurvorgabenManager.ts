import { JavaObject } from '../../../java/lang/JavaObject';
import { GostFach } from '../../../core/data/gost/GostFach';
import { GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { MapUtils } from '../../../core/utils/MapUtils';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import type { Comparator } from '../../../java/util/Comparator';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { Map3DUtils } from '../../../core/utils/Map3DUtils';
import { GostKlausurvorgabe } from '../../../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';
import { HashMap3D } from '../../../core/adt/map/HashMap3D';

export class GostKlausurvorgabenManager extends JavaObject {

	private _faecherManager : GostFaecherManager | null = null;

	private readonly _compVorgabe : Comparator<GostKlausurvorgabe> = { compare : (a: GostKlausurvorgabe, b: GostKlausurvorgabe) => {
		if (JavaString.compareTo(a.kursart, b.kursart) < 0)
			return +1;
		if (JavaString.compareTo(a.kursart, b.kursart) > 0)
			return -1;
		if (this._faecherManager !== null) {
			const aFach : GostFach | null = this._faecherManager.get(a.idFach);
			const bFach : GostFach | null = this._faecherManager.get(b.idFach);
			if (aFach !== null && bFach !== null) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		if (a.halbjahr !== b.halbjahr)
			return JavaInteger.compare(a.halbjahr, b.halbjahr);
		return JavaInteger.compare(a.quartal, b.quartal);
	} };

	private readonly _vorgabe_by_id : JavaMap<number, GostKlausurvorgabe> = new HashMap();

	private readonly _vorgabenmenge : List<GostKlausurvorgabe> = new ArrayList();

	private readonly _vorgabenmenge_by_quartal : JavaMap<number, List<GostKlausurvorgabe>> = new HashMap();

	private readonly _vorgabe_by_quartal_and_kursartAllg_and_idFach : HashMap3D<number, string, number, GostKlausurvorgabe> = new HashMap3D();

	private readonly _vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach : HashMap3D<number, string, number, List<GostKlausurvorgabe>> = new HashMap3D();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param listVorgaben die Liste der GostKlausurvorgaben eines Abiturjahrgangs
	 *                      und Gost-Halbjahres
	 * @param faecherManager   der Fächermanager
	 */
	public constructor(listVorgaben : List<GostKlausurvorgabe>, faecherManager : GostFaecherManager | null) {
		super();
		this._faecherManager = faecherManager;
		this.initAll(listVorgaben);
	}

	/**
	 * Berechnet die intern verwendete Quartal-Id, die aus Gost-Halbjahr und Quartal berechnet wird.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal   das Quartal
	 *
	 * @return die intern verwendete Quartals-Id
	 */
	public static getInternalQuartalIdWithGosthalbjahr(halbjahr : GostHalbjahr, quartal : number) : number {
		return GostKlausurvorgabenManager.getInternalQuartalsIdWithHalbjahrid(halbjahr.id, quartal);
	}

	/**
	 * Berechnet die intern verwendete Quartal-Id, die aus Gost-Halbjahr und Quartal berechnet wird.
	 *
	 * @param halbjahr die Id des Gost-Halbjahres
	 * @param quartal   das Quartal
	 *
	 * @return die intern verwendete Quartals-Id
	 */
	public static getInternalQuartalsIdWithHalbjahrid(halbjahr : number, quartal : number) : number {
		return halbjahr * 10 + quartal;
	}

	private initAll(listVorgaben : List<GostKlausurvorgabe>) : void {
		this.vorgabeAddAll(listVorgaben);
		this.update_all();
	}

	/**
	 * Liefert den Fächermanager
	 *
	 * @return den Fächermanager
	 */
	public getFaecherManager() : GostFaecherManager | null {
		return this._faecherManager;
	}

	private update_all() : void {
		this.update_vorgabemenge();
		this.update_vorgabenmenge_by_quartal();
		this.update_vorgabe_by_quartal_and_kursartAllg_and_idFach();
		this.update_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach();
	}

	private update_vorgabenmenge_by_quartal() : void {
		this._vorgabenmenge_by_quartal.clear();
		for (const v of this._vorgabenmenge) {
			MapUtils.getOrCreateArrayList(this._vorgabenmenge_by_quartal, GostKlausurvorgabenManager.getInternalQuartalsIdWithHalbjahrid(v.halbjahr, v.quartal)).add(v);
			MapUtils.getOrCreateArrayList(this._vorgabenmenge_by_quartal, GostKlausurvorgabenManager.getInternalQuartalsIdWithHalbjahrid(v.halbjahr, 0)).add(v);
		}
	}

	private update_vorgabe_by_quartal_and_kursartAllg_and_idFach() : void {
		this._vorgabe_by_quartal_and_kursartAllg_and_idFach.clear();
		for (const v of this._vorgabenmenge)
			this._vorgabe_by_quartal_and_kursartAllg_and_idFach.put(GostKlausurvorgabenManager.getInternalQuartalsIdWithHalbjahrid(v.halbjahr, v.quartal), v.kursart, v.idFach, v);
	}

	private update_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach() : void {
		this._vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.clear();
		for (const v of this._vorgabenmenge)
			Map3DUtils.getOrCreateArrayList(this._vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach, v.halbjahr, v.kursart, v.idFach).add(v);
	}

	private update_vorgabemenge() : void {
		this._vorgabenmenge.clear();
		this._vorgabenmenge.addAll(this._vorgabe_by_id.values());
		this._vorgabenmenge.sort(this._compVorgabe);
	}

	private vorgabeAddOhneUpdate(vorgabe : GostKlausurvorgabe) : void {
		GostKlausurvorgabenManager.vorgabeCheck(vorgabe);
		DeveloperNotificationException.ifMapPutOverwrites(this._vorgabe_by_id, vorgabe.idVorgabe, vorgabe);
	}

	/**
	 * Fügt ein {@link GostKlausurvorgabe}-Objekt hinzu.
	 *
	 * @param vorgabe Das {@link GostKlausurvorgabe}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public vorgabeAdd(vorgabe : GostKlausurvorgabe) : void {
		this.vorgabeAddOhneUpdate(vorgabe);
		this.update_all();
	}

	private vorgabeAddAllOhneUpdate(listVorgaben : List<GostKlausurvorgabe>) : void {
		for (const vorgabe of listVorgaben)
			this.vorgabeAddOhneUpdate(vorgabe);
	}

	/**
	 * Fügt alle {@link GostKlausurvorgabe}-Objekte hinzu.
	 *
	 * @param listVorgaben Die Menge der {@link GostKlausurvorgabe}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public vorgabeAddAll(listVorgaben : List<GostKlausurvorgabe>) : void {
		this.vorgabeAddAllOhneUpdate(listVorgaben);
		this.update_all();
	}

	private static vorgabeCheck(vorgabe : GostKlausurvorgabe) : void {
		DeveloperNotificationException.ifInvalidID("kursklausur.id", vorgabe.idVorgabe);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurvorgabe}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idVorgabe Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurvorgabe}-Objekt.
	 */
	public vorgabeGetByIdOrException(idVorgabe : number) : GostKlausurvorgabe {
		return DeveloperNotificationException.ifMapGetIsNull(this._vorgabe_by_id, idVorgabe);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurvorgabe}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurvorgabe}-Objekte.
	 */
	public vorgabeGetMengeAsList() : List<GostKlausurvorgabe> {
		return this._vorgabenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurvorgabe}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param vorgabe Das neue {@link GostKlausurvorgabe}-Objekt.
	 */
	public vorgabePatchAttributes(vorgabe : GostKlausurvorgabe) : void {
		GostKlausurvorgabenManager.vorgabeCheck(vorgabe);
		DeveloperNotificationException.ifMapRemoveFailes(this._vorgabe_by_id, vorgabe.idVorgabe);
		DeveloperNotificationException.ifMapPutOverwrites(this._vorgabe_by_id, vorgabe.idVorgabe, vorgabe);
		this.update_all();
	}

	private vorgabeRemoveOhneUpdateById(idVorgabe : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._vorgabe_by_id, idVorgabe);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurvorgabe}-Objekt.
	 *
	 * @param idVorgabe Die ID des {@link GostKlausurvorgabe}-Objekts.
	 */
	public vorgabeRemoveById(idVorgabe : number) : void {
		this.vorgabeRemoveOhneUpdateById(idVorgabe);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurvorgabe}-Objekte.
	 *
	 * @param listVorgaben Die Liste der zu entfernenden
	 *                          {@link GostKlausurvorgabe}-Objekte.
	 */
	public vorgabeRemoveAll(listVorgaben : List<GostKlausurvorgabe>) : void {
		for (const vorgabe of listVorgaben)
			this.vorgabeRemoveOhneUpdateById(vorgabe.idVorgabe);
		this.update_all();
	}

	/**
	 * Liefert eine Liste von GostKlausurvorgabe-Objekten zum übergebenen Gost-Halkbjahr und Quartal
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public vorgabeGetMengeByQuartal(halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurvorgabe> {
		let vorgaben : List<GostKlausurvorgabe> | null = this._vorgabenmenge_by_quartal.get(GostKlausurvorgabenManager.getInternalQuartalIdWithGosthalbjahr(halbjahr, quartal));
		return vorgaben !== null ? vorgaben : new ArrayList();
	}

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal     das Quartal
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public vorgabeGetByQuartalAndKursartallgAndFachid(halbjahr : GostHalbjahr, quartal : number, kursartAllg : GostKursart, idFach : number) : GostKlausurvorgabe | null {
		return this._vorgabe_by_quartal_and_kursartAllg_and_idFach.getOrNull(GostKlausurvorgabenManager.getInternalQuartalIdWithGosthalbjahr(halbjahr, quartal), kursartAllg.kuerzel, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal     das Quartal, wenn 0, Vorgaben für alle Quartale
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public vorgabeGetMengeByQuartalAndKursartallgAndFachid(halbjahr : GostHalbjahr, quartal : number, kursartAllg : GostKursart, idFach : number) : List<GostKlausurvorgabe> {
		if (quartal > 0) {
			const retList : List<GostKlausurvorgabe> | null = new ArrayList();
			const vorgabe : GostKlausurvorgabe | null = this.vorgabeGetByQuartalAndKursartallgAndFachid(halbjahr, quartal, kursartAllg, idFach);
			if (vorgabe !== null)
				retList.add(vorgabe);
			return retList;
		}
		return this.vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(halbjahr, kursartAllg, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(halbjahr : GostHalbjahr, kursartAllg : GostKursart, idFach : number) : List<GostKlausurvorgabe> {
		const list : List<GostKlausurvorgabe> | null = this._vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.getOrNull(halbjahr.id, kursartAllg.kuerzel, idFach);
		return list !== null ? list : new ArrayList();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplanung.GostKlausurvorgabenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplanung_GostKlausurvorgabenManager(obj : unknown) : GostKlausurvorgabenManager {
	return obj as GostKlausurvorgabenManager;
}
