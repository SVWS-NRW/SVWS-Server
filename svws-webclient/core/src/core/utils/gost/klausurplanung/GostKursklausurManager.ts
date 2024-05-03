import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap2D } from '../../../../core/adt/map/HashMap2D';
import { SchuelerListeEintrag } from '../../../../core/data/schueler/SchuelerListeEintrag';
import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import type { JavaSet } from '../../../../java/util/JavaSet';
import { GostFaecherManager } from '../../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { JavaString } from '../../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import { DateUtils } from '../../../../core/utils/DateUtils';
import { GostKlausurenUpdate } from '../../../../core/data/gost/klausurplanung/GostKlausurenUpdate';
import type { Comparator } from '../../../../java/util/Comparator';
import { Map3DUtils } from '../../../../core/utils/Map3DUtils';
import { KursDaten } from '../../../../core/data/kurse/KursDaten';
import { KursManager } from '../../../../core/utils/KursManager';
import { LehrerListeEintrag } from '../../../../core/data/lehrer/LehrerListeEintrag';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { ZulaessigesFach } from '../../../../core/types/fach/ZulaessigesFach';
import type { List } from '../../../../java/util/List';
import { cast_java_util_List } from '../../../../java/util/List';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';
import { HashMap3D } from '../../../../core/adt/map/HashMap3D';
import { HashSet } from '../../../../java/util/HashSet';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { StundenplanManager } from '../../../../core/utils/stundenplan/StundenplanManager';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { MapUtils } from '../../../../core/utils/MapUtils';
import { StundenplanZeitraster } from '../../../../core/data/stundenplan/StundenplanZeitraster';
import { Map2DUtils } from '../../../../core/utils/Map2DUtils';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import { GostKlausurvorgabenManager, cast_de_svws_nrw_core_utils_gost_klausurplanung_GostKlausurvorgabenManager } from '../../../../core/utils/gost/klausurplanung/GostKlausurvorgabenManager';
import { GostKlausurvorgabe } from '../../../../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { JavaLong } from '../../../../java/lang/JavaLong';
import { ListUtils } from '../../../../core/utils/ListUtils';
import { Wochentag } from '../../../../core/types/Wochentag';
import type { JavaMap } from '../../../../java/util/JavaMap';
import { HashMap4D } from '../../../../core/adt/map/HashMap4D';
import { Map4DUtils } from '../../../../core/utils/Map4DUtils';

export class GostKursklausurManager extends JavaObject {

	private _vorgabenManager : GostKlausurvorgabenManager;

	private _kursManager : KursManager | null = null;

	private _lehrerMap : JavaMap<number, LehrerListeEintrag> | null = null;

	private _schuelerMap : JavaMap<number, SchuelerListeEintrag> | null = null;

