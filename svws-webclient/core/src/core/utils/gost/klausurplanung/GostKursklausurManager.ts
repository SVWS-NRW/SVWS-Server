import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap2D } from '../../../../core/adt/map/HashMap2D';
import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import type { JavaSet } from '../../../../java/util/JavaSet';
import { GostFaecherManager } from '../../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { JavaString } from '../../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { DateUtils } from '../../../../core/utils/DateUtils';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import type { Comparator } from '../../../../java/util/Comparator';
import { Map3DUtils } from '../../../../core/utils/Map3DUtils';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';
import { HashMap3D } from '../../../../core/adt/map/HashMap3D';
import { HashSet } from '../../../../java/util/HashSet';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { StundenplanManager } from '../../../../core/utils/stundenplan/StundenplanManager';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { MapUtils } from '../../../../core/utils/MapUtils';
import { StundenplanZeitraster } from '../../../../core/data/stundenplan/StundenplanZeitraster';
import { Map2DUtils } from '../../../../core/utils/Map2DUtils';
import { GostKlausurvorgabenManager } from '../../../../core/utils/gost/klausurplanung/GostKlausurvorgabenManager';
import { GostKlausurvorgabe } from '../../../../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { ListUtils } from '../../../../core/utils/ListUtils';
import { Wochentag } from '../../../../core/types/Wochentag';
import type { JavaMap } from '../../../../java/util/JavaMap';

export class GostKursklausurManager extends JavaObject {

	private _vorgabenManager : GostKlausurvorgabenManager;

	private static readonly _compTermin : Comparator<GostKlausurtermin> = { compare : (a: GostKlausurtermin, b: GostKlausurtermin) => {
		if (a.datum === null && b.datum !== null)
			return +1;
		if (b.datum === null && a.datum !== null)
			return -1;
		if (a.datum !== null && b.datum !== null)
			return JavaString.compareTo(a.datum, b.datum);
		if (a.quartal !== b.quartal)
			return a.quartal - b.quartal;
		return a.id > b.id ? +1 : -1;
	} };

