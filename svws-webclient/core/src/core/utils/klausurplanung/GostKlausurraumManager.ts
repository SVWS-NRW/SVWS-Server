import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostKursklausur } from '../../../core/data/gost/klausurplanung/GostKursklausur';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostKlausurraumstunde } from '../../../core/data/gost/klausurplanung/GostKlausurraumstunde';
import { GostSchuelerklausur } from '../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { MapUtils } from '../../../core/utils/MapUtils';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import type { Comparator } from '../../../java/util/Comparator';
import { GostKlausurenCollectionSkrsKrs } from '../../../core/data/gost/klausurplanung/GostKlausurenCollectionSkrsKrs';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { GostKursklausurManager, cast_de_svws_nrw_core_utils_klausurplanung_GostKursklausurManager } from '../../../core/utils/klausurplanung/GostKursklausurManager';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { GostKlausurraum, cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraum } from '../../../core/data/gost/klausurplanung/GostKlausurraum';
import type { JavaMap } from '../../../java/util/JavaMap';
import { GostSchuelerklausurraumstunde } from '../../../core/data/gost/klausurplanung/GostSchuelerklausurraumstunde';

export class GostKlausurraumManager extends JavaObject {

	private readonly _kursklausurManager : GostKursklausurManager;

	/**
	 * Ein Comparator für die GostKlausurräume.
	 */
	private static readonly _compRaum : Comparator<GostKlausurraum> = { compare : (a: GostKlausurraum, b: GostKlausurraum) => JavaLong.compare(a.id, b.id) };

	private readonly _raum_by_id : JavaMap<number, GostKlausurraum> = new HashMap();

	private readonly _raummenge : List<GostKlausurraum> = new ArrayList();

	private readonly _klausurraum_by_idStundenplanraum : JavaMap<number, GostKlausurraum> = new HashMap();

	private readonly _klausurraum_by_idSchuelerklausur : JavaMap<number, GostKlausurraum> = new HashMap();

	private readonly _raumstunde_by_id : JavaMap<number, GostKlausurraumstunde> = new HashMap();

	private readonly _raumstundenmenge : List<GostKlausurraumstunde> = new ArrayList();

	private readonly _raumstundenmenge_by_idRaum : JavaMap<number, List<GostKlausurraumstunde>> = new HashMap();

	private readonly _raumstunde_by_idRaum_and_idZeitraster : HashMap2D<number, number, GostKlausurraumstunde> = new HashMap2D();

	private readonly _raumstundenmenge_by_idSchuelerklausur : JavaMap<number, List<GostKlausurraumstunde>> = new HashMap();

	private readonly _schuelerklausur_by_id : JavaMap<number, GostSchuelerklausur> = new HashMap();

	private readonly _schuelerklausurmenge : List<GostSchuelerklausur> = new ArrayList();

	private readonly _schuelerklausurmenge_by_idRaum : JavaMap<number, List<GostSchuelerklausur>> = new HashMap();

	private readonly _schuelerklausurmenge_by_idRaum_and_idKursklausur : HashMap2D<number, number, List<GostSchuelerklausur>> = new HashMap2D();

	private readonly _schuelerklausurmenge_by_idKursklausur : JavaMap<number, List<GostSchuelerklausur>> = new HashMap();

	private readonly _schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde : HashMap2D<number, number, GostSchuelerklausurraumstunde> = new HashMap2D();

	private readonly _schuelerklausurraumstundenmenge : List<GostSchuelerklausurraumstunde> = new ArrayList();

	private readonly _schuelerklausurraumstundenmenge_by_idRaumstunde : JavaMap<number, List<GostSchuelerklausurraumstunde>> = new HashMap();