	private static readonly _compTermin : Comparator<GostKlausurtermin> = { compare : (a: GostKlausurtermin, b: GostKlausurtermin) => {
		if (a.datum !== null && b.datum !== null)
			return JavaString.compareTo(a.datum, b.datum);
		if (b.datum !== null)
			return +1;
		if (a.datum !== null)
			return -1;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _compKursklausur : Comparator<GostKursklausur> = { compare : (a: GostKursklausur, b: GostKursklausur) => {
		const faecherManager : GostFaecherManager | null = this._vorgabenManager.getFaecherManagerOrNull();
		const va : GostKlausurvorgabe = this.vorgabeByKursklausur(a);
		const vb : GostKlausurvorgabe = this.vorgabeByKursklausur(b);
		if (JavaString.compareTo(va.kursart, vb.kursart) < 0)
			return +1;
		if (JavaString.compareTo(va.kursart, vb.kursart) > 0)
			return -1;
		if (faecherManager !== null) {
			const aFach : GostFach | null = faecherManager.get(va.idFach);
			const bFach : GostFach | null = faecherManager.get(vb.idFach);
			if (aFach !== null && bFach !== null) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		if (va.halbjahr !== vb.halbjahr)
			return va.halbjahr - vb.halbjahr;
		if (va.quartal !== vb.quartal)
			return va.quartal - vb.quartal;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _compSchuelerklausur : Comparator<GostSchuelerklausur> = { compare : (a: GostSchuelerklausur, b: GostSchuelerklausur) => {
		const faecherManager : GostFaecherManager | null = this._vorgabenManager.getFaecherManagerOrNull();
		const aV : GostKlausurvorgabe | null = this.vorgabeBySchuelerklausur(a);
		const bV : GostKlausurvorgabe | null = this.vorgabeBySchuelerklausur(b);
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
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _compSchuelerklausurTermin : Comparator<GostSchuelerklausurTermin> = { compare : (a: GostSchuelerklausurTermin, b: GostSchuelerklausurTermin) => {
		if (a.idSchuelerklausur === b.idSchuelerklausur) {
			return JavaInteger.compare(a.folgeNr, b.folgeNr);
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _kursklausur_by_id : JavaMap<number, GostKursklausur> = new HashMap<number, GostKursklausur>();

	private readonly _kursklausurmenge : List<GostKursklausur> = new ArrayList<GostKursklausur>();

	private readonly _kursklausurmenge_by_halbjahr_and_quartal : HashMap3D<number, number, number, List<GostKursklausur>> = new HashMap3D<number, number, number, List<GostKursklausur>>();

	private readonly _kursklausurmenge_by_idTermin : JavaMap<number, List<GostKursklausur>> = new HashMap<number, List<GostKursklausur>>();

	private readonly _kursklausurmenge_by_idVorgabe : JavaMap<number, List<GostKursklausur>> = new HashMap<number, List<GostKursklausur>>();

	private readonly _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal : HashMap4D<number, number, number, number, List<GostKursklausur>> = new HashMap4D<number, number, number, number, List<GostKursklausur>>();

	private readonly _kursklausur_by_idKurs_and_abijahr_and_halbjahr_and_quartal : HashMap4D<number, number, number, number, GostKursklausur> = new HashMap4D<number, number, number, number, GostKursklausur>();

	private readonly _kursklausurmenge_by_kw_and_schuelerId : HashMap2D<number, number, List<GostKursklausur>> = new HashMap2D<number, number, List<GostKursklausur>>();

	private readonly _kursklausurmenge_by_terminId_and_schuelerId : HashMap2D<number, number, List<GostKursklausur>> = new HashMap2D<number, number, List<GostKursklausur>>();

	private readonly _termin_by_id : JavaMap<number, GostKlausurtermin> = new HashMap<number, GostKlausurtermin>();

	private readonly _terminmenge : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();

	private readonly _terminmenge_by_abijahr_and_halbjahr_and_quartal : HashMap3D<number, number, number, List<GostKlausurtermin>> = new HashMap3D<number, number, number, List<GostKlausurtermin>>();

	private readonly _terminmenge_by_datum_and_abijahr : HashMap2D<string, number, List<GostKlausurtermin>> = new HashMap2D<string, number, List<GostKlausurtermin>>();

	private readonly _schuelerklausur_by_id : JavaMap<number, GostSchuelerklausur> = new HashMap<number, GostSchuelerklausur>();

	private readonly _schuelerklausurmenge : List<GostSchuelerklausur> = new ArrayList<GostSchuelerklausur>();

	private readonly _schuelerklausurmenge_by_idKursklausur : JavaMap<number, List<GostSchuelerklausur>> = new HashMap<number, List<GostSchuelerklausur>>();

	private readonly _schuelerklausurtermin_by_id : JavaMap<number, GostSchuelerklausurTermin> = new HashMap<number, GostSchuelerklausurTermin>();

	private readonly _schuelerklausurterminmenge : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();

	private readonly _schuelerklausurterminmenge_by_idSchuelerklausur : JavaMap<number, List<GostSchuelerklausurTermin>> = new HashMap<number, List<GostSchuelerklausurTermin>>();

	private readonly _schuelerklausurterminmenge_by_idTermin : JavaMap<number, List<GostSchuelerklausurTermin>> = new HashMap<number, List<GostSchuelerklausurTermin>>();

	private readonly _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur : HashMap2D<number, number, List<GostSchuelerklausurTermin>> = new HashMap2D<number, number, List<GostSchuelerklausurTermin>>();

	private readonly _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin : HashMap4D<number, number, number, number, List<GostSchuelerklausurTermin>> = new HashMap4D<number, number, number, number, List<GostSchuelerklausurTermin>>();


	/**
	 * Erstellt einen leeren Manager.
	 */
	public constructor();

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param vorgabenManager der Klausurvorgaben-Manager
	 * @param listKlausuren   die Liste der GostKursklausuren eines Abiturjahrgangs
	 * @param listTermine     die Liste der GostKlausurtermine eines Abiturjahrgangs
	 * @param listSchuelerklausuren   die Liste der GostSchuelerklausuren eines Abiturjahrgangs
	 * @param listSchuelerklausurtermine   die Liste der GostSchuelerklausurTermine eines Abiturjahrgangs
	 */
	public constructor(vorgabenManager : GostKlausurvorgabenManager, listKlausuren : List<GostKursklausur>, listTermine : List<GostKlausurtermin> | null, listSchuelerklausuren : List<GostSchuelerklausur> | null, listSchuelerklausurtermine : List<GostSchuelerklausurTermin> | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : GostKlausurvorgabenManager, __param1? : List<GostKursklausur>, __param2? : List<GostKlausurtermin> | null, __param3? : List<GostSchuelerklausur> | null, __param4? : List<GostSchuelerklausurTermin> | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined")) {
			this._vorgabenManager = new GostKlausurvorgabenManager();
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurvorgabenManager')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && ((__param2 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && ((__param4 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param4 === null))) {
			const vorgabenManager : GostKlausurvorgabenManager = cast_de_svws_nrw_core_utils_gost_klausurplanung_GostKlausurvorgabenManager(__param0);
			const listKlausuren : List<GostKursklausur> = cast_java_util_List(__param1);
			const listTermine : List<GostKlausurtermin> | null = cast_java_util_List(__param2);
			const listSchuelerklausuren : List<GostSchuelerklausur> | null = cast_java_util_List(__param3);
			const listSchuelerklausurtermine : List<GostSchuelerklausurTermin> | null = cast_java_util_List(__param4);
			this._vorgabenManager = vorgabenManager;
			this.initAll(listKlausuren, listTermine, listSchuelerklausuren, listSchuelerklausurtermine);
		} else throw new Error('invalid method overload');
	}

	private initAll(listKlausuren : List<GostKursklausur>, listTermine : List<GostKlausurtermin> | null, listSchuelerklausuren : List<GostSchuelerklausur> | null, listSchuelerklausurtermine : List<GostSchuelerklausurTermin> | null) : void {
		this.kursklausurAddAllOhneUpdate(listKlausuren);
		if (listTermine !== null)
			this.terminAddAllOhneUpdate(listTermine);
		if (listSchuelerklausuren !== null)
			this.schuelerklausurAddAllOhneUpdate(listSchuelerklausuren);
		if (listSchuelerklausurtermine !== null)
			this.schuelerklausurterminAddAllOhneUpdate(listSchuelerklausurtermine);
		this.update_all();
	}

	/**
	 * Setzt den KursManager
	 *
	 * @param kursManager der KursManager
	 */
	public setKursManager(kursManager : KursManager) : void {
		this._kursManager = kursManager;
	}

	/**
	 * Liefert den KursManager, falls dieser gesetzt ist, sonst wird eine DeveloperNotificationException geworfen.
	 *
	 * @return den KursManager
	 */
	public getKursManager() : KursManager {
		if (this._kursManager === null)
			throw new DeveloperNotificationException("KursManager not set.")
		return this._kursManager;
	}

	/**
	 * Liefert den GostFaecherManager, falls dieser gesetzt ist, sonst wird eine DeveloperNotificationException geworfen.
	 *
	 * @return den GostFaecherManager
	 */
	public getFaecherManager() : GostFaecherManager {
		return this._vorgabenManager.getFaecherManager();
	}

	/**
	 * Setzt die LehrerMap
	 *
	 * @param lehrerMap die LehrerMap
	 */
	public setLehrerMap(lehrerMap : JavaMap<number, LehrerListeEintrag>) : void {
		this._lehrerMap = lehrerMap;
	}

	/**
	 * Liefert die LehrerMap, falls diese gesetzt ist, sonst wird eine DeveloperNotificationException geworfen.
	 *
	 * @return die LehrerMap
	 */
	public getLehrerMap() : JavaMap<number, LehrerListeEintrag> {
		if (this._lehrerMap === null)
			throw new DeveloperNotificationException("LehrerMap not set.")
		return this._lehrerMap;
	}

	/**
	 * Liefert die SchuelerMap, falls diese gesetzt ist, sonst wird eine DeveloperNotificationException geworfen.
	 *
	 * @return die SchuelerMap
	 */
	public getSchuelerMap() : JavaMap<number, SchuelerListeEintrag> {
		if (this._schuelerMap === null)
			throw new DeveloperNotificationException("SchuelerMap not set.")
		return this._schuelerMap;
	}

	/**
	 * Setzt die SchuelerMap
	 *
	 * @param schuelerMap die SchuelerMap
	 */
	public setSchuelerMap(schuelerMap : JavaMap<number, SchuelerListeEintrag>) : void {
		this._schuelerMap = schuelerMap;
	}

	private update_all() : void {
		this.update_kursklausurmenge();
		this.update_terminmenge();
		this.update_schuelerklausurmenge();
		this.update_schuelerklausurterminmenge();
		this.update_kursklausurmenge_by_halbjahr_and_quartal();
		this.update_kursklausurmenge_by_idTermin();
		this.update_kursklausurmenge_by_idVorgabe();
		this.update_schuelerklausurmenge_by_idKursklausur();
		this.update_schuelerklausurterminmenge_by_idSchuelerklausur();
		this.update_schuelerklausurterminmenge_by_idTermin();
		this.update_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur();
		this.update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin();
		this.update_kursklausur_by_idKurs_and_halbjahr_and_quartal();
		this.update_terminmenge_by_halbjahr_and_quartal();
		this.update_terminmenge_by_datum();
		this.update_kursklausurmenge_by_terminId_and_schuelerId();
		this.update_kursklausurmenge_by_kw_and_schuelerId();
		this.update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal();
	}

	private update_kursklausurmenge_by_halbjahr_and_quartal() : void {
		this._kursklausurmenge_by_halbjahr_and_quartal.clear();
		for (const kk of this._kursklausurmenge) {
			const v : GostKlausurvorgabe = this.vorgabeByKursklausur(kk);
			Map3DUtils.getOrCreateArrayList(this._kursklausurmenge_by_halbjahr_and_quartal, v.abiJahrgang, v.halbjahr, v.quartal).add(kk);
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
		this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.clear();
		for (const kk of this._kursklausurmenge) {
			const v : GostKlausurvorgabe = this.vorgabeByKursklausur(kk);
			Map4DUtils.getOrCreateArrayList(this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal, v.abiJahrgang, v.halbjahr, kk.idTermin !== null ? kk.idTermin : -1, v.quartal).add(kk);
		}
	}

	private update_kursklausur_by_idKurs_and_halbjahr_and_quartal() : void {
		this._kursklausur_by_idKurs_and_abijahr_and_halbjahr_and_quartal.clear();
		for (const kk of this._kursklausurmenge) {
			const v : GostKlausurvorgabe = this.vorgabeByKursklausur(kk);
			this._kursklausur_by_idKurs_and_abijahr_and_halbjahr_and_quartal.put(kk.idKurs, v.abiJahrgang, v.halbjahr, v.quartal, kk);
		}
	}

	private update_terminmenge_by_halbjahr_and_quartal() : void {
		this._terminmenge_by_abijahr_and_halbjahr_and_quartal.clear();
		for (const t of this._terminmenge)
			Map3DUtils.getOrCreateArrayList(this._terminmenge_by_abijahr_and_halbjahr_and_quartal, t.abijahr, t.halbjahr, t.quartal).add(t);
	}

	private update_terminmenge_by_datum() : void {
		this._terminmenge_by_datum_and_abijahr.clear();
		for (const t of this._terminmenge)
			Map2DUtils.getOrCreateArrayList(this._terminmenge_by_datum_and_abijahr, t.datum, t.abijahr).add(t);
	}

	private update_schuelerklausurmenge_by_idKursklausur() : void {
		this._schuelerklausurmenge_by_idKursklausur.clear();
		for (const sk of this._schuelerklausurmenge) {
			MapUtils.getOrCreateArrayList(this._schuelerklausurmenge_by_idKursklausur, sk.idKursklausur).add(sk);
		}
	}

	private update_kursklausurmenge_by_kw_and_schuelerId() : void {
		this._kursklausurmenge_by_kw_and_schuelerId.clear();
		for (const t of this._terminmenge) {
			if (t.datum === null)
				continue;
			const kw : number = DateUtils.gibKwDesDatumsISO8601(t.datum);
			const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(t.id);
			if (klausuren !== null)
				for (const kk of klausuren) {
					for (const sk of this.schuelerklausurGetMengeByKursklausurid(kk.id))
						Map2DUtils.getOrCreateArrayList(this._kursklausurmenge_by_kw_and_schuelerId, kw, sk.idSchueler).add(kk);
				}
		}
	}

	private update_kursklausurmenge_by_terminId_and_schuelerId() : void {
		this._kursklausurmenge_by_terminId_and_schuelerId.clear();
		for (const kk of this._kursklausurmenge) {
			for (const sk of this.schuelerklausurGetMengeByKursklausurid(kk.id))
				Map2DUtils.getOrCreateArrayList(this._kursklausurmenge_by_terminId_and_schuelerId, kk.idTermin, sk.idSchueler).add(kk);
		}
	}

	private update_schuelerklausurterminmenge_by_idSchuelerklausur() : void {
		this._schuelerklausurterminmenge_by_idSchuelerklausur.clear();
		for (const skt of this._schuelerklausurterminmenge)
			MapUtils.getOrCreateArrayList(this._schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur).add(skt);
		for (const sktList of this._schuelerklausurterminmenge_by_idSchuelerklausur.values())
			sktList.sort(this._compSchuelerklausurTermin);
	}

	private update_schuelerklausurterminmenge_by_idTermin() : void {
		this._schuelerklausurterminmenge_by_idTermin.clear();
		for (const skt of this._schuelerklausurterminmenge) {
			if (skt.folgeNr === 0)
				MapUtils.getOrCreateArrayList(this._schuelerklausurterminmenge_by_idTermin, this.kursklausurBySchuelerklausurTermin(skt).idTermin).add(skt);
			else
				MapUtils.getOrCreateArrayList(this._schuelerklausurterminmenge_by_idTermin, skt.idTermin).add(skt);
		}
	}

	private update_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur() : void {
		this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.clear();
		for (const e of this._schuelerklausurterminmenge_by_idTermin.entrySet())
			for (const skt of e.getValue())
				if (this.istAktuellerSchuelerklausurtermin(skt))
					Map2DUtils.getOrCreateArrayList(this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur, e.getKey(), this.schuelerklausurBySchuelerklausurtermin(skt).idKursklausur).add(skt);
	}

	private update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal() : void {
		this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.clear();
		for (const sk of this._schuelerklausurmenge) {
			const sktLast : GostSchuelerklausurTermin | null = this.schuelerklausurterminaktuellBySchuelerklausur(sk.id);
			if (sktLast.folgeNr > 0) {
				const v : GostKlausurvorgabe | null = this.vorgabeBySchuelerklausurTermin(sktLast);
				Map4DUtils.getOrCreateArrayList(this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin, v.abiJahrgang, v.halbjahr, v.quartal, sktLast.idTermin !== null ? sktLast.idTermin : -1).add(sktLast);
			}
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
		const setOfIDs : HashSet<number> = new HashSet<number>();
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
		this.kursklausurPatchAttributesOhneUpdate(kursklausur);
		this.update_all();
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public kursklausurPatchAttributesOhneUpdate(kursklausur : GostKursklausur) : void {
		GostKursklausurManager.kursklausurCheck(kursklausur);
		DeveloperNotificationException.ifMapRemoveFailes(this._kursklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._kursklausur_by_id, kursklausur.id, kursklausur);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausurMenge Das neue {@link GostKursklausur}-Objekt.
	 */
	public kursklausurMengePatchAttributes(kursklausurMenge : List<GostKursklausur>) : void {
		for (const kursklausur of kursklausurMenge)
			this.kursklausurPatchAttributesOhneUpdate(kursklausur);
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
		const setOfIDs : HashSet<number> = new HashSet<number>();
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
		const kursklausurenZuTermin : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		if (kursklausurenZuTermin !== null)
			for (const k of kursklausurenZuTermin)
				k.idTermin = null;
		const schuelerklausurtermineZuTermin : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(idTermin);
		if (schuelerklausurtermineZuTermin !== null)
			for (const skt of schuelerklausurtermineZuTermin)
				skt.idTermin = null;
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
		const setOfIDs : HashSet<number> = new HashSet<number>();
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
		return new ArrayList<GostSchuelerklausur>(this._schuelerklausurmenge);
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

	private update_schuelerklausurterminmenge() : void {
		this._schuelerklausurterminmenge.clear();
		this._schuelerklausurterminmenge.addAll(this._schuelerklausurtermin_by_id.values());
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurTermin}-Objekt hinzu.
	 *
	 * @param schuelerklausurtermin Das {@link GostSchuelerklausurTermin}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public schuelerklausurterminAdd(schuelerklausurtermin : GostSchuelerklausurTermin) : void {
		this.schuelerklausurterminAddAll(ListUtils.create1(schuelerklausurtermin));
		this.update_all();
	}

	private schuelerklausurterminAddAllOhneUpdate(list : List<GostSchuelerklausurTermin>) : void {
		const setOfIDs : HashSet<number> = new HashSet<number>();
		for (const schuelerklausurtermin of list) {
			GostKursklausurManager.schuelerklausurterminCheck(schuelerklausurtermin);
			DeveloperNotificationException.ifTrue("schuelerklausurterminAddAllOhneUpdate: ID=" + schuelerklausurtermin.id + " existiert bereits!", this._schuelerklausurtermin_by_id.containsKey(schuelerklausurtermin.id));
			DeveloperNotificationException.ifTrue("schuelerklausurterminAddAllOhneUpdate: ID=" + schuelerklausurtermin.id + " doppelt in der Liste!", !setOfIDs.add(schuelerklausurtermin.id));
		}
		for (const schuelerklausurtermin of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausurtermin_by_id, schuelerklausurtermin.id, schuelerklausurtermin);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurTermin}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurtermine Die Menge der {@link GostSchuelerklausurTermin}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public schuelerklausurterminAddAll(listSchuelerklausurtermine : List<GostSchuelerklausurTermin>) : void {
		this.schuelerklausurterminAddAllOhneUpdate(listSchuelerklausurtermine);
		this.update_all();
	}

	private static schuelerklausurterminCheck(schuelerklausurtermin : GostSchuelerklausurTermin) : void {
		DeveloperNotificationException.ifInvalidID("schuelerschuelerklausurtermin.idSchuelerschuelerklausurtermin", schuelerklausurtermin.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public schuelerklausurterminGetByIdOrException(idSchuelerklausurtermin : number) : GostSchuelerklausurTermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurtermin_by_id, idSchuelerklausurtermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurTermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public schuelerklausurterminGetMengeAsList() : List<GostSchuelerklausurTermin> {
		return new ArrayList<GostSchuelerklausurTermin>(this._schuelerklausurterminmenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurTermin}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param schuelerklausurtermin Das neue {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public schuelerklausurterminPatchAttributes(schuelerklausurtermin : GostSchuelerklausurTermin) : void {
		GostKursklausurManager.schuelerklausurterminCheck(schuelerklausurtermin);
		DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausurtermin_by_id, schuelerklausurtermin.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausurtermin_by_id, schuelerklausurtermin.id, schuelerklausurtermin);
		this.update_all();
	}

	private schuelerklausurterminRemoveOhneUpdateById(idSchuelerklausurtermin : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausurtermin_by_id, idSchuelerklausurtermin);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurTermin}-Objekt.
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 */
	public schuelerklausurterminRemoveById(idSchuelerklausurtermin : number) : void {
		this.schuelerklausurterminRemoveOhneUpdateById(idSchuelerklausurtermin);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurTermin}-Objekte.
	 *
	 * @param listSchuelerklausurtermine Die Liste der zu entfernenden
	 *                          {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public schuelerklausurterminRemoveAll(listSchuelerklausurtermine : List<GostSchuelerklausurTermin>) : void {
		for (const schuelerklausurtermin of listSchuelerklausurtermine)
			this.schuelerklausurterminRemoveOhneUpdateById(schuelerklausurtermin.id);
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
		const ergebnis : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();
		if (!this._terminmenge_by_datum_and_abijahr.containsKey1(datum))
			return ergebnis;
		for (const termine of this._terminmenge_by_datum_and_abijahr.getNonNullValuesOfKey1AsList(datum))
			ergebnis.addAll(termine);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum das Datum der Klausurtermine im Format YYYY-MM-DD
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGruppierteUeberschneidungenGetMengeByDatum(datum : string) : List<List<GostKlausurtermin>> {
		return this.gruppiereUeberschneidungen(this.terminGetMengeByDatum(datum));
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum das Datum der Klausurtermine im Format YYYY-MM-DD
	 * @param abiJahrgang der Abiturjahrgang
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGruppierteUeberschneidungenGetMengeByDatumAndAbijahr(datum : string, abiJahrgang : number | null) : List<List<GostKlausurtermin>> {
		if (abiJahrgang === null)
			return this.terminGruppierteUeberschneidungenGetMengeByDatum(datum);
		const termine : List<GostKlausurtermin> | null = this._terminmenge_by_datum_and_abijahr.getOrNull(datum, abiJahrgang);
		return termine !== null ? this.gruppiereUeberschneidungen(termine) : new ArrayList();
	}

	private gruppiereUeberschneidungen(termine : List<GostKlausurtermin>) : List<List<GostKlausurtermin>> {
		const ergebnis : List<List<GostKlausurtermin>> = new ArrayList<List<GostKlausurtermin>>();
		let added : boolean = false;
		for (const terminToAdd of termine) {
			for (const listToCheck of ergebnis) {
				for (const terminInListe of termine) {
					if (this.checkTerminUeberschneidung(terminInListe, terminToAdd)) {
						listToCheck.add(terminToAdd);
						added = true;
					}
					if (added)
						break;
				}
				if (added)
					break;
			}
			if (!added)
				ergebnis.add(ListUtils.create1(terminToAdd));
		}
		return ergebnis;
	}

	private checkTerminUeberschneidung(t1 : GostKlausurtermin, t2 : GostKlausurtermin) : boolean {
		let s1 : number = this.minKursklausurstartzeitByTerminid(t1.id);
		let s2 : number = this.minKursklausurstartzeitByTerminid(t2.id);
		let e1 : number = this.maxKursklausurendzeitByTerminid(t1.id);
		let e2 : number = this.maxKursklausurendzeitByTerminid(t2.id);
		return e1 >= s2 && e2 >= s1;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum das Datum der Klausurtermine im Format YYYY-MM-DD
	 * @param abiJahrgang der Abiturjahrgang
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGetMengeByDatumAndAbijahr(datum : string, abiJahrgang : number) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = this._terminmenge_by_datum_and_abijahr.getOrNull(datum, abiJahrgang);
		return termine !== null ? termine : new ArrayList();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum das Datum der Klausurtermine im Format YYYY-MM-DD
	 * @param abiJahrgang der Abiturjahrgang
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGetFremdmengeByDatumAndAbijahr(datum : string, abiJahrgang : number) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();
		const jgDatumMap : JavaMap<number, List<GostKlausurtermin> | null> | null = this._terminmenge_by_datum_and_abijahr.getSubMapOrNull(datum);
		if (jgDatumMap !== null)
			for (const entry of jgDatumMap.entrySet())
				if (entry.getKey() !== abiJahrgang)
					if (entry.getValue() !== null)
						termine.addAll(entry.getValue());
		return termine;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum   das Datum der Klausurtermine
	 * @param abiJahrgang der Abiturjahrgang
	 * @param zr      Zeitraster
	 * @param manager der StundenplanManager
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGetMengeByDatumAndAbijahrAndZeitraster(datum : string, abiJahrgang : number, zr : StundenplanZeitraster, manager : StundenplanManager) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = this.terminGetMengeByDatumAndAbijahr(datum, abiJahrgang);
		const retList : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const termin of termine) {
			const maxKlausurDauer : number = this.maxKlausurdauerGetByTerminid(termin.id);
			const zrsTermin : List<StundenplanZeitraster> = manager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(zr.wochentag), DeveloperNotificationException.ifNull("Startzeit des Klausurtermins", termin.startzeit)!, maxKlausurDauer);
			for (const zrTermin of zrsTermin)
				if (zrTermin.id === zr.id)
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
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Termin auf Wunsch mit Kursklausuren der Nachschreiber
	 *
	 * @param idTermin die ID des Klausurtermins
	 * @param mitNachschreibern wenn true werden die Kursklausuren einzelner Nachschreiber mit inkludiert
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurMitNachschreibernGetMengeByTerminid(idTermin : number | null, mitNachschreibern : boolean) : JavaSet<GostKursklausur> {
		const klausuren : JavaSet<GostKursklausur> | null = new HashSet<GostKursklausur>(this.kursklausurGetMengeByTerminid(idTermin));
		if (mitNachschreibern)
			for (let skt of this.schuelerklausurterminGetMengeByTerminid(idTermin!)) {
				klausuren.add(this.kursklausurBySchuelerklausurTermin(skt));
			}
		return klausuren;
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKursklausur> | null {
		return this._kursklausurmenge_by_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, quartal);
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
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurOhneTerminGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKursklausur> {
		if (quartal > 0) {
			const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.getOrNull(abiJahrgang, halbjahr.id, -1, quartal);
			return klausuren !== null ? klausuren : new ArrayList();
		}
		const klausuren : List<GostKursklausur> | null = new ArrayList<GostKursklausur>();
		for (const kl of this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.getNonNullValuesOfMap4AsList(abiJahrgang, halbjahr.id, -1)) {
			klausuren.addAll(kl);
		}
		return klausuren;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals, 0 für alle Quartale
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, includeMultiquartal : boolean) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		if (quartal > 0) {
			if (this._terminmenge_by_abijahr_and_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, quartal) !== null)
				termine.addAll(this._terminmenge_by_abijahr_and_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, quartal));
			if (includeMultiquartal && this._terminmenge_by_abijahr_and_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, 0) !== null)
				termine.addAll(this._terminmenge_by_abijahr_and_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, 0));
			return termine;
		}
		if (this._terminmenge_by_abijahr_and_halbjahr_and_quartal.containsKey1AndKey2(abiJahrgang, halbjahr.id))
			for (const qTermine of this._terminmenge_by_abijahr_and_halbjahr_and_quartal.getNonNullValuesOfMap3AsList(abiJahrgang, halbjahr.id)) {
				termine.addAll(qTermine);
			}
		return termine;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten, die für Nachschreiber zugelassen sind zum übergebenen Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals, 0 für alle Quartale
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von NT-GostKlausurtermin-Objekten
	 */
	public terminGetNTMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, includeMultiquartal : boolean) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const t of this.terminGetMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal, includeMultiquartal))
			if (!t.istHaupttermin || t.nachschreiberZugelassen)
				termine.add(t);
		termine.sort(GostKursklausurManager._compTermin);
		return termine;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten, die als Haupttermin angelegt wurden zum übergebenen Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals, 0 für alle Quartale
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von HT-GostKlausurtermin-Objekten
	 */
	public terminGetHTMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, includeMultiquartal : boolean) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const t of this.terminGetMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal, includeMultiquartal))
			if (t.istHaupttermin)
				termine.add(t);
		termine.sort(GostKursklausurManager._compTermin);
		return termine;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Halbjahres, bei denen
	 * ein Datum gesetzt ist
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminMitDatumGetMenge() : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
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
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminMitDatumGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, includeMultiquartal : boolean) : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const termin of this.terminGetMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal, includeMultiquartal))
			if (termin.datum !== null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(GostKursklausurManager._compTermin);
		return termineMitDatum;
	}

	/**
	 * Liefert eine Liste von HT-GostKlausurtermin-Objekten des Quartals, bei denen ein
	 * Datum gesetzt ist
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gost-Halbjahr
	 * @param quartal             die Nummer des Quartals
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von HT-GostKlausurtermin-Objekten
	 */
	public terminMitDatumGetHTMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, includeMultiquartal : boolean) : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const termin of this.terminGetHTMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal, includeMultiquartal))
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
		const klausuren : List<GostKursklausur> = this.kursklausurGetMengeByTerminid(idTermin);
		const schuelertermine : List<GostSchuelerklausurTermin> = this.schuelerklausurterminNtByTerminid(idTermin);
		if (klausuren.isEmpty() && schuelertermine.isEmpty())
			return DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, idTermin).quartal;
		const vorgaben : List<GostKlausurvorgabe> = new ArrayList<GostKlausurvorgabe>();
		for (const k of klausuren)
			vorgaben.add(this.vorgabeByKursklausur(k));
		for (const k of schuelertermine)
			vorgaben.add(this.vorgabeBySchuelerklausurTermin(k));
		let quartal : number = -1;
		for (const v of vorgaben) {
			if (quartal === -1)
				quartal = v.quartal;
			if (quartal !== v.quartal)
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
		const skts : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(idTermin);
		return skts === null ? 0 : skts.size();
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
		const termin : GostKlausurtermin = this.terminGetByIdOrException(idTermin);
		const kks : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		const skts : List<GostSchuelerklausurTermin> | null = this.schuelerklausurterminFolgeterminGetMengeByTerminid(idTermin);
		if ((kks === null || kks.isEmpty()) && (skts === null || skts.isEmpty()))
			return termin.startzeit!;
		if (kks !== null)
			for (const kk of kks) {
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
		if (skts !== null)
			for (const skt of skts) {
				let skStartzeit : number = -1;
				if (skt.startzeit !== null)
					skStartzeit = skt.startzeit.valueOf();
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
		let maxEnd : number = this.minKursklausurstartzeitByTerminid(idTermin) + 1;
		const termin : GostKlausurtermin = this.terminGetByIdOrException(idTermin);
		const kks : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		const skts : List<GostSchuelerklausurTermin> | null = this.schuelerklausurterminFolgeterminGetMengeByTerminid(idTermin);
		if ((kks === null || kks.isEmpty()) && (skts === null || skts.isEmpty()))
			return maxEnd;
		if (kks !== null)
			for (const kk of kks) {
				const vorgabe : GostKlausurvorgabe = this._vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
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
		if (skts !== null)
			for (const skt of skts) {
				const vorgabe : GostKlausurvorgabe = this.vorgabeBySchuelerklausurTermin(skt);
				let skStartzeit : number = -1;
				if (skt.startzeit !== null)
					skStartzeit = skt.startzeit.valueOf();
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
		let maxDauer : number = 0;
		const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		if (klausuren !== null)
			for (const klausur of klausuren) {
				const vorgabe : GostKlausurvorgabe = this.vorgabeByKursklausur(klausur);
				maxDauer = vorgabe.dauer > maxDauer ? vorgabe.dauer : maxDauer;
			}
		const skts : List<GostSchuelerklausurTermin> | null = this.schuelerklausurterminFolgeterminGetMengeByTerminid(idTermin);
		if (skts !== null)
			for (const skt of skts) {
				const vorgabe : GostKlausurvorgabe = this.vorgabeBySchuelerklausurTermin(skt);
				maxDauer = vorgabe.dauer > maxDauer ? vorgabe.dauer : maxDauer;
			}
		return maxDauer;
	}

	/**
	 * Prüft, ob Schülerklausurtermine aus der Menge menge2 konfliktfrei in die Menge menge1 hinzugefügt werden können. Falls ein Schülerklausurtermin aus menge1 bereits in menge2 enthalten ist, wird dies nicht als Konflikt bewertet.
	 *
	 * @param menge1 f
	 * @param menge2 f
	 *
	 * @return d
	 */
	private berechneKonflikteSchuelerklausurtermine(menge1 : List<GostSchuelerklausurTermin> | null, menge2 : List<GostSchuelerklausurTermin> | null) : JavaMap<GostSchuelerklausurTermin, GostSchuelerklausurTermin> {
		const map1 : JavaMap<number, GostSchuelerklausurTermin> = new HashMap<number, GostSchuelerklausurTermin>();
		if (menge1 !== null)
			for (const termin1 of menge1)
				DeveloperNotificationException.ifMapPutOverwrites(map1, this.schuelerklausurGetByIdOrException(termin1.idSchuelerklausur).idSchueler, termin1);
		return this.berechneKonflikteMapschuelerklausurterminToListSchuelerklausurtermin(map1, menge2);
	}

	/**
	 * Prüft, ob Schülerklausurtermine aus der Menge menge2 konfliktfrei in die Menge menge1 hinzugefügt werden können. Falls ein Schülerklausurtermin aus menge1 bereits in menge2 enthalten ist, wird dies nicht als Konflikt bewertet.
	 *
	 * @param menge1 f
	 * @param menge2 f
	 *
	 * @return d
	 */
	private berechneKonflikteMapschuelerklausurterminToListSchuelerklausurtermin(menge1 : JavaMap<number, GostSchuelerklausurTermin> | null, menge2 : List<GostSchuelerklausurTermin> | null) : JavaMap<GostSchuelerklausurTermin, GostSchuelerklausurTermin> {
		const ergebnis : JavaMap<GostSchuelerklausurTermin, GostSchuelerklausurTermin> = new HashMap<GostSchuelerklausurTermin, GostSchuelerklausurTermin>();
		if (menge1 === null || menge2 === null)
			return ergebnis;
		for (const skt2 of menge2) {
			const sk : GostSchuelerklausur = this.schuelerklausurBySchuelerklausurtermin(skt2);
			const skt1 : GostSchuelerklausurTermin | null = menge1.get(sk.idSchueler);
			if (skt1 !== null && skt1.id !== skt2.id)
				ergebnis.put(skt1, skt2);
		}
		return ergebnis;
	}

	/**
	 * Prüft, ob ein Schülerklausurtermin konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Falls der Schülerklausurtermin bereits dem Termin zugewiesen war, wird dies nicht als Konflikt bewertet.
	 *
	 * @param termin  der zu prüfende Klausurtermin
	 * @param skt der zu prüfende Schülerklausurtermin
	 *
	 * @return die Anzahl der Konflikte
	 */
	public konflikteAnzahlZuTerminGetByTerminAndSchuelerklausurtermin(termin : GostKlausurtermin, skt : GostSchuelerklausurTermin) : number {
		return this.berechneKonflikteSchuelerklausurtermine(this._schuelerklausurterminmenge_by_idTermin.get(termin.id), ListUtils.create1(skt)).size();
	}

	/**
	 * Prüft, ob der zu einer Schülerklausur gehörige Schüler in einer Kursklausur enthalten ist.
	 *
	 * @param schuelerklausur die zu prüfende Schülerklausur
	 * @param kursklausur  die zu prüfende Kursklausur
	 *
	 * @return die Anzahl der Konflikte
	 */
	public konfliktZuKursklausurBySchuelerklausur(schuelerklausur : GostSchuelerklausur, kursklausur : GostKursklausur) : boolean {
		let schuelerids : List<number> = new ArrayList<number>();
		for (let sk of this.schuelerklausurGetMengeByKursklausurid(kursklausur.id))
			schuelerids.add(sk.idSchueler);
		return schuelerids.contains(schuelerklausur.idSchueler);
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
		return this.berechneKonflikte(klausuren, klausuren);
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
		const klausuren2 : List<GostKursklausur> | null = new ArrayList<GostKursklausur>();
		klausuren2.add(DeveloperNotificationException.ifMapGetIsNull(this._kursklausur_by_id, idKursklausur));
		return this.berechneKonflikte(klausuren1 !== null ? klausuren1 : new ArrayList(), klausuren2);
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
		const klausuren1 : List<GostKursklausur> = new ArrayList<GostKursklausur>(DeveloperNotificationException.ifMapGetIsNull(this._kursklausurmenge_by_idTermin, klausur.idTermin));
		klausuren1.remove(klausur);
		const klausuren2 : List<GostKursklausur> | null = new ArrayList<GostKursklausur>();
		klausuren2.add(klausur);
		return GostKursklausurManager.countKonflikte(this.berechneKonflikte(klausuren1, klausuren2));
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

	private berechneKonflikte(klausuren1 : List<GostKursklausur>, klausuren2 : List<GostKursklausur>) : JavaMap<GostKursklausur, JavaSet<number>> {
		const result : JavaMap<GostKursklausur, JavaSet<number>> | null = new HashMap<GostKursklausur, JavaSet<number>>();
		const kursklausuren2Copy : List<GostKursklausur> | null = new ArrayList<GostKursklausur>(klausuren2);
		for (const kk1 of klausuren1) {
			kursklausuren2Copy.remove(kk1);
			for (const kk2 of kursklausuren2Copy) {
				const konflikte : JavaSet<number> | null = this.berechneKlausurKonflikte(kk1, kk2);
				if (!konflikte.isEmpty()) {
					MapUtils.getOrCreateHashSet(result, kk1).addAll(konflikte);
					MapUtils.getOrCreateHashSet(result, kk2).addAll(konflikte);
				}
			}
		}
		return result;
	}

	private berechneKlausurKonflikte(kk1 : GostKursklausur, kk2 : GostKursklausur) : JavaSet<number> {
		const konflikte : HashSet<number> = new HashSet<number>(this.getSchuelerIDsFromKursklausur(kk1));
		konflikte.retainAll(this.getSchuelerIDsFromKursklausur(kk2));
		return konflikte;
	}

	private static countKonflikte(konflikte : JavaMap<GostKursklausur, JavaSet<number>>) : number {
		const susIds : HashSet<number> = new HashSet<number>();
		for (const klausurSids of konflikte.values())
			susIds.addAll(klausurSids);
		return susIds.size();
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
		const ergebnis : JavaMap<number, List<GostKursklausur>> | null = new HashMap<number, List<GostKursklausur>>();
		if (termin.datum === null)
			return ergebnis;
		const kw : number = DateUtils.gibKwDesDatumsISO8601(termin.datum);
		const kursklausurmenge_by_schuelerId : JavaMap<number, List<GostKursklausur> | null> | null = this._kursklausurmenge_by_kw_and_schuelerId.getSubMapOrNull(kw);
		if (kursklausurmenge_by_schuelerId === null)
			return ergebnis;
		for (const entry of kursklausurmenge_by_schuelerId.entrySet()) {
			const temp : List<GostKursklausur> | null = entry.getValue();
			const klausuren : List<GostKursklausur> = temp !== null ? new ArrayList(temp) : new ArrayList();
			if (klausur !== null && klausur.idTermin !== termin.id && this.getSchuelerIDsFromKursklausur(klausur).contains(entry.getKey()))
				klausuren.add(klausur);
			if (klausuren.size() >= threshold)
				ergebnis.put(entry.getKey(), klausuren);
		}
		return ergebnis;
	}

	/**
	 * Liefert für eine Liste von GostSchuelerklausur-Objekten die zugehörigen Schüler-IDs als Liste.
	 *
	 * @param sks        Die Liste von GostSchuelerklausur-Objekten
	 *
	 * @return die Liste der Schüler-IDs
	 */
	public getSchuelerIDsFromSchuelerklausuren(sks : List<GostSchuelerklausur>) : List<number> {
		const ids : List<number> = new ArrayList<number>();
		for (const sk of sks) {
			ids.add(sk.idSchueler);
		}
		return ids;
	}

	/**
	 * Liefert für ein GostKursklausur-Objekt die zugehörigen Schüler-IDs als Liste.
	 *
	 * @param kk        die Kursklausur, zu der die Schüler-IDs gesucht werden.
	 *
	 * @return die Liste der Schüler-IDs
	 */
	public getSchuelerIDsFromKursklausur(kk : GostKursklausur) : List<number> {
		return this.getSchuelerIDsFromSchuelerklausuren(this.schuelerklausurGetMengeByKursklausurid(kk.id));
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
		const kwDatum : number = DateUtils.gibKwDesDatumsISO8601(datum);
		return this.klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(kwDatum, termin, threshold, thresholdOnly);
	}

	private klausurenProSchueleridExceedingKWThresholdByKwAndTerminAndThreshold(kw : number, termin : GostKlausurtermin | null, threshold : number, thresholdOnly : boolean) : JavaMap<number, HashSet<GostKursklausur>> {
		const ergebnis : JavaMap<number, HashSet<GostKursklausur>> = new HashMap<number, HashSet<GostKursklausur>>();
		const kursklausurmenge_by_schuelerId : JavaMap<number, List<GostKursklausur> | null> | null = this._kursklausurmenge_by_kw_and_schuelerId.getSubMapOrNull(kw);
		if (kursklausurmenge_by_schuelerId === null)
			return ergebnis;
		for (const entry of kursklausurmenge_by_schuelerId.entrySet()) {
			const temp : List<GostKursklausur> | null = entry.getValue();
			const klausuren : HashSet<GostKursklausur> = temp !== null ? new HashSet(temp) : new HashSet();
			if (termin !== null) {
				const klausurenInTermin : List<GostKursklausur> | null = this._kursklausurmenge_by_terminId_and_schuelerId.getOrNull(termin.id, entry.getKey());
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
	public terminOrNullByKursklausur(klausur : GostKursklausur) : GostKlausurtermin | null {
		return this._termin_by_id.get(klausur.idTermin);
	}

	/**
	 * Liefert den Klausurtermin zu einer Kursklausur, sonst NULL.
	 *
	 * @param klausur die Kursklausur, zu der der Termin gesucht wird.
	 *
	 * @return den Klausurtermin
	 */
	public terminOrExceptionByKursklausur(klausur : GostKursklausur) : GostKlausurtermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, DeveloperNotificationException.ifNull(JavaString.format("idTermin von Klausur %d darf nicht NULL sein", klausur.id), klausur.idTermin));
	}

	/**
	 * Liefert den Klausurtermin zu einem Schülerklausurtermin oder NULL.
	 *
	 * @param termin der Schülerklausurtermin, zu dem der Termin gesucht wird.
	 *
	 * @return den Klausurtermin
	 */
	public terminOrNullBySchuelerklausurTermin(termin : GostSchuelerklausurTermin) : GostKlausurtermin | null {
		if (termin.folgeNr > 0)
			return termin.idTermin === null ? null : this.terminGetByIdOrException(termin.idTermin);
		return this.terminOrNullByKursklausur(this.kursklausurBySchuelerklausurTermin(termin));
	}

	/**
	 * Liefert den Klausurtermin zu einem Schülerklausurtermin oder NULL.
	 *
	 * @param termin der Schülerklausurtermin, zu dem der Termin gesucht wird.
	 *
	 * @return den Klausurtermin
	 */
	public terminOrExceptionBySchuelerklausurTermin(termin : GostSchuelerklausurTermin) : GostKlausurtermin {
		if (termin.folgeNr > 0) {
			return this.terminGetByIdOrException(DeveloperNotificationException.ifNull(JavaString.format("idTermin von Termin %d darf nicht NULL sein", termin.id), termin.idTermin)!);
		}
		return this.terminOrExceptionByKursklausur(this.kursklausurBySchuelerklausurTermin(termin));
	}

	/**
	 * Liefert den Klausurtermin zu einer Schülerklausur oder NULL.
	 *
	 * @param sk die Schülerklausur
	 *
	 * @return den Klausurtermin
	 */
	public terminKursklausurBySchuelerklausur(sk : GostSchuelerklausur) : GostKlausurtermin | null {
		return this.terminOrNullByKursklausur(this.kursklausurBySchuelerklausur(sk));
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
		const kk : GostKursklausur = this.kursklausurGetByIdOrException(klausur.idKursklausur);
		return this._vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
	}

	/**
	 * Liefert die Klausurvorgabe zu einem Schuelerklausurtermin.
	 *
	 * @param klausur der Schuelerklausurtermin, zu der die Vorgabe gesucht wird.
	 *
	 * @return die Klausurvorgabe
	 */
	public vorgabeBySchuelerklausurTermin(klausur : GostSchuelerklausurTermin) : GostKlausurvorgabe {
		return this.vorgabeBySchuelerklausur(this.schuelerklausurGetByIdOrException(klausur.idSchuelerklausur));
	}

	/**
	 * Liefert die GostSchuelerklausur zu einem GostSchuelerklausurTermin.
	 *
	 * @param klausur die Schuelerklausur, zu der die GostKursklausur gesucht wird.
	 *
	 * @return die GostSchuelerklausur
	 */
	public schuelerklausurBySchuelerklausurtermin(klausur : GostSchuelerklausurTermin) : GostSchuelerklausur {
		return this.schuelerklausurGetByIdOrException(klausur.idSchuelerklausur);
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
		return this.kursklausurBySchuelerklausur(this.schuelerklausurGetByIdOrException(termin.idSchuelerklausur));
	}

	/**
	 * Liefert zurück, ob die übergebene Klausurvorgabe von einer Kursklausur
	 * verwendet wird.
	 *
	 * @param vorgabe die Klausurvorgabe, die auf Verwendung geprüft werden soll.
	 *
	 * @return true oder false
	 */
	public istVorgabeVerwendetByKursklausur(vorgabe : GostKlausurvorgabe) : boolean {
		const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idVorgabe.get(vorgabe.idVorgabe);
		return klausuren !== null && !klausuren.isEmpty();
	}

	/**
	 * Liefert das GostKursklausur-Objekt zu den übergebenen Parametern.
	 *
	 * @param idKurs  die ID des Kurses
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal das Quartal der Klausur
	 *
	 * @return die Kursklausur
	 */
	public kursklausurByKursidAndHalbjahrAndQuartal(idKurs : number, abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : GostKursklausur | null {
		return this._kursklausur_by_idKurs_and_abijahr_and_halbjahr_and_quartal.getOrNull(idKurs, abiJahrgang, halbjahr.id, quartal);
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
		const previousVorgabe : GostKlausurvorgabe | null = this._vorgabenManager.getPrevious(this._vorgabenManager.vorgabeGetByIdOrException(klausur.idVorgabe));
		if (previousVorgabe === null)
			return null;
		const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idVorgabe.get(previousVorgabe.idVorgabe);
		if (klausuren === null)
			return null;
		for (const k of klausuren) {
			const kKurs : KursDaten | null = this.getKursManager().get(k.idKurs);
			const klausurKurs : KursDaten | null = this.getKursManager().get(klausur.idKurs);
			if (kKurs === null || klausurKurs === null)
				throw new DeveloperNotificationException("Keine Kurszuordnung im kursManager zu Kurs-ID")
			if (JavaObject.equalsTranspiler(kKurs.kuerzel, (klausurKurs.kuerzel)))
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
	public startzeitByKursklausurOrNull(klausur : GostKursklausur) : number | null {
		const termin : GostKlausurtermin | null = this.terminOrNullByKursklausur(klausur);
		if (klausur.startzeit !== null)
			return klausur.startzeit;
		return termin === null ? null : termin.startzeit;
	}

	/**
	 * Gibt die Startzeit der übergebenen Klausur aus. Falls keine individuelle gesetzt ist, wird die des Termins zurückgegeben.
	 * Sollte kein Termin gesetzt sein oder der Termin keine Startzeit definiert haben, wird eine Exception zurückgegeben.
	 *
	 * @param klausur die Kursklausur, deren Startzeit gesucht wird.
	 *
	 * @return die Startzeit der Klausur
	 */
	public startzeitByKursklausurOrException(klausur : GostKursklausur) : number {
		return klausur.startzeit !== null ? klausur.startzeit : DeveloperNotificationException.ifNull("Startzeit darf nicht null sein.", this.terminOrExceptionByKursklausur(klausur).startzeit)!;
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
		const termin : GostKlausurtermin | null = this.terminOrNullByKursklausur(klausur);
		return !(klausur.startzeit === null || termin === null || termin.startzeit === null || JavaObject.equalsTranspiler(termin.startzeit, (klausur.startzeit)));
	}

	/**
	 * Gibt die Liste von Schülerklausur-Terminen zu einer Schülerklausur zurück.
	 *
	 * @param sk die Schülerklausur, zu der die Termine gesucht werden.
	 *
	 * @return die Liste von Schülerklausur-Terminen
	 */
	public schuelerklausurterminGetMengeBySchuelerklausur(sk : GostSchuelerklausur) : List<GostSchuelerklausurTermin> {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurterminmenge_by_idSchuelerklausur, sk.id);
	}

	/**
	 * Gibt die Liste von Schülerklausur-Terminen zu einem Klausurtermin zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste von Schülerklausur-Terminen
	 */
	public schuelerklausurterminGetMengeByTerminid(idTermin : number) : List<GostSchuelerklausurTermin> {
		const list : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(idTermin);
		return list !== null ? list : new ArrayList();
	}

	/**
	 * Gibt die Liste von Schülerklausur-Terminen zu einem Klausurtermin und einer Kursklausur zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 * @param idKursklausur die ID der Kursklausur
	 *
	 * @return die Liste von Schülerklausur-Terminen
	 */
	public schuelerklausurterminaktuellGetMengeByTerminidAndKursklausurid(idTermin : number, idKursklausur : number) : List<GostSchuelerklausurTermin> {
		const list : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.getOrNull(idTermin, idKursklausur);
		return list !== null ? list : new ArrayList();
	}

	/**
	 * Gibt die Liste von Schülerklausur-Terminen zu einem Klausurtermin zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste von Schülerklausur-Terminen
	 */
	public schuelerklausurGetMengeByTerminid(idTermin : number) : List<GostSchuelerklausur> {
		const ergebnis : List<GostSchuelerklausur> | null = new ArrayList<GostSchuelerklausur>();
		const list : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(idTermin);
		if (list === null)
			return ergebnis;
		for (const termin of list)
			ergebnis.add(this.schuelerklausurBySchuelerklausurtermin(termin));
		return ergebnis;
	}

	/**
	 * Gibt die Liste von Folge-Schülerklausur-Terminen (Nachschreibtermine) zu einem Klausurtermin zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste von Folge-Schülerklausur-Terminen
	 */
	public schuelerklausurterminFolgeterminGetMengeByTerminid(idTermin : number) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const skt of this.schuelerklausurterminGetMengeByTerminid(idTermin))
			if (skt.folgeNr > 0)
				ergebnis.add(skt);
		return ergebnis;
	}

	/**
	 * Prüft, ob der übergebene Schülerklausurtermin der aktuellste Termin der Schülerklausur ist.
	 *
	 * @param skt der Schülerklausurtermin, der geprüft werden soll
	 *
	 * @return true, wenn es sich um den aktuellen Termin handelt, sonst false
	 */
	public istAktuellerSchuelerklausurtermin(skt : GostSchuelerklausurTermin) : boolean {
		return this.schuelerklausurterminaktuellBySchuelerklausur(skt.idSchuelerklausur) as unknown === skt as unknown;
	}

	/**
	 * Liefert den aktuellen Schülerklausurtermin zu einer übergebenen Schülerklausur
	 *
	 * @param idSchuelerklausur die ID der Schülerklausur, deren aktueller Schülerklausurtermin gesucht wird
	 *
	 * @return den aktuellen Schülerklausurtermin
	 */
	public schuelerklausurterminaktuellBySchuelerklausur(idSchuelerklausur : number) : GostSchuelerklausurTermin {
		const skts : List<GostSchuelerklausurTermin | null> = DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurterminmenge_by_idSchuelerklausur, idSchuelerklausur);
		return DeveloperNotificationException.ifNull("Schülerklausur " + idSchuelerklausur + " enthält keine Schülerklausurtermine.", skts.getLast());
	}

	/**
	 * Liefert eine Liste von aktuellen Nachschreib-GostSchuelerklausurTermin-Objekten zum übergebenen Quartal für
	 * die ein Termin gesetzt wurde
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostSchuelerklausurTermin-Objekten
	 */
	public schuelerklausurterminNtAktuellMitTerminGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		if (quartal > 0) {
			const skts : JavaMap<number, List<GostSchuelerklausurTermin>> | null = this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.getMap4OrNull(abiJahrgang, halbjahr.id, quartal);
			if (skts !== null)
				for (const entry of skts.entrySet())
					if (entry.getKey() !== -1)
						ergebnis.addAll(entry.getValue());
		} else {
			const skts : JavaMap<number, JavaMap<number, List<GostSchuelerklausurTermin>>> | null = this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.getMap3OrNull(abiJahrgang, halbjahr.id);
			if (skts !== null)
				for (const entry of skts.entrySet())
					for (const entry2 of entry.getValue().entrySet())
						if (entry2.getKey() !== -1)
							ergebnis.addAll(entry2.getValue());
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von aktuellen Nachschreib-GostSchuelerklausurTermin-Objekten zum übergebenen Quartal für
	 * die ein Termin gesetzt wurde
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostSchuelerklausurTermin-Objekten
	 */
	public schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const termin of this.schuelerklausurterminNtAktuellMitTerminGetMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal)) {
			const t : GostKlausurtermin | null = this.terminOrNullBySchuelerklausurTermin(termin);
			if (t !== null && t.datum !== null)
				ergebnis.add(termin);
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von aktuellen Nachschreib-GostSchuelerklausurTermin-Objekten zum übergebenen Quartal für
	 * die noch kein Termin gesetzt wurde
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das Gosthalbjahr
	 * @param quartal  die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostSchuelerklausurTermin-Objekten
	 */
	public schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostSchuelerklausurTermin> {
		if (quartal > 0) {
			const skts : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.getOrNull(abiJahrgang, halbjahr.id, quartal, -1);
			return skts !== null ? skts : new ArrayList();
		}
		const skts : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		const mapHalbjahr : JavaMap<number, JavaMap<number, List<GostSchuelerklausurTermin>>> | null = this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.getMap3OrNull(abiJahrgang, halbjahr.id);
		if (mapHalbjahr !== null)
			for (const sktList of mapHalbjahr.values()) {
				const listTermine : List<GostSchuelerklausurTermin> | null = sktList.get(-1);
				if (listTermine !== null)
					skts.addAll(listTermine);
			}
		return skts;
	}

	/**
	 * Liefert eine Liste von  Nachschreib-GostSchuelerklausurTermin-Objekten zum übergebenen Klausurtermin
	 *
	 * @param termin der Gost-Klausurtermin
	 *
	 * @return die Liste von GostSchuelerklausurTermin-Objekten
	 */
	public schuelerklausurterminNtByTermin(termin : GostKlausurtermin) : List<GostSchuelerklausurTermin> {
		return this.schuelerklausurterminNtByTerminid(termin.id);
	}

	/**
	 * Liefert eine Liste von  Nachschreib-GostSchuelerklausurTermin-Objekten zur übergebenen Klausurtermin-ID
	 *
	 * @param idTermin die ID des Gost-Klausurtermins
	 *
	 * @return die Liste von GostSchuelerklausurTermin-Objekten
	 */
	public schuelerklausurterminNtByTerminid(idTermin : number) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		const listSkts : List<GostSchuelerklausurTermin> = this.schuelerklausurterminGetMengeByTerminid(idTermin);
		if (listSkts !== null)
			for (const skt of listSkts)
				if (skt.folgeNr > 0)
					ergebnis.add(skt);
		return ergebnis;
	}

	/**
	 * Liefert den GostSchuelerklausurTermin, sofern vorhanden, zu einer Klausurtermin-ID und einer Schüler-ID
	 *
	 * @param idTermin die ID des Klausurtermins
	 * @param idSchueler  die ID des Schülers
	 *
	 * @return das GostSchuelerklausurTermin-Objekt, sofern vorhanden
	 */
	public schuelerklausurterminByTerminidAndSchuelerid(idTermin : number, idSchueler : number) : GostSchuelerklausurTermin | null {
		const skts : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(idTermin);
		if (skts !== null)
			for (const skt of skts)
				if (this.schuelerklausurGetByIdOrException(skt.idSchuelerklausur).idSchueler === idSchueler)
					return skt;
		return null;
	}

	/**
	 * Liefert die GostSchuelerklausur-Objekte zur übergebenen Kursklausur-ID
	 *
	 * @param idKursklausur die ID der Kursklausur
	 *
	 * @return die GostSchuelerklausur-Objekte
	 */
	public schuelerklausurGetMengeByKursklausurid(idKursklausur : number) : List<GostSchuelerklausur> {
		const listSks : List<GostSchuelerklausur> | null = this._schuelerklausurmenge_by_idKursklausur.get(idKursklausur);
		return listSks === null ? new ArrayList() : listSks;
	}

	/**
	 * Liefert das Lehrerkürzel zu einer übergebenen Kursklausur.
	 *
	 * @param k die Kursklausur
	 *
	 * @return das Lehrerkürzel
	 */
	public kursLehrerKuerzelByKursklausur(k : GostKursklausur) : string {
		const kurs : KursDaten = this.getKursByKursklausur(k);
		const lehrer : LehrerListeEintrag | null = this.getLehrerMap().get(kurs.lehrer);
		if (lehrer === null)
			throw new DeveloperNotificationException("Lehrer mit ID " + kurs.lehrer + " nicht in LehrerMap vorhanden.")
		return lehrer.kuerzel;
	}

	/**
	 * Liefert den KursDaten aus dem KursManager zu einer übergebenen Kursklausur.
	 *
	 * @param k die Kursklausur
	 *
	 * @return den KursDaten
	 */
	public getKursByKursklausur(k : GostKursklausur) : KursDaten {
		const kurs : KursDaten | null = this.getKursManager().get(k.idKurs);
		if (kurs === null)
			throw new DeveloperNotificationException("Kurs mit ID " + k.idKurs + " nicht in KursManager vorhanden.")
		return kurs;
	}

	/**
	 * Liefert das GostFach aus dem GostFaecherManager zu einer übergebenen Kursklausur.
	 *
	 * @param k die Kursklausur
	 *
	 * @return das GostFach
	 */
	public getGostFachByKursklausur(k : GostKursklausur) : GostFach {
		const fach : GostFach | null = this.getFaecherManager().get(this.vorgabeByKursklausur(k).idFach);
		if (fach === null)
			throw new DeveloperNotificationException("Fach mit ID " + this.vorgabeByKursklausur(k).idFach + " nicht in GostFaecherManager vorhanden.")
		return fach;
	}

	/**
	 * Liefert das Lehrerkürzel zu einer übergebenen Kursklausur.
	 *
	 * @param k die Kursklausur
	 *
	 * @return das Lehrerkürzel
	 */
	public kursSchieneByKursklausur(k : GostKursklausur) : List<number> {
		return this.getKursByKursklausur(k).schienen;
	}

	/**
	 * Liefert die Kurzbezeichnung des Kurses zu einer übergebenen Kursklausur.
	 *
	 * @param k die Kursklausur
	 *
	 * @return die Kurzbezeichnung
	 */
	public kursKurzbezeichnungByKursklausur(k : GostKursklausur) : string {
		return this.getKursByKursklausur(k).kuerzel;
	}

	/**
	 * Liefert die Kurzbezeichnung des Kurses zu einer übergebenen Kursklausur.
	 *
	 * @param k die Kursklausur
	 *
	 * @return die Kurzbezeichnung
	 */
	public kursKurzbezeichnungBySchuelerklausur(k : GostSchuelerklausur) : string {
		return this.getKursByKursklausur(this.kursklausurBySchuelerklausur(k)).kuerzel;
	}

	/**
	 * Liefert die Anzahl aller Schüler im Kurs zu einer übergebenen Kursklausur.
	 *
	 * @param k die Kursklausur
	 *
	 * @return die Schüleranzahl
	 */
	public kursAnzahlSchuelerGesamtByKursklausur(k : GostKursklausur) : number {
		return this.getKursByKursklausur(k).schueler.size();
	}

	/**
	 * Liefert die Anzahl der Klausurscheiber im Kurs zu einer übergebenen Kursklausur.
	 *
	 * @param k die Kursklausur
	 *
	 * @return die Schüleranzahl
	 */
	public kursAnzahlKlausurschreiberByKursklausur(k : GostKursklausur) : number {
		const liste : List<GostSchuelerklausur> | null = this._schuelerklausurmenge_by_idKursklausur.get(k.id);
		return liste === null ? 0 : liste.size();
	}

	/**
	 * Liefert das Kürzel zur Anzeige des Faches zu einer übergebenen Kursklausur.
	 * Falls kein Anzeigekürzel gesetzt ist, wird das interne Kürzel zurückgegeben.
	 *
	 * @param k die Kursklausur
	 *
	 * @return die Kurzbezeichnung
	 */
	public fachKuerzelAnzeigeByKursklausur(k : GostKursklausur) : string {
		const fach : GostFach | null = this.getGostFachByKursklausur(k);
		return fach.kuerzelAnzeige !== null ? fach.kuerzelAnzeige : fach.kuerzel;
	}

	/**
	 * Liefert das Kürzel zur Anzeige des Faches zu einer übergebenen Kursklausur.
	 * Falls kein Anzeigekürzel gesetzt ist, wird das interne Kürzel zurückgegeben.
	 *
	 * @param k die Kursklausur
	 *
	 * @return die Kurzbezeichnung
	 */
	public fachKuerzelByKursklausur(k : GostKursklausur) : string {
		return this.getGostFachByKursklausur(k).kuerzel;
	}

	/**
	 * Liefert die Hintergrundfarbe zur übergebenen Kursklausur.
	 *
	 * @param k die Kursklausur
	 *
	 * @return die Hintergrundfarbe
	 */
	public fachBgColorByKursklausur(k : GostKursklausur) : string {
		return ZulaessigesFach.getByKuerzelASD(this.fachKuerzelByKursklausur(k)).getHMTLFarbeRGBA(1.0);
	}

	/**
	 * Liefert den Vorgänger-Schülerklausurtermin zu einer Schülerklausur, also den versäumte Schülerklausurtermin
	 *
	 * @param skt der Schülerklausurtermin, dessen Vorgänger gesucht wird
	 *
	 * @return den Vorgänger-Schülerklausurtermin
	 */
	public schuelerklausurterminVorgaengerBySchuelerklausurtermin(skt : GostSchuelerklausurTermin) : GostSchuelerklausurTermin | null {
		const alleTermine : List<GostSchuelerklausurTermin> = DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur);
		for (const skAktuell of alleTermine)
			if (skAktuell.folgeNr === skt.folgeNr - 1)
				return skAktuell;
		return null;
	}

	/**
	 * Prüft, ob eine Kursklausur externe Schüler enthält
	 *
	 * @param k die zu prüfende Kursklausur
	 *
	 * @return true, falls externe Schüler enthalten sind, sonst false
	 */
	public kursklausurMitExternenS(k : GostKursklausur) : boolean {
		const listSks : List<GostSchuelerklausur> | null = this._schuelerklausurmenge_by_idKursklausur.get(k.id);
		if (listSks !== null)
			for (const sk of listSks)
				if (DeveloperNotificationException.ifMapGetIsNull(this.getSchuelerMap(), sk.idSchueler).externeSchulNr !== null)
					return true;
		return false;
	}

	/**
	 * Gibt das Datum des Vorgängertermins zum Übergebenen Schülerklausurtermin zurück.
	 *
	 * @param skt der Schülerklausurtermin
	 *
	 * @return das Datum als String oder null
	 */
	public datumSchuelerklausurVorgaenger(skt : GostSchuelerklausurTermin) : string | null {
		const vorgaengerSkt : GostSchuelerklausurTermin | null = this.schuelerklausurterminVorgaengerBySchuelerklausurtermin(skt);
		if (vorgaengerSkt === null)
			throw new DeveloperNotificationException("Kein Vorgängertermin zu Schülerklausurtermin gefunden.")
		const termin : GostKlausurtermin | null = this.terminOrNullBySchuelerklausurTermin(vorgaengerSkt);
		return termin === null ? null : termin.datum;
	}

	/**
	 * Erstellt ein GostKlausurenUpdate-Objekt für den API-Call, das beim übergebenen Gost-Klausurtermin die Nachschreiberzulassung entfernt und ggf. schon zugewiesene Schülerklausurtermine aus diesem entfernt.
	 *
	 * @param termin der Schülerklausurtermin
	 *
	 * @return das GostKlausurenUpdate-Objekt mit den zu patchenden GostSchuelerklausurTermin-Objekten und dem Gost-Klausurtermin
	 */
	public patchKlausurterminNachschreiberZuglassenFalse(termin : GostKlausurtermin) : GostKlausurenUpdate {
		const update : GostKlausurenUpdate | null = new GostKlausurenUpdate();
		update.listKlausurtermineNachschreiberZugelassenFalse.add(termin.id);
		for (const skt of this.schuelerklausurterminNtByTermin(termin))
			update.listSchuelerklausurTermineRemoveIdTermin.add(skt.id);
		return update;
	}

	/**
	 * Führt alle Attribut-Patches aller Objekte im übergeben Update-Objekt im Manager durch.
	 *
	 * @param update das GostKlausurenUpdate-Objekt mit den zu patchenden
	 */
	public updateExecute(update : GostKlausurenUpdate) : void {
		for (const sktId of update.listSchuelerklausurTermineRemoveIdTermin) {
			const skt : GostSchuelerklausurTermin = this.schuelerklausurterminGetByIdOrException(sktId!);
			skt.idTermin = null;
			this.schuelerklausurterminPatchAttributes(skt);
		}
		for (const ktId of update.listKlausurtermineNachschreiberZugelassenFalse) {
			const kt : GostKlausurtermin = this.terminGetByIdOrException(ktId!);
			kt.nachschreiberZugelassen = false;
			this.terminPatchAttributes(kt);
		}
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