	private readonly _compKursklausur : Comparator<GostKursklausur> = { compare : (a: GostKursklausur, b: GostKursklausur) => {
		let faecherManager : GostFaecherManager | null = this._vorgabenManager.getFaecherManager();
		if (JavaString.compareTo(a.kursart, b.kursart) < 0)
			return +1;
		if (JavaString.compareTo(a.kursart, b.kursart) > 0)
			return -1;
		if (faecherManager !== null) {
			const aFach : GostFach | null = faecherManager.get(a.idFach);
			const bFach : GostFach | null = faecherManager.get(b.idFach);
			if (aFach !== null && bFach !== null) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		if (a.halbjahr !== b.halbjahr)
			return a.halbjahr - b.halbjahr;
		if (a.quartal !== b.quartal)
			return a.quartal - b.quartal;
		return a.id > b.id ? +1 : -1;
	} };

	private readonly _compSchuelerklausur : Comparator<GostSchuelerklausur> = { compare : (a: GostSchuelerklausur, b: GostSchuelerklausur) => {
		let faecherManager : GostFaecherManager | null = this._vorgabenManager.getFaecherManager();
		let aV : GostKlausurvorgabe | null = this.vorgabeBySchuelerklausur(a);
		let bV : GostKlausurvorgabe | null = this.vorgabeBySchuelerklausur(b);
		if (aV.quartal !== bV.quartal)
			return aV.quartal - bV.quartal;
		if (JavaString.compareTo(aV.kursart, bV.kursart) < 0)
			return +1;
		if (JavaString.compareTo(aV.kursart, bV.kursart) > 0)
			return -1;
		if (faecherManager !== null) {
			const aFach : GostFach | null = faecherManager.get(aV.idFach);
			const bFach : GostFach | null = faecherManager.get(bV.idFach);
			if (aFach !== null && bFach !== null) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		return a.id > b.id ? +1 : -1;
	} };

	private readonly _kursklausur_by_id : JavaMap<number, GostKursklausur> = new HashMap();

	private readonly _kursklausurmenge : List<GostKursklausur> = new ArrayList();

	private readonly _kursklausurmenge_by_halbjahr_and_quartal : HashMap2D<number, number, List<GostKursklausur>> = new HashMap2D();

	private readonly _kursklausurmenge_by_idTermin : JavaMap<number, List<GostKursklausur>> = new HashMap();

	private readonly _kursklausurmenge_by_idVorgabe : JavaMap<number, List<GostKursklausur>> = new HashMap();

	private readonly _kursklausurmenge_by_halbjahr_and_idTermin_and_quartal : HashMap3D<number, number, number, List<GostKursklausur>> = new HashMap3D();

	private readonly _kursklausur_by_idKurs_and_halbjahr_and_quartal : HashMap3D<number, number, number, GostKursklausur> = new HashMap3D();

	private readonly _kursklausurmenge_by_kw_and_schuelerId : HashMap2D<number, number, List<GostKursklausur>> = new HashMap2D();

	private readonly _kursklausurmenge_by_terminId_and_schuelerId : HashMap2D<number, number, List<GostKursklausur>> = new HashMap2D();

	private readonly _termin_by_id : JavaMap<number, GostKlausurtermin> = new HashMap();

	private readonly _terminmenge : List<GostKlausurtermin> = new ArrayList();

	private readonly _terminmenge_by_halbjahr_and_quartal : HashMap2D<number, number, List<GostKlausurtermin>> = new HashMap2D();

	private readonly _terminmenge_by_datum : JavaMap<string, List<GostKlausurtermin>> = new HashMap();

	private readonly _schuelerIds_by_idTermin : JavaMap<number, List<number>> = new HashMap();

	private readonly _schuelerklausur_by_id : JavaMap<number, GostSchuelerklausur> = new HashMap();

	private readonly _schuelerklausurmenge : List<GostSchuelerklausur> = new ArrayList();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param vorgabenManager der Klausurvorgaben-Manager
	 * @param listKlausuren   die Liste der GostKursklausuren eines Abiturjahrgangs
	 * @param listTermine     die Liste der GostKlausurtermine eines Abiturjahrgangs
	 * @param listSchuelerklausuren   die Liste der GostSchuelerklausuren eines Abiturjahrgangs
	 */
	public constructor(vorgabenManager : GostKlausurvorgabenManager, listKlausuren : List<GostKursklausur>, listTermine : List<GostKlausurtermin> | null, listSchuelerklausuren : List<GostSchuelerklausur> | null) {
		super();
		this._vorgabenManager = vorgabenManager;
		this.initAll(listKlausuren, listTermine, listSchuelerklausuren);
	}

	private initAll(listKlausuren : List<GostKursklausur>, listTermine : List<GostKlausurtermin> | null, listSchuelerklausuren : List<GostSchuelerklausur> | null) : void {
		this.kursklausurAddAll(listKlausuren);
		if (listTermine !== null)
			this.terminAddAll(listTermine);
		if (listSchuelerklausuren !== null)
			this.schuelerklausurAddAll(listSchuelerklausuren);
		this.update_all();
	}

	private update_all() : void {
		this.update_kursklausurmenge();
		this.update_terminmenge();
		this.update_schuelerklausurmenge();
		this.update_kursklausurmenge_by_halbjahr_and_quartal();
		this.update_kursklausurmenge_by_idTermin();
		this.update_kursklausurmenge_by_idVorgabe();
		this.update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin();
		this.update_kursklausur_by_idKurs_and_halbjahr_and_quartal();
		this.update_terminmenge_by_halbjahr_and_quartal();
		this.update_terminmenge_by_datum();
		this.update_kursklausurmenge_by_terminId_and_schuelerId();
		this.update_schuelerIds_by_idTermin();
		this.update_kursklausurmenge_by_kw_and_schuelerId();
	}

	private update_kursklausurmenge_by_halbjahr_and_quartal() : void {
		this._kursklausurmenge_by_halbjahr_and_quartal.clear();
		for (const kk of this._kursklausurmenge) {
			Map2DUtils.getOrCreateArrayList(this._kursklausurmenge_by_halbjahr_and_quartal, kk.halbjahr, kk.quartal).add(kk);
		}
	}

	private update_kursklausurmenge_by_idTermin() : void {
		this._kursklausurmenge_by_idTermin.clear();
		for (const kk of this._kursklausurmenge)
			MapUtils.getOrCreateArrayList(this._kursklausurmenge_by_idTermin, kk.idTermin !== null ? kk.idTermin : -1).add(kk);
	}

	private update_kursklausurmenge_by_idVorgabe() : void {
		this._kursklausurmenge_by_idVorgabe.clear();
		for (const kk of this._kursklausurmenge)
			MapUtils.getOrCreateArrayList(this._kursklausurmenge_by_idVorgabe, kk.idVorgabe).add(kk);
	}

	private update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin() : void {
		this._kursklausurmenge_by_halbjahr_and_idTermin_and_quartal.clear();
		for (const kk of this._kursklausurmenge) {
			Map3DUtils.getOrCreateArrayList(this._kursklausurmenge_by_halbjahr_and_idTermin_and_quartal, kk.halbjahr, kk.idTermin !== null ? kk.idTermin : -1, kk.quartal).add(kk);
		}
	}

	private update_kursklausur_by_idKurs_and_halbjahr_and_quartal() : void {
		this._kursklausur_by_idKurs_and_halbjahr_and_quartal.clear();
		for (const kk of this._kursklausurmenge)
			this._kursklausur_by_idKurs_and_halbjahr_and_quartal.put(kk.idKurs, kk.halbjahr, kk.quartal, kk);
	}

	private update_terminmenge_by_halbjahr_and_quartal() : void {
		this._terminmenge_by_halbjahr_and_quartal.clear();
		for (const t of this._terminmenge)
			Map2DUtils.getOrCreateArrayList(this._terminmenge_by_halbjahr_and_quartal, t.halbjahr, t.quartal).add(t);
	}

	private update_terminmenge_by_datum() : void {
		this._terminmenge_by_datum.clear();
		for (const t of this._terminmenge)
			MapUtils.getOrCreateArrayList(this._terminmenge_by_datum, t.datum).add(t);
	}

	private update_schuelerIds_by_idTermin() : void {
		this._schuelerIds_by_idTermin.clear();
		for (const t of this._terminmenge) {
			const listSchuelerIds : ArrayList<number> | null = new ArrayList();
			this._schuelerIds_by_idTermin.put(t.id, listSchuelerIds);
			const klausurenZuTermin : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(t.id);
			if (klausurenZuTermin !== null)
				for (const k of klausurenZuTermin)
					listSchuelerIds.addAll(k.schuelerIds);
		}
	}

	private update_kursklausurmenge_by_kw_and_schuelerId() : void {
		this._kursklausurmenge_by_kw_and_schuelerId.clear();
		for (const t of this._terminmenge) {
			if (t.datum === null)
				continue;
			let kw : number = DateUtils.gibKwDesDatumsISO8601(t.datum);
			let klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(t.id);
			if (klausuren !== null)
				for (const kk of klausuren) {
					for (const sId of kk.schuelerIds)
						Map2DUtils.getOrCreateArrayList(this._kursklausurmenge_by_kw_and_schuelerId, kw, sId).add(kk);
				}
		}
	}

	private update_kursklausurmenge_by_terminId_and_schuelerId() : void {
		this._kursklausurmenge_by_terminId_and_schuelerId.clear();
		for (const kk of this._kursklausurmenge) {
			for (const sId of kk.schuelerIds)
				Map2DUtils.getOrCreateArrayList(this._kursklausurmenge_by_terminId_and_schuelerId, kk.idTermin, sId).add(kk);
		}
	}

	private update_kursklausurmenge() : void {
		this._kursklausurmenge.clear();
		this._kursklausurmenge.addAll(this._kursklausur_by_id.values());
		this._kursklausurmenge.sort(this._compKursklausur);
	}

	/**
	 * Fügt ein {@link GostKursklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostKursklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public kursklausurAdd(kursklausur : GostKursklausur) : void {
		this.kursklausurAddAll(ListUtils.create1(kursklausur));
		this.update_all();
	}

	private kursklausurAddAllOhneUpdate(list : List<GostKursklausur>) : void {
		const setOfIDs : HashSet<number> = new HashSet();
		for (const klausur of list) {
			GostKursklausurManager.kursklausurCheck(klausur);
			DeveloperNotificationException.ifTrue("kursklausurAddAllOhneUpdate: ID=" + klausur.id + " existiert bereits!", this._kursklausur_by_id.containsKey(klausur.id));
			DeveloperNotificationException.ifTrue("kursklausurAddAllOhneUpdate: ID=" + klausur.id + " doppelt in der Liste!", !setOfIDs.add(klausur.id));
		}
		for (const klausur of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._kursklausur_by_id, klausur.id, klausur);
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public kursklausurAddAll(listKursklausuren : List<GostKursklausur>) : void {
		this.kursklausurAddAllOhneUpdate(listKursklausuren);
		this.update_all();
	}

	private static kursklausurCheck(kursklausur : GostKursklausur) : void {
		DeveloperNotificationException.ifInvalidID("kursklausur.id", kursklausur.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKursklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idKursklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKursklausur}-Objekt.
	 */
	public kursklausurGetByIdOrException(idKursklausur : number) : GostKursklausur {
		return DeveloperNotificationException.ifMapGetIsNull(this._kursklausur_by_id, idKursklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public kursklausurGetMengeAsList() : List<GostKursklausur> {
		return this._kursklausurmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public kursklausurPatchAttributes(kursklausur : GostKursklausur) : void {
		GostKursklausurManager.kursklausurCheck(kursklausur);
		DeveloperNotificationException.ifMapRemoveFailes(this._kursklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._kursklausur_by_id, kursklausur.id, kursklausur);
		this.update_all();
	}

	private kursklausurRemoveOhneUpdateById(idKursklausur : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._kursklausur_by_id, idKursklausur);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idKursklausur Die ID des {@link GostKursklausur}-Objekts.
	 */
	public kursklausurRemoveById(idKursklausur : number) : void {
		this.kursklausurRemoveOhneUpdateById(idKursklausur);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public kursklausurRemoveAll(listKursklausuren : List<GostKursklausur>) : void {
		for (const kursklausur of listKursklausuren)
			this.kursklausurRemoveOhneUpdateById(kursklausur.id);
		this.update_all();
	}

	private update_terminmenge() : void {
		this._terminmenge.clear();
		this._terminmenge.addAll(this._termin_by_id.values());
		this._terminmenge.sort(GostKursklausurManager._compTermin);
	}

	/**
	 * Fügt ein {@link GostKlausurtermin}-Objekt hinzu.
	 *
	 * @param termin Das {@link GostKlausurtermin}-Objekt, welches hinzugefügt
	 *               werden soll.
	 */
	public terminAdd(termin : GostKlausurtermin) : void {
		this.terminAddAll(ListUtils.create1(termin));
	}

	private terminAddAllOhneUpdate(list : List<GostKlausurtermin>) : void {
		const setOfIDs : HashSet<number> = new HashSet();
		for (const termin of list) {
			GostKursklausurManager.terminCheck(termin);
			DeveloperNotificationException.ifTrue("terminAddAllOhneUpdate: ID=" + termin.id + " existiert bereits!", this._termin_by_id.containsKey(termin.id));
			DeveloperNotificationException.ifTrue("terminAddAllOhneUpdate: ID=" + termin.id + " doppelt in der Liste!", !setOfIDs.add(termin.id));
		}
		for (const termin of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._termin_by_id, termin.id, termin);
	}

	/**
	 * Fügt alle {@link GostKlausurtermin}-Objekte hinzu.
	 *
	 * @param listTermine Die Menge der {@link GostKlausurtermin}-Objekte, welche
	 *                    hinzugefügt werden soll.
	 */
	public terminAddAll(listTermine : List<GostKlausurtermin>) : void {
		this.terminAddAllOhneUpdate(listTermine);
		this.update_all();
	}

	private static terminCheck(termin : GostKlausurtermin) : void {
		DeveloperNotificationException.ifInvalidID("termin.id", termin.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurtermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idTermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurtermin}-Objekt.
	 */
	public terminGetByIdOrException(idTermin : number) : GostKlausurtermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, idTermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurtermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurtermin}-Objekte.
	 */
	public terminGetMengeAsList() : List<GostKlausurtermin> {
		return this._terminmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurtermin}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param termin Das neue {@link GostKlausurtermin}-Objekt.
	 */
	public terminPatchAttributes(termin : GostKlausurtermin) : void {
		GostKursklausurManager.terminCheck(termin);
		DeveloperNotificationException.ifMapRemoveFailes(this._termin_by_id, termin.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._termin_by_id, termin.id, termin);
		this.update_all();
	}

	private terminRemoveOhneUpdateById(idTermin : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._termin_by_id, idTermin);
		const klausurenZuTermin : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		if (klausurenZuTermin !== null)
			for (const k of klausurenZuTermin)
				k.idTermin = null;
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurtermin}-Objekt.
	 *
	 * @param idTermin Die ID des {@link GostKlausurtermin}-Objekts.
	 */
	public terminRemoveById(idTermin : number) : void {
		this.terminRemoveOhneUpdateById(idTermin);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurtermin}-Objekte.
	 *
	 * @param listTermine Die Liste der zu entfernenden
	 *                    {@link GostKlausurtermin}-Objekte.
	 */
	public terminRemoveAll(listTermine : List<GostKlausurtermin>) : void {
		for (const termin of listTermine)
			this.terminRemoveOhneUpdateById(termin.id);
		this.update_all();
	}

	private update_schuelerklausurmenge() : void {
		this._schuelerklausurmenge.clear();
		this._schuelerklausurmenge.addAll(this._schuelerklausur_by_id.values());
		this._schuelerklausurmenge.sort(this._compSchuelerklausur);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostSchuelerklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public schuelerklausurAdd(kursklausur : GostSchuelerklausur) : void {
		this.schuelerklausurAddAll(ListUtils.create1(kursklausur));
		this.update_all();
	}

	private schuelerklausurAddAllOhneUpdate(list : List<GostSchuelerklausur>) : void {
		const setOfIDs : HashSet<number> = new HashSet();
		for (const klausur of list) {
			GostKursklausurManager.schuelerklausurCheck(klausur);
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + klausur.id + " existiert bereits!", this._schuelerklausur_by_id.containsKey(klausur.id));
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + klausur.id + " doppelt in der Liste!", !setOfIDs.add(klausur.id));
		}
		for (const klausur of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausur_by_id, klausur.id, klausur);
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public schuelerklausurAddAll(listKursklausuren : List<GostSchuelerklausur>) : void {
		this.schuelerklausurAddAllOhneUpdate(listKursklausuren);
		this.update_all();
	}

	private static schuelerklausurCheck(kursklausur : GostSchuelerklausur) : void {
		DeveloperNotificationException.ifInvalidID("schuelerklausur.idSchuelerklausur", kursklausur.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKursklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idKursklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKursklausur}-Objekt.
	 */
	public schuelerklausurGetByIdOrException(idKursklausur : number) : GostSchuelerklausur {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausur_by_id, idKursklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public schuelerklausurGetMengeAsList() : List<GostSchuelerklausur> {
		return new ArrayList(this._schuelerklausurmenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public schuelerklausurPatchAttributes(kursklausur : GostSchuelerklausur) : void {
		GostKursklausurManager.schuelerklausurCheck(kursklausur);
		DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausur_by_id, kursklausur.id, kursklausur);
		this.update_all();
	}

	private schuelerklausurRemoveOhneUpdateById(idKursklausur : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausur_by_id, idKursklausur);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idKursklausur Die ID des {@link GostKursklausur}-Objekts.
	 */
	public schuelerklausurRemoveById(idKursklausur : number) : void {
		this.schuelerklausurRemoveOhneUpdateById(idKursklausur);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public schuelerklausurRemoveAll(listKursklausuren : List<GostSchuelerklausur>) : void {
		for (const kursklausur of listKursklausuren)
			this.schuelerklausurRemoveOhneUpdateById(kursklausur.id);
		this.update_all();
	}

	/**
	 * Liefert das GostKursklausur-Objekt zum übergebenen Termin und Kurs
	 *
	 * @param idTermin die ID des Klausurtermins
	 * @param idKurs   die ID des Kurses
	 *
	 * @return das GostKursklausur-Objekt
	 */
	public kursklausurGetByTerminidAndKursid(idTermin : number, idKurs : number) : GostKursklausur | null {
		const klausuren : List<GostKursklausur> | null = this.kursklausurGetMengeByTerminid(idTermin);
		for (const klaus of klausuren) {
			if (klaus.idKurs === idKurs)
				return klaus;
		}
		return null;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum das Datum der Klausurtermine im Format YYYY-MM-DD
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGetMengeByDatum(datum : string) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = this._terminmenge_by_datum.get(datum);
		return termine !== null ? termine : new ArrayList();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum   das Datum der Klausurtermine
	 * @param zr      Zeitraster
	 * @param manager Manager
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGetMengeByDatumAndZeitraster(datum : string, zr : StundenplanZeitraster, manager : StundenplanManager) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = this.terminGetMengeByDatum(datum);
		const retList : List<GostKlausurtermin> | null = new ArrayList();
		for (const termin of termine) {
			const zrsTermin : List<StundenplanZeitraster> | null = manager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(zr.wochentag), DeveloperNotificationException.ifNull("Startzeit des Klausurtermins", termin.startzeit)!, this.maxKlausurdauerGetByTerminid(termin.id));
			for (const zrTermin of zrsTermin)
				if (zrTermin !== null && zrTermin.id === zr.id)
					retList.add(termin);
		}
		return retList;
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Termin
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurGetMengeByTerminid(idTermin : number | null) : List<GostKursklausur> {
		const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin !== null ? idTermin : -1);
		return klausuren !== null ? klausuren : new ArrayList();
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal
	 *
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurGetMengeByHalbjahrAndQuartal(halbjahr : GostHalbjahr, quartal : number) : List<GostKursklausur> | null {
		return this._kursklausurmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, quartal);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten für die noch kein Termin /
	 * Schiene gesetzt wurde
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurOhneTerminGetMenge() : List<GostKursklausur> {
		return this.kursklausurGetMengeByTerminid(-1);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 *
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurOhneTerminGetMengeByHalbjahrAndQuartal(halbjahr : GostHalbjahr, quartal : number) : List<GostKursklausur> {
		if (quartal > 0) {
			const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_halbjahr_and_idTermin_and_quartal.getOrNull(halbjahr.id, -1, quartal);
			return klausuren !== null ? klausuren : new ArrayList();
		}
		const klausuren : List<GostKursklausur> | null = new ArrayList();
		for (const kl of this._kursklausurmenge_by_halbjahr_and_idTermin_and_quartal.getNonNullValuesOfMap3AsList(halbjahr.id, -1)) {
			klausuren.addAll(kl);
		}
		return klausuren;
	}

	/**
	 * Gibt eine Liste von Schüler-IDs zurück, die vom übergebenen Termin betroffen
	 * sind.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der betroffenen Schüler-IDs
	 */
	public schueleridsGetMengeByTerminid(idTermin : number) : List<number> {
		const schuelerIds : List<number> | null = this._schuelerIds_by_idTermin.get(idTermin);
		return schuelerIds !== null ? schuelerIds : new ArrayList();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Quartal
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals, 0 für alle Quartale
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGetMengeByHalbjahrAndQuartal(halbjahr : GostHalbjahr, quartal : number, includeMultiquartal : boolean) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = new ArrayList();
		if (quartal > 0) {
			if (this._terminmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, quartal) !== null)
				termine.addAll(this._terminmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, quartal));
			if (includeMultiquartal && this._terminmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, 0) !== null)
				termine.addAll(this._terminmenge_by_halbjahr_and_quartal.getOrNull(halbjahr.id, 0));
			return termine;
		}
		if (this._terminmenge_by_halbjahr_and_quartal.containsKey1(halbjahr.id))
			for (let qTermine of this._terminmenge_by_halbjahr_and_quartal.getNonNullValuesOfKey1AsList(halbjahr.id)) {
				termine.addAll(qTermine);
			}
		return termine;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Halbjahres, bei denen
	 * ein Datum gesetzt ist
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminMitDatumGetMenge() : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList();
		for (const termin of this._terminmenge)
			if (termin.datum !== null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(GostKursklausurManager._compTermin);
		return termineMitDatum;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Quartals, bei denen ein
	 * Datum gesetzt ist
	 *
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminMitDatumGetMengeByHalbjahrAndQuartal(halbjahr : GostHalbjahr, quartal : number, includeMultiquartal : boolean) : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList();
		for (const termin of this.terminGetMengeByHalbjahrAndQuartal(halbjahr, quartal, includeMultiquartal))
			if (termin.datum !== null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(GostKursklausurManager._compTermin);
		return termineMitDatum;
	}

	/**
	 * Liefert das Quartal der Kursklausuren innerhalb des Klausurtermins, sofern
	 * alle identisch sind, sonst -1.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return das Quartal aller Klausuren, sofern identisch, sonst -1.
	 */
	public quartalGetByTerminid(idTermin : number) : number {
		const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		if (klausuren === null)
			return DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, idTermin).quartal;
		let quartal : number = -1;
		for (const k of klausuren) {
			if (quartal === -1)
				quartal = k.quartal;
			if (quartal !== k.quartal)
				return -1;
		}
		return quartal;
	}

	/**
	 * Liefert die Anzahl der Schülerklausuren zu einem bestimmten Klausurtermin.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Anzahl der Schülerklausuren des Termins.
	 */
	public schuelerklausurAnzahlGetByTerminid(idTermin : number) : number {
		return this.schueleridsGetMengeByTerminid(idTermin).size();
	}

	/**
	 * Liefert die minimale Startzeit des Klausurtermins in Minuten
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die minimale Startzeit
	 */
	public minKursklausurstartzeitByTerminid(idTermin : number) : number {
		let minStart : number = 1440;
		const termin : GostKlausurtermin | null = DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, idTermin);
		for (const kk of DeveloperNotificationException.ifMapGetIsNull(this._kursklausurmenge_by_idTermin, idTermin)) {
			let skStartzeit : number = -1;
			if (kk.startzeit !== null)
				skStartzeit = kk.startzeit.valueOf();
			else
				if (termin.startzeit !== null)
					skStartzeit = termin.startzeit.valueOf();
				else
					throw new DeveloperNotificationException("Startzeit des Termins nicht definiert, Termin-ID: " + idTermin)
			if (skStartzeit < minStart)
				minStart = skStartzeit;
		}
		return minStart;
	}

	/**
	 * Liefert die maximale Endzeit des Klausurtermins in Minuten
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die maximale Endzeit
	 */
	public maxKursklausurendzeitByTerminid(idTermin : number) : number {
		let maxEnd : number = 0;
		const termin : GostKlausurtermin | null = DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, idTermin);
		for (const kk of DeveloperNotificationException.ifMapGetIsNull(this._kursklausurmenge_by_idTermin, idTermin)) {
			let vorgabe : GostKlausurvorgabe = this._vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
			let skStartzeit : number = -1;
			if (kk.startzeit !== null)
				skStartzeit = kk.startzeit.valueOf();
			else
				if (termin.startzeit !== null)
					skStartzeit = termin.startzeit.valueOf();
				else
					throw new DeveloperNotificationException("Startzeit des Termins nicht definiert, Termin-ID: " + idTermin)
			const endzeit : number = skStartzeit + vorgabe.dauer + vorgabe.auswahlzeit;
			if (endzeit > maxEnd)
				maxEnd = endzeit;
		}
		return maxEnd;
	}

	/**
	 * Liefert die maximale Klausurdauer innerhalb eines Klausurtermins
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die maximale Klausurdauer innerhalb des Termins
	 */
	public maxKlausurdauerGetByTerminid(idTermin : number) : number {
		const klausuren : List<GostKursklausur> = DeveloperNotificationException.ifMapGetIsNull(this._kursklausurmenge_by_idTermin, idTermin);
		let maxDauer : number = -1;
		for (const klausur of klausuren) {
			let vorgabe : GostKlausurvorgabe = this._vorgabenManager.vorgabeGetByIdOrException(klausur.idVorgabe);
			maxDauer = vorgabe.dauer > maxDauer ? vorgabe.dauer : maxDauer;
		}
		return maxDauer;
	}

	private static berechneKonflikte(klausuren1 : List<GostKursklausur>, klausuren2 : List<GostKursklausur>) : JavaMap<GostKursklausur, JavaSet<number>> {
		const result : JavaMap<GostKursklausur, JavaSet<number>> | null = new HashMap();
		const kursklausuren2Copy : List<GostKursklausur> | null = new ArrayList(klausuren2);
		for (const kk1 of klausuren1) {
			kursklausuren2Copy.remove(kk1);
			for (const kk2 of kursklausuren2Copy) {
				const konflikte : JavaSet<number> | null = GostKursklausurManager.berechneKlausurKonflikte(kk1, kk2);
				if (!konflikte.isEmpty()) {
					MapUtils.getOrCreateHashSet(result, kk1).addAll(konflikte);
					MapUtils.getOrCreateHashSet(result, kk2).addAll(konflikte);
				}
			}
		}
		return result;
	}

	private static berechneKlausurKonflikte(kk1 : GostKursklausur, kk2 : GostKursklausur) : JavaSet<number> {
		const konflikte : HashSet<number> | null = new HashSet(kk1.schuelerIds);
		konflikte.retainAll(kk2.schuelerIds);
		return konflikte;
	}

	private static countKonflikte(konflikte : JavaMap<GostKursklausur, JavaSet<number>>) : number {
		const susIds : HashSet<number> = new HashSet();
		for (const klausurSids of konflikte.values())
			susIds.addAll(klausurSids);
		return susIds.size();
	}

	/**
	 * Liefert eine Map Kursklausur -> Schülerids, die die Konflikte in jeder
	 * Klausur der übergebenen Termin-ID enthält
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Map Kursklausur -> Schülerids
	 */
	public konflikteMapKursklausurSchueleridsByTerminid(idTermin : number) : JavaMap<GostKursklausur, JavaSet<number>> {
		const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		if (klausuren === null)
			return new HashMap();
		return GostKursklausurManager.berechneKonflikte(klausuren, klausuren);
	}

	/**
	 * Liefert eine Map Kursklausur -> Schülerids, die nur die Konflikte liefert,
	 * die die übergeben Klausur im übergebenen Termin verursacht
	 *
	 * @param idTermin      die ID des Klausurtermins
	 * @param idKursklausur die ID der Kursklausur
	 *
	 * @return die Map Kursklausur -> Schülerids
	 */
	public konflikteNeuMapKursklausurSchueleridsByTerminidAndKursklausurid(idTermin : number, idKursklausur : number) : JavaMap<GostKursklausur, JavaSet<number>> {
		const klausuren1 : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		const klausuren2 : List<GostKursklausur> | null = new ArrayList();
		klausuren2.add(DeveloperNotificationException.ifMapGetIsNull(this._kursklausur_by_id, idKursklausur));
		return GostKursklausurManager.berechneKonflikte(klausuren1 !== null ? klausuren1 : new ArrayList(), klausuren2);
	}

	/**
	 * Prüft, ob eine Kursklausur konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Es werden die Schüler-IDs, die den Konflikt
	 * verursachen, als Liste zurückgegeben. Wenn die zurückgegebene Liste leer ist,
	 * gibt es keinen Konflikt.
	 *
	 * @param klausur die zu prüfende Kursklausur
	 *
	 * @return die Anzahl der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public konflikteAnzahlZuEigenemTerminGetByKursklausur(klausur : GostKursklausur) : number {
		const klausuren1 : List<GostKursklausur> = new ArrayList(DeveloperNotificationException.ifMapGetIsNull(this._kursklausurmenge_by_idTermin, klausur.idTermin));
		klausuren1.remove(klausur);
		const klausuren2 : List<GostKursklausur> | null = new ArrayList();
		klausuren2.add(klausur);
		return GostKursklausurManager.countKonflikte(GostKursklausurManager.berechneKonflikte(klausuren1, klausuren2));
	}

	/**
	 * Prüft, ob eine Kursklausur konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Es werden die Schüler-IDs, die den Konflikt
	 * verursachen, als Liste zurückgegeben. Wenn die zurückgegebene Liste leer ist,
	 * gibt es keinen Konflikt.
	 *
	 * @param termin  der zu prüfende Klausurtermin
	 * @param klausur die zu prüfende Kursklausur
	 *
	 * @return die Liste der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public konflikteAnzahlZuTerminGetByTerminAndKursklausur(termin : GostKlausurtermin, klausur : GostKursklausur) : number {
		return GostKursklausurManager.countKonflikte(this.konflikteNeuMapKursklausurSchueleridsByTerminidAndKursklausurid(termin.id, klausur.id));
	}

	/**
	 * Prüft, ob es innerhalb eines bestehenden Klausurtermins Konflikte gibt. Es
	 * wird die Anzahl der Konflikte zurückgegeben.
	 *
	 * @param idTermin die ID des zu prüfenden Klausurtermins
	 *
	 * @return die Anzahl der Konflikte innerhalb des Termins.
	 */
	public konflikteAnzahlGetByTerminid(idTermin : number) : number {
		return GostKursklausurManager.countKonflikte(this.konflikteMapKursklausurSchueleridsByTerminid(idTermin));
	}

	/**
	 * Liefert für einen Schwellwert und einen Klausurtermin eine Map, die alle
	 * Schülerids mit einer Kursklausur-Liste enthält, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreibt, als der Schwellwert
	 * definiert
	 *
	 * @param termin    der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param threshold der Schwellwert (z.B. 3), der erreicht sein muss, damit die
	 *                  Klausuren in die Map aufgenommen werden
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public klausurenProSchueleridExceedingKWThresholdByTerminAndThreshold(termin : GostKlausurtermin, threshold : number) : JavaMap<number, List<GostKursklausur>> {
		return this.klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(termin, null, threshold);
	}

	/**
	 * Liefert für einen Schwellwert, einen Klausurtermin und eine Kursklausur eine
	 * Map, die alle Schülerids mit einer Kursklausur-Liste enthält, die in der den
	 * Termin enthaltenen Kalenderwoche mehr (>=) Klausuren schreibt, als der
	 * Schwellwert definiert, wenn die übergebene Kursklausur in den Termin
	 * integriert würde
	 *
	 * @param termin    der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param klausur   die Klausur, deren Integration in den Termin angenommen wird
	 * @param threshold der Schwellwert (z.B. 3), der erreicht sein muss, damit die
	 *                  Klausuren berücksichtigt werden
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(termin : GostKlausurtermin, klausur : GostKursklausur | null, threshold : number) : JavaMap<number, List<GostKursklausur>> {
		let ergebnis : JavaMap<number, List<GostKursklausur>> | null = new HashMap();
		if (termin.datum === null)
			return ergebnis;
		let kw : number = DateUtils.gibKwDesDatumsISO8601(termin.datum);
		let kursklausurmenge_by_schuelerId : JavaMap<number, List<GostKursklausur> | null> | null = this._kursklausurmenge_by_kw_and_schuelerId.getSubMapOrNull(kw);
		if (kursklausurmenge_by_schuelerId === null)
			return ergebnis;
		for (let entry of kursklausurmenge_by_schuelerId.entrySet()) {
			let temp : List<GostKursklausur> | null = entry.getValue();
			let klausuren : List<GostKursklausur> | null = temp !== null ? new ArrayList(temp) : new ArrayList();
			if (klausur !== null && klausur.idTermin !== termin.id && klausur.schuelerIds.contains(entry.getKey()))
				klausuren.add(klausur);
			if (klausuren.size() >= threshold)
				ergebnis.put(entry.getKey(), klausuren);
		}
		return ergebnis;
	}

	/**
	 * Liefert für einen Schwellwert und einen Klausurtermin eine Map, die alle
	 * Schülerids mit einer Kursklausur-Liste enthält, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreibt, als der Schwellwert
	 * definiert
	 *
	 * @param termin        der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param datum         das Datum, auf
	 * @param threshold     der Schwellwert (z.B. 3), der erreicht sein muss, damit
	 *                      die Klausuren in die Map aufgenommen werden
	 * @param thresholdOnly nur die exakte Anzahl an Klausurkonflikten wird in die
	 *                      Ergebnismap übernommen
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(termin : GostKlausurtermin, datum : string, threshold : number, thresholdOnly : boolean) : JavaMap<number, HashSet<GostKursklausur>> {
		let kwDatum : number = DateUtils.gibKwDesDatumsISO8601(datum);
		return this.klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(kwDatum, termin, threshold, thresholdOnly);
	}

	private klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(kw : number, termin : GostKlausurtermin | null, threshold : number, thresholdOnly : boolean) : JavaMap<number, HashSet<GostKursklausur>> {
		let ergebnis : JavaMap<number, HashSet<GostKursklausur>> | null = new HashMap();
		let kursklausurmenge_by_schuelerId : JavaMap<number, List<GostKursklausur> | null> | null = this._kursklausurmenge_by_kw_and_schuelerId.getSubMapOrNull(kw);
		if (kursklausurmenge_by_schuelerId === null)
			return ergebnis;
		for (let entry of kursklausurmenge_by_schuelerId.entrySet()) {
			let temp : List<GostKursklausur> | null = entry.getValue();
			let klausuren : HashSet<GostKursklausur> | null = temp !== null ? new HashSet(temp) : new HashSet();
			if (termin !== null) {
				let klausurenInTermin : List<GostKursklausur> | null = this._kursklausurmenge_by_terminId_and_schuelerId.getOrNull(termin.id, entry.getKey());
				if (klausurenInTermin !== null)
					klausuren.addAll(klausurenInTermin);
			}
			if (klausuren.size() === threshold || (klausuren.size() > threshold && !thresholdOnly))
				ergebnis.put(entry.getKey(), klausuren);
		}
		return ergebnis;
	}

	/**
	 * Liefert für einen Schwellwert und einen Klausurtermin eine Map, die alle
	 * Schülerids mit einer Kursklausur-Liste enthält, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreibt, als der Schwellwert
	 * definiert
	 *
	 * @param kw            der Klausurtermin, dessen Kalenderwoche geprüft wird
	 * @param threshold     der Schwellwert (z.B. 3), der erreicht sein muss, damit
	 *                      die Klausuren in die Map aufgenommen werden
	 * @param thresholdOnly nur die exakte Anzahl an Klausurkonflikten wird in die
	 *                      Ergebnismap übernommen
	 *
	 * @return die Map (Schülerid -> GostKursklausur)
	 */
	public klausurenProSchueleridExceedingKWThresholdByKwAndThreshold(kw : number, threshold : number, thresholdOnly : boolean) : JavaMap<number, HashSet<GostKursklausur>> {
		return this.klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(kw, null, threshold, thresholdOnly);
	}

	/**
	 * Liefert den Klausurtermin zu einer Kursklausur, sonst NULL.
	 *
	 * @param klausur die Kursklausur, zu der der Termin gesucht wird.
	 *
	 * @return den Klausurtermin
	 */
	public terminByKursklausur(klausur : GostKursklausur) : GostKlausurtermin | null {
		return this._termin_by_id.get(klausur.idTermin);
	}

	/**
	 * Liefert den Klausurtermin zu einem Schülerklausurtermin oder NULL.
	 *
	 * @param termin der Schülerklausurtermin, zu dem der Termin gesucht wird.
	 *
	 * @return den Klausurtermin
	 */
	public terminBySchuelerklausurTermin(termin : GostSchuelerklausurTermin) : GostKlausurtermin | null {
		if (termin.folgeNr > 0)
			return termin.idTermin === null ? null : this.terminGetByIdOrException(termin.idTermin);
		return this.terminByKursklausur(this.kursklausurBySchuelerklausurTermin(termin));
	}

	/**
	 * Liefert den Klausurtermin zu einer Schülerklausur oder NULL.
	 *
	 * @param sk die Schülerklausur
	 *
	 * @return den Klausurtermin
	 */
	public terminKursklausurBySchuelerklausur(sk : GostSchuelerklausur) : GostKlausurtermin | null {
		return this.terminByKursklausur(this.kursklausurBySchuelerklausur(sk));
	}

	/**
	 * Liefert die Klausurvorgabe zu einer Kursklausur.
	 *
	 * @param klausur die Kursklausur, zu der die Vorgabe gesucht wird.
	 *
	 * @return die Klausurvorgabe
	 */
	public vorgabeByKursklausur(klausur : GostKursklausur) : GostKlausurvorgabe {
		return this._vorgabenManager.vorgabeGetByIdOrException(klausur.idVorgabe);
	}

	/**
	 * Liefert die Klausurvorgabe zu einer Schuelerklausur.
	 *
	 * @param klausur die Schuelerklausur, zu der die Vorgabe gesucht wird.
	 *
	 * @return die Klausurvorgabe
	 */
	public vorgabeBySchuelerklausur(klausur : GostSchuelerklausur) : GostKlausurvorgabe {
		let kk : GostKursklausur = this.kursklausurGetByIdOrException(klausur.idKursklausur);
		return this._vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
	}

	/**
	 * Liefert die GostKursklausur zu einer Schuelerklausur.
	 *
	 * @param klausur die Schuelerklausur, zu der die GostKursklausur gesucht wird.
	 *
	 * @return die GostKursklausur
	 */
	public kursklausurBySchuelerklausur(klausur : GostSchuelerklausur) : GostKursklausur {
		return this.kursklausurGetByIdOrException(klausur.idKursklausur);
	}

	/**
	 * Liefert die GostKursklausur zu einem Schuelerklausurtermin.
	 *
	 * @param termin der Schuelerklausurtermin, zu der die GostKursklausur gesucht wird.
	 *
	 * @return die GostKursklausur
	 */
	public kursklausurBySchuelerklausurTermin(termin : GostSchuelerklausurTermin) : GostKursklausur {
		return this.kursklausurGetByIdOrException(termin.idKursklausur);
	}

	/**
	 * Liefert zurück, ob die übergebene Klausurvorgabe von einer Kursklausur
	 * verwendet wird.
	 *
	 * @param vorgabe die Klausurvorgabe, die auf Verwendung geprüft werden soll.
	 *
	 * @return true oder false
	 */
	public istVorgabeVerwendetByVorgabe(vorgabe : GostKlausurvorgabe) : boolean {
		let klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idVorgabe.get(vorgabe.idVorgabe);
		return klausuren !== null && !klausuren.isEmpty();
	}

	/**
	 * Liefert das GostKursklausur-Objekt zu den übergebenen Parametern.
	 *
	 * @param idKurs  die ID des Kurses
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal das Quartal der Klausur
	 *
	 * @return die Kursklausur
	 */
	public kursklausurByKursidAndHalbjahrAndQuartal(idKurs : number, halbjahr : GostHalbjahr, quartal : number) : GostKursklausur | null {
		return this._kursklausur_by_idKurs_and_halbjahr_and_quartal.getOrNull(idKurs, halbjahr.id, quartal);
	}

	/**
	 * Liefert die Vorgänger-GostKursklausur aus dem letzten Quartal, soweit
	 * vorhanden.
	 *
	 * @param klausur die Kursklausur, deren Vorgänger gesucht wird
	 *
	 * @return die Kursklausur
	 */
	public kursklausurVorterminByKursklausur(klausur : GostKursklausur) : GostKursklausur | null {
		let previousVorgabe : GostKlausurvorgabe | null = this._vorgabenManager.getPrevious(this._vorgabenManager.vorgabeGetByIdOrException(klausur.idVorgabe));
		if (previousVorgabe === null)
			return null;
		let klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idVorgabe.get(previousVorgabe.idVorgabe);
		if (klausuren === null)
			return null;
		for (let k of klausuren) {
			if (JavaObject.equalsTranspiler(k.kursKurzbezeichnung, (klausur.kursKurzbezeichnung)))
				return k;
		}
		return null;
	}

	/**
	 * Gibt die Startzeit der übergebenen Klausur aus. Falls keine individuelle gesetzt ist, wird die des Termins zurückgegeben.
	 * Sollte kein Termin gesetzt sein oder der Termin keine Startzeit definiert haben, wird null zurückgegeben.
	 *
	 * @param klausur die Kursklausur, deren Startzeit gesucht wird.
	 *
	 * @return die Startzeit der Klausur
	 */
	public startzeitByKursklausur(klausur : GostKursklausur) : number | null {
		let termin : GostKlausurtermin | null = this.terminByKursklausur(klausur);
		if (klausur.startzeit !== null)
			return klausur.startzeit;
		return termin === null ? null : termin.startzeit;
	}

	/**
	 * Gibt die Startzeit der übergebenen Klausur aus. Falls keine individuelle gesetzt ist, wird die des Termins zurückgegeben.
	 * Sollte kein Termin gesetzt sein oder der Termin keine Startzeit definiert haben, wird null zurückgegeben.
	 *
	 * @param klausur die Kursklausur, deren Startzeit gesucht wird.
	 *
	 * @return die Startzeit der Klausur
	 */
	public hatAbweichendeStartzeitByKursklausur(klausur : GostKursklausur) : boolean {
		let termin : GostKlausurtermin | null = this.terminByKursklausur(klausur);
		return !(klausur.startzeit === null || termin === null || termin.startzeit === null || JavaObject.equalsTranspiler(termin.startzeit, (klausur.startzeit)));
	}

	/**
	 * Prüft, ob der übergebene Schülerklausurtermin der aktuellste Termin der Schülerklausur ist.
	 *
	 * @param skt der Schülerklausurtermin, der geprüft werden soll
	 *
	 * @return true, wenn es sich um den aktuellen Termin handelt, sonst false
	 */
	public istAktuellerSchuelerklausurtermin(skt : GostSchuelerklausurTermin) : boolean {
		let skts : List<GostSchuelerklausurTermin | null> = this.schuelerklausurGetByIdOrException(skt.idSchuelerklausur).schuelerklausurTermine;
		return skts.get(skts.size() - 1) as unknown === skt as unknown;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.GostKursklausurManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.GostKursklausurManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_GostKursklausurManager(obj : unknown) : GostKursklausurManager {
	return obj as GostKursklausurManager;
}