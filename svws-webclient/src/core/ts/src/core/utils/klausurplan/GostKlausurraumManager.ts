import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostKlausurraumstunde } from '../../../core/data/gost/klausuren/GostKlausurraumstunde';
import { GostSchuelerklausur } from '../../../core/data/gost/klausuren/GostSchuelerklausur';
import { MapUtils } from '../../../core/utils/MapUtils';
import type { Comparator } from '../../../java/util/Comparator';
import { GostKlausurenCollectionSkrsKrs } from '../../../core/data/gost/klausuren/GostKlausurenCollectionSkrsKrs';
import { GostKursklausurManager } from '../../../core/utils/klausurplan/GostKursklausurManager';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { GostKlausurraum, cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurraum } from '../../../core/data/gost/klausuren/GostKlausurraum';
import type { JavaMap } from '../../../java/util/JavaMap';
import { GostSchuelerklausurraumstunde } from '../../../core/data/gost/klausuren/GostSchuelerklausurraumstunde';

export class GostKlausurraumManager extends JavaObject {

	/**
	 * Die Kursklausuren, die im Manager vorhanden sind
	 */
	private readonly _raeume : List<GostKlausurraum> = new ArrayList();

	/**
	 * Eine Map id -> GostKlausurraum
	 */
	private readonly _mapIdRaum : JavaMap<number, GostKlausurraum> = new HashMap();

	/**
	 * Die Klausurraumstunden, die im Manager vorhanden sind
	 */
	private readonly _stunden : List<GostKlausurraumstunde> = new ArrayList();

	/**
	 * Eine Map id -> GostKlausurraumstunde
	 */
	private readonly _mapIdRaumStunde : JavaMap<number, GostKlausurraumstunde> = new HashMap();

	/**
	 * Eine Map idRaum -> Liste von Stunden
	 */
	private readonly _mapRaumStunden : JavaMap<number, List<GostKlausurraumstunde>> = new HashMap();

	/**
	 * Eine Map idRaum, idZeitraster -> Klausurraumstunde
	 */
	private readonly _mapRaumZeitrasterStunde : HashMap2D<number, number, GostKlausurraumstunde> = new HashMap2D();

