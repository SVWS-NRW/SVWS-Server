import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { GostFaecherManager } from '../../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { JavaString } from '../../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { GostKursart } from '../../../../core/types/gost/GostKursart';
import type { Comparator } from '../../../../java/util/Comparator';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import { Map3DUtils } from '../../../../core/utils/Map3DUtils';
import { GostKlausurvorgabe } from '../../../../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { ListUtils } from '../../../../core/utils/ListUtils';
import type { JavaMap } from '../../../../java/util/JavaMap';
import { HashMap5D } from '../../../../core/adt/map/HashMap5D';
import { HashMap4D } from '../../../../core/adt/map/HashMap4D';
import { HashMap3D } from '../../../../core/adt/map/HashMap3D';
import { Map4DUtils } from '../../../../core/utils/Map4DUtils';
import { HashSet } from '../../../../java/util/HashSet';

export class GostKlausurvorgabenManager extends JavaObject {

	private _faecherManager : GostFaecherManager | null = null;

	private readonly _compVorgabe : Comparator<GostKlausurvorgabe> = { compare : (a: GostKlausurvorgabe, b: GostKlausurvorgabe) => {
		if (JavaString.compareTo(a.kursart, b.kursart) < 0)
			return +1;
		if (JavaString.compareTo(a.kursart, b.kursart) > 0)
			return -1;
		if (this.getFaecherManagerOrNull() !== null) {
			const aFach : GostFach | null = this.getFaecherManager().get(a.idFach);
			const bFach : GostFach | null = this.getFaecherManager().get(b.idFach);
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

	private readonly _vorgabenmenge_by_halbjahr_and_quartal : HashMap3D<number, number, number, List<GostKlausurvorgabe>> = new HashMap3D();

	private readonly _vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach : HashMap5D<number, number, number, string, number, GostKlausurvorgabe> = new HashMap5D();

	private readonly _vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach : HashMap4D<number, number, string, number, List<GostKlausurvorgabe>> = new HashMap4D();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param listVorgaben die Liste der GostKlausurvorgaben eines Abiturjahrgangs
	 *                      und Gost-Halbjahres
	 */
	public constructor(listVorgaben : List<GostKlausurvorgabe>) {
		super();
		this.initAll(listVorgaben);
	}

	private initAll(listVorgaben : List<GostKlausurvorgabe>) : void {
		this.vorgabeAddAll(listVorgaben);
		this.update_all();
	}

	/**
	 * Setzt den GostFaecherManager
	 *
	 * @param faecherManager der GostFaecherManager
	 */
	public setFaecherManager(faecherManager : GostFaecherManager) : void {
		this._faecherManager = faecherManager;
	}

	/**
	 * Liefert den GostFaecherManager, falls dieser gesetzt ist, sonst wird eine DeveloperNotificationException geworfen.
	 *
	 * @return den GostFaecherManager
	 */
	public getFaecherManager() : GostFaecherManager {
		if (this._faecherManager === null)
			throw new DeveloperNotificationException("GostFaecherManager not set.")
		return this._faecherManager;
	}

	/**
	 * Liefert den GostFaecherManager, falls dieser gesetzt ist, sonst null.
	 *
	 * @return den GostFaecherManager
	 */
	public getFaecherManagerOrNull() : GostFaecherManager | null {
		return this._faecherManager;
	}

	private update_all() : void {
		this.update_vorgabemenge();
		this.update_vorgabenmenge_by_halbjahr_and_quartal();
		this.update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach();
		this.update_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach();
	}

	private update_vorgabenmenge_by_halbjahr_and_quartal() : void {
		this._vorgabenmenge_by_halbjahr_and_quartal.clear();
		for (const v of this._vorgabenmenge) {
			Map3DUtils.getOrCreateArrayList(this._vorgabenmenge_by_halbjahr_and_quartal, v.abiJahrgang, v.halbjahr, v.quartal).add(v);
		}
	}

	private update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach() : void {
		this._vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach.clear();
		for (const v of this._vorgabenmenge)
			this._vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach.put(v.abiJahrgang, v.halbjahr, v.quartal, v.kursart, v.idFach, v);
	}

	private update_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach() : void {
		this._vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.clear();
		for (const v of this._vorgabenmenge)
			Map4DUtils.getOrCreateArrayList(this._vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach, v.abiJahrgang, v.halbjahr, v.kursart, v.idFach).add(v);
	}

	private update_vorgabemenge() : void {
		this._vorgabenmenge.clear();
		this._vorgabenmenge.addAll(this._vorgabe_by_id.values());
		this._vorgabenmenge.sort(this._compVorgabe);
	}

	/**
	 * Fügt ein {@link GostKlausurvorgabe}-Objekt hinzu.
	 *
	 * @param vorgabe Das {@link GostKlausurvorgabe}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public vorgabeAdd(vorgabe : GostKlausurvorgabe) : void {
		this.vorgabeAddAll(ListUtils.create1(vorgabe));
	}

	private vorgabeAddAllOhneUpdate(list : List<GostKlausurvorgabe>) : void {
		const setOfIDs : HashSet<number> = new HashSet();
		for (const vorgabe of list) {
			GostKlausurvorgabenManager.vorgabeCheck(vorgabe);
			DeveloperNotificationException.ifTrue("vorgabeAddAllOhneUpdate: ID=" + vorgabe.idVorgabe + " existiert bereits!", this._vorgabe_by_id.containsKey(vorgabe.idVorgabe));
			DeveloperNotificationException.ifTrue("vorgabeAddAllOhneUpdate: ID=" + vorgabe.idVorgabe + " doppelt in der Liste!", !setOfIDs.add(vorgabe.idVorgabe));
		}
		for (const vorgabe of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._vorgabe_by_id, vorgabe.idVorgabe, vorgabe);
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
		DeveloperNotificationException.ifInvalidID("vorgabe.idVorgabe", vorgabe.idVorgabe);
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
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public vorgabeGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurvorgabe> {
		if (quartal === 0) {
			const vorgaben : List<GostKlausurvorgabe> | null = new ArrayList();
			if (this._vorgabenmenge_by_halbjahr_and_quartal.containsKey1AndKey2(abiJahrgang, halbjahr.id))
				for (const vQuartal of this._vorgabenmenge_by_halbjahr_and_quartal.getNonNullValuesOfMap3AsList(abiJahrgang, halbjahr.id)) {
					vorgaben.addAll(vQuartal);
				}
			return vorgaben;
		}
		const vorgaben : List<GostKlausurvorgabe> | null = this._vorgabenmenge_by_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, quartal);
		return vorgaben !== null ? vorgaben : new ArrayList();
	}

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal     das Quartal
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, kursartAllg : GostKursart, idFach : number) : GostKlausurvorgabe | null {
		return this._vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach.getOrNull(abiJahrgang, halbjahr.id, quartal, kursartAllg.kuerzel, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal     das Quartal, wenn 0, Vorgaben für alle Quartale
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public vorgabeGetMengeByHalbjahrAndQuartalAndKursartallgAndFachid(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, kursartAllg : GostKursart, idFach : number) : List<GostKlausurvorgabe> {
		if (quartal > 0) {
			const retList : List<GostKlausurvorgabe> | null = new ArrayList();
			const vorgabe : GostKlausurvorgabe | null = this.vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(abiJahrgang, halbjahr, quartal, kursartAllg, idFach);
			if (vorgabe !== null)
				retList.add(vorgabe);
			return retList;
		}
		return this.vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(abiJahrgang, halbjahr, kursartAllg, idFach);
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gost-Halbjahr
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(abiJahrgang : number, halbjahr : GostHalbjahr, kursartAllg : GostKursart, idFach : number) : List<GostKlausurvorgabe> {
		const list : List<GostKlausurvorgabe> | null = this._vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.getOrNull(abiJahrgang, halbjahr.id, kursartAllg.kuerzel, idFach);
		return list !== null ? list : new ArrayList();
	}

	/**
	 * Gibt die das Klausurvorgabe-Objekt zum übergebenen Parameter zurück (vorhergehendes Quartal des aktuellen Schuljahres).
	 *
	 * @param vorgabe das Klausurvorgabe-Objekt, dessen Vorgänger gesucht ist.
	 *
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public getPrevious(vorgabe : GostKlausurvorgabe) : GostKlausurvorgabe | null {
		const vorgabenSchuljahr : List<GostKlausurvorgabe> | null = this._vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.getNonNullOrException(vorgabe.abiJahrgang, vorgabe.halbjahr, vorgabe.kursart, vorgabe.idFach);
		if (vorgabe.halbjahr % 2 === 1) {
			const vorgabenVorhalbjahr : List<GostKlausurvorgabe> | null = this._vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach.getOrNull(vorgabe.abiJahrgang, vorgabe.halbjahr - 1, vorgabe.kursart, vorgabe.idFach);
			if (vorgabenVorhalbjahr !== null)
				vorgabenSchuljahr.addAll(vorgabenVorhalbjahr);
		}
		vorgabenSchuljahr.sort(this._compVorgabe);
		let listIndex : number = vorgabenSchuljahr.indexOf(vorgabe);
		if (listIndex === 0)
			return null;
		return vorgabenSchuljahr.get(listIndex - 1);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurvorgabenManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurvorgabenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_GostKlausurvorgabenManager(obj : unknown) : GostKlausurvorgabenManager {
	return obj as GostKlausurvorgabenManager;
}