	private readonly _schuelerklausurraumstundenmenge_by_idSchuelerklausur : JavaMap<number, List<GostSchuelerklausurraumstunde>> = new HashMap();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raum              der Gost-Klausurraum
	 * @param stunden           die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 * @param kursklausurmanager der Kursklausur-Manager
	 */
	public constructor(raum : GostKlausurraum, stunden : List<GostKlausurraumstunde>, schuelerklausuren : List<GostSchuelerklausur>, kursklausurmanager : GostKursklausurManager);

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raeume            die Liste der GostKlausurräume eines
	 *                          Gost-Klausurtermins
	 * @param listRs            die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param listSkrs          die Liste der Schülerklausurraumstunden
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 * @param kursklausurmanager der Kursklausur-Manager
	 */
	public constructor(raeume : List<GostKlausurraum>, listRs : List<GostKlausurraumstunde>, listSkrs : List<GostSchuelerklausurraumstunde>, schuelerklausuren : List<GostSchuelerklausur>, kursklausurmanager : GostKursklausurManager);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : GostKlausurraum | List<GostKlausurraum>, __param1 : List<GostKlausurraumstunde>, __param2 : List<GostSchuelerklausur> | List<GostSchuelerklausurraumstunde>, __param3 : GostKursklausurManager | List<GostSchuelerklausur>, __param4? : GostKursklausurManager) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && ((__param2 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.utils.klausurplanung.GostKursklausurManager')))) && (typeof __param4 === "undefined")) {
			const raum : GostKlausurraum = cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraum(__param0);
			const stunden : List<GostKlausurraumstunde> = cast_java_util_List(__param1);
			const schuelerklausuren : List<GostSchuelerklausur> = cast_java_util_List(__param2);
			const kursklausurmanager : GostKursklausurManager = cast_de_svws_nrw_core_utils_klausurplanung_GostKursklausurManager(__param3);
			this._kursklausurManager = kursklausurmanager;
			const raeume : List<GostKlausurraum> | null = new ArrayList();
			raeume.add(raum);
			this.initAll(raeume, stunden, new ArrayList(), schuelerklausuren);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && ((__param2 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && ((__param4 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.utils.klausurplanung.GostKursklausurManager'))))) {
			const raeume : List<GostKlausurraum> = cast_java_util_List(__param0);
			const listRs : List<GostKlausurraumstunde> = cast_java_util_List(__param1);
			const listSkrs : List<GostSchuelerklausurraumstunde> = cast_java_util_List(__param2);
			const schuelerklausuren : List<GostSchuelerklausur> = cast_java_util_List(__param3);
			const kursklausurmanager : GostKursklausurManager = cast_de_svws_nrw_core_utils_klausurplanung_GostKursklausurManager(__param4);
			this._kursklausurManager = kursklausurmanager;
			this.initAll(raeume, listRs, listSkrs, schuelerklausuren);
		} else throw new Error('invalid method overload');
	}

	private initAll(listRaum : List<GostKlausurraum>, listRaumstunde : List<GostKlausurraumstunde>, listSchuelerklausurraumstunde : List<GostSchuelerklausurraumstunde>, listSchuelerklausur : List<GostSchuelerklausur>) : void {
		this.raumAddAll(listRaum);
		this.raumstundeAddAll(listRaumstunde);
		this.schuelerklausurAddAll(listSchuelerklausur);
		this.schuelerklausurraumstundeAddAll(listSchuelerklausurraumstunde);
		this.update_all();
	}

	private update_all() : void {
		this.update_raummenge();
		this.update_raumstundenmenge();
		this.update_schuelerklausurmenge();
		this.update_schuelerklausurraumstundenmenge();
		this.update_klausurraum_by_idStundenplanraum();
		this.update_raumstundenmenge_by_idRaum();
		this.update_raumstunde_by_idRaum_and_idZeitraster();
		this.update_raumstundenmenge_by_idSchuelerklausur();
		this.update_schuelerklausurmenge_by_idRaum();
		this.update_schuelerklausurmenge_by_idRaum_and_idKursklausur();
		this.update_schuelerklausurmenge_by_idKursklausur();
		this.update_schuelerklausurraumstundenmenge_by_idRaumstunde();
		this.update_schuelerklausurraumstundenmenge_by_idSchuelerklausur();
		this.update_klausurraum_by_idSchuelerklausur();
	}

	private update_klausurraum_by_idStundenplanraum() : void {
		this._klausurraum_by_idStundenplanraum.clear();
		for (const raum of this._raummenge)
			if (raum.idStundenplanRaum !== null)
				DeveloperNotificationException.ifMapPutOverwrites(this._klausurraum_by_idStundenplanraum, raum.idStundenplanRaum, raum);
	}

	private update_raumstundenmenge_by_idRaum() : void {
		this._raumstundenmenge_by_idRaum.clear();
		for (const krs of this._raumstundenmenge)
			MapUtils.getOrCreateArrayList(this._raumstundenmenge_by_idRaum, krs.idRaum).add(krs);
	}

	private update_raumstunde_by_idRaum_and_idZeitraster() : void {
		this._raumstunde_by_idRaum_and_idZeitraster.clear();
		for (const rs of this._raumstundenmenge)
			DeveloperNotificationException.ifMap2DPutOverwrites(this._raumstunde_by_idRaum_and_idZeitraster, rs.idRaum, rs.idZeitraster, rs);
	}

	private update_raumstundenmenge_by_idSchuelerklausur() : void {
		this._raumstundenmenge_by_idSchuelerklausur.clear();
		for (const skrs of this._schuelerklausurraumstundenmenge)
			MapUtils.getOrCreateArrayList(this._raumstundenmenge_by_idSchuelerklausur, skrs.idSchuelerklausur).add(DeveloperNotificationException.ifMapGetIsNull(this._raumstunde_by_id, skrs.idRaumstunde));
	}

	private update_schuelerklausurmenge_by_idRaum() : void {
		this._schuelerklausurmenge_by_idRaum.clear();
		for (const k of this._schuelerklausurmenge) {
			const raumstunden : List<GostKlausurraumstunde> | null = this._raumstundenmenge_by_idSchuelerklausur.get(k.idSchuelerklausur);
			MapUtils.getOrCreateArrayList(this._schuelerklausurmenge_by_idRaum, raumstunden === null || raumstunden.isEmpty() ? -1 : raumstunden.get(0).idRaum).add(k);
		}
	}

	private update_schuelerklausurmenge_by_idRaum_and_idKursklausur() : void {
		this._schuelerklausurmenge_by_idRaum_and_idKursklausur.clear();
		for (const k of this._schuelerklausurmenge) {
			const raumstunden : List<GostKlausurraumstunde> | null = this._raumstundenmenge_by_idSchuelerklausur.get(k.idSchuelerklausur);
			Map2DUtils.getOrCreateArrayList(this._schuelerklausurmenge_by_idRaum_and_idKursklausur, raumstunden === null || raumstunden.isEmpty() ? -1 : raumstunden.get(0).idRaum, k.idKursklausur).add(k);
		}
	}

	private update_schuelerklausurmenge_by_idKursklausur() : void {
		this._schuelerklausurmenge_by_idKursklausur.clear();
		for (const k of this._schuelerklausurmenge)
			MapUtils.getOrCreateArrayList(this._schuelerklausurmenge_by_idKursklausur, k.idKursklausur).add(k);
	}

	private update_schuelerklausurraumstundenmenge_by_idRaumstunde() : void {
		this._schuelerklausurraumstundenmenge_by_idRaumstunde.clear();
		for (const skrs of this._schuelerklausurraumstundenmenge)
			MapUtils.getOrCreateArrayList(this._schuelerklausurraumstundenmenge_by_idRaumstunde, skrs.idRaumstunde).add(skrs);
	}

	private update_schuelerklausurraumstundenmenge_by_idSchuelerklausur() : void {
		this._schuelerklausurraumstundenmenge_by_idSchuelerklausur.clear();
		for (const skrs of this._schuelerklausurraumstundenmenge)
			MapUtils.getOrCreateArrayList(this._schuelerklausurraumstundenmenge_by_idSchuelerklausur, skrs.idSchuelerklausur).add(skrs);
	}

	private update_klausurraum_by_idSchuelerklausur() : void {
		this._klausurraum_by_idSchuelerklausur.clear();
		for (const skrs of this._schuelerklausurraumstundenmenge) {
			const krsList : List<GostKlausurraumstunde> = DeveloperNotificationException.ifMapGetIsNull(this._raumstundenmenge_by_idSchuelerklausur, skrs.idSchuelerklausur);
			for (const krs of krsList) {
				const kr : GostKlausurraum = DeveloperNotificationException.ifMapGetIsNull(this._raum_by_id, krs.idRaum);
				const krAlt : GostKlausurraum | null = this._klausurraum_by_idSchuelerklausur.put(skrs.idSchuelerklausur, kr);
				if (krAlt !== null && krAlt as unknown !== kr as unknown)
					throw new DeveloperNotificationException("Schülerklausur " + skrs.idSchuelerklausur + " ist zwei Klausurräumen zugeordnet.")
			}
		}
	}

	private update_raummenge() : void {
		this._raummenge.clear();
		this._raummenge.addAll(this._raum_by_id.values());
		this._raummenge.sort(GostKlausurraumManager._compRaum);
	}

	private raumAddOhneUpdate(raum : GostKlausurraum) : void {
		GostKlausurraumManager.raumCheck(raum);
		DeveloperNotificationException.ifMapPutOverwrites(this._raum_by_id, raum.id, raum);
	}

	/**
	 * Fügt ein {@link GostKlausurraum}-Objekt hinzu.
	 *
	 * @param raum Das {@link GostKlausurraum}-Objekt, welches hinzugefügt werden
	 *             soll.
	 */
	public raumAdd(raum : GostKlausurraum) : void {
		this.raumAddOhneUpdate(raum);
		this.update_all();
	}

	private raumAddAllOhneUpdate(listRaum : List<GostKlausurraum>) : void {
		for (const raum of listRaum)
			this.raumAddOhneUpdate(raum);
	}

	/**
	 * Fügt alle {@link GostKlausurraum}-Objekte hinzu.
	 *
	 * @param listRaum Die Menge der {@link GostKlausurraum}-Objekte, welche
	 *                 hinzugefügt werden soll.
	 */
	public raumAddAll(listRaum : List<GostKlausurraum>) : void {
		this.raumAddAllOhneUpdate(listRaum);
		this.update_all();
	}

	private static raumCheck(raum : GostKlausurraum) : void {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurraum}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurraum}-Objekt.
	 */
	public raumGetByIdOrException(idRaum : number) : GostKlausurraum {
		return DeveloperNotificationException.ifMapGetIsNull(this._raum_by_id, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurraum}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurraum}-Objekte.
	 */
	public raumGetMengeAsList() : List<GostKlausurraum> {
		return this._raummenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurraum}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param raum Das neue {@link GostKlausurraum}-Objekt.
	 */
	public raumPatchAttributes(raum : GostKlausurraum) : void {
		GostKlausurraumManager.raumCheck(raum);
		DeveloperNotificationException.ifMapRemoveFailes(this._raum_by_id, raum.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._raum_by_id, raum.id, raum);
		this.update_all();
	}

	private raumRemoveOhneUpdateById(idRaum : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._raum_by_id, idRaum);
		const rsList : List<GostKlausurraumstunde> | null = this._raumstundenmenge_by_idRaum.get(idRaum);
		if (rsList !== null)
			for (const rs of rsList)
				this.raumstundeRemoveOhneUpdateById(rs.id);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurraum}-Objekt.
	 *
	 * @param idRaum Die ID des {@link GostKlausurraum}-Objekts.
	 */
	public raumRemoveById(idRaum : number) : void {
		this.raumRemoveOhneUpdateById(idRaum);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum Die Liste der zu entfernenden
	 *                 {@link StundenplanRaum}-Objekte.
	 */
	public raumRemoveAll(listRaum : List<GostKlausurraum>) : void {
		for (const raum of listRaum)
			this.raumRemoveOhneUpdateById(raum.id);
		this.update_all();
	}

	private update_raumstundenmenge() : void {
		this._raumstundenmenge.clear();
		this._raumstundenmenge.addAll(this._raumstunde_by_id.values());
	}

	private raumstundeAddOhneUpdate(raumstunde : GostKlausurraumstunde) : void {
		GostKlausurraumManager.raumstundeCheck(raumstunde);
		DeveloperNotificationException.ifMapPutOverwrites(this._raumstunde_by_id, raumstunde.id, raumstunde);
	}

	/**
	 * Fügt ein {@link GostKlausurraumstunde}-Objekt hinzu.
	 *
	 * @param raumstunde Das {@link GostKlausurraumstunde}-Objekt, welches
	 *                   hinzugefügt werden soll.
	 */
	public raumstundeAdd(raumstunde : GostKlausurraumstunde) : void {
		this.raumstundeAddOhneUpdate(raumstunde);
		this.update_all();
	}

	private raumstundeAddAllOhneUpdate(listRaumstunde : List<GostKlausurraumstunde>) : void {
		for (const raumstunde of listRaumstunde)
			this.raumstundeAddOhneUpdate(raumstunde);
	}

	/**
	 * Fügt alle {@link GostKlausurraumstunde}-Objekte hinzu.
	 *
	 * @param listRaumstunde Die Menge der {@link GostKlausurraumstunde}-Objekte,
	 *                       welche hinzugefügt werden soll.
	 */
	public raumstundeAddAll(listRaumstunde : List<GostKlausurraumstunde>) : void {
		this.raumstundeAddAllOhneUpdate(listRaumstunde);
		this.update_all();
	}

	private static raumstundeCheck(raumstunde : GostKlausurraumstunde) : void {
		DeveloperNotificationException.ifInvalidID("raumstunde.id", raumstunde.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurraumstunde}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idRaumstunde Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurraumstunde}-Objekt.
	 */
	public raumstundeGetByIdOrException(idRaumstunde : number) : GostKlausurraumstunde {
		return DeveloperNotificationException.ifMapGetIsNull(this._raumstunde_by_id, idRaumstunde);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurraumstunde}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurraumstunde}-Objekte.
	 */
	public raumstundeGetMengeAsList() : List<GostKlausurraumstunde> {
		return this._raumstundenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurraumstunde}-Objekt durch das
	 * neue Objekt.
	 *
	 * @param raumstunde Das neue {@link GostKlausurraumstunde}-Objekt.
	 */
	public raumstundePatchAttributes(raumstunde : GostKlausurraumstunde) : void {
		GostKlausurraumManager.raumstundeCheck(raumstunde);
		DeveloperNotificationException.ifMapRemoveFailes(this._raumstunde_by_id, raumstunde.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._raumstunde_by_id, raumstunde.id, raumstunde);
		this.update_all();
	}

	private raumstundeRemoveOhneUpdateById(idRaumstunde : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._raumstunde_by_id, idRaumstunde);
		const skrsList : List<GostSchuelerklausurraumstunde> | null = this._schuelerklausurraumstundenmenge_by_idRaumstunde.get(idRaumstunde);
		if (skrsList !== null)
			for (const skrs of skrsList)
				this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(skrs.idSchuelerklausur, skrs.idRaumstunde);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurraumstunde}-Objekt.
	 *
	 * @param idRaumstunde Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public raumstundeRemoveById(idRaumstunde : number) : void {
		this.raumstundeRemoveOhneUpdateById(idRaumstunde);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurraumstunde}-Objekte.
	 *
	 * @param listRaumstunde Die Liste der zu entfernenden
	 *                       {@link GostKlausurraumstunde}-Objekte.
	 */
	public raumstundeRemoveAll(listRaumstunde : List<GostKlausurraumstunde>) : void {
		for (const raumstunde of listRaumstunde)
			this.raumstundeRemoveOhneUpdateById(raumstunde.id);
		this.update_all();
	}

	private update_schuelerklausurmenge() : void {
		this._schuelerklausurmenge.clear();
		this._schuelerklausurmenge.addAll(this._schuelerklausur_by_id.values());
	}

	private schuelerklausurAddOhneUpdate(schuelerklausur : GostSchuelerklausur) : void {
		GostKlausurraumManager.schuelerklausurCheck(schuelerklausur);
		DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausur_by_id, schuelerklausur.idSchuelerklausur, schuelerklausur);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausur}-Objekt hinzu.
	 *
	 * @param schuelerklausur Das {@link GostSchuelerklausur}-Objekt, welches
	 *                        hinzugefügt werden soll.
	 */
	public schuelerklausurAdd(schuelerklausur : GostSchuelerklausur) : void {
		this.schuelerklausurAddOhneUpdate(schuelerklausur);
		this.update_all();
	}

	private schuelerklausurAddAllOhneUpdate(listSchuelerklausur : List<GostSchuelerklausur>) : void {
		for (const schuelerklausur of listSchuelerklausur)
			this.schuelerklausurAddOhneUpdate(schuelerklausur);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausur}-Objekte hinzu.
	 *
	 * @param listSchuelerklausur Die Menge der {@link GostSchuelerklausur}-Objekte,
	 *                            welche hinzugefügt werden soll.
	 */
	public schuelerklausurAddAll(listSchuelerklausur : List<GostSchuelerklausur>) : void {
		this.schuelerklausurAddAllOhneUpdate(listSchuelerklausur);
		this.update_all();
	}

	private static schuelerklausurCheck(schuelerklausur : GostSchuelerklausur) : void {
		DeveloperNotificationException.ifInvalidID("schuelerklausur.idSchuelerklausur", schuelerklausur.idSchuelerklausur);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausur}-Objekt.
	 */
	public schuelerklausurGetByIdOrException(idSchuelerklausur : number) : GostSchuelerklausur {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausur_by_id, idSchuelerklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausur}-Objekte.
	 */
	public schuelerklausurGetMengeAsList() : List<GostSchuelerklausur> {
		return this._schuelerklausurmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param schuelerklausur Das neue {@link GostSchuelerklausur}-Objekt.
	 */
	public schuelerklausurPatchAttributes(schuelerklausur : GostSchuelerklausur) : void {
		GostKlausurraumManager.schuelerklausurCheck(schuelerklausur);
		DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausur_by_id, schuelerklausur.idSchuelerklausur);
		DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausur_by_id, schuelerklausur.idSchuelerklausur, schuelerklausur);
		this.update_all();
	}

	private schuelerklausurRemoveOhneUpdateById(idSchuelerklausur : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausur_by_id, idSchuelerklausur);
		this.schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausur(idSchuelerklausur);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausur}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 */
	public schuelerklausurRemoveById(idSchuelerklausur : number) : void {
		this.schuelerklausurRemoveOhneUpdateById(idSchuelerklausur);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausur}-Objekte.
	 *
	 * @param listSchuelerklausur Die Liste der zu entfernenden
	 *                            {@link GostSchuelerklausur}-Objekte.
	 */
	public schuelerklausurRemoveAll(listSchuelerklausur : List<GostSchuelerklausur>) : void {
		for (const schuelerklausur of listSchuelerklausur)
			this.schuelerklausurRemoveOhneUpdateById(schuelerklausur.idSchuelerklausur);
		this.update_all();
	}

	private update_schuelerklausurraumstundenmenge() : void {
		this._schuelerklausurraumstundenmenge.clear();
		this._schuelerklausurraumstundenmenge.addAll(this._schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde.getNonNullValuesAsList());
	}

	private schuelerklausurraumstundeAddOhneUpdate(schuelerklausurraumstunde : GostSchuelerklausurraumstunde) : void {
		GostKlausurraumManager.schuelerklausurraumstundeCheck(schuelerklausurraumstunde);
		DeveloperNotificationException.ifMap2DPutOverwrites(this._schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, schuelerklausurraumstunde.idSchuelerklausur, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurraumstunde}-Objekt hinzu.
	 *
	 * @param schuelerklausurraumstunde Das
	 *                                  {@link GostSchuelerklausurraumstunde}-Objekt,
	 *                                  welches hinzugefügt werden soll.
	 */
	public schuelerklausurraumstundeAdd(schuelerklausurraumstunde : GostSchuelerklausurraumstunde) : void {
		this.schuelerklausurraumstundeAddOhneUpdate(schuelerklausurraumstunde);
		this.update_all();
	}

	private schuelerklausurraumstundeAddAllOhneUpdate(listSchuelerklausurraumstunde : List<GostSchuelerklausurraumstunde>) : void {
		for (const schuelerklausurraumstunde of listSchuelerklausurraumstunde)
			this.schuelerklausurraumstundeAddOhneUpdate(schuelerklausurraumstunde);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurraumstunde}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurraumstunde Die Menge der
	 *                                      {@link GostSchuelerklausurraumstunde}-Objekte,
	 *                                      welche hinzugefügt werden soll.
	 */
	public schuelerklausurraumstundeAddAll(listSchuelerklausurraumstunde : List<GostSchuelerklausurraumstunde>) : void {
		this.schuelerklausurraumstundeAddAllOhneUpdate(listSchuelerklausurraumstunde);
		this.update_all();
	}

	private static schuelerklausurraumstundeCheck(schuelerklausurraumstunde : GostSchuelerklausurraumstunde) : void {
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.id", schuelerklausurraumstunde.idSchuelerklausur);
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.id", schuelerklausurraumstunde.idRaumstunde);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurraumstunde}-Objekt.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurraumstunde}-Objekt.
	 */
	public schuelerklausurraumstundeGetByIdSchuelerklausurAndIdRaumstundeOrException(idSchuelerklausur : number, idRaumstunde : number) : GostSchuelerklausurraumstunde {
		return DeveloperNotificationException.ifMap2DGetIsNull(this._schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, idSchuelerklausur, idRaumstunde);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurraumstunde}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurraumstunde}-Objekte.
	 */
	public schuelerklausurraumstundeGetMengeAsList() : List<GostSchuelerklausurraumstunde> {
		return this._schuelerklausurraumstundenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurraumstunde}-Objekt
	 * durch das neue Objekt.
	 *
	 * @param schuelerklausurraumstunde Das neue
	 *                                  {@link GostSchuelerklausurraumstunde}-Objekt.
	 */
	public schuelerklausurraumstundePatchAttributes(schuelerklausurraumstunde : GostSchuelerklausurraumstunde) : void {
		GostKlausurraumManager.schuelerklausurraumstundeCheck(schuelerklausurraumstunde);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, schuelerklausurraumstunde.idSchuelerklausur, schuelerklausurraumstunde.idRaumstunde);
		DeveloperNotificationException.ifMap2DPutOverwrites(this._schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, schuelerklausurraumstunde.idSchuelerklausur, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
		this.update_all();
	}

	private schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(idSchuelerklausur : number, idRaumstunde : number) : void {
		DeveloperNotificationException.ifMap2DRemoveFailes(this._schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, idSchuelerklausur, idRaumstunde);
	}

	private schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausur(idSchuelerklausur : number) : void {
		this._schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde.removeSubMap(idSchuelerklausur);
	}

	private schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausur(idSchuelerklausur : number) : void {
		const skrsList : List<GostSchuelerklausurraumstunde> | null = this._schuelerklausurraumstundenmenge_by_idSchuelerklausur.get(idSchuelerklausur);
		if (skrsList !== null)
			for (const skrs of skrsList)
				DeveloperNotificationException.ifMap2DRemoveFailes(this._schuelerklausurraumstunde_by_idSchuelerklausur_and_idRaumstunde, skrs.idSchuelerklausur, skrs.idRaumstunde);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public schuelerklausurraumstundeRemoveByIdSchuelerklausurAndIdRaumstunde(idSchuelerklausur : number, idRaumstunde : number) : void {
		this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(idSchuelerklausur, idRaumstunde);
		this.update_all();
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 */
	public schuelerklausurraumstundeRemoveByIdSchuelerklausur(idSchuelerklausur : number) : void {
		this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausur(idSchuelerklausur);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurraumstunde}-Objekte, deren Schülerklausur-ID in der übergebenen Liste enthalten ist.
	 *
	 * @param idsSchuelerklausuren die Liste der Schülerklausur-IDs.
	 */
	public schuelerklausurraumstundeRemoveAllByIdSchuelerklausur(idsSchuelerklausuren : List<number>) : void {
		for (const idSchuelerklausur of idsSchuelerklausuren)
			this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausur(idSchuelerklausur);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurraumstunde}-Objekte.
	 *
	 * @param listSchuelerklausurRaumstunde Die Liste der zu entfernenden
	 *                                      {@link GostSchuelerklausurraumstunde}-Objekte.
	 */
	public schuelerklausurraumstundeRemoveAll(listSchuelerklausurRaumstunde : List<GostSchuelerklausurraumstunde>) : void {
		for (const schuelerklausurraumstunde of listSchuelerklausurRaumstunde)
			this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurAndIdRaumstunde(schuelerklausurraumstunde.idSchuelerklausur, schuelerklausurraumstunde.idRaumstunde);
		this.update_all();
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum       die ID des Klausurraums
	 * @param idZeitraster die ID des Zeitrasters
	 *
	 * @return die Klausurraumstunde
	 */
	public klausurraumstundeGetByRaumidAndZeitrasterid(idRaum : number, idZeitraster : number) : GostKlausurraumstunde | null {
		return this._raumstunde_by_idRaum_and_idZeitraster.getOrNull(idRaum, idZeitraster);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum       die ID des Klausurraums
	 *
	 * @return die Klausurraumstunde
	 */
	public klausurraumstundeGetMengeByRaumid(idRaum : number) : List<GostKlausurraumstunde> {
		const stunden : List<GostKlausurraumstunde> | null = this._raumstundenmenge_by_idRaum.get(idRaum);
		return stunden !== null ? stunden : new ArrayList();
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Klausurraum geändert
	 * hat.
	 *
	 * @param collectionSkrsKrs das GostKlausurraum-Objekt
	 */
	public setzeRaumZuSchuelerklausuren(collectionSkrsKrs : GostKlausurenCollectionSkrsKrs) : void {
		this.raumstundeRemoveAll(collectionSkrsKrs.raumstundenGeloescht);
		this.raumstundeAddAll(collectionSkrsKrs.raumstunden);
		this.schuelerklausurraumstundeRemoveAllByIdSchuelerklausur(collectionSkrsKrs.idsSchuelerklausuren);
		this.schuelerklausurraumstundeAddAll(collectionSkrsKrs.skRaumstunden);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public kursklausurGetMenge() : List<GostKursklausur> {
		const kursklausuren : List<GostKursklausur> | null = new ArrayList();
		for (const kkId of this._schuelerklausurmenge_by_idKursklausur.keySet()) {
			kursklausuren.add(this._kursklausurManager.kursklausurGetByIdOrException(kkId));
		}
		return kursklausuren;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idKursklausur die Id der Kursklausur
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public schuelerklausurGetMengeByKursklausurid(idKursklausur : number) : List<GostSchuelerklausur> {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurmenge_by_idKursklausur, idKursklausur);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum  die Id des Klausurraums
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public kursklausurGetMengeByRaumid(idRaum : number) : List<GostKursklausur> {
		const kursklausuren : List<GostKursklausur> | null = new ArrayList();
		if (!this._schuelerklausurmenge_by_idRaum_and_idKursklausur.containsKey1(idRaum))
			return kursklausuren;
		for (const idKK of this._schuelerklausurmenge_by_idRaum_and_idKursklausur.getKeySetOf(idRaum)) {
			if (!this._schuelerklausurmenge_by_idRaum_and_idKursklausur.getNonNullOrException(idRaum, idKK).isEmpty())
				kursklausuren.add(this._kursklausurManager.kursklausurGetByIdOrException(idKK));
		}
		return kursklausuren;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum  die Id des Klausurraums
	 * @param idKursklausur die Id der Kursklausur
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public schuelerklausurGetMengeByRaumidAndKursklausurid(idRaum : number, idKursklausur : number) : List<GostSchuelerklausur> {
		return DeveloperNotificationException.ifMap2DGetIsNull(this._schuelerklausurmenge_by_idRaum_and_idKursklausur, idRaum, idKursklausur);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum  die Id des Klausurraums
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public schuelerklausurGetMengeByRaumid(idRaum : number) : List<GostSchuelerklausur> {
		const schuelerklausuren : List<GostSchuelerklausur> | null = new ArrayList();
		if (!this._schuelerklausurmenge_by_idRaum_and_idKursklausur.containsKey1(idRaum))
			return schuelerklausuren;
		for (const idKK of this._schuelerklausurmenge_by_idRaum_and_idKursklausur.getKeySetOf(idRaum))
			schuelerklausuren.addAll(this._schuelerklausurmenge_by_idRaum_and_idKursklausur.getNonNullOrException(idRaum, idKK));
		return schuelerklausuren;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public schuelerklausurOhneRaumGetMenge() : List<GostSchuelerklausur> {
		return this.schuelerklausurGetMengeByRaumid(-1);
	}

	/**
	 * Liefert eine Liste von Stundenplanräumen, die nicht für diesen Klausurtermin
	 * verplant sind.
	 *
	 * @param alleRaeume die Liste aller Stundenplanräume
	 *
	 * @return die Liste der nicht verplanten StundenplanRäume
	 */
	public stundenplanraumVerfuegbarGetMenge(alleRaeume : List<StundenplanRaum>) : List<StundenplanRaum> {
		const raeume : List<StundenplanRaum> | null = new ArrayList();
		for (const raum of alleRaeume)
			if (!this._klausurraum_by_idStundenplanraum.containsKey(raum.id))
				raeume.add(raum);
		return raeume;
	}

	/**
	 * Prüft, ob alle zu einer Kursklausur gehörenden Schülerklausuren einem Raum
	 * zugeordnet sind.
	 *
	 * @param kk die zu prüfende Kursklausur
	 *
	 * @return true, wenn alle Schülerklausuren verplant sind, sonst false
	 */
	public isAlleSchuelerklausurenVerplant(kk : GostKursklausur) : boolean {
		for (const sk of DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurmenge_by_idKursklausur, kk.id)) {
			if (!this._raumstundenmenge_by_idSchuelerklausur.containsKey(sk.idSchuelerklausur))
				return false;
		}
		return true;
	}

	/**
	 * Prüft, ob eine Kursklausur im übergebenen Klausurraum enthalten ist.
	 *
	 * @param idRaum der Raum, in dem die Kursklausur geprüft wird
	 * @param idKursklausur die zu prüfende Kursklausur
	 *
	 * @return true, wenn enthalten, sonst false
	 */
	public containsKlausurraumKursklausur(idRaum : number, idKursklausur : number) : boolean {
		return this._schuelerklausurmenge_by_idRaum_and_idKursklausur.contains(idRaum, idKursklausur);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplanung.GostKlausurraumManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplanung_GostKlausurraumManager(obj : unknown) : GostKlausurraumManager {
	return obj as GostKlausurraumManager;
}