	/**
	 * Ein Comparator für die GostKlausurräume.
	 */
	private static readonly _compRaumId : Comparator<GostKlausurraum> = { compare : (a: GostKlausurraum, b: GostKlausurraum) => {
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Die Schuelerklausuren, die im Manager vorhanden sind
	 */
	private readonly _schuelerklausuren : List<GostSchuelerklausur> = new ArrayList();

	/**
	 * Eine Map Kursklausur-Id -> Liste von GostSchuelerklausuren
	 */
	private readonly _mapKkidSk : JavaMap<number, List<GostSchuelerklausur>> = new HashMap();

	/**
	 * Eine Map Raumstunde-Id -> Liste von Schuelerklausurraumstunden
	 */
	private readonly _mapidRsSkrs : JavaMap<number, List<GostSchuelerklausurraumstunde>> = new HashMap();

	/**
	 * Eine Map Schuelerklausur-Id -> Liste von Raumstunden
	 */
	private readonly _mapidRsSkrsRevert : JavaMap<number, List<GostKlausurraumstunde>> = new HashMap();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raum              der Gost-Klausurraum
	 * @param stunden           die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 */
	public constructor(raum : GostKlausurraum, stunden : List<GostKlausurraumstunde>, schuelerklausuren : List<GostSchuelerklausur>);

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raeume            die Liste der GostKlausurräume eines
	 *                          Gost-Klausurtermins
	 * @param listRs           die Liste der GostKlausurraumstunden eines
	 *                          Gost-Klausurtermins
	 * @param listSkrs			die Liste der Schülerklausurraumstunden
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des
	 *                          Gost-Klausurtermins
	 */
	public constructor(raeume : List<GostKlausurraum>, listRs : List<GostKlausurraumstunde>, listSkrs : List<GostSchuelerklausurraumstunde>, schuelerklausuren : List<GostSchuelerklausur>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : GostKlausurraum | List<GostKlausurraum>, __param1 : List<GostKlausurraumstunde>, __param2 : List<GostSchuelerklausur> | List<GostSchuelerklausurraumstunde>, __param3? : List<GostSchuelerklausur>) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausuren.GostKlausurraum')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && ((__param2 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && (typeof __param3 === "undefined")) {
			const raum : GostKlausurraum = cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurraum(__param0);
			const stunden : List<GostKlausurraumstunde> = cast_java_util_List(__param1);
			const schuelerklausuren : List<GostSchuelerklausur> = cast_java_util_List(__param2);
			this.addKlausurraum(raum);
			for (const s of stunden)
				this.addKlausurraumstunde(s);
			for (const k of schuelerklausuren)
				this.addSchuelerklausur(k);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && ((__param2 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param3 === null))) {
			const raeume : List<GostKlausurraum> = cast_java_util_List(__param0);
			const listRs : List<GostKlausurraumstunde> = cast_java_util_List(__param1);
			const listSkrs : List<GostSchuelerklausurraumstunde> = cast_java_util_List(__param2);
			const schuelerklausuren : List<GostSchuelerklausur> = cast_java_util_List(__param3);
			for (const r of raeume)
				this.addKlausurraum(r);
			for (const s of listRs)
				this.addKlausurraumstunde(s);
			for (const s of listSkrs)
				this.addSchuelerklausurraumstunde(s);
			for (const k of schuelerklausuren)
				this.addSchuelerklausur(k);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum die ID des Klausurraums
	 *
	 * @return den Klausurraum
	 */
	public getKlausurraum(idRaum : number) : GostKlausurraum {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapIdRaum, idRaum);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @return die Liste der Klausurräume
	 */
	public getKlausurraeume() : List<GostKlausurraum> {
		return this._raeume;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum       die ID des Klausurraums
	 * @param idZeitraster die ID des Zeitrasters
	 *
	 * @return die Klausurraumstunde
	 */
	public getKlausurraumstundeByRaumZeitraster(idRaum : number, idZeitraster : number) : GostKlausurraumstunde | null {
		return this._mapRaumZeitrasterStunde.getOrNull(idRaum, idZeitraster);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param raum das Gost-Klausurraum-Objekt
	 */
	public addKlausurraum(raum : GostKlausurraum) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_raeume", this._raeume, raum);
		this._raeume.sort(GostKlausurraumManager._compRaumId);
		DeveloperNotificationException.ifMapPutOverwrites(this._mapIdRaum, raum.id, raum);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param stunde das Gost-Klausurraumstunde-Objekt
	 */
	public addKlausurraumstunde(stunde : GostKlausurraumstunde) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_stunden", this._stunden, stunde);
		DeveloperNotificationException.ifMapGetIsNull(this._mapIdRaum, stunde.idRaum);
		DeveloperNotificationException.ifMapPutOverwrites(this._mapIdRaumStunde, stunde.id, stunde);
		DeveloperNotificationException.ifListAddsDuplicate("_mapRaumStundenList", MapUtils.getOrCreateArrayList(this._mapRaumStunden, stunde.idRaum), stunde);
		DeveloperNotificationException.ifMap2DPutOverwrites(this._mapRaumZeitrasterStunde, stunde.idRaum, stunde.idZeitraster, stunde);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param stunde das Gost-Klausurraumstunde-Objekt
	 */
	public addSchuelerklausurraumstunde(stunde : GostSchuelerklausurraumstunde) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_mapidRsSkrsList", MapUtils.getOrCreateArrayList(this._mapidRsSkrs, stunde.idRaumstunde), stunde);
		DeveloperNotificationException.ifListAddsDuplicate("_mapidRsSkrsRevertList", MapUtils.getOrCreateArrayList(this._mapidRsSkrsRevert, stunde.idSchuelerklausur), DeveloperNotificationException.ifMapGetIsNull(this._mapIdRaumStunde, stunde.idRaumstunde));
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param klausur das Gost-Klausurraum-Objekt
	 */
	public addSchuelerklausur(klausur : GostSchuelerklausur) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_mapKkidSkList", MapUtils.getOrCreateArrayList(this._mapKkidSk, klausur.idKursklausur), klausur);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Klausurraum geändert
	 * hat.
	 *
	 * @param r das GostKlausurraum-Objekt
	 */
	public patchKlausurraum(r : GostKlausurraum) : void {
		DeveloperNotificationException.ifListRemoveFailes("_raeume", this._raeume, r);
		DeveloperNotificationException.ifMapRemoveFailes(this._mapIdRaum, r.id);
		this._raeume.add(r);
		this._mapIdRaum.put(r.id, r);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Klausurraum geändert
	 * hat.
	 *
	 * @param skids
	 * @param collectionSkrsKrs das GostKlausurraum-Objekt
	 */
	public setzeRaumZuSchuelerklausuren(skids : List<number>, collectionSkrsKrs : GostKlausurenCollectionSkrsKrs) : void {
		for (const skid of skids) {
			let listKrs : List<GostKlausurraumstunde | null> | null = this._mapidRsSkrsRevert.get(skid);
			if (listKrs === null)
				continue;
			for (const rsid of listKrs) {
				if (rsid === null)
					continue;
				const skrsList : List<GostSchuelerklausurraumstunde> = DeveloperNotificationException.ifMapGetIsNull(this._mapidRsSkrs, rsid.id);
				const toRemove : List<GostSchuelerklausurraumstunde> | null = new ArrayList();
				for (const skrs of skrsList)
					if (skrs.idSchuelerklausur === skid)
						toRemove.add(skrs);
				skrsList.removeAll(toRemove);
			}
		}
		const raumstunden : List<GostKlausurraumstunde> = collectionSkrsKrs.raumstunden;
		const skRaumstunden : List<GostSchuelerklausurraumstunde> = collectionSkrsKrs.skRaumstunden;
		for (const rs of raumstunden)
			this.addKlausurraumstunde(rs);
		for (const skrs of skRaumstunden)
			this.addSchuelerklausurraumstunde(skrs);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param manager das GostKlausurraumManager-Objekt
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public getKursklausuren(manager : GostKursklausurManager) : List<GostKursklausur> {
		let kursklausuren : List<GostKursklausur> | null = new ArrayList();
		for (const kkId of this._mapKkidSk.keySet()) {
			kursklausuren.add(manager.getKursklausurById(kkId));
		}
		return kursklausuren;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idKlausur die Id der Kursklausur
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public getSchuelerklausurenByKursklausur(idKlausur : number) : List<GostSchuelerklausur> {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapKkidSk, idKlausur);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.GostKlausurraumManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_GostKlausurraumManager(obj : unknown) : GostKlausurraumManager {
	return obj as GostKlausurraumManager;
}
